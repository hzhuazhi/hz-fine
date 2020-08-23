package com.hz.fine.master.core.model.did;

import com.hz.fine.master.core.protocol.page.BasePage;

import java.io.Serializable;
import java.util.List;

/**
 * @Description 用户的微信收款账号金额监控的实体属性Bean
 * @Author yoko
 * @Date 2020/8/23 19:28
 * @Version 1.0
 */
public class DidWxMonitorModel extends BasePage implements Serializable {
    private static final long   serialVersionUID = 1203223202121L;

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
     * 失效时间
     */
    private String invalidTime;

    /**
     * 创建日期：存的日期格式20160530
     */
    private Integer curday;

    /**
     * 创建所属小时：24小时制
     */
    private Integer curhour;

    /**
     * 创建所属分钟：60分钟制
     */
    private Integer curminute;

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
     * SQL查询条件
     */
    private String invalidTimeStr;


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

    public String getInvalidTime() {
        return invalidTime;
    }

    public void setInvalidTime(String invalidTime) {
        this.invalidTime = invalidTime;
    }

    public Integer getCurday() {
        return curday;
    }

    public void setCurday(Integer curday) {
        this.curday = curday;
    }

    public Integer getCurhour() {
        return curhour;
    }

    public void setCurhour(Integer curhour) {
        this.curhour = curhour;
    }

    public Integer getCurminute() {
        return curminute;
    }

    public void setCurminute(Integer curminute) {
        this.curminute = curminute;
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

    public String getInvalidTimeStr() {
        return invalidTimeStr;
    }

    public void setInvalidTimeStr(String invalidTimeStr) {
        this.invalidTimeStr = invalidTimeStr;
    }
}
