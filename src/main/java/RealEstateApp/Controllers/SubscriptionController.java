package RealEstateApp.Controllers;

import java.util.Arrays;
import java.util.Base64;
import com.stripe.Stripe;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import com.stripe.param.checkout.SessionCreateParams;

import RealEstateApp.Pojo.Company;
import RealEstateApp.Pojo.Role;
import RealEstateApp.Pojo.SubscriptionPlan;
import RealEstateApp.Pojo.SubscriptionRegistrationForm;
import RealEstateApp.Pojo.User;
import RealEstateApp.dao.CompanyDao;
import RealEstateApp.dao.UserDao;
import jakarta.servlet.http.HttpSession;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDate;import com.stripe.model.Account;

import com.stripe.model.Customer;
import com.stripe.param.AccountCreateParams;
import com.stripe.param.CustomerCreateParams;
import com.stripe.model.AccountSession;
import com.stripe.param.AccountSessionCreateParams;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/subscribe")
public class SubscriptionController {

  private final CompanyDao companyDao;
  private final UserDao userDao;


  @Value("${stripe.secret.key}")
  private String stripeSecretKey;

  @Value("${app.base-url}")
  private String baseUrl;

  public SubscriptionController(CompanyDao companyDao, UserDao userDao) {
    this.companyDao = companyDao;
    this.userDao = userDao;
  }

  @GetMapping
  public String subscribePage(Model model,
                              @RequestParam(value="error", required=false) String error) {
    
	  System.out.println("values= " + Arrays.toString(SubscriptionPlan.values() ));
	  
	  model.addAttribute("error", error);
    model.addAttribute("form", new SubscriptionRegistrationForm());
    model.addAttribute("plans", SubscriptionPlan.values());
    return "subscription/step1";
  }
  
  
  @PostMapping("/step1")
  public String step1Post(@ModelAttribute("draft") SubscriptionRegistrationForm draft, HttpSession session) {
	  if (draft.getFirstName() == null) return "redirect:/subscribe?error=FirstName%20required";
	  if (draft.getLastName() == null) return "redirect:/subscribe?error=LastName%20required";
      if (draft.getCompanyName() == null || draft.getCompanyName().isBlank()) return "redirect:/subscribe?error=Company%20name%20required";
      if (draft.getOwnerEmail() == null || draft.getOwnerEmail().isBlank()) return "redirect:/subscribe?error=Email%20required";
      if (draft.getPassword() == null || draft.getPassword().isBlank()) return "redirect:/subscribe?error=Password%20required";
      if (draft.getPlan() == null) return "redirect:/subscribe?error=Plan%20required";

      session.setAttribute("SUB_DRAFT", draft);
      return "redirect:/subscribe/billing";
  }
  
  // STEP 2: billing details (no card here; Stripe collects card)
  @GetMapping("/billing")
  public String step2(Model model, HttpSession session,
                      @RequestParam(value="error", required=false) String error) {
	  SubscriptionRegistrationForm draft = (SubscriptionRegistrationForm) session.getAttribute("SUB_DRAFT");
      if (draft == null) return "redirect:/subscribe";

      model.addAttribute("error", error);
      model.addAttribute("draft", draft);
      return "subscription/step2-billing";
  }
  
  

  @PostMapping("/start")
  public String startSubscription(@ModelAttribute("draft") SubscriptionRegistrationForm step2Draft,
                                  HttpSession session) {
	  try {
		  SubscriptionRegistrationForm draft = (SubscriptionRegistrationForm) session.getAttribute("SUB_DRAFT");
          if (draft == null) return "redirect:/subscribe";
	  
	  // Merge step2 fields into session draft
      draft.setOwnerPhone(step2Draft.getOwnerPhone());
      draft.setBillingAddress1(step2Draft.getBillingAddress1());
      draft.setBillingCity(step2Draft.getBillingCity());
      draft.setBillingState(step2Draft.getBillingState());
      draft.setBillingZip(step2Draft.getBillingZip());
      draft.setBillingCountry(step2Draft.getBillingCountry());
      
      if (draft.getOwnerPhone() == null || draft.getOwnerPhone().isBlank())
          return "redirect:/subscribe/billing?error=Phone%20required";


      ///check if company exist
      
      // 1) Create Company (PENDING)
      Company company = new Company();
      company.setName(draft.getCompanyName());
      company.setPlan(draft.getPlan());
      company.setSubscriptionStatus("PENDING");
      company.setSubscriptionStartDate(LocalDate.now());
      company.setBillingDate(LocalDate.now());
      company.setMaxPropertyLimit(draft.getPlan().getMaxProperties());
      company.setMonthlyUsd(draft.getPlan().getMonthlyUsd());
      company = companyDao.save(company);
      
      String encoded = Base64.getEncoder()
    	        .encodeToString(draft.getPassword().getBytes(StandardCharsets.UTF_8));
     
      
      //check email duplicates

      // 2) Create Owner User (LANDLORD)
      User owner = new User();
      owner.setFirstname(draft.getFirstName());
      owner.setLastname(draft.getLastName());
      owner.setPhoneNumber(draft.getOwnerPhone());
      owner.setCompany(company);
      owner.setRole(Role.LANDLORD);
      owner.setEmail(draft.getOwnerEmail());
      owner.setUsername(draft.getOwnerEmail()); // or separate username
      owner.setPasscode(encoded); // TODO: hash this!
      owner.setBillingAddress(draft.getBillingAddress1());
      owner = userDao.save(owner);

      // store temporarily so after payment you can log them in or finish setup
      session.setAttribute("PENDING_COMPANY_ID", company.getId());
      session.setAttribute("PENDING_USER_ID", owner.getId());

      // 3) Create Stripe Checkout Session for subscription
      
      Stripe.apiKey = stripeSecretKey;

      // 3a) Create Stripe Customer for SaaS subscription billing (PLATFORM account)
      CustomerCreateParams custParams = CustomerCreateParams.builder()
          .setEmail(draft.getOwnerEmail())
          .setName(draft.getCompanyName())
          .putMetadata("companyId", String.valueOf(company.getId()))
          .build();

      Customer customer = Customer.create(custParams);

      // 3b) Create Stripe Connected Account (Express) for this business (tenant payments later)
      AccountCreateParams acctParams = AccountCreateParams.builder()
          .setType(AccountCreateParams.Type.EXPRESS)
          .setEmail(draft.getOwnerEmail())
          .putMetadata("companyId", String.valueOf(company.getId()))
          .build();

      Account connected = Account.create(acctParams);

      // 3c) Persist Stripe IDs on company
      company.setStripeCustomerId(customer.getId());
      company.setStripeConnectedAccountId(connected.getId());
      companyDao.save(company);


      // IMPORTANT: priceId must be created in Stripe dashboard (recurring price)
      String priceId = mapPlanToStripePriceId(draft.getPlan());

      SessionCreateParams params =
          SessionCreateParams.builder()
              .setMode(SessionCreateParams.Mode.SUBSCRIPTION)
              .setSuccessUrl(baseUrl + "/subscribe/success?session_id={CHECKOUT_SESSION_ID}")
              .setCancelUrl(baseUrl + "/subscribe?error=Checkout%20cancelled")
              .addLineItem(
                  SessionCreateParams.LineItem.builder()
                      .setPrice(priceId)
                      .setQuantity(1L)
                      .build()
              )
              .setBillingAddressCollection(SessionCreateParams.BillingAddressCollection.REQUIRED)

              // IMPORTANT: use the created customer so subscription is attached cleanly
              .setCustomer(customer.getId())

              // Helpful metadata
              .putMetadata("companyId", String.valueOf(company.getId()))
              .putMetadata("ownerUserId", String.valueOf(owner.getId()))
              .putMetadata("plan", draft.getPlan().name())
              .putMetadata("connectedAccountId", connected.getId())
              .build();
      
      Session checkoutSession = Session.create(params);

      return "redirect:" + checkoutSession.getUrl();

    } catch (Exception e) {
      return "redirect:/subscribe?error=" + url(e.getMessage());
    }
  }

  @GetMapping("/success")
  public String success(@RequestParam("session_id") String sessionId, Model model) throws StripeException {
	  Session session = Session.retrieve(sessionId);

	  
	  
	  model.addAttribute("success", " 'Subscription is' + session.getStatus()+ 'Your payment is ' + session.getPaymentStatus() ");
	  model.addAttribute("paymentStatus", session.getPaymentStatus()); // "paid", "unpaid"
	  model.addAttribute("status", session.getStatus());  
	  
    return "complete";
  }
  

  @PostMapping("/connect/account-session")
  @ResponseBody
  public ResponseEntity<?> createConnectAccountSession(@RequestParam Long companyId) {
    try {
      Company company = companyDao.findById(companyId).orElseThrow();

      if (company.getStripeConnectedAccountId() == null) {
        return ResponseEntity.badRequest().body("Connected account not set for company.");
      }

      Stripe.apiKey = stripeSecretKey;

      AccountSessionCreateParams params = AccountSessionCreateParams.builder()
          .setAccount(company.getStripeConnectedAccountId())
          .setComponents(
              AccountSessionCreateParams.Components.builder()
                  .setAccountOnboarding(
                      AccountSessionCreateParams.Components.AccountOnboarding.builder()
                          .setEnabled(true)
                          .build()
                  )
                  .build()
          )
          .setComponents(
              AccountSessionCreateParams.Components.builder()
                  .setNotificationBanner(
                      AccountSessionCreateParams.Components.NotificationBanner.builder()
                          .setEnabled(true)
                          .build()
                  )
                  .build()
          )
          .build();

      AccountSession session = AccountSession.create(params);

      return ResponseEntity.ok(java.util.Map.of(
          "clientSecret", session.getClientSecret(),
          "connectedAccountId", company.getStripeConnectedAccountId()
      ));

    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  
  
  ///Add endpoint to upgrade/cancel of change subscrption
  
  

  private String mapPlanToStripePriceId(SubscriptionPlan plan) {
    // Replace with your real Stripe Price IDs (created in Stripe dashboard)
    return switch (plan) {
      case STARTER -> "price_1SzM5uGRGtNQxoblZIexO2QE";
      case GROWTH  -> "price_1SzM8jGRGtNQxoblA3xrS6DJ";
      case PRO     -> "price_1SzM9hGRGtNQxoblw9DUjw2Q";
    };
  }

  private String url(String s) {
    try { return java.net.URLEncoder.encode(String.valueOf(s), java.nio.charset.StandardCharsets.UTF_8); }
    catch (Exception e) { return "Error"; }
  }
}
