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
}
