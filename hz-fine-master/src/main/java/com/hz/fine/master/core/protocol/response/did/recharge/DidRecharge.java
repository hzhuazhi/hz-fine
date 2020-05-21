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
     * 订单状态：1初始化，2超时/失败，3成功
     */
    public Integer orderStatus;

    /**
     * 创建时间
     */
    public String createTime;

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
}
