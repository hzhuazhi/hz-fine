package com.hz.fine.master.core.protocol.response.sell;

import com.hz.fine.master.core.protocol.base.BaseResponse;

import java.io.Serializable;
import java.util.List;

/**
 * @Description 协议：我要卖
 * @Author yoko
 * @Date 2020/6/30 11:54
 * @Version 1.0
 */
public class ResponseSell extends BaseResponse implements Serializable {
    private static final long   serialVersionUID = 2233023131150L;

    public List<Sell> dataList;
    public Integer totalNum;// 总订单数
    public Integer waitNum;// 等待数

    public Integer rowCount;
    public ResponseSell(){

    }

    public List<Sell> getDataList() {
        return dataList;
    }

    public void setDataList(List<Sell> dataList) {
        this.dataList = dataList;
    }

    public Integer getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(Integer totalNum) {
        this.totalNum = totalNum;
    }

    public Integer getWaitNum() {
        return waitNum;
    }

    public void setWaitNum(Integer waitNum) {
        this.waitNum = waitNum;
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
