package RealEstateApp.Controllers;


import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import RealEstateApp.dao.PropertyDao;
import RealEstateApp.dao.UserDao;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import RealEstateApp.Pojo.AppCustomerServ;
import RealEstateApp.Pojo.AppFeedback;
import RealEstateApp.Pojo.Company;
import RealEstateApp.Pojo.CompanySubscription;
import RealEstateApp.Pojo.RentPayment;
import RealEstateApp.Pojo.User;
import RealEstateApp.Service.ImageUploadService;
import RealEstateApp.Service.MessagingService;
import RealEstateApp.dao.AppCustomerServDao;
import RealEstateApp.dao.AppFeedbackDao;
import RealEstateApp.dao.CompanyDao;
import RealEstateApp.dao.CompanySubscriptionDao;
import RealEstateApp.dao.ConversationDao;
import RealEstateApp.dao.DocumentDao;
import RealEstateApp.dao.EmployeeProfileDao;
import RealEstateApp.dao.ImageAssetDao;
import RealEstateApp.dao.MaintenanceRequestDao;

import RealEstateApp.dao.MessageDao;
import RealEstateApp.dao.RentPaymentDao;



@Controller
@RequestMapping("/admin")
public class AdminDashboardController {

    private final CompanyDao companyDao;
    private final PropertyDao propertyDao;
    private final AppFeedbackDao appFeedbackDao;
    private final AppCustomerServDao appCustomerServDao;
    private final CompanySubscriptionDao subscriptionDao;
    private final UserDao userDao;
    private RentPaymentDao paymentDao;

    public AdminDashboardController(CompanyDao companyDao, PropertyDao propertyDao,
    		                        AppCustomerServDao appCustomerServDao,
                                    MaintenanceRequestDao maintenanceRequestDao,
                                    CompanySubscriptionDao subscriptionDao,
                                    UserDao userDao, AppFeedbackDao appFeedbackDao, 
                                    RentPaymentDao paymentDao
                                    ) {
        this.userDao= userDao;
    	this.companyDao = companyDao;
        this.propertyDao = propertyDao;
        this.appCustomerServDao =appCustomerServDao;
        this.appFeedbackDao=appFeedbackDao;
        this.subscriptionDao = subscriptionDao;
        this.paymentDao = paymentDao;
       
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, HttpSession session) {
    	
    	
    	model.addAttribute("customerServCount", appFeedbackDao.count());
    	model.addAttribute("customerServCount", appCustomerServDao.count());
    	model.addAttribute("userCount",userDao.count());
        model.addAttribute("companyCount", companyDao.count());
        model.addAttribute("propertyCount", propertyDao.count());
        
        return "admin/dashboard";
    }
    
    
        @GetMapping ("/view/subscriptions")
        public String viewSubscriptions(Model model) {
        	
        	
        	List<Company> company= companyDao.findAll();
        	
        	model.addAttribute("company", company);
        	return "admin/subscriptions";
        }
        
        @GetMapping ("/subscription/delete{id}")
        public String deleteSubscriptions(@RequestParam("id") Long id, Model model) {
        	
        	//delete all user Associated with company first, 
        	List<User> user= userDao.findAllByCompanyId(id);
        	userDao.deleteAll(user);
        	  
        	//then delete company, then subscription
            Company company= companyDao.getReferenceById(id);
        	companyDao.delete(company);
        	
        	model.addAttribute("company", company);
        	return "redirect:/admin/view/subscriptions";
        }
        
        
        @GetMapping ("/view/userData")
        public String viewUserData(Model model) {
        	
        	List<User> user= userDao.findAll();
            List<Company> companies = companyDao.findAll();

            Map<Long, Long> userCounts = new HashMap<>();
            for (Object[] row : userDao.countUsersByCompany()) {
                Long companyId = (Long) row[0];
                Long count = (Long) row[1];
                userCounts.put(companyId, count);
            }

            model.addAttribute("companies", companies);
            model.addAttribute("userCounts", userCounts);
            model.addAttribute("user", user);
        	
        	return "admin/userdata";
        }
        
        
        @GetMapping("/users.csv")
        public void downloadUsersCsv(
                @RequestParam(value = "companyId", required = false) Long companyId,
                HttpServletResponse response
        ) throws Exception {

            List<User> users = (companyId == null)
                    ? userDao.findAllByOrderByCompany_NameAscLastnameAscFirstnameAsc()
                    : userDao.findByCompanyIdOrderByLastnameAscFirstnameAsc(companyId);

            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            response.setContentType("text/csv");
            response.setHeader("Content-Disposition", "attachment; filename=\"users.csv\"");

            try (PrintWriter w = response.getWriter()) {
                // Header
                w.println("company_id,company_name,user_id,first_name,last_name,email,username,role,phone");

                for (User u : users) {
                    Company c = u.getCompany();
                    String companyIdVal = (c != null && c.getId() != null) ? c.getId().toString() : "";
                    String companyNameVal = (c != null && c.getName() != null) ? c.getName() : "";

                    w.printf("%s,%s,%s,%s,%s,%s,%s,%s,%s%n",
                            csv(companyIdVal),
                            csv(companyNameVal),
                            csv(u.getId() != null ? u.getId().toString() : ""),
                            csv(u.getFirstname()),
                            csv(u.getLastname()),
                            csv(u.getEmail()),
                            csv(u.getUsername()),
                            csv(u.getRole() != null ? u.getRole().name() : ""),
                            csv(u.getPhoneNumber())
                    );
                }
                w.flush();
            }
        }

        /** Minimal CSV escaping */
        private String csv(String s) {
            if (s == null) return "";
            String cleaned = s.replace("\"", "\"\"");
            if (cleaned.contains(",") || cleaned.contains("\n") || cleaned.contains("\r")) {
                return "\"" + cleaned + "\"";
            }
            return cleaned;
        }
        
        
        
     //customer service and feedback forums
        
        @GetMapping("/view/customerservice")
		public String viewCustomerServiceRequests(Model model) {

			List<AppCustomerServ> serv = appCustomerServDao.findAll();

			model.addAttribute("serv", serv);
			return "admin/app-forums/customerservice";
		}
        

@GetMapping("/customer/service/update")
public String toggleStatus(@RequestParam Long id) {
    // Fetch service by id, toggle status, save
     AppCustomerServ service = appCustomerServDao.findById(id).orElseThrow(()
    		       -> new IllegalArgumentException("Invalid service ID"));
    
     service.setIsResolved(true);
     appCustomerServDao.save(service);
    
    return "redirect:/admin/view/customerservice";
}


       @GetMapping("/customer/service/delete")
         public String removeServiceTicket (@RequestParam Long id) {
 
          AppCustomerServ service = appCustomerServDao.findById(id).orElseThrow(()
    		       -> new IllegalArgumentException("Invalid service ID"));
    
         appCustomerServDao.delete(service);
    
         return "redirect:/admin/view/customerservice";
}

        
        
        @GetMapping("/customer-service")
        public String customerSupport(Model model) {
        	
        	
            return "admin/app-forums/customer-service-form";
        }
        

       @PostMapping("/customerservice/submit")
       public String submitCustomerServiceForm(
        @RequestParam("name") String name,
        @RequestParam("email") String email,
        @RequestParam("subject") String subject,
        @RequestParam("message") String message,
        Model model
) {
    	   AppCustomerServ serv= new AppCustomerServ();
    	   serv.setTitle(subject);
    	   serv.setName(name);
    	   serv.setEmail(email);
    	   serv.setDescription(message);
    	   serv.setDate(LocalDate.now());
    	   
    	   appCustomerServDao.save(serv);
  
    model.addAttribute("message", "Thank you for contacting customer service!");
    return "admin/app-forums/customer-service-thankyou";
}
        
        
        @GetMapping("/feedback")
		public String feedback(Model model) {

			return "admin/app-forums/feedback-form";
		}
        

       @PostMapping("/feedback/submit")
        public String submitFeedback( @RequestParam("navigation") String navigation,
                                      @RequestParam("performance") String performance,
                                      @RequestParam("design") String design,
                                      @RequestParam("bugs") String bugs,
                                      @RequestParam(value="email", required=false) String email,
                                      @RequestParam(value="name", required=false) String name,
                                      @RequestParam("recommend") String recommend,
                                      @RequestParam(value = "comments", required = false) String comments,
                                        Model model
) {
    	   
    	   AppFeedback feedback= new AppFeedback();
    	   feedback.setNavigationExperience(navigation);
    	   feedback.setPerformanceExperience(performance);
    	   feedback.setDesignExperience(design);
    	   feedback.setMalfunctionExperience(bugs);
    	   feedback.setRecommendation(recommend);
    	   feedback.setComments(comments);
    	   feedback.setEmail(email);
    	   feedback.setUserName(name);
    	   feedback.setUploadedAt(Instant.now());
    	   
           appFeedbackDao.save(feedback);

           model.addAttribute("message", "Thank you for your feedback!");
           return "admin/app-forums/feedback-thankyou";
}
       
       
       @GetMapping("/view/feedback")
               public String viewFeedback(Model model) {
    	   List<AppFeedback> feedback = appFeedbackDao.findAll();
    	   model.addAttribute("feedbacklist", feedback);
    	   return "admin/app-forums/feedback";
       }
       
       
       
       
       @GetMapping("/view/payments")
		public String viewPayments(Model model) {

			List<RentPayment> pay = paymentDao.findAllBySourceIn(
					List.of(RealEstateApp.Pojo.PaymentSource.PAYPAL, RealEstateApp.Pojo.PaymentSource.STRIPE));
			
			model.addAttribute("payments", pay);
			return "admin/financial/payments";
		}
       
       @PostMapping("/view/payments/filter")
       public String filterPayments(@RequestParam("companyId") Long companyId,
                                     Model model) {
    	      List<RentPayment> pay;
			            if (companyId != null) {
							pay = paymentDao.findAllByCompanyIdAndSourceIn(companyId, List.of(
									RealEstateApp.Pojo.PaymentSource.PAYPAL, RealEstateApp.Pojo.PaymentSource.STRIPE));
							} else {
								pay = paymentDao.findAllBySourceIn(
										List.of(RealEstateApp.Pojo.PaymentSource.PAYPAL, RealEstateApp.Pojo.PaymentSource.STRIPE));
								
							}
			            
			            
			            
							model.addAttribute("payments", pay);
							return "admin/financial/payments";
       }
       
       
}
