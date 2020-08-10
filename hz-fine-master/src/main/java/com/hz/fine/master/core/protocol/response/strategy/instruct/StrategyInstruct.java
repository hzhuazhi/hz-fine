package com.hz.fine.master.core.protocol.response.strategy.instruct;

import java.io.Serializable;

/**
 * @Description 微信群回复指令
 * @Author yoko
 * @Date 2020/7/21 14:30
 * @Version 1.0
 */
public class StrategyInstruct implements Serializable {
    private static final long   serialVersionUID = 1233023331160L;

    /**
     * 成功指令回复
     */
    public String successInstruct;

    /**
     * 失败指令回复
     */
    public String failInstruct;

    /**
     * 群审核指令回复
     */
    public String checkInstruct;

    /**
     * 坏指令：指当收款账号无法收钱时，在群里面回复的指令
     */
    public String badInstruct;

    public StrategyInstruct(){

    }


    public String getSuccessInstruct() {
        return successInstruct;
    }

    public void setSuccessInstruct(String successInstruct) {
        this.successInstruct = successInstruct;
    }

    public String getFailInstruct() {
        return failInstruct;
    }

    public void setFailInstruct(String failInstruct) {
        this.failInstruct = failInstruct;
    }


    public String getCheckInstruct() {
        return checkInstruct;
    }

    public void setCheckInstruct(String checkInstruct) {
        this.checkInstruct = checkInstruct;
    }

    public String getBadInstruct() {
        return badInstruct;
    }

    public void setBadInstruct(String badInstruct) {
        this.badInstruct = badInstruct;
    }
}
