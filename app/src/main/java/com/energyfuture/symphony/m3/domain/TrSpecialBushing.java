package com.energyfuture.symphony.m3.domain;

import java.io.Serializable;

public class TrSpecialBushing implements Serializable{
    /* ******** private property ******** */
	/**
	 * ID
	 */
    private String id;
	/**
	 * 检测项目ID
	 */
    private String detectionprojectid;
	/**
	 * 检测内容
	 */
    private String detectiondes;
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
	 * 检查状态（是、否、/）
	 */
    private String status;
	/**
	 * 检查结果描述
	 */
    private String resultdescribe;
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

    public String toString(){
        return "{"+"id:"+id+","+"detectionprojectid:"+detectionprojectid+","+"detectiondes:"+detectiondes+","+"detectionmethod:"+detectionmethod+","+"detectionstandard:"+detectionstandard+","+"detectionexample:"+detectionexample+","+"status:"+status+","+"resultdescribe:"+resultdescribe+","+"remarks:"+remarks+","+"updatetime:"+updatetime+","+"updateperson:"+updateperson+"}";
    }
    
    public boolean equals(Object o){
        if (!(o instanceof TrSpecialBushing))
            return false;
     	 TrSpecialBushing t=(TrSpecialBushing)o;
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
    
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getResultdescribe() {
        return resultdescribe;
    }
    public void setResultdescribe(String resultdescribe) {
        this.resultdescribe = resultdescribe;
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
    
	
    /* ******** table column/field name  mnemonic symbol ******** */
    public static final String TABLENAME="tr_special_bushing";
    public static final class FIELDS{
		/**
		 * ID
		 */
        public static final String ID="tr_special_bushing.ID";
		/**
		 * 检测项目ID
		 */
        public static final String DETECTIONPROJECTID="tr_special_bushing.DETECTIONPROJECTID";
		/**
		 * 检测内容
		 */
        public static final String DETECTIONDES="tr_special_bushing.DETECTIONDES";
		/**
		 * 检查方法
		 */
        public static final String DETECTIONMETHOD="tr_special_bushing.DETECTIONMETHOD";
		/**
		 * 检测标准
		 */
        public static final String DETECTIONSTANDARD="tr_special_bushing.DETECTIONSTANDARD";
		/**
		 * 检测样例（图片、文件）
		 */
        public static final String DETECTIONEXAMPLE="tr_special_bushing.DETECTIONEXAMPLE";
		/**
		 * 检查状态（是、否、/）
		 */
        public static final String STATUS="tr_special_bushing.STATUS";
		/**
		 * 检查结果描述
		 */
        public static final String RESULTDESCRIBE="tr_special_bushing.RESULTDESCRIBE";
		/**
		 * 备注
		 */
        public static final String REMARKS="tr_special_bushing.REMARKS";
		/**
		 * 修改时间
		 */
        public static final String UPDATETIME="tr_special_bushing.UPDATETIME";
		/**
		 * 修改人
		 */
        public static final String UPDATEPERSON="tr_special_bushing.UPDATEPERSON";
    }
}
