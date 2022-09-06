package RealEstateApp.Pojo;




import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.springframework.beans.factory.annotation.Value;


@Entity
public class User {

	@Id@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	private String firstname;
	private String lastname;
	private String username;
	private String passcode;
	private String email;
	@Value("${level1}")
	private String accessStatus;
	
	//Rental Communication
	private String comments;
	@ManyToOne 
	@JoinColumn(name= "property_Id", unique = true)
	private Property property;
	
	//Employee Statistics
	private String hireDate;
	private Double payRate;
	private String position;
	
	
	

	public User() {
		super();
	}
	
	public User(Long id, String firstname, String lastname, String username, String passcode, String email,
			String accessStatus, String leasePeriod, String address, String rentDueDate, Double rentAmount,
			Double lateFee, String comments, String hireDate, Double payRate, String position) {
		super();
		this.id = id;
		this.firstname = firstname;
		this.lastname = lastname;
		this.username = username;
		this.passcode = passcode;
		this.email = email;
		this.accessStatus = accessStatus;
		
		this.comments = comments;
		this.hireDate = hireDate;
		this.payRate = payRate;
		this.position = position;
	}
	

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
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
	public void setPayRate( Double payRate) {
		this.payRate = payRate;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getFirstname() {
		return firstname;
	}
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	public String getLastname() {
		return lastname;
	}
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPasscode() {
		return passcode;
	}
	public void setPasscode(String passcode) {
		this.passcode = passcode;
	}
	public String getAccessStatus() {
		return accessStatus;
	}
	public void setAccessStatus(String accessStatus) {
		this.accessStatus = accessStatus;
	}

	public Property getProperty() {
		return property;
	}

	public void setProperty(Property property) {
		this.property = property;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", firstname=" + firstname + ", lastname=" + lastname + ", username=" + username
				+ ", passcode=" + passcode + ", email=" + email + ", accessStatus=" + accessStatus + ", comments="
				+ comments + ", property=" + property + ", hireDate=" + hireDate + ", payRate=" + payRate
				+ ", position=" + position + "]";
	}


	
	}
	


	
	
	

	
	
	

	
	
	
	

	
	
	
	
	
