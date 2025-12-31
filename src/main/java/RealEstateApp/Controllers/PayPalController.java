package RealEstateApp.Controllers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.Transaction;
import com.paypal.base.rest.PayPalRESTException;

import RealEstateApp.Pojo.ApprovalResponse;
import RealEstateApp.Pojo.Order;
import RealEstateApp.Pojo.PaymentHistory;
import RealEstateApp.Pojo.User;
import RealEstateApp.Service.PayPalService;
import RealEstateApp.dao.PaymentHistoryDao;
import RealEstateApp.dao.UserDao;

@Controller
public class PayPalController {

	@Autowired
	private PayPalService  service;
	
	@Autowired
	UserDao userDao;
	
	@Autowired
	private PaymentHistoryDao payHistoryDao;
	
	@Autowired
	HttpSession session;
	
	public static final String SUCCESS_URL = "pay/success";
	public static final String CANCEL_URL = "pay/cancel";
	
	
	
	@GetMapping("/paymenthome")
	public String showPayPalHome() {
		
		return "paymentform";
	}
	
	
	@PostMapping("/pay")
	public String payment(@ModelAttribute("order") Order order) {
	
		
		try {
			Payment payment = service.createPayment(order.getPrice(), order.getCurrency(), order.getMethod(),
					order.getIntent(), order.getDescription(), "http://localhost:8080/" + CANCEL_URL,
					"http://localhost:8080/" + SUCCESS_URL);
			for(Links link:payment.getLinks()) {
				if(link.getRel().equals("approval_url")) {
					return "redirect:"+link.getHref();
				}
				
			}
			
		}catch(PayPalRESTException e) {
			
			e.printStackTrace();
		}
		
		return  "redirect:/";
	}
	
	 @GetMapping(value = CANCEL_URL)
	    public String cancelPay(Model model) {
		 String cancel= "cancel";
		 
		 model.addAttribute("message", cancel);
		 
	        return "paymentresponse";
	    }

	    @GetMapping(value = SUCCESS_URL)
	    public String successPay(Model model, @RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId) {
	    	
			User currentUser= (User)session.getAttribute("user");
			Long id =  currentUser.getId();
			User user= userDao.findUserById(id);
	    	
	    	try {
	            Payment payment = service.executePayment(paymentId, payerId);
	            System.out.println(payment.toJSON());
	            System.out.println(payment.getTransactions().get(0).getAmount().getTotal());
	            System.out.println(payment.getUpdateTime());
	            System.out.println(user.getProperty().getRentAddress());
	           
	           PaymentHistory ph= new PaymentHistory();
	           ph.setTime(PaymentHistory.simpleDate(payment.getUpdateTime()));
	           ph.setAmount(payment.getTransactions().get(0).getAmount().getTotal());
	           ph.setProperty(user.getProperty().getRentAddress());
	           payHistoryDao.save(ph);
	           
	            if (payment.getState().equals("approved")) {
	            	String success= "success";
	            	model.addAttribute("message", success);
	                return "paymentresponse";
	            }
	        } catch (PayPalRESTException e) {
	         System.out.println(e.getMessage());
	        }
	        return "redirect:/";
	    }
	
}
