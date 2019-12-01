package ca.mcgill.ecse321.eventregistration.model;

import java.sql.Date;
import java.sql.Time;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("Cinema")
public class Cinema extends Event {
	
    private String movie;
    public void setMovie(String movie) {
        this.movie = movie;
    }
    public String getMovie() {
        return this.movie;
    }
}
