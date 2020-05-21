package com.hz.fine.master.core.model.strategy;

/**
 * @Description 策略数据：银行卡流水日月总限制的实体属性Bean
 * @Author yoko
 * @Date 2020/5/20 15:50
 * @Version 1.0
 */
public class StrategyBankLimit {

    private Long id;

    private Long stgKey;

    private String inDayMoney;

    private String outDayMoney;

    private String inMonthMoney;

    private String outMonthMoney;

    private String inTotalMoney;

    private String outTotalMoney;

    private Integer inDayNum;

    private Integer outDayNum;

    private Integer inMonthNum;

    private Integer outMonthNum;

    private Integer inTotalNum;

    private Integer outTotalNum;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getStgKey() {
        return stgKey;
    }

    public void setStgKey(Long stgKey) {
        this.stgKey = stgKey;
    }

    public String getInDayMoney() {
        return inDayMoney;
    }

    public void setInDayMoney(String inDayMoney) {
        this.inDayMoney = inDayMoney;
    }

    public String getOutDayMoney() {
        return outDayMoney;
    }

    public void setOutDayMoney(String outDayMoney) {
        this.outDayMoney = outDayMoney;
    }

    public String getInMonthMoney() {
        return inMonthMoney;
    }

    public void setInMonthMoney(String inMonthMoney) {
        this.inMonthMoney = inMonthMoney;
    }

    public String getOutMonthMoney() {
        return outMonthMoney;
    }

    public void setOutMonthMoney(String outMonthMoney) {
        this.outMonthMoney = outMonthMoney;
    }

    public String getInTotalMoney() {
        return inTotalMoney;
    }

    public void setInTotalMoney(String inTotalMoney) {
        this.inTotalMoney = inTotalMoney;
    }

    public String getOutTotalMoney() {
        return outTotalMoney;
    }

    public void setOutTotalMoney(String outTotalMoney) {
        this.outTotalMoney = outTotalMoney;
    }

    public Integer getInDayNum() {
        return inDayNum;
    }

    public void setInDayNum(Integer inDayNum) {
        this.inDayNum = inDayNum;
    }

    public Integer getOutDayNum() {
        return outDayNum;
    }

    public void setOutDayNum(Integer outDayNum) {
        this.outDayNum = outDayNum;
    }

    public Integer getInMonthNum() {
        return inMonthNum;
    }

    public void setInMonthNum(Integer inMonthNum) {
        this.inMonthNum = inMonthNum;
    }

    public Integer getOutMonthNum() {
        return outMonthNum;
    }

    public void setOutMonthNum(Integer outMonthNum) {
        this.outMonthNum = outMonthNum;
    }

    public Integer getInTotalNum() {
        return inTotalNum;
    }

    public void setInTotalNum(Integer inTotalNum) {
        this.inTotalNum = inTotalNum;
    }

    public Integer getOutTotalNum() {
        return outTotalNum;
    }

    public void setOutTotalNum(Integer outTotalNum) {
        this.outTotalNum = outTotalNum;
    }
}
