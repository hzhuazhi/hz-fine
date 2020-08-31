package com.hz.fine.master.core.mapper;

import com.hz.fine.master.core.common.dao.BaseDao;
import com.hz.fine.master.core.model.did.DidWxSortModel;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Description 用户的微信出码排序的Dao层
 * @Author yoko
 * @Date 2020/8/31 16:17
 * @Version 1.0
 */
@Mapper
public interface DidWxSortMapper<T> extends BaseDao<T> {

    /**
     * @Description: 获取最大的排序
     * @param model
     * @return 
     * @author yoko
     * @date 2020/8/31 16:32
    */
    public int maxSort(DidWxSortModel model);

    /**
     * @Description: 添加用户的微信出码排序的数据
     * <p>
     *     自动累加排序
     * </p>
     * @param model
     * @return
     * @author yoko
     * @date 2020/8/31 16:38
    */
    public int addBySort(DidWxSortModel model);

    /**
     * @Description: 更新使用中的状态
     * @param model
     * @return 
     * @author yoko
     * @date 2020/8/31 17:05 
    */
    public int updateInUse(DidWxSortModel model);



}
