package com.hz.fine.master.core.protocol.response.did.onoff;

import java.io.Serializable;

/**
 * @Description 用户抢单上下线的属性
 * @Author yoko
 * @Date 2020/6/30 18:29
 * @Version 1.0
 */
public class DidOnoff implements Serializable {
    private static final long   serialVersionUID = 2233023531149L;

    /**
     * 数据类型;1下线（取消抢单），2上线（开始抢单）
     */
    public Integer dataType;

    public DidOnoff(){

    }

    public Integer getDataType() {
        return dataType;
    }

    public void setDataType(Integer dataType) {
        this.dataType = dataType;
    }
}
