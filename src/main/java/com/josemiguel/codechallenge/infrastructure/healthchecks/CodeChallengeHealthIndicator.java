package com.josemiguel.codechallenge.infrastructure.healthchecks;

import java.nio.file.Paths;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component("codeChallengeHealthIndicator")
public class CodeChallengeHealthIndicator implements HealthIndicator {

	@Override
	public Health health() {
		if (Paths.get("/tmp/codechallenge_readiness").toFile().exists()) {
			return Health.up().build();
		}
		else {
			return Health.down().build();
		}
	}

}
