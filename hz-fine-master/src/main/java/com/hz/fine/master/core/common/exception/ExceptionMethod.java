package com.hz.fine.master.core.common.exception;

import com.hz.fine.master.core.common.utils.constant.ErrorCode;
import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description TODO
 * @Author long
 * @Date 2019/11/25 10:54
 * @Version 1.0
 */
public class ExceptionMethod {
    /**
     * @Description: TODO
     * @param e  异常信息，包括自定义异常，未捕捉异常等
    * @param type  1、显示自定义异常code 码  2、显示系统状态码
     * @return java.util.Map<java.lang.String,java.lang.String>
     * @author long
     * @date 2019/12/19 15:08
     */
    public  static Map<String,String> getException(Exception e,Integer type){
        Map<String,String>  map   =  new HashMap<>();
        String code = ""; // 返回给客户端的状态码
        String message = ""; // 返回给客户端的错误信息
        String dbCode = ""; // 插入数据库的状态码
        String dbMessage = ""; //插入数据库的错误信息
        if (e instanceof ServiceException){
            if (!StringUtils.isBlank(((ServiceException) e).getCode())){
                dbCode = ((ServiceException) e).getCode();
                code = ErrorCode.ERROR_CONSTANT.DEFAULT_SERVICE_EXCEPTION_ERROR_CODE;
                message = e.getMessage();
            }else {
                code = ErrorCode.ERROR_CONSTANT.DEFAULT_SERVICE_EXCEPTION_ERROR_CODE;
                message = ErrorCode.ERROR_CONSTANT.DEFAULT_SERVICE_EXCEPTION_ERROR_MESSAGE;
            }

            if(!StringUtils.isBlank(((ServiceException) e).getCode()) && type==1){
//            db&&Code = ((ServiceException) e).getCode();
                code = ((ServiceException) e).getCode();
                message = e.getMessage();
            }
        }else {
            code = ErrorCode.ERROR_CONSTANT.DEFAULT_EXCEPTION_ERROR_CODE;
            message = ErrorCode.ERROR_CONSTANT.DEFAULT_EXCEPTION_ERROR_MESSAGE;
        }



        // 获取录入数据库的错误信息
        if (!StringUtils.isBlank(dbCode)){
            dbMessage = getErrorDbDesc(dbCode);
        }
        map.put("code",code);
        map.put("message",message);
        map.put("dbCode", dbCode);
        map.put("dbMessage", dbMessage);
        return map;
    }

    /**
     * @Description: 通过枚举循环获取要插入数据库的错误信息
     * @param code - 错误码
     * @return String - 要录入数据库的错误信息
     * @author yoko
     * @date 2019/12/16 18:03
    */
    public static String getErrorDbDesc(String code){
        String dbDesc = "";
        // 错误信息-A
        for (ErrorCode.ENUM_ERROR enums : ErrorCode.ENUM_ERROR.values()){
            if (enums.geteCode().equals(code)){
                dbDesc = enums.getDbDesc();
                break;
            }
        }

        return dbDesc;
    }
}
