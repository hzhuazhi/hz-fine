package com.hz.fine.master.core.mapper;

import com.hz.fine.master.core.common.dao.BaseDao;
import com.hz.fine.master.core.model.did.DidCollectionAccountModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Description 用户的收款账号的Dao层
 * @Author yoko
 * @Date 2020/5/15 13:54
 * @Version 1.0
 */
@Mapper
public interface DidCollectionAccountMapper<T> extends BaseDao<T> {


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
     * @Description: 计算无效的收款账号数量-微信群
     * <p>
     *     分页
     * </p>
     * @param model
     * @return
     * @author yoko
     * @date 2020/7/31 14:33
    */
    public int countDidCollectionAccountByInvalid(DidCollectionAccountModel model);

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

    /**
     * @Description: 根据自动解析来更新收款账号的二维码信息
     * @param model
     * @return
     * @author yoko
     * @date 2020/8/20 11:17
    */
    public int updateQrCodeByAnalysis(DidCollectionAccountModel model);

    /**
     * @Description: 根据用户以及用户的原始微信ID获取有效群集合
     * @param model
     * @return
     * @author yoko
     * @date 2020/9/1 0:38
     */
    public List<DidCollectionAccountModel> getEffectiveDidCollectionAccountByUserId(DidCollectionAccountModel model);
}
