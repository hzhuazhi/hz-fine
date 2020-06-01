package com.hz.fine.master.core.controller.did;

import com.alibaba.fastjson.JSON;
import com.hz.fine.master.core.common.exception.ExceptionMethod;
import com.hz.fine.master.core.common.utils.JsonResult;
import com.hz.fine.master.core.common.utils.SignUtil;
import com.hz.fine.master.core.common.utils.StringUtil;
import com.hz.fine.master.core.common.utils.constant.ServerConstant;
import com.hz.fine.master.core.model.RequestEncryptionJson;
import com.hz.fine.master.core.model.ResponseEncryptionJson;
import com.hz.fine.master.core.model.did.DidRechargeModel;
import com.hz.fine.master.core.model.did.DidRewardModel;
import com.hz.fine.master.core.protocol.request.did.recharge.RequestDidRecharge;
import com.hz.fine.master.core.protocol.request.did.reward.RequestReward;
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
 * @Description 用户奖励纪录的Controller层
 * @Author yoko
 * @Date 2020/5/21 17:36
 * @Version 1.0
 */
@RestController
@RequestMapping("/fine/reward")
public class DidRewardController {

    private static Logger log = LoggerFactory.getLogger(DidRewardController.class);

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

    @Value("${secret.key.token}")
    private String secretKeyToken;

    @Value("${secret.key.sign}")
    private String secretKeySign;



    /**
     * @Description: 用户奖励纪录-集合
     * @param request
     * @param response
     * @return com.gd.chain.common.utils.JsonResult<java.lang.Object>
     * @author yoko
     * @date 2019/11/25 22:58
     * local:http://localhost:8086/fine/reward/getDataList
     * 请求的属性类:RequestReward
     * 必填字段:{"rewardType":1,"curdayStart":20200521,"curdayEnd":20200521,"agtVer":1,"clientVer":1,"clientType":1,"ctime":201911071802959,"cctime":201911071802959,"sign":"abcdefg","pageNumber":1,"pageSize":3,"token":"111111"}
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

        RequestReward requestModel = new RequestReward();
        try{
            // 解密
            data = StringUtil.decoderBase64(requestData.jsonData);
            requestModel  = JSON.parseObject(data, RequestReward.class);
            //#临时数据
            if (!StringUtils.isBlank(requestModel.token)){
                if (requestModel.token.equals("111111")){
                    ComponentUtil.redisService.set(requestModel.token, "1");
                }
            }
            // check校验数据
            did = HodgepodgeMethod.checkDidRewardListData(requestModel);

            // 获取用户充值订单记录集合数据
            DidRewardModel didRewardModelQuery = HodgepodgeMethod.assembleDidRewardListByDid(requestModel, did);
            List<DidRewardModel> didRechargeList = ComponentUtil.didRewardService.queryByList(didRewardModelQuery);
            // 组装返回客户端的数据
            long stime = System.currentTimeMillis();
            String sign = SignUtil.getSgin(stime, secretKeySign); // stime+秘钥=sign
            String strData = HodgepodgeMethod.assembleDidRewardListResult(stime, sign, didRechargeList, didRewardModelQuery.getRowCount());
            // 数据加密
            String encryptionData = StringUtil.mergeCodeBase64(strData);
            ResponseEncryptionJson resultDataModel = new ResponseEncryptionJson();
            resultDataModel.jsonData = encryptionData;
            // 返回数据给客户端
            return JsonResult.successResult(resultDataModel, cgid, sgid);
        }catch (Exception e){
            Map<String,String> map = ExceptionMethod.getException(e, ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_TWO);
            // #添加异常
            log.error(String.format("this DidRewardController.getDataList() is error , the cgid=%s and sgid=%s and all data=%s!", cgid, sgid, data));
            e.printStackTrace();
            return JsonResult.failedResult(map.get("message"), map.get("code"), cgid, sgid);
        }
    }




    /**
     * @Description: 用户奖励纪录-详情
     * @param request
     * @param response
     * @return com.gd.chain.common.utils.JsonResult<java.lang.Object>
     * @author yoko
     * @date 2019/11/25 22:58
     * local:http://localhost:8086/fine/reward/getData
     * 请求的属性类:RequestReward
     * 必填字段:{"id":1,"agtVer":1,"clientVer":1,"clientType":1,"ctime":201911071802959,"cctime":201911071802959,"sign":"abcdefg","token":"111111"}
     * 加密字段:{"jsonData":"eyJpZCI6MSwiYWd0VmVyIjoxLCJjbGllbnRWZXIiOjEsImNsaWVudFR5cGUiOjEsImN0aW1lIjoyMDE5MTEwNzE4MDI5NTksImNjdGltZSI6MjAxOTExMDcxODAyOTU5LCJzaWduIjoiYWJjZGVmZyIsInRva2VuIjoiMTExMTExIn0="}
     * 客户端加密字段:id+ctime+cctime+秘钥=sign
     * 服务端加密字段:stime+秘钥=sign
     * result={
     *     "resultCode": "0",
     *     "message": "success",
     *     "data": {
     *         "jsonData": "eyJkYXRhTW9kZWwiOnsiY3JlYXRlVGltZSI6IjIwMjAtMDUtMjEgMTg6MDk6MjMiLCJpZCI6MSwibW9uZXkiOiIxMCIsIm9yZGVyTm8iOiJvcmRlcl9ub18xIiwib3JpZ2luIjoiMTAwMCIsInByb29mIjoiMTAwMCIsInJld2FyZFR5cGUiOjF9LCJzaWduIjoiNTA1NzQ2MWRjOGNkMzY0NGNmMzBhM2U4OWI5NTE0MDMiLCJzdGltZSI6MTU5MDA1NjUxNTkwN30="
     *     },
     *     "sgid": "202005211821540000001",
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

        RequestReward requestModel = new RequestReward();
        try{
            // 解密
            data = StringUtil.decoderBase64(requestData.jsonData);
            requestModel  = JSON.parseObject(data, RequestReward.class);
            //#临时数据
            if (!StringUtils.isBlank(requestModel.token)){
                if (requestModel.token.equals("111111")){
                    ComponentUtil.redisService.set(requestModel.token, "1");
                }
            }
            // check校验请求的数据
            did = HodgepodgeMethod.checkDidRewardData(requestModel);

            // 用户充值订单记录详情数据
            DidRewardModel didRewardModelQuery = HodgepodgeMethod.assembleDidRewardByDidAndId(did, requestModel.id);
            DidRewardModel didRechargeModelData = (DidRewardModel) ComponentUtil.didRewardService.findByObject(didRewardModelQuery);
            // 组装返回客户端的数据
            long stime = System.currentTimeMillis();
            String sign = SignUtil.getSgin(stime, secretKeySign); // stime+秘钥=sign
            String strData = HodgepodgeMethod.assembleDidRewardDataResult(stime, sign, didRechargeModelData);
            // 数据加密
            String encryptionData = StringUtil.mergeCodeBase64(strData);
            ResponseEncryptionJson resultDataModel = new ResponseEncryptionJson();
            resultDataModel.jsonData = encryptionData;
            // 返回数据给客户端
            return JsonResult.successResult(resultDataModel, cgid, sgid);
        }catch (Exception e){
            Map<String,String> map = ExceptionMethod.getException(e, ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_TWO);
            // 添加异常
            log.error(String.format("this DidRewardController.getData() is error , the cgid=%s and sgid=%s and all data=%s!", cgid, sgid, data));
            e.printStackTrace();
            return JsonResult.failedResult(map.get("message"), map.get("code"), cgid, sgid);
        }
    }




    /**
     * @Description: 用户好友分享纪录-集合
     * @param request
     * @param response
     * @return com.gd.chain.common.utils.JsonResult<java.lang.Object>
     * @author yoko
     * @date 2019/11/25 22:58
     * local:http://localhost:8086/fine/reward/getShareDataList
     * 请求的属性类:RequestReward
     * 必填字段:{"agtVer":1,"clientVer":1,"clientType":1,"ctime":201911071802959,"cctime":201911071802959,"sign":"abcdefg","pageNumber":1,"pageSize":3,"token":"111111"}
     * 加密字段:{"jsonData":"eyJhZ3RWZXIiOjEsImNsaWVudFZlciI6MSwiY2xpZW50VHlwZSI6MSwiY3RpbWUiOjIwMTkxMTA3MTgwMjk1OSwiY2N0aW1lIjoyMDE5MTEwNzE4MDI5NTksInNpZ24iOiJhYmNkZWZnIiwicGFnZU51bWJlciI6MSwicGFnZVNpemUiOjMsInRva2VuIjoiMTExMTExIn0="}
     * 客户端加密字段:ctime+秘钥=sign
     * 返回加密字段:stime+秘钥=sign
     * result={
     *     "resultCode": "0",
     *     "message": "success",
     *     "data": {
     *         "jsonData": "eyJyb3dDb3VudCI6NCwic2hhcmVMaXN0IjpbeyJhY051bSI6IjE1OSoqKioxNDE1Iiwibmlja25hbWUiOiJuaWNrbmFtZTUiLCJwcm9maXQiOiIxMDAuMDAiLCJzaGFyZVRpbWUiOiIyMDIwLTA1LTE0IDE5OjMzOjM5In0seyJhY051bSI6IjE1OSoqKioxNDE0Iiwibmlja25hbWUiOiJuaWNrbmFtZTQiLCJwcm9maXQiOiIzNjAuMzYiLCJzaGFyZVRpbWUiOiIyMDIwLTA1LTE0IDE5OjMxOjM0In0seyJhY051bSI6IjE1OSoqKioxNDEzIiwibmlja25hbWUiOiJuaWNrbmFtZTMiLCJwcm9maXQiOiIyMDAuMjAiLCJzaGFyZVRpbWUiOiIyMDIwLTA1LTE0IDE5OjI4OjQwIn1dLCJzaWduIjoiYjU2OTQ1YzIxY2YyNmFiZjM1MmQzZmJjZmE5NzkxYTgiLCJzdGltZSI6MTU5MTAwNDIxOTk4Mn0="
     *     },
     *     "sgid": "202006011736580000001",
     *     "cgid": ""
     * }
     */
    @RequestMapping(value = "/getShareDataList", method = {RequestMethod.POST})
    public JsonResult<Object> getShareDataList(HttpServletRequest request, HttpServletResponse response, @RequestBody RequestEncryptionJson requestData) throws Exception{
        String sgid = ComponentUtil.redisIdService.getNewId();
        String cgid = "";
        String ip = StringUtil.getIpAddress(request);
        String data = "";
        long did = 0;

        RequestReward requestModel = new RequestReward();
        try{
            // 解密
            data = StringUtil.decoderBase64(requestData.jsonData);
            requestModel  = JSON.parseObject(data, RequestReward.class);
            //#临时数据
            if (!StringUtils.isBlank(requestModel.token)){
                if (requestModel.token.equals("111111")){
                    ComponentUtil.redisService.set(requestModel.token, "1");
                }
            }
            // check校验数据
            did = HodgepodgeMethod.checkDidShareRewardListData(requestModel);

            // 获取用户分享奖励记录集合数据
            DidRewardModel didRewardModelQuery = HodgepodgeMethod.assembleDidShareRewardListByDid(requestModel, did);
            List<DidRewardModel> didRechargeList = ComponentUtil.didRewardService.getShareList(didRewardModelQuery);
            // 组装返回客户端的数据
            long stime = System.currentTimeMillis();
            String sign = SignUtil.getSgin(stime, secretKeySign); // stime+秘钥=sign
            String strData = HodgepodgeMethod.assembleDidShareRewardListResult(stime, sign, didRechargeList, didRewardModelQuery.getRowCount());
            // 数据加密
            String encryptionData = StringUtil.mergeCodeBase64(strData);
            ResponseEncryptionJson resultDataModel = new ResponseEncryptionJson();
            resultDataModel.jsonData = encryptionData;
            // 返回数据给客户端
            return JsonResult.successResult(resultDataModel, cgid, sgid);
        }catch (Exception e){
            Map<String,String> map = ExceptionMethod.getException(e, ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_TWO);
            // #添加异常
            log.error(String.format("this DidRewardController.getShareDataList() is error , the cgid=%s and sgid=%s and all data=%s!", cgid, sgid, data));
            e.printStackTrace();
            return JsonResult.failedResult(map.get("message"), map.get("code"), cgid, sgid);
        }
    }


}
