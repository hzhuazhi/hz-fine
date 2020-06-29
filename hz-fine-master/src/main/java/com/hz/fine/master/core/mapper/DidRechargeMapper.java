package com.hz.fine.master.core.mapper;

import com.hz.fine.master.core.common.dao.BaseDao;
import com.hz.fine.master.core.model.did.DidRechargeModel;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Description 用户充值记录的Dao层
 * @Author yoko
 * @Date 2020/5/19 14:47
 * @Version 1.0
 */
@Mapper
public interface DidRechargeMapper<T> extends BaseDao<T> {

    /**
     * @Description: 充值订单申诉
     * @param model
     * @return
     * @author yoko
     * @date 2020/6/9 12:06
     */
    public int updateDidRechargeByAppeal(DidRechargeModel model);

    /**
     * @Description: 修改用户充值之后的存入打款的账号信息
     * @param model
     * @return
     * @author yoko
     * @date 2020/6/29 17:33
     */
    public int updateDidRechargeByDeposit(DidRechargeModel model);

}
