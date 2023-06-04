package com.josemiguel.codechallenge.infrastructure.healthchecks;

import java.nio.file.Paths;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component("codeChallengeHealthIndicator")
@Slf4j
public class CodeChallengeHealthIndicator implements HealthIndicator {

	@Override
	public Health health() {
		log.info("Calling codeChallengeHealthIndicator:" + 
				Paths.get("/tmp/codechallenge_readiness").toFile().exists());
		if (!Paths.get("/tmp/codechallenge_readiness").toFile().exists()) {
			return Health.up().build();
		}
		else {
			return Health.down().build(); 
		}
	}

}
