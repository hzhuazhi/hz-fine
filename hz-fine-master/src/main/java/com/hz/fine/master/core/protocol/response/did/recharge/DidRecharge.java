package com.hz.fine.master.core.protocol.response.did.recharge;

import java.io.Serializable;

/**
 * @Description 用户的充值记录
 * @Author yoko
 * @Date 2020/5/21 14:18
 * @Version 1.0
 */
public class DidRecharge implements Serializable {
    private static final long   serialVersionUID = 1233023331041L;

    /**
     * 订单号
     */
    public String orderNo;

    /**
     * 订单金额
     */
    public String orderMoney;

    /**
     * 派分给用户的订单金额：假如用户要充值5000，我们可以给他派分成4999.91
     */
    public String distributionMoney;

    /**
     * 充值记录银行卡转账图片凭证
     */
    public String pictureAds;

    /**
     * 失效时间
     */
    public String invalidTime;

    /**
     * 订单状态：-1申诉状态，1初始化，2超时/失败，3成功
     */
    public Integer orderStatus;

    /**
     * 存款人
     */
    public String depositor;

    /**
     * 存款金额
     */
    public String depositMoney;

    /**
     * 存款时间
     */
    public String depositTime;

    /**
     * 存款账号尾号
     */
    public String lastNum;

    /**
     * 申诉状态：1初始化，2申诉中，3申诉失败，4申诉成功
     */
    public Integer appealStatus;

    /**
     * 申诉失败原因：描述申诉结果的原因
     */
    public String appealOrigin;

    /**
     * 创建时间
     */
    public String createTime;

    /**
     * 创建日期-Int
     */
    public Integer curday;

    /**
     * 创建小时
     */
    public Integer curhour;

    /**
     * 创建分钟
     */
    public Integer curminute;

    public DidRecharge(){

    }


    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getOrderMoney() {
        return orderMoney;
    }

    public void setOrderMoney(String orderMoney) {
        this.orderMoney = orderMoney;
    }

    public String getDistributionMoney() {
        return distributionMoney;
    }

    public void setDistributionMoney(String distributionMoney) {
        this.distributionMoney = distributionMoney;
    }

    public String getPictureAds() {
        return pictureAds;
    }

    public void setPictureAds(String pictureAds) {
        this.pictureAds = pictureAds;
    }

    public String getInvalidTime() {
        return invalidTime;
    }

    public void setInvalidTime(String invalidTime) {
        this.invalidTime = invalidTime;
    }

    public Integer getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public Integer getCurday() {
        return curday;
    }

    public void setCurday(Integer curday) {
        this.curday = curday;
    }

    public Integer getCurhour() {
        return curhour;
    }

    public void setCurhour(Integer curhour) {
        this.curhour = curhour;
    }

    public Integer getCurminute() {
        return curminute;
    }

    public void setCurminute(Integer curminute) {
        this.curminute = curminute;
    }

    public Integer getAppealStatus() {
        return appealStatus;
    }

    public void setAppealStatus(Integer appealStatus) {
        this.appealStatus = appealStatus;
    }

    public String getAppealOrigin() {
        return appealOrigin;
    }

    public void setAppealOrigin(String appealOrigin) {
        this.appealOrigin = appealOrigin;
    }

    public String getDepositor() {
        return depositor;
    }

    public void setDepositor(String depositor) {
        this.depositor = depositor;
    }

    public String getDepositMoney() {
        return depositMoney;
    }

    public void setDepositMoney(String depositMoney) {
        this.depositMoney = depositMoney;
    }

    public String getDepositTime() {
        return depositTime;
    }

    public void setDepositTime(String depositTime) {
        this.depositTime = depositTime;
    }

    public String getLastNum() {
        return lastNum;
    }

    public void setLastNum(String lastNum) {
        this.lastNum = lastNum;
    }
}
