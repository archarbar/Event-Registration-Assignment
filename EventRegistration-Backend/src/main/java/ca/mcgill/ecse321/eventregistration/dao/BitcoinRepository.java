package ca.mcgill.ecse321.eventregistration.dao;

import org.springframework.data.repository.CrudRepository;

import ca.mcgill.ecse321.eventregistration.model.Cinema;

public interface BitcoinRepository extends CrudRepository<Cinema, String> {

}
