package com.hz.fine.master.core.protocol.response.did.reward;

import java.io.Serializable;

/**
 * @Description 用户奖励纪录的具体属性
 * @Author yoko
 * @Date 2020/5/21 17:48
 * @Version 1.0
 */
public class DidReward implements Serializable {
    private static final long   serialVersionUID = 1233023331042L;

    /**
     * 主键ID
     */
    public Long id;

    /**
     * 订单号
     */
    public String orderNo;

    /**
     * 奖励的金额
     */
    public String money;

    /**
     * 奖励类型：1充值奖励，2充值总金额档次奖励，3直推奖励，4裂变奖励
     */
    public Integer rewardType;

    /**
     * 奖励的依据：当奖励类型等于1时此值为充值订单金额的值，2时则是充值总金额档次的具体值，3时则是用户did/用户昵称，4时则是用户did/用户昵称
     */
    public String proof;

    /**
     * 奖励来由值：是所有奖励的订单充值的金额
     */
    public String origin;


    /**
     * 创建时间
     */
    public String createTime;

    /**
     * 创建日期-Int
     */
    public Integer curday;

    /**
     * 创建小时
     */
    public Integer curhour;

    /**
     * 创建分钟
     */
    public Integer curminute;

    public DidReward(){

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
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
}
