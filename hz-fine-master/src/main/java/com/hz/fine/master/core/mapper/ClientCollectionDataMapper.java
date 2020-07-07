package com.hz.fine.master.core.mapper;

import com.hz.fine.master.core.common.dao.BaseDao;
import com.hz.fine.master.core.model.client.ClientCollectionDataModel;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Description 客户端监听的收款信息：存储所有收款信息的Dao层
 * @Author yoko
 * @Date 2020/7/7 15:18
 * @Version 1.0
 */
@Mapper
public interface ClientCollectionDataMapper<T> extends BaseDao<T> {

    /**
     * @Description: 获取当前系统时间前5分钟以为创建时间最大的那条数据
     * @param model
     * @return
     * @author yoko
     * @date 2020/7/7 19:30
    */
    public ClientCollectionDataModel getClientCollectionDataByCreateTime(ClientCollectionDataModel model);

}
