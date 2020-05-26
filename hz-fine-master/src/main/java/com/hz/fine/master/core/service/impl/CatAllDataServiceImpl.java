package com.hz.fine.master.core.service.impl;

import com.hz.fine.master.core.common.dao.BaseDao;
import com.hz.fine.master.core.common.service.impl.BaseServiceImpl;
import com.hz.fine.master.core.mapper.CatAllDataMapper;
import com.hz.fine.master.core.service.CatAllDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description 可爱猫回调原始数据的Service层的实现层
 * @Author yoko
 * @Date 2020/5/26 10:27
 * @Version 1.0
 */
@Service
public class CatAllDataServiceImpl<T> extends BaseServiceImpl<T> implements CatAllDataService<T> {
    /**
     * 5分钟.
     */
    public long FIVE_MIN = 300;

    public long TWO_HOUR = 2;

    @Autowired
    private CatAllDataMapper catAllDataMapper;

    public BaseDao<T> getDao() {
        return catAllDataMapper;
    }
}
