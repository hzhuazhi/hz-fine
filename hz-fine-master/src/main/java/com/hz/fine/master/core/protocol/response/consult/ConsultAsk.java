package com.hz.fine.master.core.protocol.response.consult;

import java.io.Serializable;

/**
 * @Description 协议：在线客服、咨询的发问的属性
 * @Author yoko
 * @Date 2020/7/8 20:31
 * @Version 1.0
 */
public class ConsultAsk implements Serializable {
    public static final long   serialVersionUID = 2233023331146L;

    /**
     * 自增主键ID
     */
    public Long id;

    /**
     * 咨询类别名称
     */
    public String categoryName;

    /**
     * 咨询描述_问_文字
     */
    public String ask;

    /**
     * 咨询描述_问_图片地址
     */
    public String askAds;

    /**
     * 回复的状态：1初始化，2回答完毕
     */
    public Integer replyStatus;

    /**
     * 创建时间
     */
    public String createTime;

    public ConsultAsk(){

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
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

    public Integer getReplyStatus() {
        return replyStatus;
    }

    public void setReplyStatus(Integer replyStatus) {
        this.replyStatus = replyStatus;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
