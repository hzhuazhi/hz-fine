package com.hz.fine.master.core.protocol.request.did.collection;

import com.hz.fine.master.core.protocol.base.BaseRequest;

import java.io.Serializable;
import java.util.List;

/**
 * @Description 协议：用户收款账号+用户二维码信息合二为一
 * @Author yoko
 * @Date 2020/6/17 16:33
 * @Version 1.0
 */
public class RequestDidCollectionAccountAll extends BaseRequest implements Serializable {
    private static final long   serialVersionUID = 1233283932171L;

    /**
     * 用户收款账号的ID
     */
    public Long id;

    /**
     * 收款账户名称：用户备注使用
     */
    public String acName;

    /**
     * 收款账户类型：1微信，2支付宝，3银行卡
     */
    public Integer acType;

    /**
     * 收款的具体账号：类型为微信则微信账号，支付宝为支付宝账号；怕后期有其它冲突
     */
    public String acNum;

    /**
     * 收款人：微信昵称、支付宝昵称、银行卡持有人名称
     */
    public String payee;

    /**
     * 银行名称/银行卡开户行
     */
    public String bankName;

    /**
     * 经营范围类型:1个人，2个体，3公司
     */
    public Integer businessType;

    /**
     * 小微商户二维码图片地址
     */
    public String wxQrCodeAds;

    /**
     * 收款账号的二维码集合
     */
    public List<QrCode> dataList;



    public RequestDidCollectionAccountAll(){

    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAcName() {
        return acName;
    }

    public void setAcName(String acName) {
        this.acName = acName;
    }

    public Integer getAcType() {
        return acType;
    }

    public void setAcType(Integer acType) {
        this.acType = acType;
    }

    public String getAcNum() {
        return acNum;
    }

    public void setAcNum(String acNum) {
        this.acNum = acNum;
    }

    public String getPayee() {
        return payee;
    }

    public void setPayee(String payee) {
        this.payee = payee;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public Integer getBusinessType() {
        return businessType;
    }

    public void setBusinessType(Integer businessType) {
        this.businessType = businessType;
    }

    public String getWxQrCodeAds() {
        return wxQrCodeAds;
    }

    public void setWxQrCodeAds(String wxQrCodeAds) {
        this.wxQrCodeAds = wxQrCodeAds;
    }

    public List<QrCode> getDataList() {
        return dataList;
    }

    public void setDataList(List<QrCode> dataList) {
        this.dataList = dataList;
    }
}
