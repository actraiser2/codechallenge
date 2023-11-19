package com.josemiguel.codechallenge.infrastructure.config;

import java.io.File;
import java.io.IOException;
import java.util.function.Consumer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;

import com.google.common.io.Files;
import com.josemiguel.codechallenge.application.ports.output.AccountRepository;
import com.josemiguel.codechallenge.domain.Account;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
@AllArgsConstructor
public class MessagesConsumerConfig {

	private AccountRepository accountRepository;
	
	@Bean
	public Consumer<Message<Account>> accounts(){
		return e -> {
			log.info("Event received (headers):  {}", e.getHeaders());
			log.info("Event received (payload):  {}", e.getPayload());
			
			var accountEntity = accountRepository.findById(e.getPayload().getAccountId());
			
			log.info("Entity account: {}", accountEntity);
			/*DatumReader<Account> accountDatumReader = 
					new SpecificDatumReader<>(Account.class);
			
			var decoder = DecoderFactory.get().binaryDecoder(e.getPayload(), null);
			
			Account account = null;
			try {
				account = accountDatumReader.read(null, decoder);
			} catch (IOException ex) {
				// TODO Auto-generated catch block
				log.error("Error:", ex);
			}
			log.info("Account received: {}", account);*/
		};
	}
} 
