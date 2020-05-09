package com.hz.fine.master.core.service;

import com.hz.fine.master.core.common.service.BaseService;
import com.hz.fine.master.core.model.alipay.AlipayNotifyModel;

/**
 * @Description 阿里支付的Service层
 * @Author yoko
 * @Date 2019/12/26 13:51
 * @Version 1.0
 */
public interface AlipayService<T> extends BaseService<T> {

    /**
     * @Description: 添加阿里支付宝订单结果数据
     * @param model - 订单回调的数据
     * @return int
     * @author yoko
     * @date 2019/12/27 15:56
    */
    public int addAlipayNotify(AlipayNotifyModel model);


}
