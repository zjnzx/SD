package com.energyfuture.symphony.m3.domain;

/**
 * Created by Administrator on 2015/9/15 0015.
 */
public class TrSpecialWork {
    private String id;
    private String detectionprojectid;
    private String detectiondes;
    private String detectiondate;
    private String detectiontime;
    private String detectioncondition;
    private String temp;
    private String radiation;
    private String loads;
    private String detectiondeperson;
    private String updatetime;
    private String updateperson;
    private String remarks; //数据库新增的字段：备注

    @Override
    public String toString() {
        return "TrSpecialWork{" +
                "id='" + id + '\'' +
                ", detectionprojectid='" + detectionprojectid + '\'' +
                ", detectiondes='" + detectiondes + '\'' +
                ", detectiondate='" + detectiondate + '\'' +
                ", detectiontime='" + detectiontime + '\'' +
                ", detectioncondition='" + detectioncondition + '\'' +
                ", temp='" + temp + '\'' +
                ", radiation='" + radiation + '\'' +
                ", loads='" + loads + '\'' +
                ", detectiondeperson='" + detectiondeperson + '\'' +
                ", updatetime='" + updatetime + '\'' +
                ", updateperson='" + updateperson + '\'' +
                ", remark='" + remarks + '\'' +
                '}';
    }

    public String getUpdateperson() {
        return updateperson;
    }

    public void setUpdateperson(String updateperson) {
        this.updateperson = updateperson;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDetectionprojectid() {
        return detectionprojectid;
    }

    public void setDetectionprojectid(String detectionprojectid) {
        this.detectionprojectid = detectionprojectid;
    }

    public String getDetectiondes() {
        return detectiondes;
    }

    public void setDetectiondes(String detectiondes) {
        this.detectiondes = detectiondes;
    }

    public String getDetectiondate() {
        return detectiondate;
    }

    public void setDetectiondate(String detectiondate) {
        this.detectiondate = detectiondate;
    }

    public String getDetectiontime() {
        return detectiontime;
    }

    public void setDetectiontime(String detectiontime) {
        this.detectiontime = detectiontime;
    }

    public String getDetectioncondition() {
        return detectioncondition;
    }

    public void setDetectioncondition(String detectioncondition) {
        this.detectioncondition = detectioncondition;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getRadiation() {
        return radiation;
    }

    public void setRadiation(String radiation) {
        this.radiation = radiation;
    }

    public String getLoads() {
        return loads;
    }

    public void setLoads(String loads) {
        this.loads = loads;
    }

    public String getDetectiondeperson() {
        return detectiondeperson;
    }

    public void setDetectiondeperson(String detectiondeperson) {
        this.detectiondeperson = detectiondeperson;
    }

    public String getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public static final String TABLENAME="tr_special_work";

}
