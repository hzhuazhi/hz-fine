package com.hz.fine.master.core.service.impl;

import com.hz.fine.master.core.common.dao.BaseDao;
import com.hz.fine.master.core.common.service.impl.BaseServiceImpl;
import com.hz.fine.master.core.mapper.DidMapper;
import com.hz.fine.master.core.model.did.DidModel;
import com.hz.fine.master.core.service.DidService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description 用户Service层的实现层
 * @Author yoko
 * @Date 2020/5/13 18:34
 * @Version 1.0
 */
@Service
public class DidServiceImpl<T> extends BaseServiceImpl<T> implements DidService<T> {
    /**
     * 5分钟.
     */
    public long FIVE_MIN = 300;

    public long TWO_HOUR = 2;

    @Autowired
    private DidMapper didMapper;

    public BaseDao<T> getDao() {
        return didMapper;
    }

    @Override
    public List<DidModel> getEffectiveDidList(DidModel model) {
        return didMapper.getEffectiveDidList(model);
    }

    @Override
    public int updateDidMoneyByOrder(DidModel model) {
        return didMapper.updateDidMoneyByOrder(model);
    }

    @Override
    public List<DidModel> getEffectiveDidByZfbList(DidModel model) {
        return didMapper.getEffectiveDidByZfbList(model);
    }

    @Override
    public int updateDidBalance(DidModel model) {
        return didMapper.updateDidBalance(model);
    }

    @Override
    public List<DidModel> getEffectiveDidByWxGroupList(DidModel model) {
        return didMapper.getEffectiveDidByWxGroupList(model);
    }

    @Override
    public int updateDidGroupNumOrSwitchType(DidModel model) {
        return didMapper.updateDidGroupNumOrSwitchType(model);
    }

    @Override
    public List<DidModel> getNewEffectiveDidByWxGroupList(DidModel model) {
        return didMapper.getNewEffectiveDidByWxGroupList(model);
    }
}
