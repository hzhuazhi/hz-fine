package com.hz.fine.master.core.protocol.request.consult;

import com.hz.fine.master.core.protocol.base.BaseRequest;

import java.io.Serializable;

/**
 * @Description 协议：在线客服、咨询
 * @Author yoko
 * @Date 2020/7/6 13:44
 * @Version 1.0
 */
public class RequestConsult extends BaseRequest implements Serializable {
    private static final long   serialVersionUID = 2233283332140L;

    public Long id;

    public RequestConsult(){

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
