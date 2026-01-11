package RealEstateApp.Pojo;

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

   
}