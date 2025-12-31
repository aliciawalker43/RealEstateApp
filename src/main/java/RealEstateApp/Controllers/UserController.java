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
import RealEstateApp.Pojo.Property;
import RealEstateApp.Pojo.Role;
import RealEstateApp.dao.PropertyDao;

import RealEstateApp.dao.UserDao;

	@Controller
	public class UserController {

		@Autowired
		UserDao userDao;
		@Autowired
		private PropertyDao propDao;


		@Autowired
		HttpSession session;

		// SIGNUP METHODS
		
		@RequestMapping("/signup")
		public String signUpHome() {
			
			return("newmember");
		}
		
		@PostMapping("/signup")
		public String submitSignup(User user,@RequestParam("passwordConfirm") String passwordConfirm, 
				@RequestParam("passcode") String password,@RequestParam("firstname") String name1,
				@RequestParam("lastname") String name2,
				@RequestParam("email") String email,
				@RequestParam("username") String username, Model model) {

			User existingUser = userDao.findByUsername(username);
			if (existingUser != null) {
				model.addAttribute("message", "That username already exists. Please choose a different username.");

				return "newmember";

			}
			
			
			if (!passwordConfirm.contentEquals(password)) {
				model.addAttribute("message", "The passwords you entered do not match. Please try again.");
				return "newmember";
			}
			if (passwordConfirm.contentEquals(password)) {

			String encodePass = Base64.getEncoder().encodeToString(password.getBytes());
			user.setPasscode(encodePass);
		
		
			userDao.save(user);
			
			session.setAttribute("user", user);
			model.addAttribute("message", "Thank You for Signing Up.");
			
			}
			return "redirect:/index";
			
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

			if (!password.contentEquals(decodePass)) {
				model.addAttribute("message", "Incorrect password. Please try again.");
				return "homelogin";
			}
			session.setAttribute("user", user);
			
			try { if(user.getRole() == Role.ADMIN){
				//employee access level
				return "redirect:/admin/dashboard";
			}
			}catch(NullPointerException e) {
				
			}
			try { if(user.getRole() == Role.LANDLORD) {
				//employee access level
				return "redirect:/landlord/dashboard";
			}
			}catch(NullPointerException e) {
				
			}
		    return "redirect:/tenant/dashboard";
		}
		
		// LOGOUT METHOD
		@RequestMapping("/logout")
		public String logout(RedirectAttributes redirect) {
			session.invalidate();
			redirect.addFlashAttribute("message", "You are now logged out.");
			
			return "redirect:/index";
		}

		@RequestMapping("/updateinfo")
		public String updateUserForm() {
			
			return "userprofileupdate";
		}
		
		
		@RequestMapping("/updateProfile")
		public String updateProfile(Model model, @RequestParam ("username") String username,
				@RequestParam ("email") String email,
				@RequestParam ("password") String password,
				@RequestParam ("password2") String password2,
				RedirectAttributes redirect) {
			
			User currentUser= (User)session.getAttribute("user");
			Long id =  currentUser.getId();
			User user= userDao.findUserById(id);
			
			System.out.println(user);
			if (!password2.contentEquals(password)) {
				model.addAttribute("message", "The passwords you entered do not match. Please try again.");
				return "userprofileupdate";
			}
			if (password2.contentEquals(password)) {

			String encodePass = Base64.getEncoder().encodeToString(password.getBytes());
			user.setPasscode(encodePass);
			
			}
			user.setEmail(email);
			user.setUsername(username);
			
			userDao.save(user);
			session.setAttribute("user", user);
			model.addAttribute("user", user);
			return "userprofile";
	
	}
		
		@RequestMapping("/viewprofile")
		public String viewprofile(Model model) {
			User currentUser= (User)session.getAttribute("user");
			Long id =  currentUser.getId();
			User user= userDao.findUserById(id);
			
			//if(user.getAccessStatus() == "level2") {
		           
				model.addAttribute("user", user);
				return("staffprofile");
		//	}else 
		
			//model.addAttribute("user", user);
		//	return("userprofile");
		}
		
		@RequestMapping("/makepayment")
		public String showPaymentForm(Model model) {
			User currentUser= (User)session.getAttribute("user");
			Long id =  currentUser.getId();
			User user= userDao.findUserById(id);
			
			LocalDateTime myObj = LocalDateTime.now();
			DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			String formattedDate = myObj.format(myFormatObj);
			
			//Check if Date is after due date, if true add late fee
			// double amount= Property.amountDue(user, myObj);
			 
			 
		    // look for prior balance due
			//if partial payment created, deduct from total and add to past due
		    
		
			model.addAttribute("user", user);
			//model.addAttribute("amount", amount);
			model.addAttribute("date", formattedDate);
			
			
			
			return "paymentform";
		}
		
		
		@RequestMapping("/payAmount")
		public String chooseAmount(Model model, @RequestParam ("price") String price) {
			User currentUser= (User)session.getAttribute("user");
			Long id =  currentUser.getId();
			User user= userDao.findUserById(id);
			
			System.out.println(price);
	    	double amount= Property.convertAmount(price);
				
			//System.out.println(amount);
			model.addAttribute("user", user);
			model.addAttribute("amount", amount);
			
			return "paymethod";
		}
	}
	
	
	
