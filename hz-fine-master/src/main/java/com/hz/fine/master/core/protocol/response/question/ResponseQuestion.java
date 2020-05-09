package com.hz.fine.master.core.protocol.response.question;


import com.hz.fine.master.core.protocol.base.BaseResponse;

import java.io.Serializable;
import java.util.List;

/**
 * @Description TODO
 * @Author yoko
 * @Date 2020/1/7 17:39
 * @Version 1.0
 */
public class ResponseQuestion extends BaseResponse implements Serializable {
    private static final long   serialVersionUID = 1233023331141L;

    public List<QuestionM> qMList;
    public List<QuestionD> qDList;
    public QuestionD qD;
    public Integer rowCount;

    public ResponseQuestion(){

    }

    public List<QuestionM> getqMList() {
        return qMList;
    }

    public void setqMList(List<QuestionM> qMList) {
        this.qMList = qMList;
    }

    public List<QuestionD> getqDList() {
        return qDList;
    }

    public void setqDList(List<QuestionD> qDList) {
        this.qDList = qDList;
    }

    public QuestionD getqD() {
        return qD;
    }

    public void setqD(QuestionD qD) {
        this.qD = qD;
    }

    @Override
    public Integer getRowCount() {
        return rowCount;
    }

    @Override
    public void setRowCount(Integer rowCount) {
        this.rowCount = rowCount;
    }
}
