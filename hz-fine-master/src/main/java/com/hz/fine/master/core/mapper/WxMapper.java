package com.hz.fine.master.core.mapper;

import com.hz.fine.master.core.common.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Description 小微管理的Dao层
 * @Author yoko
 * @Date 2020/5/25 16:24
 * @Version 1.0
 */
@Mapper
public interface WxMapper<T> extends BaseDao<T> {
}
