package com.hz.fine.master.core.common.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;


/**
 * @Description 短信验证码发送类
 * @Author yoko
 * @Date 2019/12/9 15:20
 * @Version 1.0
 */
public class SendSms {

    /**
     * @Description: 阿里云的验证码发送
     * @param phoneNum - 要发送的手机号
     * @param code - 要发送的验证码
     * @return boolean -是否发送成功：true表示发送成功；false表示发送失败
     * @author yoko
     * @date 2019/12/9 20:36
    */
    public static boolean aliSendSms(String phoneNum, String code){
//        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", "LTAI8UsQKfgMqzse", "BSc6JnPo8efgW78MQWzqg1HQ9Dhdhm");
        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", "LTAI4FoLtbXepN66yL6kB9ns", "66crjO6kEtjOnw5cAsJKXdxEF824k2");
        IAcsClient client = new DefaultAcsClient(profile);
        CommonRequest request = new CommonRequest();
        request.setMethod(MethodType.POST);
        request.setDomain("dysmsapi.aliyuncs.com");
        request.setVersion("2017-05-25");
        request.setAction("SendSms");
        request.putQueryParameter("RegionId", "cn-hangzhou");
        request.putQueryParameter("PhoneNumbers", phoneNum);
//        request.putQueryParameter("SignName", "趣红人");
//        request.putQueryParameter("TemplateCode", "SMS_180051427");
        request.putQueryParameter("SignName", "五百分享缤纷");
        request.putQueryParameter("TemplateCode", "SMS_182677934");
        request.putQueryParameter("TemplateParam", "{\"code\":"+ code +"}");
        try {
            CommonResponse response = client.getCommonResponse(request);
            System.out.println(response.getData());
            // {"Message":"OK","RequestId":"A774523C-1326-4082-AFCE-6E21A5455664","BizId":"116701575884312509^0","Code":"OK"}
            if (response != null && response.getData() != null){
                Object obj = JSON.parseObject(response.getData());
                String resultCode = (String) ((JSONObject) obj).get("Code");
                if (resultCode.equals("OK")){
                    return true;
                }else {
                    return false;
                }
            }else {
                return false;
            }
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return false;
    }


    public static void main(String[] args) {
//        String phoneNum = "15221696790";
        String phoneNum = "15221696790";
        String code = "1298";
        boolean flag = aliSendSms(phoneNum, code);
        System.out.println(flag);
    }
}
