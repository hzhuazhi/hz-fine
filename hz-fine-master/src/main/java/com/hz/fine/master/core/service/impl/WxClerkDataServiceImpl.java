package com.hz.fine.master.core.service.impl;

import com.hz.fine.master.core.common.dao.BaseDao;
import com.hz.fine.master.core.common.service.impl.BaseServiceImpl;
import com.hz.fine.master.core.mapper.WxClerkDataMapper;
import com.hz.fine.master.core.service.WxClerkDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description 小微旗下店员上下线纪录的Service层
 * @Author yoko
 * @Date 2020/5/26 10:06
 * @Version 1.0
 */
@Service
public class WxClerkDataServiceImpl<T> extends BaseServiceImpl<T> implements WxClerkDataService<T> {
    /**
     * 5分钟.
     */
    public long FIVE_MIN = 300;

    public long TWO_HOUR = 2;

    @Autowired
    private WxClerkDataMapper wxClerkDataMapper;

    public BaseDao<T> getDao() {
        return wxClerkDataMapper;
    }
}
