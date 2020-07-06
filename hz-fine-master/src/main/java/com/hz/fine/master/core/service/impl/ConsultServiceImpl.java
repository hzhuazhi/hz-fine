package com.hz.fine.master.core.service.impl;

import com.hz.fine.master.core.common.dao.BaseDao;
import com.hz.fine.master.core.common.service.impl.BaseServiceImpl;
import com.hz.fine.master.core.mapper.ConsultMapper;
import com.hz.fine.master.core.service.ConsultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description 在线客服、咨询的Service层的实现层
 * @Author yoko
 * @Date 2020/7/6 11:52
 * @Version 1.0
 */
@Service
public class ConsultServiceImpl<T> extends BaseServiceImpl<T> implements ConsultService<T> {
    /**
     * 5分钟.
     */
    public long FIVE_MIN = 300;

    public long TWO_HOUR = 2;

    @Autowired
    private ConsultMapper consultMapper;

    public BaseDao<T> getDao() {
        return consultMapper;
    }
}
