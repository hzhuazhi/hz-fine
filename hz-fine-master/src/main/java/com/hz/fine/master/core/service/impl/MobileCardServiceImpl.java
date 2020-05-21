package com.hz.fine.master.core.service.impl;

import com.hz.fine.master.core.common.dao.BaseDao;
import com.hz.fine.master.core.common.service.impl.BaseServiceImpl;
import com.hz.fine.master.core.mapper.MobileCardMapper;
import com.hz.fine.master.core.service.MobileCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description 手机卡的Service层的实现层
 * @Author yoko
 * @Date 2020/5/18 17:23
 * @Version 1.0
 */
@Service
public class MobileCardServiceImpl<T> extends BaseServiceImpl<T> implements MobileCardService<T> {
    /**
     * 5分钟.
     */
    public long FIVE_MIN = 300;

    public long TWO_HOUR = 2;

    @Autowired
    private MobileCardMapper mobileCardMapper;

    public BaseDao<T> getDao() {
        return mobileCardMapper;
    }
}
