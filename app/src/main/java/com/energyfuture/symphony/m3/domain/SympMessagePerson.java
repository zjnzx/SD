package com.energyfuture.symphony.m3.domain;

import java.io.Serializable;

public class SympMessagePerson implements Serializable{
    /* ******** private property ******** */
	/**
	 * ID
	 */
    private String id;
	/**
	 * 消息人员ID
	 */
    private String personid;
	/**
	 * 消息人员名称
	 */
    private String personname;
	/**
	 * 备注
	 */
    private String remarks;
	/**
	 * 记录时间
	 */
    private String recordtime;

    public String toString(){
        return "{"+"id:"+id+","+"personid:"+personid+","+"personname:"+personname+","+"remarks:"+remarks+","+"recordtime:"+recordtime+"}";
    }
    
    public boolean equals(Object o){
        if (!(o instanceof SympMessagePerson))
            return false;
     	 SympMessagePerson t=(SympMessagePerson)o;
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
    
    public String getPersonid() {
        return personid;
    }
    public void setPersonid(String personid) {
        this.personid = personid;
    }
    
    public String getPersonname() {
        return personname;
    }
    public void setPersonname(String personname) {
        this.personname = personname;
    }
    
    public String getRemarks() {
        return remarks;
    }
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getRecordtime() {
        return recordtime;
    }

    public void setRecordtime(String recordtime) {
        this.recordtime = recordtime;
    }

    /* ******** table column/field name  mnemonic symbol ******** */
    public static final String TABLENAME="symp_message_person";
    public static final class FIELDS{
		/**
		 * ID
		 */
        public static final String ID="symp_message_person.ID";
		/**
		 * 消息人员ID
		 */
        public static final String PERSONID="symp_message_person.PERSONID";
		/**
		 * 消息人员名称
		 */
        public static final String PERSONNAME="symp_message_person.PERSONNAME";
		/**
		 * 备注
		 */
        public static final String REMARKS="symp_message_person.REMARKS";
		/**
		 * 记录时间
		 */
        public static final String RECORDTIME="symp_message_person.RECORDTIME";
    }
}
