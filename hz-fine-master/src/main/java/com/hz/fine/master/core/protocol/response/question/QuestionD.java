package com.hz.fine.master.core.protocol.response.question;

import java.io.Serializable;

/**
 * @Description TODO
 * @Author yoko
 * @Date 2020/1/8 10:54
 * @Version 1.0
 */
public class QuestionD implements Serializable {
    private static final long   serialVersionUID = 1233023331141L;
    public Long id;
    public String categoryName;
    public Integer seatD;
    public String title;
    public String sketch;
    public String pageAds;

    public QuestionD(){

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
}
