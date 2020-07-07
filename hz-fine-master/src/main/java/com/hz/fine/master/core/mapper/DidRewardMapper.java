package com.hz.fine.master.core.mapper;

import com.hz.fine.master.core.common.dao.BaseDao;
import com.hz.fine.master.core.model.did.DidRewardModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

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
    
    
    /**
     * @Description: 获取用户直推奖励的数据的总行数-分页
     * <p>
     *     分页使用，配合方法:getShareList
     * </p>
     * @param model - 根据用户获取分享奖励（直推奖励的数据集合的总行数）
     * @return 
     * @author yoko
     * @date 2020/6/1 16:41
    */
    public int countShare(DidRewardModel model);


    /**
     * @Description: 获取用户直推奖励的数据集合
     * <p>数据要根据直推人的名称进行group by</p>
     * @param model - 根据用户获取分享奖励（直推奖励的数据集合）
     * @return
     * @author yoko
     * @date 2020/6/1 16:30 
     */
    public List<DidRewardModel> getShareList(DidRewardModel model);

    /**
     * @Description: 根据订单号，奖励类型查询奖励数据
     * @param model
     * @return
     * @author yoko
     * @date 2020/7/7 20:15
    */
    public DidRewardModel getDidRewardByOrderNoAndType(DidRewardModel model);
}
