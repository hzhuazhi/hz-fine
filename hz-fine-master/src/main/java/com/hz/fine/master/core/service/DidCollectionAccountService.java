package com.hz.fine.master.core.service;

import com.hz.fine.master.core.common.service.BaseService;
import com.hz.fine.master.core.model.did.DidCollectionAccountModel;

import java.util.List;

/**
 * @Description 用户的收款账号的Service层
 * @Author yoko
 * @Date 2020/5/15 14:01
 * @Version 1.0
 */
public interface DidCollectionAccountService<T> extends BaseService<T> {

    /**
     * @Description: 修改用户收款账号的基本信息
     * <p>基本信息包括：1收款账号名称：用户备注使用=ac_name</p>
     * @param model
     * @return
     * @author yoko
     * @date 2020/5/18 14:52
    */
    public void updateBasic(DidCollectionAccountModel model);

    /**
     * @Description: 更新用户收款账号信息
     * @param model - 用户收款账号信息
     * @return
     * @author yoko
     * @date 2020/5/18 15:52
    */
    public void updateDidCollectionAccount(DidCollectionAccountModel model);

    /**
     * @Description: 更新用户支付宝收款账号信息
     * @param model - 用户支付宝收款账号信息
     * @return
     * @author yoko
     * @date 2020/5/18 15:52
     */
    public void updateDidCollectionAccountZfb(DidCollectionAccountModel model);

    /**
     * @Description: 获取用户最新的收款账号的信息
     * <p>
     *     yn 可以等于任意
     * </p>
     * @param model
     * @return
     * @author yoko
     * @date 2020/7/30 16:22
     */
    public DidCollectionAccountModel getNewDidCollectionAccount(DidCollectionAccountModel model);

    /**
     * @Description: 获取此用户的最新的收款账号
     * <p>
     *     yn = 0
     * </p>
     * @param model
     * @return
     * @author yoko
     * @date 2020/7/30 21:59
     */
    public DidCollectionAccountModel getDidCollectionAccount(DidCollectionAccountModel model);

    /**
     * @Description: 查询无效的收款账号-微信群
     * <p>
     *     分页
     * </p>
     * @param model
     * @return
     * @author yoko
     * @date 2020/7/31 14:32
     */
    public List<DidCollectionAccountModel> getDidCollectionAccountByInvalid(DidCollectionAccountModel model);

    /**
     * @Description: 获取有效的微信群收款账号
     * @param model
     * @return
     * @author yoko
     * @date 2020/7/31 16:37
     */
    public List<DidCollectionAccountModel> getEffectiveDidCollectionAccountByWxGroup(DidCollectionAccountModel model);

}
