package ca.mcgill.ecse321.eventregistration.model;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import java.sql.Date;
import java.sql.Time;
import java.util.Set;

@Entity
@Table(name="event")
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(discriminatorType=DiscriminatorType.STRING, name="TYPE")
public class Event {
    private String name;

    public void setName(String value) {
        this.name = value;
    }
    @Id
    public String getName() {
        return this.name;
    }
    private Date date;

    public void setDate(Date value) {
        this.date = value;
    }
    public Date getDate() {
        return this.date;
    }
    private Time startTime;

    public void setStartTime(Time value) {
        this.startTime = value;
    }
    public Time getStartTime() {
        return this.startTime;
    }
    private Time endTime;

    public void setEndTime(Time value) {
        this.endTime = value;
    }
    public Time getEndTime() {
        return this.endTime;
    }
    
    private Set<Person> participants;
    
    @ManyToMany(mappedBy = "events")
    public Set<Person> getParticipants() {
        return this.participants;
      }
    
    public void setParticipants(Set<Person> participants) {
        this.participants = participants;
      }
    
    private Set<Volunteer> volunteers;
    
    @ManyToMany(mappedBy = "events")
    public Set<Volunteer> getVolunteers() {
      return this.volunteers;
    }
    public void setVolunteers(Set<Volunteer> volunteers) {
        this.volunteers = volunteers;
      }
}
