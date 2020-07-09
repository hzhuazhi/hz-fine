package com.hz.fine.master.core.protocol.response.consult;

import java.io.Serializable;

/**
 * @Description 协议：在线客服、咨询的发问的问答纪录
 * @Author yoko
 * @Date 2020/7/9 14:04
 * @Version 1.0
 */
public class ConsultAskReply implements Serializable {
    public static final long   serialVersionUID = 2233023331147L;

    /**
     * 自增主键ID
     */
    public Long id;

    /**
     * 在线客服、咨询的发问表的主键ID：对应表tb_fn_consult_ask的主键ID
     */
    public Long consultAskId;

    /**
     * 标题-归属在线客服、咨询的发问里面的标题；
     * 对应表tb_fn_consult_ask的表的标题
     */
    public String title;

    /**
     * 问_文字-归属在线客服、咨询的发问里面的咨询描述_问_文字；
     */
    public String ask;


    /**
     * 咨询描述_问答_文字
     */
    public String askReply;

    /**
     * 问答_图片地址
     */
    public String askReplyAds;

    /**
     * 数据类型：1问，2回答
     */
    public Integer dataType;
    
    public ConsultAskReply(){
        
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getConsultAskId() {
        return consultAskId;
    }

    public void setConsultAskId(Long consultAskId) {
        this.consultAskId = consultAskId;
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

    public String getAskReply() {
        return askReply;
    }

    public void setAskReply(String askReply) {
        this.askReply = askReply;
    }

    public String getAskReplyAds() {
        return askReplyAds;
    }

    public void setAskReplyAds(String askReplyAds) {
        this.askReplyAds = askReplyAds;
    }

    public Integer getDataType() {
        return dataType;
    }

    public void setDataType(Integer dataType) {
        this.dataType = dataType;
    }
}
