package com.energyfuture.symphony.m3.domain;

import java.io.Serializable;

public class TrProjectEquipment implements Serializable{
    /* ******** private property ******** */
	/**
	 * 关系ID
	 */
    private String relationid;
	/**
	 * 项目ID
	 */
    private String projectid;
	/**
	 * 带电检测名称
	 */
    private String detectionname;
	/**
	 * 顺序
	 */
    private String orderby;

    public String toString(){
        return "{"+"relationid:"+relationid+","+"projectid:"+projectid+","+"detectionname:"+detectionname+","+"orderby:"+orderby+"}";
    }
    
    public boolean equals(Object o){
        if (!(o instanceof TrProjectEquipment))
            return false;
     	 TrProjectEquipment t=(TrProjectEquipment)o;
        if (relationid==null)
            return this==t;
        return  relationid.equals(t.relationid);
    }
    
     public int hashCode(){
        return relationid.hashCode();
    }

    
    /* ******** property's public getter setter ******** */
    public String getRelationid() {
        return relationid;
    }
    public void setRelationid(String relationid) {
        this.relationid = relationid;
    }
    
    public String getProjectid() {
        return projectid;
    }
    public void setProjectid(String projectid) {
        this.projectid = projectid;
    }
    
    public String getDetectionname() {
        return detectionname;
    }
    public void setDetectionname(String detectionname) {
        this.detectionname = detectionname;
    }
    
    public String getOrderby() {
        return orderby;
    }
    public void setOrderby(String orderby) {
        this.orderby = orderby;
    }
    
	
    /* ******** table column/field name  mnemonic symbol ******** */
    public static final String TABLENAME="tr_project_equipment";
    public static final class FIELDS{
		/**
		 * 关系ID
		 */
        public static final String RELATIONID="tr_project_equipment.RELATIONID";
		/**
		 * 项目ID
		 */
        public static final String PROJECTID="tr_project_equipment.PROJECTID";
		/**
		 * 带电检测名称
		 */
        public static final String DETECTIONNAME="tr_project_equipment.DETECTIONNAME";
		/**
		 * 顺序
		 */
        public static final String ORDERBY="tr_project_equipment.ORDERBY";
    }
}
