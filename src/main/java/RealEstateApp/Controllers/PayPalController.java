package RealEstateApp.Controllers;



import com.fasterxml.jackson.databind.ObjectMapper;


import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.Transaction;
import com.paypal.base.rest.PayPalRESTException;


import RealEstateApp.Pojo.ApprovalResponse;
import RealEstateApp.Pojo.Order;
import RealEstateApp.Pojo.PaymentMethod;
import RealEstateApp.Pojo.PaymentSource;
import RealEstateApp.Pojo.PaymentStatus;
import RealEstateApp.Pojo.RentPayment;
import RealEstateApp.Pojo.TenantProfile;
import RealEstateApp.Pojo.User;

import RealEstateApp.Service.PayPalService;
import RealEstateApp.dao.RentPaymentDao;
import jakarta.servlet.http.HttpSession;
import RealEstateApp.dao.TenantProfileDao;
import RealEstateApp.dao.UserDao;

@Controller
public class PayPalController {

	
	private final PayPalService  payPalService;
	private final UserDao userDao;
	private RentPaymentDao rentPaymentDao;
	private final HttpSession session;
	private final TenantProfileDao tenantProfileDao;
	public static final String SUCCESS_URL = "pay/success";
	public static final String CANCEL_URL = "pay/cancel";
	
	//extenedd constructor for object mapper and paypal client
	  private final ObjectMapper objectMapper;
     
	
	
	public PayPalController( PayPalService payPalService, 
			       UserDao userDao,
			       RentPaymentDao rentPaymentDao, 
			       TenantProfileDao tenantProfileDao,
			      
			       ObjectMapper objectMapper,
			        HttpSession session) {
		
		this.tenantProfileDao=  tenantProfileDao;
		this.session = session;
		this.rentPaymentDao= rentPaymentDao;
		this.payPalService= payPalService;
		this.userDao= userDao;
		 this.objectMapper = objectMapper;
      
	}
	
	
	
	@GetMapping("/paymenthome")
	public String showPayPalHome() {
		
		return      "paymentform";
	}
	

	@RequestMapping("/tenant/paypal-checkout")
	public String payment( @RequestParam("amount") BigDecimal price,
			               @RequestParam("currency") String currency,
			               @RequestParam("method") String method,
			               @RequestParam("intent") String intent,
			               @RequestParam("description") String description
			) {
			    Order order = new Order();
			    order.setPrice(price);
			    order.setCurrency(currency);
			    order.setMethod(method);
			    order.setIntent(intent);
			    order.setDescription(description);
		
		try {
			Payment payment = payPalService.createPayment(order.getPrice(), order.getCurrency(), order.getMethod(),
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
		
		return  "paymentresponse";
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
			
			TenantProfile tp = tenantProfileDao.findById(currentUser.getId()).orElse(null);
	    	
	    	try {
	            Payment payment = payPalService.executePayment(paymentId, payerId);
	            System.out.println(payment.toJSON());
	            System.out.println(payment.getTransactions().get(0).getAmount().getTotal());
	            System.out.println(payment.getUpdateTime());
	            System.out.println(tp.getProperty().getRentalAddress());
	            
	            //convert amount to BigDecimal
	            String amountStr = payment.getTransactions().get(0).getAmount().getTotal();
	            BigDecimal amount = new BigDecimal(amountStr);
	            
	           
	           RentPayment ph= new RentPayment();
	           ph.setPaymentDate(OffsetDateTime.parse(payment.getUpdateTime()).toLocalDate());
	           ph.setAmountPaid( amount);
	           ph.setAmountDue( tp.getProperty().getRentAmount());
	           ph.setProperty(tp.getProperty());
	           ph.setUser(tp.getUser());
	           ph.setCompany(user.getCompany());
	           ph.setCurrency( payment.getTransactions().get(0).getAmount().getCurrency());
	           ph.setMonthBalanceDue(tp.getProperty().getRentAmount().subtract(amount));
	           ph.setMethod(PaymentMethod.PAYPAL);
	           ph.setSource(PaymentSource.PAYPAL);
	           ph.setStatus(PaymentStatus.PAID);
	           ph.setNotes(payment.getTransactions().get(0).getDescription());
	           ph.setTransactionId(payment.getId());
	           
	           
	           
	           
	           rentPaymentDao.save(ph);
	           
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
	
