package com.hz.fine.master.core.service.impl;

import com.hz.fine.master.core.common.dao.BaseDao;
import com.hz.fine.master.core.common.service.impl.BaseServiceImpl;
import com.hz.fine.master.core.mapper.QuestionDMapper;
import com.hz.fine.master.core.service.QuestionDService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description 百问百答问题-详情的Service层的实现层
 * @Author yoko
 * @Date 2020/1/7 16:56
 * @Version 1.0
 */
@Service
public class QuestionDServiceImpl<T> extends BaseServiceImpl<T> implements QuestionDService<T> {
    /**
     * 5分钟.
     */
    public long FIVE_MIN = 300;

    public long TWO_HOUR = 2;

    @Autowired
    private QuestionDMapper questionDMapper;


    public BaseDao<T> getDao() {
        return questionDMapper;
    }
}
