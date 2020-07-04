package com.hz.fine.master.core.mapper;

import com.hz.fine.master.core.common.dao.BaseDao;
import com.hz.fine.master.core.model.upgrade.UpgradeModel;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Description 客户端升级更新的Dao层
 * @Author yoko
 * @Date 2020/1/14 13:49
 * @Version 1.0
 */
@Mapper
public interface UpgradeMapper<T> extends BaseDao<T> {

    /**
     * @Description: 根据用户版本号查询最大版本号的客户端更新信息
     * @param model
     * @return UpgradeModel
     * @author yoko
     * @date 2020/1/14 14:33
     */
    public UpgradeModel getMaxUpgradeData(UpgradeModel model);
}
