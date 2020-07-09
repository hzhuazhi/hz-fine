package com.hz.fine.master.core.mapper;

import com.hz.fine.master.core.common.dao.BaseDao;
import com.hz.fine.master.core.model.consult.ConsultAskModel;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Description 在线客服、咨询的发问的Dao层
 * @Author yoko
 * @Date 2020/7/8 20:09
 * @Version 1.0
 */
@Mapper
public interface ConsultAskMapper<T> extends BaseDao<T> {

    /**
     * @Description: 查询提问的详情-一对多
     * @param model
     * @return
     * @author yoko
     * @date 2020/7/9 15:42
     */
    public ConsultAskModel getConsultAskInfo(ConsultAskModel model);
}
