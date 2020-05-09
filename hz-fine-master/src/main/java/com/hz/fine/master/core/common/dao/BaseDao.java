package com.hz.fine.master.core.common.dao;




import com.hz.fine.master.core.protocol.page.BasePage;

import java.util.List;


/**
 * 
 * @Title: BaseDao
 * @Package: com.xn.ad.common.mapper
 * @Description: 数据库基本操作的dao工具类
 * @Author: df
 * @Date: 2016-11-28 下午03:37:07
 */
public interface BaseDao<T> {
	
	/**
	 * 添加实体
	 * 
	 * @param t
	 * @return
	 */
	public int add(T t);

	/**
	 * 添加实体
	 * 返回添加数据的主键ID
	 * @param t
	 * @return
	 */
	public long addLong(T t);

	/**
	 * 更新实体
	 * 
	 * @param t
	 * @return
	 */
	public int update(T t);

	/**
	 * 根据实体ID删除实体
	 * 
	 * @param id
	 * @return
	 */
	public int delete(Object id);

	/**
	 * 更新实体:主要作用1.逻辑删除，2更新状态
	 *
	 * @param obj
	 * @return
	 */
	public int manyOperation(Object obj);


	/**
	 * 查询所有
	 * 
	 * @return
	 */
	public List<T> findAll();

	/**
	 * 根据实体ID查找该实体
	 * 
	 * @param id
	 * @return
	 */
	public T findById(Object id);

	/**
	 * 根据条件查询数据
	 *
	 * @param obj
	 * @return
	 */
	public T findByObject(Object obj);

	/**
	 * 根据查询
	 * @param obj
	 * @return
	 */
	public List<T> findByCondition(Object obj);

	/**
	 * 查询总数-分页
	 * @param page
	 * @return
	 */
	public int queryByCount(BasePage page);

	/**
	 * 查询列表数据-分页
	 * @param page
	 * @return
	 */
	public List<T> queryByList(BasePage page);
}
