package RealEstateApp.Pojo;



import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import RealEstateApp.Pojo.Company;
import RealEstateApp.Pojo.Role;
import org.springframework.beans.factory.annotation.Value;



@Entity
public class User {

	@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    private String firstname;
    private String lastname;
    private String username;
    private String passcode;
    private String email;

    @Enumerated(EnumType.STRING)
    private Role role = Role.TENANT;

    private String phoneNumber;

    private String profileImageUrl;

    private String billingAddress;

    private String mailingAddress;

    @Value("${level1}")
    private String accessStatus;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    private String emergencyContact;

    //Employee Statistics
    private String hireDate;
    private Double payRate;
    private String position;

    //Rental Communication
    private String comments;
    @ManyToOne
    @JoinColumn(name= "property_Id", unique = true)
    private Property property;

    public User() {
            super();
    }

    public User(Long id, String firstname, String lastname, String username, String passcode, String email,
                    String accessStatus, String hireDate, Double payRate, String position, String comments, Property property) {
            super();
            this.id = id;
            this.firstname = firstname;
            this.lastname = lastname;
            this.username = username;
            this.passcode = passcode;
            this.email = email;
            this.accessStatus = accessStatus;
            this.hireDate = hireDate;
            this.payRate = payRate;
            this.position = position;
            this.comments = comments;
            this.property = property;
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

    public String getEmail() {
            return email;
    }

    public void setEmail(String email) {
            this.email = email;
    }
    
    public Role getRole() {
        return role;
}

public void setRole(Role role) {
        this.role = role;
}

public String getPhoneNumber() {
        return phoneNumber;
}

public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
}

public String getProfileImageUrl() {
        return profileImageUrl;
}

public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
}

public String getBillingAddress() {
        return billingAddress;
}

public void setBillingAddress(String billingAddress) {
        this.billingAddress = billingAddress;
}

public String getMailingAddress() {
        return mailingAddress;
}

public void setMailingAddress(String mailingAddress) {
        this.mailingAddress = mailingAddress;
}

public String getAccessStatus() {
        return accessStatus;
}

public void setAccessStatus(String accessStatus) {
        this.accessStatus = accessStatus;
}

public Company getCompany() {
        return company;
}

public void setCompany(Company company) {
        this.company = company;
}

public String getEmergencyContact() {
        return emergencyContact;
}

public void setEmergencyContact(String emergencyContact) {
        this.emergencyContact = emergencyContact;
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

public String getComments() {
        return comments;
}

public void setComments(String comments) {
        this.comments = comments;
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
                        + ", passcode=" + passcode + ", email=" + email + ", accessStatus=" + accessStatus + ", hireDate="
                        + hireDate + ", payRate=" + payRate + ", position=" + position + ", comments=" + comments
                        + ", property=" + property + "]";
}
}