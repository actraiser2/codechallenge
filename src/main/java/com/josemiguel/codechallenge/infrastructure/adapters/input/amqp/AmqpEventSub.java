package com.josemiguel.codechallenge.infrastructure.adapters.input.amqp;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.stereotype.Component;

import com.josemiguel.codechallenge.domain.model.events.AccountCreatedEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class AmqpEventSub {

	@RabbitListener(queues = "${amqp.queue.account-created}")
	public void handleAccountCreatedEvent(AccountCreatedEvent event,
			@Headers Map<String, String> headers)  {
		log.info("Headers received:" + headers);
		log.info("Event received:" + event);
		
		try {
			Files.readString(Paths.get("c:/tmp/" + event.getFileName()));
		} catch (IOException ex) {
			// TODO Auto-generated catch block
			throw new AmqpRejectAndDontRequeueException(ex);
		}
	}
}
