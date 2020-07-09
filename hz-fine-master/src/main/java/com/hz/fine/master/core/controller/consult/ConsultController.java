package com.hz.fine.master.core.controller.consult;

import com.alibaba.fastjson.JSON;
import com.hz.fine.master.core.common.exception.ExceptionMethod;
import com.hz.fine.master.core.common.utils.BeanUtils;
import com.hz.fine.master.core.common.utils.JsonResult;
import com.hz.fine.master.core.common.utils.SignUtil;
import com.hz.fine.master.core.common.utils.StringUtil;
import com.hz.fine.master.core.common.utils.constant.ServerConstant;
import com.hz.fine.master.core.controller.question.QuestionController;
import com.hz.fine.master.core.model.RequestEncryptionJson;
import com.hz.fine.master.core.model.ResponseEncryptionJson;
import com.hz.fine.master.core.model.consult.ConsultAskModel;
import com.hz.fine.master.core.model.consult.ConsultAskReplyModel;
import com.hz.fine.master.core.model.consult.ConsultModel;
import com.hz.fine.master.core.model.question.QuestionMModel;
import com.hz.fine.master.core.model.region.RegionModel;
import com.hz.fine.master.core.protocol.request.consult.RequestConsult;
import com.hz.fine.master.core.protocol.request.question.RequestQuestion;
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
 * @Description 在线客服、咨询的Controller层
 * @Author yoko
 * @Date 2020/7/6 11:22
 * @Version 1.0
 */
@RestController
@RequestMapping("/fine/consult")
public class ConsultController {

    private static Logger log = LoggerFactory.getLogger(QuestionController.class);

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
     * @Description: 在线客服、咨询-类别数据-集合
     * @param request
     * @param response
     * @return com.gd.chain.common.utils.JsonResult<java.lang.Object>
     * @author yoko
     * @date 2019/11/25 22:58
     * local:http://localhost:8086/fine/consult/getDataList
     * 请求的属性类:RequestQuestion
     * 必填字段:{"agtVer":1,"clientVer":1,"clientType":1,"ctime":201911071802959,"cctime":201911071802959,"sign":"abcdefg","pageNumber":1,"pageSize":3,"token":"111111"}
     * 加密字段:{"jsonData":"eyJhZ3RWZXIiOjEsImNsaWVudFZlciI6MSwiY2xpZW50VHlwZSI6MSwiY3RpbWUiOjIwMTkxMTA3MTgwMjk1OSwiY2N0aW1lIjoyMDE5MTEwNzE4MDI5NTksInNpZ24iOiJhYmNkZWZnIiwicGFnZU51bWJlciI6MSwicGFnZVNpemUiOjMsInRva2VuIjoiMTExMTExIn0="}
     * 客户端加密字段:ctime+cctime+秘钥=sign
     * 服务端加密字段:stime+秘钥=sign
     * result={
     *     "resultCode": "0",
     *     "message": "success",
     *     "data": {
     *         "jsonData": "eyJkYXRhTGlzdCI6W3siY2F0ZWdvcnlOYW1lIjoi5YGa54ix6Zeu6aKYIiwiaWQiOjEsInNlYXQiOjF9LHsiY2F0ZWdvcnlOYW1lIjoi5Y+j5Lqk6Zeu6aKYIiwiaWQiOjIsInNlYXQiOjJ9LHsiY2F0ZWdvcnlOYW1lIjoi5oyB5LmF6Zeu6aKYIiwiaWQiOjMsInNlYXQiOjN9XSwicm93Q291bnQiOjUsInNpZ24iOiJmYjgyMTY3OWQ5YzBiNTBmYmIxMTgzMmQ3M2Y1YWNmOSIsInN0aW1lIjoxNTk0MDE0OTY5OTQ0fQ=="
     *     },
     *     "sgid": "202007061356090000001",
     *     "cgid": ""
     * }
     */
    @RequestMapping(value = "/getDataList", method = {RequestMethod.POST})
    public JsonResult<Object> getDataList(HttpServletRequest request, HttpServletResponse response, @RequestBody RequestEncryptionJson requestData) throws Exception{
        String sgid = ComponentUtil.redisIdService.getNewId();
        String cgid = "";
        String token;
        String ip = StringUtil.getIpAddress(request);
        String data = "";
        long did = 0;
        RegionModel regionModel = HodgepodgeMethod.assembleRegionModel(ip);

        RequestConsult requestModel = new RequestConsult();
        try{
            // 解密
            data = StringUtil.decoderBase64(requestData.jsonData);
            requestModel  = JSON.parseObject(data, RequestConsult.class);

            // 百问百答类别数据
            ConsultModel consultQuery = BeanUtils.copy(requestModel, ConsultModel.class);
            List<ConsultModel> consultList = ComponentUtil.consultService.queryByList(consultQuery);
            // 组装返回客户端的数据
            long stime = System.currentTimeMillis();
            String sign = SignUtil.getSgin(stime, secretKeySign); // stime+秘钥=sign
            String strData = HodgepodgeMethod.assembleConsultDataListResult(stime, sign, consultList, consultQuery.getRowCount());
            // 数据加密
            String encryptionData = StringUtil.mergeCodeBase64(strData);
            ResponseEncryptionJson resultDataModel = new ResponseEncryptionJson();
            resultDataModel.jsonData = encryptionData;
            // 返回数据给客户端
            return JsonResult.successResult(resultDataModel, cgid, sgid);
        }catch (Exception e){
            Map<String,String> map = ExceptionMethod.getException(e, ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_TWO);
            log.error(String.format("this ConsultController.getDataList() is error , the cgid=%s and sgid=%s and all data=%s!", cgid, sgid, data));
            if (!StringUtils.isBlank(map.get("dbCode"))){
                log.error(String.format("this ConsultController.getDataList() is error codeInfo, the dbCode=%s and dbMessage=%s !", map.get("dbCode"), map.get("dbMessage")));
            }
            e.printStackTrace();
            return JsonResult.failedResult(map.get("message"), map.get("code"), cgid, sgid);
        }
    }




    /**
     * @Description: 新增用户在线客服、咨询的发问
     * @param request
     * @param response
     * @return com.gd.chain.common.utils.JsonResult<java.lang.Object>
     * @author yoko
     * @date 2019/11/25 22:58
     * local:http://localhost:8086/fine/consult/addAsk
     * 请求的属性类:RequestConsult
     * 必填字段:{"consultId":1,"title":"title1","ask":"ask1","askAds":"askAds1","agtVer":1,"clientVer":1,"clientType":1,"ctime":201911071802959,"cctime":201911071802959,"sign":"abcdefg","token":"111111"}
     * 加密字段:{"jsonData":"eyJjb25zdWx0SWQiOjEsInRpdGxlIjoidGl0bGUxIiwiYXNrIjoiYXNrMSIsImFza0FkcyI6ImFza0FkczEiLCJhZ3RWZXIiOjEsImNsaWVudFZlciI6MSwiY2xpZW50VHlwZSI6MSwiY3RpbWUiOjIwMTkxMTA3MTgwMjk1OSwiY2N0aW1lIjoyMDE5MTEwNzE4MDI5NTksInNpZ24iOiJhYmNkZWZnIiwidG9rZW4iOiIxMTExMTEifQ=="}
     * 客户端加密字段:ctime+cctime+秘钥=sign
     * 服务端加密字段:stime+秘钥=sign
     * result={
     *     "resultCode": "0",
     *     "message": "success",
     *     "data": {
     *         "jsonData": "eyJzaWduIjoiYzQ1NTgxMTk5M2Q3ZGY5ZGQ4N2Q0MjVlYzgzOWM2OTQiLCJzdGltZSI6MTU5NDIxNTE2NDkxNn0="
     *     },
     *     "sgid": "202007082132440000001",
     *     "cgid": ""
     * }
     */
    @RequestMapping(value = "/addAsk", method = {RequestMethod.POST})
    public JsonResult<Object> addAsk(HttpServletRequest request, HttpServletResponse response, @RequestBody RequestEncryptionJson requestData) throws Exception{
        String sgid = ComponentUtil.redisIdService.getNewId();
        String cgid = "";
        String token = "";
        String ip = StringUtil.getIpAddress(request);
        String data = "";
        long did = 0;
        RegionModel regionModel = HodgepodgeMethod.assembleRegionModel(ip);

        RequestConsult requestModel = new RequestConsult();
        try{
            // 解密
            data = StringUtil.decoderBase64(requestData.jsonData);
            requestModel  = JSON.parseObject(data, RequestConsult.class);

            //#临时数据
//            if (!StringUtils.isBlank(requestModel.token)){
//                if (requestModel.token.equals("111111")){
//                    ComponentUtil.redisService.set(requestModel.token, "1");
//                }
//            }

            // check校验数据
            did = HodgepodgeMethod.checkAddAskData(requestModel);

            ConsultAskModel consultAskAdd = HodgepodgeMethod.assembleConsultAskAdd(did, requestModel);
            ComponentUtil.consultAskService.add(consultAskAdd);
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
            log.error(String.format("this ConsultController.addAsk() is error , the cgid=%s and sgid=%s and all data=%s!", cgid, sgid, data));
            if (!StringUtils.isBlank(map.get("dbCode"))){
                log.error(String.format("this ConsultController.addAsk() is error codeInfo, the dbCode=%s and dbMessage=%s !", map.get("dbCode"), map.get("dbMessage")));
            }
            e.printStackTrace();
            return JsonResult.failedResult(map.get("message"), map.get("code"), cgid, sgid);
        }
    }




    /**
     * @Description: 用户在线客服、咨询的发问数据-集合
     * @param request
     * @param response
     * @return com.gd.chain.common.utils.JsonResult<java.lang.Object>
     * @author yoko
     * @date 2019/11/25 22:58
     * local:http://localhost:8086/fine/consult/getAskDataList
     * 请求的属性类:RequestConsult
     * 必填字段:{"agtVer":1,"clientVer":1,"clientType":1,"ctime":201911071802959,"cctime":201911071802959,"sign":"abcdefg","pageNumber":1,"pageSize":3,"token":"111111"}
     * 加密字段:{"jsonData":"eyJhZ3RWZXIiOjEsImNsaWVudFZlciI6MSwiY2xpZW50VHlwZSI6MSwiY3RpbWUiOjIwMTkxMTA3MTgwMjk1OSwiY2N0aW1lIjoyMDE5MTEwNzE4MDI5NTksInNpZ24iOiJhYmNkZWZnIiwicGFnZU51bWJlciI6MSwicGFnZVNpemUiOjMsInRva2VuIjoiMTExMTExIn0="}
     * 客户端加密字段:ctime+cctime+秘钥=sign
     * 服务端加密字段:stime+秘钥=sign
     * result={
     *     "resultCode": "0",
     *     "message": "success",
     *     "data": {
     *         "jsonData": "eyJhc2tMaXN0IjpbeyJhc2siOiLmhJ/mg4Xpl67pophfNF80IiwiYXNrQWRzIjoiaHR0cDovL2ltZy5tcC5pdGMuY24vdXBsb2FkLzIwMTYxMTExLzkyNzI4YjI1YjUyYjRiZTc4Y2RmMmE3ODI2MjFmMDZlX3RoLmpwZyIsImNhdGVnb3J5TmFtZSI6IuaEn+aDhemXrumimCIsImNyZWF0ZVRpbWUiOiIyMDIwLTA3LTA4IDIwOjQ3OjEzIiwiaWQiOjEwLCJyZXBseVN0YXR1cyI6MX0seyJhc2siOiLmhJ/mg4Xpl67pophfNF8zIiwiYXNrQWRzIjoiaHR0cDovL2d0cHFuLnRpYW9jaGVuZy10ZWNoLmNvbS9lNGY1MjQ4ODA0MzM0ZTM4YWM3NGU1YTA2NWVjOWNmMS5wbmciLCJjYXRlZ29yeU5hbWUiOiLmhJ/mg4Xpl67popgiLCJjcmVhdGVUaW1lIjoiMjAyMC0wNy0wOCAyMDo0NzowOCIsImlkIjo5LCJyZXBseVN0YXR1cyI6MX0seyJhc2siOiLmhJ/mg4Xpl67pophfNF8yIiwiYXNrQWRzIjoiaHR0cDovL3Bob3RvY2RuLnNvaHUuY29tLzIwMTUxMTAxL0ltZzQyNDgzMzg4MC5qcGciLCJjYXRlZ29yeU5hbWUiOiLmhJ/mg4Xpl67popgiLCJjcmVhdGVUaW1lIjoiMjAyMC0wNy0wOCAyMDo0NzowMiIsImlkIjo4LCJyZXBseVN0YXR1cyI6MX1dLCJyb3dDb3VudCI6MTAsInNpZ24iOiI0NzI3MjczOTgxYzYyOGIwMmI4OWE3NjE1YmU5NGYwMCIsInN0aW1lIjoxNTk0MjEyNTI1NDAxfQ=="
     *     },
     *     "sgid": "202007082048450000001",
     *     "cgid": ""
     * }
     */
    @RequestMapping(value = "/getAskDataList", method = {RequestMethod.POST})
    public JsonResult<Object> getAskDataList(HttpServletRequest request, HttpServletResponse response, @RequestBody RequestEncryptionJson requestData) throws Exception{
        String sgid = ComponentUtil.redisIdService.getNewId();
        String cgid = "";
        String token;
        String ip = StringUtil.getIpAddress(request);
        String data = "";
        long did = 0;
        RegionModel regionModel = HodgepodgeMethod.assembleRegionModel(ip);

        RequestConsult requestModel = new RequestConsult();
        try{
            // 解密
            data = StringUtil.decoderBase64(requestData.jsonData);
            requestModel  = JSON.parseObject(data, RequestConsult.class);
            //#临时数据
//            if (!StringUtils.isBlank(requestModel.token)){
//                if (requestModel.token.equals("111111")){
//                    ComponentUtil.redisService.set(requestModel.token, "1");
//                }
//            }

            // check校验数据
            did = HodgepodgeMethod.checkGetAskDataList(requestModel);

            // 获取在线客服、咨询的发问的数据
            ConsultAskModel consultAskQuery = HodgepodgeMethod.assembleConsultAskModel(did, requestModel);
            List<ConsultAskModel> consultAskList = ComponentUtil.consultAskService.queryByList(consultAskQuery);
            // 组装返回客户端的数据
            long stime = System.currentTimeMillis();
            String sign = SignUtil.getSgin(stime, secretKeySign); // stime+秘钥=sign
            String strData = HodgepodgeMethod.assembleConsultAskDataListResult(stime, sign, consultAskList, consultAskQuery.getRowCount());
            // 数据加密
            String encryptionData = StringUtil.mergeCodeBase64(strData);
            ResponseEncryptionJson resultDataModel = new ResponseEncryptionJson();
            resultDataModel.jsonData = encryptionData;
            // 返回数据给客户端
            return JsonResult.successResult(resultDataModel, cgid, sgid);
        }catch (Exception e){
            Map<String,String> map = ExceptionMethod.getException(e, ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_TWO);
            log.error(String.format("this ConsultController.getAskDataList() is error , the cgid=%s and sgid=%s and all data=%s!", cgid, sgid, data));
            if (!StringUtils.isBlank(map.get("dbCode"))){
                log.error(String.format("this ConsultController.getAskDataList() is error codeInfo, the dbCode=%s and dbMessage=%s !", map.get("dbCode"), map.get("dbMessage")));
            }
            e.printStackTrace();
            return JsonResult.failedResult(map.get("message"), map.get("code"), cgid, sgid);
        }
    }


    /**
     * @Description: 用户在线客服、咨询的发问数据-详情
     * @param request
     * @param response
     * @return com.gd.chain.common.utils.JsonResult<java.lang.Object>
     * @author yoko
     * @date 2019/11/25 22:58
     * local:http://localhost:8086/fine/consult/getAskData
     * 请求的属性类:RequestConsult
     * 必填字段:{"id":1,"agtVer":1,"clientVer":1,"clientType":1,"ctime":201911071802959,"cctime":201911071802959,"sign":"abcdefg","token":"111111"}
     * 加密字段:{"jsonData":"eyJpZCI6MSwiYWd0VmVyIjoxLCJjbGllbnRWZXIiOjEsImNsaWVudFR5cGUiOjEsImN0aW1lIjoyMDE5MTEwNzE4MDI5NTksImNjdGltZSI6MjAxOTExMDcxODAyOTU5LCJzaWduIjoiYWJjZGVmZyIsInRva2VuIjoiMTExMTExIn0="}
     * 客户端加密字段:ctime+cctime+秘钥=sign
     * 服务端加密字段:stime+秘钥=sign
     * result={
     *     "resultCode": "0",
     *     "message": "success",
     *     "data": {
     *         "jsonData": "eyJhc2tNb2RlbCI6eyJhc2siOiLkuqTlj4vnlpHpl64iLCJhc2tBZHMiOiJodHRwOi8vaW1nLm1wLml0Yy5jbi91cGxvYWQvMjAxNjExMTEvOTI3MjhiMjViNTJiNGJlNzhjZGYyYTc4MjYyMWYwNmVfdGguanBnIiwiY2F0ZWdvcnlOYW1lIjoi5Lqk5Y+L6Zeu6aKYIiwiY3JlYXRlVGltZSI6IjIwMjAtMDctMDggMjA6NDQ6MjAiLCJpZCI6MSwicmVwbHlTdGF0dXMiOjF9LCJzaWduIjoiZWQyMjRhY2I2NGU1Y2E5MTM1YmU2NjAyZjhlYTIwYmQiLCJzdGltZSI6MTU5NDIxMzMwNzAzMH0="
     *     },
     *     "sgid": "202007082101460000001",
     *     "cgid": ""
     * }
     */
    @RequestMapping(value = "/getAskData", method = {RequestMethod.POST})
    public JsonResult<Object> getAskData(HttpServletRequest request, HttpServletResponse response, @RequestBody RequestEncryptionJson requestData) throws Exception {
        String sgid = ComponentUtil.redisIdService.getNewId();
        String cgid = "";
        String token;
        String ip = StringUtil.getIpAddress(request);
        String data = "";
        long did = 0;
        RegionModel regionModel = HodgepodgeMethod.assembleRegionModel(ip);

        RequestConsult requestModel = new RequestConsult();
        try {
            // 解密
            data = StringUtil.decoderBase64(requestData.jsonData);
            requestModel = JSON.parseObject(data, RequestConsult.class);
            //#临时数据
//            if (!StringUtils.isBlank(requestModel.token)){
//                if (requestModel.token.equals("111111")){
//                    ComponentUtil.redisService.set(requestModel.token, "1");
//                }
//            }

            // check校验数据
            did = HodgepodgeMethod.checkGetAskData(requestModel);

            // 获取在线客服、咨询的发问的数据
            ConsultAskModel consultAskQuery = HodgepodgeMethod.assembleConsultAskModel(did, requestModel);
            ConsultAskModel consultAskModel = (ConsultAskModel) ComponentUtil.consultAskService.findByObject(consultAskQuery);
            // 组装返回客户端的数据
            long stime = System.currentTimeMillis();
            String sign = SignUtil.getSgin(stime, secretKeySign); // stime+秘钥=sign
            String strData = HodgepodgeMethod.assembleConsultAskDataResult(stime, sign, consultAskModel);
            // 数据加密
            String encryptionData = StringUtil.mergeCodeBase64(strData);
            ResponseEncryptionJson resultDataModel = new ResponseEncryptionJson();
            resultDataModel.jsonData = encryptionData;
            // 返回数据给客户端
            return JsonResult.successResult(resultDataModel, cgid, sgid);
        } catch (Exception e) {
            Map<String, String> map = ExceptionMethod.getException(e, ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_TWO);
            log.error(String.format("this ConsultController.getAskData() is error , the cgid=%s and sgid=%s and all data=%s!", cgid, sgid, data));
            if (!StringUtils.isBlank(map.get("dbCode"))){
                log.error(String.format("this ConsultController.getAskData() is error codeInfo, the dbCode=%s and dbMessage=%s !", map.get("dbCode"), map.get("dbMessage")));
            }
            e.printStackTrace();
            return JsonResult.failedResult(map.get("message"), map.get("code"), cgid, sgid);
        }
    }



    /**
     * @Description: 新增追加问答
     * @param request
     * @param response
     * @return com.gd.chain.common.utils.JsonResult<java.lang.Object>
     * @author yoko
     * @date 2019/11/25 22:58
     * local:http://localhost:8086/fine/consult/addAskReply
     * 请求的属性类:RequestConsult
     * 必填字段:{"consultAskId":1,"askReply":"askReply1","askReplyAds":"askReplyAds1","dataType":1,"agtVer":1,"clientVer":1,"clientType":1,"ctime":201911071802959,"cctime":201911071802959,"sign":"abcdefg","token":"111111"}
     * 加密字段:{"jsonData":"eyJjb25zdWx0QXNrSWQiOjEsImFza1JlcGx5IjoiYXNrUmVwbHkxIiwiYXNrUmVwbHlBZHMiOiJhc2tSZXBseUFkczEiLCJkYXRhVHlwZSI6MSwiYWd0VmVyIjoxLCJjbGllbnRWZXIiOjEsImNsaWVudFR5cGUiOjEsImN0aW1lIjoyMDE5MTEwNzE4MDI5NTksImNjdGltZSI6MjAxOTExMDcxODAyOTU5LCJzaWduIjoiYWJjZGVmZyIsInRva2VuIjoiMTExMTExIn0="}
     * 客户端加密字段:ctime+cctime+秘钥=sign
     * 服务端加密字段:stime+秘钥=sign
     * result={
     *     "resultCode": "0",
     *     "message": "success",
     *     "data": {
     *         "jsonData": "eyJzaWduIjoiYzQ1NTgxMTk5M2Q3ZGY5ZGQ4N2Q0MjVlYzgzOWM2OTQiLCJzdGltZSI6MTU5NDIxNTE2NDkxNn0="
     *     },
     *     "sgid": "202007082132440000001",
     *     "cgid": ""
     * }
     */
    @RequestMapping(value = "/addAskReply", method = {RequestMethod.POST})
    public JsonResult<Object> addAskReply(HttpServletRequest request, HttpServletResponse response, @RequestBody RequestEncryptionJson requestData) throws Exception{
        String sgid = ComponentUtil.redisIdService.getNewId();
        String cgid = "";
        String token = "";
        String ip = StringUtil.getIpAddress(request);
        String data = "";
        long did = 0;
        RegionModel regionModel = HodgepodgeMethod.assembleRegionModel(ip);

        RequestConsult requestModel = new RequestConsult();
        try{
            // 解密
            data = StringUtil.decoderBase64(requestData.jsonData);
            requestModel  = JSON.parseObject(data, RequestConsult.class);

            //#临时数据
//            if (!StringUtils.isBlank(requestModel.token)){
//                if (requestModel.token.equals("111111")){
//                    ComponentUtil.redisService.set(requestModel.token, "1");
//                }
//            }

            // check校验数据
            did = HodgepodgeMethod.checkAddAskReplyData(requestModel);

            ConsultAskReplyModel consultAskReplyAdd = BeanUtils.copy(requestModel, ConsultAskReplyModel.class);
            ComponentUtil.consultAskReplyService.add(consultAskReplyAdd);
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
            log.error(String.format("this ConsultController.addAskReply() is error , the cgid=%s and sgid=%s and all data=%s!", cgid, sgid, data));
            if (!StringUtils.isBlank(map.get("dbCode"))){
                log.error(String.format("this ConsultController.addAskReply() is error codeInfo, the dbCode=%s and dbMessage=%s !", map.get("dbCode"), map.get("dbMessage")));
            }
            e.printStackTrace();
            return JsonResult.failedResult(map.get("message"), map.get("code"), cgid, sgid);
        }
    }


    /**
     * @Description: 获取追加问答数据-集合
     * @param request
     * @param response
     * @return com.gd.chain.common.utils.JsonResult<java.lang.Object>
     * @author yoko
     * @date 2019/11/25 22:58
     * local:http://localhost:8086/fine/consult/getAskReplyDataList
     * 请求的属性类:RequestConsult
     * 必填字段:{"consultAskId":1,"agtVer":1,"clientVer":1,"clientType":1,"ctime":201911071802959,"cctime":201911071802959,"sign":"abcdefg","pageNumber":1,"pageSize":3,"token":"111111"}
     * 加密字段:{"jsonData":"eyJhZ3RWZXIiOjEsImNsaWVudFZlciI6MSwiY2xpZW50VHlwZSI6MSwiY3RpbWUiOjIwMTkxMTA3MTgwMjk1OSwiY2N0aW1lIjoyMDE5MTEwNzE4MDI5NTksInNpZ24iOiJhYmNkZWZnIiwicGFnZU51bWJlciI6MSwicGFnZVNpemUiOjMsInRva2VuIjoiMTExMTExIn0="}
     * 客户端加密字段:ctime+cctime+秘钥=sign
     * 服务端加密字段:stime+秘钥=sign
     * result={
     *     "resultCode": "0",
     *     "message": "success",
     *     "data": {
     *         "jsonData": "eyJhc2tMaXN0IjpbeyJhc2siOiLmhJ/mg4Xpl67pophfNF80IiwiYXNrQWRzIjoiaHR0cDovL2ltZy5tcC5pdGMuY24vdXBsb2FkLzIwMTYxMTExLzkyNzI4YjI1YjUyYjRiZTc4Y2RmMmE3ODI2MjFmMDZlX3RoLmpwZyIsImNhdGVnb3J5TmFtZSI6IuaEn+aDhemXrumimCIsImNyZWF0ZVRpbWUiOiIyMDIwLTA3LTA4IDIwOjQ3OjEzIiwiaWQiOjEwLCJyZXBseVN0YXR1cyI6MX0seyJhc2siOiLmhJ/mg4Xpl67pophfNF8zIiwiYXNrQWRzIjoiaHR0cDovL2d0cHFuLnRpYW9jaGVuZy10ZWNoLmNvbS9lNGY1MjQ4ODA0MzM0ZTM4YWM3NGU1YTA2NWVjOWNmMS5wbmciLCJjYXRlZ29yeU5hbWUiOiLmhJ/mg4Xpl67popgiLCJjcmVhdGVUaW1lIjoiMjAyMC0wNy0wOCAyMDo0NzowOCIsImlkIjo5LCJyZXBseVN0YXR1cyI6MX0seyJhc2siOiLmhJ/mg4Xpl67pophfNF8yIiwiYXNrQWRzIjoiaHR0cDovL3Bob3RvY2RuLnNvaHUuY29tLzIwMTUxMTAxL0ltZzQyNDgzMzg4MC5qcGciLCJjYXRlZ29yeU5hbWUiOiLmhJ/mg4Xpl67popgiLCJjcmVhdGVUaW1lIjoiMjAyMC0wNy0wOCAyMDo0NzowMiIsImlkIjo4LCJyZXBseVN0YXR1cyI6MX1dLCJyb3dDb3VudCI6MTAsInNpZ24iOiI0NzI3MjczOTgxYzYyOGIwMmI4OWE3NjE1YmU5NGYwMCIsInN0aW1lIjoxNTk0MjEyNTI1NDAxfQ=="
     *     },
     *     "sgid": "202007082048450000001",
     *     "cgid": ""
     * }
     */
    @RequestMapping(value = "/getAskReplyDataList", method = {RequestMethod.POST})
    public JsonResult<Object> getAskReplyDataList(HttpServletRequest request, HttpServletResponse response, @RequestBody RequestEncryptionJson requestData) throws Exception{
        String sgid = ComponentUtil.redisIdService.getNewId();
        String cgid = "";
        String token;
        String ip = StringUtil.getIpAddress(request);
        String data = "";
        long did = 0;
        RegionModel regionModel = HodgepodgeMethod.assembleRegionModel(ip);

        RequestConsult requestModel = new RequestConsult();
        try{
            // 解密
            data = StringUtil.decoderBase64(requestData.jsonData);
            requestModel  = JSON.parseObject(data, RequestConsult.class);
            //#临时数据
//            if (!StringUtils.isBlank(requestModel.token)){
//                if (requestModel.token.equals("111111")){
//                    ComponentUtil.redisService.set(requestModel.token, "1");
//                }
//            }

            // check校验数据
            did = HodgepodgeMethod.checkAddAskReplyData(requestModel);

            // 获取获取追加问答数据-集合的数据
            ConsultAskReplyModel consultAskQuery = HodgepodgeMethod.assembleConsultAskReplyModel(did, requestModel);
            List<ConsultAskModel> consultAskList = ComponentUtil.consultAskService.queryByList(consultAskQuery);
            // 组装返回客户端的数据
            long stime = System.currentTimeMillis();
            String sign = SignUtil.getSgin(stime, secretKeySign); // stime+秘钥=sign
            String strData = HodgepodgeMethod.assembleConsultAskDataListResult(stime, sign, consultAskList, consultAskQuery.getRowCount());
            // 数据加密
            String encryptionData = StringUtil.mergeCodeBase64(strData);
            ResponseEncryptionJson resultDataModel = new ResponseEncryptionJson();
            resultDataModel.jsonData = encryptionData;
            // 返回数据给客户端
            return JsonResult.successResult(resultDataModel, cgid, sgid);
        }catch (Exception e){
            Map<String,String> map = ExceptionMethod.getException(e, ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_TWO);
            log.error(String.format("this ConsultController.getAskReplyDataList() is error , the cgid=%s and sgid=%s and all data=%s!", cgid, sgid, data));
            if (!StringUtils.isBlank(map.get("dbCode"))){
                log.error(String.format("this ConsultController.getAskReplyDataList() is error codeInfo, the dbCode=%s and dbMessage=%s !", map.get("dbCode"), map.get("dbMessage")));
            }
            e.printStackTrace();
            return JsonResult.failedResult(map.get("message"), map.get("code"), cgid, sgid);
        }
    }




}
