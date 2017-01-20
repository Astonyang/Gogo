package com.xxx.gogo.utils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

public class CryptoUtil {
    private static final byte[] IV = {0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08};
    private static final String KEY = "12345678";

    public static byte[] encrypt(byte[] data){
        try {
            DESKeySpec desKey = new DESKeySpec(KEY.getBytes());
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey securekey = keyFactory.generateSecret(desKey);

            IvParameterSpec iv = new IvParameterSpec(IV);

            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, securekey, iv);

            return cipher.doFinal(data);
        }catch (Exception e){
            return null;
        }
    }

    public static byte[] deEncrypt(byte[] data){
        try {
            DESKeySpec desKey = new DESKeySpec(KEY.getBytes());
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey securekey = keyFactory.generateSecret(desKey);
            IvParameterSpec iv = new IvParameterSpec(IV);

            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, securekey, iv);

            return cipher.doFinal(data);
        }catch (Exception e){
            LogUtil.e(e.toString());
            return null;
        }
    }
}
