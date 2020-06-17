package com.hz.fine.master.core.model.did;

import com.hz.fine.master.core.protocol.page.BasePage;

import java.io.Serializable;
import java.util.List;

/**
 * @Description 用户的收款账号二维码的实体属性Bean
 * @Author yoko
 * @Date 2020/6/17 14:54
 * @Version 1.0
 */
public class DidCollectionAccountQrCodeModel extends BasePage implements Serializable {
    private static final long   serialVersionUID = 1233223301145L;

    /**
     * 自增主键ID
     */
    private Long id;

    /**
     * 用户收款账号ID：对应表tb_fn_did_collection_account的主键ID
     */
    private Long collectionAccountId;

    /**
     * 别名
     */
    private String alias;

    /**
     * 收款账号/地址（原）：二维码
     */
    private String mmQrCode;

    /**
     * 收款账号/地址（转码）：二维码
     */
    private String ddQrCode;

    /**
     * 二维码类型：1普通二维码，2个人固码，3固定金额码
     */
    private Integer dataType;

    /**
     * 码的金额：固码的具体金额
     */
    private String qrCodeMoney;

    /**
     * 可使用次数：可以给出多少次
     */
    private Integer limitNum;

    /**
     * 已经使用次数：已经给出多少次
     */
    private Integer isLimitNum;

    /**
     * 使用状态:1初始化有效正常使用，2无效暂停使用
     */
    private Integer useStatus;

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

    private List<DidCollectionAccountQrCodeModel> dataList;

    public DidCollectionAccountQrCodeModel(){

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCollectionAccountId() {
        return collectionAccountId;
    }

    public void setCollectionAccountId(Long collectionAccountId) {
        this.collectionAccountId = collectionAccountId;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getMmQrCode() {
        return mmQrCode;
    }

    public void setMmQrCode(String mmQrCode) {
        this.mmQrCode = mmQrCode;
    }

    public String getDdQrCode() {
        return ddQrCode;
    }

    public void setDdQrCode(String ddQrCode) {
        this.ddQrCode = ddQrCode;
    }

    public Integer getDataType() {
        return dataType;
    }

    public void setDataType(Integer dataType) {
        this.dataType = dataType;
    }

    public String getQrCodeMoney() {
        return qrCodeMoney;
    }

    public void setQrCodeMoney(String qrCodeMoney) {
        this.qrCodeMoney = qrCodeMoney;
    }

    public Integer getUseStatus() {
        return useStatus;
    }

    public void setUseStatus(Integer useStatus) {
        this.useStatus = useStatus;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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

    public List<DidCollectionAccountQrCodeModel> getDataList() {
        return dataList;
    }

    public void setDataList(List<DidCollectionAccountQrCodeModel> dataList) {
        this.dataList = dataList;
    }
}
