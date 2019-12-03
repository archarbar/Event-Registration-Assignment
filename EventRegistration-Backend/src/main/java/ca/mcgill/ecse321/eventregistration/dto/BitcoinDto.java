package ca.mcgill.ecse321.eventregistration.dto;


public class BitcoinDto {
	
	private int amount;
	private String userID;

	public BitcoinDto() {
	}

	public BitcoinDto(int amount) {
		this(amount, "QWER-1234");
	}
	
	public BitcoinDto(int amount, String userID) {
		this.amount = amount;
		this.userID = userID;
	}
	
	public int getAmount() {
		return this.amount;
	}
	
	public void setAmount(int amount) {
		this.amount = amount;
	}
	
	public String getUserID() {
		return this.userID;
	}
	
	public void setUserID(String userID) {
		this.userID = userID;
	}
}
