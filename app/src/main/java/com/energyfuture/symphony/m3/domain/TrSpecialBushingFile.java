package com.energyfuture.symphony.m3.domain;

import java.io.Serializable;

public class TrSpecialBushingFile implements Serializable{
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
    private String orderby;

    @Override
    public String toString() {
        return "TrSpecialBushingFile{" +
                "id='" + id + '\'' +
                ", positionid='" + positionid + '\'' +
                ", required='" + required + '\'' +
                ", filenumber='" + filenumber + '\'' +
                ", filename='" + filename + '\'' +
                ", fileurl='" + fileurl + '\'' +
                ", updatetime='" + updatetime + '\'' +
                ", updateperson='" + updateperson + '\'' +
                ", isupload='" + isupload + '\'' +
                ", projectid='" + projectid + '\'' +
                ", taskid='" + taskid + '\'' +
                ", orderby='" + orderby + '\'' +
                '}';
    }

    public boolean equals(Object o){
        if (!(o instanceof TrSpecialBushingFile))
            return false;
     	 TrSpecialBushingFile t=(TrSpecialBushingFile)o;
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

    public String getOrderby() {
        return orderby;
    }

    public void setOrderby(String orderby) {
        this.orderby = orderby;
    }

    /* ******** table column/field name  mnemonic symbol ******** */
    public static final String TABLENAME="tr_special_bushing_file";
    public static final class FIELDS{
		/**
		 * ID
		 */
        public static final String ID="tr_special_bushing_file.ID";
		/**
		 * 检测位置ID
		 */
        public static final String POSITIONID="tr_special_bushing_file.POSITIONID";
		/**
		 * 是否必填
		 */
        public static final String REQUIRED="tr_special_bushing_file.REQUIRED";
		/**
		 * 文件号码
		 */
        public static final String FILENUMBER="tr_special_bushing_file.FILENUMBER";
		/**
		 * 文件名称
		 */
        public static final String FILENAME="tr_special_bushing_file.FILENAME";
		/**
		 * 文件URL
		 */
        public static final String FILEURL="tr_special_bushing_file.FILEURL";
		/**
		 * 修改时间
		 */
        public static final String UPDATETIME="tr_special_bushing_file.UPDATETIME";
		/**
		 * 修改人
		 */
        public static final String UPDATEPERSON="tr_special_bushing_file.UPDATEPERSON";
		/**
		 * 是否上传
		 */
        public static final String ISUPLOAD="tr_special_bushing_file.ISUPLOAD";
    }
}
