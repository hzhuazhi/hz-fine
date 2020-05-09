package com.hz.fine.master.core.model.alipay;

/**
 * @Description Participant类型，收款方信息
 * @Author yoko
 * @Date 2020/1/9 21:00
 * @Version 1.0
 */
public class PayeeInfo {
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

    public PayeeInfo(){

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
}
