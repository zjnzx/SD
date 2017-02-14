package com.energyfuture.symphony.m3.domain;

import java.io.Serializable;

public class TrDetectiontempletFile implements Serializable{
    /* ******** private property ******** */
	/**
	 * ID
	 */
    private String id;
	/**
	 * 检测项目模板ID
	 */
    private String templetid;
	/**
	 * 模板类型（代码表）
	 */
    private String filetype;
	/**
	 * 描述（内容描述）
	 */
    private String describetion;
	/**
	 * 是否必填
	 */
    private String required;
	/**
	 * 修改时间
	 */
    private String updatetime;
	/**
	 * 修改人
	 */
    private String updateperson;

    public String toString(){
        return "{"+"id:"+id+","+"templetid:"+templetid+","+"filetype:"+filetype+","+"describetion:"+describetion+","+"required:"+required+","+"updatetime:"+updatetime+","+"updateperson:"+updateperson+"}";
    }
    
    public boolean equals(Object o){
        if (!(o instanceof TrDetectiontempletFile))
            return false;
     	 TrDetectiontempletFile t=(TrDetectiontempletFile)o;
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
    
    public String getFiletype() {
        return filetype;
    }
    public void setFiletype(String filetype) {
        this.filetype = filetype;
    }
    
    public String getDescribetion() {
        return describetion;
    }
    public void setDescribetion(String describetion) {
        this.describetion = describetion;
    }
    
    public String getRequired() {
        return required;
    }
    public void setRequired(String required) {
        this.required = required;
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
    public static final String TABLENAME="tr_detectiontemplet_file";
    public static final class FIELDS{
		/**
		 * ID
		 */
        public static final String ID="tr_detectiontemplet_file.ID";
		/**
		 * 检测项目模板ID
		 */
        public static final String TEMPLETID="tr_detectiontemplet_file.TEMPLETID";
		/**
		 * 模板类型（代码表）
		 */
        public static final String FILETYPE="tr_detectiontemplet_file.FILETYPE";
		/**
		 * 描述（内容描述）
		 */
        public static final String DESCRIBETION="tr_detectiontemplet_file.DESCRIBETION";
		/**
		 * 是否必填
		 */
        public static final String REQUIRED="tr_detectiontemplet_file.REQUIRED";
		/**
		 * 修改时间
		 */
        public static final String UPDATETIME="tr_detectiontemplet_file.UPDATETIME";
		/**
		 * 修改人
		 */
        public static final String UPDATEPERSON="tr_detectiontemplet_file.UPDATEPERSON";
    }
}
