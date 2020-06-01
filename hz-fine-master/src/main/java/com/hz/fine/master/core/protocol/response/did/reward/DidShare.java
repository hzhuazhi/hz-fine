package com.hz.fine.master.core.protocol.response.did.reward;

import java.io.Serializable;

/**
 * @Description 协议：用户分享奖励
 * @Author yoko
 * @Date 2020/6/1 17:29
 * @Version 1.0
 */
public class DidShare implements Serializable {
    private static final long   serialVersionUID = 1233023331060L;

    /**
     * 直推奖励收益
     */
    public String profit;

    /**
     * 直推人的昵称
     */
    public String nickname;

    /**
     * 直推人的登录账号
     */
    public String acNum;

    /**
     * 直推时间
     */
    public String shareTime;

    public DidShare(){

    }

    public String getProfit() {
        return profit;
    }

    public void setProfit(String profit) {
        this.profit = profit;
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

    public String getShareTime() {
        return shareTime;
    }

    public void setShareTime(String shareTime) {
        this.shareTime = shareTime;
    }
}
