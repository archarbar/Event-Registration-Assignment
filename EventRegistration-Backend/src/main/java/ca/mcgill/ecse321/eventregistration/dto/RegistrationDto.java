package ca.mcgill.ecse321.eventregistration.dto;

public class RegistrationDto {

	private PersonDto person;
	private EventDto event;
	private BitcoinDto bitcoin;

	public RegistrationDto() {
	}

	public RegistrationDto(PersonDto person, EventDto event) {
		this.person = person;
		this.event = event;
		this.bitcoin = null;
	}

	public EventDto getEvent() {
		return event;
	}

	public void setEvent(EventDto event) {
		this.event = event;
	}

	public PersonDto getPerson() {
		return person;
	}

	public void setPerson(PersonDto person) {
		this.person = person;
	}
	
	public BitcoinDto getBitcoin() {
		return bitcoin;
	}

	public void setBitcoin(BitcoinDto bitcoin) {
		this.bitcoin = bitcoin;
	}

}
