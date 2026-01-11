package RealEstateApp.Controllers;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.stripe.param.InvoiceListParams.Status;

import RealEstateApp.dao.PropertyDao;
import RealEstateApp.dao.UserDao;
import jakarta.servlet.http.HttpSession;
import RealEstateApp.Pojo.Company;
import RealEstateApp.Pojo.Conversation;
import RealEstateApp.Pojo.Document;
import RealEstateApp.Pojo.EmployeeProfile;
import RealEstateApp.Pojo.ImageAsset;
import RealEstateApp.Pojo.ImageCategory;
import RealEstateApp.Pojo.MaintenanceRequest;
import RealEstateApp.Pojo.MaintenanceRequestImage;
import RealEstateApp.Pojo.Payment;
import RealEstateApp.Pojo.PaymentMethod;
import RealEstateApp.Pojo.PaymentStatus;
import RealEstateApp.Pojo.Property;
import RealEstateApp.Pojo.Role;
import RealEstateApp.Pojo.TenantProfile;
import RealEstateApp.Pojo.User;
import RealEstateApp.Service.ImageUploadService;
import RealEstateApp.Service.MessagingService;
import RealEstateApp.dao.CompanyDao;
import RealEstateApp.dao.ConversationDao;
import RealEstateApp.dao.DocumentDao;
import RealEstateApp.dao.EmployeeProfileDao;
import RealEstateApp.dao.ImageAssetDao;
import RealEstateApp.dao.MaintenanceRequestDao;
import RealEstateApp.dao.MaintenanceRequestImageDao;
import RealEstateApp.dao.MessageDao;
import RealEstateApp.dao.PaymentDao;

@Controller
@RequestMapping("/landlord")
public class LandlordDashboardController {

	private final UserDao userDao;
	 private final ImageUploadService imageUploadService;
	 private final ImageAssetDao imageAssetDao;
	private final DocumentDao documentDao;
    private final CompanyDao companyDao;
    private final PropertyDao propertyDao;
    private final PaymentDao paymentDao;
    private final MaintenanceRequestDao maintenanceRequestDao;
    private final MaintenanceRequestImageDao maintenanceRequestImageDao;
    private final ConversationDao conversationDao;
    private final MessagingService messagingService;
    private final EmployeeProfileDao employeeProfileDao;

    public LandlordDashboardController(CompanyDao companyDao, PropertyDao propertyDao,
                                    PaymentDao paymentDao,
                                    MaintenanceRequestDao maintenanceRequestDao,
                                    MessageDao messageDao,
                                    ConversationDao conversationDao,
                                    MessagingService messagingService,
                                    UserDao userDao,
                                    DocumentDao documentDao,
                                    ImageAssetDao imageAssetDao,
                                    ImageUploadService imageUploadService,
                                    EmployeeProfileDao employeeProfileDao,
                                    MaintenanceRequestImageDao maintenanceRequestImageDao) {
    	this.imageUploadService =imageUploadService;
    	this.imageAssetDao= imageAssetDao;
    	this. documentDao= documentDao;
    	this.userDao= userDao;
    	this.employeeProfileDao= employeeProfileDao;
    	this.companyDao = companyDao;
        this.propertyDao = propertyDao;
        this.paymentDao= paymentDao;
        this.maintenanceRequestDao = maintenanceRequestDao;
        this.maintenanceRequestImageDao = maintenanceRequestImageDao;
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
        
         
        return "landlord/dashboard";
    }
    
    @GetMapping("/company/profile")
    public String companyProfile(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || user.getCompany() == null) {
            return "redirect:/login";
        }

        Company company = companyDao.findById(user.getCompany().getId())
                .orElseThrow();
    
        List<EmployeeProfile> employees = employeeProfileDao.findByUser_Company_Id(company.getId());
      
        System.out.print("employees= "+ ""+ employees );
        model.addAttribute("company", company);
        model.addAttribute("employees", employees);

        return "landlord/company-profile";
    }
    
    //Company Details End point
    
  /*  @PostMapping("/company/billing-settings")
    public String saveBillingSettings(@RequestParam("lateDays") int lateDays,
                                     HttpSession session,
                                     RedirectAttributes ra) {
        User u = (User) session.getAttribute("user");
        if (u == null || u.getCompany() == null) return "redirect:/login";

        Company c = companyDao.findById(u.getCompany().getId())
                .orElseThrow(() -> new IllegalStateException("Company not found"));

        c.setLateNoticeDaysAfterDue(Math.max(0, lateDays));
        if (c.getTimeZone() == null || c.getTimeZone().isBlank()) c.setTimeZone("America/Detroit");

        companyDao.save(c);
        ra.addFlashAttribute("success", "Billing settings updated.");
        return "redirect:/landlord/company/profile";
    }

        companyDao.save(c);

        ra.addFlashAttribute("success", "Auto invoice send time updated.");
        return "redirect:/landlord/company/profile";
    }*/


//Company Branding   
    @PostMapping("/branding/logo")
    public String uploadLogo(@RequestParam("file") MultipartFile file,
                             HttpSession session,
                             RedirectAttributes ra) {
        try {
            User currentUser = (User) session.getAttribute("user");
            if (currentUser == null) return "redirect:/login";

            Company company = currentUser.getCompany();
            if (company == null) return "redirect:/login";

            // upload as ImageAsset (stored under uploads/company/{companyId}/company_logo/)
            ImageAsset saved = imageUploadService.upload(file, ImageCategory.COMPANY_LOGO, null, session);

            // ✅ set current company logo pointer
            company.setLogoUrl(saved.getStoragePath()); // must be a web path like /uploads/...
            companyDao.save(company);

            ra.addFlashAttribute("success", "Logo updated.");
            return "redirect:/landlord/dashboard";
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Upload failed: " + e.getMessage());
            return "redirect:/landlord/dashboard";
        }
    }
    
    @PostMapping("/branding/color")
    public String updateBrandColor(@RequestParam("brandPrimary") String brandPrimary,
                                   HttpSession session,
                                   RedirectAttributes ra) {

        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null || currentUser.getCompany() == null) {
            return "redirect:/login";
        }

        Company company = companyDao.findById(currentUser.getCompany().getId())
                .orElseThrow(() -> new IllegalStateException("Company not found"));

        if (!isValidHexColor(brandPrimary)) {
            ra.addFlashAttribute("error", "Invalid color format. Use hex format like #2563eb.");
            return "redirect:/landlord/company/profile";
        }

        company.setPrimaryColor(brandPrimary);
        companyDao.save(company);

        ra.addFlashAttribute("success", "Brand color updated.");
        return "redirect:/landlord/company/profile";
    }

    private boolean isValidHexColor(String value) {
        return value != null && value.matches("^#[0-9A-Fa-f]{6}$");
    }
    
    //Property Endpoints
    @GetMapping("/propertyList")
	public String viewPropertyList(Model model, HttpSession session) {
     	User currentUser= (User)session.getAttribute("user");
    		Long userId =  currentUser.getId();
    		 User user = userDao.findById(userId)
    	                .orElseThrow(() -> new IllegalStateException("User not found"));
    		 
    		 if (!"ACTIVE".equals(user.getCompany().getSubscriptionStatus())) {
    	    	  return "redirect:/subscribe?error=Subscription%20required%20to%20access%20dashboard";
    	    	}
    		 
		List<Property> prop= propertyDao.findAllByCompany(user.getCompany());
		
		model.addAttribute("property", prop);
		return "landlord/propertychart";
	}
    
    @PostMapping("/addProperty")
    public String addPropertyForm(Model model, HttpSession session,
    		                     @RequestParam ("property") String address,
    		                     RedirectAttributes ra) {
    	User currentUser= (User)session.getAttribute("user");
    	if (currentUser == null) return "redirect:/login";
    	
    	Company company = currentUser.getCompany();
    	if (company == null) return "redirect:/login";
    	
    	 if (address == null || address.trim().isEmpty()) {
    	        ra.addFlashAttribute("error", "Rental address is required.");
    	 }
    	        Property p = new Property();
    	        p.setCompany(company); // ✅ company id comes from session user
    	        p.setRentalAddress(address.trim());
    	        p.setRentDueDay(1);
    	        
    	        
    	        propertyDao.save(p);
    	 
    	        return "redirect:/landlord/propertyList";
    	 
    }
    
	   @RequestMapping("/property/update{id}")
		   public String updatePropertyForm(Model model, HttpSession session, @RequestParam ("id") Long id) { 
			User currentUser= (User)session.getAttribute("user");
	    	if (currentUser == null) return "redirect:/login";
	    	
	    	Company company = currentUser.getCompany();
	    	if (company == null) return "redirect:/login";
	    	
		   Property prop= propertyDao.findPropertyById(id);
		   List<User> user= userDao.findAllByCompanyId(currentUser.getCompany().getId());
		    
		   
		  /* User use: user
		    if(use.getRole().EMPLOYEE || use.getRole().LANDLORD) {
		    	user.remove(use);*/
		    
			
		   model.addAttribute( "company", company);
			model.addAttribute( "user", user);
	      model.addAttribute("property", prop);
		   return "landlord/updatepropertyform";
	   }

    @PostMapping("/property/update{id}")
	   public String updatePropertyForm(Model model,HttpSession session,
			            @RequestParam ("tenant")User user,
			            @RequestParam ("leaseEndDate") LocalDate leaseEndDate,
			            @RequestParam ("dueDate")Integer rentDueDay,
	                    @RequestParam ("rentAmount") BigDecimal rentAmount,
                        @RequestParam ("lateFeeAmount") BigDecimal lateFee,
			            @RequestParam ("id") Long id) { 
			  
	   Property prop= propertyDao.findPropertyById(id);
	   
	   prop.setLeaseEndDate(leaseEndDate);
	   prop.setLateFee(lateFee);
	   prop.setRentAmount(rentAmount);
	   prop.setRentDueDay(rentDueDay);
	   
	   propertyDao.save(prop);
	   
		
		model.addAttribute( "user", user);
        model.addAttribute("property", prop);
	   return "updatepropertyform";
}
    
    
	@RequestMapping("/property/delete{id}")
	public String removeProperty (@RequestParam("id") Long id) {
	propertyDao.deleteById(id);
	return "redirect:/landlord/propertylist";
}
	
	
	//Add Employees
	@RequestMapping("/employees/new")
	public String showAddEmployeeForm(HttpSession session, Model model) {
	    User currentUser = (User) session.getAttribute("user");
	    if (currentUser == null || currentUser.getCompany() == null) {
	        return "redirect:/login";
	    }
	    model.addAttribute("employee", new User());
		return "landlord/addemployee";
	}
	
	
	@PostMapping("/addEmployee")
	public String addEmployee(@RequestParam ("firstname") String firstname,
	                          @RequestParam ("lastname") String lastname,
	                          @RequestParam ("email") String email,
	                          @RequestParam ("hireDate")@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hireDate,
	                          @RequestParam("payRate") Double payRate,
	                          @RequestParam("position") String position,
	                          HttpSession session,
	                          RedirectAttributes ra) {

	    User currentUser = (User) session.getAttribute("user");
	    if (currentUser == null || currentUser.getCompany() == null) {
	        return "redirect:/login";
	    }

	    Company company = currentUser.getCompany();

	    if (userDao.existsByEmail(email)) {
	        ra.addFlashAttribute("error", "An account with this email already exists.");
	        return "redirect:/landlord/employees/new";
	    }

	    // 1️⃣ Create USER
	    User employee = new User();
	    employee.setFirstname(firstname);
	    employee.setLastname(lastname);
	    employee.setEmail(email);
	    employee.setUsername(email);
	    employee.setRole(Role.EMPLOYEE);
	    employee.setCompany(company);

	    // Temporary password (recommend invite flow later)
	    employee.setPasscode(Base64.getEncoder()
	            .encodeToString("changeme123".getBytes()));

	    userDao.save(employee);

	    // 2️⃣ Create EMPLOYEE PROFILE
	    EmployeeProfile profile = new EmployeeProfile();
	    profile.setUser(employee);
	    profile.setHireDate(hireDate);
	    profile.setPayRate(payRate);
	    profile.setPosition(position);

	    employeeProfileDao.save(profile);

	    ra.addFlashAttribute("success", "Employee added successfully.");
	    return "redirect:/landlord/company/profile";
	}


	
	
	///Add Payments Manually
	 @PostMapping("/addPayment")
	    public String addPaymentForm(Model model, HttpSession session,
	    		                     @RequestParam ("paymentAmount") BigDecimal amount,
	    		                     @RequestParam ("method") String method,
	    		                     @RequestParam ("property") Long propId,
	    		                     @RequestParam ("date")@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
	    		                     RedirectAttributes ra) {
		 
	    	User currentUser= (User)session.getAttribute("user");
	    	if (currentUser == null) return "redirect:/login";
	    	
	    	Company company = currentUser.getCompany();
	    	if (company == null) return "redirect:/login";
	    	
	    	 Property property= propertyDao.findPropertyById(propId);
	    	        
	    	 System.out.println("property=" + propId);
	    	 
	    	        Payment pay = new Payment();
	    	        pay.setCompany(company); // ✅ company id comes from session user
	    	        pay.setAmount(amount);
	    	        pay.setPaymentDate(date);
	    	        pay.setMethod(PaymentMethod.OTHER);
	    	        pay.setProperty(property);
	    	        pay.setStatus(PaymentStatus.PAID);
	    	        
	    	        paymentDao.save(pay);
	    	 
	    	        return "redirect:/landlord/dashboard";
	    	 }
	    	 
    @GetMapping("/messages")
    public String companyInbox(Model model, HttpSession session) {
        User staff = (User) session.getAttribute("user");
        if (staff == null) return "redirect:/login";

        // only company users should access (LANDLORD/EMPLOYEE)
        // if (staff.getRole() == Role.TENANT) return "redirect:/tenant/dashboard";

        Long companyId = staff.getCompany().getId();
        model.addAttribute("conversations", conversationDao.findByCompanyIdOrderByCreatedAtDesc(companyId));
        return "landlord/inbox";
    }

    @GetMapping("/messages/{conversationId}")
    public String companyThread(@PathVariable Long conversationId,
                                Model model,
                                HttpSession session,
                                @RequestParam(value="error", required=false) String error) {
        User staff = (User) session.getAttribute("user");
        if (staff == null) return "redirect:/login";

        Conversation convo = conversationDao.findById(conversationId)
                .orElseThrow(() -> new IllegalArgumentException("Conversation not found"));

        // 🔒 company scope security
        if (!convo.getCompany().getId().equals(staff.getCompany().getId())) {
            throw new IllegalStateException("Unauthorized conversation access");
        }

        model.addAttribute("error", error);
        model.addAttribute("conversation", convo);
        model.addAttribute("messages", messagingService.getMessages(conversationId));
        return "company/thread"; // templates/company/thread.html
    }

    @PostMapping("/messages/{conversationId}/reply")
    public String companyReply(@PathVariable Long conversationId,
                               @RequestParam("content") String content,
                               HttpSession session) {
        try {
            User staff = (User) session.getAttribute("user");
            if (staff == null) return "redirect:/login";

            Conversation convo = conversationDao.findById(conversationId)
                    .orElseThrow(() -> new IllegalArgumentException("Conversation not found"));

            if (!convo.getCompany().getId().equals(staff.getCompany().getId())) {
                throw new IllegalStateException("Unauthorized conversation access");
            }

            messagingService.sendMessage(staff, convo, content);
            return "redirect:/company/messages/" + conversationId;

        } catch (Exception e) {
            return "redirect:/company/messages/" + conversationId + "?error=" +
                    URLEncoder.encode(e.getMessage(), StandardCharsets.UTF_8);
        }
    
}
    
    @RequestMapping("/maintenance")
	public String viewMaintenanceRequest (Model model, HttpSession session) {
    	User currentUser= (User)session.getAttribute("user");
    	if (currentUser == null) return "redirect:/login";
    	
    	Company company = currentUser.getCompany();
    	if (company == null) return "redirect:/login";
    	
    	
        // 1) Requests for this company
        List<MaintenanceRequest> requests =
                maintenanceRequestDao.findByCompanyIdOrderByCreatedAtDesc(company.getId());

        // 2) Pull all request IDs
        List<Long> requestIds = requests.stream()
                .map(MaintenanceRequest::getId)
                .toList();

        // 3) Fetch images in one query (avoids N+1)
        Map<Long, List<MaintenanceRequestImage>> imagesByRequestId = new HashMap<>();
        if (!requestIds.isEmpty()) {
            List<MaintenanceRequestImage> allImages =
                    maintenanceRequestImageDao.findByRequestIdIn(requestIds);

            imagesByRequestId = allImages.stream()
                    .collect(Collectors.groupingBy(img -> img.getRequest().getId()));
        }

        model.addAttribute("company", company);
        model.addAttribute("requests", requests);
        model.addAttribute("imagesByRequestId", imagesByRequestId);
		
	return "/landlord/maintenance";
	}
    
    
}