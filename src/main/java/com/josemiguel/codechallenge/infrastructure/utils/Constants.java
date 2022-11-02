package com.josemiguel.codechallenge.infrastructure.utils;

import javax.crypto.Mac;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.HmacAlgorithms;
import org.apache.commons.codec.digest.HmacUtils;

public final class Constants {

	private Constants() {
		
	}
	public static int PAGE_SIZE = 100;
	
	public static void main(String... args) {
		
		int a = 1;
		int b = 1;
		System.out.println(Integer.toBinaryString(a ^ b));
		System.out.println(Integer.toBinaryString(a << 2));
		System.out.println(Integer.toBinaryString(a | 2));
		System.out.println(generateHMac("a"));
		
	}
	
	public static String generateHMac(String text) {
		String key = "josemi";
		Mac mac = 	HmacUtils.getInitializedMac(HmacAlgorithms.HMAC_SHA_256, key.getBytes());
		return new String(Hex.encodeHex(mac.doFinal(text.getBytes())));
	}
}
