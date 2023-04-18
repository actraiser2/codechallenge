package com.josemiguel.codechallenge.infrastructure.adapters.input;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import com.josemiguel.codechallenge.infrastructure.adapters.input.dto.InputMessage;
import com.josemiguel.codechallenge.infrastructure.adapters.input.dto.OutputMessage;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@AllArgsConstructor
public class CodeChallengeWebSocketController {

	//private SimpMessagingTemplate simpMessagingTemplate;
	
	
	@MessageMapping("/echo")
	@SendTo("/topic/test01")
	public OutputMessage sendMessage(InputMessage input) {
		log.info("Input message sent to echo: " + input);
		
		return OutputMessage.builder().echo(input.getMessage()).
				timestamp(ZonedDateTime.now()).
				build();
		
		/*simpMessagingTemplate.convertAndSendToUser("/topic/test01", 
				OutputMessage.builder().echo(input.getMessage()).
				timestamp(LocalDateTime.now().atZone(ZoneId.systemDefault())).
				build());*/
	}
	
}
