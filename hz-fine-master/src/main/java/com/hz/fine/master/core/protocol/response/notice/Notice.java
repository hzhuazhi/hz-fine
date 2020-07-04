package com.hz.fine.master.core.protocol.response.notice;

import java.io.Serializable;

/**
 * @Description 公告的属性
 * @Author yoko
 * @Date 2020/1/14 21:26
 * @Version 1.0
 */
public class Notice implements Serializable {
    private static final long   serialVersionUID = 1233023331141L;

    public Long id;
    public String title;
    public String sketch;
    public String content;
    public String iconAds;
    public String pageAds;
    public Integer seat;
    public String createTime;

    public Notice(){

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getIconAds() {
        return iconAds;
    }

    public void setIconAds(String iconAds) {
        this.iconAds = iconAds;
    }

    public String getPageAds() {
        return pageAds;
    }

    public void setPageAds(String pageAds) {
        this.pageAds = pageAds;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public Integer getSeat() {
        return seat;
    }

    public void setSeat(Integer seat) {
        this.seat = seat;
    }
}
