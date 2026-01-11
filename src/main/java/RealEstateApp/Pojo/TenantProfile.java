package RealEstateApp.Pojo;


import jakarta.persistence.*;

@Entity
@Table(name="tenant_profile")
public class TenantProfile {

  @Id
  private Long userId;

  @OneToOne(optional=false)
  @MapsId
  @JoinColumn(name="user_id")
  private User user;

  @ManyToOne(optional = true)
  @JoinColumn(name="property_id", nullable = true)
  private Property property;


  @Column(length=255)
  private String mailingAddress;

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

public Property getProperty() {
	return property;
}

public void setProperty(Property property) {
	this.property = property;
}

public String getMailingAddress() {
	return mailingAddress;
}

public void setMailingAddress(String mailingAddress) {
	this.mailingAddress = mailingAddress;
}

public String getProfileImageUrl() {
	return profileImageUrl;
}

public void setProfileImageUrl(String profileImageUrl) {
	this.profileImageUrl = profileImageUrl;
}


}
  
  
  
