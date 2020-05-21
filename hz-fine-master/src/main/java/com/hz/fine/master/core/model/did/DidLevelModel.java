package com.hz.fine.master.core.model.did;

import com.hz.fine.master.core.protocol.page.BasePage;

import java.io.Serializable;

/**
 * @Description 用户的层级关系的实体Bean
 * @Author yoko
 * @Date 2020/5/14 17:50
 * @Version 1.0
 */
public class DidLevelModel extends BasePage implements Serializable {
    private static final long   serialVersionUID = 1233223301144L;

    /**
     * 自增主键ID
     */
    private Long id;

    /**
     * 归属用户ID：对应表tb_fn_did的主键ID
     */
    private Long did;

    /**
     * 层级关系的用户ID
     */
    private Long levelDid;

    /**
     * 层级关系类型：1直推关系，2裂变关系
     */
    private Integer levelType;

    /**
     * 充值总金额：#不做更改，预留吧
     */
    private String totalMoney;

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

    public Long getDid() {
        return did;
    }

    public void setDid(Long did) {
        this.did = did;
    }

    public Long getLevelDid() {
        return levelDid;
    }

    public void setLevelDid(Long levelDid) {
        this.levelDid = levelDid;
    }

    public Integer getLevelType() {
        return levelType;
    }

    public void setLevelType(Integer levelType) {
        this.levelType = levelType;
    }

    public String getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(String totalMoney) {
        this.totalMoney = totalMoney;
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
