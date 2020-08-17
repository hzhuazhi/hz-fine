package com.hz.fine.master.core.model.question;

import com.hz.fine.master.core.protocol.page.BasePage;

import java.io.Serializable;

/**
 * @Description 百问百答问题-详情-步骤的实体属性Bean
 * @Author yoko
 * @Date 2020/7/6 14:14
 * @Version 1.0
 */
public class QuestionDDModel extends BasePage implements Serializable {
    private static final long   serialVersionUID = 1233223301144L;

    /**
     * 自增主键ID
     */
    private Long id;

    /**
     * 问题详情的主键ID：对应表tb_ga_question_d的主键ID
     */
    private Long questionDId;

    /**
     * 类别名称
     */
    private String categoryName;

    /**
     * 位置（顺序）
     */
    private Integer seatDD;

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
     * 数据类型：1文字或图片数据，2视频数据
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

    public Long getQuestionDId() {
        return questionDId;
    }

    public void setQuestionDId(Long questionDId) {
        this.questionDId = questionDId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Integer getSeatDD() {
        return seatDD;
    }

    public void setSeatDD(Integer seatDD) {
        this.seatDD = seatDD;
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

    public Integer getDataType() {
        return dataType;
    }

    public void setDataType(Integer dataType) {
        this.dataType = dataType;
    }
}
