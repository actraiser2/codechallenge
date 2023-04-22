package com.josemiguel.codechallenge.infrastructure.adapters.input;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class CodeChallengeJmsListener {

	//@JmsListener(destination = "jms-queue")
	public void handleMessage(String content) {
		log.info("Message received:" + content);
	}
}
