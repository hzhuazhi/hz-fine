package com.hz.fine.master.core.service.impl;

import com.hz.fine.master.core.common.dao.BaseDao;
import com.hz.fine.master.core.common.service.impl.BaseServiceImpl;
import com.hz.fine.master.core.mapper.TaskHodgepodgeMapper;
import com.hz.fine.master.core.service.TaskHodgepodgeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description 大杂烩的task的Service层的实现层
 * @Author yoko
 * @Date 2020/1/21 17:57
 * @Version 1.0
 */
@Service
public class TaskHodgepodgeServiceImpl<T> extends BaseServiceImpl<T> implements TaskHodgepodgeService<T> {
    /**
     * 5分钟.
     */
    public long FIVE_MIN = 300;

    public long TWO_HOUR = 2;

    @Autowired
    private TaskHodgepodgeMapper taskHodgepodgeMapper;


    public BaseDao<T> getDao() {
        return taskHodgepodgeMapper;
    }

    @Override
    public int updateTransResultStatus(Object obj) {
        return taskHodgepodgeMapper.updateTransResultStatus(obj);
    }

    @Override
    public int updateCashOutProcedLogTheIsOk(Object obj) {
        return taskHodgepodgeMapper.updateCashOutProcedLogTheIsOk(obj);
    }
}
