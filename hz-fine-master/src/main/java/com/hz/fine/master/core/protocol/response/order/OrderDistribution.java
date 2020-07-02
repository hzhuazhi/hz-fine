package com.hz.fine.master.core.protocol.response.order;

import java.io.Serializable;

/**
 * @Description 协议：派发的订单
 * @Author yoko
 * @Date 2020/6/2 15:09
 * @Version 1.0
 */
public class OrderDistribution implements Serializable {
    private static final long   serialVersionUID = 1233023331070L;

    /**
     * 订单号
     */
    public String orderNo;

    /**
     * 收款二维码地址
     */
    public String qrCode;

    /**
     * 订单金额
     */
    public String orderMoney;

    /**
     * 失效时间
     */
    public String invalidTime;

    /**
     * 失效时间-秒
     */
    public String invalidSecond;

    /**
     * 生成二维码扫码的HTML地址
     */
    public String qrCodeUrl;

    /**
     * 支付宝账号ID
     */
    public String userId;

    /**
     * 支付宝账号
     */
    public String zfbAcNum;

    /**
     * 数据类型
     */
    public Integer dataType;

    /**
     *
     */
    public String key;

    public OrderDistribution(){

    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public String getOrderMoney() {
        return orderMoney;
    }

    public void setOrderMoney(String orderMoney) {
        this.orderMoney = orderMoney;
    }

    public String getInvalidTime() {
        return invalidTime;
    }

    public void setInvalidTime(String invalidTime) {
        this.invalidTime = invalidTime;
    }

    public String getQrCodeUrl() {
        return qrCodeUrl;
    }

    public void setQrCodeUrl(String qrCodeUrl) {
        this.qrCodeUrl = qrCodeUrl;
    }

    public String getInvalidSecond() {
        return invalidSecond;
    }

    public void setInvalidSecond(String invalidSecond) {
        this.invalidSecond = invalidSecond;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getZfbAcNum() {
        return zfbAcNum;
    }

    public void setZfbAcNum(String zfbAcNum) {
        this.zfbAcNum = zfbAcNum;
    }

    public Integer getDataType() {
        return dataType;
    }

    public void setDataType(Integer dataType) {
        this.dataType = dataType;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
