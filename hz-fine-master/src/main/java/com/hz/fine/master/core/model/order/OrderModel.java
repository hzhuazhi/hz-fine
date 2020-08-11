package com.hz.fine.master.core.model.order;

import com.hz.fine.master.core.protocol.page.BasePage;

import java.io.Serializable;
import java.util.List;

/**
 * @Description 任务订单的实体属性Bean
 * @Author yoko
 * @Date 2020/5/21 19:27
 * @Version 1.0
 */
public class OrderModel extends BasePage implements Serializable {
    private static final long   serialVersionUID = 1203223201121L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 奖励归属用户ID：对应表tb_fn_did的主键ID；奖励给哪个用户
     */
    private Long did;

    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 订单金额
     */
    private String orderMoney;

    /**
     * 用户收款账号ID：对应表tb_fn_did_collection_account的主键ID
     */
    private Long collectionAccountId;

    /**
     * 收款账号类型：1微信，2支付宝，3银行卡
     */
    private Integer collectionType;

    /**
     * 收款二维码的主键ID：对应表tb_fn_did_collection_account_qr_code的主键ID
     */
    private Long qrCodeId;

    /**
     * 收款的二维码地址：转码之后的二维码
     */
    private String qrCode;

    /**
     * 微信昵称
     */
    private String wxNickname;

    /**
     *小微管理员主键ID
     */
    private Long wxId;

    /**
     * 商家订单号
     */
    private String outTradeNo;

    /**
     * 同步的接口地址:我方的同步地址
     */
    private String notifyUrl;

    /**
     * 订单状态：1初始化，2超时/失败，3有质疑，4成功
     */
    private Integer orderStatus;

    /**
     * 失效时间
     */
    private String invalidTime;

    /**
     * 支付宝的用户ID
     */
    private String userId;

    /**
     * 支付宝的账号
     */
    private String zfbAcNum;

    /**
     * 订单成功程序上报时间
     */
    private String programTime;

    /**
     * 订单状态_用户操作的状态：1初始化，2失败，3超时后默认成功，4用户点击成功
     */
    private Integer didStatus;

    /**
     * 订单成功用户上报时间
     */
    private String didTime;



    /**
     * 创建日期：存的日期格式20160530
     */
    private Integer curday;

    /**
     * 创建所属小时：24小时制
     */
    private Integer curhour;

    /**
     * 创建所属分钟：60分钟制
     */
    private Integer curminute;

    /**
     *运行计算次数
     */
    private Integer runNum;

    /**
     * 运行计算状态：：0初始化，1锁定，2计算失败，3计算成功
     */
    private Integer runStatus;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 更新时间
     */
    private String updateTime;

    /**
     * 是否有效：0有效，1无效/删除
     */
    private Integer yn;

    private Integer curdayStart;
    private Integer curdayEnd;


    /**
     * 收款账户名称：用户备注使用
     */
    private String acName;

    /**
     * 收款的具体账号：类型为微信则微信账号，支付宝为支付宝账号；怕后期有其它冲突
     */
    private String acNum;

    /**
     * 收益
     */
    private String profit;

    /**
     * 是否发了红包：1初始化未发红包，2发了红包
     */
    private Integer isRedPack;

    /**
     * 发红包的时间
     */
    private String redPackTime;

    /**
     * 是否回复：1初始化未回复，2系统默认回复，3已回复失败，4已回复成功
     */
    private Integer isReply;

    /**
     * 回复类容
     */
    private String replyData;

    /**
     * 回复时间
     */
    private String replyTime;

    /**
     * 回复的实际金额
     */
    private String actualMoney;

    /**
     * 金额是否与上报金额一致：1初始化，2少了，3多了，4一致
     */
    private Integer moneyFitType;

    /**
     * 是否是补单：1初始化不是补单，2是补单
     */
    private Integer replenishType;


    /**
     * 收益比例
     */
    private String ratio;

    /**
     * 失效时间-秒
     */
    private String invalidSecond;

    /**
     * 备注
     */
    private String remark;

    /**
     * did集合
     */
    private List<Long> didList;

    /**
     * 订单状态
     */
    private String orderStatusStr;

    /**
     * SQL条件
     */
    private String isReplyStr;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDid() {
        return did;
    }

    public void setDid(Long did) {
        this.did = did;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getOrderMoney() {
        return orderMoney;
    }

    public void setOrderMoney(String orderMoney) {
        this.orderMoney = orderMoney;
    }

    public Long getCollectionAccountId() {
        return collectionAccountId;
    }

    public void setCollectionAccountId(Long collectionAccountId) {
        this.collectionAccountId = collectionAccountId;
    }

    public Integer getCollectionType() {
        return collectionType;
    }

    public void setCollectionType(Integer collectionType) {
        this.collectionType = collectionType;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public String getWxNickname() {
        return wxNickname;
    }

    public void setWxNickname(String wxNickname) {
        this.wxNickname = wxNickname;
    }

    public Long getWxId() {
        return wxId;
    }

    public void setWxId(Long wxId) {
        this.wxId = wxId;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public Integer getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getInvalidTime() {
        return invalidTime;
    }

    public void setInvalidTime(String invalidTime) {
        this.invalidTime = invalidTime;
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

    public Integer getRunNum() {
        return runNum;
    }

    public void setRunNum(Integer runNum) {
        this.runNum = runNum;
    }

    public Integer getRunStatus() {
        return runStatus;
    }

    public void setRunStatus(Integer runStatus) {
        this.runStatus = runStatus;
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

    public Integer getCurdayStart() {
        return curdayStart;
    }

    public void setCurdayStart(Integer curdayStart) {
        this.curdayStart = curdayStart;
    }

    public Integer getCurdayEnd() {
        return curdayEnd;
    }

    public void setCurdayEnd(Integer curdayEnd) {
        this.curdayEnd = curdayEnd;
    }

    public String getAcName() {
        return acName;
    }

    public void setAcName(String acName) {
        this.acName = acName;
    }

    public String getAcNum() {
        return acNum;
    }

    public void setAcNum(String acNum) {
        this.acNum = acNum;
    }

    public String getProfit() {
        return profit;
    }

    public void setProfit(String profit) {
        this.profit = profit;
    }


    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public Long getQrCodeId() {
        return qrCodeId;
    }

    public void setQrCodeId(Long qrCodeId) {
        this.qrCodeId = qrCodeId;
    }

    public String getRatio() {
        return ratio;
    }

    public void setRatio(String ratio) {
        this.ratio = ratio;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProgramTime() {
        return programTime;
    }

    public void setProgramTime(String programTime) {
        this.programTime = programTime;
    }

    public Integer getDidStatus() {
        return didStatus;
    }

    public void setDidStatus(Integer didStatus) {
        this.didStatus = didStatus;
    }

    public String getDidTime() {
        return didTime;
    }

    public void setDidTime(String didTime) {
        this.didTime = didTime;
    }

    public String getZfbAcNum() {
        return zfbAcNum;
    }

    public void setZfbAcNum(String zfbAcNum) {
        this.zfbAcNum = zfbAcNum;
    }

    public String getInvalidSecond() {
        return invalidSecond;
    }

    public void setInvalidSecond(String invalidSecond) {
        this.invalidSecond = invalidSecond;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<Long> getDidList() {
        return didList;
    }

    public void setDidList(List<Long> didList) {
        this.didList = didList;
    }

    public String getOrderStatusStr() {
        return orderStatusStr;
    }

    public void setOrderStatusStr(String orderStatusStr) {
        this.orderStatusStr = orderStatusStr;
    }

    public Integer getIsRedPack() {
        return isRedPack;
    }

    public void setIsRedPack(Integer isRedPack) {
        this.isRedPack = isRedPack;
    }

    public String getRedPackTime() {
        return redPackTime;
    }

    public void setRedPackTime(String redPackTime) {
        this.redPackTime = redPackTime;
    }

    public Integer getIsReply() {
        return isReply;
    }

    public void setIsReply(Integer isReply) {
        this.isReply = isReply;
    }

    public String getReplyData() {
        return replyData;
    }

    public void setReplyData(String replyData) {
        this.replyData = replyData;
    }

    public String getReplyTime() {
        return replyTime;
    }

    public void setReplyTime(String replyTime) {
        this.replyTime = replyTime;
    }

    public String getActualMoney() {
        return actualMoney;
    }

    public void setActualMoney(String actualMoney) {
        this.actualMoney = actualMoney;
    }

    public Integer getMoneyFitType() {
        return moneyFitType;
    }

    public void setMoneyFitType(Integer moneyFitType) {
        this.moneyFitType = moneyFitType;
    }

    public Integer getReplenishType() {
        return replenishType;
    }

    public void setReplenishType(Integer replenishType) {
        this.replenishType = replenishType;
    }

    public String getIsReplyStr() {
        return isReplyStr;
    }

    public void setIsReplyStr(String isReplyStr) {
        this.isReplyStr = isReplyStr;
    }
}
