package com.hz.fine.master.core.service.impl;

import com.hz.fine.master.core.common.dao.BaseDao;
import com.hz.fine.master.core.common.service.impl.BaseServiceImpl;
import com.hz.fine.master.core.mapper.ClientCollectionDataMapper;
import com.hz.fine.master.core.model.client.ClientCollectionDataModel;
import com.hz.fine.master.core.service.ClientCollectionDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description 客户端监听的收款信息：存储所有收款信息的Service层的实现层
 * @Author yoko
 * @Date 2020/7/7 15:42
 * @Version 1.0
 */
@Service
public class ClientCollectionDataServiceImpl<T> extends BaseServiceImpl<T> implements ClientCollectionDataService<T> {
    /**
     * 5分钟.
     */
    public long FIVE_MIN = 300;

    public long TWO_HOUR = 2;

    @Autowired
    private ClientCollectionDataMapper clientCollectionDataMapper;

    public BaseDao<T> getDao() {
        return clientCollectionDataMapper;
    }

    @Override
    public ClientCollectionDataModel getClientCollectionDataByCreateTime(ClientCollectionDataModel model) {
        return clientCollectionDataMapper.getClientCollectionDataByCreateTime(model);
    }
}
