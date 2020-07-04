package com.hz.fine.master.core.controller.notice;

import com.alibaba.fastjson.JSON;
import com.hz.fine.master.core.common.exception.ExceptionMethod;
import com.hz.fine.master.core.common.utils.BeanUtils;
import com.hz.fine.master.core.common.utils.JsonResult;
import com.hz.fine.master.core.common.utils.SignUtil;
import com.hz.fine.master.core.common.utils.StringUtil;
import com.hz.fine.master.core.common.utils.constant.ServerConstant;
import com.hz.fine.master.core.model.RequestEncryptionJson;
import com.hz.fine.master.core.model.ResponseEncryptionJson;
import com.hz.fine.master.core.model.notice.NoticeModel;
import com.hz.fine.master.core.protocol.request.notice.RequestNotice;
import com.hz.fine.master.util.ComponentUtil;
import com.hz.fine.master.util.HodgepodgeMethod;
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
 * @Description 公告的Controller层
 * @Author yoko
 * @Date 2020/1/14 20:09
 * @Version 1.0
 */
@RestController
@RequestMapping("/fine/nc")
public class NoticeDController {
    private static Logger log = LoggerFactory.getLogger(NoticeDController.class);

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
     * @Description: 获取公告数据-集合
     * @param request
     * @param response
     * @return com.gd.chain.common.utils.JsonResult<java.lang.Object>
     * @author yoko
     * @date 2019/11/25 22:58
     * local:http://localhost:8082/fine/nc/getDataList
     * 请求的属性类:RequestAppeal
     * 必填字段:{"noticeType":1,"agtVer":1,"clientVer":1,"clientType":1,"ctime":201911071802959,"cctime":201911071802959,"sign":"abcdefg","pageNumber":1,"pageSize":3,"token":"111111"}
     * 客户端加密字段:ctime+秘钥=sign
     * 返回加密字段:stime+秘钥=sign
     *
     * {
     *     "resultCode": "0",
     *     "message": "success",
     *     "data": {
     *         "jsonData": "eyJuY0xpc3QiOlt7ImNvbnRlbnQiOiIiLCJjcmVhdGVUaW1lIjoiMjAyMC0wMS0yMCAxODozMDozMCIsImljb25BZHMiOiJodHRwOi8vNTAwLmx6c2h1LmNuLzE1Nzk1MjY4MzMxMzAuanBnIiwiaWQiOjExLCJwYWdlQWRzIjoiaHR0cDovLzUwMC5senNodS5jbi8xNTc5NTI2NDM0NjkyLmpwZyIsInNrZXRjaCI6IuesrOS4gOadoeWFrOWRiiIsInRpdGxlIjoiNTAw55CG6LSiIOivlei/kOihjOmAmuefpSJ9XSwicm93Q291bnQiOjEsInNpZ24iOiI5NjM1MDllOWU4OWM4MWNjYjA3ZjI3OTIwODMxZGI3OSIsInN0aW1lIjoxNTgzMjE0ODIwMzMwfQ=="
     *     },
     *     "sgid": "202003031353400000001",
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

        RequestNotice requestModel = new RequestNotice();
        try{
            // 解密
            data = StringUtil.decoderBase64(requestData.jsonData);
            requestModel  = JSON.parseObject(data, RequestNotice.class);
            // 公告数据
            NoticeModel noticeQuery = BeanUtils.copy(requestModel, NoticeModel.class);
            List<NoticeModel> noticeList = ComponentUtil.noticeService.queryByList(noticeQuery);
            // 组装返回客户端的数据
            long stime = System.currentTimeMillis();
            String sign = SignUtil.getSgin(stime, secretKeySign); // stime+秘钥=sign
            String strData = HodgepodgeMethod.assembleNoticeResult(stime, sign, noticeList, noticeQuery.getRowCount());
            // 数据加密
            String encryptionData = StringUtil.mergeCodeBase64(strData);
            ResponseEncryptionJson resultDataModel = new ResponseEncryptionJson();
            resultDataModel.jsonData = encryptionData;
            // #添加流水
            // 返回数据给客户端
            return JsonResult.successResult(resultDataModel, cgid, sgid);
        }catch (Exception e){
            Map<String,String> map = ExceptionMethod.getException(e, ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_TWO);
            // #添加异常
            log.error(String.format("this NoticeController.getDataList() is error , the cgid=%s and sgid=%s and all data=%s!", cgid, sgid, data));
            e.printStackTrace();
            return JsonResult.failedResult(map.get("message"), map.get("code"), cgid, sgid);
        }
    }


    /**
     * @Description: 获取公告数据-详情
     * @param request
     * @param response
     * @return com.gd.chain.common.utils.JsonResult<java.lang.Object>
     * @author yoko
     * @date 2019/11/25 22:58
     * local:http://localhost:8082/fine/nc/getData
     * 请求的属性类:RequestAppeal
     * 必填字段:{"id":1,"agtVer":1,"clientVer":1,"clientType":1,"ctime":201911071802959,"cctime":201911071802959,"sign":"abcdefg","token":"111111"}
     * 客户端加密字段:id+ctime+cctime+秘钥=sign
     * 服务端加密字段:stime+秘钥=sign
     * {
     *     "resultCode": "0",
     *     "message": "success",
     *     "data": {
     *         "jsonData": "eyJuYyI6eyJjb250ZW50IjoiIiwiY3JlYXRlVGltZSI6IjIwMjAtMDEtMjAgMTg6MzA6MzAiLCJpY29uQWRzIjoiaHR0cDovLzUwMC5senNodS5jbi8xNTc5NTI2ODMzMTMwLmpwZyIsImlkIjoxMSwicGFnZUFkcyI6Imh0dHA6Ly81MDAubHpzaHUuY24vMTU3OTUyNjQzNDY5Mi5qcGciLCJza2V0Y2giOiLnrKzkuIDmnaHlhazlkYoiLCJ0aXRsZSI6IjUwMOeQhui0oiDor5Xov5DooYzpgJrnn6UifSwic2lnbiI6ImI1ZTBiMTdiNGIxNWIzNjlhMGNkOTlmNTEzNTYzOGJiIiwic3RpbWUiOjE1ODMyMTQ4NzU1NDd9"
     *     },
     *     "sgid": "202003031354350000001",
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

        RequestNotice requestModel = new RequestNotice();
        try{
            // 解密
            data = StringUtil.decoderBase64(requestData.jsonData);
            requestModel  = JSON.parseObject(data, RequestNotice.class);
            // check校验请求的数据
            HodgepodgeMethod.checkNoticeGetData(requestModel);
            // 公告数据
            NoticeModel noticeQuery = BeanUtils.copy(requestModel, NoticeModel.class);
            NoticeModel noticeModel = (NoticeModel) ComponentUtil.noticeService.findByObject(noticeQuery);
            // 组装返回客户端的数据
            long stime = System.currentTimeMillis();
            String sign = SignUtil.getSgin(stime, secretKeySign); // stime+秘钥=sign
            String strData = HodgepodgeMethod.assembleNoticeDataResult(stime, sign, noticeModel);
            // 数据加密
            String encryptionData = StringUtil.mergeCodeBase64(strData);
            ResponseEncryptionJson resultDataModel = new ResponseEncryptionJson();
            resultDataModel.jsonData = encryptionData;
            // 添加流水
            // 返回数据给客户端
            return JsonResult.successResult(resultDataModel, cgid, sgid);
        }catch (Exception e){
            Map<String,String> map = ExceptionMethod.getException(e, ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_TWO);
            // 添加异常
            log.error(String.format("this NoticeController.getData() is error , the cgid=%s and sgid=%s and all data=%s!", cgid, sgid, data));
            e.printStackTrace();
            return JsonResult.failedResult(map.get("message"), map.get("code"), cgid, sgid);
        }
    }
}
