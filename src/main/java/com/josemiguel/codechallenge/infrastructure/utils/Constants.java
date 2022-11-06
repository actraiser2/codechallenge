package com.josemiguel.codechallenge.infrastructure.utils;

import java.io.File;
import java.security.Key;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Signature;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.HmacAlgorithms;
import org.apache.commons.codec.digest.HmacUtils;
import org.apache.commons.lang3.StringUtils;

public final class Constants {

	private Constants() {
		
	}
	public static int PAGE_SIZE = 100;
	
	public static void main(String... args) throws Exception {
		
		int a = 1;
		int b = 1;
		System.out.println(Integer.toBinaryString(a ^ b));
		System.out.println(Integer.toBinaryString(a << 2));
		System.out.println((2) % 7);
		System.out.println(encryptAes("Hello World"));
		System.out.println(signSHA256RSA("Hello World"));
		
	}
	
	public static String generateHMac(String text) {
		String key = "josemi";
		Mac mac = 	HmacUtils.getInitializedMac(HmacAlgorithms.HMAC_SHA_256, key.getBytes());
		return new String(Hex.encodeHex(mac.doFinal(text.getBytes())));
	}
	
	public static String encryptAes(String text) throws Exception{
		Cipher cipher = Cipher.getInstance("AES");
		String key = "josemi";
		String paddedKey = StringUtils.leftPad(key, 32, "0");
		SecretKeySpec secretKeySpec = new SecretKeySpec(paddedKey.getBytes(), "AES");
		cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
		return Hex.encodeHexString(cipher.doFinal(text.getBytes()));
	}
	
	public static String signSHA256RSA(String text) throws Exception{
		Signature signature = Signature.getInstance("SHA256withRSA");
		KeyStore keyStore = KeyStore.getInstance(new File("d:/tmp/josemi.jks"), "Fenix000_".toCharArray());
		PrivateKey privateKey = (PrivateKey)keyStore.getKey("com.josemi2", "Fenix000_".toCharArray());
		signature.initSign(privateKey);
		signature.update(text.getBytes());
		return Hex.encodeHexString(signature.sign());
	}
}
