package com.hz.fine.master.core.protocol.response.strategy;

import com.hz.fine.master.core.protocol.base.BaseResponse;
import com.hz.fine.master.core.protocol.response.did.reward.DidReward;
import com.hz.fine.master.core.protocol.response.strategy.money.StrategyMoney;
import com.hz.fine.master.core.protocol.response.strategy.money.StrategyMoneyGrade;
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
    public QiNiu qiNiu;// 获取七牛的token
    public StrategyShare share;// 分享地址


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
}
