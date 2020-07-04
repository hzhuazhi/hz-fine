package com.hz.fine.master.core.protocol.response.notice;



import com.hz.fine.master.core.protocol.base.BaseResponse;

import java.io.Serializable;
import java.util.List;

/**
 * @Description 协议：公告
 * @Author yoko
 * @Date 2020/1/14 21:26
 * @Version 1.0
 */
public class ResponseNotice extends BaseResponse implements Serializable {
    private static final long   serialVersionUID = 1233023331141L;
    public Notice dataModel;
    public List<Notice> dataList;
    public Integer rowCount;

    public ResponseNotice(){

    }

    public Notice getDataModel() {
        return dataModel;
    }

    public void setDataModel(Notice dataModel) {
        this.dataModel = dataModel;
    }

    public List<Notice> getDataList() {
        return dataList;
    }

    public void setDataList(List<Notice> dataList) {
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
}
