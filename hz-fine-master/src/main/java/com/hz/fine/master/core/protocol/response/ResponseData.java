package com.hz.fine.master.core.protocol.response;

import com.hz.fine.master.core.protocol.base.BaseResponse;

import java.io.Serializable;

/**
 * @Description 公告的返回结果数据给客户端是bean
 * @Author yoko
 * @Date 2020/5/14 16:46
 * @Version 1.0
 */
public class ResponseData extends BaseResponse implements Serializable {
    private static final long   serialVersionUID = 1233023331141L;

    /**
     * 是否有绑定的支付宝账号：1没有绑定，2绑定
     */
    public Integer haveType;

    /**
     * 支付宝用户ID
     */
    public String userId;

    public ResponseData(){

    }

    public Integer getHaveType() {
        return haveType;
    }

    public void setHaveType(Integer haveType) {
        this.haveType = haveType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
