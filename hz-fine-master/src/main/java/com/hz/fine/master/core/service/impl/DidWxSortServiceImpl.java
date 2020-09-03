package com.hz.fine.master.core.service.impl;

import com.hz.fine.master.core.common.dao.BaseDao;
import com.hz.fine.master.core.common.service.impl.BaseServiceImpl;
import com.hz.fine.master.core.mapper.DidWxSortMapper;
import com.hz.fine.master.core.model.did.DidCollectionAccountModel;
import com.hz.fine.master.core.model.did.DidWxSortModel;
import com.hz.fine.master.core.service.DidWxSortService;
import com.hz.fine.master.util.ComponentUtil;
import com.hz.fine.master.util.HodgepodgeMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * @Description 用户的微信出码排序的Service层的实现层
 * @Author yoko
 * @Date 2020/8/31 16:41
 * @Version 1.0
 */
@Service
public class DidWxSortServiceImpl<T> extends BaseServiceImpl<T> implements DidWxSortService<T> {
    /**
     * 5分钟.
     */
    public long FIVE_MIN = 300;

    public long TWO_HOUR = 2;

    @Autowired
    private DidWxSortMapper didWxSortMapper;

    public BaseDao<T> getDao() {
        return didWxSortMapper;
    }

    @Override
    public int maxSort(DidWxSortModel model) {
        return didWxSortMapper.maxSort(model);
    }

    @Override
    public int addBySort(DidWxSortModel model) {
        return didWxSortMapper.addBySort(model);
    }

    @Override
    public int updateInUse(DidWxSortModel model) {
        return didWxSortMapper.updateInUse(model);
    }

    @Override
    public int addByExist(DidWxSortModel model) {
        DidWxSortModel didWxSortModel = (DidWxSortModel)didWxSortMapper.findByObject(model);
        if (didWxSortModel != null && didWxSortModel.getId() != null && didWxSortModel.getId() > 0){
            return 0;
        }else {
            return didWxSortMapper.addBySort(model);
        }
    }

//    @Override
//    public DidWxSortModel screenDidWxSort(long did, List<String> monitorWxList) {
//        DidWxSortModel dataModel = new DidWxSortModel();
//        int sort = 0;
//        // 查询此用户的微信集合
//        DidWxSortModel didWxSortByDid = new DidWxSortModel();
//        didWxSortByDid.setDid(did);
//        List<DidWxSortModel> didWxSortList = didWxSortMapper.findByCondition(didWxSortByDid);
//        if (didWxSortList != null && didWxSortList.size() > 0){
//            for (DidWxSortModel didWxSortModel : didWxSortList){
//                if (monitorWxList != null && monitorWxList.size() > 0){
//                    for (String toWxid : monitorWxList){
//                        if (didWxSortModel.getToWxid().equals(toWxid) && didWxSortModel.getInUse() == 2){
//                            // 更新此微信的使用状态
//                            DidWxSortModel didWxSortUpInUse = HodgepodgeMethod.assembleDidWxSortData(didWxSortModel.getId(), 0, null, 0, 0, 1, 0, 0);
//                            didWxSortMapper.updateInUse(didWxSortUpInUse);
//
//                            // 纪录排序的位置
//                            sort = didWxSortModel.getSort();
//
//                        }
//                    }
//                }else{
//                    // 查询出正在使用的原始微信ID
//                    if (didWxSortModel.getInUse() == 2){
//                        dataModel = didWxSortModel;
//                        return dataModel;
//                    }
//                }
//            }
//
//            if (sort > 0){
//                // 表示正在使用的被限制住了
//                // 获取此微信位置的下一个排序位置的原始微信用户
//                DidWxSortModel didWxSortNextQuery = HodgepodgeMethod.assembleDidWxSortData(0, did, null, 0, 0, 0, sort, 0);
//                DidWxSortModel didWxSortNextModel = (DidWxSortModel)didWxSortMapper.findByObject(didWxSortNextQuery);
//                if (didWxSortNextModel != null && didWxSortNextModel.getId() != null && didWxSortNextModel.getId() > 0){
////                    // 查询此微信是否有有效群
////                    DidCollectionAccountModel didCollectionAccountQuery = HodgepodgeMethod.assembleDidCollectionAccountListEffectiveByToWxid(did, 3, 1, 3,1,2, didWxSortNextModel.getToWxid());
////                    List<DidCollectionAccountModel> didCollectionAccountList = ComponentUtil.didCollectionAccountService.getEffectiveDidCollectionAccountByUserId(didCollectionAccountQuery);
//
//                    if (didWxSortNextModel.getInUse() == 2){
//                        dataModel = didWxSortNextModel;
//                        return dataModel;
//                    }else{
//                        // 更新成正在使用状态
//                        DidWxSortModel didWxSortUpInUse = HodgepodgeMethod.assembleDidWxSortData(didWxSortNextModel.getId(), 0, null, 0, 0, 2, 0, 0);
//                        didWxSortMapper.updateInUse(didWxSortUpInUse);
//
//                        dataModel = didWxSortNextModel;
//                        return dataModel;
//                    }
//                }else{
//                    int num = 0;
//                    // 表示一个轮询结束，从排序的最小位置开始
//                    DidWxSortModel didWxSortMinQuery = HodgepodgeMethod.assembleDidWxSortData(0, did, null, 0, 0, 0, 0, 0);
//                    DidWxSortModel didWxSortMinModel = (DidWxSortModel)didWxSortMapper.findByObject(didWxSortMinQuery);
//                    if (monitorWxList != null && monitorWxList.size() > 0){
//                        for (String toWxid : monitorWxList){
//                            if (didWxSortMinModel.getToWxid().equals(toWxid)){
//                                num = 1;
//                            }
//                        }
//                    }
//                    if (num == 1){
//                        return null;
//                    }else {
//                        // 更新排序最小的那个原始微信ID，更新成正在使用状态
//                        DidWxSortModel didWxSortUpInUse = HodgepodgeMethod.assembleDidWxSortData(didWxSortMinModel.getId(), 0, null, 0, 0, 2, 0, 0);
//                        didWxSortMapper.updateInUse(didWxSortUpInUse);
//
//                        dataModel = didWxSortMinModel;
//                        return dataModel;
//                    }
//
//                }
//
//            }
//        }else {
//            return null;
//        }
//        return null;
//    }





    @Override
    public DidWxSortModel screenDidWxSort(long did, DidWxSortModel didWxSortModel) {
        DidWxSortModel dataModel = new DidWxSortModel();

        // 获取不在延迟时间使用的微信集合
        DidWxSortModel didWxSortQuery = HodgepodgeMethod.assembleDidWxSortData(0, did, null,
                0, 0, 0, 0, 0, null, "1", null);
        List<DidWxSortModel> didWxSortList = ComponentUtil.didWxSortService.findByCondition(didWxSortQuery);
        if (didWxSortList == null || didWxSortList.size() <= 0){
            return null;
        }

        if (didWxSortModel != null && didWxSortModel.getId() != null && didWxSortModel.getId() > 0){
            // 这里表示有当前使用的微信，但是此微信旗下没有微信群
            dataModel = getUseDidWxSort(didWxSortList, did, didWxSortModel.getSort());
            if (dataModel == null || dataModel.getId() == null || dataModel.getId() <= 0){
                // 表示大于当前使用微信的排序的所有微信没有有效群

                // 从位置最小的开始check
                dataModel = getUseDidWxSort(didWxSortList, did, 0);
            }

        }else {
            // 表示当前没有任何一个微信正在使用

            // 从位置最小的开始check
            dataModel = getUseDidWxSort(didWxSortList, did, 0);

        }

        // 统一更新此用户的所有微信更新成未使用状态：不管筛选没有有，如果没有正好全部更新成未使用，如果筛选有，则下面的if判断进行更新正在使用的
        DidWxSortModel didWxSortUpInUseByDid = HodgepodgeMethod.assembleDidWxSortData(0, did, null,
                0, 0, 1, 0, 0, null, null, null);
        ComponentUtil.didWxSortService.updateInUse(didWxSortUpInUseByDid);

        if (dataModel != null && dataModel.getId() != null && dataModel.getId() > 0){
            // 在把被选中的微信更新成正在使用
            // 把此微信更新成使用状态
            DidWxSortModel didWxSortUpInUseIng = HodgepodgeMethod.assembleDidWxSortData(dataModel.getId(), 0, null,
                    0, 0, 2, 0, 0, null, null, null);
            ComponentUtil.didWxSortService.updateInUse(didWxSortUpInUseIng);

        }

        return dataModel;
    }


    public DidWxSortModel getUseDidWxSort(List<DidWxSortModel> wxSortList, long did, int nowSort){
        for (DidWxSortModel didWxSortModel : wxSortList){
            if (didWxSortModel.getSort() > nowSort){
                // 查询这个微信是否有有效群
                DidCollectionAccountModel didCollectionAccountToWxQuery = HodgepodgeMethod.assembleDidCollectionAccountListEffectiveByToWxid(did, 3, 1, 3,1,2, didWxSortModel.getToWxid());
                List<DidCollectionAccountModel> didCollectionAccountToWxList = ComponentUtil.didCollectionAccountService.getEffectiveDidCollectionAccountByUserId(didCollectionAccountToWxQuery);
                if (didCollectionAccountToWxList != null && didCollectionAccountToWxList.size() > 0){
                    return didWxSortModel;
                }
            }
        }
        return null;
    }



}
