package com.hz.fine.master.core.common.utils;

import org.apache.commons.codec.binary.Hex;

import java.security.SecureRandom;
import java.util.UUID;

/**
 * Created by wenqi.huang on 16/6/16.
 */
public class UUIDUtils {
    /**
     * 生成一个32位uuid，注意此方法生成的uuid是不安全的uuid，顺序性较强，随机性弱，不能用于sessionId等场合。
     * @return
     */
    public static String createUUID(){
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * 生成一个32位安全uuid，可以用于sessionId、作为密钥等场合。
     * @return
     */
    public static String createSecureUUID(){
        SecureRandom ranGen = new SecureRandom();
        byte[] bs = new byte[16]; // 16 bytes = 128 bits
        ranGen.nextBytes(bs);
        return Hex.encodeHexString(bs);
    }

    /**
     * 生成一个32位安全uuid，可以用于sessionId、作为密钥等场合。
     * @return
     */
    public static String createInviteCode(){
        SecureRandom ranGen = new SecureRandom();
        byte[] bs = new byte[6]; // 16 bytes = 128 bits
        ranGen.nextBytes(bs);
        String InviteCode=Hex.encodeHexString(bs).substring(0,6);
        return InviteCode;
    }


}
