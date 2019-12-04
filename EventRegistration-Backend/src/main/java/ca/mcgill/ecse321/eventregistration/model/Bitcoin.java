package ca.mcgill.ecse321.eventregistration.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import ca.mcgill.ecse321.eventregistration.model.Person;

@Entity
public class Bitcoin {
	
	private int amount;
	
	public int getAmount() {
		return this.amount;
	}
	
	public void setAmount(int amount) {
		this.amount = amount;
	}
	
	private String userID;
	
	@Id
	public String getUserID() {
		return this.userID;
	}
	
	public void setUserID(String userID) {
		this.userID = userID;
	}

}
