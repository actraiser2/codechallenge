package com.josemiguel.codechallenge.infrastructure.adapters.input;


import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class CodeChallengeJmsListener implements MessageListener {

	//@JmsListener(destination = "jms-queue")
	public void handleMessage(String content) {
		log.info("Message received1:" + content);
	}

	@Override
	public void onMessage(Message message) {
		// TODO Auto-generated method stub
		try {
			log.info("Message received2:" + message.getBody(String.class));
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
