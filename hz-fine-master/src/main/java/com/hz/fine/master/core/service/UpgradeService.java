package com.hz.fine.master.core.service;


import com.hz.fine.master.core.common.service.BaseService;
import com.hz.fine.master.core.model.upgrade.UpgradeModel;

/**
 * @Description 客户端升级更新的Service层
 * @Author yoko
 * @Date 2020/1/14 13:49
 * @Version 1.0
 */
public interface UpgradeService<T> extends BaseService<T> {

    /**
     * @Description: 根据用户版本号查询最大版本号的客户端更新信息
     * @param model
     * @return UpgradeModel
     * @author yoko
     * @date 2020/1/14 14:33
    */
    public UpgradeModel getMaxUpgradeData(UpgradeModel model);
}
