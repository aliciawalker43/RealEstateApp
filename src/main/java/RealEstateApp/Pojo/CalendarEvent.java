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

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "calendar_event")
public class CalendarEvent {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name="company_id", nullable=false)
    private Company company;

    // Optional links (keep nullable for flexibility)
    @ManyToOne
    @JoinColumn(name="property_id")
    private Property property;

    @ManyToOne
    @JoinColumn(name="assigned_user_id")
    private User assignedUser; // employee or tenant if needed

    @Enumerated(EnumType.STRING)
    @Column(nullable=false, length=40)
    private CalendarEventType type = CalendarEventType.OTHER;

    @Column(nullable=false, length=120)
    private String title;

    @Column(length=2000)
    private String description;

    @Column(nullable=false)
    private LocalDateTime startAt;

    @Column(nullable=false)
    private LocalDateTime endAt;

    @Column(nullable=false)
    private boolean allDay = false;

    @Column(nullable=false)
    private boolean active = true;

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

	public Property getProperty() {
		return property;
	}

	public void setProperty(Property property) {
		this.property = property;
	}

	public User getAssignedUser() {
		return assignedUser;
	}

	public void setAssignedUser(User assignedUser) {
		this.assignedUser = assignedUser;
	}

	public CalendarEventType getType() {
		return type;
	}

	public void setType(CalendarEventType type) {
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

	public LocalDateTime getStartAt() {
		return startAt;
	}

	public void setStartAt(LocalDateTime startAt) {
		this.startAt = startAt;
	}

	public LocalDateTime getEndAt() {
		return endAt;
	}

	public void setEndAt(LocalDateTime endAt) {
		this.endAt = endAt;
	}

	public boolean isAllDay() {
		return allDay;
	}

	public void setAllDay(boolean allDay) {
		this.allDay = allDay;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

    
    
    
}

