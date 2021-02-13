package com.crocodic.koperasi.aes;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.io.Serializable;
import java.security.Key;
import java.security.MessageDigest;

public class Encryption implements Serializable {

    private static final IvParameterSpec DEFAULT_IV = new IvParameterSpec(new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0});
    /**
     * 加密演算法使用AES
     */
    private static final String ALGORITHM = "AES";
    /**
     * AES使用CBC模式与PKCS5Padding
     */
    private static final String TRANSFORMATION = "AES/CBC/PKCS5Padding";
    //-----成员变量-----
    /**
     * 取得AES加解密的秘钥
     */
    private Key key;
    /**
     * AES CBC模式使用的Initialization Vector
     */
    private IvParameterSpec iv;
    /**
     * Cipher 物件
     */
    private Cipher cipher;

    /**
     * 构造函数，使用128 Bits的AES秘钥(计算任意长度秘钥的MD5)和预设IV
     *
     * @param key 传入任意长度的AES秘钥
     */
    public Encryption(final String key) {
        this(key, 256);
    }

    /**
     * 构造函数，使用128 Bits或是256 Bits的AES秘钥(计算任意长度秘钥的MD5或是SHA256)和预设IV
     *
     * @param key 传入任意长度的AES秘钥
     * @param bit 传入AES秘钥长度，数值可以是128、256 (Bits)
     */
    public Encryption(final String key, final int bit) {
        this(key, bit, null);
    }

    /**
     * 构造函数，使用128 Bits或是256 Bits的AES秘钥(计算任意长度秘钥的MD5或是SHA256)，用MD5计算IV值
     *
     * @param key 传入任意长度的AES秘钥
     * @param bit 传入AES秘钥长度，数值可以是128、256 (Bits)
     * @param iv 传入任意长度的IV字串
     */
    public Encryption(final String key, final int bit, final String iv) {
        if (bit == 256) {
            this.key = new SecretKeySpec(getHash("SHA-256", key), ALGORITHM);
        } else {
            this.key = new SecretKeySpec(getHash("MD5", key), ALGORITHM);
        }
        if (iv != null) {
            this.iv = new IvParameterSpec(getHash("MD5", iv));
        } else {
            this.iv = DEFAULT_IV;
        }

        init();
    }

    //-----物件方法-----
    /**
     * 取得字串的Hash值
     *
     * @param algorithm 传入散列算法
     * @param text 传入要散列的字串
     * @return 传回散列后內容
     */
    private static byte[] getHash(final String algorithm, final String text) {
        try {
            return getHash(algorithm, text.getBytes("UTF-8"));
        } catch (final Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    /**
     * 取得资料的Hash值
     *
     * @param algorithm 传入散列算法
     * @param data 传入要散列的资料
     * @return 传回散列后內容
     */
    private static byte[] getHash(final String algorithm, final byte[] data) {
        try {
            final MessageDigest digest = MessageDigest.getInstance(algorithm);
            digest.update(data);
            return digest.digest();
        } catch (final Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    /**
     * 初始化
     */
    private void init() {
        try {
            cipher = Cipher.getInstance(TRANSFORMATION);
        } catch (final Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    /**
     * 加密文字
     *
     * @param str 传入要加密的文字
     * @return 传回加密后的文字
     */
    public String encrypt(final String str) {
        try {
            return encrypt(str.getBytes("UTF-8"));
        } catch (final Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    /**
     * 加密资料
     *
     * @param data 传入要加密的资料
     * @return 传回加密后的资料
     */
    public String encrypt(final byte[] data) {
        try {
            cipher.init(Cipher.ENCRYPT_MODE, key, iv);
            final byte[] encryptData = cipher.doFinal(data);

            return DatatypeConverter.printBase64Binary(encryptData);
        } catch (final Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    /**
     * 解密文字
     *
     * @param str 传入要解密的文字
     * @return 传回解密后的文字
     */
    public String decrypt(final String str) {
        try {

            return decrypt(DatatypeConverter.parseBase64Binary(str));
        } catch (final Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    /**
     * 解密文字
     *
     * @param data 传入要解密的资料
     * @return 传回解密后的文字
     */
    public String decrypt(final byte[] data) {
        try {
            cipher.init(Cipher.DECRYPT_MODE, key, iv);
            final byte[] decryptData = cipher.doFinal(data);
            return new String(decryptData, "UTF-8");
        } catch (final Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    public static String encryptString(String content) {
        //这里填写密码和iv字符串，注意要确保16位的
        Encryption ea = new Encryption("****************", 128, "################");
        return ea.encrypt(content);
    }

    public static String decryptString(String content) {
        String result = null;
        try {
            //这里填写密码和iv字符串，注意要确保16位的
            Encryption ea = new Encryption("****************", 128, "################");
            result = ea.decrypt(content);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

}
