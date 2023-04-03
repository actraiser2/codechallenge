package com.josemiguel.codechallenge.infrastructure.listeners;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ContextClosedEventListener implements ApplicationListener<ContextClosedEvent>{

	@Override
	public void onApplicationEvent(ContextClosedEvent event) {
		log.info("Closing context....");
		// TODO Auto-generated method stub
		
		try {
			Thread.sleep(60000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
