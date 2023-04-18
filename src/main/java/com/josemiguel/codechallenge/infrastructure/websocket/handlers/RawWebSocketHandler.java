package com.josemiguel.codechallenge.infrastructure.websocket.handlers;

import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RawWebSocketHandler extends TextWebSocketHandler {

	@Override
	protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) {
		// TODO Auto-generated method stub
		super.handleBinaryMessage(session, message);
	}

	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		// TODO Auto-generated method .
		log.info("Message received by websocket:" + message.getPayload());
		log.info("Simulating a long duration request:");
		
		Thread.sleep(20000);
		session.sendMessage(new TextMessage(message.getPayload()));
	}

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		// TODO Auto-generated method stub
		super.afterConnectionEstablished(session);
		
		log.info("Session entablished:" + session.getId());
	}
	
	

}
