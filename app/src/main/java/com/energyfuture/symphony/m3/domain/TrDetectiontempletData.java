package com.energyfuture.symphony.m3.domain;

import java.io.Serializable;

public class TrDetectiontempletData implements Serializable{
    /* ******** private property ******** */
	/**
	 * 区域设备ID
	 */
    private String id;
	/**
	 * 区域ID
	 */
    private String templetid;
	/**
	 * 
	 */
    private String required;
	/**
	 * 
	 */
    private String dataformat;
	/**
	 * 
	 */
    private String dataunit;
	/**
	 * 描述
	 */
    private String describetion;
	/**
	 * 
	 */
    private String ordermunber;
	/**
	 * 
	 */
    private String updatetime;
	/**
	 * 
	 */
    private String updateperson;

    public String toString(){
        return "{"+"id:"+id+","+"templetid:"+templetid+","+"required:"+required+","+"dataformat:"+dataformat+","+"dataunit:"+dataunit+","+"describetion:"+describetion+","+"ordermunber:"+ordermunber+","+"updatetime:"+updatetime+","+"updateperson:"+updateperson+"}";
    }
    
    public boolean equals(Object o){
        if (!(o instanceof TrDetectiontempletData))
            return false;
     	 TrDetectiontempletData t=(TrDetectiontempletData)o;
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
    
    public String getTempletid() {
        return templetid;
    }
    public void setTempletid(String templetid) {
        this.templetid = templetid;
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
    public static final String TABLENAME="tr_detectiontemplet_data";
    public static final class FIELDS{
		/**
		 * 区域设备ID
		 */
        public static final String ID="tr_detectiontemplet_data.ID";
		/**
		 * 区域ID
		 */
        public static final String TEMPLETID="tr_detectiontemplet_data.TEMPLETID";
		/**
		 * 
		 */
        public static final String REQUIRED="tr_detectiontemplet_data.REQUIRED";
		/**
		 * 
		 */
        public static final String DATAFORMAT="tr_detectiontemplet_data.DATAFORMAT";
		/**
		 * 
		 */
        public static final String DATAUNIT="tr_detectiontemplet_data.DATAUNIT";
		/**
		 * 描述
		 */
        public static final String DESCRIBETION="tr_detectiontemplet_data.DESCRIBETION";
		/**
		 * 
		 */
        public static final String ORDERMUNBER="tr_detectiontemplet_data.ORDERMUNBER";
		/**
		 * 
		 */
        public static final String UPDATETIME="tr_detectiontemplet_data.UPDATETIME";
		/**
		 * 
		 */
        public static final String UPDATEPERSON="tr_detectiontemplet_data.UPDATEPERSON";
    }
}
