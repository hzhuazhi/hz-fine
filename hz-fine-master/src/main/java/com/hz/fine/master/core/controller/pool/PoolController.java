package com.hz.fine.master.core.controller.pool;

import com.alibaba.fastjson.JSON;
import com.hz.fine.master.core.common.exception.ExceptionMethod;
import com.hz.fine.master.core.common.utils.JsonResult;
import com.hz.fine.master.core.common.utils.SignUtil;
import com.hz.fine.master.core.common.utils.StringUtil;
import com.hz.fine.master.core.common.utils.constant.ServerConstant;
import com.hz.fine.master.core.model.RequestEncryptionJson;
import com.hz.fine.master.core.model.ResponseEncryptionJson;
import com.hz.fine.master.core.model.did.DidCollectionAccountModel;
import com.hz.fine.master.core.model.did.DidModel;
import com.hz.fine.master.core.model.did.DidWxMonitorModel;
import com.hz.fine.master.core.model.order.OrderModel;
import com.hz.fine.master.core.model.pool.PoolOpenModel;
import com.hz.fine.master.core.model.pool.PoolWaitModel;
import com.hz.fine.master.core.model.strategy.StrategyModel;
import com.hz.fine.master.core.protocol.request.pool.RequestPool;
import com.hz.fine.master.util.ComponentUtil;
import com.hz.fine.master.util.HodgepodgeMethod;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Description 接单池的Controller层
 * @Author yoko
 * @Date 2020/8/13 16:06
 * @Version 1.0
 */
@RestController
@RequestMapping("/fine/pool")
public class PoolController {

    private static Logger log = LoggerFactory.getLogger(PoolController.class);

    /**
     * 5分钟.
     */
    public long FIVE_MIN = 300;

    /**
     * 11分钟.
     */
    public long ELEVEN_MIN = 660;

    /**
     * 15分钟.
     */
    public long FIFTEEN_MIN = 900;

    /**
     * 30分钟.
     */
    public long THIRTY_MIN = 30;

    public long TWO_HOUR = 2;

    @Value("${secret.key.token}")
    private String secretKeyToken;

    @Value("${secret.key.sign}")
    private String secretKeySign;


    /**
     * @Description: 获取用户在池子中抢单状态
     * <p>
     *     池子中状态分别是：1未排队，2排队中，3进行中；
     *     未排队跟进行中无需查询排队当前人数以及预计等待时长
     * </p>
     * @param request
     * @param response
     * @return com.gd.chain.common.utils.JsonResult<java.lang.Object>
     * @author yoko
     * @date 2019/11/25 22:58
     * local:http://localhost:8086/fine/pool/getPoolStatus
     * 请求的属性类:RequestPool
     * 必填字段:{"agtVer":1,"clientVer":1,"clientType":1,"ctime":201911071802959,"cctime":201911071802959,"sign":"abcdefg","token":"111111"}
     * 加密字段:{"jsonData":"eyJhZ3RWZXIiOjEsImNsaWVudFZlciI6MSwiY2xpZW50VHlwZSI6MSwiY3RpbWUiOjIwMTkxMTA3MTgwMjk1OSwiY2N0aW1lIjoyMDE5MTEwNzE4MDI5NTksInNpZ24iOiJhYmNkZWZnIiwidG9rZW4iOiIxMTExMTEifQ=="}
     * 客户端加密字段:ctime+秘钥=sign
     * 返回加密字段:stime+秘钥=sign
     * result={
     *     "resultCode": "0",
     *     "message": "success",
     *     "data": {
     *         "jsonData": "eyJwb29sU3RhdHVzIjoyLCJzaWduIjoiNWNiY2QyYzM0NWVlZjY1MWFmNGVjNjE0MTBiMTk3NjEiLCJzdGltZSI6MTU5NzMxNTE4Mzk3Nywid2FpdCI6eyJ0b3RhbE51bSI6MjAsIndhaXROdW0iOjksIndhaXRUaW1lIjo0NX19"
     *     },
     *     "sgid": "202008131839430000001",
     *     "cgid": ""
     * }
     *
     */
    @RequestMapping(value = "/getPoolStatus", method = {RequestMethod.POST})
    public JsonResult<Object> getPoolStatus(HttpServletRequest request, HttpServletResponse response, @RequestBody RequestEncryptionJson requestData) throws Exception{
        String sgid = ComponentUtil.redisIdService.getNewId();
        String cgid = "";
        String ip = StringUtil.getIpAddress(request);
        String data = "";
        long did = 0;
        RequestPool requestModel = new RequestPool();
        try{
            // 解密
            data = StringUtil.decoderBase64(requestData.jsonData);
            requestModel  = JSON.parseObject(data, RequestPool.class);
            //#临时数据
//            if (!StringUtils.isBlank(requestModel.token)){
//                if (requestModel.token.equals("111111")){
//                    ComponentUtil.redisService.set(requestModel.token, "1");
//                }
//            }

            // check校验请求的数据
            did = HodgepodgeMethod.checkGetPoolStatusData(requestModel);


            // 查询策略里面的池子中每个人的消耗时间
            int stgTime = 0;
            StrategyModel strategyQuery = HodgepodgeMethod.assembleStrategyQuery(ServerConstant.StrategyEnum.POOL_CONSUME_TIME.getStgType());
            StrategyModel strategyModel = ComponentUtil.strategyService.getStrategyModel(strategyQuery, ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_ZERO);
            stgTime = strategyModel.getStgNumValue();

            int poolStatus = 0;
            // 查询用户当前抢单状态
            String createTime = "";
            PoolWaitModel poolWaitQuery = HodgepodgeMethod.assemblePoolWaitQuery(0, did, null);
            PoolWaitModel poolWaitModel = (PoolWaitModel) ComponentUtil.poolWaitService.findByObject(poolWaitQuery);
            if (poolWaitModel != null && poolWaitModel.getId() != null && poolWaitModel.getId() > 0){
                // 说明用户在等待池中
                poolStatus = 2;
                createTime = poolWaitModel.getCreateTime();
            }else {
                PoolOpenModel poolOpenQuery = HodgepodgeMethod.assemblePoolOpenQuery(0, did);
                PoolOpenModel poolOpenModel = (PoolOpenModel) ComponentUtil.poolOpenService.findByObject(poolOpenQuery);
                if (poolOpenModel != null && poolOpenModel.getId() != null && poolOpenModel.getId() > 0){
                    // 说明用户处于抢单进行中
                    poolStatus = 3;
                }else{
                    // 说明用户未排队
                    poolStatus = 1;
                }
            }

            int waitNum = 0;
            int totalNum = 0;
            int waitTime = 0;
            if (poolStatus == 1){
                totalNum = ComponentUtil.poolWaitService.queryByCount(new PoolWaitModel());
            }else if (poolStatus == 2){
                // 需要计算出当前排队的详情数据
                PoolWaitModel poolWaitCountQuery = HodgepodgeMethod.assemblePoolWaitQuery(0, 0, createTime);
                waitNum = ComponentUtil.poolWaitService.queryByCount(poolWaitCountQuery);
                totalNum = ComponentUtil.poolWaitService.queryByCount(new PoolWaitModel());
                if (waitNum > 0){
                    waitTime = waitNum * stgTime;
                }
            }




            // 组装返回客户端的数据
            long stime = System.currentTimeMillis();
            String sign = SignUtil.getSgin(stime, secretKeySign); // stime+秘钥=sign
            String strData = HodgepodgeMethod.assembleGetPoolStatusResult(stime, sign, poolStatus, waitNum, totalNum,waitTime,null);
            // 数据加密
            String encryptionData = StringUtil.mergeCodeBase64(strData);
            ResponseEncryptionJson resultDataModel = new ResponseEncryptionJson();
            resultDataModel.jsonData = encryptionData;
            // 返回数据给客户端
            return JsonResult.successResult(resultDataModel, cgid, sgid);
        }catch (Exception e){
            Map<String,String> map = ExceptionMethod.getException(e, ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_TWO);
            // #添加异常
            log.error(String.format("this PoolController.getPoolStatus() is error , the cgid=%s and sgid=%s and all data=%s!", cgid, sgid, data));
            if (!StringUtils.isBlank(map.get("dbCode"))){
                log.error(String.format("this PoolController.getPoolStatus() is error codeInfo, the dbCode=%s and dbMessage=%s !", map.get("dbCode"), map.get("dbMessage")));
            }
            e.printStackTrace();
            return JsonResult.failedResult(map.get("message"), map.get("code"), cgid, sgid);
        }
    }


    /**
     * @Description: 更新抢单池状态
     * <p>
     *     用户可更新状态：1取消抢单，2抢单
     * </p>
     * @param request
     * @param response
     * @return com.gd.chain.common.utils.JsonResult<java.lang.Object>
     * @author yoko
     * @date 2019/11/25 22:58
     * local:http://localhost:8086/fine/pool/updatePoolStatus
     * 请求的属性类:RequestPool
     * 必填字段:{"actionStatus":1,"agtVer":1,"clientVer":1,"clientType":1,"ctime":201911071802959,"cctime":201911071802959,"sign":"abcdefg","token":"111111"}
     * 加密字段:{"jsonData":"eyJhY3Rpb25TdGF0dXMiOjEsImFndFZlciI6MSwiY2xpZW50VmVyIjoxLCJjbGllbnRUeXBlIjoxLCJjdGltZSI6MjAxOTExMDcxODAyOTU5LCJjY3RpbWUiOjIwMTkxMTA3MTgwMjk1OSwic2lnbiI6ImFiY2RlZmciLCJ0b2tlbiI6IjExMTExMSJ9"}
     * 客户端加密字段:ctime+秘钥=sign
     * 返回加密字段:stime+秘钥=sign
     * result={
     *     "resultCode": "0",
     *     "message": "success",
     *     "data": {
     *         "jsonData": "eyJzaWduIjoiZWFlNjllNTQ3NTc2Yjg3NjI0YjE2NGRlYWVhZTM1N2IiLCJzdGltZSI6MTU5NzMyMjQzODU4M30="
     *     },
     *     "sgid": "202008132040380000001",
     *     "cgid": ""
     * }
     *
     */
    @RequestMapping(value = "/updatePoolStatus", method = {RequestMethod.POST})
    public JsonResult<Object> updatePoolStatus(HttpServletRequest request, HttpServletResponse response, @RequestBody RequestEncryptionJson requestData) throws Exception{
        String sgid = ComponentUtil.redisIdService.getNewId();
        String cgid = "";
        String ip = StringUtil.getIpAddress(request);
        String data = "";
        long did = 0;
        RequestPool requestModel = new RequestPool();
        try{
            // 解密
            data = StringUtil.decoderBase64(requestData.jsonData);
            requestModel  = JSON.parseObject(data, RequestPool.class);
            //#临时数据
//            if (!StringUtils.isBlank(requestModel.token)){
//                if (requestModel.token.equals("111111")){
//                    ComponentUtil.redisService.set(requestModel.token, "1");
//                }
//            }

            // check校验请求的数据
            did = HodgepodgeMethod.checkUpdatePoolStatusData(requestModel);
            int poolStatus = 0;
            if (requestModel.actionStatus == 1){
                // 开始抢单

                // 查询策略里面的池子开启抢单最低保底金额
                String minMoney = "";
                StrategyModel strategyQuery = HodgepodgeMethod.assembleStrategyQuery(ServerConstant.StrategyEnum.POOL_OPEN_MIN_MONEY.getStgType());
                StrategyModel strategyModel = ComponentUtil.strategyService.getStrategyModel(strategyQuery, ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_ZERO);
                minMoney = strategyModel.getStgValue();

                // 查询此用户是否已经存在在抢单等待中
                PoolWaitModel poolWaitQuery = HodgepodgeMethod.assemblePoolWaitQuery(0, did, null);
                PoolWaitModel poolWaitModel = (PoolWaitModel) ComponentUtil.poolWaitService.findByObject(poolWaitQuery);
                // check校验是否存在抢单等待中
                HodgepodgeMethod.checkPoolWait(poolWaitModel);

                // 查询此用户是否已经存在在抢单进行中
                PoolOpenModel poolOpenQuery = HodgepodgeMethod.assemblePoolOpenQuery(0, did);
                PoolOpenModel poolOpenModel = (PoolOpenModel) ComponentUtil.poolOpenService.findByObject(poolOpenQuery);
                HodgepodgeMethod.checkPoolOpen(poolOpenModel);

                // 查询用户信息
                DidModel didQuery = HodgepodgeMethod.assembleDidQueryByDid(did);
                DidModel didModel = (DidModel) ComponentUtil.didService.findByObject(didQuery);
                // 校验用户余额是否满足策略部署的余额的最低标准
                HodgepodgeMethod.checkDidMoney(didModel, minMoney);

                // 查询此用户的有效群
                DidCollectionAccountModel didCollectionAccountQuery = HodgepodgeMethod.assembleDidCollectionAccountListEffective(didModel.getId(), 3, 1, 3,1,2, 0, null);
                List<DidCollectionAccountModel> didCollectionAccountList = ComponentUtil.didCollectionAccountService.getEffectiveDidCollectionAccountByWxGroup(didCollectionAccountQuery);
                // 校验有效群
                HodgepodgeMethod.checkDidCollectionAccountListEffective(didCollectionAccountList);

                // 查询此用户下订单，已经发过红包，但是没有回复的订单信息
                OrderModel orderQuery = HodgepodgeMethod.assembleOrderByIsReply(did, 3, 1, 2,2);
                OrderModel orderModel = ComponentUtil.orderService.getOrderByNotIsReply(orderQuery);
                // 校验订单已发过红包，但是没有回复的订单信息
                HodgepodgeMethod.checkOrderByIsReply(orderModel);

                // 获取用户的微信收款账号金额监控超过范围的微信ID集合
                DidWxMonitorModel didWxMonitorQuery = HodgepodgeMethod.assembleDidWxMonitorByDidQuery(didModel.getId(), "1", null);
                List<String> toWxidList = ComponentUtil.didWxMonitorService.getToWxidList(didWxMonitorQuery);
                if (toWxidList != null && toWxidList.size() > 0){
                    List<DidWxMonitorModel> didWxMonitorList = new ArrayList<>();
                    for (String toWxid : toWxidList){
                        DidWxMonitorModel didWxMonitorByWxQuery = HodgepodgeMethod.assembleDidWxMonitorByDidQuery(didModel.getId(), "1", toWxid);
                        DidWxMonitorModel didWxMonitorModel = (DidWxMonitorModel)ComponentUtil.didWxMonitorService.findByObject(didWxMonitorByWxQuery);
                        if (didWxMonitorModel != null && didWxMonitorModel.getId() != null && didWxMonitorModel.getId() > 0){
                            didWxMonitorList.add(didWxMonitorModel);
                        }
                    }
                    // 排除微信集合的其它微信用户是否拥有有效群
                    DidCollectionAccountModel didCollectionAccountToWxQuery = HodgepodgeMethod.assembleDidCollectionAccountListEffective(didModel.getId(), 3, 1, 3,1,2, 0, toWxidList);
                    List<DidCollectionAccountModel> didCollectionAccountToWxList = ComponentUtil.didCollectionAccountService.getEffectiveDidCollectionAccountByWxGroup(didCollectionAccountToWxQuery);
                    // 校验排除微信集合的其它微信用户是否拥有有效群
                    HodgepodgeMethod.checkDidCollectionAccountListNotWxEffective(didCollectionAccountToWxList, didWxMonitorList);
                }

                // 添加数据到排队表中
                PoolWaitModel poolWaitAdd = HodgepodgeMethod.assemblePoolWaitAdd(did, 1);
                ComponentUtil.poolWaitService.add(poolWaitAdd);



            }else if (requestModel.actionStatus == 2){
                // 取消抢单
                PoolWaitModel poolWaitUpdate = HodgepodgeMethod.assemblePoolWaitUpdate(0, did, 1);
                ComponentUtil.poolWaitService.manyOperation(poolWaitUpdate);

                PoolOpenModel poolOpenUpdate = HodgepodgeMethod.assemblePoolOpenUpdate(0, did , 1);
                ComponentUtil.poolOpenService.manyOperation(poolOpenUpdate);
            }

            // 组装返回客户端的数据
            long stime = System.currentTimeMillis();
            String sign = SignUtil.getSgin(stime, secretKeySign); // stime+秘钥=sign
            String strData = HodgepodgeMethod.assembleResult(stime, null, sign);
            // 数据加密
            String encryptionData = StringUtil.mergeCodeBase64(strData);
            ResponseEncryptionJson resultDataModel = new ResponseEncryptionJson();
            resultDataModel.jsonData = encryptionData;
            // 返回数据给客户端
            return JsonResult.successResult(resultDataModel, cgid, sgid);
        }catch (Exception e){
            Map<String,String> map = ExceptionMethod.getException(e, ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_TWO);
            // #添加异常
            log.error(String.format("this PoolController.updatePoolStatus() is error , the cgid=%s and sgid=%s and all data=%s!", cgid, sgid, data));
            if (!StringUtils.isBlank(map.get("dbCode"))){
                log.error(String.format("this PoolController.updatePoolStatus() is error codeInfo, the dbCode=%s and dbMessage=%s !", map.get("dbCode"), map.get("dbMessage")));
            }
            e.printStackTrace();
            return JsonResult.failedResult(map.get("message"), map.get("code"), cgid, sgid);
        }
    }

}
