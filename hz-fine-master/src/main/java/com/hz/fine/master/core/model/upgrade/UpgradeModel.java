package com.hz.fine.master.core.model.upgrade;

import java.io.Serializable;

/**
 * @Description 客户端升级更新的实体属性Bean
 * @Author yoko
 * @Date 2020/1/14 13:49
 * @Version 1.0
 */
public class UpgradeModel implements Serializable {
    private static final long   serialVersionUID = 1233223301144L;

    /**
     * 自增主键ID
     */
    private Long id;

    /**
     * 名称
     */
    private String upName;

    /**
     * 此客户端的版本号
     */
    private Integer clientVer;

    /**
     * 客户端类型:1Android，2IOS
     */
    private Integer clientType;

    /**
     * 更新类型：1表示限数更新，2表示全部更新
     */
    private Integer limitType;

    /**
     * 要限制多少个用户
     */
    private Integer limitNum;

    /**
     * 已经限制了多少用户
     */
    private Integer isLimitNum;

    /**
     * 是否以及完成了限制目标：1未完成，2完成
     */
    private Integer isLimitOk;

    /**
     * 客户端文件的唯一值（md5值）
     */
    private String md5Value;

    /**
     * 资源文件地址
     */
    private String resUrl;

    /**
     * 是否属于强制更新：1不是强制更新，2属于强制更新
     */
    private Integer upType;

    /**
     * 更新内容简介
     */
    private String content;

    /**
     * 展现版本号：展现给客户端看的
     */
    private String showVer;

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

    public String getUpName() {
        return upName;
    }

    public void setUpName(String upName) {
        this.upName = upName;
    }

    public Integer getClientVer() {
        return clientVer;
    }

    public void setClientVer(Integer clientVer) {
        this.clientVer = clientVer;
    }

    public Integer getClientType() {
        return clientType;
    }

    public void setClientType(Integer clientType) {
        this.clientType = clientType;
    }

    public Integer getLimitType() {
        return limitType;
    }

    public void setLimitType(Integer limitType) {
        this.limitType = limitType;
    }

    public Integer getLimitNum() {
        return limitNum;
    }

    public void setLimitNum(Integer limitNum) {
        this.limitNum = limitNum;
    }

    public Integer getIsLimitNum() {
        return isLimitNum;
    }

    public void setIsLimitNum(Integer isLimitNum) {
        this.isLimitNum = isLimitNum;
    }

    public Integer getIsLimitOk() {
        return isLimitOk;
    }

    public void setIsLimitOk(Integer isLimitOk) {
        this.isLimitOk = isLimitOk;
    }

    public String getMd5Value() {
        return md5Value;
    }

    public void setMd5Value(String md5Value) {
        this.md5Value = md5Value;
    }

    public String getResUrl() {
        return resUrl;
    }

    public void setResUrl(String resUrl) {
        this.resUrl = resUrl;
    }

    public Integer getUpType() {
        return upType;
    }

    public void setUpType(Integer upType) {
        this.upType = upType;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getShowVer() {
        return showVer;
    }

    public void setShowVer(String showVer) {
        this.showVer = showVer;
    }
}
