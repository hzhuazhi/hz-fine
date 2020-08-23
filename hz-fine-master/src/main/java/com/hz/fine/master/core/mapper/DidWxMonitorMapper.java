package com.hz.fine.master.core.mapper;

import com.hz.fine.master.core.common.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Description 用户的微信收款账号金额监控的Dao层
 * @Author yoko
 * @Date 2020/8/23 19:34
 * @Version 1.0
 */
@Mapper
public interface DidWxMonitorMapper<T> extends BaseDao<T> {
}
