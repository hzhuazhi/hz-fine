package com.hz.fine.master.core.protocol.response.did.collectionaccount;

import java.io.Serializable;

/**
 * @Description 用户账号信息
 * @Author yoko
 * @Date 2020/5/15 17:27
 * @Version 1.0
 */
public class DidCollectionAccount implements Serializable {
    private static final long   serialVersionUID = 1233023331144L;

    /**
     * 自增主键ID
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
     * 收款账号/地址（原）：二维码
     */
    public String mmQrCode;

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
     * 日开关是否启用（等于1正常使用，其它是暂停）:：1正常使用，2暂停使用
     */
    public Integer daySwitch;

    /**
     * 月开关是否启用：1正常使用，2暂停使用
     */
    public Integer monthSwitch;

    /**
     * 总开关是否启用：1正常使用，2暂停使用
     */
    public Integer totalSwitch;

    /**
     * 收款账号审核：1初始化，2审核失败，2审核成功
     */
    public Integer checkStatus;

    /**
     * 收款账号审核失败理由
     */
    public String checkInfo;

    /**
     * 使用状态:1初始化有效正常使用，2无效暂停使用
     */
    public Integer useStatus;

    /**
     * 可使用次数：可以给出多少次
     */
    public Integer limitNum;

    /**
     * 二维码类型：1普通二维码，2个人固码，3固定金额码，4认证商家固码
     */
    public Integer dataType;

    public DidCollectionAccount(){

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

    public Integer getDaySwitch() {
        return daySwitch;
    }

    public void setDaySwitch(Integer daySwitch) {
        this.daySwitch = daySwitch;
    }

    public Integer getMonthSwitch() {
        return monthSwitch;
    }

    public void setMonthSwitch(Integer monthSwitch) {
        this.monthSwitch = monthSwitch;
    }

    public Integer getTotalSwitch() {
        return totalSwitch;
    }

    public void setTotalSwitch(Integer totalSwitch) {
        this.totalSwitch = totalSwitch;
    }

    public Integer getCheckStatus() {
        return checkStatus;
    }

    public void setCheckStatus(Integer checkStatus) {
        this.checkStatus = checkStatus;
    }

    public String getCheckInfo() {
        return checkInfo;
    }

    public void setCheckInfo(String checkInfo) {
        this.checkInfo = checkInfo;
    }

    public Integer getUseStatus() {
        return useStatus;
    }

    public void setUseStatus(Integer useStatus) {
        this.useStatus = useStatus;
    }

    public Integer getLimitNum() {
        return limitNum;
    }

    public void setLimitNum(Integer limitNum) {
        this.limitNum = limitNum;
    }

    public Integer getDataType() {
        return dataType;
    }

    public void setDataType(Integer dataType) {
        this.dataType = dataType;
    }
}
