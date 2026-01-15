package RealEstateApp.Pojo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table( name="app_feedback")
public class AppFeedback {
	
	
	
	
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
