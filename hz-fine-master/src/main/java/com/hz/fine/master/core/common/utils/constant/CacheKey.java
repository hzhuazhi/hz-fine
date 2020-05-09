package com.hz.fine.master.core.common.utils.constant;

/**
 * @author df
 * @Description:redis的key
 * @create 2019-05-22 15:43
 **/
public interface CacheKey {

    /**
     * 电话号码号码
     */
    String PHONE_INFO = "-1";

    /**
     * deviceId
     */
    String DEVICE_INFO = "-2";

    /**
     * 微信号
     */
    String WX_INFO = "-3";

    /**
     * token
     */
    String TOKEN_INFO = "-4";

    /**
     * 邀请码
     */
    String INVITE_INFO = "-5";

    /**
     * 交易地址
     */
    String TRADING_ADDRESS_INFO = "-6";


    /***
     * 用户id
     */
    String MEMBER_ID_INFO = "-7";

    /**
     * PW_TOKEN
     */
    String PW_TOKEN= "-8";



    /**
     * 注册_SMS
     */
    String REGISTER_SMS = "reg-sms-";

    /**
     * 忘记密码_sms
     */
    String FORGET_SMS  = "forget-sms-";

    /**
     * 登陆_sms
     */
    String SIGN_IN_SMS  = "signin-sms-";

    /**
     * 修改支付密码_sms
     */
    String UPDATE_PAYPW_SMS  = "paypw-sms-";

    /**
     * task操作支付宝转账
     * 如果多台服务器运行，会出现问题，所以在变量名称前加了lock
     */
    String LOCK_TRANSFER = "-9";

    /**
     * task跑阿里支付宝订单同步数据时：锁住这条任务
     */
    String LOCK_TASK_ALIPAY_NOTIFY = "-10";

    /**
     * 密保
     */
    String ITEM_BANK = "-11";

    /**
     * 密保：已通过密保问答
     */
    String ITEM_BANK_ANSWER = "-12";

    /**
     * 系统通知，系统公告，传播、扩散的通知
     */
    String SPREAD_NOTICE = "-13";

    /**
     * 系统通知，系统公告，传播、扩散的通知-详情
     */
    String SPREAD_NOTICE_INFO = "-14";

    /**
     * 阿里支付：用户频繁调起支付宝转账
     */
    String ALIPAY_MEMBER = "-15";

    /**
     * task操作支付宝转账结果操作
     * 如果多台服务器运行，会出现问题，所以在变量名称前加了lock
     */
    String LOCK_TRANSFER_RESULT = "-17";
}
