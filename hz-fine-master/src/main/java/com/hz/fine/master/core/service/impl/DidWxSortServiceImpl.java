package com.hz.fine.master.core.service.impl;

import com.hz.fine.master.core.common.dao.BaseDao;
import com.hz.fine.master.core.common.service.impl.BaseServiceImpl;
import com.hz.fine.master.core.mapper.DidWxSortMapper;
import com.hz.fine.master.core.model.did.DidWxSortModel;
import com.hz.fine.master.core.service.DidWxSortService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description 用户的微信出码排序的Service层的实现层
 * @Author yoko
 * @Date 2020/8/31 16:41
 * @Version 1.0
 */
@Service
public class DidWxSortServiceImpl<T> extends BaseServiceImpl<T> implements DidWxSortService<T> {
    /**
     * 5分钟.
     */
    public long FIVE_MIN = 300;

    public long TWO_HOUR = 2;

    @Autowired
    private DidWxSortMapper didWxSortMapper;

    public BaseDao<T> getDao() {
        return didWxSortMapper;
    }

    @Override
    public int maxSort(DidWxSortModel model) {
        return didWxSortMapper.maxSort(model);
    }

    @Override
    public int addBySort(DidWxSortModel model) {
        return didWxSortMapper.addBySort(model);
    }

    @Override
    public int updateInUse(DidWxSortModel model) {
        return didWxSortMapper.updateInUse(model);
    }

    @Override
    public int addByExist(DidWxSortModel model) {
        DidWxSortModel didWxSortModel = (DidWxSortModel)didWxSortMapper.findByObject(model);
        if (didWxSortModel != null && didWxSortModel.getId() != null && didWxSortModel.getId() > 0){
            return 0;
        }else {
            return didWxSortMapper.addBySort(model);
        }
    }
}
