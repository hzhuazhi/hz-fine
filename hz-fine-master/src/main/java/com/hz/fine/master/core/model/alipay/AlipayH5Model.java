package com.hz.fine.master.core.model.alipay;

/**
 * @Description 阿里支付的实体bean-H5
 * @Author yoko
 * @Date 2020/1/19 11:13
 * @Version 1.0
 */
public class AlipayH5Model {
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
    public String out_trade_no;


    /**
     * 超时时间
     * 30m（30分钟）
     */
    public String timeout_express;

    /**
     * 总金额
     */
    public String total_amount;


    /**
     * 商品编码
     */
    public String product_code;

    /**
     * 阿里返回的订单串
     */
    public String aliOrder;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public String getTimeout_express() {
        return timeout_express;
    }

    public void setTimeout_express(String timeout_express) {
        this.timeout_express = timeout_express;
    }

    public String getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(String total_amount) {
        this.total_amount = total_amount;
    }

    public String getProduct_code() {
        return product_code;
    }

    public void setProduct_code(String product_code) {
        this.product_code = product_code;
    }

    public String getAliOrder() {
        return aliOrder;
    }

    public void setAliOrder(String aliOrder) {
        this.aliOrder = aliOrder;
    }
}
