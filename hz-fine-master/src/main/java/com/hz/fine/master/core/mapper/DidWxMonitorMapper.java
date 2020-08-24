package com.hz.fine.master.core.mapper;

import com.hz.fine.master.core.common.dao.BaseDao;
import com.hz.fine.master.core.model.did.DidWxMonitorModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Description 用户的微信收款账号金额监控的Dao层
 * @Author yoko
 * @Date 2020/8/23 19:34
 * @Version 1.0
 */
@Mapper
public interface DidWxMonitorMapper<T> extends BaseDao<T> {

    /**
     * @Description: 根据条件获取去重复的微信原始ID集合
     * @param model
     * @return
     * @author yoko
     * @date 2020/8/24 11:53
     */
    public List<String> getToWxidList(DidWxMonitorModel model);
}
