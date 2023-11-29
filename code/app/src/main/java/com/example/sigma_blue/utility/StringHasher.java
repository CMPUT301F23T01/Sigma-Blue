package com.example.sigma_blue.utility;

import android.util.Log;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * used code from https://www.baeldung.com/sha-256-hashing-java
 * Utility class for getting the SHA-256 hash of a string. Used for passwords.
 */
public class StringHasher {
    /**
     * Return the SHA 256 hash of the input string
     * @param str string to hash
     * @return a string representing the hex value of the hash
     */
    public static String getHash(String str) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedHash = digest.digest(str.getBytes(StandardCharsets.UTF_8));
            return bytesToString(encodedHash);
        } catch (Exception e) {
            Log.e("DEBUG", "Couldn't hash password!");
            return str;
        }
    }

    /**
     * Converts a bytes array into a hex string
     * @param bytes input bytes
     * @return hex string representation
     */
    private static String bytesToString(byte[] bytes){
        StringBuilder hexString = new StringBuilder(2 * bytes.length);
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xff & bytes[i]);
            if(hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
