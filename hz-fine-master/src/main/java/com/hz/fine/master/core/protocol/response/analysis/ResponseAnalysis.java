package com.hz.fine.master.core.protocol.response.analysis;

import com.hz.fine.master.core.protocol.base.BaseResponse;

import java.io.Serializable;

/**
 * @Description 协议：解析
 * @Author yoko
 * @Date 2020/8/19 18:43
 * @Version 1.0
 */
public class ResponseAnalysis extends BaseResponse implements Serializable {
    private static final long   serialVersionUID = 2233023131152L;
    public Analysis dataModel;
    public Integer rowCount;
    public ResponseAnalysis(){

    }

    public Analysis getDataModel() {
        return dataModel;
    }

    public void setDataModel(Analysis dataModel) {
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
