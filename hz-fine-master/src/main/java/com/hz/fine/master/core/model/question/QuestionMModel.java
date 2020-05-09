package com.hz.fine.master.core.model.question;


import com.hz.fine.master.core.protocol.page.BasePage;

import java.io.Serializable;
import java.util.List;

/**
 * @Description 百问百答问题类别的Entity层-对应数据库所有字段
 * @Author yoko
 * @Date 2020/1/7 13:44
 * @Version 1.0
 */
public class QuestionMModel extends BasePage implements Serializable {
    private static final long   serialVersionUID = 1233223301144L;

    /**
     * 自增主键ID
     */
    private Long id;

    /**
     * 类别名称
     */
    private String categoryName;

    /**
     * 位置（顺序）
     */
    private Integer seatM;

    /**
     * 图标地址
     */
    private String iconAds;


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
     * 百问百答详情集合
     */
    private List<QuestionDModel> questionDList;


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

    public Integer getSeatM() {
        return seatM;
    }

    public void setSeatM(Integer seatM) {
        this.seatM = seatM;
    }

    public String getIconAds() {
        return iconAds;
    }

    public void setIconAds(String iconAds) {
        this.iconAds = iconAds;
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

    public List<QuestionDModel> getQuestionDList() {
        return questionDList;
    }

    public void setQuestionDList(List<QuestionDModel> questionDList) {
        this.questionDList = questionDList;
    }
}
