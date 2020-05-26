package com.hz.fine.master.core.protocol.response.order;

import com.hz.fine.master.core.protocol.base.BaseResponse;

import java.io.Serializable;
import java.util.List;

/**
 * @Description 协议：任务订单（平台派发订单）
 * @Author yoko
 * @Date 2020/5/22 11:04
 * @Version 1.0
 */
public class ResponseOrder extends BaseResponse implements Serializable {
    private static final long   serialVersionUID = 1233023131150L;


//    public List<DidReward> dataList;
//    public DidReward dataModel;
    public Integer rowCount;



    public ResponseOrder(){

    }
}
