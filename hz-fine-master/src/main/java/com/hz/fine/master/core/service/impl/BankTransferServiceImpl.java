package com.hz.fine.master.core.service.impl;

import com.hz.fine.master.core.common.dao.BaseDao;
import com.hz.fine.master.core.common.service.impl.BaseServiceImpl;
import com.hz.fine.master.core.mapper.BankTransferMapper;
import com.hz.fine.master.core.service.BankTransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description 银行转账信息的Service层的实现层
 * @Author yoko
 * @Date 2020/5/18 19:48
 * @Version 1.0
 */
@Service
public class BankTransferServiceImpl<T> extends BaseServiceImpl<T> implements BankTransferService<T> {
    /**
     * 5分钟.
     */
    public long FIVE_MIN = 300;

    public long TWO_HOUR = 2;

    @Autowired
    private BankTransferMapper bankTransferMapper;

    public BaseDao<T> getDao() {
        return bankTransferMapper;
    }
}
