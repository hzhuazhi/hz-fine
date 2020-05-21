package com.hz.fine.master.core.protocol.response.did.recharge;

import java.io.Serializable;

/**
 * @Description 用户调起充值之后，返回给用户的信息
 * <p>返回要充值的银行卡信息，订单金额，实际充值金额</p>
 * @Author yoko
 * @Date 2020/5/21 14:22
 * @Version 1.0
 */
public class RechargeInfo implements Serializable {
    private static final long   serialVersionUID = 1233023331042L;

    /**
     * 银行名称/归属开户行
     */
    public String bankName;

    /**
     * 银行卡账号/银行卡号
     */
    public String bankCard;

    /**
     * 银行支行/支行名称
     */
    public String subbranchName;

    /**
     * 开户名
     */
    public String accountName;

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
     * 订单失效时间
     */
    public String invalidTime;
    public RechargeInfo(){

    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankCard() {
        return bankCard;
    }

    public void setBankCard(String bankCard) {
        this.bankCard = bankCard;
    }

    public String getSubbranchName() {
        return subbranchName;
    }

    public void setSubbranchName(String subbranchName) {
        this.subbranchName = subbranchName;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
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

    public String getInvalidTime() {
        return invalidTime;
    }

    public void setInvalidTime(String invalidTime) {
        this.invalidTime = invalidTime;
    }
}
