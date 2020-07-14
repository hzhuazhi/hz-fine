package com.hz.fine.master.core.controller.order;

import com.alibaba.fastjson.JSON;
import com.hz.fine.master.core.common.exception.ExceptionMethod;
import com.hz.fine.master.core.common.utils.DateUtil;
import com.hz.fine.master.core.common.utils.JsonResult;
import com.hz.fine.master.core.common.utils.SignUtil;
import com.hz.fine.master.core.common.utils.StringUtil;
import com.hz.fine.master.core.common.utils.constant.ServerConstant;
import com.hz.fine.master.core.model.RequestEncryptionJson;
import com.hz.fine.master.core.model.ResponseEncryptionJson;
import com.hz.fine.master.core.model.client.ClientCollectionDataModel;
import com.hz.fine.master.core.model.did.DidBalanceDeductModel;
import com.hz.fine.master.core.model.did.DidCollectionAccountModel;
import com.hz.fine.master.core.model.did.DidModel;
import com.hz.fine.master.core.model.did.DidRewardModel;
import com.hz.fine.master.core.model.order.OrderModel;
import com.hz.fine.master.core.model.region.RegionModel;
import com.hz.fine.master.core.model.strategy.StrategyData;
import com.hz.fine.master.core.model.strategy.StrategyModel;
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

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import java.util.Random;

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


//    /**
//     * @Description: 派发订单
//     * <p>
//     * 1.用户余额是否满足此次派单的金额。
//     * 2.用户的使用状态是否正常：需要正常使用状态的用户。
//     * 3.已经派单过的用户并且名下有订单没有过失效期的用户不派单。
//     * 4.有冻结金额的也要排除派单（冻结金额包含两层意思：有订单没有过有效期的，名下挂了有质疑的订单）
//     * 5.判断是否拥有已审核通过的收款账号。
//     * 6.判断收款账号的开关全部是开启的。
//     * 7.判断收款账号的使用状态。
//     * 8.判断此账号的小微账号是否上线。
//     * 9.派单进行中：需要从余额里面剔除相对应的订单金额，把剔除到的存放到冻结金额里面。
//     * </p>
//     * @param request
//     * @param response
//     * @return com.gd.chain.common.utils.JsonResult<java.lang.Object>
//     * @author yoko
//     * @date 2019/11/25 22:58
//     * local:http://localhost:8086/fine/order/qrCode
//     * 请求的属性类:RequestOrder
//     * 必填字段:{"money":"1111","payType":1,"outTradeNo":"outTradeNo1","notifyUrl":"notify_url","agtVer":1,"clientVer":1,"clientType":1,"ctime":201911071802959,"cctime":201911071802959,"sign":"abcdefg","token":"111111"}
//     * 加密字段:{"jsonData":"eyJtb25leSI6IjExMTEiLCJwYXlUeXBlIjoxLCJub3RpZnlVcmwiOiJub3RpZnlfdXJsIiwiYWd0VmVyIjoxLCJjbGllbnRWZXIiOjEsImNsaWVudFR5cGUiOjEsImN0aW1lIjoyMDE5MTEwNzE4MDI5NTksImNjdGltZSI6MjAxOTExMDcxODAyOTU5LCJzaWduIjoiYWJjZGVmZyIsInRva2VuIjoiMTExMTExIn0="}
//     * 客户端加密字段:ctime+cctime+秘钥=sign
//     * 服务端加密字段:stime+秘钥=sign
//     * result={
//     *     "resultCode": "0",
//     *     "message": "success",
//     *     "data": {
//     *         "jsonData": "eyJvcmRlciI6eyJpbnZhbGlkVGltZSI6IjIwMjAtMDYtMDIgMTY6MTk6MjkiLCJvcmRlck1vbmV5IjoiMTExMSIsIm9yZGVyTm8iOiIyMDIwMDYwMjE2MDYzODAwMDAwMDEiLCJxckNvZGUiOiJkZF9xcl9jb2RlMyJ9LCJzaWduIjoiIiwic3RpbWUiOjE1OTEwODUzNjkzMTB9"
//     *     },
//     *     "sgid": "202006021606380000001",
//     *     "cgid": ""
//     * }
//     */
//    @RequestMapping(value = "/qrCode", method = {RequestMethod.POST})
//    public JsonResult<Object> qrCode(HttpServletRequest request, HttpServletResponse response, @RequestBody RequestEncryptionJson requestData) throws Exception{
//        String sgid = ComponentUtil.redisIdService.getNewId();
//        String cgid = "";
//        String token = "";
//        String ip = StringUtil.getIpAddress(request);
//        String data = "";
//        long did = 0;
//        RegionModel regionModel = HodgepodgeMethod.assembleRegionModel(ip);
//
//        RequestOrder requestModel = new RequestOrder();
//        try{
//            // 解密
//            data = StringUtil.decoderBase64(requestData.jsonData);
//            requestModel  = JSON.parseObject(data, RequestOrder.class);
//
//            // check校验数据
//            HodgepodgeMethod.checkOrderAdd(requestModel);
//
//            // 获取可派单的用户集合
//            DidModel didQuery = HodgepodgeMethod.assembleEffectiveDid(requestModel);
//            List<DidModel> didList = ComponentUtil.didService.getEffectiveDidList(didQuery);
//            // check校验是否有有效的用户
//            HodgepodgeMethod.checkEffectiveDidData(didList);
//
//            // 循环筛选有效
//            DidCollectionAccountModel didCollectionAccountModel = ComponentUtil.orderService.screenCollectionAccount(didList, requestModel.money, requestModel.payType);
//            // check校验
//            HodgepodgeMethod.checkDidCollectionAccountByAddOrder(didCollectionAccountModel);
//
//            // 组装派发订单的数据
//            OrderModel orderModel = HodgepodgeMethod.assembleOrderByAdd(did, sgid, requestModel.money, requestModel.notifyUrl, requestModel.outTradeNo, didCollectionAccountModel);
//            ComponentUtil.orderService.add(orderModel);
//
//            // #从余额中扣除对应的订单金额，存放到冻结金额中
//
//            // 组装返回客户端的数据
//            long stime = System.currentTimeMillis();
//            String sign = SignUtil.getSgin(stime, secretKeySign); // stime+秘钥=sign
//            String strData = HodgepodgeMethod.assembleOrderAddDataResult(stime, token, orderModel);
//            // 数据加密
//            String encryptionData = StringUtil.mergeCodeBase64(strData);
//            ResponseEncryptionJson resultDataModel = new ResponseEncryptionJson();
//            resultDataModel.jsonData = encryptionData;
//            // 返回数据给客户端
//            return JsonResult.successResult(resultDataModel, cgid, sgid);
//        }catch (Exception e){
//            Map<String,String> map = ExceptionMethod.getException(e, ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_TWO);
//            log.error(String.format("this OrderController.add() is error , the cgid=%s and sgid=%s and all data=%s!", cgid, sgid, data));
//            e.printStackTrace();
//            return JsonResult.failedResult(map.get("message"), map.get("code"), cgid, sgid);
//        }
//    }



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
     * 8.判断此账号的小微账号是否上线。
     * 9.派单进行中：需要从余额里面剔除相对应的订单金额，把剔除到的存放到冻结金额里面。
     * </p>
     * @param request
     * @param response
     * @return com.gd.chain.common.utils.JsonResult<java.lang.Object>
     * @author yoko
     * @date 2019/11/25 22:58
     * local:http://localhost:8086/fine/order/qrCode
     * 请求的属性类:RequestOrder
     * 必填字段:{"money":"1111","payType":1,"outTradeNo":"outTradeNo1","notifyUrl":"notify_url","returnUrl":"http://www.baidu.com","agtVer":1,"clientVer":1,"clientType":1,"ctime":201911071802959,"cctime":201911071802959,"sign":"abcdefg","token":"111111"}
     * 加密字段:{"jsonData":"eyJtb25leSI6IjExMTEiLCJwYXlUeXBlIjoxLCJub3RpZnlVcmwiOiJub3RpZnlfdXJsIiwiYWd0VmVyIjoxLCJjbGllbnRWZXIiOjEsImNsaWVudFR5cGUiOjEsImN0aW1lIjoyMDE5MTEwNzE4MDI5NTksImNjdGltZSI6MjAxOTExMDcxODAyOTU5LCJzaWduIjoiYWJjZGVmZyIsInRva2VuIjoiMTExMTExIn0="}
     * 客户端加密字段:ctime+cctime+秘钥=sign
     * 服务端加密字段:stime+秘钥=sign
     * result={
     *     "resultCode": "0",
     *     "message": "success",
     *     "data": {
     *         "jsonData": "eyJvcmRlciI6eyJpbnZhbGlkVGltZSI6IjIwMjAtMDYtMDIgMTY6MTk6MjkiLCJvcmRlck1vbmV5IjoiMTExMSIsIm9yZGVyTm8iOiIyMDIwMDYwMjE2MDYzODAwMDAwMDEiLCJxckNvZGUiOiJkZF9xcl9jb2RlMyJ9LCJzaWduIjoiIiwic3RpbWUiOjE1OTEwODUzNjkzMTB9"
     *     },
     *     "sgid": "202006021606380000001",
     *     "cgid": ""
     * }
     */
    @RequestMapping(value = "/qrCode", method = {RequestMethod.POST})
    public JsonResult<Object> qrCode(HttpServletRequest request, HttpServletResponse response, @RequestBody RequestEncryptionJson requestData) throws Exception{
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

            if (requestModel.money.indexOf(".") <= -1){
                requestModel.money = requestModel.money + ".00";
            }

            // 获取可派单的用户集合
            DidModel didQuery = HodgepodgeMethod.assembleEffectiveDid(requestModel);
            List<DidModel> didList = ComponentUtil.didService.getEffectiveDidList(didQuery);
            // check校验是否有有效的用户
            HodgepodgeMethod.checkEffectiveDidData(didList);

            // 循环筛选有效
            DidCollectionAccountModel didCollectionAccountModel = ComponentUtil.orderService.screenCollectionAccount(didList, requestModel.money, requestModel.payType);
            // check校验
            HodgepodgeMethod.checkDidCollectionAccountByAddOrder(didCollectionAccountModel);
            // 组装派发订单的数据
            OrderModel orderModel = HodgepodgeMethod.assembleOrderByAdd(did, sgid, requestModel.money, requestModel.notifyUrl, requestModel.outTradeNo, didCollectionAccountModel);
            ComponentUtil.orderService.add(orderModel);


            // 组装返回客户端的数据
            long stime = System.currentTimeMillis();
            String sign = SignUtil.getSgin(stime, secretKeySign); // stime+秘钥=sign
            String strData = HodgepodgeMethod.assembleOrderQrCodeDataResult(stime, token, orderModel, requestModel.returnUrl, ComponentUtil.loadConstant.qrCodeUrl);
            // 数据加密
            String encryptionData = StringUtil.mergeCodeBase64(strData);
            ResponseEncryptionJson resultDataModel = new ResponseEncryptionJson();
            resultDataModel.jsonData = encryptionData;
            // 返回数据给客户端
            return JsonResult.successResult(resultDataModel, cgid, sgid);
        }catch (Exception e){
            Map<String,String> map = ExceptionMethod.getException(e, ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_TWO);
            log.error(String.format("this OrderController.add() is error , the cgid=%s and sgid=%s and all data=%s!", cgid, sgid, data));
            if (!StringUtils.isBlank(map.get("dbCode"))){
                log.error(String.format("this OrderController.add() is error codeInfo, the dbCode=%s and dbMessage=%s !", map.get("dbCode"), map.get("dbMessage")));
            }
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
//            if (!StringUtils.isBlank(requestModel.token)){
//                if (requestModel.token.equals("111111")){
//                    ComponentUtil.redisService.set(requestModel.token, "1");
//                }
//            }
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
            if (!StringUtils.isBlank(map.get("dbCode"))){
                log.error(String.format("this OrderController.getDataList() is error codeInfo, the dbCode=%s and dbMessage=%s !", map.get("dbCode"), map.get("dbMessage")));
            }
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
//            if (!StringUtils.isBlank(requestModel.token)){
//                if (requestModel.token.equals("111111")){
//                    ComponentUtil.redisService.set(requestModel.token, "1");
//                }
//            }
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
            if (!StringUtils.isBlank(map.get("dbCode"))){
                log.error(String.format("this OrderController.getData() is error codeInfo, the dbCode=%s and dbMessage=%s !", map.get("dbCode"), map.get("dbMessage")));
            }
            e.printStackTrace();
            return JsonResult.failedResult(map.get("message"), map.get("code"), cgid, sgid);
        }
    }



    /**
     * @Description: 获取派单数据-详情-返回码的接口
     * @param request
     * @param response
     * @return com.gd.chain.common.utils.JsonResult<java.lang.Object>
     * @author yoko
     * @date 2019/11/25 22:58
     * local:http://localhost:8086/fine/order/getQrCode
     * 请求的属性类:RequestOrder
     * 必填字段:{"orderNo":"order_no_3"}
     * 加密字段:{"jsonData":"eyJvcmRlck5vIjoib3JkZXJfbm9fMyIsImFndFZlciI6MSwiY2xpZW50VmVyIjoxLCJjbGllbnRUeXBlIjoxLCJjdGltZSI6MjAxOTExMDcxODAyOTU5LCJjY3RpbWUiOjIwMTkxMTA3MTgwMjk1OSwic2lnbiI6ImFiY2RlZmciLCJ0b2tlbiI6IjExMTExMSJ9"}
     * 客户端加密字段:id+ctime+cctime+秘钥=sign
     * 服务端加密字段:stime+秘钥=sign
     * result={
     *     "resultCode": "0",
     *     "message": "success",
     *     "data": {
     *         "jsonData": "eyJvcmRlciI6eyJpbnZhbGlkVGltZSI6IjIwMjEtMDUtMjkgMTQ6MjI6MDciLCJvcmRlck1vbmV5IjoiMTAuMDEiLCJvcmRlck5vIjoib3JkZXJfbm9fMSIsInFyQ29kZSI6InFyX2NvZGVfMSJ9LCJzaWduIjoiOThiYTcxY2NhZjhhY2NiZDYyYWU3ZDUwMjRlZDY3MDEiLCJzdGltZSI6MTU5MTYxNzM2MjQwNH0="
     *     },
     *     "sgid": "202006081956000000001",
     *     "cgid": ""
     * }
     */
    @RequestMapping(value = "/getQrCode", method = {RequestMethod.POST})
    public JsonResult<Object> getQrCode(HttpServletRequest request, HttpServletResponse response, @RequestBody RequestEncryptionJson requestData) throws Exception{
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

            // check校验请求的数据
            HodgepodgeMethod.checkOrderByQrCodeData(requestModel);

            // 收款账号详情数据
            OrderModel orderQuery = HodgepodgeMethod.assembleOrderOrderNo(requestModel.orderNo, 0);
            OrderModel orderModelData = ComponentUtil.orderService.getOrderQrCodeByOrderNo(orderQuery);
            // 组装返回客户端的数据
            long stime = System.currentTimeMillis();
            String sign = SignUtil.getSgin(stime, secretKeySign); // stime+秘钥=sign
            String strData = HodgepodgeMethod.assembleOrderAddDataResult(stime, sign, orderModelData);
            // 数据加密
            String encryptionData = StringUtil.mergeCodeBase64(strData);
            ResponseEncryptionJson resultDataModel = new ResponseEncryptionJson();
            resultDataModel.jsonData = encryptionData;
            // 返回数据给客户端
            return JsonResult.successResult(resultDataModel, cgid, sgid);
        }catch (Exception e){
            Map<String,String> map = ExceptionMethod.getException(e, ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_TWO);
            // 添加异常
            log.error(String.format("this OrderController.getQrCode() is error , the cgid=%s and sgid=%s and all data=%s!", cgid, sgid, data));
            if (!StringUtils.isBlank(map.get("dbCode"))){
                log.error(String.format("this OrderController.getQrCode() is error codeInfo, the dbCode=%s and dbMessage=%s !", map.get("dbCode"), map.get("dbMessage")));
            }
            e.printStackTrace();
            return JsonResult.failedResult(map.get("message"), map.get("code"), cgid, sgid);
        }
    }


    /**
     * @Description: 获取派单的订单状态
     * @param request
     * @param response
     * @return com.gd.chain.common.utils.JsonResult<java.lang.Object>
     * @author yoko
     * @date 2019/11/25 22:58
     * local:http://localhost:8086/fine/order/getOrderStatus
     * 请求的属性类:RequestOrder
     * 必填字段:{"orderNo":"order_no_3"}
     * 加密字段:{"jsonData":"eyJvcmRlck5vIjoiMjAyMDA3MDcxMDM2MDUwMDAwMDAxIn0="}
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
    @RequestMapping(value = "/getOrderStatus", method = {RequestMethod.POST})
    public JsonResult<Object> getOrderStatus(HttpServletRequest request, HttpServletResponse response, @RequestBody RequestEncryptionJson requestData) throws Exception{
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

            // check校验请求的数据
            HodgepodgeMethod.checkOrderByQrCodeData(requestModel);

            // 收款账号详情数据
            OrderModel orderQuery = HodgepodgeMethod.assembleOrderOrderNo(requestModel.orderNo, 0);
            int orderStatus = ComponentUtil.orderService.getOrderStatus(orderQuery);
            if (orderStatus != 0){
                orderStatus = 1;
            }
            // 组装返回客户端的数据
            long stime = System.currentTimeMillis();
            String sign = SignUtil.getSgin(stime, secretKeySign); // stime+秘钥=sign
            String strData = HodgepodgeMethod.assembleOrderStatusResult(stime, sign, orderStatus);
            // 数据加密
            String encryptionData = StringUtil.mergeCodeBase64(strData);
            ResponseEncryptionJson resultDataModel = new ResponseEncryptionJson();
            resultDataModel.jsonData = encryptionData;

            // 返回数据给客户端
            return JsonResult.successResult(resultDataModel, cgid, sgid);
        }catch (Exception e){
            Map<String,String> map = ExceptionMethod.getException(e, ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_TWO);
            // 添加异常
            log.error(String.format("this OrderController.getOrderStatus() is error , the cgid=%s and sgid=%s and all data=%s!", cgid, sgid, data));
            if (!StringUtils.isBlank(map.get("dbCode"))){
                log.error(String.format("this OrderController.getOrderStatus() is error codeInfo, the dbCode=%s and dbMessage=%s !", map.get("dbCode"), map.get("dbMessage")));
            }
            e.printStackTrace();
            return JsonResult.failedResult(map.get("message"), map.get("code"), cgid, sgid);
        }
    }



//    /**
//     * @Description: 获取派单的订单状态
//     * @param request
//     * @param response
//     * @return com.gd.chain.common.utils.JsonResult<java.lang.Object>
//     * @author yoko
//     * @date 2019/11/25 22:58
//     * local:http://localhost:8086/fine/order/getOrderStatus
//     * 请求的属性类:RequestOrder
//     * 必填字段:{"orderNo":"order_no_3"}
//     * 加密字段:{"jsonData":"eyJvcmRlck5vIjoib3JkZXJfbm9fMyIsImFndFZlciI6MSwiY2xpZW50VmVyIjoxLCJjbGllbnRUeXBlIjoxLCJjdGltZSI6MjAxOTExMDcxODAyOTU5LCJjY3RpbWUiOjIwMTkxMTA3MTgwMjk1OSwic2lnbiI6ImFiY2RlZmciLCJ0b2tlbiI6IjExMTExMSJ9"}
//     * 客户端加密字段:id+ctime+cctime+秘钥=sign
//     * 服务端加密字段:stime+秘钥=sign
//     * result={
//     *     "resultCode": "0",
//     *     "message": "success",
//     *     "data": {
//     *         "jsonData": "eyJkYXRhTW9kZWwiOnsiYWNOYW1lIjoiYWNOYW1lMyIsImNvbGxlY3Rpb25UeXBlIjoxLCJjcmVhdGVUaW1lIjoiMjAyMC0wNS0yOSAxNDoyMjowNyIsIm9yZGVyTW9uZXkiOiIzMC4wMyIsIm9yZGVyTm8iOiJvcmRlcl9ub18zIiwib3JkZXJTdGF0dXMiOjR9LCJzaWduIjoiZTdiMzNlZTJiNWU5ZTVhNGVkOWQ4ZDU4NmEzZGM5YTUiLCJzdGltZSI6MTU5MDc0NTc5OTQzMX0="
//     *     },
//     *     "sgid": "202005291749570000001",
//     *     "cgid": ""
//     * }
//     */
//    @RequestMapping(value = "/getOrderStatus", method = {RequestMethod.POST})
//    public void getOrderStatus(HttpServletRequest request, HttpServletResponse response, @RequestBody RequestEncryptionJson requestData) throws Exception{
//        String sgid = ComponentUtil.redisIdService.getNewId();
//        String cgid = "";
//        String token;
//        String ip = StringUtil.getIpAddress(request);
//        String data = "";
//        long did = 0;
//
//        RequestOrder requestModel = new RequestOrder();
//        try{
//            // 解密
//            data = StringUtil.decoderBase64(requestData.jsonData);
//            requestModel  = JSON.parseObject(data, RequestOrder.class);
//
//            // check校验请求的数据
//            HodgepodgeMethod.checkOrderByQrCodeData(requestModel);
//
//            // 收款账号详情数据
//            OrderModel orderQuery = HodgepodgeMethod.assembleOrderOrderNo(requestModel.orderNo, 0);
//            int orderStatus = ComponentUtil.orderService.getOrderStatus(orderQuery);
//            if (orderStatus != 0){
//                orderStatus = 1;
//            }
//            // 组装返回客户端的数据
//            long stime = System.currentTimeMillis();
//            String sign = SignUtil.getSgin(stime, secretKeySign); // stime+秘钥=sign
//            String strData = HodgepodgeMethod.assembleOrderStatusResult(stime, sign, orderStatus);
//            // 数据加密
//            String encryptionData = StringUtil.mergeCodeBase64(strData);
//            ResponseEncryptionJson resultDataModel = new ResponseEncryptionJson();
//            resultDataModel.jsonData = encryptionData;
//
////            response.setHeader("Accept" ,"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3");
////            response.setHeader("Accept-Encoding" ,"gzip, deflate, br");
////            response.setHeader("Accept-Language" ,"zh-CN,zh;q=0.9");
////            response.setHeader("Cache-Control" ,"keep-alive");
//////            response.setHeader("Cookie" ,"_qda_uuid=39269c72-b5b2-6618-3747-ef488c5495f8; _csrfToken=xf01V17JFygSY3dTqS1KkCxLY4PclRg3jKAMQqk7; newstatisticUUID=1572256635_620240973; pgv_pvi=6316058624; qdrs=0%7C3%7C0%7C0%7C1; qdgd=1; showSectionCommentGuide=1; rcr=1049807%2C2226569%2C1003541158; lrbc=1049807%7C21108391%7C0%2C2226569%7C36854452%7C0%2C1003541158%7C309402995%7C0; e1=%7B%22pid%22%3A%22qd_p_qidian%22%2C%22eid%22%3A%22%22%7D; e2=%7B%22pid%22%3A%22qd_p_qidian%22%2C%22eid%22%3A%22%22%7D");
////            response.setHeader("Host", "https://www.jd.com");
//////            response.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/77.0.3865.120 Safari/537.36");
////            response.setHeader("Referer", "https://www.jd.com");
//            // 查询策略里面的来源地址集合
//            StrategyModel strategyQuery = HodgepodgeMethod.assembleStrategyQuery(ServerConstant.StrategyEnum.REFERER_LIST.getStgType());
//            StrategyModel strategyModel = ComponentUtil.strategyService.getStrategyModel(strategyQuery, ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_ZERO);
//            HodgepodgeMethod.checkStrategyByRefererList(strategyModel);
//
//            // 解析金额列表的值
//            List<StrategyData> strategyDataList = JSON.parseArray(strategyModel.getStgBigValue(), StrategyData.class);
//            int random = new Random().nextInt(strategyDataList.size());
//
//            StrategyData dataModel = strategyDataList.get(random);
//            response.setHeader("Accept" ,request.getHeader("Accept"));
//            response.setHeader("Accept-Encoding" ,request.getHeader("Accept-Encoding"));
//            response.setHeader("Accept-Language" ,request.getHeader("Accept-Language"));
//            response.setHeader("Cache-Control" ,request.getHeader("Cache-Control"));
////            String strCookie = "";
////            if (request.getCookies() != null){
////                Cookie[] cookies = request.getCookies();
////                for (Cookie cookie : cookies){
////                    strCookie += cookie;
////                }
////            }
//            if (!StringUtils.isBlank(requestModel.ck)){
//                log.info("------------------------------------------------strCookie:" + requestModel.ck);
//                response.setHeader("Cookie" , requestModel.ck);
//            }
//            response.setHeader("Host", dataModel.getStgValue());
//            response.setHeader("User-Agent", request.getHeader("User-Agent"));
//            response.setHeader("Referer", dataModel.getStgValue());
//
//            PrintWriter out = response.getWriter();
//            out.print(JsonResult.successResult(resultDataModel, cgid, sgid));
//            out.flush();
//            out.close();
//            // 返回数据给客户端
//        }catch (Exception e){
//            Map<String,String> map = ExceptionMethod.getException(e, ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_TWO);
//            // 添加异常
//            log.error(String.format("this OrderController.getOrderStatus() is error , the cgid=%s and sgid=%s and all data=%s!", cgid, sgid, data));
//            if (!StringUtils.isBlank(map.get("dbCode"))){
//                log.error(String.format("this OrderController.getOrderStatus() is error codeInfo, the dbCode=%s and dbMessage=%s !", map.get("dbCode"), map.get("dbMessage")));
//            }
//            e.printStackTrace();
//            return;
//        }
//    }





    /**
     * @Description: 获取初始化派单数据-详情
     * <p>
     *     用户在开始抢单之后，程序派单给用户，客户端进行数据监听查询
     * </p>
     * @param request
     * @param response
     * @return com.gd.chain.common.utils.JsonResult<java.lang.Object>
     * @author yoko
     * @date 2019/11/25 22:58
     * local:http://localhost:8086/fine/order/getInitOrderData
     * 请求的属性类:RequestOrder
     * 必填字段:{"agtVer":1,"clientVer":1,"clientType":1,"ctime":201911071802959,"cctime":201911071802959,"sign":"abcdefg","token":"111111"}
     * 加密字段:{"jsonData":"eyJhZ3RWZXIiOjEsImNsaWVudFZlciI6MSwiY2xpZW50VHlwZSI6MSwiY3RpbWUiOjIwMTkxMTA3MTgwMjk1OSwiY2N0aW1lIjoyMDE5MTEwNzE4MDI5NTksInNpZ24iOiJhYmNkZWZnIiwidG9rZW4iOiIxMTExMTEifQ=="}
     * 客户端加密字段:id+ctime+cctime+秘钥=sign
     * 服务端加密字段:stime+秘钥=sign
     * result={
     *     "resultCode": "0",
     *     "message": "success",
     *     "data": {
     *         "jsonData": "eyJkYXRhTW9kZWwiOnsiY29sbGVjdGlvblR5cGUiOjIsImNyZWF0ZVRpbWUiOiIyMDIwLTA3LTAxIDE2OjIxOjQ3IiwiY3VyZGF5IjoyMDIwMDcwMSwiY3VyaG91ciI6MCwiY3VybWludXRlIjowLCJpbnZhbGlkU2Vjb25kIjoiMzE0IiwiaW52YWxpZFRpbWUiOiIyMDIwLTA3LTAxIDE3OjIwOjIxIiwib3JkZXJNb25leSI6IjEwMDAiLCJvcmRlck5vIjoiMjAyMDA3MDFfb3JkZXJfbm9fMSIsIm9yZGVyU3RhdHVzIjoxLCJwcm9maXQiOiIxMC4wMCIsInpmYkFjTnVtIjoiIn0sInNpZ24iOiJjMjBkNjY5Y2FkMWVmNDVkYzM3MjlkN2M2OGM5YTgxNyIsInN0aW1lIjoxNTkzNTk0OTA4NTg5fQ=="
     *     },
     *     "sgid": "202007011715080000001",
     *     "cgid": ""
     * }
     */
    @RequestMapping(value = "/getInitOrderData", method = {RequestMethod.POST})
    public JsonResult<Object> getInitOrderData(HttpServletRequest request, HttpServletResponse response, @RequestBody RequestEncryptionJson requestData) throws Exception{
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
//            if (!StringUtils.isBlank(requestModel.token)){
//                if (requestModel.token.equals("111111")){
//                    ComponentUtil.redisService.set(requestModel.token, "1");
//                }
//            }
            // check校验请求的数据
            did = HodgepodgeMethod.checkGetOrderData(requestModel);

            // 策略数据：查询用户提交订单状态的最后读秒时间
            StrategyModel strategyQuery = HodgepodgeMethod.assembleStrategyQuery(ServerConstant.StrategyEnum.LAST_TIME.getStgType());
            StrategyModel strategyModel = ComponentUtil.strategyService.getStrategyModel(strategyQuery, ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_ZERO);
            HodgepodgeMethod.checkStrategyByLastTime(strategyModel);

            // 获取派单信息（初始化的派单信息）
            OrderModel orderQuery = HodgepodgeMethod.assembleOrderByDidQuery(did, 1, 1, "0.01");
            OrderModel orderModelData = (OrderModel) ComponentUtil.orderService.getInitOrder(orderQuery);
            // 组装返回客户端的数据
            long stime = System.currentTimeMillis();
            String sign = SignUtil.getSgin(stime, secretKeySign); // stime+秘钥=sign
            String strData = HodgepodgeMethod.assembleInitOrderDataResult(stime, sign, orderModelData, strategyModel.getStgNumValue());
            // 数据加密
            String encryptionData = StringUtil.mergeCodeBase64(strData);
            ResponseEncryptionJson resultDataModel = new ResponseEncryptionJson();
            resultDataModel.jsonData = encryptionData;
            // 返回数据给客户端
            return JsonResult.successResult(resultDataModel, cgid, sgid);
        }catch (Exception e){
            Map<String,String> map = ExceptionMethod.getException(e, ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_TWO);
            // 添加异常
            log.error(String.format("this OrderController.getInitOrderData() is error , the cgid=%s and sgid=%s and all data=%s!", cgid, sgid, data));
            if (!StringUtils.isBlank(map.get("dbCode"))){
                log.error(String.format("this OrderController.getInitOrderData() is error codeInfo, the dbCode=%s and dbMessage=%s !", map.get("dbCode"), map.get("dbMessage")));
            }
            e.printStackTrace();
            return JsonResult.failedResult(map.get("message"), map.get("code"), cgid, sgid);
        }
    }



    /**
     * @Description: 用户订单的用户操作派单状态
     * @param request
     * @param response
     * @return com.gd.chain.common.utils.JsonResult<java.lang.Object>
     * @author yoko
     * @date 2019/11/25 22:58
     * local:http://localhost:8086/fine/order/updateOrderStatus
     * 请求的属性类:RequestOrder
     * 必填字段:{"orderNo":"20200701_order_no_1","status":2,"agtVer":1,"clientVer":1,"clientType":1,"ctime":201911071802959,"cctime":201911071802959,"sign":"abcdefg","token":"111111"}
     * 加密字段:{"jsonData":"eyJvcmRlck5vIjoiMjAyMDA3MDFfb3JkZXJfbm9fMSIsInN0YXR1cyI6MiwiYWd0VmVyIjoxLCJjbGllbnRWZXIiOjEsImNsaWVudFR5cGUiOjEsImN0aW1lIjoyMDE5MTEwNzE4MDI5NTksImNjdGltZSI6MjAxOTExMDcxODAyOTU5LCJzaWduIjoiYWJjZGVmZyIsInRva2VuIjoiMTExMTExIn0="}
     * 客户端加密字段:ctime+cctime+秘钥=sign
     * 服务端加密字段:stime+秘钥=sign
     * result={
     *     "resultCode": "0",
     *     "message": "success",
     *     "data": {
     *         "jsonData": "eyJzaWduIjoiNmU3NTJiOGFjNDViNjk0Yjc0NGY4OWQ5N2ZiOTRmNTUiLCJzdGltZSI6MTU5MzU5NTM3MjMxNX0="
     *     },
     *     "sgid": "202007011722520000001",
     *     "cgid": ""
     * }
     */
    @RequestMapping(value = "/updateOrderStatus", method = {RequestMethod.POST})
    public JsonResult<Object> updateOrderStatus(HttpServletRequest request, HttpServletResponse response, @RequestBody RequestEncryptionJson requestData) throws Exception{
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

            //#临时数据
//            if (!StringUtils.isBlank(requestModel.token)){
//                if (requestModel.token.equals("111111")){
//                    ComponentUtil.redisService.set(requestModel.token, "1");
//                }
//            }

            // check校验数据
            did = HodgepodgeMethod.checkUpdateOrderStatus(requestModel);


            if (requestModel.status == 4){
                // 根据订单号查询订单基本信息
                OrderModel orderQuery = HodgepodgeMethod.assembleOrderByRewardQuery(did, requestModel.orderNo);
                OrderModel orderModel = ComponentUtil.orderService.getOrderByReward(orderQuery);
                if (orderModel != null && orderModel.getId() > 0){
                    boolean flag = false;// false 表示没有奖励，true表示有奖励
                    // 查询客户端监听的收款信息-5分钟
                    ClientCollectionDataModel clientCollectionDataQuery = HodgepodgeMethod.assembleClientCollectionDataQuery(did, orderModel.getUserId());
                    ClientCollectionDataModel clientCollectionDataModel = ComponentUtil.clientCollectionDataService.getClientCollectionDataByCreateTime(clientCollectionDataQuery);
                    if (clientCollectionDataModel == null || clientCollectionDataModel.getId() < 0){
                        // 可以给与消耗带来的奖励:规定因为没有查询到任何信息，直接给与奖励
                        flag = true;
                    }else{
                        // 查询到了监听信息，需要对监听信息的创建时间与当前系统时间做比较，是否有超过策略里面规定的时间内进行提交上报数据的

                        // 策略数据：查询用户提交订单状态的最后读秒时间
                        StrategyModel strategyQuery = HodgepodgeMethod.assembleStrategyQuery(ServerConstant.StrategyEnum.LAST_TIME.getStgType());
                        StrategyModel strategyModel = ComponentUtil.strategyService.getStrategyModel(strategyQuery, ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_ZERO);
                        HodgepodgeMethod.checkStrategyByLastTime(strategyModel);
                        // 计算两时间相差多少秒
                        int differSecond = DateUtil.differSecond(DateUtil.getNowPlusTime(), clientCollectionDataModel.getCreateTime());
                        if (differSecond <= strategyModel.getStgNumValue()){
                            // 在规定时间内上报消息：给与消耗奖励
                            flag = true;
                        }
                    }

                    if (flag){
                        // 校验此订单是否之前给过奖励
                        DidRewardModel didRewardQuery = HodgepodgeMethod.assembleDidRewardQueryByOrderNo(orderModel.getOrderNo(), 6);
                        DidRewardModel didRewardModel = ComponentUtil.didRewardService.getDidRewardByOrderNoAndType(didRewardQuery);
                        if (didRewardModel == null || didRewardModel.getId() <= 0 ){
                            // 正式给与奖励
                            DidRewardModel didRewardAdd = HodgepodgeMethod.assembleDidRewardAdd(orderModel, 6);
                            ComponentUtil.didRewardService.add(didRewardAdd);
                        }
                    }

                }

            }

            // 正式修改派单的用户的操作状态
            OrderModel orderModel = HodgepodgeMethod.assembleUpdateOrderStatusData(did, requestModel);
            ComponentUtil.orderService.updateDidStatus(orderModel);

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
            log.error(String.format("this OrderController.updateOrderStatus() is error , the cgid=%s and sgid=%s and all data=%s!", cgid, sgid, data));
            if (!StringUtils.isBlank(map.get("dbCode"))){
                log.error(String.format("this OrderController.updateOrderStatus() is error codeInfo, the dbCode=%s and dbMessage=%s !", map.get("dbCode"), map.get("dbMessage")));
            }
            e.printStackTrace();
            return JsonResult.failedResult(map.get("message"), map.get("code"), cgid, sgid);
        }
    }





    /**
     * @Description: 派发订单-支付宝接口
     * <p>
     * 1.用户余额是否满足此次派单的金额。
     * 2.用户抢单开始的状态是在上线状态。
     * 3.用户的支付宝收款账号是否正常。
     * 4.已经派单过的用户并且名下有订单没有过失效期的用户不派单。
     * 5.派单进行中：需要从余额里面剔除相对应的订单金额，把剔除到的存放到表《用户扣减余额流水表》里面。
     * </p>
     * @param request
     * @param response
     * @return com.gd.chain.common.utils.JsonResult<java.lang.Object>
     * @author yoko
     * @date 2019/11/25 22:58
     * local:http://localhost:8086/fine/order/zfbQrCode
     * 请求的属性类:RequestOrder
     * 必填字段:{"money":"1111","payType":2,"outTradeNo":"outTradeNo1","notifyUrl":"notify_url","returnUrl":"http://www.baidu.com","agtVer":1,"clientVer":1,"clientType":1,"ctime":201911071802959,"cctime":201911071802959,"sign":"abcdefg","token":"111111"}
     * 加密字段:{"jsonData":"eyJtb25leSI6IjExMTEiLCJwYXlUeXBlIjoyLCJvdXRUcmFkZU5vIjoib3V0VHJhZGVObzEiLCJub3RpZnlVcmwiOiJub3RpZnlfdXJsIiwicmV0dXJuVXJsIjoiaHR0cDovL3d3dy5iYWlkdS5jb20iLCJhZ3RWZXIiOjEsImNsaWVudFZlciI6MSwiY2xpZW50VHlwZSI6MSwiY3RpbWUiOjIwMTkxMTA3MTgwMjk1OSwiY2N0aW1lIjoyMDE5MTEwNzE4MDI5NTksInNpZ24iOiJhYmNkZWZnIiwidG9rZW4iOiIxMTExMTEifQ=="}
     * 客户端加密字段:ctime+cctime+秘钥=sign
     * 服务端加密字段:stime+秘钥=sign
     * result={
     *     "resultCode": "0",
     *     "message": "success",
     *     "data": {
     *         "jsonData": "eyJvcmRlciI6eyJpbnZhbGlkU2Vjb25kIjoiNDk4IiwiaW52YWxpZFRpbWUiOiIyMDIwLTA3LTAyIDE2OjAwOjIzIiwib3JkZXJNb25leSI6IjExMTEuMDAiLCJvcmRlck5vIjoiMjAyMDA3MDIxNTUwMTAwMDAwMDAxIiwicXJDb2RlVXJsIjoiaHR0cCUzQSUyRiUyRnd3dy55YnpmbS5jb20lM0E4MDAyJTJGcGF5JTJGaW5kZXguaHRtbCUzRm9yZGVyTm8lM0QyMDIwMDcwMjE1NTAxMDAwMDAwMDElMjZyZXR1cm5VcmwlM0RodHRwJTNBJTJGJTJGd3d3LmJhaWR1LmNvbSJ9LCJzaWduIjoiIiwic3RpbWUiOjE1OTM2NzYzMjQ4NDN9"
     *     },
     *     "sgid": "202007021550100000001",
     *     "cgid": ""
     * }
     */
    @RequestMapping(value = "/zfbQrCode", method = {RequestMethod.POST})
    public JsonResult<Object> zfbQrCode(HttpServletRequest request, HttpServletResponse response, @RequestBody RequestEncryptionJson requestData) throws Exception{
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

            if (requestModel.money.indexOf(".") <= -1){
                requestModel.money = requestModel.money + ".00";
            }
            log.info("");

            // 获取可派单的用户集合
            DidModel didQuery = HodgepodgeMethod.assembleEffectiveDid(requestModel);
            List<DidModel> didList = ComponentUtil.didService.getEffectiveDidByZfbList(didQuery);
            // check校验是否有有效的用户
            HodgepodgeMethod.checkEffectiveDidData(didList);

            // 循环筛选有效
            DidModel didModel = ComponentUtil.orderService.screenCollectionAccountByZfb(didList, requestModel.money);
            // check校验
            HodgepodgeMethod.checkDidAndByAddCollectionAccountOrder(didModel);

            did = didModel.getId();

            // 查询策略里面的消耗金额范围内的奖励规则列表
            StrategyModel strategyQuery = HodgepodgeMethod.assembleStrategyQuery(ServerConstant.StrategyEnum.CONSUME_MONEY_LIST.getStgType());
            StrategyModel strategyModel = ComponentUtil.strategyService.getStrategyModel(strategyQuery, ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_ZERO);
            // 解析奖励规则的值
            List<StrategyData> consumeMoneyList = JSON.parseArray(strategyModel.getStgBigValue(), StrategyData.class);

            // 查询策略里面的派单的超时时间
            StrategyModel strategyInvalidTimeQuery = HodgepodgeMethod.assembleStrategyQuery(ServerConstant.StrategyEnum.DELIVERY_ORDER_INVALID_TIME.getStgType());
            StrategyModel strategyInvalidTimeModel = ComponentUtil.strategyService.getStrategyModel(strategyInvalidTimeQuery, ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_ZERO);

            // 查询策略里面的用户无操作状态锁定金额时间
            StrategyModel strategyLockTimeQuery = HodgepodgeMethod.assembleStrategyQuery(ServerConstant.StrategyEnum.LOCK_MONEY_TIME.getStgType());
            StrategyModel strategyLockTimeModel = ComponentUtil.strategyService.getStrategyModel(strategyLockTimeQuery, ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_ZERO);

            // 组装派发订单的数据
            OrderModel orderModel = HodgepodgeMethod.assembleOrderByZfbAdd(did, sgid, requestModel.money, requestModel.notifyUrl, requestModel.outTradeNo, didModel, requestModel.payType, consumeMoneyList, strategyInvalidTimeModel.getStgNumValue());

            // 组装用户扣除余额流水的数据
            DidBalanceDeductModel didBalanceDeductModel = HodgepodgeMethod.assembleDidBalanceDeductAdd(did, sgid, requestModel.money, strategyLockTimeModel.getStgNumValue());

            // 组装扣除用户余额
            DidModel updateBalance = HodgepodgeMethod.assembleUpdateDidBalance(did, requestModel.money);

            // 正式处理派单的逻辑
            ComponentUtil.orderService.handleOrder(orderModel, didBalanceDeductModel, updateBalance);
            // 组装返回客户端的数据
            long stime = System.currentTimeMillis();
            String sign = SignUtil.getSgin(stime, secretKeySign); // stime+秘钥=sign
            String strData = HodgepodgeMethod.assembleOrderZfbQrCodeDataResult(stime, token, orderModel, requestModel.returnUrl, ComponentUtil.loadConstant.zfbQrCodeUrl);
            // 数据加密
            String encryptionData = StringUtil.mergeCodeBase64(strData);
            ResponseEncryptionJson resultDataModel = new ResponseEncryptionJson();
            resultDataModel.jsonData = encryptionData;
            // 返回数据给客户端
            return JsonResult.successResult(resultDataModel, cgid, sgid);
        }catch (Exception e){
            Map<String,String> map = ExceptionMethod.getException(e, ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_TWO);
            log.error(String.format("this OrderController.zfbQrCode() is error , the cgid=%s and sgid=%s and all data=%s!", cgid, sgid, data));
            if (!StringUtils.isBlank(map.get("dbCode"))){
                log.error(String.format("this OrderController.zfbQrCode() is error codeInfo, the dbCode=%s and dbMessage=%s !", map.get("dbCode"), map.get("dbMessage")));
            }
            e.printStackTrace();
            return JsonResult.failedResult(map.get("message"), map.get("code"), cgid, sgid);
        }
    }



    /**
     * @Description: 获取派单的订单信息-支付宝
     * @param request
     * @param response
     * @return com.gd.chain.common.utils.JsonResult<java.lang.Object>
     * @author yoko
     * @date 2019/11/25 22:58
     * local:http://localhost:8086/fine/order/getZfbQrCode
     * 请求的属性类:RequestOrder
     * 必填字段:{"orderNo":"202007021853550000001"}
     * 加密字段:{"jsonData":"eyJvcmRlck5vIjoiMjAyMDA3MDIxODUzNTUwMDAwMDAxIn0="}
     * 客户端加密字段:id+ctime+cctime+秘钥=sign
     * 服务端加密字段:stime+秘钥=sign
     * result={
     *     "resultCode": "0",
     *     "message": "success",
     *     "data": {
     *         "jsonData": "eyJvcmRlciI6eyJkYXRhVHlwZSI6MiwiaW52YWxpZFNlY29uZCI6Ijc0NzciLCJpbnZhbGlkVGltZSI6IjIwMjAtMDctMDIgMjE6MDM6NTUiLCJrZXkiOiI4RkY3QUM3MkRFMCIsIm9yZGVyTW9uZXkiOiIxMTExLjAwIiwib3JkZXJObyI6IjIwMjAwNzAyMTg1MzU1MDAwMDAwMSIsInVzZXJJZCI6IjIwODg2MDIwNzkyMTg3ODIiLCJ6ZmJBY051bSI6IjEzNjA2NzA2MzQ2In0sInNpZ24iOiJlY2MyOGYzM2RkYWU0MjA5ZDBlMTUzYjdmMWIyNDJjYSIsInN0aW1lIjoxNTkzNjg3NTU4NDAzfQ=="
     *     },
     *     "sgid": "202007021859180000001",
     *     "cgid": ""
     * }
     */
    @RequestMapping(value = "/getZfbQrCode", method = {RequestMethod.POST})
    public JsonResult<Object> getZfbQrCode(HttpServletRequest request, HttpServletResponse response, @RequestBody RequestEncryptionJson requestData) throws Exception{
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

            // check校验请求的数据
            HodgepodgeMethod.checkZfbOrderByQrCodeData(requestModel);
            // 收款账号详情数据
            OrderModel orderQuery = HodgepodgeMethod.assembleOrderOrderNo(requestModel.orderNo, 1);
            OrderModel orderModelData = ComponentUtil.orderService.getOrderQrCodeByOrderNo(orderQuery);
            // 组装返回客户端的数据
            long stime = System.currentTimeMillis();
            String sign = SignUtil.getSgin(stime, secretKeySign); // stime+秘钥=sign
            String strData = HodgepodgeMethod.assembleZfbOrderDataResult(stime, sign, orderModelData);
            // 数据加密
            String encryptionData = StringUtil.mergeCodeBase64(strData);
            ResponseEncryptionJson resultDataModel = new ResponseEncryptionJson();
            resultDataModel.jsonData = encryptionData;
            // 返回数据给客户端
            return JsonResult.successResult(resultDataModel, cgid, sgid);
        }catch (Exception e){
            Map<String,String> map = ExceptionMethod.getException(e, ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_TWO);
            // 添加异常
            log.error(String.format("this OrderController.getZfbQrCode() is error , the cgid=%s and sgid=%s and all data=%s!", cgid, sgid, data));
            if (!StringUtils.isBlank(map.get("dbCode"))){
                log.error(String.format("this OrderController.getZfbQrCode() is error codeInfo, the dbCode=%s and dbMessage=%s !", map.get("dbCode"), map.get("dbMessage")));
            }
            e.printStackTrace();
            return JsonResult.failedResult(map.get("message"), map.get("code"), cgid, sgid);
        }
    }






}
