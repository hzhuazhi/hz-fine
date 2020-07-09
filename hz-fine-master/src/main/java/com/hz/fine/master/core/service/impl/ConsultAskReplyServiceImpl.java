package com.hz.fine.master.core.service.impl;

import com.hz.fine.master.core.common.dao.BaseDao;
import com.hz.fine.master.core.common.service.impl.BaseServiceImpl;
import com.hz.fine.master.core.mapper.ConsultAskReplyMapper;
import com.hz.fine.master.core.service.ConsultAskReplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description 在线客服、咨询的发问的问答纪录的Service层的实现层
 * @Author yoko
 * @Date 2020/7/9 11:23
 * @Version 1.0
 */
@Service
public class ConsultAskReplyServiceImpl<T> extends BaseServiceImpl<T> implements ConsultAskReplyService<T> {
    /**
     * 5分钟.
     */
    public long FIVE_MIN = 300;

    public long TWO_HOUR = 2;

    @Autowired
    private ConsultAskReplyMapper consultAskReplyMapper;

    public BaseDao<T> getDao() {
        return consultAskReplyMapper;
    }
}
