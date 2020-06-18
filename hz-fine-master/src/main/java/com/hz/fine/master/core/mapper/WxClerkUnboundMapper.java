package com.hz.fine.master.core.mapper;

import com.hz.fine.master.core.common.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Description 小微需要解绑店员的Dao层
 * @Author yoko
 * @Date 2020/6/18 0:57
 * @Version 1.0
 */
@Mapper
public interface WxClerkUnboundMapper<T> extends BaseDao<T> {
}
