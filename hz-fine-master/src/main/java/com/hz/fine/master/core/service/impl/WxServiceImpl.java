package com.hz.fine.master.core.service.impl;

import com.hz.fine.master.core.common.dao.BaseDao;
import com.hz.fine.master.core.common.service.impl.BaseServiceImpl;
import com.hz.fine.master.core.mapper.WxMapper;
import com.hz.fine.master.core.service.WxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description 小微管理的Service层的实现层
 * @Author yoko
 * @Date 2020/5/25 16:27
 * @Version 1.0
 */
@Service
public class WxServiceImpl<T> extends BaseServiceImpl<T> implements WxService<T> {
    /**
     * 5分钟.
     */
    public long FIVE_MIN = 300;

    public long TWO_HOUR = 2;

    @Autowired
    private WxMapper wxMapper;

    public BaseDao<T> getDao() {
        return wxMapper;
    }
}
