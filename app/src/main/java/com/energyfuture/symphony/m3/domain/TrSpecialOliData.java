package com.energyfuture.symphony.m3.domain;

import java.io.Serializable;

public class TrSpecialOliData implements Serializable{
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
	 * 是否必填
	 */
    private String required;
	/**
	 * 数据格式（0.01,0.1等）
	 */
    private String dataformat;
	/**
	 * 数据单位（摄氏度、厘米等）
	 */
    private String dataunit;
	/**
	 * 检测内容
	 */
    private String detectiondes;
	/**
	 * 检查结果
	 */
    private String result1;
    private String result2;
    private String result3;
	/**
	 * 检查结果描述
	 */
    private String resultdescribe;
	/**
	 * 移动端展示
	 */
    private String mobildisplay;
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
    private String orderby;

    @Override
    public String toString() {
        return "TrSpecialOliData{" +
                "id='" + id + '\'' +
                ", detectionprojectid='" + detectionprojectid + '\'' +
                ", detectionmethod='" + detectionmethod + '\'' +
                ", detectionstandard='" + detectionstandard + '\'' +
                ", detectionexample='" + detectionexample + '\'' +
                ", status='" + status + '\'' +
                ", required='" + required + '\'' +
                ", dataformat='" + dataformat + '\'' +
                ", dataunit='" + dataunit + '\'' +
                ", detectiondes='" + detectiondes + '\'' +
                ", result1='" + result1 + '\'' +
                ", result2='" + result2 + '\'' +
                ", result3='" + result3 + '\'' +
                ", resultdescribe='" + resultdescribe + '\'' +
                ", mobildisplay='" + mobildisplay + '\'' +
                ", remarks='" + remarks + '\'' +
                ", updatetime='" + updatetime + '\'' +
                ", updateperson='" + updateperson + '\'' +
                ", orderby=" + orderby +
                '}';
    }

    public boolean equals(Object o){
        if (!(o instanceof TrSpecialOliData))
            return false;
     	 TrSpecialOliData t=(TrSpecialOliData)o;
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
    
    public String getRequired() {
        return required;
    }
    public void setRequired(String required) {
        this.required = required;
    }
    
    public String getDataformat() {
        return dataformat;
    }
    public void setDataformat(String dataformat) {
        this.dataformat = dataformat;
    }
    
    public String getDataunit() {
        return dataunit;
    }
    public void setDataunit(String dataunit) {
        this.dataunit = dataunit;
    }
    
    public String getDetectiondes() {
        return detectiondes;
    }
    public void setDetectiondes(String detectiondes) {
        this.detectiondes = detectiondes;
    }

    public String getResult1() {
        return result1;
    }

    public void setResult1(String result1) {
        this.result1 = result1;
    }

    public String getResult2() {
        return result2;
    }

    public void setResult2(String result2) {
        this.result2 = result2;
    }

    public String getResult3() {
        return result3;
    }

    public void setResult3(String result3) {
        this.result3 = result3;
    }

    public String getResultdescribe() {
        return resultdescribe;
    }
    public void setResultdescribe(String resultdescribe) {
        this.resultdescribe = resultdescribe;
    }
    
    public String getMobildisplay() {
        return mobildisplay;
    }
    public void setMobildisplay(String mobildisplay) {
        this.mobildisplay = mobildisplay;
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

    public String getOrderby() {
        return orderby;
    }

    public void setOrderby(String orderby) {
        this.orderby = orderby;
    }

    /* ******** table column/field name  mnemonic symbol ******** */
    public static final String TABLENAME="tr_special_oli_data";
    public static final class FIELDS{
		/**
		 * ID
		 */
        public static final String ID="tr_special_oli_data.ID";
		/**
		 * 检测项目ID
		 */
        public static final String DETECTIONPROJECTID="tr_special_oli_data.DETECTIONPROJECTID";
		/**
		 * 检查方法
		 */
        public static final String DETECTIONMETHOD="tr_special_oli_data.DETECTIONMETHOD";
		/**
		 * 检测标准
		 */
        public static final String DETECTIONSTANDARD="tr_special_oli_data.DETECTIONSTANDARD";
		/**
		 * 检测样例（图片、文件）
		 */
        public static final String DETECTIONEXAMPLE="tr_special_oli_data.DETECTIONEXAMPLE";
		/**
		 * 检查状态（是、否、/）
		 */
        public static final String STATUS="tr_special_oli_data.STATUS";
		/**
		 * 是否必填
		 */
        public static final String REQUIRED="tr_special_oli_data.REQUIRED";
		/**
		 * 数据格式（0.01,0.1等）
		 */
        public static final String DATAFORMAT="tr_special_oli_data.DATAFORMAT";
		/**
		 * 数据单位（摄氏度、厘米等）
		 */
        public static final String DATAUNIT="tr_special_oli_data.DATAUNIT";
		/**
		 * 检测内容
		 */
        public static final String DETECTIONDES="tr_special_oli_data.DETECTIONDES";
		/**
		 * 检查结果
		 */
        public static final String RESULT="tr_special_oli_data.RESULT";
		/**
		 * 检查结果描述
		 */
        public static final String RESULTDESCRIBE="tr_special_oli_data.RESULTDESCRIBE";
		/**
		 * 移动端展示
		 */
        public static final String MOBILDISPLAY="tr_special_oli_data.MOBILDISPLAY";
		/**
		 * 备注
		 */
        public static final String REMARKS="tr_special_oli_data.REMARKS";
		/**
		 * 修改时间
		 */
        public static final String UPDATETIME="tr_special_oli_data.UPDATETIME";
		/**
		 * 修改人
		 */
        public static final String UPDATEPERSON="tr_special_oli_data.UPDATEPERSON";
    }
}
