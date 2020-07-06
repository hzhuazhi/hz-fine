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
import com.hz.fine.master.core.model.question.QuestionDDModel;
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
import java.util.ArrayList;
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
     * local:http://localhost:8086/fine/qt/getDataMList
     * 请求的属性类:RequestQuestion
     * 必填字段:{"agtVer":1,"clientVer":1,"clientType":1,"ctime":201911071802959,"cctime":201911071802959,"sign":"abcdefg","pageNumber":1,"pageSize":3,"token":"111111"}
     * 加密字段:{"jsonData":"eyJhZ3RWZXIiOjEsImNsaWVudFZlciI6MSwiY2xpZW50VHlwZSI6MSwiY3RpbWUiOjIwMTkxMTA3MTgwMjk1OSwiY2N0aW1lIjoyMDE5MTEwNzE4MDI5NTksInNpZ24iOiJhYmNkZWZnIiwicGFnZU51bWJlciI6MSwicGFnZVNpemUiOjMsInRva2VuIjoiMTExMTExIiwiYW5kcm9pZFZlciI6IjcuMS4yIn0="}
     * 客户端加密字段:ctime+cctime+秘钥=sign
     * 服务端加密字段:stime+秘钥=sign
     * result={
     *     "resultCode": "0",
     *     "message": "success",
     *     "data": {
     *         "jsonData": "eyJxTUxpc3QiOlt7ImNhdGVnb3J5TmFtZSI6IuWvhueggeebuOWFsyIsImljb25BZHMiOiJodHRwOi8vcTU1dm5kYmlmLmJrdC5jbG91ZGRuLmNvbS9tbXhnLnBuZyIsImlkIjoxLCJzZWF0TSI6MX0seyJjYXRlZ29yeU5hbWUiOiLnkIbotKLmlLvnlaUiLCJpY29uQWRzIjoiaHR0cDovL3E1NXZuZGJpZi5ia3QuY2xvdWRkbi5jb20vbGNjbC5wbmciLCJpZCI6Miwic2VhdE0iOjJ9LHsiY2F0ZWdvcnlOYW1lIjoi5aW95Y+L5biu5YqpIiwiaWNvbkFkcyI6Imh0dHA6Ly9xNTV2bmRiaWYuYmt0LmNsb3VkZG4uY29tL2h5YnoucG5nIiwiaWQiOjMsInNlYXRNIjozfV0sInJvd0NvdW50Ijo0LCJzaWduIjoiNTgwMzg5Njc0NGI3M2JkZDkzZDBlOGM2NTE4OTJhN2YiLCJzdGltZSI6MTU5Mzg2MzM0ODM5NH0="
     *     },
     *     "sgid": "202007041949080000001",
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
     * local:http://localhost:8086/fine/qt/getDataDList
     * 请求的属性类:RequestQuestion
     * 必填字段:{"questionMId":1,"agtVer":1,"clientVer":1,"clientType":1,"ctime":201911071802959,"cctime":201911071802959,"sign":"abcdefg","pageNumber":1,"pageSize":3,"token":"111111"}
     * 加密字段:{"jsonData":"eyJxdWVzdGlvbk1JZCI6MSwiYWd0VmVyIjoxLCJjbGllbnRWZXIiOjEsImNsaWVudFR5cGUiOjEsImN0aW1lIjoyMDE5MTEwNzE4MDI5NTksImNjdGltZSI6MjAxOTExMDcxODAyOTU5LCJzaWduIjoiYWJjZGVmZyIsInBhZ2VOdW1iZXIiOjEsInBhZ2VTaXplIjozLCJ0b2tlbiI6IjExMTExMSJ9"}
     * 客户端加密字段:ctime+cctime+秘钥=sign
     * 服务端加密字段:stime+秘钥=sign
     * result={
     *     "resultCode": "0",
     *     "message": "success",
     *     "data": {
     *         "jsonData": "eyJxRExpc3QiOlt7ImNhdGVnb3J5TmFtZSI6IuWvhueggeebuOWFsyIsImlkIjo0MSwicGFnZUFkcyI6Imh0dHA6Ly9xNTV2bmRiaWYuYmt0LmNsb3VkZG4uY29tL3VwbG9hZCUyRmltYWdlJTJGMjAyMF8wMl8wNCUyRjAyNGI4OWQ0NDU5OGE2MmQ1ZmI1N2M1MjliMjZjODI3MWJiYzAyZjEucG5nIiwic2VhdEQiOjEsInNrZXRjaCI6IuW/mOiusOeZu+mZhuWvhueggeS6huaAjuS5iOWKnu+8nyIsInRpdGxlIjoi5b+Y6K6w55m76ZmG5a+G56CB5LqG5oCO5LmI5Yqe77yfIn0seyJjYXRlZ29yeU5hbWUiOiLlr4bnoIHnm7jlhbMiLCJpZCI6NDIsInBhZ2VBZHMiOiJodHRwOi8vcTU1dm5kYmlmLmJrdC5jbG91ZGRuLmNvbS91cGxvYWQlMkZpbWFnZSUyRjIwMjBfMDJfMDQlMkY5OWJjMjNmNzNkOThkYWJmOTVmN2Y0MDY4NTJlMTFjYWE5MTI1NzA5LnBuZyIsInNlYXREIjoyLCJza2V0Y2giOiLmj5DnjrDlr4bnoIHlv5jorrDkuobmgI7kuYjlip7vvJ8iLCJ0aXRsZSI6IuaPkOeOsOWvhueggeW/mOiusOS6huaAjuS5iOWKnu+8nyJ9XSwicm93Q291bnQiOjIsInNpZ24iOiI1YzI4YzIyMDYxNTMxYzkyYmNjMDZjYTFjMmZjMjgwMCIsInN0aW1lIjoxNTkzODYzNjUyMzUzfQ=="
     *     },
     *     "sgid": "202007041954110000001",
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
     * local:http://localhost:8086/fine/qt/getDataD
     * 请求的属性类:RequestQuestion
     * 必填字段:{"id":41,"agtVer":1,"clientVer":1,"clientType":1,"ctime":201911071802959,"cctime":201911071802959,"sign":"abcdefg","token":"111111"}
     * 加密字段:{"jsonData":"eyJhZ3RWZXIiOjEsImNsaWVudFZlciI6MSwiY2xpZW50VHlwZSI6MSwiY3RpbWUiOjIwMTkxMTA3MTgwMjk1OSwiY2N0aW1lIjoyMDE5MTEwNzE4MDI5NTksInNpZ24iOiJhYmNkZWZnIiwicGFnZU51bWJlciI6MSwicGFnZVNpemUiOjMsInRva2VuIjoiMTExMTExIiwiYW5kcm9pZFZlciI6IjcuMS4yIn0="}
     * 客户端加密字段:ctime+cctime+秘钥=sign
     * 服务端加密字段:stime+秘钥=sign
     * result={
     *     "resultCode": "0",
     *     "message": "success",
     *     "data": {
     *         "jsonData": "eyJxRCI6eyJjYXRlZ29yeU5hbWUiOiLlr4bnoIHnm7jlhbMiLCJpZCI6NDEsInBhZ2VBZHMiOiJodHRwOi8vcTU1dm5kYmlmLmJrdC5jbG91ZGRuLmNvbS91cGxvYWQlMkZpbWFnZSUyRjIwMjBfMDJfMDQlMkYwMjRiODlkNDQ1OThhNjJkNWZiNTdjNTI5YjI2YzgyNzFiYmMwMmYxLnBuZyIsInNlYXREIjoxLCJza2V0Y2giOiLlv5jorrDnmbvpmYblr4bnoIHkuobmgI7kuYjlip7vvJ8iLCJ0aXRsZSI6IuW/mOiusOeZu+mZhuWvhueggeS6huaAjuS5iOWKnu+8nyJ9LCJzaWduIjoiMDUyYTgzNDRhZWU2YmJjYTBkYjgzZjY3N2FiZDUwOGUiLCJzdGltZSI6MTU5Mzg2Mzc1Mjg3Mn0="
     *     },
     *     "sgid": "202007041955520000001",
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



    /**
     * @Description: 获取百问百答-类别数据、具体详情-集合
     * @param request
     * @param response
     * @return com.gd.chain.common.utils.JsonResult<java.lang.Object>
     * @author yoko
     * @date 2019/11/25 22:58
     * local:http://localhost:8086/fine/qt/getDataMDList
     * 请求的属性类:RequestQuestion
     * 必填字段:{"agtVer":1,"clientVer":1,"clientType":1,"ctime":201911071802959,"cctime":201911071802959,"sign":"abcdefg","pageNumber":1,"pageSize":3,"token":"111111"}
     * 加密字段:{"jsonData":"eyJhZ3RWZXIiOjEsImNsaWVudFZlciI6MSwiY2xpZW50VHlwZSI6MSwiY3RpbWUiOjIwMTkxMTA3MTgwMjk1OSwiY2N0aW1lIjoyMDE5MTEwNzE4MDI5NTksInNpZ24iOiJhYmNkZWZnIiwicGFnZU51bWJlciI6MSwicGFnZVNpemUiOjMsInRva2VuIjoiMTExMTExIiwiYW5kcm9pZFZlciI6IjcuMS4yIn0="}
     * 客户端加密字段:ctime+cctime+秘钥=sign
     * 服务端加密字段:stime+秘钥=sign
     * result={
     *     "resultCode": "0",
     *     "message": "success",
     *     "data": {
     *         "jsonData": "eyJxTUxpc3QiOlt7ImNhdGVnb3J5TmFtZSI6IuWvhueggeebuOWFsyIsImljb25BZHMiOiJodHRwOi8vaW1nLm1wLml0Yy5jbi91cGxvYWQvMjAxNjExMTEvOTI3MjhiMjViNTJiNGJlNzhjZGYyYTc4MjYyMWYwNmVfdGguanBnIiwiaWQiOjEsInFETGlzdCI6W3siY2F0ZWdvcnlOYW1lIjoi5a+G56CB55u45YWzIiwiY3JlYXRlVGltZSI6IjIwMjAtMDEtMjAgMTU6MTM6NDAiLCJpZCI6NDEsImtleXdvcmQiOiIiLCJwYWdlIjp7fSwicGFnZUFkcyI6Imh0dHA6Ly9pbWcubXAuaXRjLmNuL3VwbG9hZC8yMDE2MTExMS85MjcyOGIyNWI1MmI0YmU3OGNkZjJhNzgyNjIxZjA2ZV90aC5qcGciLCJxdWVzdGlvbk1JZCI6MSwic2VhdEQiOjEsInNrZXRjaCI6IuW/mOiusOeZu+mZhuWvhueggeS6huaAjuS5iOWKnu+8nyIsInRpdGxlIjoi5b+Y6K6w55m76ZmG5a+G56CB5LqG5oCO5LmI5Yqe77yfIiwieW4iOjB9LHsiY2F0ZWdvcnlOYW1lIjoi5a+G56CB55u45YWzIiwiY3JlYXRlVGltZSI6IjIwMjAtMDEtMjAgMTU6MTM6NDAiLCJpZCI6NDIsImtleXdvcmQiOiIiLCJwYWdlIjp7fSwicGFnZUFkcyI6Imh0dHA6Ly9pbWcubXAuaXRjLmNuL3VwbG9hZC8yMDE2MTExMS9jMjdkYmQ4YzNhMmE0ODc5OTgzYmUwMzg2ODYxMGNjY190aC5qcGciLCJxdWVzdGlvbk1JZCI6MSwic2VhdEQiOjIsInNrZXRjaCI6IuaPkOeOsOWvhueggeW/mOiusOS6huaAjuS5iOWKnu+8nyIsInRpdGxlIjoi5o+Q546w5a+G56CB5b+Y6K6w5LqG5oCO5LmI5Yqe77yfIiwieW4iOjB9XSwic2VhdE0iOjF9LHsiY2F0ZWdvcnlOYW1lIjoi55CG6LSi5pS755WlIiwiaWNvbkFkcyI6Imh0dHA6Ly9pbWcubXAuaXRjLmNuL3VwbG9hZC8yMDE2MTExMS9jMjdkYmQ4YzNhMmE0ODc5OTgzYmUwMzg2ODYxMGNjY190aC5qcGciLCJpZCI6MiwicURMaXN0IjpbeyJjYXRlZ29yeU5hbWUiOiLnkIbotKLmlLvnlaUiLCJjcmVhdGVUaW1lIjoiMjAyMC0wMS0yMCAxNToxMzo0MCIsImlkIjo0Mywia2V5d29yZCI6IiIsInBhZ2UiOnt9LCJwYWdlQWRzIjoiaHR0cDovL2ltZy5tcC5pdGMuY24vdXBsb2FkLzIwMTYxMTExL2I5N2UyNTQwZGE3ZDQ3YTU5NTVlYWI0ZWEyMjc0ZjExX3RoLmpwZyIsInF1ZXN0aW9uTUlkIjoyLCJzZWF0RCI6MSwic2tldGNoIjoi44CKNTAw55CG6LSi44CL5piv5LuA5LmI77yfIiwidGl0bGUiOiLjgIo1MDDnkIbotKLjgIvmmK/ku4DkuYjvvJ8iLCJ5biI6MH0seyJjYXRlZ29yeU5hbWUiOiLnkIbotKLmlLvnlaUiLCJjcmVhdGVUaW1lIjoiMjAyMC0wMS0yMCAxNToxMzo0MCIsImlkIjo0NCwia2V5d29yZCI6IiIsInBhZ2UiOnt9LCJwYWdlQWRzIjoiaHR0cDovL2luZXdzLmd0aW1nLmNvbS9uZXdzYXBwX2J0LzAvOTMwMDQzODQ4OC8xMDAwLzAiLCJxdWVzdGlvbk1JZCI6Miwic2VhdEQiOjIsInNrZXRjaCI6IuWmguS9leaIkOS4ulZJUO+8nyIsInRpdGxlIjoi5aaC5L2V5oiQ5Li6VklQ77yfIiwieW4iOjB9LHsiY2F0ZWdvcnlOYW1lIjoi55CG6LSi5pS755WlIiwiY3JlYXRlVGltZSI6IjIwMjAtMDEtMjAgMTU6MTM6NDAiLCJpZCI6NDUsImtleXdvcmQiOiIiLCJwYWdlIjp7fSwicGFnZUFkcyI6Imh0dHA6Ly9uLnNpbmFpbWcuY24vdHJhbnNsYXRlL3c3NjhoNTI3LzIwMTgwMTEyL3BJb3QtZnlxbmljbTIxOTQ5NjIuanBnIiwicXVlc3Rpb25NSWQiOjIsInNlYXREIjozLCJza2V0Y2giOiJWSVDmnInlk6rkupvmnYPnm4rvvJ8iLCJ0aXRsZSI6IlZJUOacieWTquS6m+adg+ebiu+8nyIsInluIjowfSx7ImNhdGVnb3J5TmFtZSI6IueQhui0ouaUu+eVpSIsImNyZWF0ZVRpbWUiOiIyMDIwLTAxLTIwIDE1OjEzOjQwIiwiaWQiOjQ2LCJrZXl3b3JkIjoiIiwicGFnZSI6e30sInBhZ2VBZHMiOiJodHRwOi8vNWIwOTg4ZTU5NTIyNS5jZG4uc29odWNzLmNvbS9pbWFnZXMvMjAxODAzMjQvMmZjNzg4Nzk5NDQ0NDA1NDg2ZTE1M2RjOWE3NmI5Y2EuanBlZyIsInF1ZXN0aW9uTUlkIjoyLCJzZWF0RCI6NCwic2tldGNoIjoi5pyq5a6M5oiQ5byV6I2Q5Lu75Yqh6K+l5aaC5L2V6Kej5Yaz77yfIiwidGl0bGUiOiLmnKrlrozmiJDlvJXojZDku7vliqHor6XlpoLkvZXop6PlhrPvvJ8iLCJ5biI6MH1dLCJzZWF0TSI6Mn0seyJjYXRlZ29yeU5hbWUiOiLlpb3lj4vluK7liqkiLCJpY29uQWRzIjoiaHR0cDovL2ltZy5tcC5pdGMuY24vdXBsb2FkLzIwMTYxMTExL2I5N2UyNTQwZGE3ZDQ3YTU5NTVlYWI0ZWEyMjc0ZjExX3RoLmpwZyIsImlkIjozLCJxRExpc3QiOlt7ImNhdGVnb3J5TmFtZSI6IuWlveWPi+W4ruWKqSIsImNyZWF0ZVRpbWUiOiIyMDIwLTAxLTIwIDE1OjEzOjQwIiwiaWQiOjQ3LCJrZXl3b3JkIjoiIiwicGFnZSI6e30sInBhZ2VBZHMiOiJodHRwOi8vbjEuaXRjLmNuL2ltZzgvd2IvcmVjb20vMjAxNi8wNS8xOS8xNDYzNjI0Mjg0MTYyNDkwMjYuSlBFRyIsInF1ZXN0aW9uTUlkIjozLCJzZWF0RCI6MSwic2tldGNoIjoi5aW95Y+L55u45YWz6K+m5oOF5biu5Yqp44CCIiwidGl0bGUiOiLlpb3lj4vnm7jlhbPor6bmg4XluK7liqnjgIIiLCJ5biI6MH1dLCJzZWF0TSI6M31dLCJyb3dDb3VudCI6NCwic2lnbiI6IjAyOWE5MWZiNjFhM2Q5NDRjYmIxY2ZlZDY0YjQ5NmNkIiwic3RpbWUiOjE1OTQwMTAxMjc2Mjh9"
     *     },
     *     "sgid": "202007061235270000001",
     *     "cgid": ""
     * }
     */
    @RequestMapping(value = "/getDataMDList", method = {RequestMethod.POST})
    public JsonResult<Object> getDataMDList(HttpServletRequest request, HttpServletResponse response, @RequestBody RequestEncryptionJson requestData) throws Exception{
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

            // 百问百答类别数据
            QuestionMModel questionMQuery = BeanUtils.copy(requestModel, QuestionMModel.class);
            List<QuestionMModel> questionMList = ComponentUtil.questionMService.queryByList(questionMQuery);
            List<QuestionMModel> mdList = new ArrayList<>();
            if (questionMList != null && questionMList.size() > 0){
                for (QuestionMModel dataMModel : questionMList){
                    // 百问百答-详情集合数据
                    QuestionDModel questionDQuery = HodgepodgeMethod.assembleQuestionDQuery(dataMModel.getId());
                    List<QuestionDModel> questionDList = ComponentUtil.questionDService.findByCondition(questionDQuery);
                    if (questionDList != null && questionDList.size() > 0){
                        dataMModel.setqDList(questionDList);
                    }
                    mdList.add(dataMModel);
                }
            }
            // 组装返回客户端的数据
            long stime = System.currentTimeMillis();
            String sign = SignUtil.getSgin(stime, secretKeySign); // stime+秘钥=sign
            String strData = HodgepodgeMethod.assembleQuestionMResult(stime, sign, mdList, questionMQuery.getRowCount());
            // 数据加密
            String encryptionData = StringUtil.mergeCodeBase64(strData);
            ResponseEncryptionJson resultDataModel = new ResponseEncryptionJson();
            resultDataModel.jsonData = encryptionData;
            // 返回数据给客户端
            return JsonResult.successResult(resultDataModel, cgid, sgid);
        }catch (Exception e){
            Map<String,String> map = ExceptionMethod.getException(e, ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_TWO);
            log.error(String.format("this QuestionController.getDataMDList() is error , the cgid=%s and sgid=%s and all data=%s!", cgid, sgid, data));
            e.printStackTrace();
            return JsonResult.failedResult(map.get("message"), map.get("code"), cgid, sgid);
        }
    }


    /**
     * @Description: 百问百答问题-详情-步骤的-集合数据
     * @param request
     * @param response
     * @return com.gd.chain.common.utils.JsonResult<java.lang.Object>
     * @author yoko
     * @date 2019/11/25 22:58
     * local:http://localhost:8086/fine/qt/getDataDDList
     * 请求的属性类:RequestQuestion
     * 必填字段:{"questionDId":41,"agtVer":1,"clientVer":1,"clientType":1,"ctime":201911071802959,"cctime":201911071802959,"sign":"abcdefg","pageNumber":1,"pageSize":3,"token":"111111"}
     * 加密字段:{"jsonData":"eyJxdWVzdGlvbkRJZCI6NDEsImFndFZlciI6MSwiY2xpZW50VmVyIjoxLCJjbGllbnRUeXBlIjoxLCJjdGltZSI6MjAxOTExMDcxODAyOTU5LCJjY3RpbWUiOjIwMTkxMTA3MTgwMjk1OSwic2lnbiI6ImFiY2RlZmciLCJwYWdlTnVtYmVyIjoxLCJwYWdlU2l6ZSI6MywidG9rZW4iOiIxMTExMTEifQ=="}
     * 客户端加密字段:ctime+cctime+秘钥=sign
     * 服务端加密字段:stime+秘钥=sign
     * result={
     *     "resultCode": "0",
     *     "message": "success",
     *     "data": {
     *         "jsonData": "eyJxRExpc3QiOlt7ImNhdGVnb3J5TmFtZSI6IuWvhueggeebuOWFsyIsImlkIjo0MSwicGFnZUFkcyI6Imh0dHA6Ly9xNTV2bmRiaWYuYmt0LmNsb3VkZG4uY29tL3VwbG9hZCUyRmltYWdlJTJGMjAyMF8wMl8wNCUyRjAyNGI4OWQ0NDU5OGE2MmQ1ZmI1N2M1MjliMjZjODI3MWJiYzAyZjEucG5nIiwic2VhdEQiOjEsInNrZXRjaCI6IuW/mOiusOeZu+mZhuWvhueggeS6huaAjuS5iOWKnu+8nyIsInRpdGxlIjoi5b+Y6K6w55m76ZmG5a+G56CB5LqG5oCO5LmI5Yqe77yfIn0seyJjYXRlZ29yeU5hbWUiOiLlr4bnoIHnm7jlhbMiLCJpZCI6NDIsInBhZ2VBZHMiOiJodHRwOi8vcTU1dm5kYmlmLmJrdC5jbG91ZGRuLmNvbS91cGxvYWQlMkZpbWFnZSUyRjIwMjBfMDJfMDQlMkY5OWJjMjNmNzNkOThkYWJmOTVmN2Y0MDY4NTJlMTFjYWE5MTI1NzA5LnBuZyIsInNlYXREIjoyLCJza2V0Y2giOiLmj5DnjrDlr4bnoIHlv5jorrDkuobmgI7kuYjlip7vvJ8iLCJ0aXRsZSI6IuaPkOeOsOWvhueggeW/mOiusOS6huaAjuS5iOWKnu+8nyJ9XSwicm93Q291bnQiOjIsInNpZ24iOiI1YzI4YzIyMDYxNTMxYzkyYmNjMDZjYTFjMmZjMjgwMCIsInN0aW1lIjoxNTkzODYzNjUyMzUzfQ=="
     *     },
     *     "sgid": "202007041954110000001",
     *     "cgid": ""
     * }
     */
    @RequestMapping(value = "/getDataDDList", method = {RequestMethod.POST})
    public JsonResult<Object> getDataDDList(HttpServletRequest request, HttpServletResponse response, @RequestBody RequestEncryptionJson requestData) throws Exception{
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


            // 百问百答-详情-步骤集合数据
            QuestionDDModel questionDDQuery = BeanUtils.copy(requestModel, QuestionDDModel.class);
            List<QuestionDDModel> questionDDList = ComponentUtil.questionDDService.queryByList(questionDDQuery);
            // 组装返回客户端的数据
            long stime = System.currentTimeMillis();
            String sign = SignUtil.getSgin(stime, secretKeySign); // stime+秘钥=sign
            String strData = HodgepodgeMethod.assembleQuestionDDResult(stime, sign, questionDDList, questionDDQuery.getRowCount());
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



}
