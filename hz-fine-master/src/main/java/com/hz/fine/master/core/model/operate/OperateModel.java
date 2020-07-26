package com.hz.fine.master.core.model.operate;


import com.hz.fine.master.core.protocol.page.BasePage;

import java.io.Serializable;

/**
 * @Description 运营的实体属性Bean
 * @Author yoko
 * @Date 2020/7/23 14:05
 * @Version 1.0
 */
public class OperateModel extends BasePage implements Serializable {
    private static final long   serialVersionUID = 1203223202139L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 对应可爱猫数据解析的主键ID：对应表tb_fn_cat_data_analysis的主键ID
     */
    private Long analysisId;

    /**
     * 用户ID
     */
    private Long did;

    /**
     * 归属小微管理的主键ID：对应表tb_fn_wx的主键ID
     */
    private Long wxId;

    /**
     * 用户收款账号ID：对应表tb_fn_did_collection_account的主键ID
     */
    private Long collectionAccountId;

    /**
     * 微信群ID
     */
    private String groupWxid;

    /**
     * 微信群名称
     */
    private String groupName;

    /**
     * 微信原始账号ID
     */
    private String userId;

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
     * 处罚类型：1不处罚，2处罚
     */
    private Integer punishType;

    /**
     * 处罚金额
     */
    private String punishMoney;

    /**
     * 数据类型：1初始化，2其它，3加群
     */
    private Integer dataType;

    /**
     * 数据说明
     */
    private String dataExplain;

    /**
     * 数据处理状态：1初始化，2处理中，3处理失败，4处理成功
     */
    private Integer isOk;

    /**
     * 是否需要操作完毕才能派单类型：1需要处理完毕，2不需要处理完毕；此数据需要处理成功，才能给此用户进行派单
     */
    private Integer endType;

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

    /**
     * 查询SQL的条件
     */
    private Integer notOk;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAnalysisId() {
        return analysisId;
    }

    public void setAnalysisId(Long analysisId) {
        this.analysisId = analysisId;
    }

    public Long getDid() {
        return did;
    }

    public void setDid(Long did) {
        this.did = did;
    }

    public Long getWxId() {
        return wxId;
    }

    public void setWxId(Long wxId) {
        this.wxId = wxId;
    }

    public Long getCollectionAccountId() {
        return collectionAccountId;
    }

    public void setCollectionAccountId(Long collectionAccountId) {
        this.collectionAccountId = collectionAccountId;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public Integer getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Integer getPunishType() {
        return punishType;
    }

    public void setPunishType(Integer punishType) {
        this.punishType = punishType;
    }

    public String getPunishMoney() {
        return punishMoney;
    }

    public void setPunishMoney(String punishMoney) {
        this.punishMoney = punishMoney;
    }

    public Integer getDataType() {
        return dataType;
    }

    public void setDataType(Integer dataType) {
        this.dataType = dataType;
    }

    public String getDataExplain() {
        return dataExplain;
    }

    public void setDataExplain(String dataExplain) {
        this.dataExplain = dataExplain;
    }

    public Integer getIsOk() {
        return isOk;
    }

    public void setIsOk(Integer isOk) {
        this.isOk = isOk;
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

    public Integer getEndType() {
        return endType;
    }

    public void setEndType(Integer endType) {
        this.endType = endType;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public Integer getNotOk() {
        return notOk;
    }

    public void setNotOk(Integer notOk) {
        this.notOk = notOk;
    }
}
