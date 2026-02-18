package RealEstateApp.Controllers;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;
import java.util.HashMap;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.stripe.Stripe;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import com.stripe.param.ChargeCreateParams;
import com.stripe.param.PaymentIntentCreateParams;
import com.stripe.model.PaymentIntent;
import com.google.gson.Gson;

import RealEstateApp.Pojo.PaymentMethod;
import RealEstateApp.Pojo.PaymentSource;
import RealEstateApp.Pojo.PaymentStatus;
import RealEstateApp.Pojo.RentPayment;
import RealEstateApp.Pojo.TenantProfile;
import RealEstateApp.Pojo.User;
import RealEstateApp.dao.RentPaymentDao;
import RealEstateApp.dao.TenantProfileDao;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpSession;
import com.stripe.param.checkout.SessionCreateParams;
import com.stripe.param.checkout.SessionRetrieveParams;



@Controller
public class StripePaymentController {


	    @Value("${stripe.secret.key}")
	    private String stripeSecretKey;
	    
	    @Autowired
	    private TenantProfileDao tenantProfileDao; // Inject your DAO to access tenant profiles
	    
	    @Autowired
	    private RentPaymentDao rentPaymentDao; // Inject DAO to save payment records

	    @PostConstruct
	    public void init() {
	        Stripe.apiKey = stripeSecretKey;
	    }
	
	    // Endpoint to create a Stripe Checkout session
	    
	
	        @RequestMapping("/create-checkout-session")
	        public ResponseEntity<Map<String, String>> createCheckoutSession(@RequestParam("amount") BigDecimal paymentAmount) {
	            String YOUR_DOMAIN = "http://localhost:8080";
	            
	            Map<String, String> map = new HashMap<>();
	            try {
	                SessionCreateParams params =
                      SessionCreateParams.builder()
                        .setUiMode(SessionCreateParams.UiMode.CUSTOM)
                        .setMode(SessionCreateParams.Mode.PAYMENT)
                        .setReturnUrl(YOUR_DOMAIN + "/complete.html?session_id={CHECKOUT_SESSION_ID}")
                        .setAutomaticTax(
                          SessionCreateParams.AutomaticTax.builder()
                            .setEnabled(true)
                            .build())
                        .addLineItem(
                          SessionCreateParams.LineItem.builder()
                            .setQuantity(1L)
                            // Provide the exact Price ID (for example, price_1234) of the product you want to sell
                            .setPrice(paymentAmount.multiply(BigDecimal.valueOf(100)).longValue() + "")
                            .build())
                        .build();
	    
	                Session session = Session.create(params);
	                map.put("clientSecret", session.getRawJsonObject().getAsJsonPrimitive("client_secret").getAsString());
	                return ResponseEntity.ok(map);
	            } catch (StripeException e) {
	                map.put("error", e.getMessage());
	                return ResponseEntity.status(500).body(map);
	            }
	        }
	        
			// Show the checkout page where the Stripe.js code will run and call the above endpoint to create a session
		@RequestMapping("/stripe/checkout")
        public String showCheckoutForm(@RequestParam("amount") BigDecimal amount,
    		                          @RequestParam("currency") String currency, Model model) {
  
			
			
		 model.addAttribute("currency", currency);
        model.addAttribute("amount",amount); // example amount in cents
        return "tenant/stripe-checkout"; // renders checkout.html from templates
}
	       
         // After the user completes the checkout, Stripe will redirect to the return URL with a session_id parameter. We can use that to retrieve the session and confirm payment.
			
		@PostMapping(value= "/create-payment-intent", consumes = "application/x-www-form-urlencoded")
			public ResponseEntity<Map<String, String>> createPaymentIntent(@RequestParam Map<String, Object> data)
					                                                          throws StripeException {
			 
			        String amountStr = (String) data.get("amount");
			        Long amount = Long.parseLong(amountStr);
			        
				    String currency = (String) data.get("currency");
 
				    PaymentIntentCreateParams params =
				    		  PaymentIntentCreateParams.builder()
				    		  .setAmount(amount)
						        .setCurrency(currency)
				    		    .setAutomaticPaymentMethods(
				    		      PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
				    		        .setEnabled(true)
				    		        .build()
				    		    )
				    		    .build();
 
				    PaymentIntent intent = PaymentIntent.create(params);
                 
				    Map<String, String> responseData = new HashMap<>();
				    responseData.put("Client secret", intent.getClientSecret());
				    return ResponseEntity.ok(responseData);
				}
			
			
			

				@RequestMapping("/retrieve-payment-intent")
				public ResponseEntity<Map<String, Object>> retrievePaymentIntent(
						@RequestParam("paymentIntentId") String paymentIntentId) {
					try {
						PaymentIntent intent = PaymentIntent.retrieve(paymentIntentId);
						Map<String, Object> responseData = new HashMap<>();
						responseData.put("id", intent.getId());
						responseData.put("amount", intent.getAmount());
						responseData.put("currency", intent.getCurrency());
						responseData.put("status", intent.getStatus());
						return ResponseEntity.ok(responseData);
					} catch (StripeException e) {
						Map<String, Object> errorData = new HashMap<>();
						errorData.put("error", e.getMessage());
						return ResponseEntity.status(500).body(errorData);
					}
			}
	        
	        
	    
	        
	    
	    // Show the Stripe checkout page with the amount to be paid
	
	
	
	@PostMapping("/tenant/stripe-payment")
	public String processStripePayment(@RequestParam("stripeToken") String token,
	                                   @RequestParam("amount") BigDecimal amount,
	                                   HttpSession session,
	                                   RedirectAttributes ra) {
		
	    User tenant = (User) session.getAttribute("user");
	    if (tenant == null) return "redirect:/login";

		
	    try {
	        ChargeCreateParams params = ChargeCreateParams.builder()
	            .setAmount(amount.multiply(BigDecimal.valueOf(100)).longValue()) // amount in cents
	            .setCurrency("usd")
	            .setSource(token)
	            .setDescription("Tenant rent payment")
	            .build();

	        Charge charge = Charge.create(params);
	        
	        TenantProfile tp = tenantProfileDao.findByUserId(tenant.getId());

	        // Save payment info to DB as needed
	        
	        RentPayment payment = new RentPayment();
	        payment.setAmountPaid(amount);
	        payment.setMonthBalanceDue(tp.getProperty().getRentAmount().subtract(amount));
	        payment.setTransactionId(charge.getId());
	        payment.setUser(tenant);
	        payment.setCompany(tenant.getCompany());
	        payment.setProperty(tp.getProperty());
	        payment.setPaymentDate(LocalDate.now());
	        payment.setStatus(PaymentStatus.PAID);
	        payment.setSource(PaymentSource.STRIPE);
	        payment.setMethod(PaymentMethod.CARD);
	        payment.setCurrency("usd");
	        
	        rentPaymentDao.save(payment);

	        ra.addFlashAttribute("success", "Payment successful!");
	        return "redirect:/tenant/dashboard";
	        
	    } catch (StripeException e) {
	        ra.addFlashAttribute("error", "Stripe payment failed: " + e.getMessage());
	        return "redirect:/tenant/paymentform";
	    }
}
	
	

}
