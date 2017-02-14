package com.energyfuture.symphony.m3.domain;

import java.io.Serializable;

public class TrDetectionTemplet implements Serializable{
    /* ******** private property ******** */
	/**
	 * ID
	 */
    private String id;
	/**
	 * 检查方法
	 */
    private String detectionmethod;
	/**
	 * 检测标准
	 */
    private String detectionstandard;
	/**
	 * 检测样例（图片、文件）
	 */
    private String detectionexample;
	/**
	 * 备注
	 */
    private String remarks;
	/**
	 * 修改时间
	 */
    private String updatetime;
	/**
	 * 修改人
	 */
    private String updateperson;
	/**
	 * 模板类型
	 */
    private String templettype;

    public String toString(){
        return "{"+"id:"+id+","+"detectionmethod:"+detectionmethod+","+"detectionstandard:"+detectionstandard+","+"detectionexample:"+detectionexample+","+"remarks:"+remarks+","+"updatetime:"+updatetime+","+"updateperson:"+updateperson+","+"templettype:"+templettype+"}";
    }
    
    public boolean equals(Object o){
        if (!(o instanceof TrDetectionTemplet))
            return false;
     	 TrDetectionTemplet t=(TrDetectionTemplet)o;
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
    
    public String getDetectionmethod() {
        return detectionmethod;
    }
    public void setDetectionmethod(String detectionmethod) {
        this.detectionmethod = detectionmethod;
    }
    
    public String getDetectionstandard() {
        return detectionstandard;
    }
    public void setDetectionstandard(String detectionstandard) {
        this.detectionstandard = detectionstandard;
    }
    
    public String getDetectionexample() {
        return detectionexample;
    }
    public void setDetectionexample(String detectionexample) {
        this.detectionexample = detectionexample;
    }
    
    public String getRemarks() {
        return remarks;
    }
    public void setRemarks(String remarks) {
        this.remarks = remarks;
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
    
    public String getTemplettype() {
        return templettype;
    }
    public void setTemplettype(String templettype) {
        this.templettype = templettype;
    }
    
	
    /* ******** table column/field name  mnemonic symbol ******** */
    public static final String TABLENAME="tr_detection_templet";
    public static final class FIELDS{
		/**
		 * ID
		 */
        public static final String ID="tr_detection_templet.ID";
		/**
		 * 检查方法
		 */
        public static final String DETECTIONMETHOD="tr_detection_templet.DETECTIONMETHOD";
		/**
		 * 检测标准
		 */
        public static final String DETECTIONSTANDARD="tr_detection_templet.DETECTIONSTANDARD";
		/**
		 * 检测样例（图片、文件）
		 */
        public static final String DETECTIONEXAMPLE="tr_detection_templet.DETECTIONEXAMPLE";
		/**
		 * 备注
		 */
        public static final String REMARKS="tr_detection_templet.REMARKS";
		/**
		 * 修改时间
		 */
        public static final String UPDATETIME="tr_detection_templet.UPDATETIME";
		/**
		 * 修改人
		 */
        public static final String UPDATEPERSON="tr_detection_templet.UPDATEPERSON";
		/**
		 * 模板类型
		 */
        public static final String TEMPLETTYPE="tr_detection_templet.TEMPLETTYPE";
    }
}
