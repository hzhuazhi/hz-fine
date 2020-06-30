package com.hz.fine.master.core.mapper;

import com.hz.fine.master.core.common.dao.BaseDao;
import com.hz.fine.master.core.model.did.DidOnoffModel;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Description 用户抢单上下线的Dao层
 * @Author yoko
 * @Date 2020/6/30 17:16
 * @Version 1.0
 */
@Mapper
public interface DidOnoffMapper<T> extends BaseDao<T> {

    /**
     * @Description: 更新用户抢单的上下线
     * @param model
     * @return
     * @author yoko
     * @date 2020/6/30 17:26
     */
    public int updateDidOnoff(DidOnoffModel model);
}
