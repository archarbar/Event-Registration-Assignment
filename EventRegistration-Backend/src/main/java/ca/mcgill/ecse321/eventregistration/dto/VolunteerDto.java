package ca.mcgill.ecse321.eventregistration.dto;

import java.util.List;

public class VolunteerDto{
    
	private String name;
	private List<EventDto> eventsAttended;
    private List<EventDto> eventsVolunteered;
    
    public VolunteerDto() {
    }
	
    public VolunteerDto(String name, List<EventDto> eventsAttended, List<EventDto> eventsVolunteered) {
    	this.name = name;
    	this.eventsAttended = eventsAttended;
    	this.eventsVolunteered = eventsVolunteered;
    }

	public String getName() {
		return name;
	}

	public List<EventDto> getEventsAttended() {
		return eventsAttended;
	}

	public void setEventsAttended(List<EventDto> events) {
		this.eventsAttended = events;
	}
    
    public List<EventDto> getVolunteers() {
    	return this.eventsVolunteered;
    }
    
    public void setVolunteers(List<EventDto> events) {
    	this.eventsVolunteered = events;
    }
}
