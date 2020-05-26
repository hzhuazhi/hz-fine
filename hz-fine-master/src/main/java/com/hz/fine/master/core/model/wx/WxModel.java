package com.hz.fine.master.core.model.wx;

import com.hz.fine.master.core.protocol.page.BasePage;

import java.io.Serializable;

/**
 * @Description 小微管理的实体属性Bean
 * @Author yoko
 * @Date 2020/5/25 16:01
 * @Version 1.0
 */
public class WxModel extends BasePage implements Serializable {
    private static final long   serialVersionUID = 1233223301149L;

    /**
     * 自增主键ID
     */
    private Long id;

    /**
     * 微信账号
     */
    private String acName;

    /**
     * 微信登录密码
     */
    private String wxPassWd;

    /**
     * 微信昵称
     */
    private String wxName;

    /**
     * 可爱猫的to_wxid
     */
    private String toWxid;

    /**
     * 旗下绑定多少个店员
     */
    private String totalLink;

    /**
     * 使用状态:1初始化有效正常使用，2无效暂停使用
     */
    private Integer useStatus;

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

    public String getAcName() {
        return acName;
    }

    public void setAcName(String acName) {
        this.acName = acName;
    }

    public String getWxPassWd() {
        return wxPassWd;
    }

    public void setWxPassWd(String wxPassWd) {
        this.wxPassWd = wxPassWd;
    }

    public String getWxName() {
        return wxName;
    }

    public void setWxName(String wxName) {
        this.wxName = wxName;
    }

    public String getToWxid() {
        return toWxid;
    }

    public void setToWxid(String toWxid) {
        this.toWxid = toWxid;
    }

    public String getTotalLink() {
        return totalLink;
    }

    public void setTotalLink(String totalLink) {
        this.totalLink = totalLink;
    }

    public Integer getUseStatus() {
        return useStatus;
    }

    public void setUseStatus(Integer useStatus) {
        this.useStatus = useStatus;
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
