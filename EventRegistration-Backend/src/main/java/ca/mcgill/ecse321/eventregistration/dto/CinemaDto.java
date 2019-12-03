package ca.mcgill.ecse321.eventregistration.dto;

import java.sql.Date;
import java.sql.Time;

public class CinemaDto {

	private String name;
	private Date date;
	private Time startTime;
	private Time endTime;
	private String movie;

	public CinemaDto() {
	}

	public CinemaDto(String name, Date date, Time startTime, Time endTime, String movie) {
		this.name = name;
		this.date = date;
		this.startTime = startTime;
		this.endTime = endTime;
		this.movie = movie;
	}

	public String getName() {
		return name;
	}

	public Date getDate() {
		return date;
	}

	public Time getStartTime() {
		return startTime;
	}

	public Time getEndTime() {
		return endTime;
	}
	
	public String getMovie() {
		return this.movie;
	}

}
