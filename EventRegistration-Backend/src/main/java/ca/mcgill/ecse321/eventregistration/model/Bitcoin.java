package ca.mcgill.ecse321.eventregistration.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import ca.mcgill.ecse321.eventregistration.model.Person;

@Entity
public class Bitcoin {
	
	private String amount;
	
	@Id
	public String getAmount() {
		return this.amount;
	}
	
	public void setAmount(String amount) {
		this.amount = amount;
	}
	
	private String userID;
	
	public String getUserID() {
		return this.userID;
	}
	
	public void setUserID(String userID) {
		this.userID = userID;
	}

}
