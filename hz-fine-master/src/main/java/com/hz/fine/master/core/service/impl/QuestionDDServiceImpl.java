package com.hz.fine.master.core.service.impl;

import com.hz.fine.master.core.common.dao.BaseDao;
import com.hz.fine.master.core.common.service.impl.BaseServiceImpl;
import com.hz.fine.master.core.mapper.QuestionDDMapper;
import com.hz.fine.master.core.service.QuestionDDService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description 百问百答问题-详情-步骤的Service层的实现层
 * @Author yoko
 * @Date 2020/7/6 14:19
 * @Version 1.0
 */
@Service
public class QuestionDDServiceImpl<T> extends BaseServiceImpl<T> implements QuestionDDService<T> {
    /**
     * 5分钟.
     */
    public long FIVE_MIN = 300;

    public long TWO_HOUR = 2;

    @Autowired
    private QuestionDDMapper questionDDMapper;

    public BaseDao<T> getDao() {
        return questionDDMapper;
    }
}
