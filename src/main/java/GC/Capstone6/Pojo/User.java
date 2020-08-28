package GC.Capstone6.Pojo;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;


@Entity
public class User {

	@Id@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	private String name;
	private String username;
	private String passcode;
	
	@OneToMany(mappedBy= "user")
	private Set<Task> task;
	
	
	public User() {
		super();
	}
	
	
	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", username=" + username + ", passcode=" + passcode + ", task="
				+ task + "]";
	}


	public User(Long id, String name, String username, String passcode, Set<Task> task) {
		super();
		this.id = id;
		this.name = name;
		this.username = username;
		this.passcode = passcode;
		this.task = task;
	}
	public String getUserName() {
		return username;
	}
	public void setUserName(String username) {
		this.username = username;
	}
	public Set<Task> getTask() {
		return task;
	}
	public void setTask(Set<Task> task) {
		this.task = task;
	}
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
	public String getPasscode() {
		return passcode;
	}
	public void setPasscode(String passcode) {
		this.passcode = passcode;
	}
	
	
	
	
}
