package RealEstateApp.Controllers;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import RealEstateApp.dao.PropertyDao;
import RealEstateApp.dao.UserDao;
import jakarta.servlet.http.HttpSession;
import RealEstateApp.Pojo.Conversation;
import RealEstateApp.Pojo.EmployeeProfile;
import RealEstateApp.Pojo.TenantProfile;
import RealEstateApp.Pojo.User;
import RealEstateApp.Service.MessagingService;
import RealEstateApp.dao.CompanyDao;
import RealEstateApp.dao.ConversationDao;
import RealEstateApp.dao.EmployeeProfileDao;
import RealEstateApp.dao.MaintenanceRequestDao;
import RealEstateApp.dao.MessageDao;
import RealEstateApp.dao.PaymentDao;

@Controller
@RequestMapping("/landlord")
public class LandlordDashboardController {

	private final UserDao userDao;
    private final CompanyDao companyDao;
    private final PropertyDao propertyDao;
    private final PaymentDao paymentDao;
    private final MaintenanceRequestDao maintenanceRequestDao;
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
                                    EmployeeProfileDao employeeProfileDao) {
    	this.userDao= userDao;
    	this.employeeProfileDao= employeeProfileDao;
    	this.companyDao = companyDao;
        this.propertyDao = propertyDao;
        this.paymentDao= paymentDao;
        this.maintenanceRequestDao = maintenanceRequestDao;
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
		   
		model.addAttribute("user", user);    
		model.addAttribute("employeeProfile", employeeProfile);
        model.addAttribute("companyCount", companyDao.count());
        model.addAttribute("propertyCount", propertyDao.count());
        model.addAttribute("paymentCount", paymentDao.count());
        model.addAttribute("openMaintenanceCount", maintenanceRequestDao.count());
        return "admin/dashboard";
    }
    
    
    @GetMapping("/messages")
    public String companyInbox(Model model, HttpSession session) {
        User staff = (User) session.getAttribute("user");
        if (staff == null) return "redirect:/login";

        // only company users should access (LANDLORD/EMPLOYEE)
        // if (staff.getRole() == Role.TENANT) return "redirect:/tenant/dashboard";

        Long companyId = staff.getCompany().getId();
        model.addAttribute("conversations", conversationDao.findByCompanyIdOrderByCreatedAtDesc(companyId));
        return "landlord/inbox"; // templates/company/inbox.html
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
}