package com.hz.fine.master.core.mapper;

import com.hz.fine.master.core.common.dao.BaseDao;
import com.hz.fine.master.core.model.did.DidModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Description 用户的Dao层
 * @Author yoko
 * @Date 2020/5/13 18:31
 * @Version 1.0
 */
@Mapper
public interface DidMapper<T> extends BaseDao<T> {

    /**
     * @Description: 查询出可派单的用户集合
     * @param model
     * @return
     * @author yoko
     * @date 2020/5/25 10:42
     */
    public List<DidModel> getEffectiveDidList(DidModel model);
}
