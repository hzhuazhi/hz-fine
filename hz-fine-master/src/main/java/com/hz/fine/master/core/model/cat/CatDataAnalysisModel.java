package com.hz.fine.master.core.model.cat;


import com.hz.fine.master.core.protocol.page.BasePage;

import java.io.Serializable;

/**
 * @Description 可爱猫数据解析的实体属性Bean
 * @Author yoko
 * @Date 2020/7/21 19:14
 * @Version 1.0
 */
public class CatDataAnalysisModel extends BasePage implements Serializable {
    private static final long   serialVersionUID = 1203223201139L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 原始数据的ID：对应表tb_fn_cat_all_data的主键ID
     */
    private Long allId;

    /**
     * 归属小微管理的主键ID：对应表tb_fn_wx的主键ID
     */
    private Long wxId;

    /**
     * 归属用户ID：对应表tb_fn_did的主键ID
     */
    private Long did;

    /**
     * 用户收款账号ID：对应表tb_fn_did_collection_account的主键ID
     */
    private Long collectionAccountId;

    /**
     * 收款账号状态：1初始化，2没找到对应的收款账号，3账号被删除，4账号名称被修改，5正常状态的账号
     */
    private Integer collectionAccountType;

    /**
     * 订单号：订单状态如果成功，则把相对应的订单号录入更新进来
     */
    private String orderNo;

    /**
     * 订单金额
     */
    private String orderMoney;

    /**
     * 订单状态：1初始化，2超时/失败，3有质疑，4成功
     */
    private Integer orderStatus;

    /**
     * 用户成功收款上报的金额
     */
    private String money;

    /**
     * 订单超时时间_失效时间
     */
    private String invalidTime;

    /**
     * 支付用户发红包是否超时：1初始化，2已超时，3未超时
     */
    private Integer redPackInvalidType;

    /**
     * 金额是否与上报金额一致：1初始化，2少了，3多了，4一致
     */
    private Integer moneyFitType;

    /**
     * 回复成功or失败是否超时：1初始化，2已超时，3未超时
     */
    private Integer replyInvalidType;

    /**
     * 可爱猫的final_from_wxid
     */
    private String finalFromWxid;

    /**
     * 可爱猫from_name：数据来源于某某，比如来源于群的名字，来源于某个人
     */
    private String fromName;

    /**
     * 可爱猫final_from_name
     */
    private String finalFromName;

    /**
     * 可爱猫from_wxid
     */
    private String fromWxid;

    /**
     * 可爱猫msg具体信息
     */
    private String msg;

    /**
     * 可爱猫msg_type：信息类型
     */
    private String msgType;

    /**
     * 可爱猫robot_wxid的数据
     */
    private String robotWxid;

    /**
     * 可爱猫type
     */
    private String type;

    /**
     * 微信群group_wxid
     */
    private String groupWxid;

    /**
     * 微信群group_name：微信群名称
     */
    private String groupName;

    /**
     * 微信群guest：微信群里面的所有成员集合
     */
    private String guest;

    /**
     * 成员的微信ID-踢出群类型里面的字段
     */
    private String memberWxid;

    /**
     * 成员的微信昵称-踢出群类型里面的字段
     */
    private String memberNickname;

    /**
     * 数据类型：1初始化，2其它，3发送固定指令4表示审核使用，4加群信息，5发红包，6剔除成员，7成功收款，8收款失败，9订单完结
     */
    private Integer dataType;

    /**
     * 数据说明
     */
    private String dataExplain;

    /**
     * 是否能匹配到数据关系：1初始化，2匹配不成功，3匹配成功；根据我方wx_id加上微信群名匹配关联关系
     */
    private Integer isMatching;

    /**
     * 数据来源：1可爱猫，2新版微信端
     */
    private Integer dataFrom;

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
     * 补充数据的类型：1初始化，2补充数据失败（其它原因等..），3补充数据成功
     */
    private Integer workType;

    /**
     * 备注
     */
    private String remark;

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

    private String invalidTimeBig;
    private String invalidTimeSmall;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAllId() {
        return allId;
    }

    public void setAllId(Long allId) {
        this.allId = allId;
    }

    public Long getWxId() {
        return wxId;
    }

    public void setWxId(Long wxId) {
        this.wxId = wxId;
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

    public String getFinalFromWxid() {
        return finalFromWxid;
    }

    public void setFinalFromWxid(String finalFromWxid) {
        this.finalFromWxid = finalFromWxid;
    }

    public String getFromName() {
        return fromName;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    public String getFinalFromName() {
        return finalFromName;
    }

    public void setFinalFromName(String finalFromName) {
        this.finalFromName = finalFromName;
    }

    public String getFromWxid() {
        return fromWxid;
    }

    public void setFromWxid(String fromWxid) {
        this.fromWxid = fromWxid;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public String getRobotWxid() {
        return robotWxid;
    }

    public void setRobotWxid(String robotWxid) {
        this.robotWxid = robotWxid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getGroupWxid() {
        return groupWxid;
    }

    public void setGroupWxid(String groupWxid) {
        this.groupWxid = groupWxid;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGuest() {
        return guest;
    }

    public void setGuest(String guest) {
        this.guest = guest;
    }

    public Integer getDataType() {
        return dataType;
    }

    public void setDataType(Integer dataType) {
        this.dataType = dataType;
    }

    public Integer getIsMatching() {
        return isMatching;
    }

    public void setIsMatching(Integer isMatching) {
        this.isMatching = isMatching;
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

    public Integer getWorkType() {
        return workType;
    }

    public void setWorkType(Integer workType) {
        this.workType = workType;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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

    public String getMemberWxid() {
        return memberWxid;
    }

    public void setMemberWxid(String memberWxid) {
        this.memberWxid = memberWxid;
    }

    public String getMemberNickname() {
        return memberNickname;
    }

    public void setMemberNickname(String memberNickname) {
        this.memberNickname = memberNickname;
    }

    public Integer getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public Long getDid() {
        return did;
    }

    public void setDid(Long did) {
        this.did = did;
    }

    public Long getCollectionAccountId() {
        return collectionAccountId;
    }

    public void setCollectionAccountId(Long collectionAccountId) {
        this.collectionAccountId = collectionAccountId;
    }

    public Integer getCollectionAccountType() {
        return collectionAccountType;
    }

    public void setCollectionAccountType(Integer collectionAccountType) {
        this.collectionAccountType = collectionAccountType;
    }

    public String getInvalidTime() {
        return invalidTime;
    }

    public void setInvalidTime(String invalidTime) {
        this.invalidTime = invalidTime;
    }

    public Integer getRedPackInvalidType() {
        return redPackInvalidType;
    }

    public void setRedPackInvalidType(Integer redPackInvalidType) {
        this.redPackInvalidType = redPackInvalidType;
    }

    public Integer getMoneyFitType() {
        return moneyFitType;
    }

    public void setMoneyFitType(Integer moneyFitType) {
        this.moneyFitType = moneyFitType;
    }

    public Integer getReplyInvalidType() {
        return replyInvalidType;
    }

    public void setReplyInvalidType(Integer replyInvalidType) {
        this.replyInvalidType = replyInvalidType;
    }

    public String getDataExplain() {
        return dataExplain;
    }

    public void setDataExplain(String dataExplain) {
        this.dataExplain = dataExplain;
    }

    public String getInvalidTimeBig() {
        return invalidTimeBig;
    }

    public void setInvalidTimeBig(String invalidTimeBig) {
        this.invalidTimeBig = invalidTimeBig;
    }

    public String getInvalidTimeSmall() {
        return invalidTimeSmall;
    }

    public void setInvalidTimeSmall(String invalidTimeSmall) {
        this.invalidTimeSmall = invalidTimeSmall;
    }

    public Integer getDataFrom() {
        return dataFrom;
    }

    public void setDataFrom(Integer dataFrom) {
        this.dataFrom = dataFrom;
    }
}
