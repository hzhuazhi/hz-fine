package com.hz.fine.master.core.protocol.response.strategy;

import com.hz.fine.master.core.protocol.base.BaseResponse;
import com.hz.fine.master.core.protocol.response.did.reward.DidReward;
import com.hz.fine.master.core.protocol.response.strategy.instruct.StrategyInstruct;
import com.hz.fine.master.core.protocol.response.strategy.money.StrategyMoney;
import com.hz.fine.master.core.protocol.response.strategy.money.StrategyMoneyGrade;
import com.hz.fine.master.core.protocol.response.strategy.money.StrategySpare;
import com.hz.fine.master.core.protocol.response.strategy.money.StrategyTeamConsumeReward;
import com.hz.fine.master.core.protocol.response.strategy.qiniu.QiNiu;
import com.hz.fine.master.core.protocol.response.strategy.share.StrategyShare;

import java.io.Serializable;
import java.util.List;

/**
 * @Description 协议：策略数据
 * @Author yoko
 * @Date 2020/5/27 18:49
 * @Version 1.0
 */
public class ResponseStrategy extends BaseResponse implements Serializable {
    private static final long   serialVersionUID = 1233023131151L;


    public List<StrategyMoney> moneyList;// 充值金额列表
    public List<StrategyMoneyGrade> moneyGradeList;// 总金额充值档次奖励列表
    public List<StrategyTeamConsumeReward> teamConsumeRewardList;// 团队日派单消耗成功累计总额奖励规则
    public List<StrategySpare> spareList;//  备用域名地址列表
    public QiNiu qiNiu;// 获取七牛的token
    public StrategyShare share;// 分享地址
    public StrategyInstruct instruct;// 微信群回复指令


    public Integer rowCount;



    public ResponseStrategy(){

    }


    public List<StrategyMoney> getMoneyList() {
        return moneyList;
    }

    public void setMoneyList(List<StrategyMoney> moneyList) {
        this.moneyList = moneyList;
    }

    public List<StrategyMoneyGrade> getMoneyGradeList() {
        return moneyGradeList;
    }

    public void setMoneyGradeList(List<StrategyMoneyGrade> moneyGradeList) {
        this.moneyGradeList = moneyGradeList;
    }

    @Override
    public Integer getRowCount() {
        return rowCount;
    }

    @Override
    public void setRowCount(Integer rowCount) {
        this.rowCount = rowCount;
    }

    public QiNiu getQiNiu() {
        return qiNiu;
    }

    public void setQiNiu(QiNiu qiNiu) {
        this.qiNiu = qiNiu;
    }

    public StrategyShare getShare() {
        return share;
    }

    public void setShare(StrategyShare share) {
        this.share = share;
    }

    public List<StrategyTeamConsumeReward> getTeamConsumeRewardList() {
        return teamConsumeRewardList;
    }

    public void setTeamConsumeRewardList(List<StrategyTeamConsumeReward> teamConsumeRewardList) {
        this.teamConsumeRewardList = teamConsumeRewardList;
    }

    public List<StrategySpare> getSpareList() {
        return spareList;
    }

    public void setSpareList(List<StrategySpare> spareList) {
        this.spareList = spareList;
    }

    public StrategyInstruct getInstruct() {
        return instruct;
    }

    public void setInstruct(StrategyInstruct instruct) {
        this.instruct = instruct;
    }
}
