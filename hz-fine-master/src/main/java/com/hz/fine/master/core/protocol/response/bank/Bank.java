package com.hz.fine.master.core.protocol.response.bank;

import java.io.Serializable;

/**
 * @Description 银行卡属性
 * @Author yoko
 * @Date 2020/6/29 14:15
 * @Version 1.0
 */
public class Bank implements Serializable {
    private static final long   serialVersionUID = 2233023531141L;


    /**
     * 银行名称/归属开户行
     */
    public String bankName;

    /**
     * 银行卡账号/银行卡号
     */
    public String bankCard;

    /**
     * 银行支行/支行名称
     */
    public String subbranchName;

    /**
     * 开户名
     */
    public String accountName;

    public Bank(){

    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankCard() {
        return bankCard;
    }

    public void setBankCard(String bankCard) {
        this.bankCard = bankCard;
    }

    public String getSubbranchName() {
        return subbranchName;
    }

    public void setSubbranchName(String subbranchName) {
        this.subbranchName = subbranchName;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }
}
