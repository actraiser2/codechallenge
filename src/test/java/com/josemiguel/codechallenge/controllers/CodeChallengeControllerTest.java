package com.josemiguel.codechallenge.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.josemiguel.codechallenge.application.ports.input.CreateAccountUseCase;
import com.josemiguel.codechallenge.domain.commands.CreateAccountCommand;

@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
@AutoConfigureMockMvc
@TestPropertySource(properties = "spring.profiles.active=test" )
public class CodeChallengeControllerTest {

	@Autowired MockMvc mockMvc;
	@Autowired CreateAccountUseCase createAccounbtUseCase;
	
	@Test
	public void testCreateAccount() throws Exception {
		var command = CreateAccountCommand.builder().accountName("Cuenta Josemi").
				iban("ES31233323223").build();
		
		ObjectMapper objectMapper = new ObjectMapper();
		
		mockMvc.perform(post("/api/v1/accounts").contentType("application/json").
				content(objectMapper.writeValueAsString(command))).
			andExpect(status().is(201));	
	}
}
