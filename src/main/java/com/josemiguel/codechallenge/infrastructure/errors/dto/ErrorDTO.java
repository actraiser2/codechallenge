package com.josemiguel.codechallenge.infrastructure.errors.dto;

import javax.validation.constraints.NotEmpty;

import org.apache.commons.lang3.StringUtils;

import lombok.Builder;

@Builder
public record ErrorDTO(@NotEmpty String description) {

	public ErrorDTO{
		if (StringUtils.isEmpty(description)) {
			throw new IllegalArgumentException();
		}
	}
	public String doubleDescription() {
		return this.description.concat(description);
	}

}
