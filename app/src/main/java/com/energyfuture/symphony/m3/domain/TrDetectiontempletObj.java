package com.energyfuture.symphony.m3.domain;

import java.io.Serializable;

public class TrDetectiontempletObj implements Serializable{
    /* ******** private property ******** */
	/**
	 * ID
	 */
    private String id;
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

    @Override
    public String toString() {
        return "TrDetectiontempletObj{" +
                "id='" + id + '\'' +
                ", detectiondes='" + detectiondes + '\'' +
                ", detectionmethod='" + detectionmethod + '\'' +
                ", detectionstandard='" + detectionstandard + '\'' +
                ", detectionexample='" + detectionexample + '\'' +
                ", status='" + status + '\'' +
                ", resultdescribe='" + resultdescribe + '\'' +
                ", remarks='" + remarks + '\'' +
                ", updatetime='" + updatetime + '\'' +
                ", updateperson='" + updateperson + '\'' +
                '}';
    }

    public boolean equals(Object o){
        if (!(o instanceof TrDetectiontempletObj))
            return false;
     	 TrDetectiontempletObj t=(TrDetectiontempletObj)o;
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
    public static final String TABLENAME="tr_detectiontemplet_obj";
    public static final class FIELDS{
		/**
		 * ID
		 */
        public static final String ID="tr_detectiontemplet_obj.ID";
		/**
		 * 检测内容
		 */
        public static final String DETECTIONDES="tr_detectiontemplet_obj.DETECTIONDES";
		/**
		 * 检查方法
		 */
        public static final String DETECTIONMETHOD="tr_detectiontemplet_obj.DETECTIONMETHOD";
		/**
		 * 检测标准
		 */
        public static final String DETECTIONSTANDARD="tr_detectiontemplet_obj.DETECTIONSTANDARD";
		/**
		 * 检测样例（图片、文件）
		 */
        public static final String DETECTIONEXAMPLE="tr_detectiontemplet_obj.DETECTIONEXAMPLE";
		/**
		 * 检查状态（是、否、/）
		 */
        public static final String STATUS="tr_detectiontemplet_obj.STATUS";
		/**
		 * 检查结果描述
		 */
        public static final String RESULTDESCRIBE="tr_detectiontemplet_obj.RESULTDESCRIBE";
		/**
		 * 备注
		 */
        public static final String REMARKS="tr_detectiontemplet_obj.REMARKS";
		/**
		 * 修改时间
		 */
        public static final String UPDATETIME="tr_detectiontemplet_obj.UPDATETIME";
		/**
		 * 修改人
		 */
        public static final String UPDATEPERSON="tr_detectiontemplet_obj.UPDATEPERSON";
    }
}
