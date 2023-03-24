package com.example.IPSEN2_GROEP7.service;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
public class EncryptionService {
    private static final String secretPhrase = "o9szYIOq1rRMiouNhNvaq96lqUvCekxR";

    public static String decrypt(String ciphertext) throws Exception {
        SecretKey secretKey = getKey(secretPhrase);
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");

        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        return new String(cipher.doFinal(Base64.getDecoder().decode(ciphertext)));
    }

    public static SecretKey getKey(String secretKey) throws Exception {
        byte[] decodeSecretKey = Base64.getDecoder().decode(secretKey);
        return new SecretKeySpec(decodeSecretKey, 0, decodeSecretKey.length, "AES");
    }
}
