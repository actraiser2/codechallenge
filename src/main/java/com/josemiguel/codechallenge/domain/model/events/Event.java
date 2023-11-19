package com.josemiguel.codechallenge.domain.model.events;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Event<K, T> {

	public enum Type{CREATED, DELETED, UPDATED};
	
	
	private K key;
	private T data;
	private Type eventType;
	private LocalDateTime timestamp;
}
