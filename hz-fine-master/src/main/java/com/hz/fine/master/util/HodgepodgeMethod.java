package com.hz.fine.master.util;

import com.alibaba.fastjson.JSON;
import com.hz.fine.master.core.common.exception.ServiceException;
import com.hz.fine.master.core.common.utils.*;
import com.hz.fine.master.core.common.utils.constant.CacheKey;
import com.hz.fine.master.core.common.utils.constant.CachedKeyUtils;
import com.hz.fine.master.core.common.utils.constant.ErrorCode;
import com.hz.fine.master.core.common.utils.constant.ServerConstant;
import com.hz.fine.master.core.model.bank.BankModel;
import com.hz.fine.master.core.model.client.ClientCollectionDataModel;
import com.hz.fine.master.core.model.consult.ConsultModel;
import com.hz.fine.master.core.model.did.*;
import com.hz.fine.master.core.model.mobilecard.MobileCardModel;
import com.hz.fine.master.core.model.notice.NoticeModel;
import com.hz.fine.master.core.model.order.OrderModel;
import com.hz.fine.master.core.model.question.QuestionDDModel;
import com.hz.fine.master.core.model.question.QuestionDModel;
import com.hz.fine.master.core.model.question.QuestionMModel;
import com.hz.fine.master.core.model.region.RegionModel;
import com.hz.fine.master.core.model.strategy.StrategyData;
import com.hz.fine.master.core.model.strategy.StrategyModel;
import com.hz.fine.master.core.model.upgrade.UpgradeModel;
import com.hz.fine.master.core.model.wx.WxClerkModel;
import com.hz.fine.master.core.model.wx.WxClerkUnboundModel;
import com.hz.fine.master.core.model.wx.WxModel;
import com.hz.fine.master.core.protocol.request.bank.RequestBank;
import com.hz.fine.master.core.protocol.request.did.RequestDid;
import com.hz.fine.master.core.protocol.request.did.RequestDidCollectionAccount;
import com.hz.fine.master.core.protocol.request.did.collection.QrCode;
import com.hz.fine.master.core.protocol.request.did.collection.RequestDidCollectionAccountAll;
import com.hz.fine.master.core.protocol.request.did.onoff.RequestDidOnoff;
import com.hz.fine.master.core.protocol.request.did.qrcode.DidCollectionAccountQrCode;
import com.hz.fine.master.core.protocol.request.did.qrcode.RequestDidCollectionAccountQrCode;
import com.hz.fine.master.core.protocol.request.did.recharge.RequestDidRecharge;
import com.hz.fine.master.core.protocol.request.did.reward.RequestReward;
import com.hz.fine.master.core.protocol.request.notice.RequestNotice;
import com.hz.fine.master.core.protocol.request.order.RequestOrder;
import com.hz.fine.master.core.protocol.request.strategy.RequestStrategy;
import com.hz.fine.master.core.protocol.request.vcode.RequestVcode;
import com.hz.fine.master.core.protocol.response.ResponseData;
import com.hz.fine.master.core.protocol.response.bank.Bank;
import com.hz.fine.master.core.protocol.response.bank.BankMoney;
import com.hz.fine.master.core.protocol.response.bank.BuyBank;
import com.hz.fine.master.core.protocol.response.bank.ResponseBank;
import com.hz.fine.master.core.protocol.response.consult.Consult;
import com.hz.fine.master.core.protocol.response.consult.ResponseConsult;
import com.hz.fine.master.core.protocol.response.did.ResponseDid;
import com.hz.fine.master.core.protocol.response.did.basic.DidBasic;
import com.hz.fine.master.core.protocol.response.did.collectionaccount.DidCollectionAccount;
import com.hz.fine.master.core.protocol.response.did.collectionaccount.ResponseDidCollectionAccount;
import com.hz.fine.master.core.protocol.response.did.onoff.DidOnoff;
import com.hz.fine.master.core.protocol.response.did.onoff.ResponseDidOnoff;
import com.hz.fine.master.core.protocol.response.did.qrcode.ResponseDidCollectionAccountQrCode;
import com.hz.fine.master.core.protocol.response.did.recharge.DidRecharge;
import com.hz.fine.master.core.protocol.response.did.recharge.RechargeInfo;
import com.hz.fine.master.core.protocol.response.did.recharge.ResponseDidRecharge;
import com.hz.fine.master.core.protocol.response.did.reward.DidReward;
import com.hz.fine.master.core.protocol.response.did.reward.DidShare;
import com.hz.fine.master.core.protocol.response.did.reward.ResponseDidReward;
import com.hz.fine.master.core.protocol.response.notice.Notice;
import com.hz.fine.master.core.protocol.response.notice.ResponseNotice;
import com.hz.fine.master.core.protocol.response.order.Order;
import com.hz.fine.master.core.protocol.response.order.OrderDistribution;
import com.hz.fine.master.core.protocol.response.order.ResponseOrder;
import com.hz.fine.master.core.protocol.response.question.QuestionD;
import com.hz.fine.master.core.protocol.response.question.QuestionDD;
import com.hz.fine.master.core.protocol.response.question.QuestionM;
import com.hz.fine.master.core.protocol.response.question.ResponseQuestion;
import com.hz.fine.master.core.protocol.response.sell.ResponseSell;
import com.hz.fine.master.core.protocol.response.sell.Sell;
import com.hz.fine.master.core.protocol.response.strategy.ResponseStrategy;
import com.hz.fine.master.core.protocol.response.strategy.money.StrategyMoney;
import com.hz.fine.master.core.protocol.response.strategy.money.StrategyMoneyGrade;
import com.hz.fine.master.core.protocol.response.strategy.qiniu.QiNiu;
import com.hz.fine.master.core.protocol.response.strategy.share.StrategyShare;
import com.hz.fine.master.core.protocol.response.upgrade.ResponseUpgrade;
import com.hz.fine.master.core.protocol.response.vcode.ResponseVcode;
import com.hz.fine.master.core.protocol.response.vcode.Vcode;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @Description 公共方法类
 * @Author yoko
 * @Date 2020/1/7 20:25
 * @Version 1.0
 */
public class HodgepodgeMethod {
    private static Logger log = LoggerFactory.getLogger(HodgepodgeMethod.class);

    /**
     * @Description: 组装查询地域的查询条件
     * @param ip
     * @return RegionModel
     * @author yoko
     * @date 2019/12/18 18:41
     */
    public static RegionModel assembleRegionModel(String ip){
        RegionModel resBean = new RegionModel();
        resBean.setIp(ip);
        return resBean;
    }


    /**
     * @Description: 根据token获取缓存中用户ID的值
     * @param token - 登录token
     * @return Long
     * @author yoko
     * @date 2019/11/21 18:01
     */
    public static long getDidByToken(String token){
        Long did = 0L;
        if (!StringUtils.isBlank(token)){
            //        String strKeyCache = CachedKeyUtils.getCacheKey(CacheKey.TOKEN_INFO, token);
            String strCache = (String) ComponentUtil.redisService.get(token);
            if (!StringUtils.isBlank(strCache)) {
                // 登录存储在缓存中的用户id
                did = Long.parseLong(strCache);
            }
        }
        return did;
    }

    /**
     * @Description: 百问百答类别集合的数据组装返回客户端的方法
     * @param stime - 服务器的时间
     * @param sign - 签名
     * @param questionMList - 百问百答类别集合
     * @param rowCount - 总行数
     * @return java.lang.String
     * @author yoko
     * @date 2019/11/25 22:45
     */
    public static String assembleQuestionMResult(long stime, String sign, List<QuestionMModel> questionMList, Integer rowCount){
        ResponseQuestion dataModel = new ResponseQuestion();
        if (questionMList != null && questionMList.size() > ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_ZERO){
            List<QuestionM> dataList = BeanUtils.copyList(questionMList, QuestionM.class);
            dataModel.qMList = dataList;
        }
        if (rowCount != null){
            dataModel.rowCount = rowCount;
        }
        dataModel.setStime(stime);
        dataModel.setSign(sign);
        return JSON.toJSONString(dataModel);
    }


    /**
     * @Description: 百问百答-详情集合的数据组装返回客户端的方法
     * @param stime - 服务器的时间
     * @param sign - 签名
     * @param questionDList - 百问百答详情集合
     * @param rowCount - 总行数
     * @return java.lang.String
     * @author yoko
     * @date 2019/11/25 22:45
     */
    public static String assembleQuestionDResult(long stime, String sign, List<QuestionDModel> questionDList, Integer rowCount){
        ResponseQuestion dataModel = new ResponseQuestion();
        if (questionDList != null && questionDList.size() > ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_ZERO){
            List<QuestionD> dataList = BeanUtils.copyList(questionDList, QuestionD.class);
            dataModel.qDList = dataList;
        }
        if (rowCount != null){
            dataModel.rowCount = rowCount;
        }
        dataModel.setStime(stime);
        dataModel.setSign(sign);
        return JSON.toJSONString(dataModel);
    }


    /**
     * @Description: 百问百答-详情-步骤集合的数据组装返回客户端的方法
     * @param stime - 服务器的时间
     * @param sign - 签名
     * @param questionDDList - 百问百答详情-步骤集合
     * @param rowCount - 总行数
     * @return java.lang.String
     * @author yoko
     * @date 2019/11/25 22:45
     */
    public static String assembleQuestionDDResult(long stime, String sign, List<QuestionDDModel> questionDDList, Integer rowCount){
        ResponseQuestion dataModel = new ResponseQuestion();
        if (questionDDList != null && questionDDList.size() > ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_ZERO){
            List<QuestionDD> dataList = BeanUtils.copyList(questionDDList, QuestionDD.class);
            dataModel.qDdList = dataList;
        }
        if (rowCount != null){
            dataModel.rowCount = rowCount;
        }
        dataModel.setStime(stime);
        dataModel.setSign(sign);
        return JSON.toJSONString(dataModel);
    }


    /**
     * @Description: 百问百答-详情的数据组装返回客户端的方法
     * @param stime - 服务器的时间
     * @param sign - 签名
     * @param questionDModel - 百问百答详情
     * @param rowCount - 总行数
     * @return java.lang.String
     * @author yoko
     * @date 2019/11/25 22:45
     */
    public static String assembleQuestionDDataResult(long stime, String sign, QuestionDModel questionDModel, Integer rowCount){
        ResponseQuestion dataModel = new ResponseQuestion();
        if (questionDModel != null){
            QuestionD data = BeanUtils.copy(questionDModel, QuestionD.class);
            dataModel.qD = data;
        }
        if (rowCount != null){
            dataModel.rowCount = rowCount;
        }
        dataModel.setStime(stime);
        dataModel.setSign(sign);
        return JSON.toJSONString(dataModel);
    }

    /**
     * @Description: check校验获取验证码的方法
     * @param requestModel
     * @return
     * @author yoko
     * @date 2020/05/14 15:57
    */
    public static long checkGetCd(RequestVcode requestModel) throws Exception{
        long did = 0;
        // 1.校验所有数据
        if (requestModel == null ){
            throw new ServiceException(ErrorCode.ENUM_ERROR.V00001.geteCode(), ErrorCode.ENUM_ERROR.V00001.geteDesc());
        }

        // 校验手机号是否为空
        if (StringUtils.isBlank(requestModel.phoneNum)){
            throw new ServiceException(ErrorCode.ENUM_ERROR.V00002.geteCode(), ErrorCode.ENUM_ERROR.V00002.geteDesc());
        }
        // 校验是否需要检测是否登录
        boolean flag = false;//值等于false表示无需登录，值等于true表示需要登录
        if (requestModel.vType == ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_ONE || requestModel.vType == ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_TWO){
            // 类型等于1表示注册，2忘记登录密码，所以无需登录
            flag = false;
        }
        if (flag){
            // 2.校验token值
            if (StringUtils.isBlank(requestModel.token)){
                throw new ServiceException(ErrorCode.ENUM_ERROR.D00001.geteCode(), ErrorCode.ENUM_ERROR.D00001.geteDesc());
            }
        }
        if (flag){
            // 3.校验用户是否登录
            did = HodgepodgeMethod.checkIsLogin(requestModel.token);
        }
        return did;
    }


    /**
     * @Description: check校验提交验证码的方法
     * @param requestModel
     * @return
     * @author yoko
     * @date 2020/05/14 15:57
     */
    public static void checkSubmitCd(RequestVcode requestModel) throws Exception{
        // 1.校验所有数据
        if (requestModel == null ){
            throw new ServiceException(ErrorCode.ENUM_ERROR.V00007.geteCode(), ErrorCode.ENUM_ERROR.V00007.geteDesc());
        }

        // 校验手机号是否为空
        if (StringUtils.isBlank(requestModel.phoneNum)){
            throw new ServiceException(ErrorCode.ENUM_ERROR.V00008.geteCode(), ErrorCode.ENUM_ERROR.V00008.geteDesc());
        }

        if (requestModel.vType == null || requestModel.vType == 0){
            throw new ServiceException(ErrorCode.ENUM_ERROR.V00009.geteCode(), ErrorCode.ENUM_ERROR.V00009.geteDesc());
        }
    }

    /**
     * @Description: 校验用户是否处于登录状态
     * @param token - 登录token
     * @return Long
     * @author yoko
     * @date 2020/05/14 18:01
     */
    public static long checkIsLogin(String token) throws Exception{
        Long did;
        String strCache = (String) ComponentUtil.redisService.get(token);
        if (!StringUtils.isBlank(strCache)) {
            // 登录存储在缓存中的用户id
            did = Long.parseLong(strCache);
        }else {
            throw new ServiceException(ErrorCode.ENUM_ERROR.D00000.geteCode(), ErrorCode.ENUM_ERROR.D00000.geteDesc());
        }
        return did;
    }


    /**
     * @Description: 判断用户是否频繁发送验证码
     * @param redisKey - redis的key
     * @return
     * @author yoko
     * @date 2020/05/14 17:56
     */
    public static void checkOftenSendCode(String redisKey) throws Exception{
        String strKeyCache = redisKey;
        String strCache = (String) ComponentUtil.redisService.get(strKeyCache);
        if (!StringUtils.isBlank(strCache)){
            throw new ServiceException(ErrorCode.ENUM_ERROR.V00003.geteCode(), ErrorCode.ENUM_ERROR.V00003.geteDesc());
        }
    }


    /**
     * @Description: 校验发送验证码是否成功
     * @param code - 验证码：验证码值为空，则代表发送失败，反之则发送成功
     * @return void
     * @author yoko
     * @date 2020/5/14 19:11
     */
    public static void checkSendCode(String code) throws Exception{
        if (StringUtils.isBlank(code)){
            throw new ServiceException(ErrorCode.ENUM_ERROR.V00004.geteCode(), ErrorCode.ENUM_ERROR.V00004.geteDesc());
        }
    }


    /**
     * @Description: 校验验证码的数据
     * @param vType - 验证码类型：1注册....
     * @param phoneNum - 手机号
     * @param vcode - 验证码
     * @return void
     * @author yoko
     * @date 2020/5/14 19:08
     */
    public static void checkVcode(int vType, String  phoneNum, String vcode) throws Exception{
        String strKeyCache = CachedKeyUtils.getCacheKey(CacheKey.PHONE_VCODE, vType, phoneNum);
        String strCache = (String) ComponentUtil.redisService.get(strKeyCache);
        if (StringUtils.isBlank(strCache)){
            throw new ServiceException(ErrorCode.ENUM_ERROR.V00005.geteCode(), ErrorCode.ENUM_ERROR.V00005.geteDesc());
        }else {
            if (!strCache.equals(vcode)){
                throw new ServiceException(ErrorCode.ENUM_ERROR.V00006.geteCode(), ErrorCode.ENUM_ERROR.V00006.geteDesc());
            }
        }
    }


    /**
     * @Description: 公共的返回客户端的方法
     * @param stime - 服务器的时间
     * @param token - 登录token
     * @param sign - 签名
     * @return java.lang.String
     * @author yoko
     * @date 2019/11/13 21:45
     */
    public static String assembleResult(long stime, String token, String sign){
        ResponseData dataModel = new ResponseData();
        dataModel.stime = stime;
        if (!StringUtils.isBlank(token)){
            dataModel.token = token;
        }
        dataModel.sign = sign;
        return JSON.toJSONString(dataModel);
    }

    /**
     * @Description: 登录成功后返回客户端的方法
     * @param stime - 服务器的时间
     * @param token - 登录token
     * @param sign - 签名
     * @param haveType - 是否有绑定的支付宝账号：1没有绑定，2绑定
     * @param userId - 支付宝账号ID
     * @return java.lang.String
     * @author yoko
     * @date 2019/11/13 21:45
     */
    public static String assembleLogOnResult(long stime, String token, String sign, int haveType, String userId){
        ResponseData dataModel = new ResponseData();
        dataModel.stime = stime;
        if (!StringUtils.isBlank(token)){
            dataModel.token = token;
        }
        if (!StringUtils.isBlank(userId)){
            dataModel.userId = userId;
        }
        dataModel.sign = sign;
        dataModel.haveType = haveType;
        return JSON.toJSONString(dataModel);
    }


    /**
     * @Description: 提交验证码的返回客户端的方法-返回vtoken
     * @param stime - 服务器的时间
     * @param vtoken - 登录token
     * @param sign - 签名
     * @return java.lang.String
     * @author yoko
     * @date 2019/11/13 21:45
     */
    public static String assembleSubmitCdResult(long stime, String vtoken, String sign){
        ResponseVcode dataModel = new ResponseVcode();
        Vcode vcode = new Vcode();
        vcode.vtoken = vtoken;
        dataModel.dataModel = vcode;
        dataModel.stime = stime;
        dataModel.sign = sign;
        return JSON.toJSONString(dataModel);
    }

    /**
     * @Description: check校验数据当用户注册的时候
     * @param requestModel
     * @return
     * @author yoko
     * @date 2020/05/14 15:57
     */
    public static void checkRegister(RequestDid requestModel) throws Exception{
        // 1.校验所有数据
        if (requestModel == null ){
            throw new ServiceException(ErrorCode.ENUM_ERROR.D00002.geteCode(), ErrorCode.ENUM_ERROR.D00002.geteDesc());
        }

        // 校验账号是否为空
        if (StringUtils.isBlank(requestModel.acNum)){
            throw new ServiceException(ErrorCode.ENUM_ERROR.D00003.geteCode(), ErrorCode.ENUM_ERROR.D00003.geteDesc());
        }

        // 校验登录密码是否为空
        if (StringUtils.isBlank(requestModel.passWd)){
            throw new ServiceException(ErrorCode.ENUM_ERROR.D00004.geteCode(), ErrorCode.ENUM_ERROR.D00004.geteDesc());
        }

        // 校验邀请码是否为空
        if (StringUtils.isBlank(requestModel.icode)){
            throw new ServiceException(ErrorCode.ENUM_ERROR.D00005.geteCode(), ErrorCode.ENUM_ERROR.D00005.geteDesc());
        }

        // 校验注册时的验证码
        if (StringUtils.isBlank(requestModel.vcode)){
            throw new ServiceException(ErrorCode.ENUM_ERROR.D00006.geteCode(), ErrorCode.ENUM_ERROR.D00006.geteDesc());
        }

        // 校验验证码
        HodgepodgeMethod.checkVcode(ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_ONE, requestModel.acNum, requestModel.vcode);

    }


    /**
     * @Description: 组装根据用户账号查询用户信息的方法
     * @param acNum - 用户登录账号
     * @return
     * @author yoko
     * @date 2020/5/14 17:20
    */
    public static DidModel assembleDidByAcNum(String acNum){
        DidModel resBean = new DidModel();
        resBean.setAcNum(acNum);
        return resBean;
    }

    /**
     * @Description: check校验根据账号查询的数据是否为空
     * <p>不为空，则代表之前已被注册过，不能进行再次注册</p>
     * @param didModel - 用户账号信息
     * @return
     * @author yoko
     * @date 2020/5/14 17:25
    */
    public static void checkRegisterByAcNum(DidModel didModel) throws Exception{
        if (didModel == null || didModel.getId() <= ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_ZERO){
        }else{
            throw new ServiceException(ErrorCode.ENUM_ERROR.D00007.geteCode(), ErrorCode.ENUM_ERROR.D00007.geteDesc());
        }
    }


    /**
     * @Description: 组装根据邀请码查询用户信息的方法
     * @param icode - 邀请码
     * @return
     * @author yoko
     * @date 2020/5/14 17:20
     */
    public static DidModel assembleDidByIcode(String icode){
        DidModel resBean = new DidModel();
        resBean.setIcode(icode);
        return resBean;
    }



    /**
     * @Description: check校验根据邀请码查询的数据是否为空
     * <p>为空，则代表邀请码填写有误</p>
     * @param didModel - 用户账号信息
     * @return
     * @author yoko
     * @date 2020/5/14 17:25
     */
    public static void checkRegisterByIcode(DidModel didModel) throws Exception{
        if (didModel == null || didModel.getId() <= ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_ZERO){
            throw new ServiceException(ErrorCode.ENUM_ERROR.D00008.geteCode(), ErrorCode.ENUM_ERROR.D00008.geteDesc());
        }
    }


    /**
     * @Description: 组装要添加的用户数据
     * @param requestModel - 用户基本信息
     * @param icode - 要注册的用户本身的邀请码
     * @param ownId - 上级ID：归属哪个直推用户
     * @return com.hz.fine.master.core.model.did.DidModel
     * @author yoko
     * @date 2020/5/14 18:36
     */
    public static DidModel assembleDidModel(RequestDid requestModel, String icode, long ownId){
        DidModel resBean = BeanUtils.copy(requestModel, DidModel.class);
        resBean.setPhoneNum(resBean.getAcNum());
        resBean.setIcode(icode);
        resBean.setOwnId(ownId);
        return resBean;
    }


    /**
     * @Description: 组装新增用户的层级关系的数据
     * @param ownId - 新注册的Did
     * @param did - 归属的did
     * @param lbDid - 裂变归属的did
     * @return java.util.List<com.hz.fine.master.core.model.did.DidLevelModel>
     * @author yoko
     * @date 2020/5/14 18:47
     */
    public static List<DidLevelModel> assembleDidLevelList(long ownId, long did, long lbDid){
        List<DidLevelModel> resList = new ArrayList<>();
        DidLevelModel ztModel = new DidLevelModel();
        ztModel.setDid(ownId);
        ztModel.setLevelDid(did);
        ztModel.setLevelType(1);// 直推关系
        resList.add(ztModel);
        if (lbDid > 0){
            DidLevelModel lbModel = new DidLevelModel();
            lbModel.setDid(ownId);
            lbModel.setLevelDid(lbDid);
            lbModel.setLevelType(2);// 裂变关系
            resList.add(lbModel);
        }

        return resList;

    }


    /**
     * @Description: check校验数据当用户修改密码的时候
     * @param requestModel
     * @return
     * @author yoko
     * @date 2020/05/14 15:57
     */
    public static long checkChangePassword(RequestDid requestModel) throws Exception{
        long did;
        // 1.校验所有数据
        if (requestModel == null ){
            throw new ServiceException(ErrorCode.ENUM_ERROR.D00009.geteCode(), ErrorCode.ENUM_ERROR.D00009.geteDesc());
        }

        // 校验token值
        if (StringUtils.isBlank(requestModel.token)){
            throw new ServiceException(ErrorCode.ENUM_ERROR.D00001.geteCode(), ErrorCode.ENUM_ERROR.D00001.geteDesc());
        }

        // 校验用户是否登录
        did = HodgepodgeMethod.checkIsLogin(requestModel.token);

        // 校验账号原始密码
        if (StringUtils.isBlank(requestModel.passWd)){
            throw new ServiceException(ErrorCode.ENUM_ERROR.D00010.geteCode(), ErrorCode.ENUM_ERROR.D00010.geteDesc());
        }

        // 校验账号新密码
        if (StringUtils.isBlank(requestModel.newPassWd)){
            throw new ServiceException(ErrorCode.ENUM_ERROR.D00011.geteCode(), ErrorCode.ENUM_ERROR.D00011.geteDesc());
        }

       return did;

    }


    /**
     * @Description: 组装根据用户ID以及用户密码查询用户信息的方法
     * <p>用户修改密码时：用户处于登录状态，获取用户的ID加上用户输入的密码就可以查到这个账号的信息</p>
     * @param did - 用户的ID
     * @param passWd - 用户的密码
     * @return
     * @author yoko
     * @date 2020/5/14 17:20
     */
    public static DidModel assembleDidByAcNumAndPassWd(long did, String passWd){
        DidModel resBean = new DidModel();
        resBean.setId(did);
        resBean.setPassWd(passWd);
        return resBean;
    }


    /**
     * @Description: check校验原始密码是否与数据库的原始密码匹配
     * @param didModel
     * @return
     * @author yoko
     * @date 2020/5/14 19:54
    */
    public static void checkPassWd(DidModel didModel) throws Exception{
        if (didModel == null || didModel.getId() <= ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_ZERO){
            throw new ServiceException(ErrorCode.ENUM_ERROR.D00012.geteCode(), ErrorCode.ENUM_ERROR.D00012.geteDesc());
        }
    }


    /**
     * @Description: 组装更新用户密码的方法
     * @param did - 用户的ID
     * @param newPassWd - 用户要更新的新密码
     * @return
     * @author yoko
     * @date 2020/5/14 19:56
    */
    public static DidModel assembleUpdatePassWdData(long did, String newPassWd){
        DidModel resBean = new DidModel();
        resBean.setId(did);
        resBean.setNewPassWd(newPassWd);
        return resBean;
    }




    /**
     * @Description: check校验数据当用户修改操作密码/安全密码的时候
     * @param requestModel
     * @return
     * @author yoko
     * @date 2020/05/14 15:57
     */
    public static long checkChangeOperatePassword(RequestDid requestModel) throws Exception{
        long did;
        // 1.校验所有数据
        if (requestModel == null ){
            throw new ServiceException(ErrorCode.ENUM_ERROR.D00024.geteCode(), ErrorCode.ENUM_ERROR.D00024.geteDesc());
        }

        // 校验token值
        if (StringUtils.isBlank(requestModel.token)){
            throw new ServiceException(ErrorCode.ENUM_ERROR.D00001.geteCode(), ErrorCode.ENUM_ERROR.D00001.geteDesc());
        }

        // 校验用户是否登录
        did = HodgepodgeMethod.checkIsLogin(requestModel.token);

        // 校验账号原始操作密码/安全密码
        if (StringUtils.isBlank(requestModel.operateWd)){
            throw new ServiceException(ErrorCode.ENUM_ERROR.D00025.geteCode(), ErrorCode.ENUM_ERROR.D00025.geteDesc());
        }

        // 校验账号新操作密码/安全密码
        if (StringUtils.isBlank(requestModel.newOperateWd)){
            throw new ServiceException(ErrorCode.ENUM_ERROR.D00026.geteCode(), ErrorCode.ENUM_ERROR.D00026.geteDesc());
        }

        return did;

    }

    /**
     * @Description: 组装根据用户ID以及用户操作密码查询用户信息的方法
     * <p>用户修改操作密码时：用户处于登录状态，获取用户的ID加上用户输入的操作密码就可以查到这个账号的信息</p>
     * @param did - 用户的ID
     * @param operateWd - 用户的操作密码
     * @return
     * @author yoko
     * @date 2020/5/14 17:20
     */
    public static DidModel assembleDidByAcNumAndOperateWd(long did, String operateWd){
        DidModel resBean = new DidModel();
        resBean.setId(did);
        resBean.setOperateWd(operateWd);
        return resBean;
    }


    /**
     * @Description: check校验原始安全密码/操作密码是否与数据库的原始安全密码匹配
     * @param didModel
     * @return
     * @author yoko
     * @date 2020/5/14 19:54
     */
    public static void checkOperateWd(DidModel didModel) throws Exception{
        if (didModel == null || didModel.getId() <= ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_ZERO){
            throw new ServiceException(ErrorCode.ENUM_ERROR.D00027.geteCode(), ErrorCode.ENUM_ERROR.D00027.geteDesc());
        }
    }


    /**
     * @Description: 组装更新用户操作密码/安全密码的方法
     * @param did - 用户的ID
     * @param newOperateWd - 用户要更新的新安全密码
     * @return
     * @author yoko
     * @date 2020/5/14 19:56
     */
    public static DidModel assembleUpdateOperateWdData(long did, String newOperateWd){
        DidModel resBean = new DidModel();
        resBean.setId(did);
        resBean.setNewOperateWd(newOperateWd);
        return resBean;
    }



    /**
     * @Description: check校验数据当用户登录的时候
     * @param requestModel
     * @return
     * @author yoko
     * @date 2020/05/14 15:57
     */
    public static void checkLogOnData(RequestDid requestModel) throws Exception{
        // 1.校验所有数据
        if (requestModel == null ){
            throw new ServiceException(ErrorCode.ENUM_ERROR.D00013.geteCode(), ErrorCode.ENUM_ERROR.D00013.geteDesc());
        }

        // 校验登录账号
        if (StringUtils.isBlank(requestModel.acNum)){
            throw new ServiceException(ErrorCode.ENUM_ERROR.D00014.geteCode(), ErrorCode.ENUM_ERROR.D00014.geteDesc());
        }

        // 校验登录密码
        if (StringUtils.isBlank(requestModel.passWd)){
            throw new ServiceException(ErrorCode.ENUM_ERROR.D00015.geteCode(), ErrorCode.ENUM_ERROR.D00015.geteDesc());
        }

    }


    /**
     * @Description: 组装根据用户账号，密码登录查询的查询方法
     * @param acNum - 登录账号
     * @param passWd - 用户的登录密码
     * @return
     * @author yoko
     * @date 2020/5/14 17:20
     */
    public static DidModel assembleDidLogOn(String acNum, String passWd){
        DidModel resBean = new DidModel();
        resBean.setAcNum(acNum);
        resBean.setPassWd(passWd);
        return resBean;
    }

    /**
     * @Description: 组装查询支付宝账号的查询条件
     * @param did - 用户ID
     * @param acType - 收款账户类型：1微信，2支付宝，3银行卡
     * @return com.hz.fine.master.core.model.did.DidCollectionAccountModel
     * @author yoko
     * @date 2020/7/4 15:39
     */
    public static DidCollectionAccountModel assembleDidCollectionAccountQueryByAcType(long did, int acType){
        DidCollectionAccountModel resBean = new DidCollectionAccountModel();
        resBean.setDid(did);
        resBean.setAcType(acType);
        return resBean;
    }


    /**
     * @Description: check校验用户是否登录成功
     * <p>根据账号密码进行查询的数据，是否为空，为空则表示登录失败</p>
     * @param didModel
     * @return
     * @author yoko
     * @date 2020/5/14 19:54
     */
    public static void checkLogOn(DidModel didModel) throws Exception{
        if (didModel == null || didModel.getId() <= ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_ZERO){
            throw new ServiceException(ErrorCode.ENUM_ERROR.D00016.geteCode(), ErrorCode.ENUM_ERROR.D00016.geteDesc());
        }
    }



    /**
     * @Description: check校验数据当重新设置密码的时候/忘记密码需要重新设置密码
     * @param requestModel
     * @return
     * @author yoko
     * @date 2020/05/14 15:57
     */
    public static void checkSetUpPasswordData(RequestDid requestModel) throws Exception{
        // 1.校验所有数据
        if (requestModel == null ){
            throw new ServiceException(ErrorCode.ENUM_ERROR.D00017.geteCode(), ErrorCode.ENUM_ERROR.D00017.geteDesc());
        }

        // 校验vtoken是否为空
        if (StringUtils.isBlank(requestModel.vtoken)){
            throw new ServiceException(ErrorCode.ENUM_ERROR.D00018.geteCode(), ErrorCode.ENUM_ERROR.D00018.geteDesc());
        }

        // 校验登录密码是否为空
        if (StringUtils.isBlank(requestModel.newPassWd)){
            throw new ServiceException(ErrorCode.ENUM_ERROR.D00019.geteCode(), ErrorCode.ENUM_ERROR.D00019.geteDesc());
        }

//        // 校验注册时的验证码
//        if (StringUtils.isBlank(requestModel.vcode)){
//            throw new ServiceException(ErrorCode.ENUM_ERROR.D00020.geteCode(), ErrorCode.ENUM_ERROR.D00020.geteDesc());
//        }

        // 校验验证码-忘记密码的验证码
//        HodgepodgeMethod.checkVcode(ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_TWO, requestModel.acNum, requestModel.vcode);

    }


    /**
     * @Description: 根据vtoken获取用户账号
     * @param vtoken
     * @return
     * @author yoko
     * @date 2020/6/9 15:15
    */
    public static String getAcNumByVtoken(String vtoken) throws Exception{
        String strCache = (String) ComponentUtil.redisService.get(vtoken);
        if (!StringUtils.isBlank(strCache)){
            return strCache;
        }else {
            throw new ServiceException(ErrorCode.ENUM_ERROR.D00020.geteCode(), ErrorCode.ENUM_ERROR.D00020.geteDesc());
        }
    }


    /**
     * @Description: 组装根据用户账号查询用户信息的方法
     * <p>用户忘记密码时：获取用户的账号查这个账号的信息</p>
     * @param acNum - 用户的登录账号
     * @return
     * @author yoko
     * @date 2020/5/14 17:20
     */
    public static DidModel assembleDidByAcNumForFindPassWd(String acNum){
        DidModel resBean = new DidModel();
        resBean.setAcNum(acNum);
        return resBean;
    }


    /**
     * @Description: check校验用户账号是否存在
     * @param didModel
     * @return
     * @author yoko
     * @date 2020/5/14 19:54
     */
    public static void checkSetUpPassword(DidModel didModel) throws Exception{
        if (didModel == null || didModel.getId() <= ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_ZERO){
            throw new ServiceException(ErrorCode.ENUM_ERROR.D00021.geteCode(), ErrorCode.ENUM_ERROR.D00021.geteDesc());
        }
    }



    /**
     * @Description: check校验数据当重新设置安全密码的时候/忘记安全密码需要重新设置安全密码
     * @param requestModel
     * @return
     * @author yoko
     * @date 2020/05/14 15:57
     */
    public static void checkSetUpOperateWdData(RequestDid requestModel) throws Exception{
        // 1.校验所有数据
        if (requestModel == null ){
            throw new ServiceException(ErrorCode.ENUM_ERROR.D00028.geteCode(), ErrorCode.ENUM_ERROR.D00028.geteDesc());
        }

        // 校验账号是否为空
        if (StringUtils.isBlank(requestModel.acNum)){
            throw new ServiceException(ErrorCode.ENUM_ERROR.D00029.geteCode(), ErrorCode.ENUM_ERROR.D00029.geteDesc());
        }

        // 校验新安全密码是否为空
        if (StringUtils.isBlank(requestModel.newOperateWd)){
            throw new ServiceException(ErrorCode.ENUM_ERROR.D00030.geteCode(), ErrorCode.ENUM_ERROR.D00030.geteDesc());
        }

        // 校验安全密码时的验证码
        if (StringUtils.isBlank(requestModel.vcode)){
            throw new ServiceException(ErrorCode.ENUM_ERROR.D00031.geteCode(), ErrorCode.ENUM_ERROR.D00031.geteDesc());
        }

        // 校验验证码-忘记安全密码的验证码
//        HodgepodgeMethod.checkVcode(ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_THREE, requestModel.acNum, requestModel.vcode);
    }


    /**
     * @Description: check校验用户账号是否存在
     * @param didModel
     * @return
     * @author yoko
     * @date 2020/5/14 19:54
     */
    public static void checkSetUpOperateWd(DidModel didModel) throws Exception{
        if (didModel == null || didModel.getId() <= ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_ZERO){
            throw new ServiceException(ErrorCode.ENUM_ERROR.D00032.geteCode(), ErrorCode.ENUM_ERROR.D00032.geteDesc());
        }
    }


    /**
     * @Description: 组装更新用户安全密码的方法
     * @param did - 用户的ID
     * @param newOperateWd - 用户要更新的新安全密码
     * @return
     * @author yoko
     * @date 2020/5/14 19:56
     */
    public static DidModel assembleSetUpOperateWdData(long did, String newOperateWd){
        DidModel resBean = new DidModel();
        resBean.setId(did);
        resBean.setNewOperateWd(newOperateWd);
        return resBean;
    }





    /**
     * @Description: check校验数据当用户添加收款账号时
     * @param requestModel
     * @return
     * @author yoko
     * @date 2020/05/14 15:57
     */
    public static long checkDidCollectionAccountAddData(RequestDidCollectionAccount requestModel) throws Exception{
        long did;
        // 1.校验所有数据
        if (requestModel == null ){
            throw new ServiceException(ErrorCode.ENUM_ERROR.DC00001.geteCode(), ErrorCode.ENUM_ERROR.DC00001.geteDesc());
        }

        // 校验收款账号类型
        if (requestModel.acType == null || requestModel.acType == 0){
            throw new ServiceException(ErrorCode.ENUM_ERROR.DC00002.geteCode(), ErrorCode.ENUM_ERROR.DC00002.geteDesc());
        }

//        // 校验收款的具体账号：类型为微信则微信账号，支付宝为支付宝账号；怕后期有其它冲突
//        if (requestModel.acType != 3){
//            if (StringUtils.isBlank(requestModel.acNum)){
//                throw new ServiceException(ErrorCode.ENUM_ERROR.DC00003.geteCode(), ErrorCode.ENUM_ERROR.DC00003.geteDesc());
//            }
//        }




        // check经营范围类型
        if (requestModel.businessType == null || requestModel.businessType == 0){
            throw new ServiceException(ErrorCode.ENUM_ERROR.DC00007.geteCode(), ErrorCode.ENUM_ERROR.DC00007.geteDesc());
        }

        // check小微商户二维码图片地址
        if (requestModel.acType == 1){
            if (StringUtils.isBlank(requestModel.wxQrCodeAds)){
                throw new ServiceException(ErrorCode.ENUM_ERROR.DC00008.geteCode(), ErrorCode.ENUM_ERROR.DC00008.geteDesc());
            }

            if (StringUtils.isBlank(requestModel.mmQrCode)){
                throw new ServiceException(ErrorCode.ENUM_ERROR.DC00004.geteCode(), ErrorCode.ENUM_ERROR.DC00004.geteDesc());
            }

            // check收款人
            if (StringUtils.isBlank(requestModel.payee)){
                throw new ServiceException(ErrorCode.ENUM_ERROR.DC00005.geteCode(), ErrorCode.ENUM_ERROR.DC00005.geteDesc());
            }
        }else if (requestModel.acType == 2){
            // 收款账号
            if (StringUtils.isBlank(requestModel.acNum)){
                throw new ServiceException(ErrorCode.ENUM_ERROR.DC00003.geteCode(), ErrorCode.ENUM_ERROR.DC00003.geteDesc());
            }

            // 支付宝userId
            if (StringUtils.isBlank(requestModel.userId)){
                throw new ServiceException(ErrorCode.ENUM_ERROR.DC00029.geteCode(), ErrorCode.ENUM_ERROR.DC00029.geteDesc());
            }

            // check支付宝持卡人真实姓名
            if (StringUtils.isBlank(requestModel.payee)){
                throw new ServiceException(ErrorCode.ENUM_ERROR.DC00005.geteCode(), ErrorCode.ENUM_ERROR.DC00005.geteDesc());
            }
        }else if (requestModel.acType == 3){
            // check银行名称/银行卡开户行
            if (StringUtils.isBlank(requestModel.bankName)){
                throw new ServiceException(ErrorCode.ENUM_ERROR.DC00006.geteCode(), ErrorCode.ENUM_ERROR.DC00006.geteDesc());
            }
        }

        // 校验token值
        if (StringUtils.isBlank(requestModel.token)){
            throw new ServiceException(ErrorCode.ENUM_ERROR.D00001.geteCode(), ErrorCode.ENUM_ERROR.D00001.geteDesc());
        }

        // 校验用户是否登录
        did = HodgepodgeMethod.checkIsLogin(requestModel.token);

        return did;

    }

    /**
     * @Description: check校验用户信息
     * <p>
     *     用户只有充值以后才能进行收款账号的添加
     * </p>
     * @param didModel
     * @return
     * @author yoko
     * @date 2020/6/12 14:26
    */
    public static void checkDidInfo(DidModel didModel) throws Exception{
        if (didModel == null || didModel.getId() <= 0){
            throw new ServiceException(ErrorCode.ENUM_ERROR.DC00025.geteCode(), ErrorCode.ENUM_ERROR.DC00025.geteDesc());
        }

//        if (didModel.getVipType() == 1){
//            throw new ServiceException(ErrorCode.ENUM_ERROR.DC00026.geteCode(), ErrorCode.ENUM_ERROR.DC00026.geteDesc());
//        }
    }

    /**
     * @Description: 组装根据收款具体账号昵称查询的查询条件
     * @param payee - 收款账号昵称
     * @return
     * @author yoko
     * @date 2020/5/15 16:10
    */
    public static DidCollectionAccountModel assembleDidCollectionAccountByPayee(String payee){
        DidCollectionAccountModel resBean = new DidCollectionAccountModel();
        resBean.setPayee(payee);
        return resBean;
    }

    /**
     * @Description: 组装根据收款具体账号查询的查询条件
     * @param acNum - 收款账号
     * @return
     * @author yoko
     * @date 2020/5/15 16:10
     */
    public static DidCollectionAccountModel assembleDidCollectionAccountByAcNum(String acNum){
        DidCollectionAccountModel resBean = new DidCollectionAccountModel();
        resBean.setAcNum(acNum);
        return resBean;
    }


    /**
     * @Description: check校验根据收款具体账号查询的数据是否为空
     * <p>不为空，则代表之前已录入过，不能进行再次录入添加</p>
     * @param didCollectionAccountModel - 用户具体收款账号信息
     * @return
     * @author yoko
     * @date 2020/5/14 17:25
     */
    public static void checkDidCollectionAccountAddByAcNum(DidCollectionAccountModel didCollectionAccountModel) throws Exception{
        if (didCollectionAccountModel == null || didCollectionAccountModel.getId() <= ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_ZERO){
        }else{
            throw new ServiceException(ErrorCode.ENUM_ERROR.DC00009.geteCode(), ErrorCode.ENUM_ERROR.DC00009.geteDesc());
        }
    }

    /**
     * @Description: 组装要添加的用户收款账号信息
     * @param requestDidCollectionAccount - 用户收款账号信息
     * @param did - 用户DID
     * @return com.hz.fine.master.core.model.did.DidCollectionAccountModel
     * @author yoko
     * @date 2020/5/15 16:22
     */
    public static DidCollectionAccountModel assembleDidCollectionAccount(RequestDidCollectionAccount requestDidCollectionAccount, long did){
        DidCollectionAccountModel resBean = BeanUtils.copy(requestDidCollectionAccount, DidCollectionAccountModel.class);
        resBean.setDid(did);
        return resBean;
    }

    /**
     * @Description: check校验数据获取用户收款账号集合时
     * @param requestModel
     * @return
     * @author yoko
     * @date 2020/05/14 15:57
     */
    public static long checkDidCollectionAccountListData(RequestDidCollectionAccount requestModel) throws Exception{
        long did;
        // 1.校验所有数据
        if (requestModel == null ){
            throw new ServiceException(ErrorCode.ENUM_ERROR.DC00010.geteCode(), ErrorCode.ENUM_ERROR.DC00010.geteDesc());
        }

        // 校验token值
        if (StringUtils.isBlank(requestModel.token)){
            throw new ServiceException(ErrorCode.ENUM_ERROR.D00001.geteCode(), ErrorCode.ENUM_ERROR.D00001.geteDesc());
        }

        // 校验用户是否登录
        did = HodgepodgeMethod.checkIsLogin(requestModel.token);

        return did;

    }



    /**
     * @Description: 根据条件查询用户收款账号的数据-集合
     * @param requestDidCollectionAccount - 基本查询条件
     * @param did - 用户ID
     * @return com.hz.fine.master.core.model.did.DidCollectionAccountModel
     * @author yoko
     * @date 2020/5/15 17:17
     */
    public static DidCollectionAccountModel assembleDidCollectionAccountListByDid(RequestDidCollectionAccount requestDidCollectionAccount, long did){
        DidCollectionAccountModel resBean = BeanUtils.copy(requestDidCollectionAccount, DidCollectionAccountModel.class);
        resBean.setDid(did);
        return resBean;
    }


    /**
     * @Description: 根据条件查询用户收款账号的数据-集合
     * <p>
     *     查询小微二维码失效的收款账号
     * </p>
     * @param requestDidCollectionAccount - 基本查询条件
     * @param did - 用户ID
     * @return com.hz.fine.master.core.model.did.DidCollectionAccountModel
     * @author yoko
     * @date 2020/5/15 17:17
     */
    public static DidCollectionAccountModel assembleDidCollectionAccountListByDidAndCheck(RequestDidCollectionAccount requestDidCollectionAccount, long did, int checkStatus){
        DidCollectionAccountModel resBean = BeanUtils.copy(requestDidCollectionAccount, DidCollectionAccountModel.class);
        resBean.setDid(did);
        resBean.setCheckStatus(checkStatus);
        resBean.setCheckInfo(ServerConstant.PUBLIC_CONSTANT.CHECK_INFO);
        return resBean;
    }



    /**
     * @Description: 用户收款账号数据组装返回客户端的方法-集合
     * @param stime - 服务器的时间
     * @param sign - 签名
     * @param didCollectionAccountList - 用户收款账号列表集合
     * @param rowCount - 总行数
     * @return java.lang.String
     * @author yoko
     * @date 2019/11/25 22:45
     */
    public static String assembleDidCollectionAccountListResult(long stime, String sign, List<DidCollectionAccountModel> didCollectionAccountList, Integer rowCount){
        ResponseDidCollectionAccount dataModel = new ResponseDidCollectionAccount();
        if (didCollectionAccountList != null && didCollectionAccountList.size() > ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_ZERO){
            List<DidCollectionAccount> dataList = BeanUtils.copyList(didCollectionAccountList, DidCollectionAccount.class);
            dataModel.dataList = dataList;
        }
        if (rowCount != null){
            dataModel.rowCount = rowCount;
        }
        dataModel.setStime(stime);
        dataModel.setSign(sign);
        return JSON.toJSONString(dataModel);
    }


    /**
     * @Description: check校验数据获取用户收款账号详情时
     * @param requestModel
     * @return
     * @author yoko
     * @date 2020/05/14 15:57
     */
    public static long checkDidCollectionAccountData(RequestDidCollectionAccount requestModel) throws Exception{
        long did;
        // 1.校验所有数据
        if (requestModel == null ){
            throw new ServiceException(ErrorCode.ENUM_ERROR.DC00011.geteCode(), ErrorCode.ENUM_ERROR.DC00011.geteDesc());
        }

        if (requestModel.id == null || requestModel.id <= ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_ZERO){
            throw new ServiceException(ErrorCode.ENUM_ERROR.DC00012.geteCode(), ErrorCode.ENUM_ERROR.DC00012.geteDesc());
        }

        // 校验token值
        if (StringUtils.isBlank(requestModel.token)){
            throw new ServiceException(ErrorCode.ENUM_ERROR.D00001.geteCode(), ErrorCode.ENUM_ERROR.D00001.geteDesc());
        }

        // 校验用户是否登录
        did = HodgepodgeMethod.checkIsLogin(requestModel.token);

        return did;

    }

    /**
     * @Description: 组装根据用户以及用户收款账号的ID查询收款账号详情的查询条件
     * @param did - 用户ID
     * @param id - 收款账号的主键ID
     * @return com.hz.fine.master.core.model.did.DidCollectionAccountModel
     * @author yoko
     * @date 2020/5/18 11:41
     */
    public static DidCollectionAccountModel assembleDidCollectionAccountByDidAndId(long did, long id){
        DidCollectionAccountModel resBean = new DidCollectionAccountModel();
        resBean.setDid(did);
        resBean.setId(id);
        return resBean;
    }


    /**
     * @Description: 用户收款账号-详情的数据组装返回客户端的方法
     * @param stime - 服务器的时间
     * @param sign - 签名
     * @param didCollectionAccountModel - 用户收款账号的详情
     * @return java.lang.String
     * @author yoko
     * @date 2019/11/25 22:45
     */
    public static String assembleDidCollectionAccountDataResult(long stime, String sign, DidCollectionAccountModel didCollectionAccountModel){
        ResponseDidCollectionAccount dataModel = new ResponseDidCollectionAccount();
        if (didCollectionAccountModel != null){
            DidCollectionAccount data = BeanUtils.copy(didCollectionAccountModel, DidCollectionAccount.class);
            dataModel.dataModel = data;
        }
        dataModel.setStime(stime);
        dataModel.setSign(sign);
        return JSON.toJSONString(dataModel);
    }


    /**
     * @Description: check校验数据当用户修改收款账号基本信息的时候
     * @param requestModel
     * @return
     * @author yoko
     * @date 2020/05/14 15:57
     */
    public static long checkDidCollectionAccountUpdateBasic(RequestDidCollectionAccount requestModel) throws Exception{
        long did;
        // 1.校验所有数据
        if (requestModel == null ){
            throw new ServiceException(ErrorCode.ENUM_ERROR.DC00013.geteCode(), ErrorCode.ENUM_ERROR.DC00013.geteDesc());
        }

        if (requestModel.id == null || requestModel.id <= ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_ZERO){
            throw new ServiceException(ErrorCode.ENUM_ERROR.DC00014.geteCode(), ErrorCode.ENUM_ERROR.DC00014.geteDesc());
        }

        // 校验token值
        if (StringUtils.isBlank(requestModel.token)){
            throw new ServiceException(ErrorCode.ENUM_ERROR.D00001.geteCode(), ErrorCode.ENUM_ERROR.D00001.geteDesc());
        }

        // 校验用户是否登录
        did = HodgepodgeMethod.checkIsLogin(requestModel.token);

        return did;

    }


    /**
     * @Description: 组装修改用户收款账号的基本信息
     * @param did - 用户的ID
     * @param id - 用户收款账号的主键ID
     * @param acName - 收款账户名称：用户备注使用
     * @return
     * @author yoko
     * @date 2020/5/14 17:20
     */
    public static DidCollectionAccountModel assembleDidCollectionAccountUpdateBasic(long did, long id, String acName){
        DidCollectionAccountModel resBean = new DidCollectionAccountModel();
        resBean.setDid(did);
        resBean.setId(id);
        resBean.setAcName(acName);
        return resBean;
    }


    /**
     * @Description: check校验数据当用户更新收款款账号时
     * @param requestModel
     * @return
     * @author yoko
     * @date 2020/05/14 15:57
     */
    public static long checkDidCollectionAccountUpdateData(RequestDidCollectionAccount requestModel) throws Exception{
        long did;
        // 1.校验所有数据
        if (requestModel == null ){
            throw new ServiceException(ErrorCode.ENUM_ERROR.DC00015.geteCode(), ErrorCode.ENUM_ERROR.DC00015.geteDesc());
        }

        if (requestModel.id == null || requestModel.id <= ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_ZERO){
            throw new ServiceException(ErrorCode.ENUM_ERROR.DC00021.geteCode(), ErrorCode.ENUM_ERROR.DC00021.geteDesc());
        }

//        // 校验收款账号类型
//        if (requestModel.acType == null || requestModel.acType == 0){
//            throw new ServiceException(ErrorCode.ENUM_ERROR.DC00016.geteCode(), ErrorCode.ENUM_ERROR.DC00016.geteDesc());
//        }
//
//        // 校验收款的具体账号：类型为微信则微信账号，支付宝为支付宝账号；怕后期有其它冲突
//        if (requestModel.acType != 3){
//            if (StringUtils.isBlank(requestModel.acNum)){
//                throw new ServiceException(ErrorCode.ENUM_ERROR.DC00017.geteCode(), ErrorCode.ENUM_ERROR.DC00017.geteDesc());
//            }
//        }
//
//        if (StringUtils.isBlank(requestModel.mmQrCode)){
//            throw new ServiceException(ErrorCode.ENUM_ERROR.DC00018.geteCode(), ErrorCode.ENUM_ERROR.DC00018.geteDesc());
//        }

        // check收款人
        if (StringUtils.isBlank(requestModel.payee)){
            throw new ServiceException(ErrorCode.ENUM_ERROR.DC00019.geteCode(), ErrorCode.ENUM_ERROR.DC00019.geteDesc());
        }

//        if (requestModel.acType == 3){
//            // check银行名称/银行卡开户行
//            if (StringUtils.isBlank(requestModel.bankName)){
//                throw new ServiceException(ErrorCode.ENUM_ERROR.DC00006.geteCode(), ErrorCode.ENUM_ERROR.DC00006.geteDesc());
//            }
//        }
//
//
//        // check经营范围类型
//        if (requestModel.businessType == null || requestModel.businessType == 0){
//            throw new ServiceException(ErrorCode.ENUM_ERROR.DC00007.geteCode(), ErrorCode.ENUM_ERROR.DC00007.geteDesc());
//        }
//
//        // check小微商户二维码图片地址
//        if (requestModel.acType == 1){
//            if (StringUtils.isBlank(requestModel.wxQrCodeAds)){
//                throw new ServiceException(ErrorCode.ENUM_ERROR.DC00020.geteCode(), ErrorCode.ENUM_ERROR.DC00020.geteDesc());
//            }
//        }

        // 校验token值
        if (StringUtils.isBlank(requestModel.token)){
            throw new ServiceException(ErrorCode.ENUM_ERROR.D00001.geteCode(), ErrorCode.ENUM_ERROR.D00001.geteDesc());
        }

        // 校验用户是否登录
        did = HodgepodgeMethod.checkIsLogin(requestModel.token);

        return did;

    }


    /**
     * @Description: check校验数据当用户更新收款账号的小微二维码时
     * @param requestModel
     * @return
     * @author yoko
     * @date 2020/05/14 15:57
     */
    public static long checkDidCollectionAccountUpdateWxQrCodeData(RequestDidCollectionAccount requestModel) throws Exception{
        long did;
        // 1.校验所有数据
        if (requestModel == null ){
            throw new ServiceException(ErrorCode.ENUM_ERROR.DC00015.geteCode(), ErrorCode.ENUM_ERROR.DC00015.geteDesc());
        }
        if (requestModel.id == null || requestModel.id <= ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_ZERO){
            throw new ServiceException(ErrorCode.ENUM_ERROR.DC00027.geteCode(), ErrorCode.ENUM_ERROR.DC00027.geteDesc());
        }

        if (StringUtils.isBlank(requestModel.wxQrCodeAds)){
            throw new ServiceException(ErrorCode.ENUM_ERROR.DC00028.geteCode(), ErrorCode.ENUM_ERROR.DC00028.geteDesc());
        }

        // 校验token值
        if (StringUtils.isBlank(requestModel.token)){
            throw new ServiceException(ErrorCode.ENUM_ERROR.D00001.geteCode(), ErrorCode.ENUM_ERROR.D00001.geteDesc());
        }

        // 校验用户是否登录
        did = HodgepodgeMethod.checkIsLogin(requestModel.token);

        return did;

    }



    /**
     * @Description: 组装用户更新收款账号的信息
     * @param did - 用户的ID
     * @param requestDidCollectionAccount - 要更新的基本信息
     * @return
     * @author yoko
     * @date 2020/5/14 17:20
     */
    public static DidCollectionAccountModel assembleDidCollectionAccountUpdate(long did, RequestDidCollectionAccount requestDidCollectionAccount){
        DidCollectionAccountModel resBean = BeanUtils.copy(requestDidCollectionAccount, DidCollectionAccountModel.class);
        resBean.setDid(did);
        resBean.setCheckStatus(ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_ONE);
        resBean.setCheckInfo("用户修改了信息，需重新审核");
        return resBean;
    }


    /**
     * @Description: 组装用户更新收款账号小微二维码的信息
     * @param did - 用户的ID
     * @param requestDidCollectionAccount - 要更新的基本信息
     * @return
     * @author yoko
     * @date 2020/5/14 17:20
     */
    public static DidCollectionAccountModel assembleDidCollectionAccountUpdateWxQrCode(long did, RequestDidCollectionAccount requestDidCollectionAccount){
        DidCollectionAccountModel resBean = new DidCollectionAccountModel();
        resBean.setDid(did);
        resBean.setId(requestDidCollectionAccount.getId());
        resBean.setWxQrCodeAds(requestDidCollectionAccount.getWxQrCodeAds());
        resBean.setCheckStatus(ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_ONE);
        resBean.setCheckInfo("用户修改小微二维码，需重新审核");
        return resBean;
    }



    /**
     * @Description: check校验数据当用户更新收款款账号使用状态时
     * @param requestModel
     * @return
     * @author yoko
     * @date 2020/05/14 15:57
     */
    public static long checkDidCollectionAccountUpdateUseData(RequestDidCollectionAccount requestModel) throws Exception{
        long did;
        // 1.校验所有数据
        if (requestModel == null ){
            throw new ServiceException(ErrorCode.ENUM_ERROR.DC00022.geteCode(), ErrorCode.ENUM_ERROR.DC00022.geteDesc());
        }

        if (requestModel.id == null || requestModel.id <= ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_ZERO){
            throw new ServiceException(ErrorCode.ENUM_ERROR.DC00023.geteCode(), ErrorCode.ENUM_ERROR.DC00023.geteDesc());
        }

        if ((requestModel.useStatus == null || requestModel.useStatus <= ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_ZERO)
        && (requestModel.yn == null || requestModel.yn <= ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_ZERO)){
            throw new ServiceException(ErrorCode.ENUM_ERROR.DC00024.geteCode(), ErrorCode.ENUM_ERROR.DC00024.geteDesc());
        }

        // 校验token值
        if (StringUtils.isBlank(requestModel.token)){
            throw new ServiceException(ErrorCode.ENUM_ERROR.D00001.geteCode(), ErrorCode.ENUM_ERROR.D00001.geteDesc());
        }

        // 校验用户是否登录
        did = HodgepodgeMethod.checkIsLogin(requestModel.token);

        return did;

    }


    /**
     * @Description: 组装用户更新收款账号的使用状态
     * @param did - 用户的ID
     * @param requestDidCollectionAccount - 要更新的基本信息
     * @return
     * @author yoko
     * @date 2020/5/14 17:20
     */
    public static DidCollectionAccountModel assembleDidCollectionAccountUpdateUse(long did, RequestDidCollectionAccount requestDidCollectionAccount){
        DidCollectionAccountModel resBean = BeanUtils.copy(requestDidCollectionAccount, DidCollectionAccountModel.class);
        resBean.setDid(did);
        return resBean;
    }


    /**
     * @Description: check校验数据当用户发起充值时
     * @param requestModel
     * @return
     * @author yoko
     * @date 2020/05/14 15:57
     */
    public static long checkRechargeAdd(RequestDidRecharge requestModel) throws Exception{
        long did;
        // 1.校验所有数据
        if (requestModel == null ){
            throw new ServiceException(ErrorCode.ENUM_ERROR.DR00001.geteCode(), ErrorCode.ENUM_ERROR.DR00001.geteDesc());
        }

        // 校验充值金额值
        if (StringUtils.isBlank(requestModel.orderMoney)){
            throw new ServiceException(ErrorCode.ENUM_ERROR.DR00002.geteCode(), ErrorCode.ENUM_ERROR.DR00002.geteDesc());
        }

        // 校验token值
        if (StringUtils.isBlank(requestModel.token)){
            throw new ServiceException(ErrorCode.ENUM_ERROR.D00001.geteCode(), ErrorCode.ENUM_ERROR.D00001.geteDesc());
        }

        // 校验用户是否登录
        did = HodgepodgeMethod.checkIsLogin(requestModel.token);

        return did;

    }

    /**
     * @Description: 组装查询策略数据条件的方法
     * @return com.pf.play.rule.core.model.strategy.StrategyModel
     * @author yoko
     * @date 2020/5/19 17:12
     */
    public static StrategyModel assembleStrategyQuery(int stgType){
        StrategyModel resBean = new StrategyModel();
        resBean.setStgType(stgType);
        return resBean;
    }


    /**
     * @Description: 校验策略类型数据
     * @return void
     * @author yoko
     * @date 2019/12/2 14:35
     */
    public static void checkStrategyByMoney(StrategyModel strategyModel) throws Exception{
        if (strategyModel == null){
            throw new ServiceException(ErrorCode.ENUM_ERROR.S00001.geteCode(), ErrorCode.ENUM_ERROR.S00001.geteDesc());
        }
    }

    /**
     * @Description: check校验充值金额是否与部署策略里面的金额列表匹配
     * @param strategyDataList - 策略中的充值金额列表
     * @param orderMoney - 用户充值的金额
     * @return void
     * @author yoko
     * @date 2020/5/19 19:21
     */
    public static long checkRechargeMoney(List<StrategyData> strategyDataList, String orderMoney) throws Exception{
        long moneyId = 0;
        for (StrategyData dataModel : strategyDataList){
            if (dataModel.getStgValue().equals(orderMoney)){
                moneyId = dataModel.getId();
            }
        }
        if (moneyId == 0){
            throw new ServiceException(ErrorCode.ENUM_ERROR.DR00003.geteCode(), ErrorCode.ENUM_ERROR.DR00003.geteDesc());
        }
        return moneyId;

    }

    /**
     * @Description: 组装查询手机卡的查询条件
     * @param useStatus - 使用状态:1初始化有效正常使用，2无效暂停使用
     * @return 
     * @author yoko
     * @date 2020/5/20 11:07 
    */
    public static MobileCardModel assembleMobileCardQueryByUseStatus(int useStatus){
        MobileCardModel resBean = new MobileCardModel();
        resBean.setUseStatus(useStatus);
        return resBean;
    }

    /**
     * @Description: check校验手机卡的数据是否为空
     * @param mobileCardList
     * @return
     * @author yoko
     * @date 2020/5/20 11:10
    */
    public static void checkMobileCardDataIsNull(List<MobileCardModel> mobileCardList) throws Exception{
        if (mobileCardList == null || mobileCardList.size() <= ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_ZERO){
            throw new ServiceException(ErrorCode.ENUM_ERROR.M00001.geteCode(), ErrorCode.ENUM_ERROR.M00001.geteDesc());
        }
    }


    /**
     * @Description: 校验策略类型数据:银行工作日期
     * @return void
     * @author yoko
     * @date 2019/12/2 14:35
     */
    public static void checkStrategyByBankWork(StrategyModel strategyModel) throws Exception{
        if (strategyModel == null){
            throw new ServiceException(ErrorCode.ENUM_ERROR.S00002.geteCode(), ErrorCode.ENUM_ERROR.S00002.geteDesc());
        }
        if (StringUtils.isBlank(strategyModel.getStgValue())){
            throw new ServiceException(ErrorCode.ENUM_ERROR.S00003.geteCode(), ErrorCode.ENUM_ERROR.S00003.geteDesc());
        }
    }

    public static BankModel assembleBankQuery(List<MobileCardModel> mobileCardList, String bankWorkTime){
        BankModel resBean = new BankModel();
        List<Long> mobileCardIdList = new ArrayList<>();
        for (MobileCardModel mobileCardModel : mobileCardList){
            mobileCardIdList.add(mobileCardModel.getId());
        }
        resBean.setMobileCardIdList(mobileCardIdList);

        // 获得是白天银行上班的工作时间还是银行休息时间：开启类型：1白天，2晚上，3两者都支持；银行卡是白天跑还是晚上跑数据
        int openType = getOpenTypeByBankWork(bankWorkTime);
        List<Integer> openTypeList = new ArrayList<>();
        openTypeList.add(openType);
        openTypeList.add(ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_THREE);
        resBean.setOpenTypeList(openTypeList);
        resBean.setDaySwitch(ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_ONE);
        resBean.setMonthSwitch(ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_ONE);
        resBean.setTotalSwitch(ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_ONE);
        resBean.setUseStatus(ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_ONE);
        return resBean;
    }

    public static int getOpenTypeByBankWork(String bankWorkTime){
        int num = 0;
        String[] str = bankWorkTime.split("-");
        boolean flag = DateUtil.isBelong(str[0], str[1]);
        if (flag){
            num = 1;
        }else {
            num = 2;
        }
        return num;
    }

    /**
     * @Description: check校验银行卡数据
     * @param bankList - 银行卡数据
     * @return
     * @author yoko
     * @date 2020/5/20 14:15
    */
    public static void checkBankListData(List<BankModel> bankList) throws Exception{
        if (bankList == null || bankList.size() <= ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_ZERO){
            throw new ServiceException(ErrorCode.ENUM_ERROR.B00001.geteCode(), ErrorCode.ENUM_ERROR.B00001.geteDesc());
        }
    }


    /**
     * @Description: 校验策略类型数据
     * @return void
     * @author yoko
     * @date 2019/12/2 14:35
     */
    public static void checkStrategyByBankLimit(StrategyModel strategyModel) throws Exception{
        if (strategyModel == null){
            throw new ServiceException(ErrorCode.ENUM_ERROR.S00004.geteCode(), ErrorCode.ENUM_ERROR.S00004.geteDesc());
        }
    }


    /**
     * @Description: 校验策略类型数据
     * @return void
     * @author yoko
     * @date 2019/12/2 14:35
     */
    public static void checkStrategyByMoneyAddSubtractList(StrategyModel strategyModel) throws Exception{
        if (strategyModel == null){
            throw new ServiceException(ErrorCode.ENUM_ERROR.S00006.geteCode(), ErrorCode.ENUM_ERROR.S00006.geteDesc());
        }
    }

    /**
     * @Description: 校验筛选出的银行卡以及金额是否有数据
     * @return void
     * @author yoko
     * @date 2019/12/2 14:35
     */
    public static void checkScreenBankData(Map<String, Object> map) throws Exception{
        if (map == null){
            throw new ServiceException(ErrorCode.ENUM_ERROR.DR00004.geteCode(), ErrorCode.ENUM_ERROR.DR00004.geteDesc());
        }
    }

    /**
     * @Description: 校验筛选出的具体金额是否有值
     * @return void
     * @author yoko
     * @date 2019/12/2 14:35
     */
    public static void checkScreenBankMoneyData(String money) throws Exception{
        if (StringUtils.isBlank(money)){
            throw new ServiceException(ErrorCode.ENUM_ERROR.DR00015.geteCode(), ErrorCode.ENUM_ERROR.DR00015.geteDesc());
        }
    }

    /**
     * @Description: 校验策略类型数据
     * @return void
     * @author yoko
     * @date 2019/12/2 14:35
     */
    public static void checkStrategyInvalidTime(StrategyModel strategyModel) throws Exception{
        if (strategyModel == null){
            throw new ServiceException(ErrorCode.ENUM_ERROR.S00007.geteCode(), ErrorCode.ENUM_ERROR.S00007.geteDesc());
        }
    }


    /**
     * @Description: 组装用户充值记录的方法
     * @param map - 筛选出来的银行卡以及具体分配的金额信息
     * @param did - 用户ID
     * @param orderNo - 订单号
     * @param moneyId - 策略里面的金额的主键ID
     * @param orderMoney - 订单金额
     * @param invalid - 策略里面的失效时间多少分钟
     * @return com.hz.fine.master.core.model.did.DidRechargeModel
     * @author yoko
     * @date 2020/5/21 13:52
     */
    public static DidRechargeModel assembleDidRechargeAdd(Map<String, Object> map, long did, String orderNo, long moneyId, String orderMoney, int invalid){
        DidRechargeModel resBean = new DidRechargeModel();
        BankModel bankModel = (BankModel) map.get("bankModel");
        String distributionMoney = map.get("distributionMoney").toString();
        resBean.setDid(did);
        resBean.setOrderNo(orderNo);
        resBean.setMoneyId(moneyId);
        resBean.setOrderMoney(orderMoney);
        resBean.setDistributionMoney(distributionMoney);
        resBean.setBankId(bankModel.getId());

        // 订单失效时间
        String invalidTime = DateUtil.addDateMinute(invalid);
        resBean.setInvalidTime(invalidTime);

        resBean.setCurday(DateUtil.getDayNumber(new Date()));
        resBean.setCurhour(DateUtil.getHour(new Date()));
        resBean.setCurminute(DateUtil.getCurminute(new Date()));
        return resBean;
    }



    /**
     * @Description: 用户充值数据组装返回客户端的方法
     * @param stime - 服务器的时间
     * @param sign - 签名
     * @param bankModel - 银行卡信息
     * @param orderNo - 订单号
     * @param orderMoney - 订单充值金额
     * @param distributionMoney - 订单实际分配金额
     * @param invalidTime - 失效时间
     * @return java.lang.String
     * @author yoko
     * @date 2019/11/25 22:45
     */
    public static String assembleDidRechargeAddDataResult(long stime, String sign, BankModel bankModel, String orderNo, String orderMoney, String distributionMoney, String invalidTime){
        ResponseDidRecharge dataModel = new ResponseDidRecharge();
        RechargeInfo data = new RechargeInfo();
        if (bankModel != null){
            data = BeanUtils.copy(bankModel, RechargeInfo.class);
            dataModel.recharge = data;
        }
        data.orderNo = orderNo;
        data.orderMoney = orderMoney;
        data.distributionMoney = distributionMoney;
        data.invalidTime = invalidTime;
        data.depositor = "";
        data.depositMoney = "";
        data.depositTime = "";
        data.lastNum = "";
        dataModel.setStime(stime);
        dataModel.setSign(sign);
        return JSON.toJSONString(dataModel);
    }

    /**
     * @Description: check校验用户是否有未处理完毕的订单
     * @param did - 用户ID
     * @return
     * @author yoko
     * @date 2020/5/21 15:38
    */
    public static String checkDidOrderByRedis(long did) throws Exception{

        String strKeyCache = CachedKeyUtils.getCacheKey(CacheKey.LOCK_DID_ORDER_INVALID_TIME, did);
        String strCache = (String) ComponentUtil.redisService.get(strKeyCache);
        if (!StringUtils.isBlank(strCache)) {
            // 说明还有充值订单未处理
//            throw new ServiceException(ErrorCode.ENUM_ERROR.DR00005.geteCode(), ErrorCode.ENUM_ERROR.DR00005.geteDesc());
            return strCache;
        }else{
            return null;
        }
    }

    /**
     * @Description: 组装查询充值订单信息的查询条件
     * @param did - 用户ID
     * @param orderStatus - 订单状态：-1申诉状态（被申诉），1初始化，2超时/失败，3成功
     * @param workType - 存款数据录入状态（存款人，存款人时间，尾号）：1初始化，2录入完毕
     * @return com.hz.fine.master.core.model.did.DidRechargeModel
     * @author yoko
     * @date 2020/7/4 16:57
     */
    public static DidRechargeModel assembleDidRechargeQueryByWorkType(long did, int orderStatus, int workType){
        DidRechargeModel resBean = new DidRechargeModel();
        resBean.setDid(did);
        resBean.setOrderStatus(orderStatus);
        resBean.setWorkType(workType);
        return resBean;
    }


    /**
     * @Description: check校验数据当用户上传转账图片凭证
     * @param requestModel
     * @return
     * @author yoko
     * @date 2020/05/14 15:57
     */
    public static long checkLoadPicture(RequestDidRecharge requestModel) throws Exception{
        long did;
        // 1.校验所有数据
        if (requestModel == null ){
            throw new ServiceException(ErrorCode.ENUM_ERROR.DR00006.geteCode(), ErrorCode.ENUM_ERROR.DR00006.geteDesc());
        }

        // 校验订单号值
        if (StringUtils.isBlank(requestModel.orderNo)){
            throw new ServiceException(ErrorCode.ENUM_ERROR.DR00007.geteCode(), ErrorCode.ENUM_ERROR.DR00007.geteDesc());
        }

        // 校验转账图片凭证值
        if (StringUtils.isBlank(requestModel.pictureAds)){
            throw new ServiceException(ErrorCode.ENUM_ERROR.DR00008.geteCode(), ErrorCode.ENUM_ERROR.DR00008.geteDesc());
        }

        // 校验用户是否登录
        did = HodgepodgeMethod.checkIsLogin(requestModel.token);

        return did;

    }

    /**
     * @Description: 组装更新用户上传转账图片凭证的方法-申诉
     * @param requestModel - 用户的银行转账图片凭证
     * @param did - 用户ID
     * @return
     * @author yoko
     * @date 2020/5/21 15:57
    */
    public static DidRechargeModel assembleLoadPictureAndAppealUpdate(RequestDidRecharge requestModel, long did){
        DidRechargeModel resBean = new DidRechargeModel();
        resBean.setDid(did);
        resBean.setOrderNo(requestModel.orderNo);
        resBean.setPictureAds(requestModel.pictureAds);
        resBean.setOrderStatus(-1);
        resBean.setAppealStatus(2);
        return resBean;
    }


    /**
     * @Description: check校验数据获取用户充值订单记录集合时
     * @param requestModel
     * @return
     * @author yoko
     * @date 2020/05/14 15:57
     */
    public static long checkDidRechargeListData(RequestDidRecharge requestModel) throws Exception{
        long did;
        // 1.校验所有数据
        if (requestModel == null ){
            throw new ServiceException(ErrorCode.ENUM_ERROR.DR00009.geteCode(), ErrorCode.ENUM_ERROR.DR00009.geteDesc());
        }

        // 校验token值
        if (StringUtils.isBlank(requestModel.token)){
            throw new ServiceException(ErrorCode.ENUM_ERROR.D00001.geteCode(), ErrorCode.ENUM_ERROR.D00001.geteDesc());
        }

        // 校验用户是否登录
        did = HodgepodgeMethod.checkIsLogin(requestModel.token);

        return did;

    }


    /**
     * @Description: 根据条件查询用户充值订单记录的数据-集合
     * @param requestModel - 基本查询条件
     * @param did - 用户ID
     * @return com.hz.fine.master.core.model.did.DidCollectionAccountModel
     * @author yoko
     * @date 2020/5/15 17:17
     */
    public static DidRechargeModel assembleDidRechargeListByDid(RequestDidRecharge requestModel, long did){
        DidRechargeModel resBean = BeanUtils.copy(requestModel, DidRechargeModel.class);
        resBean.setDid(did);
        return resBean;
    }


    /**
     * @Description: 用户充值记录数据组装返回客户端的方法-集合
     * @param stime - 服务器的时间
     * @param sign - 签名
     * @param didRechargeList - 用户充值记录集合
     * @param rowCount - 总行数
     * @return java.lang.String
     * @author yoko
     * @date 2019/11/25 22:45
     */
    public static String assembleDidRechargeModelListResult(long stime, String sign, List<DidRechargeModel> didRechargeList, Integer rowCount){
        ResponseDidRecharge dataModel = new ResponseDidRecharge();
        if (didRechargeList != null && didRechargeList.size() > ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_ZERO){
            List<DidRecharge> dataList = BeanUtils.copyList(didRechargeList, DidRecharge.class);
            dataModel.dataList = dataList;
        }
        if (rowCount != null){
            dataModel.rowCount = rowCount;
        }
        dataModel.setStime(stime);
        dataModel.setSign(sign);
        return JSON.toJSONString(dataModel);
    }


    /**
     * @Description: check校验数据获取用户充值订单记录详情时
     * @param requestModel
     * @return
     * @author yoko
     * @date 2020/05/14 15:57
     */
    public static long checkDidRechargeData(RequestDidRecharge requestModel) throws Exception{
        long did;
        // 1.校验所有数据
        if (requestModel == null ){
            throw new ServiceException(ErrorCode.ENUM_ERROR.DR00010.geteCode(), ErrorCode.ENUM_ERROR.DR00010.geteDesc());
        }

        if (StringUtils.isBlank(requestModel.orderNo)){
            throw new ServiceException(ErrorCode.ENUM_ERROR.DR00011.geteCode(), ErrorCode.ENUM_ERROR.DR00011.geteDesc());
        }

        // 校验token值
        if (StringUtils.isBlank(requestModel.token)){
            throw new ServiceException(ErrorCode.ENUM_ERROR.D00001.geteCode(), ErrorCode.ENUM_ERROR.D00001.geteDesc());
        }

        // 校验用户是否登录
        did = HodgepodgeMethod.checkIsLogin(requestModel.token);

        return did;

    }


    /**
     * @Description: 组装根据用户以及充值订单号查询充值订单的详情的查询条件
     * @param did - 用户ID
     * @param orderNo - 订单号
     * @return com.hz.fine.master.core.model.did.DidRechargeModel
     * @author yoko
     * @date 2020/5/18 11:41
     */
    public static DidRechargeModel assembleDidRechargeByDidAndOrderNo(long did, String orderNo){
        DidRechargeModel resBean = new DidRechargeModel();
        resBean.setDid(did);
        resBean.setOrderNo(orderNo);
        return resBean;
    }


    /**
     * @Description: 用户充值订单记录-详情的数据组装返回客户端的方法
     * @param stime - 服务器的时间
     * @param sign - 签名
     * @param didRechargeModel - 用户充值订单的详情
     * @return java.lang.String
     * @author yoko
     * @date 2019/11/25 22:45
     */
    public static String assembleDidRechargeDataResult(long stime, String sign, DidRechargeModel didRechargeModel){
        ResponseDidRecharge dataModel = new ResponseDidRecharge();
        if (didRechargeModel != null){
            DidRecharge data = BeanUtils.copy(didRechargeModel, DidRecharge.class);
            dataModel.dataModel = data;
        }
        dataModel.setStime(stime);
        dataModel.setSign(sign);
        return JSON.toJSONString(dataModel);
    }



    /**
     * @Description: check校验数据获取用户奖励记录集合时
     * @param requestModel
     * @return
     * @author yoko
     * @date 2020/05/14 15:57
     */
    public static long checkDidRewardListData(RequestReward requestModel) throws Exception{
        long did;
        // 1.校验所有数据
        if (requestModel == null ){
            throw new ServiceException(ErrorCode.ENUM_ERROR.R00001.geteCode(), ErrorCode.ENUM_ERROR.R00001.geteDesc());
        }

        // 校验token值
        if (StringUtils.isBlank(requestModel.token)){
            throw new ServiceException(ErrorCode.ENUM_ERROR.D00001.geteCode(), ErrorCode.ENUM_ERROR.D00001.geteDesc());
        }

        // 校验用户是否登录
        did = HodgepodgeMethod.checkIsLogin(requestModel.token);

        return did;

    }


    /**
     * @Description: 根据条件查询用户奖励纪录的数据-集合
     * @param requestModel - 基本查询条件
     * @param did - 用户ID
     * @return com.hz.fine.master.core.model.did.DidCollectionAccountModel
     * @author yoko
     * @date 2020/5/15 17:17
     */
    public static DidRewardModel assembleDidRewardListByDid(RequestReward requestModel, long did){
        DidRewardModel resBean = BeanUtils.copy(requestModel, DidRewardModel.class);
        resBean.setDid(did);
        return resBean;
    }


    /**
     * @Description: 用户奖励记录数据组装返回客户端的方法-集合
     * @param stime - 服务器的时间
     * @param sign - 签名
     * @param didRechargeList - 用户奖励记录集合
     * @param rowCount - 总行数
     * @return java.lang.String
     * @author yoko
     * @date 2019/11/25 22:45
     */
    public static String assembleDidRewardListResult(long stime, String sign, List<DidRewardModel> didRechargeList, Integer rowCount){
        ResponseDidReward dataModel = new ResponseDidReward();
        if (didRechargeList != null && didRechargeList.size() > ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_ZERO){
            List<DidReward> dataList = BeanUtils.copyList(didRechargeList, DidReward.class);
            dataModel.dataList = dataList;
        }
        if (rowCount != null){
            dataModel.rowCount = rowCount;
        }
        dataModel.setStime(stime);
        dataModel.setSign(sign);
        return JSON.toJSONString(dataModel);
    }



    /**
     * @Description: 根据条件查询用户分享奖励纪录的数据-集合
     * @param requestModel - 基本查询条件
     * @param did - 用户ID
     * @return com.hz.fine.master.core.model.did.DidCollectionAccountModel
     * @author yoko
     * @date 2020/5/15 17:17
     */
    public static DidRewardModel assembleDidShareRewardListByDid(RequestReward requestModel, long did){
        DidRewardModel resBean = BeanUtils.copy(requestModel, DidRewardModel.class);
        resBean.setDid(did);
        resBean.setRewardType(3);
        return resBean;
    }


    /**
     * @Description: check校验数据获取用户分享奖励记录集合时
     * @param requestModel
     * @return
     * @author yoko
     * @date 2020/05/14 15:57
     */
    public static long checkDidShareRewardListData(RequestReward requestModel) throws Exception{
        long did;
        // 1.校验所有数据
        if (requestModel == null ){
            throw new ServiceException(ErrorCode.ENUM_ERROR.R00004.geteCode(), ErrorCode.ENUM_ERROR.R00004.geteDesc());
        }

        // 校验token值
        if (StringUtils.isBlank(requestModel.token)){
            throw new ServiceException(ErrorCode.ENUM_ERROR.D00001.geteCode(), ErrorCode.ENUM_ERROR.D00001.geteDesc());
        }

        // 校验用户是否登录
        did = HodgepodgeMethod.checkIsLogin(requestModel.token);

        return did;

    }


    /**
     * @Description: 用户分享奖励记录数据组装返回客户端的方法-集合
     * @param stime - 服务器的时间
     * @param sign - 签名
     * @param didRechargeList - 用户分享奖励记录集合
     * @param rowCount - 总行数
     * @return java.lang.String
     * @author yoko
     * @date 2019/11/25 22:45
     */
    public static String assembleDidShareRewardListResult(long stime, String sign, List<DidRewardModel> didRechargeList, Integer rowCount){
        ResponseDidReward dataModel = new ResponseDidReward();
        if (didRechargeList != null && didRechargeList.size() > ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_ZERO){
            List<DidShare> dataList = BeanUtils.copyList(didRechargeList, DidShare.class);
            dataModel.shareList = dataList;
        }
        if (rowCount != null){
            dataModel.rowCount = rowCount;
        }
        dataModel.setStime(stime);
        dataModel.setSign(sign);
        return JSON.toJSONString(dataModel);
    }



    /**
     * @Description: check校验数据获取用户奖励订单记录详情时
     * @param requestModel
     * @return
     * @author yoko
     * @date 2020/05/14 15:57
     */
    public static long checkDidRewardData(RequestReward requestModel) throws Exception{
        long did;
        // 1.校验所有数据
        if (requestModel == null ){
            throw new ServiceException(ErrorCode.ENUM_ERROR.R00002.geteCode(), ErrorCode.ENUM_ERROR.R00002.geteDesc());
        }

        if (requestModel.id == null || requestModel.id <= ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_ZERO){
            throw new ServiceException(ErrorCode.ENUM_ERROR.R00003.geteCode(), ErrorCode.ENUM_ERROR.R00003.geteDesc());
        }

        // 校验token值
        if (StringUtils.isBlank(requestModel.token)){
            throw new ServiceException(ErrorCode.ENUM_ERROR.D00001.geteCode(), ErrorCode.ENUM_ERROR.D00001.geteDesc());
        }

        // 校验用户是否登录
        did = HodgepodgeMethod.checkIsLogin(requestModel.token);

        return did;

    }


    /**
     * @Description: 组装根据用户以及奖励纪录的ID查询用户奖励纪录的详情的查询条件
     * @param did - 用户ID
     * @param id - 奖励纪录的主键ID
     * @return com.hz.fine.master.core.model.did.DidRechargeModel
     * @author yoko
     * @date 2020/5/18 11:41
     */
    public static DidRewardModel assembleDidRewardByDidAndId(long did, long id){
        DidRewardModel resBean = new DidRewardModel();
        resBean.setDid(did);
        resBean.setId(id);
        return resBean;
    }


    /**
     * @Description: 用户奖励记录-详情的数据组装返回客户端的方法
     * @param stime - 服务器的时间
     * @param sign - 签名
     * @param didRewardModel - 用户奖励纪录的详情
     * @return java.lang.String
     * @author yoko
     * @date 2019/11/25 22:45
     */
    public static String assembleDidRewardDataResult(long stime, String sign, DidRewardModel didRewardModel){
        ResponseDidReward dataModel = new ResponseDidReward();
        if (didRewardModel != null){
            DidReward data = BeanUtils.copy(didRewardModel, DidReward.class);
            dataModel.dataModel = data;
        }
        dataModel.setStime(stime);
        dataModel.setSign(sign);
        return JSON.toJSONString(dataModel);
    }



    /**
     * @Description: check校验数据当派发订单的时候
     * @param requestModel
     * @return
     * @author yoko
     * @date 2020/05/14 15:57
     */
    public static void checkOrderAdd(RequestOrder requestModel) throws Exception{
        // 1.校验所有数据
        if (requestModel == null ){
            throw new ServiceException(ErrorCode.ENUM_ERROR.OR00001.geteCode(), ErrorCode.ENUM_ERROR.OR00001.geteDesc());
        }

        // 校验金额是否为空
        if (StringUtils.isBlank(requestModel.money)){
            throw new ServiceException(ErrorCode.ENUM_ERROR.OR00002.geteCode(), ErrorCode.ENUM_ERROR.OR00002.geteDesc());
        }else {
            // 金额是否有效
            if (requestModel.money.indexOf(".") > -1){
                boolean flag = StringUtil.isNumberByMoney(requestModel.money);
                if (!flag){
                    throw new ServiceException(ErrorCode.ENUM_ERROR.OR00006.geteCode(), ErrorCode.ENUM_ERROR.OR00006.geteDesc());
                }
            }else {
                boolean flag = StringUtil.isNumer(requestModel.money);
                if (!flag){
                    throw new ServiceException(ErrorCode.ENUM_ERROR.OR00007.geteCode(), ErrorCode.ENUM_ERROR.OR00007.geteDesc());
                }
            }
        }


        // 校验支付类型为空
        if (requestModel.payType == null || requestModel.payType == 0){
            throw new ServiceException(ErrorCode.ENUM_ERROR.OR00003.geteCode(), ErrorCode.ENUM_ERROR.OR00003.geteDesc());
        }
    }



    /**
     * @Description: check校验数据当派发订单的时候：是否有可以派单的用户数据
     * @param didList
     * @return
     * @author yoko
     * @date 2020/05/14 15:57
     */
    public static void checkEffectiveDidData(List<DidModel> didList) throws Exception{
        if (didList == null || didList.size() <= 0){
            throw new ServiceException(ErrorCode.ENUM_ERROR.OR00005.geteCode(), ErrorCode.ENUM_ERROR.OR00005.geteDesc());
        }
    }


    /**
     * @Description: 组装查询可以进行派发订单的用户查询条件的方法
     * @param requestModel - 金额跟支付类型
     * @return 
     * @author yoko
     * @date 2020/5/25 14:15 
    */
    public static DidModel assembleEffectiveDid(RequestOrder requestModel){
        DidModel resBean = new DidModel();
        resBean.setBalance(requestModel.money);
        resBean.setAcType(requestModel.payType);
//        resBean.setMoney(Double.parseDouble(requestModel.money));
        BigDecimal bd = new BigDecimal(requestModel.money);
        resBean.setMoney(bd);
        return resBean;
    }


    /**
     * @Description: check校验是否筛选出收款账号
     * @param didCollectionAccountModel - 用户收款账号
     * @return
     * @author yoko
     * @date 2020/6/2 14:13
    */
    public static void checkDidCollectionAccountByAddOrder(DidCollectionAccountModel didCollectionAccountModel) throws Exception{
        if (didCollectionAccountModel == null || didCollectionAccountModel.getId() <= 0){
            throw new ServiceException(ErrorCode.ENUM_ERROR.OR00004.geteCode(), ErrorCode.ENUM_ERROR.OR00004.geteDesc());
        }
    }


    /**
     * @Description: 组装添加派单数据的方法
     * @param did -  用户ID
     * @param orderNo - 订单号
     * @param orderMoney - 订单金额
     * @param outTradeNo - 商家订单号
     * @param notifyUlr - 同步地址
     * @param didCollectionAccountModel - 用户收款账号信息
     * @return com.hz.fine.master.core.model.order.OrderModel
     * @author yoko
     * @date 2020/6/2 14:53
     */
    public static OrderModel assembleOrderByAdd(long did, String orderNo, String orderMoney, String notifyUlr, String outTradeNo,
                                                DidCollectionAccountModel didCollectionAccountModel){
        OrderModel resBean = new OrderModel();
        resBean.setDid(didCollectionAccountModel.getDid());
        resBean.setOrderNo(orderNo);
        resBean.setOrderMoney(orderMoney);
        resBean.setCollectionAccountId(didCollectionAccountModel.getId());
        resBean.setCollectionType(didCollectionAccountModel.getAcType());
        resBean.setQrCodeId(didCollectionAccountModel.getQrCodeId());
        resBean.setQrCode(didCollectionAccountModel.getDdQrCode());
        resBean.setWxNickname(didCollectionAccountModel.getPayee());
        resBean.setWxId(didCollectionAccountModel.getWxId());
        if (!StringUtils.isBlank(outTradeNo)){
            resBean.setOutTradeNo(outTradeNo);
        }
        if (!StringUtils.isBlank(notifyUlr)){
            resBean.setNotifyUrl(notifyUlr);
        }
        // 订单失效时间
        String invalidTime = DateUtil.addDateMinute(10);// 目前默认5分钟：后续可以从策略取数据
        resBean.setInvalidTime(invalidTime);
        resBean.setCurday(DateUtil.getDayNumber(new Date()));
        resBean.setCurhour(DateUtil.getHour(new Date()));
        resBean.setCurminute(DateUtil.getCurminute(new Date()));
        return resBean;
    }



    /**
     * @Description: 用户派单成功的数据组装返回客户端的方法
     * @param stime - 服务器的时间
     * @param sign - 签名
     * @param orderModel - 用户派单的详情
     * @param returnUrl - 支付完成之后自动跳转的地址
     * @param qrCodeUrl - 生成的HTML页面的地址
     * @return java.lang.String
     * @author yoko
     * @date 2019/11/25 22:45
     */
    public static String assembleOrderQrCodeDataResult(long stime, String sign, OrderModel orderModel, String returnUrl, String qrCodeUrl) throws Exception{
        ResponseOrder dataModel = new ResponseOrder();
        if (orderModel != null){
            OrderDistribution order = new OrderDistribution();
            order.orderNo = orderModel.getOrderNo();
            order.qrCode = orderModel.getQrCode();
            order.orderMoney = orderModel.getOrderMoney();
            order.invalidTime = orderModel.getInvalidTime();
            String resQrCodeUrl = "";
            if (!StringUtils.isBlank(returnUrl)){
                resQrCodeUrl = qrCodeUrl + "?" + "orderNo=" +  orderModel.getOrderNo() + "&" + "returnUrl=" + returnUrl;
            }else {
                resQrCodeUrl = qrCodeUrl + "?" + "orderNo=" +  orderModel.getOrderNo() + "&" + "returnUrl=";
            }
            order.qrCodeUrl = URLEncoder.encode(resQrCodeUrl,"UTF-8");
            dataModel.order = order;
        }
        dataModel.setStime(stime);
        dataModel.setSign(sign);
        return JSON.toJSONString(dataModel);
    }



    /**
     * @Description: 用户派单成功的数据组装返回客户端的方法
     * @param stime - 服务器的时间
     * @param sign - 签名
     * @param orderModel - 用户派单的详情
     * @return java.lang.String
     * @author yoko
     * @date 2019/11/25 22:45
     */
    public static String assembleOrderAddDataResult(long stime, String sign, OrderModel orderModel){
        ResponseOrder dataModel = new ResponseOrder();
        if (orderModel != null){
            OrderDistribution order = new OrderDistribution();
            order.orderNo = orderModel.getOrderNo();
            order.qrCode = orderModel.getQrCode();
            order.orderMoney = orderModel.getOrderMoney();
            order.invalidTime = orderModel.getInvalidTime();
            dataModel.order = order;
        }
        dataModel.setStime(stime);
        dataModel.setSign(sign);
        return JSON.toJSONString(dataModel);
    }



    /**
     * @Description: 查询派单成功的订单状态数据组装返回客户端的方法
     * @param stime - 服务器的时间
     * @param sign - 签名
     * @param orderStatus - 不等于0表示成功
     * @return java.lang.String
     * @author yoko
     * @date 2019/11/25 22:45
     */
    public static String assembleOrderStatusResult(long stime, String sign, int orderStatus){
        ResponseOrder resBean = new ResponseOrder();
        Order dataModel = new Order();
        dataModel.orderStatus = orderStatus;
        resBean.dataModel = dataModel;
        resBean.setStime(stime);
        resBean.setSign(sign);
        return JSON.toJSONString(dataModel);
    }


    /**
     * @Description: 组装查询有效的收款账号的查询条件
     * @param did - 用户ID
     * @param acType - 收款账号类型
     * @return
     * @author yoko
     * @date 2020/5/26 19:21
    */
    public static DidCollectionAccountModel assembleDidCollectionAccount(long did, int acType){
        DidCollectionAccountModel resBean = new DidCollectionAccountModel();
        resBean.setDid(did);
        resBean.setAcType(acType);
        resBean.setDaySwitch(1);
        resBean.setMonthSwitch(1);
        resBean.setTotalSwitch(1);
        resBean.setCheckStatus(3);
        resBean.setUseStatus(1);
        return resBean;
    }

    /**
     * @Description: 组装查询小微旗下店员的有效收款账号的查询条件
     * @param did - 用户ID
     * @param collectionAccountId - 用户收款账号ID
     * @return com.hz.fine.master.core.model.wx.WxClerkModel
     * @author yoko
     * @date 2020/5/26 19:44
     */
    public static WxClerkModel assembleWxClerk(long did, long collectionAccountId){
        WxClerkModel resBean = new WxClerkModel();
//        resBean.setDid(did);
        resBean.setCollectionAccountId(collectionAccountId);
        resBean.setUseStatus(1);
        return resBean;
    }

    /**
     * @Description: 组装查询小微管理的查询条件
     * @param id - 小微管理的主键ID
     * @return
     * @author yoko
     * @date 2020/6/9 10:08
    */
    public static WxModel assembleWxQuery(long id){
        WxModel resBean = new WxModel();
        resBean.setId(id);
        resBean.setUseStatus(1);
        return resBean;
    }

    /**
     * @Description: 组装更新用户金额的方法
     * @param did - 用户ID
     * @param orderMoney - 派单的具体金额
     * @return com.hz.fine.master.core.model.did.DidModel
     * @author yoko
     * @date 2020/6/9 10:47
     */
    public static DidModel assembleUpdateDidMoneyByOrder(long did, String orderMoney){
        DidModel resBean = new DidModel();
        resBean.setId(did);
        resBean.setOrderMoney(orderMoney);
        return resBean;
    }




    /**
     * @Description: check校验数据获取策略：充值金额列表
     * @param requestModel
     * @return
     * @author yoko
     * @date 2020/05/14 15:57
     */
    public static void checkStrategyMoneyListData(RequestStrategy requestModel) throws Exception{
        // 1.校验所有数据
        if (requestModel == null ){
            throw new ServiceException(ErrorCode.ENUM_ERROR.S00008.geteCode(), ErrorCode.ENUM_ERROR.S00008.geteDesc());
        }
    }



    /**
     * @Description: 策略：充值金额列表数据组装返回客户端的方法-集合
     * @param stime - 服务器的时间
     * @param sign - 签名
     * @param strategyDataList - 充值金额列表
     * @param rowCount - 总行数
     * @return java.lang.String
     * @author yoko
     * @date 2019/11/25 22:45
     */
    public static String assembleStrategyMoneyListResult(long stime, String sign,  List<StrategyData> strategyDataList, Integer rowCount){
        ResponseStrategy dataModel = new ResponseStrategy();
        if (strategyDataList != null && strategyDataList.size() > ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_ZERO){
            List<StrategyMoney> moneyList = new ArrayList<>();
            for (StrategyData strategyData : strategyDataList){
                StrategyMoney strategyMoney = new StrategyMoney();
                strategyMoney.money = strategyData.getStgValue();
                strategyMoney.rewardRatio = strategyData.getStgValueOne();
                strategyMoney.seat = strategyData.getStgValueTwo();
                moneyList.add(strategyMoney);
            }
            dataModel.moneyList = moneyList;
        }
        if (rowCount != null){
            dataModel.rowCount = rowCount;
        }
        dataModel.setStime(stime);
        dataModel.setSign(sign);
        return JSON.toJSONString(dataModel);
    }



    /**
     * @Description: check校验数据获取策略：总金额充值档次奖励列表
     * @param requestModel
     * @return
     * @author yoko
     * @date 2020/05/14 15:57
     */
    public static void checkStrategyMoneyGradeListData(RequestStrategy requestModel) throws Exception{
        // 1.校验所有数据
        if (requestModel == null ){
            throw new ServiceException(ErrorCode.ENUM_ERROR.S00009.geteCode(), ErrorCode.ENUM_ERROR.S00009.geteDesc());
        }
    }


    /**
     * @Description: 校验策略类型数据
     * @return void
     * @author yoko
     * @date 2019/12/2 14:35
     */
    public static void checkStrategyByMoneyGrade(StrategyModel strategyModel) throws Exception{
        if (strategyModel == null){
            throw new ServiceException(ErrorCode.ENUM_ERROR.S00010.geteCode(), ErrorCode.ENUM_ERROR.S00010.geteDesc());
        }
    }


    /**
     * @Description: 策略：总金额充值档次奖励列表数据组装返回客户端的方法-集合
     * @param stime - 服务器的时间
     * @param sign - 签名
     * @param strategyDataList - 总金额充值档次奖励列表
     * @param rowCount - 总行数
     * @return java.lang.String
     * @author yoko
     * @date 2019/11/25 22:45
     */
    public static String assembleStrategyMoneyGradeListResult(long stime, String sign,  List<StrategyData> strategyDataList, Integer rowCount){
        ResponseStrategy dataModel = new ResponseStrategy();
        if (strategyDataList != null && strategyDataList.size() > ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_ZERO){
            List<StrategyMoneyGrade> moneyGradeList = new ArrayList<>();
            for (StrategyData strategyData : strategyDataList){
                StrategyMoneyGrade strategyMoneyGrade = new StrategyMoneyGrade();
                strategyMoneyGrade.moneyGrade = strategyData.getStgValue();
                strategyMoneyGrade.rewardRatio = strategyData.getStgValueOne();
                strategyMoneyGrade.seat = strategyData.getStgValueTwo();
                moneyGradeList.add(strategyMoneyGrade);
            }
            dataModel.moneyGradeList = moneyGradeList;
        }
        if (rowCount != null){
            dataModel.rowCount = rowCount;
        }
        dataModel.setStime(stime);
        dataModel.setSign(sign);
        return JSON.toJSONString(dataModel);
    }


    /**
     * @Description: check校验数据获取七牛云的token
     * @param requestModel
     * @return
     * @author yoko
     * @date 2020/05/14 15:57
     */
    public static long checkStrategyQiNiuTokenData(RequestStrategy requestModel) throws Exception{
        long did;
        // 1.校验所有数据
        if (requestModel == null ){
            throw new ServiceException(ErrorCode.ENUM_ERROR.S00011.geteCode(), ErrorCode.ENUM_ERROR.S00011.geteDesc());
        }

        // 校验token值
        if (StringUtils.isBlank(requestModel.token)){
            throw new ServiceException(ErrorCode.ENUM_ERROR.D00001.geteCode(), ErrorCode.ENUM_ERROR.D00001.geteDesc());
        }

        // 校验用户是否登录
        did = HodgepodgeMethod.checkIsLogin(requestModel.token);

        return did;

    }



    /**
     * @Description: 策略：获取七牛云的token数据
     * @param stime - 服务器的时间
     * @param sign - 签名
     * @param qiNiuToken - 七牛的token
     * @return java.lang.String
     * @author yoko
     * @date 2019/11/25 22:45
     */
    public static String assembleStrategyQiNiuResult(long stime, String sign, String qiNiuToken){
        ResponseStrategy dataModel = new ResponseStrategy();

        QiNiu qiNiu = new QiNiu();
        qiNiu.key = UUID.randomUUID().toString().replaceAll("\\-", "");
        qiNiu.token = qiNiuToken;
        dataModel.qiNiu = qiNiu;
        dataModel.setStime(stime);
        dataModel.setSign(sign);
        return JSON.toJSONString(dataModel);
    }


    /**
     * @Description: 策略：七牛云空间图片上传之后图片url组装返回给前端
     * @param stime - 服务器的时间
     * @param sign - 签名
     * @param httpUrl - 图片地址
     * @return java.lang.String
     * @author yoko
     * @date 2019/11/25 22:45
     */
    public static String assembleStrategyQiNiuUploadResult(long stime, String sign, String httpUrl){
        ResponseStrategy dataModel = new ResponseStrategy();
        QiNiu qiNiu = new QiNiu();
        qiNiu.url = httpUrl;
        dataModel.qiNiu = qiNiu;
        dataModel.setStime(stime);
        dataModel.setSign(sign);
        return JSON.toJSONString(dataModel);
    }



    /**
     * @Description: check校验数据获取分享链接时
     * @param requestModel
     * @return
     * @author yoko
     * @date 2020/05/14 15:57
     */
    public static long checkStrategyShareData(RequestStrategy requestModel) throws Exception{
        long did = 0;
        // 1.校验所有数据
        if (requestModel == null ){
            throw new ServiceException(ErrorCode.ENUM_ERROR.S00012.geteCode(), ErrorCode.ENUM_ERROR.S00012.geteDesc());
        }

        // 校验token值
        if (!StringUtils.isBlank(requestModel.token)){
            did = HodgepodgeMethod.getDidByToken(requestModel.token);
        }

        return did;

    }


    /**
     * @Description: 校验策略类型数据
     * @return void
     * @author yoko
     * @date 2019/12/2 14:35
     */
    public static void checkStrategyByShare(StrategyModel strategyModel) throws Exception{
        if (strategyModel == null){
            throw new ServiceException(ErrorCode.ENUM_ERROR.S00013.geteCode(), ErrorCode.ENUM_ERROR.S00013.geteDesc());
        }
    }

    /**
     * @Description: 校验策略类型数据:分享状态的开关
     * @return void
     * @author yoko
     * @date 2019/12/2 14:35
     */
    public static void checkStrategyByShareSwitch(StrategyModel strategyModel) throws Exception{
        if (strategyModel == null){
            throw new ServiceException(ErrorCode.ENUM_ERROR.S00019.geteCode(), ErrorCode.ENUM_ERROR.S00019.geteDesc());
        }
    }
    
    /**
     * @Description: 组装根据DID查询用户信息的方法
     * @param did - 用户ID
     * @return 
     * @author yoko
     * @date 2020/5/28 13:57
    */
    public static DidModel assembleDidQuery(long did){
        DidModel resBean = new DidModel();
        resBean.setId(did);
        return resBean;
    }


    /**
     * @Description: 策略：获取分享链接数据
     * @param stime - 服务器的时间
     * @param sign - 签名
     * @param shareAddres - 分享的连接地址
     * @param icode - 推广码
     * @return java.lang.String
     * @author yoko
     * @date 2019/11/25 22:45
     */
    public static String assembleShareResult(long stime, String sign, String shareAddres, String icode){
        ResponseStrategy dataModel = new ResponseStrategy();
        StrategyShare share = new StrategyShare();
        if (!StringUtils.isBlank(icode)){
            share.shareAddress = shareAddres + icode;
            share.icode = icode;
        }else {
            share.shareAddress = shareAddres;
        }
        dataModel.share = share;
        dataModel.setStime(stime);
        dataModel.setSign(sign);
        return JSON.toJSONString(dataModel);
    }


    /**
     * @Description: 策略：分享状态的开关数据
     * @param stime - 服务器的时间
     * @param sign - 签名
     * @param shareSwitch - 分享状态的开关：1表示打开，2表示关闭
     * @return java.lang.String
     * @author yoko
     * @date 2019/11/25 22:45
     */
    public static String assembleShareSwitchResult(long stime, String sign, int shareSwitch){
        ResponseStrategy dataModel = new ResponseStrategy();
        StrategyShare share = new StrategyShare();
        share.shareSwitch = shareSwitch;
        dataModel.share = share;
        dataModel.setSign(sign);
        dataModel.setStime(stime);
        return JSON.toJSONString(dataModel);
    }



    /**
     * @Description: check校验数据获取用户账号基本信息时
     * @param requestModel
     * @return
     * @author yoko
     * @date 2020/05/14 15:57
     */
    public static long checkDidGetData(RequestDid requestModel) throws Exception{
        long did;
        // 1.校验所有数据
        if (requestModel == null ){
            throw new ServiceException(ErrorCode.ENUM_ERROR.D00022.geteCode(), ErrorCode.ENUM_ERROR.D00022.geteDesc());
        }

        // 校验token值
        if (StringUtils.isBlank(requestModel.token)){
            throw new ServiceException(ErrorCode.ENUM_ERROR.D00001.geteCode(), ErrorCode.ENUM_ERROR.D00001.geteDesc());
        }

        // 校验用户是否登录
        did = HodgepodgeMethod.checkIsLogin(requestModel.token);

        return did;

    }

    /**
     * @Description: 组装根据did查询用户信息的查询条件
     * @param did - 用户账号ID
     * @return
     * @author yoko
     * @date 2020/5/15 16:10
     */
    public static DidModel assembleDidQueryByDid(long did){
        DidModel resBean = new DidModel();
        resBean.setId(did);
        return resBean;
    }


    /**
     * @Description: check校验根据用户did查询用户的数据是否为空
     * @param didModel - 用户具体收款账号信息
     * @return
     * @author yoko
     * @date 2020/5/14 17:25
     */
    public static void checkDidData(DidModel didModel) throws Exception{
        if (didModel == null || didModel.getId() <= ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_ZERO){
            throw new ServiceException(ErrorCode.ENUM_ERROR.D00023.geteCode(), ErrorCode.ENUM_ERROR.D00023.geteDesc());
        }
    }

    /**
     * @Description: 组装查询用户今日收益的查询条件
     * @param did - 用户ID
     * @return
     * @author yoko
     * @date 2020/5/29 11:44
    */
    public static DidRewardModel assembleDidRewardTodayProfit(long did){
        DidRewardModel resBean = new DidRewardModel();
        resBean.setDid(did);
        List<Integer> rewardTypeList = new ArrayList<>();
        rewardTypeList.add(1);
        rewardTypeList.add(2);
        rewardTypeList.add(3);
        rewardTypeList.add(4);// 裂变
        rewardTypeList.add(5);// 团队奖励
        resBean.setCurday(DateUtil.getDayNumber(new Date()));
        return resBean;
    }
    
    /**
     * @Description: 组装查询今天日兑换：今日派发订单成功的查询条件
     * @param did - 用户ID
     * @return 
     * @author yoko
     * @date 2020/5/29 14:06 
    */
    public static OrderModel assembleOrderByTodayExchange(long did){
        OrderModel resBean = new OrderModel();
        resBean.setDid(did);
        resBean.setOrderStatus(4);
        resBean.setCurday(DateUtil.getDayNumber(new Date()));
        return resBean;
    }


    /**
     * @Description: 获取用户账号基本信息的数据组装返回客户端
     * @param stime - 服务器的时间
     * @param sign - 签名
     * @param didModel - 用户基本信息
     * @param todayProfit - 今日收益
     * @param todayExchange - 今日兑换
     * @return java.lang.String
     * @author yoko
     * @date 2019/11/25 22:45
     */
    public static String assembleDidBasicDataResult(long stime, String sign, DidModel didModel, String todayProfit, String todayExchange){
        ResponseDid dataModel = new ResponseDid();
        if (didModel != null && didModel.getId() > 0){

            DidBasic didBasic = BeanUtils.copy(didModel, DidBasic.class);
            if (StringUtils.isBlank(didModel.getBalance())){
                didBasic.balance = "0.00";
            }
            if (StringUtils.isBlank(didModel.getBalance())){
                didBasic.lockMoney = "0.00";
            }else {
                if (didModel.getBalance().equals("0")){
                    didBasic.lockMoney = "0.00";
                }
            }
            if (StringUtils.isBlank(didModel.getTotalMoney())){
                didBasic.totalMoney = "0.00";
            }
            if (StringUtils.isBlank(didModel.getTotalProfit())){
                didBasic.totalProfit = "0.00";
            }
            if (didModel.getTotalDirectNum() == null || didModel.getTotalDirectNum() == 0){
                didBasic.totalDirectNum = 0;
            }
            if (StringUtils.isBlank(didModel.getTotalDirectProfit())){
                didBasic.totalDirectProfit = "0.00";
            }
            if (StringUtils.isBlank(didModel.getTotalTeamProfit())){
                didBasic.totalTeamProfit = "0.00";
            }
            if (StringUtils.isBlank(didModel.getTotalRechargeProfit())){
                didBasic.totalRechargeProfit = "0.00";
            }
            if (StringUtils.isBlank(didModel.getTotalGradeProfit())){
                didBasic.totalGradeProfit = "0.00";
            }
            didBasic.todayProfit = todayProfit;
            didBasic.todayExchange = todayExchange;


            dataModel.dataModel = didBasic;
        }
        dataModel.setStime(stime);
        dataModel.setSign(sign);
        return JSON.toJSONString(dataModel);
    }


    /**
     * @Description: check校验数据获取用户派单信息时-集合
     * @param requestModel
     * @return
     * @author yoko
     * @date 2020/05/14 15:57
     */
    public static long checkOrderListData(RequestOrder requestModel) throws Exception{
        long did;
        // 1.校验所有数据
        if (requestModel == null ){
            throw new ServiceException(ErrorCode.ENUM_ERROR.OR00010.geteCode(), ErrorCode.ENUM_ERROR.OR00010.geteDesc());
        }

        // 校验token值
        if (StringUtils.isBlank(requestModel.token)){
            throw new ServiceException(ErrorCode.ENUM_ERROR.D00001.geteCode(), ErrorCode.ENUM_ERROR.D00001.geteDesc());
        }

        // 校验用户是否登录
        did = HodgepodgeMethod.checkIsLogin(requestModel.token);

        return did;

    }



    /**
     * @Description: 根据条件查询用户派单的数据-集合
     * @param requestModel - 基本查询条件
     * @param did - 用户ID
     * @return com.hz.fine.master.core.model.did.DidCollectionAccountModel
     * @author yoko
     * @date 2020/5/15 17:17
     */
    public static OrderModel assembleOrderListByDid(RequestOrder requestModel, long did){
        OrderModel resBean = BeanUtils.copy(requestModel, OrderModel.class);
        resBean.setDid(did);
        return resBean;
    }


    /**
     * @Description: 用户派单数据组装返回客户端的方法-集合
     * @param stime - 服务器的时间
     * @param sign - 签名
     * @param orderList - 用户派单列表集合
     * @param rowCount - 总行数
     * @return java.lang.String
     * @author yoko
     * @date 2019/11/25 22:45
     */
    public static String assembleOrderListResult(long stime, String sign, List<OrderModel> orderList, Integer rowCount){
        ResponseOrder dataModel = new ResponseOrder();
        if (orderList != null && orderList.size() > ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_ZERO){
            List<Order> dataList = BeanUtils.copyList(orderList, Order.class);
            dataModel.dataList = dataList;
        }
        if (rowCount != null){
            dataModel.rowCount = rowCount;
        }
        dataModel.setStime(stime);
        dataModel.setSign(sign);
        return JSON.toJSONString(dataModel);
    }


    /**
     * @Description: check校验数据获取用户派单详情时
     * @param requestModel
     * @return
     * @author yoko
     * @date 2020/05/14 15:57
     */
    public static long checkOrderData(RequestOrder requestModel) throws Exception{
        long did;
        // 1.校验所有数据
        if (requestModel == null ){
            throw new ServiceException(ErrorCode.ENUM_ERROR.OR00011.geteCode(), ErrorCode.ENUM_ERROR.OR00011.geteDesc());
        }

        if (StringUtils.isBlank(requestModel.orderNo)){
            throw new ServiceException(ErrorCode.ENUM_ERROR.OR00012.geteCode(), ErrorCode.ENUM_ERROR.OR00012.geteDesc());
        }

        // 校验token值
        if (StringUtils.isBlank(requestModel.token)){
            throw new ServiceException(ErrorCode.ENUM_ERROR.D00001.geteCode(), ErrorCode.ENUM_ERROR.D00001.geteDesc());
        }

        // 校验用户是否登录
        did = HodgepodgeMethod.checkIsLogin(requestModel.token);

        return did;

    }


    /**
     * @Description: 组装根据用户以及派单的订单号查询派单详情的查询条件
     * @param did - 用户ID
     * @param orderNo - 派单的订单号
     * @return com.hz.fine.master.core.model.did.DidCollectionAccountModel
     * @author yoko
     * @date 2020/5/18 11:41
     */
    public static OrderModel assembleOrderDataByOrderNo(long did, String orderNo){
        OrderModel resBean = new OrderModel();
        resBean.setDid(did);
        resBean.setOrderNo(orderNo);
        return resBean;
    }


    /**
     * @Description: 用户派单-详情的数据组装返回客户端的方法
     * @param stime - 服务器的时间
     * @param sign - 签名
     * @param orderModel - 用户派单的详情
     * @return java.lang.String
     * @author yoko
     * @date 2019/11/25 22:45
     */
    public static String assembleOrderDataResult(long stime, String sign, OrderModel orderModel){
        ResponseOrder dataModel = new ResponseOrder();
        if (orderModel != null && !StringUtils.isBlank(orderModel.getOrderNo())){
            Order data = BeanUtils.copy(orderModel, Order.class);
            dataModel.dataModel = data;
        }
        dataModel.setStime(stime);
        dataModel.setSign(sign);
        return JSON.toJSONString(dataModel);
    }


    public static String qiNiuUpload(MultipartFile image) throws Exception{
        String httpUrl = "http://gtpqn.tiaocheng-tech.com/";
        String suffix = image.getOriginalFilename().substring(image.getOriginalFilename().lastIndexOf(".") + 1);
        byte[] bytes = image.getBytes();
        String imageName = UUID.randomUUID().toString().replaceAll("\\-", "") + "." + suffix;

        QiniuCloudUtil qiniuUtil = new QiniuCloudUtil();
        String resStr = qiniuUtil.put64image(bytes, imageName);
        if (StringUtils.isBlank(resStr)){
            throw new ServiceException(ErrorCode.ENUM_ERROR.S00015.geteCode(), ErrorCode.ENUM_ERROR.S00015.geteDesc());
        }
        httpUrl = httpUrl + resStr;
        return httpUrl;
    }



    /**
     * @Description: check校验数据获取派单数据-详情-返回码的接口时
     * @param requestModel
     * @return
     * @author yoko
     * @date 2020/05/14 15:57
     */
    public static void checkOrderByQrCodeData(RequestOrder requestModel) throws Exception{
        // 1.校验所有数据
        if (requestModel == null ){
            throw new ServiceException(ErrorCode.ENUM_ERROR.OR00013.geteCode(), ErrorCode.ENUM_ERROR.OR00013.geteDesc());
        }

        if (StringUtils.isBlank(requestModel.orderNo)){
            throw new ServiceException(ErrorCode.ENUM_ERROR.OR00014.geteCode(), ErrorCode.ENUM_ERROR.OR00014.geteDesc());
        }

    }


    /**
     * @Description: 组装根据派单的订单号查询派单信息
     * @param orderNo - 派单的订单号
     * @return com.hz.fine.master.core.model.did.DidCollectionAccountModel
     * @author yoko
     * @date 2020/5/18 11:41
     */
    public static OrderModel assembleOrderOrderNo(String orderNo, int didStatus){
        OrderModel resBean = new OrderModel();
        resBean.setOrderNo(orderNo);
        if (didStatus != 0){
            resBean.setDidStatus(didStatus);
        }
        return resBean;
    }



    /**
     * @Description: check校验数据当用户添加收款账号的二维码时
     * @param requestModel
     * @return
     * @author yoko
     * @date 2020/05/14 15:57
     */
    public static long checkDidCollectionAccountQrCodeAddData(RequestDidCollectionAccountQrCode requestModel) throws Exception{
        long did;
        // 1.校验所有数据
        if (requestModel == null ){
            throw new ServiceException(ErrorCode.ENUM_ERROR.Q00001.geteCode(), ErrorCode.ENUM_ERROR.Q00001.geteDesc());
        }

        // 校验收款账号ID
        if (requestModel.collectionAccountId == null || requestModel.collectionAccountId == 0){
            throw new ServiceException(ErrorCode.ENUM_ERROR.Q00002.geteCode(), ErrorCode.ENUM_ERROR.Q00002.geteDesc());
        }

        if (requestModel.dataList == null || requestModel.dataList.size() <= 0){
            throw new ServiceException(ErrorCode.ENUM_ERROR.Q00003.geteCode(), ErrorCode.ENUM_ERROR.Q00003.geteDesc());
        }else {
            for (DidCollectionAccountQrCode data : requestModel.dataList){
                if (StringUtils.isBlank(data.ddQrCode)){
                    throw new ServiceException(ErrorCode.ENUM_ERROR.Q00004.geteCode(), ErrorCode.ENUM_ERROR.Q00004.geteDesc());
                }
                if(data.dataType == null || data.dataType == 0 ){
                    throw new ServiceException(ErrorCode.ENUM_ERROR.Q00005.geteCode(), ErrorCode.ENUM_ERROR.Q00005.geteDesc());
                }
            }
        }

        // 校验token值
        if (StringUtils.isBlank(requestModel.token)){
            throw new ServiceException(ErrorCode.ENUM_ERROR.D00001.geteCode(), ErrorCode.ENUM_ERROR.D00001.geteDesc());
        }

        // 校验用户是否登录
        did = HodgepodgeMethod.checkIsLogin(requestModel.token);

        return did;

    }


    /**
     * @Description: check校验数据当用户添加收款账号时-合二为一
     * @param requestModel
     * @return
     * @author yoko
     * @date 2020/05/14 15:57
     */
    public static long checkDidCollectionAccountAllAddData(RequestDidCollectionAccountAll requestModel) throws Exception{
        long did;
        // 1.校验所有数据
        if (requestModel == null ){
            throw new ServiceException(ErrorCode.ENUM_ERROR.A00001.geteCode(), ErrorCode.ENUM_ERROR.A00001.geteDesc());
        }

        // 校验收款账号类型
        if (requestModel.acType == null || requestModel.acType == 0){
            throw new ServiceException(ErrorCode.ENUM_ERROR.A00002.geteCode(), ErrorCode.ENUM_ERROR.A00002.geteDesc());
        }

//        // check收款人
//        if (StringUtils.isBlank(requestModel.payee)){
//            throw new ServiceException(ErrorCode.ENUM_ERROR.A00003.geteCode(), ErrorCode.ENUM_ERROR.A00003.geteDesc());
//        }

        if (requestModel.acType == 3){
            // check银行名称/银行卡开户行
            if (StringUtils.isBlank(requestModel.bankName)){
                throw new ServiceException(ErrorCode.ENUM_ERROR.A00004.geteCode(), ErrorCode.ENUM_ERROR.A00004.geteDesc());
            }
        }

        // check经营范围类型
        if (requestModel.businessType == null || requestModel.businessType == 0){
            throw new ServiceException(ErrorCode.ENUM_ERROR.A00005.geteCode(), ErrorCode.ENUM_ERROR.A00005.geteDesc());
        }


        // check小微商户二维码图片地址
        if (requestModel.acType == 1){

            if (requestModel.dataList == null || requestModel.dataList.size() <= 0){
                throw new ServiceException(ErrorCode.ENUM_ERROR.A00006.geteCode(), ErrorCode.ENUM_ERROR.A00006.geteDesc());
            }else {
                for (QrCode data : requestModel.dataList){
                    if (StringUtils.isBlank(data.ddQrCode)){
                        throw new ServiceException(ErrorCode.ENUM_ERROR.A00007.geteCode(), ErrorCode.ENUM_ERROR.A00007.geteDesc());
                    }
                    if(data.dataType == null || data.dataType == 0 ){
                        throw new ServiceException(ErrorCode.ENUM_ERROR.A00008.geteCode(), ErrorCode.ENUM_ERROR.A00008.geteDesc());
                    }
                }
            }

//            if (StringUtils.isBlank(requestModel.wxQrCodeAds)){
//                throw new ServiceException(ErrorCode.ENUM_ERROR.DC00008.geteCode(), ErrorCode.ENUM_ERROR.DC00008.geteDesc());
//            }
        }

        // 校验token值
        if (StringUtils.isBlank(requestModel.token)){
            throw new ServiceException(ErrorCode.ENUM_ERROR.D00001.geteCode(), ErrorCode.ENUM_ERROR.D00001.geteDesc());
        }

        // 校验用户是否登录
        did = HodgepodgeMethod.checkIsLogin(requestModel.token);

        return did;

    }


    /**
     * @Description: 组装要添加的用户收款账号信息-合二为一
     * @param requestDidCollectionAccountAll - 用户收款账号信息
     * @param did - 用户DID
     * @return com.hz.fine.master.core.model.did.DidCollectionAccountModel
     * @author yoko
     * @date 2020/5/15 16:22
     */
    public static DidCollectionAccountModel assembleDidCollectionAccount(RequestDidCollectionAccountAll requestDidCollectionAccountAll, long did){
        DidCollectionAccountModel resBean = BeanUtils.copy(requestDidCollectionAccountAll, DidCollectionAccountModel.class);
        resBean.setDid(did);
        return resBean;
    }


    /**
     * @Description: 组装添加二维码的数据
     * @param dataList
     * @return
     * @author yoko
     * @date 2020/6/17 18:17
    */
    public static List<DidCollectionAccountQrCodeModel> assembleDidCollectionAccountQrCodeList(List<QrCode> dataList){
        List<DidCollectionAccountQrCodeModel> resList = BeanUtils.copyList(dataList, DidCollectionAccountQrCodeModel.class);
        return resList;
    }



    /**
     * @Description: check校验数据获取用户收款账号二维码集合时
     * @param requestModel
     * @return
     * @author yoko
     * @date 2020/05/14 15:57
     */
    public static long checkDidCollectionAccountQrCodeListData(RequestDidCollectionAccountQrCode requestModel) throws Exception{
        long did;
        // 1.校验所有数据
        if (requestModel == null ){
            throw new ServiceException(ErrorCode.ENUM_ERROR.Q00006.geteCode(), ErrorCode.ENUM_ERROR.Q00006.geteDesc());
        }

        // 校验用户收款账号ID
        if (requestModel.collectionAccountId == null || requestModel.collectionAccountId <= 0){
            throw new ServiceException(ErrorCode.ENUM_ERROR.Q00007.geteCode(), ErrorCode.ENUM_ERROR.Q00007.geteDesc());
        }

        // 校验token值
        if (StringUtils.isBlank(requestModel.token)){
            throw new ServiceException(ErrorCode.ENUM_ERROR.D00001.geteCode(), ErrorCode.ENUM_ERROR.D00001.geteDesc());
        }

        // 校验用户是否登录
        did = HodgepodgeMethod.checkIsLogin(requestModel.token);

        return did;

    }

    /**
     * @Description: 组装根据主键ID用户收款账号ID查询收款账号信息的查询条件
     * @param did - 用户ID
     * @param id - 收款账号ID
     * @return com.hz.fine.master.core.model.did.DidCollectionAccountModel
     * @author yoko
     * @date 2020/6/17 18:51
     */
    public static DidCollectionAccountModel assembleDidCollectionAccountQueryByDid(long did, long id){
        DidCollectionAccountModel resBean = new DidCollectionAccountModel();
        resBean.setId(id);
        resBean.setDid(did);
        return resBean;
    }

    /**
     * @Description: check校验用户收款账号数据
     * @param didCollectionAccountModel
     * @return
     * @author yoko
     * @date 2020/6/17 20:30
    */
    public static void checkDidCollectionAccountById(DidCollectionAccountModel didCollectionAccountModel) throws Exception{
        if (didCollectionAccountModel == null || didCollectionAccountModel.getId() <= 0){
            throw new ServiceException(ErrorCode.ENUM_ERROR.Q00008.geteCode(), ErrorCode.ENUM_ERROR.Q00008.geteDesc());
        }
    }

    /**
     * @Description: 组装查询用户收款二维码的查询条件
     * @param requestDidCollectionAccountQrCode - 用户收款账号的主键ID
     * @return
     * @author yoko
     * @date 2020/6/17 20:34
    */
    public static DidCollectionAccountQrCodeModel assembleDidCollectionAccountQrCodeQuery(RequestDidCollectionAccountQrCode requestDidCollectionAccountQrCode){
        DidCollectionAccountQrCodeModel resBean = BeanUtils.copy(requestDidCollectionAccountQrCode, DidCollectionAccountQrCodeModel.class);
        return resBean;
    }



    /**
     * @Description: 用户收款账号的二维码数据组装返回客户端的方法-集合
     * @param stime - 服务器的时间
     * @param sign - 签名
     * @param didCollectionAccountQrCodeList - 用户收款账号列表集合
     * @param rowCount - 总行数
     * @return java.lang.String
     * @author yoko
     * @date 2019/11/25 22:45
     */
    public static String assembleDidCollectionAccountQrCodeListResult(long stime, String sign, List<DidCollectionAccountQrCodeModel> didCollectionAccountQrCodeList, Integer rowCount){
        ResponseDidCollectionAccountQrCode dataModel = new ResponseDidCollectionAccountQrCode();
        if (didCollectionAccountQrCodeList != null && didCollectionAccountQrCodeList.size() > ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_ZERO){
            List<com.hz.fine.master.core.protocol.response.did.qrcode.QrCode> dataList = BeanUtils.copyList(didCollectionAccountQrCodeList, com.hz.fine.master.core.protocol.response.did.qrcode.QrCode.class);
            dataModel.dataList = dataList;
        }
        if (rowCount != null){
            dataModel.rowCount = rowCount;
        }
        dataModel.setStime(stime);
        dataModel.setSign(sign);
        return JSON.toJSONString(dataModel);
    }


    /**
     * @Description: check校验数据获取用户收款账号的二维码的详情时
     * @param requestModel
     * @return
     * @author yoko
     * @date 2020/05/14 15:57
     */
    public static long checkDidCollectionAccountQrCodeData(RequestDidCollectionAccountQrCode requestModel) throws Exception{
        long did;
        // 1.校验所有数据
        if (requestModel == null ){
            throw new ServiceException(ErrorCode.ENUM_ERROR.Q00009.geteCode(), ErrorCode.ENUM_ERROR.Q00009.geteDesc());
        }

        if (requestModel.id == null || requestModel.id <= ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_ZERO){
            throw new ServiceException(ErrorCode.ENUM_ERROR.Q00010.geteCode(), ErrorCode.ENUM_ERROR.Q00010.geteDesc());
        }

        // 校验token值
        if (StringUtils.isBlank(requestModel.token)){
            throw new ServiceException(ErrorCode.ENUM_ERROR.D00001.geteCode(), ErrorCode.ENUM_ERROR.D00001.geteDesc());
        }

        // 校验用户是否登录
        did = HodgepodgeMethod.checkIsLogin(requestModel.token);

        return did;

    }


    /**
     * @Description: 根据二维码收款码的主键ID查询信息
     * @param id - 主键ID
     * @return
     * @author yoko
     * @date 2020/6/17 21:20
    */
    public static DidCollectionAccountQrCodeModel assembleDidCollectionAccountQrCodeById(long id){
        DidCollectionAccountQrCodeModel resBean = new DidCollectionAccountQrCodeModel();
        resBean.setId(id);
        return resBean;
    }


    /**
     * @Description: 用户收款账号的收款二维码数据-详情的数据组装返回客户端的方法
     * @param stime - 服务器的时间
     * @param sign - 签名
     * @param didCollectionAccountQrCodeModel - 用户收款账号的二维码数据的详情
     * @return java.lang.String
     * @author yoko
     * @date 2019/11/25 22:45
     */
    public static String assembleDidCollectionAccountDataResult(long stime, String sign, DidCollectionAccountQrCodeModel didCollectionAccountQrCodeModel){
        ResponseDidCollectionAccountQrCode dataModel = new ResponseDidCollectionAccountQrCode();
        if (didCollectionAccountQrCodeModel != null){
            com.hz.fine.master.core.protocol.response.did.qrcode.QrCode data = BeanUtils.copy(didCollectionAccountQrCodeModel, com.hz.fine.master.core.protocol.response.did.qrcode.QrCode.class);
            dataModel.dataModel = data;
        }
        dataModel.setStime(stime);
        dataModel.setSign(sign);
        return JSON.toJSONString(dataModel);
    }



    /**
     * @Description: check校验数据当用户更新收款账号的二维码信息的时候
     * @param requestModel
     * @return
     * @author yoko
     * @date 2020/05/14 15:57
     */
    public static long checRequestDidCollectionAccountQrCodeUpdate(RequestDidCollectionAccountQrCode requestModel) throws Exception{
        long did;
        // 1.校验所有数据
        if (requestModel == null ){
            throw new ServiceException(ErrorCode.ENUM_ERROR.Q00011.geteCode(), ErrorCode.ENUM_ERROR.Q00011.geteDesc());
        }

        // 校验ID
        if (requestModel.id == null || requestModel.id <= ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_ZERO){
            throw new ServiceException(ErrorCode.ENUM_ERROR.Q00012.geteCode(), ErrorCode.ENUM_ERROR.Q00012.geteDesc());
        }

        // 校验转码后的二维码地址
        if (StringUtils.isBlank(requestModel.ddQrCode)){
            throw new ServiceException(ErrorCode.ENUM_ERROR.Q00013.geteCode(), ErrorCode.ENUM_ERROR.Q00013.geteDesc());
        }

        // 校验二维码类型
        if(requestModel.dataType == null || requestModel.dataType == 0 ){
            throw new ServiceException(ErrorCode.ENUM_ERROR.Q00014.geteCode(), ErrorCode.ENUM_ERROR.Q00014.geteDesc());
        }

        // 校验token值
        if (StringUtils.isBlank(requestModel.token)){
            throw new ServiceException(ErrorCode.ENUM_ERROR.D00001.geteCode(), ErrorCode.ENUM_ERROR.D00001.geteDesc());
        }

        // 校验用户是否登录
        did = HodgepodgeMethod.checkIsLogin(requestModel.token);

        return did;

    }

    /**
     * @Description: 组装根据用户收款账号ID查询二维码的方法
     * @param collectionAccountId - 用户收款账号ID：对应表tb_fn_did_collection_account的主键ID
     * @return 
     * @author yoko
     * @date 2020/6/17 22:10 
    */
    public static DidCollectionAccountQrCodeModel assembleDidCollectionAccountQrCodeByCollId(long collectionAccountId){
        DidCollectionAccountQrCodeModel resBean = new DidCollectionAccountQrCodeModel();
        resBean.setCollectionAccountId(collectionAccountId);
        return resBean;
    }


    /**
     * @Description: check校验数据当用户更新收款款账号二维码使用状态时
     * @param requestModel
     * @return
     * @author yoko
     * @date 2020/05/14 15:57
     */
    public static long checkDidCollectionAccountQrCodeUpdateUseData(RequestDidCollectionAccountQrCode requestModel) throws Exception{
        long did;
        // 1.校验所有数据
        if (requestModel == null ){
            throw new ServiceException(ErrorCode.ENUM_ERROR.Q00015.geteCode(), ErrorCode.ENUM_ERROR.Q00015.geteDesc());
        }

        if (requestModel.id == null || requestModel.id <= ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_ZERO){
            throw new ServiceException(ErrorCode.ENUM_ERROR.Q00016.geteCode(), ErrorCode.ENUM_ERROR.Q00016.geteDesc());
        }

        if ((requestModel.useStatus == null || requestModel.useStatus <= ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_ZERO)
                && (requestModel.yn == null || requestModel.yn <= ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_ZERO)){
            throw new ServiceException(ErrorCode.ENUM_ERROR.Q00017.geteCode(), ErrorCode.ENUM_ERROR.Q00017.geteDesc());
        }

        // 校验token值
        if (StringUtils.isBlank(requestModel.token)){
            throw new ServiceException(ErrorCode.ENUM_ERROR.D00001.geteCode(), ErrorCode.ENUM_ERROR.D00001.geteDesc());
        }

        // 校验用户是否登录
        did = HodgepodgeMethod.checkIsLogin(requestModel.token);

        return did;

    }


    /**
     * @Description: 组装用户更新收款账号的二维码的使用状态
     * @param requestDidCollectionAccountQrCode - 要更新的基本信息
     * @return
     * @author yoko
     * @date 2020/5/14 17:20
     */
    public static DidCollectionAccountQrCodeModel assembleDidCollectionAccountQrCodeUpdateUse(RequestDidCollectionAccountQrCode requestDidCollectionAccountQrCode){
        DidCollectionAccountQrCodeModel resBean = BeanUtils.copy(requestDidCollectionAccountQrCode, DidCollectionAccountQrCodeModel.class);
        return resBean;
    }



    /**
     * @Description: check校验数据当用户批量更新收款款账号二维码使用状态时
     * @param requestModel
     * @return
     * @author yoko
     * @date 2020/05/14 15:57
     */
    public static long checkDidCollectionAccountQrCodeBatchUpdateUseData(RequestDidCollectionAccountQrCode requestModel) throws Exception{
        long did;
        // 1.校验所有数据
        if (requestModel == null ){
            throw new ServiceException(ErrorCode.ENUM_ERROR.Q00018.geteCode(), ErrorCode.ENUM_ERROR.Q00018.geteDesc());
        }

        // check收款账号的主键ID
        if (requestModel.collectionAccountId == null || requestModel.collectionAccountId <= ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_ZERO){
            throw new ServiceException(ErrorCode.ENUM_ERROR.Q00019.geteCode(), ErrorCode.ENUM_ERROR.Q00019.geteDesc());
        }

        // check要操作的类型
        if ((requestModel.useStatus == null || requestModel.useStatus <= ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_ZERO)
                && (requestModel.yn == null || requestModel.yn <= ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_ZERO)){
            throw new ServiceException(ErrorCode.ENUM_ERROR.Q00020.geteCode(), ErrorCode.ENUM_ERROR.Q00020.geteDesc());
        }

        // 校验token值
        if (StringUtils.isBlank(requestModel.token)){
            throw new ServiceException(ErrorCode.ENUM_ERROR.D00001.geteCode(), ErrorCode.ENUM_ERROR.D00001.geteDesc());
        }

        // 校验用户是否登录
        did = HodgepodgeMethod.checkIsLogin(requestModel.token);

        return did;

    }

    /**
     * @Description: 组装添加二维码数据的方法
     * @param requestDidCollectionAccountQrCode - 请求的二维码数据
     * @return
     * @author yoko
     * @date 2020/6/17 23:47
    */
    public static DidCollectionAccountQrCodeModel assembleDidCollectionAccountQrCodeAdd(RequestDidCollectionAccountQrCode requestDidCollectionAccountQrCode){
        DidCollectionAccountQrCodeModel resBean = new DidCollectionAccountQrCodeModel();
        resBean.setCollectionAccountId(requestDidCollectionAccountQrCode.collectionAccountId);
        List<DidCollectionAccountQrCodeModel> dataList = BeanUtils.copyList(requestDidCollectionAccountQrCode.dataList, DidCollectionAccountQrCodeModel.class);
        resBean.setDataList(dataList);
        return resBean;
    }


    /**
     * @Description: 组装查询小微旗下店员的有效收款账号的查询条件
     * @param collectionAccountId - 用户收款账号ID
     * @return com.hz.fine.master.core.model.wx.WxClerkModel
     * @author yoko
     * @date 2020/5/26 19:44
     */
    public static WxClerkModel assembleWxClerkByCollectionAccountId(long collectionAccountId){
        WxClerkModel resBean = new WxClerkModel();
        resBean.setCollectionAccountId(collectionAccountId);
        return resBean;
    }

    /**
     * @Description: 组装删除小微旗下店员的方法
     * @param collectionAccountId - 收款账号主键ID
     * @return
     * @author yoko
     * @date 2020/6/18 10:39
    */
    public static WxClerkModel assembleWxClerkUpdateData(long collectionAccountId){
        WxClerkModel resBean = new WxClerkModel();
        resBean.setCollectionAccountId(collectionAccountId);
        resBean.setYn(1);
        return resBean;
    }


    /**
     * @Description: 组装小微需要解绑店员数据添加的方法
     * @param wxId - 小微的主键ID
     * @param didCollectionAccountModel - 用户收款账号信息
     * @return
     * @author yoko
     * @date 2020/6/18 10:43
    */
    public static WxClerkUnboundModel assembleWxClerkUnbound(long wxId, DidCollectionAccountModel didCollectionAccountModel){
        WxClerkUnboundModel resBean = new WxClerkUnboundModel();
        resBean.setWxId(wxId);
        resBean.setDid(didCollectionAccountModel.getDid());
        resBean.setCollectionAccountId(didCollectionAccountModel.getId());
        if (!StringUtils.isBlank(didCollectionAccountModel.getPayee())){
            resBean.setWxName(didCollectionAccountModel.getPayee());
        }
        return resBean;
    }

    /**
     * @Description: 组装查询二维码的查询条件
     * @param collectionAccountId - 收款账号的主键ID
     * @return
     * @author yoko
     * @date 2020/6/18 19:34
    */
    public static DidCollectionAccountQrCodeModel assembleDidCollectionAccountQrCode(long collectionAccountId){
        DidCollectionAccountQrCodeModel resBean = new DidCollectionAccountQrCodeModel();
        resBean.setCollectionAccountId(collectionAccountId);
        resBean.setIsLimitNum(1);
        resBean.setUseStatus(1);
        return resBean;
    }

    /**
     * @Description: 组装要批量添加的二维码数据
     * @param collectionAccountId - 收款账号的主键ID
     * @param dataList - 二维码数据集合
     * @return
     * @author yoko
     * @date 2020/6/20 15:03
    */
    public static DidCollectionAccountQrCodeModel assembleDidCollectionAccountQrCodeBatch(long collectionAccountId, List<QrCode> dataList) {
        DidCollectionAccountQrCodeModel resBean = new DidCollectionAccountQrCodeModel();
        List<DidCollectionAccountQrCodeModel> qcCodeList = BeanUtils.copyList(dataList, DidCollectionAccountQrCodeModel.class);
        resBean.setCollectionAccountId(collectionAccountId);
        resBean.setDataList(qcCodeList);
        return resBean;

    }

    /**
     * @Description: 校验策略来源地址集合
     * @return void
     * @author yoko
     * @date 2019/12/2 14:35
     */
    public static void checkStrategyByRefererList(StrategyModel strategyModel) throws Exception{
        if (strategyModel == null){
            throw new ServiceException(ErrorCode.ENUM_ERROR.S00016.geteCode(), ErrorCode.ENUM_ERROR.S00016.geteDesc());
        }
    }

    /**
     * @Description: 组装查询我要买时：查询银行卡的组装查询条件
     * @param mobileCardList - 手机卡主键ID
     * @param bankWorkTime - 银行工作日期
     * @param requestModel - 查询条件
     * @return
     * @author yoko
     * @date 2020/6/29 14:27
    */
    public static BankModel assembleBankByBuyQuery(List<MobileCardModel> mobileCardList, String bankWorkTime, RequestBank requestModel){
        BankModel resBean = new BankModel();
        resBean = BeanUtils.copy(requestModel, BankModel.class);
        List<Long> mobileCardIdList = new ArrayList<>();
        for (MobileCardModel mobileCardModel : mobileCardList){
            mobileCardIdList.add(mobileCardModel.getId());
        }
        resBean.setMobileCardIdList(mobileCardIdList);

        // 获得是白天银行上班的工作时间还是银行休息时间：开启类型：1白天，2晚上，3两者都支持；银行卡是白天跑还是晚上跑数据
        int openType = getOpenTypeByBankWork(bankWorkTime);
        List<Integer> openTypeList = new ArrayList<>();
        openTypeList.add(openType);
        openTypeList.add(ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_THREE);
        resBean.setOpenTypeList(openTypeList);
        resBean.setDaySwitch(ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_ONE);
        resBean.setMonthSwitch(ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_ONE);
        resBean.setTotalSwitch(ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_ONE);
        resBean.setUseStatus(ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_ONE);
        return resBean;
    }


    /**
     * @Description: 生成1万-5万以内的随机数
     * @return
     * @author yoko
     * @date 2020/6/29 15:07
    */
    public static int getRandom(){
        Random rand = new Random();
        int num = rand.nextInt(50000 - 10000 + 1) + 10000; // 将被赋值为一个 MIN 和 MAX 范围内的随机数
        return num;
    }



    /**
     * @Description: 用户我要买的银行集合数据组装返回客户端的方法-集合
     * @param stime - 服务器的时间
     * @param sign - 签名
     * @param buyBankList - 银行卡数据集合集合
     * @param rowCount - 总行数
     * @return java.lang.String
     * @author yoko
     * @date 2019/11/25 22:45
     */
    public static String assembleBankListResult(long stime, String sign, List<BuyBank> buyBankList, Integer rowCount){
        ResponseBank dataModel = new ResponseBank();
        if (buyBankList != null && buyBankList.size() > ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_ZERO){
            dataModel.dataList = buyBankList;
        }
        if (rowCount != null){
            dataModel.rowCount = rowCount;
        }
        dataModel.setStime(stime);
        dataModel.setSign(sign);
        return JSON.toJSONString(dataModel);
    }


    /**
     * @Description: check校验数据获取银行卡详情时
     * @param requestModel
     * @return
     * @author yoko
     * @date 2020/05/14 15:57
     */
    public static long checkBankData(RequestBank requestModel) throws Exception{
        long did;
        // 1.校验所有数据
        if (requestModel == null ){
            throw new ServiceException(ErrorCode.ENUM_ERROR.BK00001.geteCode(), ErrorCode.ENUM_ERROR.BK00001.geteDesc());
        }

        if (requestModel.id == null || requestModel.id <= 0){
            throw new ServiceException(ErrorCode.ENUM_ERROR.BK00002.geteCode(), ErrorCode.ENUM_ERROR.BK00002.geteDesc());
        }

        // 校验token值
        if (StringUtils.isBlank(requestModel.token)){
            throw new ServiceException(ErrorCode.ENUM_ERROR.D00001.geteCode(), ErrorCode.ENUM_ERROR.D00001.geteDesc());
        }

        // 校验用户是否登录
        did = HodgepodgeMethod.checkIsLogin(requestModel.token);

        return did;

    }


    /**
     * @Description: 组装查询银行卡信息的查询条件
     * @param id - 银行卡主键ID
     * @return
     * @author yoko
     * @date 2020/6/29 15:43
    */
    public static BankModel assembleBankById(long id){
        BankModel resBean = new BankModel();
        resBean.setId(id);
        return resBean;
    }


    /**
     * @Description: 获取银行卡信息-详情的数据组装返回客户端的方法
     * @param stime - 服务器的时间
     * @param sign - 签名
     * @param bankModel - 银行卡详情
     * @return java.lang.String
     * @author yoko
     * @date 2019/11/25 22:45
     */
    public static String assembleBankDataResult(long stime, String sign, BankModel bankModel){
        ResponseBank dataModel = new ResponseBank();
        if (bankModel != null){
            Bank data = BeanUtils.copy(bankModel, Bank.class);
            dataModel.dataModel = data;
        }
        dataModel.setStime(stime);
        dataModel.setSign(sign);
        return JSON.toJSONString(dataModel);
    }


    /**
     * @Description: check校验数据当用户正式购买是
     * @param requestModel
     * @return
     * @author yoko
     * @date 2020/05/14 15:57
     */
    public static long checkRechargeBuy(RequestDidRecharge requestModel) throws Exception{
        long did;
        // 1.校验所有数据
        if (requestModel == null ){
            throw new ServiceException(ErrorCode.ENUM_ERROR.DR00012.geteCode(), ErrorCode.ENUM_ERROR.DR00012.geteDesc());
        }

        // 校验充值金额值
        if (StringUtils.isBlank(requestModel.orderMoney)){
            throw new ServiceException(ErrorCode.ENUM_ERROR.DR00013.geteCode(), ErrorCode.ENUM_ERROR.DR00013.geteDesc());
        }

        // 校验银行卡ID
        if (requestModel.bankId == null || requestModel.bankId <= 0){
            throw new ServiceException(ErrorCode.ENUM_ERROR.DR00014.geteCode(), ErrorCode.ENUM_ERROR.DR00014.geteDesc());
        }

        // 校验token值
        if (StringUtils.isBlank(requestModel.token)){
            throw new ServiceException(ErrorCode.ENUM_ERROR.D00001.geteCode(), ErrorCode.ENUM_ERROR.D00001.geteDesc());
        }

        // 校验用户是否登录
        did = HodgepodgeMethod.checkIsLogin(requestModel.token);

        return did;

    }

    /**
     * @Description: check银行卡数据
     * @param bankModel - 银行卡信息
     * @return
     * @author yoko
     * @date 2020/6/29 16:56
    */
    public static void checkBank(BankModel bankModel) throws Exception{
        if (bankModel == null || bankModel.getId() <= 0){
            throw new ServiceException(ErrorCode.ENUM_ERROR.BK00003.geteCode(), ErrorCode.ENUM_ERROR.BK00003.geteDesc());
        }
    }


    /**
     * @Description: 组装用户充值记录的方法
     * @param money - 筛选出来的金额信息
     * @param bankId - 银行卡主键ID
     * @param did - 用户ID
     * @param orderNo - 订单号
     * @param moneyId - 策略里面的金额的主键ID
     * @param orderMoney - 订单金额
     * @param invalid - 策略里面的失效时间多少分钟
     * @return com.hz.fine.master.core.model.did.DidRechargeModel
     * @author yoko
     * @date 2020/5/21 13:52
     */
    public static DidRechargeModel assembleDidRechargeBuy(String money, long bankId, long did, String orderNo, long moneyId, String orderMoney, int invalid){
        DidRechargeModel resBean = new DidRechargeModel();
        resBean.setDid(did);
        resBean.setOrderNo(orderNo);
        resBean.setMoneyId(moneyId);
        resBean.setOrderMoney(orderMoney);
        resBean.setDistributionMoney(money);
        resBean.setBankId(bankId);

        // 订单失效时间
        String invalidTime = DateUtil.addDateMinute(invalid);
        resBean.setInvalidTime(invalidTime);
        resBean.setDataType(2);
        resBean.setCurday(DateUtil.getDayNumber(new Date()));
        resBean.setCurhour(DateUtil.getHour(new Date()));
        resBean.setCurminute(DateUtil.getCurminute(new Date()));
        return resBean;
    }


    /**
     * @Description: check校验数据用户充值之后，更新充值存入账号的信息
     * @param requestModel
     * @return
     * @author yoko
     * @date 2020/05/14 15:57
     */
    public static long checkDeposit(RequestDidRecharge requestModel) throws Exception{
        long did;
        // 1.校验所有数据
        if (requestModel == null ){
            throw new ServiceException(ErrorCode.ENUM_ERROR.DR00016.geteCode(), ErrorCode.ENUM_ERROR.DR00016.geteDesc());
        }

        // 校验订单号值
        if (StringUtils.isBlank(requestModel.orderNo)){
            throw new ServiceException(ErrorCode.ENUM_ERROR.DR00017.geteCode(), ErrorCode.ENUM_ERROR.DR00017.geteDesc());
        }

        // 校验存入人的值
        if (StringUtils.isBlank(requestModel.depositor)){
            throw new ServiceException(ErrorCode.ENUM_ERROR.DR00018.geteCode(), ErrorCode.ENUM_ERROR.DR00018.geteDesc());
        }

        // 校验存款金额的值
        if (StringUtils.isBlank(requestModel.depositMoney)){
            throw new ServiceException(ErrorCode.ENUM_ERROR.DR00022.geteCode(), ErrorCode.ENUM_ERROR.DR00022.geteDesc());
        }

        // 校验存入时间的值
        if (StringUtils.isBlank(requestModel.depositTime)){
            throw new ServiceException(ErrorCode.ENUM_ERROR.DR00019.geteCode(), ErrorCode.ENUM_ERROR.DR00019.geteDesc());
        }

        // 校验存入账号的尾号的值
        if (StringUtils.isBlank(requestModel.lastNum)){
            throw new ServiceException(ErrorCode.ENUM_ERROR.DR00020.geteCode(), ErrorCode.ENUM_ERROR.DR00020.geteDesc());
        }

        // 校验token值
        if (StringUtils.isBlank(requestModel.token)){
            throw new ServiceException(ErrorCode.ENUM_ERROR.D00001.geteCode(), ErrorCode.ENUM_ERROR.D00001.geteDesc());
        }

        // 校验用户是否登录
        did = HodgepodgeMethod.checkIsLogin(requestModel.token);

        return did;

    }

    /**
     * @Description: 组装用户充值之后，更新充值存入账号的信息
     * @param requestModel - 用户的存入信息数据
     * @param did - 用户ID
     * @return
     * @author yoko
     * @date 2020/5/21 15:57
     */
    public static DidRechargeModel assembleDepositUpdate(RequestDidRecharge requestModel, long did){
        DidRechargeModel resBean = BeanUtils.copy(requestModel, DidRechargeModel.class);
        resBean.setDid(did);
        resBean.setWorkType(2);
        return resBean;
    }


    /**
     * @Description: 用户修改存入信息后，修改redis的缓存数据
     * @param redisData - 用户缓存中的充值订单数据
     * @param requestModel - 更新的存入信息
     * @return java.lang.String
     * @author yoko
     * @date 2019/11/25 22:45
     */
    public static String assembleDidRechargeUpdateRedisByDeposit(String redisData, RequestDidRecharge requestModel){
        ResponseDidRecharge dataModel = JSON.parseObject(redisData, ResponseDidRecharge.class);
        dataModel.recharge.depositor = requestModel.depositor;
        dataModel.recharge.depositMoney = requestModel.depositMoney;
        dataModel.recharge.depositTime = requestModel.depositTime;
        dataModel.recharge.lastNum = requestModel.lastNum;
        return JSON.toJSONString(dataModel);
    }

    /**
     * @Description: 组装查询成功订单的查询条件
     * @param ratio - 收益比例
     * @return 
     * @author yoko
     * @date 2020/6/30 11:25 
    */
    public static OrderModel assembleOrderQuery(String ratio){
        OrderModel resBean = new OrderModel();
        resBean.setRatio(ratio);
        resBean.setCreateTime(DateUtil.addDateMinute(5));
        return resBean;
    }


    /**
     * @Description: 组装临时假数据
     * @param consumeMoneyList - 消耗金额范围内的奖励规则列表
     * @param dataNum - 值等于0则生成5条数据，等于1则生成2条数据
     * @return
     * @author yoko
     * @date 2020/6/30 11:34
    */
    public static List<OrderModel> getTempOrderList(List<StrategyData> consumeMoneyList, int dataNum){
        List<OrderModel> resList = new ArrayList<>();
        OrderModel bean1 = new OrderModel();
        bean1.setAcName("****");
        // 随机生成数字

        int random1 = (int)(Math.random()*20+1) * 100;
        bean1.setOrderMoney(String.valueOf(random1) + ".00");
//        String profit1 = StringUtil.getMultiply(String.valueOf(random1), ratio);
        String profit1 = getConsumeProfit(consumeMoneyList, String.valueOf(random1) + ".00");

        bean1.setProfit(profit1);

        OrderModel bean2 = new OrderModel();
        bean2.setAcName("****");
        // 随机生成数字
        int random2 = (int)(Math.random()*20+1) * 100;
        bean2.setOrderMoney(String.valueOf(random2) + ".00");
//        String profit2 = StringUtil.getMultiply(String.valueOf(random2), ratio);
        String profit2 = getConsumeProfit(consumeMoneyList, String.valueOf(random2) + ".00");
        bean2.setProfit(profit2);

        OrderModel bean3 = new OrderModel();
        bean3.setAcName("****");
        // 随机生成数字
        int random3 = (int)(Math.random()*20+1) * 100;
        bean3.setOrderMoney(String.valueOf(random3) + ".00");
//        String profit3 = StringUtil.getMultiply(String.valueOf(random3), ratio);
        String profit3 = getConsumeProfit(consumeMoneyList, String.valueOf(random3) + ".00");
        bean3.setProfit(profit3);

        OrderModel bean4 = new OrderModel();
        bean4.setAcName("****");
        // 随机生成数字
        int random4 = (int)(Math.random()*20+1) * 100;
        bean4.setOrderMoney(String.valueOf(random4) + ".00");
//        String profit4 = StringUtil.getMultiply(String.valueOf(random4), ratio);
        String profit4 = getConsumeProfit(consumeMoneyList, String.valueOf(random4) + ".00");
        bean4.setProfit(profit4);

        OrderModel bean5 = new OrderModel();
        bean5.setAcName("****");
        // 随机生成数字
        int random5 = (int)(Math.random()*20+1) * 100;
        bean5.setOrderMoney(String.valueOf(random5) + ".00");
//        String profit5 = StringUtil.getMultiply(String.valueOf(random5), ratio);
        String profit5 = getConsumeProfit(consumeMoneyList, String.valueOf(random5) + ".00");
        bean5.setProfit(profit5);

        if (dataNum == 0){
            resList.add(bean1);
            resList.add(bean2);
            resList.add(bean3);
            resList.add(bean4);
            resList.add(bean5);
        }else{
            resList.add(bean1);
            resList.add(bean2);
            resList.add(bean3);
        }
        return resList;
    }


    /**
     * @Description: 我要卖的数据返回客户端的方法-集合
     * @param stime - 服务器的时间
     * @param sign - 签名
     * @param orderList - 成功订单集合
     * @param rowCount - 总行数
     * @return java.lang.String
     * @author yoko
     * @date 2019/11/25 22:45
     */
    public static String assembleSellListResult(long stime, String sign, List<OrderModel> orderList, Integer rowCount){
        ResponseSell dataModel = new ResponseSell();
        if (orderList != null && orderList.size() > ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_ZERO){
            List<Sell> dataList = new ArrayList<>();
            for (OrderModel orderModel : orderList){
                Sell sell = new Sell();
                sell.acName = orderModel.getAcName();
                sell.sellNum = orderModel.getOrderMoney();
                sell.orderMoney = orderModel.getOrderMoney();
                sell.profit = orderModel.getProfit();
                String time = DateUtil.getDateHmd(new Date());
                int random = (int)(Math.random()*59);
                String second = "";
                if (random < 10){
                    second = "0" + String.valueOf(random);
                }else {
                    second = String.valueOf(random);
                }
                time = time.substring(0, 6) + second;
                sell.time = time;
                dataList.add(sell);
            }
            dataModel.dataList = dataList;
        }
        int minNum = StringUtil.getRandom(50, 100);
        int maxNum = StringUtil.getRandom(120, 200);
        dataModel.totalNum = maxNum;
        dataModel.waitNum = minNum;
        if (rowCount != null){
            dataModel.rowCount = rowCount;
        }
        dataModel.setStime(stime);
        dataModel.setSign(sign);
        return JSON.toJSONString(dataModel);
    }


    /**
     * @Description: check校验数据当用户抢单上下线修改时
     * @param requestModel
     * @return
     * @author yoko
     * @date 2020/05/14 15:57
     */
    public static long checkOnoffUpdate(RequestDidOnoff requestModel) throws Exception{
        long did = 0;
        // 1.校验所有数据
        if (requestModel == null ){
            throw new ServiceException(ErrorCode.ENUM_ERROR.OF00001.geteCode(), ErrorCode.ENUM_ERROR.OF00001.geteDesc());
        }

        // 校验数据类型是否为空
        if (requestModel.dataType == null || requestModel.dataType <= 0){
            throw new ServiceException(ErrorCode.ENUM_ERROR.OF00002.geteCode(), ErrorCode.ENUM_ERROR.OF00002.geteDesc());
        }

        // 校验token值
        if (StringUtils.isBlank(requestModel.token)){
            throw new ServiceException(ErrorCode.ENUM_ERROR.D00001.geteCode(), ErrorCode.ENUM_ERROR.D00001.geteDesc());
        }

        // 校验用户是否登录
        did = HodgepodgeMethod.checkIsLogin(requestModel.token);

        return did;

    }

    /**
     * @Description: 组装查询用户抢单上下线的查询条件
     * @param did - 用户ID
     * @return
     * @author yoko
     * @date 2020/6/30 18:51
    */
    public static DidOnoffModel assembleDidOnoffQueryByDid(long did){
        DidOnoffModel resBean = new DidOnoffModel();
        resBean.setDid(did);
        return resBean;
    }

    /**
     * @Description: 组装更新用户抢单上下线的数据
     * @param did - 用户ID
     * @param dataType - 数据类型;1下线（取消抢单），2上线（开始抢单）
     * @return com.hz.fine.master.core.model.did.DidOnoffModel
     * @author yoko
     * @date 2020/6/30 18:54
     */
    public static DidOnoffModel assembleDidOnoffUpdate(long did, int dataType) throws Exception{
        DidOnoffModel resBean = new DidOnoffModel();
        resBean.setDid(did);
        resBean.setDataType(dataType);
        if (dataType == ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_ONE){
            // 下线
            resBean.setOfflineTime(DateUtil.getNowPlusTime());
        }else{
            resBean.setOnlineTime(DateUtil.getNowPlusTime());
        }
        return resBean;
    }


    /**
     * @Description: 用户抢单上下线最终状态-的数据组装返回客户端的方法
     * @param stime - 服务器的时间
     * @param sign - 签名
     * @param dataType - 数据类型;1下线（取消抢单），2上线（开始抢单）
     * @return java.lang.String
     * @author yoko
     * @date 2019/11/25 22:45
     */
    public static String assembleDidOnoffResult(long stime, String sign, int dataType){
        ResponseDidOnoff dataModel = new ResponseDidOnoff();
        DidOnoff data = new DidOnoff();
        data.dataType = dataType;
        dataModel.dataModel = data;
        dataModel.setStime(stime);
        dataModel.setSign(sign);
        return JSON.toJSONString(dataModel);
    }

    /**
     * @Description: check用户余额
     * @param balance - 用户余额
     * @return
     * @author yoko
     * @date 2020/6/30 20:38
    */
    public static void checkDidbalance(String balance) throws Exception{
        if (StringUtils.isBlank(balance)){
            throw new ServiceException(ErrorCode.ENUM_ERROR.OF00004.geteCode(), ErrorCode.ENUM_ERROR.OF00004.geteDesc());
        }
        if (Double.parseDouble(balance) <= 0){
            throw new ServiceException(ErrorCode.ENUM_ERROR.OF00004.geteCode(), ErrorCode.ENUM_ERROR.OF00004.geteDesc());
        }
    }


    /**
     * @Description: check校验数据获取用户抢单上下线的状态时
     * @param requestModel
     * @return
     * @author yoko
     * @date 2020/05/14 15:57
     */
    public static long checkOnoffData(RequestDidOnoff requestModel) throws Exception{
        long did = 0;
        // 1.校验所有数据
        if (requestModel == null ){
            throw new ServiceException(ErrorCode.ENUM_ERROR.OF00010.geteCode(), ErrorCode.ENUM_ERROR.OF00010.geteDesc());
        }

        // 校验token值
        if (StringUtils.isBlank(requestModel.token)){
            throw new ServiceException(ErrorCode.ENUM_ERROR.D00001.geteCode(), ErrorCode.ENUM_ERROR.D00001.geteDesc());
        }

        // 校验用户是否登录
        did = HodgepodgeMethod.checkIsLogin(requestModel.token);

        return did;

    }



    /**
     * @Description: check校验数据获取初始化派单数据详情时
     * @param requestModel
     * @return
     * @author yoko
     * @date 2020/05/14 15:57
     */
    public static long checkGetOrderData(RequestOrder requestModel) throws Exception{
        long did;
        // 1.校验所有数据
        if (requestModel == null ){
            throw new ServiceException(ErrorCode.ENUM_ERROR.OR00015.geteCode(), ErrorCode.ENUM_ERROR.OR00015.geteDesc());
        }

        // 校验token值
        if (StringUtils.isBlank(requestModel.token)){
            throw new ServiceException(ErrorCode.ENUM_ERROR.D00001.geteCode(), ErrorCode.ENUM_ERROR.D00001.geteDesc());
        }

        // 校验用户是否登录
        did = HodgepodgeMethod.checkIsLogin(requestModel.token);

        return did;

    }

    /**
     * @Description: 组装查询派单的查询条件
     * @param did - 用户ID
     * @param orderStatus - 订单状态：1初始化，2超时/失败，3有质疑，4成功
     * @param didStatus - 订单状态_用户操作的状态：1初始化，2失败，3超时后默认成功，4用户点击成功
     * @param ratio - 收益比例
     * @return com.hz.fine.master.core.model.order.OrderModel
     * @author yoko
     * @date 2020/7/1 16:17
     */
    public static OrderModel assembleOrderByDidQuery(long did, int orderStatus, int didStatus, String ratio){
        OrderModel resBean = new OrderModel();
        resBean.setDid(did);
        resBean.setOrderStatus(orderStatus);
        resBean.setDidStatus(didStatus);
//        resBean.setRatio(ratio);
        return resBean;
    }


    /**
     * @Description: check校验数据当用户订单的用户操作派单状态的时候
     * @param requestModel
     * @return
     * @author yoko
     * @date 2020/05/14 15:57
     */
    public static long checkUpdateOrderStatus(RequestOrder requestModel) throws Exception{
        long did;
        // 1.校验所有数据
        if (requestModel == null ){
            throw new ServiceException(ErrorCode.ENUM_ERROR.OR00016.geteCode(), ErrorCode.ENUM_ERROR.OR00016.geteDesc());

        }

        if (StringUtils.isBlank(requestModel.orderNo)){
            throw new ServiceException(ErrorCode.ENUM_ERROR.OR00017.geteCode(), ErrorCode.ENUM_ERROR.OR00017.geteDesc());
        }

        if (requestModel.status == null || requestModel.status <= 1){
            throw new ServiceException(ErrorCode.ENUM_ERROR.OR00018.geteCode(), ErrorCode.ENUM_ERROR.OR00018.geteDesc());
        }

        // 校验token值
        if (StringUtils.isBlank(requestModel.token)){
            throw new ServiceException(ErrorCode.ENUM_ERROR.D00001.geteCode(), ErrorCode.ENUM_ERROR.D00001.geteDesc());
        }

        // 校验用户是否登录
        did = HodgepodgeMethod.checkIsLogin(requestModel.token);
        return did;

    }

    /**
     * @Description: 组装更新用户订单的用户操作派单状态的方法
     * @param requestModel - 请求的数据
     * @return
     * @author yoko
     * @date 2020/5/14 19:56
     */
    public static OrderModel assembleUpdateOrderStatusData(long did, RequestOrder requestModel){
        OrderModel resBean = new OrderModel();
        resBean.setDid(did);
        resBean.setOrderNo(requestModel.orderNo);
        resBean.setDidStatus(requestModel.status);
        if (requestModel.status >= 3){
            resBean.setOrderStatus(4);
            resBean.setRemark("手动或者系统默认点击成功");
        }
        return resBean;
    }


    /**
     * @Description: 校验策略类型数据:用户提交订单状态的最后读秒时间
     * @return void
     * @author yoko
     * @date 2019/12/2 14:35
     */
    public static void checkStrategyByLastTime(StrategyModel strategyModel) throws Exception{
        if (strategyModel == null){
            throw new ServiceException(ErrorCode.ENUM_ERROR.S00017.geteCode(), ErrorCode.ENUM_ERROR.S00017.geteDesc());
        }
        if (strategyModel.getStgNumValue() == 0){
            throw new ServiceException(ErrorCode.ENUM_ERROR.S00018.geteCode(), ErrorCode.ENUM_ERROR.S00018.geteDesc());
        }
    }

    /**
     * @Description: 用户派单（初始化）-详情的数据组装返回客户端的方法
     * @param stime - 服务器的时间
     * @param sign - 签名
     * @param orderModel - 用户派单的详情
     * @return java.lang.String
     * @author yoko
     * @date 2019/11/25 22:45
     */
    public static String assembleInitOrderDataResult(long stime, String sign, OrderModel orderModel, int lastTime){
        ResponseOrder dataModel = new ResponseOrder();
        if (orderModel != null && !StringUtils.isBlank(orderModel.getOrderNo())){
            Order data = BeanUtils.copy(orderModel, Order.class);
            if (lastTime > 0){
                data.invalidTime = DateUtil.addAndSubtractDateSecond(data.invalidTime, - lastTime);
                data.invalidSecond = String.valueOf(Integer.parseInt(data.invalidSecond) - lastTime - 1);
                if (data.invalidSecond.indexOf("-") > -1){
                    data.invalidSecond = "0";
                }
                data.lastTime = lastTime;
            }
            dataModel.dataModel = data;
        }
        dataModel.setStime(stime);
        dataModel.setSign(sign);
        return JSON.toJSONString(dataModel);
    }


    /**
     * @Description: 计算派单的订单金额的奖励金额（收益）
     * @param consumeMoneyList - 消耗金额范围内的奖励规则列表
     * @param orderMoney - 订单金额
     * @return java.lang.String
     * @author yoko
     * @date 2020/6/6 11:48
     */
    public static String getConsumeProfit(List<StrategyData> consumeMoneyList, String orderMoney){
        String profit = "";
        for (StrategyData dataModel : consumeMoneyList){
            String [] rule = dataModel.getStgValue().split("-");
            String ratio = dataModel.getStgValueOne();
            if(rule[0].equals(rule[1])){
                double dbl = Double.parseDouble(rule[0]);
                if (Double.parseDouble(orderMoney) >= dbl){
                    profit = StringUtil.getMultiply(orderMoney, ratio);
                    break;
                }
            }else{
                double start = Double.parseDouble(rule[0]);
                double end = Double.parseDouble(rule[1]);
                if (Double.parseDouble(orderMoney) >= start && Double.parseDouble(orderMoney) <= end){
                    profit = StringUtil.getMultiply(orderMoney, ratio);
                    break;
                }
            }
        }
        return profit;
    }


    /**
     * @Description: check校验是否筛选出收款账号
     * @param didModel - 用户收款账号以及用户
     * @return
     * @author yoko
     * @date 2020/6/2 14:13
     */
    public static void checkDidAndByAddCollectionAccountOrder(DidModel didModel) throws Exception{
        if (didModel == null || didModel.getId() <= 0){
            throw new ServiceException(ErrorCode.ENUM_ERROR.OR00019.geteCode(), ErrorCode.ENUM_ERROR.OR00019.geteDesc());
        }
    }


    /**
     * @Description: 组装添加派单数据的方法-支付宝
     * @param did -  用户ID
     * @param orderNo - 订单号
     * @param orderMoney - 订单金额
     * @param outTradeNo - 商家订单号
     * @param notifyUlr - 同步地址
     * @param didModel - 用户收款账号信息
     * @return com.hz.fine.master.core.model.order.OrderModel
     * @author yoko
     * @date 2020/6/2 14:53
     */
    public static OrderModel assembleOrderByZfbAdd(long did, String orderNo, String orderMoney, String notifyUlr, String outTradeNo,
                                                DidModel didModel, int collectionType, List<StrategyData> consumeMoneyList){
        OrderModel resBean = new OrderModel();
        resBean.setDid(did);
        resBean.setOrderNo(orderNo);
        resBean.setOrderMoney(orderMoney);
        resBean.setCollectionAccountId(didModel.getCollectionAccountId());
        resBean.setCollectionType(collectionType);
        if (!StringUtils.isBlank(outTradeNo)){
            resBean.setOutTradeNo(outTradeNo);
        }
        if (!StringUtils.isBlank(notifyUlr)){
            resBean.setNotifyUrl(notifyUlr);
        }
        // 订单失效时间
        String invalidTime = DateUtil.addDateMinute(5);// 目前默认5分钟：后续可以从策略取数据
        resBean.setInvalidTime(invalidTime);
        resBean.setUserId(didModel.getUserId());
        resBean.setZfbAcNum(didModel.getZfbAcNum());
        String profit = getConsumeProfit(consumeMoneyList, orderMoney);
        resBean.setProfit(profit);
        resBean.setCurday(DateUtil.getDayNumber(new Date()));
        resBean.setCurhour(DateUtil.getHour(new Date()));
        resBean.setCurminute(DateUtil.getCurminute(new Date()));
        return resBean;
    }


    /**
     * @Description: 组装扣除用户余额流水的数据的方法
     * @param did - 用户ID
     * @param orderNo - 订单号
     * @param money - 订单金额
     * @return com.hz.fine.master.core.model.did.DidBalanceDeductModel
     * @author yoko
     * @date 2020/7/2 14:52
     */
    public static DidBalanceDeductModel assembleDidBalanceDeductAdd(long did, String orderNo, String money){
        DidBalanceDeductModel resBean = new DidBalanceDeductModel();
        resBean.setDid(did);
        resBean.setOrderNo(orderNo);
        resBean.setMoney(money);
        resBean.setCurday(DateUtil.getDayNumber(new Date()));
        resBean.setCurhour(DateUtil.getHour(new Date()));
        resBean.setCurminute(DateUtil.getCurminute(new Date()));
        return resBean;
    }

    /**
     * @Description: 组装更新用户金额的方法
     * @param did - 用户ID
     * @param orderMoney - 派单的具体金额
     * @return com.hz.fine.master.core.model.did.DidModel
     * @author yoko
     * @date 2020/6/9 10:47
     */
    public static DidModel assembleUpdateDidBalance(long did, String orderMoney){
        DidModel resBean = new DidModel();
        resBean.setId(did);
        resBean.setOrderMoney(orderMoney);
        return resBean;
    }


    /**
     * @Description: 用户派单成功的数据组装返回客户端的方法-支付宝
     * @param stime - 服务器的时间
     * @param sign - 签名
     * @param orderModel - 用户派单的详情
     * @param returnUrl - 支付完成之后自动跳转的地址
     * @param zfbQrCodeUrl - 生成的HTML页面的地址
     * @return java.lang.String
     * @author yoko
     * @date 2019/11/25 22:45
     */
    public static String assembleOrderZfbQrCodeDataResult(long stime, String sign, OrderModel orderModel, String returnUrl, String zfbQrCodeUrl) throws Exception{
        ResponseOrder dataModel = new ResponseOrder();
        if (orderModel != null){
            OrderDistribution order = new OrderDistribution();
            order.orderNo = orderModel.getOrderNo();
            order.qrCode = orderModel.getQrCode();
            order.orderMoney = orderModel.getOrderMoney();
            order.invalidTime = orderModel.getInvalidTime();
            int invalidSecond = DateUtil.calLastedTime(orderModel.getInvalidTime());
            order.invalidSecond = String.valueOf(invalidSecond);
            String resQrCodeUrl = "";
            if (!StringUtils.isBlank(returnUrl)){
                resQrCodeUrl = zfbQrCodeUrl + "?" + "orderNo=" +  orderModel.getOrderNo() + "&" + "returnUrl=" + returnUrl;
            }else {
                resQrCodeUrl = zfbQrCodeUrl + "?" + "orderNo=" +  orderModel.getOrderNo() + "&" + "returnUrl=";
            }
            order.qrCodeUrl = URLEncoder.encode(resQrCodeUrl,"UTF-8");
            dataModel.order = order;
        }
        dataModel.setStime(stime);
        dataModel.setSign(sign);
        return JSON.toJSONString(dataModel);
    }


    /**
     * @Description: check校验数据获取支付宝派单数据-详情-返回码的接口时
     * @param requestModel
     * @return
     * @author yoko
     * @date 2020/05/14 15:57
     */
    public static void checkZfbOrderByQrCodeData(RequestOrder requestModel) throws Exception{
        // 1.校验所有数据
        if (requestModel == null ){
            throw new ServiceException(ErrorCode.ENUM_ERROR.OR00020.geteCode(), ErrorCode.ENUM_ERROR.OR00020.geteDesc());
        }

        if (StringUtils.isBlank(requestModel.orderNo)){
            throw new ServiceException(ErrorCode.ENUM_ERROR.OR00021.geteCode(), ErrorCode.ENUM_ERROR.OR00021.geteDesc());
        }

    }


    /**
     * @Description: 支付宝派单的数据组装返回客户端的方法
     * @param stime - 服务器的时间
     * @param sign - 签名
     * @param orderModel - 用户派单的详情
     * @return java.lang.String
     * @author yoko
     * @date 2019/11/25 22:45
     */
    public static String assembleZfbOrderDataResult(long stime, String sign, OrderModel orderModel){
        ResponseOrder dataModel = new ResponseOrder();
        if (orderModel != null){
            OrderDistribution order = new OrderDistribution();
            order.orderNo = orderModel.getOrderNo();
            order.orderMoney = orderModel.getOrderMoney();
            order.invalidTime = orderModel.getInvalidTime();
            order.invalidSecond = orderModel.getInvalidSecond();
            order.userId = orderModel.getUserId();
            order.zfbAcNum = orderModel.getZfbAcNum();
            Map<String, Object> map = getZfbKey();
            order.dataType = Integer.parseInt(map.get("dataType").toString());
            order.key = map.get("key").toString();
            dataModel.order = order;
        }
        dataModel.setStime(stime);
        dataModel.setSign(sign);
        return JSON.toJSONString(dataModel);
    }

    /**
     * @Description: 生成秘钥给客户端使用
     * @return
     * @author yoko
     * @date 2020/7/2 18:47
    */
    public static Map<String, Object> getZfbKey(){
        int random = (int)(Math.random()*10+1);
        String key = UUID.randomUUID().toString().replaceAll("\\-", "");
        if (random == 1 || random == 9){
            key = MD5Util.getMD5String(key).substring(0, 10).toUpperCase();
        }else if(random == 2 || random == 8){
            key = MD5Util.getMD5String(key).substring(0, 11).toUpperCase();
        }else if(random == 3 || random == 7){
            key = MD5Util.getMD5String(key).substring(0, 12).toUpperCase();
        }else if(random == 4 || random == 6){
            key = MD5Util.getMD5String(key).substring(0, 13).toUpperCase();
        }else if(random == 5 || random == 10){
            key = MD5Util.getMD5String(key).substring(0, 14).toUpperCase();
        }

        System.out.println("key:" + key);
        Map<String, Object> map = new HashMap<>();
        map.put("dataType", random);
        map.put("key", key);
        return map;
    }


    /**
     * @Description: 随机获取一张银行卡
     * @param bankList
     * @return
     * @author yoko
     * @date 2020/7/3 16:18
    */
    public static BankModel screenBank(List<BankModel> bankList){
        int random = new Random().nextInt(bankList.size());
        BankModel bankModel = bankList.get(random);
        return bankModel;
    }


    /**
     * @Description: check校验数据点击我要买-展现可用金额信息时
     * @param requestModel
     * @return
     * @author yoko
     * @date 2020/05/14 15:57
     */
    public static long checkGetMoneyListData(RequestBank requestModel) throws Exception{
        long did;
        // 1.校验所有数据
        if (requestModel == null ){
            throw new ServiceException(ErrorCode.ENUM_ERROR.BK00004.geteCode(), ErrorCode.ENUM_ERROR.BK00004.geteDesc());
        }
        // 校验token值
        if (StringUtils.isBlank(requestModel.token)){
            throw new ServiceException(ErrorCode.ENUM_ERROR.D00001.geteCode(), ErrorCode.ENUM_ERROR.D00001.geteDesc());
        }

        // 校验用户是否登录
        did = HodgepodgeMethod.checkIsLogin(requestModel.token);

        return did;

    }

    /**
     * @Description: check随机筛选的金额集合数据
     * @param moneyList - 金额集合
     * @return
     * @author yoko
     * @date 2020/7/3 16:59
    */
    public static void checkMoneyList(List<String> moneyList) throws Exception{
        if (moneyList == null || moneyList.size() <= 0){
            throw new ServiceException(ErrorCode.ENUM_ERROR.BK00005.geteCode(), ErrorCode.ENUM_ERROR.BK00005.geteDesc());
        }
    }


    /**
     * @Description: 点击我要买-展现可用金额信息数据组装返回客户端的方法-集合
     * @param stime - 服务器的时间
     * @param sign - 签名
     * @param order - 银行卡主键ID
     * @param moneyList - 金额列表集合
     * @param rowCount - 总行数
     * @return java.lang.String
     * @author yoko
     * @date 2019/11/25 22:45
     */
    public static String assembleBankMoneyListResult(long stime, String sign,String order, List<String> moneyList, Integer rowCount){
        ResponseBank dataModel = new ResponseBank();
        if (moneyList != null && moneyList.size() > ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_ZERO){
            BankMoney bankMoney = new BankMoney();
            bankMoney.order = order;
            bankMoney.moneyList = moneyList;
            dataModel.bankMoney = bankMoney;
        }
        if (rowCount != null){
            dataModel.rowCount = rowCount;
        }
        dataModel.setStime(stime);
        dataModel.setSign(sign);
        return JSON.toJSONString(dataModel);
    }


    /**
     * @Description: check校验数据获取用户是否有充值挂单
     * @param requestModel
     * @return
     * @author yoko
     * @date 2020/05/14 15:57
     */
    public static long checkHaveOrderData(RequestDidRecharge requestModel) throws Exception{
        long did;
        // 1.校验所有数据
        if (requestModel == null ){
            throw new ServiceException(ErrorCode.ENUM_ERROR.DR00021.geteCode(), ErrorCode.ENUM_ERROR.DR00021.geteDesc());
        }
        // 校验token值
        if (StringUtils.isBlank(requestModel.token)){
            throw new ServiceException(ErrorCode.ENUM_ERROR.D00001.geteCode(), ErrorCode.ENUM_ERROR.D00001.geteDesc());
        }

        // 校验用户是否登录
        did = HodgepodgeMethod.checkIsLogin(requestModel.token);

        return did;

    }


    /**
     * @Description: 获取用户是否有充值挂单数据组装返回客户端的方法
     * @param stime - 服务器的时间
     * @param sign - 签名
     * @param responseDidRecharge - 存储在缓存里面的挂单信息
     * @param haveType - 用户名下是否充值挂单：1没有挂单，2有挂单
     * @return java.lang.String
     * @author yoko
     * @date 2019/11/25 22:45
     */
    public static String assembleDidRechargeHaveOrderDataResult(long stime, String sign, ResponseDidRecharge responseDidRecharge, int haveType){
        ResponseDidRecharge dataModel = new ResponseDidRecharge();
        if (responseDidRecharge != null && responseDidRecharge.recharge != null && !StringUtils.isBlank(responseDidRecharge.recharge.orderNo)){
            dataModel = responseDidRecharge;
        }
        dataModel.haveType = haveType;
        dataModel.setStime(stime);
        dataModel.setSign(sign);
        return JSON.toJSONString(dataModel);
    }


    /**
     * @Description: 获取用户是否有充值挂单数据组装返回客户端的方法
     * @param stime - 服务器的时间
     * @param sign - 签名
     * @param didRechargeModel - 数据库的挂单信息
     * @param bankModel - 银行卡信息
     * @param haveType - 用户名下是否充值挂单：1没有挂单，2有挂单
     * @return java.lang.String
     * @author yoko
     * @date 2019/11/25 22:45
     */
    public static String assembleDidRechargeHaveOrderByQueryDataResult(long stime, String sign, DidRechargeModel didRechargeModel, BankModel bankModel, int haveType){
        ResponseDidRecharge dataModel = new ResponseDidRecharge();
        if (didRechargeModel != null && didRechargeModel.getId() > 0 && bankModel != null && bankModel.getId() > 0){
            RechargeInfo recharge = BeanUtils.copy(didRechargeModel, RechargeInfo.class);
            if (!StringUtils.isBlank(bankModel.getBankName())){
                recharge.bankName = bankModel.getBankName();
            }
            if (!StringUtils.isBlank(bankModel.getBankCard())){
                recharge.bankCard = bankModel.getBankCard();
            }
            if (!StringUtils.isBlank(bankModel.getSubbranchName())){
                recharge.subbranchName = bankModel.getSubbranchName();
            }
            if (!StringUtils.isBlank(bankModel.getAccountName())){
                recharge.accountName = bankModel.getAccountName();
            }
            dataModel.recharge = recharge;
        }
        dataModel.haveType = haveType;
        dataModel.setStime(stime);
        dataModel.setSign(sign);
        return JSON.toJSONString(dataModel);
    }


    /**
     * @Description: check校验数据当用户正式充值-拉起充值
     * @param requestModel
     * @return
     * @author yoko
     * @date 2020/05/14 15:57
     */
    public static long checkRechargeBuyOrder(RequestDidRecharge requestModel) throws Exception{

        long did;
        // 1.校验所有数据
        if (requestModel == null ){
            throw new ServiceException(ErrorCode.ENUM_ERROR.DR00023.geteCode(), ErrorCode.ENUM_ERROR.DR00023.geteDesc());
        }

        // 校验充值金额值
        if (StringUtils.isBlank(requestModel.orderMoney)){
            throw new ServiceException(ErrorCode.ENUM_ERROR.DR00024.geteCode(), ErrorCode.ENUM_ERROR.DR00024.geteDesc());
        }else {
            // 金额是否有效
            if (requestModel.orderMoney.indexOf(".") > -1){
                boolean flag = StringUtil.isNumberByMoney(requestModel.orderMoney);
                if (!flag){
                    throw new ServiceException(ErrorCode.ENUM_ERROR.DR00025.geteCode(), ErrorCode.ENUM_ERROR.DR00025.geteDesc());
                }
            }else {
                boolean flag = StringUtil.isNumer(requestModel.orderMoney);
                if (!flag){
                    throw new ServiceException(ErrorCode.ENUM_ERROR.DR00026.geteCode(), ErrorCode.ENUM_ERROR.DR00026.geteDesc());
                }
            }
        }

        // 校验order
        if (StringUtils.isBlank(requestModel.order)){
            throw new ServiceException(ErrorCode.ENUM_ERROR.DR00027.geteCode(), ErrorCode.ENUM_ERROR.DR00027.geteDesc());
        }

        // 校验token值
        if (StringUtils.isBlank(requestModel.token)){
            throw new ServiceException(ErrorCode.ENUM_ERROR.D00001.geteCode(), ErrorCode.ENUM_ERROR.D00001.geteDesc());
        }

        // 校验用户是否登录
        did = HodgepodgeMethod.checkIsLogin(requestModel.token);

        return did;

    }

    /**
     * @Description: 获取redis存储的银行卡主键ID
     * @param did - 用户ID
     * @param order - 订单
     * @return long
     * @author yoko
     * @date 2020/7/4 13:57
     */
    public static long getBankIdByRedis(long did, String order) throws Exception{
        long bankId = 0;
        String strKeyCache = CachedKeyUtils.getCacheKey(CacheKey.BANK_ID_BY_SGID, did, order);
        String strCache = (String) ComponentUtil.redisService.get(strKeyCache);
        if (!StringUtils.isBlank(strCache)) {
            // 从缓存里面获取银行卡主键ID
            bankId = Long.parseLong(strCache);
        }else {
            throw new ServiceException(ErrorCode.ENUM_ERROR.DR00028.geteCode(), ErrorCode.ENUM_ERROR.DR00028.geteDesc());
        }
        return bankId;
    }


    /**
     * @Description: 组装查询银行卡旗下订单金额是否有挂单的查询方法
     * @param bankId - 银行卡主键ID
     * @param orderMoney - 充值金额
     * @return
     * @author yoko
     * @date 2020/7/4 14:19
    */
    public static DidRechargeModel assembleDidRechargeQuery(long bankId, String orderMoney){
        DidRechargeModel resBean = new DidRechargeModel();
        resBean.setBankId(bankId);
        resBean.setDistributionMoney(orderMoney);
        resBean.setOrderStatus(1);
        return resBean;
    }

    /**
     * @Description: check金额是否被抢占
     * @param didRechargeModel
     * @return
     * @author yoko
     * @date 2020/7/4 14:25
    */
    public static void checkBankMoneyIsHave(DidRechargeModel didRechargeModel) throws Exception{
        if (didRechargeModel != null && didRechargeModel.getId() > 0 ){
            throw new ServiceException(ErrorCode.ENUM_ERROR.DR00030.geteCode(), ErrorCode.ENUM_ERROR.DR00030.geteDesc());
        }
    }


    /**
     * @Description: 公告集合的数据组装返回客户端的方法
     * @param stime - 服务器的时间
     * @param sign - 签名
     * @param noticeList - 公告集合
     * @param rowCount - 总行数
     * @return java.lang.String
     * @author yoko
     * @date 2019/11/25 22:45
     */
    public static String assembleNoticeResult(long stime, String sign, List<NoticeModel> noticeList, Integer rowCount){
        ResponseNotice dataModel = new ResponseNotice();
        if (noticeList != null && noticeList.size() > ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_ZERO){
            List<Notice> dataList = BeanUtils.copyList(noticeList, Notice.class);
            dataModel.dataList = dataList;
        }
        if (rowCount != null){
            dataModel.rowCount = rowCount;
        }
        dataModel.setStime(stime);
        dataModel.setSign(sign);
        return JSON.toJSONString(dataModel);
    }

    /**
     * @Description: 获取公告信息时，校验数据
     * @param requestModel - 用户数据
     * @return void
     * @author yoko
     * @date 2019/11/21 18:59
     */
    public static void checkNoticeGetData(RequestNotice requestModel) throws Exception{
        long did;
        // 校验所有数据
        if (requestModel == null ){
            throw new ServiceException(ErrorCode.ENUM_ERROR.N00001.geteCode(), ErrorCode.ENUM_ERROR.N00001.geteDesc());
        }

        // 校验ID值
        if (requestModel.id == null ){
            throw new ServiceException(ErrorCode.ENUM_ERROR.N00002.geteCode(), ErrorCode.ENUM_ERROR.N00002.geteDesc());
        }
    }


    /**
     * @Description: 公告-详情的数据组装返回客户端的方法
     * @param stime - 服务器的时间
     * @param sign - 签名
     * @param noticeModel - 公告的详情
     * @return java.lang.String
     * @author yoko
     * @date 2019/11/25 22:45
     */
    public static String assembleNoticeDataResult(long stime, String sign, NoticeModel noticeModel){
        ResponseNotice dataModel = new ResponseNotice();
        if (noticeModel != null){
            Notice data = BeanUtils.copy(noticeModel, Notice.class);
            dataModel.dataModel = data;
        }
        dataModel.setStime(stime);
        dataModel.setSign(sign);
        return JSON.toJSONString(dataModel);
    }


    /**
     * @Description: 客户端升级的数据组装返回客户端的方法
     * @param stime - 服务器的时间
     * @param sign - 签名
     * @param upgradeModel - 客户端更新要更新的数据
     * @return java.lang.String
     * @author yoko
     * @date 2019/11/25 22:45
     */
    public static String assembleUpgradeDataResult(long stime, String sign, UpgradeModel upgradeModel){
        ResponseUpgrade dataModel = new ResponseUpgrade();
        if (upgradeModel != null){
            dataModel = BeanUtils.copy(upgradeModel, ResponseUpgrade.class);
        }

        dataModel.setStime(stime);
        dataModel.setSign(sign);
        return JSON.toJSONString(dataModel);
    }

    /**
     * @Description: 组装查询百问百答详情的查询条件
     * @param questionMId - 百问百答-类别的主键ID
     * @return
     * @author yoko
     * @date 2020/7/6 12:25
    */
    public static QuestionDModel assembleQuestionDQuery(long questionMId){
        QuestionDModel resBean = new QuestionDModel();
        resBean.setQuestionMId(questionMId);
        return resBean;
    }

    /**
     * @Description: 在线客服、咨询集合的数据组装返回客户端的方法
     * @param stime - 服务器的时间
     * @param sign - 签名
     * @param consultList - 在线客服、咨询集合
     * @param rowCount - 总行数
     * @return java.lang.String
     * @author yoko
     * @date 2019/11/25 22:45
     */
    public static String assembleConsultDataListResult(long stime, String sign, List<ConsultModel> consultList, Integer rowCount){
        ResponseConsult dataModel = new ResponseConsult();
        if (consultList != null && consultList.size() > ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_ZERO){
            List<Consult> dataList = BeanUtils.copyList(consultList, Consult.class);
            dataModel.dataList = dataList;
        }
        if (rowCount != null){
            dataModel.rowCount = rowCount;
        }
        dataModel.setStime(stime);
        dataModel.setSign(sign);
        return JSON.toJSONString(dataModel);
    }

    /**
     * @Description: 组装查询符合给与消耗奖励订单的查询方法
     * @param did - 用户ID
     * @param orderNo - 订单号
     * @return com.hz.fine.master.core.model.order.OrderModel
     * @author yoko
     * @date 2020/7/7 18:43
     */
    public static OrderModel assembleOrderByRewardQuery(long did, String orderNo){
        OrderModel resBean = new OrderModel();
        resBean.setDid(did);
        resBean.setOrderNo(orderNo);
        return resBean;
    }


    /**
     * @Description: 组装查询客户端监听的收款信息的方法
     * @param did - 用户ID
     * @param userId - 支付宝账号ID
     * @return
     * @author yoko
     * @date 2020/7/7 19:50
    */
    public static ClientCollectionDataModel assembleClientCollectionDataQuery(long did, String userId) throws Exception{
        ClientCollectionDataModel resBean = new ClientCollectionDataModel();
        resBean.setDid(did);
        resBean.setUserId(userId);
        String startTime = DateUtil.addDateMinute(-5);
        String endTime = DateUtil.getNowPlusTime();
        resBean.setStartTime(startTime);
        resBean.setEndTime(endTime);
        return resBean;
    }

    /**
     * @Description: 组装根据订单号，奖励状态查询奖励数据
     * @param orderNo - 订单号
     * @param rewardType - 奖励类型：1充值奖励，2充值总金额档次奖励，3直推奖励，4裂变奖励，5团队奖励，6订单成功消耗奖励
     * @return com.hz.fine.master.core.model.did.DidRewardModel
     * @author yoko
     * @date 2020/7/7 20:12
     */
    public static DidRewardModel assembleDidRewardQueryByOrderNo(String orderNo, int rewardType){
        DidRewardModel resBean = new DidRewardModel();
        resBean.setOrderNo(orderNo);
        resBean.setRewardType(rewardType);
        return resBean;
    }

    /**
     * @Description: 组装添加奖励数据的方法
     * @param orderModel - 订单信息
     * @param rewardType - 奖励类型：1充值奖励，2充值总金额档次奖励，3直推奖励，4裂变奖励，5团队奖励，6订单成功消耗奖励
     * @return
     * @author yoko
     * @date 2020/7/7 20:25
    */
    public static DidRewardModel assembleDidRewardAdd(OrderModel orderModel, int rewardType){
        DidRewardModel resBean = new DidRewardModel();
        resBean.setDid(orderModel.getDid());
        resBean.setOrderNo(orderModel.getOrderNo());
        resBean.setMoney(orderModel.getProfit());
        resBean.setRewardType(rewardType);
        resBean.setProof(orderModel.getOrderMoney());
        resBean.setOrigin(orderModel.getOrderMoney());
        resBean.setOriginIid(orderModel.getDid());
        resBean.setCurday(DateUtil.getDayNumber(new Date()));
        resBean.setCurhour(DateUtil.getHour(new Date()));
        resBean.setCurminute(DateUtil.getCurminute(new Date()));
        return resBean;
    }



    public static void main(String [] args){
        String bankWorkTime = "09:00-14:10";
        int num = getOpenTypeByBankWork(bankWorkTime);
        System.out.println("num:" + num);

        List<StrategyData> list = new ArrayList<>();
        StrategyData bean1 = new StrategyData();
        bean1.setId(1L);
        bean1.setStgKey(1L);
        bean1.setStgValue("1000.00");
        bean1.setStgValueOne("0.01");

        StrategyData bean2 = new StrategyData();
        bean2.setId(2L);
        bean2.setStgKey(2L);
        bean2.setStgValue("2000.00");
        bean2.setStgValueOne("0.02");

        StrategyData bean3 = new StrategyData();
        bean3.setId(3L);
        bean3.setStgKey(3L);
        bean3.setStgValue("3000.00");
        bean3.setStgValueOne("0.03");

        StrategyData bean4 = new StrategyData();
        bean4.setId(4L);
        bean4.setStgKey(4L);
        bean4.setStgValue("4000.00");
        bean4.setStgValueOne("0.04");

        StrategyData bean5 = new StrategyData();
        bean5.setId(5L);
        bean5.setStgKey(5L);
        bean5.setStgValue("5000.00");
        bean5.setStgValueOne("0.05");

        list.add(bean1);
        list.add(bean2);
        list.add(bean3);
        list.add(bean4);
        list.add(bean5);
        String str = JSON.toJSONString(list);
        System.out.println(str);
//        String money = "-552.254";
        String money = "1111";
        double m1 = Double.parseDouble(money);
        if (m1 >0){
            System.out.println("m1大于0:" + m1);
        }else {
            System.out.println("m1小于0:" + m1);
        }

        int sb1 = getRandom();
        System.out.println("sb1:" + sb1);

        Map<String, Object> map = getZfbKey();

        String sb3 = "1000";
        int sb4 = Integer.parseInt(sb3);
        System.out.println("sb4:" + sb4);

    }




    

}
