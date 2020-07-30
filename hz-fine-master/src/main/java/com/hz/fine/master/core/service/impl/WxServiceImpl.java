package com.hz.fine.master.core.service.impl;

import com.hz.fine.master.core.common.dao.BaseDao;
import com.hz.fine.master.core.common.service.impl.BaseServiceImpl;
import com.hz.fine.master.core.common.utils.constant.CacheKey;
import com.hz.fine.master.core.common.utils.constant.CachedKeyUtils;
import com.hz.fine.master.core.mapper.WxMapper;
import com.hz.fine.master.core.model.wx.WxModel;
import com.hz.fine.master.core.service.WxService;
import com.hz.fine.master.util.ComponentUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

/**
 * @Description 小微管理的Service层的实现层
 * @Author yoko
 * @Date 2020/5/25 16:27
 * @Version 1.0
 */
@Service
public class WxServiceImpl<T> extends BaseServiceImpl<T> implements WxService<T> {
    /**
     * 5分钟.
     */
    public long FIVE_MIN = 300;

    public long TWO_HOUR = 2;

    @Autowired
    private WxMapper wxMapper;

    public BaseDao<T> getDao() {
        return wxMapper;
    }

    @Override
    public WxModel screenWx(WxModel model) {
        List<WxModel> wxList = wxMapper.getWxList(model);
        WxModel resModel = new WxModel();
        if (wxList != null && wxList.size() > 0){
            Iterator<WxModel> iter_wx = wxList.iterator();
            while(iter_wx.hasNext()){
                // 获取集合中最小ID
                Optional<WxModel> dataOp = wxList.stream().min(Comparator.comparing(WxModel :: getId));
                long minId = dataOp.get().getId();
                // 从缓存中获取今日加好友的用户数
                String redis_wx_day_num = getRedisDataByKey(CacheKey.WX_DAY_NUM, minId);
                int dayNum = 0;// 当日加好友的数量
                if (!StringUtils.isBlank(redis_wx_day_num)){
                    dayNum = Integer.parseInt(redis_wx_day_num);
                }
                WxModel wxDto = iter_wx.next();
                if (wxDto.getId() == minId && wxDto.getDayNum() <= dayNum){
                    // 代表着今日加好友超过当日的上限
                    iter_wx.remove();
                }else {
                    resModel = wxDto;
                    return resModel;
                }
            }

        }
        return null;
    }



    /**
     * @Description: 组装缓存key查询缓存中存在的数据
     * @param cacheKey - 缓存的类型key
     * @param obj - 数据的ID
     * @return
     * @author yoko
     * @date 2020/5/20 14:59
     */
    public String getRedisDataByKey(String cacheKey, Object obj){
        String str = null;
        String strKeyCache = CachedKeyUtils.getCacheKey(cacheKey, obj);
        String strCache = (String) ComponentUtil.redisService.get(strKeyCache);
        if (StringUtils.isBlank(strCache)){
            return str;
        }else{
            str = strCache;
            return str;
        }
    }
}
