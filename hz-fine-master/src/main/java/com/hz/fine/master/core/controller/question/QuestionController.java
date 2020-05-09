package com.hz.fine.master.core.controller.question;

import com.alibaba.fastjson.JSON;
import com.hz.fine.master.core.common.exception.ExceptionMethod;
import com.hz.fine.master.core.common.utils.BeanUtils;
import com.hz.fine.master.core.common.utils.JsonResult;
import com.hz.fine.master.core.common.utils.SignUtil;
import com.hz.fine.master.core.common.utils.StringUtil;
import com.hz.fine.master.core.common.utils.constant.ServerConstant;
import com.hz.fine.master.core.model.RequestEncryptionJson;
import com.hz.fine.master.core.model.ResponseEncryptionJson;
import com.hz.fine.master.core.model.question.QuestionDModel;
import com.hz.fine.master.core.model.question.QuestionMModel;
import com.hz.fine.master.core.model.region.RegionModel;
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
 * @Description 百问百答的Controller层
 * @Author yoko
 * @Date 2020/1/7 11:49
 * @Version 1.0
 */
@RestController
@RequestMapping("/fine/qt")
public class QuestionController {
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
     * @Description: 获取百问百答-类别数据
     * @param request
     * @param response
     * @return com.gd.chain.common.utils.JsonResult<java.lang.Object>
     * @author yoko
     * @date 2019/11/25 22:58
     * local:http://localhost:8082/fine/qt/getDataMList
     * 请求的属性类:RequestAppeal
     * 必填字段:{"agtVer":1,"clientVer":1,"clientType":1,"ctime":201911071802959,"cctime":201911071802959,"sign":"abcdefg","pageNumber":1,"pageSize":3,"token":"111111","androidVer":"7.1.2"}
     * 客户端加密字段:ctime+cctime+秘钥=sign
     * 服务端加密字段:stime+秘钥=sign
     * result={
     *     "resultCode": "0",
     *     "message": "success",
     *     "data": {
     *         "jsonData": "eyJxTUxpc3QiOlt7ImNhdGVnb3J5TmFtZSI6Iui0puWPt+mXrumimF8xIiwiaWNvbkFkcyI6Imh0dHBzOi8vcGljczcuYmFpZHUuY29tL2ZlZWQvMjFhNDQ2MjMwOWY3OTA1Mjg4MTBhNTA2NzM4YjAwY2Y3YmNiZDU3ZC5qcGVnP3Rva2VuPTEzZTFmODhkNjc5NjQzNmY5YmVlMGY3NDBkOGNjN2IzJnM9MEUyMUQyMDU1RTcyMTA5NDc0ODQ2OEI3MDMwMEEwMDIiLCJpZCI6MSwic2VhdE0iOjF9LHsiY2F0ZWdvcnlOYW1lIjoiWFjpl67pophfMiIsImljb25BZHMiOiJodHRwczovL3BpY3M3LmJhaWR1LmNvbS9mZWVkLzIxYTQ0NjIzMDlmNzkwNTI4ODEwYTUwNjczOGIwMGNmN2JjYmQ1N2QuanBlZz90b2tlbj0xM2UxZjg4ZDY3OTY0MzZmOWJlZTBmNzQwZDhjYzdiMyZzPTBFMjFEMjA1NUU3MjEwOTQ3NDg0NjhCNzAzMDBBMDAyIiwiaWQiOjIsInNlYXRNIjoyfSx7ImNhdGVnb3J5TmFtZSI6IllZ6Zeu6aKYXzMiLCJpY29uQWRzIjoiaHR0cHM6Ly9waWNzNy5iYWlkdS5jb20vZmVlZC8yMWE0NDYyMzA5Zjc5MDUyODgxMGE1MDY3MzhiMDBjZjdiY2JkNTdkLmpwZWc/dG9rZW49MTNlMWY4OGQ2Nzk2NDM2ZjliZWUwZjc0MGQ4Y2M3YjMmcz0wRTIxRDIwNTVFNzIxMDk0NzQ4NDY4QjcwMzAwQTAwMiIsImlkIjozLCJzZWF0TSI6M30seyJjYXRlZ29yeU5hbWUiOiJaWumXrumimF80IiwiaWNvbkFkcyI6Imh0dHBzOi8vcGljczcuYmFpZHUuY29tL2ZlZWQvMjFhNDQ2MjMwOWY3OTA1Mjg4MTBhNTA2NzM4YjAwY2Y3YmNiZDU3ZC5qcGVnP3Rva2VuPTEzZTFmODhkNjc5NjQzNmY5YmVlMGY3NDBkOGNjN2IzJnM9MEUyMUQyMDU1RTcyMTA5NDc0ODQ2OEI3MDMwMEEwMDIiLCJpZCI6NCwic2VhdE0iOjR9XSwicm93Q291bnQiOjQsInNpZ24iOiIzOGFmYTBjYzY3YzlhMWNiMmI0ODBjOTBlMzcwMmY3YSIsInN0aW1lIjoxNTc4NDUzODk4MjU2fQ=="
     *     },
     *     "sgid": "202001081124540000001",
     *     "cgid": ""
     * }
     */
    @RequestMapping(value = "/getDataMList", method = {RequestMethod.POST})
    public JsonResult<Object> getDataMList(HttpServletRequest request, HttpServletResponse response, @RequestBody RequestEncryptionJson requestData) throws Exception{
        String sgid = ComponentUtil.redisIdService.getNewId();
        String cgid = "";
        String token;
        String ip = StringUtil.getIpAddress(request);
        String data = "";
        long did = 0;
        RegionModel regionModel = HodgepodgeMethod.assembleRegionModel(ip);

        RequestQuestion requestModel = new RequestQuestion();
        try{
            // 解密
            data = StringUtil.decoderBase64(requestData.jsonData);
            requestModel  = JSON.parseObject(data, RequestQuestion.class);

            // 获取用户ID
            if (requestModel != null && !StringUtils.isBlank(requestModel.token)){
                token = requestModel.token;
                // #零时数据
                did = HodgepodgeMethod.getDidByToken(requestModel.token);
            }

            // 百问百答类别数据
            QuestionMModel questionMQuery = BeanUtils.copy(requestModel, QuestionMModel.class);
            List<QuestionMModel> questionMList = ComponentUtil.questionMService.queryByList(questionMQuery);
            // 组装返回客户端的数据
            long stime = System.currentTimeMillis();
            String sign = SignUtil.getSgin(stime, secretKeySign); // stime+秘钥=sign
            String strData = HodgepodgeMethod.assembleQuestionMResult(stime, sign, questionMList, questionMQuery.getRowCount());
            // 数据加密
            String encryptionData = StringUtil.mergeCodeBase64(strData);
            ResponseEncryptionJson resultDataModel = new ResponseEncryptionJson();
            resultDataModel.jsonData = encryptionData;
            // 返回数据给客户端
            return JsonResult.successResult(resultDataModel, cgid, sgid);
        }catch (Exception e){
            Map<String,String> map = ExceptionMethod.getException(e, ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_TWO);
            log.error(String.format("this QuestionController.getDataMList() is error , the cgid=%s and sgid=%s and all data=%s!", cgid, sgid, data));
            e.printStackTrace();
            return JsonResult.failedResult(map.get("message"), map.get("code"), cgid, sgid);
        }
    }


    /**
     * @Description: 获取百问百答-详情-集合数据
     * @param request
     * @param response
     * @return com.gd.chain.common.utils.JsonResult<java.lang.Object>
     * @author yoko
     * @date 2019/11/25 22:58
     * local:http://localhost:8082/fine/qt/getDataDList
     * 请求的属性类:RequestAppeal
     * 必填字段:{"questionMId":1,"searchKey":"YY简述_1_1","agtVer":1,"clientVer":1,"clientType":1,"ctime":201911071802959,"cctime":201911071802959,"sign":"abcdefg","pageNumber":1,"pageSize":3,"token":"111111","androidVer":"7.1.2","channel":"channel_1","channelNum":"channelNum_1","spreadValue":"spreadValue_1"}
     * 客户端加密字段:ctime+cctime+秘钥=sign
     * 服务端加密字段:stime+秘钥=sign
     * result={
     *     "resultCode": "0",
     *     "message": "success",
     *     "data": {
     *         "jsonData": "eyJxRExpc3QiOlt7ImNhdGVnb3J5TmFtZSI6Iui0puWPt+mXrumimF8xIiwiaWQiOjEsInBhZ2VBZHMiOiJodHRwczovL3d3dy5iYWlkdS5jb20vIzEiLCJzZWF0RCI6MSwic2tldGNoIjoi6LSm5Y+3566A6L+wXzFfMSIsInRpdGxlIjoi6LSm5Y+35qCH6aKYXzFfMSJ9LHsiY2F0ZWdvcnlOYW1lIjoi6LSm5Y+36Zeu6aKYXzEiLCJpZCI6MiwicGFnZUFkcyI6Imh0dHBzOi8vd3d3LmJhaWR1LmNvbS8jMiIsInNlYXREIjoyLCJza2V0Y2giOiLotKblj7fnroDov7BfMV8yIiwidGl0bGUiOiLotKblj7fmoIfpophfMV8yIn0seyJjYXRlZ29yeU5hbWUiOiLotKblj7fpl67pophfMSIsImlkIjozLCJwYWdlQWRzIjoiaHR0cHM6Ly93d3cuYmFpZHUuY29tLyMzIiwic2VhdEQiOjMsInNrZXRjaCI6Iui0puWPt+eugOi/sF8xXzMiLCJ0aXRsZSI6Iui0puWPt+agh+mimF8xXzMifSx7ImNhdGVnb3J5TmFtZSI6Iui0puWPt+mXrumimF8xIiwiaWQiOjQsInBhZ2VBZHMiOiJodHRwczovL3d3dy5iYWlkdS5jb20vIzQiLCJzZWF0RCI6NCwic2tldGNoIjoi6LSm5Y+3566A6L+wXzFfNCIsInRpdGxlIjoi6LSm5Y+35qCH6aKYXzFfNCJ9LHsiY2F0ZWdvcnlOYW1lIjoi6LSm5Y+36Zeu6aKYXzEiLCJpZCI6NSwicGFnZUFkcyI6Imh0dHBzOi8vd3d3LmJhaWR1LmNvbS8jNSIsInNlYXREIjo1LCJza2V0Y2giOiLotKblj7fnroDov7BfMV81IiwidGl0bGUiOiLotKblj7fmoIfpophfMV81In0seyJjYXRlZ29yeU5hbWUiOiLotKblj7fpl67pophfMSIsImlkIjo2LCJwYWdlQWRzIjoiaHR0cHM6Ly93d3cuYmFpZHUuY29tLyM2Iiwic2VhdEQiOjYsInNrZXRjaCI6Iui0puWPt+eugOi/sF8xXzYiLCJ0aXRsZSI6Iui0puWPt+agh+mimF8xXzYifSx7ImNhdGVnb3J5TmFtZSI6Iui0puWPt+mXrumimF8xIiwiaWQiOjcsInBhZ2VBZHMiOiJodHRwczovL3d3dy5iYWlkdS5jb20vIzciLCJzZWF0RCI6Nywic2tldGNoIjoi6LSm5Y+3566A6L+wXzFfNyIsInRpdGxlIjoi6LSm5Y+35qCH6aKYXzFfNyJ9LHsiY2F0ZWdvcnlOYW1lIjoi6LSm5Y+36Zeu6aKYXzEiLCJpZCI6OCwicGFnZUFkcyI6Imh0dHBzOi8vd3d3LmJhaWR1LmNvbS8jOCIsInNlYXREIjo4LCJza2V0Y2giOiLotKblj7fnroDov7BfMV84IiwidGl0bGUiOiLotKblj7fmoIfpophfMV84In0seyJjYXRlZ29yeU5hbWUiOiLotKblj7fpl67pophfMSIsImlkIjo5LCJwYWdlQWRzIjoiaHR0cHM6Ly93d3cuYmFpZHUuY29tLyM5Iiwic2VhdEQiOjksInNrZXRjaCI6Iui0puWPt+eugOi/sF8xXzkiLCJ0aXRsZSI6Iui0puWPt+agh+mimF8xXzkifSx7ImNhdGVnb3J5TmFtZSI6Iui0puWPt+mXrumimF8xIiwiaWQiOjEwLCJwYWdlQWRzIjoiaHR0cHM6Ly93d3cuYmFpZHUuY29tLyMxMCIsInNlYXREIjoxMCwic2tldGNoIjoi6LSm5Y+3566A6L+wXzFfMTAiLCJ0aXRsZSI6Iui0puWPt+agh+mimF8xXzEwIn1dLCJyb3dDb3VudCI6MTAsInNpZ24iOiJlM2VjYjYyOWViOTJjYzg5ZTA3ZjZiZmUzN2U1ZjI1MCIsInN0aW1lIjoxNTc4NDY1NzAxNzkzfQ=="
     *     },
     *     "sgid": "202001081441360000001",
     *     "cgid": ""
     * }
     */
    @RequestMapping(value = "/getDataDList", method = {RequestMethod.POST})
    public JsonResult<Object> getDataDList(HttpServletRequest request, HttpServletResponse response, @RequestBody RequestEncryptionJson requestData) throws Exception{
        String sgid = ComponentUtil.redisIdService.getNewId();
        String cgid = "";
        String token;
        String ip = StringUtil.getIpAddress(request);
        String data = "";
        long did = 0;
        RegionModel regionModel = HodgepodgeMethod.assembleRegionModel(ip);

        RequestQuestion requestModel = new RequestQuestion();
        try{
            // 解密
            data = StringUtil.decoderBase64(requestData.jsonData);
            requestModel  = JSON.parseObject(data, RequestQuestion.class);

            // 获取用户ID
            if (requestModel != null && !StringUtils.isBlank(requestModel.token)){
                token = requestModel.token;
                // #零时数据
//                ComponentUtil.redisService.set(token, "3");
                did = HodgepodgeMethod.getDidByToken(requestModel.token);

            }

            // 百问百答-详情集合数据
            QuestionDModel questionDQuery = BeanUtils.copy(requestModel, QuestionDModel.class);
            List<QuestionDModel> questionDList = ComponentUtil.questionDService.queryByList(questionDQuery);
            // 组装返回客户端的数据
            long stime = System.currentTimeMillis();
            String sign = SignUtil.getSgin(stime, secretKeySign); // stime+秘钥=sign
            String strData = HodgepodgeMethod.assembleQuestionDResult(stime, sign, questionDList, questionDQuery.getRowCount());
            // 数据加密
            String encryptionData = StringUtil.mergeCodeBase64(strData);
            ResponseEncryptionJson resultDataModel = new ResponseEncryptionJson();
            resultDataModel.jsonData = encryptionData;
            // 返回数据给客户端
            return JsonResult.successResult(resultDataModel, cgid, sgid);
        }catch (Exception e){
            Map<String,String> map = ExceptionMethod.getException(e, ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_TWO);
            log.error(String.format("this QuestionController.getDataDList() is error , the cgid=%s and sgid=%s and all data=%s!", cgid, sgid, data));
            e.printStackTrace();
            return JsonResult.failedResult(map.get("message"), map.get("code"), cgid, sgid);
        }
    }


    /**
     * @Description: 获取百问百答-详情数据
     * @param request
     * @param response
     * @return com.gd.chain.common.utils.JsonResult<java.lang.Object>
     * @author yoko
     * @date 2019/11/25 22:58
     * local:http://localhost:8082/fine/qt/getDataD
     * 请求的属性类:RequestAppeal
     * 必填字段:{"id":1,"agtVer":1,"clientVer":1,"clientType":1,"ctime":201911071802959,"cctime":201911071802959,"sign":"abcdefg","token":"111111","androidVer":"7.1.2","channel":"channel_1","channelNum":"channelNum_1","spreadValue":"spreadValue_1"}
     * 客户端加密字段:ctime+cctime+秘钥=sign
     * 服务端加密字段:stime+秘钥=sign
     * result={
     *     "resultCode": "0",
     *     "message": "success",
     *     "data": {
     *         "jsonData": "eyJxRCI6eyJjYXRlZ29yeU5hbWUiOiLotKblj7fpl67pophfMSIsImlkIjoxLCJwYWdlQWRzIjoiaHR0cHM6Ly93d3cuYmFpZHUuY29tLyMxIiwic2VhdEQiOjEsInNrZXRjaCI6Iui0puWPt+eugOi/sF8xXzEiLCJ0aXRsZSI6Iui0puWPt+agh+mimF8xXzEifSwic2lnbiI6IjQ2OWQwZTIzZmVmZjY4ODg1MDlmYzZkZTljOTRiYmU2Iiwic3RpbWUiOjE1Nzg0NjYyNTIwOTV9"
     *     },
     *     "sgid": "202001081450490000001",
     *     "cgid": ""
     * }
     */
    @RequestMapping(value = "/getDataD", method = {RequestMethod.POST})
    public JsonResult<Object> getDataD(HttpServletRequest request, HttpServletResponse response, @RequestBody RequestEncryptionJson requestData) throws Exception{
        String sgid = ComponentUtil.redisIdService.getNewId();
        String cgid = "";
        String token;
        String ip = StringUtil.getIpAddress(request);
        String data = "";
        long did = 0;
        RegionModel regionModel = HodgepodgeMethod.assembleRegionModel(ip);

        RequestQuestion requestModel = new RequestQuestion();
        try{
            // 解密
            data = StringUtil.decoderBase64(requestData.jsonData);
            requestModel  = JSON.parseObject(data, RequestQuestion.class);

            // 获取用户ID
            if (requestModel != null && !StringUtils.isBlank(requestModel.token)){
                token = requestModel.token;
                // #零时数据
//                ComponentUtil.redisService.set(token, "3");
                did = HodgepodgeMethod.getDidByToken(requestModel.token);

            }

            // 百问百答详情数据
            QuestionDModel questionDQuery = BeanUtils.copy(requestModel, QuestionDModel.class);
            QuestionDModel questionDModel = (QuestionDModel) ComponentUtil.questionDService.findByObject(questionDQuery);
            // 组装返回客户端的数据
            long stime = System.currentTimeMillis();
            String sign = SignUtil.getSgin(stime, secretKeySign); // stime+秘钥=sign
            String strData = HodgepodgeMethod.assembleQuestionDDataResult(stime, sign, questionDModel, questionDQuery.getRowCount());
            // 数据加密
            String encryptionData = StringUtil.mergeCodeBase64(strData);
            ResponseEncryptionJson resultDataModel = new ResponseEncryptionJson();
            resultDataModel.jsonData = encryptionData;
            // 返回数据给客户端
            return JsonResult.successResult(resultDataModel, cgid, sgid);
        }catch (Exception e){
            Map<String,String> map = ExceptionMethod.getException(e, ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_TWO);
            log.error(String.format("this QuestionController.getDataD() is error , the cgid=%s and sgid=%s and all data=%s!", cgid, sgid, data));
            e.printStackTrace();
            return JsonResult.failedResult(map.get("message"), map.get("code"), cgid, sgid);
        }
    }
}
