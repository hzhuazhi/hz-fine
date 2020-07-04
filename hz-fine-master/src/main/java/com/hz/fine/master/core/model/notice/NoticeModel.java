package com.hz.fine.master.core.model.notice;



import com.hz.fine.master.core.protocol.page.BasePage;

import java.io.Serializable;

/**
 * @Description 公告的实体属性Bean
 * @Author yoko
 * @Date 2020/1/14 20:14
 * @Version 1.0
 */
public class NoticeModel extends BasePage implements Serializable {
    private static final long   serialVersionUID = 1233223301144L;

    /**
     * 自增主键ID
     */
    private Long id;

    /**
     * 公告名称
     */
    private String noticeName;

    /**
     * 公告标题
     */
    private String title;

    /**
     * 简述：简单描述
     */
    private String sketch;

    /**
     * 公告内容
     */
    private String content;

    /**
     * 公告的图标/图片
     */
    private String iconAds;

    /**
     * 内容的页面地址
     */
    private String pageAds;

    /**
     * 公告的开始有效时间
     */
    private String startTime;

    /**
     * 公告的结束有效时间
     */
    private String endTime;

    /**
     * 位置（顺序）
     */
    private Integer seat;

    /**
     * 公告类型：1系统公告，2首页提醒公告
     */
    public Integer noticeType;

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
     * 服务器当前时间
     */
    private String nowTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNoticeName() {
        return noticeName;
    }

    public void setNoticeName(String noticeName) {
        this.noticeName = noticeName;
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

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
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

    public String getNowTime() {
        return nowTime;
    }

    public void setNowTime(String nowTime) {
        this.nowTime = nowTime;
    }

    public Integer getSeat() {
        return seat;
    }

    public void setSeat(Integer seat) {
        this.seat = seat;
    }

    public Integer getNoticeType() {
        return noticeType;
    }

    public void setNoticeType(Integer noticeType) {
        this.noticeType = noticeType;
    }
}
