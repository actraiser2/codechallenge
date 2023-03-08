package com.josemiguel.codechallenge;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.jayway.jsonpath.JsonPath;

import lombok.extern.slf4j.Slf4j;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(Lifecycle.PER_CLASS)
@Slf4j
@Disabled
class CodechallengeApplicationTests {

	 @Autowired private MockMvc mockMvc;
	 
	 @BeforeAll
	 public void setUpEnvironment() throws Exception {
		 //Adding a account in the database for running the tests
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
	void testBusinessFlowA() throws Exception {
		
		/*Given: A transaction that is not stored in our system
		When: I check the status from any channel
		Then: The system returns the status 'INVALID'*/
		
		 String transactionStatusBody = "{\n"
		 		+ "    \"reference\":\"12345\"\n"
		 		+ "}";
		 
		 String response = mockMvc.perform(post("/api/v1/transactionStatus").
			 contentType(MediaType.APPLICATION_JSON).
			 content(transactionStatusBody)).
		 	 andExpect(status().is(200)).andReturn().getResponse().getContentAsString();
		 
		
		 String status = JsonPath.parse(response).read("$.status");
		 
		 Assertions.assertTrue(status.equals("INVALID"));
		 
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
			 
			
		 String status = JsonPath.parse(response).read("$.status");
		 Double resultAmount = JsonPath.parse(response).read("$.amount", Double.class);
		 
		 Assertions.assertTrue(status.equals("SETTLED"));
		 Assertions.assertTrue(resultAmount == amount - fee);
		 
	}
	
	
	@Test
	void testBusinessFlowC() throws Exception {
		
		//Invoking the endpoint transaction for inserting the test transaction
		String dateBeforeToday = DateTimeFormatter.ISO_DATE_TIME.format(LocalDateTime.now().minusDays(1));
		double amount = 200;
		double fee = 1;
		
		String transactionBody = "{\n"
				+ "\"reference\":\"12346\",\n"
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
		When: I check the status from INTERNAL channel
		And the transaction date is before today
		Then: The system returns the status 'SETTLED'
		And the amount
		And the fee*/
		
		
	    
		 String transactionStatusBody = "{\n"
		 		+ "    \"reference\":\"12346\",\n"
		 		+ "    \"channel\":\"INTERNAL\"\n"
		 		+ "}";
			 
		 String response = mockMvc.perform(post("/api/v1/transactionStatus").
			 contentType(MediaType.APPLICATION_JSON).
			 content(transactionStatusBody)).
		 	 andDo(r -> {
		 		 
			 }).
		 	 andExpect(status().is(200)).andReturn().getResponse().getContentAsString();
			 
			
		 String status = JsonPath.parse(response).read("$.status");
		 Double resultAmount = JsonPath.parse(response).read("$.amount", Double.class);
		 Double resultFee = JsonPath.parse(response).read("$.fee", Double.class);
		 
		 Assertions.assertTrue(status.equals("SETTLED"));
		 Assertions.assertTrue(resultAmount == amount);
		 Assertions.assertTrue(resultFee == fee);
		 
	}
	
	@Test
	void testBusinessFlowD() throws Exception {
		
		//Invoking the endpoint transaction for inserting the test transaction
		String today = DateTimeFormatter.ISO_DATE_TIME.format(LocalDateTime.now());
		double amount = 200;
		double fee = 1;
		
		String transactionBody = "{\n"
				+ "\"reference\":\"12347\",\n"
				+ "\"account_iban\":\"ES9820385778983000760236\",\n"
				+ "\"date\":\"" + today + "\",\n"
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
		And the transaction date is equals to today
		Then: The system returns the status 'PENDING'
		And the amount substracting the fee*/
		
		
	    
		 String transactionStatusBody = "{\n"
		 		+ "    \"reference\":\"12347\",\n"
		 		+ "    \"channel\":\"CLIENT\"\n"
		 		+ "}";
			 
		 String response = mockMvc.perform(post("/api/v1/transactionStatus").
			 contentType(MediaType.APPLICATION_JSON).
			 content(transactionStatusBody)).
		 	 andDo(r -> {
		 		 
			 }).
		 	 andExpect(status().is(200)).andReturn().getResponse().getContentAsString();
			 
			
		 String status = JsonPath.parse(response).read("$.status");
		 Double resultAmount = JsonPath.parse(response).read("$.amount", Double.class);
		 
		 Assertions.assertTrue(status.equals("PENDING"));
		 Assertions.assertTrue(resultAmount == amount - fee);
		 
	}
	
	@Test
	void testBusinessFlowE() throws Exception {
		
		//Invoking the endpoint transaction for inserting the test transaction
		String today = DateTimeFormatter.ISO_DATE_TIME.format(LocalDateTime.now());
		double amount = 200;
		double fee = 1;
		
		String transactionBody = "{\n"
				+ "\"reference\":\"12348\",\n"
				+ "\"account_iban\":\"ES9820385778983000760236\",\n"
				+ "\"date\":\"" + today + "\",\n"
				+ "\"amount\":" + amount + ",\n"
				+ "\"fee\": " + fee + ",\n"
				+ "\"description\":\"Restaurant payment\"\n"
				+ "}";
		
		
		mockMvc.perform(post("/api/v1/transactions").
			 contentType(MediaType.APPLICATION_JSON).
			 content(transactionBody)).
		 	 andExpect(status().is(201));
		
		
		/*Given: A transaction that is stored in our system
		When: I check the status from INTERNAL channel
		And the transaction date is equals to today
		Then: The system returns the status 'PENDING'
		And the amount
		And the fee*/
		
		
	    
		 String transactionStatusBody = "{\n"
		 		+ "    \"reference\":\"12348\",\n"
		 		+ "    \"channel\":\"INTERNAL\"\n"
		 		+ "}";
			 
		 String response = mockMvc.perform(post("/api/v1/transactionStatus").
			 contentType(MediaType.APPLICATION_JSON).
			 content(transactionStatusBody)).
		 	 andDo(r -> {
		 		 
			 }).
		 	 andExpect(status().is(200)).andReturn().getResponse().getContentAsString();
			 
			
		 String status = JsonPath.parse(response).read("$.status");
		 Double resultAmount = JsonPath.parse(response).read("$.amount", Double.class);
		 Double resultFee = JsonPath.parse(response).read("$.fee", Double.class);
		 
		 
		 Assertions.assertTrue(status.equals("PENDING"));
		 Assertions.assertTrue(resultAmount == amount);
		 Assertions.assertTrue(resultFee == fee);
		 
	}
	
	@Test
	void testBusinessFlowF() throws Exception {
		
		//Invoking the endpoint transaction for inserting the test transaction
		String today = DateTimeFormatter.ISO_DATE_TIME.format(LocalDateTime.now().plusDays(1));
		double amount = 200;
		double fee = 1;
		
		String transactionBody = "{\n"
				+ "\"reference\":\"12349\",\n"
				+ "\"account_iban\":\"ES9820385778983000760236\",\n"
				+ "\"date\":\"" + today + "\",\n"
				+ "\"amount\":" + amount + ",\n"
				+ "\"fee\": " + fee + ",\n"
				+ "\"description\":\"Restaurant payment\"\n"
				+ "}";
		
		
		mockMvc.perform(post("/api/v1/transactions").
			 contentType(MediaType.APPLICATION_JSON).
			 content(transactionBody)).
		 	 andExpect(status().is(201));
		
		
		/*Given: A transaction that is stored in our system
		When: I check the status from CLIENT channel
		And the transaction date is greater than today
		Then: The system returns the status 'FUTURE'
		And the amount substracting the fee*/
		
		
	    
		 String transactionStatusBody = "{\n"
		 		+ "    \"reference\":\"12349\",\n"
		 		+ "    \"channel\":\"CLIENT\"\n"
		 		+ "}";
			 
		 String response = mockMvc.perform(post("/api/v1/transactionStatus").
			 contentType(MediaType.APPLICATION_JSON).
			 content(transactionStatusBody)).
		 	 andDo(r -> {
		 		 
			 }).
		 	 andExpect(status().is(200)).andReturn().getResponse().getContentAsString();
			 
			
		 String status = JsonPath.parse(response).read("$.status");
		 Double resultAmount = JsonPath.parse(response).read("$.amount", Double.class);
		 
		 
		 Assertions.assertTrue(status.equals("FUTURE"));
		 Assertions.assertTrue(resultAmount == amount - fee);
		 
	}
	
	
	@Test
	void testBusinessFlowG() throws Exception {
		
		//Invoking the endpoint transaction for inserting the test transaction
		String today = DateTimeFormatter.ISO_DATE_TIME.format(LocalDateTime.now().plusDays(1));
		double amount = 200;
		double fee = 1;
		
		String transactionBody = "{\n"
				+ "\"reference\":\"12350\",\n"
				+ "\"account_iban\":\"ES9820385778983000760236\",\n"
				+ "\"date\":\"" + today + "\",\n"
				+ "\"amount\":" + amount + ",\n"
				+ "\"fee\": " + fee + ",\n"
				+ "\"description\":\"Restaurant payment\"\n"
				+ "}";
		
		
		mockMvc.perform(post("/api/v1/transactions").
			 contentType(MediaType.APPLICATION_JSON).
			 content(transactionBody)).
		 	 andExpect(status().is(201));
		
		
		/*Given: A transaction that is stored in our system
		When: I check the status from ATM channel
		And the transaction date is greater than today
		Then: The system returns the status 'PENDING'
		And the amount substracting the fee*/
		
		
	    
		 String transactionStatusBody = "{\n"
		 		+ "    \"reference\":\"12350\",\n"
		 		+ "    \"channel\":\"ATM\"\n"
		 		+ "}";
			 
		 String response = mockMvc.perform(post("/api/v1/transactionStatus").
			 contentType(MediaType.APPLICATION_JSON).
			 content(transactionStatusBody)).
		 	 andDo(r -> {
		 		 
			 }).
		 	 andExpect(status().is(200)).andReturn().getResponse().getContentAsString();
			 
			
		 String status = JsonPath.parse(response).read("$.status");
		 Double resultAmount = JsonPath.parse(response).read("$.amount", Double.class);
		 
		 
		 Assertions.assertTrue(status.equals("PENDING"));
		 Assertions.assertTrue(resultAmount == amount - fee);
		 
	}
	
	@Test
	void testBusinessFlowH() throws Exception {
		
		//Invoking the endpoint transaction for inserting the test transaction
		String today = DateTimeFormatter.ISO_DATE_TIME.format(LocalDateTime.now().plusDays(1));
		double amount = 200;
		double fee = 1;
		
		String transactionBody = "{\n"
				+ "\"reference\":\"12351\",\n"
				+ "\"account_iban\":\"ES9820385778983000760236\",\n"
				+ "\"date\":\"" + today + "\",\n"
				+ "\"amount\":" + amount + ",\n"
				+ "\"fee\": " + fee + ",\n"
				+ "\"description\":\"Restaurant payment\"\n"
				+ "}";
		
		
		mockMvc.perform(post("/api/v1/transactions").
			 contentType(MediaType.APPLICATION_JSON).
			 content(transactionBody)).
		 	 andExpect(status().is(201));
		
		
		/*Given: A transaction that is stored in our system
		When: I check the status from INTERNAL channel
		And the transaction date is greater than today
		Then: The system returns the status 'FUTURE'
		And the amount
		And the fee*/
		
		
	    
		 String transactionStatusBody = "{\n"
		 		+ "    \"reference\":\"12351\",\n"
		 		+ "    \"channel\":\"INTERNAL\"\n"
		 		+ "}";
			 
		 String response = mockMvc.perform(post("/api/v1/transactionStatus").
			 contentType(MediaType.APPLICATION_JSON).
			 content(transactionStatusBody)).
		 	 andDo(r -> {
		 		 
			 }).
		 	 andExpect(status().is(200)).andReturn().getResponse().getContentAsString();
			 
			
		 String status = JsonPath.parse(response).read("$.status");
		 Double resultAmount = JsonPath.parse(response).read("$.amount", Double.class);
		 Double resultFee = JsonPath.parse(response).read("$.fee", Double.class);
		 
		 
		 Assertions.assertTrue(status.equals("FUTURE"));
		 Assertions.assertTrue(resultAmount == amount);
		 Assertions.assertTrue(resultFee == fee);
		 
	}

}
