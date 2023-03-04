package com.josemiguel.codechallenge.infrastructure.routes;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class TransformMesage {

	public String truncate(String message) {
		return StringUtils.substring(message, 0, 40);
	}
}
