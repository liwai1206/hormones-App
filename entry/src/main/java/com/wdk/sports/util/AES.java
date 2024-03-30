package com.wdk.sports.util;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;
import java.util.Base64;

/**
 * AES加解密工具类
 */
public class AES {

    private static final String encodeRules = "kjgiamxqosxwbcsa";



    /**
     * AES加密
     * @param content 待加密的内容
     * @param encryptKey 加密密钥
     * @return 加密后的byte[]
     * @throws Exception
     */
    public static String aesEncryptToBytes(String content, String encryptKey) throws Exception {

        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(encryptKey.getBytes() , "AES"));

        byte[] bytes = cipher.doFinal(content.getBytes("utf-8"));

        return base64Encode( bytes );
    }



    /**
     * AES解密
     * @param encryptBytes 待解密的byte[]
     * @param decryptKey 解密密钥
     * @return 解密后的String
     * @throws Exception
     */
    public static String aesDecryptByBytes( String encryptBytes, String decryptKey) throws Exception {

        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(decryptKey.getBytes() , "AES"));

        byte[] bytes = base64Decode( encryptBytes );

        byte[] decryptBytes = cipher.doFinal(bytes);

        return new String(decryptBytes,"utf-8");
    }

    /**
     * base 64 加密
     * @param bytes 待编码的byte[]
     * @return 编码后的base 64 code
     */
    public static String base64Encode(byte[] bytes){
        return Base64.getEncoder().encodeToString(bytes);
    }

    /**
     * base 64 解密
     * @param base64Code 待解码的base 64 code
     * @return 解码后的byte[]
     * @throws Exception
     */
    public static byte[] base64Decode(String base64Code) throws Exception{
        return  Base64.getDecoder().decode( base64Code );
    }





}

