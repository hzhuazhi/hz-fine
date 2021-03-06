package com.hz.fine.master.core.mapper;

import com.hz.fine.master.core.common.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Description 在线客服、咨询的Dao层
 * @Author yoko
 * @Date 2020/7/6 11:22
 * @Version 1.0
 */
@Mapper
public interface ConsultMapper<T> extends BaseDao<T> {
}
