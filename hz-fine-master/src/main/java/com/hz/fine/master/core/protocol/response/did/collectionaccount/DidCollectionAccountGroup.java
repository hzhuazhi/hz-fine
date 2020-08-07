package com.hz.fine.master.core.protocol.response.did.collectionaccount;

import java.io.Serializable;

/**
 * @Description 用户账号信息-微信群
 * @Author yoko
 * @Date 2020/7/30 23:11
 * @Version 1.0
 */
public class DidCollectionAccountGroup implements Serializable {
    private static final long   serialVersionUID = 1233023331544L;

    /**
     * 自增主键ID
     */
    public Long id;

    /**
     * 收款账户类型：1微信，2支付宝，3微信群
     */
    public Integer acType;

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
     * 失效时间：微信群二维码的失效时间
     */
    public String invalidTime;

    /**
     * 是否失效：1未失效，2已失效
     */
    public Integer isInvalid;

    /**
     * 剩余收红包次数
     */
    public Integer redPackNum;

    /**
     * 收款账号审核：1初始化，2审核失败，3审核成功
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
     * 执行类型：1需要回复指令，2需要上传二维码
     */
    public Integer isOk;

    public DidCollectionAccountGroup(){

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getAcType() {
        return acType;
    }

    public void setAcType(Integer acType) {
        this.acType = acType;
    }

    public String getMmQrCode() {
        return mmQrCode;
    }

    public void setMmQrCode(String mmQrCode) {
        this.mmQrCode = mmQrCode;
    }

    public String getDdQrCode() {
        return ddQrCode;
    }

    public void setDdQrCode(String ddQrCode) {
        this.ddQrCode = ddQrCode;
    }

    public String getPayee() {
        return payee;
    }

    public void setPayee(String payee) {
        this.payee = payee;
    }

    public String getInvalidTime() {
        return invalidTime;
    }

    public void setInvalidTime(String invalidTime) {
        this.invalidTime = invalidTime;
    }

    public Integer getIsInvalid() {
        return isInvalid;
    }

    public void setIsInvalid(Integer isInvalid) {
        this.isInvalid = isInvalid;
    }

    public Integer getRedPackNum() {
        return redPackNum;
    }

    public void setRedPackNum(Integer redPackNum) {
        this.redPackNum = redPackNum;
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

    public Integer getIsOk() {
        return isOk;
    }

    public void setIsOk(Integer isOk) {
        this.isOk = isOk;
    }
}
