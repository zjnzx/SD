package com.energyfuture.symphony.m3.domain;

import java.io.Serializable;

public class TrDetectiontempletFileObj implements Serializable{
    /* ******** private property ******** */
	/**
	 * ID
	 */
    private String id;
	/**
	 * 检测项目/关系模板实体表ID
	 */
    private String detectionobjid;
	/**
	 * 模板名称
	 */
    private String templetname;
	/**
	 * 模板类型（代码表）
	 */
    private String templettype;
	/**
	 * 描述（内容描述）
	 */
    private String describetion;
	/**
	 * 是否必填
	 */
    private String required;
	/**
	 * 文件号码
	 */
    private String filenumber;
	/**
	 * 文件名称
	 */
    private String filename;
	/**
	 * 文件URL
	 */
    private String fileurl;
	/**
	 * 修改时间
	 */
    private String updatetime;
	/**
	 * 修改人
	 */
    private String updateperson;
	/**
	 * 是否上传
	 */
    private String isupload;
    private String projectid;
    private String taskid;
    private String signid;
    private String orderby;

    @Override
    public String toString() {
        return "TrDetectiontempletFileObj{" +
                "id='" + id + '\'' +
                ", detectionobjid='" + detectionobjid + '\'' +
                ", templetname='" + templetname + '\'' +
                ", templettype='" + templettype + '\'' +
                ", describetion='" + describetion + '\'' +
                ", required='" + required + '\'' +
                ", filenumber='" + filenumber + '\'' +
                ", filename='" + filename + '\'' +
                ", fileurl='" + fileurl + '\'' +
                ", updatetime='" + updatetime + '\'' +
                ", updateperson='" + updateperson + '\'' +
                ", isupload='" + isupload + '\'' +
                ", projectid='" + projectid + '\'' +
                ", taskid='" + taskid + '\'' +
                ", signid='" + signid + '\'' +
                ", orderby='" + orderby + '\'' +
                '}';
    }

    public boolean equals(Object o){
        if (!(o instanceof TrDetectiontempletFileObj))
            return false;
     	 TrDetectiontempletFileObj t=(TrDetectiontempletFileObj)o;
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
    
    public String getTempletname() {
        return templetname;
    }
    public void setTempletname(String templetname) {
        this.templetname = templetname;
    }
    
    public String getTemplettype() {
        return templettype;
    }
    public void setTemplettype(String templettype) {
        this.templettype = templettype;
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
    
    public String getFilenumber() {
        return filenumber;
    }
    public void setFilenumber(String filenumber) {
        this.filenumber = filenumber;
    }
    
    public String getFilename() {
        return filename;
    }
    public void setFilename(String filename) {
        this.filename = filename;
    }
    
    public String getFileurl() {
        return fileurl;
    }
    public void setFileurl(String fileurl) {
        this.fileurl = fileurl;
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
    
    public String getIsupload() {
        return isupload;
    }
    public void setIsupload(String isupload) {
        this.isupload = isupload;
    }

    public String getProjectid() {
        return projectid;
    }

    public void setProjectid(String projectid) {
        this.projectid = projectid;
    }

    public String getTaskid() {
        return taskid;
    }

    public void setTaskid(String taskid) {
        this.taskid = taskid;
    }

    public String getSignid() {
        return signid;
    }

    public void setSignid(String signid) {
        this.signid = signid;
    }

    public String getOrderby() {
        return orderby;
    }

    public void setOrderby(String orderby) {
        this.orderby = orderby;
    }

    /* ******** table column/field name  mnemonic symbol ******** */
    public static final String TABLENAME="tr_detectiontemplet_file_obj";
    public static final class FIELDS{
		/**
		 * ID
		 */
        public static final String ID="tr_detectiontemplet_file_obj.ID";
		/**
		 * 检测项目/关系模板实体表ID
		 */
        public static final String DETECTIONOBJID="tr_detectiontemplet_file_obj.DETECTIONOBJID";
		/**
		 * 模板名称
		 */
        public static final String TEMPLETNAME="tr_detectiontemplet_file_obj.TEMPLETNAME";
		/**
		 * 模板类型（代码表）
		 */
        public static final String TEMPLETTYPE="tr_detectiontemplet_file_obj.TEMPLETTYPE";
		/**
		 * 描述（内容描述）
		 */
        public static final String DESCRIBETION="tr_detectiontemplet_file_obj.DESCRIBETION";
		/**
		 * 是否必填
		 */
        public static final String REQUIRED="tr_detectiontemplet_file_obj.REQUIRED";
		/**
		 * 文件号码
		 */
        public static final String FILENUMBER="tr_detectiontemplet_file_obj.FILENUMBER";
		/**
		 * 文件名称
		 */
        public static final String FILENAME="tr_detectiontemplet_file_obj.FILENAME";
		/**
		 * 文件URL
		 */
        public static final String FILEURL="tr_detectiontemplet_file_obj.FILEURL";
		/**
		 * 修改时间
		 */
        public static final String UPDATETIME="tr_detectiontemplet_file_obj.UPDATETIME";
		/**
		 * 修改人
		 */
        public static final String UPDATEPERSON="tr_detectiontemplet_file_obj.UPDATEPERSON";
		/**
		 * 是否上传
		 */
        public static final String ISUPLOAD="tr_detectiontemplet_file_obj.ISUPLOAD";
    }
}
