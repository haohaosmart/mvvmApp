package com.wanghao.mvvmapp.mvvm.encrypt;


import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;


public class RSASignatureUtil {

    private  String publicKeyString = Const.RSA_PUBLIC_KEY;

    private  String privateKeyString = Const.RSA_PRIVATE_KEY;


    /** 秘钥对算法名称 */
    private static final String ALGORITHM = "RSA";

    /** 密钥长度 */
    private static final int KEY_SIZE = 1024;

    /** 签名算法 */
    private static final String SIGNATURE_ALGORITHM = "SHA1withRSA";

    private static PrivateKey priKey;
    private static PublicKey pubKey;

    public static RSASignatureUtil getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final RSASignatureUtil INSTANCE = new RSASignatureUtil();
    }


    private RSASignatureUtil() {
        try {
            //公钥初始化
            X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(Base64Utils.decode(publicKeyString.toCharArray()));
            KeyFactory publicKeyFactory = KeyFactory.getInstance(ALGORITHM);
            pubKey = publicKeyFactory.generatePublic(x509EncodedKeySpec);
            //私钥初始化
            PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(Base64Utils.decode(privateKeyString.toCharArray()));
            KeyFactory privateKeyFactory = KeyFactory.getInstance(ALGORITHM);
            priKey = privateKeyFactory.generatePrivate(pkcs8EncodedKeySpec);
        } catch (Exception e) {
            System.out.println("RSA密钥初始化错误！！！");
        }
    }


    /**
     * 随机生成 RSA 密钥对（包含公钥和私钥）
     */
    public static KeyPair generateKeyPair() throws Exception {
        // 获取指定算法的密钥对生成器
        KeyPairGenerator gen = KeyPairGenerator.getInstance(ALGORITHM);

        // 初始化密钥对生成器（指定密钥长度, 使用默认的安全随机数源）
        gen.initialize(KEY_SIZE);

        // 随机生成一对密钥（包含公钥和私钥）
        return gen.generateKeyPair();
    }



    /**
     * 私钥签名（数据）: 用私钥对指定字节数组数据进行签名, 返回签名信息
     */

    public  String sign(String data) throws Exception {
        // 根据指定算法获取签名工具
        Signature sign = Signature.getInstance(SIGNATURE_ALGORITHM);

        // 用私钥初始化签名工具
        sign.initSign(priKey);

        // 添加要签名的数据
        sign.update(data.getBytes());

        // 计算签名结果（签名信息）
        byte[] signInfo = sign.sign();

        return new String(MyBase64.getUrlEncoder().encodeToString(signInfo));
    }

    /**
     * 公钥验签（数据）: 用公钥校验指定数据的签名是否来自对应的私钥
     */
    public  boolean verify(String data, String signInfo) throws Exception {
        // 根据指定算法获取签名工具
        Signature sign = Signature.getInstance(SIGNATURE_ALGORITHM);

        // 用公钥初始化签名工具
;        sign.initVerify(pubKey);

        // 添加要校验的数据
        sign.update(data.getBytes());

        // 校验数据的签名信息是否正确,
        // 如果返回 true, 说明该数据的签名信息来自该公钥对应的私钥,
        // 同一个私钥的签名, 数据和签名信息一一对应, 只要其中有一点修改, 则用公钥无法校验通过,
        // 因此可以用私钥签名, 然后用公钥来校验数据的完整性与签名者（所有者）

        boolean verify = sign.verify( MyBase64.getUrlDecoder().decode(signInfo));

        return verify;
    }
}
