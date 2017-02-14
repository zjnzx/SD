package com.energyfuture.symphony.m3.domain;

import java.io.Serializable;

public class TrProject implements Serializable{
    /* ******** private property ******** */
	/**
	 * 项目ID
	 */
    private String projectid;
	/**
	 * 项目名称
	 */
    private String projectname;
	/**
	 * 项目状态（枚举）
	 */
    private String projectstate;
	/**
	 * 变电站名称
	 */
    private String stationname;
	/**
	 * 变电站ID
	 */
    private String stationid;
	/**
	 * 计划起始日期
	 */
    private String starttime;
	/**
	 * 计划截止日期
	 */
    private String endtime;
	/**
	 * 实际开始日期
	 */
    private String actualstarttime;
	/**
	 * 实际完成日期
	 */
    private String actualsendtime;
	/**
	 * 项目创建日期
	 */
    private String createtime;
	/**
	 * 注意事项
	 */
    private String aware;
	/**
	 * 指派人
	 */
    private String zpperson;
	/**
	 * 创建人
	 */
    private String createperson;
	/**
	 * 责任人
	 */
    private String zrpearson;
	/**
	 * 小组成员
	 */
    private String workperson;
	/**
	 * 项目修改时间
	 */
    private String updatetime;
	/**
	 * 检修原因
	 */
    private String reasondescribe;
	/**
	 * 安全须知
	 */
    private String safetynotice;
	/**
	 * 任务总数
	 */
    private String taskcount;
	/**
	 * 已完成任务数
	 */
    private String finishcount;
    /**
     * 部门id
     */
    private String department;

    public String toString(){
        return "{"+"projectid:"+projectid+","+"projectname:"+projectname+","+"projectstate:"+projectstate+","+"stationname:"+stationname+","+"stationid:"+stationid+","+"starttime:"+starttime+","+"endtime:"+endtime+","+"actualstarttime:"+actualstarttime+","+"actualsendtime:"+actualsendtime+","+"createtime:"+createtime+","+"aware:"+aware+","+"zpperson:"+zpperson+","+"createperson:"+createperson+","+"zrpearson:"+zrpearson+","+"workperson:"+workperson+","+"updatetime:"+updatetime+","+"reasondescribe:"+reasondescribe+","+"safetynotice:"+safetynotice+","+"department:"+department+","+"taskcount:"+taskcount+","+"finishcount:"+finishcount+"}";
    }
    
    public boolean equals(Object o){
        if (!(o instanceof TrProject))
            return false;
     	 TrProject t=(TrProject)o;
        if (projectid==null)
            return this==t;
        return  projectid.equals(t.projectid);
    }
    
     public int hashCode(){
        return projectid.hashCode();
    }

    
    /* ******** property's public getter setter ******** */
    public String getProjectid() {
        return projectid;
    }
    public void setProjectid(String projectid) {
        this.projectid = projectid;
    }
    
    public String getProjectname() {
        return projectname;
    }
    public void setProjectname(String projectname) {
        this.projectname = projectname;
    }
    
    public String getProjectstate() {
        return projectstate;
    }
    public void setProjectstate(String projectstate) {
        this.projectstate = projectstate;
    }
    
    public String getStationname() {
        return stationname;
    }
    public void setStationname(String stationname) {
        this.stationname = stationname;
    }
    
    public String getStationid() {
        return stationid;
    }
    public void setStationid(String stationid) {
        this.stationid = stationid;
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public String getActualstarttime() {
        return actualstarttime;
    }

    public void setActualstarttime(String actualstarttime) {
        this.actualstarttime = actualstarttime;
    }

    public String getActualsendtime() {
        return actualsendtime;
    }

    public void setActualsendtime(String actualsendtime) {
        this.actualsendtime = actualsendtime;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
    }

    public String getTaskcount() {
        return taskcount;
    }

    public void setTaskcount(String taskcount) {
        this.taskcount = taskcount;
    }

    public String getFinishcount() {
        return finishcount;
    }

    public void setFinishcount(String finishcount) {
        this.finishcount = finishcount;
    }

    public String getAware() {
        return aware;
    }
    public void setAware(String aware) {
        this.aware = aware;
    }
    
    public String getZpperson() {
        return zpperson;
    }
    public void setZpperson(String zpperson) {
        this.zpperson = zpperson;
    }
    
    public String getCreateperson() {
        return createperson;
    }
    public void setCreateperson(String createperson) {
        this.createperson = createperson;
    }
    
    public String getZrpearson() {
        return zrpearson;
    }
    public void setZrpearson(String zrpearson) {
        this.zrpearson = zrpearson;
    }
    
    public String getWorkperson() {
        return workperson;
    }
    public void setWorkperson(String workperson) {
        this.workperson = workperson;
    }
    
    public String getReasondescribe() {
        return reasondescribe;
    }
    public void setReasondescribe(String reasondescribe) {
        this.reasondescribe = reasondescribe;
    }
    
    public String getSafetynotice() {
        return safetynotice;
    }
    public void setSafetynotice(String safetynotice) {
        this.safetynotice = safetynotice;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    /* ******** table column/field name  mnemonic symbol ******** */
    public static final String TABLENAME="tr_project";
    public static final class FIELDS{
		/**
		 * 项目ID
		 */
        public static final String PROJECTID="tr_project.PROJECTID";
		/**
		 * 项目名称
		 */
        public static final String PROJECTNAME="tr_project.PROJECTNAME";
		/**
		 * 项目状态（枚举）
		 */
        public static final String PROJECTSTATE="tr_project.PROJECTSTATE";
		/**
		 * 变电站名称
		 */
        public static final String STATIONNAME="tr_project.STATIONNAME";
		/**
		 * 变电站ID
		 */
        public static final String STATIONID="tr_project.STATIONID";
		/**
		 * 计划起始日期
		 */
        public static final String STATETIME="tr_project.STATETIME";
		/**
		 * 计划截止日期
		 */
        public static final String ENDTIME="tr_project.ENDTIME";
		/**
		 * 实际开始日期
		 */
        public static final String ACTUALSTARTTIME="tr_project.ACTUALSTARTTIME";
		/**
		 * 实际完成日期
		 */
        public static final String ACTUALSENDTIME="tr_project.ACTUALSENDTIME";
		/**
		 * 项目创建日期
		 */
        public static final String CREATETIME="tr_project.CREATETIME";
		/**
		 * 注意事项
		 */
        public static final String AWARE="tr_project.AWARE";
		/**
		 * 指派人
		 */
        public static final String ZPPERSON="tr_project.ZPPERSON";
		/**
		 * 创建人
		 */
        public static final String CREATEPERSON="tr_project.CREATEPERSON";
		/**
		 * 责任人
		 */
        public static final String ZRPEARSON="tr_project.ZRPEARSON";
		/**
		 * 小组成员
		 */
        public static final String WORKPERSON="tr_project.WORKPERSON";
		/**
		 * 项目修改时间
		 */
        public static final String UPDATETIME="tr_project.UPDATETIME";
		/**
		 * 检修原因
		 */
        public static final String REASONDESCRIBE="tr_project.REASONDESCRIBE";
		/**
		 * 安全须知
		 */
        public static final String SAFETYNOTICE="tr_project.SAFETYNOTICE";
		/**
		 * 任务总数
		 */
        public static final String TASKCOUNT="tr_project.TASKCOUNT";
		/**
		 * 已完成任务数
		 */
        public static final String FINISHCOUNT="tr_project.FINISHCOUNT";
    }
}
