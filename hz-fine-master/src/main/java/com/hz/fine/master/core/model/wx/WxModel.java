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
     * 手机号具体与手机型号绑定
     */
    private String bindingMobile;

    /**
     * 总共可以加多少个用户
     */
    private Integer dataNum;

    /**
     * 已经加了多少用户
     */
    private Integer isDataNum;

    /**
     * 是否以及完成了限制目标：1未完成，2完成
     */
    private Integer isOk;

    /**
     * 每日可加用户的数量
     */
    private Integer dayNum;

    /**
     * 总共可以加多少个群
     */
    private Integer groupNum;

    /**
     * 已经下了多少个群
     */
    private Integer isGroupNum;

    /**
     * 加群是否以及完成了限制目标：1未完成，2完成
     */
    private Integer isOkGroup;

    /**
     * 每天可加多少个群
     */
    private Integer dayGroupNum;

    /**
     * 微信名片二维码地址
     */
    private String wxQrCode;

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

    public String getBindingMobile() {
        return bindingMobile;
    }

    public void setBindingMobile(String bindingMobile) {
        this.bindingMobile = bindingMobile;
    }

    public Integer getDataNum() {
        return dataNum;
    }

    public void setDataNum(Integer dataNum) {
        this.dataNum = dataNum;
    }

    public Integer getIsDataNum() {
        return isDataNum;
    }

    public void setIsDataNum(Integer isDataNum) {
        this.isDataNum = isDataNum;
    }

    public Integer getIsOk() {
        return isOk;
    }

    public void setIsOk(Integer isOk) {
        this.isOk = isOk;
    }

    public Integer getDayNum() {
        return dayNum;
    }

    public void setDayNum(Integer dayNum) {
        this.dayNum = dayNum;
    }

    public String getWxQrCode() {
        return wxQrCode;
    }

    public void setWxQrCode(String wxQrCode) {
        this.wxQrCode = wxQrCode;
    }

    public Integer getGroupNum() {
        return groupNum;
    }

    public void setGroupNum(Integer groupNum) {
        this.groupNum = groupNum;
    }

    public Integer getIsGroupNum() {
        return isGroupNum;
    }

    public void setIsGroupNum(Integer isGroupNum) {
        this.isGroupNum = isGroupNum;
    }

    public Integer getIsOkGroup() {
        return isOkGroup;
    }

    public void setIsOkGroup(Integer isOkGroup) {
        this.isOkGroup = isOkGroup;
    }

    public Integer getDayGroupNum() {
        return dayGroupNum;
    }

    public void setDayGroupNum(Integer dayGroupNum) {
        this.dayGroupNum = dayGroupNum;
    }
}
