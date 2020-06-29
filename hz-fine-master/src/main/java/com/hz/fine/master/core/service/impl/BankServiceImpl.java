package com.hz.fine.master.core.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.hz.fine.master.core.common.dao.BaseDao;
import com.hz.fine.master.core.common.exception.ServiceException;
import com.hz.fine.master.core.common.service.impl.BaseServiceImpl;
import com.hz.fine.master.core.common.utils.BeanUtils;
import com.hz.fine.master.core.common.utils.StringUtil;
import com.hz.fine.master.core.common.utils.constant.CacheKey;
import com.hz.fine.master.core.common.utils.constant.CachedKeyUtils;
import com.hz.fine.master.core.common.utils.constant.ErrorCode;
import com.hz.fine.master.core.mapper.BankMapper;
import com.hz.fine.master.core.model.bank.BankModel;
import com.hz.fine.master.core.model.strategy.StrategyBankLimit;
import com.hz.fine.master.core.model.strategy.StrategyData;
import com.hz.fine.master.core.protocol.response.bank.BuyBank;
import com.hz.fine.master.core.service.BankService;
import com.hz.fine.master.util.ComponentUtil;
import com.hz.fine.master.util.HodgepodgeMethod;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @Description 银行的Service层的实现层
 * @Author yoko
 * @Date 2020/5/18 19:08
 * @Version 1.0
 */
@Service
public class BankServiceImpl<T> extends BaseServiceImpl<T> implements BankService<T> {
    /**
     * 5分钟.
     */
    public long FIVE_MIN = 300;

    /**
     * 11分钟.
     */
    public long ELEVEN_MIN = 660;

    public long TWO_HOUR = 2;

    @Autowired
    private BankMapper bankMapper;

    public BaseDao<T> getDao() {
        return bankMapper;
    }

    @Override
    public Map<String, Object> screenBank(List<BankModel> bankList, List<StrategyBankLimit> strategyBankLimitList, List<StrategyData> strategyMoneyAddSubtractList, String orderMoney) throws Exception{
        Map<String, Object> map = new HashMap<>();
        String money = "";
        BankModel dataModel = new BankModel();
        List<BankModel> yesSpecialList = new ArrayList<>();// 优先级高的银行卡
        List<BankModel> noSpecialList = new ArrayList<>();// 不是优先级高的银行卡/普通银行卡
        for (BankModel bankModel : bankList){
            if (bankModel.getSpecialType() == 2){
                yesSpecialList.add(bankModel);
            }else {
                noSpecialList.add(bankModel);
            }
        }
        // 优先消耗
        if (yesSpecialList != null && yesSpecialList.size() > 0){
            map = checkDataAndGetMoney(yesSpecialList, strategyBankLimitList, strategyMoneyAddSubtractList, orderMoney);
        }

        if (map == null || map.isEmpty()){
            map = checkDataAndGetMoney(noSpecialList, strategyBankLimitList, strategyMoneyAddSubtractList, orderMoney);
        }

        if (map != null){
            // 缓存挂单- 表示这个银行卡的这个金额已经给出去了
            String strKeyCache = CachedKeyUtils.getCacheKey(CacheKey.HANG_MONEY, map.get("bankId"), map.get("distributionMoney"));
            ComponentUtil.redisService.set(strKeyCache, map.get("distributionMoney").toString(), TWO_HOUR, TimeUnit.HOURS);
        }

        return map;
    }

    @Override
    public List<BuyBank> screenBankByBuy(List<BankModel> bankList) {
        List<BuyBank> resList = new ArrayList<>();
        for (BankModel dataModel : bankList){
            boolean flag = checkBank(dataModel);
            if (flag){
                BuyBank buyBank = new BuyBank();
                buyBank.setId(dataModel.getId());
                buyBank.setNickname("****");
                // 获取已成功收款的金额
                String strKeyCache_lock_bank_day_suc_money = CachedKeyUtils.getCacheKey(CacheKey.LOCK_BANK_DAY_SUC_MONEY, dataModel.getId());
                String strCache_lock_bank_day_suc_money = (String) ComponentUtil.redisService.get(strKeyCache_lock_bank_day_suc_money);
                String money = "";
                if (!StringUtils.isBlank(strCache_lock_bank_day_suc_money)){
                    money = strCache_lock_bank_day_suc_money;
                }else{
                    // 随机生成一个5万以下的数据
                    money = String.valueOf(HodgepodgeMethod.getRandom());
                }
                String buyNum = StringUtil.getBigDecimalSubtractByStr(dataModel.getInDayMoney(), money);
                buyBank.setBuyNum(buyNum);
                buyBank.setSellNum(money);
                // 计算占比：还剩多少的比例
                String divide = StringUtil.getBigDecimalDivide(money, dataModel.getInDayMoney());
                String ratio = StringUtil.getBigDecimalSubtractByStr("1", divide);
                ratio = StringUtil.getMultiply(ratio, "100");
                buyBank.setRatio(ratio);
                buyBank.setMinQuota("1000.00");
                buyBank.setMaxQuota("50000.00");
                buyBank.setUnitPrice("1.00");
                resList.add(buyBank);

            }
        }

        return resList;
    }

    @Override
    public String getMoney(BankModel bankModel, String orderMoney) throws Exception {
        String money = "";
        // 获取订单金额的百分之十的额度的值
        String [] orderMoney_arr = orderMoney.split("\\.");
        int minMoney = Integer.parseInt(orderMoney_arr[0]);
        int maxMoney = (int) (minMoney + minMoney * 0.1);
        int count = 0;// 加一把防止死循环的锁
        while (true){
            if (count <= 100){
                int num = StringUtil.getRandom(minMoney, maxMoney);
                // 先锁定这个数值
                String str = String.valueOf(num) + ".00";
                // 这个金额可以使用，判断这个金额是否被锁定；
                String lockKey_orderMoney = CachedKeyUtils.getCacheKey(CacheKey.LOCK_MONEY_CENT, bankModel.getId(), str);
                boolean flagLock_orderMoney = ComponentUtil.redisIdService.lock(lockKey_orderMoney);
                if (flagLock_orderMoney){
                    // 判断金额是否有挂单金额
                    String redis_data = getRedisMoneyDataByKey(CacheKey.HANG_MONEY, bankModel.getId(), str);
                    if (StringUtils.isBlank(redis_data)) {
                        // 表示整数金额目前没有挂单的金额，可以直接给出订单金额
                        money = str;
                    }
                }
                if (!StringUtils.isBlank(money)){
                    // 缓存挂单- 表示这个银行卡的这个金额已经给出去了
                    String strKeyCache = CachedKeyUtils.getCacheKey(CacheKey.HANG_MONEY, bankModel.getId(), money);
                    ComponentUtil.redisService.set(strKeyCache, money, TWO_HOUR, TimeUnit.HOURS);

                    // 解锁
                    ComponentUtil.redisIdService.delLock(lockKey_orderMoney);
                    break;
                }
                count ++;
            }else{
                break;
            }
        }

        return money;
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
     * @Description: 组装缓存key查询缓存中存在的数据:银行卡挂单金额数据
     * <p>
     *     这里获取银行卡目前是否有挂单的数据金额
     * </p>
     * @param cacheKey - 缓存的类型key
     * @param money - 数据的ID
     * @return
     * @author yoko
     * @date 2020/5/20 14:59
     */
    public String getRedisMoneyDataByKey(String cacheKey, long dataId, String money){
        String str = null;
        String strKeyCache = CachedKeyUtils.getCacheKey(cacheKey, dataId, money);
        String strCache = (String) ComponentUtil.redisService.get(strKeyCache);
        if (StringUtils.isBlank(strCache)){
            return str;
        }else{
            str = strCache;
            return str;
        }
    }


    /**
     * @Description: check金额是否超过上限
     * <p>
     *     这里check的金额上限包括：日月总金额
     * </p>
     * @param cacheKey - 缓存的类型Key
     * @param dataId - 数据的主键ID
     * @param orderMoney - 订单金额
     * @param moneyLimit - 要限制的金额
     * @return boolean
     * @author yoko
     * @date 2020/5/20 16:39
     */
    public boolean checkMoney(String cacheKey, long dataId, String orderMoney, String moneyLimit){
        boolean flag;
        String money = "";
        String redis_money = getRedisDataByKey(cacheKey, dataId);
        if (StringUtils.isBlank(redis_money)){
            money = orderMoney;
        }else{
            // 缓存中的金额加订单金额
            money = StringUtil.getBigDecimalAdd(redis_money, orderMoney);
        }
        flag = StringUtil.getBigDecimalSubtract(moneyLimit, money);
        return flag;
    }


    /**
     * @Description: check金额是否超过上限
     * <p>
     *     这里check的金额上限包括：日月总金额
     * </p>
     * @param cacheKey - 缓存的类型Key
     * @param dataId - 数据的主键ID
     * @return boolean
     * @author yoko
     * @date 2020/5/20 16:39
     */
    public boolean checkRedisMoney(String cacheKey, long dataId){
        String redis_money = getRedisDataByKey(cacheKey, dataId);
        if (StringUtils.isBlank(redis_money)){
            return true;
        }else{
            return false;
        }
    }


    /**
     * @Description: check次数是否超过上限
     * <p>
     *     这里check的次数上限包括：日月总次数
     * </p>
     * @param cacheKey - 缓存的类型Key
     * @param dataId - 数据的主键ID
     * @param numLimit - 要限制的次数
     * @return boolean
     * @author yoko
     * @date 2020/5/20 16:39
     */
    public boolean checkNum(String cacheKey, long dataId, int numLimit){
        boolean flag;
        int num = 0;
        String redis_num = getRedisDataByKey(cacheKey, dataId);
        if (StringUtils.isBlank(redis_num)){
            num = 1;
        }else{
            // 缓存中的金额加订单金额
            num = 1 + Integer.parseInt(redis_num);
        }
        if (numLimit < num){
            flag = false;
        }else {
            flag = true;
        }
        return flag;
    }

    /**
     * @Description: check次数是否超过上限
     * <p>
     *     这里check的次数上限包括：日月总次数
     * </p>
     * @param cacheKey - 缓存的类型Key
     * @param dataId - 数据的主键ID
     * @return boolean
     * @author yoko
     * @date 2020/5/20 16:39
     */
    public boolean checkRedisNum(String cacheKey, long dataId){
        String redis_num = getRedisDataByKey(cacheKey, dataId);
        if (StringUtils.isBlank(redis_num)){
            return true;
        }else{
            return false;
        }
    }


    /**
     * @Description: 根据银行卡，筛选出当前银行卡可用金额
     * @param strategyMoneyAddSubtractList - 订单金额加减范围列表的数据
     * @param cacheKey - 缓存类型= HANG_MONEY
     * @param bankId - 银行卡的ID
     * @param orderMoney - 订单金额
     * @return java.lang.String
     * @author yoko
     * @date 2020/5/20 19:07
     */
    public String getUseMoney(List<StrategyData> strategyMoneyAddSubtractList, String cacheKey, long bankId, String orderMoney){
        String str = null;
        int num = 0;
        // 这个金额可以使用，判断这个金额是否被锁定；
        String lockKey_orderMoney = CachedKeyUtils.getCacheKey(CacheKey.LOCK_MONEY_CENT, bankId, orderMoney);
        boolean flagLock_orderMoney = ComponentUtil.redisIdService.lock(lockKey_orderMoney);
        if (flagLock_orderMoney){
            // 首先判断整数金额是否有挂单金额
            String redis_data = getRedisMoneyDataByKey(cacheKey, bankId, orderMoney);
            if (StringUtils.isBlank(redis_data)) {
                // 表示整数金额目前没有挂单的金额，可以直接给出订单金额
                str = orderMoney;
            }
            num = 1;
        }
        if (StringUtils.isBlank(str)){
            // 表示整数金额已有挂单了：先删除整数金额的锁
//            if (num == 1){
//                // 解锁
//                ComponentUtil.redisIdService.delLock(lockKey_orderMoney);
//            }

            // 表示整数金额目前有挂单金额，需要进行订单金额的加减
            List<StrategyData> resList = new ArrayList<StrategyData>();
            resList = strategyMoneyAddSubtractList;
            Iterator<StrategyData> itList = resList.iterator();
            while (itList.hasNext()) {
                StrategyData data = itList.next();
                if (data.getStgValueTwo() == 1){
                    // 订单金额 + 策略补充金额
                    String money = StringUtil.getBigDecimalAdd(orderMoney, data.getStgValue());
                    String res_data = getRedisMoneyDataByKey(cacheKey, bankId, money);
                    if (StringUtils.isBlank(res_data)){
                        // 这个金额可以使用，判断这个金额是否被锁定；
                        String lockKey = CachedKeyUtils.getCacheKey(CacheKey.LOCK_MONEY_CENT, bankId, res_data);
                        boolean flagLock = ComponentUtil.redisIdService.lock(lockKey);
                        // 但是这里一定要把可用金额进行锁定：不然并发的时候会出大问题
                        if (!flagLock){
                            // 另外一个进程已经锁住这张银行卡的这个金额
                            itList.remove();
                        }

                    }else {
                        itList.remove();
                    }
                }else{
                    // 订单金额 - 策略补充金额
                    String money = StringUtil.getBigDecimalSubtractByStr(orderMoney, data.getStgValue());
                    String res_data = getRedisMoneyDataByKey(cacheKey, bankId, money);
                    if (StringUtils.isBlank(res_data)){
                        System.out.println();
                        // 这个金额可以使用，判断这个金额是否被锁定；
                        String lockKey = CachedKeyUtils.getCacheKey(CacheKey.LOCK_MONEY_CENT, bankId, res_data);
                        boolean flagLock = ComponentUtil.redisIdService.lock(lockKey);
                        // 但是这里一定要把可用金额进行锁定：不然并发的时候会出大问题
                        if (!flagLock){
                            // 另外一个进程已经锁住这张银行卡的这个金额
                            itList.remove();
                        }
                    }else {
                        itList.remove();
                    }
                }
            }

            // 最后符合金额的列表集合
            if (resList == null || resList.size() <= 0){
                return str;
            }

            // 从符合的金额列表集合中随机选择一个金额
            StrategyData strategyData = new StrategyData();
            if (resList != null && resList.size() > 0){
                int random = new Random().nextInt(resList.size());
                strategyData = resList.get(random);// 被选中的金额规则

                // 解锁其它金额
                for (StrategyData data : resList){
                    if (data.getId() != strategyData.getId()){
                        // 未被选中的
                        if (data.getStgValueTwo() == 1){
                            // 订单金额 + 策略补充金额
                            String money = StringUtil.getBigDecimalAdd(orderMoney, data.getStgValue());
                            String res_data = getRedisMoneyDataByKey(cacheKey, bankId, money);
                            String lockKey = CachedKeyUtils.getCacheKey(CacheKey.LOCK_MONEY_CENT, bankId, res_data);
                            // 解锁
                            ComponentUtil.redisIdService.delLock(lockKey);
                        }else {
                            // 订单金额 - 策略补充金额
                            String money = StringUtil.getBigDecimalSubtractByStr(orderMoney, data.getStgValue());
                            String res_data = getRedisMoneyDataByKey(cacheKey, bankId, money);
                            String lockKey = CachedKeyUtils.getCacheKey(CacheKey.LOCK_MONEY_CENT, bankId, res_data);
                            // 解锁
                            ComponentUtil.redisIdService.delLock(lockKey);
                        }

                    }else{
                        // 被选中的
                        if (data.getStgValueTwo() == 1){
                            // 订单金额 + 策略补充金额
                            str = StringUtil.getBigDecimalAdd(orderMoney, data.getStgValue());

                        }else {
                            // 订单金额 - 策略补充金额
                            str = StringUtil.getBigDecimalSubtractByStr(orderMoney, data.getStgValue());
                        }
                    }
                }
            }
        }

        return str;
    }


    /**
     * @Description: 筛选出最终符合银行卡的金额
     * <p>
     *     这里实现由两部分组成：1.check校验上限
     *     2.具体筛选可用金额
     * </p>
     * @param bankList - 可用的银行卡集合
     * @param strategyBankLimitList - 银行卡日月总限制规则策略
     * @param strategyMoneyAddSubtractList - 订单金额加减范围列表
     * @param orderMoney - 订单金额
     * @return java.lang.String
     * @author yoko
     * @date 2020/5/20 19:30
     */
    public Map<String, Object> checkDataAndGetMoney(List<BankModel> bankList, List<StrategyBankLimit> strategyBankLimitList,
                                       List<StrategyData> strategyMoneyAddSubtractList, String orderMoney) throws Exception{
        String money = "";
        BankModel dataModel = new BankModel();
        if (bankList != null && bankList.size() > 0){
            for (BankModel bankModel : bankList){
                // 每次循环，重新给这个金额加减规则赋值：因为在后面调用的时候使用了金额规则删除，会导致这个集合的数据会被全部删除；所以这里每次循环的时候重新赋值
//                List<StrategyData> strategyMoneyAddSubtractList_initial = new ArrayList<>();
//                strategyMoneyAddSubtractList_initial = strategyMoneyAddSubtractList;
                List<StrategyData> strategyMoneyAddSubtractList_initial = BeanUtils.copyList(strategyMoneyAddSubtractList, StrategyData.class);

//                StrategyBankLimit strategyBankLimitData = new StrategyBankLimit();
//                for (StrategyBankLimit strategyBankLimit : strategyBankLimitList){
//                    if (bankModel.getBankType() == strategyBankLimit.getStgKey()){
//                        strategyBankLimitData = strategyBankLimit;
//                    }
//                }
//                if (strategyBankLimitData == null || strategyBankLimitData.getId() <= 0){
//                    throw new ServiceException(ErrorCode.ENUM_ERROR.S00005.geteCode(), ErrorCode.ENUM_ERROR.S00005.geteDesc());
//                }

                // check日收款金额上限
//                boolean dayMoneyFlag = checkMoney(CacheKey.SHARE_BANK_MONEY_DAY, bankModel.getId(), orderMoney, strategyBankLimitData.getInDayMoney());
                boolean dayInMoneyFlag = checkRedisMoney(CacheKey.SHARE_BANK_IN_MONEY_DAY, bankModel.getId());
                if (!dayInMoneyFlag){
                    continue;
                }

                // check日转账金额上限
                boolean dayOutMoneyFlag = checkRedisMoney(CacheKey.SHARE_BANK_OUT_MONEY_DAY, bankModel.getId());
                if (!dayOutMoneyFlag){
                    continue;
                }

                // check月收款金额上限
//                boolean monthMoneyFlag = checkMoney(CacheKey.SHARE_BANK_MONEY_MONTH, bankModel.getId(), orderMoney, strategyBankLimitData.getInMonthMoney());
                boolean monthInMoneyFlag = checkRedisMoney(CacheKey.SHARE_BANK_IN_MONEY_MONTH, bankModel.getId());
                if (!monthInMoneyFlag){
                    continue;
                }

                // check月转账金额上限
                boolean monthOutMoneyFlag = checkRedisMoney(CacheKey.SHARE_BANK_OUT_MONEY_MONTH, bankModel.getId());
                if (!monthOutMoneyFlag){
                    continue;
                }
                // check总收款金额上限
//                boolean totalMoneyFlag = checkMoney(CacheKey.SHARE_BANK_MONEY_TOTAL, bankModel.getId(), orderMoney, strategyBankLimitData.getInTotalMoney());
                boolean totalInMoneyFlag = checkRedisMoney(CacheKey.SHARE_BANK_IN_MONEY_TOTAL, bankModel.getId());
                if (!totalInMoneyFlag){
                    continue;
                }

                // check总转账金额上限
                boolean totalOutMoneyFlag = checkRedisMoney(CacheKey.SHARE_BANK_OUT_MONEY_TOTAL, bankModel.getId());
                if (!totalOutMoneyFlag){
                    continue;
                }

                // check日次数上限
//                boolean dayNumFlag = checkNum(CacheKey.SHARE_BANK_NUM_DAY, bankModel.getId(), strategyBankLimitData.getInDayNum());
                boolean dayNumFlag = checkRedisNum(CacheKey.SHARE_BANK_NUM_DAY, bankModel.getId());
                if (!dayNumFlag){
                    continue;
                }
                // check月次数上限
//                boolean monthNumFlag = checkNum(CacheKey.SHARE_BANK_NUM_MONTH, bankModel.getId(), strategyBankLimitData.getInMonthNum());
                boolean monthNumFlag = checkRedisNum(CacheKey.SHARE_BANK_NUM_MONTH, bankModel.getId());
                if (!monthNumFlag){
                    continue;
                }
                // check总次数上限
//                boolean totalNumFlag = checkNum(CacheKey.SHARE_BANK_NUM_TOTAL, bankModel.getId(), strategyBankLimitData.getInTotalNum());
                boolean totalNumFlag = checkRedisNum(CacheKey.SHARE_BANK_NUM_TOTAL, bankModel.getId());
                if (!totalNumFlag){
                    continue;
                }
                // 筛选此银行卡可用的金额
                money = getUseMoney(strategyMoneyAddSubtractList_initial, CacheKey.HANG_MONEY, bankModel.getId(), orderMoney);
                if (!StringUtils.isBlank(money)){
                    dataModel = bankModel;
                    break;
                }
            }
        }

        Map<String, Object> map = new HashMap<>();
        if (!StringUtils.isBlank(money)){
            map.put("distributionMoney", money);
            map.put("bankModel", dataModel);
            map.put("bankId", dataModel.getId());
            return map;
        }else {
            return null;
        }

    }

    /**
     * @Description: check银行的日上限月上限总上限
     * @param bankModel - 银行信息
     * @return
     * @author yoko
     * @date 2020/6/29 14:37
    */
    public boolean checkBank(BankModel bankModel){
        // check日收款金额上限
        boolean dayInMoneyFlag = checkRedisMoney(CacheKey.SHARE_BANK_IN_MONEY_DAY, bankModel.getId());
        if (!dayInMoneyFlag){
            return false;
        }

        // check日转账金额上限
        boolean dayOutMoneyFlag = checkRedisMoney(CacheKey.SHARE_BANK_OUT_MONEY_DAY, bankModel.getId());
        if (!dayOutMoneyFlag){
            return false;
        }

        // check月收款金额上限
//                boolean monthMoneyFlag = checkMoney(CacheKey.SHARE_BANK_MONEY_MONTH, bankModel.getId(), orderMoney, strategyBankLimitData.getInMonthMoney());
        boolean monthInMoneyFlag = checkRedisMoney(CacheKey.SHARE_BANK_IN_MONEY_MONTH, bankModel.getId());
        if (!monthInMoneyFlag){
            return false;
        }

        // check月转账金额上限
        boolean monthOutMoneyFlag = checkRedisMoney(CacheKey.SHARE_BANK_OUT_MONEY_MONTH, bankModel.getId());
        if (!monthOutMoneyFlag){
            return false;
        }
        // check总收款金额上限
//                boolean totalMoneyFlag = checkMoney(CacheKey.SHARE_BANK_MONEY_TOTAL, bankModel.getId(), orderMoney, strategyBankLimitData.getInTotalMoney());
        boolean totalInMoneyFlag = checkRedisMoney(CacheKey.SHARE_BANK_IN_MONEY_TOTAL, bankModel.getId());
        if (!totalInMoneyFlag){
            return false;
        }

        // check总转账金额上限
        boolean totalOutMoneyFlag = checkRedisMoney(CacheKey.SHARE_BANK_OUT_MONEY_TOTAL, bankModel.getId());
        if (!totalOutMoneyFlag){
            return false;
        }

        // check日次数上限
//                boolean dayNumFlag = checkNum(CacheKey.SHARE_BANK_NUM_DAY, bankModel.getId(), strategyBankLimitData.getInDayNum());
        boolean dayNumFlag = checkRedisNum(CacheKey.SHARE_BANK_NUM_DAY, bankModel.getId());
        if (!dayNumFlag){
            return false;
        }
        // check月次数上限
//                boolean monthNumFlag = checkNum(CacheKey.SHARE_BANK_NUM_MONTH, bankModel.getId(), strategyBankLimitData.getInMonthNum());
        boolean monthNumFlag = checkRedisNum(CacheKey.SHARE_BANK_NUM_MONTH, bankModel.getId());
        if (!monthNumFlag){
            return false;
        }
        // check总次数上限
//                boolean totalNumFlag = checkNum(CacheKey.SHARE_BANK_NUM_TOTAL, bankModel.getId(), strategyBankLimitData.getInTotalNum());
        boolean totalNumFlag = checkRedisNum(CacheKey.SHARE_BANK_NUM_TOTAL, bankModel.getId());
        if (!totalNumFlag){
            return false;
        }
        return true;

    }


}
