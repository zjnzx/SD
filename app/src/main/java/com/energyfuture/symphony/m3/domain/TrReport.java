package com.energyfuture.symphony.m3.domain;

import java.io.Serializable;

public class TrReport implements Serializable{
    /* ******** private property ******** */
    private String id;
    private String auditingSuggestion;
    private String reportName;
    private String auditingPerson;
    private String stationId;
    private String createPerson;
    private String stationName;
    private String wordUrl;
    private String auditingTime;
    private String projectId;
    private String createTime;
    private String status;

    @Override
    public String toString() {
        return "TrReport{" +
                "id='" + id + '\'' +
                ", auditingSuggestion='" + auditingSuggestion + '\'' +
                ", reportName='" + reportName + '\'' +
                ", auditingPerson='" + auditingPerson + '\'' +
                ", stationId='" + stationId + '\'' +
                ", createPerson='" + createPerson + '\'' +
                ", stationName='" + stationName + '\'' +
                ", wordUrl='" + wordUrl + '\'' +
                ", auditingTime='" + auditingTime + '\'' +
                ", projectId='" + projectId + '\'' +
                ", createTime='" + createTime + '\'' +
                ", status='" + status + '\'' +
                '}';
    }

    public boolean equals(Object o){
        if (!(o instanceof TrReport))
            return false;
     	 TrReport t=(TrReport)o;
        if (id==null)
            return this==t;
        return  id.equals(t.id);
    }
    
     public int hashCode(){
        return id.hashCode();
    }

    
    /* ******** property's public getter setter ******** */

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuditingSuggestion() {
        return auditingSuggestion;
    }

    public void setAuditingSuggestion(String auditingSuggestion) {
        this.auditingSuggestion = auditingSuggestion;
    }

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public String getAuditingPerson() {
        return auditingPerson;
    }

    public void setAuditingPerson(String auditingPerson) {
        this.auditingPerson = auditingPerson;
    }

    public String getStationId() {
        return stationId;
    }

    public void setStationId(String stationId) {
        this.stationId = stationId;
    }

    public String getCreatePerson() {
        return createPerson;
    }

    public void setCreatePerson(String createPerson) {
        this.createPerson = createPerson;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getWordUrl() {
        return wordUrl;
    }

    public void setWordUrl(String wordUrl) {
        this.wordUrl = wordUrl;
    }

    public String getAuditingTime() {
        return auditingTime;
    }

    public void setAuditingTime(String auditingTime) {
        this.auditingTime = auditingTime;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static final String TABLENAME="tr_report";
}
