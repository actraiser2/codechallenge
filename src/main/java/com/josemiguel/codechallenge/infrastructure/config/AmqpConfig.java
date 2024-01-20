package com.josemiguel.codechallenge.infrastructure.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



@Configuration
public class AmqpConfig {

	
	@Bean
	TopicExchange accountCreatedTopicExchange(
			@Value("${amqp.exchange.account-created}") String exchangeName) {
		return ExchangeBuilder.topicExchange(exchangeName).durable(true).build();
	}
	
	@Bean
	Queue accountCreatedQueue(
			@Value("${amqp.queue.account-created}") String queueName) {
		return QueueBuilder.durable(queueName).ttl(1000*300).build();
	}
	
	@Bean
	Binding accountCreatedBinding(@Qualifier("accountCreatedQueue") Queue queue,
			@Qualifier("accountCreatedTopicExchange") TopicExchange exchange) {
		return BindingBuilder.bind(queue).to(exchange).with("accounts");
	}
}
