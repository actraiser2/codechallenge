package com.josemiguel.codechallenge.domain.model.entities;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

@MappedSuperclass
public class AuditingEntity {

	@CreatedDate
	@Column(name = "CREATED_DATE")
	private LocalDateTime createdDate;
	
	@LastModifiedDate
	@Column(name = "LAST_MODIFIED_DATE")
	private LocalDateTime lastModifiedDate;
	
	
	@CreatedBy
	@Column(name = "CREATED_BY")
	private String createdBy;
}
