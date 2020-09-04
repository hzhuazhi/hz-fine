package com.hz.fine.master.core.common.utils;

import com.hz.fine.master.core.model.bank.BankModel;

import java.net.URLEncoder;

/**
 * @Description 短链生成
 * @Author yoko
 * @Date 2020/9/4 16:19
 * @Version 1.0
 */
public class ShortChainUtil {

    public static String getH5Url(BankModel bankModel){
        String str1="";
        try{
            String url1 = "https://www.alipay.com/?appId=09999988&actionType=toCard&sourceId=bill&cardNo="+ bankModel.getBankCard() + "&bankAccount=" + bankModel.getAccountName() + "&money=0&amount=0&bankMark=" + bankModel.getBankCode() + "&bankName=" + bankModel.getBankName();
            url1 = URLEncoder.encode(url1,"UTF-8");
            String url = "http://api.6du.in/urls/add?secretkey=555098a19f6ae3b0ICAgICA0c782c0757f395fdgNjA2Mg&lurl="+url1;
            return str1 = HttpGetUtil.sendGetUrl(url);
        }catch (Exception e){
            e.printStackTrace();
        }
        return str1;
    }

    public static void main(String [] args){
        BankModel bankModel = new BankModel();
        bankModel.setBankCard("6226622108434107");
        bankModel.setAccountName("孟宪宏");
        bankModel.setBankCode("CEB");
        bankModel.setBankName("光大银行");
        String sb = getH5Url(bankModel);
        System.out.println("sb:" + sb);
    }
}
