package ca.mcgill.ecse321.eventregistration.dto;


public class BitcoinDto {
	
	private String amount;
	private String userID;

	public BitcoinDto() {
	}
	
	public BitcoinDto(String amount, String userID) {
		this.amount = amount;
		this.userID = userID;
	}
	
	public String getAmount() {
		return this.amount;
	}
	
	public void setAmount(String amount) {
		this.amount = amount;
	}
	
	public String getUserID() {
		return this.userID;
	}
	
	public void setUserID(String userID) {
		this.userID = userID;
	}
}
