package com.hz.fine.master.core.model.alipay;

/**
 * @Description 阿里支付宝订单结果同步的实体Bean
 * @Author yoko
 * @Date 2019/12/27 15:08
 * @Version 1.0
 */
public class AlipayNotifyModel {
    /**
     * 主键ID
     */
    public Long id;

    /**
     * 会员id
     */
    public String appId;

    /**
     * 认证的应用ID
     */
    public String authAppId;

    /**
     * 订单类容体
     */
    public String body;

    /**
     * 买家ID
     */
    public String buyerId;

    /**
     * 买家登录的ID
     */
    public String buyerLogonId;

    /**
     * 买家付款金额
     */
    public String buyerPayAmount;

    /**
     * 字符集编码
     */
    public String dataCharset;

    /**
     * 资金清单
     */
    public String fundBillList;

    /**
     * 订单的创建时间
     */
    public String gmtCreate;

    /**
     * 订单的支付时间
     */
    public String gmtPayment;

    /**
     * 发票金额
     */
    public String invoiceAmount;

    /**
     * 同步的ID（防重码）
     */
    public String notifyId;

    /**
     * 同步的时间
     */
    public String notifyTime;

    /**
     * 同步的类型
     */
    public String notifyType;

    /**
     * 交易订单号
     */
    public String outTradeNo;

    /**
     * 点数
     */
    public String pointAmount;

    /**
     * 收款金额
     */
    public String receiptAmount;

    /**
     * 卖家邮箱
     */
    public String sellerEmail;

    /**
     * 卖家ID
     */
    public String sellerId;

    /**
     * 主题
     */
    public String subject;

    /**
     * 总金额
     */
    public String totalAmount;

    /**
     * 支付宝外部订单编号
     */
    public String tradeNo;

    /**
     * 交易的状态
     */
    public String tradeStatus;

    /**
     * 版本号
     */
    public String dataVersion;


    /**
     * 创建日期：存的日期格式20160530
     */
    public Integer curday;

    /**
     * 创建所属小时：24小时制
     */
    public Integer curhour;

    /**
     * 创建所属分钟：60分钟制
     */
    public Integer curminute;

    /**
     * 创建时间
     */
    public String createTime;

    /**
     * 更新时间
     */
    public String updateTime;

    /**
     * 是否有效：0有效，1无效/删除
     */
    public Integer yn;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAuthAppId() {
        return authAppId;
    }

    public void setAuthAppId(String authAppId) {
        this.authAppId = authAppId;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(String buyerId) {
        this.buyerId = buyerId;
    }

    public String getBuyerLogonId() {
        return buyerLogonId;
    }

    public void setBuyerLogonId(String buyerLogonId) {
        this.buyerLogonId = buyerLogonId;
    }

    public String getBuyerPayAmount() {
        return buyerPayAmount;
    }

    public void setBuyerPayAmount(String buyerPayAmount) {
        this.buyerPayAmount = buyerPayAmount;
    }

    public String getDataCharset() {
        return dataCharset;
    }

    public void setDataCharset(String dataCharset) {
        this.dataCharset = dataCharset;
    }

    public String getFundBillList() {
        return fundBillList;
    }

    public void setFundBillList(String fundBillList) {
        this.fundBillList = fundBillList;
    }

    public String getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(String gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public String getGmtPayment() {
        return gmtPayment;
    }

    public void setGmtPayment(String gmtPayment) {
        this.gmtPayment = gmtPayment;
    }

    public String getInvoiceAmount() {
        return invoiceAmount;
    }

    public void setInvoiceAmount(String invoiceAmount) {
        this.invoiceAmount = invoiceAmount;
    }

    public String getNotifyId() {
        return notifyId;
    }

    public void setNotifyId(String notifyId) {
        this.notifyId = notifyId;
    }

    public String getNotifyTime() {
        return notifyTime;
    }

    public void setNotifyTime(String notifyTime) {
        this.notifyTime = notifyTime;
    }

    public String getNotifyType() {
        return notifyType;
    }

    public void setNotifyType(String notifyType) {
        this.notifyType = notifyType;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public String getPointAmount() {
        return pointAmount;
    }

    public void setPointAmount(String pointAmount) {
        this.pointAmount = pointAmount;
    }

    public String getReceiptAmount() {
        return receiptAmount;
    }

    public void setReceiptAmount(String receiptAmount) {
        this.receiptAmount = receiptAmount;
    }

    public String getSellerEmail() {
        return sellerEmail;
    }

    public void setSellerEmail(String sellerEmail) {
        this.sellerEmail = sellerEmail;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public String getTradeStatus() {
        return tradeStatus;
    }

    public void setTradeStatus(String tradeStatus) {
        this.tradeStatus = tradeStatus;
    }

    public String getDataVersion() {
        return dataVersion;
    }

    public void setDataVersion(String dataVersion) {
        this.dataVersion = dataVersion;
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

}
