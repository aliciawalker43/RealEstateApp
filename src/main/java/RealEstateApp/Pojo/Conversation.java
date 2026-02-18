package RealEstateApp.Pojo;


import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "conversation")
//uniqueConstraints = @UniqueConstraint(columnNames = {"company_id", "tenant_user_id"}))
public class Conversation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name="company_id")
    private Company company;

    @ManyToOne(optional = false)
    @JoinColumn(name="tenant_user_id", nullable = false)
    private User tenant; // the tenant for this thread

    @Column(nullable = false)
    private Instant createdAt = Instant.now();
    
    
    
    
    

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

	public void setTenant(User tenant2) {
		this.tenant = tenant2;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Instant createdAt) {
		this.createdAt = createdAt;
	}
    
	
    

}
