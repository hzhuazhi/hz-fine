package com.hz.fine.master.core.model.wx;

import com.hz.fine.master.core.protocol.page.BasePage;

import java.io.Serializable;

/**
 * @Description 小微旗下店员的实体属性Bean
 * @Author yoko
 * @Date 2020/5/25 16:59
 * @Version 1.0
 */
public class WxClerkModel extends BasePage implements Serializable {
    private static final long   serialVersionUID = 1233223301150L;

    /**
     * 自增主键ID
     */
    private Long id;

    /**
     * 归属小微管理的主键ID：对应表tb_fn_wx的主键ID
     */
    private String wxId;

    /**
     * 用户账号ID
     */
    private Long did;

    /**
     * 用户收款账号ID：对应表tb_fn_did_collection_account的主键ID
     */
    private Long collectionAccountId;

    /**
     * 最新激活时间
     */
    private String onlineTime;

    /**
     * 最新下线时间
     */
    private String offlineTime;

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

    public String getWxId() {
        return wxId;
    }

    public void setWxId(String wxId) {
        this.wxId = wxId;
    }

    public Long getCollectionAccountId() {
        return collectionAccountId;
    }

    public void setCollectionAccountId(Long collectionAccountId) {
        this.collectionAccountId = collectionAccountId;
    }

    public String getOnlineTime() {
        return onlineTime;
    }

    public void setOnlineTime(String onlineTime) {
        this.onlineTime = onlineTime;
    }

    public String getOfflineTime() {
        return offlineTime;
    }

    public void setOfflineTime(String offlineTime) {
        this.offlineTime = offlineTime;
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

    public Long getDid() {
        return did;
    }

    public void setDid(Long did) {
        this.did = did;
    }
}
