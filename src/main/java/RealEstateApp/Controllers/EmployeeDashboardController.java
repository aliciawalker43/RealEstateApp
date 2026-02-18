package RealEstateApp.Controllers;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import RealEstateApp.Pojo.Document;
import RealEstateApp.Pojo.EmployeeProfile;
import RealEstateApp.Pojo.ImageCategory;
import RealEstateApp.Pojo.TenantProfile;
import RealEstateApp.Pojo.User;
import RealEstateApp.Service.CalendarService;
import RealEstateApp.Service.ImageUploadService;
import RealEstateApp.Service.MessagingService;
import RealEstateApp.dao.CompanyDao;
import RealEstateApp.dao.ConversationDao;
import RealEstateApp.dao.DocumentDao;
import RealEstateApp.dao.EmployeeProfileDao;
import RealEstateApp.dao.ExpenseDao;
import RealEstateApp.dao.ImageAssetDao;
import RealEstateApp.dao.MaintenanceRequestDao;
import RealEstateApp.dao.MessageDao;
import RealEstateApp.dao.PropertyDao;
import RealEstateApp.dao.RentPaymentDao;
import RealEstateApp.dao.TenantProfileDao;
import RealEstateApp.dao.UserDao;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/employee")
public class EmployeeDashboardController {
	
	private final UserDao userDao;
	 private final ImageUploadService imageUploadService;
	 private final ImageAssetDao imageAssetDao;
	private final DocumentDao documentDao;
   private final CompanyDao companyDao;
   private final PropertyDao propertyDao;
   private final RentPaymentDao paymentDao;
   private final MaintenanceRequestDao maintenanceRequestDao;
   private final ExpenseDao expenseDao;
   private final ConversationDao conversationDao;
   private final MessagingService messagingService;
   private final EmployeeProfileDao employeeProfileDao;
   private final TenantProfileDao tenantProfileDao;
   private final CalendarService calendarService;

   public EmployeeDashboardController(CompanyDao companyDao, PropertyDao propertyDao,
                                   RentPaymentDao paymentDao,
                                   MaintenanceRequestDao maintenanceRequestDao,
                                   MessageDao messageDao,
                                   ConversationDao conversationDao,
                                   MessagingService messagingService,
                                   UserDao userDao,
                                   DocumentDao documentDao,
                                   ImageAssetDao imageAssetDao,
                                   ImageUploadService imageUploadService,
                                   EmployeeProfileDao employeeProfileDao,
                                   TenantProfileDao tenantProfileDao,
                                   ExpenseDao expenseDao,
                                   CalendarService calendarService) {
   	this. calendarService= calendarService;
   	this.tenantProfileDao =tenantProfileDao;
   	this.imageUploadService =imageUploadService;
   	this.imageAssetDao= imageAssetDao;
   	this. documentDao= documentDao;
   	this.userDao= userDao;
   	this.employeeProfileDao= employeeProfileDao;
   	this.companyDao = companyDao;
       this.propertyDao = propertyDao;
       this.paymentDao= paymentDao;
       this.maintenanceRequestDao = maintenanceRequestDao;
      this.expenseDao =expenseDao;
       this.conversationDao = conversationDao;
       this.messagingService = messagingService;
   }
   
   @GetMapping("/dashboard")
   public String dashboard(Model model, HttpSession session) {
   	
   	User currentUser= (User)session.getAttribute("user");
		Long userId =  currentUser.getId();
		 User user = userDao.findById(userId)
	                .orElseThrow(() -> new IllegalStateException("User not found"));
		 
		 if (!"ACTIVE".equals(user.getCompany().getSubscriptionStatus())) {
	    	  return "redirect:/subscribe?error=Subscription%20required%20to%20access%20dashboard";
	    	}
		    // ✅ Add Employee Profile (even if not found)
		    EmployeeProfile employeeProfile = employeeProfileDao.findById(user.getId()).orElse(null);
		   List<Document> d=  documentDao.findAllByCompany(user.getCompany());
		  Long c = user.getCompany().getId();
		   System.out.println("company=" + c);	
		   
		model.addAttribute("user", user);    
		model.addAttribute("documents", d);
		model.addAttribute("employeeProfile", employeeProfile);
       model.addAttribute("company", companyDao.getReferenceById(c)) ;
       model.addAttribute("properties", propertyDao.findAllByCompany(user.getCompany()));
       model.addAttribute("payments", paymentDao.findAllByCompanyId(c));
       model.addAttribute("openMaintenanceCount", maintenanceRequestDao.count());
       
       calendarService.add7DayCalendarToModel(model, user.getCompany()); 
       
       return "employee/dashboard";
   }
   
   @RequestMapping("/profile")
  	String showProfile(Model model, HttpSession session) {
  		User tenant = (User) session.getAttribute("user");
  		if (tenant == null)
  			return "redirect:/login";

  		EmployeeProfile employeeProfile = employeeProfileDao.findById(tenant.getId()).orElse(null);
  		model.addAttribute("employeeProfile", employeeProfile);
  		model.addAttribute("user", tenant);
      return "employee/profile";
      }
   
   //Employee Profile Endpoints
   
   @PostMapping("/profile/image")
   public String uploadProfileImage(@RequestParam("file") MultipartFile file,
                                    HttpSession session) {
       try {
           imageUploadService.uploadProfileImage(file, ImageCategory.PROFILE, null, session);
           return "redirect:/employee/dashboard";
       } catch (Exception e) {
           return "redirect:/employee/dashboard?error=" +
                   URLEncoder.encode(e.getMessage(), StandardCharsets.UTF_8);
       }
   }
   
   
   @PostMapping("/profile/update")
	public String updateProfile(@RequestParam("firstname") String firstName, 
			                    @RequestParam("lastname") String lastName,
			                    @RequestParam("email") String email,
			                    @RequestParam("username") String username,
			                    @RequestParam("password") String password,
			                    @RequestParam("passwordconfirm") String confirmPassword,
			                    @RequestParam("phone") String phoneNumber, HttpSession session) {
		User currentUser = (User) session.getAttribute("user");
		if (currentUser == null) {
			return "redirect:/login";
		}
		User use = userDao.findById(currentUser.getId()).orElseThrow(() -> new IllegalStateException("User not found"));
		if (!password.isEmpty() && !password.equals(confirmPassword)) {
			return "redirect:/employee/profile?error=Passwords%20do%20not%20match";
		}
		if (!password.isEmpty()) {
			String encodedPasscode = Base64.getEncoder().encodeToString(password.getBytes());
			use.setPasscode(encodedPasscode);
			session.setAttribute("user", use); // Update session with new password
		}
		use.setUsername(username);
		use.setFirstname(firstName);
		use.setLastname(lastName);
		use.setEmail(email);
		use.setPhoneNumber(phoneNumber);
		
	    userDao.save(use);

		return "redirect:/employee/profile?success=Profile%20updated%20successfully";
	}

}
