package com.hz.fine.master.core.service.impl;

import com.hz.fine.master.core.common.dao.BaseDao;
import com.hz.fine.master.core.common.service.impl.BaseServiceImpl;
import com.hz.fine.master.core.mapper.TaskMapper;
import com.hz.fine.master.core.model.task.TaskAlipayNotifyModel;
import com.hz.fine.master.core.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description 定时任务的Service层的实现层
 * @Author yoko
 * @Date 2020/1/11 14:33
 * @Version 1.0
 */
@Service
public class TaskServiceImpl<T> extends BaseServiceImpl<T> implements TaskService<T> {
    /**
     * 5分钟.
     */
    public long FIVE_MIN = 300;

    public long TWO_HOUR = 2;

    @Autowired
    private TaskMapper taskMapper;


    public BaseDao<T> getDao() {
        return taskMapper;
    }


    @Override
    public int updateTransStatus(Object obj) {
        return taskMapper.updateTransStatus(obj);
    }

    @Override
    public long addTransData(Object obj) {
        return taskMapper.addTransData(obj);
    }

    @Override
    public List<TaskAlipayNotifyModel> getTaskAlipayNotify(Object obj) {
        return taskMapper.getTaskAlipayNotify(obj);
    }

    @Override
    public int updateTaskAlipayNotifyStatus(Object obj) {
        return taskMapper.updateTaskAlipayNotifyStatus(obj);
    }

    @Override
    public int addPayCust(Object obj) {
        return taskMapper.addPayCust(obj);
    }
}
