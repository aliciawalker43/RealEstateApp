package RealEstateApp.Pojo;

import java.time.Instant;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "email_log")
public class EmailLog {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional=false)
    @JoinColumn(name="company_id")
    private Company company;

    @ManyToOne(optional=false)
    @JoinColumn(name="user_id")
    private User from; 

    @ManyToOne(optional = true)
    @JoinColumn(name = "property_id", nullable = true)
    private Property property;

    @OneToMany
    private List<User> audience; //to
    
    @Column(nullable=false)
    private Instant sentAt = Instant.now();
    
    @Column(nullable = false, length = 200)
    private String subject;

    @Column(nullable = false, length = 10000)
    private String body;
    
    @Column(nullable = false, length = 100)
    private Integer recipientCount= 1;

    
    
    
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

	public User getFrom() {
		return from;
	}

	public void setFrom(User from) {
		this.from = from;
	}

	public Property getProperty() {
		return property;
	}

	public void setProperty(Property property) {
		this.property = property;
	}

	public List<User> getAudience() {
		return audience;
	}

	public void setAudience(List<User> audience) {
		this.audience = audience;
	}

	public Instant getSentAt() {
		return sentAt;
	}

	public void setSentAt(Instant sentAt) {
		this.sentAt = sentAt;
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

	public Integer getRecipientCount() {
		return recipientCount;
	}

	public void setRecipientCount(Integer recipientCount) {
		this.recipientCount = recipientCount;
	}
    
    
    
}

