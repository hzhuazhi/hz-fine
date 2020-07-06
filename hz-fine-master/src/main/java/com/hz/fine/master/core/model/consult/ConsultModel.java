package com.hz.fine.master.core.model.consult;

import com.hz.fine.master.core.protocol.page.BasePage;

import java.io.Serializable;

/**
 * @Description 在线客服、咨询的实体属性Bean
 * @Author yoko
 * @Date 2020/7/6 11:23
 * @Version 1.0
 */
public class ConsultModel extends BasePage implements Serializable {
    private static final long   serialVersionUID = 1233223301144L;

    /**
     * 自增主键ID
     */
    private Long id;

    /**
     * 咨询类别名称
     */
    private String categoryName;

    /**
     * 位置（顺序）
     */
    private Integer seat;

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

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Integer getSeat() {
        return seat;
    }

    public void setSeat(Integer seat) {
        this.seat = seat;
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
}
