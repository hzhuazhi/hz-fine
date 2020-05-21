package com.hz.fine.master.core.service.impl;

import com.hz.fine.master.core.common.dao.BaseDao;
import com.hz.fine.master.core.common.service.impl.BaseServiceImpl;
import com.hz.fine.master.core.mapper.BankCollectionDataMapper;
import com.hz.fine.master.core.service.BankCollectionDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description 银行卡收款回调数据的Service层的实现层
 * @Author yoko
 * @Date 2020/5/19 10:47
 * @Version 1.0
 */
@Service
public class BankCollectionDataServiceImpl<T> extends BaseServiceImpl<T> implements BankCollectionDataService<T> {
    /**
     * 5分钟.
     */
    public long FIVE_MIN = 300;

    public long TWO_HOUR = 2;

    @Autowired
    private BankCollectionDataMapper bankCollectionDataMapper;

    public BaseDao<T> getDao() {
        return bankCollectionDataMapper;
    }
}
