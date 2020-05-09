package com.hz.fine.master.core.common.utils;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Md5Utils {

  /**
   * 默认的密码字符串组合，用来将字节转换成 16 进制表示的字符,apache校验下载的文件的正确性用的就是默认的这个组合
   */
  protected static char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

  protected static MessageDigest messagedigest = null;

  public static String getMd5(String key) {
    return HexUtil.toHexString(md5(key));
  }

  public static String getMd5(String key, String charsetName) {
    return HexUtil.toHexString(md5(key, charsetName));
  }

  static MessageDigest getDigest() {
    try {
      return MessageDigest.getInstance("MD5");
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Calculates the MD5 digest and returns the value as a 16 element
   * <code>byte[]</code>.
   * 
   * @param data
   *          Data to digest
   * @return MD5 digest
   */
  public static byte[] md5(byte[] data) {
    return getDigest().digest(data);
  }

  /**
   * Calculates the MD5 digest and returns the value as a 16 element
   * <code>byte[]</code>.
   * 
   * @param data
   *          Data to digest
   * @return MD5 digest
   */
  public static byte[] md5(String data) {
    return md5(data.getBytes());
  }

  /**
   * Calculates the MD5 digest and returns the value as a 16 element
   * <code>byte[]</code>.
   * 
   * @param data
   *          Data to digest
   * @return MD5 digest
   */
  public static byte[] md5(String data, String charsetName) {
    try {
      return md5(data.getBytes(charsetName));
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Calculates the MD5 digest and returns the value as a 32 character hex
   * string.
   * 
   * @param data
   *          Data to digest
   * @return MD5 digest as a hex string
   */
  public static String md5Hex(byte[] data) {
    return HexUtil.toHexString(md5(data));
  }

  /**
   * Calculates the MD5 digest and returns the value as a 32 character hex
   * string.
   * 
   * @param data
   *          Data to digest
   * @return MD5 digest as a hex string
   */
  public static String md5Hex(String data) {
    return HexUtil.toHexString(md5(data));
  }

  private static String bufferToHex(byte bytes[]) {
    return bufferToHex(bytes, 0, bytes.length);
  }

  private static String bufferToHex(byte bytes[], int m, int n) {
    StringBuffer stringbuffer = new StringBuffer(2 * n);
    int k = m + n;
    for (int l = m; l < k; l++) {
      appendHexPair(bytes[l], stringbuffer);
    }
    return stringbuffer.toString();
  }

  private static void appendHexPair(byte bt, StringBuffer stringbuffer) {
    char c0 = hexDigits[(bt & 0xf0) >> 4];// 取字节中高 4 位的数字转换, >>>
    // 为逻辑右移，将符号位一起右移,此处未发现两种符号有何不同
    char c1 = hexDigits[bt & 0xf];// 取字节中低 4 位的数字转换
    stringbuffer.append(c0);
    stringbuffer.append(c1);
  }

  /**
   * 生成文件的md5校验值
   *
   * @param file
   * @return
   * @throws IOException
   */
  public static String getFileMD5String(File file) throws IOException {
    InputStream fis;
    fis = new FileInputStream(file);
    byte[] buffer = new byte[1024];
    int numRead = 0;
    while ((numRead = fis.read(buffer)) > 0) {
      messagedigest.update(buffer, 0, numRead);
    }
    fis.close();
    return bufferToHex(messagedigest.digest());
  }

  public static void main(String[] args) {
    System.out.println(Md5Utils.getMd5(
        "10098460077095324982865920026769359111.50.105.170178095656451000149259715918997159189金币礼包欢乐斗地主ZQYD3b5123b8036b4dbcaea441e8d2e5d566", "UTF-8"));
  }
}
