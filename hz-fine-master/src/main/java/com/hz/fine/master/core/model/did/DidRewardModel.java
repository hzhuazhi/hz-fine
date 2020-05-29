package com.hz.fine.master.core.model.did;

import com.hz.fine.master.core.protocol.page.BasePage;

import java.io.Serializable;
import java.util.List;

/**
 * @Description 用户奖励纪录的实体属性Bean
 * @Author yoko
 * @Date 2020/5/21 17:18
 * @Version 1.0
 */
public class DidRewardModel extends BasePage implements Serializable {
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
     * 奖励的金额
     */
    private String money;

    /**
     * 奖励类型：1充值奖励，2充值总金额档次奖励，3直推奖励，4裂变奖励
     */
    private Integer rewardType;

    /**
     * 奖励的依据：当奖励类型等于1时此值为充值订单金额的值，2时则是充值总金额档次的具体值，3时则是用户did，4时则是用户did
     */
    private String proof;

    /**
     * 奖励来由值：是所有奖励的订单充值的金额
     */
    private String origin;


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
     * 收益
     */
    private String profit;

    /**
     * 类型集合
     */
    private List<Integer> rewardTypeList;

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

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public Integer getRewardType() {
        return rewardType;
    }

    public void setRewardType(Integer rewardType) {
        this.rewardType = rewardType;
    }

    public String getProof() {
        return proof;
    }

    public void setProof(String proof) {
        this.proof = proof;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
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

    public String getProfit() {
        return profit;
    }

    public void setProfit(String profit) {
        this.profit = profit;
    }

    public List<Integer> getRewardTypeList() {
        return rewardTypeList;
    }

    public void setRewardTypeList(List<Integer> rewardTypeList) {
        this.rewardTypeList = rewardTypeList;
    }
}
