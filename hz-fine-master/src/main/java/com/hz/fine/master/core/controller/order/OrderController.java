package com.hz.fine.master.core.controller.order;

import com.alibaba.fastjson.JSON;
import com.hz.fine.master.core.common.exception.ExceptionMethod;
import com.hz.fine.master.core.common.utils.JsonResult;
import com.hz.fine.master.core.common.utils.SignUtil;
import com.hz.fine.master.core.common.utils.StringUtil;
import com.hz.fine.master.core.common.utils.constant.ServerConstant;
import com.hz.fine.master.core.model.RequestEncryptionJson;
import com.hz.fine.master.core.model.ResponseEncryptionJson;
import com.hz.fine.master.core.model.did.DidLevelModel;
import com.hz.fine.master.core.model.did.DidModel;
import com.hz.fine.master.core.model.order.OrderModel;
import com.hz.fine.master.core.model.region.RegionModel;
import com.hz.fine.master.core.protocol.request.did.RequestDid;
import com.hz.fine.master.core.protocol.request.order.RequestOrder;
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
 * @Description 任务订单（平台派发订单）的Controller层
 * @Author yoko
 * @Date 2020/5/22 10:21
 * @Version 1.0
 */
@RestController
@RequestMapping("/fine/order")
public class OrderController {

    private static Logger log = LoggerFactory.getLogger(OrderController.class);

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
     * @Description: 派发订单
     * <p>
     * 1.用户余额是否满足此次派单的金额。
     * 2.用户的使用状态是否正常：需要正常使用状态的用户。
     * 3.已经派单过的用户并且名下有订单没有过失效期的用户不派单。
     * 4.有冻结金额的也要排除派单（冻结金额包含两层意思：有订单没有过有效期的，名下挂了有质疑的订单）
     * 5.判断是否拥有已审核通过的收款账号。
     * 6.判断收款账号的开关全部是开启的。
     * 7.判断收款账号的使用状态。
     * 8.判断此账号的小微账号是否上线
     * </p>
     * @param request
     * @param response
     * @return com.gd.chain.common.utils.JsonResult<java.lang.Object>
     * @author yoko
     * @date 2019/11/25 22:58
     * local:http://localhost:8086/fine/order/add
     * 请求的属性类:RequestOrder
     * 必填字段:{"money":"1111","payType":1,"agtVer":1,"clientVer":1,"clientType":1,"ctime":201911071802959,"cctime":201911071802959,"sign":"abcdefg","token":"111111"}
     * 加密字段:{"jsonData":"eyJuaWNrbmFtZSI6Im5pY2tuYW1lMSIsImFjTnVtIjoiMTU5NjcxNzE0MTUiLCJwYXNzV2QiOiJwYXNzV2QxIiwib3BlcmF0ZVdkIjoib3BlcmF0ZVdkMSIsImljb2RlIjoiMSIsInZjb2RlIjoiMTExMSIsImFndFZlciI6MSwiY2xpZW50VmVyIjoxLCJjbGllbnRUeXBlIjoxLCJjdGltZSI6MjAxOTExMDcxODAyOTU5LCJjY3RpbWUiOjIwMTkxMTA3MTgwMjk1OSwic2lnbiI6ImFiY2RlZmciLCJ0b2tlbiI6IjExMTExMSJ9"}
     * 客户端加密字段:ctime+cctime+秘钥=sign
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
    @RequestMapping(value = "/add", method = {RequestMethod.POST})
    public JsonResult<Object> add(HttpServletRequest request, HttpServletResponse response, @RequestBody RequestEncryptionJson requestData) throws Exception{
        String sgid = ComponentUtil.redisIdService.getNewId();
        String cgid = "";
        String token = "";
        String ip = StringUtil.getIpAddress(request);
        String data = "";
        long did = 0;
        RegionModel regionModel = HodgepodgeMethod.assembleRegionModel(ip);

        RequestOrder requestModel = new RequestOrder();
        try{
            // 解密
            data = StringUtil.decoderBase64(requestData.jsonData);
            requestModel  = JSON.parseObject(data, RequestOrder.class);

            // check校验数据
            HodgepodgeMethod.checkOrderAdd(requestModel);

            // 获取可派单的用户集合
            DidModel didQuery = HodgepodgeMethod.assembleEffectiveDid(requestModel);
            List<DidModel> didList = ComponentUtil.didService.getEffectiveDidList(didQuery);

            // 循环筛选有效
//            段峰
            ComponentUtil.orderService.screenCollectionAccount(didList, requestModel.money, requestModel.payType);

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
            log.error(String.format("this OrderController.add() is error , the cgid=%s and sgid=%s and all data=%s!", cgid, sgid, data));
            e.printStackTrace();
            return JsonResult.failedResult(map.get("message"), map.get("code"), cgid, sgid);
        }
    }



    /**
     * @Description: 用户获取派单信息-集合
     * @param request
     * @param response
     * @return com.gd.chain.common.utils.JsonResult<java.lang.Object>
     * @author yoko
     * @date 2019/11/25 22:58
     * local:http://localhost:8086/fine/order/getDataList
     * 请求的属性类:RequestAppeal
     * 必填字段:{"orderStatus":4,"curdayStart":20200529,"curdayEnd":20200529,"agtVer":1,"clientVer":1,"clientType":1,"ctime":201911071802959,"cctime":201911071802959,"sign":"abcdefg","pageNumber":1,"pageSize":3,"token":"111111"}
     * 加密字段:{"jsonData":"eyJvcmRlclN0YXR1cyI6NCwiY3VyZGF5U3RhcnQiOjIwMjAwNTI5LCJjdXJkYXlFbmQiOjIwMjAwNTI5LCJhZ3RWZXIiOjEsImNsaWVudFZlciI6MSwiY2xpZW50VHlwZSI6MSwiY3RpbWUiOjIwMTkxMTA3MTgwMjk1OSwiY2N0aW1lIjoyMDE5MTEwNzE4MDI5NTksInNpZ24iOiJhYmNkZWZnIiwicGFnZU51bWJlciI6MSwicGFnZVNpemUiOjMsInRva2VuIjoiMTExMTExIn0="}
     * 客户端加密字段:ctime+秘钥=sign
     * 返回加密字段:stime+秘钥=sign
     * result={
     *     "resultCode": "0",
     *     "message": "success",
     *     "data": {
     *         "jsonData": "eyJkYXRhTGlzdCI6W3siYWNOYW1lIjoiYWNOYW1lMyIsImNvbGxlY3Rpb25UeXBlIjoxLCJjcmVhdGVUaW1lIjoiMjAyMC0wNS0yOSAxNDoyMjowNyIsIm9yZGVyTW9uZXkiOiIzMC4wMyIsIm9yZGVyTm8iOiJvcmRlcl9ub18zIiwib3JkZXJTdGF0dXMiOjR9LHsiYWNOYW1lIjoiYWNOYW1lMSIsImNvbGxlY3Rpb25UeXBlIjoxLCJjcmVhdGVUaW1lIjoiMjAyMC0wNS0yOSAxNDoyMjowNyIsIm9yZGVyTW9uZXkiOiI0MCIsIm9yZGVyTm8iOiJvcmRlcl9ub180Iiwib3JkZXJTdGF0dXMiOjR9LHsiYWNOYW1lIjoiYWNOYW1lMiIsImNvbGxlY3Rpb25UeXBlIjoxLCJjcmVhdGVUaW1lIjoiMjAyMC0wNS0yOSAxNDoyMjowNyIsIm9yZGVyTW9uZXkiOiI1MC4wMCIsIm9yZGVyTm8iOiJvcmRlcl9ub181Iiwib3JkZXJTdGF0dXMiOjR9XSwicm93Q291bnQiOjYsInNpZ24iOiI3MzMwNGU1OTU0MTYwOGEzZGRmNjcwNTZlZTNjZDg3NSIsInN0aW1lIjoxNTkwNzQ1MjEzNzM1fQ=="
     *     },
     *     "sgid": "202005291740120000001",
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

        RequestOrder requestModel = new RequestOrder();
        try{
            // 解密
            data = StringUtil.decoderBase64(requestData.jsonData);
            requestModel  = JSON.parseObject(data, RequestOrder.class);
            //#临时数据
            if (!StringUtils.isBlank(requestModel.token)){
                if (requestModel.token.equals("111111")){
                    ComponentUtil.redisService.set(requestModel.token, "1");
                }
            }
            // check校验数据
            did = HodgepodgeMethod.checkOrderListData(requestModel);

            // 获取用户派单集合数据
            OrderModel orderQuery = HodgepodgeMethod.assembleOrderListByDid(requestModel, did);
            List<OrderModel> orderList = ComponentUtil.orderService.queryByList(orderQuery);
            // 组装返回客户端的数据
            long stime = System.currentTimeMillis();
            String sign = SignUtil.getSgin(stime, secretKeySign); // stime+秘钥=sign
            String strData = HodgepodgeMethod.assembleOrderListResult(stime, sign, orderList, orderQuery.getRowCount());
            // 数据加密
            String encryptionData = StringUtil.mergeCodeBase64(strData);
            ResponseEncryptionJson resultDataModel = new ResponseEncryptionJson();
            resultDataModel.jsonData = encryptionData;
            // 返回数据给客户端
            return JsonResult.successResult(resultDataModel, cgid, sgid);
        }catch (Exception e){
            Map<String,String> map = ExceptionMethod.getException(e, ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_TWO);
            // #添加异常
            log.error(String.format("this OrderController.getDataList() is error , the cgid=%s and sgid=%s and all data=%s!", cgid, sgid, data));
            e.printStackTrace();
            return JsonResult.failedResult(map.get("message"), map.get("code"), cgid, sgid);
        }
    }




    /**
     * @Description: 获取派单数据-详情
     * @param request
     * @param response
     * @return com.gd.chain.common.utils.JsonResult<java.lang.Object>
     * @author yoko
     * @date 2019/11/25 22:58
     * local:http://localhost:8086/fine/order/getData
     * 请求的属性类:RequestOrder
     * 必填字段:{"orderNo":"order_no_3","agtVer":1,"clientVer":1,"clientType":1,"ctime":201911071802959,"cctime":201911071802959,"sign":"abcdefg","token":"111111"}
     * 加密字段:{"jsonData":"eyJvcmRlck5vIjoib3JkZXJfbm9fMyIsImFndFZlciI6MSwiY2xpZW50VmVyIjoxLCJjbGllbnRUeXBlIjoxLCJjdGltZSI6MjAxOTExMDcxODAyOTU5LCJjY3RpbWUiOjIwMTkxMTA3MTgwMjk1OSwic2lnbiI6ImFiY2RlZmciLCJ0b2tlbiI6IjExMTExMSJ9"}
     * 客户端加密字段:id+ctime+cctime+秘钥=sign
     * 服务端加密字段:stime+秘钥=sign
     * result={
     *     "resultCode": "0",
     *     "message": "success",
     *     "data": {
     *         "jsonData": "eyJkYXRhTW9kZWwiOnsiYWNOYW1lIjoiYWNOYW1lMyIsImNvbGxlY3Rpb25UeXBlIjoxLCJjcmVhdGVUaW1lIjoiMjAyMC0wNS0yOSAxNDoyMjowNyIsIm9yZGVyTW9uZXkiOiIzMC4wMyIsIm9yZGVyTm8iOiJvcmRlcl9ub18zIiwib3JkZXJTdGF0dXMiOjR9LCJzaWduIjoiZTdiMzNlZTJiNWU5ZTVhNGVkOWQ4ZDU4NmEzZGM5YTUiLCJzdGltZSI6MTU5MDc0NTc5OTQzMX0="
     *     },
     *     "sgid": "202005291749570000001",
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

        RequestOrder requestModel = new RequestOrder();
        try{
            // 解密
            data = StringUtil.decoderBase64(requestData.jsonData);
            requestModel  = JSON.parseObject(data, RequestOrder.class);
            //#临时数据
            if (!StringUtils.isBlank(requestModel.token)){
                if (requestModel.token.equals("111111")){
                    ComponentUtil.redisService.set(requestModel.token, "1");
                }
            }
            // check校验请求的数据
            did = HodgepodgeMethod.checkOrderData(requestModel);

            // 收款账号详情数据
            OrderModel orderQuery = HodgepodgeMethod.assembleOrderDataByOrderNo(did, requestModel.orderNo);
            OrderModel orderModelData = (OrderModel) ComponentUtil.orderService.findByObject(orderQuery);
            // 组装返回客户端的数据
            long stime = System.currentTimeMillis();
            String sign = SignUtil.getSgin(stime, secretKeySign); // stime+秘钥=sign
            String strData = HodgepodgeMethod.assembleOrderDataResult(stime, sign, orderModelData);
            // 数据加密
            String encryptionData = StringUtil.mergeCodeBase64(strData);
            ResponseEncryptionJson resultDataModel = new ResponseEncryptionJson();
            resultDataModel.jsonData = encryptionData;
            // 返回数据给客户端
            return JsonResult.successResult(resultDataModel, cgid, sgid);
        }catch (Exception e){
            Map<String,String> map = ExceptionMethod.getException(e, ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_TWO);
            // 添加异常
            log.error(String.format("this OrderController.getData() is error , the cgid=%s and sgid=%s and all data=%s!", cgid, sgid, data));
            e.printStackTrace();
            return JsonResult.failedResult(map.get("message"), map.get("code"), cgid, sgid);
        }
    }


}
