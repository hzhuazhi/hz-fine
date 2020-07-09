package com.hz.fine.master.core.controller.upgrade;

import com.alibaba.fastjson.JSON;
import com.hz.fine.master.core.common.exception.ExceptionMethod;
import com.hz.fine.master.core.common.utils.*;
import com.hz.fine.master.core.common.utils.constant.ServerConstant;
import com.hz.fine.master.core.model.RequestEncryptionJson;
import com.hz.fine.master.core.model.ResponseEncryptionJson;
import com.hz.fine.master.core.model.region.RegionModel;
import com.hz.fine.master.core.model.upgrade.UpgradeModel;
import com.hz.fine.master.core.protocol.request.upgrade.RequestUpgrade;
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
import java.util.Map;

/**
 * @Description 客户端升级的Controller层
 * @Author yoko
 * @Date 2020/1/14 14:49
 * @Version 1.0
 */
@RestController
@RequestMapping("/fine/up")
public class UpgradeController {
    private static Logger log = LoggerFactory.getLogger(UpgradeController.class);

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
     * @Description: 获取客户端版本更新数据
     * @param request
     * @param response
     * @return com.gd.chain.common.utils.JsonResult<java.lang.Object>
     * @author yoko
     * @date 2019/11/25 22:58
     * local:http://localhost:8086/fine/up/getData
     * 请求的属性类:RequestUpgrade
     * 必填字段:{"agtVer":1,"clientVer":1,"clientType":1,"ctime":201911071802959,"cctime":201911071802959,"sign":"abcdefg","token":"111111"}
     * 客户端加密字段:ctime+cctime+秘钥=sign
     * 服务端加密字段:stime+clientType+clientVer+md5Value+resUrl+upType+秘钥=sign
     * result={
     *     "resultCode": "0",
     *     "message": "success",
     *     "data": {
     *         "jsonData": "eyJjbGllbnRUeXBlIjoxLCJjbGllbnRWZXIiOjIsImNvbnRlbnQiOiLmm7TmlrDlhoXlrrlfMiIsIm1kNVZhbHVlIjoiYWJjZDIiLCJyZXNVcmwiOiJodHRwOi8vd3d3LmJhaWR1LmNvbSIsInNob3dWZXIiOiIxLjAuMiIsInNpZ24iOiJlN2FhY2UzODQxMjRiYWZkY2U0MDg3NjdlY2YyZmYwOCIsInN0aW1lIjoxNTgzMzEyODgwODE5LCJ1cFR5cGUiOjJ9"
     *     },
     *     "sgid": "202003041707590000001",
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
        RegionModel regionModel = HodgepodgeMethod.assembleRegionModel(ip);

        RequestUpgrade requestModel = new RequestUpgrade();
        try{
            // 解密
            data = StringUtil.decoderBase64(requestData.jsonData);
            requestModel  = JSON.parseObject(data, RequestUpgrade.class);


            // 客户端升级详情数据
            UpgradeModel upgradeQuery = BeanUtils.copy(requestModel, UpgradeModel.class);
            UpgradeModel upgradeModel = ComponentUtil.upgradeService.getMaxUpgradeData(upgradeQuery);
            // 组装返回客户端的数据
            long stime = System.currentTimeMillis();
            String sign = "";
            if (upgradeModel != null){
                sign = SignUtil.getSgin(stime, upgradeModel.getClientType(), upgradeModel.getClientVer(), upgradeModel.getMd5Value(),
                        upgradeModel.getResUrl(), upgradeModel.getUpType(), secretKeySign); // stime+clientType+clientVer+md5Value+resUrl+upType秘钥=sign
            }else{
                SignUtil.getSgin(stime, secretKeySign); // stime+秘钥=sign
            }

            String strData = HodgepodgeMethod.assembleUpgradeDataResult(stime, sign, upgradeModel);
            // 数据加密
            String encryptionData = DesCipher.encryptData(strData);
            ResponseEncryptionJson resultDataModel = new ResponseEncryptionJson();
            resultDataModel.jsonData = encryptionData;
            // 添加流水
            // 返回数据给客户端
            return JsonResult.successResult(resultDataModel, cgid, sgid);
        }catch (Exception e){
            Map<String,String> map = ExceptionMethod.getException(e, ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_TWO);
            // 添加异常
            log.error(String.format("this UpgradeController.getData() is error , the cgid=%s and sgid=%s and all data=%s!", cgid, sgid, data));
            if (!StringUtils.isBlank(map.get("dbCode"))){
                log.error(String.format("this UpgradeController.getData() is error codeInfo, the dbCode=%s and dbMessage=%s !", map.get("dbCode"), map.get("dbMessage")));
            }
            e.printStackTrace();
            return JsonResult.failedResult(map.get("message"), map.get("code"), cgid, sgid);
        }
    }
}
