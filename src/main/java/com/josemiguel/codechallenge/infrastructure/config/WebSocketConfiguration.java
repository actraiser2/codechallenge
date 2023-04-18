package com.josemiguel.codechallenge.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import com.josemiguel.codechallenge.infrastructure.websocket.handlers.RawWebSocketHandler;

@Configuration
@EnableWebSocket
public class WebSocketConfiguration implements WebSocketConfigurer  {

	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		// TODO Auto-generated method stub
		registry.addHandler(websocketHandler(), "/websocket");
		registry.addHandler(websocketHandler() , "/websocket-sockjs").withSockJS();
	}
	
	@Bean
    public WebSocketHandler websocketHandler() {
        return new RawWebSocketHandler();
    }

}
