package com.hz.fine.master.core.protocol.request.pool;

import com.hz.fine.master.core.protocol.base.BaseRequest;

import java.io.Serializable;

/**
 * @Description 协议：池子
 * @Author yoko
 * @Date 2020/8/13 16:51
 * @Version 1.0
 */
public class RequestPool extends BaseRequest implements Serializable {
    private static final long   serialVersionUID = 1233223332241L;

    /**
     * 抢单行为：1开始抢单，2取消抢单
     */
    public Integer actionStatus;

    public RequestPool(){

    }


    public Integer getActionStatus() {
        return actionStatus;
    }

    public void setActionStatus(Integer actionStatus) {
        this.actionStatus = actionStatus;
    }
}
