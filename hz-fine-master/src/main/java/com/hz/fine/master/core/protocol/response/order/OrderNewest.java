package com.hz.fine.master.core.protocol.response.order;

import java.io.Serializable;

/**
 * @Description 协议：最新的订单待操作属性
 * @Author yoko
 * @Date 2020/7/21 16:56
 * @Version 1.0
 */
public class OrderNewest implements Serializable {
    private static final long   serialVersionUID = 1233023331149L;

    /**
     * 是否有挂单：1没有挂单，2有挂单
     */
    public Integer isHave;

    /**
     * 目前到达的目的/步骤
     */
    public String purpose;

    /**
     * 缘由，起源，起因：导致目前在这个步骤停留的原因
     */
    public String origin;
    public OrderNewest(){

    }

    public Integer getIsHave() {
        return isHave;
    }

    public void setIsHave(Integer isHave) {
        this.isHave = isHave;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }
}
