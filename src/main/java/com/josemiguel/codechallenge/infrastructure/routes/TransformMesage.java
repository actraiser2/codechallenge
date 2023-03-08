package com.josemiguel.codechallenge.infrastructure.routes;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.josemiguel.codechallenge.infrastructure.routes.dto.CodeChallengeMessage;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class TransformMesage {

	public CodeChallengeMessage truncate(String message) {
		return CodeChallengeMessage.builder().rawMessage(StringUtils.abbreviate(message, 50)).build();
		
	}
}
