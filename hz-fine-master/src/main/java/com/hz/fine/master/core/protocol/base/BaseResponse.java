package com.hz.fine.master.core.protocol.base;


import com.hz.fine.master.core.protocol.page.Page;

/**
 * @Description 服务的响应给客户端的基础类
 * @Author yoko
 * @Date 2019/11/6 17:44
 * @Version 1.0
 */
public class BaseResponse extends Page {
    public Integer agtVer;
    public String sign;
    public Long stime;
    public String token;

    public Integer getAgtVer() {
        return agtVer;
    }

    public void setAgtVer(Integer agtVer) {
        this.agtVer = agtVer;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public Long getStime() {
        return stime;
    }

    public void setStime(Long stime) {
        this.stime = stime;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
