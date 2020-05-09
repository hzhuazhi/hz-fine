//package com.hz.fine.master.core.runner;
//
//import com.alibaba.fastjson.JSON;
//import com.alipay.api.response.AlipayFundTransUniTransferResponse;
//import com.hz.gather.master.core.common.alipay.Alipay;
//import com.hz.gather.master.core.common.utils.constant.CacheKey;
//import com.hz.gather.master.core.common.utils.constant.CachedKeyUtils;
//import com.hz.gather.master.core.common.utils.constant.ServerConstant;
//import com.hz.gather.master.core.model.alipay.AlipayData;
//import com.hz.gather.master.core.model.alipay.AlipayTransferModel;
//import com.hz.gather.master.core.model.entity.UCashOutLog;
//import com.hz.gather.master.core.model.pay.PayCustModel;
//import com.hz.gather.master.core.model.task.TaskAlipayNotifyModel;
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
// * @Description 阿里支付宝的task类
// * @Author yoko
// * @Date 2020/1/9 19:28
// * @Version 1.0
// */
//@Component
//@EnableScheduling
//public class TaskAlipay {
//    private final static Logger log = LoggerFactory.getLogger(TaskAlipay.class);
//
//
//    @Value("${task.limit.num}")
//    private int limitNum;
//
//
//    /**
//     * @Description: 阿里云支付：单笔转账到账户
//     * @author yoko
//     * @date 2019/12/27 21:30
//     */
////    @Scheduled(cron = "1 * * * * ?")
//    @Scheduled(fixedDelay = 1000) // 每秒执行
//    public void taskTransferAlipay() throws Exception{
//        log.info("TaskAlipay.taskTransferAlipay()------------------进来了!");
//        StatusModel statusQuery = TaskMethod.assembleTaskByAliapyTransferStatusQuery(limitNum);
//        List<UCashOutLog> synchroList = ComponentUtil.taskService.getTransferList(statusQuery);
//        for (UCashOutLog data : synchroList){
//            if (data != null){
//                boolean flag = false;
//                try{
//                    String lockKey = CachedKeyUtils.getCacheKey(CacheKey.LOCK_TRANSFER, data.getId());
//                    boolean flagLock = ComponentUtil.redisIdService.lock(lockKey);
//                    if (flagLock){
//                        // 组装阿里支付宝转账所需的数据
//                        AlipayTransferModel alipayTransferModel = TaskMethod.assembleAlipayTransfer(data);
//                        String strData = JSON.toJSONString(alipayTransferModel);
//                        AlipayFundTransUniTransferResponse alipayFundTransUniTransferResponse = Alipay.transferAlipay(strData);
//                        if (alipayFundTransUniTransferResponse != null){
//                            if (alipayFundTransUniTransferResponse.isSuccess()){
//                                flag = true;
//                                // 更新此次task的状态：更新成成功
//                                StatusModel statusModel = TaskMethod.assembleUpdateStatusModel(data.getId(), ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_THREE);
//                                ComponentUtil.taskService.updateTransStatus(statusModel);
//                                // 添加提现成功数据
//                                ComponentUtil.payService.insertSuccess(data.getMemberId(), data.getRealMoney().doubleValue());
//                            }else{
//                                // 更新此次task的状态：更新成失败
//                                StatusModel statusModel = TaskMethod.assembleUpdateStatusModel(data.getId(), ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_TWO);
//                                ComponentUtil.taskService.updateTransStatus(statusModel);
//                            }
//                            AlipayData addAlipayData = TaskMethod.assembleAlipayData(alipayTransferModel, alipayFundTransUniTransferResponse, data);
//                            ComponentUtil.taskService.addTransData(addAlipayData);
//                        }else{
//                            if (!flag){
//                                // #这里如果阿里支付已经转账成功，则不进行重试了；因为如果重试则表示又要给对方进行转账
//                                // 更新此次task的状态：更新成失败
//                                StatusModel statusModel = TaskMethod.assembleUpdateStatusModel(data.getId(), ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_TWO);
//                                ComponentUtil.taskService.updateTransStatus(statusModel);
//                            }
//
//                        }
//                    }
//                    // 解锁
//                    ComponentUtil.redisIdService.delLock(lockKey);
//                }catch (Exception e){
//                    // 更新此次task的状态：更新成失败
//                    StatusModel statusModel = TaskMethod.assembleUpdateStatusModel(data.getId(), ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_TWO);
//                    ComponentUtil.taskService.updateTransStatus(statusModel);
//                    log.error(String.format("this TaskAlipay.taskTransferAlipay() is error , the id=%s !", data.getId()));
//                    e.printStackTrace();
//                }
//            }
//        }
//        log.info("TaskAlipay.taskTransferAlipay()------------------结束了!");
//    }
//
//    /**
//     * @Description: 阿里云支付：用户支付（用户充值）
//     * @author yoko
//     * @date 2019/12/27 21:30
//     */
////    @Scheduled(cron = "1 * * * * ?")
//    @Scheduled(fixedDelay = 3000) // 每3秒执行
//    public void taskAlipay(){
//        log.info("TaskAlipay.taskAlipay()------------------进来了!");
//        // 查询要跑的数据
//        StatusModel statusQuery = TaskMethod.assembleTaskAlipayNotifyStatusQuery(limitNum);
//        List<TaskAlipayNotifyModel> dataList = ComponentUtil.taskService.getTaskAlipayNotify(statusQuery);
//        if (dataList != null && dataList.size() > ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_ZERO){
//            for (TaskAlipayNotifyModel dataModel : dataList){
//                //锁住这个订单交易流水
//                String lockKey = CachedKeyUtils.getCacheKey(CacheKey.LOCK_TASK_ALIPAY_NOTIFY, dataModel.getId());
//                boolean flagLock = ComponentUtil.redisIdService.lock(lockKey);
//                if (flagLock){
//                    try {
//                        // 更新用户以及用户想关联的层级数据、以及直推的数据
//                        boolean flag = ComponentUtil.payService.paymentSuccess(dataModel.getMemberId().intValue(), dataModel.getOutTradeNo());
//                        if (flag){
//                            // 组装更改运行状态的数据：更新成成功
//                            StatusModel statusModel = TaskMethod.assembleUpdateStatusModel(dataModel.getId(), ServerConstant.PUBLIC_CONSTANT.RUN_STATUS_THREE);
//                            ComponentUtil.taskService.updateTaskAlipayNotifyStatus(statusModel);
//                            // 添加已经支付成功的用户纪录
//                            PayCustModel payCustModel = TaskMethod.assemblePayCust(dataModel.getMemberId());
//                            ComponentUtil.taskService.addPayCust(payCustModel);
//                        }else {
//                            // 更新此次task的状态：更新成失败
//                            StatusModel statusModel = TaskMethod.assembleUpdateStatusModel(dataModel.getId(), ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_TWO);
//                            ComponentUtil.taskService.updateTaskAlipayNotifyStatus(statusModel);
//                        }
//                    }catch (Exception e){
//                        log.error(String.format("this TaskAlipay.taskAlipay() is error , the dataId=%s !", dataModel.getId()));
//                        e.printStackTrace();
//                        // 更新此次task的状态：更新成失败
//                        StatusModel statusModel = TaskMethod.assembleUpdateStatusModel(dataModel.getId(), ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_TWO);
//                        ComponentUtil.taskService.updateTaskAlipayNotifyStatus(statusModel);
//                    }finally {
//                        // 解锁
//                        ComponentUtil.redisIdService.delLock(lockKey);
//                    }
//                }
//
//            }
//        }
//        log.info("TaskAlipay.taskAlipay()------------------结束了!");
//    }
//
//    public static void main(String [] args) throws Exception{
//        TaskAlipay t = new TaskAlipay();
//        t.taskTransferAlipay();
//    }
//}
