package com.hz.fine.master.core.protocol.request.bank;

import com.hz.fine.master.core.protocol.base.BaseRequest;

import java.io.Serializable;

/**
 * @Description 协议;银行卡
 * @Author yoko
 * @Date 2020/6/29 13:52
 * @Version 1.0
 */
public class RequestBank extends BaseRequest implements Serializable {
    private static final long   serialVersionUID = 1233283332313L;

    /**
     * 银行卡主键ID
     */
    public Long id;

    public RequestBank(){

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
