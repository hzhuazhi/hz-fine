package com.hz.fine.master.core.service.impl;

import com.hz.fine.master.core.common.dao.BaseDao;
import com.hz.fine.master.core.common.service.impl.BaseServiceImpl;
import com.hz.fine.master.core.mapper.UpgradeMapper;
import com.hz.fine.master.core.model.upgrade.UpgradeModel;
import com.hz.fine.master.core.service.UpgradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description 客户端升级更新的Service层的实现层
 * @Author yoko
 * @Date 2020/1/14 13:49
 * @Version 1.0
 */
@Service
public class UpgradeServiceImpl<T> extends BaseServiceImpl<T> implements UpgradeService<T> {
    /**
     * 5分钟.
     */
    public long FIVE_MIN = 300;

    public long TWO_HOUR = 2;

    @Autowired
    private UpgradeMapper upgradeMapper;


    public BaseDao<T> getDao() {
        return upgradeMapper;
    }

    @Override
    public UpgradeModel getMaxUpgradeData(UpgradeModel model) {
        return upgradeMapper.getMaxUpgradeData(model);
    }
}
