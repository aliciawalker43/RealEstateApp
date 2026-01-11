package RealEstateApp.Pojo;



import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate leaseStartDate;
    private LocalDate leaseEndDate;


    @Column(nullable=false, length=255)
    private String rentalAddress;

    @Column(nullable = false)
    private Integer rentDueDay= 1;

    @Column(precision = 10, scale = 2)
    private BigDecimal rentAmount;

    @Column(precision = 10, scale = 2)
    private BigDecimal lateFee;

    // One property belongs to one company; company has many properties
    @ManyToOne(optional = false)
    @JoinColumn(name="company_id", nullable=false)
    private Company company;

    // One property has many maintenance requests
    @OneToMany(mappedBy = "property", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MaintenanceRequest> requests = new ArrayList<>();

    // One property has many images (recommended: ImageAsset has property + context)
    @OneToMany(mappedBy = "property", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ImageAsset> propertyImages = new ArrayList<>();

    // One property has at most one tenant; tenant can have at most one property/unit
    @OneToOne
    @JoinColumn(name = "tenant_user_id", unique = true)
    private User tenant;
       
     

	    
	    
		
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDate getLeaseStartDate() {
		return leaseStartDate;
	}

	public void setLeaseStartDate(LocalDate leaseStartDate) {
		this.leaseStartDate = leaseStartDate;
	}

	public LocalDate getLeaseEndDate() {
		return leaseEndDate;
	}


	public void setLeaseEndDate(LocalDate leaseEndDate) {
		this.leaseEndDate = leaseEndDate;
	}


	public String getRentalAddress() {
		return rentalAddress;
	}


	public void setRentalAddress(String rentalAddress) {
		this.rentalAddress = rentalAddress;
	}


	public int getRentDueDay() {
		return rentDueDay;
	}


	public void setRentDueDay(int rentDueDay) {
		this.rentDueDay = rentDueDay;
	}

	public BigDecimal getRentAmount() {
		return rentAmount;
	}


	public void setRentAmount(BigDecimal rentAmount) {
		this.rentAmount = rentAmount;
	}


	public BigDecimal getLateFee() {
		return lateFee;
	}


	public void setLateFee(BigDecimal lateFee) {
		this.lateFee = lateFee;
	}



	public Company getCompany() {
		return company;
	}


	public void setCompany(Company company) {
		this.company = company;
	}



	public List<MaintenanceRequest> getRequests() {
		return requests;
	}

	public void setRequests(List<MaintenanceRequest> requests) {
		this.requests = requests;
	}

	public List<ImageAsset> getPropertyImages() {
		return propertyImages;
	}

	public void setPropertyImages(List<ImageAsset> propertyImages) {
		this.propertyImages = propertyImages;
	}


	public User getTenant() {
		return tenant;
	}

	public void setTenant(User tenant) {
		this.tenant = tenant;
	}



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
