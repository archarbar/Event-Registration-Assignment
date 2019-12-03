package ca.mcgill.ecse321.eventregistration.controller;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ca.mcgill.ecse321.eventregistration.model.*;
import ca.mcgill.ecse321.eventregistration.dao.EventRepository;
import ca.mcgill.ecse321.eventregistration.dao.PersonRepository;
import ca.mcgill.ecse321.eventregistration.dao.VolunteerRepository;
import ca.mcgill.ecse321.eventregistration.dto.*;
import ca.mcgill.ecse321.eventregistration.service.EventRegistrationService;

@CrossOrigin(origins = "*")
@RestController
public class EventRegistrationRestController {

	@Autowired
	private EventRegistrationService service;
	@Autowired
	private PersonRepository personRepository;
	@Autowired
	private EventRepository eventRepository;
	@Autowired
	private VolunteerRepository volunteerRepository;

	// POST Mappings

	// @formatter:off
	// Turning off formatter here to ease comprehension of the sample code by
	// keeping the linebreaks
	// Example REST call:
	// http://localhost:8088/persons/John
	@PostMapping(value = { "/persons/{name}", "/persons/{name}/" })
	public PersonDto createPerson(@PathVariable("name") String name) throws IllegalArgumentException {
		// @formatter:on
		Person person = service.createPerson(name);
		return convertToDto(person);
	}
	
	// @formatter:off
	// Example REST call:
	// http://localhost:8088/volunteers/John
	@PostMapping(value = { "/volunteers/{name}", "/volunteers/{name}/" })
	public VolunteerDto createVolunteer(@PathVariable("name") String name) throws IllegalArgumentException {
		// @formatter:on
		Volunteer volunteer = service.createVolunteer(name);
		return convertToDto(volunteer);
	}

	// @formatter:off
	// Example REST call:
	// http://localhost:8080/events/testevent?date=2013-10-23&startTime=00:00&endTime=23:59
	@PostMapping(value = { "/events/{name}", "/events/{name}/" })
	public EventDto createEvent(@PathVariable("name") String name, @RequestParam Date date,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME, pattern = "HH:mm") LocalTime startTime,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME, pattern = "HH:mm") LocalTime endTime)
			throws IllegalArgumentException {
		// @formatter:on
		Event event = service.createEvent(name, date, Time.valueOf(startTime), Time.valueOf(endTime));
		return convertToDto(event);
	}
	
	// @formatter:off
	// Example REST call:
	// http://localhost:8080/cinemas/testcinema?date=2013-10-23&startTime=00:00&endTime=23:59&movie=joker
	@PostMapping(value = { "/cinemas/{name}", "/cinemas/{name}/" })
	public CinemaDto createCinema(@PathVariable("name") String name, @RequestParam Date date,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME, pattern = "HH:mm") LocalTime startTime,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME, pattern = "HH:mm") LocalTime endTime, 
			@RequestParam(name = "movie") String movie)
			throws IllegalArgumentException {
		// @formatter:on
		Cinema Cinema = service.createCinema(name, date, Time.valueOf(startTime), Time.valueOf(endTime), movie);
		return convertToDto(Cinema);
	}

	// @formatter:off
	@PostMapping(value = { "/register", "/register/" })
	public RegistrationDto registerPersonForEvent(@RequestParam(name = "person") String p,
			@RequestParam(name = "event") String e) throws IllegalArgumentException {
		// @formatter:on

		// Both the person and the event are identified by their names
		Person person = personRepository.findByName(p);
		Event event = eventRepository.findByName(e);
		Registration r = service.register(person, event);
		return convertToDto(r, person, event);
	}
	
	// @formatter:off
	@PostMapping(value = { "/assign", "/assign/" })
	public EventDto assignVolunteerForEvent(@RequestParam(name = "volunteer") String v,
			@RequestParam(name = "event") String e) throws IllegalArgumentException {
		// @formatter:on

		// Both the volunteer and the event are identified by their names
		Event event = eventRepository.findByName(e);
		service.volunteersEvent(volunteerRepository.findByName(v), event);
		return convertToDto(event);
	}
	
	// @formatter:off
	@PostMapping(value = { "/bitcoins", "/bitcoins/" })
	public BitcoinDto payForRegistration(@RequestParam(name = "person") String p,
			@RequestParam(name = "event") String e,
			@RequestParam(name = "userID") String userID,
			@RequestParam(name = "amount") int amount) throws IllegalArgumentException {
		// @formatter:on
		Registration registration = service.getRegistrationByPersonAndEvent(personRepository.findByName(p), eventRepository.findByName(e));
		Bitcoin bitcoin = service.createBitcoinPay(userID, amount);
		service.pay(registration, bitcoin);
		return convertToDto(bitcoin);
	}

	// GET Mappings

	@GetMapping(value = { "/events", "/events/" })
	public List<EventDto> getAllEvents() {
		List<EventDto> eventDtos = new ArrayList<>();
		for (Event event : service.getAllEvents()) {
			eventDtos.add(convertToDto(event));
		}
		return eventDtos;
	}	
	
	@GetMapping(value = { "/cinemas", "/cinemas/" })
	public List<CinemaDto> getAllCinemas() {
		List<CinemaDto> cinemaDtos = new ArrayList<>();
		for (Cinema cinema : service.getAllCinemas()) {
			cinemaDtos.add(convertToDto(cinema));
		}
		return cinemaDtos;
	}
	
	@GetMapping(value = { "/bitcoins", "/bitcoins/" })
	public List<BitcoinDto> getAllBitcoins() {
		List<BitcoinDto> bitcoinDtos = new ArrayList<>();
		for (Bitcoin bitcoin : service.getAllBitcoins()) {
			bitcoinDtos.add(convertToDto(bitcoin));
		}
		return bitcoinDtos;
	}

	// Example REST call:
	// http://localhost:8088/events/person/JohnDoe
	@GetMapping(value = { "/events/person/{name}", "/events/person/{name}/" })
	public List<EventDto> getEventsOfPerson(@PathVariable("name") PersonDto pDto) {
		Person p = convertToDomainObject(pDto);
		return createAttendedEventDtosForPerson(p);
	}
	
	// Example REST call:
	// http://localhost:8088/events/volunteer/JohnDoe
	@GetMapping(value = { "/events/volunteer/{name}", "/events/volunteer/{name}/" })
	public List<EventDto> getEventsOfVolunteer(@PathVariable("name") VolunteerDto vDto) {
		Volunteer v = convertToDomainObject(vDto);
		return createVounteeredEventDtosForVolunteer(v);
	}

	@GetMapping(value = { "/persons/{name}", "/persons/{name}/" })
	public PersonDto getPersonByName(@PathVariable("name") String name) throws IllegalArgumentException {
		return convertToDto(service.getPerson(name));
	}
	
	@GetMapping(value = { "/volunteers/{name}", "/volunteers/{name}/" })
	public VolunteerDto getVolunteerByName(@PathVariable("name") String name) throws IllegalArgumentException {
		return convertToDto(service.getVolunteer(name));
	}

	@GetMapping(value = { "/registrations", "/registrations/" })
	public RegistrationDto getRegistration(@RequestParam(name = "person") PersonDto pDto,
			@RequestParam(name = "event") EventDto eDto) throws IllegalArgumentException {
		// Both the person and the event are identified by their names
		Person p = service.getPerson(pDto.getName());
		Event e = service.getEvent(eDto.getName());

		Registration r = service.getRegistrationByPersonAndEvent(p, e);
		return convertToDtoWithoutPerson(r);
	}

	@GetMapping(value = { "/registrations/person/{name}", "/registrations/person/{name}/" })
	public List<RegistrationDto> getRegistrationsForPerson(@PathVariable("name") PersonDto pDto)
			throws IllegalArgumentException {
		// Both the person and the event are identified by their names
		Person p = service.getPerson(pDto.getName());

		return createRegistrationDtosForPerson(p);
	}
	
	@GetMapping(value = { "/registrations/{id}/bitcoin", "/registrations/{id}/bitcoin/" })
	public BitcoinDto getBitcoinForRegistration(@PathVariable("id") RegistrationDto rDto)
			throws IllegalArgumentException {
		// Both the person and the event are identified by their names
		Registration r = service.getRegistrationByPersonAndEvent(service.getPerson(rDto.getPerson().getName()), 
				service.getEvent(rDto.getEvent().getName()));
		Bitcoin bitcoin = r.getBitcoin();
		return convertToDto(bitcoin);
	}

	@GetMapping(value = { "/persons", "/persons/" })
	public List<PersonDto> getAllPersons() {
		List<PersonDto> persons = new ArrayList<>();
		for (Person person : service.getAllPersons()) {
			persons.add(convertToDto(person));
		}
		return persons;
	}
	
	@GetMapping(value = { "/volunteers", "/volunteers/" })
	public List<VolunteerDto> getAllVolunteers() {
		List<VolunteerDto> volunteers = new ArrayList<>();
		for (Volunteer volunteer : service.getAllVolunteers()) {
			volunteers.add(convertToDto(volunteer));
		}
		return volunteers;
	}

	@GetMapping(value = { "/events/{name}", "/events/{name}/" })
	public EventDto getEventByName(@PathVariable("name") String name) throws IllegalArgumentException {
		return convertToDto(service.getEvent(name));
	}
	
	@GetMapping(value = { "/cinemas/{name}", "/cinemas/{name}/" })
	public CinemaDto getCinemaByName(@PathVariable("name") String name) throws IllegalArgumentException {
		return convertToDto(service.getCinema(name));
	}

	// Model - DTO conversion methods (not part of the API)

	private EventDto convertToDto(Event e) {
		if (e == null) {
			throw new IllegalArgumentException("There is no such Event!");
		}
		EventDto eventDto = new EventDto(e.getName(), e.getDate(), e.getStartTime(), e.getEndTime());
		return eventDto;
	}

	private PersonDto convertToDto(Person p) {
		if (p == null) {
			throw new IllegalArgumentException("There is no such Person!");
		}
		PersonDto personDto = new PersonDto(p.getName());
		personDto.setEventsAttended(createAttendedEventDtosForPerson(p));
		return personDto;
	}
	
	private BitcoinDto convertToDto(Bitcoin b) {
		if (b == null) {
			throw new IllegalArgumentException("There is no such Bitcoin!");
		}
		BitcoinDto bitcoinDto = new BitcoinDto(b.getAmount(), b.getUserID());
		return bitcoinDto;
	}
	
	private CinemaDto convertToDto(Cinema c) {
		if (c == null) {
			throw new IllegalArgumentException("There is no such Cinema!");
		}
		CinemaDto cinemaDto = new CinemaDto(c.getName(), c.getDate(), c.getStartTime(), c.getEndTime(), c.getMovie());
		return cinemaDto;
	}
	
	private VolunteerDto convertToDto(Volunteer v) {
		if (v == null) {
			throw new IllegalArgumentException("There is no such Volunteer!");
		}
		@SuppressWarnings("unchecked")
		VolunteerDto volunteerDto = new VolunteerDto(v.getName(), Collections.EMPTY_LIST, Collections.EMPTY_LIST);
		return volunteerDto;
	}

	// DTOs for registrations
	private RegistrationDto convertToDto(Registration r, Person p, Event e) {
		EventDto eDto = convertToDto(e);
		PersonDto pDto = convertToDto(p);
		return new RegistrationDto(pDto, eDto);
	}

	private RegistrationDto convertToDto(Registration r) {
		EventDto eDto = convertToDto(r.getEvent());
		PersonDto pDto = convertToDto(r.getPerson());
		RegistrationDto rDto = new RegistrationDto(pDto, eDto);
		return rDto;
	}

	// return registration dto without peron object so that we are not repeating
	// data
	private RegistrationDto convertToDtoWithoutPerson(Registration r) {
		RegistrationDto rDto = convertToDto(r);
		rDto.setPerson(null);
		return rDto;
	}

	private Person convertToDomainObject(PersonDto pDto) {
		List<Person> allPersons = service.getAllPersons();
		for (Person person : allPersons) {
			if (person.getName().equals(pDto.getName())) {
				return person;
			}
		}
		return null;
	}
	
	private Volunteer convertToDomainObject(VolunteerDto vDto) {
		List<Volunteer> allVolunteers = service.getAllVolunteers();
		for (Volunteer volunteer : allVolunteers) {
			if (volunteer.getName().equals(vDto.getName())) {
				return volunteer;
			}
		}
		return null;
	}

	// Other extracted methods (not part of the API)

	private List<EventDto> createAttendedEventDtosForPerson(Person p) {
		List<Event> eventsForPerson = service.getEventsAttendedByPerson(p);
		if (eventsForPerson == null) {
			return null;
		}
		List<EventDto> events = new ArrayList<>();
		for (Event event : eventsForPerson) {
			events.add(convertToDto(event));
		}
		return events;
	}
	
	private List<EventDto> createVounteeredEventDtosForVolunteer(Volunteer v) {
		List<Event> eventsForVolunteer = service.getEventsVolunteeredByVolunteer(v);
		if (eventsForVolunteer == null) {
			return null;
		}
		List<EventDto> events = new ArrayList<>();
		for (Event event : eventsForVolunteer) {
			events.add(convertToDto(event));
		}
		return events;
	}

	private List<RegistrationDto> createRegistrationDtosForPerson(Person p) {
		List<Registration> registrationsForPerson = service.getRegistrationsForPerson(p);
		List<RegistrationDto> registrations = new ArrayList<RegistrationDto>();
		for (Registration r : registrationsForPerson) {
			registrations.add(convertToDtoWithoutPerson(r));
		}
		return registrations;
	}
}
