package com.josemiguel.codechallenge.infrastructure.utils;

public final class Constants {

	private Constants() {
		
	}
	public static int PAGE_SIZE = 100;
	
	public static int calculateLength(String cad, Integer exp) {
		return (int)Math.pow(cad.length(), exp);
	}
}
