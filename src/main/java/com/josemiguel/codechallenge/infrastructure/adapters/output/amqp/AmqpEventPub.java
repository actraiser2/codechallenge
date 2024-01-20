package com.josemiguel.codechallenge.infrastructure.adapters.output.amqp;



import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.stereotype.Component;

import com.josemiguel.codechallenge.domain.model.events.AccountCreatedEvent;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AmqpEventPub {

	private final AmqpTemplate amqpTemplate;
	private final TopicExchange topicExchange;

	
	public void sendEventAccountCreated(AccountCreatedEvent event) {
		amqpTemplate.convertAndSend(topicExchange.getName(), "accounts", event);
	}
}
