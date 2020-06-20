package com.hz.fine.master.core.service.impl;

import com.hz.fine.master.core.common.dao.BaseDao;
import com.hz.fine.master.core.common.service.impl.BaseServiceImpl;
import com.hz.fine.master.core.common.utils.StringUtil;
import com.hz.fine.master.core.common.utils.constant.CacheKey;
import com.hz.fine.master.core.common.utils.constant.CachedKeyUtils;
import com.hz.fine.master.core.mapper.OrderMapper;
import com.hz.fine.master.core.mapper.TaskMapper;
import com.hz.fine.master.core.model.did.DidBalanceDeductModel;
import com.hz.fine.master.core.model.did.DidCollectionAccountModel;
import com.hz.fine.master.core.model.did.DidCollectionAccountQrCodeModel;
import com.hz.fine.master.core.model.did.DidModel;
import com.hz.fine.master.core.model.order.OrderModel;
import com.hz.fine.master.core.model.strategy.StrategyData;
import com.hz.fine.master.core.model.wx.WxClerkModel;
import com.hz.fine.master.core.model.wx.WxModel;
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

    /**
     * 15分钟.
     */
    public long FIFTEEN_MIN = 900;

    public long TWO_HOUR = 2;

    @Autowired
    private OrderMapper orderMapper;

    public BaseDao<T> getDao() {
        return orderMapper;
    }

    @Override
    public DidCollectionAccountModel screenCollectionAccount(List<DidModel> didList, String orderMoney, int payType) {

        DidCollectionAccountModel didCollectionAccount = new DidCollectionAccountModel();
        int count = 0;// 加一把防止死循环的锁

        while (1==1){
            if (count <= 50){
                // 随机选一个用户
                int random = new Random().nextInt(didList.size());
                DidModel didModel = didList.get(random);

                // 筛选收款账号
                didCollectionAccount = getDidCollectionAccount(didModel.getId(), payType, orderMoney);
                if (didCollectionAccount != null && didCollectionAccount.getId() > 0){
                    break;
                }
                count ++;
            }else {
                break;
            }
        }


//        while (1==1){
//            if (count <= 50){
//                // 随机选一个用户
//                int random = new Random().nextInt(didList.size());
//                DidModel didModel = didList.get(random);
//                // 查看此用户是否有派发的订单正在执行中：只有缓存中没有数据才代表此用户名下没挂单
//                String redisKey_did = getRedisDataByKey(CacheKey.LOCK_DID_ORDER_ING, didModel.getId());
//                if (StringUtils.isBlank(redisKey_did)){
//                    didCollectionAccount = getDidCollectionAccount(didModel.getId(), payType, orderMoney);
//                    if (didCollectionAccount != null && didCollectionAccount.getId() > 0){
//                        break;
//                    }
//                }else{
//                    // 查看此用户挂单金额在redis中存在相同金额
//                    String redisKey_did_money = getRedisDataByKey(CacheKey.LOCK_DID_ORDER_MONEY, didModel.getId(), orderMoney);
//                    if (StringUtils.isBlank(redisKey_did_money)){
//                        // 可以继续执行
//                        didCollectionAccount = getDidCollectionAccount(didModel.getId(), payType, orderMoney);
//                        if (didCollectionAccount != null && didCollectionAccount.getId() > 0){
//                            break;
//                        }
//                    }
//                }
//                count ++;
//            }else {
//                break;
//            }
//        }

        return didCollectionAccount;
    }


    /**
     * @Description: 筛选可使用的账号
     * @param did - 用户ID
     * @param payType - 支付类型
     * @param orderMoney - 派单金额
     * @return
     * @author yoko
     * @date 2020/6/1 19:51
     */
    public DidCollectionAccountModel getDidCollectionAccount(long did, int payType, String orderMoney){
        // 判断此用户是否被锁住
        String lockKey_did = CachedKeyUtils.getCacheKey(CacheKey.LOCK_DID_ORDER, did);
        boolean flagLock_did = ComponentUtil.redisIdService.lock(lockKey_did);
        if (flagLock_did){
            // 查询用户账号下的收款账号
            DidCollectionAccountModel didCollectionAccountQuery = HodgepodgeMethod.assembleDidCollectionAccount(did, payType);
            List<DidCollectionAccountModel> didCollectionAccountList = ComponentUtil.didCollectionAccountService.findByCondition(didCollectionAccountQuery);
            if (didCollectionAccountList != null && didCollectionAccountList.size() > 0){
                for (DidCollectionAccountModel didCollectionAccountModel : didCollectionAccountList){
                    // 锁住这个收款账号
                    String lockKey_did_collection_account = CachedKeyUtils.getCacheKey(CacheKey.LOCK_DID_COLLECTION_ACCOUNT_FOR, didCollectionAccountModel.getId());
                    boolean flagLock_did_collection_account = ComponentUtil.redisIdService.lock(lockKey_did_collection_account);
                    if (flagLock_did_collection_account){

                        // 判断这个收款账号是否超过今日收款金额上限
                        String strKeyCache_check_lock_did_collection_account_day_suc_money = CachedKeyUtils.getCacheKey(CacheKey.LOCK_DID_COLLECTION_ACCOUNT_DAY_SUC_MONEY, didCollectionAccountModel.getId());
                        String strCache_check_lock_did_collection_account_day_suc_money = (String) ComponentUtil.redisService.get(strKeyCache_check_lock_did_collection_account_day_suc_money);
                        if (StringUtils.isBlank(strCache_check_lock_did_collection_account_day_suc_money)){
                            // 判断收款账号今日给出码的次数是否超过上限
                            String strKeyCache_check_lock_did_collection_account_day_limit_num = CachedKeyUtils.getCacheKey(CacheKey.LOCK_DID_COLLECTION_ACCOUNT_DAY_LIMIT_NUM, didCollectionAccountModel.getId());
                            String strCache_check_lock_did_collection_account_day_limit_num = (String) ComponentUtil.redisService.get(strKeyCache_check_lock_did_collection_account_day_limit_num);
                            if (StringUtils.isBlank(strCache_check_lock_did_collection_account_day_limit_num)){
                                // 判断收款账号今日成功收款次数是否超过上限
                                String strKeyCache_check_lock_did_collection_account_day_suc_limit_num = CachedKeyUtils.getCacheKey(CacheKey.LOCK_DID_COLLECTION_ACCOUNT_DAY_SUC_LIMIT_NUM, didCollectionAccountModel.getId());
                                String strCache_check_lock_did_collection_account_day_suc_limit_num = (String) ComponentUtil.redisService.get(strKeyCache_check_lock_did_collection_account_day_suc_limit_num);
                                if (StringUtils.isBlank(strCache_check_lock_did_collection_account_day_suc_limit_num)){
                                    // 判断收款账号是否在5分钟之内给出过码
                                    String strKeyCache_check_lock_did_collection_account_fifteen = CachedKeyUtils.getCacheKey(CacheKey.LOCK_DID_COLLECTION_ACCOUNT_FIFTEEN, didCollectionAccountModel.getId());
                                    String strCache_check_lock_did_collection_account_fifteen = (String) ComponentUtil.redisService.get(strKeyCache_check_lock_did_collection_account_fifteen);
                                    if (StringUtils.isBlank(strCache_check_lock_did_collection_account_fifteen)){
                                        // 判断用户收款账号与小微的关系正常：上线状态
                                        WxClerkModel wxClerkQuery = HodgepodgeMethod.assembleWxClerk(did, didCollectionAccountModel.getId());
                                        WxClerkModel wxClerkData = (WxClerkModel) ComponentUtil.wxClerkService.findByObject(wxClerkQuery);
                                        if (wxClerkData != null && wxClerkData.getId() > 0){
                                            // 判断小微的使用状态是否正常使用
                                            WxModel wxQuery = HodgepodgeMethod.assembleWxQuery(wxClerkData.getWxId());
                                            WxModel wxData = (WxModel) ComponentUtil.wxService.findByObject(wxQuery);
                                            if (wxData != null && wxData.getId() > 0){
                                                // 查询此账号的收款二维码
                                                DidCollectionAccountQrCodeModel didCollectionAccountQrCodeQuery = HodgepodgeMethod.assembleDidCollectionAccountQrCode(didCollectionAccountModel.getId());
                                                DidCollectionAccountQrCodeModel didCollectionAccountQrCodeModel  = (DidCollectionAccountQrCodeModel) ComponentUtil.didCollectionAccountQrCodeService.findByObject(didCollectionAccountQrCodeQuery);
                                                if (didCollectionAccountQrCodeModel != null && didCollectionAccountQrCodeModel.getId() > 0){
                                                    // 锁住用户： 因为要对用户的金额进行更改； 更改如下：余额 = 余额 - 派单金额， 冻结金额 = 冻结金额 + 派单金额
                                                    String lockKey_did_money = CachedKeyUtils.getCacheKey(CacheKey.LOCK_DID_MONEY, did);
                                                    boolean flagLock_did_money = ComponentUtil.redisIdService.lock(lockKey_did_money);
                                                    if (flagLock_did_money){
                                                        DidModel didModel = HodgepodgeMethod.assembleUpdateDidMoneyByOrder(did, orderMoney);
                                                        int num  = ComponentUtil.didService.updateDidMoneyByOrder(didModel);
                                                        if (num > 0){

                                                            // 这里没有再次做日上总上限的判断了，因为直接用task来跑：如果在这里做了日上月上总上限的话会导致这个收款账号永远不会超，我这边要的是计算好超一次就可以了
                                                            didCollectionAccountModel.setWxId(wxClerkData.getWxId());// 赋值归属小微
                                                            didCollectionAccountModel.setQrCodeId(didCollectionAccountQrCodeModel.getId());// 赋值二维码主键ID
//                                                            didCollectionAccountModel.setDdQrCode(didCollectionAccountQrCodeModel.getDdQrCode());// 赋值收款二维码
                                                            didCollectionAccountModel.setDdQrCode(didCollectionAccountQrCodeModel.getMmQrCode());// 赋值收款二维码
                                                            // redis存储
                                                            // 此收款账号给出过码，需要5分钟之后才自动失效
                                                            String strKeyCache_lock_did_collection_account = CachedKeyUtils.getCacheKey(CacheKey.LOCK_DID_COLLECTION_ACCOUNT_FIFTEEN, didCollectionAccountModel.getId());
                                                            ComponentUtil.redisService.set(strKeyCache_lock_did_collection_account, String.valueOf(didCollectionAccountModel.getId()) + "," + orderMoney, FIFTEEN_MIN);
                                                            // 解锁
                                                            ComponentUtil.redisIdService.delLock(lockKey_did_money);
                                                            return didCollectionAccountModel;
                                                        }else {
                                                            // 解锁
                                                            ComponentUtil.redisIdService.delLock(lockKey_did_money);
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        // 解锁
                        ComponentUtil.redisIdService.delLock(lockKey_did_collection_account);
                    }
                }

            }
            // 解锁
            ComponentUtil.redisIdService.delLock(lockKey_did);
        }
        return null;
    }
        
    
//    /**
//     * @Description: 筛选可使用的账号
//     * @param did - 用户ID
//     * @param payType - 支付类型
//     * @param orderMoney - 派单金额
//     * @return
//     * @author yoko
//     * @date 2020/6/1 19:51
//    */
//    public DidCollectionAccountModel getDidCollectionAccount(long did, int payType, String orderMoney){
//        // 判断此用户是否被锁住
//        String lockKey_did = CachedKeyUtils.getCacheKey(CacheKey.LOCK_DID_ORDER, did);
//        boolean flagLock_did = ComponentUtil.redisIdService.lock(lockKey_did);
//        if (flagLock_did){
//            // 查询用户账号下的收款账号
//            DidCollectionAccountModel didCollectionAccountQuery = HodgepodgeMethod.assembleDidCollectionAccount(did, payType);
//            List<DidCollectionAccountModel> didCollectionAccountList = ComponentUtil.didCollectionAccountService.findByCondition(didCollectionAccountQuery);
//            if (didCollectionAccountList != null && didCollectionAccountList.size() > 0){
//                for (DidCollectionAccountModel didCollectionAccountModel : didCollectionAccountList){
//                    // 锁住这个收款账号
//                    String lockKey_did_collection_account = CachedKeyUtils.getCacheKey(CacheKey.LOCK_DID_COLLECTION_ACCOUNT_FOR, didCollectionAccountModel.getId());
//                    boolean flagLock_did_collection_account = ComponentUtil.redisIdService.lock(lockKey_did_collection_account);
//                    if (flagLock_did_collection_account){
//                        // 判断用户收款账号与小微的关系正常：上线状态
//                        WxClerkModel wxClerkQuery = HodgepodgeMethod.assembleWxClerk(did, didCollectionAccountModel.getId());
//                        WxClerkModel wxClerkData = (WxClerkModel) ComponentUtil.wxClerkService.findByObject(wxClerkQuery);
//                        if (wxClerkData != null && wxClerkData.getId() > 0){
//                            // 判断小微的使用状态是否正常使用
//                            WxModel wxQuery = HodgepodgeMethod.assembleWxQuery(wxClerkData.getWxId());
//                            WxModel wxData = (WxModel) ComponentUtil.wxService.findByObject(wxQuery);
//                            if (wxData != null && wxData.getId() > 0){
//                                // 锁住用户： 因为要对用户的金额进行更改； 更改如下：余额 = 余额 - 派单金额， 冻结金额 = 冻结金额 + 派单金额
//                                String lockKey_did_money = CachedKeyUtils.getCacheKey(CacheKey.LOCK_DID_MONEY, did);
//                                boolean flagLock_did_money = ComponentUtil.redisIdService.lock(lockKey_did_money);
//                                if (flagLock_did_money){
//                                    DidModel didModel = HodgepodgeMethod.assembleUpdateDidMoneyByOrder(did, orderMoney);
//                                    int num  = ComponentUtil.didService.updateDidMoneyByOrder(didModel);
//                                    if (num > 0){
//                                        // 这里没有再次做日上总上限的判断了，因为直接用task来跑：如果在这里做了日上月上总上限的话会导致这个收款账号永远不会超，我这边要的是计算好超一次就可以了
//
//                                        didCollectionAccountModel.setWxId(wxClerkData.getWxId());// 赋值归属小微
//
//                                        // 锁住这个用户 - 因为已经给这个用户派单了
//                                        String strKeyCache_did = CachedKeyUtils.getCacheKey(CacheKey.LOCK_DID_ORDER_ING, did);
//                                        ComponentUtil.redisService.set(strKeyCache_did, String.valueOf(did), ELEVEN_MIN);
//                                        // 锁住这个用户以及派单的金额 - 因为已经给这个用户派单了并且纪录金额，因为在用户少的情况下可以支持用户同时挂单多个，但是收款金额不能一样
//                                        String strKeyCache_did_money = CachedKeyUtils.getCacheKey(CacheKey.LOCK_DID_ORDER_MONEY, did, orderMoney);
//                                        ComponentUtil.redisService.set(strKeyCache_did_money, String.valueOf(did) + "," + orderMoney, ELEVEN_MIN);
//
//                                        // #锁住这个用户下派发订单的收款账号：目前只是先纪录值； 后续如果收款用户多了，就要把这个锁给加上（加上判断）
//                                        String strKeyCache_did_collection_account = CachedKeyUtils.getCacheKey(CacheKey.LOCK_DID_COLLECTION_ACCOUNT, didCollectionAccountModel.getId());
//                                        ComponentUtil.redisService.set(strKeyCache_did_collection_account, String.valueOf(didCollectionAccountModel.getId()), ELEVEN_MIN);
//
//                                        // #锁住这个用户下派发订单的收款账号的收款金额：目前只是先纪录值； 后续如果收款用户多了，就要把这个锁给加上（加上判断）；存储纪录用户收款账号派单的具体金额
//                                        String strKeyCache_did_collection_account_money = CachedKeyUtils.getCacheKey(CacheKey.LOCK_DID_COLLECTION_ACCOUNT_MONEY, didCollectionAccountModel.getId(), orderMoney);
//                                        ComponentUtil.redisService.set(strKeyCache_did_collection_account_money, String.valueOf(didCollectionAccountModel.getId()) + "," + orderMoney, ELEVEN_MIN);
//
//                                        // 解锁
//                                        ComponentUtil.redisIdService.delLock(lockKey_did_money);
//                                        return didCollectionAccountModel;
//                                    }else {
//                                        // 解锁
//                                        ComponentUtil.redisIdService.delLock(lockKey_did_money);
//                                    }
//                                }
//                            }
//                        }
//                        // 解锁
//                        ComponentUtil.redisIdService.delLock(lockKey_did_collection_account);
//                    }
//                }
//
//            }
//            // 解锁
//            ComponentUtil.redisIdService.delLock(lockKey_did);
//        }
//        return null;
//    }

    @Override
    public String getProfitByCurday(OrderModel model) {
        return orderMapper.getProfitByCurday(model);
    }

    @Override
    public OrderModel getOrderQrCodeByOrderNo(OrderModel model) {
        return orderMapper.getOrderQrCodeByOrderNo(model);
    }

    @Override
    public int getOrderStatus(OrderModel model) {
        return orderMapper.getOrderStatus(model);
    }

    @Override
    public boolean handleOrder(DidBalanceDeductModel didBalanceDeductModel, OrderModel orderModel, DidModel didModel) {
        return false;
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
