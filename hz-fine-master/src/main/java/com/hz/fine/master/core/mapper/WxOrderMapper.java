package com.hz.fine.master.core.mapper;

import com.hz.fine.master.core.common.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Description 小微给出订单记录的Dao层
 * @Author yoko
 * @Date 2020/8/8 15:06
 * @Version 1.0
 */
@Mapper
public interface WxOrderMapper<T> extends BaseDao<T> {
}
