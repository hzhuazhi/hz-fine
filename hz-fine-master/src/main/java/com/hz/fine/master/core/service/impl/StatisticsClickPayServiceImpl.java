package com.hz.fine.master.core.service.impl;

import com.hz.fine.master.core.common.dao.BaseDao;
import com.hz.fine.master.core.common.service.impl.BaseServiceImpl;
import com.hz.fine.master.core.mapper.StatisticsClickPayMapper;
import com.hz.fine.master.core.service.StatisticsClickPayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description 支付用户点击支付页统计的Service层的实现层
 * @Author yoko
 * @Date 2020/7/15 18:35
 * @Version 1.0
 */
@Service
public class StatisticsClickPayServiceImpl<T> extends BaseServiceImpl<T> implements StatisticsClickPayService<T> {
    /**
     * 5分钟.
     */
    public long FIVE_MIN = 300;

    public long TWO_HOUR = 2;

    @Autowired
    private StatisticsClickPayMapper statisticsClickPayMapper;

    public BaseDao<T> getDao() {
        return statisticsClickPayMapper;
    }
}
