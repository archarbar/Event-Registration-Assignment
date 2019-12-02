package ca.mcgill.ecse321.eventregistration.dto;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import ca.mcgill.ecse321.eventregistration.model.Event;

public class VolunteerDto{
    
	private String name;
	private List<EventDto> eventsAttended;
    private List<EventDto> eventsVolunteered;
    
    public VolunteerDto() {
    }

	@SuppressWarnings("unchecked")
	public VolunteerDto(String name) {
		this(name, Collections.EMPTY_LIST, Collections.EMPTY_LIST);
	}
	
    public VolunteerDto(String name, List<EventDto> eventsAttended, List<EventDto> eventsVolunteered) {
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
