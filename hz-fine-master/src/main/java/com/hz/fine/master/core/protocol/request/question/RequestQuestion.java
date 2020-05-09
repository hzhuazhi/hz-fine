package com.hz.fine.master.core.protocol.request.question;



import com.hz.fine.master.core.protocol.base.BaseRequest;

import java.io.Serializable;

/**
 * @Description 百问百科
 * @Author yoko
 * @Date 2020/1/7 17:31
 * @Version 1.0
 */
public class RequestQuestion extends BaseRequest implements Serializable {
    private static final long   serialVersionUID = 1233223332140L;

    public Long id;

    public Long questionMId;

    public String searchKey;

    public RequestQuestion(){

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getQuestionMId() {
        return questionMId;
    }

    public void setQuestionMId(Long questionMId) {
        this.questionMId = questionMId;
    }

    public String getSearchKey() {
        return searchKey;
    }

    public void setSearchKey(String searchKey) {
        this.searchKey = searchKey;
    }
}
