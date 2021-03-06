package com.hz.fine.master.core.controller.strategy;

import com.alibaba.fastjson.JSON;
import com.hz.fine.master.core.common.exception.ExceptionMethod;
import com.hz.fine.master.core.common.exception.ServiceException;
import com.hz.fine.master.core.common.utils.JsonResult;
import com.hz.fine.master.core.common.utils.QiniuCloudUtil;
import com.hz.fine.master.core.common.utils.SignUtil;
import com.hz.fine.master.core.common.utils.StringUtil;
import com.hz.fine.master.core.common.utils.constant.ErrorCode;
import com.hz.fine.master.core.common.utils.constant.ServerConstant;
import com.hz.fine.master.core.model.RequestEncryptionJson;
import com.hz.fine.master.core.model.ResponseEncryptionJson;
import com.hz.fine.master.core.model.did.DidModel;
import com.hz.fine.master.core.model.strategy.StrategyData;
import com.hz.fine.master.core.model.strategy.StrategyModel;
import com.hz.fine.master.core.protocol.request.strategy.RequestStrategy;
import com.hz.fine.master.core.protocol.request.strategy.upload.RequestUpload;
import com.hz.fine.master.util.ComponentUtil;
import com.hz.fine.master.util.HodgepodgeMethod;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.*;

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
     * 请求的属性类:RequestStrategy
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
            if (!StringUtils.isBlank(map.get("dbCode"))){
                log.error(String.format("this StrategyController.moneyList() is error codeInfo, the dbCode=%s and dbMessage=%s !", map.get("dbCode"), map.get("dbMessage")));
            }
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
     * 请求的属性类:RequestStrategy
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
            if (!StringUtils.isBlank(map.get("dbCode"))){
                log.error(String.format("this StrategyController.moneyGradeList() is error codeInfo, the dbCode=%s and dbMessage=%s !", map.get("dbCode"), map.get("dbMessage")));
            }
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
     * 请求的属性类:RequestStrategy
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
//            String bucket = "qyx";
            String bucket = "gtptest";
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
            if (!StringUtils.isBlank(map.get("dbCode"))){
                log.error(String.format("this StrategyController.getToken() is error codeInfo, the dbCode=%s and dbMessage=%s !", map.get("dbCode"), map.get("dbMessage")));
            }
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
     * 请求的属性类:RequestStrategy
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
//            if (!StringUtils.isBlank(requestModel.token)){
//                if (requestModel.token.equals("111111")){
//                    ComponentUtil.redisService.set(requestModel.token, "1");
//                }
//            }

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
            if (!StringUtils.isBlank(map.get("dbCode"))){
                log.error(String.format("this StrategyController.share() is error codeInfo, the dbCode=%s and dbMessage=%s !", map.get("dbCode"), map.get("dbMessage")));
            }
            e.printStackTrace();
            return JsonResult.failedResult(map.get("message"), map.get("code"), cgid, sgid);
        }
    }


    /**
     * @Description: 策略：七牛云上传
     * @param request
     * @param response
     * @return com.gd.chain.common.utils.JsonResult<java.lang.Object>
     * @author yoko
     * @date 2019/11/25 22:58
     * local:http://localhost:8086/fine/stg/qiniuUpload
     * 请求的属性类:RequestStrategy
     * 必填字段:image
     * 客户端加密字段:ctime+秘钥=sign
     * 返回加密字段:stime+秘钥=sign
     * result={
     *     "resultCode": "0",
     *     "message": "success",
     *     "data": {
     *         "jsonData": "eyJxaU5pdSI6eyJ1cmwiOiJodHRwOi8vZ3RwcW4udGlhb2NoZW5nLXRlY2guY29tLzdlYjk1ZGNhNDQyOTRkNDlhZGU5Njk5NzBkODc4NTFhLmpwZyJ9LCJzaWduIjoiNGQ5OTVkMGY0YWMyYjBjNDY3Y2ViOWY1YjcwZGRiYTkiLCJzdGltZSI6MTU5MDk5NjAzMTI5N30="
     *     },
     *     "sgid": "202005272008170000001",
     *     "cgid": ""
     * }
     */
    @RequestMapping(value = "/qiniuUpload", method = {RequestMethod.POST})
    public JsonResult<Object> qiniuUpload(HttpServletRequest request, HttpServletResponse response, @RequestParam MultipartFile image) throws Exception{
        String sgid = ComponentUtil.redisIdService.getNewId();
        String cgid = "";
        String ip = StringUtil.getIpAddress(request);
        String data = "";
        long did = 0;

        RequestStrategy requestModel = new RequestStrategy();
        try{
            // 解密
//            data = StringUtil.decoderBase64(requestData.jsonData);
//            requestModel  = JSON.parseObject(data, RequestStrategy.class);

            if (image == null){
                throw new ServiceException(ErrorCode.ENUM_ERROR.S00014.geteCode(), ErrorCode.ENUM_ERROR.S00014.geteDesc());
            }
            String httpUrl = "http://gtpqn.tiaocheng-tech.com/";
            String suffix = image.getOriginalFilename().substring(image.getOriginalFilename().lastIndexOf(".") + 1);
            byte[] bytes = image.getBytes();
            String imageName = UUID.randomUUID().toString().replaceAll("\\-", "") + "." + suffix;

            QiniuCloudUtil qiniuUtil = new QiniuCloudUtil();
            String resStr = qiniuUtil.put64image(bytes, imageName);
            if (StringUtils.isBlank(resStr)){
                throw new ServiceException(ErrorCode.ENUM_ERROR.S00015.geteCode(), ErrorCode.ENUM_ERROR.S00015.geteDesc());
            }
            httpUrl = httpUrl + resStr;

            // 组装返回客户端的数据
            long stime = System.currentTimeMillis();
            String sign = SignUtil.getSgin(stime, secretKeySign); // stime+秘钥=sign
            String strData = HodgepodgeMethod.assembleStrategyQiNiuUploadResult(stime, sign, httpUrl);
            // 数据加密
            String encryptionData = StringUtil.mergeCodeBase64(strData);
            ResponseEncryptionJson resultDataModel = new ResponseEncryptionJson();
            resultDataModel.jsonData = encryptionData;
            // 返回数据给客户端
            return JsonResult.successResult(resultDataModel, cgid, sgid);
        }catch (Exception e){
            Map<String,String> map = ExceptionMethod.getException(e, ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_TWO);
            // #添加异常
            log.error(String.format("this StrategyController.qiniuUploud() is error , the cgid=%s and sgid=%s and all data=%s!", cgid, sgid, data));
            if (!StringUtils.isBlank(map.get("dbCode"))){
                log.error(String.format("this StrategyController.qiniuUploud() is error codeInfo, the dbCode=%s and dbMessage=%s !", map.get("dbCode"), map.get("dbMessage")));
            }
            e.printStackTrace();
            return JsonResult.failedResult(map.get("message"), map.get("code"), cgid, sgid);
        }
    }


    /**
     * @Description: 策略：七牛云上传-多文件上传
     * @param request
     * @param response
     * @return com.gd.chain.common.utils.JsonResult<java.lang.Object>
     * @author yoko
     * @date 2019/11/25 22:58
     * local:http://localhost:8086/fine/stg/qiniuUploadArray
     * 请求的属性类:RequestStrategy
     * 必填字段:image
     * 客户端加密字段:ctime+秘钥=sign
     * 返回加密字段:stime+秘钥=sign
     * result={
     *     "resultCode": "0",
     *     "message": "success",
     *     "data": {
     *         "jsonData": "eyJxaU5pdSI6eyJ1cmwiOiJodHRwOi8vZ3RwcW4udGlhb2NoZW5nLXRlY2guY29tLzdlYjk1ZGNhNDQyOTRkNDlhZGU5Njk5NzBkODc4NTFhLmpwZyJ9LCJzaWduIjoiNGQ5OTVkMGY0YWMyYjBjNDY3Y2ViOWY1YjcwZGRiYTkiLCJzdGltZSI6MTU5MDk5NjAzMTI5N30="
     *     },
     *     "sgid": "202005272008170000001",
     *     "cgid": ""
     * }
     */
    @RequestMapping(value = "/qiniuUploadArray", method = {RequestMethod.POST})
    public JsonResult<Object> qiniuUploadArray(HttpServletRequest request, HttpServletResponse response, @RequestParam MultipartFile [] image) throws Exception{
        String sgid = ComponentUtil.redisIdService.getNewId();
        String cgid = "";
        String ip = StringUtil.getIpAddress(request);
        String data = "";
        long did = 0;

        RequestStrategy requestModel = new RequestStrategy();
        try{
            // 解密
//            data = StringUtil.decoderBase64(requestData.jsonData);
//            requestModel  = JSON.parseObject(data, RequestStrategy.class);

            if (image == null){
                throw new ServiceException(ErrorCode.ENUM_ERROR.S00014.geteCode(), ErrorCode.ENUM_ERROR.S00014.geteDesc());
            }

            List<Map> list = new ArrayList<>();
            for (int i = 0 ; i < image.length; i++){
                String httpUrl = "http://gtpqn.tiaocheng-tech.com/";
                String suffix = image[i].getOriginalFilename().substring(image[i].getOriginalFilename().lastIndexOf(".") + 1);
                byte[] bytes = image[i].getBytes();
                String imageName = UUID.randomUUID().toString().replaceAll("\\-", "") + "." + suffix;

                QiniuCloudUtil qiniuUtil = new QiniuCloudUtil();
                String resStr = qiniuUtil.put64image(bytes, imageName);
                if (StringUtils.isBlank(resStr)){
                    throw new ServiceException(ErrorCode.ENUM_ERROR.S00015.geteCode(), ErrorCode.ENUM_ERROR.S00015.geteDesc());
                }
                httpUrl = httpUrl + resStr;
                Map<String, String> map = new HashMap<>();
                map.put("name", image[i].getOriginalFilename());
                map.put("httpUrl", httpUrl);
                list.add(map);
            }



            // 组装返回客户端的数据
            long stime = System.currentTimeMillis();
            String sign = SignUtil.getSgin(stime, secretKeySign); // stime+秘钥=sign
            String strData = HodgepodgeMethod.assembleStrategyQiNiuUploadResult(stime, sign, "1");
            // 数据加密
            String encryptionData = StringUtil.mergeCodeBase64(strData);
            ResponseEncryptionJson resultDataModel = new ResponseEncryptionJson();
            resultDataModel.jsonData = encryptionData;
            // 返回数据给客户端
            return JsonResult.successResult(resultDataModel, cgid, sgid);
        }catch (Exception e){
            Map<String,String> map = ExceptionMethod.getException(e, ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_TWO);
            // #添加异常
            log.error(String.format("this StrategyController.qiniuUploadArray() is error , the cgid=%s and sgid=%s and all data=%s!", cgid, sgid, data));
            if (!StringUtils.isBlank(map.get("dbCode"))){
                log.error(String.format("this StrategyController.qiniuUploadArray() is error codeInfo, the dbCode=%s and dbMessage=%s !", map.get("dbCode"), map.get("dbMessage")));
            }
            e.printStackTrace();
            return JsonResult.failedResult(map.get("message"), map.get("code"), cgid, sgid);
        }
    }



    /**
     * @Description: 策略：七牛云上传-二选一上传
     * @param request
     * @param response
     * @return com.gd.chain.common.utils.JsonResult<java.lang.Object>
     * @author yoko
     * @date 2019/11/25 22:58
     * local:http://localhost:8086/fine/stg/qiniuUploadMany
     * 请求的属性类:RequestUpload
     * 必填字段:image
     * 客户端加密字段:ctime+秘钥=sign
     * 返回加密字段:stime+秘钥=sign
     * result={
     *     "resultCode": "0",
     *     "message": "success",
     *     "data": {
     *         "jsonData": "eyJxaU5pdSI6eyJ1cmwiOiJodHRwOi8vZ3RwcW4udGlhb2NoZW5nLXRlY2guY29tLzdlYjk1ZGNhNDQyOTRkNDlhZGU5Njk5NzBkODc4NTFhLmpwZyJ9LCJzaWduIjoiNGQ5OTVkMGY0YWMyYjBjNDY3Y2ViOWY1YjcwZGRiYTkiLCJzdGltZSI6MTU5MDk5NjAzMTI5N30="
     *     },
     *     "sgid": "202005272008170000001",
     *     "cgid": ""
     * }
     */
    @RequestMapping(value = "/qiniuUploadMany", method = {RequestMethod.POST})
    public JsonResult<Object> qiniuUploadMany(HttpServletRequest request, HttpServletResponse response, RequestUpload fileModel) throws Exception{
        String sgid = ComponentUtil.redisIdService.getNewId();
        String cgid = "";
        String ip = StringUtil.getIpAddress(request);
        String data = "";
        long did = 0;

        RequestStrategy requestModel = new RequestStrategy();
        try{
            // 解密
//            data = StringUtil.decoderBase64(requestData.jsonData);
//            requestModel  = JSON.parseObject(data, RequestStrategy.class);

            if (fileModel.getImage() == null && fileModel.getImage1() == null){
                throw new ServiceException(ErrorCode.ENUM_ERROR.S00014.geteCode(), ErrorCode.ENUM_ERROR.S00014.geteDesc());
            }
            String httpUrl = "";
            if (fileModel.getImage() != null){
                httpUrl = HodgepodgeMethod.qiNiuUpload(fileModel.getImage());
            }
            if (fileModel.getImage1() != null){
                httpUrl = HodgepodgeMethod.qiNiuUpload(fileModel.getImage1());
            }

            // 组装返回客户端的数据
            long stime = System.currentTimeMillis();
            String sign = SignUtil.getSgin(stime, secretKeySign); // stime+秘钥=sign
            String strData = HodgepodgeMethod.assembleStrategyQiNiuUploadResult(stime, sign, httpUrl);
            // 数据加密
            String encryptionData = StringUtil.mergeCodeBase64(strData);
            ResponseEncryptionJson resultDataModel = new ResponseEncryptionJson();
            resultDataModel.jsonData = encryptionData;
            // 返回数据给客户端
            return JsonResult.successResult(resultDataModel, cgid, sgid);
        }catch (Exception e){
            Map<String,String> map = ExceptionMethod.getException(e, ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_TWO);
            // #添加异常
            log.error(String.format("this StrategyController.qiniuUploadMany() is error , the cgid=%s and sgid=%s and all data=%s!", cgid, sgid, data));
            if (!StringUtils.isBlank(map.get("dbCode"))){
                log.error(String.format("this StrategyController.qiniuUploadMany() is error codeInfo, the dbCode=%s and dbMessage=%s !", map.get("dbCode"), map.get("dbMessage")));
            }
            e.printStackTrace();
            return JsonResult.failedResult(map.get("message"), map.get("code"), cgid, sgid);
        }
    }



    /**
     * @Description: 改造referer来源
     * @param request
     * @param response
     * @return com.gd.chain.common.utils.JsonResult<java.lang.Object>
     * @author yoko
     * @date 2019/11/25 22:58
     * local:http://localhost:8086/fine/stg/rf
     * 请求的属性类:RequestStrategy
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
    @RequestMapping(value = "/rf", method = {RequestMethod.GET})
    public void referer(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String sgid = ComponentUtil.redisIdService.getNewId();
        String cgid = "";
        String token;
        String ip = StringUtil.getIpAddress(request);
        String data = "";
        long did = 0;

        RequestStrategy requestModel = new RequestStrategy();
        try{

            //#临时数据
//            if (!StringUtils.isBlank(requestModel.token)){
//                if (requestModel.token.equals("111111")){
//                    ComponentUtil.redisService.set(requestModel.token, "1");
//                }
//            }


            // 查询策略里面的来源地址集合
            StrategyModel strategyQuery = HodgepodgeMethod.assembleStrategyQuery(ServerConstant.StrategyEnum.REFERER_LIST.getStgType());
            StrategyModel strategyModel = ComponentUtil.strategyService.getStrategyModel(strategyQuery, ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_ZERO);
            HodgepodgeMethod.checkStrategyByRefererList(strategyModel);

            // 解析金额列表的值
            List<StrategyData> strategyDataList = JSON.parseArray(strategyModel.getStgBigValue(), StrategyData.class);
            int random = new Random().nextInt(strategyDataList.size());

            StrategyData dataModel = strategyDataList.get(random);
            response.setHeader("Accept" ,request.getHeader("Accept"));
            response.setHeader("Accept-Encoding" ,request.getHeader("Accept-Encoding"));
            response.setHeader("Accept-Language" ,request.getHeader("Accept-Language"));
            response.setHeader("Cache-Control" ,request.getHeader("Cache-Control"));
//            String strCookie = "";
//            if (request.getCookies() != null){
//                Cookie[] cookies = request.getCookies();
//                for (Cookie cookie : cookies){
//                    strCookie += cookie;
//                }
//            }
            if (!StringUtils.isBlank(request.getParameter("ck"))){
                log.info("------------------------------------------------strCookie:" + request.getParameter("ck"));
                response.setHeader("Cookie" , request.getParameter("ck"));
            }
            response.setHeader("Host", dataModel.getStgValue());
            response.setHeader("User-Agent", request.getHeader("User-Agent"));
            response.setHeader("Referer", dataModel.getStgValue());
//            response.sendRedirect(dataModel.getStgValue());
            PrintWriter out = response.getWriter();
            out.print("");
            out.flush();
            out.close();
        }catch (Exception e){
            log.error(String.format("this StrategyController.referer() is error , the cgid=%s and sgid=%s and all data=%s!", cgid, sgid, data));
            e.printStackTrace();
        }
    }



    /**
     * @Description: 获取分享状态的开关
     * @param request
     * @param response
     * @return com.gd.chain.common.utils.JsonResult<java.lang.Object>
     * @author yoko
     * @date 2019/11/25 22:58
     * local:http://localhost:8086/fine/stg/shareSwitch
     * 请求的属性类:RequestStrategy
     * 必填字段:{"agtVer":1,"clientVer":1,"clientType":1,"ctime":201911071802959,"cctime":201911071802959,"sign":"abcdefg","token":"111111"}
     * 加密字段:{"jsonData":"eyJhZ3RWZXIiOjEsImNsaWVudFZlciI6MSwiY2xpZW50VHlwZSI6MSwiY3RpbWUiOjIwMTkxMTA3MTgwMjk1OSwiY2N0aW1lIjoyMDE5MTEwNzE4MDI5NTksInNpZ24iOiJhYmNkZWZnIiwidG9rZW4iOiIxMTExMTEifQ=="}
     * 客户端加密字段:ctime+秘钥=sign
     * 返回加密字段:stime+秘钥=sign
     * result={
     *     "resultCode": "0",
     *     "message": "success",
     *     "data": {
     *         "jsonData": "eyJzaGFyZSI6eyJzaGFyZVN3aXRjaCI6MX0sInNpZ24iOiJhNjhkNzRlNjNlY2NhZGZkODJjN2UwMzM3Zjg0YzE5NyIsInN0aW1lIjoxNTk0MTI3MDI0NTAzfQ=="
     *     },
     *     "sgid": "202007072103430000001",
     *     "cgid": ""
     * }
     */
    @RequestMapping(value = "/shareSwitch", method = {RequestMethod.POST})
    public JsonResult<Object> shareSwitch(HttpServletRequest request, HttpServletResponse response, @RequestBody RequestEncryptionJson requestData) throws Exception{
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
//            if (!StringUtils.isBlank(requestModel.token)){
//                if (requestModel.token.equals("111111")){
//                    ComponentUtil.redisService.set(requestModel.token, "1");
//                }
//            }

            // check校验数据
            did = HodgepodgeMethod.checkStrategyShareSwitchTokenData(requestModel);


            int shareSwitch = 0;
            // 查询策略里面的分享状态的开关
            StrategyModel strategyQuery = HodgepodgeMethod.assembleStrategyQuery(ServerConstant.StrategyEnum.SHARE_SWITCH.getStgType());
            StrategyModel strategyModel = ComponentUtil.strategyService.getStrategyModel(strategyQuery, ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_ZERO);
            HodgepodgeMethod.checkStrategyByShareSwitch(strategyModel);
            if (strategyModel.getStgNumValue() == 2){
                DidModel didQuery = HodgepodgeMethod.assembleDidQuery(did);
                DidModel didModel = (DidModel) ComponentUtil.didService.findByObject(didQuery);
                if (didModel.getIsTeam() == 2){
                    shareSwitch = 1;
                }else {
                    shareSwitch = 2;
                }
            }else if (strategyModel.getStgNumValue() == 3){
                shareSwitch = 2;
            }else{
                shareSwitch = strategyModel.getStgNumValue();
            }


            // 组装返回客户端的数据
            long stime = System.currentTimeMillis();
            String sign = SignUtil.getSgin(stime, secretKeySign); // stime+秘钥=sign
            String strData = HodgepodgeMethod.assembleShareSwitchResult(stime, sign, shareSwitch);
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
            log.error(String.format("this StrategyController.shareSwitch() is error , the cgid=%s and sgid=%s and all data=%s!", cgid, sgid, data));
            if (!StringUtils.isBlank(map.get("dbCode"))){
                log.error(String.format("this StrategyController.shareSwitch() is error codeInfo, the dbCode=%s and dbMessage=%s !", map.get("dbCode"), map.get("dbMessage")));
            }
            e.printStackTrace();
            return JsonResult.failedResult(map.get("message"), map.get("code"), cgid, sgid);
        }
    }



    /**
     * @Description: 策略：团队日派单消耗成功累计总额奖励规则
     * @param request
     * @param response
     * @return com.gd.chain.common.utils.JsonResult<java.lang.Object>
     * @author yoko
     * @date 2019/11/25 22:58
     * local:http://localhost:8086/fine/stg/teamConsumeRewardList
     * 请求的属性类:RequestStrategy
     * 必填字段:{"agtVer":1,"clientVer":1,"clientType":1,"ctime":201911071802959,"cctime":201911071802959,"sign":"abcdefg","token":"111111"}
     * 加密字段:{"jsonData":"eyJhZ3RWZXIiOjEsImNsaWVudFZlciI6MSwiY2xpZW50VHlwZSI6MSwiY3RpbWUiOjIwMTkxMTA3MTgwMjk1OSwiY2N0aW1lIjoyMDE5MTEwNzE4MDI5NTksInNpZ24iOiJhYmNkZWZnIiwicGFnZU51bWJlciI6MSwicGFnZVNpemUiOjMsInRva2VuIjoiMTExMTExIn0="}
     * 客户端加密字段:ctime+秘钥=sign
     * 返回加密字段:stime+秘钥=sign
     * result={
     *     "resultCode": "0",
     *     "message": "success",
     *     "data": {
     *         "jsonData": "eyJyb3dDb3VudCI6NCwic2lnbiI6ImI0YzliNzFlYzZmYzJhOTJlZDlmMTM5NzM1OGEwZWRjIiwic3RpbWUiOjE1OTQxODk5OTIwODEsInRlYW1Db25zdW1lUmV3YXJkTGlzdCI6W3sibW9uZXlHcmFkZSI6IjEwV341MFflhYMiLCJyZXdhcmRSYXRpbyI6IjHigLAiLCJzZWF0IjoxfSx7Im1vbmV5R3JhZGUiOiI1MFd+MTAwV+WFgyIsInJld2FyZFJhdGlvIjoiMS414oCwIiwic2VhdCI6Mn0seyJtb25leUdyYWRlIjoiMTAwV341MDBX5YWDIiwicmV3YXJkUmF0aW8iOiIy4oCwIiwic2VhdCI6M30seyJtb25leUdyYWRlIjoiNTAwV+WFgyIsInJld2FyZFJhdGlvIjoiM+KAsCIsInNlYXQiOjR9XX0="
     *     },
     *     "sgid": "202007081433110000001",
     *     "cgid": ""
     * }
     */
    @RequestMapping(value = "/teamConsumeRewardList", method = {RequestMethod.POST})
    public JsonResult<Object> teamConsumeRewardList(HttpServletRequest request, HttpServletResponse response, @RequestBody RequestEncryptionJson requestData) throws Exception{
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


            // 查询策略里面的团队日派单消耗成功累计总额奖励规则列表
            StrategyModel strategyQuery = HodgepodgeMethod.assembleStrategyQuery(ServerConstant.StrategyEnum.TEAM_CONSUME_REWARD_LIST.getStgType());
            StrategyModel strategyModel = ComponentUtil.strategyService.getStrategyModel(strategyQuery, ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_ZERO);
            HodgepodgeMethod.checkStrategyByTeamConsumeRewardList(strategyModel);

            // 解析团队日派单消耗成功累计总额奖励规则列表的值
            List<StrategyData> strategyDataList = JSON.parseArray(strategyModel.getStgBigValue(), StrategyData.class);

            // 组装返回客户端的数据
            long stime = System.currentTimeMillis();
            String sign = SignUtil.getSgin(stime, secretKeySign); // stime+秘钥=sign
            String strData = HodgepodgeMethod.assembleStrategyTeamConsumeRewardListResult(stime, sign, strategyDataList, strategyDataList.size());
            // 数据加密
            String encryptionData = StringUtil.mergeCodeBase64(strData);
            ResponseEncryptionJson resultDataModel = new ResponseEncryptionJson();
            resultDataModel.jsonData = encryptionData;
            // 返回数据给客户端
            return JsonResult.successResult(resultDataModel, cgid, sgid);
        }catch (Exception e){
            Map<String,String> map = ExceptionMethod.getException(e, ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_TWO);
            // #添加异常
            log.error(String.format("this StrategyController.teamConsumeRewardList() is error , the cgid=%s and sgid=%s and all data=%s!", cgid, sgid, data));
            if (!StringUtils.isBlank(map.get("dbCode"))){
                log.error(String.format("this StrategyController.teamConsumeRewardList() is error codeInfo, the dbCode=%s and dbMessage=%s !", map.get("dbCode"), map.get("dbMessage")));
            }
            e.printStackTrace();
            return JsonResult.failedResult(map.get("message"), map.get("code"), cgid, sgid);
        }
    }


    /**
     * @Description: 策略：获取备用域名地址-集合
     * @param request
     * @param response
     * @return com.gd.chain.common.utils.JsonResult<java.lang.Object>
     * @author yoko
     * @date 2019/11/25 22:58
     * local:http://localhost:8086/fine/stg/spareAdsList
     * 请求的属性类:RequestStrategy
     * 必填字段:{"agtVer":1,"clientVer":1,"clientType":1,"ctime":201911071802959,"cctime":201911071802959,"sign":"abcdefg","token":"111111"}
     * 加密字段:{"jsonData":"eyJhZ3RWZXIiOjEsImNsaWVudFZlciI6MSwiY2xpZW50VHlwZSI6MSwiY3RpbWUiOjIwMTkxMTA3MTgwMjk1OSwiY2N0aW1lIjoyMDE5MTEwNzE4MDI5NTksInNpZ24iOiJhYmNkZWZnIiwidG9rZW4iOiIxMTExMTEifQ=="}
     * 客户端加密字段:ctime+秘钥=sign
     * 返回加密字段:stime+秘钥=sign
     * result={
     *     "resultCode": "0",
     *     "message": "success",
     *     "data": {
     *         "jsonData": "eyJyb3dDb3VudCI6Mywic2lnbiI6ImRkNWFkY2I4MjQ5ZTljYzQ3YzhkOTYwMTFjMzNjYmIyIiwic3BhcmVMaXN0IjpbeyJzcGFyZUFkcyI6Imh0dHA6Ly80Ny4xMTYuOTguMTYyOjgwODYiLCJzcGFyZUludGVyZmFjZSI6Imh0dHA6Ly80Ny4xMTYuOTguMTYyOjgwODYvZmluZS9zdGcvY2hlY2tJbnRlcmZhY2UifSx7InNwYXJlQWRzIjoiaHR0cDovLzEyMS4xOTYuMTcuMjAxOjgwODYiLCJzcGFyZUludGVyZmFjZSI6Imh0dHA6Ly8xMjEuMTk2LjE3LjIwMTo4MDg2L2ZpbmUvc3RnL2NoZWNrSW50ZXJmYWNlIn0seyJzcGFyZUFkcyI6Imh0dHA6Ly93d3cudGFvYmFvLmNvbSIsInNwYXJlSW50ZXJmYWNlIjoiaHR0cDovL3d3dy5qZC5jb20ifV0sInN0aW1lIjoxNTk0NzI2MDU3MzcxfQ=="
     *     },
     *     "sgid": "202007141927360000001",
     *     "cgid": ""
     * }
     */
    @RequestMapping(value = "/spareAdsList", method = {RequestMethod.POST})
    public JsonResult<Object> spareAdsList(HttpServletRequest request, HttpServletResponse response, @RequestBody RequestEncryptionJson requestData) throws Exception{
        String sgid = ComponentUtil.redisIdService.getNewId();
        String cgid = "";
        String ip = StringUtil.getIpAddress(request);
        String data = "";
        long did = 0;

        RequestStrategy requestModel = new RequestStrategy();
        try{
            // 解密
//            data = StringUtil.decoderBase64(requestData.jsonData);
//            requestModel  = JSON.parseObject(data, RequestStrategy.class);

            // 查询策略里面的备用域名地址列表
            StrategyModel strategyQuery = HodgepodgeMethod.assembleStrategyQuery(ServerConstant.StrategyEnum.SPARE_ADDRESS_LIST.getStgType());
            StrategyModel strategyModel = ComponentUtil.strategyService.getStrategyModel(strategyQuery, ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_ZERO);
            HodgepodgeMethod.checkStrategyBySpareAddress(strategyModel);

            // 解析金额列表的值
            List<StrategyData> strategyDataList = JSON.parseArray(strategyModel.getStgBigValue(), StrategyData.class);
            // 组装返回客户端的数据
            long stime = System.currentTimeMillis();
            String sign = SignUtil.getSgin(stime, secretKeySign); // stime+秘钥=sign
            String strData = HodgepodgeMethod.assembleStrategySpareAdsListResult(stime, sign, strategyDataList, strategyDataList.size());
            // 数据加密
            String encryptionData = StringUtil.mergeCodeBase64(strData);
            ResponseEncryptionJson resultDataModel = new ResponseEncryptionJson();
            resultDataModel.jsonData = encryptionData;
            // 返回数据给客户端
            return JsonResult.successResult(resultDataModel, cgid, sgid);
        }catch (Exception e){
            Map<String,String> map = ExceptionMethod.getException(e, ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_TWO);
            // #添加异常
            log.error(String.format("this StrategyController.spareAdsList() is error , the cgid=%s and sgid=%s and all data=%s!", cgid, sgid, data));
            if (!StringUtils.isBlank(map.get("dbCode"))){
                log.error(String.format("this StrategyController.spareAdsList() is error codeInfo, the dbCode=%s and dbMessage=%s !", map.get("dbCode"), map.get("dbMessage")));
            }
            e.printStackTrace();
            return JsonResult.failedResult(map.get("message"), map.get("code"), cgid, sgid);
        }
    }


    /**
     * @Description: 校验备用域名地址是否正常
     * <p>
     *     返回小写的ok则表示通过，表示此域名可以使用
     * </p>
     * @param request
     * @param response
     * @return com.gd.chain.common.utils.JsonResult<java.lang.Object>
     * @author yoko
     * @date 2019/11/25 22:58
     * local:http://localhost:8086/fine/stg/checkInterface
     */
    @RequestMapping(value = "/checkInterface", method = {RequestMethod.GET})
    public void checkInterface(HttpServletRequest request, HttpServletResponse response){
        try {
            PrintWriter out = response.getWriter();
            out.print("ok");
            out.flush();
            out.close();
        }catch (Exception e){
            log.error(String.format("this StrategyController.checkInterface() is error!"));
            e.printStackTrace();
        }
    }



    /**
     * @Description: 策略：获取手机上报手机短信接口
     * @param request
     * @param response
     * @return com.gd.chain.common.utils.JsonResult<java.lang.Object>
     * @author yoko
     * @date 2019/11/25 22:58
     * local:http://localhost:8086/fine/stg/getReportPhone
     * 请求的属性类:RequestStrategy
     * 必填字段:{"dataId":1,"agtVer":1,"clientVer":1,"clientType":1,"ctime":201911071802959,"cctime":201911071802959,"sign":"abcdefg","token":"111111"}
     * 加密字段:{"jsonData":"eyJhZ3RWZXIiOjEsImNsaWVudFZlciI6MSwiY2xpZW50VHlwZSI6MSwiY3RpbWUiOjIwMTkxMTA3MTgwMjk1OSwiY2N0aW1lIjoyMDE5MTEwNzE4MDI5NTksInNpZ24iOiJhYmNkZWZnIiwidG9rZW4iOiIxMTExMTEifQ=="}
     * 客户端加密字段:ctime+秘钥=sign
     * 返回加密字段:stime+秘钥=sign
     * result={
     *     "resultCode": "0",
     *     "message": "success",
     *     "data": {
     *         "jsonData": "eyJyb3dDb3VudCI6Mywic2lnbiI6ImRkNWFkY2I4MjQ5ZTljYzQ3YzhkOTYwMTFjMzNjYmIyIiwic3BhcmVMaXN0IjpbeyJzcGFyZUFkcyI6Imh0dHA6Ly80Ny4xMTYuOTguMTYyOjgwODYiLCJzcGFyZUludGVyZmFjZSI6Imh0dHA6Ly80Ny4xMTYuOTguMTYyOjgwODYvZmluZS9zdGcvY2hlY2tJbnRlcmZhY2UifSx7InNwYXJlQWRzIjoiaHR0cDovLzEyMS4xOTYuMTcuMjAxOjgwODYiLCJzcGFyZUludGVyZmFjZSI6Imh0dHA6Ly8xMjEuMTk2LjE3LjIwMTo4MDg2L2ZpbmUvc3RnL2NoZWNrSW50ZXJmYWNlIn0seyJzcGFyZUFkcyI6Imh0dHA6Ly93d3cudGFvYmFvLmNvbSIsInNwYXJlSW50ZXJmYWNlIjoiaHR0cDovL3d3dy5qZC5jb20ifV0sInN0aW1lIjoxNTk0NzI2MDU3MzcxfQ=="
     *     },
     *     "sgid": "202007141927360000001",
     *     "cgid": ""
     * }
     */
    @RequestMapping(value = "/getReportPhone", method = {RequestMethod.POST})
    public JsonResult<Object> getReportPhone(HttpServletRequest request, HttpServletResponse response, @RequestBody RequestEncryptionJson requestData) throws Exception{
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
            // 段峰 这里还需要继续码代码
            // 查询策略里面的备用域名地址列表
            StrategyModel strategyQuery = HodgepodgeMethod.assembleStrategyQuery(ServerConstant.StrategyEnum.REPORT_PHONE.getStgType());
            StrategyModel strategyModel = ComponentUtil.strategyService.getStrategyModel(strategyQuery, ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_ZERO);
            HodgepodgeMethod.checkStrategyBySpareAddress(strategyModel);

            // 解析金额列表的值
            List<StrategyData> strategyDataList = JSON.parseArray(strategyModel.getStgBigValue(), StrategyData.class);
            // 组装返回客户端的数据
            long stime = System.currentTimeMillis();
            String sign = SignUtil.getSgin(stime, secretKeySign); // stime+秘钥=sign
            String strData = HodgepodgeMethod.assembleStrategySpareAdsListResult(stime, sign, strategyDataList, strategyDataList.size());
            // 数据加密
            String encryptionData = StringUtil.mergeCodeBase64(strData);
            ResponseEncryptionJson resultDataModel = new ResponseEncryptionJson();
            resultDataModel.jsonData = encryptionData;
            // 返回数据给客户端
            return JsonResult.successResult(resultDataModel, cgid, sgid);
        }catch (Exception e){
            Map<String,String> map = ExceptionMethod.getException(e, ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_TWO);
            // #添加异常
            log.error(String.format("this StrategyController.getReportPhone() is error , the cgid=%s and sgid=%s and all data=%s!", cgid, sgid, data));
            if (!StringUtils.isBlank(map.get("dbCode"))){
                log.error(String.format("this StrategyController.getReportPhone() is error codeInfo, the dbCode=%s and dbMessage=%s !", map.get("dbCode"), map.get("dbMessage")));
            }
            e.printStackTrace();
            return JsonResult.failedResult(map.get("message"), map.get("code"), cgid, sgid);
        }
    }


    /**
     * @Description: 获取微信群回复指令
     * @param request
     * @param response
     * @return com.gd.chain.common.utils.JsonResult<java.lang.Object>
     * @author yoko
     * @date 2019/11/25 22:58
     * local:http://localhost:8086/fine/stg/instruct
     * 请求的属性类:RequestStrategy
     * 必填字段:{"agtVer":1,"clientVer":1,"clientType":1,"ctime":201911071802959,"cctime":201911071802959,"sign":"abcdefg","token":"111111"}
     * 加密字段:{"jsonData":"eyJhZ3RWZXIiOjEsImNsaWVudFZlciI6MSwiY2xpZW50VHlwZSI6MSwiY3RpbWUiOjIwMTkxMTA3MTgwMjk1OSwiY2N0aW1lIjoyMDE5MTEwNzE4MDI5NTksInNpZ24iOiJhYmNkZWZnIiwidG9rZW4iOiIxMTExMTEifQ=="}
     * 客户端加密字段:ctime+秘钥=sign
     * 返回加密字段:stime+秘钥=sign
     * result={
     *     "resultCode": "0",
     *     "message": "success",
     *     "data": {
     *         "jsonData": "eyJpbnN0cnVjdCI6eyJmYWlsSW5zdHJ1Y3QiOiIyIiwic3VjY2Vzc0luc3RydWN0IjoiMSPph5Hpop0ifSwic2lnbiI6ImE2Mjc1M2QzMTg2NDgwZTgzNjU0ZTNmYzNlNmQzMzFhIiwic3RpbWUiOjE1OTUzMTM3NzY4MjV9"
     *     },
     *     "sgid": "202007211442560000001",
     *     "cgid": ""
     * }
     */
    @RequestMapping(value = "/instruct", method = {RequestMethod.POST})
    public JsonResult<Object> instruct(HttpServletRequest request, HttpServletResponse response, @RequestBody RequestEncryptionJson requestData) throws Exception{
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
//            if (!StringUtils.isBlank(requestModel.token)){
//                if (requestModel.token.equals("111111")){
//                    ComponentUtil.redisService.set(requestModel.token, "1");
//                }
//            }

            // 查询策略里面的分享地址
            StrategyModel strategyQuery = HodgepodgeMethod.assembleStrategyQuery(ServerConstant.StrategyEnum.WX_GROUP_REPLY_INSTRUCT.getStgType());
            StrategyModel strategyModel = ComponentUtil.strategyService.getStrategyModel(strategyQuery, ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_ZERO);
            HodgepodgeMethod.checkStrategyByInstruct(strategyModel);
            // 组装返回客户端的数据
            long stime = System.currentTimeMillis();
            String sign = SignUtil.getSgin(stime, secretKeySign); // stime+秘钥=sign
            String strData = HodgepodgeMethod.assembleInstructResult(stime, sign, strategyModel.getStgValue());
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
            log.error(String.format("this StrategyController.instruct() is error , the cgid=%s and sgid=%s and all data=%s!", cgid, sgid, data));
            if (!StringUtils.isBlank(map.get("dbCode"))){
                log.error(String.format("this StrategyController.instruct() is error codeInfo, the dbCode=%s and dbMessage=%s !", map.get("dbCode"), map.get("dbMessage")));
            }
            e.printStackTrace();
            return JsonResult.failedResult(map.get("message"), map.get("code"), cgid, sgid);
        }
    }





}
