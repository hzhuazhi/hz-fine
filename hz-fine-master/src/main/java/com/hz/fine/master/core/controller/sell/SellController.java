package com.hz.fine.master.core.controller.sell;

import com.alibaba.fastjson.JSON;
import com.hz.fine.master.core.common.exception.ExceptionMethod;
import com.hz.fine.master.core.common.utils.JsonResult;
import com.hz.fine.master.core.common.utils.SignUtil;
import com.hz.fine.master.core.common.utils.StringUtil;
import com.hz.fine.master.core.common.utils.constant.CacheKey;
import com.hz.fine.master.core.common.utils.constant.CachedKeyUtils;
import com.hz.fine.master.core.common.utils.constant.ServerConstant;
import com.hz.fine.master.core.model.RequestEncryptionJson;
import com.hz.fine.master.core.model.ResponseEncryptionJson;
import com.hz.fine.master.core.model.did.DidRechargeModel;
import com.hz.fine.master.core.model.order.OrderModel;
import com.hz.fine.master.core.model.strategy.StrategyData;
import com.hz.fine.master.core.model.strategy.StrategyModel;
import com.hz.fine.master.core.protocol.request.did.recharge.RequestDidRecharge;
import com.hz.fine.master.core.protocol.request.sell.RequestSell;
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
 * @Description 我要卖的Controller层
 * @Author yoko
 * @Date 2020/6/29 19:27
 * @Version 1.0
 */
@RestController
@RequestMapping("/fine/sell")
public class SellController {

    private static Logger log = LoggerFactory.getLogger(SellController.class);

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
     * @Description: 获取我要卖的数据-集合
     * @param request
     * @param response
     * @return com.gd.chain.common.utils.JsonResult<java.lang.Object>
     * @author yoko
     * @date 2019/11/25 22:58
     * local:http://localhost:8086/fine/sell/getDataList
     * 请求的属性类:RequestSell
     * 必填字段:{"agtVer":1,"clientVer":1,"clientType":1,"ctime":201911071802959,"cctime":201911071802959,"sign":"abcdefg","pageNumber":1,"pageSize":3,"token":"111111"}
     * 加密字段:{"jsonData":"eyJhZ3RWZXIiOjEsImNsaWVudFZlciI6MSwiY2xpZW50VHlwZSI6MSwiY3RpbWUiOjIwMTkxMTA3MTgwMjk1OSwiY2N0aW1lIjoyMDE5MTEwNzE4MDI5NTksInNpZ24iOiJhYmNkZWZnIiwicGFnZU51bWJlciI6MSwicGFnZVNpemUiOjMsInRva2VuIjoiMTExMTExIn0="}
     * 客户端加密字段:ctime+秘钥=sign
     * 返回加密字段:stime+秘钥=sign
     * result={
     *     "resultCode": "0",
     *     "message": "success",
     *     "data": {
     *         "jsonData": "eyJkYXRhTGlzdCI6W3siYWNOYW1lIjoiKioqKiIsIm9yZGVyTW9uZXkiOiI4MDAuMDAiLCJwcm9maXQiOiI4Iiwic2VsbE51bSI6IjgwMC4wMCIsInRpbWUiOiIxNTo1MjoxMCJ9LHsiYWNOYW1lIjoiKioqKiIsIm9yZGVyTW9uZXkiOiIxNTAwLjAwIiwicHJvZml0IjoiMTUiLCJzZWxsTnVtIjoiMTUwMC4wMCIsInRpbWUiOiIxNTo1MjozNSJ9LHsiYWNOYW1lIjoiKioqKiIsIm9yZGVyTW9uZXkiOiIxOTAwLjAwIiwicHJvZml0IjoiMTkiLCJzZWxsTnVtIjoiMTkwMC4wMCIsInRpbWUiOiIxNTo1MjowNSJ9LHsiYWNOYW1lIjoiKioqKiIsIm9yZGVyTW9uZXkiOiIxNzAwLjAwIiwicHJvZml0IjoiMTciLCJzZWxsTnVtIjoiMTcwMC4wMCIsInRpbWUiOiIxNTo1MjoxNCJ9LHsiYWNOYW1lIjoiKioqKiIsIm9yZGVyTW9uZXkiOiIxNTAwLjAwIiwicHJvZml0IjoiMTUiLCJzZWxsTnVtIjoiMTUwMC4wMCIsInRpbWUiOiIxNTo1MjoyNCJ9XSwic2lnbiI6IjlhMGY0ODNmMmZiMDc0NTFjZmJiMzBlZmZlMjk1YjI2Iiwic3RpbWUiOjE1OTM1MDM1NjMzODgsInRvdGFsTnVtIjoxNTAsIndhaXROdW0iOjk4fQ=="
     *     },
     *     "sgid": "202006301552370000001",
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
        RequestSell requestModel = new RequestSell();
        try{
            // 解密
            data = StringUtil.decoderBase64(requestData.jsonData);
            requestModel  = JSON.parseObject(data, RequestSell.class);
            //#临时数据
//            if (!StringUtils.isBlank(requestModel.token)){
//                if (requestModel.token.equals("111111")){
//                    ComponentUtil.redisService.set(requestModel.token, "1");
//                }
//            }

            // 获取用户的ID
            String strCache = (String) ComponentUtil.redisService.get(requestModel.token);
            if (!StringUtils.isBlank(strCache)) {
                // 登录存储在缓存中的用户id
                did = Long.parseLong(strCache);
                // 设置10秒钟的redis缓存时间

                String strKeyCache = CachedKeyUtils.getCacheKey(CacheKey.DID_ONOFF, did);
                ComponentUtil.redisService.set(strKeyCache, String.valueOf(did), 10L);

            }

            // 查询策略里面的消耗金额范围内的奖励规则列表
            StrategyModel strategyQuery = HodgepodgeMethod.assembleStrategyQuery(ServerConstant.StrategyEnum.CONSUME_MONEY_LIST.getStgType());
            StrategyModel strategyModel = ComponentUtil.strategyService.getStrategyModel(strategyQuery, ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_ZERO);
            // 解析奖励规则的值
            List<StrategyData> consumeMoneyList = JSON.parseArray(strategyModel.getStgBigValue(), StrategyData.class);

            OrderModel orderQuery = HodgepodgeMethod.assembleOrderQuery("0.01");
            List<OrderModel> orderList = ComponentUtil.orderService.getSucOrderList(orderQuery);
            if (orderList == null || orderList.size() <= 0){
                // 组装假数据
                orderList = HodgepodgeMethod.getTempOrderList(consumeMoneyList, 0);
            }else if(orderList.size() > 0 && orderList.size() <= 3){
                // 因为数据少，还需组装假数据
                List<OrderModel> tempOrderList = HodgepodgeMethod.getTempOrderList(consumeMoneyList, 1);
                orderList.addAll(tempOrderList);
            }

            // 组装返回客户端的数据
            long stime = System.currentTimeMillis();
            String sign = SignUtil.getSgin(stime, secretKeySign); // stime+秘钥=sign
            String strData = HodgepodgeMethod.assembleSellListResult(stime, sign, orderList, null);
            // 数据加密
            String encryptionData = StringUtil.mergeCodeBase64(strData);
            ResponseEncryptionJson resultDataModel = new ResponseEncryptionJson();
            resultDataModel.jsonData = encryptionData;
            // 返回数据给客户端
            return JsonResult.successResult(resultDataModel, cgid, sgid);
        }catch (Exception e){
            Map<String,String> map = ExceptionMethod.getException(e, ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_TWO);
            // #添加异常
            log.error(String.format("this SellController.getDataList() is error , the cgid=%s and sgid=%s and all data=%s!", cgid, sgid, data));
            if (!StringUtils.isBlank(map.get("dbCode"))){
                log.error(String.format("this SellController.getDataList() is error codeInfo, the dbCode=%s and dbMessage=%s !", map.get("dbCode"), map.get("dbMessage")));
            }
            e.printStackTrace();
            return JsonResult.failedResult(map.get("message"), map.get("code"), cgid, sgid);
        }
    }

}
