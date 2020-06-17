package com.hz.fine.master.core.mapper;

import com.hz.fine.master.core.common.dao.BaseDao;
import com.hz.fine.master.core.model.did.DidCollectionAccountQrCodeModel;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Description 用户的收款账号二维码的Dao层
 * @Author yoko
 * @Date 2020/6/17 14:53
 * @Version 1.0
 */
@Mapper
public interface DidCollectionAccountQrCodeMapper<T> extends BaseDao<T> {

    /**
     * @Description: 批量更新二维码的使用状态或者批量删除二维码
     * @param model
     * @return
     * @author yoko
     * @date 2020/6/17 15:25
     */
    public int updateBatchStatus(DidCollectionAccountQrCodeModel model);

    /**
     * @Description: 批量添加用户收款账号的二维码信息
     * @param model
     * @return
     * @author yoko
     * @date 2020/6/17 18:06
    */
    public int addBatchDidCollectionAccountQrCode(DidCollectionAccountQrCodeModel model);
}
