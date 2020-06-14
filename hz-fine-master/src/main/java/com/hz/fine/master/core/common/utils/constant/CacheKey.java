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
     * 银行卡的日收款金额已经到达多少钱 - 日上限金额
     */
    String SHARE_BANK_IN_MONEY_DAY = "-3";

    /**
     * 共享：其它地方要进行设置值的，多个地方要用到
     * 银行卡的月收款金额已经到达多少钱 - 月上限金额
     */
    String SHARE_BANK_IN_MONEY_MONTH = "-4";

    /**
     * 共享：其它地方要进行设置值的，多个地方要用到
     * 银行卡的总收款金额已经到达多少钱 - 总上限金额
     */
    String SHARE_BANK_IN_MONEY_TOTAL = "-5";


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
    String HANG_MONEY = "-9";// 当有回执数据时：要使用task需要删除redis缓存

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
    String LOCK_DID_ORDER_INVALID_TIME = "-11";// 当有回执数据时：要使用task需要删除redis缓存

    /**
     * 判断用户是否有挂单正在进行中
     * 才正式派单完成之后，需要把这个用户挂单填入缓存中；直到订单成功：把缓存删除、或者订单超过有效期
     */
    String LOCK_DID_ORDER_ING = "-12";// 当有回执数据时：要使用task需要删除redis缓存

    /**
     * 存储用户挂单的具体金额：用于redis锁使用
     * 因为前提如果取消用户可以同时派发多个单的限制时，则需要加上不同金额才能同时派发多个订单给用户
     */
    String LOCK_DID_ORDER_MONEY = "-13";// 当有回执数据时：要使用task需要删除redis缓存

    /**
     * 在派单的时候，需要先锁住此用户
     * 高并发避免为题
     */
    String LOCK_DID_ORDER = "-14";

    /**
     * 存储用户下的收款账号挂单：用于redis锁使用
     * 才正式派单完成之后，需要把这个用户挂单填入缓存中；直到订单成功：把缓存删除、或者订单超过有效期
     */
    String LOCK_DID_COLLECTION_ACCOUNT = "-15";// 当有回执数据时：要使用task需要删除redis缓存

    /**
     * 存储用户下的收款账号挂单的具体金额：用于redis锁使用
     * 才正式派单完成之后，需要把这个用户挂单填入缓存中；直到订单成功：把缓存删除、或者订单超过有效期
     */
    String LOCK_DID_COLLECTION_ACCOUNT_MONEY = "-16";// 当有回执数据时：要使用task需要删除redis缓存

    /**
     * 锁住用户：lock
     * 当只要操作用户金额时，都要调用这个redis的key
     * 重
     */
    String LOCK_DID_MONEY = "-17";


    /**
     * 在派单的时候，需要先锁住此收款账号
     * 高并发避免为题
     */
    String LOCK_DID_COLLECTION_ACCOUNT_FOR = "-18";

    /**
     * 存储用户登录的token
     */
    String DID_TOKEN_BY_ID = "-19";

    /**
     * 共享：其它地方要进行设置值的，多个地方要用到
     * 银行卡的日转账金额已经到达多少钱 - 日上限金额
     */
    String SHARE_BANK_OUT_MONEY_DAY = "-20";

    /**
     * 共享：其它地方要进行设置值的，多个地方要用到
     * 银行卡的月转账金额已经到达多少钱 - 月上限金额
     */
    String SHARE_BANK_OUT_MONEY_MONTH = "-21";

    /**
     * 共享：其它地方要进行设置值的，多个地方要用到
     * 银行卡的总转账金额已经到达多少钱 - 总上限金额
     */
    String SHARE_BANK_OUT_MONEY_TOTAL = "-22";

}
