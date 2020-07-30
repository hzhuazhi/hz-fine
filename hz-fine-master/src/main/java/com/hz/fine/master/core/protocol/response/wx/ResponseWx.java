package com.hz.fine.master.core.protocol.response.wx;

import com.hz.fine.master.core.protocol.base.BaseResponse;

import java.io.Serializable;
import java.util.List;

/**
 * @Description 协议：小微
 * @Author yoko
 * @Date 2020/7/30 15:12
 * @Version 1.0
 */
public class ResponseWx extends BaseResponse implements Serializable {
    private static final long   serialVersionUID = 1233023331141L;
    public Wx dataModel;
    public List<Wx> dataList;
    public Integer rowCount;

    public ResponseWx(){

    }
}
