package com.josemiguel.codechallenge.infrastructure.config;

import javax.jms.ConnectionFactory;
import javax.jms.MessageListener;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.listener.DefaultMessageListenerContainer;

@Configuration
public class JmsConfig {
	
	@Bean              
    public DefaultMessageListenerContainer  customMessageListenerContainer(
                ConnectionFactory connectionFactory,
                   MessageListener queueListener){
        
		DefaultMessageListenerContainer listener = new DefaultMessageListenerContainer();
        listener.setConnectionFactory(connectionFactory);
        listener.setDestinationName("jms-queue");
        listener.setMessageListener(queueListener);
        return listener;
    }
}
