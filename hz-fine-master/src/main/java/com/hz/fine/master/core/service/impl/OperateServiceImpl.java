package com.hz.fine.master.core.service.impl;

import com.hz.fine.master.core.common.dao.BaseDao;
import com.hz.fine.master.core.common.service.impl.BaseServiceImpl;
import com.hz.fine.master.core.mapper.OperateMapper;
import com.hz.fine.master.core.service.OperateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description 运营的Service层的实现层
 * @Author yoko
 * @Date 2020/7/23 14:18
 * @Version 1.0
 */
@Service
public class OperateServiceImpl<T> extends BaseServiceImpl<T> implements OperateService<T> {
    /**
     * 5分钟.
     */
    public long FIVE_MIN = 300;

    public long TWO_HOUR = 2;

    @Autowired
    private OperateMapper operateMapper;

    public BaseDao<T> getDao() {
        return operateMapper;
    }
}
