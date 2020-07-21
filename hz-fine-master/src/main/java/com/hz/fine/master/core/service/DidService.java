package com.hz.fine.master.core.service;

import com.hz.fine.master.core.common.service.BaseService;
import com.hz.fine.master.core.model.did.DidModel;

import java.util.List;

/**
 * @Description 用户的Service层
 * @Author yoko
 * @Date 2020/5/13 18:34
 * @Version 1.0
 */
public interface DidService<T> extends BaseService<T> {

    /**
     * @Description: 查询出可派单的用户集合
     * @param model
     * @return
     * @author yoko
     * @date 2020/5/25 10:42
    */
    public List<DidModel> getEffectiveDidList(DidModel model);

    /**
     * @Description: 修改用户金额信息-根据派单
     * <p>
     *     更新字段如下：：余额 = 余额 - 派单金额， 冻结金额 = 冻结金额 + 派单金额
     * </p>
     * @param model - 用户信息
     * @return
     * @author yoko
     * @date 2020/6/9 10:25
     */
    public int updateDidMoneyByOrder(DidModel model);


    /**
     * @Description: 获取有效的用户-支付宝
     * <p>
     *     获取有余额，并且配置了支付宝账号的用户
     * </p>
     * @param model
     * @return
     * @author yoko
     * @date 2020/7/2 10:56
    */
    public List<DidModel> getEffectiveDidByZfbList(DidModel model);

    /**
     * @Description: 更新用户余额
     * @param model
     * @return
     * @author yoko
     * @date 2020/7/2 14:56
    */
    public int updateDidBalance(DidModel model);

    /**
     * @Description: 获取有效的用户-微信群
     * <p>
     *     获取有余额，并且配置了已审核通过、没有超过有效期的微信群的用户
     * </p>
     * @param model
     * @return
     * @author yoko
     * @date 2020/7/2 10:56
     */
    public List<DidModel> getEffectiveDidByWxGroupList(DidModel model);
}
