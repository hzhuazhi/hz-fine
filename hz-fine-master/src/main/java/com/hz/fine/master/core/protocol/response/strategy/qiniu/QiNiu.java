package com.hz.fine.master.core.protocol.response.strategy.qiniu;

import java.io.Serializable;

/**
 * @Description 协议：策略获取七牛的token
 * @Author yoko
 * @Date 2020/5/27 19:57
 * @Version 1.0
 */
public class QiNiu implements Serializable {
    private static final long   serialVersionUID = 1233023331062L;

    public String token;
    public String key;

    public String url;


    public QiNiu(){

    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
