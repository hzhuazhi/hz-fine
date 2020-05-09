//package com.hz.fine.master.core.runner;
//
//import com.hz.gather.master.core.common.utils.constant.CacheKey;
//import com.hz.gather.master.core.common.utils.constant.CachedKeyUtils;
//import com.hz.gather.master.core.common.utils.constant.ServerConstant;
//import com.hz.gather.master.core.model.entity.UCashOutLog;
//import com.hz.gather.master.core.model.entity.UCashOutProcedLog;
//import com.hz.gather.master.core.model.task.base.StatusModel;
//import com.hz.gather.master.util.ComponentUtil;
//import com.hz.gather.master.util.TaskMethod;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.scheduling.annotation.EnableScheduling;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//import java.util.List;
//
///**
// * @Description 大杂烩的task类
// * @Author yoko
// * @Date 2020/1/21 17:37
// * @Version 1.0
// */
//@Component
//@EnableScheduling
//public class TaskHodgepodge {
//
//    private final static Logger log = LoggerFactory.getLogger(TaskHodgepodge.class);
//
//
//    @Value("${task.limit.num}")
//    private int limitNum;
//
//
//    /**
//     * @Description: 用户提现之后的处理的task
//     * <p>
//     *     1.修改u_cash_out_proced_log表的状态，把is_ok修改成2
//     *     2.用户提现失败的金额进行返还
//     * </p>
//     * @author yoko
//     * @date 2019/12/27 21:30
//     */
////    @Scheduled(cron = "1 * * * * ?")
//    @Scheduled(fixedDelay = 5000) // 每秒执行
//    public void taskCashOutHandle() throws Exception{
//        log.info("TaskAlipay.taskCashOutHandle()------------------进来了!");
//        StatusModel statusQuery = TaskMethod.assembleTaskByCashOutHandleStatusQuery(limitNum);
//        List<UCashOutLog> synchroList = ComponentUtil.taskHodgepodgeService.getTransferResultList(statusQuery);
//        for (UCashOutLog data : synchroList){
//            if (data != null){
//                try{
//                    String lockKey = CachedKeyUtils.getCacheKey(CacheKey.LOCK_TRANSFER_RESULT, data.getId());
//                    boolean flagLock = ComponentUtil.redisIdService.lock(lockKey);
//                    if (flagLock){
//                        if (data.getRunStatus() == ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_TWO){
//                            // 执行用户金额返还
//                            int num = ComponentUtil.userInfoService.caseMoneyFail(data.getMemberId(), data.getRealMoney().doubleValue(), data.getOutTradeNo());
//                            if (num == ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_ONE){
//                                // 更新此次task的状态：更新成成功
//                                StatusModel statusModel = TaskMethod.assembleUpdateResultStatusModel(data.getId(), ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_THREE);
//                                ComponentUtil.taskHodgepodgeService.updateTransResultStatus(statusModel);
//                            }else{
//                                // 更新此次task的状态：更新成失败
//                                StatusModel statusModel = TaskMethod.assembleUpdateResultStatusModel(data.getId(), ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_TWO);
//                                ComponentUtil.taskHodgepodgeService.updateTransResultStatus(statusModel);
//                            }
//                        }else {
//                            // 更新此次task的状态：更新成成功
//                            StatusModel statusModel = TaskMethod.assembleUpdateResultStatusModel(data.getId(), ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_THREE);
//                            ComponentUtil.taskHodgepodgeService.updateTransResultStatus(statusModel);
//                        }
//                        int isOk = ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_ZERO;
//                        if (data.getRunStatus() == ServerConstant.PUBLIC_CONSTANT.RUN_STATUS_THREE){
//                            isOk = ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_ONE;
//                        }else if(data.getRunStatus() == ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_TWO){
//                            isOk = ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_TWO;
//                        }
//                        UCashOutProcedLog uCashOutProcedLog = TaskMethod.assembleUCashOutProcedLog(data.getMemberId(), data.getOutTradeNo(), isOk);
//                        ComponentUtil.taskHodgepodgeService.updateCashOutProcedLogTheIsOk(uCashOutProcedLog);
//                    }
//                    // 解锁
//                    ComponentUtil.redisIdService.delLock(lockKey);
//                }catch (Exception e){
//                    // 更新此次task的状态：更新成失败
//                    StatusModel statusModel = TaskMethod.assembleUpdateResultStatusModel(data.getId(), ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_TWO);
//                    ComponentUtil.taskHodgepodgeService.updateTransResultStatus(statusModel);
//                    log.error(String.format("this TaskAlipay.taskCashOutHandle() is error , the id=%s !", data.getId()));
//                    e.printStackTrace();
//                }
//            }
//        }
//        log.info("TaskAlipay.taskCashOutHandle()------------------结束了!");
//    }
//}
