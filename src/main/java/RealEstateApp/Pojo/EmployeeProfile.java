package RealEstateApp.Pojo;

import jakarta.persistence.*;

@Entity
@Table(name="employee_profile")
public class EmployeeProfile {
	
	
	 @Id
	  private Long userId;
	
	 @OneToOne(optional=false)
	  @MapsId
	  @JoinColumn(name="user_id")
	  private User user;

    //Employee Statistics
    private String hireDate;
    private Double payRate;
    
    @Column(length=80)
    private String position;
    
    @Column(length=255)
    private String profileImageUrl;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getHireDate() {
		return hireDate;
	}

	public void setHireDate(String hireDate) {
		this.hireDate = hireDate;
	}

	public Double getPayRate() {
		return payRate;
	}

	public void setPayRate(Double payRate) {
		this.payRate = payRate;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getProfileImageUrl() {
		return profileImageUrl;
	}

	public void setProfileImageUrl(String profileImageUrl) {
		this.profileImageUrl = profileImageUrl;
	}
    

}
