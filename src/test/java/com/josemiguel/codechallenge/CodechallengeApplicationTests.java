package com.josemiguel.codechallenge;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.josemiguel.codechallenge.infrastructure.config.CacheConfig;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@TestInstance(Lifecycle.PER_CLASS)
@SpringBootTest()
//@WithMockUser(username = "codechallenge_test_user", authorities = "SCOPE_read:accounts")
class CodechallengeApplicationTests {

	@Autowired MockMvc mockMvc;
	@MockBean JwtDecoder jwtDecoder;
	
	@Sql(scripts = "classpath:/sql/inserts.sql")
	@Test
	void testBusinescsFlowA() throws Exception {
		/*Given: A transaction that is not stored in our system
		When: I check the status from any channel
		Then: The system returns the status 'INVALID'*/
		
		String body = "{\r\n"
				+ "    \"accountName\":\"ssSsabadell Cuenta pepito\",\r\n"
				+ "    \"iban\":\"923456789555\",\r\n"
				+ "    \"holderName\":\"Jose Miguel\"\r\n"
				+ "}";
		mockMvc.perform(post("/api/v1/accounts").
			 contentType(MediaType.APPLICATION_JSON).
			 content(body));
		
		mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/accounts")).
			//andDo(MockMvcResultHandlers.print()).
			andExpect(jsonPath("$", Matchers.hasSize(3)));
		 
	}
	
	//@Test
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
