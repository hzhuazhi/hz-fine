package com.hz.fine.master.core.mapper;

import com.hz.fine.master.core.common.dao.BaseDao;
import com.hz.fine.master.core.model.order.OrderModel;
import org.apache.ibatis.annotations.Mapper;

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
}
