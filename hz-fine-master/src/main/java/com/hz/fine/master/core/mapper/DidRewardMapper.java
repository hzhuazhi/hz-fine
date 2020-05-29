package com.hz.fine.master.core.mapper;

import com.hz.fine.master.core.common.dao.BaseDao;
import com.hz.fine.master.core.model.did.DidRewardModel;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Description 用户奖励纪录的Dao层
 * @Author yoko
 * @Date 2020/5/21 17:26
 * @Version 1.0
 */
@Mapper
public interface DidRewardMapper<T> extends BaseDao<T> {

    /**
     * @Description: 获取用户收益
     * <p>sum查询出来的数据</p>
     * @param model - 根据用户，用户收益类型，日期查询总收益
     * @return
     * @author yoko
     * @date 2020/5/29 11:23
    */
    public String getProfitByRewardType(DidRewardModel model);
}
