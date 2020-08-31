package com.hz.fine.master.core.controller;

import com.alibaba.fastjson.JSON;
import com.hz.fine.master.util.HodgepodgeMethod;

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

//        String balance = "0.73221121";
//        String groupMinMoney = "100.2458712313312";
        String balance = "18";
        String groupMinMoney = "3";
        int groupNum = 5;

//        int num = getGroupNumByMoney(balance, groupMinMoney, groupNum);
//        System.out.println("num:" + num);


//        String sb = "{'pid': 2500, 'type': 1, 'self': 0, 'head': '<msgsource>\\n\\t<silence>0</silence>\\n\\t<membercount>3</membercount>\\n</msgsource>\\n', 'wxid1': 'wxid_61qowsrtpt4d11', 'wxid2': '卢云', 'content': '1#5', 'log_nickname': '只听', 'log_wechatid': 'wxid_zsehsimp97dr22', 'chartid': '19349014070@chatroom', 'wxid3': '29闪电9'}";
        String sb1 = "{'pid': 2500, 'type': 1, 'self': 0, 'head': '<msgsource>\n\t<silence>0</silence>\n\t<membercount>3</membercount>\n</msgsource>\n', 'wxid1': 'wxid_61qowsrtpt4d11', 'wxid2': '卢云', 'content': '1#5', 'log_nickname': '只听', 'log_wechatid': 'wxid_zsehsimp97dr22', 'chartid': '19349014070@chatroom', 'wxid3': '29闪电9'}";
        sb1 = sb1.replaceAll("'", "\"");

        JSON.toJSONString(sb1);
        System.out.println("sb:" + sb1);

        String smsContent = "您尾号为1602的京卡于20年8月18日11:27通过超级网银转帐收入1061.00元。活期余额5.94元。对方账号尾号:86";
        String lastNum = "1602";
        if (smsContent.indexOf(lastNum) > -1){
            System.out.println("包含:" + smsContent.indexOf(lastNum));
        }
        for (int i = 0; i<50; i++){
            String str1 = "流行,小池,开心,俏皮,领空,浓烟,春水堂,溜达,无奈,红龙,那波,科技,发小,混合,核对";
            String str2 =HodgepodgeMethod.randomGroupName(str1);
            System.out.println("str2:" + str2);
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

//    public static int getGroupNumByMoney(String balance, String groupMinMoney, int groupNum){
//        int num = 0;
//        double balance_db = Double.parseDouble(balance);
//        double groupMinMoney_db = Double.parseDouble(groupMinMoney);
//
//        double res = balance_db/groupMinMoney_db;
//        int res_num = (int) Math.floor(res);
//        if (res_num <= 0){
//            num = 0;
//        }else{
//            if (res_num <= groupNum){
//                num = 0;
//            }else{
//                num = res_num - groupNum;
//            }
//        }
//        return num;
//    }



}
