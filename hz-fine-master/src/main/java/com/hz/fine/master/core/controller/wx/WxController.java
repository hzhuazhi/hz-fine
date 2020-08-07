package com.hz.fine.master.core.controller.wx;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.hz.fine.master.core.common.exception.ExceptionMethod;
import com.hz.fine.master.core.common.utils.JsonResult;
import com.hz.fine.master.core.common.utils.SignUtil;
import com.hz.fine.master.core.common.utils.StringUtil;
import com.hz.fine.master.core.common.utils.constant.CacheKey;
import com.hz.fine.master.core.common.utils.constant.CachedKeyUtils;
import com.hz.fine.master.core.common.utils.constant.ServerConstant;
import com.hz.fine.master.core.model.RequestEncryptionJson;
import com.hz.fine.master.core.model.ResponseEncryptionJson;
import com.hz.fine.master.core.model.did.DidCollectionAccountModel;
import com.hz.fine.master.core.model.did.DidRewardModel;
import com.hz.fine.master.core.model.wx.WxClerkModel;
import com.hz.fine.master.core.model.wx.WxModel;
import com.hz.fine.master.core.protocol.request.wx.RequestWx;
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
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Description 小微的Controller层
 * @Author yoko
 * @Date 2020/7/30 15:02
 * @Version 1.0
 */
@RestController
@RequestMapping("/fine/wx")
public class WxController {

    private static Logger log = LoggerFactory.getLogger(WxController.class);

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
     * 5小时
     */
    public long FIVE_HOUR = 5;

    @Value("${secret.key.token}")
    private String secretKeyToken;

    @Value("${secret.key.sign}")
    private String secretKeySign;



    /**
     * @Description: 小微数据-集合
     * @param request
     * @param response
     * @return com.gd.chain.common.utils.JsonResult<java.lang.Object>
     * @author yoko
     * @date 2019/11/25 22:58
     * local:http://localhost:8086/fine/wx/getDataList
     * 请求的属性类:RequestReward
     * 必填字段:{"agtVer":1,"clientVer":1,"clientType":1,"ctime":201911071802959,"cctime":201911071802959,"sign":"abcdefg","pageNumber":1,"pageSize":3,"token":"111111"}
     * 加密字段:{"jsonData":"eyJjdXJkYXlTdGFydCI6MjAyMDA1MjEsImN1cmRheUVuZCI6MjAyMDA1MjEsImFndFZlciI6MSwiY2xpZW50VmVyIjoxLCJjbGllbnRUeXBlIjoxLCJjdGltZSI6MjAxOTExMDcxODAyOTU5LCJjY3RpbWUiOjIwMTkxMTA3MTgwMjk1OSwic2lnbiI6ImFiY2RlZmciLCJwYWdlTnVtYmVyIjoxLCJwYWdlU2l6ZSI6MywidG9rZW4iOiIxMTExMTEifQ=="}
     * 客户端加密字段:ctime+秘钥=sign
     * 返回加密字段:stime+秘钥=sign
     * result={
     *     "resultCode": "0",
     *     "message": "success",
     *     "data": {
     *         "jsonData": "eyJkYXRhTGlzdCI6W3siY3JlYXRlVGltZSI6IjIwMjAtMDUtMjEgMTg6MDk6MjMiLCJpZCI6MSwibW9uZXkiOiIxMCIsIm9yZGVyTm8iOiJvcmRlcl9ub18xIiwib3JpZ2luIjoiMTAwMCIsInByb29mIjoiMTAwMCIsInJld2FyZFR5cGUiOjF9LHsiY3JlYXRlVGltZSI6IjIwMjAtMDUtMjEgMTg6MDk6MjMiLCJpZCI6MiwibW9uZXkiOiIyMCIsIm9yZGVyTm8iOiJvcmRlcl9ub18yIiwib3JpZ2luIjoiMjAwMCIsInByb29mIjoiMjAwMCIsInJld2FyZFR5cGUiOjJ9LHsiY3JlYXRlVGltZSI6IjIwMjAtMDUtMjEgMTg6MDk6MjMiLCJpZCI6MywibW9uZXkiOiIzMCIsIm9yZGVyTm8iOiJvcmRlcl9ub18zIiwib3JpZ2luIjoiMzAwMCIsInByb29mIjoi5bCP5LqU5ZOl55u05o6oIiwicmV3YXJkVHlwZSI6M31dLCJyb3dDb3VudCI6NSwic2lnbiI6IjU4YWJhMTA0MDlhNDk0YmQyMjc2YWI0YjM0ZjQ3MTljIiwic3RpbWUiOjE1OTAwNTU5MzM4NDJ9"
     *     },
     *     "sgid": "202005211812120000001",
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

        RequestWx requestModel = new RequestWx();
        try{
            // 解密
            data = StringUtil.decoderBase64(requestData.jsonData);
            requestModel  = JSON.parseObject(data, RequestWx.class);
            //#临时数据
//            if (!StringUtils.isBlank(requestModel.token)){
//                if (requestModel.token.equals("111111")){
//                    ComponentUtil.redisService.set(requestModel.token, "1");
//                }
//            }
            // check校验数据
            did = HodgepodgeMethod.checkWxListData(requestModel);

            // 获取小微集合数据
            WxModel wxQuery = HodgepodgeMethod.assembleWxList(requestModel);
            List<WxModel> dataList = ComponentUtil.wxService.queryByList(wxQuery);
            // 组装返回客户端的数据
            long stime = System.currentTimeMillis();
            String sign = SignUtil.getSgin(stime, secretKeySign); // stime+秘钥=sign
            String strData = HodgepodgeMethod.assembleWxListResult(stime, sign, dataList, wxQuery.getRowCount());
            // 数据加密
            String encryptionData = StringUtil.mergeCodeBase64(strData);
            ResponseEncryptionJson resultDataModel = new ResponseEncryptionJson();
            resultDataModel.jsonData = encryptionData;
            // 返回数据给客户端
            return JsonResult.successResult(resultDataModel, cgid, sgid);
        }catch (Exception e){
            Map<String,String> map = ExceptionMethod.getException(e, ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_TWO);
            // #添加异常
            log.error(String.format("this WxController.getDataList() is error , the cgid=%s and sgid=%s and all data=%s!", cgid, sgid, data));
            if (!StringUtils.isBlank(map.get("dbCode"))){
                log.error(String.format("this WxController.getDataList() is error codeInfo, the dbCode=%s and dbMessage=%s !", map.get("dbCode"), map.get("dbMessage")));
            }
            e.printStackTrace();
            return JsonResult.failedResult(map.get("message"), map.get("code"), cgid, sgid);
        }
    }




    /**
     * @Description: 小微-详情
     * <p>
     *     1.查询小微集合
     *     2.查询此用户的收款账号ID集合，找出最新的那条小微与账号的关联关系的数据。
     *     3.判断小微ID是否正常正常，如果正常，则把此小微给出
     * </p>
     * @param request
     * @param response
     * @return com.gd.chain.common.utils.JsonResult<java.lang.Object>
     * @author yoko
     * @date 2019/11/25 22:58
     * local:http://localhost:8086/fine/wx/getData
     * 请求的属性类:RequestReward
     * 必填字段:{"agtVer":1,"clientVer":1,"clientType":1,"ctime":201911071802959,"cctime":201911071802959,"sign":"abcdefg","token":"111111"}
     * 加密字段:{"jsonData":"eyJhZ3RWZXIiOjEsImNsaWVudFZlciI6MSwiY2xpZW50VHlwZSI6MSwiY3RpbWUiOjIwMTkxMTA3MTgwMjk1OSwiY2N0aW1lIjoyMDE5MTEwNzE4MDI5NTksInNpZ24iOiJhYmNkZWZnIiwidG9rZW4iOiIxMTExMTEifQ=="}
     * 客户端加密字段:id+ctime+cctime+秘钥=sign
     * 服务端加密字段:stime+秘钥=sign
     * result={
     *     "resultCode": "0",
     *     "message": "success",
     *     "data": {
     *         "jsonData": "eyJkYXRhTW9kZWwiOnsiYWNOYW1lIjoieHh4eCIsInd4TmFtZSI6IuWknOepuiIsInd4UXJDb2RlIjoiIn0sInNpZ24iOiIzMDY2ZWNlNTRmYzZiZmZjYzE1M2RhZGQzOWI5ZjI4ZCIsInN0aW1lIjoxNTk2MTA4MTE5NTYzfQ=="
     *     },
     *     "sgid": "202007301920500000001",
     *     "cgid": ""
     * }
     */
/*
    @RequestMapping(value = "/getData", method = {RequestMethod.POST})
    public JsonResult<Object> getData(HttpServletRequest request, HttpServletResponse response, @RequestBody RequestEncryptionJson requestData) throws Exception{
        String sgid = ComponentUtil.redisIdService.getNewId();
        String cgid = "";
        String token;
        String ip = StringUtil.getIpAddress(request);
        String data = "";
        long did = 0;

        RequestWx requestModel = new RequestWx();
        try{
            // 解密
            data = StringUtil.decoderBase64(requestData.jsonData);
            requestModel  = JSON.parseObject(data, RequestWx.class);
            //#临时数据
//            if (!StringUtils.isBlank(requestModel.token)){
//                if (requestModel.token.equals("111111")){
//                    ComponentUtil.redisService.set(requestModel.token, "420");
//                }
//            }

            // check校验请求的数据
            did = HodgepodgeMethod.checkWxData(requestModel);

            WxModel dataModel = new WxModel();

            // redis中取出之前给出的小微
            String redis_wx = HodgepodgeMethod.getRedisDataByKey(CacheKey.WX_BY_DID, did);
            if (!StringUtils.isBlank(redis_wx)){
                // 之前已给过的小微-储存在缓存中的
                WxModel redis_wxModel = JSON.parseObject(redis_wx, WxModel.class);
                if (redis_wxModel != null && redis_wxModel.getId() > 0){
                    // 查询储存在缓存中的小微目前状态是否正常
                    WxModel wxQuery = HodgepodgeMethod.assembleWxByIdQuery(redis_wxModel.getId(), 1);
                    WxModel wxModel = (WxModel) ComponentUtil.wxService.findByObject(wxQuery);
                    if (wxModel != null && wxModel.getId() >0 ){
                        dataModel = wxModel;
                    }else {
                        // 说明之前的小微已经被暂停或者删除了
                        WxModel wxByQuery = HodgepodgeMethod.assembleWxByIsOkAndUseStatusQuery(1, 1, 1);
                        dataModel = ComponentUtil.wxService.screenWx(wxByQuery);
                    }

                }else {
                    // 缓存中的数据可能是脏数据
                    WxModel wxByQuery = HodgepodgeMethod.assembleWxByIsOkAndUseStatusQuery(1, 1, 1);
                    dataModel = ComponentUtil.wxService.screenWx(wxByQuery);
                }

            }else {
                // 查询此用户下属于微信群的最新的收款账号ID：包含yn=1的收款账号都要查询出来
                DidCollectionAccountModel didCollectionAccountQuery = HodgepodgeMethod.assembleDidCollectionAccountByDidAndAcTypeQuery(did, 3);
                DidCollectionAccountModel didCollectionAccountModel = ComponentUtil.didCollectionAccountService.getNewDidCollectionAccount(didCollectionAccountQuery);
                if (didCollectionAccountModel != null && didCollectionAccountModel.getId() > 0){
                    // 之前有加过我方小微，查找之前的小微信息
                    WxClerkModel wxClerkQuery = HodgepodgeMethod.assembleWxClerkByCollectionAccountQuery(didCollectionAccountModel.getId());
                    WxClerkModel wxClerkModel = ComponentUtil.wxClerkService.getWxClerk(wxClerkQuery);
                    if (wxClerkModel != null && wxClerkModel.getId() > 0){
                        // 之前有我方小微的关联关系
                        // 查询此小微是否是正常状态的小微
                        WxModel wxQuery = HodgepodgeMethod.assembleWxByIdQuery(wxClerkModel.getWxId(), 1);
                        WxModel wxModel = (WxModel) ComponentUtil.wxService.findByObject(wxQuery);
                        if (wxModel != null && wxModel.getId() > 0){
                            // 此小微是可以正式给出的小微
                            dataModel = wxModel;
                        }else{
                            // 代表之前的小微暂停使用或者已经被删除了；需要给出新的小微
                            WxModel wxByQuery = HodgepodgeMethod.assembleWxByIsOkAndUseStatusQuery(1, 1, 1);
                            dataModel = ComponentUtil.wxService.screenWx(wxByQuery);
                        }
                    }else{
                        // 之前没有与我方小微建立关联关系，需要给出新的小微
                        WxModel wxByQuery = HodgepodgeMethod.assembleWxByIsOkAndUseStatusQuery(1, 1, 1);
                        dataModel = ComponentUtil.wxService.screenWx(wxByQuery);
                    }

                }else {
                    // 之前没有加过我方小微，需要给出新的小微
                    WxModel wxByQuery = HodgepodgeMethod.assembleWxByIsOkAndUseStatusQuery(1, 1, 1);
                    dataModel = ComponentUtil.wxService.screenWx(wxByQuery);
                }
            }

            if (dataModel != null && dataModel.getId() > 0){
                // 储存到redis缓存中
                String strKeyCache = CachedKeyUtils.getCacheKey(CacheKey.WX_BY_DID, did);
                ComponentUtil.redisService.set(strKeyCache, JSON.toJSONString(dataModel, SerializerFeature.WriteMapNullValue, SerializerFeature.WriteNullStringAsEmpty), FIVE_HOUR, TimeUnit.HOURS);
            }

            // 组装返回客户端的数据
            long stime = System.currentTimeMillis();
            String sign = SignUtil.getSgin(stime, secretKeySign); // stime+秘钥=sign
            String strData = HodgepodgeMethod.assembleWxDataResult(stime, sign, dataModel);
            // 数据加密
            String encryptionData = StringUtil.mergeCodeBase64(strData);
            ResponseEncryptionJson resultDataModel = new ResponseEncryptionJson();
            resultDataModel.jsonData = encryptionData;
            // 返回数据给客户端
            return JsonResult.successResult(resultDataModel, cgid, sgid);
        }catch (Exception e){
            Map<String,String> map = ExceptionMethod.getException(e, ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_TWO);
            // 添加异常
            log.error(String.format("this WxController.getData() is error , the cgid=%s and sgid=%s and all data=%s!", cgid, sgid, data));
            if (!StringUtils.isBlank(map.get("dbCode"))){
                log.error(String.format("this WxController.getData() is error codeInfo, the dbCode=%s and dbMessage=%s !", map.get("dbCode"), map.get("dbMessage")));
            }
            e.printStackTrace();
            return JsonResult.failedResult(map.get("message"), map.get("code"), cgid, sgid);
        }
    }*/

}
