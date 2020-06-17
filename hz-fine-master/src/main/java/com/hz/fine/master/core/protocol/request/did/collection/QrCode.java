package com.hz.fine.master.core.protocol.request.did.collection;

import java.io.Serializable;

/**
 * @Description 用户的收款账号二维码的属性字段
 * @Author yoko
 * @Date 2020/6/17 16:34
 * @Version 1.0
 */
public class QrCode implements Serializable {
    private static final long   serialVersionUID = 1233283332273L;

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

    public QrCode(){

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
}
