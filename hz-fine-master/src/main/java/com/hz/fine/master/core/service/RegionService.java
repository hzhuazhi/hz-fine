package com.hz.fine.master.core.service;


import com.hz.fine.master.core.common.service.BaseService;
import com.hz.fine.master.core.model.region.RegionModel;

/**
 * @Description ip号段获取地域信息的Service层
 * @Author yoko
 * @Date 2019/12/18 16:51
 * @Version 1.0
 */
public interface RegionService<T> extends BaseService<T> {
    /**
     * @Description: TODO(根据IP地址获取省份跟城市)
     * @author df
     * @param model
     * @create 21:01 2019/2/13
     **/
    public RegionModel getRegion(RegionModel model);

    /**
     * @Description: TODO(根据IP地址获取省份跟城市-缓存)
     * @author df
     * @param model
     * @create 21:02 2019/2/13
     **/
    public RegionModel getCacheRegion(RegionModel model);
}
