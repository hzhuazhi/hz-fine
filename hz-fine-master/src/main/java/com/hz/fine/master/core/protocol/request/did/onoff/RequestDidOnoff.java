package com.hz.fine.master.core.protocol.request.did.onoff;

import com.hz.fine.master.core.protocol.base.BaseRequest;

import java.io.Serializable;

/**
 * @Description 协议：用户抢单上下线
 * @Author yoko
 * @Date 2020/6/30 17:35
 * @Version 1.0
 */
public class RequestDidOnoff extends BaseRequest implements Serializable {
    private static final long   serialVersionUID = 2233283332313L;

    /**
     * 数据类型;1下线（取消抢单），2上线（开始抢单）
     */
    public Integer dataType;

    public RequestDidOnoff(){

    }

    public Integer getDataType() {
        return dataType;
    }

    public void setDataType(Integer dataType) {
        this.dataType = dataType;
    }
}
