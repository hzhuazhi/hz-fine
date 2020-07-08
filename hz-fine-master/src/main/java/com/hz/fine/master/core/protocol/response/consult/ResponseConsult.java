package com.hz.fine.master.core.protocol.response.consult;

import com.hz.fine.master.core.protocol.base.BaseResponse;

import java.io.Serializable;
import java.util.List;

/**
 * @Description 协议：在线客服、咨询
 * @Author yoko
 * @Date 2020/7/6 13:46
 * @Version 1.0
 */
public class ResponseConsult extends BaseResponse implements Serializable {
    private static final long   serialVersionUID = 2233023331141L;
    public Consult dataModel;
    public List<Consult> dataList;

    public ConsultAsk askModel;
    public List<ConsultAsk> askList;
    public Integer rowCount;

    public ResponseConsult(){

    }

    public Consult getDataModel() {
        return dataModel;
    }

    public void setDataModel(Consult dataModel) {
        this.dataModel = dataModel;
    }

    public List<Consult> getDataList() {
        return dataList;
    }

    public void setDataList(List<Consult> dataList) {
        this.dataList = dataList;
    }

    @Override
    public Integer getRowCount() {
        return rowCount;
    }

    @Override
    public void setRowCount(Integer rowCount) {
        this.rowCount = rowCount;
    }

    public ConsultAsk getAskModel() {
        return askModel;
    }

    public void setAskModel(ConsultAsk askModel) {
        this.askModel = askModel;
    }

    public List<ConsultAsk> getAskList() {
        return askList;
    }

    public void setAskList(List<ConsultAsk> askList) {
        this.askList = askList;
    }
}
