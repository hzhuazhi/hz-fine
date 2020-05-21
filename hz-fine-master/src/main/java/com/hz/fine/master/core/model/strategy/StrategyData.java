package com.hz.fine.master.core.model.strategy;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description 策略里面值的属性
 * @Author yoko
 * @Date 2020/5/19 16:01
 * @Version 1.0
 */
public class StrategyData {

    private Long id;

    private Long stgKey;

    private String stgValue;

    private String stgValueOne;

    private Integer stgValueTwo;

    private Integer stgValueThree;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getStgKey() {
        return stgKey;
    }

    public void setStgKey(Long stgKey) {
        this.stgKey = stgKey;
    }

    public String getStgValue() {
        return stgValue;
    }

    public void setStgValue(String stgValue) {
        this.stgValue = stgValue;
    }

    public String getStgValueOne() {
        return stgValueOne;
    }

    public void setStgValueOne(String stgValueOne) {
        this.stgValueOne = stgValueOne;
    }

    public Integer getStgValueTwo() {
        return stgValueTwo;
    }

    public void setStgValueTwo(Integer stgValueTwo) {
        this.stgValueTwo = stgValueTwo;
    }

    public Integer getStgValueThree() {
        return stgValueThree;
    }

    public void setStgValueThree(Integer stgValueThree) {
        this.stgValueThree = stgValueThree;
    }
}
