package com.hz.fine.master.core.controller.did;

import com.alibaba.fastjson.JSON;
import com.hz.fine.master.core.common.exception.ExceptionMethod;
import com.hz.fine.master.core.common.utils.BeanUtils;
import com.hz.fine.master.core.common.utils.JsonResult;
import com.hz.fine.master.core.common.utils.SignUtil;
import com.hz.fine.master.core.common.utils.StringUtil;
import com.hz.fine.master.core.common.utils.constant.ServerConstant;
import com.hz.fine.master.core.model.RequestEncryptionJson;
import com.hz.fine.master.core.model.ResponseEncryptionJson;
import com.hz.fine.master.core.model.did.DidCollectionAccountModel;
import com.hz.fine.master.core.model.did.DidCollectionAccountQrCodeModel;
import com.hz.fine.master.core.model.did.DidModel;
import com.hz.fine.master.core.model.region.RegionModel;
import com.hz.fine.master.core.protocol.request.did.RequestDidCollectionAccount;
import com.hz.fine.master.core.protocol.request.did.qrcode.RequestDidCollectionAccountQrCode;
import com.hz.fine.master.core.protocol.response.did.collectionaccount.DidCollectionAccount;
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
 * @Description 用户的收款账号二维码的Controller层
 * @Author yoko
 * @Date 2020/6/17 14:50
 * @Version 1.0
 */
@RestController
@RequestMapping("/fine/collAcQrCode")
public class DidCollectionAccountQrCodeController {

    private static Logger log = LoggerFactory.getLogger(DidCollectionAccountController.class);

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
     * @Description: 用户新增收款账号的二维码
     * @param request
     * @param response
     * @return com.gd.chain.common.utils.JsonResult<java.lang.Object>
     * @author yoko
     * @date 2019/11/25 22:58
     * local:http://localhost:8086/fine/collAcQrCode/add
     * 请求的属性类:RequestDid
     * 必填字段:{"acName":"acName1","acType":1,"acNum":"acNum1","mmQrCode":"mmQrCode1","payee":"payee1","bankName":"bankName1","businessType":1,"wxQrCodeAds":"wxQrCodeAds1","agtVer":1,"clientVer":1,"clientType":1,"ctime":201911071802959,"cctime":201911071802959,"sign":"abcdefg","token":"111111"}
     * 加密字段:{"jsonData":"eyJhY05hbWUiOiJhY05hbWUxIiwiYWNUeXBlIjoxLCJhY051bSI6ImFjTnVtMSIsIm1tUXJDb2RlIjoibW1RckNvZGUxIiwicGF5ZWUiOiJwYXllZTEiLCJiYW5rTmFtZSI6ImJhbmtOYW1lMSIsImJ1c2luZXNzVHlwZSI6MSwid3hRckNvZGVBZHMiOiJ3eFFyQ29kZUFkczEiLCJhZ3RWZXIiOjEsImNsaWVudFZlciI6MSwiY2xpZW50VHlwZSI6MSwiY3RpbWUiOjIwMTkxMTA3MTgwMjk1OSwiY2N0aW1lIjoyMDE5MTEwNzE4MDI5NTksInNpZ24iOiJhYmNkZWZnIiwidG9rZW4iOiIxMTExMTEifQ=="}
     * 客户端加密字段:ctime+cctime+秘钥=sign
     * 服务端加密字段:stime+秘钥=sign
     * result={
     *     "resultCode": "0",
     *     "message": "success",
     *     "data": {
     *         "jsonData": "eyJzaWduIjoiNDgzNTAxMWJiZjdkYzc2YWQ3ZmYwYWVkZWFiMDRhZDAiLCJzdGltZSI6MTU4OTUzMTIyNDg0OX0="
     *     },
     *     "sgid": "202005151627040000001",
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

        RequestDidCollectionAccountQrCode requestModel = new RequestDidCollectionAccountQrCode();
        try{
            // 解密
            data = StringUtil.decoderBase64(requestData.jsonData);
            requestModel  = JSON.parseObject(data, RequestDidCollectionAccountQrCode.class);

            //#临时数据
//            if (!StringUtils.isBlank(requestModel.token)){
//                if (requestModel.token.equals("111111")){
//                    ComponentUtil.redisService.set(requestModel.token, "1");
//                }
//            }

            // check校验数据
            did = HodgepodgeMethod.checkDidCollectionAccountQrCodeAddData(requestModel);

//            // 判断用户是否充值过：只有充值过的用户才能进行收款账号的添加
//            DidModel didQuery = HodgepodgeMethod.assembleDidQuery(did);
//            DidModel didModel = (DidModel) ComponentUtil.didService.findByObject(didQuery);
//            HodgepodgeMethod.checkDidInfo(didModel);
//

            // 校验这个用户账号下是否有这个收款账号
//            // 组装要录入的用户收款账号信息
//            DidCollectionAccountModel addData = HodgepodgeMethod.assembleDidCollectionAccount(requestModel, did);
//            ComponentUtil.didCollectionAccountService.add(addData);
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
            log.error(String.format("this DidCollectionAccountQrCodeController.add() is error , the cgid=%s and sgid=%s and all data=%s!", cgid, sgid, data));
            if (!StringUtils.isBlank(map.get("dbCode"))){
                log.error(String.format("this DidCollectionAccountQrCodeController.add() is error codeInfo, the dbCode=%s and dbMessage=%s !", map.get("dbCode"), map.get("dbMessage")));
            }
            e.printStackTrace();
            return JsonResult.failedResult(map.get("message"), map.get("code"), cgid, sgid);
        }
    }



    /**
     * @Description: 用户获取收款账号的二维码信息-集合
     * @param request
     * @param response
     * @return com.gd.chain.common.utils.JsonResult<java.lang.Object>
     * @author yoko
     * @date 2019/11/25 22:58
     * local:http://localhost:8086/fine/collAcQrCode/getDataList
     * 请求的属性类:RequestAppeal
     * 必填字段:{"collectionAccountId":37,"agtVer":1,"clientVer":1,"clientType":1,"ctime":201911071802959,"cctime":201911071802959,"sign":"abcdefg","pageNumber":1,"pageSize":3,"token":"111111"}
     * 加密字段:{"jsonData":"eyJjb2xsZWN0aW9uQWNjb3VudElkIjozNywiYWd0VmVyIjoxLCJjbGllbnRWZXIiOjEsImNsaWVudFR5cGUiOjEsImN0aW1lIjoyMDE5MTEwNzE4MDI5NTksImNjdGltZSI6MjAxOTExMDcxODAyOTU5LCJzaWduIjoiYWJjZGVmZyIsInBhZ2VOdW1iZXIiOjEsInBhZ2VTaXplIjozLCJ0b2tlbiI6IjExMTExMSJ9"}
     * 客户端加密字段:ctime+秘钥=sign
     * 返回加密字段:stime+秘钥=sign
     * result={
     *     "resultCode": "0",
     *     "message": "success",
     *     "data": {
     *         "jsonData": "eyJkYXRhTGlzdCI6W3siYWxpYXMiOiJhbGlhc18xIiwiZGF0YVR5cGUiOjEsImRkUXJDb2RlIjoiZGRRckNvZGVfMSIsImlkIjoxLCJpc0xpbWl0TnVtIjowLCJsaW1pdE51bSI6MTAsIm1tUXJDb2RlIjoiIiwicXJDb2RlTW9uZXkiOiIifSx7ImFsaWFzIjoiYWxpYXNfMiIsImRhdGFUeXBlIjoyLCJkZFFyQ29kZSI6ImRkUXJDb2RlXzIiLCJpZCI6MiwiaXNMaW1pdE51bSI6MCwibGltaXROdW0iOjIwLCJtbVFyQ29kZSI6IiIsInFyQ29kZU1vbmV5IjoiIn0seyJhbGlhcyI6ImFsaWFzXzMiLCJkYXRhVHlwZSI6MywiZGRRckNvZGUiOiJkZFFyQ29kZV8zIiwiaWQiOjMsImlzTGltaXROdW0iOjAsImxpbWl0TnVtIjozMCwibW1RckNvZGUiOiIiLCJxckNvZGVNb25leSI6IiJ9XSwic2lnbiI6IjhjMGIzY2RjN2Y3Mjk1YjM2NDM4MmNkMTkwODVkNDg0Iiwic3RpbWUiOjE1OTIzOTg4Njc5NjZ9"
     *     },
     *     "sgid": "202006172101060000001",
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

        RequestDidCollectionAccountQrCode requestModel = new RequestDidCollectionAccountQrCode();
        try{
            // 解密
            data = StringUtil.decoderBase64(requestData.jsonData);
            requestModel  = JSON.parseObject(data, RequestDidCollectionAccountQrCode.class);
            //#临时数据
//            if (!StringUtils.isBlank(requestModel.token)){
//                if (requestModel.token.equals("111111")){
//                    ComponentUtil.redisService.set(requestModel.token, "1");
//                }
//            }
            // check校验数据
            did = HodgepodgeMethod.checkDidCollectionAccountQrCodeListData(requestModel);

            // 校验这个用户账号下是否有这个收款账号
            DidCollectionAccountModel didCollectionAccountQuery = HodgepodgeMethod.assembleDidCollectionAccountQueryByDid(did, requestModel.collectionAccountId);
            DidCollectionAccountModel didCollectionAccountModel = (DidCollectionAccountModel) ComponentUtil.didCollectionAccountService.findByObject(didCollectionAccountQuery);
            HodgepodgeMethod.checkDidCollectionAccountById(didCollectionAccountModel);

            // 获取用户收款账号的收款二维码集合集合数据
            DidCollectionAccountQrCodeModel didCollectionAccountQrCodeQuery = HodgepodgeMethod.assembleDidCollectionAccountQrCodeQuery(requestModel);
            List<DidCollectionAccountQrCodeModel> didCollectionAccountQrCodeList = ComponentUtil.didCollectionAccountQrCodeService.queryByList(didCollectionAccountQrCodeQuery);

            // 组装返回客户端的数据
            long stime = System.currentTimeMillis();
            String sign = SignUtil.getSgin(stime, secretKeySign); // stime+秘钥=sign
            String strData = HodgepodgeMethod.assembleDidCollectionAccountQrCodeListResult(stime, sign, didCollectionAccountQrCodeList, didCollectionAccountQuery.getRowCount());
            // 数据加密
            String encryptionData = StringUtil.mergeCodeBase64(strData);
            ResponseEncryptionJson resultDataModel = new ResponseEncryptionJson();
            resultDataModel.jsonData = encryptionData;
            // 返回数据给客户端
            return JsonResult.successResult(resultDataModel, cgid, sgid);
        }catch (Exception e){
            Map<String,String> map = ExceptionMethod.getException(e, ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_TWO);
            // #添加异常
            log.error(String.format("this DidCollectionAccountQrCodeController.getDataList() is error , the cgid=%s and sgid=%s and all data=%s!", cgid, sgid, data));
            if (!StringUtils.isBlank(map.get("dbCode"))){
                log.error(String.format("this DidCollectionAccountQrCodeController.getDataList() is error codeInfo, the dbCode=%s and dbMessage=%s !", map.get("dbCode"), map.get("dbMessage")));
            }
            e.printStackTrace();
            return JsonResult.failedResult(map.get("message"), map.get("code"), cgid, sgid);
        }
    }




    /**
     * @Description: 获取收款账号的二维码数据-详情
     * @param request
     * @param response
     * @return com.gd.chain.common.utils.JsonResult<java.lang.Object>
     * @author yoko
     * @date 2019/11/25 22:58
     * local:http://localhost:8086/fine/collAcQrCode/getData
     * 请求的属性类:RequestAppeal
     * 必填字段:{"id":1,"agtVer":1,"clientVer":1,"clientType":1,"ctime":201911071802959,"cctime":201911071802959,"sign":"abcdefg","token":"111111"}
     * 加密字段:{"jsonData":"eyJpZCI6MSwiYWd0VmVyIjoxLCJjbGllbnRWZXIiOjEsImNsaWVudFR5cGUiOjEsImN0aW1lIjoyMDE5MTEwNzE4MDI5NTksImNjdGltZSI6MjAxOTExMDcxODAyOTU5LCJzaWduIjoiYWJjZGVmZyIsInRva2VuIjoiMTExMTExIn0="}
     * 客户端加密字段:id+ctime+cctime+秘钥=sign
     * 服务端加密字段:stime+秘钥=sign
     * result={
     *     "resultCode": "0",
     *     "message": "success",
     *     "data": {
     *         "jsonData": "eyJkYXRhTW9kZWwiOnsiYWxpYXMiOiJhbGlhc18xIiwiZGF0YVR5cGUiOjEsImRkUXJDb2RlIjoiZGRRckNvZGVfMSIsImlkIjoxLCJpc0xpbWl0TnVtIjowLCJsaW1pdE51bSI6MTAsIm1tUXJDb2RlIjoiIiwicXJDb2RlTW9uZXkiOiIifSwic2lnbiI6ImQzOTQ3YjkxYmM3YWQ1ZDk2NDY4NTczOTM5ZDljNmQ2Iiwic3RpbWUiOjE1OTI0MDAyODkyNzR9"
     *     },
     *     "sgid": "202006172124470000001",
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

        RequestDidCollectionAccountQrCode requestModel = new RequestDidCollectionAccountQrCode();
        try{
            // 解密
            data = StringUtil.decoderBase64(requestData.jsonData);
            requestModel  = JSON.parseObject(data, RequestDidCollectionAccountQrCode.class);
            //#临时数据
//            if (!StringUtils.isBlank(requestModel.token)){
//                if (requestModel.token.equals("111111")){
//                    ComponentUtil.redisService.set(requestModel.token, "1");
//                }
//            }
            // check校验请求的数据
            did = HodgepodgeMethod.checkDidCollectionAccountQrCodeData(requestModel);

            // 收款账号二维码的详情数据
            DidCollectionAccountQrCodeModel didCollectionAccountQrCodeQuery = HodgepodgeMethod.assembleDidCollectionAccountQrCodeById(requestModel.id);
            DidCollectionAccountQrCodeModel didCollectionAccountQrCodeData = (DidCollectionAccountQrCodeModel) ComponentUtil.didCollectionAccountQrCodeService.findByObject(didCollectionAccountQrCodeQuery);
            // 组装返回客户端的数据
            long stime = System.currentTimeMillis();
            String sign = SignUtil.getSgin(stime, secretKeySign); // stime+秘钥=sign
            String strData = HodgepodgeMethod.assembleDidCollectionAccountDataResult(stime, sign, didCollectionAccountQrCodeData);
            // 数据加密
            String encryptionData = StringUtil.mergeCodeBase64(strData);
            ResponseEncryptionJson resultDataModel = new ResponseEncryptionJson();
            resultDataModel.jsonData = encryptionData;
            // 返回数据给客户端
            return JsonResult.successResult(resultDataModel, cgid, sgid);
        }catch (Exception e){
            Map<String,String> map = ExceptionMethod.getException(e, ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_TWO);
            // 添加异常
            log.error(String.format("this DidCollectionAccountQrCodeController.getData() is error , the cgid=%s and sgid=%s and all data=%s!", cgid, sgid, data));
            if (!StringUtils.isBlank(map.get("dbCode"))){
                log.error(String.format("this DidCollectionAccountQrCodeController.getData() is error codeInfo, the dbCode=%s and dbMessage=%s !", map.get("dbCode"), map.get("dbMessage")));
            }
            e.printStackTrace();
            return JsonResult.failedResult(map.get("message"), map.get("code"), cgid, sgid);
        }
    }




    /**
     * @Description: 用户更新收款账号的二维码信息
     * @param request
     * @param response
     * @return com.gd.chain.common.utils.JsonResult<java.lang.Object>
     * @author yoko
     * @date 2019/11/25 22:58
     * local:http://localhost:8086/fine/collAcQrCode/update
     * 请求的属性类:RequestDid
     * 必填字段:{"id":1,"alias":"alias_1_1","ddQrCode":"ddQrCode_1_1","dataType":2,"limitNum":20,"agtVer":1,"clientVer":1,"clientType":1,"ctime":201911071802959,"cctime":201911071802959,"sign":"abcdefg","token":"111111"}
     * 加密字段:{"jsonData":"eyJpZCI6MSwiYWNOYW1lIjoiYWNOYW1lMTEiLCJhZ3RWZXIiOjEsImNsaWVudFZlciI6MSwiY2xpZW50VHlwZSI6MSwiY3RpbWUiOjIwMTkxMTA3MTgwMjk1OSwiY2N0aW1lIjoyMDE5MTEwNzE4MDI5NTksInNpZ24iOiJhYmNkZWZnIiwidG9rZW4iOiIxMTExMTEifQ=="}
     * 客户端加密字段:ctime+cctime+秘钥=sign
     * 服务端加密字段:stime+秘钥=sign
     * result={
     *     "resultCode": "0",
     *     "message": "success",
     *     "data": {
     *         "jsonData": "eyJzaWduIjoiZTI3MjI3YjJiYmEzNzg5ZWI0MGM4OTc5ZjQ0YTllZGIiLCJzdGltZSI6MTU4OTc4NTc1MzY1NX0="
     *     },
     *     "sgid": "202005181509120000001",
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

        RequestDidCollectionAccountQrCode requestModel = new RequestDidCollectionAccountQrCode();
        try{
            // 解密
            data = StringUtil.decoderBase64(requestData.jsonData);
            requestModel  = JSON.parseObject(data, RequestDidCollectionAccountQrCode.class);

            //#临时数据
//            if (!StringUtils.isBlank(requestModel.token)){
//                if (requestModel.token.equals("111111")){
//                    ComponentUtil.redisService.set(requestModel.token, "1");
//                }
//            }

            // check校验数据
            did = HodgepodgeMethod.checRequestDidCollectionAccountQrCodeUpdate(requestModel);

            // 组装要更新的数据进行更新
            DidCollectionAccountQrCodeModel didCollectionAccountQrCodeModelUpdate = BeanUtils.copy(requestModel, DidCollectionAccountQrCodeModel.class);
            ComponentUtil.didCollectionAccountQrCodeService.update(didCollectionAccountQrCodeModelUpdate);

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
            log.error(String.format("this DidCollectionAccountQrCodeController.update() is error , the cgid=%s and sgid=%s and all data=%s!", cgid, sgid, data));
            if (!StringUtils.isBlank(map.get("dbCode"))){
                log.error(String.format("this DidCollectionAccountQrCodeController.update() is error codeInfo, the dbCode=%s and dbMessage=%s !", map.get("dbCode"), map.get("dbMessage")));
            }
            e.printStackTrace();
            return JsonResult.failedResult(map.get("message"), map.get("code"), cgid, sgid);
        }
    }






    /**
     * @Description: 修改用户收款账号二维码的使用状态
     * <p>
     *     用户可以对收款账号二维码进行：暂停使用，恢复成正常使用，删除收款账号这三个动作的操作
     * </p>
     * @param request
     * @param response
     * @return com.gd.chain.common.utils.JsonResult<java.lang.Object>
     * @author yoko
     * @date 2019/11/25 22:58
     * local:http://localhost:8086/fine/collAcQrCode/updateUse
     * 请求的属性类:RequestDid
     * 必填字段:{"id":1,"useStatus":2,"yn":1,"agtVer":1,"clientVer":1,"clientType":1,"ctime":201911071802959,"cctime":201911071802959,"sign":"abcdefg","token":"111111"}
     * 加密字段:{"jsonData":"eyJpZCI6MSwidXNlU3RhdHVzIjoyLCJ5biI6MSwiYWd0VmVyIjoxLCJjbGllbnRWZXIiOjEsImNsaWVudFR5cGUiOjEsImN0aW1lIjoyMDE5MTEwNzE4MDI5NTksImNjdGltZSI6MjAxOTExMDcxODAyOTU5LCJzaWduIjoiYWJjZGVmZyIsInRva2VuIjoiMTExMTExIn0="}
     * 客户端加密字段:ctime+cctime+秘钥=sign
     * 服务端加密字段:stime+秘钥=sign
     * result={
     *     "resultCode": "0",
     *     "message": "success",
     *     "data": {
     *         "jsonData": "eyJzaWduIjoiMWM5ZTA3MjNkYmU5ZTM4NDQ3NWYyZTA4MDU1ZTEyMjQiLCJzdGltZSI6MTU4OTc5MTYyMzY1Nn0="
     *     },
     *     "sgid": "202005181647030000001",
     *     "cgid": ""
     * }
     */
    @RequestMapping(value = "/updateUse", method = {RequestMethod.POST})
    public JsonResult<Object> updateUse(HttpServletRequest request, HttpServletResponse response, @RequestBody RequestEncryptionJson requestData) throws Exception {
        String sgid = ComponentUtil.redisIdService.getNewId();
        String cgid = "";
        String token = "";
        String ip = StringUtil.getIpAddress(request);
        String data = "";
        long did = 0;
        RegionModel regionModel = HodgepodgeMethod.assembleRegionModel(ip);

        RequestDidCollectionAccount requestModel = new RequestDidCollectionAccount();
        try {
            // 解密
            data = StringUtil.decoderBase64(requestData.jsonData);
            requestModel = JSON.parseObject(data, RequestDidCollectionAccount.class);

            //#临时数据
//            if (!StringUtils.isBlank(requestModel.token)){
//                if (requestModel.token.equals("111111")){
//                    ComponentUtil.redisService.set(requestModel.token, "1");
//                }
//            }

            // check校验数据
            did = HodgepodgeMethod.checkDidCollectionAccountUpdateUseData(requestModel);

            // 组装要更新的数据进行更新
            DidCollectionAccountModel didCollectionAccountUpdate = HodgepodgeMethod.assembleDidCollectionAccountUpdateUse(did, requestModel);
            ComponentUtil.didCollectionAccountService.manyOperation(didCollectionAccountUpdate);

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
        } catch (Exception e) {
            Map<String, String> map = ExceptionMethod.getException(e, ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_TWO);
            log.error(String.format("this DidCollectionAccountController.updateUse() is error , the cgid=%s and sgid=%s and all data=%s!", cgid, sgid, data));
            if (!StringUtils.isBlank(map.get("dbCode"))){
                log.error(String.format("this DidCollectionAccountController.updateUse() is error codeInfo, the dbCode=%s and dbMessage=%s !", map.get("dbCode"), map.get("dbMessage")));
            }
            e.printStackTrace();
            return JsonResult.failedResult(map.get("message"), map.get("code"), cgid, sgid);
        }

    }



    /**
     * @Description: 用户获取未审核的收款账号信息-集合
     * <p>
     *     通过小微下线，导致收款账号重新审核的收款账号
     * </p>
     * @param request
     * @param response
     * @return com.gd.chain.common.utils.JsonResult<java.lang.Object>
     * @author yoko
     * @date 2019/11/25 22:58
     * local:http://localhost:8086/fine/collAc/getNoCheckDataList
     * 请求的属性类:RequestAppeal
     * 必填字段:{"agtVer":1,"clientVer":1,"clientType":1,"ctime":201911071802959,"cctime":201911071802959,"sign":"abcdefg","pageNumber":1,"pageSize":3,"token":"111111"}
     * 加密字段:{"jsonData":"eyJhZ3RWZXIiOjEsImNsaWVudFZlciI6MSwiY2xpZW50VHlwZSI6MSwiY3RpbWUiOjIwMTkxMTA3MTgwMjk1OSwiY2N0aW1lIjoyMDE5MTEwNzE4MDI5NTksInNpZ24iOiJhYmNkZWZnIiwicGFnZU51bWJlciI6MSwicGFnZVNpemUiOjMsInRva2VuIjoiMTExMTExIn0="}
     * 客户端加密字段:ctime+秘钥=sign
     * 返回加密字段:stime+秘钥=sign
     * result={
     *     "resultCode": "0",
     *     "message": "success",
     *     "data": {
     *         "jsonData": "eyJkYXRhTGlzdCI6W3siYWNOYW1lIjoiYWNOYW1lMSIsImFjTnVtIjoiYWNOdW0xIiwiYWNUeXBlIjoxLCJiYW5rTmFtZSI6ImJhbmtOYW1lMSIsImJ1c2luZXNzVHlwZSI6MSwiY2hlY2tJbmZvIjoiIiwiY2hlY2tTdGF0dXMiOjEsImRheVN3aXRjaCI6MSwiaWQiOjEsIm1tUXJDb2RlIjoibW1RckNvZGUxIiwibW9udGhTd2l0Y2giOjEsInBheWVlIjoicGF5ZWUxIiwidG90YWxTd2l0Y2giOjEsInVzZVN0YXR1cyI6MSwid3hRckNvZGVBZHMiOiJ3eFFyQ29kZUFkczEifSx7ImFjTmFtZSI6ImFjTmFtZTIiLCJhY051bSI6ImFjTnVtMiIsImFjVHlwZSI6MSwiYmFua05hbWUiOiJiYW5rTmFtZTIiLCJidXNpbmVzc1R5cGUiOjEsImNoZWNrSW5mbyI6IiIsImNoZWNrU3RhdHVzIjoxLCJkYXlTd2l0Y2giOjEsImlkIjoyLCJtbVFyQ29kZSI6Im1tUXJDb2RlMiIsIm1vbnRoU3dpdGNoIjoxLCJwYXllZSI6InBheWVlMiIsInRvdGFsU3dpdGNoIjoxLCJ1c2VTdGF0dXMiOjEsInd4UXJDb2RlQWRzIjoid3hRckNvZGVBZHMyIn0seyJhY05hbWUiOiJhY05hbWUzIiwiYWNOdW0iOiJhY051bTMiLCJhY1R5cGUiOjEsImJhbmtOYW1lIjoiYmFua05hbWUzIiwiYnVzaW5lc3NUeXBlIjoxLCJjaGVja0luZm8iOiIiLCJjaGVja1N0YXR1cyI6MSwiZGF5U3dpdGNoIjoxLCJpZCI6MywibW1RckNvZGUiOiJtbVFyQ29kZTMiLCJtb250aFN3aXRjaCI6MSwicGF5ZWUiOiJwYXllZTMiLCJ0b3RhbFN3aXRjaCI6MSwidXNlU3RhdHVzIjoxLCJ3eFFyQ29kZUFkcyI6Ind4UXJDb2RlQWRzMyJ9XSwicm93Q291bnQiOjQsInNpZ24iOiJkNmMwOTRjOTM5MDc3NjY1YzNkNDQzZmMzNTEzYmIzOSIsInN0aW1lIjoxNTg5NzcyNTU1ODE0fQ=="
     *     },
     *     "sgid": "202005181129140000001",
     *     "cgid": ""
     * }
     */
    @RequestMapping(value = "/getNoCheckDataList", method = {RequestMethod.POST})
    public JsonResult<Object> getNoCheckDataList(HttpServletRequest request, HttpServletResponse response, @RequestBody RequestEncryptionJson requestData) throws Exception{
        String sgid = ComponentUtil.redisIdService.getNewId();
        String cgid = "";
        String ip = StringUtil.getIpAddress(request);
        String data = "";
        long did = 0;

        RequestDidCollectionAccount requestModel = new RequestDidCollectionAccount();
        try{
            // 解密
            data = StringUtil.decoderBase64(requestData.jsonData);
            requestModel  = JSON.parseObject(data, RequestDidCollectionAccount.class);
            //#临时数据
//            if (!StringUtils.isBlank(requestModel.token)){
//                if (requestModel.token.equals("111111")){
//                    ComponentUtil.redisService.set(requestModel.token, "1");
//                }
//            }
            // check校验数据
            did = HodgepodgeMethod.checkDidCollectionAccountListData(requestModel);

            // 获取用户收款账号集合数据
            DidCollectionAccountModel didCollectionAccountQuery = HodgepodgeMethod.assembleDidCollectionAccountListByDidAndCheck(requestModel, did, ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_ONE);
            List<DidCollectionAccountModel> didCollectionAccountList = ComponentUtil.didCollectionAccountService.queryByList(didCollectionAccountQuery);
            // 组装返回客户端的数据
            long stime = System.currentTimeMillis();
            String sign = SignUtil.getSgin(stime, secretKeySign); // stime+秘钥=sign
            String strData = HodgepodgeMethod.assembleDidCollectionAccountListResult(stime, sign, didCollectionAccountList, didCollectionAccountQuery.getRowCount());
            // 数据加密
            String encryptionData = StringUtil.mergeCodeBase64(strData);
            ResponseEncryptionJson resultDataModel = new ResponseEncryptionJson();
            resultDataModel.jsonData = encryptionData;
            // 返回数据给客户端
            return JsonResult.successResult(resultDataModel, cgid, sgid);
        }catch (Exception e){
            Map<String,String> map = ExceptionMethod.getException(e, ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_TWO);
            // #添加异常
            log.error(String.format("this DidCollectionAccountController.getNoCheckDataList() is error , the cgid=%s and sgid=%s and all data=%s!", cgid, sgid, data));
            if (!StringUtils.isBlank(map.get("dbCode"))){
                log.error(String.format("this DidCollectionAccountController.getNoCheckDataList() is error codeInfo, the dbCode=%s and dbMessage=%s !", map.get("dbCode"), map.get("dbMessage")));
            }
            e.printStackTrace();
            return JsonResult.failedResult(map.get("message"), map.get("code"), cgid, sgid);
        }
    }




    /**
     * @Description: 用户修改收款账号的小微二维码信息
     * <p>
     *     可更新的字段：wx_qr_code_ads
     *     更新以上字段中的字段，这个收款账号都需要进行重新审核
     *     #需要提醒用户慎重操作
     *
     * </p>
     * @param request
     * @param response
     * @return com.gd.chain.common.utils.JsonResult<java.lang.Object>
     * @author yoko
     * @date 2019/11/25 22:58
     * local:http://localhost:8086/fine/collAc/updateWxQrCode
     * 请求的属性类:RequestDid
     * 必填字段:{"id":1,"wxQrCodeAds":"wxQrCodeAds11","agtVer":1,"clientVer":1,"clientType":1,"ctime":201911071802959,"cctime":201911071802959,"sign":"abcdefg","token":"111111"}
     * 加密字段:{"jsonData":"eyJpZCI6MSwiYWNUeXBlIjoiMiIsImFjTnVtIjoiYWNOdW0xMSIsIm1tUXJDb2RlIjoibW1RckNvZGUxMSIsInBheWVlIjoicGF5ZWUxMSIsImJhbmtOYW1lIjoiYmFua05hbWUxMSIsImJ1c2luZXNzVHlwZSI6IjIiLCJ3eFFyQ29kZUFkcyI6Ind4UXJDb2RlQWRzMTEiLCJhZ3RWZXIiOjEsImNsaWVudFZlciI6MSwiY2xpZW50VHlwZSI6MSwiY3RpbWUiOjIwMTkxMTA3MTgwMjk1OSwiY2N0aW1lIjoyMDE5MTEwNzE4MDI5NTksInNpZ24iOiJhYmNkZWZnIiwidG9rZW4iOiIxMTExMTEifQ=="}
     * 客户端加密字段:ctime+cctime+秘钥=sign
     * 服务端加密字段:stime+秘钥=sign
     * result={
     *     "resultCode": "0",
     *     "message": "success",
     *     "data": {
     *         "jsonData": "eyJzaWduIjoiZjQyZDEwNjU4NzMzMzI2NDc5NmUxZTM5YjE4MWM5MWMiLCJzdGltZSI6MTU4OTc4ODcwOTY1NX0="
     *     },
     *     "sgid": "202005181558280000001",
     *     "cgid": ""
     * }
     */
    @RequestMapping(value = "/updateWxQrCode", method = {RequestMethod.POST})
    public JsonResult<Object> updateWxQrCode(HttpServletRequest request, HttpServletResponse response, @RequestBody RequestEncryptionJson requestData) throws Exception {
        String sgid = ComponentUtil.redisIdService.getNewId();
        String cgid = "";
        String token = "";
        String ip = StringUtil.getIpAddress(request);
        String data = "";
        long did = 0;
        RegionModel regionModel = HodgepodgeMethod.assembleRegionModel(ip);

        RequestDidCollectionAccount requestModel = new RequestDidCollectionAccount();
        try {
            // 解密
            data = StringUtil.decoderBase64(requestData.jsonData);
            requestModel = JSON.parseObject(data, RequestDidCollectionAccount.class);

            //#临时数据
//            if (!StringUtils.isBlank(requestModel.token)){
//                if (requestModel.token.equals("111111")){
//                    ComponentUtil.redisService.set(requestModel.token, "1");
//                }
//            }
            // check校验数据
            did = HodgepodgeMethod.checkDidCollectionAccountUpdateWxQrCodeData(requestModel);


            // 组装要更新的数据进行更新
            DidCollectionAccountModel didCollectionAccountUpdate = HodgepodgeMethod.assembleDidCollectionAccountUpdateWxQrCode(did, requestModel);
            ComponentUtil.didCollectionAccountService.updateDidCollectionAccount(didCollectionAccountUpdate);

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
        } catch (Exception e) {
            Map<String, String> map = ExceptionMethod.getException(e, ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_TWO);
            log.error(String.format("this DidCollectionAccountController.updateWxQrCode() is error , the cgid=%s and sgid=%s and all data=%s!", cgid, sgid, data));
            if (!StringUtils.isBlank(map.get("dbCode"))){
                log.error(String.format("this DidCollectionAccountController.updateWxQrCode() is error codeInfo, the dbCode=%s and dbMessage=%s !", map.get("dbCode"), map.get("dbMessage")));
            }
            e.printStackTrace();
            return JsonResult.failedResult(map.get("message"), map.get("code"), cgid, sgid);
        }

    }
}
