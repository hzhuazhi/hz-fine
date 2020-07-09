package com.hz.fine.master.core.model.consult;

import com.hz.fine.master.core.protocol.page.BasePage;

import java.io.Serializable;

/**
 * @Description 在线客服、咨询的发问的问答纪录的实体属性Bean
 * @Author yoko
 * @Date 2020/7/8 21:11
 * @Version 1.0
 */
public class ConsultAskReplyModel extends BasePage implements Serializable {
    private static final long   serialVersionUID = 1233223301151L;

    /**
     * 自增主键ID
     */
    private Long id;

    /**
     * 用户ID
     */
    private Long did;

    /**
     * 在线客服、咨询的发问表的主键ID：对应表tb_fn_consult_ask的主键ID
     */
    private Long consultAskId;

    /**
     * 标题-归属对应表tb_fn_consult_ask的主键ID表的标题
     */
    private String title;


    /**
     * 咨询描述_问_文字
     */
    private String askReply;

    /**
     * 问答_图片地址
     */
    private String askReplyAds;

    /**
     * 数据类型：1问，2回答
     */
    private Integer dataType;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 更新时间
     */
    private String updateTime;

    /**
     * 是否有效：0有效，1无效/删除
     */
    private Integer yn;


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

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getYn() {
        return yn;
    }

    public void setYn(Integer yn) {
        this.yn = yn;
    }

    public Long getDid() {
        return did;
    }

    public void setDid(Long did) {
        this.did = did;
    }
}
