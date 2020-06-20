package com.hz.fine.master.core.mapper;

import com.hz.fine.master.core.common.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Description 用户扣减余额流水的Dao层
 * @Author yoko
 * @Date 2020/6/20 12:05
 * @Version 1.0
 */
@Mapper
public interface DidBalanceDeductMapper<T> extends BaseDao<T> {
}
