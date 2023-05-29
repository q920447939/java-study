package cn.withme.https;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.crypto.Cipher;

public class RSAUtil {

	public static final String SIGNATURE_INSTANCE = "SHA1WithRSA";

	/**
	 * @Title: getPublicKey
	 * @Description:实例化公钥
	 * @author weny.yang
	 * @date May 13, 2021
	 */
	public static PublicKey getPublicKey(String publicKey) throws Exception {
		byte[] publicKeyBytes = Base64.getDecoder().decode(publicKey.getBytes());
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		return keyFactory.generatePublic(keySpec);
	}

	/**
	 * @Title: getPrivateKey
	 * @Description:实例化私钥
	 * @author weny.yang
	 * @date May 13, 2021
	 */
	public static PrivateKey getPrivateKey(String privateKey) throws Exception {
		byte[] privateKeyBytes = Base64.getDecoder().decode(privateKey.getBytes());
		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		return keyFactory.generatePrivate(keySpec);
	}

	/**
	 * @Title: encryptByPublicKey
	 * @Description:公钥加密
	 * @author weny.yang
	 * @date May 13, 2021
	 */
	public static byte[] encryptByPublicKey(byte[] content, String publicKey) throws Exception {
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.ENCRYPT_MODE, getPublicKey(publicKey));
		return cipher.doFinal(content);
	}

	/**
	 * @Title: decryptByPrivateKey
	 * @Description:私钥解密
	 * @author weny.yang
	 * @date May 13, 2021
	 */
	public static byte[] decryptByPrivateKey(byte[] content, String privateKey) throws Exception {
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.DECRYPT_MODE, getPrivateKey(privateKey));
		return cipher.doFinal(content);
	}


	/**
	 * @Title: sign
	 * @Description:私钥签名
	 * @author weny.yang
	 * @date May 13, 2021
	 */
	public static byte[] sign(byte[] content, String privateKey) throws Exception {
		Signature signature = Signature.getInstance(SIGNATURE_INSTANCE);
		signature.initSign(getPrivateKey(privateKey));
		signature.update(content);
		return signature.sign();
	}

	/**
	 * @Title: verify
	 * @Description:公钥验签
	 * @author weny.yang
	 * @date May 13, 2021
	 */
	public static boolean verify(byte[] content, byte[] sign, String publicKey) throws Exception {
		Signature signature = Signature.getInstance(SIGNATURE_INSTANCE);
		signature.initVerify(getPublicKey(publicKey));
		signature.update(content);
		return signature.verify(sign);
	}

	public static void main(String[] args) throws Exception {
		String content = "这座城市每一个角落，都填满了若有所思的生活！";
		String privateKey = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBALJdXl5hVjEl8VLvwB1UaBRfzSetFch3Y6lQnmkV43XYGifRmrayM/JZWLA1ZGNXI+gqu73GYrMY8tLhQ7sneYF8rMdKqgYuBgwvYLtoNz8Rc8On/qkSssuCXXzRyOE1NHXheXeFBbkOvxXkzoG6Ry7O1205RD8Iw6ZWQV4OMPB7AgMBAAECgYBlgzQ5POakNDcDf8X0K1zM87U8NosmO6Sx8Vsm7bk2EZbZQLbv8b4U2Prb1pda3nZmJSQSnvz/d1yD3ocA1jeVIVUUEPhVPVVNFkF3Cq0I5Y/USAJFJAQjJRO71pwEO116PwDLmAVKwUFnhq7QKvvFMAq9kYSCrVQLnV3oDF+Z+QJBAOoOFU5ocGVSRCk+D3ub7vTm9nkIvIGjvQIzJV8l8Iz9S5PX1kVh7VF4pUJjVUsdB9gmmsJm//5QyvhbujULvL0CQQDDFpOUdtuYMAqTN4HJ3QUMdsVYs0yscu66bhrlYziJEjVNVD9H6Twm8Z20xqvbDT3955oe1UF/wP2pH7AzVWGXAkAlrmTBvdCyjz8IAMTPWrhq1zBBJiXrHShVzVQr8Fi86h6by7jkbLKfVjQM7x8RO7hClVx/BcQa9njm6SWeWqhlAkBQT2PzkzZ1IRnJXulUE2N3rREyoaUnvKygHKF/2ue0LcW5jrv6O36ivYZhnAO1Sm9Bre0ZUksDb4w86imcCEMDAkAdC7dQQPCElFsVIcIaw639LqPshy0J98t67z5NleTOnx1KvhpoG+7O92+PLNUWH+LmkiLuVRe0LNRY2vVVOk93";
		String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCyXV5eYVYxJfFS78AdVGgUX80nrRXId2OpUJ5pFeN12Bon0Zq2sjPyWViwNWRjVyPoKru9xmKzGPLS4UO7J3mBfKzHSqoGLgYML2C7aDc/EXPDp/6pErLLgl180cjhNTR14Xl3hQW5Dr8V5M6BukcuztdtOUQ/CMOmVkFeDjDwewIDAQAB";

		byte[] encrypt = encryptByPublicKey(content.getBytes("UTF-8"), publicKey);
		byte[] decryp = decryptByPrivateKey(encrypt, privateKey);
		System.out.println("明文："+new String(decryp));

		byte[] sign = sign(content.getBytes("UTF-8"), privateKey);
		boolean verify = verify(content.getBytes("UTF-8"), sign, publicKey);
		System.out.println("验签结果："+verify);
	}

}
