package com.energyfuture.symphony.m3.domain;

import java.io.Serializable;

public class TrDetectiontypeTemplet implements Serializable{
    /* ******** private property ******** */
	/**
	 * 区域设备ID
	 */
    private String id;
	/**
	 * 区域ID
	 */
    private String detectionname;
	/**
	 * 区域设备名称
	 */
    private String pid;
	/**
	 * 显示顺序
	 */
    private String detectiontempletid;
	/**
	 * 
	 */
    private String updatetime;
	/**
	 * 
	 */
    private String updateperson;
    private String orderby;
    private int piccount;

    @Override
    public String toString() {
        return "TrDetectiontypeTemplet{" +
                "id='" + id + '\'' +
                ", detectionname='" + detectionname + '\'' +
                ", pid='" + pid + '\'' +
                ", detectiontempletid='" + detectiontempletid + '\'' +
                ", updatetime='" + updatetime + '\'' +
                ", updateperson='" + updateperson + '\'' +
                ", orderby='" + orderby + '\'' +
                ", piccount=" + piccount +
                '}';
    }

    public boolean equals(Object o){
        if (!(o instanceof TrDetectiontypeTemplet))
            return false;
     	 TrDetectiontypeTemplet t=(TrDetectiontypeTemplet)o;
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
    
    public String getDetectiontempletid() {
        return detectiontempletid;
    }
    public void setDetectiontempletid(String detectiontempletid) {
        this.detectiontempletid = detectiontempletid;
    }

    public String getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
    }

    public String getUpdateperson() {
        return updateperson;
    }
    public void setUpdateperson(String updateperson) {
        this.updateperson = updateperson;
    }

    public String getOrderby() {
        return orderby;
    }

    public void setOrderby(String orderby) {
        this.orderby = orderby;
    }

    public int getPiccount() {
        return piccount;
    }

    public void setPiccount(int piccount) {
        this.piccount = piccount;
    }

    /* ******** table column/field name  mnemonic symbol ******** */
    public static final String TABLENAME="tr_detectiontype_templet";
    public static final class FIELDS{
		/**
		 * 区域设备ID
		 */
        public static final String ID="tr_detectiontype_templet.ID";
		/**
		 * 区域ID
		 */
        public static final String DETECTIONNAME="tr_detectiontype_templet.DETECTIONNAME";
		/**
		 * 区域设备名称
		 */
        public static final String PID="tr_detectiontype_templet.PID";
		/**
		 * 显示顺序
		 */
        public static final String DETECTIONTEMPLETID="tr_detectiontype_templet.DETECTIONTEMPLETID";
		/**
		 * 
		 */
        public static final String UPDATETIME="tr_detectiontype_templet.UPDATETIME";
		/**
		 * 
		 */
        public static final String UPDATEPERSON="tr_detectiontype_templet.UPDATEPERSON";
    }
}
