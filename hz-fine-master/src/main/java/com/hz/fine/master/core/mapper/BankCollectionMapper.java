package com.hz.fine.master.core.mapper;

import com.hz.fine.master.core.common.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Description 银行收款信息的Dao层
 * @Author yoko
 * @Date 2020/5/21 17:00
 * @Version 1.0
 */
@Mapper
public interface BankCollectionMapper<T> extends BaseDao<T> {
}
