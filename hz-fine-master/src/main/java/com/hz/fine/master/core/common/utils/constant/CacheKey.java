package com.hz.fine.master.core.common.utils.constant;

/**
 * @author df
 * @Description:redis的key
 * @create 2019-05-22 15:43
 **/
public interface CacheKey {

    /**
     * 手机发送验证码
     */
    String PHONE_VCODE = "-1";

    /**
     * 策略数据
     */
    String STRATEGY = "-2";

    /**
     * 共享：其它地方要进行设置值的，多个地方要用到
     * 银行卡的日金额已经到达多少钱 - 日上限金额
     */
    String SHARE_BANK_MONEY_DAY = "-3";

    /**
     * 共享：其它地方要进行设置值的，多个地方要用到
     * 银行卡的月金额已经到达多少钱 - 月上限金额
     */
    String SHARE_BANK_MONEY_MONTH = "-4";

    /**
     * 共享：其它地方要进行设置值的，多个地方要用到
     * 银行卡的总金额已经到达多少钱 - 总上限金额
     */
    String SHARE_BANK_MONEY_TOTAL = "-5";


    /**
     * 共享：其它地方要进行设置值的，多个地方要用到
     * 银行卡的日次数已经到达多少次 - 日上限次数
     */
    String SHARE_BANK_NUM_DAY = "-6";

    /**
     * 共享：其它地方要进行设置值的，多个地方要用到
     * 银行卡的月次数已经到达多少次 - 月上限次数
     */
    String SHARE_BANK_NUM_MONTH = "-7";

    /**
     * 共享：其它地方要进行设置值的，多个地方要用到
     * 银行卡的总次数已经到达多少次 - 总上限次数
     */
    String SHARE_BANK_NUM_TOTAL = "-8";

    /**
     * 挂单金额
     * <p>银行卡具体的挂单金额</p>
     */
    String HANG_MONEY = "-9";

    /**
     * 需要锁的金额
     * 锁住带分（元，分）的金额
     */
    String LOCK_MONEY_CENT = "-10";

    /**
     * 用户调起充值订单的失效时间
     * 为了避免用户重复频繁调起充值设置的
     * 当用户充值完毕：这个值需要进行删除（task跑时间时）
     */
    String LOCK_DID_ORDER_INVALID_TIME = "-11";

}
