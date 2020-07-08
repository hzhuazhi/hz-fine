package com.hz.fine.master.core.protocol.response.strategy.money;

import java.io.Serializable;

/**
 * @Description 团队日派单消耗成功累计总额奖励规则的实体属性Bean
 * @Author yoko
 * @Date 2020/7/8 14:24
 * @Version 1.0
 */
public class StrategyTeamConsumeReward implements Serializable {
    private static final long   serialVersionUID = 1263023331060L;

    /**
     * 金额档次
     * 多少钱到多少钱
     */
    public String moneyGrade;

    /**
     * 奖励的比例/奖励的规则
     */
    public String rewardRatio;

    /**
     * 位置：显示的顺序
     */
    public Integer seat;

    public StrategyTeamConsumeReward(){

    }

    public String getMoneyGrade() {
        return moneyGrade;
    }

    public void setMoneyGrade(String moneyGrade) {
        this.moneyGrade = moneyGrade;
    }

    public String getRewardRatio() {
        return rewardRatio;
    }

    public void setRewardRatio(String rewardRatio) {
        this.rewardRatio = rewardRatio;
    }

    public Integer getSeat() {
        return seat;
    }

    public void setSeat(Integer seat) {
        this.seat = seat;
    }
}
