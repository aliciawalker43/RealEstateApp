package RealEstateApp.Pojo;

import java.time.Instant;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class AppFeedback {

	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	private String userName;
	private String email;
	
	private String navigationExperience;
	private String performanceExperience;
	private String designExperience;
	private String malfunctionExperience;
	private String recommendation;
	private String comments;
    @Column(nullable=false)
    private Instant uploadedAt = Instant.now();
    
     

	
	public Instant getUploadedAt() {
		return uploadedAt;
	}
	public void setUploadedAt(Instant uploadedAt) {
		this.uploadedAt = uploadedAt;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getNavigationExperience() {
		return navigationExperience;
	}
	public void setNavigationExperience(String navigationExperience) {
		this.navigationExperience = navigationExperience;
	}
	public String getPerformanceExperience() {
		return performanceExperience;
	}
	public void setPerformanceExperience(String performanceExperience) {
		this.performanceExperience = performanceExperience;
	}
	public String getDesignExperience() {
		return designExperience;
	}
	public void setDesignExperience(String designExperience) {
		this.designExperience = designExperience;
	}
	public String getMalfunctionExperience() {
		return malfunctionExperience;
	}
	public void setMalfunctionExperience(String malfunctionExperience) {
		this.malfunctionExperience = malfunctionExperience;
	}
	public String getRecommendation() {
		return recommendation;
	}
	public void setRecommendation(String recommendation) {
		this.recommendation = recommendation;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	
	
	
	
	
	
}
