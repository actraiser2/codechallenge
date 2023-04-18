package com.josemiguel.codechallenge.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@EnableWebSocketMessageBroker
@Configuration
public class WebSocketStompConfiguration implements WebSocketMessageBrokerConfigurer{

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		// TODO Auto-generated method stub
		registry.addEndpoint("/websocket-stomp");
	}

	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		// TODO Auto-generated method stub
		//registry.enableSimpleBroker("/queue", "/topic");
		
			registry.enableStompBrokerRelay("/queue", "/topic").
			setUserDestinationBroadcast("/topic/unresolved.user.dest").
			setUserRegistryBroadcast("/topic/registry.broadcast").
			setRelayHost("192.168.1.18").
			setRelayPort(61613).
			setSystemLogin("jmbesada").
			setSystemPasscode("fenix000").
			setClientLogin("jmbesada").
			setClientPasscode("fenix000");
		registry.setApplicationDestinationPrefixes("/codechallenge");
	}

	
}
