package com.hz.fine.master.core.service;

import com.hz.fine.master.core.common.service.BaseService;
import com.hz.fine.master.core.model.did.DidRewardModel;

import java.util.List;

/**
 * @Description 用户奖励纪录的Service层
 * @Author yoko
 * @Date 2020/5/21 17:23
 * @Version 1.0
 */
public interface DidRewardService<T> extends BaseService<T> {

    /**
     * @Description: 获取用户收益
     * <p>sum查询出来的数据</p>
     * @param model - 根据用户，用户收益类型，日期查询总收益
     * @return
     * @author yoko
     * @date 2020/5/29 11:23
     */
    public String getProfitByRewardType(DidRewardModel model);


    /**
     * @Description: 获取用户直推奖励的数据集合
     * <p>数据要根据直推人的名称进行group by</p>
     * @param model - 根据用户获取分享奖励（直推奖励的数据集合）
     * @return
     * @author yoko
     * @date 2020/6/1 16:30
    */
    public List<DidRewardModel> getShareList(DidRewardModel model);
}
