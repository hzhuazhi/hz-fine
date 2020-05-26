package com.hz.fine.master.core.mapper;

import com.hz.fine.master.core.common.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Description 可爱猫回调原始数据的Dao层
 * @Author yoko
 * @Date 2020/5/26 10:23
 * @Version 1.0
 */
@Mapper
public interface CatAllDataMapper<T> extends BaseDao<T> {
}
