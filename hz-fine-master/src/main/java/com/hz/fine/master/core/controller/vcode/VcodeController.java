package com.hz.fine.master.core.controller.vcode;

import com.alibaba.fastjson.JSON;
import com.hz.fine.master.core.common.exception.ExceptionMethod;
import com.hz.fine.master.core.common.utils.JsonResult;
import com.hz.fine.master.core.common.utils.SendSmsUtils;
import com.hz.fine.master.core.common.utils.SignUtil;
import com.hz.fine.master.core.common.utils.StringUtil;
import com.hz.fine.master.core.common.utils.constant.CacheKey;
import com.hz.fine.master.core.common.utils.constant.CachedKeyUtils;
import com.hz.fine.master.core.common.utils.constant.ServerConstant;
import com.hz.fine.master.core.model.RequestEncryptionJson;
import com.hz.fine.master.core.model.ResponseEncryptionJson;
import com.hz.fine.master.core.protocol.request.question.RequestQuestion;
import com.hz.fine.master.core.protocol.request.vcode.RequestVcode;
import com.hz.fine.master.util.ComponentUtil;
import com.hz.fine.master.util.HodgepodgeMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @Description 获取手机验证码的Controller层
 * @Author yoko
 * @Date 2020/5/13 19:44
 * @Version 1.0
 */
@RestController
@RequestMapping("/fine/cd")
public class VcodeController {
    private static Logger log = LoggerFactory.getLogger(VcodeController.class);

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
     * @Description: 获取验证码
     * @param request
     * @param response
     * @return com.gd.chain.common.utils.JsonResult<java.lang.Object>
     * @author yoko
     * @date 2019/11/7 16:58
     * local:http://localhost:8086/fine/cd/getCd
     * 请求的属性类:RequestConsumer
     * 必填字段:{"phoneNum":"15967171415","vType":1,"agtVer":1,"clientVer":1,"ctime":201911071802959,"cctime":201911071802959,"sign":"abcdefg","token":"111111"}
     * 加密值:eyJwaG9uZU51bSI6IjE1OTY3MTcxNDE1IiwidlR5cGUiOjEsImFndFZlciI6MSwiY2xpZW50VmVyIjoxLCJjdGltZSI6MjAxOTExMDcxODAyOTU5LCJjY3RpbWUiOjIwMTkxMTA3MTgwMjk1OSwic2lnbiI6ImFiY2RlZmciLCJ0b2tlbiI6IjExMTExMSJ9
     * 客户端加密字段:ctime+cctime+token+秘钥=sign
     * 服务端加密字段:stime+token+秘钥=sign
     */
    @RequestMapping(value = "/getCd", method = {RequestMethod.POST})
    public JsonResult<Object> getDataMList(HttpServletRequest request, HttpServletResponse response, @RequestBody RequestEncryptionJson requestData) throws Exception{
        String sgid = ComponentUtil.redisIdService.getNewId();
        String cgid = "";
        String token;
        String ip = StringUtil.getIpAddress(request);
        String data = "";
        long did = 0;

        RequestVcode requestModel = new RequestVcode();
        try{
            // 解密
            data = StringUtil.decoderBase64(requestData.jsonData);
            requestModel  = JSON.parseObject(data, RequestVcode.class);

            // check校验数据、校验用户是否登录、获得用户ID
            did = HodgepodgeMethod.checkGetCd(requestModel);

            token = requestModel.token;

            // #判断是否属于注册，如果属于注册，则需要校验一下这个手机号之前是否注册过

            // 判断是否频繁请求发送验证码
            String strKeyCache = CachedKeyUtils.getCacheKey(CacheKey.PHONE_VCODE, requestModel.vType, requestModel.phoneNum);
            HodgepodgeMethod.checkOftenSendCode(strKeyCache);
            // 校验ctime
            // 校验sign


            // 发送验证码
            String strCode = SendSmsUtils.sendSmsCode(strKeyCache, requestModel.phoneNum);
            HodgepodgeMethod.checkSendCode(strCode);
            // 组装返回客户端的数据
            long stime = System.currentTimeMillis();
            String sign = SignUtil.getSgin(stime, token, secretKeySign); // 服务器时间+token+秘钥=sign
            String strData = HodgepodgeMethod.assembleResult(stime, token, sign);
            // 数据加密
            String encryptionData = StringUtil.mergeCodeBase64(strData);
            ResponseEncryptionJson resultDataModel = new ResponseEncryptionJson();
            resultDataModel.jsonData = encryptionData;
            // 返回数据给客户端
            return JsonResult.successResult(resultDataModel, cgid, sgid);
        }catch (Exception e){
            Map<String,String> map = ExceptionMethod.getException(e, ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_TWO);
            log.error(String.format("this VcodeController.getCd() is error , the cgid=%s and sgid=%s and all data=%s!", cgid, sgid, data));
            e.printStackTrace();
            return JsonResult.failedResult(map.get("message"), map.get("code"), cgid, sgid);
        }
    }
}
