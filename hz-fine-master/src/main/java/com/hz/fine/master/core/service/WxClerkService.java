package com.hz.fine.master.core.service;

import com.hz.fine.master.core.common.service.BaseService;
import com.hz.fine.master.core.model.wx.WxClerkModel;

/**
 * @Description 小微旗下店员的Service层
 * @Author yoko
 * @Date 2020/5/25 17:34
 * @Version 1.0
 */
public interface WxClerkService<T> extends BaseService<T> {

    /**
     * @Description: 查询微信旗下店员的信息
     * <p>
     *     这里yn不做硬性条件。
     *     查询到的数据取最新的一条关联关系的数据
     * </p>
     * @param model
     * @return
     * @author yoko
     * @date 2020/7/30 16:49
     */
    public WxClerkModel getWxClerk(WxClerkModel model);
}
