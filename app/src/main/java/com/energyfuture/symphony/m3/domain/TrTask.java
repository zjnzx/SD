package com.energyfuture.symphony.m3.domain;

import java.io.Serializable;

public class TrTask implements Serializable{
    /* ******** private property ******** */
	/**
	 * 任务ID
	 */
    private String taskid;
	/**
	 * 任务名称
	 */
    private String taskname;
	/**
	 * 任务状态（枚举）
	 */
    private String taskstate;
	/**
	 * 计划起始日期
	 */
    private String planstarttime;
	/**
	 * 计划截止日期
	 */
    private String planendtime;
	/**
	 * 实际开始日期
	 */
    private String actualstarttime;
	/**
	 * 实际完成日期
	 */
    private String actualsendtime;
	/**
	 * 责任人
	 */
    private String person;
	/**
	 * 任务指派人
	 */
    private String taskperson;
	/**
	 * 带电检测类型
	 */
    private String detectionttype;
	/**
	 * 任务修改时间
	 */
    private String updatetime;
	/**
	 * 现场任务完成时间
	 */
    private String finishtime;
	/**
	 * 步骤总数
	 */
    private String stepcount;
	/**
	 * 已完成步骤数
	 */
    private String finishcount;
	/**
	 * 
	 */
    private String workperson;
    /**
     *项目ID
     */
    private String projectid;
    /**
     *设备ID
     */
    private String equipmentid;
    /**
     *序号
     */
    private String orderby;
    /**
     *作业评价_是否异常
     */
    private String appraise_abnormal;
    /**
     *作业评价_处理意见
     */
    private String appraise_suggest;
    /**
     *作业评价_流程的变化
     */
    private String appraise_changeflow;
    /**
     *作业评价_人员引起的变化
     */
    private String appraise_changeperson;
    /**
     *作业评价_作业环境引起的变化
     */
    private String changeenv;
    /**
     *作业评价_生产设备及设备参数发生的变化
     */
    private String changeequipment;
    /**
     *作业评价_作业方式引起的变化
     */
    private String changework;
    /**
     *作业评价_相关方引起的变化
     */
    private String changeother;
    /**
     *改进机会
     */
    private String improve;
    /**
     *具体措施
     */
    private String method;
    /**
     *恢复现场确认人
     */
    private String recoverperson;
    /**
     *清理现场确认人
     */
    private String cleanperson;
    /**
     *总结人
     */
    private String conclusionperson;
    /**
     *审核_巡检过程整体情况
     */
    private String audit_result;
    /**
     *审核_巡检发现问题及后续措施
     */
    private String audit_question;
    /**
     *审核人
     */
    private String auditperson;

    public String toString(){
        return "{"+"taskid:"+taskid+","+"taskname:"+taskname+","+"taskstate:"+taskstate+","+"planstatetime:"+planstarttime+","+"planendtime:"+planendtime+","+"actualstatetime:"+actualstarttime+","+"actualsendtime:"+actualsendtime+","+"person:"+person+","+"taskperson:"+taskperson+","+"detectionttype:"+detectionttype+","+"updatetime:"+updatetime+","+"finishtime:"+finishtime+","+"stepcount:"+stepcount+","+"finishcount:"+finishcount+","+"workperson:"+workperson+","+"equipmentid:"+equipmentid+","+"projectid:"+projectid+"," +
                ""+"orderby:"+orderby+","+"appraise_abnormal:"+appraise_abnormal+","+"appraise_suggest:"+appraise_suggest+","+"appraise_changeflow:"+appraise_changeflow+","+"appraise_changeperson:"+appraise_changeperson+","+"changeenv:"+changeenv+","+"changeequipment:"+changeequipment+","+"changework:"+changework+","+"changeother:"+changeother+","+"improve:"+improve+","+"method:"+method+","+"recoverperson:"+recoverperson+","+"cleanperson:"+cleanperson+","+"conclusionperson:"+conclusionperson+","+"audit_result:"+audit_result+","+"audit_question:"+audit_question+","+"auditperson:"+auditperson+"}";
    }
    
    public boolean equals(Object o){
        if (!(o instanceof TrTask))
            return false;
     	 TrTask t=(TrTask)o;
        if (taskid==null)
            return this==t;
        return  taskid.equals(t.taskid);
    }
    
     public int hashCode(){
        return taskid.hashCode();
    }

    
    /* ******** property's public getter setter ******** */
    public String getTaskid() {
        return taskid;
    }
    public void setTaskid(String taskid) {
        this.taskid = taskid;
    }
    
    public String getTaskname() {
        return taskname;
    }
    public void setTaskname(String taskname) {
        this.taskname = taskname;
    }
    
    public String getTaskstate() {
        return taskstate;
    }
    public void setTaskstate(String taskstate) {
        this.taskstate = taskstate;
    }
    
    public String getPerson() {
        return person;
    }
    public void setPerson(String person) {
        this.person = person;
    }
    
    public String getTaskperson() {
        return taskperson;
    }
    public void setTaskperson(String taskperson) {
        this.taskperson = taskperson;
    }
    
    public String getDetectionttype() {
        return detectionttype;
    }
    public void setDetectionttype(String detectionttype) {
        this.detectionttype = detectionttype;
    }

    public String getPlanstarttime() {
        return planstarttime;
    }

    public void setPlanstarttime(String planstarttime) {
        this.planstarttime = planstarttime;
    }

    public String getActualstarttime() {
        return actualstarttime;
    }

    public void setActualstarttime(String actualstarttime) {
        this.actualstarttime = actualstarttime;
    }

    public String getPlanendtime() {
        return planendtime;
    }

    public void setPlanendtime(String planendtime) {
        this.planendtime = planendtime;
    }

    public String getActualsendtime() {
        return actualsendtime;
    }

    public void setActualsendtime(String actualsendtime) {
        this.actualsendtime = actualsendtime;
    }

    public String getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
    }

    public String getFinishtime() {
        return finishtime;
    }

    public void setFinishtime(String finishtime) {
        this.finishtime = finishtime;
    }

    public String getStepcount() {
        return stepcount;
    }

    public void setStepcount(String stepcount) {
        this.stepcount = stepcount;
    }

    public String getFinishcount() {
        return finishcount;
    }

    public void setFinishcount(String finishcount) {
        this.finishcount = finishcount;
    }

    public String getWorkperson() {
        return workperson;
    }
    public void setWorkperson(String workperson) {
        this.workperson = workperson;
    }

    public String getProjectid() {
        return projectid;
    }

    public void setProjectid(String projectid) {
        this.projectid = projectid;
    }

    public String getEquipmentid() {
        return equipmentid;
    }

    public void setEquipmentid(String equipmentid) {
        this.equipmentid = equipmentid;
    }

    public String getOrderby() {
        return orderby;
    }

    public void setOrderby(String orderby) {
        this.orderby = orderby;
    }

    public String getAppraise_abnormal() {
        return appraise_abnormal;
    }

    public void setAppraise_abnormal(String appraise_abnormal) {
        this.appraise_abnormal = appraise_abnormal;
    }

    public String getAppraise_suggest() {
        return appraise_suggest;
    }

    public void setAppraise_suggest(String appraise_suggest) {
        this.appraise_suggest = appraise_suggest;
    }

    public String getAppraise_changeflow() {
        return appraise_changeflow;
    }

    public void setAppraise_changeflow(String appraise_changeflow) {
        this.appraise_changeflow = appraise_changeflow;
    }

    public String getAppraise_changeperson() {
        return appraise_changeperson;
    }

    public void setAppraise_changeperson(String appraise_changeperson) {
        this.appraise_changeperson = appraise_changeperson;
    }

    public String getChangeenv() {
        return changeenv;
    }

    public void setChangeenv(String changeenv) {
        this.changeenv = changeenv;
    }

    public String getChangeequipment() {
        return changeequipment;
    }

    public void setChangeequipment(String changeequipment) {
        this.changeequipment = changeequipment;
    }

    public String getChangework() {
        return changework;
    }

    public void setChangework(String changework) {
        this.changework = changework;
    }

    public String getChangeother() {
        return changeother;
    }

    public void setChangeother(String changeother) {
        this.changeother = changeother;
    }

    public String getImprove() {
        return improve;
    }

    public void setImprove(String improve) {
        this.improve = improve;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getRecoverperson() {
        return recoverperson;
    }

    public void setRecoverperson(String recoverperson) {
        this.recoverperson = recoverperson;
    }

    public String getCleanperson() {
        return cleanperson;
    }

    public void setCleanperson(String cleanperson) {
        this.cleanperson = cleanperson;
    }

    public String getConclusionperson() {
        return conclusionperson;
    }

    public void setConclusionperson(String conclusionperson) {
        this.conclusionperson = conclusionperson;
    }

    public String getAudit_result() {
        return audit_result;
    }

    public void setAudit_result(String audit_result) {
        this.audit_result = audit_result;
    }

    public String getAudit_question() {
        return audit_question;
    }

    public void setAudit_question(String audit_question) {
        this.audit_question = audit_question;
    }

    public String getAuditperson() {
        return auditperson;
    }

    public void setAuditperson(String auditperson) {
        this.auditperson = auditperson;
    }

    /* ******** table column/field name  mnemonic symbol ******** */
    public static final String TABLENAME="tr_task";
    public static final class FIELDS{
		/**
		 * 任务ID
		 */
        public static final String TASKID="tr_task.TASKID";
		/**
		 * 任务名称
		 */
        public static final String TASKNAME="tr_task.TASKNAME";
		/**
		 * 任务状态（枚举）
		 */
        public static final String TASKSTATE="tr_task.TASKSTATE";
		/**
		 * 计划起始日期
		 */
        public static final String PLANSTATETIME="tr_task.PLANSTATETIME";
		/**
		 * 计划截止日期
		 */
        public static final String PLANENDTIME="tr_task.PLANENDTIME";
		/**
		 * 实际开始日期
		 */
        public static final String ACTUALSTARTTIME="tr_task.ACTUALSTARTTIME";
		/**
		 * 实际完成日期
		 */
        public static final String ACTUALSENDTIME="tr_task.ACTUALSENDTIME";
		/**
		 * 责任人
		 */
        public static final String PERSON="tr_task.PERSON";
		/**
		 * 任务指派人
		 */
        public static final String TASKPERSON="tr_task.TASKPERSON";
		/**
		 * 带电检测类型
		 */
        public static final String DETECTIONTTYPE="tr_task.DETECTIONTTYPE";
		/**
		 * 任务修改时间
		 */
        public static final String UPDATETIME="tr_task.UPDATETIME";
		/**
		 * 现场任务完成时间
		 */
        public static final String FINISHTIME="tr_task.FINISHTIME";
		/**
		 * 步骤总数
		 */
        public static final String STEPCOUNT="tr_task.STEPCOUNT";
		/**
		 * 已完成步骤数
		 */
        public static final String FINISHCOUNT="tr_task.FINISHCOUNT";
		/**
		 * 
		 */
        public static final String WORKPERSON="tr_task.WORKPERSON";
		/**
		 * 
		 */
        public static final String SUMMARY="tr_task.SUMMARY";
    }
}
