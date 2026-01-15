package RealEstateApp.Controllers;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import RealEstateApp.Pojo.Company;
import RealEstateApp.Pojo.User;
import jakarta.servlet.http.HttpSession;

@ControllerAdvice
public class GlobalModelAdvice {

	
	  @ModelAttribute("dashboardUrl")
	  public String dashboardUrl(HttpSession session) {
	    User u = (User) session.getAttribute("user");
	    if (u == null || u.getRole() == null) return "/login";

	    return switch (u.getRole()) {
	      case TENANT -> "/tenant/dashboard";
	      case LANDLORD, EMPLOYEE -> "/landlord/dashboard";
	      case ADMIN -> "/admin/dashboard";
	    };
	  }
	  
	  @ModelAttribute("currentUser")
	  public User currentUser(HttpSession session) {
	    Object obj = session.getAttribute("user");
	    return (obj instanceof User) ? (User) obj : null;
	  }

	  @ModelAttribute("company")
	  public Company company(HttpSession session) {
	    Object obj = session.getAttribute("user");
	    if (!(obj instanceof User u)) return null;
	    return u.getCompany();
	  }
}
