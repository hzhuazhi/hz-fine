package com.hz.fine.master.core.service.impl;

import com.hz.fine.master.core.common.dao.BaseDao;
import com.hz.fine.master.core.common.service.impl.BaseServiceImpl;
import com.hz.fine.master.core.mapper.DidLevelMapper;
import com.hz.fine.master.core.service.DidLevelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description 用户的层级关系的Service层的实现层
 * @Author yoko
 * @Date 2020/5/14 17:54
 * @Version 1.0
 */
@Service
public class DidLevelServiceImpl<T> extends BaseServiceImpl<T> implements DidLevelService<T> {
    /**
     * 5分钟.
     */
    public long FIVE_MIN = 300;

    public long TWO_HOUR = 2;

    @Autowired
    private DidLevelMapper didLevelMapper;

    public BaseDao<T> getDao() {
        return didLevelMapper;
    }
}
