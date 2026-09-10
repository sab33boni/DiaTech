package it.unisa.diatech.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Classe di utilità per la sicurezza e la crittografia delle password.
 * Utilizza l'algoritmo standard SHA-256 per calcolare l'hash esadecimale,
 * garantendo che le password non vengano mai memorizzate in chiaro nel database.
 */
public class PasswordUtil {

    private static final String ALGORITHM = "SHA-256";

    // Costruttore privato per evitare l'istanziazione di classi utility
    private PasswordUtil() {}

    /**
     * Calcola l'hash SHA-256 di una password testuale.
     *
     * @param plainPassword Password in chiaro fornita dall'utente
     * @return Stringa esadecimale di 64 caratteri rappresentante l'hash della password
     */
    public static String hashPassword(String plainPassword) {
        if (plainPassword == null) {
            throw new IllegalArgumentException("La password non può essere null.");
        }

        try {
            MessageDigest digest = MessageDigest.getInstance(ALGORITHM);
            byte[] encodedhash = digest.digest(plainPassword.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(encodedhash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Algoritmo di hashing " + ALGORITHM + " non supportato.", e);
        }
    }

    /**
     * Converte un array di byte in una stringa esadecimale leggibile.
     */
    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
