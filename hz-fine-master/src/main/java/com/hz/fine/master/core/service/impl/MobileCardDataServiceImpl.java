package com.hz.fine.master.core.service.impl;

import com.hz.fine.master.core.common.dao.BaseDao;
import com.hz.fine.master.core.common.service.impl.BaseServiceImpl;
import com.hz.fine.master.core.mapper.MobileCardDataMapper;
import com.hz.fine.master.core.service.MobileCardDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description 手机卡所有短信内容数据的Service层的实现层
 * @Author yoko
 * @Date 2020/5/18 17:54
 * @Version 1.0
 */
@Service
public class MobileCardDataServiceImpl<T> extends BaseServiceImpl<T> implements MobileCardDataService<T> {
    /**
     * 5分钟.
     */
    public long FIVE_MIN = 300;

    public long TWO_HOUR = 2;

    @Autowired
    private MobileCardDataMapper mobileCardDataMapper;

    public BaseDao<T> getDao() {
        return mobileCardDataMapper;
    }
}
