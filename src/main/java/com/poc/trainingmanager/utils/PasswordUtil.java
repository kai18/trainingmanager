package com.poc.trainingmanager.utils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordUtil {

	public static String getPasswordHash(String password) {
		String hashedPassword = null;
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(password.getBytes());
			byte[] hash = md.digest();
			BigInteger bigInt = new BigInteger(1, hash);
			hashedPassword = bigInt.toString(16);
		} catch (NoSuchAlgorithmException e) {

			e.printStackTrace();
		}
		return hashedPassword;
	}
}

