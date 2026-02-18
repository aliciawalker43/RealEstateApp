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

import RealEstateApp.dao.PropertyDao;
import RealEstateApp.dao.UserDao;
import RealEstateApp.Pojo.Expense;
import RealEstateApp.Pojo.RentPayment;
import RealEstateApp.Pojo.Property;

import com.paypal.api.payments.Payment;


@Controller
public class RealEstateAppController {



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
	

	
	

	   
	 
	   
	



}