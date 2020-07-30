package com.hz.fine.master.core.service;

import com.hz.fine.master.core.common.service.BaseService;
import com.hz.fine.master.core.model.wx.WxModel;

/**
 * @Description 小微管理的Service层
 * @Author yoko
 * @Date 2020/5/25 16:26
 * @Version 1.0
 */
public interface WxService<T> extends BaseService<T> {

    /**
     * @Description: 筛选一条可给出的小微数据
     * <p>
     *     从最小的主键ID开始筛选，判断今日加好友是否超过上限，如果超过上限则循环一次判断
     * </p>
     * @param model
     * @return
     * @author yoko
     * @date 2020/7/30 17:15
    */
    public WxModel screenWx(WxModel model);
}
