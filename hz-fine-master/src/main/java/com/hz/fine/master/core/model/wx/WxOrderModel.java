package com.hz.fine.master.core.model.wx;

import com.hz.fine.master.core.protocol.page.BasePage;

import java.io.Serializable;

/**
 * @Description 小微给出订单记录的实体属性Bean
 * @Author yoko
 * @Date 2020/8/8 15:04
 * @Version 1.0
 */
public class WxOrderModel extends BasePage implements Serializable {
    private static final long   serialVersionUID = 1263223301153L;

    /**
     * 自增主键ID
     */
    private Long id;

    /**
     * 归属小微管理的主键ID：对应表tb_fn_wx的主键ID
     */
    private Long wxId;

    /**
     * 归属用户ID：对应表tb_fn_did的主键ID
     */
    private Long did;

    /**
     * 用户收款账号ID：对应表tb_fn_did_collection_account的主键ID；也就是具体上下线的账号
     */
    private Long collectionAccountId;

    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 数据类型;1初始化，2表示小微有数据回执
     */
    private Integer dataType;

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

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Integer getDataType() {
        return dataType;
    }

    public void setDataType(Integer dataType) {
        this.dataType = dataType;
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
