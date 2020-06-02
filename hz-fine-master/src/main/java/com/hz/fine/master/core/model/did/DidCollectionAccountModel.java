package com.hz.fine.master.core.model.did;

import com.hz.fine.master.core.protocol.page.BasePage;

import java.io.Serializable;

/**
 * @Description 用户的收款账号的实体属性Bean
 * @Author yoko
 * @Date 2020/5/15 11:31
 * @Version 1.0
 */
public class DidCollectionAccountModel extends BasePage implements Serializable {
    private static final long   serialVersionUID = 1233223301145L;

    /**
     * 自增主键ID
     */
    private Long id;

    /**
     * 归属用户ID：对应表tb_fn_did的主键ID
     */
    private Long did;

    /**
     * 收款账户名称：用户备注使用
     */
    private String acName;

    /**
     * 收款账户类型：1微信，2支付宝，3银行卡
     */
    private Integer acType;

    /**
     * 收款的具体账号：类型为微信则微信账号，支付宝为支付宝账号；怕后期有其它冲突
     */
    private String acNum;

    /**
     * 收款账号/地址（原）：二维码
     */
    private String mmQrCode;

    /**
     * 收款账号/地址（转码）：二维码
     */
    private String ddQrCode;

    /**
     * 收款人：微信昵称、支付宝昵称、银行卡持有人名称
     */
    private String payee;

    /**
     * 银行名称/银行卡开户行
     */
    private String bankName;

    /**
     * 经营范围类型:1个人，2个体，3公司
     */
    private Integer businessType;

    /**
     * 小微商户二维码图片地址
     */
    private String wxQrCodeAds;

    /**
     * 日开关是否启用（等于1正常使用，其它是暂停）:：1正常使用，2暂停使用
     */
    private Integer daySwitch;

    /**
     * 月开关是否启用：1正常使用，2暂停使用
     */
    private Integer monthSwitch;

    /**
     * 总开关是否启用：1正常使用，2暂停使用
     */
    private Integer totalSwitch;

    /**
     * 收款账号审核：1初始化，2审核失败，2审核成功
     */
    private Integer checkStatus;

    /**
     * 收款账号审核失败理由
     */
    private String checkInfo;

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

    /**
     * 小微管理员主键ID
     */
    private Long wxId;


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

    public String getAcName() {
        return acName;
    }

    public void setAcName(String acName) {
        this.acName = acName;
    }

    public Integer getAcType() {
        return acType;
    }

    public void setAcType(Integer acType) {
        this.acType = acType;
    }

    public String getAcNum() {
        return acNum;
    }

    public void setAcNum(String acNum) {
        this.acNum = acNum;
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

    public String getPayee() {
        return payee;
    }

    public void setPayee(String payee) {
        this.payee = payee;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public Integer getBusinessType() {
        return businessType;
    }

    public void setBusinessType(Integer businessType) {
        this.businessType = businessType;
    }

    public String getWxQrCodeAds() {
        return wxQrCodeAds;
    }

    public void setWxQrCodeAds(String wxQrCodeAds) {
        this.wxQrCodeAds = wxQrCodeAds;
    }

    public Integer getDaySwitch() {
        return daySwitch;
    }

    public void setDaySwitch(Integer daySwitch) {
        this.daySwitch = daySwitch;
    }

    public Integer getMonthSwitch() {
        return monthSwitch;
    }

    public void setMonthSwitch(Integer monthSwitch) {
        this.monthSwitch = monthSwitch;
    }

    public Integer getTotalSwitch() {
        return totalSwitch;
    }

    public void setTotalSwitch(Integer totalSwitch) {
        this.totalSwitch = totalSwitch;
    }

    public Integer getCheckStatus() {
        return checkStatus;
    }

    public void setCheckStatus(Integer checkStatus) {
        this.checkStatus = checkStatus;
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

    public String getCheckInfo() {
        return checkInfo;
    }

    public void setCheckInfo(String checkInfo) {
        this.checkInfo = checkInfo;
    }

    public Long getWxId() {
        return wxId;
    }

    public void setWxId(Long wxId) {
        this.wxId = wxId;
    }
}
