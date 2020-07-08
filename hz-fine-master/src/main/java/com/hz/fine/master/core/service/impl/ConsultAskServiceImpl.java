package com.hz.fine.master.core.service.impl;

import com.hz.fine.master.core.common.dao.BaseDao;
import com.hz.fine.master.core.common.service.impl.BaseServiceImpl;
import com.hz.fine.master.core.mapper.ConsultAskMapper;
import com.hz.fine.master.core.service.ConsultAskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description 在线客服、咨询的发问的Service层的实现层
 * @Author yoko
 * @Date 2020/7/8 20:10
 * @Version 1.0
 */
@Service
public class ConsultAskServiceImpl<T> extends BaseServiceImpl<T> implements ConsultAskService<T> {
    /**
     * 5分钟.
     */
    public long FIVE_MIN = 300;

    public long TWO_HOUR = 2;

    @Autowired
    private ConsultAskMapper consultAskMapper;

    public BaseDao<T> getDao() {
        return consultAskMapper;
    }
}
