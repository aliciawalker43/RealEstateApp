package RealEstateApp.Pojo;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

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
	
	//Rental Statistics
	private String leasePeriod;
	private String rentalAddress;
	private String rentDueDate;
	private Double rentAmount;
	private Double lateFee;
	private String comments;
	
	//Employee Statistics
	private String hireDate;
	private String payRate;
	private String position;
	
	
	

	public User() {
		super();
	}
	
	public User(Long id, String firstname, String lastname, String username, String passcode, String email,
			String accessStatus, String leasePeriod, String rentalAddress, String rentDueDate, Double rentAmount,
			Double lateFee, String comments, String hireDate, String payRate, String position) {
		super();
		this.id = id;
		this.firstname = firstname;
		this.lastname = lastname;
		this.username = username;
		this.passcode = passcode;
		this.email = email;
		this.accessStatus = accessStatus;
		this.leasePeriod = leasePeriod;
		this.rentalAddress = rentalAddress;
		this.rentDueDate = rentDueDate;
		this.rentAmount = rentAmount;
		this.lateFee = lateFee;
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
	public String getPayRate() {
		return payRate;
	}
	public void setPayRate(String payRate) {
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
	public String getLeasePeriod() {
		return leasePeriod;
	}
	public void setLeasePeriod(String leasePeriod) {
		this.leasePeriod = leasePeriod;
	}
	public String getRentalAddress() {
		return rentalAddress;
	}
	public void setRentalAddress(String rentalAddress) {
		this.rentalAddress = rentalAddress;
	}
	public String getRentDueDate() {
		return rentDueDate;
	}
	public void setRentDueDate(String rentDueDate) {
		this.rentDueDate = rentDueDate;
	}
	public Double getRentAmount() {
		return rentAmount;
	}
	public void setRentAmount(Double rentAmount) {
		this.rentAmount = rentAmount;
	}
	public Double getLateFee() {
		return lateFee;
	}
	public void setLateFee(Double lateFee) {
		this.lateFee = lateFee;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", firstname=" + firstname + ", lastname=" + lastname + ", username=" + username
				+ ", passcode=" + passcode + ", email=" + email + ", accessStatus=" + accessStatus + ", leasePeriod="
				+ leasePeriod + ", rentalAddress=" + rentalAddress + ", rentDueDate=" + rentDueDate + ", rentAmount="
				+ rentAmount + ", lateFee=" + lateFee + ", comments=" + comments + ", hireDate=" + hireDate
				+ ", payRate=" + payRate + ", position=" + position + "]";
	}
	
	
	
}
	
	
	

	
	
	
	

	
	
	
	
	
