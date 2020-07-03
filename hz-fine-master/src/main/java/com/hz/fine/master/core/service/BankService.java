package com.hz.fine.master.core.service;

import com.hz.fine.master.core.common.service.BaseService;
import com.hz.fine.master.core.model.bank.BankModel;
import com.hz.fine.master.core.model.strategy.StrategyBankLimit;
import com.hz.fine.master.core.model.strategy.StrategyData;
import com.hz.fine.master.core.protocol.response.bank.BuyBank;

import java.util.List;
import java.util.Map;

/**
 * @Description 银行的Service层
 * @Author yoko
 * @Date 2020/5/18 19:06
 * @Version 1.0
 */
public interface BankService<T> extends BaseService<T> {

    /**
     * @Description: 筛选银行卡
     * @param bankList - 银行卡信息
     * @param strategyBankLimitList - 银行卡流水日月总规则
     * @param strategyMoneyAddSubtractList - 订单金额加减范围列表
     * @param orderMoney - 订单充值的金额
     * @return Map - 返回筛选确认的银行卡以及选择的金额
     * @author yoko
     * @date 2020/5/20 14:45
    */
    public Map<String, Object> screenBank(List<BankModel> bankList, List<StrategyBankLimit> strategyBankLimitList,
                                          List<StrategyData> strategyMoneyAddSubtractList, String orderMoney) throws Exception;


    /**
     * @Description: 筛选出未被限制的银行卡
     * @param bankList - 银行卡集合数据
     * @return
     * @author yoko
     * @date 2020/6/29 14:33
    */
    public List<BuyBank> screenBankByBuy(List<BankModel> bankList);

    /**
     * @Description: 筛选此银行可用金额
     * @param bankModel - 银行卡信息
     * @param orderMoney - 订单金额
     * @return
     * @author yoko
     * @date 2020/6/29 16:29
    */
    public String getMoney(BankModel bankModel, String orderMoney) throws Exception;

    /**
     * @Description: 筛选出未超过日月总上限的银行卡
     * @param bankList
     * @return
     * @author yoko
     * @date 2020/7/3 14:52
    */
    public List<BankModel> screenBankByMoney(List<BankModel> bankList);

    /**
     * @Description: 筛选可用的金额集合
     * @param bankModel - 银行卡信息
     * @param strategyMoneyList - 策略里面的金额
     * @return
     * @author yoko
     * @date 2020/7/3 16:22
    */
    public List<String> screenMoneyList(BankModel bankModel, List<StrategyData> strategyMoneyList);
}
