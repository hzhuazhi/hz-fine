package com.hz.fine.master.core.mapper;

import com.hz.fine.master.core.common.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Description 银行转账信息的Dao层
 * @Author yoko
 * @Date 2020/5/18 19:47
 * @Version 1.0
 */
@Mapper
public interface BankTransferMapper<T> extends BaseDao<T> {
}
