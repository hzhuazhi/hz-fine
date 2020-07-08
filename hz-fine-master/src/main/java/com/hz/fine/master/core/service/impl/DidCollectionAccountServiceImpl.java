package com.hz.fine.master.core.service.impl;

import com.hz.fine.master.core.common.dao.BaseDao;
import com.hz.fine.master.core.common.service.impl.BaseServiceImpl;
import com.hz.fine.master.core.mapper.DidCollectionAccountMapper;
import com.hz.fine.master.core.model.did.DidCollectionAccountModel;
import com.hz.fine.master.core.service.DidCollectionAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description 用户的收款账号的Service层的实现层
 * @Author yoko
 * @Date 2020/5/15 14:02
 * @Version 1.0
 */
@Service
public class DidCollectionAccountServiceImpl<T> extends BaseServiceImpl<T> implements DidCollectionAccountService<T> {
    /**
     * 5分钟.
     */
    public long FIVE_MIN = 300;

    public long TWO_HOUR = 2;

    @Autowired
    private DidCollectionAccountMapper didCollectionAccountMapper;

    public BaseDao<T> getDao() {
        return didCollectionAccountMapper;
    }

    @Override
    public void updateBasic(DidCollectionAccountModel model) {
        didCollectionAccountMapper.updateBasic(model);
    }

    @Override
    public void updateDidCollectionAccount(DidCollectionAccountModel model) {
        didCollectionAccountMapper.updateDidCollectionAccount(model);
    }

    @Override
    public void updateDidCollectionAccountZfb(DidCollectionAccountModel model) {
        didCollectionAccountMapper.updateDidCollectionAccountZfb(model);
    }
}
