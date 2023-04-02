package com.josemiguel.codechallenge.domain.model.valueobjects;

public enum ShapeType {

	CIRCLE, SQUARE, TRIANGLE;
	
	public String newInstance() {
		return this.name();
	}
}
