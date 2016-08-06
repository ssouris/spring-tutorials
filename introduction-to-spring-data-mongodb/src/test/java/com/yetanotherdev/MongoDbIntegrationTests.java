package com.yetanotherdev;

import com.yetanotherdev.domain.Person;
import com.yetanotherdev.domain.QPerson;
import com.yetanotherdev.domain.Venue;
import com.yetanotherdev.repository.PersonRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.GeospatialIndex;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;
import static org.springframework.data.mongodb.core.query.Update.update;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MongoDbApplication.class)
public class MongoDbIntegrationTests {

	@Autowired
	private PersonRepository personRepository;

	@Autowired
	private MongoTemplate mongoTemplate;

	@Before
	public void clearDbBefore() {
		mongoTemplate.dropCollection(Venue.class);
		mongoTemplate.dropCollection(Person.class);
	}

	@Test
	public void test_Insert_Find_RepositoryMethods() {
		String firstName = "firstName"+ RandomStringUtils.random(3);
		String lastName  = "lastName"+ RandomStringUtils.random(3);
		Person p = savePerson(firstName, lastName);

		Assert.isTrue(personRepository.findOne(p.getId()) != null, "Person exists");
		Assert.isTrue(personRepository.findByFirstname(firstName).isPresent(), "Person exists");
		Assert.isTrue(personRepository.findByLastname(lastName).isPresent(), "Person exists");
	}

	@Test
	public void test_RepositoryPage() {
		final int NUMBER_OF_USERS = 6;

		insertPeopleInMongo(NUMBER_OF_USERS);

		Page<Person> people = personRepository.findAll(new PageRequest(0, 4));
		System.out.println(people.getTotalElements());
		Assert.isTrue(people.hasNext());
		Assert.isTrue(people.getTotalElements() == NUMBER_OF_USERS);
	}

	@Test
	public void test_MongoTemplate_UpdateFirst() {
		String firstName = "firstName"+ RandomStringUtils.random(3);
		String lastName  = "lastName"+ RandomStringUtils.random(3);
		savePerson(firstName, lastName);

		final String updateFirstNameTo = "TESTER";
		mongoTemplate.updateFirst(
				query(where("lastname").is(lastName)),
				update("firstname", updateFirstNameTo),
				Person.class);

		Person foundPerson = mongoTemplate.findOne(
				query(where("lastname").is(lastName)),
				Person.class);

		Assert.isTrue(foundPerson != null, "Must not be null");
		Assert.isTrue(foundPerson.getFirstname().equals(updateFirstNameTo), "Must have changed");
	}

	@Test
	public void test_GeoSpacialQuery() {

		mongoTemplate.indexOps(Venue.class).ensureIndex(new GeospatialIndex("location"));

		mongoTemplate.insert(new Venue("Venue 1", new Point(51.10682735591432, -114.11773681640625)));
		mongoTemplate.insert(new Venue("Venue 2", new Point(51.09144802136697, -114.10400390625)));
		mongoTemplate.insert(new Venue("Venue 3", new Point(51.08282186160978, -114.10400390625)));
		mongoTemplate.insert(new Venue("Venue 4", new Point(51.12076493195686, -113.98040771484375)));
		mongoTemplate.insert(new Venue("Venue 5", new Point(50.93939251390387, -113.98040771484375)));

		mongoTemplate.insert(new Venue("Venue 6", new Point(150.93939251390387, 113.98040771484375)));

		long venuesNear = mongoTemplate.count(
				query(where("location")
						.near(new Point(51.00, -114.00))
						.maxDistance(1.00)), Venue.class);

		Assert.isTrue(venuesNear == 5);
	}

	@Test
	public void test_QueryDSL() {
		String firstName = "firstName"+ RandomStringUtils.random(3);
		String lastName  = "lastName"+ RandomStringUtils.random(3);
		savePerson(firstName, lastName);

		QPerson person = QPerson.person;
		Assert.isTrue(personRepository.exists(person.firstname.eq(firstName)));
		Assert.isTrue(personRepository.count(person.lastname.eq(lastName)) == 1);
	}

	private void insertPeopleInMongo(int NUMBER_OF_USERS) {
		for (int i = 0; i < NUMBER_OF_USERS; i++) {
			String firstName = "firstName"+ RandomStringUtils.random(3);
			String lastName  = "lastName"+ RandomStringUtils.random(3);
			savePerson(firstName, lastName);
		}
	}

	private Person savePerson(String firstName, String lastName) {
		Person p = new Person();
		p.setFirstname(firstName);
		p.setLastname(lastName);
		return personRepository.save(p);
	}

}
