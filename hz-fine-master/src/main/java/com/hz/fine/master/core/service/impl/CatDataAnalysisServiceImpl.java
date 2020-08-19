package com.hz.fine.master.core.service.impl;

import com.hz.fine.master.core.common.dao.BaseDao;
import com.hz.fine.master.core.common.service.impl.BaseServiceImpl;
import com.hz.fine.master.core.mapper.CatDataAnalysisMapper;
import com.hz.fine.master.core.service.CatDataAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description 可爱猫数据解析的Service层的实现层
 * @Author yoko
 * @Date 2020/7/21 19:52
 * @Version 1.0
 */
@Service
public class CatDataAnalysisServiceImpl<T> extends BaseServiceImpl<T> implements CatDataAnalysisService<T> {
    /**
     * 5分钟.
     */
    public long FIVE_MIN = 300;

    public long TWO_HOUR = 2;

    @Autowired
    private CatDataAnalysisMapper catDataAnalysisMapper;

    public BaseDao<T> getDao() {
        return catDataAnalysisMapper;
    }
}
