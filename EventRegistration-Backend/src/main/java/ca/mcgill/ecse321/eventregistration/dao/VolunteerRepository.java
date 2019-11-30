package ca.mcgill.ecse321.eventregistration.dao;

import org.springframework.data.repository.CrudRepository;

import ca.mcgill.ecse321.eventregistration.model.Volunteer;

public interface VolunteerRepository extends CrudRepository<Volunteer, String> {

}
