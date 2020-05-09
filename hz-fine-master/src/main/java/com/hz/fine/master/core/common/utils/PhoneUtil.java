package com.hz.fine.master.core.common.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Description TODO
 * @Author long
 * @Date 2019/11/26 11:40
 * @Version 1.0
 */
public class PhoneUtil {
    public static boolean isMobile( String str) {
        Pattern p = null;
        Matcher m = null;
        boolean b = false;
        p = Pattern.compile("^[1][3,4,5,7,8,9][0-9]{9}$"); // 验证手机号
        m = p.matcher(str);
        b = m.matches();
        return b;
    }

    public static boolean  isInvalidMobile(String mobiles){
        String  mobStr = mobiles.substring(0,4);
        if(mobStr.indexOf("167")<=0||mobStr.indexOf("170")<=0||
                mobStr.indexOf("171")<=0){
            return  true;
        }

        return  false;
    }

    public static  void  main(String  [] agas){

        System.out.println(PhoneUtil.isInvalidMobile("17145332232"));
        System.out.println(PhoneUtil.isMobile("144045332231"));
    }
}
