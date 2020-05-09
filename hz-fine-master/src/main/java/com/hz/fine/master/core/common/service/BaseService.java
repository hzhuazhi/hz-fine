package com.hz.fine.master.core.common.service;



import com.hz.fine.master.core.protocol.page.BasePage;

import java.util.List;


/**
 * 
 * @Title: BaseService
 * @Package: com.zq.tv.common.service
 * @Description: 业务接口基础类，所有service类的父类
 * @Author: df
 * @Date: 2016-11-28 下午03:47:05
 */
public interface BaseService<T> {
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
	 * 删除实体，支持批量删除
	 * 
	 * @param t
	 * @return
	 */
	public int delete(Object... ids);


	/**
	 * 更新实体:主要作用1.逻辑删除，2更新状态
	 *
	 * @param t
	 * @return
	 */
	public int manyOperation(Object t);

	/**
	 * 查询所有
	 * 
	 * @param t
	 * @return
	 */
	public List<T> findAll();

	/**
	 * 根据实体ID查找该实体
	 * 
	 * @param t
	 * @return
	 */
	public T findById(Object id);

	/**
	 * 根据条件查询数据
	 *
	 * @param t
	 * @return
	 */
	public T findByObject(Object obj);

	/**
	 * 根据查询
	 * @param t
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
