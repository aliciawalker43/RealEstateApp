package RealEstateApp.Controllers;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import RealEstateApp.Pojo.Company;
import RealEstateApp.Pojo.EmailLog;
import RealEstateApp.Pojo.EmployeeProfile;
import RealEstateApp.Pojo.InvoiceEmailLog;
import RealEstateApp.Pojo.Property;
import RealEstateApp.Pojo.Role;
import RealEstateApp.Pojo.TenantProfile;
import RealEstateApp.Pojo.User;
import RealEstateApp.Service.EmailService;
import RealEstateApp.dao.EmailLogDao;
import RealEstateApp.dao.EmployeeProfileDao;
import RealEstateApp.dao.InvoiceEmailLogDao;
import RealEstateApp.dao.PropertyDao;
import RealEstateApp.dao.TenantProfileDao;
import RealEstateApp.dao.UserDao;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/landlord/email")
public class EmailController {

  private final UserDao userDao;
  private final EmailService emailService;
  private final PropertyDao propertyDao;
  private final EmailLogDao emailLogDao;
  private final TenantProfileDao tenantDao;
  private final EmployeeProfileDao employeeDao;
  

  public EmailController(UserDao userDao, 
		  EmailService emailService, 
		  PropertyDao propertyDao,
		 EmailLogDao emailLogDao,
		  EmployeeProfileDao employeeDao,
		  TenantProfileDao tenantDao
		  ) {
	  this. employeeDao= employeeDao;
	  this.tenantDao =tenantDao;
    this.userDao = userDao;
    this.emailService = emailService;
    this.propertyDao= propertyDao;
    this.emailLogDao=emailLogDao;
  }

  @GetMapping
  public String emailCenter(Model model, HttpSession session) {
    User currentUser = (User) session.getAttribute("user");
    if (currentUser == null || currentUser.getCompany() == null) return "redirect:/login";

    Long companyId = currentUser.getCompany().getId();
    
    model.addAttribute("recentEmails", emailLogDao.findAllByCompanyId(companyId));
    model.addAttribute("properties", propertyDao.findAllByCompany(currentUser.getCompany()));
    model.addAttribute("company", currentUser.getCompany());
    model.addAttribute("tenants", userDao.findUsersByCompanyIdAndRole(companyId, Role.TENANT));
    model.addAttribute("employees", userDao.findUsersByCompanyIdAndRole(companyId, Role.EMPLOYEE));

    return "landlord/emailcenter";
  }

  private static final Long ALL_EMPLOYEES = 0L;
  private static final Long ALL_TENANTS = 99999L;
  private static final Long ALL_USERS = 88888L;

  @PostMapping("/send")
  public String sendEmail(@RequestParam(value="audienceId", required =false) Long audienceId,
		                  @RequestParam(value = "customRecipient", required = false) String customRecipient,
                          @RequestParam(value="propertyId", required=false) Long propertyId,
                          @RequestParam("subject") String subject,
                          @RequestParam("message") String body,
                          HttpSession session,
                          RedirectAttributes ra) {

      User currentUser = (User) session.getAttribute("user");
      if (currentUser == null || currentUser.getCompany() == null) return "redirect:/login";

      Company company = currentUser.getCompany();
      Long companyId = company.getId();

      try {
    	    List<User> recipients = new ArrayList<>();
            Set<String> seen = new HashSet<>();
            int sentCount = 0;

            if (customRecipient != null && !customRecipient.isBlank()) {
                String[] emails = customRecipient.split(",");
                for (String email : emails) {
                    String to = email.trim().toLowerCase();
                    if (to.isBlank() || !to.contains("@")) continue; // basic validation
                    if (!seen.add(to)) continue;
                    emailService.sendCompanyEmail(to, subject, body);
                    sentCount++;
                }
            }else if (audienceId != null) {
    	  
          // Load lists
          List<User> tenants = userDao.findUsersByCompanyIdAndRole(companyId, Role.TENANT);
          List<User> employees = userDao.findUsersByCompanyIdAndRole(companyId, Role.EMPLOYEE);

          if (ALL_EMPLOYEES.equals(audienceId)) {
        	  recipients =  employees;

          } else if (ALL_TENANTS.equals(audienceId)) {
        	  recipients = tenants;

          } else if (ALL_USERS.equals(audienceId)) {
              recipients = new ArrayList<>();
              recipients.addAll(tenants);
              recipients.addAll(employees);

          } else {
              // Treat as individual user id
              User u = userDao.findById(audienceId)
                      .orElseThrow(() -> new IllegalArgumentException("Selected user not found"));

              // safety: ensure same company
              if (u.getCompany() == null || !companyId.equals(u.getCompany().getId())) {
                  throw new IllegalArgumentException("Selected user is not part of your company");
              }

              recipients = List.of(u);
          }

          for (User u : recipients) {
              if (u.getEmail() == null || u.getEmail().isBlank()) continue;
               String to = u.getEmail().trim().toLowerCase();
              if (!seen.add(to)) continue;
              emailService.sendCompanyEmail(to, subject, body); // ✅ send to recipient
              sentCount++;
              }
            }
          if (sentCount == 0) {
              throw new IllegalArgumentException("No recipients had a valid email address.");
          }
          // Log the email for Email Center view
          EmailLog log = new EmailLog();
          log.setCompany(company);
          log.setFrom(currentUser);
          log.setAudience(recipients);
          log.setSubject(subject);
          log.setBody(body);
          log.setSentAt(Instant.now());
          log.setRecipientCount(sentCount);
          
if (propertyId != null) {
    Property p = propertyDao.findById(propertyId).orElse(null);
    log.setProperty(p);
          }
           emailLogDao.save(log);
          ra.addFlashAttribute("success", "Email sent to " + sentCount + " recipient(s).");
          return "redirect:/landlord/email";
          
      } catch (Exception e) {
    	  ra.addFlashAttribute("error", e.getMessage());
          return "redirect:/landlord/email"; 
      }
  }
  
  //Pre-fill endpoints for quick send 
  
  @PostMapping("/prefill-invite")
  public String prefillTenantInvite(HttpSession session, Model model) {
      User currentUser = (User) session.getAttribute("user");
      if (currentUser == null || currentUser.getCompany() == null) return "redirect:/login";

      Long companyId = currentUser.getCompany().getId();
      String token = java.util.UUID.randomUUID().toString();
      String signupUrl = String.format(
          "https://yourdomain.com/signup?token=%s&company=%d&role=TENANT",
          token, companyId
      );

      String subject = "You're Invited to Create an Account as a Tenant";
      String body = "Hello,\n\nPlease use the following link to sign up as a tenant:\n" + signupUrl + "\n\nThank you!";

      model.addAttribute("prefillSubject", subject);
      model.addAttribute("prefillBody", body);

      return "landlord/emailcenter";
  }
  
  

}