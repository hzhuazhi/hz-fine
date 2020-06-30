package com.hz.fine.master.core.service;

import com.hz.fine.master.core.common.service.BaseService;
import com.hz.fine.master.core.model.did.DidOnoffModel;

/**
 * @Description 用户抢单上下线的Service层
 * @Author yoko
 * @Date 2020/6/30 17:17
 * @Version 1.0
 */
public interface DidOnoffService<T> extends BaseService<T> {

    /**
     * @Description: 更新用户抢单的上下线
     * @param model
     * @return
     * @author yoko
     * @date 2020/6/30 17:26
    */
    public int updateDidOnoff(DidOnoffModel model);

}
