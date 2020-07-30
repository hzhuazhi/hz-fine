package com.hz.fine.master.core.mapper;

import com.hz.fine.master.core.common.dao.BaseDao;
import com.hz.fine.master.core.model.wx.WxModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Description 小微管理的Dao层
 * @Author yoko
 * @Date 2020/5/25 16:24
 * @Version 1.0
 */
@Mapper
public interface WxMapper<T> extends BaseDao<T> {

    /**
     * @Description: 获取小微的数据集合
     * @param model
     * @return
     * @author yoko
     * @date 2020/7/30 17:20
    */
    public List<WxModel> getWxList(WxModel model);
}
