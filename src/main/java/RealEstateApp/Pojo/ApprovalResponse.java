package RealEstateApp.Pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payer;
import com.paypal.api.payments.Transactions;


public class ApprovalResponse {

	@JsonProperty("payer")
	private Payer payer;
	@JsonProperty("transactions")
	private Transactions transactions;
	@JsonProperty("links")
	private Links links;
	
	
	public Payer getPayer() {
		return payer;
	}
	public void setPayer(Payer payer) {
		this.payer = payer;
	}
	public Transactions getTransactions() {
		return transactions;
	}
	public void setTransactions(Transactions transactions) {
		this.transactions = transactions;
	}
	public Links getLinks() {
		return links;
	}
	public void setLinks(Links links) {
		this.links = links;
	}
	@Override
	public String toString() {
		return "ApprovalResponse [payer=" + payer + ", transactions=" + transactions + ", links=" + links + "]";
	}
	
	
	
	
	
}
