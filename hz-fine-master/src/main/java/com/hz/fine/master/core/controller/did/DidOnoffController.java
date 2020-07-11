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
import com.hz.fine.master.core.model.did.DidModel;
import com.hz.fine.master.core.model.did.DidOnoffModel;
import com.hz.fine.master.core.model.region.RegionModel;
import com.hz.fine.master.core.protocol.request.did.RequestDid;
import com.hz.fine.master.core.protocol.request.did.onoff.RequestDidOnoff;
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

/**
 * @Description 用户抢单上下线的Controller层
 * @Author yoko
 * @Date 2020/6/30 17:32
 * @Version 1.0
 */
@RestController
@RequestMapping("/fine/onoff")
public class DidOnoffController {

    private static Logger log = LoggerFactory.getLogger(DidOnoffController.class);

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
     * @Description: 用户抢单上下线
     * @param request
     * @param response
     * @return com.gd.chain.common.utils.JsonResult<java.lang.Object>
     * @author yoko
     * @date 2019/11/25 22:58
     * local:http://localhost:8086/fine/onoff/update
     * 请求的属性类:RequestDidOnoff
     * 必填字段:{"dataType":1,"agtVer":1,"clientVer":1,"clientType":1,"ctime":201911071802959,"cctime":201911071802959,"sign":"abcdefg","token":"111111"}
     * 加密字段:{"jsonData":"eyJkYXRhVHlwZSI6MSwiYWd0VmVyIjoxLCJjbGllbnRWZXIiOjEsImNsaWVudFR5cGUiOjEsImN0aW1lIjoyMDE5MTEwNzE4MDI5NTksImNjdGltZSI6MjAxOTExMDcxODAyOTU5LCJzaWduIjoiYWJjZGVmZyIsInRva2VuIjoiMTExMTExIn0="}
     * 客户端加密字段:acNum+ctime+秘钥=sign
     * 服务端加密字段:stime+秘钥=sign
     * result={
     *     "resultCode": "0",
     *     "message": "success",
     *     "data": {
     *         "jsonData": "eyJkYXRhTW9kZWwiOnsiZGF0YVR5cGUiOjF9LCJzaWduIjoiMjk4NjhhYjljNmNmNDUwZWU4OTZhNDU3YjAwZTAzZDgiLCJzdGltZSI6MTU5MzUyMTMyOTA0M30="
     *     },
     *     "sgid": "202006302048440000001",
     *     "cgid": ""
     * }
     */
    @RequestMapping(value = "/update", method = {RequestMethod.POST})
    public JsonResult<Object> update(HttpServletRequest request, HttpServletResponse response, @RequestBody RequestEncryptionJson requestData) throws Exception{
        String sgid = ComponentUtil.redisIdService.getNewId();
        String cgid = "";
        String token = "";
        String ip = StringUtil.getIpAddress(request);
        String data = "";
        long did = 0;
        RegionModel regionModel = HodgepodgeMethod.assembleRegionModel(ip);

        RequestDidOnoff requestModel = new RequestDidOnoff();
        try{
            // 解密
            data = StringUtil.decoderBase64(requestData.jsonData);
            requestModel  = JSON.parseObject(data, RequestDidOnoff.class);
            //#临时数据
//            if (!StringUtils.isBlank(requestModel.token)){
//                if (requestModel.token.equals("111111")){
//                    ComponentUtil.redisService.set(requestModel.token, "1");
//                }
//            }
            // check校验数据
            did = HodgepodgeMethod.checkOnoffUpdate(requestModel);

            // 先锁住这个用户
            String lockKey_did = CachedKeyUtils.getCacheKey(CacheKey.LOCK_DID_ONOFF, did);
            boolean flagLock_did = ComponentUtil.redisIdService.lock(lockKey_did);
            if (flagLock_did){
                // 判断用户是否在上下线的表中有数据
                DidOnoffModel didOnoffQuery = HodgepodgeMethod.assembleDidOnoffQueryByDid(did);
                DidOnoffModel didOnoffData = (DidOnoffModel) ComponentUtil.didOnoffService.findByObject(didOnoffQuery);
                if (didOnoffData == null || didOnoffData.getId() <= 0){
                    // 添加数据
                    ComponentUtil.didOnoffService.add(didOnoffQuery);
                }
                if (requestModel.dataType == 1){
                    // 下线（取消抢单）
                    DidOnoffModel didOnoffUpdate = HodgepodgeMethod.assembleDidOnoffUpdate(did, 1);
                    ComponentUtil.didOnoffService.updateDidOnoff(didOnoffUpdate);
                }else{
                    // 判断用户是否有余额可进行抢单
                    DidModel didQuery = HodgepodgeMethod.assembleDidQueryByDid(did);
                    DidModel didModel = (DidModel) ComponentUtil.didService.findByObject(didQuery);
                    HodgepodgeMethod.checkDidData(didModel);
                    HodgepodgeMethod.checkDidbalance(didModel.getBalance(), lockKey_did);

                    // 判断是否属于监听期间

                    // 判断连续有三个订单未交易成功

                    // 上线（开始抢单）
                    DidOnoffModel didOnoffUpdate = HodgepodgeMethod.assembleDidOnoffUpdate(did, 2);
                    ComponentUtil.didOnoffService.updateDidOnoff(didOnoffUpdate);

                }
                // 解锁
                ComponentUtil.redisIdService.delLock(lockKey_did);
            }else {
                throw new ServiceException("ONOFF01", "错误,请重试!");
            }

            // 组装返回客户端的数据
            long stime = System.currentTimeMillis();
            String sign = SignUtil.getSgin(stime, secretKeySign); // stime+秘钥=sign
            String strData = HodgepodgeMethod.assembleDidOnoffResult(stime, sign, requestModel.dataType);
            // 数据加密
            String encryptionData = StringUtil.mergeCodeBase64(strData);
            ResponseEncryptionJson resultDataModel = new ResponseEncryptionJson();
            resultDataModel.jsonData = encryptionData;
            // 返回数据给客户端
            return JsonResult.successResult(resultDataModel, cgid, sgid);
        }catch (Exception e){
            Map<String,String> map = ExceptionMethod.getException(e, ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_TWO);
            log.error(String.format("this DidOnoffController.update() is error , the cgid=%s and sgid=%s and all data=%s!", cgid, sgid, data));
            if (!StringUtils.isBlank(map.get("dbCode"))){
                log.error(String.format("this DidOnoffController.update() is error codeInfo, the dbCode=%s and dbMessage=%s !", map.get("dbCode"), map.get("dbMessage")));
            }
            e.printStackTrace();
            return JsonResult.failedResult(map.get("message"), map.get("code"), cgid, sgid);
        }
    }


    /**
     * @Description: 获取用户抢单上下线的状态
     * @param request
     * @param response
     * @return com.gd.chain.common.utils.JsonResult<java.lang.Object>
     * @author yoko
     * @date 2019/11/25 22:58
     * local:http://localhost:8086/fine/onoff/getData
     * 请求的属性类:RequestDidOnoff
     * 必填字段:{"agtVer":1,"clientVer":1,"clientType":1,"ctime":201911071802959,"cctime":201911071802959,"sign":"abcdefg","token":"111111"}
     * 加密字段:{"jsonData":"eyJhZ3RWZXIiOjEsImNsaWVudFZlciI6MSwiY2xpZW50VHlwZSI6MSwiY3RpbWUiOjIwMTkxMTA3MTgwMjk1OSwiY2N0aW1lIjoyMDE5MTEwNzE4MDI5NTksInNpZ24iOiJhYmNkZWZnIiwidG9rZW4iOiIxMTExMTEifQ=="}
     * 客户端加密字段:acNum+ctime+秘钥=sign
     * 服务端加密字段:stime+秘钥=sign
     * result={
     *     "resultCode": "0",
     *     "message": "success",
     *     "data": {
     *         "jsonData": "eyJkYXRhTW9kZWwiOnsiZGF0YVR5cGUiOjJ9LCJzaWduIjoiNTRmMjk3MDM4YTU0N2I1MDgxNGVjNjcxNjE0Yjk4ZmMiLCJzdGltZSI6MTU5MzUyMTkzMTk5OX0="
     *     },
     *     "sgid": "202006302058510000001",
     *     "cgid": ""
     * }
     */
    @RequestMapping(value = "/getData", method = {RequestMethod.POST})
    public JsonResult<Object> getData(HttpServletRequest request, HttpServletResponse response, @RequestBody RequestEncryptionJson requestData) throws Exception{
        String sgid = ComponentUtil.redisIdService.getNewId();
        String cgid = "";
        String token = "";
        String ip = StringUtil.getIpAddress(request);
        String data = "";
        long did = 0;
        RegionModel regionModel = HodgepodgeMethod.assembleRegionModel(ip);

        RequestDidOnoff requestModel = new RequestDidOnoff();
        try{
            // 解密
            data = StringUtil.decoderBase64(requestData.jsonData);
            requestModel  = JSON.parseObject(data, RequestDidOnoff.class);
            //#临时数据
//            if (!StringUtils.isBlank(requestModel.token)){
//                if (requestModel.token.equals("111111")){
//                    ComponentUtil.redisService.set(requestModel.token, "1");
//                }
//            }
            // check校验数据
            did = HodgepodgeMethod.checkOnoffData(requestModel);

            int dataType = 0;
            // 先锁住这个用户
            String lockKey_did = CachedKeyUtils.getCacheKey(CacheKey.LOCK_DID_ONOFF, did);
            boolean flagLock_did = ComponentUtil.redisIdService.lock(lockKey_did);
            if (flagLock_did){
                // 判断用户是否在上下线的表中有数据
                DidOnoffModel didOnoffQuery = HodgepodgeMethod.assembleDidOnoffQueryByDid(did);
                DidOnoffModel didOnoffData = (DidOnoffModel) ComponentUtil.didOnoffService.findByObject(didOnoffQuery);
                if (didOnoffData == null || didOnoffData.getId() <= 0){
                    // 添加数据
                    ComponentUtil.didOnoffService.add(didOnoffQuery);
                    dataType = 1;
                }else{
                    dataType = didOnoffData.getDataType();
                }
                // 解锁
                ComponentUtil.redisIdService.delLock(lockKey_did);
            }else {
                throw new ServiceException("ONOFF01", "错误,请重试!");
            }

            // 组装返回客户端的数据
            long stime = System.currentTimeMillis();
            String sign = SignUtil.getSgin(stime, secretKeySign); // stime+秘钥=sign
            String strData = HodgepodgeMethod.assembleDidOnoffResult(stime, sign, dataType);
            // 数据加密
            String encryptionData = StringUtil.mergeCodeBase64(strData);
            ResponseEncryptionJson resultDataModel = new ResponseEncryptionJson();
            resultDataModel.jsonData = encryptionData;
            // 返回数据给客户端
            return JsonResult.successResult(resultDataModel, cgid, sgid);
        }catch (Exception e){
            Map<String,String> map = ExceptionMethod.getException(e, ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_TWO);
            log.error(String.format("this DidOnoffController.getData() is error , the cgid=%s and sgid=%s and all data=%s!", cgid, sgid, data));
            if (!StringUtils.isBlank(map.get("dbCode"))){
                log.error(String.format("this DidOnoffController.getData() is error codeInfo, the dbCode=%s and dbMessage=%s !", map.get("dbCode"), map.get("dbMessage")));
            }
            e.printStackTrace();
            return JsonResult.failedResult(map.get("message"), map.get("code"), cgid, sgid);
        }
    }
}
