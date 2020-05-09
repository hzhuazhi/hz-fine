package com.hz.fine.master.core.model.alipay;

/**
 * @Description 阿里支付宝单笔转账到账户的实体属性Bean
 * @Author yoko
 * @Date 2020/1/9 19:46
 * @Version 1.0
 */
public class AlipayTransferModel {

    /**
     * 商户端的唯一订单号，对于同一笔转账请求，商户需保证该订单号唯一。
     */
    public String out_biz_no;
    /**
     * 订单总金额，单位为元，精确到小数点后两位，取值范围[0.01,100000000]
     */
    public String trans_amount;
    /**
     * 销售产品码，单笔无密转账固定为TRANS_ACCOUNT_NO_PWD
     */
    public String product_code;
    /**
     * 业务场景，单笔无密转账固定为DIRECT_TRANSFER
     */
    public String biz_scene;
    /**
     * Participant类型，收款方信息
     */
    public PayeeInfo payee_info;
    /**
     * 转账业务的标题，用于在支付宝用户的账单里显示。
     */
    public String order_title;

    public AlipayTransferModel(){

    }

    public String getOut_biz_no() {
        return out_biz_no;
    }

    public void setOut_biz_no(String out_biz_no) {
        this.out_biz_no = out_biz_no;
    }

    public String getTrans_amount() {
        return trans_amount;
    }

    public void setTrans_amount(String trans_amount) {
        this.trans_amount = trans_amount;
    }

    public String getProduct_code() {
        return product_code;
    }

    public void setProduct_code(String product_code) {
        this.product_code = product_code;
    }

    public String getBiz_scene() {
        return biz_scene;
    }

    public void setBiz_scene(String biz_scene) {
        this.biz_scene = biz_scene;
    }

    public PayeeInfo getPayee_info() {
        return payee_info;
    }

    public void setPayee_info(PayeeInfo payee_info) {
        this.payee_info = payee_info;
    }

    public String getOrder_title() {
        return order_title;
    }

    public void setOrder_title(String order_title) {
        this.order_title = order_title;
    }
}
