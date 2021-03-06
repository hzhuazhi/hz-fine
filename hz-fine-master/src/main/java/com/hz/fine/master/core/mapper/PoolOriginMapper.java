package com.hz.fine.master.core.mapper;

import com.hz.fine.master.core.common.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Description 接单池子被移出的起源的Dao层
 * @Author yoko
 * @Date 2020/8/13 14:58
 * @Version 1.0
 */
@Mapper
public interface PoolOriginMapper<T> extends BaseDao<T> {
}
