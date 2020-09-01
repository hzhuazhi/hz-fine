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
     * 要过15分钟才失效，自动失效
     */
    String LOCK_DID_COLLECTION_ACCOUNT = "-15";

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

    /**
     * 用户收款账号日收款金额是否达到上限：用于redis锁使用
     * task时时跑每个成功订单里面的收款账号的收款金额，当到达策略里面的金额时，则往redis里面添加数据
     * #这里需要补充跑收款账号金额数据的task，并且纪录redis的失效时间是到第二天凌晨0点失效
     */
    String LOCK_DID_COLLECTION_ACCOUNT_DAY_SUC_MONEY = "-23";

    /**
     * 用户收款账号日给出次数否达到上限：用于redis锁使用
     * 每次给出，redis存储的失效时间到第二天凌晨0点失效，
     * 每次给出，先从redis里面获取，如果没值则往里面填1，下次有值则进行加1
     */
    String LOCK_DID_COLLECTION_ACCOUNT_DAY_LIMIT_NUM = "-24";

    /**
     * 用户收款账号日成功收款账号是否达到上限：用于redis锁使用
     * task时时跑每个成功金额里面的收款账号的成功收款次数，当到达策略里面的成功次数时，则往redis里面添加数据
     * #这里需要补充跑收款账号成功次数的数据的task，并且纪录redis的失效时间是到第二天凌晨0点失效
     */
    String LOCK_DID_COLLECTION_ACCOUNT_DAY_SUC_LIMIT_NUM = "-25";

    /**
     * 用户收款账号是否在15分钟之内给出过码：用于redis锁使用
     * 每次给出码时，redis存储的失效时间是15分钟，
     */
    String LOCK_DID_COLLECTION_ACCOUNT_FIFTEEN = "-26";

    /**
     * 用户收款账号连续失败达到策略部署的失败次数，纪录用户收款账号被处理过
     * 目前暂存redis的失效时间是30分钟
     */
    String LOCK_DID_COLLECTION_ACCOUNT_FAIL = "-27";


    /**
     * 银行卡当日收款的金额：用于redis锁使用；这里主要是纪录用户我要买，购买的数量
     * task时时跑每个成功充值订单里面的收款账号的收款金额
     * redis的失效时间是到第二天凌晨0点失效
     */
    String LOCK_BANK_DAY_SUC_MONEY = "-28";

    /**
     * 在用户抢单上下线修改状态的时候，需要先锁住此用户
     * 高并发避免为题
     */
    String LOCK_DID_ONOFF = "-29";

    /**
     * 用户是否一致处于开始抢单的界面的redis缓存
     * 客户端会每隔2秒访问服务端，这里代表着客户端一直跟服务端保持着通讯
     */
    String DID_ONOFF = "-30";

    /**
     * 需要锁的金额：无条件锁定金额3分钟
     * 锁住
     */
    String LOCK_MONEY_BY_RANDOM = "-31";

    /**
     * 会话ID存储银行卡的主键ID
     */
    String BANK_ID_BY_SGID = "-32";

    /**
     * 需要锁：银行卡ID + 充值金额的锁
     * <p>
     *     根据银行卡 + 充值金额 查询充值订单是否有存在相同金额的挂单
     * </p>
     * 锁住
     */
    String LOCK_BANK_ID_MONEY = "-33";

    /**
     * 小微每天加好友的数量
     * <p>
     *     1.task配合去累加当日加好友数量。
     *     2.redis的失效时间是：距离今天凌晨的时间失效
     *
     * </p>
     */
    String WX_DAY_NUM = "-34";

    /**
     * 用户已给出的小微
     */
    String WX_BY_DID = "-35";

    /**
     * 判断用户收款账号是否有挂单正在进行中
     * 才正式派单完成之后，需要把这个收款账号挂单填入缓存中；直到订单成功：把缓存删除、或者订单超过有效期
     */
    String LOCK_DID_COLLECTION_ACCOUNT_ORDER_ING = "-36";// 当有回执数据时：要使用task需要删除redis缓存

    /**
     * 小微每天加群的数量
     * <p>
     *     1.task配合去累加当日加群的数量。
     *     2.redis的失效时间是：距离今天凌晨的时间失效
     *
     * </p>
     */
    String WX_DAY_GROUP_NUM = "-37";

    /**
     * 微信每次只出一个群码的金额范围
     * <p>
     *     满足设定收款金额范围的微信ID进行redis纪录
     * </p>
     */
    String TO_WXID_RANGE_MONEY_TIME = "-38";

    /**
     * 给过码的池子的主键ID
     * <p>
     *     用于切割抢单池的用户
     * </p>
     */
    String QR_CODE_POOL_OPEN_ID = "-39";

}
