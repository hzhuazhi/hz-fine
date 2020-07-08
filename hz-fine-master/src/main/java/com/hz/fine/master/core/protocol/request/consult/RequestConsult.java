package com.hz.fine.master.core.protocol.request.consult;

import com.hz.fine.master.core.protocol.base.BaseRequest;

import java.io.Serializable;

/**
 * @Description 协议：在线客服、咨询
 * @Author yoko
 * @Date 2020/7/6 13:44
 * @Version 1.0
 */
public class RequestConsult extends BaseRequest implements Serializable {
    private static final long   serialVersionUID = 2233283332140L;

    public Long id;

    /**
     * 咨询类别的主键ID
     */
    public Long consultId;

    /**
     * 标题
     */
    public String title;

    /**
     * 咨询描述_问_文字
     */
    public String ask;

    /**
     * 咨询描述_问_图片地址
     */
    public String askAds;

    public RequestConsult(){

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getConsultId() {
        return consultId;
    }

    public void setConsultId(Long consultId) {
        this.consultId = consultId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAsk() {
        return ask;
    }

    public void setAsk(String ask) {
        this.ask = ask;
    }

    public String getAskAds() {
        return askAds;
    }

    public void setAskAds(String askAds) {
        this.askAds = askAds;
    }
}
