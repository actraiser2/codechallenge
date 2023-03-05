package com.josemiguel.codechallenge.infrastructure.routes;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class TransformMesage {

	public String truncate(String message) {
		log.info("Filtering message:" + message);
		return StringUtils.substring(message, 0, 40);
	}
}
