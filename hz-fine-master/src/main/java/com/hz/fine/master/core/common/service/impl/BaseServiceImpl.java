package com.hz.fine.master.core.common.service.impl;







import com.hz.fine.master.core.common.dao.BaseDao;
import com.hz.fine.master.core.common.service.BaseService;
import com.hz.fine.master.core.protocol.page.BasePage;

import java.util.List;

/**
 * 
 * @Title: BaseServiceImpl
 * @Package: com.zq.tv.common.service.impl
 * @Description: 业务接口实现基础类
 * @Author: df
 * @Date: 2016-11-28 下午03:50:38
 */
public abstract class BaseServiceImpl<T> implements BaseService<T> {
	/**
	 * 抽象方法，用于具体的调用某个dao
	 * 
	 * @return
	 */
	public abstract BaseDao<T> getDao();

	/**
	 * 添加实体
	 * 
	 * @param t
	 * @return
	 */
	public int add(T t) {
		return getDao().add(t);
	}

	/**
	 * 添加实体
	 * 返回添加数据的主键ID
	 * @param t
	 * @return
	 */
	public long addLong(T t) {
		return getDao().addLong(t);
	}

	/**
	 * 更新实体
	 * 
	 * @param t
	 * @return
	 */
	public int update(T t) {
		return getDao().update(t);
	}

	/**
	 * 删除实体，支持批量删除
	 * 
	 * @param ids
	 * @return
	 */
	public int delete(Object... ids) {
		if (ids == null || ids.length < 1) {
			return -1;
		}
		// 循环调用删除方法，次数为数组长度
		for (Object id : ids) {
			getDao().delete(id);
		}
		return ids.length;
	}


	/**
	 * 删除实体，支持批量删除
	 *
	 * @param obj
	 * @return
	 */
	public int manyOperation(Object obj) {
		return getDao().manyOperation(obj);
	}

	/**
	 * 查询所有
	 * 
	 * @return
	 */
	public List<T> findAll() {
		return getDao().findAll();
	};

	/**
	 * 根据实体ID查找该实体
	 * 
	 * @param id
	 * @return
	 */
	public T findById(Object id) {
		return getDao().findById(id);
	}

	/**
	 * 根据条件查询数据
	 *
	 * @param obj
	 * @return
	 */
	public T findByObject(Object obj){
		return getDao().findByObject(obj);
	}

	/**
	 * 根据查询
	 * @param obj
	 * @return
	 */
	public List<T> findByCondition(Object obj) {
		return getDao().findByCondition(obj);
	};

	public int queryByCount(BasePage page){
		return getDao().queryByCount(page);
	}

	public List<T> queryByList(BasePage page){
		Integer rowCount = queryByCount(page);
		page.setRowCount(rowCount);
		return getDao().queryByList(page);
	}
}
