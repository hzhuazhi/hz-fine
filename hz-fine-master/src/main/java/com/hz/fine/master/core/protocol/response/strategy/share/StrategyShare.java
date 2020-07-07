package com.hz.fine.master.core.protocol.response.strategy.share;

import java.io.Serializable;

/**
 * @Description 协议：策略里面的分享的属性
 * @Author yoko
 * @Date 2020/5/28 14:02
 * @Version 1.0
 */
public class StrategyShare implements Serializable {
    private static final long   serialVersionUID = 1233023331064L;

    /**
     * 分享地址
     */
    public String shareAddress;

    /**
     * 邀请码
     */
    public String icode;

    /**
     * 分享状态的开关：1表示打开，2表示关闭
     */
    public Integer shareSwitch;


    public StrategyShare(){

    }

    public String getShareAddress() {
        return shareAddress;
    }

    public void setShareAddress(String shareAddress) {
        this.shareAddress = shareAddress;
    }

    public String getIcode() {
        return icode;
    }

    public void setIcode(String icode) {
        this.icode = icode;
    }

    public Integer getShareSwitch() {
        return shareSwitch;
    }

    public void setShareSwitch(Integer shareSwitch) {
        this.shareSwitch = shareSwitch;
    }
}
