package com.hz.fine.master.core.controller.did;

import com.alibaba.fastjson.JSON;
import com.hz.fine.master.core.common.exception.ExceptionMethod;
import com.hz.fine.master.core.common.exception.ServiceException;
import com.hz.fine.master.core.common.utils.JsonResult;
import com.hz.fine.master.core.common.utils.SignUtil;
import com.hz.fine.master.core.common.utils.StringUtil;
import com.hz.fine.master.core.common.utils.constant.CacheKey;
import com.hz.fine.master.core.common.utils.constant.CachedKeyUtils;
import com.hz.fine.master.core.common.utils.constant.ErrorCode;
import com.hz.fine.master.core.common.utils.constant.ServerConstant;
import com.hz.fine.master.core.model.RequestEncryptionJson;
import com.hz.fine.master.core.model.ResponseEncryptionJson;
import com.hz.fine.master.core.model.bank.BankModel;
import com.hz.fine.master.core.model.did.DidRechargeModel;
import com.hz.fine.master.core.model.mobilecard.MobileCardModel;
import com.hz.fine.master.core.model.region.RegionModel;
import com.hz.fine.master.core.model.strategy.StrategyBankLimit;
import com.hz.fine.master.core.model.strategy.StrategyData;
import com.hz.fine.master.core.model.strategy.StrategyModel;
import com.hz.fine.master.core.protocol.request.did.recharge.RequestDidRecharge;
import com.hz.fine.master.core.protocol.response.did.recharge.ResponseDidRecharge;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Description 用户充值记录的Controller层
 * @Author yoko
 * @Date 2020/5/19 14:38
 * @Version 1.0
 */
@RestController
@RequestMapping("/fine/recharge")
public class DidRechargeController {

    private static Logger log = LoggerFactory.getLogger(DidRechargeController.class);

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

    /**
     * 2小时
     */
    public long TWO_HOUR = 2;

    /**
     * 8小时
     */
    public long EIGHT_HOUR = 8;

    @Value("${secret.key.token}")
    private String secretKeyToken;

    @Value("${secret.key.sign}")
    private String secretKeySign;



    /**
     * @Description: 用户发起充值
     * <p>
     *     用户发起充值的主要全流程：
     *     1.判断充值金额是否是策略里面部署的金额。
     *     2.筛选手机号正常的旗下的银行卡。
     *     3.筛选出银行卡是否在正常时间范围能够跑的卡（白天，晚上，全天）
     *     4.判断是否有特殊属性（优先级高的消费卡），如果有则先给优先级高的则先给出卡：给出具体机制判断日上限，月上限，总上限，日次数，月次数，总次数；然后判断金额是否有挂单的，没有挂单的则直接给出卡以及金额；
     *     如果所有金额都已挂单，则换第二张优先级高的消费卡来，依次操作；如果优先级高的卡没有满足的条件，则进行下面没有特殊属性的卡的逻辑操作。
     *     5.进行没有特殊优先级的卡操作如下：筛选出可以进行整数金额的卡（不用对金额进行加减充值金额的卡）；然后从可以进行整数金额的卡中随机获取一张银行卡给用户；
     *     （如果卡很充足的情况，可以先判断哪些卡没有给出金额，意思是卡身上没有挂着的订单，可以直接给出这些没有挂订单的卡，随机选一张）
     *     6.如果没有整数：说明所有卡身上目前都挂单了，随机一张卡，判断上限是否超过；然后以最小的金额数字1分钱开始，随机加1的金额还是减1分钱的金额，是否有挂单的存在，没有就给出去；
     *     如果加减在5分钱范围内都有挂单，则剔除这张卡，在重新随机筛选一张卡然后在进行同样操作。
     * </p>
     * @param request
     * @param response
     * @return com.gd.chain.common.utils.JsonResult<java.lang.Object>
     * @author yoko
     * @date 2019/11/25 22:58
     * local:http://localhost:8086/fine/recharge/add
     * 请求的属性类:RequestDidRecharge
     * 必填字段:{"orderMoney":"1000.00","agtVer":1,"clientVer":1,"clientType":1,"ctime":201911071802959,"cctime":201911071802959,"sign":"abcdefg","token":"111111"}
     * 加密字段:{"jsonData":"eyJvcmRlck1vbmV5IjoiMTAwMC4wMCIsImFndFZlciI6MSwiY2xpZW50VmVyIjoxLCJjbGllbnRUeXBlIjoxLCJjdGltZSI6MjAxOTExMDcxODAyOTU5LCJjY3RpbWUiOjIwMTkxMTA3MTgwMjk1OSwic2lnbiI6ImFiY2RlZmciLCJ0b2tlbiI6IjExMTExMSJ9"}
     * 客户端加密字段:ctime+cctime+秘钥=sign
     * 服务端加密字段:stime+秘钥=sign
     * result={
     *     "resultCode": "0",
     *     "message": "success",
     *     "data": {
     *         "jsonData": "eyJyZWNoYXJnZSI6eyJhY2NvdW50TmFtZSI6IuW8gOaIt+WQjV8yXzIiLCJiYW5rQ2FyZCI6IumTtuihjOWNoeWPt18yXzIiLCJiYW5rTmFtZSI6IuW9kuWxnuW8gOaIt+ihjF8yXzIiLCJkaXN0cmlidXRpb25Nb25leSI6IjEwMDAuMDAiLCJpbnZhbGlkVGltZSI6IjIwMjAtMDUtMjEgMTQ6NTY6NDgiLCJvcmRlck1vbmV5IjoiMTAwMC4wMCIsIm9yZGVyTm8iOiIyMDIwMDUyMTE0NDYwMDAwMDAwMDEiLCJzdWJicmFuY2hOYW1lIjoi5pSv6KGM5ZCN56ewXzJfMiJ9LCJzaWduIjoiYjA2MGFmODFlOTg0M2QwYTM3YzQwY2UzOWFlYzFmYTAiLCJzdGltZSI6MTU5MDA0MzYyMTE5Mn0="
     *     },
     *     "sgid": "202005211446000000001",
     *     "cgid": ""
     * }
     */
    @RequestMapping(value = "/add", method = {RequestMethod.POST})
    public JsonResult<Object> add(HttpServletRequest request, HttpServletResponse response, @RequestBody RequestEncryptionJson requestData) throws Exception{
        String sgid = ComponentUtil.redisIdService.getNewId();
        String cgid = "";
        String token = "";
        String ip = StringUtil.getIpAddress(request);
        String data = "";
        long did = 0;
        RegionModel regionModel = HodgepodgeMethod.assembleRegionModel(ip);

        RequestDidRecharge requestModel = new RequestDidRecharge();
        try{
            // 解密
            data = StringUtil.decoderBase64(requestData.jsonData);
            requestModel  = JSON.parseObject(data, RequestDidRecharge.class);

            //#临时数据
//            if (!StringUtils.isBlank(requestModel.token)){
//                if (requestModel.token.equals("111111")){
//                    ComponentUtil.redisService.set(requestModel.token, "1");
//                }
//            }
            // check校验数据
            did = HodgepodgeMethod.checkRechargeAdd(requestModel);
            String strData = "";


            // 判断是否还有未完成的订单
            strData = HodgepodgeMethod.checkDidOrderByRedis(did);
            if (StringUtils.isBlank(strData)){
                // 查询策略里面的金额列表
                StrategyModel strategyQuery = HodgepodgeMethod.assembleStrategyQuery(ServerConstant.StrategyEnum.MONEY.getStgType());
                StrategyModel strategyModel = ComponentUtil.strategyService.getStrategyModel(strategyQuery, ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_ZERO);
                HodgepodgeMethod.checkStrategyByMoney(strategyModel);

                // 解析金额列表的值
                List<StrategyData> strategyDataList = JSON.parseArray(strategyModel.getStgBigValue(), StrategyData.class);
                long moneyId = HodgepodgeMethod.checkRechargeMoney(strategyDataList, requestModel.orderMoney);

                // 查询正常使用的手机卡
                MobileCardModel mobileCardQuery = HodgepodgeMethod.assembleMobileCardQueryByUseStatus(ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_ONE);
                List<MobileCardModel> mobileCardList = ComponentUtil.mobileCardService.findByCondition(mobileCardQuery);
                HodgepodgeMethod.checkMobileCardDataIsNull(mobileCardList);

                // 策略数据：查询银行工作日期
                StrategyModel strategyBankWorkQuery = HodgepodgeMethod.assembleStrategyQuery(ServerConstant.StrategyEnum.BANK_WORK.getStgType());
                StrategyModel strategyBankWorkModel = ComponentUtil.strategyService.getStrategyModel(strategyBankWorkQuery, ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_ZERO);
                HodgepodgeMethod.checkStrategyByBankWork(strategyBankWorkModel);

                // 组装查询银行卡的查询条件
                BankModel bankQuery = HodgepodgeMethod.assembleBankQuery(mobileCardList, strategyBankWorkModel.getStgValue());
                List<BankModel> bankList = ComponentUtil.bankService.findByCondition(bankQuery);
                HodgepodgeMethod.checkBankListData(bankList);

                // 查询策略里面的银行流水日月总规则
                StrategyModel strategyBankListQuery = HodgepodgeMethod.assembleStrategyQuery(ServerConstant.StrategyEnum.BANK_LIMIT.getStgType());
                StrategyModel strategyBankListModel = ComponentUtil.strategyService.getStrategyModel(strategyBankListQuery, ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_ZERO);
                HodgepodgeMethod.checkStrategyByBankLimit(strategyBankListModel);
                // 解析银行卡流水日月总规则的值
                List<StrategyBankLimit> strategyBankLimitList = JSON.parseArray(strategyBankListModel.getStgBigValue(), StrategyBankLimit.class);

                // 查询策略里面的订单金额加减范围列表
                StrategyModel strategyMoneyAddSubtractListQuery = HodgepodgeMethod.assembleStrategyQuery(ServerConstant.StrategyEnum.MONEY_ADD_SUBTRACT_LIST.getStgType());
                StrategyModel strategyMoneyAddSubtractListModel = ComponentUtil.strategyService.getStrategyModel(strategyMoneyAddSubtractListQuery, ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_ZERO);
                HodgepodgeMethod.checkStrategyByMoneyAddSubtractList(strategyMoneyAddSubtractListModel);
                // 解析订单金额加减范围列表的值
                List<StrategyData> strategyMoneyAddSubtractList = JSON.parseArray(strategyMoneyAddSubtractListModel.getStgBigValue(), StrategyData.class);

                // 正式筛选出银行卡以及可用金额
                Map<String, Object> map = new HashMap<>();
                map = ComponentUtil.bankService.screenBank(bankList, strategyBankLimitList, strategyMoneyAddSubtractList, requestModel.orderMoney);
                HodgepodgeMethod.checkScreenBankData(map);

                // 查询策略里面的充值订单的失效时间
                StrategyModel strategyInvalidTimeQuery = HodgepodgeMethod.assembleStrategyQuery(ServerConstant.StrategyEnum.ORDER_INVALID_TIME.getStgType());
                StrategyModel strategyInvalidTimeModel = ComponentUtil.strategyService.getStrategyModel(strategyInvalidTimeQuery, ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_ZERO);
                HodgepodgeMethod.checkStrategyInvalidTime(strategyInvalidTimeModel);

                // 组装添加用处充值记录的最初数据
                DidRechargeModel didRechargeModel = HodgepodgeMethod.assembleDidRechargeAdd(map, did, sgid, moneyId, requestModel.orderMoney, strategyInvalidTimeModel.getStgNumValue());
                ComponentUtil.didRechargeService.add(didRechargeModel);
                // 组装返回客户端的数据
                long stime = System.currentTimeMillis();
                String sign = SignUtil.getSgin(stime, secretKeySign); // stime+秘钥=sign
                strData = HodgepodgeMethod.assembleDidRechargeAddDataResult(stime, sign, (BankModel) map.get("bankModel"), sgid, requestModel.orderMoney, didRechargeModel.getDistributionMoney(), didRechargeModel.getInvalidTime());

                // 记录订单信息的失效时间：用于check用户是否还有在有效期的订单未处理完毕
                String strKeyCache = CachedKeyUtils.getCacheKey(CacheKey.LOCK_DID_ORDER_INVALID_TIME, did);
                ComponentUtil.redisService.set(strKeyCache, strData, TWO_HOUR, TimeUnit.HOURS);
            }

            // 数据加密
            String encryptionData = StringUtil.mergeCodeBase64(strData);
            ResponseEncryptionJson resultDataModel = new ResponseEncryptionJson();
            resultDataModel.jsonData = encryptionData;

            // 返回数据给客户端
            return JsonResult.successResult(resultDataModel, cgid, sgid);
        }catch (Exception e){
            Map<String,String> map = ExceptionMethod.getException(e, ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_TWO);
            log.error(String.format("this DidRechargeController.add() is error , the cgid=%s and sgid=%s and all data=%s!", cgid, sgid, data));
            if (!StringUtils.isBlank(map.get("dbCode"))){
                log.error(String.format("this DidRechargeController.add() is error codeInfo, the dbCode=%s and dbMessage=%s !", map.get("dbCode"), map.get("dbMessage")));
            }
            e.printStackTrace();
            return JsonResult.failedResult("服务器繁忙,请稍后再试!", map.get("code"), cgid, sgid);
        }
    }



    /**
     * @Description: 用户正式购买
     * <p>
     *     用户正式购买的主要全流程：
     *     1.根据金额向上浮动百分之十，取这百分之十的随机数确定要充值的金额
     * </p>
     * @param request
     * @param response
     * @return com.gd.chain.common.utils.JsonResult<java.lang.Object>
     * @author yoko
     * @date 2019/11/25 22:58
     * local:http://localhost:8086/fine/recharge/buy
     * 请求的属性类:RequestDidRecharge
     * 必填字段:{"orderMoney":"1000.00","bankId":29,"agtVer":1,"clientVer":1,"clientType":1,"ctime":201911071802959,"cctime":201911071802959,"sign":"abcdefg","token":"111111"}
     * 加密字段:{"jsonData":"eyJvcmRlck1vbmV5IjoiMTAwMC4wMCIsImJhbmtJZCI6MjksImFndFZlciI6MSwiY2xpZW50VmVyIjoxLCJjbGllbnRUeXBlIjoxLCJjdGltZSI6MjAxOTExMDcxODAyOTU5LCJjY3RpbWUiOjIwMTkxMTA3MTgwMjk1OSwic2lnbiI6ImFiY2RlZmciLCJ0b2tlbiI6IjExMTExMSJ9"}
     * 客户端加密字段:ctime+cctime+秘钥=sign
     * 服务端加密字段:stime+秘钥=sign
     * result={
     *     "resultCode": "0",
     *     "message": "success",
     *     "data": {
     *         "jsonData": "eyJyZWNoYXJnZSI6eyJhY2NvdW50TmFtZSI6IuaLm+WVhumTtuihjDEiLCJiYW5rQ2FyZCI6IjQ1NzcyNjM2NjYyNzI2MzYzMjIxIiwiYmFua05hbWUiOiLmi5vllYbpk7booYwiLCJkZXBvc2l0VGltZSI6IiIsImRlcG9zaXRvciI6IiIsImRpc3RyaWJ1dGlvbk1vbmV5IjoiMTAxNC4wMCIsImludmFsaWRUaW1lIjoiMjAyMC0wNi0yOSAxOToxODoyOCIsImxhc3ROdW0iOiIiLCJvcmRlck1vbmV5IjoiMTAwMC4wMCIsIm9yZGVyTm8iOiIyMDIwMDYyOTE3MTgyMzAwMDAwMDEiLCJzdWJicmFuY2hOYW1lIjoieHh4eOmTtuihjOmSseaxn+aUr+ihjCJ9LCJzaWduIjoiNjk0MjU1MDE5OTUxZmVmM2ViYTU2ZjFlNWU3OWI3YWQiLCJzdGltZSI6MTU5MzQyMjMwODkyNn0="
     *     },
     *     "sgid": "202006291718230000001",
     *     "cgid": ""
     * }
     */
    @RequestMapping(value = "/buy", method = {RequestMethod.POST})
    public JsonResult<Object> buy(HttpServletRequest request, HttpServletResponse response, @RequestBody RequestEncryptionJson requestData) throws Exception{
        String sgid = ComponentUtil.redisIdService.getNewId();
        String cgid = "";
        String token = "";
        String ip = StringUtil.getIpAddress(request);
        String data = "";
        long did = 0;
        RegionModel regionModel = HodgepodgeMethod.assembleRegionModel(ip);

        RequestDidRecharge requestModel = new RequestDidRecharge();
        try{
            // 解密
            data = StringUtil.decoderBase64(requestData.jsonData);
            requestModel  = JSON.parseObject(data, RequestDidRecharge.class);

            //#临时数据
//            if (!StringUtils.isBlank(requestModel.token)){
//                if (requestModel.token.equals("111111")){
//                    ComponentUtil.redisService.set(requestModel.token, "1");
//                }
//            }
            // check校验数据
            did = HodgepodgeMethod.checkRechargeBuy(requestModel);
            String strData = "";

            // 判断是否还有未完成的订单
            strData = HodgepodgeMethod.checkDidOrderByRedis(did);
            if (StringUtils.isBlank(strData)){
                // 查询策略里面的金额列表
                StrategyModel strategyQuery = HodgepodgeMethod.assembleStrategyQuery(ServerConstant.StrategyEnum.MONEY.getStgType());
                StrategyModel strategyModel = ComponentUtil.strategyService.getStrategyModel(strategyQuery, ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_ZERO);
                HodgepodgeMethod.checkStrategyByMoney(strategyModel);

                // 解析金额列表的值
                List<StrategyData> strategyDataList = JSON.parseArray(strategyModel.getStgBigValue(), StrategyData.class);
                long moneyId = HodgepodgeMethod.checkRechargeMoney(strategyDataList, requestModel.orderMoney);

                // 获取银行卡信息
                BankModel bankQuery = HodgepodgeMethod.assembleBankById(requestModel.bankId);
                BankModel bankData = (BankModel) ComponentUtil.bankService.findByObject(bankQuery);
                HodgepodgeMethod.checkBank(bankData);
                // 正式筛选出银行卡以及可用金额
                String money = ComponentUtil.bankService.getMoney(bankData, requestModel.orderMoney);
                HodgepodgeMethod.checkScreenBankMoneyData(money);

                // 查询策略里面的充值订单的失效时间
                StrategyModel strategyInvalidTimeQuery = HodgepodgeMethod.assembleStrategyQuery(ServerConstant.StrategyEnum.ORDER_INVALID_TIME.getStgType());
                StrategyModel strategyInvalidTimeModel = ComponentUtil.strategyService.getStrategyModel(strategyInvalidTimeQuery, ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_ZERO);
                HodgepodgeMethod.checkStrategyInvalidTime(strategyInvalidTimeModel);

                // 组装添加用处充值记录的最初数据
                DidRechargeModel didRechargeModel = HodgepodgeMethod.assembleDidRechargeBuy(money, bankData.getId(), did, sgid, moneyId, requestModel.orderMoney, strategyInvalidTimeModel.getStgNumValue());
                ComponentUtil.didRechargeService.add(didRechargeModel);
                // 组装返回客户端的数据
                long stime = System.currentTimeMillis();
                String sign = SignUtil.getSgin(stime, secretKeySign); // stime+秘钥=sign
                strData = HodgepodgeMethod.assembleDidRechargeAddDataResult(stime, sign, bankData, sgid, requestModel.orderMoney, didRechargeModel.getDistributionMoney(), didRechargeModel.getInvalidTime());

                // 记录订单信息的失效时间：用于check用户是否还有在有效期的订单未处理完毕
                String strKeyCache = CachedKeyUtils.getCacheKey(CacheKey.LOCK_DID_ORDER_INVALID_TIME, did);
                ComponentUtil.redisService.set(strKeyCache, strData, TWO_HOUR, TimeUnit.HOURS);
            }

            // 数据加密
            String encryptionData = StringUtil.mergeCodeBase64(strData);
            ResponseEncryptionJson resultDataModel = new ResponseEncryptionJson();
            resultDataModel.jsonData = encryptionData;

            // 返回数据给客户端
            return JsonResult.successResult(resultDataModel, cgid, sgid);
        }catch (Exception e){
            Map<String,String> map = ExceptionMethod.getException(e, ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_TWO);
            log.error(String.format("this DidRechargeController.buy() is error , the cgid=%s and sgid=%s and all data=%s!", cgid, sgid, data));
            if (!StringUtils.isBlank(map.get("dbCode"))){
                log.error(String.format("this DidRechargeController.buy() is error codeInfo, the dbCode=%s and dbMessage=%s !", map.get("dbCode"), map.get("dbMessage")));
            }
            e.printStackTrace();
            return JsonResult.failedResult("服务器繁忙,请稍后再试!", map.get("code"), cgid, sgid);
        }
    }



    /**
     * @Description: 用户正式充值-拉起充值
     * <p>
     *     用户正式充值的主要全流程：
     *     1.根据order找出redis里面存的bankId（银行卡ID）
     *     2.check校验请求上来的充值金额是否是正常数字
     *     3.锁住银行卡以及此金额，查询数据库中是否有还未完成的充值订单（银行卡旗下相同金额的订单）
     * </p>
     * @param request
     * @param response
     * @return com.gd.chain.common.utils.JsonResult<java.lang.Object>
     * @author yoko
     * @date 2019/11/25 22:58
     * local:http://localhost:8086/fine/recharge/buyOrder
     * 请求的属性类:RequestDidRecharge
     * 必填字段:{"orderMoney":"1000.00","order":"111111","agtVer":1,"clientVer":1,"clientType":1,"ctime":201911071802959,"cctime":201911071802959,"sign":"abcdefg","token":"111111"}
     * 加密字段:{"jsonData":"eyJvcmRlck1vbmV5IjoiMTAwMC4wMCIsImJhbmtJZCI6MjksImFndFZlciI6MSwiY2xpZW50VmVyIjoxLCJjbGllbnRUeXBlIjoxLCJjdGltZSI6MjAxOTExMDcxODAyOTU5LCJjY3RpbWUiOjIwMTkxMTA3MTgwMjk1OSwic2lnbiI6ImFiY2RlZmciLCJ0b2tlbiI6IjExMTExMSJ9"}
     * 客户端加密字段:ctime+cctime+秘钥=sign
     * 服务端加密字段:stime+秘钥=sign
     * result={
     *     "resultCode": "0",
     *     "message": "success",
     *     "data": {
     *         "jsonData": "eyJyZWNoYXJnZSI6eyJhY2NvdW50TmFtZSI6IuaLm+WVhumTtuihjDEiLCJiYW5rQ2FyZCI6IjQ1NzcyNjM2NjYyNzI2MzYzMjIxIiwiYmFua05hbWUiOiLmi5vllYbpk7booYwiLCJkZXBvc2l0VGltZSI6IiIsImRlcG9zaXRvciI6IiIsImRpc3RyaWJ1dGlvbk1vbmV5IjoiMTAwMC4wMCIsImludmFsaWRUaW1lIjoiMjAyMC0wNy0wNCAyMjo1MToxNiIsImxhc3ROdW0iOiIiLCJvcmRlck1vbmV5IjoiMTAwMC4wMCIsIm9yZGVyTm8iOiIyMDIwMDcwNDE0NTExMTAwMDAwMDEiLCJzdWJicmFuY2hOYW1lIjoieHh4eOmTtuihjOmSseaxn+aUr+ihjCJ9LCJzaWduIjoiNzI0YjAxZTU3YzQ2YzgwMTFlOWEyYjk0YmI4MmM4NzQiLCJzdGltZSI6MTU5Mzg0NTQ3NjkzOX0="
     *     },
     *     "sgid": "202007041451110000001",
     *     "cgid": ""
     * }
     */
    @RequestMapping(value = "/buyOrder", method = {RequestMethod.POST})
    public JsonResult<Object> buyOrder(HttpServletRequest request, HttpServletResponse response, @RequestBody RequestEncryptionJson requestData) throws Exception{
        String sgid = ComponentUtil.redisIdService.getNewId();
        String cgid = "";
        String token = "";
        String ip = StringUtil.getIpAddress(request);
        String data = "";
        long did = 0;
        RegionModel regionModel = HodgepodgeMethod.assembleRegionModel(ip);
        RequestDidRecharge requestModel = new RequestDidRecharge();
        try{
            // 解密
            data = StringUtil.decoderBase64(requestData.jsonData);
            requestModel  = JSON.parseObject(data, RequestDidRecharge.class);

            //#临时数据
//            if (!StringUtils.isBlank(requestModel.token)){
//                if (requestModel.token.equals("111111")){
//                    ComponentUtil.redisService.set(requestModel.token, "1");
//                }
//            }
            //#临时数据
//            if (!StringUtils.isBlank(requestModel.order)){
//                if (requestModel.order.equals("111111")){
//                    String strKeyCache = CachedKeyUtils.getCacheKey(CacheKey.BANK_ID_BY_SGID, 1, requestModel.order);
//                    ComponentUtil.redisService.set(strKeyCache, "29");
//                }
//            }

            // check校验数据
            did = HodgepodgeMethod.checkRechargeBuyOrder(requestModel);
            if (requestModel.orderMoney.indexOf(".") <= -1){
                requestModel.orderMoney = requestModel.orderMoney + ".00";
            }
            String strData = "";

            // 判断是否还有未完成的订单
            strData = HodgepodgeMethod.checkDidOrderByRedis(did);

            // 通过redis获取缓存里面存储的银行卡的主键ID
            long bankId = HodgepodgeMethod.getBankIdByRedis(did, requestModel.order);
            if (StringUtils.isBlank(strData)){
                // 获取银行卡信息
                BankModel bankQuery = HodgepodgeMethod.assembleBankById(bankId);
                BankModel bankData = (BankModel) ComponentUtil.bankService.findByObject(bankQuery);
                HodgepodgeMethod.checkBank(bankData);

                // 先锁住
                String lockKey_orderMoney = CachedKeyUtils.getCacheKey(CacheKey.LOCK_BANK_ID_MONEY, bankId, requestModel.orderMoney);
                boolean flagLock_orderMoney = ComponentUtil.redisIdService.lock(lockKey_orderMoney);
                if (flagLock_orderMoney){
                    // 根据银行卡ID + 充值金额查询的查询条件查询充值订单是否有相同银行卡相同金额挂单
                    DidRechargeModel didRechargeQuery = HodgepodgeMethod.assembleDidRechargeQuery(bankId, requestModel.orderMoney);
                    DidRechargeModel didRechargeModel = (DidRechargeModel) ComponentUtil.didRechargeService.findByObject(didRechargeQuery);
                    HodgepodgeMethod.checkBankMoneyIsHave(didRechargeModel);

                    // 查询策略里面的充值订单的失效时间
                    StrategyModel strategyInvalidTimeQuery = HodgepodgeMethod.assembleStrategyQuery(ServerConstant.StrategyEnum.ORDER_INVALID_TIME.getStgType());
                    StrategyModel strategyInvalidTimeModel = ComponentUtil.strategyService.getStrategyModel(strategyInvalidTimeQuery, ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_ZERO);
                    HodgepodgeMethod.checkStrategyInvalidTime(strategyInvalidTimeModel);

                    // 组装添加用处充值记录的最初数据
                    DidRechargeModel didRechargeAdd = HodgepodgeMethod.assembleDidRechargeBuy(requestModel.orderMoney, bankData.getId(), did, sgid, 0, requestModel.orderMoney, strategyInvalidTimeModel.getStgNumValue());
                    ComponentUtil.didRechargeService.add(didRechargeAdd);
                    // 组装返回客户端的数据
                    long stime = System.currentTimeMillis();
                    String sign = SignUtil.getSgin(stime, secretKeySign); // stime+秘钥=sign
                    strData = HodgepodgeMethod.assembleDidRechargeAddDataResult(stime, sign, bankData, sgid, requestModel.orderMoney, didRechargeAdd.getDistributionMoney(), didRechargeAdd.getInvalidTime());

                    // 记录订单信息的失效时间：用于check用户是否还有在有效期的订单未处理完毕
                    String strKeyCache = CachedKeyUtils.getCacheKey(CacheKey.LOCK_DID_ORDER_INVALID_TIME, did);
                    ComponentUtil.redisService.set(strKeyCache, strData, strategyInvalidTimeModel.getStgNumValue(), TimeUnit.MINUTES);
                    // 解锁
                    ComponentUtil.redisIdService.delLock(lockKey_orderMoney);
                }else {
                    throw new ServiceException(ErrorCode.ENUM_ERROR.DR00029.geteCode(), ErrorCode.ENUM_ERROR.DR00029.geteDesc());
                }
            }else{
                long stime = System.currentTimeMillis();
                String sign = SignUtil.getSgin(stime, secretKeySign); // stime+秘钥=sign
                ResponseDidRecharge responseDidRecharge = JSON.parseObject(strData, ResponseDidRecharge.class);
                if (StringUtils.isBlank(responseDidRecharge.recharge.depositor)){
                    strData = HodgepodgeMethod.assembleDidRechargeHaveOrderDataResult(stime, sign, responseDidRecharge, ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_TWO);
                }else {
                    // 因为提交了存款人，则可以继续进行充值：如果用户没有充值，又乱填写存款人，排查数据会受到处罚；所以这里规定了提交了存款人信息，可以继续拉起充值订单

                    // 获取银行卡信息
                    BankModel bankQuery = HodgepodgeMethod.assembleBankById(bankId);
                    BankModel bankData = (BankModel) ComponentUtil.bankService.findByObject(bankQuery);
                    HodgepodgeMethod.checkBank(bankData);

                    // 先锁住
                    String lockKey_orderMoney = CachedKeyUtils.getCacheKey(CacheKey.LOCK_BANK_ID_MONEY, bankId, requestModel.orderMoney);
                    boolean flagLock_orderMoney = ComponentUtil.redisIdService.lock(lockKey_orderMoney);
                    if (flagLock_orderMoney){
                        // 根据银行卡ID + 充值金额查询的查询条件查询充值订单是否有相同银行卡相同金额挂单
                        DidRechargeModel didRechargeQuery = HodgepodgeMethod.assembleDidRechargeQuery(bankId, requestModel.orderMoney);
                        DidRechargeModel didRechargeModel = (DidRechargeModel) ComponentUtil.didRechargeService.findByObject(didRechargeQuery);
                        HodgepodgeMethod.checkBankMoneyIsHave(didRechargeModel);

                        // 查询策略里面的充值订单的失效时间
                        StrategyModel strategyInvalidTimeQuery = HodgepodgeMethod.assembleStrategyQuery(ServerConstant.StrategyEnum.ORDER_INVALID_TIME.getStgType());
                        StrategyModel strategyInvalidTimeModel = ComponentUtil.strategyService.getStrategyModel(strategyInvalidTimeQuery, ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_ZERO);
                        HodgepodgeMethod.checkStrategyInvalidTime(strategyInvalidTimeModel);

                        // 组装添加用处充值记录的最初数据
                        DidRechargeModel didRechargeAdd = HodgepodgeMethod.assembleDidRechargeBuy(requestModel.orderMoney, bankData.getId(), did, sgid, 0, requestModel.orderMoney, strategyInvalidTimeModel.getStgNumValue());
                        ComponentUtil.didRechargeService.add(didRechargeAdd);
                        // 组装返回客户端的数据
                        strData = HodgepodgeMethod.assembleDidRechargeAddDataResult(stime, sign, bankData, sgid, requestModel.orderMoney, didRechargeAdd.getDistributionMoney(), didRechargeAdd.getInvalidTime());

                        // 记录订单信息的失效时间：用于check用户是否还有在有效期的订单未处理完毕
                        String strKeyCache = CachedKeyUtils.getCacheKey(CacheKey.LOCK_DID_ORDER_INVALID_TIME, did);
                        ComponentUtil.redisService.set(strKeyCache, strData, strategyInvalidTimeModel.getStgNumValue(), TimeUnit.MINUTES);
                        // 解锁
                        ComponentUtil.redisIdService.delLock(lockKey_orderMoney);
                    }else {
                        throw new ServiceException(ErrorCode.ENUM_ERROR.DR00029.geteCode(), ErrorCode.ENUM_ERROR.DR00029.geteDesc());
                    }
                }
            }

            // 数据加密
            String encryptionData = StringUtil.mergeCodeBase64(strData);
            ResponseEncryptionJson resultDataModel = new ResponseEncryptionJson();
            resultDataModel.jsonData = encryptionData;

            // 返回数据给客户端
            return JsonResult.successResult(resultDataModel, cgid, sgid);
        }catch (Exception e){
            Map<String,String> map = ExceptionMethod.getException(e, ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_TWO);
            log.error(String.format("this DidRechargeController.buyOrder() is error , the cgid=%s and sgid=%s and all data=%s!", cgid, sgid, data));
            if (!StringUtils.isBlank(map.get("dbCode"))){
                log.error(String.format("this DidRechargeController.buyOrder() is error codeInfo, the dbCode=%s and dbMessage=%s !", map.get("dbCode"), map.get("dbMessage")));
            }
            e.printStackTrace();
            return JsonResult.failedResult(map.get("message"), map.get("code"), cgid, sgid);
        }
    }



    /**
     * @Description: 用户充值之后，更新充值存入账号的信息
     *
     * <p>
     *     更新：存款人，存款时间，存款账号的尾号
     * </p>
     * @param request
     * @param response
     * @return com.gd.chain.common.utils.JsonResult<java.lang.Object>
     * @author yoko
     * @date 2019/11/25 22:58
     * local:http://localhost:8086/fine/recharge/deposit
     * 请求的属性类:RequestDidRecharge
     * 必填字段:{"orderNo":"202007041451110000001","depositor":"存入人","depositMoney":"1000.00","depositTime":"存入时间","lastNum":"存入尾号","agtVer":1,"clientVer":1,"clientType":1,"ctime":201911071802959,"cctime":201911071802959,"sign":"abcdefg","token":"111111"}
     * 加密字段:{"jsonData":"eyJvcmRlck5vIjoiMjAyMDA2MjkxNzE4MjMwMDAwMDAxIiwiZGVwb3NpdG9yIjoi5a2Y5YWl5Lq6IiwiZGVwb3NpdFRpbWUiOiLlrZjlhaXml7bpl7QiLCJsYXN0TnVtIjoi5a2Y5YWl5bC+5Y+3IiwiYWd0VmVyIjoxLCJjbGllbnRWZXIiOjEsImNsaWVudFR5cGUiOjEsImN0aW1lIjoyMDE5MTEwNzE4MDI5NTksImNjdGltZSI6MjAxOTExMDcxODAyOTU5LCJzaWduIjoiYWJjZGVmZyIsInRva2VuIjoiMTExMTExIn0="}
     * 客户端加密字段:ctime+cctime+秘钥=sign
     * 服务端加密字段:stime+秘钥=sign
     * result={
     *     "resultCode": "0",
     *     "message": "success",
     *     "data": {
     *         "jsonData": "eyJzaWduIjoiMWM5ZTA3MjNkYmU5ZTM4NDQ3NWYyZTA4MDU1ZTEyMjQiLCJzdGltZSI6MTU4OTc5MTYyMzY1Nn0="
     *     },
     *     "sgid": "202005211446000000001",
     *     "cgid": ""
     * }
     */
    @RequestMapping(value = "/deposit", method = {RequestMethod.POST})
    public JsonResult<Object> deposit(HttpServletRequest request, HttpServletResponse response, @RequestBody RequestEncryptionJson requestData) throws Exception{
        String sgid = ComponentUtil.redisIdService.getNewId();
        String cgid = "";
        String token = "";
        String ip = StringUtil.getIpAddress(request);
        String data = "";
        long did = 0;
        RegionModel regionModel = HodgepodgeMethod.assembleRegionModel(ip);
        RequestDidRecharge requestModel = new RequestDidRecharge();
        try{
            // 解密
            data = StringUtil.decoderBase64(requestData.jsonData);
            requestModel  = JSON.parseObject(data, RequestDidRecharge.class);

            //#临时数据
//            if (!StringUtils.isBlank(requestModel.token)){
//                if (requestModel.token.equals("111111")){
//                    ComponentUtil.redisService.set(requestModel.token, "1");
//                }
//            }
            // check校验数据
            did = HodgepodgeMethod.checkDeposit(requestModel);

            DidRechargeModel didRechargeUpdate = HodgepodgeMethod.assembleDepositUpdate(requestModel, did);
            ComponentUtil.didRechargeService.updateDidRechargeByDeposit(didRechargeUpdate);

            // 更新之后，把redis缓存里面的数据也更新一下：更新存款人的一些信息
            String redis_data = HodgepodgeMethod.checkDidOrderByRedis(did);
            if (!StringUtils.isBlank(redis_data)){
                String resRedis = HodgepodgeMethod.assembleDidRechargeUpdateRedisByDeposit(redis_data, requestModel);
                // 记录订单信息的失效时间：用于check用户是否还有在有效期的订单未处理完毕
                String strKeyCache = CachedKeyUtils.getCacheKey(CacheKey.LOCK_DID_ORDER_INVALID_TIME, did);
                ComponentUtil.redisService.set(strKeyCache, resRedis, EIGHT_HOUR, TimeUnit.HOURS);
            }

            // 组装返回客户端的数据
            long stime = System.currentTimeMillis();
            String sign = SignUtil.getSgin(stime, secretKeySign); // stime+秘钥=sign
            String strData = HodgepodgeMethod.assembleResult(stime, token, sign);
            // 数据加密
            String encryptionData = StringUtil.mergeCodeBase64(strData);
            ResponseEncryptionJson resultDataModel = new ResponseEncryptionJson();
            resultDataModel.jsonData = encryptionData;

            // 返回数据给客户端
            return JsonResult.successResult(resultDataModel, cgid, sgid);
        }catch (Exception e){
            Map<String,String> map = ExceptionMethod.getException(e, ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_TWO);
            log.error(String.format("this DidRechargeController.deposit() is error , the cgid=%s and sgid=%s and all data=%s!", cgid, sgid, data));
            if (!StringUtils.isBlank(map.get("dbCode"))){
                log.error(String.format("this DidRechargeController.deposit() is error codeInfo, the dbCode=%s and dbMessage=%s !", map.get("dbCode"), map.get("dbMessage")));
            }
            e.printStackTrace();
            return JsonResult.failedResult(map.get("message"), map.get("code"), cgid, sgid);
        }


    }



    /**
     * @Description: 获取用户是否有充值挂单
     * <p>
     *     判断用户是否有充值挂单：如果没有填写存入人的一些信息，则把redis的订单进行返回用户。
     *     如果已填写存入人的一些信息，则无需把订单展现给用户
     * </p>
     * @param request
     * @param response
     * @return com.gd.chain.common.utils.JsonResult<java.lang.Object>
     * @author yoko
     * @date 2019/11/25 22:58
     * local:http://localhost:8086/fine/recharge/haveOrder
     * 请求的属性类:RequestDidRecharge
     * 必填字段:{"agtVer":1,"clientVer":1,"clientType":1,"ctime":201911071802959,"cctime":201911071802959,"sign":"abcdefg","token":"111111"}
     * 加密字段:{"jsonData":"eyJhZ3RWZXIiOjEsImNsaWVudFZlciI6MSwiY2xpZW50VHlwZSI6MSwiY3RpbWUiOjIwMTkxMTA3MTgwMjk1OSwiY2N0aW1lIjoyMDE5MTEwNzE4MDI5NTksInNpZ24iOiJhYmNkZWZnIiwidG9rZW4iOiIxMTExMTEifQ=="}
     * 客户端加密字段:ctime+cctime+秘钥=sign
     * 服务端加密字段:stime+秘钥=sign
     * result={
     *     "resultCode": "0",
     *     "message": "success",
     *     "data": {
     *         "jsonData": "eyJoYXZlVHlwZSI6MiwicmVjaGFyZ2UiOnsiYWNjb3VudE5hbWUiOiLmi5vllYbpk7booYwxIiwiYmFua0NhcmQiOiI0NTc3MjYzNjY2MjcyNjM2MzIyMSIsImJhbmtOYW1lIjoi5oub5ZWG6ZO26KGMIiwiZGVwb3NpdFRpbWUiOiIiLCJkZXBvc2l0b3IiOiIiLCJkaXN0cmlidXRpb25Nb25leSI6IjEwNzUuMDAiLCJpbnZhbGlkVGltZSI6IjIwMjAtMDctMDMgMjA6Mzk6NDciLCJsYXN0TnVtIjoiIiwib3JkZXJNb25leSI6IjEwMDAuMDAiLCJvcmRlck5vIjoiMjAyMDA3MDMxODM5MzYwMDAwMDAxIiwic3ViYnJhbmNoTmFtZSI6Inh4eHjpk7booYzpkrHmsZ/mlK/ooYwifSwic2lnbiI6ImE1YjhhMjEwMmI4MmNjMTUxMWU5YjAxZmVlZTMzMzA4Iiwic3RpbWUiOjE1OTM3NzI4NTk3NzF9"
     *     },
     *     "sgid": "202007031840590000001",
     *     "cgid": ""
     * }
     */
    @RequestMapping(value = "/haveOrder", method = {RequestMethod.POST})
    public JsonResult<Object> haveOrder(HttpServletRequest request, HttpServletResponse response, @RequestBody RequestEncryptionJson requestData) throws Exception{
        String sgid = ComponentUtil.redisIdService.getNewId();
        String cgid = "";
        String token = "";
        String ip = StringUtil.getIpAddress(request);
        String data = "";
        long did = 0;
        RegionModel regionModel = HodgepodgeMethod.assembleRegionModel(ip);

        RequestDidRecharge requestModel = new RequestDidRecharge();
        try{
            // 解密
            data = StringUtil.decoderBase64(requestData.jsonData);
            requestModel  = JSON.parseObject(data, RequestDidRecharge.class);

            //#临时数据
//            if (!StringUtils.isBlank(requestModel.token)){
//                if (requestModel.token.equals("111111")){
//                    ComponentUtil.redisService.set(requestModel.token, "1");
//                }
//            }
            // check校验数据
            did = HodgepodgeMethod.checkHaveOrderData(requestModel);
            String strData = "";
            // 判断是否还有未完成的订单
            strData = HodgepodgeMethod.checkDidOrderByRedis(did);
            // 组装返回客户端的数据
            long stime = System.currentTimeMillis();
            String sign = SignUtil.getSgin(stime, secretKeySign); // stime+秘钥=sign
            if (StringUtils.isBlank(strData)){
                strData = HodgepodgeMethod.assembleDidRechargeHaveOrderDataResult(stime, sign, null, ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_ONE);
            }else{
                ResponseDidRecharge responseDidRecharge = JSON.parseObject(strData, ResponseDidRecharge.class);
                if (StringUtils.isBlank(responseDidRecharge.recharge.depositor)){
                    strData = HodgepodgeMethod.assembleDidRechargeHaveOrderDataResult(stime, sign, responseDidRecharge, ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_TWO);
                }else {
                    strData = HodgepodgeMethod.assembleDidRechargeHaveOrderDataResult(stime, sign, null, ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_ONE);
                }
            }
            // 数据加密
            String encryptionData = StringUtil.mergeCodeBase64(strData);
            ResponseEncryptionJson resultDataModel = new ResponseEncryptionJson();
            resultDataModel.jsonData = encryptionData;

            // 返回数据给客户端
            return JsonResult.successResult(resultDataModel, cgid, sgid);
        }catch (Exception e){
            Map<String,String> map = ExceptionMethod.getException(e, ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_TWO);
            log.error(String.format("this DidRechargeController.haveOrder() is error , the cgid=%s and sgid=%s and all data=%s!", cgid, sgid, data));
            if (!StringUtils.isBlank(map.get("dbCode"))){
                log.error(String.format("this DidRechargeController.haveOrder() is error codeInfo, the dbCode=%s and dbMessage=%s !", map.get("dbCode"), map.get("dbMessage")));
            }
            e.printStackTrace();
            return JsonResult.failedResult(map.get("message"), map.get("code"), cgid, sgid);
        }
    }




    /**
     * @Description: 用户上传图片-申诉
     * <p>
     *     用户上传银行转账的截图纪录
     * </p>
     * @param request
     * @param response
     * @return com.gd.chain.common.utils.JsonResult<java.lang.Object>
     * @author yoko
     * @date 2019/11/25 22:58
     * local:http://localhost:8086/fine/recharge/appeal
     * 请求的属性类:RequestDidRecharge
     * 必填字段:{"orderNo":"202005211450200000001","pictureAds":"http://www.baidu.com","agtVer":1,"clientVer":1,"clientType":1,"ctime":201911071802959,"cctime":201911071802959,"sign":"abcdefg","token":"111111"}
     * 加密字段:{"jsonData":"eyJvcmRlck5vIjoiMjAyMDA1MjExNDUwMjAwMDAwMDAxIiwicGljdHVyZUFkcyI6Imh0dHA6Ly93d3cuYmFpZHUuY29tIiwiYWd0VmVyIjoxLCJjbGllbnRWZXIiOjEsImNsaWVudFR5cGUiOjEsImN0aW1lIjoyMDE5MTEwNzE4MDI5NTksImNjdGltZSI6MjAxOTExMDcxODAyOTU5LCJzaWduIjoiYWJjZGVmZyIsInRva2VuIjoiMTExMTExIn0="}
     * 客户端加密字段:ctime+cctime+秘钥=sign
     * 服务端加密字段:stime+秘钥=sign
     * result={
     *     "resultCode": "0",
     *     "message": "success",
     *     "data": {
     *         "jsonData": "eyJzaWduIjoiMWM5ZTA3MjNkYmU5ZTM4NDQ3NWYyZTA4MDU1ZTEyMjQiLCJzdGltZSI6MTU4OTc5MTYyMzY1Nn0="
     *     },
     *     "sgid": "202005211446000000001",
     *     "cgid": ""
     * }
     */
    @RequestMapping(value = "/appeal", method = {RequestMethod.POST})
    public JsonResult<Object> appeal(HttpServletRequest request, HttpServletResponse response, @RequestBody RequestEncryptionJson requestData) throws Exception{
        String sgid = ComponentUtil.redisIdService.getNewId();
        String cgid = "";
        String token = "";
        String ip = StringUtil.getIpAddress(request);
        String data = "";
        long did = 0;
        RegionModel regionModel = HodgepodgeMethod.assembleRegionModel(ip);

        RequestDidRecharge requestModel = new RequestDidRecharge();
        try{
            // 解密
            data = StringUtil.decoderBase64(requestData.jsonData);
            requestModel  = JSON.parseObject(data, RequestDidRecharge.class);

            //#临时数据
//            if (!StringUtils.isBlank(requestModel.token)){
//                if (requestModel.token.equals("111111")){
//                    ComponentUtil.redisService.set(requestModel.token, "1");
//                }
//            }
            // check校验数据
            did = HodgepodgeMethod.checkLoadPicture(requestModel);

            DidRechargeModel didRechargeUpdate = HodgepodgeMethod.assembleLoadPictureAndAppealUpdate(requestModel, did);
            ComponentUtil.didRechargeService.updateDidRechargeByAppeal(didRechargeUpdate);

            // 组装返回客户端的数据
            long stime = System.currentTimeMillis();
            String sign = SignUtil.getSgin(stime, secretKeySign); // stime+秘钥=sign
            String strData = HodgepodgeMethod.assembleResult(stime, token, sign);
            // 数据加密
            String encryptionData = StringUtil.mergeCodeBase64(strData);
            ResponseEncryptionJson resultDataModel = new ResponseEncryptionJson();
            resultDataModel.jsonData = encryptionData;

            // 返回数据给客户端
            return JsonResult.successResult(resultDataModel, cgid, sgid);
        }catch (Exception e){
            Map<String,String> map = ExceptionMethod.getException(e, ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_TWO);
            log.error(String.format("this DidRechargeController.loadPicture() is error , the cgid=%s and sgid=%s and all data=%s!", cgid, sgid, data));
            if (!StringUtils.isBlank(map.get("dbCode"))){
                log.error(String.format("this DidRechargeController.loadPicture() is error codeInfo, the dbCode=%s and dbMessage=%s !", map.get("dbCode"), map.get("dbMessage")));
            }
            e.printStackTrace();
            return JsonResult.failedResult(map.get("message"), map.get("code"), cgid, sgid);
        }


    }





    /**
     * @Description: 用户充值记录/用户充值订单-集合
     * @param request
     * @param response
     * @return com.gd.chain.common.utils.JsonResult<java.lang.Object>
     * @author yoko
     * @date 2019/11/25 22:58
     * local:http://localhost:8086/fine/recharge/getDataList
     * 请求的属性类:RequestDidRecharge
     * 必填字段:{"orderStatus":1,"curdayStart":20200521,"curdayEnd":20200521,"agtVer":1,"clientVer":1,"clientType":1,"ctime":201911071802959,"cctime":201911071802959,"sign":"abcdefg","pageNumber":1,"pageSize":3,"token":"111111"}
     * 加密字段:{"jsonData":"eyJvcmRlclN0YXR1cyI6MSwiY3VyZGF5U3RhcnQiOjIwMjAwNTIxLCJjdXJkYXlFbmQiOjIwMjAwNTIxLCJhZ3RWZXIiOjEsImNsaWVudFZlciI6MSwiY2xpZW50VHlwZSI6MSwiY3RpbWUiOjIwMTkxMTA3MTgwMjk1OSwiY2N0aW1lIjoyMDE5MTEwNzE4MDI5NTksInNpZ24iOiJhYmNkZWZnIiwicGFnZU51bWJlciI6MSwicGFnZVNpemUiOjMsInRva2VuIjoiMTExMTExIn0="}
     * 客户端加密字段:ctime+秘钥=sign
     * 返回加密字段:stime+秘钥=sign
     * result={
     *     "resultCode": "0",
     *     "message": "success",
     *     "data": {
     *         "jsonData": "eyJkYXRhTGlzdCI6W3siZGlzdHJpYnV0aW9uTW9uZXkiOiIxMDAwLjAwIiwiaW52YWxpZFRpbWUiOiIyMDIwLTA1LTIxIDE1OjUwOjIzIiwib3JkZXJNb25leSI6IjEwMDAuMDAiLCJvcmRlck5vIjoiMjAyMDA1MjExNTQwMTYwMDAwMDAxIiwib3JkZXJTdGF0dXMiOjEsInBpY3R1cmVBZHMiOiIifSx7ImRpc3RyaWJ1dGlvbk1vbmV5IjoiMTAwMC4wMCIsImludmFsaWRUaW1lIjoiMjAyMC0wNS0yMSAxNToxNDowNSIsIm9yZGVyTW9uZXkiOiIxMDAwLjAwIiwib3JkZXJObyI6IjIwMjAwNTIxMTUwMzU2MDAwMDAwMSIsIm9yZGVyU3RhdHVzIjoxLCJwaWN0dXJlQWRzIjoiIn0seyJkaXN0cmlidXRpb25Nb25leSI6Ijk5OS45NSIsImludmFsaWRUaW1lIjoiMjAyMC0wNS0yMSAxNToxMzoyNSIsIm9yZGVyTW9uZXkiOiIxMDAwLjAwIiwib3JkZXJObyI6IjIwMjAwNTIxMTUwMzIwMDAwMDAwMSIsIm9yZGVyU3RhdHVzIjoxLCJwaWN0dXJlQWRzIjoiIn1dLCJzaWduIjoiYjdjNjNjMmZmYjM0OTI2YmIwYWE4ZTI2ZWU1ZWMwYTMiLCJzdGltZSI6MTU5MDA0OTU3OTg4MX0="
     *     },
     *     "sgid": "202005211626180000001",
     *     "cgid": ""
     * }
     */
    @RequestMapping(value = "/getDataList", method = {RequestMethod.POST})
    public JsonResult<Object> getDataList(HttpServletRequest request, HttpServletResponse response, @RequestBody RequestEncryptionJson requestData) throws Exception{
        String sgid = ComponentUtil.redisIdService.getNewId();
        String cgid = "";
        String ip = StringUtil.getIpAddress(request);
        String data = "";
        long did = 0;

        RequestDidRecharge requestModel = new RequestDidRecharge();
        try{
            // 解密
            data = StringUtil.decoderBase64(requestData.jsonData);
            requestModel  = JSON.parseObject(data, RequestDidRecharge.class);
            //#临时数据
//            if (!StringUtils.isBlank(requestModel.token)){
//                if (requestModel.token.equals("111111")){
//                    ComponentUtil.redisService.set(requestModel.token, "1");
//                }
//            }
            // check校验数据
            did = HodgepodgeMethod.checkDidRechargeListData(requestModel);

            // 获取用户充值订单记录集合数据
            DidRechargeModel didRechargeModelQuery = HodgepodgeMethod.assembleDidRechargeListByDid(requestModel, did);
            List<DidRechargeModel> didRechargeList = ComponentUtil.didRechargeService.queryByList(didRechargeModelQuery);
            // 组装返回客户端的数据
            long stime = System.currentTimeMillis();
            String sign = SignUtil.getSgin(stime, secretKeySign); // stime+秘钥=sign
            String strData = HodgepodgeMethod.assembleDidRechargeModelListResult(stime, sign, didRechargeList, didRechargeModelQuery.getRowCount());
            // 数据加密
            String encryptionData = StringUtil.mergeCodeBase64(strData);
            ResponseEncryptionJson resultDataModel = new ResponseEncryptionJson();
            resultDataModel.jsonData = encryptionData;
            // 返回数据给客户端
            return JsonResult.successResult(resultDataModel, cgid, sgid);
        }catch (Exception e){
            Map<String,String> map = ExceptionMethod.getException(e, ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_TWO);
            // #添加异常
            log.error(String.format("this DidRechargeController.getDataList() is error , the cgid=%s and sgid=%s and all data=%s!", cgid, sgid, data));
            if (!StringUtils.isBlank(map.get("dbCode"))){
                log.error(String.format("this DidRechargeController.getDataList() is error codeInfo, the dbCode=%s and dbMessage=%s !", map.get("dbCode"), map.get("dbMessage")));
            }
            e.printStackTrace();
            return JsonResult.failedResult(map.get("message"), map.get("code"), cgid, sgid);
        }
    }




    /**
     * @Description: 用户充值记录/用户充值订单-详情
     * @param request
     * @param response
     * @return com.gd.chain.common.utils.JsonResult<java.lang.Object>
     * @author yoko
     * @date 2019/11/25 22:58
     * local:http://localhost:8086/fine/recharge/getData
     * 请求的属性类:RequestDidRecharge
     * 必填字段:{"orderNo":"202005211450200000001","agtVer":1,"clientVer":1,"clientType":1,"ctime":201911071802959,"cctime":201911071802959,"sign":"abcdefg","token":"111111"}
     * 加密字段:{"jsonData":"eyJvcmRlck5vIjoiMjAyMDA1MjExNDUwMjAwMDAwMDAxIiwiYWd0VmVyIjoxLCJjbGllbnRWZXIiOjEsImNsaWVudFR5cGUiOjEsImN0aW1lIjoyMDE5MTEwNzE4MDI5NTksImNjdGltZSI6MjAxOTExMDcxODAyOTU5LCJzaWduIjoiYWJjZGVmZyIsInRva2VuIjoiMTExMTExIn0="}
     * 客户端加密字段:id+ctime+cctime+秘钥=sign
     * 服务端加密字段:stime+秘钥=sign
     * result={
     *     "resultCode": "0",
     *     "message": "success",
     *     "data": {
     *         "jsonData": "eyJkYXRhTW9kZWwiOnsiZGlzdHJpYnV0aW9uTW9uZXkiOiIxMDAwLjAwIiwiaW52YWxpZFRpbWUiOiIyMDIwLTA1LTIxIDE1OjAwOjIxIiwib3JkZXJNb25leSI6IjEwMDAuMDAiLCJvcmRlck5vIjoiMjAyMDA1MjExNDUwMjAwMDAwMDAxIiwib3JkZXJTdGF0dXMiOjEsInBpY3R1cmVBZHMiOiJodHRwOi8vd3d3LmJhaWR1LmNvbSJ9LCJzaWduIjoiNGFmZjliY2ViYmRjZDcyNGUwMDU3OGQxNzQxODdjNzgiLCJzdGltZSI6MTU5MDA1MDUzMTE4N30="
     *     },
     *     "sgid": "202005211642090000001",
     *     "cgid": ""
     * }
     */
    @RequestMapping(value = "/getData", method = {RequestMethod.POST})
    public JsonResult<Object> getData(HttpServletRequest request, HttpServletResponse response, @RequestBody RequestEncryptionJson requestData) throws Exception{
        String sgid = ComponentUtil.redisIdService.getNewId();
        String cgid = "";
        String token;
        String ip = StringUtil.getIpAddress(request);
        String data = "";
        long did = 0;

        RequestDidRecharge requestModel = new RequestDidRecharge();
        try{
            // 解密
            data = StringUtil.decoderBase64(requestData.jsonData);
            requestModel  = JSON.parseObject(data, RequestDidRecharge.class);
            //#临时数据
//            if (!StringUtils.isBlank(requestModel.token)){
//                if (requestModel.token.equals("111111")){
//                    ComponentUtil.redisService.set(requestModel.token, "1");
//                }
//            }
            // check校验请求的数据
            did = HodgepodgeMethod.checkDidRechargeData(requestModel);

            // 用户充值订单记录详情数据
            DidRechargeModel didRechargeModelQuery = HodgepodgeMethod.assembleDidRechargeByDidAndOrderNo(did, requestModel.orderNo);
            DidRechargeModel didRechargeModelData = (DidRechargeModel) ComponentUtil.didRechargeService.findByObject(didRechargeModelQuery);
            // 组装返回客户端的数据
            long stime = System.currentTimeMillis();
            String sign = SignUtil.getSgin(stime, secretKeySign); // stime+秘钥=sign
            String strData = HodgepodgeMethod.assembleDidRechargeDataResult(stime, sign, didRechargeModelData);
            // 数据加密
            String encryptionData = StringUtil.mergeCodeBase64(strData);
            ResponseEncryptionJson resultDataModel = new ResponseEncryptionJson();
            resultDataModel.jsonData = encryptionData;
            // 返回数据给客户端
            return JsonResult.successResult(resultDataModel, cgid, sgid);
        }catch (Exception e){
            Map<String,String> map = ExceptionMethod.getException(e, ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_TWO);
            // 添加异常
            log.error(String.format("this DidRechargeController.getData() is error , the cgid=%s and sgid=%s and all data=%s!", cgid, sgid, data));
            if (!StringUtils.isBlank(map.get("dbCode"))){
                log.error(String.format("this DidRechargeController.getData() is error codeInfo, the dbCode=%s and dbMessage=%s !", map.get("dbCode"), map.get("dbMessage")));
            }
            e.printStackTrace();
            return JsonResult.failedResult(map.get("message"), map.get("code"), cgid, sgid);
        }
    }



}
