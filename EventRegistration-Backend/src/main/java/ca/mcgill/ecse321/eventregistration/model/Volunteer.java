package ca.mcgill.ecse321.eventregistration.model;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;

@Entity
public class Volunteer extends Person{
    
    private Set<Event> events;
    
    @ManyToMany
    public Set<Event> getVolunteers() {
    	return this.events;
    }
    
    public void setVolunteers(Set<Event> events) {
    	this.events = events;
    }
    
}
