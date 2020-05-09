package com.hz.fine.master.core.service.impl;

import com.hz.fine.master.core.common.dao.BaseDao;
import com.hz.fine.master.core.common.service.impl.BaseServiceImpl;
import com.hz.fine.master.core.mapper.QuestionMMapper;
import com.hz.fine.master.core.service.QuestionMService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description 百问百答问题类别的Service层的实现层
 * @Author yoko
 * @Date 2020/1/7 16:53
 * @Version 1.0
 */
@Service
public class QuestionMServiceImpl<T> extends BaseServiceImpl<T> implements QuestionMService<T> {
    /**
     * 5分钟.
     */
    public long FIVE_MIN = 300;

    public long TWO_HOUR = 2;

    @Autowired
    private QuestionMMapper questionMMapper;

    public BaseDao<T> getDao() {
        return questionMMapper;
    }

}
