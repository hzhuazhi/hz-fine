package com.hz.fine.master.core.protocol.response.question;

import java.io.Serializable;

/**
 * @Description TODO
 * @Author yoko
 * @Date 2020/7/6 15:27
 * @Version 1.0
 */
public class QuestionDD implements Serializable {
    private static final long   serialVersionUID = 1233023331141L;
    public Long id;
    public Integer seatDD;
    public String title;
    public String sketch;
    public String pageAds;
    public Integer dataType;

    public QuestionDD(){

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Integer getDataType() {
        return dataType;
    }

    public void setDataType(Integer dataType) {
        this.dataType = dataType;
    }
}
