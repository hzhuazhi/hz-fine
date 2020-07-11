package com.hz.fine.master.core.protocol.response.did.basic;

import java.io.Serializable;

/**
 * @Description 协议：用户的基本信息
 * @Author yoko
 * @Date 2020/5/29 14:39
 * @Version 1.0
 */
public class DidBasic implements Serializable {
    private static final long   serialVersionUID = 1233023331149L;

    /**
     * 余额
     */
    public String balance;

    /**
     * 锁定/冻结金额
     */
    public String lockMoney;

    /**
     * 总金额（累计充值）
     */
    public String totalMoney;

    /**
     * 总收益
     */
    public String totalProfit;

    /**
     * 今日收益
     * 今日收益：充多少送多少的收益，充值档次的赠送，昨天直推的奖励
     */
    public String todayProfit;

    /**
     * 今日兑换
     * 今日兑换：今日派发订单成功的
     */
    public String todayExchange;

    /**
     * 团队长今日旗下总消耗成功的金额
     */
    public String todayTeamConsume;

    /**
     * 触发额度奖励总收益
     */
    public String totalTriggerQuotaProfit;

    /**
     * 团队总额等级奖励总收益
     */
    public String totalTeamConsumeCumulativeProfit;

    /**
     * 账号昵称
     */
    public String nickname;

    /**
     * 登录账号
     */
    public String acNum;

    /**
     * 邀请码
     */
    public String icode;

    /**
     * 直推总人数
     */
    public Integer totalDirectNum;

    /**
     * 直推总奖励收益
     */
    public String totalDirectProfit;

    /**
     * 团队总收益
     */
    public String totalTeamProfit;

    /**
     * 充值总收益：充多少送多少
     */
    public String totalRechargeProfit;

    /**
     * 档次总收益：达到的档次之后的总奖励
     */
    public String totalGradeProfit;

    /**
     * 消耗总收益：派发订单成功之后消耗的奖励
     */
    public String totalConsumeProfit;

    /**
     * 团队消耗总收益：团队长旗下消耗总金额比例规则奖励
     */
    public String totalTeamConsumeProfit;

    /**
     * 是否是团队长：1不是团队长，2是团队长
     */
    public Integer isTeam;

    public DidBasic(){

    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getLockMoney() {
        return lockMoney;
    }

    public void setLockMoney(String lockMoney) {
        this.lockMoney = lockMoney;
    }

    public String getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(String totalMoney) {
        this.totalMoney = totalMoney;
    }

    public String getTotalProfit() {
        return totalProfit;
    }

    public void setTotalProfit(String totalProfit) {
        this.totalProfit = totalProfit;
    }

    public String getTodayProfit() {
        return todayProfit;
    }

    public void setTodayProfit(String todayProfit) {
        this.todayProfit = todayProfit;
    }

    public String getTodayExchange() {
        return todayExchange;
    }

    public void setTodayExchange(String todayExchange) {
        this.todayExchange = todayExchange;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAcNum() {
        return acNum;
    }

    public void setAcNum(String acNum) {
        this.acNum = acNum;
    }

    public String getIcode() {
        return icode;
    }

    public void setIcode(String icode) {
        this.icode = icode;
    }

    public Integer getTotalDirectNum() {
        return totalDirectNum;
    }

    public void setTotalDirectNum(Integer totalDirectNum) {
        this.totalDirectNum = totalDirectNum;
    }

    public String getTotalDirectProfit() {
        return totalDirectProfit;
    }

    public void setTotalDirectProfit(String totalDirectProfit) {
        this.totalDirectProfit = totalDirectProfit;
    }

    public String getTotalRechargeProfit() {
        return totalRechargeProfit;
    }

    public void setTotalRechargeProfit(String totalRechargeProfit) {
        this.totalRechargeProfit = totalRechargeProfit;
    }

    public String getTotalGradeProfit() {
        return totalGradeProfit;
    }

    public void setTotalGradeProfit(String totalGradeProfit) {
        this.totalGradeProfit = totalGradeProfit;
    }

    public String getTotalTeamProfit() {
        return totalTeamProfit;
    }

    public void setTotalTeamProfit(String totalTeamProfit) {
        this.totalTeamProfit = totalTeamProfit;
    }

    public String getTotalConsumeProfit() {
        return totalConsumeProfit;
    }

    public void setTotalConsumeProfit(String totalConsumeProfit) {
        this.totalConsumeProfit = totalConsumeProfit;
    }

    public String getTotalTeamConsumeProfit() {
        return totalTeamConsumeProfit;
    }

    public void setTotalTeamConsumeProfit(String totalTeamConsumeProfit) {
        this.totalTeamConsumeProfit = totalTeamConsumeProfit;
    }


    public String getTodayTeamConsume() {
        return todayTeamConsume;
    }

    public void setTodayTeamConsume(String todayTeamConsume) {
        this.todayTeamConsume = todayTeamConsume;
    }

    public String getTotalTriggerQuotaProfit() {
        return totalTriggerQuotaProfit;
    }

    public void setTotalTriggerQuotaProfit(String totalTriggerQuotaProfit) {
        this.totalTriggerQuotaProfit = totalTriggerQuotaProfit;
    }

    public String getTotalTeamConsumeCumulativeProfit() {
        return totalTeamConsumeCumulativeProfit;
    }

    public void setTotalTeamConsumeCumulativeProfit(String totalTeamConsumeCumulativeProfit) {
        this.totalTeamConsumeCumulativeProfit = totalTeamConsumeCumulativeProfit;
    }

    public Integer getIsTeam() {
        return isTeam;
    }

    public void setIsTeam(Integer isTeam) {
        this.isTeam = isTeam;
    }
}
