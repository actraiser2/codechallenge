package com.josemiguel.codechallenge.infrastructure.routes;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@AllArgsConstructor
@Slf4j
public class CodeChallengeRoute extends RouteBuilder {

	private CamelContext context;
	
	@Override
	public void configure() throws Exception {
		// TODO Auto-generated method stub
		from("sftp:localhost:2222/input?username=jmbesada&password=fenix000&useUserKnownHostsFile=false&delete=true").
		bean(TransformMesage.class).
		process(e -> {
			String payload = e.getMessage().getBody(String.class);
			
			log.info(e.getIn().getBody(String.class));
		});
	}

}
