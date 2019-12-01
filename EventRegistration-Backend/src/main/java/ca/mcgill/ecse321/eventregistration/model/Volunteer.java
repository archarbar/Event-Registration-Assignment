package ca.mcgill.ecse321.eventregistration.model;


import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;

@Entity
public class Volunteer extends Person{
    
    @ManyToMany
    private Set<Event> events;
    
    public Set<Event> getVolunteers() {
    	return this.events;
    }
    
}
