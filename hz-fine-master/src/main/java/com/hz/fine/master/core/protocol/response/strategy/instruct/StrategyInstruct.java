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
     * 结束指令回复
     */
    public String endInstruct;

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

    public String getEndInstruct() {
        return endInstruct;
    }

    public void setEndInstruct(String endInstruct) {
        this.endInstruct = endInstruct;
    }
}
