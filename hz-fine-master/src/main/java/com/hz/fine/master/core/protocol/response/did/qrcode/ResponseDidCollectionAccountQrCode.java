package com.hz.fine.master.core.protocol.response.did.qrcode;

import com.hz.fine.master.core.protocol.base.BaseResponse;

import java.io.Serializable;
import java.util.List;

/**
 * @Description 协议：用户的收款账号二维码
 * @Author yoko
 * @Date 2020/6/17 20:40
 * @Version 1.0
 */
public class ResponseDidCollectionAccountQrCode extends BaseResponse implements Serializable {
    private static final long   serialVersionUID = 1233023341143L;

    public QrCode dataModel;
    public List<QrCode> dataList;
    public Integer rowCount;

    public ResponseDidCollectionAccountQrCode(){

    }

    public QrCode getDataModel() {
        return dataModel;
    }

    public void setDataModel(QrCode dataModel) {
        this.dataModel = dataModel;
    }

    public List<QrCode> getDataList() {
        return dataList;
    }

    public void setDataList(List<QrCode> dataList) {
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
