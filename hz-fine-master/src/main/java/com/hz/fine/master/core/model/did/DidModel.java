package com.hz.fine.master.core.model.did;

import com.hz.fine.master.core.protocol.page.BasePage;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Description 用户的基本属性实体Bean
 * @Author yoko
 * @Date 2020/5/13 17:40
 * @Version 1.0
 */
public class DidModel extends BasePage implements Serializable {
    private static final long   serialVersionUID = 1233223301144L;

    /**
     * 自增主键ID
     */
    private Long id;

    /**
     * 账号昵称
     */
    private String nickname;

    /**
     * 账号
     */
    private String acNum;

    /**
     * 联系人电话
     */
    private String phoneNum;

    /**
     * 登录密码
     */
    private String passWd;

    /**
     * 新密码
     */
    private String newPassWd;

    /**
     * 操作密码
     */
    private String operateWd;

    /**
     * 新操作密码/安全密码：在修改密码的时候使用
     */
    private String newOperateWd;

    /**
     * 邀请码
     */
    private String icode;

    /**
     * 总金额（累计充值）
     */
    private String totalMoney;

    /**
     * 余额
     */
    private String balance;

    /**
     * 锁定/冻结金额
     */
    private String lockMoney;

    /**
     * 是否有过充值记录/是否是正事会员:1初始化，2充值会员
     */
    private Integer vipType;

    /**
     * 充值总收益：充多少送多少
     */
    private String totalRechargeProfit;

    /**
     * 档次总收益：达到的档次之后的总奖励
     */
    private String totalGradeProfit;

    /**
     * 直推总奖励收益
     */
    private String totalDirectProfit;

    /**
     * 裂变总奖励收益
     */
    private String totalIndirectProfit;

    /**
     * 团队总收益
     */
    private String totalTeamProfit;

    /**
     * 直推总人数
     */
    private Integer totalDirectNum;

    /**
     * 裂变总人数
     */
    private Integer totalIndirectNum;

    /**
     * 总收益
     */
    private String totalProfit;

    /**
     * 是否需要同步数据:1不需要，2需要同步
     */
    private Integer isNotify;

    /**
     * 同步的接口地址:我方的同步地址
     */
    private String notifyUrl;

    /**
     * 归属用户ID：上级的用户ID；对应本表的主键ID
     */
    private Long ownId;

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
     *支付类型：1微信，2支付宝，3银行卡
     * <p>
     *     这里查询账号下面的收款账号的类型
     * </p>
     */
    private Integer acType;

    /**
     * 金额
     */
    private BigDecimal money;

    /**
     * 要进行更改的金额
     */
    private String orderMoney;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAcNum() {
        return acNum;
    }

    public void setAcNum(String acNum) {
        this.acNum = acNum;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getPassWd() {
        return passWd;
    }

    public void setPassWd(String passWd) {
        this.passWd = passWd;
    }

    public String getOperateWd() {
        return operateWd;
    }

    public void setOperateWd(String operateWd) {
        this.operateWd = operateWd;
    }

    public String getIcode() {
        return icode;
    }

    public void setIcode(String icode) {
        this.icode = icode;
    }

    public String getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(String totalMoney) {
        this.totalMoney = totalMoney;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getLockMoney() {
        return lockMoney;
    }

    public void setLockMoney(String lockMoney) {
        this.lockMoney = lockMoney;
    }

    public Integer getVipType() {
        return vipType;
    }

    public void setVipType(Integer vipType) {
        this.vipType = vipType;
    }

    public String getTotalDirectProfit() {
        return totalDirectProfit;
    }

    public void setTotalDirectProfit(String totalDirectProfit) {
        this.totalDirectProfit = totalDirectProfit;
    }

    public String getTotalIndirectProfit() {
        return totalIndirectProfit;
    }

    public void setTotalIndirectProfit(String totalIndirectProfit) {
        this.totalIndirectProfit = totalIndirectProfit;
    }

    public Integer getTotalDirectNum() {
        return totalDirectNum;
    }

    public void setTotalDirectNum(Integer totalDirectNum) {
        this.totalDirectNum = totalDirectNum;
    }

    public Integer getTotalIndirectNum() {
        return totalIndirectNum;
    }

    public void setTotalIndirectNum(Integer totalIndirectNum) {
        this.totalIndirectNum = totalIndirectNum;
    }

    public String getTotalProfit() {
        return totalProfit;
    }

    public void setTotalProfit(String totalProfit) {
        this.totalProfit = totalProfit;
    }

    public Integer getIsNotify() {
        return isNotify;
    }

    public void setIsNotify(Integer isNotify) {
        this.isNotify = isNotify;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public Long getOwnId() {
        return ownId;
    }

    public void setOwnId(Long ownId) {
        this.ownId = ownId;
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

    public String getNewPassWd() {
        return newPassWd;
    }

    public void setNewPassWd(String newPassWd) {
        this.newPassWd = newPassWd;
    }

    public Integer getAcType() {
        return acType;
    }

    public void setAcType(Integer acType) {
        this.acType = acType;
    }


    public String getNewOperateWd() {
        return newOperateWd;
    }

    public void setNewOperateWd(String newOperateWd) {
        this.newOperateWd = newOperateWd;
    }

    public String getTotalRechargeProfit() {
        return totalRechargeProfit;
    }

    public void setTotalRechargeProfit(String totalRechargeProfit) {
        this.totalRechargeProfit = totalRechargeProfit;
    }

    public String getTotalGradeProfit() {
        return totalGradeProfit;
    }

    public void setTotalGradeProfit(String totalGradeProfit) {
        this.totalGradeProfit = totalGradeProfit;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public String getTotalTeamProfit() {
        return totalTeamProfit;
    }

    public void setTotalTeamProfit(String totalTeamProfit) {
        this.totalTeamProfit = totalTeamProfit;
    }

    public String getOrderMoney() {
        return orderMoney;
    }

    public void setOrderMoney(String orderMoney) {
        this.orderMoney = orderMoney;
    }
}
