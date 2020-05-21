package com.hz.fine.master.core.protocol.request.did.recharge;

import com.hz.fine.master.core.protocol.base.BaseRequest;

import java.io.Serializable;

/**
 * @Description 协议：用户充值记录
 * @Author yoko
 * @Date 2020/5/21 14:34
 * @Version 1.0
 */
public class RequestDidRecharge extends BaseRequest implements Serializable {
    private static final long   serialVersionUID = 1233283332111L;

    /**
     *充值金额
     */
    public String orderMoney;

    /**
     * 订单号
     */
    public String orderNo;

    /**
     * 充值记录银行卡转账图片凭证
     */
    public String pictureAds;


    /**
     * 订单状态：1初始化，2超时/失败，3成功
     */
    public Integer orderStatus;

    public Integer curdayStart;
    public Integer curdayEnd;


    public RequestDidRecharge(){

    }


    public String getOrderMoney() {
        return orderMoney;
    }

    public void setOrderMoney(String orderMoney) {
        this.orderMoney = orderMoney;
    }

    public String getPictureAds() {
        return pictureAds;
    }

    public void setPictureAds(String pictureAds) {
        this.pictureAds = pictureAds;
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
}
