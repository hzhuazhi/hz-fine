package com.hz.fine.master.core.mapper;

import com.hz.fine.master.core.common.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Description 策略表：关于一些策略配置的部署的Dao层
 * @Author yoko
 * @Date 2020/5/19 13:56
 * @Version 1.0
 */
@Mapper
public interface StrategyMapper<T> extends BaseDao<T> {
}
