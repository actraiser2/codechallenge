package com.josemiguel.codechallenge.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {

	@Bean
	public OpenAPI oprnApi() {
		return new OpenAPI().
				info(new Info().
						title("Codechallenge OpenApi").
						summary("Codechallenge OpenApi for your accounts").
						contact(
							new Contact().
								email("Codechallenge OpenApi").
								name("Jose Miguel Besada"))
					);
	}
}
