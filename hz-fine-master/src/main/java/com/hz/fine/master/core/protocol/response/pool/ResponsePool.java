package com.hz.fine.master.core.protocol.response.pool;

import com.hz.fine.master.core.protocol.base.BaseResponse;

import java.io.Serializable;
import java.util.List;

/**
 * @Description 协议：池子
 * @Author yoko
 * @Date 2020/8/13 16:52
 * @Version 1.0
 */
public class ResponsePool extends BaseResponse implements Serializable {
    private static final long   serialVersionUID = 2233023131155L;


    public Integer poolStatus;// 用户目前在池子中状态：1未排队，2排队中，3进行中
    public WaitInfo wait;// 在池子属于排队中的信息

    public Integer rowCount;
    public ResponsePool(){

    }



    @Override
    public Integer getRowCount() {
        return rowCount;
    }

    @Override
    public void setRowCount(Integer rowCount) {
        this.rowCount = rowCount;
    }

    public Integer getPoolStatus() {
        return poolStatus;
    }

    public void setPoolStatus(Integer poolStatus) {
        this.poolStatus = poolStatus;
    }

    public WaitInfo getWait() {
        return wait;
    }

    public void setWait(WaitInfo wait) {
        this.wait = wait;
    }
}
