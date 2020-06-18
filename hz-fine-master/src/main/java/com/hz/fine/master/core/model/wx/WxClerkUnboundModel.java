package com.hz.fine.master.core.model.wx;

import com.hz.fine.master.core.protocol.page.BasePage;

import java.io.Serializable;

/**
 * @Description 小微需要解绑店员的实体属性Bean
 * @Author yoko
 * @Date 2020/6/18 0:53
 * @Version 1.0
 */
public class WxClerkUnboundModel extends BasePage implements Serializable {
    private static final long   serialVersionUID = 1233223311150L;

    /**
     * 自增主键ID
     */
    private Long id;

    /**
     * 归属小微管理的主键ID：对应表tb_fn_wx的主键ID
     */
    private Long wxId;

    /**
     * 用户账号ID
     */
    private Long did;

    /**
     * 用户收款账号ID：对应表tb_fn_did_collection_account的主键ID
     */
    private Long collectionAccountId;

    /**
     * 微信昵称
     */
    private String wxName;

    /**
     * 解绑类型：1未解绑，2解绑失败（没有找到对应的微信昵称），3解绑成功
     */
    private Integer unboundType;

    /**
     * 备注
     */
    private String remark;

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

    public Long getWxId() {
        return wxId;
    }

    public void setWxId(Long wxId) {
        this.wxId = wxId;
    }

    public Long getDid() {
        return did;
    }

    public void setDid(Long did) {
        this.did = did;
    }

    public Long getCollectionAccountId() {
        return collectionAccountId;
    }

    public void setCollectionAccountId(Long collectionAccountId) {
        this.collectionAccountId = collectionAccountId;
    }

    public String getWxName() {
        return wxName;
    }

    public void setWxName(String wxName) {
        this.wxName = wxName;
    }

    public Integer getUnboundType() {
        return unboundType;
    }

    public void setUnboundType(Integer unboundType) {
        this.unboundType = unboundType;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
