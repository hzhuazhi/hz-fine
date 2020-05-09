package com.hz.fine.master.core.protocol.base;



import com.hz.fine.master.core.protocol.page.BasePage;

import java.io.Serializable;

/**
 * @Description 客户端请求的基础类
 * @Author yoko
 * @Date 2019/11/6 17:43
 * @Version 1.0
 */
public class BaseRequest extends BasePage implements Serializable {
    private static final long   serialVersionUID = 1233223331148L;

    public Integer agtVer;
    public String androidVer;
    public Integer clientVer;
    public Integer clientType;
    public Long ctime;
    public Long cctime;
    public String sign;
    public String token;

    public BaseRequest(){

    }

    public Integer getAgtVer() {
        return agtVer;
    }

    public void setAgtVer(Integer agtVer) {
        this.agtVer = agtVer;
    }

    public String getAndroidVer() {
        return androidVer;
    }

    public void setAndroidVer(String androidVer) {
        this.androidVer = androidVer;
    }

    public Integer getClientVer() {
        return clientVer;
    }

    public void setClientVer(Integer clientVer) {
        this.clientVer = clientVer;
    }

    public Long getCtime() {
        return ctime;
    }

    public void setCtime(Long ctime) {
        this.ctime = ctime;
    }

    public Long getCctime() {
        return cctime;
    }

    public void setCctime(Long cctime) {
        this.cctime = cctime;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Integer getClientType() {
        return clientType;
    }

    public void setClientType(Integer clientType) {
        this.clientType = clientType;
    }

}
