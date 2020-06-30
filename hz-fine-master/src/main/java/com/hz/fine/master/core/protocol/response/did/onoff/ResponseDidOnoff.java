package com.hz.fine.master.core.protocol.response.did.onoff;

import com.hz.fine.master.core.protocol.base.BaseResponse;

import java.io.Serializable;
import java.util.List;

/**
 * @Description 协议：用户抢单上下线
 * @Author yoko
 * @Date 2020/6/30 17:55
 * @Version 1.0
 */
public class ResponseDidOnoff extends BaseResponse implements Serializable {
    private static final long   serialVersionUID = 1233023331143L;

    public DidOnoff dataModel;
    public Integer rowCount;

    public ResponseDidOnoff(){

    }

    public DidOnoff getDataModel() {
        return dataModel;
    }

    public void setDataModel(DidOnoff dataModel) {
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
