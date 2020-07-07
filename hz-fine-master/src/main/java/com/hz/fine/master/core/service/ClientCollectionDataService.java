package com.hz.fine.master.core.service;


import com.hz.fine.master.core.common.service.BaseService;
import com.hz.fine.master.core.model.client.ClientCollectionDataModel;

/**
 * @Description 客户端监听的收款信息：存储所有收款信息的Service层
 * @Author yoko
 * @Date 2020/7/7 15:27
 * @Version 1.0
 */
public interface ClientCollectionDataService<T> extends BaseService<T> {

    /**
     * @Description: 获取当前系统时间前5分钟以为创建时间最大的那条数据
     * @param model
     * @return
     * @author yoko
     * @date 2020/7/7 19:30
     */
    public ClientCollectionDataModel getClientCollectionDataByCreateTime(ClientCollectionDataModel model);
}
