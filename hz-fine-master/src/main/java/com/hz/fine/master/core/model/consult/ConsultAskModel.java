package com.hz.fine.master.core.model.consult;

import com.hz.fine.master.core.protocol.page.BasePage;

import java.io.Serializable;
import java.util.List;

/**
 * @Description 在线客服、咨询的发问的的实体属性Bean
 * @Author yoko
 * @Date 2020/7/7 10:15
 * @Version 1.0
 */
public class ConsultAskModel extends BasePage implements Serializable {
    private static final long   serialVersionUID = 1233223301151L;

    /**
     * 自增主键ID
     */
    private Long id;

    /**
     * 咨询类别的主键ID：对应表tb_fn_consult的主键ID
     */
    private Long consultId;

    /**
     * 咨询类别名称
     */
    private String categoryName;

    /**
     * 标题
     */
    private String title;

    /**
     * 归属用户ID：对应表tb_fn_did的主键ID
     */
    private Long did;

    /**
     * 咨询描述_问_文字
     */
    private String ask;

    /**
     * 咨询描述_问_图片地址
     */
    private String askAds;

    /**
     * 回复的状态：1初始化，2回答完毕
     */
    private Integer replyStatus;

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

    /**
     * 追加问答的数据集合
     */
    private List<ConsultAskReplyModel> askReplyList;


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

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Long getDid() {
        return did;
    }

    public void setDid(Long did) {
        this.did = did;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<ConsultAskReplyModel> getAskReplyList() {
        return askReplyList;
    }

    public void setAskReplyList(List<ConsultAskReplyModel> askReplyList) {
        this.askReplyList = askReplyList;
    }
}
