package com.hz.fine.master.core.protocol.request.vcode;

import com.hz.fine.master.core.protocol.base.BaseRequest;

import java.io.Serializable;

/**
 * @Description 验证码
 * @Author yoko
 * @Date 2020/5/14 11:49
 * @Version 1.0
 */
public class RequestVcode extends BaseRequest implements Serializable {
    private static final long   serialVersionUID = 1233223332140L;

    /**
     * 手机号
     */
    public String phoneNum;

    /**
     * 发送验证码的具体类型：1注册，2忘记密码需要找回密码
     */
    public Integer vType;

    public RequestVcode(){

    }


    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public Integer getvType() {
        return vType;
    }

    public void setvType(Integer vType) {
        this.vType = vType;
    }
}
