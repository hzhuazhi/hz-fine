package com.hz.fine.master.core.protocol.response.did.collectionaccount;

import com.hz.fine.master.core.protocol.base.BaseResponse;
import com.hz.fine.master.core.protocol.response.wx.Wx;

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
    public DidCollectionAccountGroup groupModel;
    public List<DidCollectionAccountGroup> groupList;

    public Wx wxModel;

    /**
     * 执行类型：1需要回复指令，2需要上传二维码
     */
    public Integer isOk;

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

    public DidCollectionAccountGroup getGroupModel() {
        return groupModel;
    }

    public void setGroupModel(DidCollectionAccountGroup groupModel) {
        this.groupModel = groupModel;
    }

    public List<DidCollectionAccountGroup> getGroupList() {
        return groupList;
    }

    public void setGroupList(List<DidCollectionAccountGroup> groupList) {
        this.groupList = groupList;
    }

    public Wx getWxModel() {
        return wxModel;
    }

    public void setWxModel(Wx wxModel) {
        this.wxModel = wxModel;
    }

    public Integer getIsOk() {
        return isOk;
    }

    public void setIsOk(Integer isOk) {
        this.isOk = isOk;
    }
}
