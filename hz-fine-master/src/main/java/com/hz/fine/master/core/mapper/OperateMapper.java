package com.hz.fine.master.core.mapper;

import com.hz.fine.master.core.common.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Description 运营的Dao层
 * @Author yoko
 * @Date 2020/7/23 14:15
 * @Version 1.0
 */
@Mapper
public interface OperateMapper<T> extends BaseDao<T> {
}
