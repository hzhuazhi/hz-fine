package com.hz.fine.master.core.protocol.request.notice;



import com.hz.fine.master.core.protocol.base.BaseRequest;

import java.io.Serializable;

/**
 * @Description 协议：公告
 * @Author yoko
 * @Date 2020/1/14 21:18
 * @Version 1.0
 */
public class RequestNotice extends BaseRequest implements Serializable {
    private static final long   serialVersionUID = 1233283332140L;
    public Long id;
    public Integer noticeType;// 公告类型：1系统公告，2首页提醒公告
    public RequestNotice(){

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNoticeType() {
        return noticeType;
    }

    public void setNoticeType(Integer noticeType) {
        this.noticeType = noticeType;
    }
}
