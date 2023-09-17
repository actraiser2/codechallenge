package com.josemiguel.codechallenge;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest()
@AutoConfigureMockMvc
@Slf4j
@Testcontainers
class CodechallengeApplicationTests {

	 @Autowired private MockMvc mockMvc;
	 
	 @Container
	 private static MySQLContainer<?> database = new MySQLContainer<>("mysql:8.0.34");
	 
	 @BeforeAll
	 public static void setUpEnv() throws Exception {
		 //Adding a account in the database for running the tests
		 database.start();
		 
		 
	 
	 }
	 
	 @AfterAll
	 public static void releaseEnv() throws Exception {
		 database.stop();
	 }
	 
	 @DynamicPropertySource
	 static void databaseProperties(DynamicPropertyRegistry registry) {
	    registry.add("spring.datasource.url", database::getJdbcUrl);
	    registry.add("spring.datasource.username", database::getUsername);
	    registry.add("spring.datasource.password", database::getPassword);
	    registry.add("spring.datasource.driverClassName", database::getDriverClassName);
	 }
	
	
	@Test
	void testBusinessFlowA() throws Exception {
		
		/*Given: A transaction that is not stored in our system
		When: I check the status from any channel
		Then: The system returns the status 'INVALID'*/
		
		String body = "{\n"
		 		+ "    \"accountName\":\"prueba\",\n"
		 		+ "    \"iban\":\"ES9820385778983000760236\"\n"
		 		+ "}";
		mockMvc.perform(post("/api/v1/accounts").
			 contentType(MediaType.APPLICATION_JSON).
			 content(body)).andDo(r -> {
			
		});
		 
	}
	
	@Test
	void testBusinessFlowB() throws Exception {
		
		//Invoking the endpoint transaction for inserting the test transaction
		String dateBeforeToday = DateTimeFormatter.ISO_DATE_TIME.format(LocalDateTime.now().minusDays(1));
		double amount = 200;
		double fee = 1;
		
		String transactionBody = "{\n"
				+ "\"reference\":\"12345\",\n"
				+ "\"account_iban\":\"ES9820385778983000760236\",\n"
				+ "\"date\":\"" + dateBeforeToday + "\",\n"
				+ "\"amount\":" + amount + ",\n"
				+ "\"fee\": " + fee + ",\n"
				+ "\"description\":\"Restaurant payment\"\n"
				+ "}";
		
		
		mockMvc.perform(post("/api/v1/transactions").
			 contentType(MediaType.APPLICATION_JSON).
			 content(transactionBody)).
		 	 andExpect(status().is(201));
		
		
		/*Given: A transaction that is stored in our system
		When: I check the status from CLIENT or ATM channel
		And the transaction date is before today
		Then: The system returns the status 'SETTLED'
		And the amount substracting the fee*/
		
		
	    
		 String transactionStatusBody = "{\n"
		 		+ "    \"reference\":\"12345\",\n"
		 		+ "    \"channel\":\"ATM\"\n"
		 		+ "}";
			 
		 String response = mockMvc.perform(post("/api/v1/transactionStatus").
			 contentType(MediaType.APPLICATION_JSON).
			 content(transactionStatusBody)).
		 	 andDo(r -> {
		 		 
			 }).
		 	 andExpect(status().is(200)).andReturn().getResponse().getContentAsString();
			 
		
	}
	
	
}
