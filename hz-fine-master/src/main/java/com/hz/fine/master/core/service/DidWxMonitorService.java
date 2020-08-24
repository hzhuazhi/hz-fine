package com.hz.fine.master.core.service;

import com.hz.fine.master.core.common.service.BaseService;
import com.hz.fine.master.core.model.did.DidWxMonitorModel;

import java.util.List;

/**
 * @Description 用户的微信收款账号金额监控的Service层
 * @Author yoko
 * @Date 2020/8/23 19:35
 * @Version 1.0
 */
public interface DidWxMonitorService<T> extends BaseService<T> {

    /**
     * @Description: 根据条件获取去重复的微信原始ID集合
     * @param model
     * @return
     * @author yoko
     * @date 2020/8/24 11:53
     */
    public List<String> getToWxidList(DidWxMonitorModel model);
}
