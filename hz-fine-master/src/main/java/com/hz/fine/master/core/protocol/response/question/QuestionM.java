package com.hz.fine.master.core.protocol.response.question;

import java.io.Serializable;

/**
 * @Description TODO
 * @Author yoko
 * @Date 2020/1/7 22:28
 * @Version 1.0
 */
public class QuestionM implements Serializable {
    private static final long   serialVersionUID = 1233023331141L;

    public Long id;
    public String categoryName;
    public Integer seatM;
    public String iconAds;

    public QuestionM(){

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
}
