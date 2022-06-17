package com.wanghao.mvvmapp.mvvm.encrypt;


import java.security.Key;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;


public class AESUtil {

    public static final String CHAR_ENCODING = "UTF-8";
    private static final String KEY_ALGORITHM = "AES";
    private static final String DEFAULT_CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";//默认的加密算法

    private static Key aesKey;

    private final  String aesKeyString = Const.AES_KEY;

    public static AESUtil getInstance() {
        return Holder.INSTANCE;
    }


    private static class Holder {
        private static final AESUtil INSTANCE = new AESUtil();
    }


    private AESUtil() {
        try {
        aesKey = new SecretKeySpec(aesKeyString.getBytes(CHAR_ENCODING), KEY_ALGORITHM);
        } catch (Exception e) {
            System.out.println("RSA密钥初始化错误！！！");
        }
    }


    /**
     * AES 加密操作
     *
     * @param content 待加密内容
     * @return 返回Base64转码后的加密数据
     */

    public  String encrypt(String content) throws Exception {
        Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);// 创建密码器

        byte[] byteContent = content.getBytes(CHAR_ENCODING);

        cipher.init(Cipher.ENCRYPT_MODE, aesKey);// 初始化为加密模式的密码器

        byte[] result = cipher.doFinal(byteContent);// 加密

        //return new String(Base64Utils.encode(result));//通过Base64转码返回
//        byte[] base64Result = Base64.getUrlEncoder().encode(result);
//        return new String(Base64.getUrlEncoder().encode(result));

        String encodeResult = MyBase64.getUrlEncoder().encodeToString(result);
       return encodeResult;
    }

    /**
     * AES 解密操作
     *
     * @param content
     * @return
     */

    public  String decrypt(String content) throws Exception {

        //实例化
        Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);

        //使用密钥初始化，设置为解密模式
        cipher.init(Cipher.DECRYPT_MODE, aesKey);

        //执行操作

        byte[] result = cipher.doFinal(MyBase64.getUrlDecoder().decode(content));


        return new String(result, "utf-8");
    }

    /**
     * 生成加密秘钥
     *
     * @return
     */
    private  String getSecretKey() throws Exception {
        //返回生成指定算法密钥生成器的 KeyGenerator 对象
        KeyGenerator kg = null;
        kg = KeyGenerator.getInstance(KEY_ALGORITHM);
        //AES 要求密钥长度为 128
        kg.init(128, new SecureRandom());
        //生成一个密钥
        SecretKey secretKey = kg.generateKey();
       // return Base64Utils.encodeToString(secretKey.getEncoded());// 转换为AES专用密钥

        return new String(Base64Utils.encode(secretKey.getEncoded()));//通过Base64转码返回
    }

}
