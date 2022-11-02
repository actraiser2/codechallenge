package com.josemiguel.codechallenge;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.jayway.jsonpath.JsonPath;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;

@SpringBootTest
@AutoConfigureMockMvc
@CucumberContextConfiguration()
public class BusinessFlowFeaturesTest {

	@Autowired MockMvc mockMvc;
	private String lastResponse;
	private String reference;
	private Double amount;
	private String operationDate;
	private double fee;
	
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
	
	@Given("^The transaction date is before today$")
	public void transaction_date_before_today() {
		operationDate = DateTimeFormatter.ISO_DATE_TIME.format(LocalDateTime.now().minusDays(1));
	}
	
	@Given("^The transaction date is equals to today$")
	public void transaction_is_equals_today() {
		operationDate = DateTimeFormatter.ISO_DATE_TIME.format(LocalDateTime.now());
	}
	
	@Given("^A transaction that is not stored in our system$")
	public void transaction_not_stored() {
		this.reference = "XXXXX";
	}
	
	@Given("^A transaction that is stored in our system$")
	public void transaction_stored() throws Exception {
		reference = UUID.randomUUID().toString();
		
		amount = 200d;
		fee = 1;
		
		String transactionBody = "{\n"
				+ "\"reference\":\"" + reference + "\",\n"
				+ "\"account_iban\":\"ES9820385778983000760236\",\n"
				+ "\"date\":\"" + operationDate + "\",\n"
				+ "\"amount\":" + amount + ",\n"
				+ "\"fee\": " + fee + ",\n"
				+ "\"description\":\"Restaurant payment\"\n"
				+ "}";
		
		
		mockMvc.perform(post("/api/v1/transactions").
			 contentType(MediaType.APPLICATION_JSON).
			 content(transactionBody)).
		 	 andExpect(status().is(201));
		
	}
	
	
	
	
	@When("^I check the status from (\\w+) channel$")
    public void i_check_status_from_channel(String channel) throws Throwable {
		String transactionStatusBody = null;
		
		if (channel.equals("any")) {
			transactionStatusBody = "{\n"
		 		+ "    \"reference\":\""+ reference + "\"\n"
		 		+ "}";
		}
		else {
			transactionStatusBody = "{\n"
		 		+ "    \"reference\":\""+ reference + "\",\n"
		 		+ "    \"channel\":\"" + channel + "\"\n"
		 		+ "}";
		}
	
		 
		lastResponse = mockMvc.perform(post("/api/v1/transactionStatus").
			 contentType(MediaType.APPLICATION_JSON).
			 content(transactionStatusBody)).
		 	 andExpect(status().is(200)).andReturn().getResponse().getContentAsString();
		
    }
	
	@Then("^The system returns the status '(\\w+)'$")
    public void se_recupera_el_status(String desiredStatus) throws Throwable {
        System.out.println("Execution step then:" + desiredStatus);
		
        String realStatus = JsonPath.parse(lastResponse).read("$.status");
        Assertions.assertEquals(realStatus, desiredStatus);
    }
	
	@Then("^the amount substracting the fee$")
    public void amount_substrating_fee() throws Throwable {
        Double realAmount = JsonPath.parse(lastResponse).read("$.amount");
        Assertions.assertTrue(realAmount == amount - fee,
        		() -> "Real amount " + realAmount + " has to substract the fee");
    }
	
	
	@Then("^the amount$")
    public void the_amount() throws Throwable {
        Double realAmount = JsonPath.parse(lastResponse).read("$.amount");
        Assertions.assertTrue(realAmount.doubleValue() == amount, 
        		() -> "Real amount:" + realAmount + " is not equals to " + amount);
    }
	
	@Then("^the fee$")
    public void the_fee() throws Throwable {
        Double realFee = JsonPath.parse(lastResponse).read("$.fee");
        Assertions.assertTrue(realFee.doubleValue() == fee, 
        		() -> "Real fee:" + realFee + " is not equals to " + fee);
    }
}
