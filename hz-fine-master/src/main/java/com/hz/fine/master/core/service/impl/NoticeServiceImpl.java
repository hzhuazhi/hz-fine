package com.hz.fine.master.core.service.impl;

import com.hz.fine.master.core.common.dao.BaseDao;
import com.hz.fine.master.core.common.service.impl.BaseServiceImpl;
import com.hz.fine.master.core.mapper.NoticeMapper;
import com.hz.fine.master.core.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description 公告的Service层的实现层
 * @Author yoko
 * @Date 2020/1/14 20:13
 * @Version 1.0
 */
@Service
public class NoticeServiceImpl<T> extends BaseServiceImpl<T> implements NoticeService<T> {
    /**
     * 5分钟.
     */
    public long FIVE_MIN = 300;

    public long TWO_HOUR = 2;

    @Autowired
    private NoticeMapper noticeMapper;


    public BaseDao<T> getDao() {
        return noticeMapper;
    }
}
