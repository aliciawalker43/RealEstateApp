package RealEstateApp.Controllers;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import RealEstateApp.Pojo.Company;
import RealEstateApp.Pojo.Conversation;
import RealEstateApp.Pojo.ImageAsset;
import RealEstateApp.Pojo.ImageCategory;
import RealEstateApp.Pojo.MaintenanceRequest;
import RealEstateApp.Pojo.MaintenanceRequestImage;
import RealEstateApp.Pojo.MaintenanceStatus;
import RealEstateApp.Pojo.PaymentHistory;
import RealEstateApp.Pojo.Property;
import RealEstateApp.Pojo.TenantProfile;
import RealEstateApp.Pojo.User;
import RealEstateApp.Service.ImageUploadService;
import RealEstateApp.Service.MessagingService;
import RealEstateApp.dao.ConversationDao;
import RealEstateApp.dao.DocumentDao;
import RealEstateApp.dao.ImageAssetDao;
import RealEstateApp.dao.MaintenanceRequestDao;
import RealEstateApp.dao.MaintenanceRequestImageDao;
import RealEstateApp.dao.MessageDao;
import RealEstateApp.dao.PaymentDao;
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
    private final PaymentDao paymentDao;
    private final DocumentDao documentDao;
    private final MaintenanceRequestDao maintenanceRequestDao;
    private final MessageDao messageDao;
    private final ImageUploadService imageUploadService;
    private final MessagingService messagingService;
    private final ConversationDao conversationDao;
    private final MaintenanceRequestImageDao maintenanceRequestImageDao;
    //private final PaymentHistory paymentHistoryDao;

    public TenantDashboardController(PaymentDao paymentDao, DocumentDao documentDao,
                                     MaintenanceRequestDao maintenanceRequestDao,
                                     MessageDao messageDao, UserDao userDao, 
                                     ImageUploadService imageUploadService,
                                     ImageAssetDao imageAssetDao,
                                     MessagingService messagingService,
                                     ConversationDao conversationDao,
                                     TenantProfileDao tenantProfileDao,
                                     MaintenanceRequestImageDao maintenanceRequestImageDao) {
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
        this.maintenanceRequestImageDao = maintenanceRequestImageDao;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, HttpSession session) {
    	
    	User currentUser= (User)session.getAttribute("user");
		Long userId =  currentUser.getId();
		 User user = userDao.findById(userId)
	                .orElseThrow(() -> new IllegalStateException("User not found"));
		 
		    // ✅ Add tenantProfile (even if not found)
		    TenantProfile tenantProfile = tenantProfileDao.findById(user.getId()).orElse(null);
		   
		 
		model.addAttribute("tenantProfile", tenantProfile);
    	model.addAttribute("user", user);
        model.addAttribute("payments", paymentDao.findByUser(user));
        model.addAttribute("documents", documentDao.findByUser(user));
        model.addAttribute("requests", maintenanceRequestDao.findByUser(user));
        model.addAttribute("messageCount", messageDao.count());
        return "tenant/dashboard";
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
                                 @RequestParam(value="error", required=false) String error) {

        User tenant = (User) session.getAttribute("user");
        if (tenant == null) return "redirect:/login";

        // tenant must be tenant role (optional)
        // if (tenant.getRole() != Role.TENANT) return "redirect:/company/dashboard";

        Conversation convo = messagingService.getOrCreateConversation(tenant.getCompany(), tenant);

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
    public String newRequestForm() {
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
      req.setStatus(MaintenanceStatus.OPEN);

      maintenanceRequestDao.save(req);

      // Upload images (optional)
      if (images != null) {
        int count = 0;
        for (MultipartFile file : images) {
          if (file == null || file.isEmpty()) continue;
          count++;
          if (count > 5) break; // simple limit

          // Use category for maintenance images
          ImageAsset asset = imageUploadService.upload(
              file,
              ImageCategory.MAINTENANCE,   // add this enum value
              req.getId(),                 // contextId ties image to request
              session
          );

          MaintenanceRequestImage link = new MaintenanceRequestImage();
          link.setRequest(req);
          link.setImageAsset(asset);
          maintenanceRequestImageDao.save(link);
        }
      }

      ra.addFlashAttribute("success", "Maintenance request submitted.");
      return "redirect:/tenant/dashboard";
    }
    
    
    
 
}
