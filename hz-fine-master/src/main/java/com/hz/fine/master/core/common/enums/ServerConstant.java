package com.hz.fine.master.core.common.enums;

/**
 * @Description TODO
 * @Author long
 * @Date 2020/1/2 17:13
 * @Version 1.0
 */
public enum ServerConstant {
    R_PHONE_LOGIN("ROOO1", "AppealController.getActiveData", "手机号码已经注册!"),
    R_VERIFY_CODE_INVALID("ROOO2", "AppealController.getActiveData", "无效的邀请码！"),
    R_VERIFY_CODE_IS_MAX("ROOO3", "AppealController.getActiveData", "发送验证码已达到上线！"),
    R_IDENTIFYING_CODE("ROOO4", "AppealController.getActiveData", "发送短信失败！"),
    ;
    private String   code;
    private String className;
    private String result;

    private ServerConstant(String code, String className, String result) {
        this.code = code;
        this.className = className;
        this.result = result;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
