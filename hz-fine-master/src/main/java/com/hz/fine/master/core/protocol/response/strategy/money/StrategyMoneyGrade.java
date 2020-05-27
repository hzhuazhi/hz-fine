package com.hz.fine.master.core.protocol.response.strategy.money;

import java.io.Serializable;

/**
 * @Description 策略：总金额充值档次奖励
 * @Author yoko
 * @Date 2020/5/27 18:56
 * @Version 1.0
 */
public class StrategyMoneyGrade implements Serializable {
    private static final long   serialVersionUID = 1233023331061L;

    /**
     * 充值总金额的档次
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

    public StrategyMoneyGrade(){

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
