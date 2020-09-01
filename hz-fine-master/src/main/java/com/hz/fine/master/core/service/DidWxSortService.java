package com.hz.fine.master.core.service;

import com.hz.fine.master.core.common.service.BaseService;
import com.hz.fine.master.core.model.did.DidWxSortModel;

import java.util.List;

/**
 * @Description 用户的微信出码排序的Service层
 * @Author yoko
 * @Date 2020/8/31 16:39
 * @Version 1.0
 */
public interface DidWxSortService<T> extends BaseService<T> {

    /**
     * @Description: 获取最大的排序
     * @param model
     * @return
     * @author yoko
     * @date 2020/8/31 16:32
     */
    public int maxSort(DidWxSortModel model);

    /**
     * @Description: 添加用户的微信出码排序的数据
     * <p>
     *     自动累加排序
     * </p>
     * @param model
     * @return
     * @author yoko
     * @date 2020/8/31 16:38
     */
    public int addBySort(DidWxSortModel model);

    /**
     * @Description: 更新使用中的状态
     * @param model
     * @return
     * @author yoko
     * @date 2020/8/31 17:05
     */
    public int updateInUse(DidWxSortModel model);

    /**
     * @Description: 添加用户的微信出码排序，根据是否已经存在数据
     * <p>
     *     如果已存在则不进行数据的添加
     * </p>
     * @param model
     * @return
     * @author yoko
     * @date 2020/8/31 17:39
     */
    public int addByExist(DidWxSortModel model);

    /**
     * @Description: 筛选出正在使用的微信
     * @param did -
     * @param monitorWxList - 被监控的原始微信ID集合
     * @return
     * @author yoko
     * @date 2020/8/31 22:16
    */
    public DidWxSortModel screenDidWxSort(long did, List<String> monitorWxList);
}
