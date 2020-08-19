package com.hz.fine.master.core.protocol.response.analysis;

import java.io.Serializable;

/**
 * @Description 需要解析的实体属性Bean
 * @Author yoko
 * @Date 2020/8/19 19:03
 * @Version 1.0
 */
public class Analysis implements Serializable {
    private static final long   serialVersionUID = 3233023531141L;

    /**
     * 解析数据的ID：对应表tb_fn_cat_data_analysis的主键ID
     */
    public Long analysisId;

    /**
     * 小微的主键ID
     */
    public Long wxId;

    /**
     * 收款账号ID
     */
    public Long collectionAccountId;

    /**
     * 图片路径
     */
    public String picturePath;

    /**
     * 图片名称
     */
    public String pictureName;

    /**
     * 微信的原始ID
     */
    public String toWxid;

    public Analysis(){

    }

    public Long getAnalysisId() {
        return analysisId;
    }

    public void setAnalysisId(Long analysisId) {
        this.analysisId = analysisId;
    }

    public Long getWxId() {
        return wxId;
    }

    public void setWxId(Long wxId) {
        this.wxId = wxId;
    }

    public Long getCollectionAccountId() {
        return collectionAccountId;
    }

    public void setCollectionAccountId(Long collectionAccountId) {
        this.collectionAccountId = collectionAccountId;
    }

    public String getPicturePath() {
        return picturePath;
    }

    public void setPicturePath(String picturePath) {
        this.picturePath = picturePath;
    }

    public String getPictureName() {
        return pictureName;
    }

    public void setPictureName(String pictureName) {
        this.pictureName = pictureName;
    }

    public String getToWxid() {
        return toWxid;
    }

    public void setToWxid(String toWxid) {
        this.toWxid = toWxid;
    }
}
