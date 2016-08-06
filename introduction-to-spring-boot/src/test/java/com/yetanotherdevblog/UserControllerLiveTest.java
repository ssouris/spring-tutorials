package com.yetanotherdevblog;

import com.jayway.restassured.http.ContentType;
import com.yetanotherdevblog.domain.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;

import static com.jayway.restassured.RestAssured.get;
import static com.jayway.restassured.RestAssured.with;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BootApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerLiveTest {

	private User aUser;

	@LocalServerPort
	private int port;

	private String getBaseUrl() {
		return "http://localhost:" + port;
	}

	/**
	 * Insert a user before every test
	 */
	@Before
	public void beforeHookActions() {
		aUser = insertUser("Yet", "Another", "dev");
		Assert.notNull(aUser);
	}

	@Test
	public void test_GetUser() {


		User getUser = get(getBaseUrl() + "/api/users/{0}", aUser.getId()).as(User.class);

		Assert.notNull(getUser);
		Assert.isTrue(aUser.getId().equals(getUser.getId()));
	}

	@Test
	public void test_GetUsers() {
		Map<String, String> requestParams = new HashMap<>();
		requestParams.put("page", "0");
		requestParams.put("size", "2");

		String response = with()
				.basePath(getBaseUrl())
				.get(getBaseUrl()+"/api/users?page={page}&size={size}", requestParams)
				.asString();

		Assert.isTrue(response.contains(aUser.getFirstName()));
		Assert.isTrue(response.contains(aUser.getLastName()));
		Assert.isTrue(response.contains(aUser.getUsername()));
	}

	@Test
	public void test_PutUser() {
		User updateDto = new User();
		updateDto.setUsername("Just");
		updateDto.setFirstName("Update");
		updateDto.setLastName("Me");

		User updatedUser = updateUser(aUser.getId(), updateDto);

		Assert.notNull(updatedUser);
		Assert.isTrue(updatedUser.getFirstName().equals(updateDto.getFirstName()));
		Assert.isTrue(updatedUser.getLastName().equals(updateDto.getLastName()));
		Assert.isTrue(updatedUser.getUsername().equals(updateDto.getUsername()));
	}

	@Test
	public void test_PutUserFailure() {
		User updateDto = new User();
		updateDto.setUsername("Just");
		updateDto.setFirstName("Update");
		updateDto.setLastName("Me");

		boolean notFound = updateUserFailure(10000L, updateDto);

		assertThat(notFound).isEqualTo(true);
	}

	private User insertUser(String firstName, String lastName, String username) {
		User aUser = new User();
		aUser.setFirstName(firstName);
		aUser.setLastName(lastName);
		aUser.setUsername(username);

		return with().body(aUser)
            	.contentType(ContentType.JSON)
				.baseUri(getBaseUrl())
            	.post("/api/users")
            	.andReturn()
            	.as(User.class);
	}

	private User updateUser(Long userId, User updateDto) {
		return with()
				.body(updateDto)
				.contentType(ContentType.JSON)
				.put("http://localhost:"+port+"/api/users/{0}", userId)
				.andReturn()
				.as(User.class);
	}


	private boolean updateUserFailure(Long userId, User updateDto) {
		return with()
				.body(updateDto)
				.contentType(ContentType.JSON)
				.put("http://localhost:"+port+"/api/users/{0}", userId)
				.andReturn()
				.statusCode() == 404;
	}

}
