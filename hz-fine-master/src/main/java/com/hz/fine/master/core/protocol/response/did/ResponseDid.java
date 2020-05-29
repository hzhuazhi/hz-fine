package com.hz.fine.master.core.protocol.response.did;

import com.hz.fine.master.core.protocol.base.BaseResponse;
import com.hz.fine.master.core.protocol.response.did.basic.DidBasic;

import java.io.Serializable;

/**
 * @Description 协议：用户账号信息
 * @Author yoko
 * @Date 2020/5/15 17:21
 * @Version 1.0
 */
public class ResponseDid extends BaseResponse implements Serializable {
    private static final long   serialVersionUID = 1233023331143L;
    public DidBasic dataModel;// 用户的基本信息
    public ResponseDid(){

    }
}
