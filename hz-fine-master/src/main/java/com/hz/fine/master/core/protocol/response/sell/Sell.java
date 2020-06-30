package com.hz.fine.master.core.protocol.response.sell;

import java.io.Serializable;

/**
 * @Description 我要卖的具体属性
 * @Author yoko
 * @Date 2020/6/30 12:19
 * @Version 1.0
 */
public class Sell implements Serializable {
    private static final long   serialVersionUID = 2233023331049L;

    /**
     * 商户名称
     */
    public String acName;

    /**
     * 卖出数量
     */
    public String sellNum;

    /**
     * 卖出金额
     */
    public String orderMoney;

    /**
     * 收益：奖励的金额
     */
    public String profit;

    public Sell(){

    }

    public String getAcName() {
        return acName;
    }

    public void setAcName(String acName) {
        this.acName = acName;
    }

    public String getSellNum() {
        return sellNum;
    }

    public void setSellNum(String sellNum) {
        this.sellNum = sellNum;
    }

    public String getOrderMoney() {
        return orderMoney;
    }

    public void setOrderMoney(String orderMoney) {
        this.orderMoney = orderMoney;
    }

    public String getProfit() {
        return profit;
    }

    public void setProfit(String profit) {
        this.profit = profit;
    }
}
