package com.hz.fine.master.core.model.task.base;

import java.io.Serializable;
import java.util.List;

/**
 * @Description task的抓取规则的实体Bean
 * @Author yoko
 * @Date 2019/12/6 21:01
 * @Version 1.0
 */
public class StatusModel implements Serializable {
    private static final long   serialVersionUID = 1233223301140L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 状态集合
     */
    private List<Integer> statusList;

    /**
     * 运行次数
     */
    private Integer runNum;

    /**
     * 运行计算状态：0初始化，1锁定，2计算失败，3计算成功
     */
    private Integer runStatus;

    /**
     * 运行计算状态：0初始化，1锁定，2计算失败，3计算成功
     * 当做条件
     */
    private Integer runStatusWhere;

    /**
     * 运行次数
     */
    private Integer handleNum;

    /**
     * 运行计算状态：0初始化，1锁定，2计算失败，3计算成功
     */
    private Integer handleStatus;

    /**
     * 查询多少条数据
     */
    private Integer limitNum;

    /**
     * 日期
     */
    private Integer curday;
    private Integer curdayStart;
    private Integer curdayEnd;



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getRunNum() {
        return runNum;
    }

    public void setRunNum(Integer runNum) {
        this.runNum = runNum;
    }

    public Integer getRunStatus() {
        return runStatus;
    }

    public void setRunStatus(Integer runStatus) {
        this.runStatus = runStatus;
    }

    public Integer getLimitNum() {
        return limitNum;
    }

    public void setLimitNum(Integer limitNum) {
        this.limitNum = limitNum;
    }

    public Integer getCurday() {
        return curday;
    }

    public void setCurday(Integer curday) {
        this.curday = curday;
    }

    public Integer getCurdayStart() {
        return curdayStart;
    }

    public void setCurdayStart(Integer curdayStart) {
        this.curdayStart = curdayStart;
    }

    public Integer getCurdayEnd() {
        return curdayEnd;
    }

    public void setCurdayEnd(Integer curdayEnd) {
        this.curdayEnd = curdayEnd;
    }

    public List<Integer> getStatusList() {
        return statusList;
    }

    public void setStatusList(List<Integer> statusList) {
        this.statusList = statusList;
    }

    public Integer getRunStatusWhere() {
        return runStatusWhere;
    }

    public void setRunStatusWhere(Integer runStatusWhere) {
        this.runStatusWhere = runStatusWhere;
    }

    public Integer getHandleNum() {
        return handleNum;
    }

    public void setHandleNum(Integer handleNum) {
        this.handleNum = handleNum;
    }

    public Integer getHandleStatus() {
        return handleStatus;
    }

    public void setHandleStatus(Integer handleStatus) {
        this.handleStatus = handleStatus;
    }
}
