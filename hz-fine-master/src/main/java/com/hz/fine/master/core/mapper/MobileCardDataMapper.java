package com.hz.fine.master.core.mapper;

import com.hz.fine.master.core.common.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Description 手机卡所有短信内容数据的Dao层
 * @Author yoko
 * @Date 2020/5/18 17:52
 * @Version 1.0
 */
@Mapper
public interface MobileCardDataMapper<T> extends BaseDao<T> {
}
