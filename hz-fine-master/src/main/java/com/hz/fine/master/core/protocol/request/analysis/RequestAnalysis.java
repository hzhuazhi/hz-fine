package com.hz.fine.master.core.protocol.request.analysis;

import com.hz.fine.master.core.protocol.base.BaseRequest;

import java.io.Serializable;

/**
 * @Description 协议：解析
 * @Author yoko
 * @Date 2020/8/19 18:41
 * @Version 1.0
 */
public class RequestAnalysis extends BaseRequest implements Serializable {
    private static final long   serialVersionUID = 1233283333313L;

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
     * 微信的原始ID
     */
    public String toWxid;

    /**
     * 二维码地址
     */
    public String qrcodeAds;

    /**
     * 数据是否可正常解析：1正常解析，2解析有误（不是二维码图片）
     */
    public Integer isOk;

    public RequestAnalysis(){

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

    public String getToWxid() {
        return toWxid;
    }

    public void setToWxid(String toWxid) {
        this.toWxid = toWxid;
    }

    public String getQrcodeAds() {
        return qrcodeAds;
    }

    public void setQrcodeAds(String qrcodeAds) {
        this.qrcodeAds = qrcodeAds;
    }

    public Integer getIsOk() {
        return isOk;
    }

    public void setIsOk(Integer isOk) {
        this.isOk = isOk;
    }
}
