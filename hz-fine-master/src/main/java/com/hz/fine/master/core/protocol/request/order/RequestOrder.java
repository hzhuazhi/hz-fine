package com.hz.fine.master.core.protocol.request.order;

import com.hz.fine.master.core.protocol.base.BaseRequest;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Description 协议：任务订单（平台派发订单）
 * @Author yoko
 * @Date 2020/5/22 10:54
 * @Version 1.0
 */
public class RequestOrder extends BaseRequest implements Serializable {
    private static final long   serialVersionUID = 1233283332513L;

    /**
     * 订单金额
     */
    public String money;

    /**
     * 支付类型：1微信，2支付宝，3银行卡
     */
    public Integer payType;

    /**
     * 商家订单号
     */
    public String outTradeNo;

    /**
     * 同步地址
     */
    public String notifyUrl;

    /**
     * 支付成功之后自动跳转的地址
     */
    public String returnUrl;


    /**
     * 订单号
     */
    public String orderNo;

    /**
     * 订单状态：1初始化，2超时/失败，3有质疑，4成功
     */
    public Integer orderStatus;

    /**
     * 根据日期查询-开始时间
     */
    public Integer curdayStart;

    /**
     * 根据日期查询-结束时间
     */
    public Integer curdayEnd;

    public String ck;


    public RequestOrder(){

    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Integer getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Integer getCurdayStart() {
        return curdayStart;
    }

    public void setCurdayStart(Integer curdayStart) {
        this.curdayStart = curdayStart;
    }

    public Integer getCurdayEnd() {
        return curdayEnd;
    }

    public void setCurdayEnd(Integer curdayEnd) {
        this.curdayEnd = curdayEnd;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public Integer getPayType() {
        return payType;
    }

    public void setPayType(Integer payType) {
        this.payType = payType;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public String getReturnUrl() {
        return returnUrl;
    }

    public void setReturnUrl(String returnUrl) {
        this.returnUrl = returnUrl;
    }

    public String getCk() {
        return ck;
    }

    public void setCk(String ck) {
        this.ck = ck;
    }
}
