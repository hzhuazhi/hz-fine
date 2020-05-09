package com.hz.fine.master.core.common.utils;

import java.util.Random;

/**
 * @Description TODO
 * @Author long
 * @Date 2019/11/12 21:42
 * @Version 1.0
 */
public class RandomUtil {
    public static String   getRandom(int   count){

        StringBuffer  random  =  new StringBuffer() ;
        Random r = new Random();
        for(int i=0 ; i<count ;  i++) {
            int ran1 = r.nextInt(10);
            random.append(ran1);
        }
        return random.toString();
    }


    public static String   getRandom(int   count,int num){

        StringBuffer  random  =  new StringBuffer() ;
        Random r = new Random();
        for(int i=0 ; i<count ;  i++) {
            int ran1 = r.nextInt(num);
            random.append(ran1);
        }
        return random.toString();
    }

    public static void main(String  [] args){
//        getRandom(6);
        for(int i=0;i<10;i++){
            System.out.println(getRandom(4,10));
        }

    }
}
