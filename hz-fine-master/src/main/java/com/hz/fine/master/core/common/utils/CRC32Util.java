package com.hz.fine.master.core.common.utils;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.zip.CRC32;

/**
 * @author df
 * @Description:计算crc的值
 * @create 2018-12-24 10:58
 **/
public class CRC32Util {
    private static Logger logger    = LoggerFactory.getLogger(CRC32Util.class);
    /**
     * @Description: TODO(计算CRC32的值)
     * @author df
     * @param data-值
     * @create 11:06 2018/12/24
     **/
    public static long getCRC32(String data){
        long num = 0;
        CRC32 crc32 = new CRC32();
        crc32.update(data.getBytes());
        num = crc32.getValue();
        logger.info("CRC32Util.getCRC32()----------------------");
        return num;
    }


}
