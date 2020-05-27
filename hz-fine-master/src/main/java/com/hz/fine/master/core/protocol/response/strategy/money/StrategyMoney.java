package com.hz.fine.master.core.protocol.response.strategy.money;

import java.io.Serializable;

/**
 * @Description 策略：充值金额列表
 * @Author yoko
 * @Date 2020/5/27 18:51
 * @Version 1.0
 */
public class StrategyMoney implements Serializable {
    private static final long   serialVersionUID = 1233023331060L;

    /**
     * 金额
     */
    public String money;

    /**
     * 奖励的比例/奖励的规则
     */
    public String rewardRatio;

    /**
     * 位置：显示的顺序
     */
    public Integer seat;

    public StrategyMoney(){

    }


    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
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
