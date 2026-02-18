package RealEstateApp.Controllers;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import RealEstateApp.Pojo.Company;
import RealEstateApp.Pojo.Conversation;
import RealEstateApp.Pojo.ImageAsset;
import RealEstateApp.Pojo.ImageCategory;
import RealEstateApp.Pojo.MaintenanceRequest;

import RealEstateApp.Pojo.MaintenanceStatus;
import RealEstateApp.Pojo.Message;
import RealEstateApp.Pojo.RentPayment;

import RealEstateApp.Pojo.Property;
import RealEstateApp.Pojo.TenantProfile;
import RealEstateApp.Pojo.User;
import RealEstateApp.Service.ImageUploadService;
import RealEstateApp.Service.MessagingService;
import RealEstateApp.dao.ConversationDao;
import RealEstateApp.dao.DocumentDao;
import RealEstateApp.dao.ImageAssetDao;
import RealEstateApp.dao.MaintenanceRequestDao;

import RealEstateApp.dao.MessageDao;

import RealEstateApp.dao.RentPaymentDao;
import RealEstateApp.dao.TenantProfileDao;
import RealEstateApp.dao.UserDao;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;

@Controller
@RequestMapping("/tenant")
public class TenantDashboardController {

	private final UserDao userDao;
	private final TenantProfileDao tenantProfileDao;
	private final ImageAssetDao imageAssetDao;
    private final RentPaymentDao paymentDao;
    private final DocumentDao documentDao;
    private final MaintenanceRequestDao maintenanceRequestDao;
    private final MessageDao messageDao;
    private final ImageUploadService imageUploadService;
    private final MessagingService messagingService;
    private final ConversationDao conversationDao;
  
    
    //private final PaymentHistory paymentHistoryDao;

    public TenantDashboardController(RentPaymentDao paymentDao, DocumentDao documentDao,
                                     MaintenanceRequestDao maintenanceRequestDao,
                                     MessageDao messageDao, UserDao userDao, 
                                     ImageUploadService imageUploadService,
                                     ImageAssetDao imageAssetDao,
                                     MessagingService messagingService,
                                     ConversationDao conversationDao,
                                     TenantProfileDao tenantProfileDao) {
        
    	this.tenantProfileDao =tenantProfileDao;
    	this.userDao = userDao;
    	this.paymentDao = paymentDao;
        this.documentDao = documentDao;
        this.maintenanceRequestDao = maintenanceRequestDao;
        this.messageDao = messageDao;
        this.imageUploadService = imageUploadService;
        this.imageAssetDao = imageAssetDao;
        this.messagingService = messagingService;
        this.conversationDao = conversationDao;
       
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, HttpSession session) {
    	
    	User currentUser= (User)session.getAttribute("user");
		Long userId =  currentUser.getId();
		 User user = userDao.findById(userId)
	                .orElseThrow(() -> new IllegalStateException("User not found"));
		 
		    // ✅ Add tenantProfile (even if not found)
		    TenantProfile tenantProfile = tenantProfileDao.findById(user.getId()).orElse(null);
		     Month month = LocalDate.now().getMonth();// display current month for rent
		    
		     //get monthly balance due add late fee if after due date
		     int dueOn= tenantProfile.getProperty().getRentDueDay();
		     int  today = LocalDate.now().getDayOfMonth();;
		     BigDecimal amountDue;
				if (today > dueOn) {
					amountDue= tenantProfile.getProperty().getRentAmount().add(tenantProfile.getProperty().getLateFee());
				} else {
					amountDue = tenantProfile.getProperty().getRentAmount();
				}
				
				
		model.addAttribute("unread", messageDao.countByReadByTenantFalse());		
		model.addAttribute("amountDue", amountDue);
		model.addAttribute("month", month); 
		model.addAttribute("tenantProfile", tenantProfile);
    	model.addAttribute("user", user);
        model.addAttribute("payments", paymentDao.findByUser(user));
        model.addAttribute("documents", documentDao.findByUser(user));
        model.addAttribute("requests", maintenanceRequestDao.findByUser(user));
        model.addAttribute("messageCount", messageDao.count());
        return "tenant/dashboard";
    }
    
    @RequestMapping("/profile")
	String showProfile(Model model, HttpSession session) {
		User tenant = (User) session.getAttribute("user");
		if (tenant == null)
			return "redirect:/login";

		TenantProfile tenantProfile = tenantProfileDao.findById(tenant.getId()).orElse(null);
		model.addAttribute("tenantProfile", tenantProfile);
		model.addAttribute("user", tenant);
    return "tenant/profile";
    }
    
    @PostMapping("/profile/update")
    String updateProfile(@RequestParam("firstname") String firstName,
    		                    @RequestParam("lastname") String lastName,
    		                    @RequestParam("email") String email,
    		                    @RequestParam("billingAddress") String billingAddress,
                                @RequestParam("password") String password,
                                @RequestParam("passwordconfirm") String passwordconfirm,
                                @RequestParam("username") String username,
                                @RequestParam("phone") String phoneNumber,
                                HttpSession session, RedirectAttributes ra) {
        User tenant = (User) session.getAttribute("user");
        if (tenant == null) return "redirect:/login";

        TenantProfile tenantProfile = tenantProfileDao.findById(tenant.getId())
                .orElseThrow(() -> new IllegalStateException("Tenant profile not found"));
        
tenant.setFirstname(firstName);
tenant.setLastname(lastName);
tenant.setEmail(email);
tenant.setUsername(username);
tenant.setPhoneNumber(phoneNumber);
tenant.setBillingAddress(billingAddress);

if (password != null && !password.isEmpty()) {
	if (password.equals(passwordconfirm)) {
		String encodedPasscode = Base64.getEncoder().encodeToString(password.getBytes());
		tenant.setPasscode(encodedPasscode);
	} else {
		ra.addFlashAttribute("error", "Passwords do not match.");
		return "redirect:/landlord/profile";
	}
}
       userDao.save(tenant);

        return "redirect:/tenant/profile";
    }
    
    
    @PostMapping("/profile/image")
    public String uploadProfileImage(@RequestParam("file") MultipartFile file,
                                     HttpSession session) {
        try {
            imageUploadService.uploadProfileImage(file, ImageCategory.PROFILE, null, session);
            return "redirect:/tenant/dashboard";
        } catch (Exception e) {
            return "redirect:/tenant/dashboard?error=" +
                    URLEncoder.encode(e.getMessage(), StandardCharsets.UTF_8);
        }
    }
    
    
 // Show payment form with monthly breakdown
    @GetMapping("/makepayment")
    public String showPaymentForm(HttpSession session, Model model) {
    	    User tenant = (User) session.getAttribute("user");
    	    if (tenant == null) return "redirect:/login";

    	    TenantProfile tenantProfile = tenantProfileDao.findById(tenant.getId()).orElse(null);
    	    if (tenantProfile == null || tenantProfile.getProperty() == null) {
    	        model.addAttribute("error", "No property assigned.");
    	        return "tenant/paymentform";
    	    }

    	    Property property = tenantProfile.getProperty();
    	    Company company = property.getCompany();
    	    int rentDueDay = property.getRentDueDay();
    	    LocalDate leaseStart = property.getLeaseStartDate();
    	    BigDecimal rentAmount = property.getRentAmount();
    	    BigDecimal lateFee = property.getLateFee();

    	    LocalDate today = LocalDate.now();
    	    int monthsSinceLeaseStart = (today.getYear() - leaseStart.getYear()) * 12 + today.getMonthValue() - leaseStart.getMonthValue() + 1;
    	    BigDecimal totalRentDue = rentAmount.multiply(BigDecimal.valueOf(monthsSinceLeaseStart));

    	    // Get all payments for this tenant
    	    List<RentPayment> payments = paymentDao.findByUser(tenant);

    	    // Calculate total paid and count late payments
    	    BigDecimal totalPaid = BigDecimal.ZERO;
    	    int latePayments = 0;
    	    for (RentPayment p : payments) {
    	        if (p.getAmountPaid() != null) {
    	            totalPaid = totalPaid.add(p.getAmountPaid());
    	        }
    	        // Late if paid after due date
    	        if (p.getPaymentDate() != null && rentDueDay > 0 && p.getPaymentDate().getDayOfMonth() > rentDueDay) {
    	            latePayments++;
    	        }
    	    }

    	    BigDecimal totalLateFees = lateFee.multiply(BigDecimal.valueOf(latePayments));
    	    BigDecimal amountDue = totalRentDue.subtract(totalPaid).add(totalLateFees);
    	    BigDecimal pastDue= amountDue.subtract(rentAmount);

    	model.addAttribute("pastDue", pastDue);
        model.addAttribute("tenant", tenantProfile);
        model.addAttribute("payments", payments);
        model.addAttribute("amountDue", amountDue);
        model.addAttribute("lateFee", totalLateFees);
        return "tenant/paymentform";
    }


 // Process payment and allocate to oldest balances
    @PostMapping("/makepayment")
    public String processPayment(
    		// @RequestParam("paymentAmountPaitial") BigDecimal paymentAmountP,
       // @RequestParam("paymentAmountFull") BigDecimal paymentAmountF,
    		 @RequestParam("paymentAmount") BigDecimal paymentAmount,
        @RequestParam("currency")String currency,
        @RequestParam("method")String method,
        @RequestParam("note")String description,
        @RequestParam("intent")String intent,
        HttpSession session,
        RedirectAttributes ra) {

    	  User tenant = (User) session.getAttribute("user");
    	    if (tenant == null) return "redirect:/login";
    	    
    	    if (!"Active".equals(tenant.getCompany().getSubscriptionStatus())) {
    	    	  throw new ResponseStatusException(HttpStatus.PAYMENT_REQUIRED, "Service Unavailable - "
    	    	   + "Please contact your property manager to make payment.");
    	    	 }
    	    
    	   /* BigDecimal paymentAmount = BigDecimal.ZERO ;
    	    	if (paymentAmountP != null && paymentAmountP.compareTo(BigDecimal.ZERO) > 0) {
    	    		paymentAmount = paymentAmountP;
    	    	} else if (paymentAmountF != null && paymentAmountF.compareTo(BigDecimal.ZERO) > 0) {
    	    		paymentAmount = paymentAmountF;
    	    	} else {
    	    		ra.addFlashAttribute("error", "Invalid payment amount.");
    	    		
    	    		return "redirect:/tenant/makepayment";
    	    	}*/
    	    	System.out.println("Payment Amount: " + paymentAmount);
    	    // Save payment info to session or DB as needed

    	    if ("paypal".equalsIgnoreCase(method)) {
    	        String url = "/tenant/paypal-checkout?amount=" + paymentAmount
    	            + "&description=" + URLEncoder.encode(description, StandardCharsets.UTF_8)
    	            + "&intent=" + URLEncoder.encode(intent, StandardCharsets.UTF_8)
    	            + "&currency=" + URLEncoder.encode(currency, StandardCharsets.UTF_8)
    	            + "&method=" + URLEncoder.encode(method, StandardCharsets.UTF_8);
    	        return "redirect:" + url;
    	    } else if ("stripe".equalsIgnoreCase(method)) {
    	        // Redirect to Stripe checkout (implement this endpoint)
    	        //return "redirect:/create-checkout-session?amount=" + paymentAmount;
    	        return "redirect:/stripe/checkout?amount=" + paymentAmount
    	        		+ "&currency=" + URLEncoder.encode(currency, StandardCharsets.UTF_8);
    	    } else {
    	        // Handle other methods or show error
    	        ra.addFlashAttribute("error", "Unsupported payment method.");
    	        return "redirect:/tenant/makepayment";
    	    }
    	}

	
    
    @PostMapping("/maintenance/{requestId}/image")
    public String uploadMaintenanceImage(@PathVariable Long requestId,
                                         @RequestParam("file") MultipartFile file,
                                         HttpSession session) {
        try {
            // Optional: verify tenant owns this maintenance request
            User currentUser = (User) session.getAttribute("user");
            if (currentUser == null) return "redirect:/login";

            MaintenanceRequest req = maintenanceRequestDao.findById(requestId)
                    .orElseThrow(() -> new IllegalArgumentException("Maintenance request not found"));

            // If your MaintenanceRequest has a user field, enforce ownership:
            if (!req.getUser().getId().equals(currentUser.getId())) {
                throw new IllegalStateException("Unauthorized maintenance request");
            }

            imageUploadService.upload(file, ImageCategory.MAINTENANCE, requestId, session);
            return "redirect:/tenant/dashboard";
        } catch (Exception e) {
            return "redirect:/tenant/dashboard?error=" +
                    URLEncoder.encode(e.getMessage(), StandardCharsets.UTF_8);
        }
    }
    
    @GetMapping("/messages")
    public String tenantMessages(Model model, HttpSession session,
                                 @RequestParam(value="error", required=false) String error,
                                 RedirectAttributes ra) {

        User tenant = (User) session.getAttribute("user");
        if (tenant == null) return "redirect:/login";

        Company company = tenant.getCompany();
        if (company == null) {
          ra.addFlashAttribute("error", "You are not associated with a company yet.");
          return "redirect:/tenant/dashboard";
          } else {
        
          }
        Conversation convo = messagingService.getOrCreateConversation(tenant.getCompany(), tenant);
        for (Message msg : messagingService.getMessages(convo.getId())) {
            if (!msg.isReadByTenant()) {
                msg.setReadByTenant(true);
                messageDao.save(msg);
            }
        }

        model.addAttribute("company", company);
        model.addAttribute("error", error);
        model.addAttribute("conversation", convo);
        model.addAttribute("messages", messagingService.getMessages(convo.getId()));
        return "tenant/messages"; // create templates/tenant/messages.html
    }

    @PostMapping("/messages/send")
    public String tenantSendMessage(@RequestParam("content") String content,
                                    HttpSession session) {
        try {
            User tenant = (User) session.getAttribute("user");
            if (tenant == null) return "redirect:/login";

            Conversation convo = messagingService.getOrCreateConversation(tenant.getCompany(), tenant);
            messagingService.sendMessage(tenant, convo, content);

            return "redirect:/tenant/messages";
        } catch (Exception e) {
            return "redirect:/tenant/messages?error=" +
                    URLEncoder.encode(e.getMessage(), StandardCharsets.UTF_8);
        }
    }
    
    //Maintenance Request End points
    
    @GetMapping("/maintenance/request")
	public String newRequestForm(Model model,
			                      HttpSession session, 
			                      RedirectAttributes ra) {
    	
    	  User currentUser = (User) session.getAttribute("user");
          if (currentUser == null) return "redirect:/login";

          Company company = currentUser.getCompany();
          if (company == null) {
            ra.addFlashAttribute("error", "You are not associated with a company yet.");
            return "redirect:/tenant/dashboard";
            } else {
            	
            }
            model.addAttribute("requests", maintenanceRequestDao.findAllByCompanyIdAndUserId(company.getId(), currentUser.getId()));
      return "tenant/maintenanceRequestForm";
    }
          
    
    @PostMapping("/makeRequest")
    @Transactional
    public String submitRequest(@RequestParam String title,
                                @RequestParam String description,
                                @RequestParam(value="images", required=false) MultipartFile[] images,
                                HttpSession session,
                                RedirectAttributes ra) throws IOException {

      User currentUser = (User) session.getAttribute("user");
      if (currentUser == null) return "redirect:/login";

      Company company = currentUser.getCompany();
      if (company == null) {
        ra.addFlashAttribute("error", "You are not associated with a company yet.");
        return "redirect:/tenant/dashboard";
      }
      
      TenantProfile tp= tenantProfileDao.findByUserId(currentUser.getId());
    		  if (tp == null || tp.getProperty() == null) {
    			    ra.addFlashAttribute("error",
    			        "You must be assigned to a property before submitting a maintenance request.");
    			    return "redirect:/tenant/dashboard";
    			}
      
       Property property= tp.getProperty();
      
      MaintenanceRequest req = new MaintenanceRequest();
      req.setCompany(company);
      req.setUser(currentUser);
      req.setProperty(property); // if tenant has property directly
      req.setTitle(title);
      req.setDescription(description);
      req.setMaintenancestatus(MaintenanceStatus.OPEN);

      maintenanceRequestDao.save(req);

      // Upload up to 5 images and attach
      if (images != null) {
        List<ImageAsset> uploaded = new ArrayList<>();
        int count = 0;

        for (MultipartFile file : images) {
          if (file == null || file.isEmpty()) continue;
          if (++count > 5) break;

          ImageAsset asset = imageUploadService.upload(
              file,
              ImageCategory.MAINTENANCE,
              req.getId(),   // contextId
              session
          );
          uploaded.add(asset);
        }
// Attach uploaded images to maintenance request	
        if (!uploaded.isEmpty()) {
          req.setImageAssets(uploaded);
          maintenanceRequestDao.save(req);
        }
      }
      ra.addFlashAttribute("success", "Maintenance request submitted.");
      return "redirect:/tenant/dashboard";
    }
    
}
