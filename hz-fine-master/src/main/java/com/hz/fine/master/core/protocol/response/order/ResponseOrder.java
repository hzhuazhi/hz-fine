package com.hz.fine.master.core.protocol.response.order;

import com.hz.fine.master.core.protocol.base.BaseResponse;

import java.io.Serializable;
import java.util.List;

/**
 * @Description 协议：任务订单（平台派发订单）
 * @Author yoko
 * @Date 2020/5/22 11:04
 * @Version 1.0
 */
public class ResponseOrder extends BaseResponse implements Serializable {
    private static final long   serialVersionUID = 1233023131150L;


    public List<Order> dataList;
    public Order dataModel;
    public Integer rowCount;



    public ResponseOrder(){

    }

    public List<Order> getDataList() {
        return dataList;
    }

    public void setDataList(List<Order> dataList) {
        this.dataList = dataList;
    }

    public Order getDataModel() {
        return dataModel;
    }

    public void setDataModel(Order dataModel) {
        this.dataModel = dataModel;
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
