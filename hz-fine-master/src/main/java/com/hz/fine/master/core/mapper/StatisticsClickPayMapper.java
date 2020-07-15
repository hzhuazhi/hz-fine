package com.hz.fine.master.core.mapper;

import com.hz.fine.master.core.common.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Description 支付用户点击支付页统计的Dao层
 * @Author yoko
 * @Date 2020/7/15 18:34
 * @Version 1.0
 */
@Mapper
public interface StatisticsClickPayMapper<T> extends BaseDao<T> {
}
