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

    public Integer waitNum;// 当前等待排的位置
    public Integer totalNum;// 总的等待数

    public Integer rowCount;
    public ResponsePool(){

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

    @Override
    public Integer getRowCount() {
        return rowCount;
    }

    @Override
    public void setRowCount(Integer rowCount) {
        this.rowCount = rowCount;
    }
}
