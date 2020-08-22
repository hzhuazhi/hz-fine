package com.hz.fine.master.core.mapper;

import com.hz.fine.master.core.common.dao.BaseDao;
import com.hz.fine.master.core.model.did.DidModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Description 用户的Dao层
 * @Author yoko
 * @Date 2020/5/13 18:31
 * @Version 1.0
 */
@Mapper
public interface DidMapper<T> extends BaseDao<T> {

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


    /**
     * @Description: 更新用户的群序号或者更新用户的出码开关
     * @param model
     * @return
     * @author yoko
     * @date 2020/7/30 19:57
    */
    public int updateDidGroupNumOrSwitchType(DidModel model);

    /**
     * @Description: 获取有效的用户-微信群-new
     * <p>
     *     获取有余额，并且有效的微信群收款账号个数超过5个
     * </p>
     * @param model
     * @return
     * @author yoko
     * @date 2020/7/2 10:56
     */
    public List<DidModel> getNewEffectiveDidByWxGroupList(DidModel model);


    /**
     * @Description: 获取可出码用户集合
     * <p>
     *     从池子中正在进行的用户ID集合找出余额比此订单金额大的用户集合数据
     * </p>
     * @param model
     * @return
     * @author yoko
     * @date 2020/8/16 19:23
     */
    public List<DidModel> getDidByWxGroupList(DidModel model);


    /**
     * @Description: 更新用户的操作群个数
     * @param model
     * @return
     * @author yoko
     * @date 2020/7/30 19:57
     */
    public int updateDidOperateGroupNum(DidModel model);


}
