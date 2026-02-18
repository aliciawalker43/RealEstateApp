package RealEstateApp.Controllers;


	import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.List;

import jakarta.servlet.http.HttpSession;

	import org.springframework.beans.factory.annotation.Autowired;
	import org.springframework.stereotype.Controller;
	import org.springframework.ui.Model;
	import org.springframework.web.bind.annotation.PostMapping;
	import org.springframework.web.bind.annotation.RequestMapping;
	import org.springframework.web.bind.annotation.RequestParam;
	import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import RealEstateApp.Pojo.User;
import RealEstateApp.Pojo.Company;
import RealEstateApp.Pojo.Property;
import RealEstateApp.Pojo.Role;
import RealEstateApp.dao.CompanyDao;
import RealEstateApp.dao.PropertyDao;

import RealEstateApp.dao.UserDao;

	@Controller
	public class UserController {

		
		private final UserDao userDao;
		private final CompanyDao companyDao;
		private final PropertyDao propDao;
        private final HttpSession session;
		
		
		public UserController(PropertyDao propDao, UserDao userDao, CompanyDao companyDao, HttpSession session) {
			this.propDao = propDao;
			this.userDao = userDao;
			this.companyDao = companyDao;
			this.session = session;
		}	

		// SIGNUP METHODS
		
		@RequestMapping("/signup")
		public String signUpTenant( @RequestParam("token") String token,
				                    @RequestParam("company") Long companyId,
				                    @RequestParam("role") String role, 
				                    Model model) {
			
			System.out.println("token: " + token);
			System.out.println("company id: " + companyId);
			
			model.addAttribute("token", token);
			model.addAttribute("company", companyDao.findCompanyById(companyId));
			model.addAttribute("role", role);
			return("auth/accept-invite");
		}
		
		
		@PostMapping("/invite/signup")
		public String submitSignup(User user,@RequestParam("passwordConfirm") String passwordConfirm, 
				@RequestParam("password") String password,@RequestParam("firstname") String name1,
				@RequestParam("lastname") String name2,
				@RequestParam("email") String email,
				@RequestParam("username") String username, 
				@RequestParam("role") String role,
				@RequestParam("company")Long companyId,
				Model model) {

			User existingUser = userDao.findByUsername(username);
			if (existingUser != null) {
				model.addAttribute("error", "That username already exists. Please choose a different username.");

				return "auth/accept-invite";

			}
			
			
			if (!passwordConfirm.contentEquals(password)) {
				model.addAttribute("error", "The passwords you entered do not match. Please try again.");
				return "auth/accept-invite";
			}
			if (passwordConfirm.contentEquals(password)) {

			String encodePass = Base64.getEncoder().encodeToString(password.getBytes());
			user.setPasscode(encodePass);
		
		    
		    user.setFirstname(name1);
		    user.setLastname(name2);
		    user.setEmail(email);
		    user.setUsername(username);
		    user.setRole(Role.valueOf(role));
		    Company company = companyDao.findCompanyById(companyId);
		    user.setCompany(company);
		        
		
			userDao.save(user);
			
			session.setAttribute("user", user);
			model.addAttribute("message", "Welcome, Thank You for Signing Up.");
			
			}
			return "/tenant/dashboard";
			
		}

		
		// LOGIN METHODS
		

		@PostMapping("/login")
		public String submitLogin(@RequestParam("username") String username, @RequestParam("password") String password,
				Model model, RedirectAttributes redirect) {
			User user = userDao.findByUsername(username);
       //System.out.println("user"+ user);
			if (user == null) {
				model.addAttribute("message", "Incorrect username. Please try again.");
				return "homelogin";
			}
			byte[] decodeByte = Base64.getDecoder().decode(user.getPasscode());
			String decodePass = new String(decodeByte);

			//System.out.println("decodePass: " + decodePass);
			if (!password.contentEquals(decodePass)) {
				model.addAttribute("message", "Incorrect password. Please try again.");
				return "homelogin";
			}
			session.setAttribute("user", user);
			
			try { if(user.getRole() == Role.ADMIN){
				//App Only access level
				return "redirect:/admin/dashboard";
			}
			}catch(NullPointerException e) {
				
			}
			try { if(user.getRole() == Role.EMPLOYEE) {
				//employee access level
				return "redirect:/employee/dashboard";
            }
                
			}catch(NullPointerException e) {
			
			}
			try { if(user.getRole() == Role.LANDLORD) {
				//landlord(company) access level
				return "redirect:/landlord/dashboard";
			}
			}catch(NullPointerException e) {
				
			}
			 {
		    return "redirect:/tenant/dashboard";
		}
		}
		
		
		
		// LOGOUT METHOD
		@RequestMapping("/logout")
		public String logout(RedirectAttributes redirect) {
			session.invalidate();
			redirect.addFlashAttribute("message", "You are now logged out.");
			
			return "redirect:/homelogin";
		}

		//Forgot Password Methods
		
		@RequestMapping("/forgot-password")
		public String forgotPassword() {
			return "forgot-password";
		}
		
		@PostMapping("/forgot-password")
		public String submitForgotPassword(@RequestParam("email") String email, Model model) {
			User user = userDao.findByEmail(email);
			
			if (user == null) {
				model.addAttribute("message", "No account found with that email address. Please try again.");
				return "forgot-password";
	             }
			
			model.addAttribute("user", user);
			return "update-password";
		}
		
		
		
		@PostMapping("/update-password")
		public String submitUpdatePassword(@RequestParam("password") String password,
				@RequestParam("passwordConfirm") String passwordConfirm, @RequestParam("userId") Long userId,
				Model model) {

			User user = userDao.findUserById(userId);

			if (!password.contentEquals(passwordConfirm)) {
				model.addAttribute("message", "The passwords you entered do not match. Please try again.");
				model.addAttribute("user", user);
				return "update-password";
			}

			String encodePass = Base64.getEncoder().encodeToString(password.getBytes());
			user.setPasscode(encodePass);
			userDao.save(user);

			model.addAttribute("message", "Your password has been successfully updated. You may now log in.");
			return "homelogin";
		}
	}
	
