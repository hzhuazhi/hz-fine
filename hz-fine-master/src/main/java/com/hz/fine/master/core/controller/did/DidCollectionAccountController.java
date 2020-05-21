package com.hz.fine.master.core.controller.did;

import com.alibaba.fastjson.JSON;
import com.hz.fine.master.core.common.exception.ExceptionMethod;
import com.hz.fine.master.core.common.utils.JsonResult;
import com.hz.fine.master.core.common.utils.SignUtil;
import com.hz.fine.master.core.common.utils.StringUtil;
import com.hz.fine.master.core.common.utils.constant.ServerConstant;
import com.hz.fine.master.core.model.RequestEncryptionJson;
import com.hz.fine.master.core.model.ResponseEncryptionJson;
import com.hz.fine.master.core.model.did.DidCollectionAccountModel;
import com.hz.fine.master.core.model.did.DidLevelModel;
import com.hz.fine.master.core.model.did.DidModel;
import com.hz.fine.master.core.model.region.RegionModel;
import com.hz.fine.master.core.protocol.request.did.RequestDid;
import com.hz.fine.master.core.protocol.request.did.RequestDidCollectionAccount;
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
 * @Description 用户的收款账号的Controller
 * @Author yoko
 * @Date 2020/5/15 15:12
 * @Version 1.0
 */
@RestController
@RequestMapping("/fine/collAc")
public class DidCollectionAccountController {

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
     * @Description: 用户新增收款账号
     * @param request
     * @param response
     * @return com.gd.chain.common.utils.JsonResult<java.lang.Object>
     * @author yoko
     * @date 2019/11/25 22:58
     * local:http://localhost:8086/fine/collAc/add
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

        RequestDidCollectionAccount requestModel = new RequestDidCollectionAccount();
        try{
            // 解密
            data = StringUtil.decoderBase64(requestData.jsonData);
            requestModel  = JSON.parseObject(data, RequestDidCollectionAccount.class);

            //#临时数据
            ComponentUtil.redisService.set(requestModel.token, "1");

            // check校验数据
            did = HodgepodgeMethod.checkDidCollectionAccountAddData(requestModel);

            // 校验收款账号是否存在：收款账号只能存在唯一
            DidCollectionAccountModel didCollectionAccountByAcNumQuery = HodgepodgeMethod.assembleDidCollectionAccountByAcNum(requestModel.acNum);
            DidCollectionAccountModel didCollectionAccountByAcNumData = (DidCollectionAccountModel) ComponentUtil.didCollectionAccountService.findByObject(didCollectionAccountByAcNumQuery);
            // check校验收款具体账号是否已被录入过
            HodgepodgeMethod.checkDidCollectionAccountAddByAcNum(didCollectionAccountByAcNumData);

            // 组装要录入的用户收款账号信息
            DidCollectionAccountModel addData = HodgepodgeMethod.assembleDidCollectionAccount(requestModel, did);
            ComponentUtil.didCollectionAccountService.add(addData);
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
            log.error(String.format("this DidCollectionAccountController.add() is error , the cgid=%s and sgid=%s and all data=%s!", cgid, sgid, data));
            e.printStackTrace();
            return JsonResult.failedResult(map.get("message"), map.get("code"), cgid, sgid);
        }
    }



    /**
     * @Description: 用户获取收款账号信息-集合
     * @param request
     * @param response
     * @return com.gd.chain.common.utils.JsonResult<java.lang.Object>
     * @author yoko
     * @date 2019/11/25 22:58
     * local:http://localhost:8086/fine/collAc/getDataList
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
    @RequestMapping(value = "/getDataList", method = {RequestMethod.POST})
    public JsonResult<Object> getDataList(HttpServletRequest request, HttpServletResponse response, @RequestBody RequestEncryptionJson requestData) throws Exception{
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
            ComponentUtil.redisService.set(requestModel.token, "1");
            // check校验数据
            did = HodgepodgeMethod.checkDidCollectionAccountListData(requestModel);

            // 获取用户收款账号集合数据
            DidCollectionAccountModel didCollectionAccountQuery = HodgepodgeMethod.assembleDidCollectionAccountListByDid(requestModel, did);
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
            log.error(String.format("this DidCollectionAccountController.getDataList() is error , the cgid=%s and sgid=%s and all data=%s!", cgid, sgid, data));
            e.printStackTrace();
            return JsonResult.failedResult(map.get("message"), map.get("code"), cgid, sgid);
        }
    }




    /**
     * @Description: 获取收款账号数据-详情
     * @param request
     * @param response
     * @return com.gd.chain.common.utils.JsonResult<java.lang.Object>
     * @author yoko
     * @date 2019/11/25 22:58
     * local:http://localhost:8086/fine/collAc/getData
     * 请求的属性类:RequestAppeal
     * 必填字段:{"id":1,"agtVer":1,"clientVer":1,"clientType":1,"ctime":201911071802959,"cctime":201911071802959,"sign":"abcdefg","token":"111111"}
     * 加密字段:{"jsonData":"eyJpZCI6MSwiYWd0VmVyIjoxLCJjbGllbnRWZXIiOjEsImNsaWVudFR5cGUiOjEsImN0aW1lIjoyMDE5MTEwNzE4MDI5NTksImNjdGltZSI6MjAxOTExMDcxODAyOTU5LCJzaWduIjoiYWJjZGVmZyIsInRva2VuIjoiMTExMTExIn0="}
     * 客户端加密字段:id+ctime+cctime+秘钥=sign
     * 服务端加密字段:stime+秘钥=sign
     * result={
     *     "resultCode": "0",
     *     "message": "success",
     *     "data": {
     *         "jsonData": "eyJkYXRhTW9kZWwiOnsiYWNOYW1lIjoiYWNOYW1lMSIsImFjTnVtIjoiYWNOdW0xIiwiYWNUeXBlIjoxLCJiYW5rTmFtZSI6ImJhbmtOYW1lMSIsImJ1c2luZXNzVHlwZSI6MSwiY2hlY2tJbmZvIjoiIiwiY2hlY2tTdGF0dXMiOjEsImRheVN3aXRjaCI6MSwiaWQiOjEsIm1tUXJDb2RlIjoibW1RckNvZGUxIiwibW9udGhTd2l0Y2giOjEsInBheWVlIjoicGF5ZWUxIiwidG90YWxTd2l0Y2giOjEsInVzZVN0YXR1cyI6MSwid3hRckNvZGVBZHMiOiJ3eFFyQ29kZUFkczEifSwic2lnbiI6IjY5ZjViZGQxYTU0OGIxOTUxOGU0ZjNiZjA3ODgwOWU4Iiwic3RpbWUiOjE1ODk3NzM2MzYyNTB9"
     *     },
     *     "sgid": "202005181147160000001",
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

        RequestDidCollectionAccount requestModel = new RequestDidCollectionAccount();
        try{
            // 解密
            data = StringUtil.decoderBase64(requestData.jsonData);
            requestModel  = JSON.parseObject(data, RequestDidCollectionAccount.class);
            //#临时数据
            ComponentUtil.redisService.set(requestModel.token, "1");
            // check校验请求的数据
            did = HodgepodgeMethod.checkDidCollectionAccountData(requestModel);

            // 收款账号详情数据
            DidCollectionAccountModel didCollectionAccountQuery = HodgepodgeMethod.assembleDidCollectionAccountByDidAndId(did, requestModel.id);
            DidCollectionAccountModel didCollectionAccountData = (DidCollectionAccountModel) ComponentUtil.didCollectionAccountService.findByObject(didCollectionAccountQuery);
            // 组装返回客户端的数据
            long stime = System.currentTimeMillis();
            String sign = SignUtil.getSgin(stime, secretKeySign); // stime+秘钥=sign
            String strData = HodgepodgeMethod.assembleDidCollectionAccountDataResult(stime, sign, didCollectionAccountData);
            // 数据加密
            String encryptionData = StringUtil.mergeCodeBase64(strData);
            ResponseEncryptionJson resultDataModel = new ResponseEncryptionJson();
            resultDataModel.jsonData = encryptionData;
            // 返回数据给客户端
            return JsonResult.successResult(resultDataModel, cgid, sgid);
        }catch (Exception e){
            Map<String,String> map = ExceptionMethod.getException(e, ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_TWO);
            // 添加异常
            log.error(String.format("this DidCollectionAccountController.getData() is error , the cgid=%s and sgid=%s and all data=%s!", cgid, sgid, data));
            e.printStackTrace();
            return JsonResult.failedResult(map.get("message"), map.get("code"), cgid, sgid);
        }
    }




    /**
     * @Description: 用户修改收款账号的基本信息
     * <p>
     *     基本信息包括：1收款账号名称：用户备注使用=ac_name
     *     2经营范围类型=business_type（支付类型有疑义，目前暂定不做修改，只做保留在这里，因为这个字段可能放到重新审核中的字段）
     *
     * </p>
     * @param request
     * @param response
     * @return com.gd.chain.common.utils.JsonResult<java.lang.Object>
     * @author yoko
     * @date 2019/11/25 22:58
     * local:http://localhost:8086/fine/collAc/updateBasic
     * 请求的属性类:RequestDid
     * 必填字段:{"id":1,"acName":"acName11","agtVer":1,"clientVer":1,"clientType":1,"ctime":201911071802959,"cctime":201911071802959,"sign":"abcdefg","token":"111111"}
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
    @RequestMapping(value = "/updateBasic", method = {RequestMethod.POST})
    public JsonResult<Object> updateBasic(HttpServletRequest request, HttpServletResponse response, @RequestBody RequestEncryptionJson requestData) throws Exception{
        String sgid = ComponentUtil.redisIdService.getNewId();
        String cgid = "";
        String token = "";
        String ip = StringUtil.getIpAddress(request);
        String data = "";
        long did = 0;
        RegionModel regionModel = HodgepodgeMethod.assembleRegionModel(ip);

        RequestDidCollectionAccount requestModel = new RequestDidCollectionAccount();
        try{
            // 解密
            data = StringUtil.decoderBase64(requestData.jsonData);
            requestModel  = JSON.parseObject(data, RequestDidCollectionAccount.class);

            //#临时数据
            ComponentUtil.redisService.set(requestModel.token, "1");

            // check校验数据
            did = HodgepodgeMethod.checkDidCollectionAccountUpdateBasic(requestModel);


            // 组装要更新的数据进行更新
            DidCollectionAccountModel didCollectionAccountUpdate = HodgepodgeMethod.assembleDidCollectionAccountUpdateBasic(did, requestModel.id, requestModel.acName);
            ComponentUtil.didCollectionAccountService.updateBasic(didCollectionAccountUpdate);

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
            log.error(String.format("this DidCollectionAccountController.updateBasic() is error , the cgid=%s and sgid=%s and all data=%s!", cgid, sgid, data));
            e.printStackTrace();
            return JsonResult.failedResult(map.get("message"), map.get("code"), cgid, sgid);
        }
    }


    /**
     * @Description: 用户修改收款账号的信息
     * <p>
     *     可更新的字段：ac_type、ac_num、mm_qr_code、payee、bank_name、business_type、wx_qr_code_ads
     *     更新以上字段中的任何一个字段，这个收款账号都需要进行重新审核
     *     #需要提醒用户慎重操作
     *
     * </p>
     * @param request
     * @param response
     * @return com.gd.chain.common.utils.JsonResult<java.lang.Object>
     * @author yoko
     * @date 2019/11/25 22:58
     * local:http://localhost:8086/fine/collAc/update
     * 请求的属性类:RequestDid
     * 必填字段:{"id":1,"acType":"2","acNum":"acNum11","mmQrCode":"mmQrCode11","payee":"payee11","bankName":"bankName11","businessType":"2","wxQrCodeAds":"wxQrCodeAds11","agtVer":1,"clientVer":1,"clientType":1,"ctime":201911071802959,"cctime":201911071802959,"sign":"abcdefg","token":"111111"}
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
    @RequestMapping(value = "/update", method = {RequestMethod.POST})
    public JsonResult<Object> update(HttpServletRequest request, HttpServletResponse response, @RequestBody RequestEncryptionJson requestData) throws Exception {
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
            ComponentUtil.redisService.set(requestModel.token, "1");

            // check校验数据
            did = HodgepodgeMethod.checkDidCollectionAccountUpdateData(requestModel);


            // 组装要更新的数据进行更新
            DidCollectionAccountModel didCollectionAccountUpdate = HodgepodgeMethod.assembleDidCollectionAccountUpdate(did, requestModel);
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
            log.error(String.format("this DidCollectionAccountController.update() is error , the cgid=%s and sgid=%s and all data=%s!", cgid, sgid, data));
            e.printStackTrace();
            return JsonResult.failedResult(map.get("message"), map.get("code"), cgid, sgid);
        }

    }



    /**
     * @Description: 修改用户收款账号的使用状态
     * <p>
     *     用户可以对收款账号进行：暂停使用，回复成正常使用，删除收款账号这三个动作的操作
     *     #需要提醒用户，当派发订单中的时候分配了这个收款账号，正在进行中，建议不暂停使用或者删除
     * </p>
     * @param request
     * @param response
     * @return com.gd.chain.common.utils.JsonResult<java.lang.Object>
     * @author yoko
     * @date 2019/11/25 22:58
     * local:http://localhost:8086/fine/collAc/updateUse
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
            ComponentUtil.redisService.set(requestModel.token, "1");

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
            e.printStackTrace();
            return JsonResult.failedResult(map.get("message"), map.get("code"), cgid, sgid);
        }

    }





}
