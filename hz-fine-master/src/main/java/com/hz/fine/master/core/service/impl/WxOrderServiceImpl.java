package com.hz.fine.master.core.service.impl;

import com.hz.fine.master.core.common.dao.BaseDao;
import com.hz.fine.master.core.common.service.impl.BaseServiceImpl;
import com.hz.fine.master.core.mapper.WxOrderMapper;
import com.hz.fine.master.core.service.WxOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description 小微给出订单记录的Service层的实现层
 * @Author yoko
 * @Date 2020/8/8 15:08
 * @Version 1.0
 */
@Service
public class WxOrderServiceImpl<T> extends BaseServiceImpl<T> implements WxOrderService<T> {
    /**
     * 5分钟.
     */
    public long FIVE_MIN = 300;

    public long TWO_HOUR = 2;

    @Autowired
    private WxOrderMapper wxOrderMapper;

    public BaseDao<T> getDao() {
        return wxOrderMapper;
    }
}
