package com.hz.fine.master.core.common.utils.constant;

/**
 * @Description task定时任务的错误码
 * @Author yoko
 * @Date 2020/1/11 17:23
 * @Version 1.0
 */
public class TkErrorCode {

    /**
     * 异常-枚举类
     */
    public enum ENUM_ERROR {



        /***********************************************
         * T打头表示跑task定时任务时错误信息
         **********************************************/
        T000001("T000001","支付宝转账:我方订单号为空!","支付宝转账:我方订单号为空!"),
        T000002("T000002","支付宝转账:要转账的用户支付宝账号为空!","支付宝转账:要转账的用户支付宝账号为空!"),
        T000003("T000003","支付宝转账:支付宝归属账号的姓名为空!","支付宝转账:支付宝归属账号的姓名为空!"),
        R000004("000004","短信发送失败！",""),
        R000005("000005","手机号码已经被注册！","手机号码已经被注册！"),
        R000006("000006","验证码不正确！","验证码不正确！"),
        R000007("000007","验证码失效了，请重新获取","验证码失效了，请重新获取"),







        ;

        /**
         * 错误码
         */
        private String eCode;
        /**
         * 给客户端看的错误信息
         */
        private String eDesc;
        /**
         * 插入数据库的错误信息
         */
        private String dbDesc;




        private ENUM_ERROR(String eCode, String eDesc,String dbDesc) {
            this.eCode = eCode;
            this.eDesc = eDesc;
            this.dbDesc  = dbDesc;
        }

        public String geteCode() {
            return eCode;
        }

        public void seteCode(String eCode) {
            this.eCode = eCode;
        }

        public String geteDesc() {
            return eDesc;
        }

        public void seteDesc(String eDesc) {
            this.eDesc = eDesc;
        }

        public String getDbDesc() {
            return dbDesc;
        }

        public void setDbDesc(String dbDesc) {
            this.dbDesc = dbDesc;
        }
    }
}
