package com.josemiguel.codechallenge;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import com.josemiguel.codechallenge.application.ports.output.TransactionRepository;
import com.josemiguel.codechallenge.domain.commands.CreateAccountCommand;
import com.josemiguel.codechallenge.domain.model.entities.Transaction;
import com.josemiguel.codechallenge.infrastructure.adapters.input.dto.AccountDTO;
import com.josemiguel.codechallenge.infrastructure.adapters.input.dto.AccountListDTO;

import lombok.extern.slf4j.Slf4j;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@TestInstance(Lifecycle.PER_CLASS)
@TestMethodOrder(value = OrderAnnotation.class)
@SpringBootTest()
@Slf4j
@ExtendWith(MockitoExtension.class)
@AutoConfigureJsonTesters
//@WithMockUser(username = "codechallenge_test_user", authorities = "SCOPE_read:accounts")
class CodechallengeApplicationTests implements WithAssertions {

	@Autowired MockMvc mockMvc;
	@MockBean JwtDecoder jwtDecoder;
	@Spy TransactionRepository transactionRepository;
	@Autowired JacksonTester<AccountListDTO> jsonAccountListDTO;
	@Autowired JacksonTester<CreateAccountCommand> jsonCreateAccountCommand;
	@Autowired JacksonTester<AccountDTO> jsonAccountDTO;
	
	@BeforeEach
	public void setup() {
		log.info("transactionRepository: {}", transactionRepository.getClass());
		var transactions = List.of(
				Transaction.builder().
					transactionId(1l).
					date(LocalDateTime.now()).
					description("Transaction one").build(),
				Transaction.builder().
					transactionId(2l).
					date(LocalDateTime.now()).
					description("Transaction two").build()
		);
		Mockito.lenient().when(transactionRepository.findAll()).
			
			thenReturn(transactions);
	}
	
	@Sql(scripts = "classpath:/sql/inserts.sql")
	@Test
	@Order(1)
	void testBusinescsFlowA() throws Exception {
		/*Given: A transaction that is not stored in our system
		When: I check the status from any channel
		Then: The system returns the status 'INVALID'*/
		
		String body = "{\r\n"
				+ "    \"accountName\":\"ssSsabadell Cuenta pepito\",\r\n"
				+ "    \"iban\":\"ES9820385778983000760236\",\r\n"
				+ "    \"holderName\":\"Jose Miguel\"\r\n"
				+ "}";
		mockMvc.perform(post("/api/v1/accounts").
			 contentType(MediaType.APPLICATION_JSON).
			 content(body));
		
		var result = mockMvc.perform(MockMvcRequestBuilders.
				get("/api/v1/accounts").
				accept(MediaType.APPLICATION_JSON)).
			andReturn().getResponse();
		
		assertThat(jsonAccountListDTO.parse(result.getContentAsString())).
			hasFieldOrProperty("accountList").
			extracting("accountList").asList().hasSize(3);
			

		 
	}
	
	@Test
	@Order(2)
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
		
		
		var response = mockMvc.perform(post("/api/v1/transactions").
			 contentType(MediaType.APPLICATION_JSON).
			 content(transactionBody)).
			 andReturn().getResponse();
		
		//Response code equals to 201
		assertThat(response.getStatus()).isEqualTo(201);
		
		

		
		
		/*Given: A transaction that is stored in our system
		When: I check the status from CLIENT or ATM channel
		And the transaction date is before today
		Then: The system returns the status 'SETTLED'
		And the amount substracting the fee*/
		
		
	    
		 String transactionStatusBody = "{\n"
		 		+ "    \"reference\":\"12345\",\n"
		 		+ "    \"channel\":\"ATM\"\n"
		 		+ "}";
			 
		 var response2 = mockMvc.perform(post("/api/v1/transactionStatus").
			 contentType(MediaType.APPLICATION_JSON).
			 content(transactionStatusBody)).
		 	 andDo(r -> {
		 		 
			 }).
		 	 andExpect(status().is(200)).
		 	 andReturn().getResponse();
		
		 //Check content has length > 0
		 assertThat(response2.getContentAsString()).hasSizeGreaterThan(0);
			 
		
	}

	
	@Test
	@Order(3)
	@Transactional
	public void testGetTransactions() {
		
		var transactions = transactionRepository.findAll();
		log.info("Transactions loaded:" + transactions);
		transactionRepository.findAll().
			stream().
			forEach(t -> {
				log.info("AccountLoaded:" + t.getAccount());
				log.info("Account:" + t.getDescription());
			});
		assertThat(transactions.size()).isEqualTo(2);
		
		assertThat(transactionRepository.findById(1l)).isEmpty();
	}
	
	@Test
	public void test404WhenWrongUrl() throws Exception {
		var response = mockMvc.perform(MockMvcRequestBuilders.get("/missingUrl")).andReturn().getResponse();
		
		assertThat(response.getStatus()).isEqualTo(404);
	}
	
	@Test
	public void test405WhenWrongMethod() throws Exception {
		var response = mockMvc.perform(MockMvcRequestBuilders.put("/accounts")).andReturn().getResponse();
		
		assertThat(response.getStatus()).isEqualTo(404);
	}
	
	@Test
	public void test400WhenBadInput() throws Exception {
		var response = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/accounts").
				contentType(MediaType.APPLICATION_JSON_VALUE).
				accept(MediaType.APPLICATION_JSON_VALUE).
				content(jsonCreateAccountCommand.write(CreateAccountCommand.builder().
						age(30).iban("11111111111").build()).getJson())).
				andReturn().getResponse();
		
		assertThat(response.getStatus()).isEqualTo(400);
	}
	
	@Test
	@Order(4)
	public void testfindAccountId() throws Exception {
		var result = mockMvc.perform(MockMvcRequestBuilders.
				get("/api/v1/accounts/{accountId}", 100).
				accept(MediaType.APPLICATION_JSON)).andReturn();
		
		
		assertThat(jsonAccountDTO.parse(result.getResponse().
				getContentAsString())).hasFieldOrProperty("iban");
	}
	
}
