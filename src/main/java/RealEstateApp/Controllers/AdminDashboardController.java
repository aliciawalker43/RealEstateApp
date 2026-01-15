package RealEstateApp.Controllers;


import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import RealEstateApp.dao.PropertyDao;
import RealEstateApp.dao.UserDao;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import RealEstateApp.Pojo.Company;
import RealEstateApp.Pojo.CompanySubscription;
import RealEstateApp.Pojo.User;
import RealEstateApp.Service.ImageUploadService;
import RealEstateApp.Service.MessagingService;
import RealEstateApp.dao.CompanyDao;
import RealEstateApp.dao.CompanySubscriptionDao;
import RealEstateApp.dao.ConversationDao;
import RealEstateApp.dao.DocumentDao;
import RealEstateApp.dao.EmployeeProfileDao;
import RealEstateApp.dao.ImageAssetDao;
import RealEstateApp.dao.MaintenanceRequestDao;
import RealEstateApp.dao.MaintenanceRequestImageDao;
import RealEstateApp.dao.MessageDao;
import RealEstateApp.dao.PaymentDao;



@Controller
@RequestMapping("/admin")
public class AdminDashboardController {

    private final CompanyDao companyDao;
    private final PropertyDao propertyDao;
    private final PaymentDao paymentDao;
    private final CompanySubscriptionDao subscriptionDao;
    private final UserDao userDao;

    public AdminDashboardController(CompanyDao companyDao, PropertyDao propertyDao,
                                    PaymentDao paymentDao,
                                    MaintenanceRequestDao maintenanceRequestDao,
                                    CompanySubscriptionDao subscriptionDao,
                                    UserDao userDao
                                    ) {
        this.userDao= userDao;
    	this.companyDao = companyDao;
        this.propertyDao = propertyDao;
        this.paymentDao= paymentDao;
        this.subscriptionDao = subscriptionDao;
       
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, HttpSession session) {
    	
    	
    	
    	model.addAttribute("userCount",userDao.count());
        model.addAttribute("companyCount", companyDao.count());
        model.addAttribute("propertyCount", propertyDao.count());
        model.addAttribute("paymentCount", paymentDao.count());
        return "admin/dashboard";
    }
    
    
        @GetMapping ("/view/subscriptions")
        public String viewSubscriptions(Model model) {
        	
        	
        	List<Company> company= companyDao.findAll();
        	
        	model.addAttribute("company", company);
        	return "admin/subscriptions";
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
        
       
}
