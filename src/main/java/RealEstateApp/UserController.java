package RealEstateApp;


	import java.util.Base64;

import javax.servlet.http.HttpSession;

	import org.springframework.beans.factory.annotation.Autowired;
	import org.springframework.stereotype.Controller;
	import org.springframework.ui.Model;
	import org.springframework.web.bind.annotation.PostMapping;
	import org.springframework.web.bind.annotation.RequestMapping;
	import org.springframework.web.bind.annotation.RequestParam;
	import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import RealEstateApp.Pojo.User;
import RealEstateApp.dao.UserDao;

	@Controller
	public class UserController {

		@Autowired
		UserDao userDao;

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
			
			try { if(user.getAccessStatus().contains("level2")) {
				//employee access level
				return "redirect:/index2";
			}
			}catch(NullPointerException e) {
				
			}
		    return "redirect:/index";
		}
		
		// LOGOUT METHOD
		@RequestMapping("/logout")
		public String logout(RedirectAttributes redirect) {
			session.invalidate();
			redirect.addFlashAttribute("message", "You are now logged out.");
			
			return "redirect:/index";
		}

	}


