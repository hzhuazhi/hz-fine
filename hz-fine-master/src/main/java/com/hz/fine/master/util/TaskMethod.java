package com.hz.fine.master.util;

import com.hz.fine.master.core.common.utils.constant.ServerConstant;
import com.hz.fine.master.core.model.task.base.StatusModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @Description 定时任务的公共类
 * @Author yoko
 * @Date 2020/1/11 16:20
 * @Version 1.0
 */
public class TaskMethod {
    private static Logger log = LoggerFactory.getLogger(TaskMethod.class);


    /**
     * @Description: 组装查询定时任务阿里支付宝转账的查询条件
     * @param limitNum - 多少条数据
     * @return
     * @author yoko
     * @date 2020/1/11 16:23
    */
    public static StatusModel assembleTaskByAliapyTransferStatusQuery(int limitNum){
        StatusModel resBean = new StatusModel();
        resBean.setRunNum(ServerConstant.PUBLIC_CONSTANT.RUN_NUM_FIVE);
        resBean.setRunStatus(ServerConstant.PUBLIC_CONSTANT.RUN_STATUS_THREE);
        resBean.setLimitNum(limitNum);
        return resBean;
    }

}
