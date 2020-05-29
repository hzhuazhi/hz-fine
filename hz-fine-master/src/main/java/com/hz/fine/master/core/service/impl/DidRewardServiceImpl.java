package com.hz.fine.master.core.service.impl;

import com.hz.fine.master.core.common.dao.BaseDao;
import com.hz.fine.master.core.common.service.impl.BaseServiceImpl;
import com.hz.fine.master.core.mapper.DidRewardMapper;
import com.hz.fine.master.core.model.did.DidRewardModel;
import com.hz.fine.master.core.service.DidRewardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description 用户奖励纪录的Service层的实现层
 * @Author yoko
 * @Date 2020/5/21 17:25
 * @Version 1.0
 */
@Service
public class DidRewardServiceImpl<T> extends BaseServiceImpl<T> implements DidRewardService<T> {
    /**
     * 5分钟.
     */
    public long FIVE_MIN = 300;

    public long TWO_HOUR = 2;

    @Autowired
    private DidRewardMapper didRewardMapper;

    public BaseDao<T> getDao() {
        return didRewardMapper;
    }

    @Override
    public String getProfitByRewardType(DidRewardModel model) {
        return didRewardMapper.getProfitByRewardType(model);
    }
}
