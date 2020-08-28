package GC.Capstone6;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import GC.Capstone6.Pojo.Task;
import GC.Capstone6.Pojo.User;
import GC.Capstone6.dao.TaskDao;
import GC.Capstone6.dao.UserDao;

@Controller
public class TaskController {

	@Autowired
	private TaskDao taskDao;
	@Autowired
	private UserDao userDao;
	
	@RequestMapping("/")
	public String showHome() {
		return ("redirect:/homelogin");
	}
	@RequestMapping("/homelogin")
	public String showRealHome() {
		return ("homelogin");
	}
	
	@RequestMapping("/login")
	public String showUserHome(Model model,@RequestParam ("name") String name,
			@RequestParam ("passcode")String passcode) {
		   
		User user= userDao.findByUsername(name);
		
		
		if (user.getPasscode().equalsIgnoreCase(passcode)) {
			
			Set<Task> task= user.getTask();
			model.addAttribute("task", task);
			return("usertask");
		}else {
			String msg= "Wrong password!";
			model.addAttribute("msg", msg);
			return ("redirect:/");
		}
		 
		
		
	}
	@RequestMapping("/createTask")
	public String addTask(Task task) {
		taskDao.save(task);
		return ("redirect:");
	}
}
