package com.hz.fine.master.core.protocol.response;

/**
 * @Description TODO
 * @Author yoko
 * @Date 2020/5/9 19:36
 * @Version 1.0
 */
public class Temp {

    public static void main(String [] args){
        String type = "108";
        boolean flag_type = true;
        if (type.equals("200")){
            flag_type = false;
        }
        if (type.equals("201")){
            flag_type = false;
        }
        System.out.println("flag_type:" + flag_type);
        if (type.equals("200") || type.equals("201")){
            System.out.println("登入、登出消息");
        }else {
            System.out.println("其它消息");
        }
    }
}
