package com.hz.fine.master.core.protocol.response.bank;

import java.io.Serializable;

/**
 * @Description 协议：买时银行卡的属性字段
 * @Author yoko
 * @Date 2020/6/29 14:01
 * @Version 1.0
 */
public class BuyBank implements Serializable {
    private static final long   serialVersionUID = 2233023531149L;

    /**
     * 银行卡主键ID
     */
    public Long id;

    /**
     * 商家昵称
     */
    public String nickname;

    /**
     * 还可购买的数量
     */
    public String buyNum;

    /**
     * 已卖出数量
     */
    public String sellNum;

    /**
     * 还剩多少的比例
     */
    public String ratio;

    /**
     * 限额：最小额度
     */
    public String minQuota;

    /**
     * 限额：最大额度
     */
    public String maxQuota;

    /**
     * 单价
     */
    public String unitPrice;


    public BuyBank(){

    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getBuyNum() {
        return buyNum;
    }

    public void setBuyNum(String buyNum) {
        this.buyNum = buyNum;
    }

    public String getSellNum() {
        return sellNum;
    }

    public void setSellNum(String sellNum) {
        this.sellNum = sellNum;
    }

    public String getRatio() {
        return ratio;
    }

    public void setRatio(String ratio) {
        this.ratio = ratio;
    }

    public String getMinQuota() {
        return minQuota;
    }

    public void setMinQuota(String minQuota) {
        this.minQuota = minQuota;
    }

    public String getMaxQuota() {
        return maxQuota;
    }

    public void setMaxQuota(String maxQuota) {
        this.maxQuota = maxQuota;
    }

    public String getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(String unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
