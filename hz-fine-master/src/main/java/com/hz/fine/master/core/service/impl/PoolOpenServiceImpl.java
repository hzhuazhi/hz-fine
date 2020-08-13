package com.hz.fine.master.core.service.impl;

import com.hz.fine.master.core.common.dao.BaseDao;
import com.hz.fine.master.core.common.service.impl.BaseServiceImpl;
import com.hz.fine.master.core.mapper.PoolOpenMapper;
import com.hz.fine.master.core.service.PoolOpenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description 接单池子正在接单的Service层的实现层
 * @Author yoko
 * @Date 2020/8/13 15:27
 * @Version 1.0
 */
@Service
public class PoolOpenServiceImpl<T> extends BaseServiceImpl<T> implements PoolOpenService<T> {
    /**
     * 5分钟.
     */
    public long FIVE_MIN = 300;

    public long TWO_HOUR = 2;

    @Autowired
    private PoolOpenMapper poolOpenMapper;

    public BaseDao<T> getDao() {
        return poolOpenMapper;
    }
}
