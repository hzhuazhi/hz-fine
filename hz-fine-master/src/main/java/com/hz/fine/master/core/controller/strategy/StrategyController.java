package com.hz.fine.master.core.controller.strategy;

import com.alibaba.fastjson.JSON;
import com.hz.fine.master.core.common.exception.ExceptionMethod;
import com.hz.fine.master.core.common.utils.JsonResult;
import com.hz.fine.master.core.common.utils.SignUtil;
import com.hz.fine.master.core.common.utils.StringUtil;
import com.hz.fine.master.core.common.utils.constant.ServerConstant;
import com.hz.fine.master.core.model.RequestEncryptionJson;
import com.hz.fine.master.core.model.ResponseEncryptionJson;
import com.hz.fine.master.core.model.did.DidModel;
import com.hz.fine.master.core.model.did.DidRewardModel;
import com.hz.fine.master.core.model.strategy.StrategyData;
import com.hz.fine.master.core.model.strategy.StrategyModel;
import com.hz.fine.master.core.protocol.request.did.reward.RequestReward;
import com.hz.fine.master.core.protocol.request.strategy.RequestStrategy;
import com.hz.fine.master.util.ComponentUtil;
import com.hz.fine.master.util.HodgepodgeMethod;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
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
import java.util.List;
import java.util.Map;

/**
 * @Description 策略表：关于一些策略配置的部署的Controller层
 * @Author yoko
 * @Date 2020/5/19 14:05
 * @Version 1.0
 */
@RestController
@RequestMapping("/fine/stg")
public class StrategyController {

    private static Logger log = LoggerFactory.getLogger(StrategyController.class);

    /**
     * 5分钟.
     */
    public long FIVE_MIN = 300;

    /**
     * 15分钟.
     */
    public long FIFTEEN_MIN = 900;

    /**
     * 30分钟.
     */
    public long THIRTY_MIN = 30;

    @Value("${secret.key.token}")
    private String secretKeyToken;

    @Value("${secret.key.sign}")
    private String secretKeySign;





    /**
     * @Description: 策略：充值金额列表
     * @param request
     * @param response
     * @return com.gd.chain.common.utils.JsonResult<java.lang.Object>
     * @author yoko
     * @date 2019/11/25 22:58
     * local:http://localhost:8086/fine/stg/moneyList
     * 请求的属性类:RequestReward
     * 必填字段:{"agtVer":1,"clientVer":1,"clientType":1,"ctime":201911071802959,"cctime":201911071802959,"sign":"abcdefg","token":"111111"}
     * 加密字段:{"jsonData":"eyJhZ3RWZXIiOjEsImNsaWVudFZlciI6MSwiY2xpZW50VHlwZSI6MSwiY3RpbWUiOjIwMTkxMTA3MTgwMjk1OSwiY2N0aW1lIjoyMDE5MTEwNzE4MDI5NTksInNpZ24iOiJhYmNkZWZnIiwicGFnZU51bWJlciI6MSwicGFnZVNpemUiOjMsInRva2VuIjoiMTExMTExIn0="}
     * 客户端加密字段:ctime+秘钥=sign
     * 返回加密字段:stime+秘钥=sign
     * result={
     *     "resultCode": "0",
     *     "message": "success",
     *     "data": {
     *         "jsonData": "eyJtb25leUxpc3QiOlt7Im1vbmV5IjoiMTAwMC4wMCIsInJld2FyZFJhdGlvIjoiMC4wMSJ9LHsibW9uZXkiOiIyMDAwLjAwIiwicmV3YXJkUmF0aW8iOiIwLjAyIn0seyJtb25leSI6IjMwMDAuMDAiLCJyZXdhcmRSYXRpbyI6IjAuMDMifSx7Im1vbmV5IjoiNDAwMC4wMCIsInJld2FyZFJhdGlvIjoiMC4wNCJ9LHsibW9uZXkiOiI1MDAwLjAwIiwicmV3YXJkUmF0aW8iOiIwLjA1In1dLCJyb3dDb3VudCI6NSwic2lnbiI6IjQyNTNkZGU0ODllNmJlNTM0MWQzZDU3ZDVmMmE1MWJkIiwic3RpbWUiOjE1OTA1NzgwODE1NTl9"
     *     },
     *     "sgid": "202005271914390000001",
     *     "cgid": ""
     * }
     */
    @RequestMapping(value = "/moneyList", method = {RequestMethod.POST})
    public JsonResult<Object> moneyList(HttpServletRequest request, HttpServletResponse response, @RequestBody RequestEncryptionJson requestData) throws Exception{
        String sgid = ComponentUtil.redisIdService.getNewId();
        String cgid = "";
        String ip = StringUtil.getIpAddress(request);
        String data = "";
        long did = 0;

        RequestStrategy requestModel = new RequestStrategy();
        try{
            // 解密
            data = StringUtil.decoderBase64(requestData.jsonData);
            requestModel  = JSON.parseObject(data, RequestStrategy.class);
            // check校验数据
            HodgepodgeMethod.checkStrategyMoneyListData(requestModel);

            // 查询策略里面的金额列表
            StrategyModel strategyQuery = HodgepodgeMethod.assembleStrategyQuery(ServerConstant.StrategyEnum.MONEY.getStgType());
            StrategyModel strategyModel = ComponentUtil.strategyService.getStrategyModel(strategyQuery, ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_ZERO);
            HodgepodgeMethod.checkStrategyByMoney(strategyModel);

            // 解析金额列表的值
            List<StrategyData> strategyDataList = JSON.parseArray(strategyModel.getStgBigValue(), StrategyData.class);

            // 组装返回客户端的数据
            long stime = System.currentTimeMillis();
            String sign = SignUtil.getSgin(stime, secretKeySign); // stime+秘钥=sign
            String strData = HodgepodgeMethod.assembleStrategyMoneyListResult(stime, sign, strategyDataList, strategyDataList.size());
            // 数据加密
            String encryptionData = StringUtil.mergeCodeBase64(strData);
            ResponseEncryptionJson resultDataModel = new ResponseEncryptionJson();
            resultDataModel.jsonData = encryptionData;
            // 返回数据给客户端
            return JsonResult.successResult(resultDataModel, cgid, sgid);
        }catch (Exception e){
            Map<String,String> map = ExceptionMethod.getException(e, ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_TWO);
            // #添加异常
            log.error(String.format("this StrategyController.moneyList() is error , the cgid=%s and sgid=%s and all data=%s!", cgid, sgid, data));
            e.printStackTrace();
            return JsonResult.failedResult(map.get("message"), map.get("code"), cgid, sgid);
        }
    }



    /**
     * @Description: 策略：总金额充值档次奖励列表
     * @param request
     * @param response
     * @return com.gd.chain.common.utils.JsonResult<java.lang.Object>
     * @author yoko
     * @date 2019/11/25 22:58
     * local:http://localhost:8086/fine/stg/moneyGradeList
     * 请求的属性类:RequestReward
     * 必填字段:{"agtVer":1,"clientVer":1,"clientType":1,"ctime":201911071802959,"cctime":201911071802959,"sign":"abcdefg","token":"111111"}
     * 加密字段:{"jsonData":"eyJhZ3RWZXIiOjEsImNsaWVudFZlciI6MSwiY2xpZW50VHlwZSI6MSwiY3RpbWUiOjIwMTkxMTA3MTgwMjk1OSwiY2N0aW1lIjoyMDE5MTEwNzE4MDI5NTksInNpZ24iOiJhYmNkZWZnIiwidG9rZW4iOiIxMTExMTEifQ=="}
     * 客户端加密字段:ctime+秘钥=sign
     * 返回加密字段:stime+秘钥=sign
     * result={
     *     "resultCode": "0",
     *     "message": "success",
     *     "data": {
     *         "jsonData": "eyJtb25leUdyYWRlTGlzdCI6W3sibW9uZXlHcmFkZSI6IjEwMDAuMDAiLCJyZXdhcmRSYXRpbyI6IjAuMDEifSx7Im1vbmV5R3JhZGUiOiIyMDAwLjAwIiwicmV3YXJkUmF0aW8iOiIwLjAyIn0seyJtb25leUdyYWRlIjoiMzAwMC4wMCIsInJld2FyZFJhdGlvIjoiMC4wMyJ9LHsibW9uZXlHcmFkZSI6IjQwMDAuMDAiLCJyZXdhcmRSYXRpbyI6IjAuMDQifSx7Im1vbmV5R3JhZGUiOiI1MDAwLjAwIiwicmV3YXJkUmF0aW8iOiIwLjA1In1dLCJyb3dDb3VudCI6NSwic2lnbiI6ImYwZGQzZjBlMDlhNjllNjQ1MmIzZjZmY2QwMzY4MWI1Iiwic3RpbWUiOjE1OTA1NzkzNjQxOTd9"
     *     },
     *     "sgid": "202005271935550000001",
     *     "cgid": ""
     * }
     */
    @RequestMapping(value = "/moneyGradeList", method = {RequestMethod.POST})
    public JsonResult<Object> moneyGradeList(HttpServletRequest request, HttpServletResponse response, @RequestBody RequestEncryptionJson requestData) throws Exception{
        String sgid = ComponentUtil.redisIdService.getNewId();
        String cgid = "";
        String ip = StringUtil.getIpAddress(request);
        String data = "";
        long did = 0;

        RequestStrategy requestModel = new RequestStrategy();
        try{
            // 解密
            data = StringUtil.decoderBase64(requestData.jsonData);
            requestModel  = JSON.parseObject(data, RequestStrategy.class);
            // check校验数据
            HodgepodgeMethod.checkStrategyMoneyGradeListData(requestModel);

            // 查询策略里面的总金额充值档次奖励列表
            StrategyModel strategyQuery = HodgepodgeMethod.assembleStrategyQuery(ServerConstant.StrategyEnum.MONEY_GRADE_LIST.getStgType());
            StrategyModel strategyModel = ComponentUtil.strategyService.getStrategyModel(strategyQuery, ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_ZERO);
            HodgepodgeMethod.checkStrategyByMoneyGrade(strategyModel);

            // 解析金额列表的值
            List<StrategyData> strategyDataList = JSON.parseArray(strategyModel.getStgBigValue(), StrategyData.class);

            // 组装返回客户端的数据
            long stime = System.currentTimeMillis();
            String sign = SignUtil.getSgin(stime, secretKeySign); // stime+秘钥=sign
            String strData = HodgepodgeMethod.assembleStrategyMoneyGradeListResult(stime, sign, strategyDataList, strategyDataList.size());
            // 数据加密
            String encryptionData = StringUtil.mergeCodeBase64(strData);
            ResponseEncryptionJson resultDataModel = new ResponseEncryptionJson();
            resultDataModel.jsonData = encryptionData;
            // 返回数据给客户端
            return JsonResult.successResult(resultDataModel, cgid, sgid);
        }catch (Exception e){
            Map<String,String> map = ExceptionMethod.getException(e, ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_TWO);
            // #添加异常
            log.error(String.format("this StrategyController.moneyGradeList() is error , the cgid=%s and sgid=%s and all data=%s!", cgid, sgid, data));
            e.printStackTrace();
            return JsonResult.failedResult(map.get("message"), map.get("code"), cgid, sgid);
        }
    }



    /**
     * @Description: 策略：总金额充值档次奖励列表
     * @param request
     * @param response
     * @return com.gd.chain.common.utils.JsonResult<java.lang.Object>
     * @author yoko
     * @date 2019/11/25 22:58
     * local:http://localhost:8086/fine/stg/getToken
     * 请求的属性类:RequestReward
     * 必填字段:{"agtVer":1,"clientVer":1,"clientType":1,"ctime":201911071802959,"cctime":201911071802959,"sign":"abcdefg","token":"111111"}
     * 加密字段:{"jsonData":"eyJhZ3RWZXIiOjEsImNsaWVudFZlciI6MSwiY2xpZW50VHlwZSI6MSwiY3RpbWUiOjIwMTkxMTA3MTgwMjk1OSwiY2N0aW1lIjoyMDE5MTEwNzE4MDI5NTksInNpZ24iOiJhYmNkZWZnIiwidG9rZW4iOiIxMTExMTEifQ=="}
     * 客户端加密字段:ctime+秘钥=sign
     * 返回加密字段:stime+秘钥=sign
     * result={
     *     "resultCode": "0",
     *     "message": "success",
     *     "data": {
     *         "jsonData": "eyJxaU5pdSI6eyJrZXkiOiJkYmY3Mjk2ZTJiOTE0M2ZmYWUwOWI3OWJlMTRhNGQwYyIsInRva2VuIjoiV3E0Y0U2YXNQS3Y3dVB0ZmtZb3FWVGFJUmEybFV6Ym5KMWpQLWhIdDpxaXZfM0JtcjgxZjJCVUY3S242cVJ1ZjJuSTA9OmV5SnpZMjl3WlNJNkluRjVlQ0lzSW1SbFlXUnNhVzVsSWpveE5Ua3dOVGd4T0RrNGZRPT0ifSwic2lnbiI6IjM1Y2FmYjQyNzdkMjk1NGQ0YmZhNTNkNzRiY2E2ZDk4Iiwic3RpbWUiOjE1OTA1ODEyOTk1NTJ9"
     *     },
     *     "sgid": "202005272008170000001",
     *     "cgid": ""
     * }
     */
    @RequestMapping(value = "/getToken", method = {RequestMethod.POST})
    public JsonResult<Object> getToken(HttpServletRequest request, HttpServletResponse response, @RequestBody RequestEncryptionJson requestData) throws Exception{
        String sgid = ComponentUtil.redisIdService.getNewId();
        String cgid = "";
        String ip = StringUtil.getIpAddress(request);
        String data = "";
        long did = 0;

        RequestStrategy requestModel = new RequestStrategy();
        try{
            // 解密
            data = StringUtil.decoderBase64(requestData.jsonData);
            requestModel  = JSON.parseObject(data, RequestStrategy.class);

            // check校验数据
            HodgepodgeMethod.checkStrategyQiNiuTokenData(requestModel);
            String accessKey = "Wq4cE6asPKv7uPtfkYoqVTaIRa2lUzbnJ1jP-hHt";
            String secretKey = "H6q-QO36ryrmThHqN_W6o0FUjSHc5yio3VRXv1wR";
            String bucket = "qyx";
            long expireSeconds = 600;   //过期时间
            StringMap putPolicy = new StringMap();
            Auth auth = Auth.create(accessKey, secretKey);
            String upToken = auth.uploadToken(bucket,null, expireSeconds,putPolicy);
            // 组装返回客户端的数据
            long stime = System.currentTimeMillis();
            String sign = SignUtil.getSgin(stime, secretKeySign); // stime+秘钥=sign
            String strData = HodgepodgeMethod.assembleStrategyQiNiuResult(stime, sign, upToken);
            // 数据加密
            String encryptionData = StringUtil.mergeCodeBase64(strData);
            ResponseEncryptionJson resultDataModel = new ResponseEncryptionJson();
            resultDataModel.jsonData = encryptionData;
            // 返回数据给客户端
            return JsonResult.successResult(resultDataModel, cgid, sgid);
        }catch (Exception e){
            Map<String,String> map = ExceptionMethod.getException(e, ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_TWO);
            // #添加异常
            log.error(String.format("this StrategyController.getToken() is error , the cgid=%s and sgid=%s and all data=%s!", cgid, sgid, data));
            e.printStackTrace();
            return JsonResult.failedResult(map.get("message"), map.get("code"), cgid, sgid);
        }
    }



    /**
     * @Description: 分享邀请
     * @param request
     * @param response
     * @return com.gd.chain.common.utils.JsonResult<java.lang.Object>
     * @author yoko
     * @date 2019/11/25 22:58
     * local:http://localhost:8086/fine/stg/share
     * 请求的属性类:RequestAppeal
     * 必填字段:{"agtVer":1,"clientVer":1,"clientType":1,"ctime":201911071802959,"cctime":201911071802959,"sign":"abcdefg","token":"111111"}
     * 加密字段:{"jsonData":"eyJhZ3RWZXIiOjEsImNsaWVudFZlciI6MSwiY2xpZW50VHlwZSI6MSwiY3RpbWUiOjIwMTkxMTA3MTgwMjk1OSwiY2N0aW1lIjoyMDE5MTEwNzE4MDI5NTksInNpZ24iOiJhYmNkZWZnIiwidG9rZW4iOiIxMTExMTEifQ=="}
     * 客户端加密字段:ctime+秘钥=sign
     * 返回加密字段:stime+秘钥=sign
     * result={
     *     "resultCode": "0",
     *     "message": "success",
     *     "data": {
     *         "jsonData": "eyJzaGFyZSI6eyJzaGFyZUFkZHJlc3MiOiJodHRwOi8vd3d3LmJhaWR1LmNvbT9pY29kZT0xIn0sInNpZ24iOiIzZTc2ZWEyMzhiMTJkZDg3NzJjNmUzYjQxZTY1NGNkOCIsInN0aW1lIjoxNTkwNjQ2MjQwOTIzfQ=="
     *     },
     *     "sgid": "202005281410380000001",
     *     "cgid": ""
     * }
     */
    @RequestMapping(value = "/share", method = {RequestMethod.POST})
    public JsonResult<Object> share(HttpServletRequest request, HttpServletResponse response, @RequestBody RequestEncryptionJson requestData) throws Exception{
        String sgid = ComponentUtil.redisIdService.getNewId();
        String cgid = "";
        String token;
        String ip = StringUtil.getIpAddress(request);
        String data = "";
        long did = 0;

        RequestStrategy requestModel = new RequestStrategy();
        try{
            // 解密
            data = StringUtil.decoderBase64(requestData.jsonData);
            requestModel  = JSON.parseObject(data, RequestStrategy.class);

            //#临时数据
            if (!StringUtils.isBlank(requestModel.token)){
                if (requestModel.token.equals("111111")){
                    ComponentUtil.redisService.set(requestModel.token, "1");
                }
            }

            // check校验数据
            did = HodgepodgeMethod.checkStrategyShareData(requestModel);
            String icode = "";
            if (did > ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_ZERO){
                DidModel didQuery = HodgepodgeMethod.assembleDidQuery(did);
                DidModel didModel = (DidModel) ComponentUtil.didService.findByObject(didQuery);
                icode = didModel.getIcode();
            }

            // 查询策略里面的分享地址
            StrategyModel strategyQuery = HodgepodgeMethod.assembleStrategyQuery(ServerConstant.StrategyEnum.SHARE_ADDRESS.getStgType());
            StrategyModel strategyModel = ComponentUtil.strategyService.getStrategyModel(strategyQuery, ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_ZERO);
            HodgepodgeMethod.checkStrategyByShare(strategyModel);

            // 组装返回客户端的数据
            long stime = System.currentTimeMillis();
            String sign = SignUtil.getSgin(stime, secretKeySign); // stime+秘钥=sign
            String strData = HodgepodgeMethod.assembleShareResult(stime, sign, strategyModel.getStgValue(), icode);
            // 数据加密
            String encryptionData = StringUtil.mergeCodeBase64(strData);
            ResponseEncryptionJson resultDataModel = new ResponseEncryptionJson();
            resultDataModel.jsonData = encryptionData;
            // 添加流水
            // 返回数据给客户端
            return JsonResult.successResult(resultDataModel, cgid, sgid);
        }catch (Exception e){
            Map<String,String> map = ExceptionMethod.getException(e, ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_TWO);
            // 添加异常
            log.error(String.format("this StrategyController.share() is error , the cgid=%s and sgid=%s and all data=%s!", cgid, sgid, data));
            e.printStackTrace();
            return JsonResult.failedResult(map.get("message"), map.get("code"), cgid, sgid);
        }
    }


}
