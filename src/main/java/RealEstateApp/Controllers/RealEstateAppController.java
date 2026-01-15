package RealEstateApp.Controllers;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.http.HttpSession;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import RealEstateApp.Pojo.User;
import RealEstateApp.dao.ExpenseDao;
import RealEstateApp.dao.PaymentHistoryDao;
import RealEstateApp.dao.PropertyDao;
import RealEstateApp.dao.UserDao;
import RealEstateApp.Pojo.Expense;
import RealEstateApp.Pojo.PaymentHistory;
import RealEstateApp.Pojo.Property;

import com.paypal.api.payments.Payment;


@Controller
public class RealEstateAppController {


	@Autowired
	private UserDao userDao;
	@Autowired
	private PropertyDao propDao;
	@Autowired
	private ExpenseDao expenseDao;
	@Autowired
	private PaymentHistoryDao payHistoryDao;
	
	@Autowired
	HttpSession session;
	
	
	
	@RequestMapping("/")
	public String showSalesPage() {
		return "admin/salesinfopage";
	}
	
	@RequestMapping("/homelogin")
	public String showLogin() {
		return "homelogin";
	}
	

	
	
	
	@RequestMapping("/user/update{id}")
	public String updateUserForm(Model model, @PathVariable("id") Long id) {
	
		List<User>user= userDao.findAllById(id);
		 System.out.println(user);
		 model.addAttribute("employee", user);
		// model.addAttribute("employeeid", id);
	return "updateuser";
	}
	
	@PostMapping("/user/update{id}")
	public String updateUser( @RequestParam("id") Long id,
			 @RequestParam ("hiredate") String hireDate,
			  @RequestParam ("payrate") Double payRate,
			   @RequestParam ("position") String position,
			   @RequestParam ("accessstatus") String accessStatus) {
	
		User use= userDao.findUserById(id);
	
	userDao.save(use);
	
	//System.out.println(use);
	return "redirect:/viewroster";
}
	
	
	@RequestMapping("/user/delete{id}")
	public String removeUser(@RequestParam("id") Long id) {
		userDao.deleteById(id);
		return "redirect:/viewroster";
	}
	
	@RequestMapping("/view/expense")
	public String viewExpensePage(Model model) {
		List<Expense> list= expenseDao.findAll();
		model.addAttribute("expense", list);
		return "expense";
	}
	
	@RequestMapping("/recordexpense")
	public String addExpense(Model model) {
		List<Property> prop= propDao.findAll(); 
		//System.out.println(prop);
		 model.addAttribute("propertys", prop);
		return "expenseform";
	}
	
	@PostMapping("/addexpense")
	public String recordExpense(Model model, Property prop, 
			@RequestParam ("property") Long id,
			@RequestParam ("expense") String expense,
			@RequestParam ("amount") BigDecimal amount,
			@RequestParam ("date") LocalDate date) {
		
		 prop= propDao.findPropertyById(id);
		Expense ex= new Expense();
		ex.setAmount(amount);
		ex.setExpenseType(expense);
		ex.setDate(date);
		 ex.setProperty(prop);
		
		 expenseDao.save(ex);
		 
		 List<Expense> list =expenseDao.findAll();
		// System.out.println(ex);
		//System.out.println(list);
		
		model.addAttribute("property", prop);
		model.addAttribute("expense", list);
		return "redirect:/view/expense";
	}
	
	@RequestMapping("/expense/delete{id}")
	public String deleteExpense(@RequestParam Long id) {
		expenseDao.deleteById(id);
		return "redirect:/view/expense";
	}
	

	
	

	
	   @RequestMapping("/property/update{id}")
		   public String updatePropertyForm(Model model, @RequestParam ("id") Long id) { 
				  
		   Property prop= propDao.findPropertyById(id);
		   List<User> user= userDao.findAll();
			
			model.addAttribute( "user", user);
	      model.addAttribute("property", prop);
		   return "updatepropertyform";
	   }
	   
	   
	 
	   
	
@RequestMapping("/calendar")
public String viewCalendar() {
	
	return "calendar";
}
// payment history for management
@RequestMapping("/view/payments")
public String viewPayments(Model model) {
	List <PaymentHistory> payHistory= payHistoryDao.findAll();
	
	
	model.addAttribute("payHistory", payHistory);
	return "paymenthistory";
}


}