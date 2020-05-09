package com.hz.fine.master.core.model.region;

import java.io.Serializable;

/**
 * @Description ip号段、地域的实体Bean
 * @Author yoko
 * @Date 2019/12/18 16:51
 * @Version 1.0
 */
public class RegionModel implements Serializable {
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1115110409167768411L;

    private long id;
    /**
     * ip值，算好的值
     */
    private long ipValue;

    /**
     * ip地址
     */
    private String ip;

    private long startIp;

    private long endIp;

    /**
     * 省份
     */
    private String province;

    /**
     * 城市
     */
    private String city;

    /**
     * 运营商
     */
    private String isp;

    public RegionModel(){

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getIpValue() {
        return ipValue;
    }

    public void setIpValue(long ipValue) {
        this.ipValue = ipValue;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public long getStartIp() {
        return startIp;
    }

    public void setStartIp(long startIp) {
        this.startIp = startIp;
    }

    public long getEndIp() {
        return endIp;
    }

    public void setEndIp(long endIp) {
        this.endIp = endIp;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getIsp() {
        return isp;
    }

    public void setIsp(String isp) {
        this.isp = isp;
    }
}
