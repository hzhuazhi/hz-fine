package com.hz.fine.master.core.protocol.response.vcode;

import java.io.Serializable;

/**
 * @Description 协议：验证码提交之后返回的vtoken
 * @Author yoko
 * @Date 2020/6/9 14:54
 * @Version 1.0
 */
public class Vcode implements Serializable {
    private static final long   serialVersionUID = 1233023331149L;

    /**
     * 验证码-通过的的token
     */
    public String vtoken;

    public Vcode(){

    }

    public String getVtoken() {
        return vtoken;
    }

    public void setVtoken(String vtoken) {
        this.vtoken = vtoken;
    }
}
