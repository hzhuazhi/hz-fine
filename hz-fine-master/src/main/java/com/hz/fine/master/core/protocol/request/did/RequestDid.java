package com.hz.fine.master.core.protocol.request.did;

import com.hz.fine.master.core.protocol.base.BaseRequest;

import java.io.Serializable;

/**
 * @Description 用户
 * @Author yoko
 * @Date 2020/5/13 16:12
 * @Version 1.0
 */
public class RequestDid extends BaseRequest implements Serializable {
    private static final long   serialVersionUID = 1233283332140L;

    /**
     * 账号昵称
     */
    public String nickname;

    /**
     * 账号
     */
    public String acNum;

    /**
     * 登录类型：1以之前微信协议，2以支付宝协议
     */
    public Integer logOnType;

    /**
     * 登录密码
     */
    public String passWd;

    /**
     * 新密码：在修改密码的时候使用
     */
    public String newPassWd;

    /**
     * 操作密码
     */
    public String operateWd;

    /**
     * 新操作密码/安全密码：在修改密码的时候使用
     */
    public String newOperateWd;

    /**
     * 邀请码
     */
    public String icode;

    /**
     * 注册的验证码
     */
    public String vcode;

    /**
     * 提交验证码成功之后返回给客户端的验证码token值
     */
    public String vtoken;

    /**
     * 个人出码开关：1打开状态，2暂停状态
     */
    public Integer switchType;

    /**
     * 同时操作群的个数：派单时同时操作群的个数
     */
    public Integer operateGroupNum;



    public RequestDid(){

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

    public String getVcode() {
        return vcode;
    }

    public void setVcode(String vcode) {
        this.vcode = vcode;
    }

    public String getNewPassWd() {
        return newPassWd;
    }

    public void setNewPassWd(String newPassWd) {
        this.newPassWd = newPassWd;
    }

    public String getNewOperateWd() {
        return newOperateWd;
    }

    public void setNewOperateWd(String newOperateWd) {
        this.newOperateWd = newOperateWd;
    }


    public String getVtoken() {
        return vtoken;
    }

    public void setVtoken(String vtoken) {
        this.vtoken = vtoken;
    }

    public Integer getLogOnType() {
        return logOnType;
    }

    public void setLogOnType(Integer logOnType) {
        this.logOnType = logOnType;
    }

    public Integer getSwitchType() {
        return switchType;
    }

    public void setSwitchType(Integer switchType) {
        this.switchType = switchType;
    }

    public Integer getOperateGroupNum() {
        return operateGroupNum;
    }

    public void setOperateGroupNum(Integer operateGroupNum) {
        this.operateGroupNum = operateGroupNum;
    }
}
