package com.youssefabidi.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.youssefabidi.model.Contact;

@Repository
public interface ContactRepository extends CrudRepository<Contact, Long> {
	
	
}
