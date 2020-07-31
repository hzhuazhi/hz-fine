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
     * 消耗总收益：派发订单成功之后消耗的奖励
     */
    private String totalConsumeProfit;

    /**
     * 团队消耗总收益：团队长旗下消耗总金额比例规则奖励
     */
    private String totalTeamConsumeProfit;

    /**
     * 触发奖励的等级：团队消耗总和除以10万得到的整数就是等级
     */
    private Integer triggerQuotaGrade;

    /**
     * 触发额度奖励总收益
     */
    private String totalTriggerQuotaProfit;

    /**
     * 团队总额等级：总和到达多少级
     */
    private Integer teamConsumeCumulativeGrade;

    /**
     * 团队总额等级奖励总收益
     */
    private String totalTeamConsumeCumulativeProfit;

    /**
     * 团队长直推的用户消耗成功奖励
     */
    private String totalTeamDirectConsumeProfit;

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
     * 支付宝总奖励金额
     */
    private String zfbTotalProfit;

    /**
     * 是否需要同步数据:1不需要，2需要同步
     */
    private Integer isNotify;

    /**
     * 同步的接口地址:我方的同步地址
     */
    private String notifyUrl;

    /**
     * 是否是团队长：1不是团队长，2是团队长
     */
    private Integer isTeam;

    /**
     * 归属用户ID：上级的用户ID；对应本表的主键ID
     */
    private Long ownId;

    /**
     * 群序号
     */
    private Integer groupNum;

    /**
     * 个人出码开关：1打开状态，2暂停状态
     */
    private Integer switchType;

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

    /**
     * 用户收款账号ID：对应表tb_fn_did_collection_account的主键ID
     */
    private Long collectionAccountId;

    /**
     * 支付宝账号ID
     */
    private String userId;

    /**
     * 支付宝账号
     */
    private String zfbAcNum;

    /**
     * 微信群名称
     */
    private String payee;

    /**
     * 收款账号/地址（转码）：二维码
     */
    private String ddQrCode;

    /**
     * 有效微信群个数
     */
    private Integer countGroupNum;

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

    public String getZfbTotalProfit() {
        return zfbTotalProfit;
    }

    public void setZfbTotalProfit(String zfbTotalProfit) {
        this.zfbTotalProfit = zfbTotalProfit;
    }

    public Long getCollectionAccountId() {
        return collectionAccountId;
    }

    public void setCollectionAccountId(Long collectionAccountId) {
        this.collectionAccountId = collectionAccountId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getZfbAcNum() {
        return zfbAcNum;
    }

    public void setZfbAcNum(String zfbAcNum) {
        this.zfbAcNum = zfbAcNum;
    }

    public Integer getIsTeam() {
        return isTeam;
    }

    public void setIsTeam(Integer isTeam) {
        this.isTeam = isTeam;
    }

    public String getTotalConsumeProfit() {
        return totalConsumeProfit;
    }

    public void setTotalConsumeProfit(String totalConsumeProfit) {
        this.totalConsumeProfit = totalConsumeProfit;
    }

    public String getTotalTeamConsumeProfit() {
        return totalTeamConsumeProfit;
    }

    public void setTotalTeamConsumeProfit(String totalTeamConsumeProfit) {
        this.totalTeamConsumeProfit = totalTeamConsumeProfit;
    }

    public Integer getTriggerQuotaGrade() {
        return triggerQuotaGrade;
    }

    public void setTriggerQuotaGrade(Integer triggerQuotaGrade) {
        this.triggerQuotaGrade = triggerQuotaGrade;
    }

    public String getTotalTriggerQuotaProfit() {
        return totalTriggerQuotaProfit;
    }

    public void setTotalTriggerQuotaProfit(String totalTriggerQuotaProfit) {
        this.totalTriggerQuotaProfit = totalTriggerQuotaProfit;
    }

    public Integer getTeamConsumeCumulativeGrade() {
        return teamConsumeCumulativeGrade;
    }

    public void setTeamConsumeCumulativeGrade(Integer teamConsumeCumulativeGrade) {
        this.teamConsumeCumulativeGrade = teamConsumeCumulativeGrade;
    }

    public String getTotalTeamConsumeCumulativeProfit() {
        return totalTeamConsumeCumulativeProfit;
    }

    public void setTotalTeamConsumeCumulativeProfit(String totalTeamConsumeCumulativeProfit) {
        this.totalTeamConsumeCumulativeProfit = totalTeamConsumeCumulativeProfit;
    }

    public String getTotalTeamDirectConsumeProfit() {
        return totalTeamDirectConsumeProfit;
    }

    public void setTotalTeamDirectConsumeProfit(String totalTeamDirectConsumeProfit) {
        this.totalTeamDirectConsumeProfit = totalTeamDirectConsumeProfit;
    }

    public String getPayee() {
        return payee;
    }

    public void setPayee(String payee) {
        this.payee = payee;
    }

    public String getDdQrCode() {
        return ddQrCode;
    }

    public void setDdQrCode(String ddQrCode) {
        this.ddQrCode = ddQrCode;
    }

    public Integer getGroupNum() {
        return groupNum;
    }

    public void setGroupNum(Integer groupNum) {
        this.groupNum = groupNum;
    }

    public Integer getSwitchType() {
        return switchType;
    }

    public void setSwitchType(Integer switchType) {
        this.switchType = switchType;
    }

    public Integer getCountGroupNum() {
        return countGroupNum;
    }

    public void setCountGroupNum(Integer countGroupNum) {
        this.countGroupNum = countGroupNum;
    }
}
