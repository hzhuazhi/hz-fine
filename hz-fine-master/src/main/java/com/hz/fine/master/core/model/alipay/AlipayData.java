package com.hz.fine.master.core.model.alipay;

/**
 * @Description 对应数据库字段
 * @Author yoko
 * @Date 2020/1/10 9:48
 * @Version 1.0
 */
public class AlipayData {

    /**
     * 主键ID
     */
    public Long id;

    /**
     * 会员id
     */
    public Long memberId;

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
//    /**
//     * Participant类型，收款方信息
//     */
//    public PayeeInfo payee_info;

    /**
     * 参与方的标识ID，比如支付宝用户UID。
     */
    public String identity;
    /**
     * 参与方的标识类型，目前支持如下枚举：1、ALIPAY_USER_ID 支付宝的会员ID
     * 2、ALIPAY_LOGON_ID：支付宝登录号，支持邮箱和手机号格式
     */
    public String identity_type;
    /**
     * 参与方真实姓名，如果非空，将校验收款支付宝账号姓名一致性。当identity_type=ALIPAY_LOGON_ID时，本字段必填。若传入该属性，则在支付宝回单中将会显示这个属性
     */
    public String name;

    /**
     * 转账业务的标题，用于在支付宝用户的账单里显示。
     */
    public String order_title;

    /**
     * 业务备注
     */
    public String remark;

    /**
     * 支付宝支付资金流水号
     */
    public String order_id;

    /**
     * 支付宝支付资金流水号
     */
    public String pay_fund_order_id;

    /**
     * SUCCESS：成功；
     */
    public String status;

    /**
     * 订单支付时间，格式为yyyy-MM-dd HH:mm:ss
     */
    public String trans_date;

    /**
     * 状态码：10000为成功，其它属于失败
     */
    public String code;

    /**
     * 错误说明
     */
    public String subMsg;

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

    public AlipayData(){

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

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getIdentity_type() {
        return identity_type;
    }

    public void setIdentity_type(String identity_type) {
        this.identity_type = identity_type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrder_title() {
        return order_title;
    }

    public void setOrder_title(String order_title) {
        this.order_title = order_title;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getPay_fund_order_id() {
        return pay_fund_order_id;
    }

    public void setPay_fund_order_id(String pay_fund_order_id) {
        this.pay_fund_order_id = pay_fund_order_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTrans_date() {
        return trans_date;
    }

    public void setTrans_date(String trans_date) {
        this.trans_date = trans_date;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSubMsg() {
        return subMsg;
    }

    public void setSubMsg(String subMsg) {
        this.subMsg = subMsg;
    }
}
