package com.energyfuture.symphony.m3.domain;

import java.io.Serializable;

public class TrDetectiontempletDataObj implements Serializable{
    /* ******** private property ******** */
	/**
	 * ID
	 */
    private String id;
	/**
	 * 检测项目模板实体表ID
	 */
    private String detectionobjid;
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
	 * 描述
	 */
    private String describetion;
	/**
	 * 序号
	 */
    private String ordermunber;
	/**
	 * 检查结果
	 */
    private String result;
	/**
	 * 检查结果描述
	 */
    private String resultdescribe;
	/**
	 * 修改时间
	 */
    private String updatetime;
	/**
	 * 修改人
	 */
    private String updateperson;

    public String toString(){
        return "{"+"id:"+id+","+"detectionobjid:"+detectionobjid+","+"required:"+required+","+"dataformat:"+dataformat+","+"dataunit:"+dataunit+","+"describetion:"+describetion+","+"ordermunber:"+ordermunber+","+"result:"+result+","+"resultdescribe:"+resultdescribe+","+"updatetime:"+updatetime+","+"updateperson:"+updateperson+"}";
    }
    
    public boolean equals(Object o){
        if (!(o instanceof TrDetectiontempletDataObj))
            return false;
     	 TrDetectiontempletDataObj t=(TrDetectiontempletDataObj)o;
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
    
    public String getDetectionobjid() {
        return detectionobjid;
    }
    public void setDetectionobjid(String detectionobjid) {
        this.detectionobjid = detectionobjid;
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
    
    public String getDescribetion() {
        return describetion;
    }
    public void setDescribetion(String describetion) {
        this.describetion = describetion;
    }
    
    public String getResult() {
        return result;
    }
    public void setResult(String result) {
        this.result = result;
    }
    
    public String getResultdescribe() {
        return resultdescribe;
    }
    public void setResultdescribe(String resultdescribe) {
        this.resultdescribe = resultdescribe;
    }

    public String getOrdermunber() {
        return ordermunber;
    }

    public void setOrdermunber(String ordermunber) {
        this.ordermunber = ordermunber;
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
    public static final String TABLENAME="tr_detectiontemplet_data_obj";
    public static final class FIELDS{
		/**
		 * ID
		 */
        public static final String ID="tr_detectiontemplet_data_obj.ID";
		/**
		 * 检测项目模板实体表ID
		 */
        public static final String DETECTIONOBJID="tr_detectiontemplet_data_obj.DETECTIONOBJID";
		/**
		 * 是否必填
		 */
        public static final String REQUIRED="tr_detectiontemplet_data_obj.REQUIRED";
		/**
		 * 数据格式（0.01,0.1等）
		 */
        public static final String DATAFORMAT="tr_detectiontemplet_data_obj.DATAFORMAT";
		/**
		 * 数据单位（摄氏度、厘米等）
		 */
        public static final String DATAUNIT="tr_detectiontemplet_data_obj.DATAUNIT";
		/**
		 * 描述
		 */
        public static final String DESCRIBETION="tr_detectiontemplet_data_obj.DESCRIBETION";
		/**
		 * 序号
		 */
        public static final String ORDERMUNBER="tr_detectiontemplet_data_obj.ORDERMUNBER";
		/**
		 * 检查结果
		 */
        public static final String RESULT="tr_detectiontemplet_data_obj.RESULT";
		/**
		 * 检查结果描述
		 */
        public static final String RESULTDESCRIBE="tr_detectiontemplet_data_obj.RESULTDESCRIBE";
		/**
		 * 修改时间
		 */
        public static final String UPDATETIME="tr_detectiontemplet_data_obj.UPDATETIME";
		/**
		 * 修改人
		 */
        public static final String UPDATEPERSON="tr_detectiontemplet_data_obj.UPDATEPERSON";
    }
}
