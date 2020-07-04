package com.hz.fine.master.core.service;

import com.hz.fine.master.core.common.service.BaseService;
import com.hz.fine.master.core.model.did.DidRechargeModel;

/**
 * @Description 用户充值记录的Service层
 * @Author yoko
 * @Date 2020/5/19 14:47
 * @Version 1.0
 */
public interface DidRechargeService<T> extends BaseService<T> {

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
