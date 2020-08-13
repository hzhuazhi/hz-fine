package com.hz.fine.master.core.protocol.response.pool;

import java.io.Serializable;

/**
 * @Description 在池子属于排队中的实体属性Bean
 * @Author yoko
 * @Date 2020/8/13 17:40
 * @Version 1.0
 */
public class WaitInfo implements Serializable {
    public static final long   serialVersionUID = 2233023331149L;

    public Integer waitNum;// 当前等待排的位置
    public Integer totalNum;// 总的等待数
    public Integer waitTime;// 预计等待时长

    public WaitInfo(){

    }

    public Integer getWaitNum() {
        return waitNum;
    }

    public void setWaitNum(Integer waitNum) {
        this.waitNum = waitNum;
    }

    public Integer getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(Integer totalNum) {
        this.totalNum = totalNum;
    }

    public Integer getWaitTime() {
        return waitTime;
    }

    public void setWaitTime(Integer waitTime) {
        this.waitTime = waitTime;
    }
}
