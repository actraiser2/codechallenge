package com.josemiguel.codechallenge.infrastructure.config.props;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "kafka")
public record KafkaConfigProperties(List<String> hosts, String username, String password) {

}
