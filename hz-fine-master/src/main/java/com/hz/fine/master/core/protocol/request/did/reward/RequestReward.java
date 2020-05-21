package com.hz.fine.master.core.protocol.request.did.reward;

import com.hz.fine.master.core.protocol.base.BaseRequest;

import java.io.Serializable;

/**
 * @Description 协议：用户奖励纪录
 * @Author yoko
 * @Date 2020/5/21 17:42
 * @Version 1.0
 */
public class RequestReward extends BaseRequest implements Serializable {
    private static final long   serialVersionUID = 1233283332511L;

    /**
     * 主键ID
     */
    public Long id;

    /**
     * 奖励类型：1充值奖励，2充值总金额档次奖励，3直推奖励，4裂变奖励
     */
    public Integer rewardType;

    /**
     * 根据日期查询-开始时间
     */
    public Integer curdayStart;

    /**
     * 根据日期查询-结束时间
     */
    public Integer curdayEnd;


    public RequestReward(){

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getRewardType() {
        return rewardType;
    }

    public void setRewardType(Integer rewardType) {
        this.rewardType = rewardType;
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
}
