package com.hz.fine.master.util;

import com.alibaba.fastjson.JSON;
import com.hz.fine.master.core.common.exception.ServiceException;
import com.hz.fine.master.core.common.utils.BeanUtils;
import com.hz.fine.master.core.common.utils.DateUtil;
import com.hz.fine.master.core.common.utils.constant.CacheKey;
import com.hz.fine.master.core.common.utils.constant.CachedKeyUtils;
import com.hz.fine.master.core.common.utils.constant.ErrorCode;
import com.hz.fine.master.core.common.utils.constant.ServerConstant;
import com.hz.fine.master.core.model.bank.BankModel;
import com.hz.fine.master.core.model.did.*;
import com.hz.fine.master.core.model.mobilecard.MobileCardModel;
import com.hz.fine.master.core.model.order.OrderModel;
import com.hz.fine.master.core.model.question.QuestionDModel;
import com.hz.fine.master.core.model.question.QuestionMModel;
import com.hz.fine.master.core.model.region.RegionModel;
import com.hz.fine.master.core.model.strategy.StrategyData;
import com.hz.fine.master.core.model.strategy.StrategyModel;
import com.hz.fine.master.core.model.wx.WxClerkModel;
import com.hz.fine.master.core.protocol.request.did.RequestDid;
import com.hz.fine.master.core.protocol.request.did.RequestDidCollectionAccount;
import com.hz.fine.master.core.protocol.request.did.recharge.RequestDidRecharge;
import com.hz.fine.master.core.protocol.request.did.reward.RequestReward;
import com.hz.fine.master.core.protocol.request.order.RequestOrder;
import com.hz.fine.master.core.protocol.request.strategy.RequestStrategy;
import com.hz.fine.master.core.protocol.request.vcode.RequestVcode;
import com.hz.fine.master.core.protocol.response.ResponseData;
import com.hz.fine.master.core.protocol.response.did.ResponseDid;
import com.hz.fine.master.core.protocol.response.did.basic.DidBasic;
import com.hz.fine.master.core.protocol.response.did.collectionaccount.DidCollectionAccount;
import com.hz.fine.master.core.protocol.response.did.collectionaccount.ResponseDidCollectionAccount;
import com.hz.fine.master.core.protocol.response.did.recharge.DidRecharge;
import com.hz.fine.master.core.protocol.response.did.recharge.RechargeInfo;
import com.hz.fine.master.core.protocol.response.did.recharge.ResponseDidRecharge;
import com.hz.fine.master.core.protocol.response.did.reward.DidReward;
import com.hz.fine.master.core.protocol.response.did.reward.ResponseDidReward;
import com.hz.fine.master.core.protocol.response.order.Order;
import com.hz.fine.master.core.protocol.response.order.ResponseOrder;
import com.hz.fine.master.core.protocol.response.question.QuestionD;
import com.hz.fine.master.core.protocol.response.question.QuestionM;
import com.hz.fine.master.core.protocol.response.question.ResponseQuestion;
import com.hz.fine.master.core.protocol.response.strategy.ResponseStrategy;
import com.hz.fine.master.core.protocol.response.strategy.money.StrategyMoney;
import com.hz.fine.master.core.protocol.response.strategy.money.StrategyMoneyGrade;
import com.hz.fine.master.core.protocol.response.strategy.qiniu.QiNiu;
import com.hz.fine.master.core.protocol.response.strategy.share.StrategyShare;
import com.hz.fine.master.core.protocol.response.vcode.ResponseVcode;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;


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
        if (requestModel.vType == ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_ONE){
            // 类型等于1表示注册，所以无需登录
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
//        HodgepodgeMethod.checkVcode(ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_ONE, requestModel.acNum, requestModel.vcode);

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

        // 校验账号是否为空
        if (StringUtils.isBlank(requestModel.acNum)){
            throw new ServiceException(ErrorCode.ENUM_ERROR.D00018.geteCode(), ErrorCode.ENUM_ERROR.D00018.geteDesc());
        }

        // 校验登录密码是否为空
        if (StringUtils.isBlank(requestModel.newPassWd)){
            throw new ServiceException(ErrorCode.ENUM_ERROR.D00019.geteCode(), ErrorCode.ENUM_ERROR.D00019.geteDesc());
        }

        // 校验注册时的验证码
        if (StringUtils.isBlank(requestModel.vcode)){
            throw new ServiceException(ErrorCode.ENUM_ERROR.D00020.geteCode(), ErrorCode.ENUM_ERROR.D00020.geteDesc());
        }

        // 校验验证码-忘记密码的验证码
//        HodgepodgeMethod.checkVcode(ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_TWO, requestModel.acNum, requestModel.vcode);

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

        // 校验收款的具体账号：类型为微信则微信账号，支付宝为支付宝账号；怕后期有其它冲突
        if (requestModel.acType != 3){
            if (StringUtils.isBlank(requestModel.acNum)){
                throw new ServiceException(ErrorCode.ENUM_ERROR.DC00003.geteCode(), ErrorCode.ENUM_ERROR.DC00003.geteDesc());
            }
        }

        if (StringUtils.isBlank(requestModel.mmQrCode)){
            throw new ServiceException(ErrorCode.ENUM_ERROR.DC00004.geteCode(), ErrorCode.ENUM_ERROR.DC00004.geteDesc());
        }

        // check收款人
        if (StringUtils.isBlank(requestModel.payee)){
            throw new ServiceException(ErrorCode.ENUM_ERROR.DC00005.geteCode(), ErrorCode.ENUM_ERROR.DC00005.geteDesc());
        }

        if (requestModel.acType == 3){
            // check银行名称/银行卡开户行
            if (StringUtils.isBlank(requestModel.bankName)){
                throw new ServiceException(ErrorCode.ENUM_ERROR.DC00006.geteCode(), ErrorCode.ENUM_ERROR.DC00006.geteDesc());
            }
        }


        // check经营范围类型
        if (requestModel.businessType == null || requestModel.businessType == 0){
            throw new ServiceException(ErrorCode.ENUM_ERROR.DC00007.geteCode(), ErrorCode.ENUM_ERROR.DC00007.geteDesc());
        }

        // check小微商户二维码图片地址
        if (requestModel.acType == 1){
            if (StringUtils.isBlank(requestModel.wxQrCodeAds)){
                throw new ServiceException(ErrorCode.ENUM_ERROR.DC00008.geteCode(), ErrorCode.ENUM_ERROR.DC00008.geteDesc());
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
     * @Description: 组装根据收款具体账号查询的查询条件
     * @param acNum - 收款具体账号
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

        // 校验收款账号类型
        if (requestModel.acType == null || requestModel.acType == 0){
            throw new ServiceException(ErrorCode.ENUM_ERROR.DC00016.geteCode(), ErrorCode.ENUM_ERROR.DC00016.geteDesc());
        }

        // 校验收款的具体账号：类型为微信则微信账号，支付宝为支付宝账号；怕后期有其它冲突
        if (requestModel.acType != 3){
            if (StringUtils.isBlank(requestModel.acNum)){
                throw new ServiceException(ErrorCode.ENUM_ERROR.DC00017.geteCode(), ErrorCode.ENUM_ERROR.DC00017.geteDesc());
            }
        }

        if (StringUtils.isBlank(requestModel.mmQrCode)){
            throw new ServiceException(ErrorCode.ENUM_ERROR.DC00018.geteCode(), ErrorCode.ENUM_ERROR.DC00018.geteDesc());
        }

        // check收款人
        if (StringUtils.isBlank(requestModel.payee)){
            throw new ServiceException(ErrorCode.ENUM_ERROR.DC00019.geteCode(), ErrorCode.ENUM_ERROR.DC00019.geteDesc());
        }

        if (requestModel.acType == 3){
            // check银行名称/银行卡开户行
            if (StringUtils.isBlank(requestModel.bankName)){
                throw new ServiceException(ErrorCode.ENUM_ERROR.DC00006.geteCode(), ErrorCode.ENUM_ERROR.DC00006.geteDesc());
            }
        }


        // check经营范围类型
        if (requestModel.businessType == null || requestModel.businessType == 0){
            throw new ServiceException(ErrorCode.ENUM_ERROR.DC00007.geteCode(), ErrorCode.ENUM_ERROR.DC00007.geteDesc());
        }

        // check小微商户二维码图片地址
        if (requestModel.acType == 1){
            if (StringUtils.isBlank(requestModel.wxQrCodeAds)){
                throw new ServiceException(ErrorCode.ENUM_ERROR.DC00020.geteCode(), ErrorCode.ENUM_ERROR.DC00020.geteDesc());
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
    public static void checkDidOrderByRedis(long did) throws Exception{
        String strKeyCache = CachedKeyUtils.getCacheKey(CacheKey.LOCK_DID_ORDER_INVALID_TIME, did);
        String strCache = (String) ComponentUtil.redisService.get(strKeyCache);
        if (!StringUtils.isBlank(strCache)) {
            // 说明还有充值订单未处理
            throw new ServiceException(ErrorCode.ENUM_ERROR.DR00005.geteCode(), ErrorCode.ENUM_ERROR.DR00005.geteDesc());
        }
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
     * @Description: 组装更新用户上传转账图片凭证的方法
     * @param requestModel - 用户的银行转账图片凭证
     * @param did - 用户ID
     * @return
     * @author yoko
     * @date 2020/5/21 15:57
    */
    public static DidRechargeModel assembleLoadPictureUpdate(RequestDidRecharge requestModel, long did){
        DidRechargeModel resBean = new DidRechargeModel();
        resBean.setDid(did);
        resBean.setOrderNo(requestModel.orderNo);
        resBean.setPictureAds(requestModel.pictureAds);
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
        }

        // 校验支付类型为空
        if (requestModel.payType == null || requestModel.payType == 0){
            throw new ServiceException(ErrorCode.ENUM_ERROR.OR00003.geteCode(), ErrorCode.ENUM_ERROR.OR00003.geteDesc());
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
        return resBean;
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
        resBean.setDid(did);
        resBean.setCollectionAccountId(collectionAccountId);
        resBean.setUseStatus(1);
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
        }else {
            share.shareAddress = shareAddres;
        }
        dataModel.share = share;
        dataModel.setStime(stime);
        dataModel.setSign(sign);
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
        if (orderModel != null){
            Order data = BeanUtils.copy(orderModel, Order.class);
            dataModel.dataModel = data;
        }
        dataModel.setStime(stime);
        dataModel.setSign(sign);
        return JSON.toJSONString(dataModel);
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
    }


    

}
