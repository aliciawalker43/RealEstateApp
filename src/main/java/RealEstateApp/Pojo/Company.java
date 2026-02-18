package RealEstateApp.Pojo;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "company")
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String logoUrl; //path

    private String primaryColor;

    private String secondaryColor;

    private String backgroundImageUrl;
    
    private Integer lateNoticeDaysAfterDue; //amount of days after duedate
    
    @Column(length=60)
    private String timeZone;  
    
    @Column(name="subscription_start_date", nullable=true)
    private LocalDate subscriptionStartDate;
    
    @Column(name="billing_date", nullable=true)
    private LocalDate billingDate;
    
    private  int monthlyUsd;          // 30, 60, 150
    
    @Column (name="max_properties")
    private  int maxPropertyLimit = 1; 

    @OneToMany(mappedBy = "company")
    private List<User> users = new ArrayList<>();
    
    @OneToMany(mappedBy = "company")
    private List<Property> properties = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable=false)
    private SubscriptionPlan plan = SubscriptionPlan.STARTER;

    @Column(nullable=false)
    private String subscriptionStatus = "PENDING"; // PENDING, ACTIVE, PAST_DUE, CANCELED

    private String stripeCustomerId;
    private String stripeSubscriptionId;
    private String stripeConnectedAccountId;
    private Boolean connectChargesEnabled;
    private Boolean connectPayoutsEnabled;
    
    
    
    
    
    
   
	public String getStripeConnectedAccountId() {
		return stripeConnectedAccountId;
	}

	public void setStripeConnectedAccountId(String stripeConnectedAccountId) {
		this.stripeConnectedAccountId = stripeConnectedAccountId;
	}

	public LocalDate getSubscriptionStartDate() {
		return subscriptionStartDate;
	}

	public void setSubscriptionStartDate(LocalDate subscriptionStartDate) {
		this.subscriptionStartDate = subscriptionStartDate;
	}

	public LocalDate getBillingDate() {
		return billingDate;
	}

	public void setBillingDate(LocalDate billingDate) {
		this.billingDate = billingDate;
	}

	public int getMonthlyUsd() {
		return monthlyUsd;
	}

	public void setMonthlyUsd(int monthlyUsd) {
		this.monthlyUsd = monthlyUsd;
	}

	

	public int getMaxPropertyLimit() {
		return maxPropertyLimit;
	}

	public void setMaxPropertyLimit(int maxPropertyLimit) {
		this.maxPropertyLimit = maxPropertyLimit;
	}

	public Integer getLateNoticeDaysAfterDue() {
		return lateNoticeDaysAfterDue;
	}

	public void setLateNoticeDaysAfterDue(Integer lateNoticeDaysAfterDue) {
		this.lateNoticeDaysAfterDue = lateNoticeDaysAfterDue;
	}

	public String getTimeZone() {
		return timeZone;
	}

	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}

	
    

    
    
    public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	public List<Property> getProperties() {
		return properties;
	}

	public void setProperties(List<Property> properties) {
		this.properties = properties;
	}

	public SubscriptionPlan getPlan() {
		return plan;
	}

	public void setPlan(SubscriptionPlan plan) {
		this.plan = plan;
	}

	public String getStripeCustomerId() {
		return stripeCustomerId;
	}

	public void setStripeCustomerId(String stripeCustomerId) {
		this.stripeCustomerId = stripeCustomerId;
	}

	public String getStripeSubscriptionId() {
		return stripeSubscriptionId;
	}

	public void setStripeSubscriptionId(String stripeSubscriptionId) {
		this.stripeSubscriptionId = stripeSubscriptionId;
	}

	public void setSubscriptionStatus() {
		this.subscriptionStatus = subscriptionStatus;
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

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getPrimaryColor() {
        return primaryColor;
    }

    public void setPrimaryColor(String primaryColor) {
        this.primaryColor = primaryColor;
    }

    public String getSecondaryColor() {
        return secondaryColor;
    }

    public void setSecondaryColor(String secondaryColor) {
        this.secondaryColor = secondaryColor;
    }

    public String getBackgroundImageUrl() {
        return backgroundImageUrl;
    }

    public void setBackgroundImageUrl(String backgroundImageUrl) {
        this.backgroundImageUrl = backgroundImageUrl;
    }

	public String getSubscriptionStatus() {
		return subscriptionStatus;
	}

	public void setSubscriptionStatus(String subscriptionStatus) {
		this.subscriptionStatus = subscriptionStatus;
	}

	public Long getPropertyCount() {
		return (long) properties.size();
	}
}