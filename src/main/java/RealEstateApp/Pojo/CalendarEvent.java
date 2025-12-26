package RealEstateApp.Pojo;

import RealEstateApp.Pojo.Property;
import RealEstateApp.Pojo.User;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.LocalDateTime;


	@Entity
	@Table(name = "calendar_event")
	public class CalendarEvent {

	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    @Enumerated(EnumType.STRING)
	    private CalendarEventType type = CalendarEventType.OTHER;

	    @ManyToOne
	    @JoinColumn(name = "company_id")
	    private Company company;

	    @ManyToOne
	    @JoinColumn(name = "tenant_id")
	    private User relatedTenant;

	    @ManyToOne
	    @JoinColumn(name = "property_id")
	    private Property relatedProperty;

	    @ManyToOne
	    @JoinColumn(name = "employee_id")
	    private Employee relatedEmployee;

	    private LocalDateTime startDateTime;

	    private LocalDateTime endDateTime;

	    private String description;

	    public Long getId() {
	        return id;
	    }

	    public void setId(Long id) {
	        this.id = id;
	    }

	    public CalendarEventType getType() {
	        return type;
	    }

	    public void setType(CalendarEventType type) {
	        this.type = type;
	    }

	    public Company getCompany() {
	        return company;
	    }

	    public void setCompany(Company company) {
	        this.company = company;
	    }

	    public User getRelatedTenant() {
	        return relatedTenant;
	    }

	    public void setRelatedTenant(User relatedTenant) {
	        this.relatedTenant = relatedTenant;
	    }

	    public Property getRelatedProperty() {
	        return relatedProperty;
	    }

	    public void setRelatedProperty(Property relatedProperty) {
	        this.relatedProperty = relatedProperty;
	    }

	    public Employee getRelatedEmployee() {
	        return relatedEmployee;
	    }

	    public void setRelatedEmployee(Employee relatedEmployee) {
	        this.relatedEmployee = relatedEmployee;
	    }

	    public LocalDateTime getStartDateTime() {
	        return startDateTime;
	    }

	    public void setStartDateTime(LocalDateTime startDateTime) {
	        this.startDateTime = startDateTime;
	    }

	    public LocalDateTime getEndDateTime() {
	        return endDateTime;
	    }

	    public void setEndDateTime(LocalDateTime endDateTime) {
	        this.endDateTime = endDateTime;
	    }

	    public String getDescription() {
	        return description;
	    }

	    public void setDescription(String description) {
	        this.description = description;
	    }
}
