package RealEstateApp.Pojo;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table( name="app_customer_serv")
public class AppCustomerServ {
	
	
	
	
	 @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	 private Long id;
	 
	 @Column(nullable = false)
	 private String name;
	 
	 @Column(nullable = false)
	 private String email;
	 
	 @Enumerated(EnumType.STRING)
	 private FeedbackType type; // FEEDBACK, CUSTOMER_SERVICE, TROUBLESHOOT
	 
	 @Column(nullable = false)
	 private String title;
	 
	 @Column(nullable = false, length = 2000)
	 private String description;
	 
	 private Boolean isResolved = false;
	 
	 private LocalDate date;
	 
	  
	 
	 
	 
	 
	 public LocalDate getDate() {
		 return date;
	 }

		public void setDate(LocalDate date) {
			this.date = date;
		}
	 
	public Boolean getIsResolved() {
		return isResolved;
	}
	public void setIsResolved(Boolean isResolved) {
		this.isResolved = isResolved;
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
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public FeedbackType getType() {
		return type;
	}
	public void setType(FeedbackType type) {
		this.type = type;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	
	 
	 
	 

}
