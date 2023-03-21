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
	private TransformMesage trasformMessage;
	
	@Override
	public void configure() throws Exception {
		// TODO Auto-generated method stub
		from("sftp:localhost:22222/input?username=jmbesada&password=RAW(fenix000)&useUserKnownHostsFile=false&delete=true").
		filter(m -> m.getIn().getBody(String.class).contains("Hello")).
		bean(trasformMessage).
		log("File passed: ${header['CamelFileName']}").
		marshal().json().
		choice().
			when(header("CamelFileName").endsWith(".xml")).
				toD("sftp:localhost:2222/output/xml?username=jmbesada&password=fenix000&"
				+ "fileName=${header['camelFileName']}-${date:now:yyyyMMdd-hhmmss}").
			when(header("CamelFileName").endsWith(".yaml")).
				toD("sftp:localhost:2222/output/yaml?username=jmbesada&password=fenix000&"
					+ "fileName=${header['camelFileName']}-${date:now:yyyyMMdd-hhmmss}").
			otherwise().
				to("sftp:localhost:2222/remainder?username=jmbesada&password=fenix000");
	}

}
