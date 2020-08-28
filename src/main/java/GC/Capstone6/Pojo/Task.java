package GC.Capstone6.Pojo;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table (name="task")
public class Task {

	@Id@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private LocalDate date;
	private String description;
	private String complete;
	
	@ManyToOne
	private User user;
	
	
	
	public Task(Long id, String name, LocalDate date, String description, String complete, User user) {
		super();
		this.id = id;
		this.date = date;
		this.description = description;
		this.complete = complete;
		this.user = user;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Task() {
		super();
	}

	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public LocalDate getDate() {
		return date;
	}
	public void setDate(LocalDate date) {
		this.date = date;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getComplete() {
		return complete;
	}
	public void setComplete(String complete) {
		this.complete = complete;
	}

	@Override
	public String toString() {
		return "Task [id=" + id + ", name=" + name + ", date=" + date + ", description=" + description + ", complete="
				+ complete + ", user=" + user + "]";
	}

	
	
	
	
	
}
