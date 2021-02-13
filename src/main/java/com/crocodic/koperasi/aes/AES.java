package com.crocodic.koperasi.aes;

import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;

@Service
public class AES {
    private static final String key = "YdHebKuEBTfkBOy";
    private static final String iv = "GaSzOzGNDmoqcVq";

    public static String Encrypt(String words) {
        Encryption aes = new Encryption(key, 128, iv);
        return aes.encrypt(words);
    }

    public static String Decrypt(String words) {
        boolean plainMode = false;
        if(plainMode) return words;
        Encryption aes = new Encryption(key, 128, iv);
        return aes.decrypt(words);
    }

    private static String byteToHex(final byte[] hash)
    {
        Formatter formatter = new Formatter();
        for (byte b : hash)
        {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }

    public static String sha1(String teks) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest crypt = MessageDigest.getInstance("SHA-1");
        crypt.reset();
        crypt.update(teks.getBytes("UTF-8"));
        return byteToHex(crypt.digest());
    }
}
