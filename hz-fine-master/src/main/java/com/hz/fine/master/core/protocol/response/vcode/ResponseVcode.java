package com.hz.fine.master.core.protocol.response.vcode;

import com.hz.fine.master.core.protocol.base.BaseResponse;
import com.hz.fine.master.core.protocol.response.question.QuestionD;
import com.hz.fine.master.core.protocol.response.question.QuestionM;

import java.io.Serializable;

/**
 * @Description 响应的验证码的实体bean
 * @Author yoko
 * @Date 2020/5/14 16:43
 * @Version 1.0
 */
public class ResponseVcode extends BaseResponse implements Serializable {
    private static final long   serialVersionUID = 1233023331141L;

    /**
     * 验证码-通过的的token
     */
    public Vcode dataModel;

    public ResponseVcode(){

    }

    public Vcode getDataModel() {
        return dataModel;
    }

    public void setDataModel(Vcode dataModel) {
        this.dataModel = dataModel;
    }
}
