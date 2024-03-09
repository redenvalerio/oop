package com.mmdc.oop.Utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class Authentication {
    private static final String HASH_ALGORITHM = "SHA-256";

    public static boolean authenticate(String username, String password, String hashedPassword) {
        String generatedHash = hashPassword(password);
        return hashedPassword.equals(generatedHash);
    }

    public static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance(HASH_ALGORITHM);
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            System.err.println("Error hashing password: " + e.getMessage());
            return null;
        }
    }
}
