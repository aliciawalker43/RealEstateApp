package RealEstateApp.Pojo;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "invoice_email_log")
public class InvoiceEmailLog {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional=false)
    @JoinColumn(name="company_id")
    private Company company;

    @ManyToOne(optional=false)
    @JoinColumn(name="tenant_user_id")
    private User tenant;

    @ManyToOne(optional=false)
    @JoinColumn(name="property_id")
    private Property property;

    @Column(nullable=false)
    private LocalDate forMonth; // e.g., 2026-01-01

    @Column(nullable=false)
    private String type; // "RENT_DUE" or "LATE_NOTICE"

    @Column(nullable=false)
    private Instant sentAt = Instant.now();
    
    @OneToMany
    @Column(nullable = false, length = 20)
    private List <User> audience; // TENANTS, EMPLOYEES, ALL
    
    @Column(nullable = false, length = 200)
    private String subject;

    @Column(nullable = false, length = 10000)
    private String body;

    
    private Integer RecipientCount = 1;
    
    
   
 

	public Integer getRecipientCount() {
		return RecipientCount;
	}

	public void setRecipientCount(Integer recipientCount) {
		RecipientCount = recipientCount;
	}

	public List<User> getAudience() {
		return audience;
	}

	public void setAudience(List<User> audience) {
		this.audience = audience;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public User getTenant() {
		return tenant;
	}

	public void setTenant(User tenant) {
		this.tenant = tenant;
	}

	public Property getProperty() {
		return property;
	}

	public void setProperty(Property property) {
		this.property = property;
	}

	



	public LocalDate getForMonth() {
		return forMonth;
	}

	public void setForMonth(LocalDate forMonth) {
		this.forMonth = forMonth;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Instant getSentAt() {
		return sentAt;
	}

	public void setSentAt(Instant sentAt) {
		this.sentAt = sentAt;
	}

    
    
    
}

