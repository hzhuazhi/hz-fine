package com.hz.fine.master.core.common.utils;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * @Description php和java之间aes加密的互通
 * @Author yoko
 * @Date 2019/12/24 11:25
 * @Version 1.0
 */
public class Security {

    /**
     * @Description: 加密
     * @param input - 要加密的数据
     * @param key - 加密key
     * @return
     * @author yoko
     * @date 2019/12/24 13:59
    */
    public static String encrypt(String input, String key){
        byte[] crypted = null;
        try{
            SecretKeySpec skey = new SecretKeySpec(key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, skey);
            crypted = cipher.doFinal(input.getBytes());
        }catch(Exception e){
            System.out.println(e.toString());
        }
        return new String(Base64.encodeBase64(crypted));
    }

    /**
     * @Description: 解密
     * @param input - 已加密的加密串
     * @param key - 加密key
     * @return String
     * @author yoko
     * @date 2019/12/24 12:00
    */
    public static String decrypt(String input, String key){
        byte[] output = null;
        try{
            SecretKeySpec skey = new SecretKeySpec(key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, skey);
            output = cipher.doFinal(Base64.decodeBase64(input));
        }catch(Exception e){
            System.out.println(e.toString());
        }
        return new String(output);
    }

    public static void main(String[] args) {
        String key = "sjglsi139djsl13d";
        String data = "{\"fullName\":\"小五哥\",\"idCard\":\"435202111111111111\",\"fixedType\":2,\"fixedNum\":13717505292,\"agtVer\":1,\"clientVer\":1,\"ctime\":201911071802959,\"cctime\":201911071802959,\"sign\":\"abcdefg\",\"token\":\"111111\"}";
//        2oxUZ+/SAwMjCejurVpgQMYRs+nHPoiIIQDwNqZ+8z5wxcq0DmhDBa5Qc8VjMzv12xYSfRHDE5JVWFbqwPNP79ca/WXh/TVbMgBcQQ/1nXZNmQSJYC95FwSj7Ig1Q9UlvdDfIQAI8L04aM/Zpa1AMRk9zcwrVraJ5WJL7ohuz5opNFDxp9c9FxoRAJ8N5FgCJq+78EDydedC/sLu8vvFlH6fNjxMoztFg7cY5XadSGG4NBQZ1OmlTK2pOUOVHQlJ9VcoxdM/yVI7piCyV5pMPg==
//        System.out.println(Security.decrypt(Security.encrypt(data, key), key));
        System.out.println(Security.encrypt(data, key));
        System.out.println(Security.decrypt("eKWTul8Sts1281MNLdiOPS7V+O4lNbNzp7fpRl+EUeGDFcEtchHmhP1xa2u3d+IetcZTkfuV0aFYOW2+DftDkLQop6nNASCmXz7Bu0UjdQ1aOXHa6TDcAr+gIP/oZBee1Rx1f7+n9NYFzpF4oVquT+g+tQBEAgUAjPlPYe9QDVrk9xUWHLbsV1AFGRnoTu33fRlHufOSldzpu5VcKKtKPj1dKQ3E1ezHGSHQvj1qgvNB/akK859BuO9UxAM0sWqAt9lXJYMMk4jMTgUCEdLL6A==", key));
    }
}
