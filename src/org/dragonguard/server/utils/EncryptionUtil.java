package org.dragonguard.server.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.TimeZone;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class EncryptionUtil {
	
	private byte[] ivkey = {-30, -87, -77, -30, -103, -83, -30, -120, -101, -30, -77, -86, -30, -72, -121, -30};
	private byte[] ivkey2 = {-30, -83, -111, -30, -93, -72, -30, -88, -88, -30, -86, -85, -30, -104, -110, -30};
	
	public SecretKeySpec setKey(String password) throws UnsupportedEncodingException, NoSuchAlgorithmException {
		MessageDigest sha = null;
		byte[] key = password.getBytes("UTF-8");
		sha = MessageDigest.getInstance("SHA-1");
		key = sha.digest(key);
		key = Arrays.copyOf(key, 16);
		SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
		return secretKey;
	}

	public String encrypt(String string, String secret) {
		try {
			SecretKeySpec key = setKey(secret);
			IvParameterSpec iv = null;
			if (getDay()) {
				iv = new IvParameterSpec(ivkey2);
			} else {
				iv = new IvParameterSpec(ivkey);
			}
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, key, iv);
			string = getRandom() + string + getRandom();
			if (getDay()) {
				string = XorEncryptionUtil.encryptXORBase64(string);
			} else {
				string = XorEncryptionUtil2.encryptXORBase64(string);
			}
			return Base64.getEncoder().encodeToString(cipher.doFinal(string.getBytes("UTF-8")));
		} catch (Exception e) {
			System.out.println("Error while encrypting: " + e.toString());
		}
		return null;
	}

	public String decrypt(String string, String secret) {
		try {
			SecretKeySpec key = setKey(secret);
			IvParameterSpec iv = null;
			if (getDay()) {
				iv = new IvParameterSpec(ivkey);
			} else {
				iv = new IvParameterSpec(ivkey2);
			}
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
			cipher.init(Cipher.DECRYPT_MODE, key, iv);
			String result = new String(cipher.doFinal(Base64.getDecoder().decode(string)));
			if (getDay()) {
				result = XorEncryptionUtil2.decryptXORBase64(result);
			} else {
				result = XorEncryptionUtil.decryptXORBase64(result);
			}
			result = result.substring(4);
			result = result.substring(0,result.length()-4);
			return result;
		} catch (Exception e) {
			System.out.println("Error while decrypting: " + e.toString());
		}
		return null;
	}
	
	public int getRandom() {
	    Random random = new Random();
	    return random.nextInt(9999 - 1000) + 1000;
	}
	
	@SuppressWarnings("deprecation")
	public boolean getDay() {
		Date date = Calendar.getInstance(TimeZone.getTimeZone("Atlantic/Azores")).getTime();
		return date.getDay() % 2 == 1;
	}
}