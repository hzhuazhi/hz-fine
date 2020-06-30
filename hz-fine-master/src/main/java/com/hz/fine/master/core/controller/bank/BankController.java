package com.hz.fine.master.core.controller.bank;

import com.alibaba.fastjson.JSON;
import com.hz.fine.master.core.common.exception.ExceptionMethod;
import com.hz.fine.master.core.common.utils.JsonResult;
import com.hz.fine.master.core.common.utils.SignUtil;
import com.hz.fine.master.core.common.utils.StringUtil;
import com.hz.fine.master.core.common.utils.constant.CacheKey;
import com.hz.fine.master.core.common.utils.constant.CachedKeyUtils;
import com.hz.fine.master.core.common.utils.constant.ServerConstant;
import com.hz.fine.master.core.controller.did.DidRechargeController;
import com.hz.fine.master.core.model.RequestEncryptionJson;
import com.hz.fine.master.core.model.ResponseEncryptionJson;
import com.hz.fine.master.core.model.bank.BankModel;
import com.hz.fine.master.core.model.mobilecard.MobileCardModel;
import com.hz.fine.master.core.model.strategy.StrategyModel;
import com.hz.fine.master.core.protocol.request.bank.RequestBank;
import com.hz.fine.master.core.protocol.response.bank.BuyBank;
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
 * @Description 银行卡信息的Controller层
 * @Author yoko
 * @Date 2020/6/29 13:42
 * @Version 1.0
 */
@RestController
@RequestMapping("/fine/buy")
public class BankController {

    private static Logger log = LoggerFactory.getLogger(BankController.class);

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
     * @Description: 我要买-银行卡数据-集合
     * @param request
     * @param response
     * @return com.gd.chain.common.utils.JsonResult<java.lang.Object>
     * @author yoko
     * @date 2019/11/25 22:58
     * local:http://localhost:8086/fine/buy/getDataList
     * 请求的属性类:RequestBank
     * 必填字段:{"agtVer":1,"clientVer":1,"clientType":1,"ctime":201911071802959,"cctime":201911071802959,"sign":"abcdefg","pageNumber":1,"pageSize":3,"token":"111111"}
     * 加密字段:{"jsonData":"eyJhZ3RWZXIiOjEsImNsaWVudFZlciI6MSwiY2xpZW50VHlwZSI6MSwiY3RpbWUiOjIwMTkxMTA3MTgwMjk1OSwiY2N0aW1lIjoyMDE5MTEwNzE4MDI5NTksInNpZ24iOiJhYmNkZWZnIiwicGFnZU51bWJlciI6MSwicGFnZVNpemUiOjMsInRva2VuIjoiMTExMTExIn0="}
     * 客户端加密字段:ctime+秘钥=sign
     * 返回加密字段:stime+秘钥=sign
     * result={
     *     "resultCode": "0",
     *     "message": "success",
     *     "data": {
     *         "jsonData": "eyJkYXRhTGlzdCI6W3siZGlzdHJpYnV0aW9uTW9uZXkiOiIxMDAwLjAwIiwiaW52YWxpZFRpbWUiOiIyMDIwLTA1LTIxIDE1OjUwOjIzIiwib3JkZXJNb25leSI6IjEwMDAuMDAiLCJvcmRlck5vIjoiMjAyMDA1MjExNTQwMTYwMDAwMDAxIiwib3JkZXJTdGF0dXMiOjEsInBpY3R1cmVBZHMiOiIifSx7ImRpc3RyaWJ1dGlvbk1vbmV5IjoiMTAwMC4wMCIsImludmFsaWRUaW1lIjoiMjAyMC0wNS0yMSAxNToxNDowNSIsIm9yZGVyTW9uZXkiOiIxMDAwLjAwIiwib3JkZXJObyI6IjIwMjAwNTIxMTUwMzU2MDAwMDAwMSIsIm9yZGVyU3RhdHVzIjoxLCJwaWN0dXJlQWRzIjoiIn0seyJkaXN0cmlidXRpb25Nb25leSI6Ijk5OS45NSIsImludmFsaWRUaW1lIjoiMjAyMC0wNS0yMSAxNToxMzoyNSIsIm9yZGVyTW9uZXkiOiIxMDAwLjAwIiwib3JkZXJObyI6IjIwMjAwNTIxMTUwMzIwMDAwMDAwMSIsIm9yZGVyU3RhdHVzIjoxLCJwaWN0dXJlQWRzIjoiIn1dLCJzaWduIjoiYjdjNjNjMmZmYjM0OTI2YmIwYWE4ZTI2ZWU1ZWMwYTMiLCJzdGltZSI6MTU5MDA0OTU3OTg4MX0="
     *     },
     *     "sgid": "202005211626180000001",
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

        RequestBank requestModel = new RequestBank();
        try{
            // 解密
            data = StringUtil.decoderBase64(requestData.jsonData);
            requestModel  = JSON.parseObject(data, RequestBank.class);
            //#临时数据
//            if (!StringUtils.isBlank(requestModel.token)){
//                if (requestModel.token.equals("111111")){
//                    ComponentUtil.redisService.set(requestModel.token, "1");
//                }
//            }
            // check校验数据

            // 查询正常使用的手机卡
            MobileCardModel mobileCardQuery = HodgepodgeMethod.assembleMobileCardQueryByUseStatus(ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_ONE);
            List<MobileCardModel> mobileCardList = ComponentUtil.mobileCardService.findByCondition(mobileCardQuery);
            HodgepodgeMethod.checkMobileCardDataIsNull(mobileCardList);

            // 策略数据：查询银行工作日期
            StrategyModel strategyBankWorkQuery = HodgepodgeMethod.assembleStrategyQuery(ServerConstant.StrategyEnum.BANK_WORK.getStgType());
            StrategyModel strategyBankWorkModel = ComponentUtil.strategyService.getStrategyModel(strategyBankWorkQuery, ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_ZERO);
            HodgepodgeMethod.checkStrategyByBankWork(strategyBankWorkModel);

            // 组装查询银行卡的查询条件
            BankModel bankQuery = HodgepodgeMethod.assembleBankByBuyQuery(mobileCardList, strategyBankWorkModel.getStgValue(), requestModel);
            List<BankModel> bankList = ComponentUtil.bankService.queryByList(bankQuery);
            HodgepodgeMethod.checkBankListData(bankList);

            // 筛选出可以使用的银行卡集合：未被日上月上限制的
            List<BuyBank> buyBankList = ComponentUtil.bankService.screenBankByBuy(bankList);


            // 组装返回客户端的数据
            long stime = System.currentTimeMillis();
            String sign = SignUtil.getSgin(stime, secretKeySign); // stime+秘钥=sign
            String strData = HodgepodgeMethod.assembleBankListResult(stime, sign, buyBankList, bankQuery.getRowCount());
            // 数据加密
            String encryptionData = StringUtil.mergeCodeBase64(strData);
            ResponseEncryptionJson resultDataModel = new ResponseEncryptionJson();
            resultDataModel.jsonData = encryptionData;
            // 返回数据给客户端
            return JsonResult.successResult(resultDataModel, cgid, sgid);
        }catch (Exception e){
            Map<String,String> map = ExceptionMethod.getException(e, ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_TWO);
            // #添加异常
            log.error(String.format("this BankController.getDataList() is error , the cgid=%s and sgid=%s and all data=%s!", cgid, sgid, data));
            if (!StringUtils.isBlank(map.get("dbCode"))){
                log.error(String.format("this BankController.getDataList() is error codeInfo, the dbCode=%s and dbMessage=%s !", map.get("dbCode"), map.get("dbMessage")));
            }
            e.printStackTrace();
            return JsonResult.failedResult(map.get("message"), map.get("code"), cgid, sgid);
        }
    }




    /**
     * @Description: 获取银行卡信息-详情
     * <p>
     *     用户正式点击我要购买
     * </p>
     * @param request
     * @param response
     * @return com.gd.chain.common.utils.JsonResult<java.lang.Object>
     * @author yoko
     * @date 2019/11/25 22:58
     * local:http://localhost:8086/fine/buy/getData
     * 请求的属性类:RequestBank
     * 必填字段:{"id":29,"agtVer":1,"clientVer":1,"clientType":1,"ctime":201911071802959,"cctime":201911071802959,"sign":"abcdefg","token":"111111"}
     * 加密字段:{"jsonData":"eyJpZCI6MjksImFndFZlciI6MSwiY2xpZW50VmVyIjoxLCJjbGllbnRUeXBlIjoxLCJjdGltZSI6MjAxOTExMDcxODAyOTU5LCJjY3RpbWUiOjIwMTkxMTA3MTgwMjk1OSwic2lnbiI6ImFiY2RlZmciLCJ0b2tlbiI6IjExMTExMSJ9"}
     * 客户端加密字段:id+ctime+cctime+秘钥=sign
     * 服务端加密字段:stime+秘钥=sign
     * result={
     *     "resultCode": "0",
     *     "message": "success",
     *     "data": {
     *         "jsonData": "eyJkYXRhTW9kZWwiOnsiYWNjb3VudE5hbWUiOiLmi5vllYbpk7booYwxIiwiYmFua0NhcmQiOiI0NTc3MjYzNjY2MjcyNjM2MzIyMSIsImJhbmtOYW1lIjoi5oub5ZWG6ZO26KGMIiwic3ViYnJhbmNoTmFtZSI6Inh4eHjpk7booYzpkrHmsZ/mlK/ooYwifSwic2lnbiI6Ijc3ZDlhM2RlN2EzMDU1NTA3MDlhNDMxNTAwMjI5OTZiIiwic3RpbWUiOjE1OTM0MTc3OTIxNTV9"
     *     },
     *     "sgid": "202006291603120000001",
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

        RequestBank requestModel = new RequestBank();
        try{
            // 解密
            data = StringUtil.decoderBase64(requestData.jsonData);
            requestModel  = JSON.parseObject(data, RequestBank.class);
            //#临时数据
//            if (!StringUtils.isBlank(requestModel.token)){
//                if (requestModel.token.equals("111111")){
//                    ComponentUtil.redisService.set(requestModel.token, "1");
//                }
//            }
            // check校验请求的数据
            did = HodgepodgeMethod.checkBankData(requestModel);

            // 获取银行卡详情数据
            BankModel bankQuery = HodgepodgeMethod.assembleBankById(requestModel.id);
            BankModel bankData = (BankModel) ComponentUtil.bankService.findByObject(bankQuery);
            // 组装返回客户端的数据
            long stime = System.currentTimeMillis();
            String sign = SignUtil.getSgin(stime, secretKeySign); // stime+秘钥=sign
            String strData = HodgepodgeMethod.assembleBankDataResult(stime, sign, bankData);
            // 数据加密
            String encryptionData = StringUtil.mergeCodeBase64(strData);
            ResponseEncryptionJson resultDataModel = new ResponseEncryptionJson();
            resultDataModel.jsonData = encryptionData;
            // 返回数据给客户端
            return JsonResult.successResult(resultDataModel, cgid, sgid);
        }catch (Exception e){
            Map<String,String> map = ExceptionMethod.getException(e, ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_TWO);
            // 添加异常
            log.error(String.format("this BankController.getData() is error , the cgid=%s and sgid=%s and all data=%s!", cgid, sgid, data));
            if (!StringUtils.isBlank(map.get("dbCode"))){
                log.error(String.format("this BankController.getData() is error codeInfo, the dbCode=%s and dbMessage=%s !", map.get("dbCode"), map.get("dbMessage")));
            }
            e.printStackTrace();
            return JsonResult.failedResult(map.get("message"), map.get("code"), cgid, sgid);
        }
    }
}
