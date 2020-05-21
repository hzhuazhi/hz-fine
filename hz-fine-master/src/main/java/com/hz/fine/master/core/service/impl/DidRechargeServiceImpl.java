package com.hz.fine.master.core.service.impl;

import com.hz.fine.master.core.common.dao.BaseDao;
import com.hz.fine.master.core.common.service.impl.BaseServiceImpl;
import com.hz.fine.master.core.mapper.DidRechargeMapper;
import com.hz.fine.master.core.service.DidRechargeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description 用户充值记录的Service层
 * @Author yoko
 * @Date 2020/5/21 14:15
 * @Version 1.0
 */
@Service
public class DidRechargeServiceImpl <T> extends BaseServiceImpl<T> implements DidRechargeService<T> {
    /**
     * 5分钟.
     */
    public long FIVE_MIN = 300;

    public long TWO_HOUR = 2;

    @Autowired
    private DidRechargeMapper didRechargeMapper;

    public BaseDao<T> getDao() {
        return didRechargeMapper;
    }
}
