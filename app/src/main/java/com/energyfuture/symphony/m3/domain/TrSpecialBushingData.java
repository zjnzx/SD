package com.energyfuture.symphony.m3.domain;

import java.io.Serializable;

public class TrSpecialBushingData implements Serializable{
    /* ******** private property ******** */
	/**
	 * ID
	 */
    private String id;
	/**
	 * 检测位置ID
	 */
    private String positionid;
	/**
	 * 检查结果
	 */
    private String result;
	/**
	 * 序号
	 */
    private String ordermunber;
	/**
	 * 数据项描述描述
	 */
    private String describetion;
	/**
	 * 修改时间
	 */
    private String updatetime;
	/**
	 * 修改人
	 */
    private String updateperson;

    public String toString(){
        return "{"+"id:"+id+","+"positionid:"+positionid+","+"result:"+result+","+"ordermunber:"+ordermunber+","+"describetion:"+describetion+","+"updatetime:"+updatetime+","+"updateperson:"+updateperson+"}";
    }
    
    public boolean equals(Object o){
        if (!(o instanceof TrSpecialBushingData))
            return false;
     	 TrSpecialBushingData t=(TrSpecialBushingData)o;
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
    
    public String getPositionid() {
        return positionid;
    }
    public void setPositionid(String positionid) {
        this.positionid = positionid;
    }
    
    public String getResult() {
        return result;
    }
    public void setResult(String result) {
        this.result = result;
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
    public static final String TABLENAME="tr_special_bushing_data";
    public static final class FIELDS{
		/**
		 * ID
		 */
        public static final String ID="tr_special_bushing_data.ID";
		/**
		 * 检测位置ID
		 */
        public static final String POSITIONID="tr_special_bushing_data.POSITIONID";
		/**
		 * 检查结果
		 */
        public static final String RESULT="tr_special_bushing_data.RESULT";
		/**
		 * 序号
		 */
        public static final String ORDERMUNBER="tr_special_bushing_data.ORDERMUNBER";
		/**
		 * 数据项描述描述
		 */
        public static final String DESCRIBETION="tr_special_bushing_data.DESCRIBETION";
		/**
		 * 修改时间
		 */
        public static final String UPDATETIME="tr_special_bushing_data.UPDATETIME";
		/**
		 * 修改人
		 */
        public static final String UPDATEPERSON="tr_special_bushing_data.UPDATEPERSON";
    }
}
