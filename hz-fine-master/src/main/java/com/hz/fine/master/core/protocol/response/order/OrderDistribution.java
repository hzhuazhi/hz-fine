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
}
