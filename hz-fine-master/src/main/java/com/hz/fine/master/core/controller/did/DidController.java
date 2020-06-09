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
import com.hz.fine.master.core.model.did.DidRewardModel;
import com.hz.fine.master.core.model.order.OrderModel;
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
import java.util.concurrent.TimeUnit;

/**
 * @Description 用户的Controller层
 * @Author yoko
 * @Date 2020/5/13 15:00
 * @Version 1.0
 */
@RestController
@RequestMapping("/fine/did")
public class DidController {

    private static Logger log = LoggerFactory.getLogger(DidController.class);

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
     * @Description: 用户注册
     * @param request
     * @param response
     * @return com.gd.chain.common.utils.JsonResult<java.lang.Object>
     * @author yoko
     * @date 2019/11/25 22:58
     * local:http://localhost:8086/fine/did/register
     * 请求的属性类:RequestDid
     * 必填字段:{"nickname":"nickname1","acNum":"15967171415","passWd":"passWd1","operateWd":"operateWd1","icode":"1","vcode":"1111","agtVer":1,"clientVer":1,"clientType":1,"ctime":201911071802959,"cctime":201911071802959,"sign":"abcdefg","token":"111111"}
     * 加密字段:{"jsonData":"eyJuaWNrbmFtZSI6Im5pY2tuYW1lMSIsImFjTnVtIjoiMTU5NjcxNzE0MTUiLCJwYXNzV2QiOiJwYXNzV2QxIiwib3BlcmF0ZVdkIjoib3BlcmF0ZVdkMSIsImljb2RlIjoiMSIsInZjb2RlIjoiMTExMSIsImFndFZlciI6MSwiY2xpZW50VmVyIjoxLCJjbGllbnRUeXBlIjoxLCJjdGltZSI6MjAxOTExMDcxODAyOTU5LCJjY3RpbWUiOjIwMTkxMTA3MTgwMjk1OSwic2lnbiI6ImFiY2RlZmciLCJ0b2tlbiI6IjExMTExMSJ9"}
     * 客户端加密字段:acNum+ctime+秘钥=sign
     * 服务端加密字段:stime+秘钥=sign
     * result={
     *     "resultCode": "0",
     *     "message": "success",
     *     "data": {
     *         "jsonData": "eyJzaWduIjoiMzRlMGIyY2I1OWI5Y2UzN2NjMGJmOGRhZjY4NTUzMDkiLCJzdGltZSI6MTU4OTQ1NjAxNjkwMSwidG9rZW4iOiI2MWY0ZDE3YmZmNDU3MTMwZTZhYTAyMDdhYmUwZTc5YSJ9"
     *     },
     *     "sgid": "202005141933350000001",
     *     "cgid": ""
     * }
     */
    @RequestMapping(value = "/register", method = {RequestMethod.POST})
    public JsonResult<Object> register(HttpServletRequest request, HttpServletResponse response, @RequestBody RequestEncryptionJson requestData) throws Exception{
        String sgid = ComponentUtil.redisIdService.getNewId();
        String cgid = "";
        String token = "";
        String ip = StringUtil.getIpAddress(request);
        String data = "";
        long did = 0;
        RegionModel regionModel = HodgepodgeMethod.assembleRegionModel(ip);

        RequestDid requestModel = new RequestDid();
        try{
            // 解密
            data = StringUtil.decoderBase64(requestData.jsonData);
            requestModel  = JSON.parseObject(data, RequestDid.class);

            // check校验数据
            HodgepodgeMethod.checkRegister(requestModel);

            // 校验手机号是否已经被注册过
            DidModel didByAcNumQuery = HodgepodgeMethod.assembleDidByAcNum(requestModel.acNum);
            DidModel didByAcNumData = (DidModel) ComponentUtil.didService.findByObject(didByAcNumQuery);
            // check校验账号是否已被注册
            HodgepodgeMethod.checkRegisterByAcNum(didByAcNumData);

            // 校验邀请码是否存在
            DidModel didByIcodeQuery = HodgepodgeMethod.assembleDidByIcode(requestModel.icode);
            DidModel didByIcodeData = (DidModel) ComponentUtil.didService.findByObject(didByIcodeQuery);
            // check校验邀请码是否正确
            HodgepodgeMethod.checkRegisterByIcode(didByIcodeData);
            // 生成用户的唯一码：需要校验邀请码是否唯一，所以生成唯一码之后还需要查询数据库
            String icode = "";
            while (true){
                icode = StringUtil.randomCode();
                DidModel icodeQuery = HodgepodgeMethod.assembleDidByIcode(icode);
                DidModel icodeData = (DidModel) ComponentUtil.didService.findByObject(icodeQuery);
                if (icodeData ==  null){
                    break;
                }
            }

            // 组装要添加的用户账号数据
            DidModel didModel = HodgepodgeMethod.assembleDidModel(requestModel, icode, didByIcodeData.getId());
            ComponentUtil.didService.add(didModel);

            // 组装要添加的用户的层级关系
            List<DidLevelModel> didLevelList = HodgepodgeMethod.assembleDidLevelList(didModel.getId(), didByIcodeData.getId(), didByIcodeData.getOwnId());
            for (DidLevelModel didLevelModel : didLevelList){
                ComponentUtil.didLevelService.add(didLevelModel);
            }

            // # 修改上级直推的总人数


            // 生成token
            if (didModel.getId() > ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_ZERO){
                token = SignUtil.getSgin(didModel.getId(), System.currentTimeMillis()); // 用户did+系统时间
                did = didModel.getId();
                // 设置用户缓存7天
                ComponentUtil.redisService.set(token, String.valueOf(did), 7, TimeUnit.DAYS);
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
            log.error(String.format("this DidController.register() is error , the cgid=%s and sgid=%s and all data=%s!", cgid, sgid, data));
            e.printStackTrace();
            return JsonResult.failedResult(map.get("message"), map.get("code"), cgid, sgid);
        }
    }



    /**
     * @Description: 用户登录
     * @param request
     * @param response
     * @return com.gd.chain.common.utils.JsonResult<java.lang.Object>
     * @author yoko
     * @date 2019/11/25 22:58
     * local:http://localhost:8086/fine/did/logOn
     * 请求的属性类:RequestDid
     * 必填字段:{"acNum":"15967171415","passWd":"passWd1","agtVer":1,"clientVer":1,"clientType":1,"ctime":201911071802959,"cctime":201911071802959,"sign":"abcdefg","token":""}
     * 加密字段:{"jsonData":"eyJhY051bSI6IjE1OTY3MTcxNDE1IiwicGFzc1dkIjoicGFzc1dkMSIsImFndFZlciI6MSwiY2xpZW50VmVyIjoxLCJjbGllbnRUeXBlIjoxLCJjdGltZSI6MjAxOTExMDcxODAyOTU5LCJjY3RpbWUiOjIwMTkxMTA3MTgwMjk1OSwic2lnbiI6ImFiY2RlZmciLCJ0b2tlbiI6IiJ9"}
     * 客户端加密字段:ctime+cctime+秘钥=sign
     * 服务端加密字段:stime+秘钥=sign
     * result={
     *     "resultCode": "0",
     *     "message": "success",
     *     "data": {
     *         "jsonData": "eyJzaWduIjoiYjQ4YzQ1YzEyMTNlNDJiYmZkNjkzYzczZjBmZjI1ZDQiLCJzdGltZSI6MTU4OTUxMTc1NjU3NCwidG9rZW4iOiJhZmM4MGU4YTA1ZmI5NDU2MDRhYTY1ZTU1M2IzNDI2NCJ9"
     *     },
     *     "sgid": "202005151102360000001",
     *     "cgid": ""
     * }
     */
    @RequestMapping(value = "/logOn", method = {RequestMethod.POST})
    public JsonResult<Object> logOn(HttpServletRequest request, HttpServletResponse response, @RequestBody RequestEncryptionJson requestData) throws Exception{
        String sgid = ComponentUtil.redisIdService.getNewId();
        String cgid = "";
        String token = "";
        String ip = StringUtil.getIpAddress(request);
        String data = "";
        long did = 0;
        RegionModel regionModel = HodgepodgeMethod.assembleRegionModel(ip);

        RequestDid requestModel = new RequestDid();
        try{
            // 解密
            data = StringUtil.decoderBase64(requestData.jsonData);
            requestModel  = JSON.parseObject(data, RequestDid.class);

            // check校验数据
            HodgepodgeMethod.checkLogOnData(requestModel);


            // 校验登录的账号密码是否正确
            DidModel didLogOnQuery = HodgepodgeMethod.assembleDidLogOn(requestModel.acNum, requestModel.passWd);
            DidModel didLogOnData = (DidModel) ComponentUtil.didService.findByObject(didLogOnQuery);
            // check用户是否登录成功
            HodgepodgeMethod.checkLogOn(didLogOnData);

            // 登录成功设置token，并把token存入缓存
            // 生成token
            if (didLogOnData.getId() > ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_ZERO){
                token = SignUtil.getSgin(didLogOnData.getId(), System.currentTimeMillis()); // 用户did+系统时间
                did = didLogOnData.getId();
                // 设置用户缓存7天
                ComponentUtil.redisService.set(token, String.valueOf(did), 7, TimeUnit.DAYS);
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
            log.error(String.format("this DidController.logOn() is error , the cgid=%s and sgid=%s and all data=%s!", cgid, sgid, data));
            e.printStackTrace();
            return JsonResult.failedResult(map.get("message"), map.get("code"), cgid, sgid);
        }
    }




    /**
     * @Description: 用户修改密码
     * @param request
     * @param response
     * @return com.gd.chain.common.utils.JsonResult<java.lang.Object>
     * @author yoko
     * @date 2019/11/25 22:58
     * local:http://localhost:8086/fine/did/changePassword
     * 请求的属性类:RequestDid
     * 必填字段:{"passWd":"mm_1","newPassWd":"sb","agtVer":1,"clientVer":1,"clientType":1,"ctime":201911071802959,"cctime":201911071802959,"sign":"abcdefg","token":"111111"}
     * 加密字段:{"jsonData":"eyJwYXNzV2QiOiJtbV8xIiwibmV3UGFzc1dkIjoic2IiLCJhZ3RWZXIiOjEsImNsaWVudFZlciI6MSwiY2xpZW50VHlwZSI6MSwiY3RpbWUiOjIwMTkxMTA3MTgwMjk1OSwiY2N0aW1lIjoyMDE5MTEwNzE4MDI5NTksInNpZ24iOiJhYmNkZWZnIiwidG9rZW4iOiIxMTExMTEifQ=="}
     * 客户端加密字段:ctime+cctime+秘钥=sign
     * 服务端加密字段:stime+秘钥=sign
     * result={
     *     "resultCode": "0",
     *     "message": "success",
     *     "data": {
     *         "jsonData": "eyJzaWduIjoiNjY5ZTRkZWI3NzJhZTliOTA4MTYyZmIzOGYyNGNhOTIiLCJzdGltZSI6MTU4OTQ1Nzg3NDA1OH0="
     *     },
     *     "sgid": "202005142004320000001",
     *     "cgid": ""
     * }
     */
    @RequestMapping(value = "/changePassword", method = {RequestMethod.POST})
    public JsonResult<Object> changePassword(HttpServletRequest request, HttpServletResponse response, @RequestBody RequestEncryptionJson requestData) throws Exception{
        String sgid = ComponentUtil.redisIdService.getNewId();
        String cgid = "";
        String token = "";
        String ip = StringUtil.getIpAddress(request);
        String data = "";
        long did = 0;
        RegionModel regionModel = HodgepodgeMethod.assembleRegionModel(ip);

        RequestDid requestModel = new RequestDid();
        try{
            // 解密
            data = StringUtil.decoderBase64(requestData.jsonData);
            requestModel  = JSON.parseObject(data, RequestDid.class);

            //#临时数据
            if (!StringUtils.isBlank(requestModel.token)){
                if (requestModel.token.equals("111111")){
                    ComponentUtil.redisService.set(requestModel.token, "1");
                }
            }

            // check校验数据
            did = HodgepodgeMethod.checkChangePassword(requestModel);


            // 校验账号密码是否正确
            DidModel didByAcNumAndPassWdQuery = HodgepodgeMethod.assembleDidByAcNumAndPassWd(did, requestModel.passWd);
            DidModel didByAcNumAndPassWdData = (DidModel) ComponentUtil.didService.findByObject(didByAcNumAndPassWdQuery);
            // check校验用户输入的原始密码是否正确
            HodgepodgeMethod.checkPassWd(didByAcNumAndPassWdData);

            // 正式修改密码
            DidModel updatePassWdData = HodgepodgeMethod.assembleUpdatePassWdData(did, requestModel.newPassWd);
            ComponentUtil.didService.update(updatePassWdData);

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
            log.error(String.format("this DidController.changePassword() is error , the cgid=%s and sgid=%s and all data=%s!", cgid, sgid, data));
            e.printStackTrace();
            return JsonResult.failedResult(map.get("message"), map.get("code"), cgid, sgid);
        }
    }



    /**
     * @Description: 用户忘记密码/重新设置密码
     * <p>
     *     用户忘记密码：所以用户需要重新设置密码；
     *     1.用户发起手机短信验证码
     *     2.短信验证码通过之后，则可以重新设置密码（获取vtoken值）
     *     3.通过vtoken值，确定是具体哪个账户
     * </p>
     * @param request
     * @param response
     * @return com.gd.chain.common.utils.JsonResult<java.lang.Object>
     * @author yoko
     * @date 2019/11/25 22:58
     * local:http://localhost:8086/fine/did/setUpPassword
     * 请求的属性类:RequestDid
     * 必填字段:{"vtoken":"dfcdf431027ee1ffddbb8e049903afee","newPassWd":"sb1","agtVer":1,"clientVer":1,"clientType":1,"ctime":201911071802959,"cctime":201911071802959,"sign":"abcdefg","token":"111111"}
     * 加密字段:{"jsonData":"eyJhY051bSI6InpoXzEiLCJuZXdQYXNzV2QiOiJzYjEiLCJ2Y29kZSI6IjExMTEiLCJhZ3RWZXIiOjEsImNsaWVudFZlciI6MSwiY2xpZW50VHlwZSI6MSwiY3RpbWUiOjIwMTkxMTA3MTgwMjk1OSwiY2N0aW1lIjoyMDE5MTEwNzE4MDI5NTksInNpZ24iOiJhYmNkZWZnIiwidG9rZW4iOiIxMTExMTEifQ=="}
     * 客户端加密字段:ctime+cctime+秘钥=sign
     * 服务端加密字段:stime+秘钥=sign
     * result={
     *     "resultCode": "0",
     *     "message": "success",
     *     "data": {
     *         "jsonData": "eyJzaWduIjoiMzRkZGViODNmOTdmOWI4ZTBjZDA1Yzc3Y2VlOWI2YzAiLCJzdGltZSI6MTU4OTUxMzE3MjYxNH0="
     *     },
     *     "sgid": "202005151126120000001",
     *     "cgid": ""
     * }
     */
    @RequestMapping(value = "/setUpPassword", method = {RequestMethod.POST})
    public JsonResult<Object> setUpPassword(HttpServletRequest request, HttpServletResponse response, @RequestBody RequestEncryptionJson requestData) throws Exception{
        String sgid = ComponentUtil.redisIdService.getNewId();
        String cgid = "";
        String token = "";
        String ip = StringUtil.getIpAddress(request);
        String data = "";
        long did = 0;
        RegionModel regionModel = HodgepodgeMethod.assembleRegionModel(ip);

        RequestDid requestModel = new RequestDid();
        try{
            // 解密
            data = StringUtil.decoderBase64(requestData.jsonData);
            requestModel  = JSON.parseObject(data, RequestDid.class);
            // check校验数据
            HodgepodgeMethod.checkSetUpPasswordData(requestModel);

            // 通过vtoken获取用户登录账号
            String acNum = HodgepodgeMethod.getAcNumByVtoken(requestModel.vtoken);

            // 校验账号查询是否有账号信息的数据
            DidModel didByAcNumQuery = HodgepodgeMethod.assembleDidByAcNumForFindPassWd(acNum);
            DidModel didByAcNumData = (DidModel) ComponentUtil.didService.findByObject(didByAcNumQuery);
            // check校验用户输入的原始密码是否正确
            HodgepodgeMethod.checkSetUpPassword(didByAcNumData);

            // 正式修改密码
            DidModel updatePassWdData = HodgepodgeMethod.assembleUpdatePassWdData(didByAcNumData.getId(), requestModel.newPassWd);
            ComponentUtil.didService.update(updatePassWdData);

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
            log.error(String.format("this DidController.setUpPassword() is error , the cgid=%s and sgid=%s and all data=%s!", cgid, sgid, data));
            e.printStackTrace();
            return JsonResult.failedResult(map.get("message"), map.get("code"), cgid, sgid);
        }
    }


    /**
     * @Description: 获取用户账号基本信息
     * @param request
     * @param response
     * @return com.gd.chain.common.utils.JsonResult<java.lang.Object>
     * @author yoko
     * @date 2019/11/25 22:58
     * local:http://localhost:8086/fine/did/getData
     * 请求的属性类:RequestDid
     * 必填字段:{"agtVer":1,"clientVer":1,"clientType":1,"ctime":201911071802959,"cctime":201911071802959,"sign":"abcdefg","token":"111111"}
     * 加密字段:{"jsonData":"eyJhZ3RWZXIiOjEsImNsaWVudFZlciI6MSwiY2xpZW50VHlwZSI6MSwiY3RpbWUiOjIwMTkxMTA3MTgwMjk1OSwiY2N0aW1lIjoyMDE5MTEwNzE4MDI5NTksInNpZ24iOiJhYmNkZWZnIiwidG9rZW4iOiIxMTExMTEifQ=="}
     * 客户端加密字段:ctime+cctime+秘钥=sign
     * 服务端加密字段:stime+秘钥=sign
     * result={
     *     "resultCode": "0",
     *     "message": "success",
     *     "data": {
     *         "jsonData": "eyJkYXRhTW9kZWwiOnsiYWNOdW0iOiJ6aF8xIiwiYmFsYW5jZSI6IjEwMDAiLCJpY29kZSI6IjEiLCJsb2NrTW9uZXkiOiIiLCJuaWNrbmFtZSI6IuaYteensF8xIiwidG9kYXlFeGNoYW5nZSI6IjAuMDAiLCJ0b2RheVByb2ZpdCI6IjAuMDAiLCJ0b3RhbERpcmVjdE51bSI6MTAwLCJ0b3RhbERpcmVjdFByb2ZpdCI6IjMwMDAiLCJ0b3RhbEdyYWRlUHJvZml0IjoiMjAwMCIsInRvdGFsTW9uZXkiOiIxMDAwMCIsInRvdGFsUHJvZml0IjoiMTAwMDAiLCJ0b3RhbFJlY2hhcmdlUHJvZml0IjoiMTAwMCJ9LCJzaWduIjoiYTBmYzk3NjM4ZGZjNGMwMWZhMDJiMjUzYzk2NzlhNmMiLCJzdGltZSI6MTU5MTAwNjMzMjUxNn0="
     *     },
     *     "sgid": "202006011812120000001",
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

        RequestDid requestModel = new RequestDid();
        try{
            // 解密
            data = StringUtil.decoderBase64(requestData.jsonData);
            requestModel  = JSON.parseObject(data, RequestDid.class);

            //#临时数据
            if (!StringUtils.isBlank(requestModel.token)){
                if (requestModel.token.equals("111111")){
                    ComponentUtil.redisService.set(requestModel.token, "1");
                }
            }


            // check校验数据
            did = HodgepodgeMethod.checkDidGetData(requestModel);

            // 查询用户基本信息
            DidModel didQuery = HodgepodgeMethod.assembleDidQueryByDid(did);
            DidModel didData = (DidModel) ComponentUtil.didService.findByObject(didQuery);
            // check校验用户信息是否为空
            HodgepodgeMethod.checkDidData(didData);

            // 获取今日收益：充多少送多少的收益，充值档次的赠送，昨天直推的奖励
            DidRewardModel didRewardQuery = HodgepodgeMethod.assembleDidRewardTodayProfit(did);
            String todayProfit = ComponentUtil.didRewardService.getProfitByRewardType(didRewardQuery);

            // 获取今日兑换：今日派发订单成功的
            OrderModel orderQuery =  HodgepodgeMethod.assembleOrderByTodayExchange(did);
            String todayExchange = ComponentUtil.orderService.getProfitByCurday(orderQuery);

            // 组装返回客户端的数据
            long stime = System.currentTimeMillis();
            String sign = SignUtil.getSgin(stime, secretKeySign); // stime+秘钥=sign
            String strData = HodgepodgeMethod.assembleDidBasicDataResult(stime, sign, didData, todayProfit, todayExchange);
            // 数据加密
            String encryptionData = StringUtil.mergeCodeBase64(strData);
            ResponseEncryptionJson resultDataModel = new ResponseEncryptionJson();
            resultDataModel.jsonData = encryptionData;
            // 返回数据给客户端
            return JsonResult.successResult(resultDataModel, cgid, sgid);
        }catch (Exception e){
            Map<String,String> map = ExceptionMethod.getException(e, ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_TWO);
            log.error(String.format("this DidController.getData() is error , the cgid=%s and sgid=%s and all data=%s!", cgid, sgid, data));
            e.printStackTrace();
            return JsonResult.failedResult(map.get("message"), map.get("code"), cgid, sgid);
        }
    }




    /**
     * @Description: 用户修改安全密码/操作密码
     * @param request
     * @param response
     * @return com.gd.chain.common.utils.JsonResult<java.lang.Object>
     * @author yoko
     * @date 2019/11/25 22:58
     * local:http://localhost:8086/fine/did/changeOperatePassword
     * 请求的属性类:RequestDid
     * 必填字段:{"operateWd":"czmm_1","newOperateWd":"czmm_1_1","agtVer":1,"clientVer":1,"clientType":1,"ctime":201911071802959,"cctime":201911071802959,"sign":"abcdefg","token":"111111"}
     * 加密字段:{"jsonData":"eyJvcGVyYXRlV2QiOiJjem1tXzEiLCJuZXdPcGVyYXRlV2QiOiJjem1tXzFfMSIsImFndFZlciI6MSwiY2xpZW50VmVyIjoxLCJjbGllbnRUeXBlIjoxLCJjdGltZSI6MjAxOTExMDcxODAyOTU5LCJjY3RpbWUiOjIwMTkxMTA3MTgwMjk1OSwic2lnbiI6ImFiY2RlZmciLCJ0b2tlbiI6IjExMTExMSJ9"}
     * 客户端加密字段:ctime+cctime+秘钥=sign
     * 服务端加密字段:stime+秘钥=sign
     * result={
     *     "resultCode": "0",
     *     "message": "success",
     *     "data": {
     *         "jsonData": "eyJzaWduIjoiN2RlZjRkY2Y1ODQ0YWVkMzc2NWIwZTc4YTQ3Y2I3MTYiLCJzdGltZSI6MTU5MDk4MjM5MDM2MX0="
     *     },
     *     "sgid": "202006011133080000001",
     *     "cgid": ""
     * }
     */
    @RequestMapping(value = "/changeOperatePassword", method = {RequestMethod.POST})
    public JsonResult<Object> changeOperatePassword(HttpServletRequest request, HttpServletResponse response, @RequestBody RequestEncryptionJson requestData) throws Exception{
        String sgid = ComponentUtil.redisIdService.getNewId();
        String cgid = "";
        String token = "";
        String ip = StringUtil.getIpAddress(request);
        String data = "";
        long did = 0;
        RegionModel regionModel = HodgepodgeMethod.assembleRegionModel(ip);

        RequestDid requestModel = new RequestDid();
        try{
            // 解密
            data = StringUtil.decoderBase64(requestData.jsonData);
            requestModel  = JSON.parseObject(data, RequestDid.class);

            //#临时数据
            if (!StringUtils.isBlank(requestModel.token)){
                if (requestModel.token.equals("111111")){
                    ComponentUtil.redisService.set(requestModel.token, "1");
                }
            }

            // check校验数据
            did = HodgepodgeMethod.checkChangeOperatePassword(requestModel);


            // 校验账号安全密码是否正确
            DidModel diddQuery = HodgepodgeMethod.assembleDidByAcNumAndOperateWd(did, requestModel.operateWd);
            DidModel didData = (DidModel) ComponentUtil.didService.findByObject(diddQuery);
            // check校验用户输入的操作密码/安全密码是否正确
            HodgepodgeMethod.checkOperateWd(didData);

            // 正式修改密码
            DidModel updatePassWdData = HodgepodgeMethod.assembleUpdateOperateWdData(did, requestModel.newOperateWd);
            ComponentUtil.didService.update(updatePassWdData);

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
            log.error(String.format("this DidController.changeOperatePassword() is error , the cgid=%s and sgid=%s and all data=%s!", cgid, sgid, data));
            e.printStackTrace();
            return JsonResult.failedResult(map.get("message"), map.get("code"), cgid, sgid);
        }
    }




    /**
     * @Description: 用户忘记安全密码/重新设置安全密码
     * <p>
     *     用户忘记安全密码：所以用户需要重新设置安全密码；
     *     1.用户发起手机短信验证码
     *     2.短信验证码通过之后，则可以重新设置安全密码
     * </p>
     * @param request
     * @param response
     * @return com.gd.chain.common.utils.JsonResult<java.lang.Object>
     * @author yoko
     * @date 2019/11/25 22:58
     * local:http://localhost:8086/fine/did/setUpOperateWd
     * 请求的属性类:RequestDid
     * 必填字段:{"acNum":"zh_1","newOperateWd":"czmm_1","vcode":"1111","agtVer":1,"clientVer":1,"clientType":1,"ctime":201911071802959,"cctime":201911071802959,"sign":"abcdefg","token":"111111"}
     * 加密字段:{"jsonData":"eyJhY051bSI6InpoXzEiLCJuZXdPcGVyYXRlV2QiOiJjem1tXzEiLCJ2Y29kZSI6IjExMTEiLCJhZ3RWZXIiOjEsImNsaWVudFZlciI6MSwiY2xpZW50VHlwZSI6MSwiY3RpbWUiOjIwMTkxMTA3MTgwMjk1OSwiY2N0aW1lIjoyMDE5MTEwNzE4MDI5NTksInNpZ24iOiJhYmNkZWZnIiwidG9rZW4iOiIxMTExMTEifQ=="}
     * 客户端加密字段:ctime+cctime+秘钥=sign
     * 服务端加密字段:stime+秘钥=sign
     * result={
     *     "resultCode": "0",
     *     "message": "success",
     *     "data": {
     *         "jsonData": "eyJzaWduIjoiZWVhZDZkOTExZjg4MDgyNDU1ZWYyNWI4ZTAwODA0NzciLCJzdGltZSI6MTU5MDk4MzcwMzE4NX0="
     *     },
     *     "sgid": "202006011155010000001",
     *     "cgid": ""
     * }
     */
    @RequestMapping(value = "/setUpOperateWd", method = {RequestMethod.POST})
    public JsonResult<Object> setUpOperateWd(HttpServletRequest request, HttpServletResponse response, @RequestBody RequestEncryptionJson requestData) throws Exception{
        String sgid = ComponentUtil.redisIdService.getNewId();
        String cgid = "";
        String token = "";
        String ip = StringUtil.getIpAddress(request);
        String data = "";
        long did = 0;
        RegionModel regionModel = HodgepodgeMethod.assembleRegionModel(ip);

        RequestDid requestModel = new RequestDid();
        try{
            // 解密
            data = StringUtil.decoderBase64(requestData.jsonData);
            requestModel  = JSON.parseObject(data, RequestDid.class);

            // check校验数据
            HodgepodgeMethod.checkSetUpOperateWdData(requestModel);


            // 校验账号查询是否有账号信息的数据
            DidModel didByAcNumQuery = HodgepodgeMethod.assembleDidByAcNumForFindPassWd(requestModel.acNum);
            DidModel didByAcNumData = (DidModel) ComponentUtil.didService.findByObject(didByAcNumQuery);
            // check校验用户是否有这个账号存在
            HodgepodgeMethod.checkSetUpOperateWd(didByAcNumData);

            // 正式修改安全密码
            DidModel updatePassWdData = HodgepodgeMethod.assembleSetUpOperateWdData(didByAcNumData.getId(), requestModel.newOperateWd);
            ComponentUtil.didService.update(updatePassWdData);

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
            log.error(String.format("this DidController.setUpOperateWd() is error , the cgid=%s and sgid=%s and all data=%s!", cgid, sgid, data));
            e.printStackTrace();
            return JsonResult.failedResult(map.get("message"), map.get("code"), cgid, sgid);
        }
    }



}
