package com.hz.fine.master.core.service.impl;

import com.hz.fine.master.core.common.dao.BaseDao;
import com.hz.fine.master.core.common.service.impl.BaseServiceImpl;
import com.hz.fine.master.core.mapper.DidOnoffMapper;
import com.hz.fine.master.core.model.did.DidOnoffModel;
import com.hz.fine.master.core.service.DidOnoffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description 用户抢单上下线的Service层的实现层
 * @Author yoko
 * @Date 2020/6/30 17:18
 * @Version 1.0
 */
@Service
public class DidOnoffServiceImpl<T> extends BaseServiceImpl<T> implements DidOnoffService<T> {
    /**
     * 5分钟.
     */
    public long FIVE_MIN = 300;

    public long TWO_HOUR = 2;

    @Autowired
    private DidOnoffMapper didOnoffMapper;

    public BaseDao<T> getDao() {
        return didOnoffMapper;
    }

    @Override
    public int updateDidOnoff(DidOnoffModel model) {
        return didOnoffMapper.updateDidOnoff(model);
    }
}
