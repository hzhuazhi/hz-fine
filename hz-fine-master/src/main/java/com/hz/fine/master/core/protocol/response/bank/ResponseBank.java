package com.hz.fine.master.core.protocol.response.bank;

import com.hz.fine.master.core.protocol.base.BaseResponse;

import java.io.Serializable;
import java.util.List;

/**
 * @Description 协议：银行卡
 * @Author yoko
 * @Date 2020/6/29 13:59
 * @Version 1.0
 */
public class ResponseBank extends BaseResponse implements Serializable {
    private static final long   serialVersionUID = 2233023131150L;


    public List<BuyBank> dataList;
    public Bank dataModel;
    public BankMoney bankMoney;// 展现给用户的银行金额
    public Integer rowCount;

    public ResponseBank(){

    }


    public List<BuyBank> getDataList() {
        return dataList;
    }

    public void setDataList(List<BuyBank> dataList) {
        this.dataList = dataList;
    }

    @Override
    public Integer getRowCount() {
        return rowCount;
    }

    @Override
    public void setRowCount(Integer rowCount) {
        this.rowCount = rowCount;
    }

    public Bank getDataModel() {
        return dataModel;
    }

    public void setDataModel(Bank dataModel) {
        this.dataModel = dataModel;
    }


    public BankMoney getBankMoney() {
        return bankMoney;
    }

    public void setBankMoney(BankMoney bankMoney) {
        this.bankMoney = bankMoney;
    }
}
