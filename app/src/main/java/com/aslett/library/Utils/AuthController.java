package com.aslett.library.Utils;

import java.security.SecureRandom;
import javax.crypto.spec.PBEKeySpec;
import com.aslett.library.Main;
import com.aslett.library.Models.User;
import javax.crypto.SecretKeyFactory;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class AuthController {
    // String input for hashing
    public static String createHash(String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        return createHash(password.toCharArray());
    }

    // Character array for hasing
    public static String createHash(char[] password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[24];
        random.nextBytes(salt);

        byte[] hash = pbkdf2(password, salt, 1000, 24);
        return 1000 + ":" + byteToHex(salt) + ":" + byteToHex(hash);
    }

    // Turn bytes into hex string
    private static String byteToHex(byte[] array) {
        BigInteger bi = new BigInteger(1, array);
        String hex = bi.toString(16);
        int paddingLength = (array.length * 2) - hex.length();
        if (paddingLength > 0)
            return String.format("%0" + paddingLength + "d", 0) + hex;
        else
            return hex;
    }

    // Turn hex string into bytes
    private static byte[] hexToByte(String hex) {
        byte[] binary = new byte[hex.length() / 2];
        for (int i = 0; i < binary.length; i++) {
            binary[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
        }
        return binary;
    }

    // Validate String password against existing hash    
    public static boolean validatePassword(String password, String goodHash)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        return validatePassword(password.toCharArray(), goodHash);
    }

    // Validate password against hash
    public static boolean validatePassword(char[] password, String goodHash)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        String[] params = goodHash.split(":");
        int iterations = Integer.parseInt(params[0]);
        byte[] salt = hexToByte(params[1]);
        byte[] hash = hexToByte(params[2]);

        byte[] testHash = pbkdf2(password, salt, iterations, hash.length);

        return checkHashes(hash, testHash);
    }

    // Compare hashes
    private static boolean checkHashes(byte[] a, byte[] b) {
        int diff = a.length ^ b.length;
        for (int i = 0; i < a.length && i < b.length; i++)
            diff |= a[i] ^ b[i];
        return diff == 0;
    }

    // Hash password using PBKDF2
    private static byte[] pbkdf2(char[] password, byte[] salt, int iterations, int bytes)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        PBEKeySpec spec = new PBEKeySpec(password, salt, iterations, bytes * 8);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        return skf.generateSecret(spec).getEncoded();
    }

    // Attempt to sign the user in, given a username and password
    public static boolean attemptLogin(String username, String password) {
        User user = User.findByField("username", username).get(0);
        if (user == null || user.getID() == 0 || user.approved == 0) {
            return false;
        }
        try {
            if (AuthController.validatePassword(password, user.password)) {
                Main.currentUser = user;
                return true;
            } else {
                Main.currentUser = null;
                return false;
            }
        } catch (Exception e) {
            Main.currentUser = null;
            System.out.println(e.getMessage());
            return false;
        }
    }
}
