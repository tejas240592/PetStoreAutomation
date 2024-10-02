package api.test;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.github.javafaker.Faker;

import api.endpoints.UserEndPoints;
import api.payload.User;
import io.restassured.response.Response;

public class UserTests {
	
	Faker faker = new Faker();
	User userPayload = new User();
	
	public Logger logger;
	
	@BeforeClass
	public void setup() {
		
		userPayload.setId(faker.idNumber().hashCode());
		userPayload.setUsername(faker.name().username());;
		userPayload.setFirstName(faker.name().firstName());
		userPayload.setLastName(faker.name().lastName());
		userPayload.setEmail(faker.internet().safeEmailAddress());
		userPayload.setPassword(faker.internet().password(5,10));
		userPayload.setPhone(faker.phoneNumber().cellPhone());
		
		//logs
		logger= LogManager.getLogger(this.getClass());
		logger.debug("debugging..............");
		
	}
	
	@Test(priority=1)
	public void testPostUser() {
		
		logger.info("*******Creating user **********");
		Response response = UserEndPoints.createUser(userPayload);
		response.then().log().all();
		
		Assert.assertEquals(response.getStatusCode(), 200);
	
		logger.info("*******User is created **********");
	
	}
	
	@Test(priority=2)
	public void testGetUser() {
		
		logger.info("******* Reading user info **********");
		Response response = UserEndPoints.readUser(this.userPayload.getUsername());
		response.then().log().all();
		Assert.assertEquals(response.getStatusCode(),200);
		
		logger.info("******* User info is displayed **********");
	}
	
	@Test(priority=3)
	public void testUpdateUser() {
		
		logger.info("******* Updating User **********");
		//update data using payload
		userPayload.setFirstName(faker.name().firstName());
		userPayload.setLastName(faker.name().lastName());
		userPayload.setEmail(faker.internet().safeEmailAddress());
		
		Response response = UserEndPoints.updateUser(this.userPayload.getUsername(),userPayload);
		response.then().log().body();
		
		Assert.assertEquals(response.getStatusCode(), 200);
		
		logger.info("*******User is updated **********");
		
		//checking data after update
		Response responseAfterupdate = UserEndPoints.readUser(this.userPayload.getUsername());
		Assert.assertEquals(responseAfterupdate.getStatusCode(),200);
	}
	
	@Test(priority=4)
	public void testDeleteUser() {
		
		logger.info("******* Deleting User **********");
		Response response = UserEndPoints.deleteUser(this.userPayload.getUsername());
		Assert.assertEquals(response.getStatusCode(),200);
	
		logger.info("******* User deleted **********");
	}

}
