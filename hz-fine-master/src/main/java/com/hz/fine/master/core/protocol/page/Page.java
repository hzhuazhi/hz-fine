package com.hz.fine.master.core.protocol.page;

/**
 * @Description 分页的基础属性
 * @Author yoko
 * @Date 2019/11/25 11:39
 * @Version 1.0
 */
public class Page {

	private Integer rowCount;

	public Page() {

	}

	public Integer getRowCount() {
		return rowCount;
	}

	public void setRowCount(Integer rowCount) {
		this.rowCount = rowCount;
	}
}