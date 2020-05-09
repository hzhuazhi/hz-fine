package com.hz.fine.master.core.model.alipay;

/**
 * @Description 阿里支付的实体bean
 * @Author yoko
 * @Date 2019/12/19 17:53
 * @Version 1.0
 */
public class AlipayModel {
    /**
     * 主键ID
     */
    public Long id;

    /**
     * 订单内容；
     * 我是测试数据
     */
    public String body;

    /**
     *主题
     */
    public String subject;

    /**
     * 交易订单号
     */
    public String outTradeNo;

    /**
     * 超时时间
     * 30m（30分钟）
     */
    public String timeoutExpress;

    /**
     * 总金额
     */
    public String totalAmount;

    /**
     * 商品编码
     */
    public String productCode;

    /**
     * 订单状态：1正常，2超时，3完成交易
     */
    public Integer outTradeStatus;

    /**
     * 数据类型：1APP支付，2H5支付
     */
    public Integer dataType;

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
     * 阿里返回的订单串
     */
    public String aliOrder;

    /**
     * 用户ID
     */
    public Long memberId;

    /**
     * 运行次数
     */
    public Integer runNum;

    /**
     * 运行计算状态：：0初始化，1锁定，2计算失败，3计算成功
     */
    public Integer runStatus;

    public Integer curdayStart;

    public Integer curdayEnd;



    public AlipayModel(){

    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public String getTimeoutExpress() {
        return timeoutExpress;
    }

    public void setTimeoutExpress(String timeoutExpress) {
        this.timeoutExpress = timeoutExpress;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }


    public Integer getOutTradeStatus() {
        return outTradeStatus;
    }

    public void setOutTradeStatus(Integer outTradeStatus) {
        this.outTradeStatus = outTradeStatus;
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

    public String getAliOrder() {
        return aliOrder;
    }

    public void setAliOrder(String aliOrder) {
        this.aliOrder = aliOrder;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getRunNum() {
        return runNum;
    }

    public void setRunNum(Integer runNum) {
        this.runNum = runNum;
    }

    public Integer getRunStatus() {
        return runStatus;
    }

    public void setRunStatus(Integer runStatus) {
        this.runStatus = runStatus;
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

    public Integer getDataType() {
        return dataType;
    }

    public void setDataType(Integer dataType) {
        this.dataType = dataType;
    }
}
