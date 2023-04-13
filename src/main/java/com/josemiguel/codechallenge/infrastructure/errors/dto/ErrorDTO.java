package com.josemiguel.codechallenge.infrastructure.errors.dto;

import javax.validation.constraints.NotEmpty;

import org.apache.commons.lang3.StringUtils;
import org.checkerframework.checker.nullness.qual.Nullable;

import lombok.Builder;

@Builder
public record ErrorDTO(@NotEmpty @Nullable String description) {

	public ErrorDTO{
		if (StringUtils.isEmpty(description)) {
			throw new IllegalArgumentException();
		}
	}

}
