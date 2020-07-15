package com.hz.fine.master.core.protocol.request.statistics;

import com.hz.fine.master.core.protocol.base.BaseRequest;

import java.io.Serializable;

/**
 * @Description 协议：支付用户点击支付页统计
 * @Author yoko
 * @Date 2020/7/15 18:54
 * @Version 1.0
 */
public class RequestStatisticsClickPay extends BaseRequest implements Serializable {
    private static final long   serialVersionUID = 1233283332413L;

    /**
     * 标识值：可以是订单号也可以是其它
     */
    public String identif;

    /**
     * 数据来源类型：1支付宝跳转转账
     */
    public Integer dataType;

    /**
     * 补充数据字段
     */
    public String dataValue;


    public RequestStatisticsClickPay(){

    }

    public String getIdentif() {
        return identif;
    }

    public void setIdentif(String identif) {
        this.identif = identif;
    }

    public Integer getDataType() {
        return dataType;
    }

    public void setDataType(Integer dataType) {
        this.dataType = dataType;
    }

    public String getDataValue() {
        return dataValue;
    }

    public void setDataValue(String dataValue) {
        this.dataValue = dataValue;
    }
}
