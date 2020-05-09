package com.hz.fine.master.core.service.impl;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.hz.fine.master.core.common.dao.BaseDao;
import com.hz.fine.master.core.common.service.impl.BaseServiceImpl;
import com.hz.fine.master.core.common.utils.AddressUtils;
import com.hz.fine.master.core.common.utils.constant.ServerConstant;
import com.hz.fine.master.core.mapper.RegionMapper;
import com.hz.fine.master.core.model.region.RegionModel;
import com.hz.fine.master.core.service.RegionService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @Description ip号段获取地域信息的Service层的实现层
 * @Author yoko
 * @Date 2019/12/18 16:51
 * @Version 1.0
 */
@Service
public class RegionServiceImpl<T> extends BaseServiceImpl<T> implements RegionService<T>,InitializingBean {
    private final static Logger log = LoggerFactory.getLogger(RegionServiceImpl.class);

    @Autowired
    private RegionMapper regionMapper;

    public BaseDao<T> getDao() {
        return regionMapper;
    }

    private Cache<String,List<RegionModel>> cache = CacheBuilder.newBuilder().expireAfterWrite(30, TimeUnit.DAYS).build();

    @Override
    public RegionModel getRegion(RegionModel model) {
        RegionModel regionModel;
        String ip = model.getIp();
        long ipValue = ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_ZERO;
        if (StringUtils.isNotEmpty(ip)){
            ipValue = AddressUtils.getIpValue(ip);
            model.setIpValue(ipValue);
        }

        //1.调用内部资源库查询
        regionModel = (RegionModel) regionMapper.findByObject(model);
        if (regionModel == null || StringUtils.isBlank(regionModel.getProvince())) {
            AddressUtils au = new AddressUtils();
            try {
                regionModel = au.taipingyang_getAddresses(ip, "GBK",1);
            } catch (Exception e) {
                log.error("queryGatewayArea(taipingyang)1"+e);
            }
        }
        if(regionModel == null || StringUtils.isBlank(regionModel.getProvince())){
            AddressUtils au = new AddressUtils();
            try {
                regionModel = au.getAddresses(ip, "UTF-8");
            } catch (Exception e) {
                log.error("queryGatewayArea(taobao)1"+e);
            }
        }

        regionModel = Optional.ofNullable(regionModel).orElse(new RegionModel());
        regionModel.setIp(ip);
        return regionModel;
    }

    @Override
    public RegionModel getCacheRegion(RegionModel model){
        RegionModel regionModel;
        String ip = model.getIp();
        long ipValue = ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_ZERO;
        if (StringUtils.isNotEmpty(ip)){
            ipValue = AddressUtils.getIpValue(ip);
            model.setIpValue(ipValue);
        }

        //1.调用内部资源库查询
        regionModel = doRegionModel(model);
        if (regionModel == null || StringUtils.isBlank(regionModel.getProvince())) {
            AddressUtils au = new AddressUtils();
            try {
                regionModel = au.taipingyang_getAddresses(ip, "GBK",1);
            } catch (Exception e) {
                log.error("queryGatewayArea(taipingyang)"+e);
            }
        }
        if(regionModel == null || StringUtils.isBlank(regionModel.getProvince())){
            AddressUtils au = new AddressUtils();
            try {
                regionModel = au.getAddresses(ip, "UTF-8");
            } catch (Exception e) {
                log.error("queryGatewayArea(taobao)"+e);
            }
        }

        regionModel = Optional.ofNullable(regionModel).orElse(new RegionModel());
        regionModel.setIp(ip);
        return regionModel;
    }


    private RegionModel doRegionModel(RegionModel model) {
        List<RegionModel> list = doGetAll();
        Optional<RegionModel> o = list.stream().filter(v -> {
            if(model.getIpValue() >= v.getStartIp() &&  model.getIpValue() <=  v.getEndIp()){
                return Boolean.TRUE;
            }
            return Boolean.FALSE;
        }).findFirst();
        return o.orElse(null);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        new Thread(() -> doGetAll()).start();
    }

    private List<RegionModel> doGetAll(){
        String key = "key";
        List<RegionModel> list = cache.getIfPresent(key);
        if(CollectionUtils.isEmpty(list)){
            try {
                list = cache.get(key,() -> regionMapper.findAll());
            } catch (ExecutionException e) {
                log.error("",e);
                e.printStackTrace();
            }
        }
        return list;
    }
}
