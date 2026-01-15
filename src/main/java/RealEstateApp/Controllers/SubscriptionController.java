package RealEstateApp.Controllers;

import java.util.Base64;
import com.stripe.Stripe;
import com.stripe.model.checkout.Session;
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
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Value;
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
              // Optional: collect billing address
              .setBillingAddressCollection(SessionCreateParams.BillingAddressCollection.REQUIRED)
              .setCustomerEmail(draft.getOwnerEmail())
              // Helpful metadata (shows up in Stripe + webhook)
              .putMetadata("companyId", String.valueOf(company.getId()))
              .putMetadata("ownerUserId", String.valueOf(owner.getId()))
              .putMetadata("plan", draft.getPlan().name())
              .build();
      Session checkoutSession = Session.create(params);

      return "redirect:" + checkoutSession.getUrl();

    } catch (Exception e) {
      return "redirect:/subscribe?error=" + url(e.getMessage());
    }
  }

  @GetMapping("/success")
  public String success(@RequestParam("session_id") String sessionId, HttpSession session) {
    // For MVP: mark company ACTIVE here.
    // Best practice: do this in Stripe webhook (see section 5) so it's secure.

    Long companyId = (Long) session.getAttribute("PENDING_COMPANY_ID");
    Long userId = (Long) session.getAttribute("PENDING_USER_ID");
    if (companyId == null || userId == null) return "redirect:/login";

    Company company = companyDao.findById(companyId).orElseThrow();
    company.setSubscriptionStatus("Active");
    companyDao.save(company);

    // log them in
    User user = userDao.findById(userId).orElseThrow();
    session.setAttribute("user", user);

    // cleanup
    session.removeAttribute("PENDING_COMPANY_ID");
    session.removeAttribute("PENDING_USER_ID");

    return "redirect:/company/dashboard";
  }
  
  
  
  
  
  

  private String mapPlanToStripePriceId(SubscriptionPlan plan) {
    // Replace with your real Stripe Price IDs (created in Stripe dashboard)
    return switch (plan) {
      case STARTER -> "price_STARTER";
      case GROWTH  -> "price_GROWTH";
      case PRO     -> "price_PRO";
    };
  }

  private String url(String s) {
    try { return java.net.URLEncoder.encode(String.valueOf(s), java.nio.charset.StandardCharsets.UTF_8); }
    catch (Exception e) { return "Error"; }
  }
}
