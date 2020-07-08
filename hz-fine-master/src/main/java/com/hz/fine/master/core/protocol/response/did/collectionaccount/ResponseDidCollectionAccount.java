package com.hz.fine.master.core.protocol.response.did.collectionaccount;

import com.hz.fine.master.core.protocol.base.BaseResponse;

import java.io.Serializable;
import java.util.List;

/**
 * @Description 协议：用户收款账号信息
 * @Author yoko
 * @Date 2020/5/15 17:23
 * @Version 1.0
 */
public class ResponseDidCollectionAccount extends BaseResponse implements Serializable {
    private static final long   serialVersionUID = 1233023331143L;

    public List<DidCollectionAccount> dataList;
    public DidCollectionAccount dataModel;
    public DidCollectionAccountZfb zfbModel;
    public Integer rowCount;

    public ResponseDidCollectionAccount(){

    }

    public List<DidCollectionAccount> getDataList() {
        return dataList;
    }

    public void setDataList(List<DidCollectionAccount> dataList) {
        this.dataList = dataList;
    }

    public DidCollectionAccount getDataModel() {
        return dataModel;
    }

    public void setDataModel(DidCollectionAccount dataModel) {
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

    public DidCollectionAccountZfb getZfbModel() {
        return zfbModel;
    }

    public void setZfbModel(DidCollectionAccountZfb zfbModel) {
        this.zfbModel = zfbModel;
    }
}
