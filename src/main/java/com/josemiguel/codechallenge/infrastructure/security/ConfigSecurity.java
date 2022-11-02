package com.josemiguel.codechallenge.infrastructure.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
public class ConfigSecurity {

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
		;
		return httpSecurity.cors().
			and().
			authorizeHttpRequests().antMatchers("/**").authenticated().and().
			csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()).
			and().
			formLogin().
			and().
			build();
	}
}
