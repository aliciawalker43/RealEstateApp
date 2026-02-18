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
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.stripe.param.InvoiceListParams.Status;

import RealEstateApp.dao.PropertyDao;
import RealEstateApp.dao.TenantProfileDao;
import RealEstateApp.dao.UserDao;
import jakarta.servlet.http.HttpSession;
import RealEstateApp.Pojo.Company;
import RealEstateApp.Pojo.Conversation;
import RealEstateApp.Pojo.Document;
import RealEstateApp.Pojo.EmployeeProfile;
import RealEstateApp.Pojo.Expense;
import RealEstateApp.Pojo.ExpenseType;
import RealEstateApp.Pojo.ImageAsset;
import RealEstateApp.Pojo.ImageCategory;
import RealEstateApp.Pojo.MaintenanceRequest;
import RealEstateApp.Pojo.MaintenanceStatus;
import RealEstateApp.Pojo.Message;
import RealEstateApp.Pojo.RentPayment;
import RealEstateApp.Pojo.PaymentMethod;
import RealEstateApp.Pojo.PaymentStatus;
import RealEstateApp.Pojo.Property;
import RealEstateApp.Pojo.Role;
import RealEstateApp.Pojo.TenantProfile;
import RealEstateApp.Pojo.User;
import RealEstateApp.Service.CalendarService;
import RealEstateApp.Service.ImageUploadService;
import RealEstateApp.Service.MessagingService;
import RealEstateApp.Service.SMSService;
import RealEstateApp.dao.CompanyDao;
import RealEstateApp.dao.ConversationDao;
import RealEstateApp.dao.DocumentDao;
import RealEstateApp.dao.EmployeeProfileDao;
import RealEstateApp.dao.ExpenseDao;
import RealEstateApp.dao.ImageAssetDao;
import RealEstateApp.dao.MaintenanceRequestDao;

import RealEstateApp.dao.MessageDao;
import RealEstateApp.dao.RentPaymentDao;

@Controller
@RequestMapping("/landlord")
public class LandlordDashboardController {

	private final UserDao userDao;
	    private final MessageDao messageDao;
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
    private final SMSService smsService;

    public LandlordDashboardController(CompanyDao companyDao, SMSService smsService, 
    		                        PropertyDao propertyDao,
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
                                    CalendarService calendarService
                                    ) {
    	 this.messageDao = messageDao;
    	this.smsService =smsService;
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
		   
		model.addAttribute("maintenanceCount", maintenanceRequestDao.countByViewedByCompanyFalse());  
		model.addAttribute("unreadMessage", messageDao.countByReadByCompanyFalse());  
		model.addAttribute("user", user);    
		model.addAttribute("documents", d);
		model.addAttribute("employeeProfile", employeeProfile);
        model.addAttribute("company", companyDao.getReferenceById(c)) ;
        model.addAttribute("properties", propertyDao.findAllByCompany(user.getCompany()));
        model.addAttribute("payments", paymentDao.findAllByCompanyId(c));
        model.addAttribute("openMaintenanceCount", maintenanceRequestDao.count());
        
        calendarService.add7DayCalendarToModel(model, user.getCompany()); 
        
        return "landlord/dashboard";
    }
    
    @GetMapping("/profile")
    public String updateProfile(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || user.getCompany() == null) {
            return "redirect:/login";
        }
        Company company = companyDao.findById(user.getCompany().getId())
                .orElseThrow();
    
        model.addAttribute("company", company);
        model.addAttribute("user", userDao.findByCompanyIdAndRole(user.getCompany().getId(), Role.LANDLORD));

        return "landlord/companysettings/updateprofile";
    }
    
    @PostMapping("/update/company/profile")	
	public String updateCompanyProfile(@RequestParam("companyName") String companyName,
			                           @RequestParam("address") String address, 
			                           @RequestParam("phone") String phone, 
			                           @RequestParam("password") String password, 
			                           @RequestParam("passwordconfirm") String passwordConfirm,
			                           @RequestParam("email") String email, 
			                           @RequestParam("username") String username,
			                           HttpSession session,
			                           RedirectAttributes ra) {
		User user = (User) session.getAttribute("user");
		if (user == null || user.getCompany() == null) {
			return "redirect:/login";
		}
		Company company = companyDao.findById(user.getCompany().getId()).orElseThrow();
        User u= userDao.findByCompanyIdAndRole(user.getCompany().getId(), Role.LANDLORD);
		
        if
		(password != null && !password.isBlank()) {
			if (!password.equals(passwordConfirm)) {
				ra.addFlashAttribute("error", "Passwords do not match.");
				return "redirect:/landlord/profile";
			}
			String encodedPasscode = Base64.getEncoder().encodeToString(password.getBytes());
			u.setPasscode(encodedPasscode);
		}
        if (StringUtils.hasText(username)) {
            u.setUsername(username);
        }
        if (StringUtils.hasText(phone)) {
        	u.setPhoneNumber(phone);
        }
        if (StringUtils.hasText(address)) {
        	u.setBillingAddress(address);
        }
        if (StringUtils.hasText(email)) {
        	 u.setEmail(email);
        }
        if (StringUtils.hasText(companyName)) {
		company.setName(companyName);
        }
        userDao.save(u);
		companyDao.save(company);
		ra.addFlashAttribute("success", "Company profile updated.");
		return "redirect:/landlord/profile";
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

        return "landlord/companysettings/company-profile";
    }
    
    
    @RequestMapping("/invoice/setting")
	public String showInvoiceSettingsPage(Model model, HttpSession session) {
		User currentUser = (User) session.getAttribute("user");
		if (currentUser == null || currentUser.getCompany() == null) {
			return "redirect:/login";
		}
		Company company = companyDao.findById(currentUser.getCompany().getId())
				.orElseThrow(() -> new IllegalStateException("Company not found"));

		model.addAttribute("company", company);
		return "landlord/companysettings/invoice-setting";
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
    
    @RequestMapping("/branding")
    public String showBrandingPage(Model model, HttpSession session) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null || currentUser.getCompany() == null) {
            return "redirect:/login";
        }
        Company company = companyDao.findById(currentUser.
        getCompany().getId())
                .orElseThrow(() -> new IllegalStateException("Company not found"));
         
        model.addAttribute("company", company);
        return "landlord/companysettings/branding";
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

    @PostMapping("/property/update")
	   public String updatePropertyForm(Model model,HttpSession session,
			            @RequestParam ("tenant")User user,
			            @RequestParam ("leaseStartDate") LocalDate startEndDate,
			            @RequestParam ("leaseEndDate") LocalDate leaseEndDate,
			            @RequestParam ("dueDate")Integer rentDueDay,
	                    @RequestParam ("rentAmount") BigDecimal rentAmount,
                        @RequestParam ("lateFeeAmount") BigDecimal lateFee,
			            @RequestParam ("id") Long propertyId) { 
			  
    	Property prop= propertyDao.findPropertyById(propertyId);
    	
    	TenantProfile tp = tenantProfileDao.findByUserId(user.getId());

    	if (tp == null) {
    	    tp = new TenantProfile();
    	    tp.setUser(user);
    	}
    	tp.setProperty(prop);
    	tp.setMailingAddress(prop.getRentalAddress());

    	tenantProfileDao.save(tp);
	   
	   prop.setTenant(user);
	   prop.setLeaseStartDate(startEndDate);
	   prop.setLeaseEndDate(leaseEndDate);
	   prop.setLateFee(lateFee);
	   prop.setRentAmount(rentAmount);
	   prop.setRentDueDay(rentDueDay);
	   
	   propertyDao.save(prop);
	   
		
		model.addAttribute( "user", user);
        model.addAttribute("property", prop);
	   return "redirect:/landlord/propertyList";
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


	
	
	/// Payments Endpoints

	@RequestMapping("/payments/view")
	public String showAllPayments(HttpSession session, Model model) {
	    User currentUser = (User) session.getAttribute("user");
	    if (currentUser == null || currentUser.getCompany() == null) {
	        return "redirect:/login";
	    }
	  
	    model.addAttribute("properties", propertyDao.findAllByCompany(currentUser.getCompany()));
	    model.addAttribute("payments",  paymentDao.findAllByCompanyId(currentUser.getCompany().getId()));
		return "landlord/paymenthistory";
	}
	
	
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
	    	 
	    	        RentPayment pay = new RentPayment();
	    	        pay.setCompany(company); // ✅ company id comes from session user
	    	        pay.setAmountPaid(amount);
	    	        pay.setPaymentDate(date);
	    	        pay.setMethod(PaymentMethod.OTHER);
	    	        pay.setProperty(property);
	    	        pay.setStatus(PaymentStatus.PAID);
	    	        
	    	        paymentDao.save(pay);
	    	 
	    	        return "redirect:/landlord/dashboard";
	    	 }
	 
	 @RequestMapping("/payment/delete{id}")
	 String deletePayment (@RequestParam("id") Long id) {
		 paymentDao.deleteById(id);
		 return "redirect:/landlord/payments/view";
	 }
	 
	 
	    	 
    @GetMapping("/messages")
    public String companyInbox(Model model, HttpSession session) {
        User staff = (User) session.getAttribute("user");
        if (staff == null) return "redirect:/login";

    Long companyId = staff.getCompany().getId();
    List<Conversation> conversations = conversationDao.findByCompanyIdOrderByCreatedAtDesc(companyId);

    // Map userId to profile image URL
    Map<Long, String> profileImages = new HashMap<>();
    for (Conversation convo : conversations) {
		User tenant = convo.getTenant();
		TenantProfile tenantProfile = tenantProfileDao.findByUserId(tenant.getId());
		if (tenantProfile != null && tenantProfile.getProfileImageUrl() != null) {
			profileImages.put(tenant.getId(), tenantProfile.getProfileImageUrl());
		} else {
			profileImages.put(tenant.getId(), "/images/default-profile.png"); // default image
		}
	}

    model.addAttribute("conversations", conversations);
    model.addAttribute("profileImages", profileImages);
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
        for (Message msg : messagingService.getMessages(conversationId)) {
            if (!msg.isReadByCompany()) {
                msg.setReadByCompany(true);
                messageDao.save(msg);
            }
        }
        

        model.addAttribute("error", error);
        model.addAttribute("conversation", convo);
        model.addAttribute("messages", messagingService.getMessages(conversationId));
        return "landlord/messages"; // templates/company/thread.html
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
            return "redirect:/landlord/messages/" + conversationId;

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
    	       
      List<MaintenanceRequest> requests= maintenanceRequestDao.findAllByCompanyId(company.getId());	
         
      for (MaintenanceRequest m : requests) {
          if (m.getViewedByCompany().equals(false)) {
              m.setViewedByCompany(true);
             maintenanceRequestDao.save(m);
          }
      
      }
    	model.addAttribute("company", company);
    	 model.addAttribute("requests", requests);
       
	return "/landlord/maintenance";
	}
    
    @PostMapping("/maintenance/update-status/{id}")
    public String updateMaintenanceStatus(Model model, HttpSession session,
                                    @RequestParam ("id") Long id,
                                    @RequestParam ("status") MaintenanceStatus status) {
    	User currentUser= (User)session.getAttribute("user");
    	if (currentUser == null) return "redirect:/login";
    	
    	Company company = currentUser.getCompany();
    	if (company == null) return "redirect:/login";
    	
    	MaintenanceRequest request= maintenanceRequestDao.findById(id).orElseThrow();
    	
    	request.setStatus(status);
    	maintenanceRequestDao.save(request);
        return "redirect:/landlord/maintenance";
    }
    
// Expense Endpoints
	@RequestMapping("/expense")
	public String showExpenses(HttpSession session, Model model) {
	    User currentUser = (User) session.getAttribute("user");
	    if (currentUser == null || currentUser.getCompany() == null) {
	        return "redirect:/login";
	    }
	    model.addAttribute("properties", propertyDao.findAllByCompany(currentUser.getCompany()));
	    model.addAttribute("company", currentUser.getCompany());
	    model.addAttribute("expense", expenseDao.findAllByCompanyId(currentUser.getCompany().getId()));
		return "landlord/expense";
	}
	
	@PostMapping("/expense/add")
	public String addExpenseForm(Model model, HttpSession session, @RequestParam ("propertyId") Long propertyId,
			                       @RequestParam("amount") BigDecimal amount,  
			                       @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
		                          @RequestParam("description") String description,
		                          @RequestParam("notes") String notes,
		                          @RequestParam("type") String type,
		                          @RequestParam("vendor") String vendor,
		                       	RedirectAttributes ra) {

		User currentUser = (User) session.getAttribute("user");
		if (currentUser == null)
			return "redirect:/login";

		Company company = currentUser.getCompany();
		if (company == null)
			return "redirect:/login";
		
	
		Property property = propertyDao.findPropertyById(propertyId);
		Expense expense = new Expense();
		expense.setCompany(company); // ✅ company id comes from session user
		expense.setAmount(amount);
		expense.setDate(date);
		expense.setDescription(description + " - NOTES: " + notes);
		 expense.setProperty(property);
		 expense.setType(ExpenseType.OTHER); 
		 expense.setVendor(vendor); 
		

		expenseDao.save(expense);

		return "redirect:/landlord/expense";
	}
    
	@RequestMapping("/documents")
	public String showDocuments(HttpSession session, Model model) {
	    User currentUser = (User) session.getAttribute("user");
	    if (currentUser == null || currentUser.getCompany() == null) {
	        return "redirect:/login";
	    }
	    
	    model.addAttribute("tenant", userDao.findByCompanyIdAndRole(currentUser.getCompany().getId(), Role.TENANT));
	    model.addAttribute("documents", documentDao.findAllByCompany(currentUser.getCompany()));
		return "landlord/documents";
	}
	
	
	@GetMapping("/text")
	public String showSmsForm(Model model, HttpSession session) {
		User staff = (User) session.getAttribute("user");
		if (staff == null)
			return "redirect:/login";
		Company company = staff.getCompany();

		List<User> tenants = userDao.findUsersByCompanyIdAndRole(company.getId(), Role.TENANT);
		model.addAttribute("tenants", tenants);
		model.addAttribute("company", company);
		return "landlord/smstext";
	}
	
	/*@PostMapping("/sms/send")
	public String sendSmsToTenant(@RequestParam("tenantId") Long tenantId, 
			                      @RequestParam("number") String number,
			                      @RequestParam("message") String message,
			                      HttpSession session, RedirectAttributes ra) {
		try {
			User staff = (User) session.getAttribute("user");
			if (staff == null)
				return "redirect:/login";

		   User tenant = userDao.findById(tenantId)
					.orElseThrow(() -> new IllegalArgumentException("Tenant not found"));

			if (!tenant.getCompany().getId().equals(staff.getCompany().getId())) {
				throw new IllegalStateException("Unauthorized tenant access");
			}

				
			smsService.sendSms(tenant.getPhoneNumber(), message);
			ra.addFlashAttribute("success", "SMS sent to tenant.");
			return "redirect:/landlord/text";

	} catch (Exception e) {
			ra.addFlashAttribute("error", "Failed to send SMS: " + e.getMessage());
			return "redirect:/landlord/messages";
		}
	}*/
	
	
	
	//test sms delete after
	
	// Add these fields to your controller
	private final boolean testMode = true; // Set to false in production
	private final String testNumber = "+18777804236"; // Your Twilio virtual/test number

	@PostMapping("/sms/send")
	public String sendSmsToTenantTest(@RequestParam("tenantId") Long tenantId,
	                              @RequestParam("number") String number,
	                              @RequestParam("message") String message,
	                              HttpSession session, RedirectAttributes ra) {
	    try {
	        User staff = (User) session.getAttribute("user");
	        if (staff == null)
	            return "redirect:/login";

	        User tenant = userDao.findById(tenantId)
	            .orElseThrow(() -> new IllegalArgumentException("Tenant not found"));

	        if (!tenant.getCompany().getId().equals(staff.getCompany().getId())) {
	            throw new IllegalStateException("Unauthorized tenant access");
	        }

	        String body= "DO NOT REPLY TO THIS MESSAGE: " + message;
	        String recipient = testMode ? testNumber : tenant.getPhoneNumber();
	        smsService.sendSms(recipient, body);

	        ra.addFlashAttribute("success", "SMS sent to tenant.");
	        return "redirect:/landlord/text";

	    } catch (Exception e) {
	        ra.addFlashAttribute("error", "Failed to send SMS: " + e.getMessage());
	        return "redirect:/landlord/text";
	    }
	
	}
}
