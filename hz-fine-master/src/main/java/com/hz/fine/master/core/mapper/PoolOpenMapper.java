package com.hz.fine.master.core.mapper;

import com.hz.fine.master.core.common.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Description 接单池子正在接单的Dao层
 * @Author yoko
 * @Date 2020/8/13 14:55
 * @Version 1.0
 */
@Mapper
public interface PoolOpenMapper<T> extends BaseDao<T> {
}
