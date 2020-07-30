package com.hz.fine.master.core.protocol.response.wx;

import java.io.Serializable;

/**
 * @Description 协议：小微的属性字段
 * @Author yoko
 * @Date 2020/7/30 15:13
 * @Version 1.0
 */
public class Wx implements Serializable {
    private static final long   serialVersionUID = 1233023331064L;

    /**
     * 微信账号
     */
    public String acName;

    /**
     * 微信昵称
     */
    public String wxName;

    /**
     * 微信名片二维码地址
     */
    public String wxQrCode;


    public Wx(){

    }

    public String getAcName() {
        return acName;
    }

    public void setAcName(String acName) {
        this.acName = acName;
    }

    public String getWxName() {
        return wxName;
    }

    public void setWxName(String wxName) {
        this.wxName = wxName;
    }

    public String getWxQrCode() {
        return wxQrCode;
    }

    public void setWxQrCode(String wxQrCode) {
        this.wxQrCode = wxQrCode;
    }
}
