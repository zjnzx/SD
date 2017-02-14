package com.energyfuture.symphony.m3.domain;

import java.io.Serializable;

public class TrDetectiontypEtempletObj implements Serializable{
    /* ******** private property ******** */
	/**
	 * ID
	 */
    private String id;
    /**
     *
     */
    private String rid;
	/**
	 * 任务ID
	 */
    private String taskid;
	/**
	 * 检测类型名称
	 */
    private String detectionname;
	/**
	 * 父节点
	 */
    private String pid;
	/**
	 * 检测项目模板
	 */
    private String detectiontemplet;
    /**
     *
     */
    private String status;
    private String templettype;
    private String orderby;
    private String piccount;

    @Override
    public String toString() {
        return "TrDetectiontypEtempletObj{" +
                "id='" + id + '\'' +
                ", rid='" + rid + '\'' +
                ", taskid='" + taskid + '\'' +
                ", detectionname='" + detectionname + '\'' +
                ", pid='" + pid + '\'' +
                ", detectiontemplet='" + detectiontemplet + '\'' +
                ", status='" + status + '\'' +
                ", templettype='" + templettype + '\'' +
                ", orderby='" + orderby + '\'' +
                ", piccount='" + piccount + '\'' +
                '}';
    }

    public boolean equals(Object o){
        if (!(o instanceof TrDetectiontypEtempletObj))
            return false;
     	 TrDetectiontypEtempletObj t=(TrDetectiontypEtempletObj)o;
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
    
    public String getTaskid() {
        return taskid;
    }
    public void setTaskid(String taskid) {
        this.taskid = taskid;
    }
    
    public String getDetectionname() {
        return detectionname;
    }
    public void setDetectionname(String detectionname) {
        this.detectionname = detectionname;
    }
    
    public String getPid() {
        return pid;
    }
    public void setPid(String pid) {
        this.pid = pid;
    }
    
    public String getDetectiontemplet() {
        return detectiontemplet;
    }
    public void setDetectiontemplet(String detectiontemplet) {
        this.detectiontemplet = detectiontemplet;
    }

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTemplettype() {
        return templettype;
    }

    public void setTemplettype(String templettype) {
        this.templettype = templettype;
    }

    public String getOrderby() {
        return orderby;
    }

    public void setOrderby(String orderby) {
        this.orderby = orderby;
    }

    public String getPiccount() {
        return piccount;
    }

    public void setPiccount(String piccount) {
        this.piccount = piccount;
    }

    /* ******** table column/field name  mnemonic symbol ******** */
    public static final String TABLENAME="tr_detectiontyp_etemplet_obj";
    public static final class FIELDS{
		/**
		 * ID
		 */
        public static final String ID="tr_detectiontyp_etemplet_obj.ID";
		/**
		 * 任务ID
		 */
        public static final String TASKID="tr_detectiontyp_etemplet_obj.TASKID";
		/**
		 * 检测类型名称
		 */
        public static final String DETECTIONNAME="tr_detectiontyp_etemplet_obj.DETECTIONNAME";
		/**
		 * 父节点
		 */
        public static final String PID="tr_detectiontyp_etemplet_obj.PID";
		/**
		 * 检测项目模板
		 */
        public static final String DETECTIONTEMPLET="tr_detectiontyp_etemplet_obj.DETECTIONTEMPLET";
    }
}
