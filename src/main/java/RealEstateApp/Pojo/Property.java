package RealEstateApp.Pojo;



import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;


@Entity
public class Property {

	   @Id@GeneratedValue(strategy = GenerationType.IDENTITY)
       private Long id;
       private String name;
       private String leaseEndDate;
       private String rentAddress;
       private String scheduleRepairs;
       private String thumbnail;
       //@OneToMany(mappedBy="property")
       //private List<Expenses> expenses;
       
       @OneToOne(fetch = FetchType.LAZY)
       @JoinColumn(name="tenant_user_id", unique = true)
       private User tenant;

       @ManyToOne
       @JoinColumn(name = "company_id")
       private Company company;
       
     
	
	private int rentDueDate;
	private double rentAmount;
	private double lateFee;
	
	
	
	public Property() {
		super();
	}
	
	public Property(Long id, String tenant, String leaseDate, String rentAddress, String scheduleRepairs,
			String thumbnail, List<Expense> expenses, int rentDueDate, double rentAmount, double lateFee) {
		super();
		this.id = id;
		
		this.leaseEndDate = leaseDate;
		this.rentAddress = rentAddress;
		this.scheduleRepairs = scheduleRepairs;
		this.thumbnail = thumbnail;
		//this.expenses = expenses;
		this.rentDueDate = rentDueDate;
		this.rentAmount = rentAmount;
		this.lateFee = lateFee;
	}
	
	
	
    public User getTenant() {
		return tenant;
	}

	public void setTenant(User tenant) {
		this.tenant = tenant;
	}

	public Long getId() {
        return id;
}
public void setId(Long id) {
        this.id = id;
}

public String getName() {
        return name;
}

public void setName(String name) {
        this.name = name;
}

	public String getLeaseEndDate() {
		return leaseEndDate;
	}
	public void setLeaseEndDate(String leaseDate) {
		this.leaseEndDate = leaseDate;
	}
	public String getRentAddress() {
		return rentAddress;
	}
	public void setRentAddress(String rentAddress) {
		this.rentAddress = rentAddress;
	}
	public String getScheduleRepairs() {
		return scheduleRepairs;
	}
	public void setScheduleRepairs(String scheduleRepairs) {
		this.scheduleRepairs = scheduleRepairs;
	}
	public String getThumbnail() {
		return thumbnail;
	}
	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}
	/*public List<Expenses> getExpenses() {
		return expenses;
	}
	public void setExpenses(List<Expenses> expenses) {
		this.expenses = expenses;
	}*/
	public int getRentDueDate() {
		return rentDueDate;
	}
	public void setRentDueDate(int rentDueDate) {
		this.rentDueDate = rentDueDate;
	}
	public double getRentAmount() {
		return rentAmount;
	}
	public void setRentAmount(double rentAmount) {
		this.rentAmount = rentAmount;
	}
	public double getLateFee() {
		return lateFee;
	}
	public void setLateFee(double lateFee) {
		this.lateFee = lateFee;
	}
	
	
	


public RealEstateApp.Pojo.Company getCompany() {
        return company;
}

public void setCompany(RealEstateApp.Pojo.Company company) {
        this.company = company;
}


	
/* 
	//Check the due date and add late fee if past due.
	public static double amountDue( User user, LocalDateTime myObj) {
		
		DateTimeFormatter myFormatObj2 = DateTimeFormatter.ofPattern("dd");
	    String formattedDate2 = myObj.format(myFormatObj2);
	    
	    double amount=0;
	    
	    
	  if(Integer.valueOf( formattedDate2) <= user.getProperty().getRentDueDate()) {
	    	amount=  user.getProperty().getRentAmount();
	    }
	    else if(Integer.valueOf( formattedDate2) > user.getProperty().getRentDueDate()) {
		   amount = user.getProperty().getRentAmount()+ user.getProperty().getLateFee(); 
		}
		
		
		return amount;
	}*/
	    
	    
		
	// convert price input from string to double
	public static double convertAmount  (String price) {
		double  amount=0;
		try {
		amount= Double.parseDouble(price);
		}catch (NumberFormatException e) {
			 e.printStackTrace() ; 
		}
		return amount;
	}
}
