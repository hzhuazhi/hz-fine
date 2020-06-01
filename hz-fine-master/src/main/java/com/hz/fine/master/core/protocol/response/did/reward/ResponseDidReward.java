package com.hz.fine.master.core.protocol.response.did.reward;

import com.hz.fine.master.core.protocol.base.BaseResponse;

import java.io.Serializable;
import java.util.List;

/**
 * @Description 协议：用户奖励纪录
 * @Author yoko
 * @Date 2020/5/21 17:45
 * @Version 1.0
 */
public class ResponseDidReward extends BaseResponse implements Serializable {
    private static final long   serialVersionUID = 1233023131149L;


    public List<DidReward> dataList;
    public DidReward dataModel;
    public List<DidShare> shareList;

    public Integer rowCount;



    public ResponseDidReward(){

    }


    public List<DidReward> getDataList() {
        return dataList;
    }

    public void setDataList(List<DidReward> dataList) {
        this.dataList = dataList;
    }

    public DidReward getDataModel() {
        return dataModel;
    }

    public void setDataModel(DidReward dataModel) {
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


    public List<DidShare> getShareList() {
        return shareList;
    }

    public void setShareList(List<DidShare> shareList) {
        this.shareList = shareList;
    }
}
