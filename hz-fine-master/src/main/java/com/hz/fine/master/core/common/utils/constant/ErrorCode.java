package com.hz.fine.master.core.common.utils.constant;

/**
 * @author df
 * @Description:异常状态码
 * @create 2018-07-27 11:13
 **/
public class ErrorCode {

    /**
     * 常量异常
     */
    public final class ERROR_CONSTANT {
        /**
         * 没有被捕捉到的异常
         * 默认系统异常状态码=255
         */
        public static final String DEFAULT_EXCEPTION_ERROR_CODE = "255";

        /**
         * 没有被捕捉到的异常
         * 默认系统异常错误信息=SYS_ERROR
         */
        public static final String DEFAULT_EXCEPTION_ERROR_MESSAGE = "ERROR";

        /**
         * 被捕捉到的异常，并且捕捉的异常错误码为空，则默认异常状态码
         * 默认捕捉的异常状态码=256
         */
        public static final String DEFAULT_SERVICE_EXCEPTION_ERROR_CODE = "256";

        /**
         * 被捕捉到的异常，但是错误信息为空，则默认异常信息提醒
         * 默认捕捉的异常信息提醒=错误
         */
        public static final String DEFAULT_SERVICE_EXCEPTION_ERROR_MESSAGE = "错误";

    }


    /**
     * 异常-枚举类
     */
    public enum ENUM_ERROR {

        /***********************************************
         * B打头表示银行卡的错误
         **********************************************/
        B00001("B00001", "错误,请重试!", "没有查询到可用的银行卡数据!"),


        /***********************************************
         * D打头表示用户的错误
         **********************************************/
        D00000("D00000", "请登录!", "服务端token失效!"),
        D00001("D00001", "错误,请重试!", "客户端没有传token值!"),
        D00002("D00002", "错误,请重试!", "用户注册时,所有数据都为空!"),
        D00003("D00003", "错误,请重试!", "用户注册时,账号数据都为空!"),
        D00004("D00004", "错误,请重试!", "用户注册时,登录密码数据为空!"),
        D00005("D00005", "错误,请重试!", "用户注册时,邀请码数据为空!"),
        D00006("D00006", "错误,请重试!", "用户注册时,验证码数据为空!"),
        D00007("D00007", "此账号已被注册过,请换其它账号注册!", "用户注册时,此账号已被注册过!"),
        D00008("D00008", "邀请码填写有误,请填写正确的邀请码!", "用户注册时,邀请码填写有误!"),

        D00009("D00009", "错误,请重试!", "用户修改密码时,所有数据都为空!"),
        D00010("D00010", "错误,请重试!", "用户修改密码时,原始密码数据为空!"),
        D00011("D00011", "错误,请重试!", "用户修改密码时,新密码数据为空!"),
        D00012("D00012", "您输入的原始密码错误,请重试!", "用户修改密码时,原始密码与数据库的不匹配!"),

        D00013("D00013", "错误,请重试!", "用户登录时,所有数据都为空!"),
        D00014("D00014", "错误,请重试!", "用户登录时,账号数据为空!"),
        D00015("D00015", "错误,请重试!", "用户登录时,密码数据为空!"),
        D00016("D00016", "账号或密码输入有误,请重试!", "用户登录时,账号密码输入有误!"),

        D00017("D00017", "错误,请重试!", "用户忘记密码时,所有数据都为空!"),
        D00018("D00018", "错误,请重试!", "用户忘记密码时,vtoken数据都为空!"),
        D00019("D00019", "错误,请重试!", "用户忘记密码时,新密码数据为空!"),
        D00020("D00020", "修改密码已经超过有效时间范围,请重试!", "用户忘记密码时,根据vtoken获取用户账号信息为空!"),
        D00021("D00021", "错误,请重试!", "用户忘记密码时,根据账号查询用户数据,没有此账号信息!"),

        D00022("D00022", "错误,请重试!", "获取用户账号基本信息时,所有数据都为空!"),
        D00023("D00023", "错误,请重试!", "获取用户账号基本信息时,根据用户DID查询数据库的数据为空!"),

        D00024("D00024", "错误,请重试!", "用户修改安全密码/操作密码时,所有数据都为空!"),
        D00025("D00025", "错误,请重试!", "用户修改安全密码/操作密码时,原始操作密码/安全密码数据为空!"),
        D00026("D00026", "错误,请重试!", "用户修改安全密码/操作密码时,新安全密码/操作密码数据为空!"),
        D00027("D00027", "您输入的原始安全密码错误,请重试!", "用户修改操作密码/安全密码时,原始操作密码/安全密码与数据库的不匹配!"),

        D00028("D00028", "错误,请重试!", "用户忘记安全密码时,所有数据都为空!"),
        D00029("D00029", "错误,请重试!", "用户忘记安全密码时,账号数据都为空!"),
        D00030("D00030", "错误,请重试!", "用户忘记安全密码时,新安全密码数据为空!"),
        D00031("D00031", "错误,请重试!", "用户忘记安全密码时,验证码数据为空!"),
        D00032("D00032", "错误,请重试!", "用户忘记安全密码时,根据账号查询用户数据,没有此账号信息!"),

        D00033("D00033", "您的安全密码输入有误,请重试!", ",根据用户操作/安全密码查询数据库的数据为空!"),

        D00034("D00034", "错误,请重试!", "更新用户出码开关时,所有数据都为空!"),
        D00035("D00025", "错误,请重试!", "更新用户出码开关时,开关类型数据为空!"),


        /***********************************************
         * DC打头表示用户收款账号错误
         **********************************************/
        DC00001("DC00001", "错误,请重试!", "用户添加收款账号时,所有数据都为空!"),
        DC00002("DC00002", "错误,请重试!", "用户添加收款账号时,收款账号类型数据为空!"),
        DC00003("DC00003", "错误,请重试!", "用户添加收款账号时,收款的具体账号数据为空!"),
        DC00004("DC00004", "错误,请重试!", "用户添加收款账号时,收款账号/地址（原）：二维码数据为空!"),
        DC00005("DC00005", "错误,请重试!", "用户添加收款账号时,收款人数据为空!"),
        DC00006("DC00006", "错误,请重试!", "用户添加收款账号时,银行名称/银行卡开户行数据为空!"),
        DC00007("DC00007", "错误,请重试!", "用户添加收款账号时,经营范围类型数据为空!"),
        DC00008("DC00008", "错误,请重试!", "用户添加收款账号时,小微商户二维码图片地址数据为空!"),
        DC00009("DC00009", "收款具体账号已录入,请不要重复录入!", "用户添加收款账号时,收款具体账号已经录入过!"),

        DC00010("DC00010", "错误,请重试!", "获取用户收款账号集合时,所有数据都为空!"),

        DC00011("DC00011", "错误,请重试!", "获取用户收款账号详情时,所有数据都为空!"),
        DC00012("DC00012", "错误,请重试!", "获取用户收款账号详情时,收款账号的ID数据为空!"),

        DC00013("DC00013", "错误,请重试!", "修改用户收款账号基本信息时,所有数据都为空!"),
        DC00014("DC00014", "错误,请重试!", "修改用户收款账号基本信息时,收款账号的ID数据为空!"),

        DC00015("DC00015", "错误,请重试!", "用户更新收款账号信息时,所有数据都为空!"),
        DC00016("DC00016", "错误,请重试!", "用户更新收款账号信息时,收款账号类型数据为空!"),
        DC00017("DC00017", "错误,请重试!", "用户更新收款账号信息时,收款的具体账号数据为空!"),
        DC00018("DC00018", "错误,请重试!", "用户更新收款账号信息时,收款账号/地址（原）：二维码数据为空!"),
        DC00019("DC00019", "错误,请重试!", "用户更新收款账号信息时,收款人数据为空!"),
        DC00020("DC00020", "错误,请重试!", "用户更新收款账号信息时,小微商户二维码图片地址数据为空!"),
        DC00021("DC00021", "错误,请重试!", "用户更新收款账号信息时,收款账号ID数据为空!"),

        DC00022("DC00022", "错误,请重试!", "用户更新收款账号使用状态时,所有数据都为空!"),
        DC00023("DC00023", "错误,请重试!", "用户更新收款账号使用状态时,收款账号ID数据为空!"),
        DC00024("DC00024", "错误,请重试!", "用户更新收款账号使用状态时,使用状态或者删除状态的数据都为空!"),

        DC00025("DC00025", "错误,请重试!", "用户添加收款账号时,根据用户ID查询用户账号数据为空!"),
        DC00026("DC00026", "错误,请重试!", "用户添加收款账号时,用户未充值过，无法添加收款账号!"),

        DC00027("DC00027", "错误,请重试!", "用户更新收款账号小微二维码信息时,收款账号ID数据为空!"),
        DC00028("DC00028", "错误,请重试!", "用户更新收款账号小微二维码信息时,小微商户二维码图片地址数据为空!"),

        DC00029("DC00029", "错误,请重试!", "用户添加收款账号时,支付宝的账号ID数据为空!"),

        DC00030("DC00030", "错误,请重试!", "获取支付宝收款账号数据详情时,所有数据都为空!"),

        DC00031("DC00031", "错误,请重试!", "用户修改支付宝收款账号的信息时,所有数据都为空!"),
        DC00032("DC00032", "错误,请重试!", "用户修改支付宝收款账号的信息时,收款账号主键ID数据为空!"),
        DC00033("DC00033", "错误,请重试!", "用户修改支付宝收款账号的信息时,收款账号类型数据为空!"),
        DC00034("DC00034", "错误,请重试!", "用户修改支付宝收款账号的信息时,收款的具体账号数据为空!"),
        DC00035("DC00035", "错误,请重试!", "用户修改支付宝收款账号的信息时,支付宝的账号ID数据为空!"),
        DC00036("DC00036", "错误,请重试!", "用户修改支付宝收款账号的信息时,收款人数据为空!"),
        DC00037("DC00037", "错误,请重试!", "用户修改支付宝收款账号的信息时,操作/安全密码数据为空!"),

        DC00038("DC00038", "错误,请重试!", "用户添加收款账号时,微信群二维码地址数据为空!"),
        DC00039("DC00039", "微信群只允许录入一个,请不要重复录入!", "用户添加收款账号时,微信群已经录入过!"),

        DC00040("DC00040", "错误,请重试!", "获取群名称时,所有数据都为空!"),

        DC00041("DC00041", "错误,请重试!", "用户修改微信群二维码时,所有数据都为空!"),
        DC00042("DC00042", "错误,请重试!", "用户修改微信群二维码时,收款账号的主键ID数据为空!"),
        DC00043("DC00043", "错误,请重试!", "用户更新收款账号信息时,收款的群二维码数据为空!"),

        DC00044("DC00044", "错误,请重试!", "用户获取微信群收款账号信息集合时,所有数据都为空!"),
        DC00045("DC00045", "错误,请重试!", "用户获取微信群收款账号信息集合时,是否失效数据为空!"),



        /***********************************************
         * DR打头表示用户充值的错误
         **********************************************/
        DR00001("DR00001", "错误,请重试!", "用户发起充值时,所有数据都为空!"),
        DR00002("DR00002", "错误,请重试!", "用户发起充值时,充值金额数据为空!"),
        DR00003("DR00003", "错误,请重试!", "用户发起充值时,充值金额与策略中的充值列表金额不匹配!"),
        DR00004("DR00004", "错误,请重试!", "用户发起充值时,未筛选出银行卡以及对应的金额!"),
        DR00005("DR00005", "您还有未处理的充值订单,请处理完毕在继续!", "用户发起充值时,还有未处理的订单!"),

        DR00006("DR00006", "错误,请重试!", "用户上传转账凭证时,所有数据都为空!"),
        DR00007("DR00007", "错误,请重试!", "用户上传转账凭证时,订单号数据为空!"),
        DR00008("DR00008", "错误,请重试!", "用户上传转账凭证时,转账图片凭证数据为空!"),

        DR00009("DR00009", "错误,请重试!", "获取用户充值记录/用户充值订单-集合时,所有数据都为空!"),

        DR00010("DR00010", "错误,请重试!", "获取用户充值记录/用户充值订单-详情时,所有数据都为空!"),
        DR00011("DR00011", "错误,请重试!", "获取用户充值记录/用户充值订单-详情时,订单号数据为空!"),

        DR00012("DR00012", "错误,请重试!", "用户正式购买时,所有数据都为空!"),
        DR00013("DR00013", "错误,请重试!", "用户正式购买时,购买金额数据为空!"),
        DR00014("DR00014", "错误,请重试!", "用户正式购买时,银行卡主键ID数据为空!"),
        DR00015("DR00015", "错误,请重试!", "用户正式购买时,未筛选出银行卡能对应的金额!"),

        DR00016("DR000016", "错误,请重试!", "用户充值之后，更新充值存入账号的信息时,所有数据都为空!"),
        DR00017("DR00017", "错误,请重试!", "用户充值之后，更新充值存入账号的信息时,订单号数据为空!"),
        DR00018("DR00018", "错误,请重试!", "用户充值之后，更新充值存入账号的信息时,存入人数据为空!"),
        DR00019("DR00019", "错误,请重试!", "用户充值之后，更新充值存入账号的信息时,存入时间数据为空!"),
        DR00020("DR00020", "错误,请重试!", "用户充值之后，更新充值存入账号的信息时,存入账号的尾号数据为空!"),
        DR00022("DR00022", "错误,请重试!", "用户充值之后，更新充值存入账号的信息时,存入金额数据为空!"),

        DR00021("DR00021", "错误,请重试!", "获取用户是否有充值挂单时,所有数据都为空!"),

        DR00023("DR00023", "错误,请重试!", "用户正式充值-拉起充值时,所有数据都为空!"),
        DR00024("DR00024", "错误,请重试!", "用户正式充值-拉起充值时,购买金额数据为空!"),
        DR00025("DR00025", "错误,请重试!", "用户正式充值-拉起充值时,金额带的小数点位数错误!"),
        DR00026("DR00026", "错误,请重试!", "用户正式充值-拉起充值时,金额填写有误!"),
        DR00027("DR00027", "错误,请重试!", "用户正式充值-拉起充值时,order订单数据为空!"),
        DR00028("DR00028", "拉起订单超时,请重新选择充值金额!", "用户正式充值-拉起充值时,通过order查询redis数据为空!"),
        DR00029("DR00029", "此金额的订单已被人占用,请重新选择金额!", "用户正式充值-拉起充值时,此金额的订单已被人占用!"),// redis锁的抢占
        DR00030("DR00030", "此金额的订单已被人占用,请重新选择金额!", "用户正式充值-拉起充值时,此金额的订单已被人占用!"),// 查询数据库的数据



        /***********************************************
         * OR打头表示订单的错误
         **********************************************/
        OR00001("OR00001", "错误,请重试!", "派发订单时,所有数据都为空!"),
        OR00002("OR00002", "错误,请重试!", "派发订单时,金额数据为空!"),
        OR00003("OR00003", "错误,请重试!", "派发订单时,支付类型数据为空!"),
        OR00004("OR00004", "错误,请重试!", "派发订单时,没有筛选出有效的收款账号!"),
        OR00005("OR00005", "错误,请重试!", "派发订单时,有效的用户集合数据为空!"),
        OR00006("OR00006", "金额小数点只能有2位!", "派发订单时,金额带的小数点位数错误!"),
        OR00007("OR00007", "请填写正常的数字金额!", "派发订单时,金额填写有误!"),


        OR00010("OR00010", "错误,请重试!", "用户获取派发订单集合时,所有数据都为空!"),

        OR00011("OR00011", "错误,请重试!", "用户获取派发订单详情时,所有数据都为空!"),
        OR00012("OR00012", "错误,请重试!", "用户获取派发订单详情时,订单号数据都为空!"),

        OR00013("OR00013", "错误,请重试!", "获取派单数据-详情-返回码的接口时,所有数据都为空!"),
        OR00014("OR00014", "错误,请重试!", "获取派单数据-详情-返回码的接口时,订单号数据都为空!"),

        OR00015("OR00015", "错误,请重试!", "获取初始化派单数据时,所有数据都为空!"),

        OR00016("OR00016", "错误,请重试!", "用户订单的用户操作派单状态时,所有数据都为空!"),
        OR00017("OR00017", "错误,请重试!", "用户订单的用户操作派单状态时,订单号数据为空!"),
        OR00018("OR00018", "错误,请重试!", "用户订单的用户操作派单状态时,用户的操作派单状态数据为空!"),

        OR00019("OR00019", "错误,请重试!", "支付宝派发订单时,没有筛选出有效的收款账号!"),

        OR00020("OR00020", "错误,请重试!", "获取支付宝派单数据-详情-返回码的接口时,所有数据都为空!"),
        OR00021("OR00021", "错误,请重试!", "获取支付宝派单数据-详情-返回码的接口时,订单号数据都为空!"),

        OR00022("OR00022", "错误,请重试!", "微信群派发订单时,没有筛选出有效的收款账号!"),

        OR00023("OR00023", "错误,请重试!", "用户获取最近的一条派单数据时,所有数据都为空!"),





        /***********************************************
         * S打头表示策略数据的错误
         **********************************************/
        S00001("S00001", "错误,请重试!", "充值金额列表策略数据为空!"),
        S00002("S00002", "错误,请重试!", "银行工作时间策略数据为空!"),
        S00003("S00003", "错误,请重试!", "银行工作时间具体值策略数据为空!"),
        S00004("S00004", "错误,请重试!", "银行流水日月总规则策略数据为空!"),
        S00005("S00005", "错误,请重试!", "银行流水日月总规则策略数据没有与银行卡匹配的类型!"),
        S00006("S00006", "错误,请重试!", "订单金额加减范围列表策略数据为空!"),
        S00007("S00007", "错误,请重试!", "充值订单的失效时间策略数据为空!"),

        S00008("S00008", "错误,请重试!", "获取充值金额列表策略数据时,所有数据都为空!"),
        S00009("S00009", "错误,请重试!", "获取总金额充值档次奖励列表数据时,所有数据都为空!"),
        S00010("S00010", "错误,请重试!", "获取总金额充值档次奖励列表数据时,数据库数据为空!"),

        S00011("S00011", "错误,请重试!", "获取七牛云token策略数据时,所有数据都为空!"),

        S00012("S00012", "错误,请重试!", "获取分享邀请地址策略数据时,所有数据都为空!"),
        S00013("S00013", "错误,请重试!", "获取分享邀请地址策略数据时,数据库数据为空!"),

        S00014("S00014", "错误,请重试!", "上传七牛云图片时,上传的图片数据为空!"),
        S00015("S00015", "错误,请重试!", "上传七牛云图片时,上传的图片出错了!"),

        S00016("S00016", "错误,请重试!", "获取来源地址集合数据时,数据库数据为空!"),

        S00017("S00017", "错误,请重试!", "获取用户提交订单状态的最后读秒时间数据时,数据库数据为空!"),
        S00018("S00018", "错误,请重试!", "获取用户提交订单状态的最后读秒时间数据时,数据库中字段值数据为空!"),

        S00019("S00019", "错误,请重试!", "获取分享状态的开关数据时,数据库数据为空!"),

        S00020("S00020", "错误,请重试!", "团队日派单消耗成功累计总额奖励规则策略数据为空!"),

        S00021("S00021", "错误,请重试!", "获取分享状态的开关时,所有数据都为空!"),

        S00022("S00022", "错误,请重试!", "获取备用域名地址列表数据时,数据库数据为空!"),

        S00023("S00023", "错误,请重试!", "出码开关策略数据为空!"),
        S00024("S00024", "目前出码处于关闭状态!", "目前出码处于关闭状态!"),
        S00025("S00025", "错误,请重试!", "出码开关时间具体值策略数据为空!"),
        S00026("S00026", "目前不在出码时间范围内!", "出码开关时间,目前不在出码时间范围内!"),

        S00027("S00027", "错误,请重试!", "获取微信群回复指令策略数据时,数据库数据为空!"),

        S00028("S00028", "错误,请重试!", "微信群有效个数才允许正常出码策略数据为空!"),


        /***********************************************
         * R打头表示用户奖励的错误
         **********************************************/
        R00001("R00001", "错误,请重试!", "获取用户奖励数据集合时,所有数据都为空!"),

        R00002("R00002", "错误,请重试!", "获取用户奖励数据详情时,所有数据都为空!"),
        R00003("R00003", "错误,请重试!", "获取用户奖励数据详情时,奖励纪录的ID数据为空!"),

        R00004("R00004", "错误,请重试!", "获取用户分享奖励数据集合时,所有数据都为空!"),


        /***********************************************
         * M打头表示策略数据的错误
         **********************************************/
        M00001("M00001", "错误,请重试!", "手机卡数据为空!"),



        /***********************************************
         * V打头表示验证码的错误
         **********************************************/
        V00001("V00001", "错误,请重试!", "获取验证码时,所有数据都为空!"),
        V00002("V00002", "错误,请重试!", "获取验证码时,手机号数据为空!"),
        V00003("V00003", "频繁发送请求,请稍后再试!", "发送验证码验证码时,频繁发送请求!"),
        V00004("V00004", "获取验证码失败,请重试!", "获取验证码时,给手机发送验证码失败!"),
        V00005("V00005", "获取验证码超过有效期,请重试!", "验证码已经超过有效期!"),
        V00006("V00006", "验证码填写错误,请重试!", "验证码填写错误!"),

        V00007("V00007", "错误,请重试!", "提交验证码时,所有数据都为空!"),
        V00008("V00008", "错误,请重试!", "提交验证码时,手机号数据为空!"),
        V00009("V00009", "错误,请重试!", "提交验证码时,提交的验证码类型为空!"),



        /***********************************************
         * Q打头表示用户收款账号二维码的错误
         **********************************************/
        Q00001("Q00001", "错误,请重试!", "用户新增收款账号的二维码时,所有数据都为空!"),
        Q00002("Q00002", "错误,请重试!", "用户新增收款账号的二维码时,收款账号ID值为空!"),
        Q00003("Q00003", "错误,请重试!", "用户新增收款账号的二维码时,收款账号收款二维码数据为空!"),
        Q00004("Q00004", "错误,请重试!", "用户新增收款账号的二维码时,收款账号/地址（转码）：二维码数据为空!"),
        Q00005("Q00005", "错误,请重试!", "用户新增收款账号的二维码时,二维码类型数据为空!"),

        Q00006("Q00006", "错误,请重试!", "用户获取收款账号的二维码信息-集合时,所有数据都为空!"),
        Q00007("Q00007", "错误,请重试!", "用户获取收款账号的二维码信息-集合时,用户收款账号ID数据为空!"),
        Q00008("Q00008", "错误,请重试!", "用户获取收款账号的二维码信息-集合时/批量修改二维码使用状态时,通过用户ID跟收款账号ID查询用户收款账号信息为空!"),

        Q00009("Q00009", "错误,请重试!", "用户获取收款账号的二维码信息-详情时,所有数据都为空!"),
        Q00010("Q00010", "错误,请重试!", "用户获取收款账号的二维码信息-详情时,收款账号二维码的ID数据为空!"),

        Q00011("Q00011", "错误,请重试!", "用户更新收款账号的二维码信息时,所有数据都为空!"),
        Q00012("Q00012", "错误,请重试!", "用户更新收款账号的二维码信息时,收款账号二维码的ID数据为空!"),
        Q00013("Q00004", "错误,请重试!", "用户更新收款账号的二维码信息时,收款账号/地址（转码）：二维码数据为空!"),
        Q00014("Q00005", "错误,请重试!", "用户更新收款账号的二维码信息时,二维码类型数据为空!"),

        Q00015("Q00015", "错误,请重试!", "用户更新收款账号的二维码使用状态时,所有数据都为空!"),
        Q00016("Q00016", "错误,请重试!", "用户更新收款账号的二维码使用状态时,收款账号二维码的ID数据为空!"),
        Q00017("Q00017", "错误,请重试!", "用户更新收款账号的二维码使用状态时,使用状态或者删除状态的数据都为空!"),

        Q00018("Q00018", "错误,请重试!", "用户批量更新收款账号的二维码使用状态时,所有数据都为空!"),
        Q00019("Q00019", "错误,请重试!", "用户批量更新收款账号的二维码使用状态时,收款账号ID数据为空!"),
        Q00020("Q00020", "错误,请重试!", "用户批量更新收款账号的二维码使用状态时,使用状态或者删除状态的数据都为空!"),


        /***********************************************
         * A打头表示用户收款账号合二为一的错误
         **********************************************/
        A00001("A00001", "错误,请重试!", "用户添加收款账号时-合二为一,所有数据都为空!"),
        A00002("A00002", "错误,请重试!", "用户添加收款账号时-合二为一,收款账号类型数据为空!"),
        A00003("A00003", "错误,请重试!", "用户添加收款账号时-合二为一,收款人数据为空!"),
        A00004("A00004", "错误,请重试!", "用户添加收款账号时-合二为一,银行名称/银行卡开户行数据为空!"),
        A00005("A00005", "错误,请重试!", "用户添加收款账号时-合二为一,经营范围类型数据为空!"),
        A00006("A00006", "错误,请重试!", "用户添加收款账号时-合二为一,收款账号收款二维码数据为空!"),
        A00007("A00007", "错误,请重试!", "用户添加收款账号时-合二为一,收款账号/地址（转码）：二维码数据为空!"),
        A00008("A00008", "错误,请重试!", "用户添加收款账号时-合二为一,二维码类型数据为空!"),
        A00009("A00009", "错误,请重试!", "用户添加收款账号时-合二为一,添加用户收款账号数据响应行为0!"),


        /***********************************************
         * BK打头表示银行卡的错误
         **********************************************/
        BK00001("BK00001", "错误,请重试!", "获取银行卡信息-详情时,所有数据都为空!"),
        BK00002("BK00002", "错误,请重试!", "获取银行卡信息-详情时,银行卡主键ID数据都空!"),

        BK00003("BK00003", "错误,请重试!", "获取根据银行卡ID获取银行卡详情时,数据库数据为空!"),

        BK00004("BK00004", "错误,请重试!", "点击我要买-展现可用金额信息时,所有数据都为空!"),
        BK00005("BK00005", "错误,请重试!", "点击我要买-展现可用金额信息时,随机筛选的金额集合数据为空!"),


        /***********************************************
         * OF打头表示用户抢单上下线的错误
         **********************************************/
        OF00001("OF00001", "错误,请重试!", "用户抢单上线下修改时,所有数据都为空!"),
        OF00002("OF00002", "错误,请重试!", "用户抢单上线下修改时,数据类型为空!"),
        OF00003("OF00003", "错误,请重试!", "用户抢单上线下修改时,账号数据为空!"),
        OF00004("OF00004", "余额不足,请充值!", "用户抢单上线时,账号余额为空!"),

        OF00010("OF00010", "错误,请重试!", "获取用户抢单上下线的状态时,所有数据都为空!"),



        /***********************************************
         * N打头表示公告的错误
         **********************************************/
        N00001("N00001", "错误,请重试!", "获取公告详情时,请求的所有数据为空!"),
        N00002("N00002", "错误,请重试!", "获取公告详情时,请求的公告ID数据为空!"),

        /***********************************************
         * C打头表示在线客服、咨询的错误
         **********************************************/
        C00001("C00001", "错误,请重试!", "获取用户在线客服、咨询的发问数据集合时,所有数据都为空!"),

        C00002("C00002", "错误,请重试!", "获取用户在线客服、咨询的发问数据详情时,所有数据都为空!"),
        C00003("C00003", "错误,请重试!", "获取用户在线客服、咨询的发问数据详情时,主键ID数据为空!"),

        C00004("C00004", "错误,请重试!", "新增用户在线客服、咨询的发问时,所有数据都为空!"),
        C00005("C00005", "错误,请重试!", "新增用户在线客服、咨询的发问时,咨询类别ID数据为空!"),
        C00006("C00006", "请填写文字描述或者上传疑问图片!", "新增用户在线客服、咨询的发问时,文字描述,图片描述数据都为空!"),

        C00007("C00007", "错误,请重试!", "新增追加问答时,所有数据都为空!"),
        C00008("C00008", "错误,请重试!", "新增追加问答时,在线客服、咨询的发问的ID数据为空!"),
        C00009("C00009", "请填写文字描述或者上传疑问图片!", "新增追加问答时,文字描述,图片描述数据都为空!"),

        C00010("C00010", "错误,请重试!", "获取追加问答数据-集合时,所有数据都为空!"),
        C00011("C00011", "错误,请重试!", "获取追加问答数据-集合时,在线客服、咨询的发问的ID数据为空!"),


        /***********************************************
         * ST打头表示统计的错误
         **********************************************/
        ST00001("ST00001", "错误,请重试!", "添加支付用户点击支付页统计时,所有数据都为空!"),
        ST00002("ST00002", "错误,请重试!", "添加支付用户点击支付页统计时,标识值数据为空!"),
        ST00003("ST00003", "错误,请重试!", "添加支付用户点击支付页统计时,数据来源类型数据为空!"),


        /***********************************************
         * WX打头表示小微的错误
         **********************************************/
        WX00001("WX00001", "错误,请重试!", "小微数据集合时,所有数据都为空!"),
        WX00002("WX00002", "错误,请重试!", "小微数据详情时,所有数据都为空!"),

        /***********************************************
         * H打头表示大杂烩的错误
         **********************************************/
        H00001("H00001", "错误,请重试!", "获取推广通知详情时,所有数据都为空!"),
        H00002("H00002", "错误,请重试!", "获取推广通知详情时,ID值为空!"),





        ;

        /**
         * 错误码
         */
        private String eCode;
        /**
         * 给客户端看的错误信息
         */
        private String eDesc;
        /**
         * 插入数据库的错误信息
         */
        private String dbDesc;




        private ENUM_ERROR(String eCode, String eDesc,String dbDesc) {
            this.eCode = eCode;
            this.eDesc = eDesc;
            this.dbDesc  = dbDesc;
        }

        public String geteCode() {
            return eCode;
        }

        public void seteCode(String eCode) {
            this.eCode = eCode;
        }

        public String geteDesc() {
            return eDesc;
        }

        public void seteDesc(String eDesc) {
            this.eDesc = eDesc;
        }

        public String getDbDesc() {
            return dbDesc;
        }

        public void setDbDesc(String dbDesc) {
            this.dbDesc = dbDesc;
        }
    }
}
