package ca.mcgill.ecse321.eventregistration.service;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ca.mcgill.ecse321.eventregistration.dao.*;
import ca.mcgill.ecse321.eventregistration.model.*;

@Service
public class EventRegistrationService {

	@Autowired
	private EventRepository eventRepository;
	@Autowired
	private PersonRepository personRepository;
	@Autowired
	private RegistrationRepository registrationRepository;
	@Autowired
	private CinemaRepository cinemaRepository;
	@Autowired
	private VolunteerRepository volunteerRepository;
	@Autowired
	private BitcoinRepository bitcoinRepository;
	

	@Transactional
	public Person createPerson(String name) {
		if (name == null || name.trim().length() == 0) {
			throw new IllegalArgumentException("Person name cannot be empty!");
		} else if (personRepository.existsById(name)) {
			throw new IllegalArgumentException("Person has already been created!");
		}
		Person person = new Person();
		person.setName(name);
		personRepository.save(person);
		return person;
	}
	
	@Transactional
	public Volunteer createVolunteer(String name) {
		if (name == null || name.trim().length() == 0) {
			throw new IllegalArgumentException("Volunteer name cannot be empty!");
		} else if (volunteerRepository.existsById(name)) {
			throw new IllegalArgumentException("Volunteer has already been created!");
		}
		Volunteer volunteer = new Volunteer();
		volunteer.setName(name);
		volunteerRepository.save(volunteer);
		return volunteer;
	}

	@Transactional
	public Person getPerson(String name) {
		if (name == null || name.trim().length() == 0) {
			throw new IllegalArgumentException("Person name cannot be empty!");
		}
		Person person = personRepository.findByName(name);
		return person;
	}
	
	@Transactional
	public Volunteer getVolunteer(String name) {
		Volunteer volunteer = (Volunteer) getPerson(name);
		return volunteer;
	}

	@Transactional
	public List<Person> getAllPersons() {
		return toList(personRepository.findAll());
	}
	
	@Transactional
	public List<Volunteer> getAllVolunteers() {
		return toList(volunteerRepository.findAll());
	}
	
	@Transactional
	public List<Cinema> getAllCinemas() {
		return toList(cinemaRepository.findAll());
	}
	
	@Transactional
	public List<Bitcoin> getAllBitcoins() {
		return toList(bitcoinRepository.findAll());
	}

	@Transactional
	public Event buildEvent(Event event, String name, Date date, Time startTime, Time endTime) {
		// Input validation
		String error = "";
		if (event == null) {
			error = error + "Event cannot be empty! ";
		}
		if (name == null || name.trim().length() == 0) {
			error = error + "Event name cannot be empty! ";
		} else if (eventRepository.existsById(name)) {
			throw new IllegalArgumentException("Event has already been created!");
		}
		if (date == null) {
			error = error + "Event date cannot be empty! ";
		}
		if (startTime == null) {
			error = error + "Event start time cannot be empty! ";
		}
		if (endTime == null) {
			error = error + "Event end time cannot be empty! ";
		}
		if (endTime != null && startTime != null && endTime.before(startTime)) {
			error = error + "Event end time cannot be before event start time!";
		}
		error = error.trim();
		if (error.length() > 0) {
			throw new IllegalArgumentException(error);
		}
		event.setName(name);
		event.setDate(date);
		event.setStartTime(startTime);
		event.setEndTime(endTime);
		return event;
	}
	
//	@Transactional
//	public Event buildCinema(Cinema cinema, String name, Date date, Time startTime, Time endTime, String movie) {
//		// Input validation
//		return cinema;
//	}

	@Transactional
	public Event createEvent(String name, Date date, Time startTime, Time endTime) {
		Event event = new Event();
		buildEvent(event, name, date, startTime, endTime);
		eventRepository.save(event);
		return event;
	}
	
	@Transactional
	public Cinema createCinema(String name, Date date, Time startTime , Time endTime, String movie) {
		String error = "";
		if (name == null || name.trim().length() == 0) {
			error = error + "Event name cannot be empty! ";
		} else if (cinemaRepository.existsById(name)) {
			throw new IllegalArgumentException("Event has already been created!");
		}
		if (date == null) {
			error = error + "Event date cannot be empty! ";
		}
		if (startTime == null) {
			error = error + "Event start time cannot be empty! ";
		}
		if (endTime == null) {
			error = error + "Event end time cannot be empty! ";
		}
		if (endTime != null && startTime != null && endTime.before(startTime)) {
			error = error + "Event end time cannot be before event start time!";
		}
		if (movie == null || movie.trim().length() == 0) {
			error = error + "Cinema movie cannot be empty!";
		}
		error = error.trim();
		if (error.length() > 0) {
			throw new IllegalArgumentException(error);
		}
		Cinema cinema = new Cinema();
		cinema.setName(name);
		cinema.setDate(date);
		cinema.setStartTime(startTime);
		cinema.setEndTime(endTime);
		cinema.setMovie(movie);
		cinemaRepository.save(cinema);
		return cinema;
	}

	@Transactional
	public Event getEvent(String name) {
		if (name == null || name.trim().length() == 0) {
			throw new IllegalArgumentException("Event name cannot be empty!");
		}
		Event event = eventRepository.findByName(name);
		return event;
	}
	
	@Transactional
	public Cinema getCinema(String name) {
		if (name == null || name.trim().length() == 0) {
			throw new IllegalArgumentException("Cinema name cannot be empty!");
		}
		Cinema cinema = cinemaRepository.findByName(name);
		return cinema;
	}

	// This returns all objects of instance "Event" (Subclasses are filtered out)
	@Transactional
	public List<Event> getAllEvents() {
		return toList(eventRepository.findAll()).stream().filter(e -> e.getClass().equals(Event.class)).collect(Collectors.toList());
	}

	@Transactional
	public Registration register(Person person, Event event) {
		String error = "";
		if (person == null) {
			error = error + "Person needs to be selected for registration! ";
		} else if (!personRepository.existsById(person.getName())) {
			error = error + "Person does not exist! ";
		}
		if (event == null) {
			error = error + "Event needs to be selected for registration!";
		} else if (!eventRepository.existsById(event.getName())) {
			error = error + "Event does not exist!";
		}
		if (registrationRepository.existsByPersonAndEvent(person, event)) {
			error = error + "Person is already registered to this event!";
		}

		error = error.trim();

		if (error.length() > 0) {
			throw new IllegalArgumentException(error);
		}

		Registration registration = new Registration();
		registration.setId(person.getName().hashCode() * event.getName().hashCode());
		registration.setPerson(person);
		registration.setEvent(event);

		registrationRepository.save(registration);

		return registration;
	}

	@Transactional
	public List<Registration> getAllRegistrations() {
		return toList(registrationRepository.findAll());
	}

	@Transactional
	public Registration getRegistrationByPersonAndEvent(Person person, Event event) {
		if (person == null || event == null) {
			throw new IllegalArgumentException("Person or Event cannot be null!");
		}

		return registrationRepository.findByPersonAndEvent(person, event);
	}
	
	@Transactional
	public Bitcoin getBitcoinByPersonAndEvent(Person person, Event event) {
		if (person == null || event == null) {
			throw new IllegalArgumentException("Person or Event cannot be null!");
		}

		Registration r = registrationRepository.findByPersonAndEvent(person, event);
		return r.getBitcoin();
	}
	
	@Transactional
	public List<Registration> getRegistrationsForPerson(Person person){
		if (person == null) {
			throw new IllegalArgumentException("Person cannot be null!");
		}
		return registrationRepository.findByPerson(person);
	}

	@Transactional
	public List<Registration> getRegistrationsByPerson(Person person) {
		return toList(registrationRepository.findByPerson(person));
	}

	@Transactional
	public List<Event> getEventsAttendedByPerson(Person person) {
		if (person == null) {
			throw new IllegalArgumentException("Person cannot be null!");
		}
		List<Event> eventsAttendedByPerson = new ArrayList<>();
		if (registrationRepository.findByPerson(person) == null) {
			return null;
		}
		for (Registration r : registrationRepository.findByPerson(person)) {
			eventsAttendedByPerson.add(r.getEvent());
		}
		return eventsAttendedByPerson;
	}
	
	@Transactional
	public List<Event> getEventsVolunteeredByVolunteer(Volunteer volunteer) {
		if (volunteer == null) {
			throw new IllegalArgumentException("Volunteer cannot be null!");
		}
		List<Event> eventsVolunteeredByVolunteer = new ArrayList<>();
		if (volunteer.getVolunteers() == null) {
			return null;
		}
		for (Event e : volunteer.getVolunteers()) {
			eventsVolunteeredByVolunteer.add(e);
		}
		return eventsVolunteeredByVolunteer;
	}
	
	@Transactional
	public void volunteersEvent(Volunteer volunteer, Event event) {
		if (volunteer == null) {
			throw new IllegalArgumentException("Volunteer needs to be selected for volunteers!");
		} else if (!volunteerRepository.existsById(volunteer.getName())) {
			throw new IllegalArgumentException("Volunteer does not exist!");
		}
		if (event == null) {
			throw new IllegalArgumentException("Event needs to be selected for volunteers!");
		} else if (!eventRepository.existsById(event.getName())) {
			throw new IllegalArgumentException("Event does not exist!");
		}
		eventRepository.save(event);
		Set<Event> events = null;
		if (volunteer.getVolunteers() != null) {
			events = volunteer.getVolunteers();
			events.add(event);

		}
		else {
			events = new HashSet<Event>();
			events.add(event);
		}
		volunteer.setVolunteers(events);
		volunteerRepository.save(volunteer);
	}
	
	public Bitcoin createBitcoinPay(String userID, int amount) {
		String regex = "[a-zA-Z]{4}-[0-9]{4}"; // match a string of 4 letters followed by a - and 4 numbers
		if (userID == null || !userID.matches(regex)) {
			throw new IllegalArgumentException("User id is null or has wrong format!");
		}
		if (amount < 0) {
			throw new IllegalArgumentException("Payment amount cannot be negative!");
		}
		Bitcoin bitcoin = new Bitcoin();
		bitcoin.setUserID(userID);
		bitcoin.setAmount(amount);
		bitcoinRepository.save(bitcoin);
		return bitcoin;
	}
	
	public void pay(Registration registration, Bitcoin bitcoin) {
		if (registration == null || bitcoin == null) {
			throw new IllegalArgumentException("Registration and payment cannot be null!");
		}
		registration.setBitcoin(bitcoin);
		registrationRepository.save(registration);
	}
	
	private <T> List<T> toList(Iterable<T> iterable) {
		List<T> resultList = new ArrayList<T>();
		for (T t : iterable) {
			resultList.add(t);
		}
		return resultList;
	}
}
