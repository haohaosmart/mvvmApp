package com.wanghao.mvvmapp.mvvm.encrypt;

/*
 --------------------------------------------**********--------------------------------------------

 该算法于1977年由美国麻省理工学院MIT(Massachusetts Institute of Technology)的Ronal Rivest，Adi Shamir和Len Adleman三位年轻教授提出，并以三人的姓氏Rivest，Shamir和Adlernan命名为RSA算法，是一个支持变长密钥的公共密钥算法，需要加密的文件快的长度也是可变的!

 所谓RSA加密算法，是世界上第一个非对称加密算法，也是数论的第一个实际应用。它的算法如下：

 1.找两个非常大的质数p和q（通常p和q都有155十进制位或都有512十进制位）并计算n=pq，k=(p-1)(q-1)。

 2.将明文编码成整数M，保证M不小于0但是小于n。

 3.任取一个整数e，保证e和k互质，而且e不小于0但是小于k。加密钥匙（称作公钥）是(e, n)。

 4.找到一个整数d，使得ed除以k的余数是1（只要e和n满足上面条件，d肯定存在）。解密钥匙（称作密钥）是(d, n)。

 加密过程： 加密后的编码C等于M的e次方除以n所得的余数。

 解密过程： 解密后的编码N等于C的d次方除以n所得的余数。

 只要e、d和n满足上面给定的条件。M等于N。

 --------------------------------------------**********--------------------------------------------
 */

import java.math.BigInteger;
import java.net.URLDecoder;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.crypto.Cipher;

public class RSA {
	/** 指定key的大小 */
	private static int KEYSIZE = 1024;

	private static final String SIGNATURE_ALGORITHM = "SHA1withRSA";
	/** 秘钥对算法名称 */
	private static final String ALGORITHM = "RSA";

	/**
	 * 生成密钥对
	 */
	public static Map<String, String> generateKeyPair() throws Exception {
		/** RSA算法要求有一个可信任的随机数源 */
		SecureRandom sr = new SecureRandom();
		/** 为RSA算法创建一个KeyPairGenerator对象 */
		KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
		/** 利用上面的随机数据源初始化这个KeyPairGenerator对象 */
		kpg.initialize(KEYSIZE, sr);
		/** 生成密匙对 */
		KeyPair kp = kpg.generateKeyPair();
		/** 得到公钥 */
		Key publicKey = kp.getPublic();
		byte[] publicKeyBytes = publicKey.getEncoded();
		String pub = new String(Base64.encodeBase64(publicKeyBytes),
				ConfigureEncryptAndDecrypt.CHAR_ENCODING);
		/** 得到私钥 */
		Key privateKey = kp.getPrivate();
		byte[] privateKeyBytes = privateKey.getEncoded();
		String pri = new String(Base64.encodeBase64(privateKeyBytes),
				ConfigureEncryptAndDecrypt.CHAR_ENCODING);

		Map<String, String> map = new HashMap<String, String>();
		map.put("publicKey", pub);
		map.put("privateKey", pri);
		RSAPublicKey rsp = (RSAPublicKey) kp.getPublic();
		BigInteger bint = rsp.getModulus();
		byte[] b = bint.toByteArray();
		byte[] deBase64Value = Base64.encodeBase64(b);
		String retValue = new String(deBase64Value);
		map.put("modulus", retValue);
		return map;
	}

	/**
	 * 加密方法 source： 源数据
	 */
	public static String encrypt(String source, String publicKey)
			throws Exception {
		Key key = getPublicKey(publicKey);
		/** 得到Cipher对象来实现对源数据的RSA加密 */
		Cipher cipher = Cipher.getInstance(ConfigureEncryptAndDecrypt.RSA_ALGORITHM);
		cipher.init(Cipher.ENCRYPT_MODE, key);
		byte[] b = source.getBytes();
		/** 执行加密操作 */
		byte[] b1 = cipher.doFinal(b);
		return new String(Base64.encodeBase64(b1),
				ConfigureEncryptAndDecrypt.CHAR_ENCODING);
	}

	/**
	 * 解密算法 cryptograph:密文
	 */
	public static String decrypt(String cryptograph, String privateKey)
			throws Exception {
		Key key = getPrivateKey(privateKey);
		/** 得到Cipher对象对已用公钥加密的数据进行RSA解密 */
		Cipher cipher = Cipher.getInstance(ConfigureEncryptAndDecrypt.RSA_ALGORITHM);
		cipher.init(Cipher.DECRYPT_MODE, key);
		byte[] b1 = Base64.decodeBase64(cryptograph.getBytes());
		/** 执行解密操作 */
		byte[] b = cipher.doFinal(b1);
		return new String(b);
	}

	/**
	 * 得到公钥
	 * 
	 * @param key
	 *            密钥字符串（经过base64编码）
	 * @throws Exception
	 */
	public static PublicKey getPublicKey(String key) throws Exception {
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(
				Base64.decodeBase64(key.getBytes()));
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		PublicKey publicKey = keyFactory.generatePublic(keySpec);
		return publicKey;
	}

	/**
	 * 得到私钥
	 * 
	 * @param key
	 *            密钥字符串（经过base64编码）
	 * @throws Exception
	 */
	public static PrivateKey getPrivateKey(String key) throws Exception {
		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(
				Base64.decodeBase64(key.getBytes()));
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
		return privateKey;
	}

	public static String sign(String content, String privateKey) {
		String charset = ConfigureEncryptAndDecrypt.CHAR_ENCODING;
		try {
			PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(
					Base64.decodeBase64(privateKey.getBytes()));
			KeyFactory keyf = KeyFactory.getInstance("RSA");
			PrivateKey priKey = keyf.generatePrivate(priPKCS8);

			Signature signature = Signature.getInstance("SHA1WithRSA");

			signature.initSign(priKey);
			signature.update(content.getBytes(charset));

			byte[] signed = signature.sign();

			return new String(Base64.encodeBase64(signed));
		} catch (Exception e) {

		}

		return null;
	}

	public static boolean checkSign(String content, String sign, String publicKey)
	{
		try 
		{
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
	        byte[] encodedKey = Base64.decode2(publicKey);
	        PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));

		
			Signature signature = Signature
			.getInstance("SHA256withRSA");
		
			signature.initVerify(pubKey);
			signature.update( content.getBytes("utf-8") );
		
			boolean bverify = signature.verify( Base64.decode2(sign) );
			return bverify;
			
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return false;
	}

	private static String privateKey="MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAKxdN+2FRtA4LLQLVhe1uvYxzvK4" +
			"a9wD9omYEPNS8dXUi2wc9ZTTeOYz5V3mLbHzBF2pubKxqJIA2DXem3mM/B6/919noT/Pwl3yqqZSWuxPPTbumOlmfeq3dl" +
			"gSbuFeRttpgI7fqKynO03K9Cauw7oFqKfVbL0toqQ8+9SJ9Rm9AgMBAAECgYARLTz8k5KJ/38c9iA/PqICFNadw5kkFNUF" +
			"wzy1MufculCmloA2s5hRwIwz/lNBb4lY2CgkUaYN3r2tKKDvzQf20hELR8n1D9cFjnNPzXqdahjv6MHfc8TEhZm+t844hXF" +
			"3ret+1fLfZbOfmRXgyV2t74z4VgrpBn3tgKaz7e5TFQJBAPgz72Uz4cGKj9htDEFMYOL1gkIb3HkEKILiOLv4pjzVpJrIxoi" +
			"YHT+8dMqpD46UX4Mmor8nkdMJxMKiudkwLksCQQCxx2IFv+xt73hHwRmfwlk41sbppThLWG3gcy8S2ScxMzzeMbJpQ4rz0oX" +
			"f0ak0tM9PUcWgHmZyvqjkXqXHODMXAkEAkJ1qCdRd4G/EejynXFCNTw/vEme7YQNsOz5mAhfRfDaaztjW3HWM7Tr37bDtcIzA" +
			"zeCwCKKck5LoguNfV8172wJALpIci4ULWoEw8RsUq1UuKkWZNfuiE5rGrdV+r2FTcwAJIevC4PBgph6wE0HJIZV7+Ttimd9c1x" +
			"I7DBf/NYDzYQJAASi6qBTBBIWxxUU3QM2AUluPKjd2WX6uOhmSE3jhtPN0wonyiAcWP3cSITI86nc0uPxkkApyXqMlPUej39lK" +
			"OQ==";

	private static  String punKey=    "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCsXTfthUbQOCy0C1YXtbr2Mc7yuGvcA/aJmBDzUvHV1ItsHPWU03jmM+Vd5i2x8wRdqbmysaiSANg13pt5jPwev/dfZ6E/z8Jd8qqmUlrsTz027pjpZn3qt3ZYEm7hXkbbaYCO36ispztNyvQmrsO6Bain1Wy9LaKkPPvUifUZvQIDAQAB";
	public static String PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCxGIk4mSchwWTUUJXH+A4Boscq\r"
			+ "7Wqd8hZyBdvYWRNClIZUCGlWzbluogLhelEqFBhnobVKQPqG7RrMTCuVb3bLv/LD\r"
			+ "kjEvWFmgECOhVE23/3bHIAgSHjYB4GTqZfpEsWY/4tclVcC+zboG2iai5oo4bnrV\r"
			+ "QmSYrIUjQx/s8IjxkwIDAQAB\r";
	public static void main(String[] args) throws Exception{
		//System.out.println(encrypt("xzdsxsas12",punKey));
		String s="oEn1n+IS0p18J2Zlibi+i83mmW9KjZUI1h7OvtcQLE8QoE+cs+wH9sJeCkz7O1HGHfJRaEiqI6x41O4Mdn6bUIH9aLwjnDDhDJO+/Fd86gmI36PmJu8uSVTkHEDBeZX8MWPXZ3WxmbkFh+QEMEECnmFprknKUPhBNN09G5L04CI=";
		//System.out.println(decrypt(s,privateKey));
		String c="XTXvP5yoIRLww4erT2DtC2JsWOuVvfk7hw+RAA83JQMiWsM8ii4onzMiYf3lgqqej3vaX9iw/HR6nN+jwQ7+4J/VHb8wyN/xhp8Xf2lRixaNTCWuF5FpsPfnraZcioTL58hqejycXWYTMvu4hqdwhus/uo8HcIU9jBY77ERTBy4=";
		String d="n0KVM2yNp6N8MdcrFQ34+a9BpyY0PR5Jwka+m5mOHMYvva6XhOKUnqJSkKvjbS6D9un7ILe0Y9lWx8PjPjUI6c376TCh0r/DJ+yC00TEIPfi5PW8Vm+rXRdyEDICwwW77Segpf3j5+WYzdp9+qJdao8NRTCP5x6W1dv//JwxkHE=";
		String zx="n0KVM2yNp6N8MdcrFQ34%2Ba9BpyY0PR5Jwka%2Bm5mOHMYvva6XhOKUnqJSkKvjbS6D9un7ILe0Y9lWx8PjPjUI6c376TCh0r%2FDJ%2ByC00TEIPfi5PW8Vm%2BrXRdyEDICwwW77Segpf3j5%2BWYzdp9%2BqJdao8NRTCP5x6W1dv%2F%2FJwxkHE%3D";
		String decrypt = decrypt(URLDecoder.decode(zx,"utf-8"), privateKey);
		System.out.println(decrypt);
		//System.out.println(decrypt.equals("Kl0Ta2ALUGYPbf5BeGWtWA=="));
		System.out.println(AES.decryptFromBase64(URLDecoder.decode("NBcbrpPenBE4Q4C%2FXm1Beg%3D%3D","utf-8"), "RxqS9W4lsJ0bk0a3"));
		//System.out.println(URLEncoder.encode(d,"utf-8"));
		//System.out.println(URLEncoder.encode("NBcbrpPenBE4Q4C/Xm1Beg==","utf-8"));

	}

	public static void main8(String[] args) {
		String postParams = "06901234567892";

		String aesKey = getKey();
		String encrypt_data = AES.encryptToBase64(postParams, aesKey);
		String postBody = "";
      //Kl0Ta2ALUGYPbf5BeGWtWA==

		//FxImhkXp3ZZqamxMI0bPju8BqMM5neMhpYn601nCaLrB80NeZ8RhGDgXxxFci5NxnKAEz5ZrQ6/yV53IlF1HAkp3B2wmjDueoZvuY+mWKHdvXVEIFOqqSBpYOT3lUaiNwTXLp8SMdW34x+Qd9qTdUM/GoidXFOw3QBnXC4J6AYE=
		//RSA加密
		try {
			String encrypt_key = RSA.encrypt(aesKey, punKey);
			System.out.println(encrypt_data);
			System.out.println(encrypt_key);
			HashMap<String, String> strHashMap = new HashMap<>();
			strHashMap.put("data", encrypt_data);
			///Log.d(TAG,"encrypt_data is "+encrypt_data);
			strHashMap.put("encryptKey", encrypt_key);
			//Log.d(TAG,"encryptKey is "+encrypt_key);
			//postBody = URLEncoder.encode(gson.toJson(strHashMap),"UTF-8");
			//Log.d(TAG,"postBody is "+postBody);
		} catch (Exception e) {
			e.printStackTrace();
			//Log.e("WTRequest", "Something is wrong with RSA");
		}
	}
	public static String getKey(){
		String str="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		StringBuffer sb = new StringBuffer();
		Random random = new Random();
		for(int i=0;i<16;i++){
			int index = random.nextInt(str.length());
			sb.append(str.charAt(index));

		}
		return sb.toString();
	}

}