package com.hz.fine.master.core.model.bank;

import com.hz.fine.master.core.protocol.page.BasePage;

import java.io.Serializable;

/**
 * @Description 银行转账信息的实体属性Bean
 * @Author yoko
 * @Date 2020/5/18 19:41
 * @Version 1.0
 */
public class BankTransferModel extends BasePage implements Serializable {
    private static final long   serialVersionUID = 1203223201103L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 归属银行卡ID：对应表tb_fn_bank的主键ID
     */
    private Long bankId;

    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 转账说明
     */
    private String tranName;

    /**
     * 操作人/录入人
     */
    private String operationName;

    /**
     * 银行名称/归属开户行
     */
    private String bankName;

    /**
     * 银行卡账号/银行卡号
     */
    private String bankCard;

    /**
     * 转账人/开户名:转给了谁
     */
    private String accountName;

    /**
     * 转账金额
     */
    private String money;

    /**
     * 转账时间
     */
    private String transferTime;

    /**
     * 创建日期：存的日期格式20160530
     */
    private Integer curday;

    /**
     * 创建所属小时：24小时制
     */
    private Integer curhour;

    /**
     * 创建所属分钟：60分钟制
     */
    private Integer curminute;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 更新时间
     */
    private String updateTime;

    /**
     * 是否有效：0有效，1无效/删除
     */
    private Integer yn;

    /**
     * 开始时间
     */
    private String startTransferTime;

    /**
     * 结束时间
     */
    private String endTransferTime;

    private Integer curdayStart;
    private Integer curdayEnd;



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBankId() {
        return bankId;
    }

    public void setBankId(Long bankId) {
        this.bankId = bankId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getTranName() {
        return tranName;
    }

    public void setTranName(String tranName) {
        this.tranName = tranName;
    }

    public String getOperationName() {
        return operationName;
    }

    public void setOperationName(String operationName) {
        this.operationName = operationName;
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

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getTransferTime() {
        return transferTime;
    }

    public void setTransferTime(String transferTime) {
        this.transferTime = transferTime;
    }

    public Integer getCurday() {
        return curday;
    }

    public void setCurday(Integer curday) {
        this.curday = curday;
    }

    public Integer getCurhour() {
        return curhour;
    }

    public void setCurhour(Integer curhour) {
        this.curhour = curhour;
    }

    public Integer getCurminute() {
        return curminute;
    }

    public void setCurminute(Integer curminute) {
        this.curminute = curminute;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getYn() {
        return yn;
    }

    public void setYn(Integer yn) {
        this.yn = yn;
    }

    public String getStartTransferTime() {
        return startTransferTime;
    }

    public void setStartTransferTime(String startTransferTime) {
        this.startTransferTime = startTransferTime;
    }

    public String getEndTransferTime() {
        return endTransferTime;
    }

    public void setEndTransferTime(String endTransferTime) {
        this.endTransferTime = endTransferTime;
    }

    public Integer getCurdayStart() {
        return curdayStart;
    }

    public void setCurdayStart(Integer curdayStart) {
        this.curdayStart = curdayStart;
    }

    public Integer getCurdayEnd() {
        return curdayEnd;
    }

    public void setCurdayEnd(Integer curdayEnd) {
        this.curdayEnd = curdayEnd;
    }
}
