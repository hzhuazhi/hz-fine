package com.hz.fine.master.core.mapper;

import com.hz.fine.master.core.common.dao.BaseDao;
import com.hz.fine.master.core.model.order.OrderModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Description 任务订单的Dao层
 * @Author yoko
 * @Date 2020/5/21 19:32
 * @Version 1.0
 */
@Mapper
public interface OrderMapper<T> extends BaseDao<T> {

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

}
