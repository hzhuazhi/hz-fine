package com.hz.fine.master.core.mapper;

import com.hz.fine.master.core.common.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Description 大杂烩的task的Dao层
 * @Author yoko
 * @Date 2020/1/21 17:55
 * @Version 1.0
 */
@Mapper
public interface TaskHodgepodgeMapper<T> extends BaseDao<T> {


    /**
     * @Description: 更新转账、提现结果的数据的运行成功、失败的结果
     * @param obj
     * @return
     * @author yoko
     * @date 2020/1/11 16:30
     */
    public int updateTransResultStatus(Object obj);

    /**
     * @Description: 用户转账/提现失败，更新状态
     * <p>更新表u_cash_out_proced_log的is_ok的状态，更新成2</p>
     * @param obj - 要更新的条件与状态
     * @return
     * @author yoko
     * @date 2020/1/21 19:46
     */
    public int updateCashOutProcedLogTheIsOk(Object obj);
}
