package com.hz.fine.master.core.common.utils.constant;

/**
 * @author df
 * @Description:服务端常量
 * @create 2018-07-24 16:05
 **/
public class ServerConstant {

    /**
     * 公共，公用的常量
     */
    public final class PUBLIC_CONSTANT{
        /**
         *值是否等于0的判断条件
         */
        public static final int SIZE_VALUE_ZERO = 0;

        /**
         * 值是否等于1的判断条件
         */
        public static final int SIZE_VALUE_ONE = 1;

        /**
         * 值是否等于2的判断条件
         */
        public static final int SIZE_VALUE_TWO = 2;

        /**
         * 值是否等于3的判断条件
         */
        public static final int SIZE_VALUE_THREE = 3;

        /**
         * 字符串值等于1
         */
        public static final String STR_VALUE_ONE = "1";

        /**
         * token计算标识
         */
        public static final String TAG_HZ = "HZ";

        /**
         * 是否需要邮件提醒：1不需要邮件提醒，2需要邮件提醒
         * 这里2表示需要邮件提醒
         */
        public static final int MAIL_REMIND_YES = 2;

        /**
         * 钱包地址：前缀标签
         */
        public static final String CURRENCY_TAG = "gd";

        /**
         * 锁redis的key的前缀标签
         */
        public static final String REDIS_LOCK_TAG = "lock";


        /**
         * 运行计算状态：：0初始化，1锁定，2计算失败，3计算成功，4扣量
         * 这里3表示成功
         */
        public static final int RUN_STATUS_THREE = 3;

        /**
         * 跑数据被执行了5次为上限
         */
        public static final int RUN_NUM_FIVE = 5;

        /**
         * 检测小微二维码失效
         */
        public static final String CHECK_INFO = "检测小微二维码失效";


    }


    /**
     * 策略的枚举
     * 策略类型：1充值金额列表
     */
    public enum StrategyEnum{
        MONEY(1, ""),
        BANK_WORK(2,""),
        BANK_LIST(3,""),
        BANK_LIMIT(4,""),
        MONEY_ADD_SUBTRACT_LIST(5,""),
        ORDER_INVALID_TIME(6,""),
        MONEY_GRADE_LIST(7,""),
        SHARE_ADDRESS(8,""),
        MOBILE_CARD_TYPE_LIST(9,""),
        DIRECT_REWARD(10,""),
        TEAM_REWARD(11,""),
        QC_CODE_LIMIT_LIST(12,""),
        QC_CODE_FAIL_NUM(13,""),
        REFERER_LIST(14,""),
        CONSUME_MONEY_LIST(15,""),
        LAST_TIME(16,""),
        ZFB_RULE(17,""),
        ZFB_MONEY_RULE(18,""),
        SHARE_SWITCH(19,""),
        TEAM_CONSUME_REWARD_LIST(20,""),
        TRIGGER_QUOTA_REWARD(21,""),
        TEAM_CONSUME_CUMULATIVE_REWARD_LIST(22,""),
        TEAM_REWARD_LIST(23,""),
        TEAM_CONSUME_REWARD(24,""),
        SPARE_ADDRESS_LIST(25,""),
        DELIVERY_ORDER_INVALID_TIME(26,""),
        LOCK_MONEY_TIME(27,""),
        REPORT_PHONE(28,""),
        QR_CODE_SWITCH(29,""),
        WX_GROUP_CONSUME_MONEY_LIST(30,""),
        TEAM_CONSUME_REWARD_WX_GROUP(31,""),
        WX_GROUP_REPLY_INSTRUCT(32,""),
        REPLY_TIME(33,""),
        GROUP_NAME(34,""),
        GROUP_RED_PACK_NUM(35,""),
        GROUP_NUM(36,""),
        WX_NUM(37,""),
        POOL_CONSUME_TIME(38,""),
        POOL_OPEN_MIN_MONEY(39,""),
        GROUP_MIN_MONEY(40,""),




        ;
        private int stgType;
        private String stgKey;

        private StrategyEnum(int stgType, String stgKey) {
            this.stgType = stgType;
            this.stgKey = stgKey;
        }

        public int getStgType() {
            return stgType;
        }

        public void setStgType(int stgType) {
            this.stgType = stgType;
        }

        public String getStgKey() {
            return stgKey;
        }

        public void setStgKey(String stgKey) {
            this.stgKey = stgKey;
        }
    }






    /**
     * @Description: 服务的所有接口说明的枚举
     * @author yoko
     * @date 2019/12/10 9:54
     */
    public enum InterfaceEnum{
        ALI_SENDALI(1, "AlipayController.sendAli", "阿里支付宝：生成订单码-APP"),
        ALI_SENDALIH5(2, "AlipayController.sendAliH5", "阿里支付宝：生成订单码-H5页面"),

        BK_GETDATALIST(3, "ItemBankController.getDataList", "获取密保数据集合"),
        BK_GETCUSTOMERDATALIST(4, "ItemBankController.getCustomerDataList", "获取用户的密保数据集合"),
        BK_ADD(5, "ItemBankController.add", "用户添加密保"),
        BK_CHECK(6, "ItemBankController.check", "用户校验密保"),

        NC_GETDATALIST(7, "NoticeDController.getDataList", "获取公告数据"),


        QT_GETDATAMLIST(8, "QuestionController.getDataMList", "获取百问百答-类别数据"),
        QT_GETDATADLIST(9, "QuestionController.getDataDList", "获取百问百答-详情-集合数据"),
        QT_GETDATAD(10, "QuestionController.getDataD", "获取百问百答-详情数据"),

        SD_GETDATALIST(11, "SpreadNoticeController.getDataList", "系统通知，系统公告，传播、扩散的通知的数据集合"),
        SD_GETDATA(12, "SpreadNoticeController.getData", "系统通知，系统公告，传播、扩散的通知-详情数据"),

        UP_GETDATA(13, "UpgradeController.getData", "获取客户端版本更新数据"),




        LOGIN_REGISTERSMS(14, "LoginController.getRegisterSms", "注册获取手机信息"),
        LOGIN_REGISTERINFO(15, "LoginController.getRegisterInfo", "用户注册接口，用户提交手机号码，以及详细信息进行提交"),
        LOGIN_REGISTERVERIFY(16, "LoginController.getRegisterVerify", "注册验证码"),
        LOGIN_FORGETPASSWORD(17, "LoginController.getForgetPassword", "忘记密码接口,修改密码接口"),
        LOGIN_FORGETPHONE(18, "LoginController.getForgetPhone", "忘记密码接口,根据手机号和验证码修改信息"),
        LOGIN_SIGNIN(19, "LoginController.getSignIn", "登录信息"),

        PAY_HAVAPAY(20, "UserController.havaPay", "是否设置了支付宝"),
        PAY_HAVAPAYINFO(21, "PayController.havaPayInfo", "支付宝列表查询"),
        PAY_ADDZFBPAY(22, "PayController.addZFBPay", "添加支付接口信息"),
        PAY_UPDATEZFBPAY(23, "PayController.updateZFBPay", "修改支付密码"),
        PAY_PAYCASHOUT(24, "PayController.payCashOut", "添加支付接口信息"),

        USER_QUERYUSERINFO(25, "UserController.payCashOut", "添加支付接口信息"),
        USER_MYFRIEND(26, "UserController.myFriend", "我的好友"),
        USER_QUERYUSER(27, "UserController.queryUser", "查询用户基本信息"),
        USER_EDITUSERINFO(28, "UserController.editUserInfo", "编辑用户信息接口"),
        USER_MYFUNDLIST(29, "UserController.myFundList", "我的资金详细信息"),
        USER_MYCASHRATE(30, "UserController.myCashRate", "我的支付信息接口"),
        USER_CHECKPAYPASSWORD(31, "UserController.checkPayPassword", "获取修改支付密码的pay"),
        USER_UPDATEPAYPASSWORD(32, "UserController.updatePayPassword", "修改支付密码接口"),
        USER_FIRSTUQDATEPAYPW(33, "UserController.firstUqdatePayPw", "第一次修改密码"),
        USER_UPDATEBASEINFO(34, "UserController.updateBaseInfo", "外部接口调用，更新数据接口"),



        REWARD_VIPRECEIVEFISSIONREWARD(35, "RewardController.vipReceivefissionReward", "永久vip 领取裂变奖励"),





        ;
        /**
         * 类型
         */
        private int type;
        /**
         * 接口地址
         */
        private String ads;
        /**
         * 接口描述
         */
        private String desc;

        private InterfaceEnum(int type,String ads, String desc){
            this.type = type;
            this.ads = ads;
            this.desc = desc;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getAds() {
            return ads;
        }

        public void setAds(String ads) {
            this.ads = ads;
        }
    }






}
