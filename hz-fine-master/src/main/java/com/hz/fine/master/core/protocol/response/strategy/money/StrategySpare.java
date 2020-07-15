package com.hz.fine.master.core.protocol.response.strategy.money;

import java.io.Serializable;

/**
 * @Description 备用域名地址
 * @Author yoko
 * @Date 2020/7/13 17:48
 * @Version 1.0
 */
public class StrategySpare implements Serializable {
    private static final long   serialVersionUID = 1233023331070L;

    /**
     * 域名加端口号
     */
    public String spareAds;

    /**
     * check校验接口
     */
    public String spareInterface;

    public StrategySpare(){

    }

    public String getSpareAds() {
        return spareAds;
    }

    public void setSpareAds(String spareAds) {
        this.spareAds = spareAds;
    }

    public String getSpareInterface() {
        return spareInterface;
    }

    public void setSpareInterface(String spareInterface) {
        this.spareInterface = spareInterface;
    }
}
