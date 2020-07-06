package com.hz.fine.master.core.protocol.response.consult;

import java.io.Serializable;

/**
 * @Description TODO
 * @Author yoko
 * @Date 2020/7/6 13:46
 * @Version 1.0
 */
public class Consult implements Serializable {
    private static final long   serialVersionUID = 2233023331141L;

    /**
     * 自增主键ID
     */
    public Long id;

    /**
     * 咨询类别名称
     */
    public String categoryName;

    /**
     * 位置（顺序）
     */
    public Integer seat;

    public Consult(){

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

    public Integer getSeat() {
        return seat;
    }

    public void setSeat(Integer seat) {
        this.seat = seat;
    }
}
