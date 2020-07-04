package com.hz.fine.master.core.protocol.response.upgrade;



import com.hz.fine.master.core.protocol.base.BaseResponse;

import java.io.Serializable;

/**
 * @Description 协议：客户端更新
 * @Author yoko
 * @Date 2020/1/14 15:16
 * @Version 1.0
 */
public class ResponseUpgrade extends BaseResponse implements Serializable {
    private static final long   serialVersionUID = 1233023331141L;
    public Integer clientVer;
    public Integer clientType;
    public String md5Value;
    public String resUrl;
    public Integer upType;
    public String content;
    public String showVer;

    public ResponseUpgrade(){

    }

    public Integer getClientVer() {
        return clientVer;
    }

    public void setClientVer(Integer clientVer) {
        this.clientVer = clientVer;
    }

    public Integer getClientType() {
        return clientType;
    }

    public void setClientType(Integer clientType) {
        this.clientType = clientType;
    }

    public String getMd5Value() {
        return md5Value;
    }

    public void setMd5Value(String md5Value) {
        this.md5Value = md5Value;
    }

    public String getResUrl() {
        return resUrl;
    }

    public void setResUrl(String resUrl) {
        this.resUrl = resUrl;
    }

    public Integer getUpType() {
        return upType;
    }

    public void setUpType(Integer upType) {
        this.upType = upType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getShowVer() {
        return showVer;
    }

    public void setShowVer(String showVer) {
        this.showVer = showVer;
    }
}
