package com.yetanotherdev.controller;

import com.yetanotherdev.domain.Person;
import com.yetanotherdev.repository.PersonRepository;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Pageable;

@RestController
@RequestMapping("/api/v1/people")
public class PersonController {

    private final PersonRepository personRepository;

    public PersonController(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @GetMapping
    public Page<Person> getPeople(Pageable pageable) {
        return personRepository.findAll(pageable);
    }

    @PostMapping
    public Person savePerson(@RequestBody Person person) {
        return personRepository.save(person);
    }

    @PutMapping("/{personId}")
    public Person updatePerson(@PathVariable String personId, @RequestBody Person person) {
        Person thePerson = personRepository.findOne(personId);
        thePerson.setFirstname(person.getFirstname());
        thePerson.setLastname(person.getLastname());
        return thePerson;
    }

}
