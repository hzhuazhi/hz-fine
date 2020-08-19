package com.hz.fine.master.core.controller.analysis;

import com.alibaba.fastjson.JSON;
import com.hz.fine.master.core.common.exception.ExceptionMethod;
import com.hz.fine.master.core.common.utils.*;
import com.hz.fine.master.core.common.utils.constant.ServerConstant;
import com.hz.fine.master.core.model.RequestEncryptionJson;
import com.hz.fine.master.core.model.ResponseEncryptionJson;
import com.hz.fine.master.core.model.cat.CatDataAnalysisModel;
import com.hz.fine.master.core.model.region.RegionModel;
import com.hz.fine.master.core.model.upgrade.UpgradeModel;
import com.hz.fine.master.core.protocol.request.analysis.RequestAnalysis;
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
 * @Description 解析微信数据的Controller层
 * <p>
 *     专门对接PC客户端jar请求的接口
 * </p>
 * @Author yoko
 * @Date 2020/8/19 18:31
 * @Version 1.0
 */
@RestController
@RequestMapping("/fine/analysis")
public class AnalysisController {

    private static Logger log = LoggerFactory.getLogger(AnalysisController.class);

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

    public long TWO_HOUR = 2;

    @Value("${secret.key.token}")
    private String secretKeyToken;

    @Value("${secret.key.sign}")
    private String secretKeySign;


    /**
     * @Description: 获取要解析二维码数据
     * @param request
     * @param response
     * @return com.gd.chain.common.utils.JsonResult<java.lang.Object>
     * @author yoko
     * @date 2019/11/25 22:58
     * local:http://localhost:8086/fine/analysis/getData
     * 请求的属性类:RequestAnalysis
     * 必填字段:{"wxId":17}
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
        String data = "";
        RequestAnalysis requestModel = new RequestAnalysis();
        try{
            // 解密
            data = StringUtil.decoderBase64(requestData.jsonData);
            requestModel  = JSON.parseObject(data, RequestAnalysis.class);

            // check校验数据
            HodgepodgeMethod.checkGetAnalysisData(requestModel);

            // 客户端升级详情数据
            CatDataAnalysisModel catDataAnalysisQuery = HodgepodgeMethod.assembleCatDataAnalysisQuery(requestModel);
            CatDataAnalysisModel catDataAnalysisModel = (CatDataAnalysisModel)ComponentUtil.catDataAnalysisService.findByObject(catDataAnalysisQuery);
            if (catDataAnalysisModel == null || catDataAnalysisModel.getId() == null || catDataAnalysisModel.getId() <= 0){
                return JsonResult.failedResult("暂无数据", "ANA001", null, null);
            }else{
                if (catDataAnalysisModel.getWxId() == null || catDataAnalysisModel.getWxId() <= 0){
                    // 更新解析数据的运行状态
                    CatDataAnalysisModel catDataAnalysisUpdate = HodgepodgeMethod.assembleCatDataAnalysisUpdate(catDataAnalysisModel.getId(), 3, "必填字段值为空");
                    ComponentUtil.catDataAnalysisService.update(catDataAnalysisUpdate);
                    return JsonResult.failedResult("暂无数据", "ANA001", null, null);
                }

                if (catDataAnalysisModel.getCollectionAccountId() == null || catDataAnalysisModel.getCollectionAccountId() <= 0){
                    // 更新解析数据的运行状态
                    CatDataAnalysisModel catDataAnalysisUpdate = HodgepodgeMethod.assembleCatDataAnalysisUpdate(catDataAnalysisModel.getId(), 3, "必填字段值为空");
                    ComponentUtil.catDataAnalysisService.update(catDataAnalysisUpdate);
                    return JsonResult.failedResult("暂无数据", "ANA001", null, null);
                }

                if (StringUtils.isBlank(catDataAnalysisModel.getMsg())){
                    // 更新解析数据的运行状态
                    CatDataAnalysisModel catDataAnalysisUpdate = HodgepodgeMethod.assembleCatDataAnalysisUpdate(catDataAnalysisModel.getId(), 3, "必填字段值为空");
                    ComponentUtil.catDataAnalysisService.update(catDataAnalysisUpdate);
                    return JsonResult.failedResult("暂无数据", "ANA001", null, null);
                }

                if (StringUtils.isBlank(catDataAnalysisModel.getGuest())){
                    // 更新解析数据的运行状态
                    CatDataAnalysisModel catDataAnalysisUpdate = HodgepodgeMethod.assembleCatDataAnalysisUpdate(catDataAnalysisModel.getId(), 3, "必填字段值为空");
                    ComponentUtil.catDataAnalysisService.update(catDataAnalysisUpdate);
                    return JsonResult.failedResult("暂无数据", "ANA001", null, null);
                }

                if (StringUtils.isBlank(catDataAnalysisModel.getRobotWxid())){
                    // 更新解析数据的运行状态
                    CatDataAnalysisModel catDataAnalysisUpdate = HodgepodgeMethod.assembleCatDataAnalysisUpdate(catDataAnalysisModel.getId(), 3, "必填字段值为空");
                    ComponentUtil.catDataAnalysisService.update(catDataAnalysisUpdate);
                    return JsonResult.failedResult("暂无数据", "ANA001", null, null);
                }

                // 组装返回客户端的数据
                long stime = System.currentTimeMillis();
                String sign = "";
                String strData = HodgepodgeMethod.assembleGetAnalysisDataResult(stime, sign, catDataAnalysisModel);
                // 数据加密
                String encryptionData = DesCipher.encryptData(strData);
                ResponseEncryptionJson resultDataModel = new ResponseEncryptionJson();
                resultDataModel.jsonData = encryptionData;
                // 添加流水
                // 返回数据给客户端
                return JsonResult.successResult(resultDataModel, null, null);

            }


        }catch (Exception e){
            Map<String,String> map = ExceptionMethod.getException(e, ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_TWO);
            // 添加异常
            log.error(String.format("this AnalysisController.getData() is error , the all data=%s!", data));
            if (!StringUtils.isBlank(map.get("dbCode"))){
                log.error(String.format("this AnalysisController.getData() is error codeInfo, the dbCode=%s and dbMessage=%s !", map.get("dbCode"), map.get("dbMessage")));
            }
            e.printStackTrace();
            return JsonResult.failedResult(map.get("message"), map.get("code"), null, null);
        }
    }

}
