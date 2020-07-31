package com.hz.fine.master.core.protocol.request.did;

import com.hz.fine.master.core.protocol.base.BaseRequest;

import java.io.Serializable;

/**
 * @Description 协议：用户的收款账号的实体Bean
 * @Author yoko
 * @Date 2020/5/15 15:19
 * @Version 1.0
 */
public class RequestDidCollectionAccount extends BaseRequest implements Serializable {
    private static final long   serialVersionUID = 1233283332141L;

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
     * 支付宝的账号ID
     */
    public String userId;

    /**
     * 收款账号/地址（原）：二维码
     */
    public String mmQrCode;

    /**
     * 收款账号/地址（转码）：二维码
     */
    public String ddQrCode;

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
     * 使用状态:1初始化有效正常使用，2无效暂停使用
     */
    public Integer useStatus;

    /**
     * 是否有效：0初始化有效，1删除
     */
    public Integer yn;

    /**
     * 操作密码
     */
    public String operateWd;

    /**
     * 是否失效：1未失效，2已失效
     */
    public Integer isInvalid;


    public RequestDidCollectionAccount(){

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

    public String getMmQrCode() {
        return mmQrCode;
    }

    public void setMmQrCode(String mmQrCode) {
        this.mmQrCode = mmQrCode;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public Integer getUseStatus() {
        return useStatus;
    }

    public void setUseStatus(Integer useStatus) {
        this.useStatus = useStatus;
    }

    public Integer getYn() {
        return yn;
    }

    public void setYn(Integer yn) {
        this.yn = yn;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOperateWd() {
        return operateWd;
    }

    public void setOperateWd(String operateWd) {
        this.operateWd = operateWd;
    }

    public String getDdQrCode() {
        return ddQrCode;
    }

    public void setDdQrCode(String ddQrCode) {
        this.ddQrCode = ddQrCode;
    }

    public Integer getIsInvalid() {
        return isInvalid;
    }

    public void setIsInvalid(Integer isInvalid) {
        this.isInvalid = isInvalid;
    }
}
