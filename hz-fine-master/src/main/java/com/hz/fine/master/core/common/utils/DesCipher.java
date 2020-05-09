package com.hz.fine.master.core.common.utils;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.security.SecureRandom;

/**
 * @Description: 将数据使用DES算法加密,方便进行网络传输,支持中文
 * @author yoko
 * @date 2019/11/22 15:45
*/
public class DesCipher {

	private final static String ALGORITHM = "DES";
	private final static String PWD = "gvTyY9PfGqdlxfZhHMxij6iJVIlAkEAv";

	private final static String ENCODING = "UTF-8";

	/**
	 * @Description: 普通DES加密
	 * <p>如果转换成字符串，则会在解密的时候报错</p>
	 * @param data - 要加密的数据
	 * @return byte
	 * @author yoko
	 * @date 2019/11/22 15:45
	*/
	public static byte[] encrypt(String data) throws Exception {
		SecureRandom random = new SecureRandom();
		DESKeySpec desKey = new DESKeySpec(PWD.getBytes());
		// 创建一个密匙工厂，然后用它把DESKeySpec转换成
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
		SecretKey securekey = keyFactory.generateSecret(desKey);
		// Cipher对象实际完成加密操作
		Cipher cipher = Cipher.getInstance(ALGORITHM);
		// 用密匙初始化Cipher对象
		cipher.init(Cipher.ENCRYPT_MODE, securekey, random);
		// 现在，获取数据并加密
		// 正式执行加密操作
		byte[] datasource = data.getBytes(ENCODING);
		return cipher.doFinal(datasource);
	}

	/**
	 * @Description: DES加密-base64
	 * <p>如果转换成字符串，则会在解密的时候报错；所以在代码最后进行base64加密返回数据</p>
	 * @param data - 要加密的数据
	 * @return String
	 * @author yoko
	 * @date 2019/11/22 15:45
	 */
	public static String encryptData(String data) throws Exception {
		SecureRandom random = new SecureRandom();
		DESKeySpec desKey = new DESKeySpec(PWD.getBytes());
		// 创建一个密匙工厂，然后用它把DESKeySpec转换成
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
		SecretKey securekey = keyFactory.generateSecret(desKey);
		// Cipher对象实际完成加密操作
		Cipher cipher = Cipher.getInstance(ALGORITHM);
		// 用密匙初始化Cipher对象
		cipher.init(Cipher.ENCRYPT_MODE, securekey, random);
		// 现在，获取数据并加密
		// 正式执行加密操作
		byte[] datasource = data.getBytes(ENCODING);
		// base64加密
		String res = Base64.encodeBase64String(cipher.doFinal(datasource));
		return res;
	}


	/**
	 * @Description: 普通DES解密
	 * <p>如果转换成字符串，则会在解密的时候报错</p>
	 * @param src - 要解密的数据
	 * @return byte
	 * @author yoko
	 * @date 2019/11/22 15:45
	 */
	public static String decrypt(byte[] src) throws Exception {
		// DES算法要求有一个可信任的随机数源
		SecureRandom random = new SecureRandom();
		// 创建一个DESKeySpec对象
		DESKeySpec desKey = new DESKeySpec(PWD.getBytes());
		// 创建一个密匙工厂
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
		// 将DESKeySpec对象转换成SecretKey对象
		SecretKey securekey = keyFactory.generateSecret(desKey);
		// Cipher对象实际完成解密操作
		Cipher cipher = Cipher.getInstance(ALGORITHM);
		// 用密匙初始化Cipher对象
		cipher.init(Cipher.DECRYPT_MODE, securekey, random);
		// 真正开始解密操作
		return new String(cipher.doFinal(src), ENCODING);
	}

	/**
	 * @Description: DES解密-base64
	 * <p>如果转换成字符串，则会在解密的时候报错；所以在解密的最后进行base64解码</p>
	 * @param data - 要解密的数据
	 * @return byte
	 * @author yoko
	 * @date 2019/11/22 15:45
	 */
	public static String decryptData(String data) throws Exception {
		// DES算法要求有一个可信任的随机数源
		SecureRandom random = new SecureRandom();
		// 创建一个DESKeySpec对象
		DESKeySpec desKey = new DESKeySpec(PWD.getBytes());
		// 创建一个密匙工厂
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
		// 将DESKeySpec对象转换成SecretKey对象
		SecretKey securekey = keyFactory.generateSecret(desKey);
		// Cipher对象实际完成解密操作
		Cipher cipher = Cipher.getInstance(ALGORITHM);
		// 用密匙初始化Cipher对象
		cipher.init(Cipher.DECRYPT_MODE, securekey, random);
		// 真正开始解密操作
		return new String(cipher.doFinal(Base64.decodeBase64(data)), ENCODING);
	}

	public static void main(String[] args) throws Exception{
		String data = "{\"fixedNum\":13717505293,\"ctime\":201911071802959,\"cctime\":201911071802959,\"sign\":\"abcdefg\",\"token\":\"小五哥1111111122a哈哈\"}";
//		String data = "{\"fixedNum\":13717505293,\"ctime\":201911071802959,\"cctime\":201911071802959,\"sign\":\"abcdefg\",\"token\":\"adcdef\"}";
		String resData = encryptData(data);
		String result = decryptData(resData);
		System.out.println("----result:" + result);
	}
}
