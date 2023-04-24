package RealEstateApp.Pojo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class PaymentHistory {

	
	@Id@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String time;
	private String property;
	private String amount;
	//private String latefee;
	
	
	
	public PaymentHistory() {
		super();
		
	}



	public PaymentHistory(Long id, String time, String property, String amount) {
		super();
		this.id = id;
		this.time = time;
		this.property = property;
		this.amount = amount;
	}



	public Long getId() {
		return id;
	}



	public void setId(Long id) {
		this.id = id;
	}



	public String getTime() {
		return time;
	}



	public void setTime(String time) {
		this.time = time;
	}






	public String getProperty() {
		return property;
	}



	public void setProperty(String property) {
		this.property = property;
	}



	public String getAmount() {
		return amount;
	}



	public void setAmount(String amount) {
		this.amount = amount;
	}



	@Override
	public String toString() {
		return "PaymentHistory [id=" + id + ", time=" + time + ", property=" + property + ", amount=" + amount + "]";
	}
	
	
	public static String simpleDate(String time) {
		
		String[] newTime=time.split("T");
		
		String date=newTime[0];
		
		return date;
	}
	
}
