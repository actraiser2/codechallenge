package com.josemiguel.codechallenge.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration

public class SecurityConfig {

	@Bean
	@Profile(value = "!test")
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
		httpSecurity.authorizeRequests().
			antMatchers("/openapi/**").permitAll().
			anyRequest().
			hasAuthority("SCOPE_read:accounts").and().
			oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt).csrf().disable();
		
		return httpSecurity.build();
	}
	
	@Bean
	@Profile(value = "test")
	public SecurityFilterChain testSecurityFilterChain(HttpSecurity httpSecurity) throws Exception {
		httpSecurity.authorizeRequests().
			antMatchers("/**").
			permitAll().
			and().csrf().disable();
		
		return httpSecurity.build();
	}
}
