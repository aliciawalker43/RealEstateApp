package RealEstateApp;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


import RealEstateApp.Pojo.User;

import RealEstateApp.dao.UserDao;

@Controller
public class RealEstateAppController {


	@Autowired
	private UserDao userDao;
	@Autowired
	HttpSession session;
	
	
	
	@RequestMapping("/")
	public String showLogin() {
		return "homelogin";
	}
	
	@RequestMapping("/index")
	public String showLoginHome(Model model) {
		User currentUser= (User)session.getAttribute("user");
		Long id =  currentUser.getId();
		List<User> user= userDao.findAllById(id);
		model.addAttribute("user", user);
		
		return "tenanthomepage";
	}
	
	@RequestMapping("/index2")
	public String showEmployeeHome(Model model) {
		User currentUser= (User)session.getAttribute("user");
		Long id =  currentUser.getId();
		List<User> user= userDao.findAllById(id);
		model.addAttribute("user", user);
		
		List<User> comment= userDao.findAll();
		model.addAttribute("comment", comment);
		
		return "employeehome";
	}
	
	
	
	@PostMapping("/comments")
	public String addComments(Model model, @RequestParam ("comments") String comments) {
		User current = (User)session.getAttribute("user");
		current.setComments(comments);
		userDao.save(current);
		return "redirect:index";
	}
	
	
		 
		
		
	
	/*@RequestMapping("/createTask")
	public String addTask(Task task) {
		taskDao.save(task);
		return ("redirect:usertask");
	}
	
	@PostMapping("/deletetask")
	public String deleteTask(Task task) {
	taskDao.delete(task);
	return ("Redirect:/usertask");*/
}
