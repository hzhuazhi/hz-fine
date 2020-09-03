package com.hz.fine.master.core.model.did;

import com.hz.fine.master.core.protocol.page.BasePage;

import java.io.Serializable;

/**
 * @Description 用户的微信出码排序的实体属性Bean
 * @Author yoko
 * @Date 2020/8/31 16:13
 * @Version 1.0
 */
public class DidWxSortModel extends BasePage implements Serializable {
    private static final long   serialVersionUID = 1203223203121L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 奖励归属用户ID：对应表tb_fn_did的主键ID；奖励给哪个用户
     */
    private Long did;

    /**
     * 微信昵称
     */
    private String wxNickname;

    /**
     * 用户收款账号的微信原始ID
     */
    private String toWxid;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 是否属于使用中：1没有使用，2正在使用中
     */
    private Integer inUse;

    /**
     * 延迟/限制的时间：什么时候可以正常使用
     */
    private String delayTime;

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

    private Integer startSort;
    private Integer endSort;

    private String startCreateTime;
    private String endCreateTime;

    /**
     * 排序类型：1升序，2降序
     */
    private Integer orderType;

    /**
     * 更新使用状态时使用的值-SQL
     */
    private Integer upInUse;


    /**
     * 被限制的类型：1在金额范围内限制，2超过金额上限被限制，3被微信限制时间
     */
    private Integer limitType;

    private String startDelayTime;
    private String endDelayTime;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDid() {
        return did;
    }

    public void setDid(Long did) {
        this.did = did;
    }

    public String getWxNickname() {
        return wxNickname;
    }

    public void setWxNickname(String wxNickname) {
        this.wxNickname = wxNickname;
    }

    public String getToWxid() {
        return toWxid;
    }

    public void setToWxid(String toWxid) {
        this.toWxid = toWxid;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }


    public Integer getInUse() {
        return inUse;
    }

    public void setInUse(Integer inUse) {
        this.inUse = inUse;
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

    public Integer getStartSort() {
        return startSort;
    }

    public void setStartSort(Integer startSort) {
        this.startSort = startSort;
    }

    public Integer getEndSort() {
        return endSort;
    }

    public void setEndSort(Integer endSort) {
        this.endSort = endSort;
    }

    public String getStartCreateTime() {
        return startCreateTime;
    }

    public void setStartCreateTime(String startCreateTime) {
        this.startCreateTime = startCreateTime;
    }

    public String getEndCreateTime() {
        return endCreateTime;
    }

    public void setEndCreateTime(String endCreateTime) {
        this.endCreateTime = endCreateTime;
    }

    public Integer getOrderType() {
        return orderType;
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }

    public Integer getUpInUse() {
        return upInUse;
    }

    public void setUpInUse(Integer upInUse) {
        this.upInUse = upInUse;
    }


    public String getDelayTime() {
        return delayTime;
    }

    public void setDelayTime(String delayTime) {
        this.delayTime = delayTime;
    }

    public Integer getLimitType() {
        return limitType;
    }

    public void setLimitType(Integer limitType) {
        this.limitType = limitType;
    }

    public String getStartDelayTime() {
        return startDelayTime;
    }

    public void setStartDelayTime(String startDelayTime) {
        this.startDelayTime = startDelayTime;
    }

    public String getEndDelayTime() {
        return endDelayTime;
    }

    public void setEndDelayTime(String endDelayTime) {
        this.endDelayTime = endDelayTime;
    }
}
