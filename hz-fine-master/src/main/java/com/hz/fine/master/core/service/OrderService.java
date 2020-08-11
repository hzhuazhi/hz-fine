package com.hz.fine.master.core.service;

import com.hz.fine.master.core.common.service.BaseService;
import com.hz.fine.master.core.model.did.DidBalanceDeductModel;
import com.hz.fine.master.core.model.did.DidCollectionAccountModel;
import com.hz.fine.master.core.model.did.DidModel;
import com.hz.fine.master.core.model.order.OrderModel;
import com.hz.fine.master.core.protocol.request.order.RequestOrder;

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
     * @param didModel -
     * @return
     * @author yoko
     * @date 2020/6/20 12:25
    */
    public boolean handleOrder(DidBalanceDeductModel didBalanceDeductModel, OrderModel orderModel, DidModel didModel);


    /**
     * @Description: 获取成功订单的数据
     * @param model
     * @return
     * @author yoko
     * @date 2020/6/29 20:02
    */
    public List<OrderModel> getSucOrderList(OrderModel model);

    /**
     * @Description: 获取初始化的派单信息
     * @param model
     * @return
     * @author yoko
     * @date 2020/7/1 16:24
    */
    public OrderModel getInitOrder(OrderModel model);

    /**
     * @Description: 修改用户操作的状态
     * <p>
     *     订单状态_用户操作的状态：1初始化，2失败，3超时后默认成功，4用户点击成功
     * </p>
     * @param model
     * @return
     * @author yoko
     * @date 2020/7/1 16:46
    */
    public int updateDidStatus(OrderModel model);

    /**
     * @Description: 筛选出要派单的支付宝收款账号以及用户
     * @param didList - 可以正常使用的did账号集合
     * @param orderMoney - 订单金额
     * @return
     * @author yoko
     * @date 2020/5/26 15:58
     */
    public DidModel screenCollectionAccountByZfb(List<DidModel> didList, String orderMoney);

    /**
     * @Description: 处理订单派发的所有逻辑
     * @param orderModel - 订单信息
     * @param didBalanceDeductModel - 用户余额要扣减的流水
     * @param didModel - 更新用户的余额
     * @return
     * @author yoko
     * @date 2020/7/2 15:03
    */
    public boolean handleOrder(OrderModel orderModel, DidBalanceDeductModel didBalanceDeductModel, DidModel didModel) throws Exception;

    /**
     * @Description: 查询是否符合给与消耗奖励的订单信息
     * @param model
     * @return
     * @author yoko
     * @date 2020/7/7 18:28
    */
    public OrderModel getOrderByReward(OrderModel model);

    /**
     * @Description: 获取直推用户某天派单消耗成功的总金额
     * @param model - 用户did集合，日期，订单成功
     * @return
     * @author yoko
     * @date 2020/6/6 11:22
     */
    public String directSumMoney(OrderModel model);

    /**
     * @Description: 筛选出要派单的微信群收款账号以及用户
     * @param didList - 可以正常使用的did账号集合
     * @param orderMoney - 订单金额
     * @return
     * @author yoko
     * @date 2020/5/26 15:58
     */
    public DidModel screenCollectionAccountByWxGroup(List<DidModel> didList, String orderMoney);

    /**
     * @Description: 根据用户查询最新的一个派单的订单信息
     * @param model
     * @return
     * @author yoko
     * @date 2020/7/20 20:24
    */
    public OrderModel getNewestOrder(OrderModel model);


    /**
     * @Description: 筛选出要派单的微信群收款账号以及用户-new
     * @param didList - 可以正常使用的did账号集合
     * @param orderMoney - 订单金额
     * @param countGroupNum - 微信群有效个数才允许正常出码
     * @return
     * @author yoko
     * @date 2020/5/26 15:58
     */
    public DidModel screenNewCollectionAccountByWxGroup(List<DidModel> didList, String orderMoney, int countGroupNum);

    
    /**
     * @Description: 根据条件查询订单信息
     * <p>
     *     查询已发红包，但是没有回复的订单
     * </p>
     * @param model
     * @return 
     * @author yoko
     * @date 2020/8/11 15:49
    */
    public OrderModel getOrderByNotIsReply(OrderModel model);
}
