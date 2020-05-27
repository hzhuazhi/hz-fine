package com.hz.fine.master.core.service.impl;

import com.hz.fine.master.core.common.dao.BaseDao;
import com.hz.fine.master.core.common.service.impl.BaseServiceImpl;
import com.hz.fine.master.core.common.utils.StringUtil;
import com.hz.fine.master.core.common.utils.constant.CacheKey;
import com.hz.fine.master.core.common.utils.constant.CachedKeyUtils;
import com.hz.fine.master.core.mapper.OrderMapper;
import com.hz.fine.master.core.model.did.DidCollectionAccountModel;
import com.hz.fine.master.core.model.did.DidModel;
import com.hz.fine.master.core.model.strategy.StrategyData;
import com.hz.fine.master.core.model.wx.WxClerkModel;
import com.hz.fine.master.core.service.OrderService;
import com.hz.fine.master.util.ComponentUtil;
import com.hz.fine.master.util.HodgepodgeMethod;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * @Description 任务订单的Service层的实现层
 * @Author yoko
 * @Date 2020/5/21 19:35
 * @Version 1.0
 */
@Service
public class OrderServiceImpl<T> extends BaseServiceImpl<T> implements OrderService<T> {
    /**
     * 5分钟.
     */
    public long FIVE_MIN = 300;

    public long TWO_HOUR = 2;

    @Autowired
    private OrderMapper orderMapper;

    public BaseDao<T> getDao() {
        return orderMapper;
    }

    @Override
    public DidCollectionAccountModel screenCollectionAccount(List<DidModel> didList, String orderMoney, int payType) {

        // 筛选出可以使用的用户
//        List<DidModel> resList = new ArrayList<DidModel>();
//        resList = didList;
//        Iterator<DidModel> itList = resList.iterator();
//        while (itList.hasNext()) {
//            DidModel data = itList.next();
//        }
//        for (DidModel didModel : didList){
//            // 判断此用户是否有被锁住，或者
//        }
        while (1==1){
            // 随机选一个用户
            int random = new Random().nextInt(didList.size());
            DidModel didModel = didList.get(random);
            // 查看此用户是否有派发的订单正在执行中：只有缓存中没有数据才代表此用户名下没挂单
            String redisKey_did = getRedisDataByKey(CacheKey.LOCK_DID_ORDER_ING, didModel.getId());
            if (StringUtils.isBlank(redisKey_did)){

                // 判断此用户是否被锁住
                String lockKey_did = CachedKeyUtils.getCacheKey(CacheKey.LOCK_DID_ORDER, didModel.getId());
                boolean flagLock_did = ComponentUtil.redisIdService.lock(lockKey_did);
                if (flagLock_did){
                    // 查询用户账号下的收款账号
                    DidCollectionAccountModel didCollectionAccountQuery = HodgepodgeMethod.assembleDidCollectionAccount(didModel.getId(), payType);
                    List<DidCollectionAccountModel> didCollectionAccountList = ComponentUtil.didCollectionAccountService.findByCondition(didCollectionAccountQuery);
                    if (didCollectionAccountList != null && didCollectionAccountList.size() > 0){
                        for (DidCollectionAccountModel didCollectionAccountModel : didCollectionAccountList){
                            // 判断用户收款账号与小微的关系正常：上线状态
                            WxClerkModel wxClerkQuery = HodgepodgeMethod.assembleWxClerk(didModel.getId(), didCollectionAccountModel.getId());
                            WxClerkModel wxClerkData = (WxClerkModel) ComponentUtil.wxClerkService.findByObject(wxClerkQuery);
                            if (wxClerkData != null && wxClerkData.getId() > 0){
                                //
//                                段峰

                            }
                        }
                    }
                }
            }else{
                // 查看此用户挂单金额在redis中存在相同金额
                String redisKey_did_money = getRedisDataByKey(CacheKey.LOCK_DID_ORDER_ING, didModel.getId(), orderMoney);
                if (StringUtils.isBlank(redisKey_did_money)){
                    // 可以继续执行
                }
            }
            break;
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

    /**
     * @Description: 组装缓存key查询缓存中存在的数据
     * @param cacheKey - 缓存的类型key
     * @param obj - 数据的ID
     * @return
     * @author yoko
     * @date 2020/5/20 14:59
     */
    public String getRedisDataByKey(String cacheKey, Object obj, Object obj1){
        String str = null;
        String strKeyCache = CachedKeyUtils.getCacheKey(cacheKey, obj, obj1);
        String strCache = (String) ComponentUtil.redisService.get(strKeyCache);
        if (StringUtils.isBlank(strCache)){
            return str;
        }else{
            str = strCache;
            return str;
        }
    }


}
