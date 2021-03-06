package ca.mcgill.ecse321.eventregistration.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import ca.mcgill.ecse321.eventregistration.model.Bitcoin;
import ca.mcgill.ecse321.eventregistration.model.Event;
import ca.mcgill.ecse321.eventregistration.model.Person;

public interface BitcoinRepository extends CrudRepository<Bitcoin, String> {
	
	  List<Bitcoin> findAll();
	 
}
