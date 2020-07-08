package com.hz.fine.master.core.protocol.response.did.collectionaccount;

import java.io.Serializable;

/**
 * @Description 用户支付宝收款账号的属性
 * @Author yoko
 * @Date 2020/7/8 17:00
 * @Version 1.0
 */
public class DidCollectionAccountZfb implements Serializable {
    private static final long   serialVersionUID = 1233123331144L;

    /**
     * 自增主键ID
     */
    public Long id;

    /**
     * 收款账户名称：用户备注使用
     */
    public String acName;

    /**
     * 收款账户类型：1微信，2支付宝，3银行卡
     */
    public Integer acType;

    /**
     * 收款的具体账号：类型为微信则微信账号，支付宝为支付宝账号；怕后期有其它冲突
     */
    public String acNum;

    /**
     * 收款人：微信昵称、支付宝昵称、银行卡持有人名称
     */
    public String payee;

    /**
     * 支付宝账号ID
     */
    public String userId;

    public DidCollectionAccountZfb(){

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAcName() {
        return acName;
    }

    public void setAcName(String acName) {
        this.acName = acName;
    }

    public Integer getAcType() {
        return acType;
    }

    public void setAcType(Integer acType) {
        this.acType = acType;
    }

    public String getAcNum() {
        return acNum;
    }

    public void setAcNum(String acNum) {
        this.acNum = acNum;
    }

    public String getPayee() {
        return payee;
    }

    public void setPayee(String payee) {
        this.payee = payee;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
