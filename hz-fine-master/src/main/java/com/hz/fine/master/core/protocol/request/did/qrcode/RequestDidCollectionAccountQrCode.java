package com.hz.fine.master.core.protocol.request.did.qrcode;

import com.hz.fine.master.core.protocol.base.BaseRequest;

import java.io.Serializable;
import java.util.List;

/**
 * @Description 协议：用户的收款账号二维码
 * @Author yoko
 * @Date 2020/6/17 15:31
 * @Version 1.0
 */
public class RequestDidCollectionAccountQrCode extends BaseRequest implements Serializable {
    private static final long   serialVersionUID = 1233283332171L;

    /**
     * 用户收款账号ID：对应表tb_fn_did_collection_account的主键ID
     */
    public Long collectionAccountId;

    /**
     * 用户收款账号的二维码主键ID
     */
    public Long id;

    /**
     * 别名
     */
    public String alias;

    /**
     * 收款账号/地址（原）：二维码
     */
    public String mmQrCode;

    /**
     * 收款账号/地址（转码）：二维码
     */
    public String ddQrCode;

    /**
     * 二维码类型：1普通二维码，2个人固码，3固定金额码
     */
    public Integer dataType;

    /**
     * 码的金额：固码的具体金额
     */
    public String qrCodeMoney;

    /**
     * 可使用次数：可以给出多少次
     */
    public Integer limitNum;

    public List<DidCollectionAccountQrCode> dataList;

    /**
     * 使用状态:1初始化有效正常使用，2无效暂停使用
     */
    public Integer useStatus;

    /**
     * 是否有效：0有效，1无效/删除
     */
    public Integer yn;


    public RequestDidCollectionAccountQrCode(){

    }

    public Long getCollectionAccountId() {
        return collectionAccountId;
    }

    public void setCollectionAccountId(Long collectionAccountId) {
        this.collectionAccountId = collectionAccountId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getMmQrCode() {
        return mmQrCode;
    }

    public void setMmQrCode(String mmQrCode) {
        this.mmQrCode = mmQrCode;
    }

    public String getDdQrCode() {
        return ddQrCode;
    }

    public void setDdQrCode(String ddQrCode) {
        this.ddQrCode = ddQrCode;
    }

    public Integer getDataType() {
        return dataType;
    }

    public void setDataType(Integer dataType) {
        this.dataType = dataType;
    }

    public String getQrCodeMoney() {
        return qrCodeMoney;
    }

    public void setQrCodeMoney(String qrCodeMoney) {
        this.qrCodeMoney = qrCodeMoney;
    }

    public Integer getLimitNum() {
        return limitNum;
    }

    public void setLimitNum(Integer limitNum) {
        this.limitNum = limitNum;
    }

    public List<DidCollectionAccountQrCode> getDataList() {
        return dataList;
    }

    public void setDataList(List<DidCollectionAccountQrCode> dataList) {
        this.dataList = dataList;
    }

    public Integer getUseStatus() {
        return useStatus;
    }

    public void setUseStatus(Integer useStatus) {
        this.useStatus = useStatus;
    }

    public Integer getYn() {
        return yn;
    }

    public void setYn(Integer yn) {
        this.yn = yn;
    }
}
