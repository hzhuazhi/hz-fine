package com.hz.fine.master.core.controller.did;

import com.alibaba.fastjson.JSON;
import com.hz.fine.master.core.common.exception.ExceptionMethod;
import com.hz.fine.master.core.common.exception.ServiceException;
import com.hz.fine.master.core.common.utils.JsonResult;
import com.hz.fine.master.core.common.utils.SignUtil;
import com.hz.fine.master.core.common.utils.StringUtil;
import com.hz.fine.master.core.common.utils.constant.ErrorCode;
import com.hz.fine.master.core.common.utils.constant.ServerConstant;
import com.hz.fine.master.core.model.RequestEncryptionJson;
import com.hz.fine.master.core.model.ResponseEncryptionJson;
import com.hz.fine.master.core.model.did.DidCollectionAccountModel;
import com.hz.fine.master.core.model.did.DidCollectionAccountQrCodeModel;
import com.hz.fine.master.core.model.did.DidModel;
import com.hz.fine.master.core.model.region.RegionModel;
import com.hz.fine.master.core.protocol.request.did.RequestDidCollectionAccount;
import com.hz.fine.master.core.protocol.request.did.collection.RequestDidCollectionAccountAll;
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
 * @Description 用户收款账号+用户二维码信息合二为一的Controller层
 * @Author yoko
 * @Date 2020/6/17 16:29
 * @Version 1.0
 */
@RestController
@RequestMapping("/fine/collAcAll")
public class DidCollectionAccountAllController {

    private static Logger log = LoggerFactory.getLogger(DidCollectionAccountAllController.class);

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
     * local:http://localhost:8086/fine/collAcAll/add
     * 请求的属性类:RequestDid
     * 必填字段:{"acName":"acName1","acType":1,"businessType":1,"wxQrCodeAds":"wxQrCodeAds1","dataList":[{"alias":"alias_1","ddQrCode":"ddQrCode_1","dataType":1,"limitNum":10},{"alias":"alias_2","ddQrCode":"ddQrCode_2","dataType":2,"limitNum":20},{"alias":"alias_3","ddQrCode":"ddQrCode_3","dataType":3,"limitNum":30}],"agtVer":1,"clientVer":1,"clientType":1,"ctime":201911071802959,"cctime":201911071802959,"sign":"abcdefg","token":"111111"}
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

        RequestDidCollectionAccountAll requestModel = new RequestDidCollectionAccountAll();
        try{
            // 解密
            data = StringUtil.decoderBase64(requestData.jsonData);
            requestModel  = JSON.parseObject(data, RequestDidCollectionAccountAll.class);

            //#临时数据
            if (!StringUtils.isBlank(requestModel.token)){
                if (requestModel.token.equals("111111")){
                    ComponentUtil.redisService.set(requestModel.token, "1");
                }
            }

            // check校验数据
            did = HodgepodgeMethod.checkDidCollectionAccountAllAddData(requestModel);

            // 判断用户是否充值过：只有充值过的用户才能进行收款账号的添加
            DidModel didQuery = HodgepodgeMethod.assembleDidQuery(did);
            DidModel didModel = (DidModel) ComponentUtil.didService.findByObject(didQuery);
            HodgepodgeMethod.checkDidInfo(didModel);

            // 组装要录入的用户收款账号信息
            DidCollectionAccountModel addData = HodgepodgeMethod.assembleDidCollectionAccount(requestModel, did);
            int num = ComponentUtil.didCollectionAccountService.add(addData);
            if (num > 0){
                // 批量新增用户收款二维码数据:批量添加后续在实现,先使用for循环实现
                // 组装要添加的二维码数据集合
                List<DidCollectionAccountQrCodeModel> didCollectionAccountQrCodeList = HodgepodgeMethod.assembleDidCollectionAccountQrCodeList(requestModel.dataList);
                for (DidCollectionAccountQrCodeModel qrCodeData : didCollectionAccountQrCodeList){
                    qrCodeData.setCollectionAccountId(addData.getId());
                    ComponentUtil.didCollectionAccountQrCodeService.add(qrCodeData);
                }
            }else {
                throw new ServiceException(ErrorCode.ENUM_ERROR.A00009.geteCode(), ErrorCode.ENUM_ERROR.A00009.geteDesc());
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
            log.error(String.format("this DidCollectionAccountAllController.add() is error , the cgid=%s and sgid=%s and all data=%s!", cgid, sgid, data));
            if (!StringUtils.isBlank(map.get("dbCode"))){
                log.error(String.format("this DidCollectionAccountAllController.add() is error codeInfo, the dbCode=%s and dbMessage=%s !", map.get("dbCode"), map.get("dbMessage")));
            }
            e.printStackTrace();
            return JsonResult.failedResult(map.get("message"), map.get("code"), cgid, sgid);
        }
    }





}
