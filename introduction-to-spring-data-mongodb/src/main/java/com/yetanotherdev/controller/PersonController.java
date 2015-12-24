package com.yetanotherdev.controller;

import com.yetanotherdev.domain.Person;
import com.yetanotherdev.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Pageable;

@RestController
@RequestMapping("/api/v1/persons")
public class PersonController {

    @Autowired
    private PersonRepository personRepository;

    @RequestMapping(method = RequestMethod.GET)
    public Page<Person> getPeople(Pageable pageable) {
        return personRepository.findAll(pageable);
    }

    @RequestMapping(method = RequestMethod.POST)
    public Person savePersons(@RequestBody Person person) {
        return personRepository.save(person);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{personId}")
    public Person updatePerson(@PathVariable String personId, @RequestBody Person person) {
        Person thePerson = personRepository.findOne(personId);
        thePerson.setFirstname(person.getFirstname());
        thePerson.setLastname(person.getLastname());
        return thePerson;
    }

}
