package com.hz.fine.master.core.mapper;

import com.hz.fine.master.core.common.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Description 银行卡收款回调数据的Dao层
 * @Author yoko
 * @Date 2020/5/19 10:46
 * @Version 1.0
 */
@Mapper
public interface BankCollectionDataMapper<T> extends BaseDao<T> {
}
