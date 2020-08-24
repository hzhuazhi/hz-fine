package com.hz.fine.master.core.service.impl;

import com.hz.fine.master.core.common.dao.BaseDao;
import com.hz.fine.master.core.common.service.impl.BaseServiceImpl;
import com.hz.fine.master.core.mapper.DidWxMonitorMapper;
import com.hz.fine.master.core.model.did.DidWxMonitorModel;
import com.hz.fine.master.core.service.DidWxMonitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description 用户的微信收款账号金额监控的Service层的实现层
 * @Author yoko
 * @Date 2020/8/23 19:36
 * @Version 1.0
 */
@Service
public class DidWxMonitorServiceImpl<T> extends BaseServiceImpl<T> implements DidWxMonitorService<T> {
    /**
     * 5分钟.
     */
    public long FIVE_MIN = 300;

    public long TWO_HOUR = 2;

    @Autowired
    private DidWxMonitorMapper didWxMonitorMapper;

    public BaseDao<T> getDao() {
        return didWxMonitorMapper;
    }

    @Override
    public List<String> getToWxidList(DidWxMonitorModel model) {
        return didWxMonitorMapper.getToWxidList(model);
    }
}
