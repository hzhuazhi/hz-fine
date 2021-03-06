package com.hz.fine.master.core.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.hz.fine.master.core.common.dao.BaseDao;
import com.hz.fine.master.core.common.exception.ServiceException;
import com.hz.fine.master.core.common.service.impl.BaseServiceImpl;
import com.hz.fine.master.core.common.utils.DateUtil;
import com.hz.fine.master.core.common.utils.StringUtil;
import com.hz.fine.master.core.common.utils.constant.CacheKey;
import com.hz.fine.master.core.common.utils.constant.CachedKeyUtils;
import com.hz.fine.master.core.common.utils.constant.ServerConstant;
import com.hz.fine.master.core.mapper.DidBalanceDeductMapper;
import com.hz.fine.master.core.mapper.DidMapper;
import com.hz.fine.master.core.mapper.OrderMapper;
import com.hz.fine.master.core.mapper.TaskMapper;
import com.hz.fine.master.core.model.did.*;
import com.hz.fine.master.core.model.operate.OperateModel;
import com.hz.fine.master.core.model.order.OrderModel;
import com.hz.fine.master.core.model.strategy.StrategyData;
import com.hz.fine.master.core.model.wx.WxClerkModel;
import com.hz.fine.master.core.model.wx.WxModel;
import com.hz.fine.master.core.service.OrderService;
import com.hz.fine.master.util.ComponentUtil;
import com.hz.fine.master.util.HodgepodgeMethod;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    private static Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);

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

    @Autowired
    private DidBalanceDeductMapper didBalanceDeductMapper;

    @Autowired
    private DidMapper didMapper;

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
                                                            if(!StringUtils.isBlank(didCollectionAccountQrCodeModel.getMmQrCode())){
                                                                didCollectionAccountModel.setDdQrCode(didCollectionAccountQrCodeModel.getMmQrCode());// 赋值收款二维码
                                                            }else {
                                                                didCollectionAccountModel.setDdQrCode(didCollectionAccountQrCodeModel.getDdQrCode());// 赋值收款二维码
                                                            }
//                                                            didCollectionAccountModel.setDdQrCode(didCollectionAccountQrCodeModel.getDdQrCode());// 赋值收款二维码
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

    @Override
    public List<OrderModel> getSucOrderList(OrderModel model) {
        return orderMapper.getSucOrderList(model);
    }

    @Override
    public OrderModel getInitOrder(OrderModel model) {
        return orderMapper.getInitOrder(model);
    }

    @Override
    public int updateDidStatus(OrderModel model) {
        return orderMapper.updateDidStatus(model);
    }

    @Override
    public DidModel screenCollectionAccountByZfb(List<DidModel> didList, String orderMoney) {
        DidModel didModel = new DidModel();
        int count = 0;// 加一把防止死循环的锁
        while (1==1){
            if (count <= 50){
                // 随机选一个用户
                int random = new Random().nextInt(didList.size());
                DidModel randomDidModel = didList.get(random);

                // 筛选收款账号
                didModel = getDidCollectionAccountByZfb(randomDidModel, orderMoney);
                if (didModel != null && didModel.getId() > 0){
                    break;
                }
                count ++;
            }else {
                break;
            }
        }

        return didModel;
    }

    @Transactional(rollbackFor=Exception.class)
    @Override
    public boolean handleOrder(OrderModel orderModel, DidBalanceDeductModel didBalanceDeductModel, DidModel didModel) throws Exception {
        int num1 = orderMapper.add(orderModel);
        int num2 = didBalanceDeductMapper.add(didBalanceDeductModel);
        int num3 = 0;
        // 锁定这个用户
        String lockKey_did_money = CachedKeyUtils.getCacheKey(CacheKey.LOCK_DID_MONEY, didModel.getId());
        boolean flagLock_did_money = ComponentUtil.redisIdService.lock(lockKey_did_money);
        if (flagLock_did_money){
            num3 = didMapper.updateDidBalance(didModel);
            // 解锁
            ComponentUtil.redisIdService.delLock(lockKey_did_money);
        }else {
            throw new ServiceException("handleOrder", "用户ID被其它锁住");
        }

        if (num1 > 0 && num2 > 0 && num3 > 0){
            return true;
        }else{
            throw new ServiceException("handleOrder", "三个执行更新SQL其中有一个或者多个响应行为0");
        }

    }

    @Override
    public OrderModel getOrderByReward(OrderModel model) {
        return orderMapper.getOrderByReward(model);
    }

    @Override
    public String directSumMoney(OrderModel model) {
        return orderMapper.directSumMoney(model);
    }

    @Override
    public DidModel screenCollectionAccountByWxGroup(List<DidModel> didList, String orderMoney) {
        DidModel didModel = new DidModel();
        int count = 0;// 加一把防止死循环的锁
        while (1==1){
            if (count <= 50){
                // 随机选一个用户
                int random = new Random().nextInt(didList.size());
                DidModel randomDidModel = didList.get(random);

                // 筛选收款账号
                didModel = getDidCollectionAccountByWxGroup(randomDidModel, orderMoney);
                if (didModel != null && didModel.getId() > 0){
                    break;
                }
                count ++;
            }else {
                break;
            }
        }

        return didModel;
    }

    @Override
    public OrderModel getNewestOrder(OrderModel model) {
        return orderMapper.getNewestOrder(model);
    }

    @Override
    public DidModel screenNewCollectionAccountByWxGroup(List<DidModel> didList, String orderMoney, int countGroupNum) {
        DidModel didModel = new DidModel();
        int count = 0;// 加一把防止死循环的锁
        while (1==1){
            if (count <= 50){
                // 随机选一个用户
                int random = new Random().nextInt(didList.size());
                DidModel randomDidModel = didList.get(random);

                // 查询此用户下订单，已经发过红包，但是没有回复的订单信息
                OrderModel orderByDidQuery = HodgepodgeMethod.assembleOrderByIsReply(randomDidModel.getId(), 3, 1, 2,2);
                OrderModel orderByDidModel = ComponentUtil.orderService.getOrderByNotIsReply(orderByDidQuery);
                if (orderByDidModel == null || orderByDidModel.getId() == null || randomDidModel.getId() <= 0){
                    // 表示用户名下订单一切正常

                    // 筛选收款账号
                    didModel = getNewDidCollectionAccountByWxGroup(randomDidModel, orderMoney, countGroupNum);
                    if (didModel != null && didModel.getId() > 0 && didModel.getCollectionAccountId() > 0){
                        break;
                    }
                }else{
                    // 表示此用户名下有订单已经发了红包，但是没有回复，只是系统默认回复
                }
                count ++;
            }else {
                break;
            }
        }

        return didModel;
    }

    @Override
    public OrderModel getOrderByNotIsReply(OrderModel model) {
        return orderMapper.getOrderByNotIsReply(model);
    }

    @Override
    public DidModel screenCollectionAccountByPool(List<DidModel> didList, String orderMoney, int countGroupNum) {
        DidModel didModel = new DidModel();
        for(DidModel didData : didList){
            // 查询此用户下订单，已经发过红包，但是没有回复的订单信息
            OrderModel orderByDidQuery = HodgepodgeMethod.assembleOrderByIsReply(didData.getId(), 3, 1, 2,2);
            OrderModel orderByDidModel = ComponentUtil.orderService.getOrderByNotIsReply(orderByDidQuery);
            if (orderByDidModel == null || orderByDidModel.getId() == null || orderByDidModel.getId() <= 0){
                // 表示用户名下订单一切正常
                // 筛选收款账号
                if (didData.getOperateGroupNum() != null && didData.getOperateGroupNum() > 0){
                    countGroupNum = didData.getOperateGroupNum();
                }

                // 获取此用户被监控的微信ID集合
                DidWxMonitorModel didWxMonitorQuery = HodgepodgeMethod.assembleDidWxMonitorByDidQuery(didData.getId(), "1", null);
                List<String> toWxidList = ComponentUtil.didWxMonitorService.getToWxidList(didWxMonitorQuery);

                // 筛选微信收款账号
                didModel = getDidCollectionAccountByPool(didData, orderMoney, countGroupNum, toWxidList);
                if (didModel != null && didModel.getId() > 0 && didModel.getCollectionAccountId() > 0){
                    break;
                }
            }
        }

        return didModel;
    }

    @Override
    public DidModel screenCollectionAccountByPoolTwo(List<DidModel> didList, String orderMoney) {
        DidModel didModel = new DidModel();
        for(DidModel didData : didList){
            // 查询此用户下订单，已经发过红包，但是没有回复的订单信息
            OrderModel orderByDidQuery = HodgepodgeMethod.assembleOrderByIsReply(didData.getId(), 3, 1, 2,2);
            OrderModel orderByDidModel = ComponentUtil.orderService.getOrderByNotIsReply(orderByDidQuery);
            if (orderByDidModel == null || orderByDidModel.getId() == null || orderByDidModel.getId() <= 0){
                // 表示用户名下订单一切正常

                // 查询此用户正在使用的微信
                DidWxSortModel didWxSortQuery = HodgepodgeMethod.assembleDidWxSortData(0, didData.getId(), null,
                        0, 2, 0, 0, 0, null, null, null);
                DidWxSortModel didWxSortModel = (DidWxSortModel)ComponentUtil.didWxSortService.findByObject(didWxSortQuery);
                if (didWxSortModel == null || didWxSortModel.getId() == null || didWxSortModel.getId() <= 0){
                    continue;
                }


                // 筛选微信收款账号
                didModel = getDidCollectionAccountByPoolTwo(didData, orderMoney, didWxSortModel.getToWxid());
                if (didModel != null && didModel.getId() > 0 && didModel.getCollectionAccountId() > 0){
                    break;
                }
            }
        }

        return didModel;
    }


    /**
     * @Description: 筛选可使用的支付宝收款账号以及用户
     * @param didModel - 用户信息
     * @param orderMoney - 派单金额
     * @return
     * @author yoko
     * @date 2020/6/1 19:51
     */
    public DidModel getDidCollectionAccountByZfb(DidModel didModel, String orderMoney){
        // 判断此用户是否被锁住
        String lockKey_did = CachedKeyUtils.getCacheKey(CacheKey.LOCK_DID_ORDER, didModel.getId());
        boolean flagLock_did = ComponentUtil.redisIdService.lock(lockKey_did);
        if (flagLock_did){

            // 查看此用户是否有派发的订单正在执行中：只有缓存中没有数据才代表此用户名下没挂单
            String redisKey_did = getRedisDataByKey(CacheKey.LOCK_DID_ORDER_ING, didModel.getId());
            if (StringUtils.isBlank(redisKey_did)){
                // redis存储
                String strKeyCache_lock_did_order_ing = CachedKeyUtils.getCacheKey(CacheKey.LOCK_DID_ORDER_ING, didModel.getId());
                ComponentUtil.redisService.set(strKeyCache_lock_did_order_ing, String.valueOf(didModel.getId()) + "," + orderMoney, FIVE_MIN);
                return didModel;
            }
//            // 判断收款账号是否在5分钟之内给出过码
//            String strKeyCache_check_lock_did_collection_account_fifteen = CachedKeyUtils.getCacheKey(CacheKey.LOCK_DID_COLLECTION_ACCOUNT_FIFTEEN, didModel.getCollectionAccountId());
//            String strCache_check_lock_did_collection_account_fifteen = (String) ComponentUtil.redisService.get(strKeyCache_check_lock_did_collection_account_fifteen);
//            if (StringUtils.isBlank(strCache_check_lock_did_collection_account_fifteen)){
//
//            }
            // 解锁
            ComponentUtil.redisIdService.delLock(lockKey_did);
        }
        return null;
    }


    /**
     * @Description: 筛选可使用的微信群收款账号以及用户
     * @param didModel - 用户信息
     * @param orderMoney - 派单金额
     * @return
     * @author yoko
     * @date 2020/6/1 19:51
     */
    public DidModel getDidCollectionAccountByWxGroup(DidModel didModel, String orderMoney){
        // 判断此用户是否被锁住
        String lockKey_did = CachedKeyUtils.getCacheKey(CacheKey.LOCK_DID_ORDER, didModel.getId());
        boolean flagLock_did = ComponentUtil.redisIdService.lock(lockKey_did);
        if (flagLock_did){

            // 查看此用户是否有派发的订单正在执行中：只有缓存中没有数据才代表此用户名下没挂单
            String redisKey_did = getRedisDataByKey(CacheKey.LOCK_DID_ORDER_ING, didModel.getId());
            if (StringUtils.isBlank(redisKey_did)){
                // 查询此用户上一个订单的订单状态是否是否已回复
                OrderModel orderQuery = HodgepodgeMethod.assembleOrderByNewest(didModel.getId(), 3, 1, 0);
                OrderModel orderModel = ComponentUtil.orderService.getNewestOrder(orderQuery);
                if (orderModel != null && orderModel.getId() > 0){
                    if (orderModel.getIsRedPack() == 1){
                        // 未发过红包
                        if (orderModel.getOrderStatus() == 1){
                            // 未发过红包，并且订单未超时
                            return null;
                        }
                    }else {
                        // 发过红包
                        if (orderModel.getIsReply() < ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_THREE){
                            // 发过红包，并且未回复结果
                            return null;
                        }
                    }
                }

//                // 运营数据是否有未处理的
//                OperateModel operateQuery = HodgepodgeMethod.assembleOperateQuery(didModel.getId(), 4, 1);
//                OperateModel operateModel = (OperateModel) ComponentUtil.operateService.findByObject(operateQuery);
//                if (operateModel != null && operateModel.getId() > 0){
//                    return null;
//                }



                // 判断这个收款账号是否超过今日收款金额上限
                String strKeyCache_check_lock_did_collection_account_day_suc_money = CachedKeyUtils.getCacheKey(CacheKey.LOCK_DID_COLLECTION_ACCOUNT_DAY_SUC_MONEY, didModel.getCollectionAccountId());
                String strCache_check_lock_did_collection_account_day_suc_money = (String) ComponentUtil.redisService.get(strKeyCache_check_lock_did_collection_account_day_suc_money);
                if (StringUtils.isBlank(strCache_check_lock_did_collection_account_day_suc_money)){
                    // 判断收款账号今日成功收款次数是否超过上限
                    String strKeyCache_check_lock_did_collection_account_day_suc_limit_num = CachedKeyUtils.getCacheKey(CacheKey.LOCK_DID_COLLECTION_ACCOUNT_DAY_SUC_LIMIT_NUM, didModel.getCollectionAccountId());
                    String strCache_check_lock_did_collection_account_day_suc_limit_num = (String) ComponentUtil.redisService.get(strKeyCache_check_lock_did_collection_account_day_suc_limit_num);
                    if (StringUtils.isBlank(strCache_check_lock_did_collection_account_day_suc_limit_num)){
                        // redis存储
                        String strKeyCache_lock_did_order_ing = CachedKeyUtils.getCacheKey(CacheKey.LOCK_DID_ORDER_ING, didModel.getId());
                        ComponentUtil.redisService.set(strKeyCache_lock_did_order_ing, String.valueOf(didModel.getId()) + "," + orderMoney, FIVE_MIN);
                        return didModel;
                    }
                }

            }
            // 解锁
            ComponentUtil.redisIdService.delLock(lockKey_did);
        }
        return null;
    }



    /**
     * @Description: 筛选可使用的微信群收款账号以及用户-new
     * @param didModel - 用户信息
     * @param orderMoney - 派单金额
     * @param countGroupNum - 微信群有效个数才允许正常出码
     * @return
     * @author yoko
     * @date 2020/6/1 19:51
     */
    public DidModel getNewDidCollectionAccountByWxGroup(DidModel didModel, String orderMoney, int countGroupNum){
        // 判断此用户是否被锁住
        String lockKey_did = CachedKeyUtils.getCacheKey(CacheKey.LOCK_DID_ORDER, didModel.getId());
        boolean flagLock_did = ComponentUtil.redisIdService.lock(lockKey_did);
        if (flagLock_did){

            // 根据用户ID查询此用户下的有效微信群数据集合
            DidCollectionAccountModel didCollectionAccountQuery = HodgepodgeMethod.assembleDidCollectionAccountListByInvalid(didModel.getId(), 3, 1, 3, countGroupNum);
            List<DidCollectionAccountModel> didCollectionAccountList = ComponentUtil.didCollectionAccountService.getEffectiveDidCollectionAccountByWxGroup(didCollectionAccountQuery);
            if (didCollectionAccountList != null && didCollectionAccountList.size() > 0){
                // 判断有效群是否是规定的数量
                if (didCollectionAccountList.size() == countGroupNum){
                    // 循环判断此收款账号是否有挂单的存在
                    for (DidCollectionAccountModel didCollectionAccountModel : didCollectionAccountList){
                        // 锁住这个收款账号
                        String lockKey_did_collection_account = CachedKeyUtils.getCacheKey(CacheKey.LOCK_DID_COLLECTION_ACCOUNT_FOR, didCollectionAccountModel.getId());
                        boolean flagLock_did_collection_account = ComponentUtil.redisIdService.lock(lockKey_did_collection_account);
                        if (flagLock_did_collection_account){
                            // 判断这个收款账号是否有挂单的-正在进行中
                            String strKeyCache_check_lock_did_collection_account_order_ing = CachedKeyUtils.getCacheKey(CacheKey.LOCK_DID_COLLECTION_ACCOUNT_ORDER_ING, didCollectionAccountModel.getId());
                            String strCache_check_strKeyCache_check_lock_did_collection_account_order_ing = (String) ComponentUtil.redisService.get(strKeyCache_check_lock_did_collection_account_order_ing);
                            if (StringUtils.isBlank(strCache_check_strKeyCache_check_lock_did_collection_account_order_ing)){
                                // 查询此用户的收款账号的上一个订单的订单状态是否是否已回复
                                OrderModel orderQuery = HodgepodgeMethod.assembleOrderByNewest(didModel.getId(), 3, 1, didCollectionAccountModel.getId());
                                OrderModel orderModel = ComponentUtil.orderService.getNewestOrder(orderQuery);
                                if (orderModel != null && orderModel.getId() > 0){
                                    if (orderModel.getIsRedPack() == 1){
                                        // 未发过红包
                                        if (orderModel.getOrderStatus() == 1){
                                            // 未发过红包，并且订单未超时
                                            continue;
                                        }
                                    }else {
                                        // 发过红包
                                        if (orderModel.getIsReply() < ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_THREE){
                                            // 发过红包，并且未回复结果
                                            continue;
                                        }
                                    }
                                }
                                // 正式给码

                                // 赋值收款账号
                                didModel.setCollectionAccountId(didCollectionAccountModel.getId());
                                if (didCollectionAccountModel.getWxId() != null && didCollectionAccountModel.getWxId() > 0){
                                    didModel.setWxId(didCollectionAccountModel.getWxId());
                                }
                                if (!StringUtils.isBlank(didCollectionAccountModel.getDdQrCode())){
                                    didModel.setDdQrCode(didCollectionAccountModel.getDdQrCode());
                                }
                                if (!StringUtils.isBlank(didCollectionAccountModel.getUserId())){
                                    didModel.setUserId(didCollectionAccountModel.getUserId());
                                }
                                if (!StringUtils.isBlank(didCollectionAccountModel.getPayee())){
                                    didModel.setPayee(didCollectionAccountModel.getPayee());
                                }
                                if (!StringUtils.isBlank(didCollectionAccountModel.getAcName())){
                                    didModel.setZfbAcNum(didCollectionAccountModel.getAcName());
                                }

                                // redis存储-用户收款账号
                                String strKeyCache_lock_did_order_ing = CachedKeyUtils.getCacheKey(CacheKey.LOCK_DID_COLLECTION_ACCOUNT_ORDER_ING, didCollectionAccountModel.getId());
                                ComponentUtil.redisService.set(strKeyCache_lock_did_order_ing, String.valueOf(didCollectionAccountModel.getId()) + "," + orderMoney, FIVE_MIN);
                                return didModel;

                            }
                        }
                        // 解锁
                        ComponentUtil.redisIdService.delLock(lockKey_did_collection_account);
                    }

                }else {
                    // 时时保持的有效群的数量不在规定的数量内
                    return null;
                }
            }else {
                // 没有查询到有效的微信群数据
                return null;
            }
            // 解锁
            ComponentUtil.redisIdService.delLock(lockKey_did);
        }
        return null;
    }



    /**
     * @Description: 筛选可使用的微信群收款账号以及用户-根据池子
     * @param didModel - 用户信息
     * @param orderMoney - 派单金额
     * @param countGroupNum - 微信群有效个数才允许正常出码
     * @param toWxidList - 被限制的原始微信ID集合
     * @return
     * @author yoko
     * @date 2020/6/1 19:51
     */
    public DidModel getDidCollectionAccountByPool(DidModel didModel, String orderMoney, int countGroupNum, List<String> toWxidList){
        // 判断此用户是否被锁住
        String lockKey_did = CachedKeyUtils.getCacheKey(CacheKey.LOCK_DID_ORDER, didModel.getId());
        boolean flagLock_did = ComponentUtil.redisIdService.lock(lockKey_did);
        if (flagLock_did){

            // 根据用户ID查询此用户下的有效微信群数据集合
            DidCollectionAccountModel didCollectionAccountQuery = HodgepodgeMethod.assembleDidCollectionAccountListEffective(didModel.getId(), 3, 1, 3,1,2, countGroupNum, toWxidList);
            List<DidCollectionAccountModel> didCollectionAccountList = ComponentUtil.didCollectionAccountService.getEffectiveDidCollectionAccountByWxGroup(didCollectionAccountQuery);
            if (didCollectionAccountList != null && didCollectionAccountList.size() > 0){
                // 判断有效群是否是规定的数量
                // 循环判断此收款账号是否有挂单的存在
                for (DidCollectionAccountModel didCollectionAccountModel : didCollectionAccountList){
                    // 锁住这个收款账号
                    String lockKey_did_collection_account = CachedKeyUtils.getCacheKey(CacheKey.LOCK_DID_COLLECTION_ACCOUNT_FOR, didCollectionAccountModel.getId());
                    boolean flagLock_did_collection_account = ComponentUtil.redisIdService.lock(lockKey_did_collection_account);
                    if (flagLock_did_collection_account){
                        // 判断这个收款账号是否有挂单的-正在进行中
                        String strKeyCache_check_lock_did_collection_account_order_ing = CachedKeyUtils.getCacheKey(CacheKey.LOCK_DID_COLLECTION_ACCOUNT_ORDER_ING, didCollectionAccountModel.getId());
                        String strCache_check_strKeyCache_check_lock_did_collection_account_order_ing = (String) ComponentUtil.redisService.get(strKeyCache_check_lock_did_collection_account_order_ing);
                        if (StringUtils.isBlank(strCache_check_strKeyCache_check_lock_did_collection_account_order_ing)){
                            // 校验此用户的收款账号的上一个订单的订单状态是否是否已回复
                            boolean check_order = checkOrderIsOk(didModel.getId(), didCollectionAccountModel.getId());
                            if (!check_order){
                                continue;
                            }
                            // 正式给码

                            // 赋值收款账号
                            didModel.setCollectionAccountId(didCollectionAccountModel.getId());
                            if (didCollectionAccountModel.getWxId() != null && didCollectionAccountModel.getWxId() > 0){
                                didModel.setWxId(didCollectionAccountModel.getWxId());
                            }
                            if (!StringUtils.isBlank(didCollectionAccountModel.getDdQrCode())){
                                didModel.setDdQrCode(didCollectionAccountModel.getDdQrCode());
                            }
                            if (!StringUtils.isBlank(didCollectionAccountModel.getUserId())){
                                didModel.setUserId(didCollectionAccountModel.getUserId());
                            }
                            if (!StringUtils.isBlank(didCollectionAccountModel.getPayee())){
                                didModel.setPayee(didCollectionAccountModel.getPayee());
                            }
                            if (!StringUtils.isBlank(didCollectionAccountModel.getAcName())){
                                didModel.setZfbAcNum(didCollectionAccountModel.getAcName());
                            }

                            // redis存储-用户收款账号
                            String strKeyCache_lock_did_order_ing = CachedKeyUtils.getCacheKey(CacheKey.LOCK_DID_COLLECTION_ACCOUNT_ORDER_ING, didCollectionAccountModel.getId());
                            ComponentUtil.redisService.set(strKeyCache_lock_did_order_ing, String.valueOf(didCollectionAccountModel.getId()) + "," + orderMoney, FIVE_MIN);
                            return didModel;
                        }else{
                            // 有挂单， 判断此收款账号的微信ID是否属于被金额限制范围内的

                            String strKeyCache_to_wxid_range_money_time = CachedKeyUtils.getCacheKey(CacheKey.TO_WXID_RANGE_MONEY_TIME, didModel.getId(), didCollectionAccountModel.getUserId());
                            String strCache_strKeyCache_to_wxid_range_money_time = (String) ComponentUtil.redisService.get(strKeyCache_to_wxid_range_money_time);
                            if (!StringUtils.isBlank(strCache_strKeyCache_to_wxid_range_money_time)){
                                // 属于锁住的微信，每次只能给出一个码
                                break;
                            }
                        }
                    }
                    // 解锁
                    ComponentUtil.redisIdService.delLock(lockKey_did_collection_account);
                }
            }else {
                // 没有查询到有效的微信群数据
                return null;
            }
            // 解锁
            ComponentUtil.redisIdService.delLock(lockKey_did);
        }
        return null;
    }




    /**
     * @Description: 筛选可使用的微信群收款账号以及用户-根据池子
     * @param didModel - 用户信息
     * @param orderMoney - 派单金额
     * @param toWxid - 筛选出来正在使用的微信ID
     * @return
     * @author yoko
     * @date 2020/6/1 19:51
     */
    public DidModel getDidCollectionAccountByPoolTwo(DidModel didModel, String orderMoney, String toWxid){
        // 判断此用户是否被锁住
        String lockKey_did = CachedKeyUtils.getCacheKey(CacheKey.LOCK_DID_ORDER, didModel.getId());
        boolean flagLock_did = ComponentUtil.redisIdService.lock(lockKey_did);
        if (flagLock_did){

            // 根据用户ID查询此用户下的有效微信群数据集合
            DidCollectionAccountModel didCollectionAccountQuery = HodgepodgeMethod.assembleDidCollectionAccountListEffectiveByToWxid(didModel.getId(), 3, 1, 3,1,2, toWxid);
            List<DidCollectionAccountModel> didCollectionAccountList = ComponentUtil.didCollectionAccountService.getEffectiveDidCollectionAccountByUserId(didCollectionAccountQuery);
            if (didCollectionAccountList != null && didCollectionAccountList.size() > 0){
                // 判断有效群是否是规定的数量
                // 循环判断此收款账号是否有挂单的存在
                for (DidCollectionAccountModel didCollectionAccountModel : didCollectionAccountList){
                    // 锁住这个收款账号
                    String lockKey_did_collection_account = CachedKeyUtils.getCacheKey(CacheKey.LOCK_DID_COLLECTION_ACCOUNT_FOR, didCollectionAccountModel.getId());
                    boolean flagLock_did_collection_account = ComponentUtil.redisIdService.lock(lockKey_did_collection_account);
                    if (flagLock_did_collection_account){
                        // 判断这个收款账号是否有挂单的-正在进行中
                        String strKeyCache_check_lock_did_collection_account_order_ing = CachedKeyUtils.getCacheKey(CacheKey.LOCK_DID_COLLECTION_ACCOUNT_ORDER_ING, didCollectionAccountModel.getId());
                        String strCache_check_strKeyCache_check_lock_did_collection_account_order_ing = (String) ComponentUtil.redisService.get(strKeyCache_check_lock_did_collection_account_order_ing);
                        if (StringUtils.isBlank(strCache_check_strKeyCache_check_lock_did_collection_account_order_ing)){
                            // 校验此用户的收款账号的上一个订单的订单状态是否是否已回复
                            boolean check_order = checkOrderIsOk(didModel.getId(), didCollectionAccountModel.getId());
                            if (!check_order){
                                continue;
                            }
                            // 正式给码

                            // 赋值收款账号
                            didModel.setCollectionAccountId(didCollectionAccountModel.getId());
                            if (didCollectionAccountModel.getWxId() != null && didCollectionAccountModel.getWxId() > 0){
                                didModel.setWxId(didCollectionAccountModel.getWxId());
                            }
                            if (!StringUtils.isBlank(didCollectionAccountModel.getDdQrCode())){
                                didModel.setDdQrCode(didCollectionAccountModel.getDdQrCode());
                            }
                            if (!StringUtils.isBlank(didCollectionAccountModel.getUserId())){
                                didModel.setUserId(didCollectionAccountModel.getUserId());
                            }
                            if (!StringUtils.isBlank(didCollectionAccountModel.getAcName())){
                                didModel.setZfbAcNum(didCollectionAccountModel.getAcName());
                            }
                            if (!StringUtils.isBlank(didCollectionAccountModel.getPayee())){
                                didModel.setPayee(didCollectionAccountModel.getPayee());
                            }


                            // redis存储-用户收款账号
                            String strKeyCache_lock_did_order_ing = CachedKeyUtils.getCacheKey(CacheKey.LOCK_DID_COLLECTION_ACCOUNT_ORDER_ING, didCollectionAccountModel.getId());
                            ComponentUtil.redisService.set(strKeyCache_lock_did_order_ing, String.valueOf(didCollectionAccountModel.getId()) + "," + orderMoney, FIVE_MIN);
                            return didModel;
                        }else{
                            // 有挂单， 判断此收款账号的微信ID是否属于被金额限制范围内的

                            String strKeyCache_to_wxid_range_money_time = CachedKeyUtils.getCacheKey(CacheKey.TO_WXID_RANGE_MONEY_TIME, didModel.getId(), didCollectionAccountModel.getUserId());
                            String strCache_strKeyCache_to_wxid_range_money_time = (String) ComponentUtil.redisService.get(strKeyCache_to_wxid_range_money_time);
                            if (!StringUtils.isBlank(strCache_strKeyCache_to_wxid_range_money_time)){
                                // 属于锁住的微信，每次只能给出一个码

                                // 发送微信排序优先级调控：金额范围被限制
                                String delayTime = strCache_strKeyCache_to_wxid_range_money_time;
                                DidWxSortModel didWxSortModel = HodgepodgeMethod.assembleDidWxSortSend(didModel.getId(), toWxid, 4, delayTime);
                                String strKeyCache_didWxSort = CachedKeyUtils.getCacheKey(CacheKey.DID_WX_SORT, didModel.getId(), toWxid);
                                ComponentUtil.redisService.set(strKeyCache_didWxSort, JSON.toJSONString(didWxSortModel, SerializerFeature.WriteMapNullValue, SerializerFeature.WriteNullStringAsEmpty));
                                break;
                            }
                        }
                    }
                    // 解锁
                    ComponentUtil.redisIdService.delLock(lockKey_did_collection_account);
                }
            }else {
                // 没有查询到有效的微信群数据

                // 发送微信排序优先级调控
                String delayTime = "";
                try{
                    delayTime = DateUtil.getNowPlusTime();
                }catch (Exception e){
                    e.printStackTrace();
                }
                DidWxSortModel didWxSortModel = HodgepodgeMethod.assembleDidWxSortSend(didModel.getId(), toWxid, 4, delayTime);
                String strKeyCache_didWxSort = CachedKeyUtils.getCacheKey(CacheKey.DID_WX_SORT, didModel.getId(), toWxid);
                ComponentUtil.redisService.set(strKeyCache_didWxSort, JSON.toJSONString(didWxSortModel, SerializerFeature.WriteMapNullValue, SerializerFeature.WriteNullStringAsEmpty));

                return null;
            }
            // 解锁
            ComponentUtil.redisIdService.delLock(lockKey_did);
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

    /**
     * @Description: 校验次用户的收款账号是否有未回复的订单
     * @return
     * @author yoko
     * @date 2020/8/24 16:14
    */
    public boolean checkOrderIsOk(long did, long collectionAccountId){
        boolean flag = true;
        // 查询此用户的收款账号的上一个订单的订单状态是否是否已回复
        OrderModel orderQuery = HodgepodgeMethod.assembleOrderByNewest(did, 3, 1, collectionAccountId);
        OrderModel orderModel = ComponentUtil.orderService.getNewestOrder(orderQuery);
        if (orderModel != null && orderModel.getId() > 0){
            if (orderModel.getIsRedPack() == 1){
                // 未发过红包
                if (orderModel.getOrderStatus() == 1){
                    // 未发过红包，并且订单未超时
                    flag = false;
                }
            }else {
                // 发过红包
                if (orderModel.getIsReply() < ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_THREE){
                    // 发过红包，并且未回复结果
                    flag = false;
                }
            }
        }
        return flag;
    }






}
