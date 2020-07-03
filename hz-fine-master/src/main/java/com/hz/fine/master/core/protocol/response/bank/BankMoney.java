package com.hz.fine.master.core.protocol.response.bank;

import java.io.Serializable;
import java.util.List;

/**
 * @Description 展现的银行卡旗下的金额
 * @Author yoko
 * @Date 2020/7/3 19:04
 * @Version 1.0
 */
public class BankMoney implements Serializable {
    private static final long   serialVersionUID = 2233023531141L;

    /**
     * 银行卡主键ID
     */
    public Long bankId;

    /**
     * 展现给用户的金额集合
     */
    public List<String> moneyList;

    public BankMoney(){

    }

    public Long getBankId() {
        return bankId;
    }

    public void setBankId(Long bankId) {
        this.bankId = bankId;
    }

    public List<String> getMoneyList() {
        return moneyList;
    }

    public void setMoneyList(List<String> moneyList) {
        this.moneyList = moneyList;
    }
}
