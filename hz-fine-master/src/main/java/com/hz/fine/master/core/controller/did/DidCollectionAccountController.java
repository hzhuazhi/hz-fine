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
import com.hz.fine.master.core.model.did.DidCollectionAccountQrCodeModel;
import com.hz.fine.master.core.model.did.DidModel;
import com.hz.fine.master.core.model.region.RegionModel;
import com.hz.fine.master.core.model.strategy.StrategyModel;
import com.hz.fine.master.core.model.wx.WxClerkModel;
import com.hz.fine.master.core.model.wx.WxClerkUnboundModel;
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
import java.util.ArrayList;
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
     * 必填字段-支付宝:{"acName":"acName1","acType":2,"acNum":"acNum1","userId":"userId1","payee":"payee1","businessType":1,"agtVer":1,"clientVer":1,"clientType":1,"ctime":201911071802959,"cctime":201911071802959,"sign":"abcdefg","token":"111111"}
     * 必填字段-微信群:{"acType":3,"mmQrCode":"mmQrCode3","businessType":1,"agtVer":1,"clientVer":1,"clientType":1,"ctime":201911071802959,"cctime":201911071802959,"sign":"abcdefg","token":"111111"}
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
//            if (!StringUtils.isBlank(requestModel.token)){
//                if (requestModel.token.equals("111111")){
//                    ComponentUtil.redisService.set(requestModel.token, "1");
//                }
//            }

            // check校验数据
            did = HodgepodgeMethod.checkDidCollectionAccountAddData(requestModel);

            // 判断用户是否充值过：只有充值过的用户才能进行收款账号的添加
            DidModel didQuery = HodgepodgeMethod.assembleDidQuery(did);
            DidModel didModel = (DidModel) ComponentUtil.didService.findByObject(didQuery);
            HodgepodgeMethod.checkDidInfo(didModel);

            if (requestModel.acType == 1){
                // 校验收款账号是否存在：收款账号昵称只能存在唯一
                DidCollectionAccountModel didCollectionAccountByAcNumQuery = HodgepodgeMethod.assembleDidCollectionAccountByPayee(requestModel.payee);
                DidCollectionAccountModel didCollectionAccountByAcNumData = (DidCollectionAccountModel) ComponentUtil.didCollectionAccountService.findByObject(didCollectionAccountByAcNumQuery);
                // check校验收款具体账号是否已被录入过
                HodgepodgeMethod.checkDidCollectionAccountAddByAcNum(didCollectionAccountByAcNumData);
            }else if (requestModel.acType == 2){
                // 校验支付宝收款账号:收款账号只能存在唯一
                DidCollectionAccountModel didCollectionAccountByAcNumQuery = HodgepodgeMethod.assembleDidCollectionAccountByPayee(requestModel.acNum);
                DidCollectionAccountModel didCollectionAccountByAcNumData = (DidCollectionAccountModel) ComponentUtil.didCollectionAccountService.findByObject(didCollectionAccountByAcNumQuery);
                // check校验收款具体账号是否已被录入过
                HodgepodgeMethod.checkDidCollectionAccountAddByAcNum(didCollectionAccountByAcNumData);
            }else if (requestModel.acType == 3){
                // 校验微信群收款账号：一个用户只能有一个微信群
                DidCollectionAccountModel didCollectionAccountByAcTypeQuery = HodgepodgeMethod.assembleDidCollectionAccountByAcType(did, requestModel.acType);
                DidCollectionAccountModel didCollectionAccountByAcTypeData = (DidCollectionAccountModel) ComponentUtil.didCollectionAccountService.findByObject(didCollectionAccountByAcTypeQuery);
                // check校验收款具体账号是否已被录入过
                HodgepodgeMethod.checkDidCollectionAccountAddByAcType(didCollectionAccountByAcTypeData);
            }


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
            if (!StringUtils.isBlank(map.get("dbCode"))){
                log.error(String.format("this DidCollectionAccountController.add() is error codeInfo, the dbCode=%s and dbMessage=%s !", map.get("dbCode"), map.get("dbMessage")));
            }
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
     * 请求的属性类:RequestDidCollectionAccount
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
//            if (!StringUtils.isBlank(requestModel.token)){
//                if (requestModel.token.equals("111111")){
//                    ComponentUtil.redisService.set(requestModel.token, "1");
//                }
//            }
            // check校验数据
            did = HodgepodgeMethod.checkDidCollectionAccountListData(requestModel);

            // 获取用户收款账号集合数据
            DidCollectionAccountModel didCollectionAccountQuery = HodgepodgeMethod.assembleDidCollectionAccountListByDid(requestModel, did);
            List<DidCollectionAccountModel> dataList = ComponentUtil.didCollectionAccountService.queryByList(didCollectionAccountQuery);

            List<DidCollectionAccountModel> didCollectionAccountList = new ArrayList<>();
            for (DidCollectionAccountModel dataModel : dataList){
                // 查询收款码的二维码信息
                DidCollectionAccountQrCodeModel didCollectionAccountQrCodeQuery = HodgepodgeMethod.assembleDidCollectionAccountQrCodeByCollId(dataModel.getId());
                List<DidCollectionAccountQrCodeModel> didCollectionAccountQrCodeList = ComponentUtil.didCollectionAccountQrCodeService.queryByList(didCollectionAccountQrCodeQuery);
                if (didCollectionAccountQrCodeList == null || didCollectionAccountQrCodeList.size() <= 0){
                    dataModel.setLimitNum(0);
                    dataModel.setDataType(0);
                }else if(didCollectionAccountQrCodeList.size() == 1){
                    if (didCollectionAccountQrCodeList.get(0).getDataType() != 1){
                        dataModel.setLimitNum(0);
                        dataModel.setDataType(didCollectionAccountQrCodeList.get(0).getDataType());
                    }else{
                        dataModel.setLimitNum(1);
                        dataModel.setDataType(didCollectionAccountQrCodeList.get(0).getDataType());
                    }
                }else if (didCollectionAccountQrCodeList.size() > 1){
                    dataModel.setLimitNum(didCollectionAccountQrCodeList.size());
                    dataModel.setDataType(1);
                }
                didCollectionAccountList.add(dataModel);
            }

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
            if (!StringUtils.isBlank(map.get("dbCode"))){
                log.error(String.format("this DidCollectionAccountController.getDataList() is error codeInfo, the dbCode=%s and dbMessage=%s !", map.get("dbCode"), map.get("dbMessage")));
            }
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
     * 请求的属性类:RequestDidCollectionAccount
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
//            if (!StringUtils.isBlank(requestModel.token)){
//                if (requestModel.token.equals("111111")){
//                    ComponentUtil.redisService.set(requestModel.token, "1");
//                }
//            }
            // check校验请求的数据
            did = HodgepodgeMethod.checkDidCollectionAccountData(requestModel);

            // 收款账号详情数据
            DidCollectionAccountModel didCollectionAccountQuery = HodgepodgeMethod.assembleDidCollectionAccountByDidAndId(did, requestModel.id);
            DidCollectionAccountModel didCollectionAccountData = (DidCollectionAccountModel) ComponentUtil.didCollectionAccountService.findByObject(didCollectionAccountQuery);

            // 查询收款码的二维码信息
            DidCollectionAccountQrCodeModel didCollectionAccountQrCodeQuery = HodgepodgeMethod.assembleDidCollectionAccountQrCodeByCollId(didCollectionAccountData.getId());
            List<DidCollectionAccountQrCodeModel> didCollectionAccountQrCodeList = ComponentUtil.didCollectionAccountQrCodeService.queryByList(didCollectionAccountQrCodeQuery);
            if (didCollectionAccountQrCodeList == null || didCollectionAccountQrCodeList.size() <= 0){
                didCollectionAccountData.setLimitNum(0);
                didCollectionAccountData.setDataType(0);
            }else if(didCollectionAccountQrCodeList.size() == 1){
                if (didCollectionAccountQrCodeList.get(0).getDataType() != 1){
                    didCollectionAccountData.setLimitNum(0);
                    didCollectionAccountData.setDataType(didCollectionAccountQrCodeList.get(0).getDataType());
                }else{
                    didCollectionAccountData.setLimitNum(1);
                    didCollectionAccountData.setDataType(didCollectionAccountQrCodeList.get(0).getDataType());
                    log.info("");
                }
            }else if (didCollectionAccountQrCodeList.size() > 1){
                didCollectionAccountData.setLimitNum(didCollectionAccountQrCodeList.size());
                didCollectionAccountData.setDataType(1);

            }

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
            if (!StringUtils.isBlank(map.get("dbCode"))){
                log.error(String.format("this DidCollectionAccountController.getData() is error codeInfo, the dbCode=%s and dbMessage=%s !", map.get("dbCode"), map.get("dbMessage")));
            }
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
//            if (!StringUtils.isBlank(requestModel.token)){
//                if (requestModel.token.equals("111111")){
//                    ComponentUtil.redisService.set(requestModel.token, "1");
//                }
//            }

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
            if (!StringUtils.isBlank(map.get("dbCode"))){
                log.error(String.format("this DidCollectionAccountController.updateBasic() is error codeInfo, the dbCode=%s and dbMessage=%s !", map.get("dbCode"), map.get("dbMessage")));
            }
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
//            if (!StringUtils.isBlank(requestModel.token)){
//                if (requestModel.token.equals("111111")){
//                    ComponentUtil.redisService.set(requestModel.token, "1");
//                }
//            }

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
            if (!StringUtils.isBlank(map.get("dbCode"))){
                log.error(String.format("this DidCollectionAccountController.update() is error codeInfo, the dbCode=%s and dbMessage=%s !", map.get("dbCode"), map.get("dbMessage")));
            }
            e.printStackTrace();
            return JsonResult.failedResult(map.get("message"), map.get("code"), cgid, sgid);
        }

    }



    /**
     * @Description: 修改用户收款账号的使用状态
     * <p>
     *     用户可以对收款账号进行：暂停使用，恢复成正常使用，删除收款账号这三个动作的操作
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
//            if (!StringUtils.isBlank(requestModel.token)){
//                if (requestModel.token.equals("111111")){
//                    ComponentUtil.redisService.set(requestModel.token, "1");
//                }
//            }

            // check校验数据
            did = HodgepodgeMethod.checkDidCollectionAccountUpdateUseData(requestModel);

            // 如果用户删除收款账号：则需要查询此收款账号是否是已审核完毕的状态，如果是审核完毕，则需要提醒运营人员进行解绑操作；并且把这个账号与小微关联的数据进行删除
            if (requestModel.yn != null && requestModel.yn > 0){
                // 根据用户ID加收款账号ID查询收款账号信息
                DidCollectionAccountModel didCollectionAccountQuery = HodgepodgeMethod.assembleDidCollectionAccountQueryByDid(did, requestModel.id);
                DidCollectionAccountModel didCollectionAccountModel = (DidCollectionAccountModel) ComponentUtil.didCollectionAccountService.findByObject(didCollectionAccountQuery);
                if (didCollectionAccountModel != null && didCollectionAccountModel.getId() > 0){
                    if (didCollectionAccountModel.getCheckStatus() == 3){
                        // 表示已审核完毕的
                        // 根据收款账号ID查询绑定的小微
                        WxClerkModel wxClerkQuery = HodgepodgeMethod.assembleWxClerkByCollectionAccountId(didCollectionAccountModel.getId());
                        WxClerkModel wxClerkData = (WxClerkModel) ComponentUtil.wxClerkService.findByObject(wxClerkQuery);
                        if (wxClerkData != null && wxClerkData.getId() > 0){
                            // 删除小微旗下店员的绑定关系数据
                            WxClerkModel wxClerkUpdate = HodgepodgeMethod.assembleWxClerkUpdateData(didCollectionAccountModel.getId());
                            ComponentUtil.wxClerkService.manyOperation(wxClerkUpdate);
                        }
                        // 添加纪录小微需要解绑店员的数据（手机上面关系解绑）
                        WxClerkUnboundModel wxClerkUnboundModel = HodgepodgeMethod.assembleWxClerkUnbound(wxClerkData.getWxId(), didCollectionAccountModel);
                        ComponentUtil.wxClerkUnboundService.add(wxClerkUnboundModel);
                    }
                }

            }

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
     * 请求的属性类:RequestDidCollectionAccount
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




    /**
     * @Description: 获取支付宝收款账号数据-详情
     * @param request
     * @param response
     * @return com.gd.chain.common.utils.JsonResult<java.lang.Object>
     * @author yoko
     * @date 2019/11/25 22:58
     * local:http://localhost:8086/fine/collAc/getZfbData
     * 请求的属性类:RequestDidCollectionAccount
     * 必填字段:{"agtVer":1,"clientVer":1,"clientType":1,"ctime":201911071802959,"cctime":201911071802959,"sign":"abcdefg","token":"111111"}
     * 加密字段:{"jsonData":"eyJhZ3RWZXIiOjEsImNsaWVudFZlciI6MSwiY2xpZW50VHlwZSI6MSwiY3RpbWUiOjIwMTkxMTA3MTgwMjk1OSwiY2N0aW1lIjoyMDE5MTEwNzE4MDI5NTksInNpZ24iOiJhYmNkZWZnIiwidG9rZW4iOiIxMTExMTEifQ=="}
     * 客户端加密字段:id+ctime+cctime+秘钥=sign
     * 服务端加密字段:stime+秘钥=sign
     * result={
     *     "resultCode": "0",
     *     "message": "success",
     *     "data": {
     *         "jsonData": "eyJzaWduIjoiN2Y1ODA4NTU1MTNlOGE0MTliNDFjMWQxMjIwNjkwMzYiLCJzdGltZSI6MTU5NDE5OTQ2NjY3NiwiemZiTW9kZWwiOnsiYWNOYW1lIjoiYWNOYW1lMyIsImFjTnVtIjoiemZiX2FjX251bSIsImFjVHlwZSI6MiwiaWQiOjIsInBheWVlIjoicGF5ZWUyIiwidXNlcklkIjoidXNlcl9pZF8xIn19"
     *     },
     *     "sgid": "202007081711050000001",
     *     "cgid": ""
     * }
     */
    @RequestMapping(value = "/getZfbData", method = {RequestMethod.POST})
    public JsonResult<Object> getZfbData(HttpServletRequest request, HttpServletResponse response, @RequestBody RequestEncryptionJson requestData) throws Exception{
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
//            if (!StringUtils.isBlank(requestModel.token)){
//                if (requestModel.token.equals("111111")){
//                    ComponentUtil.redisService.set(requestModel.token, "1");
//                }
//            }
            // check校验请求的数据
            did = HodgepodgeMethod.checkDidCollectionAccountZfbData(requestModel);

            // 收款账号详情数据
            DidCollectionAccountModel didCollectionAccountQuery = HodgepodgeMethod.assembleDidCollectionAccountByDidAndAcType(did, 2);
            DidCollectionAccountModel didCollectionAccountData = (DidCollectionAccountModel) ComponentUtil.didCollectionAccountService.findByObject(didCollectionAccountQuery);


            // 组装返回客户端的数据
            long stime = System.currentTimeMillis();
            String sign = SignUtil.getSgin(stime, secretKeySign); // stime+秘钥=sign
            String strData = HodgepodgeMethod.assembleDidCollectionAccountZfbDataResult(stime, sign, didCollectionAccountData);
            // 数据加密
            String encryptionData = StringUtil.mergeCodeBase64(strData);
            ResponseEncryptionJson resultDataModel = new ResponseEncryptionJson();
            resultDataModel.jsonData = encryptionData;
            // 返回数据给客户端
            return JsonResult.successResult(resultDataModel, cgid, sgid);
        }catch (Exception e){
            Map<String,String> map = ExceptionMethod.getException(e, ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_TWO);
            // 添加异常
            log.error(String.format("this DidCollectionAccountController.getZfbData() is error , the cgid=%s and sgid=%s and all data=%s!", cgid, sgid, data));
            if (!StringUtils.isBlank(map.get("dbCode"))){
                log.error(String.format("this DidCollectionAccountController.getZfbData() is error codeInfo, the dbCode=%s and dbMessage=%s !", map.get("dbCode"), map.get("dbMessage")));
            }
            e.printStackTrace();
            return JsonResult.failedResult(map.get("message"), map.get("code"), cgid, sgid);
        }
    }



    /**
     * @Description: 用户修改支付宝收款账号的信息
     * @param request
     * @param response
     * @return com.gd.chain.common.utils.JsonResult<java.lang.Object>
     * @author yoko
     * @date 2019/11/25 22:58
     * local:http://localhost:8086/fine/collAc/updateZfb
     * 请求的属性类:RequestDidCollectionAccount
     * 必填字段:{"id":2,"acName":"acName1","acNum":"acNum11","payee":"payee11","userId":"userId_1","agtVer":1,"clientVer":1,"clientType":1,"ctime":201911071802959,"cctime":201911071802959,"sign":"abcdefg","token":"111111"}
     * 加密字段:{"jsonData":"eyJpZCI6MSwiYWNUeXBlIjoiMiIsImFjTnVtIjoiYWNOdW0xMSIsIm1tUXJDb2RlIjoibW1RckNvZGUxMSIsInBheWVlIjoicGF5ZWUxMSIsImJhbmtOYW1lIjoiYmFua05hbWUxMSIsImJ1c2luZXNzVHlwZSI6IjIiLCJ3eFFyQ29kZUFkcyI6Ind4UXJDb2RlQWRzMTEiLCJhZ3RWZXIiOjEsImNsaWVudFZlciI6MSwiY2xpZW50VHlwZSI6MSwiY3RpbWUiOjIwMTkxMTA3MTgwMjk1OSwiY2N0aW1lIjoyMDE5MTEwNzE4MDI5NTksInNpZ24iOiJhYmNkZWZnIiwidG9rZW4iOiIxMTExMTEifQ=="}
     * 客户端加密字段:ctime+cctime+秘钥=sign
     * 服务端加密字段:stime+秘钥=sign
     * result={
     *     "resultCode": "0",
     *     "message": "success",
     *     "data": {
     *         "jsonData": "eyJzaWduIjoiZDE0NzNhZTdjM2NiMWQyNGQ5OGNkYWU0ZWQyZGEwYzQiLCJzdGltZSI6MTU5NDIwMDg4NTY3N30="
     *     },
     *     "sgid": "202007081734450000001",
     *     "cgid": ""
     * }
     */
    @RequestMapping(value = "/updateZfb", method = {RequestMethod.POST})
    public JsonResult<Object> updateZfb(HttpServletRequest request, HttpServletResponse response, @RequestBody RequestEncryptionJson requestData) throws Exception {
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
            did = HodgepodgeMethod.checkDidCollectionAccountUpdateZfbData(requestModel);

            DidModel didQuery = HodgepodgeMethod.assembleDidByOperateWdQuery(did, requestModel.operateWd);
            DidModel didModel = (DidModel) ComponentUtil.didService.findByObject(didQuery);
            HodgepodgeMethod.checkDidByOperateWd(didModel);

            // 组装要更新的数据进行更新-支付宝
            DidCollectionAccountModel didCollectionAccountUpdate = HodgepodgeMethod.assembleDidCollectionAccountUpdateZfb(did, requestModel);
            ComponentUtil.didCollectionAccountService.updateDidCollectionAccountZfb(didCollectionAccountUpdate);

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
            log.error(String.format("this DidCollectionAccountController.updateZfb() is error , the cgid=%s and sgid=%s and all data=%s!", cgid, sgid, data));
            if (!StringUtils.isBlank(map.get("dbCode"))){
                log.error(String.format("this DidCollectionAccountController.updateZfb() is error codeInfo, the dbCode=%s and dbMessage=%s !", map.get("dbCode"), map.get("dbMessage")));
            }
            e.printStackTrace();
            return JsonResult.failedResult(map.get("message"), map.get("code"), cgid, sgid);
        }

    }


    /**
     * @Description: 获取群名称-微信群
     * @param request
     * @param response
     * @return com.gd.chain.common.utils.JsonResult<java.lang.Object>
     * @author yoko
     * @date 2019/11/25 22:58
     * local:http://localhost:8086/fine/collAc/getGroupName
     * 请求的属性类:RequestDidCollectionAccount
     * 必填字段:{"agtVer":1,"clientVer":1,"clientType":1,"ctime":201911071802959,"cctime":201911071802959,"sign":"abcdefg","token":"111111"}
     * 加密字段:{"jsonData":"eyJhZ3RWZXIiOjEsImNsaWVudFZlciI6MSwiY2xpZW50VHlwZSI6MSwiY3RpbWUiOjIwMTkxMTA3MTgwMjk1OSwiY2N0aW1lIjoyMDE5MTEwNzE4MDI5NTksInNpZ24iOiJhYmNkZWZnIiwidG9rZW4iOiIxMTExMTEifQ=="}
     * 客户端加密字段:ctime+cctime+秘钥=sign
     * 服务端加密字段:stime+秘钥=sign
     * result={
     *     "resultCode": "0",
     *     "message": "success",
     *     "data": {
     *         "jsonData": "eyJncm91cE1vZGVsIjp7ImFjVHlwZSI6MywiY2hlY2tJbmZvIjoiIiwiY2hlY2tTdGF0dXMiOi0xLCJkZFFyQ29kZSI6IiIsImlkIjo4MSwiaW52YWxpZFRpbWUiOiIyMDIwLTA4LTA0IDIyOjUyOjA5IiwiaXNJbnZhbGlkIjoxLCJpc09rIjoyLCJtbVFyQ29kZSI6IiIsInBheWVlIjoiMemXqueUtTEiLCJyZWRQYWNrTnVtIjowLCJ1c2VTdGF0dXMiOjF9LCJzaWduIjoiMTJiNzZlZDk0ZmM5NDA0NmVjZDY3ZmI1MjdhMmM3MzAiLCJzdGltZSI6MTU5NjEyMjM4NTc4MX0="
     *     },
     *     "sgid": "202007302319370000001",
     *     "cgid": ""
     * }
     */
    @RequestMapping(value = "/getGroupName", method = {RequestMethod.POST})
    public JsonResult<Object> getGroupName(HttpServletRequest request, HttpServletResponse response, @RequestBody RequestEncryptionJson requestData) throws Exception {
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
//                    ComponentUtil.redisService.set(requestModel.token, "23");
//                }
//            }
            // check校验数据
            did = HodgepodgeMethod.checkDidCollectionAccountGroupName(requestModel);

            // 查询策略里面的微信群名固定词
            StrategyModel strategyGroupNameQuery = HodgepodgeMethod.assembleStrategyQuery(ServerConstant.StrategyEnum.GROUP_NAME.getStgType());
            StrategyModel strategyGroupNameModel = ComponentUtil.strategyService.getStrategyModel(strategyGroupNameQuery, ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_ZERO);

            // 查询策略里面的每个群最多允许收红包的数量
            StrategyModel strategyGroupRedPackNumQuery = HodgepodgeMethod.assembleStrategyQuery(ServerConstant.StrategyEnum.GROUP_RED_PACK_NUM.getStgType());
            StrategyModel strategygroupRedPackNumModel = ComponentUtil.strategyService.getStrategyModel(strategyGroupRedPackNumQuery, ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_ZERO);

            // 获取用户信息
            DidModel didQuery = HodgepodgeMethod.assembleDidQueryByDid(did);
            DidModel didModel = (DidModel) ComponentUtil.didService.findByObject(didQuery);
            HodgepodgeMethod.checkDidData(didModel);

            // 组装群名
            int groupNum = didModel.getGroupNum() + 1;
            String groupName = String.valueOf(did) + strategyGroupNameModel.getStgValue() + groupNum;

            int isOk = 0;
            // 查询此账号的最新的收款账号
            DidCollectionAccountModel didCollectionAccountModel = new DidCollectionAccountModel();
            DidCollectionAccountModel didCollectionAccountQuery = HodgepodgeMethod.assembleDidCollectionAccountByDidAndAcTypeQuery(did, 3);
            didCollectionAccountModel = ComponentUtil.didCollectionAccountService.getDidCollectionAccount(didCollectionAccountQuery);
            if (didCollectionAccountModel != null && didCollectionAccountModel.getId() > 0){
                if (!StringUtils.isBlank(didCollectionAccountModel.getAcName()) && !StringUtils.isBlank(didCollectionAccountModel.getAcNum())){
                    // 不需要回复指令
                    if (StringUtils.isBlank(didCollectionAccountModel.getMmQrCode()) && StringUtils.isBlank(didCollectionAccountModel.getDdQrCode())){
                        // 说明已经回复了指令，但是没有上传二维码
                        isOk = 2;
                    }else {
                        // 说明已经回复了指令，并且已经上传了二维码；需要新增账号
                        isOk = 1;
                        // 新增收款账号
                        DidCollectionAccountModel didCollectionAccountAdd = HodgepodgeMethod.assembleDidCollectionAccountAddByWx(did, 3, groupName, strategygroupRedPackNumModel.getStgNumValue());
                        ComponentUtil.didCollectionAccountService.add(didCollectionAccountAdd);
                        didCollectionAccountModel = didCollectionAccountAdd;

                        // 修改微信群序号
                        DidModel updateGroupOrSwitch = HodgepodgeMethod.assembleUpdateGroupOrSwitchData(did, 1, 0);
                        ComponentUtil.didService.updateDidGroupNumOrSwitchType(updateGroupOrSwitch);
                    }

                }else {
                    // 不需要新增：但是需要回复
                    isOk = 1;
                }
            }else {
                // 表示需要新增收款账号
                isOk = 1;
                // 新增收款账号
                DidCollectionAccountModel didCollectionAccountAdd = HodgepodgeMethod.assembleDidCollectionAccountAddByWx(did, 3, groupName, strategygroupRedPackNumModel.getStgNumValue());
                ComponentUtil.didCollectionAccountService.add(didCollectionAccountAdd);
                didCollectionAccountModel = didCollectionAccountAdd;
                log.info("");
                // 修改微信群序号
                DidModel updateGroupOrSwitch = HodgepodgeMethod.assembleUpdateGroupOrSwitchData(did, 1, 0);
                ComponentUtil.didService.updateDidGroupNumOrSwitchType(updateGroupOrSwitch);
            }


            // 组装返回客户端的数据
            long stime = System.currentTimeMillis();
            String sign = SignUtil.getSgin(stime, secretKeySign); // stime+秘钥=sign
            String strData = HodgepodgeMethod.assembleGroupNameResult(stime, sign, didCollectionAccountModel, isOk);
            // 数据加密
            String encryptionData = StringUtil.mergeCodeBase64(strData);
            ResponseEncryptionJson resultDataModel = new ResponseEncryptionJson();
            resultDataModel.jsonData = encryptionData;
            // 返回数据给客户端
            return JsonResult.successResult(resultDataModel, cgid, sgid);
        } catch (Exception e) {
            Map<String, String> map = ExceptionMethod.getException(e, ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_TWO);
            log.error(String.format("this DidCollectionAccountController.getGroupName() is error , the cgid=%s and sgid=%s and all data=%s!", cgid, sgid, data));
            if (!StringUtils.isBlank(map.get("dbCode"))){
                log.error(String.format("this DidCollectionAccountController.getGroupName() is error codeInfo, the dbCode=%s and dbMessage=%s !", map.get("dbCode"), map.get("dbMessage")));
            }
            e.printStackTrace();
            return JsonResult.failedResult(map.get("message"), map.get("code"), cgid, sgid);
        }

    }



    /**
     * @Description: 用户修改微信群二维码
     * @param request
     * @param response
     * @return com.gd.chain.common.utils.JsonResult<java.lang.Object>
     * @author yoko
     * @date 2019/11/25 22:58
     * local:http://localhost:8086/fine/collAc/updateGroupQrCode
     * 请求的属性类:RequestDid
     * 必填字段:{"id":1,"mmQrCode":"mmQrCode1","ddQrCode":"ddQrCode1","agtVer":1,"clientVer":1,"clientType":1,"ctime":201911071802959,"cctime":201911071802959,"sign":"abcdefg","token":"111111"}
     * 加密字段:{"jsonData":"eyJpZCI6MSwiYWNUeXBlIjoiMiIsImFjTnVtIjoiYWNOdW0xMSIsIm1tUXJDb2RlIjoibW1RckNvZGUxMSIsInBheWVlIjoicGF5ZWUxMSIsImJhbmtOYW1lIjoiYmFua05hbWUxMSIsImJ1c2luZXNzVHlwZSI6IjIiLCJ3eFFyQ29kZUFkcyI6Ind4UXJDb2RlQWRzMTEiLCJhZ3RWZXIiOjEsImNsaWVudFZlciI6MSwiY2xpZW50VHlwZSI6MSwiY3RpbWUiOjIwMTkxMTA3MTgwMjk1OSwiY2N0aW1lIjoyMDE5MTEwNzE4MDI5NTksInNpZ24iOiJhYmNkZWZnIiwidG9rZW4iOiIxMTExMTEifQ=="}
     * 客户端加密字段:ctime+cctime+秘钥=sign
     * 服务端加密字段:stime+秘钥=sign
     * result={
     *     "resultCode": "0",
     *     "message": "success",
     *     "data": {
     *         "jsonData": "eyJzaWduIjoiYTI2OTY5NTE2NDE3M2IwZjY4OTdiMDk2Yzg4ODYzZjgiLCJzdGltZSI6MTU5NjE2MTAzMTgxMX0="
     *     },
     *     "sgid": "202007311003510000001",
     *     "cgid": ""
     * }
     */
    @RequestMapping(value = "/updateGroupQrCode", method = {RequestMethod.POST})
    public JsonResult<Object> updateGroupQrCode(HttpServletRequest request, HttpServletResponse response, @RequestBody RequestEncryptionJson requestData) throws Exception {
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
            did = HodgepodgeMethod.checkDidCollectionAccountUpdateGroupQrCodeData(requestModel);


            // 组装要更新的数据进行更新
            DidCollectionAccountModel didCollectionAccountUpdate = HodgepodgeMethod.assembleDidCollectionAccountUpdateGroupQrCode(did, requestModel);
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
            log.error(String.format("this DidCollectionAccountController.updateGroupQrCode() is error , the cgid=%s and sgid=%s and all data=%s!", cgid, sgid, data));
            if (!StringUtils.isBlank(map.get("dbCode"))){
                log.error(String.format("this DidCollectionAccountController.updateGroupQrCode() is error codeInfo, the dbCode=%s and dbMessage=%s !", map.get("dbCode"), map.get("dbMessage")));
            }
            e.printStackTrace();
            return JsonResult.failedResult(map.get("message"), map.get("code"), cgid, sgid);
        }

    }


    /**
     * @Description: 用户获取微信群收款账号信息-集合
     * <p>
     *     获取有效或者无效的微信群集合数据
     * </p>
     * @param request
     * @param response
     * @return com.gd.chain.common.utils.JsonResult<java.lang.Object>
     * @author yoko
     * @date 2019/11/25 22:58
     * local:http://localhost:8086/fine/collAc/getGroupDataList
     * 请求的属性类:RequestDidCollectionAccount
     * 必填字段:{"isInvalid":1,"agtVer":1,"clientVer":1,"clientType":1,"ctime":201911071802959,"cctime":201911071802959,"sign":"abcdefg","pageNumber":1,"pageSize":3,"token":"111111"}
     * 加密字段:{"jsonData":"eyJpc0ludmFsaWQiOjEsImFndFZlciI6MSwiY2xpZW50VmVyIjoxLCJjbGllbnRUeXBlIjoxLCJjdGltZSI6MjAxOTExMDcxODAyOTU5LCJjY3RpbWUiOjIwMTkxMTA3MTgwMjk1OSwic2lnbiI6ImFiY2RlZmciLCJwYWdlTnVtYmVyIjoxLCJwYWdlU2l6ZSI6MTAsInRva2VuIjoiMTExMTExIn0="}
     * 客户端加密字段:ctime+秘钥=sign
     * 返回加密字段:stime+秘钥=sign
     * result={
     *     "resultCode": "0",
     *     "message": "success",
     *     "data": {
     *         "jsonData": "eyJncm91cExpc3QiOlt7ImFjVHlwZSI6MywiY2hlY2tJbmZvIjoiIiwiY2hlY2tTdGF0dXMiOjMsImRkUXJDb2RlIjoiZGRRckNvZGUxIiwiaWQiOjgxLCJpbnZhbGlkVGltZSI6IjIwMjAtMDgtMDQgMjI6NTI6MDkiLCJpc0ludmFsaWQiOjEsIm1tUXJDb2RlIjoibW1RckNvZGUxIiwicGF5ZWUiOiIx6Zeq55S1MSIsInJlZFBhY2tOdW0iOjEsInVzZVN0YXR1cyI6MX0seyJhY1R5cGUiOjMsImNoZWNrSW5mbyI6IuaIkOWKnyIsImNoZWNrU3RhdHVzIjozLCJkZFFyQ29kZSI6ImRkUXJDb2RlMiIsImlkIjo4MiwiaW52YWxpZFRpbWUiOiIyMDIwLTA4LTA0IDIzOjIyOjE1IiwiaXNJbnZhbGlkIjoxLCJtbVFyQ29kZSI6Im1tUXJDb2RlMiIsInBheWVlIjoiMemXqueUtTIiLCJyZWRQYWNrTnVtIjoyLCJ1c2VTdGF0dXMiOjF9LHsiYWNUeXBlIjozLCJjaGVja0luZm8iOiIiLCJjaGVja1N0YXR1cyI6MywiZGRRckNvZGUiOiJkZFFyQ29kZTUiLCJpZCI6ODUsImludmFsaWRUaW1lIjoiMjAyMC0wOC0wNCAyMzoyMjoxNSIsImlzSW52YWxpZCI6MSwibW1RckNvZGUiOiJtbVFyQ29kZTUiLCJwYXllZSI6Iue+pOWQjTUiLCJyZWRQYWNrTnVtIjo1LCJ1c2VTdGF0dXMiOjF9LHsiYWNUeXBlIjozLCJjaGVja0luZm8iOiIiLCJjaGVja1N0YXR1cyI6MywiZGRRckNvZGUiOiJkZFFyQ29kZTYiLCJpZCI6ODYsImludmFsaWRUaW1lIjoiMjAyMC0wOC0wNCAyMzoyMjoxNSIsImlzSW52YWxpZCI6MSwibW1RckNvZGUiOiJtbVFyQ29kZTYiLCJwYXllZSI6Iue+pOWQjTYiLCJyZWRQYWNrTnVtIjo2LCJ1c2VTdGF0dXMiOjF9LHsiYWNUeXBlIjozLCJjaGVja0luZm8iOiIiLCJjaGVja1N0YXR1cyI6MywiZGRRckNvZGUiOiJkZFFyQ29kZTciLCJpZCI6ODcsImludmFsaWRUaW1lIjoiMjAyMC0wOC0wNCAyMzoyMjoxNSIsImlzSW52YWxpZCI6MSwibW1RckNvZGUiOiJtbVFyQ29kZTciLCJwYXllZSI6Iue+pOWQjTciLCJyZWRQYWNrTnVtIjo3LCJ1c2VTdGF0dXMiOjF9LHsiYWNUeXBlIjozLCJjaGVja0luZm8iOiIiLCJjaGVja1N0YXR1cyI6MywiZGRRckNvZGUiOiJkZFFyQ29kZTgiLCJpZCI6ODgsImludmFsaWRUaW1lIjoiMjAyMC0wOC0wNCAyMzoyMjoxNSIsImlzSW52YWxpZCI6MSwibW1RckNvZGUiOiJtbVFyQ29kZTgiLCJwYXllZSI6Iue+pOWQjTgiLCJyZWRQYWNrTnVtIjo4LCJ1c2VTdGF0dXMiOjF9LHsiYWNUeXBlIjozLCJjaGVja0luZm8iOiIiLCJjaGVja1N0YXR1cyI6MywiZGRRckNvZGUiOiJkZFFyQ29kZTkiLCJpZCI6ODksImludmFsaWRUaW1lIjoiMjAyMC0wOC0wNCAyMzoyMjoxNSIsImlzSW52YWxpZCI6MSwibW1RckNvZGUiOiJtbVFyQ29kZTkiLCJwYXllZSI6Iue+pOWQjTkiLCJyZWRQYWNrTnVtIjo5LCJ1c2VTdGF0dXMiOjF9XSwicm93Q291bnQiOjcsInNpZ24iOiIzZjVlZTVkODlhZWFmZTk0MGQ5YmVhNTA5Zjc4NTA1ZSIsInN0aW1lIjoxNTk2MTc4Nzg3ODk2fQ=="
     *     },
     *     "sgid": "202007311457580000001",
     *     "cgid": ""
     * }
     */
    @RequestMapping(value = "/getGroupDataList", method = {RequestMethod.POST})
    public JsonResult<Object> getGroupDataList(HttpServletRequest request, HttpServletResponse response, @RequestBody RequestEncryptionJson requestData) throws Exception{
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

            did = HodgepodgeMethod.checkDidCollectionAccountGroupDataList(requestModel);

            DidCollectionAccountModel didCollectionAccountQuery = new DidCollectionAccountModel();
            List<DidCollectionAccountModel> dataList = new ArrayList<>();
            if (requestModel.isInvalid == 1){
                // 组装有效的收款账号的查询条件
                didCollectionAccountQuery = HodgepodgeMethod.assembleDidCollectionAccountListByInvalid(requestModel, did, 3);
                // 获取用户收款账号集合数据-有效
                dataList = ComponentUtil.didCollectionAccountService.queryByList(didCollectionAccountQuery);
            }else{
                // 组装无效的收款账号的查询条件
                didCollectionAccountQuery = HodgepodgeMethod.assembleDidCollectionAccountListByInvalidOk(requestModel, did, 3);
                dataList = ComponentUtil.didCollectionAccountService.getDidCollectionAccountByInvalid(didCollectionAccountQuery);
            }
            // 组装返回客户端的数据
            long stime = System.currentTimeMillis();
            String sign = SignUtil.getSgin(stime, secretKeySign); // stime+秘钥=sign
            String strData = HodgepodgeMethod.assembleDidCollectionAccountGroupDataListResult(stime, sign, dataList, didCollectionAccountQuery.getRowCount());
            // 数据加密
            String encryptionData = StringUtil.mergeCodeBase64(strData);
            ResponseEncryptionJson resultDataModel = new ResponseEncryptionJson();
            resultDataModel.jsonData = encryptionData;
            // 返回数据给客户端
            return JsonResult.successResult(resultDataModel, cgid, sgid);
        }catch (Exception e){
            Map<String,String> map = ExceptionMethod.getException(e, ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_TWO);
            // #添加异常
            log.error(String.format("this DidCollectionAccountController.getGroupDataList() is error , the cgid=%s and sgid=%s and all data=%s!", cgid, sgid, data));
            if (!StringUtils.isBlank(map.get("dbCode"))){
                log.error(String.format("this DidCollectionAccountController.getGroupDataList() is error codeInfo, the dbCode=%s and dbMessage=%s !", map.get("dbCode"), map.get("dbMessage")));
            }
            e.printStackTrace();
            return JsonResult.failedResult(map.get("message"), map.get("code"), cgid, sgid);
        }
    }





}
