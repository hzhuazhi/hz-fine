package com.hz.fine.master.core.service;

import com.hz.fine.master.core.common.service.BaseService;
import com.hz.fine.master.core.model.did.DidBalanceDeductModel;
import com.hz.fine.master.core.model.did.DidCollectionAccountModel;
import com.hz.fine.master.core.model.did.DidModel;
import com.hz.fine.master.core.model.order.OrderModel;

import java.util.List;

/**
 * @Description 任务订单的Service层
 * @Author yoko
 * @Date 2020/5/21 19:34
 * @Version 1.0
 */
public interface OrderService<T> extends BaseService<T> {

    /**
     * @Description: 筛选出要派单的收款账号
     * @param didList - 可以正常使用的did账号集合
     * @param orderMoney - 订单金额
     * @param payType - 支付类型：1微信，2支付宝，3银行卡
     * @return
     * @author yoko
     * @date 2020/5/26 15:58
    */
    public DidCollectionAccountModel screenCollectionAccount(List<DidModel> didList, String orderMoney, int payType);


    /**
     * @Description: 派发订单成功的金额
     * <p>sum查询出来的数据</p>
     * @param model - 根据用户，订单状态，日期查询总派发订单成功金额
     * @return
     * @author yoko
     * @date 2020/5/29 11:23
     */
    public String getProfitByCurday(OrderModel model);

    /**
     * @Description: 根据订单号获取订单的二维码信息
     * @param model
     * @return
     * @author yoko
     * @date 2020/6/8 19:51
    */
    public OrderModel getOrderQrCodeByOrderNo(OrderModel model);

    /**
     * @Description: 根据订单号查询订单状态
     * @param model
     * @return
     * @author yoko
     * @date 2020/6/8 20:00
    */
    public int getOrderStatus(OrderModel model);

    /**
     * @Description: 处理派发订单的业务流程
     * <p>
     *     1.在用户账户余额中扣除相对应的订单金额。
     *     2.把扣除的金额录入到段峰
     * </p>
     * @param didBalanceDeductModel - 扣除用于余额
     * @param orderModel - 派发的订单信息
     * @return
     * @author yoko
     * @date 2020/6/20 12:25
    */
    public boolean handleOrder(DidBalanceDeductModel didBalanceDeductModel, OrderModel orderModel);
}
