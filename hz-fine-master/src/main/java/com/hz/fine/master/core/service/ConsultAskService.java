package com.hz.fine.master.core.service;

import com.hz.fine.master.core.common.service.BaseService;
import com.hz.fine.master.core.model.consult.ConsultAskModel;

/**
 * @Description 在线客服、咨询的发问的Service层
 * @Author yoko
 * @Date 2020/7/8 20:10
 * @Version 1.0
 */
public interface ConsultAskService<T> extends BaseService<T> {

    /**
     * @Description: 查询提问的详情-一对多
     * @param model
     * @return
     * @author yoko
     * @date 2020/7/9 15:42
    */
    public ConsultAskModel getConsultAskInfo(ConsultAskModel model);
}
