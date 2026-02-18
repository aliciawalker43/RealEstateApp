package RealEstateApp.Controllers;

import com.stripe.Stripe;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.Invoice;
import com.stripe.model.InvoiceLineItem;
import com.stripe.param.InvoiceRetrieveParams;

import com.stripe.model.checkout.Session;
import com.stripe.model.Invoice;
import com.stripe.model.Subscription;
import com.stripe.net.Webhook;
import com.stripe.model.StripeObject;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import RealEstateApp.Pojo.Company;
import RealEstateApp.dao.CompanyDao;

import java.util.Map;


	
	@RestController
	@RequestMapping("/stripe/webhook")
	public class StripeWebhookController {

	  private final CompanyDao companyDao;

	  @Value("${stripe.secret.key}")
	  private String stripeSecretKey;

	  @Value("${stripe.webhook.secret}")
	  private String endpointSecret;

	  public StripeWebhookController(CompanyDao companyDao) {
	    this.companyDao = companyDao;
	  }

	  @PostMapping
	  public ResponseEntity<String> handleWebhook(
	      @RequestBody String payload,
	      @RequestHeader("Stripe-Signature") String sigHeader
	  ) {
	    Stripe.apiKey = stripeSecretKey;

	    Event event;
	    try {
	      event = Webhook.constructEvent(payload, sigHeader, endpointSecret);
	    } catch (SignatureVerificationException e) {
	      return ResponseEntity.status(400).body("Invalid signature");
	    } catch (Exception e) {
	      return ResponseEntity.status(400).body("Webhook error: " + e.getMessage());
	    }

	    StripeObject stripeObject = event.getDataObjectDeserializer()
	        .getObject()
	        .orElse(null);

	    if (stripeObject == null) {
	      System.out.println("⚠️ Could not deserialize event: " + event.getType());
	      return ResponseEntity.ok("ok");
	    }

	    try {
	      switch (event.getType()) {

	        // First time checkout finishes and subscription is created
	        case "checkout.session.completed" -> {
	          Session session = (Session) stripeObject;

	          if (!"subscription".equals(session.getMode())) return ResponseEntity.ok("ignored");

	          String companyIdStr = session.getMetadata().get("companyId");
	          if (companyIdStr == null) return ResponseEntity.ok("ok");

	          Long companyId = Long.valueOf(companyIdStr);
	          Company company = companyDao.findById(companyId).orElseThrow();

	          // Save Stripe IDs (source of truth)
	          if (session.getCustomer() != null) company.setStripeCustomerId(session.getCustomer());
	          if (session.getSubscription() != null) company.setStripeSubscriptionId(session.getSubscription());

	          company.setSubscriptionStatus("Active");
	          companyDao.save(company);

	          System.out.println("✅ Activated company " + companyId + " (checkout.session.completed)");
	        }

	        // Subscription renewal invoice paid
	        case "invoice.payment_succeeded" -> {
	          Invoice invoice = (Invoice) stripeObject;

	          String customerId = invoice.getCustomer();
	          if (customerId == null) return ResponseEntity.ok("ok");

	          Company company = companyDao.findByStripeCustomerId(customerId);
	          if (company == null) return ResponseEntity.ok("ok");

	          // Optional: if you store subscriptionId, you can validate it here
	          // (only if your Invoice model exposes subscription reliably; otherwise skip)
	          company.setSubscriptionStatus("Active");
	          companyDao.save(company);

	          System.out.println("✅ Active (invoice.payment_succeeded) company " + company.getId());
	        }

	        // Subscription renewal failed -> restrict access / block tenant payments
	        case "invoice.payment_failed" -> {
	          Invoice invoice = (Invoice) stripeObject;

	          String customerId = invoice.getCustomer();
	          if (customerId == null) return ResponseEntity.ok("ok");

	          Company company = companyDao.findByStripeCustomerId(customerId);
	          if (company == null) return ResponseEntity.ok("ok");

	          company.setSubscriptionStatus("PastDue");
	          companyDao.save(company);

	          System.out.println("⚠️ PastDue (invoice.payment_failed) company " + company.getId());
	        }

	        // Subscription canceled
	        case "customer.subscription.deleted" -> {
	          Subscription sub = (Subscription) stripeObject;

	          Company company = companyDao.findByStripeSubscriptionId(sub.getId());
	          if (company == null) return ResponseEntity.ok("ok");

	          company.setSubscriptionStatus("Canceled");
	          companyDao.save(company);

	          System.out.println("🛑 Canceled (subscription.deleted) company " + company.getId());
	        }

	        default -> {
	          // ignore
	        }
	      }
	    } catch (Exception e) {
	      System.out.println("❌ Webhook processing error: " + e.getMessage());
	      return ResponseEntity.status(500).body("Webhook handler failed");
	    }

	    return ResponseEntity.ok("ok");
	  }

	  
	  
	  

	
	  
	  

	  
	  
	}


