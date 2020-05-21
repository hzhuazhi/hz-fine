package com.hz.fine.master.core.controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @Description TODO
 * @Author yoko
 * @Date 2020/5/9 19:43
 * @Version 1.0
 */
public class Temp {
    public static void main(String[] args) {
        SimpleDateFormat df = new SimpleDateFormat("HH:mm");//设置日期格式
//初始化
        Date nowTime = null;
        Date beginTime = null;
        Date endTime = null;
        try {
            //格式化当前时间格式
//            nowTime = df.parse(df.format(new Date()));
            nowTime = df.parse("17:59");
            //定义开始时间
            beginTime = df.parse("17:00");
            //定义结束时间
//            endTime = df.parse("09:00");
            endTime = df.parse("09:00");
        } catch (Exception e) {
            e.printStackTrace();
        }
        //调用判断方法
        boolean flag = belongCalendar(nowTime, beginTime, endTime);
        //输出为结果
        if (flag) {
            //处于规定的时间段内，执行对应的逻辑代码
            System.out.println(flag);
        } else {
            //不处于规定的时间段内，执行对应的逻辑代码
            System.out.println(flag);
        }
    }

    /**
     * 判断时间是否在时间段内
     * @param nowTime
     * @param beginTime
     * @param endTime
     * @return
     */
    public static boolean belongCalendar(Date nowTime, Date beginTime, Date endTime) {
        //设置当前时间
        Calendar date = Calendar.getInstance();
        date.setTime(nowTime);
        //设置开始时间
        Calendar begin = Calendar.getInstance();
        begin.setTime(beginTime);
        //设置结束时间
        Calendar end = Calendar.getInstance();
        end.setTime(endTime);
        //处于开始时间之后，和结束时间之前的判断
        if (date.after(begin) && date.before(end)) {
            return true;
        } else {
            return false;
        }
    }



}
