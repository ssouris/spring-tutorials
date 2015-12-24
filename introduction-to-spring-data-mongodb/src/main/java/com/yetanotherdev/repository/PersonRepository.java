package com.yetanotherdev.repository;

import com.yetanotherdev.domain.Person;
import org.springframework.data.mongodb.repository.support.QueryDslMongoRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface PersonRepository extends PagingAndSortingRepository<Person, String>, QueryDslPredicateExecutor<Person> {

    Optional<Person> findByFirstname(String firstName);

    Optional<Person> findByLastname(String firstName);

}
