package com.hz.fine.master.core.model.mobilecard;

import com.hz.fine.master.core.protocol.page.BasePage;

import java.io.Serializable;

/**
 * @Description 手机卡的实体属性Bean
 * @Author yoko
 * @Date 2020/5/18 17:17
 * @Version 1.0
 */
public class MobileCardModel extends BasePage implements Serializable {
    private static final long   serialVersionUID = 1233223301147L;

    /**
     * 自增主键ID
     */
    private Long id;

    /**
     * 手机卡名称
     */
    private String cardName;

    /**
     * 使用了哪个归属人的名字：这张卡身份证上面的那个人办理的
     */
    private String useName;

    /**
     * 手机号
     */
    private String phoneNum;

    /**
     * 办理人的身份证号
     */
    private String idCard;

    /**
     * 手机号具体与手机型号绑定
     */
    private String bindingMobile;

    /**
     * 每月座机费
     */
    private String cost;

    /**
     * 手机号归属省份
     */
    private String province;

    /**
     * 手机号归属城市
     */
    private String city;

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

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public String getUseName() {
        return useName;
    }

    public void setUseName(String useName) {
        this.useName = useName;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getBindingMobile() {
        return bindingMobile;
    }

    public void setBindingMobile(String bindingMobile) {
        this.bindingMobile = bindingMobile;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
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
