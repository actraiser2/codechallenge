package com.josemiguel.codechallenge.infrastructure.routes;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class RestCodeChallengeRoute extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		// TODO Auto-generated method stub
		from("direct:restCodechallenge").
		marshal().json().
			toD("sftp:localhost:22222/output/remainder?username=jmbesada&password=fenix000&"
					+ "fileName=${date:now:ddMMyyyy-hhmmss}.json");
	}

}
