package com.hz.fine.master.core.protocol.response.did.recharge;

import com.hz.fine.master.core.protocol.base.BaseResponse;

import java.io.Serializable;
import java.util.List;

/**
 * @Description 协议：用户充值记录
 * @Author yoko
 * @Date 2020/5/19 14:45
 * @Version 1.0
 */
public class ResponseDidRecharge extends BaseResponse implements Serializable {
    private static final long   serialVersionUID = 1233023331149L;

    /**
     * 用户充值-用户发起充值返回给客户端的数据
     */
    public RechargeInfo recharge;

    public List<DidRecharge> dataList;
    public DidRecharge dataModel;
    public Integer rowCount;

    /**
     * 用户名下是否充值挂单：1没有挂单，2有挂单
     */
    public Integer haveType;



    public ResponseDidRecharge(){

    }

    public RechargeInfo getRecharge() {
        return recharge;
    }

    public void setRecharge(RechargeInfo recharge) {
        this.recharge = recharge;
    }


    public List<DidRecharge> getDataList() {
        return dataList;
    }

    public void setDataList(List<DidRecharge> dataList) {
        this.dataList = dataList;
    }

    public DidRecharge getDataModel() {
        return dataModel;
    }

    public void setDataModel(DidRecharge dataModel) {
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

    public Integer getHaveType() {
        return haveType;
    }

    public void setHaveType(Integer haveType) {
        this.haveType = haveType;
    }
}
