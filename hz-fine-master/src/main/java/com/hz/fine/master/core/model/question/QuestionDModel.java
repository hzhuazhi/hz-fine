package com.hz.fine.master.core.model.question;


import com.hz.fine.master.core.protocol.page.BasePage;

import java.io.Serializable;

/**
 * @Description 百问百答问题-详情-的Entity层-对应数据库所有字段
 * @Author yoko
 * @Date 2020/1/7 14:09
 * @Version 1.0
 */
public class QuestionDModel extends BasePage implements Serializable {
    private static final long   serialVersionUID = 1233223301144L;

    /**
     * 自增主键ID
     */
    private Long id;

    /**
     * 类别的主键ID：对应表tb_ga_question_m的主键ID
     */
    private Long questionMId;

    /**
     * 类别名称
     */
    private String categoryName;

    /**
     * 位置（顺序）
     */
    private Integer seatD;

    /**
     * 标题
     */
    private String title;

    /**
     * 简述：简单描述
     */
    private String sketch;

    /**
     * 问题详情的页面地址
     */
    private String pageAds;

    /**
     * 被搜索关键字：多个关键字以英文逗号分割
     */
    private String keyword;


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
     * 搜索的输入条件
     */
    private String searchKey;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getQuestionMId() {
        return questionMId;
    }

    public void setQuestionMId(Long questionMId) {
        this.questionMId = questionMId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Integer getSeatD() {
        return seatD;
    }

    public void setSeatD(Integer seatD) {
        this.seatD = seatD;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSketch() {
        return sketch;
    }

    public void setSketch(String sketch) {
        this.sketch = sketch;
    }

    public String getPageAds() {
        return pageAds;
    }

    public void setPageAds(String pageAds) {
        this.pageAds = pageAds;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
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

    public String getSearchKey() {
        return searchKey;
    }

    public void setSearchKey(String searchKey) {
        this.searchKey = searchKey;
    }
}
