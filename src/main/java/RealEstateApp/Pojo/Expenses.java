package RealEstateApp.Pojo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Expenses {

	
	@Id@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String expenseName;
	private String date;
	private Double expenseAmount;
	@ManyToOne
	@JoinColumn(name= "property_id", unique = true)
	private Property property;
	
	
	
	
	
	
	
	
	@Override
	public String toString() {
		return "Expenses [id=" + id + ", expenseName=" + expenseName + ", date=" + date + ", expenseAmount="
				+ expenseAmount + ", property=" + property + "]";
	}

	public Expenses(Long id, String expenseName, String date, Double expenseAmount, Property property) {
		super();
		this.id = id;
		this.expenseName = expenseName;
		this.date = date;
		this.expenseAmount = expenseAmount;
		this.property = property;
	}

	public Expenses() {
		super();
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getExpenseName() {
		return expenseName;
	}
	public void setExpenseName(String expenseName) {
		this.expenseName = expenseName;
	}
	public Double getExpenseAmount() {
		return expenseAmount;
	}
	public void setExpenseAmount(Double expenseAmount) {
		this.expenseAmount = expenseAmount;
	}
	public Property getProperty() {
		return property;
	}
	public void setProperty(Property property) {
		this.property = property;
	}
	
	
	
	
	
	
	
	
	
}
