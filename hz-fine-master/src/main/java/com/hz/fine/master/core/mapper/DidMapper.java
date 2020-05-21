package com.hz.fine.master.core.mapper;

import com.hz.fine.master.core.common.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Description 用户的Dao层
 * @Author yoko
 * @Date 2020/5/13 18:31
 * @Version 1.0
 */
@Mapper
public interface DidMapper<T> extends BaseDao<T> {
}
