package com.hz.fine.master.core.service.impl;

import com.hz.fine.master.core.common.dao.BaseDao;
import com.hz.fine.master.core.common.service.impl.BaseServiceImpl;
import com.hz.fine.master.core.mapper.WxClerkUnboundMapper;
import com.hz.fine.master.core.service.WxClerkUnboundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description 小微需要解绑店员的Service层的实现层
 * @Author yoko
 * @Date 2020/6/18 10:22
 * @Version 1.0
 */
@Service
public class WxClerkUnboundServiceImpl<T> extends BaseServiceImpl<T> implements WxClerkUnboundService<T> {
    /**
     * 5分钟.
     */
    public long FIVE_MIN = 300;

    public long TWO_HOUR = 2;

    @Autowired
    private WxClerkUnboundMapper wxClerkUnboundMapper;

    public BaseDao<T> getDao() {
        return wxClerkUnboundMapper;
    }
}
