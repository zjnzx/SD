package com.energyfuture.symphony.m3.domain;

import java.io.Serializable;

public class TrFeedBackAccessory implements Serializable{
    /* ******** private property ******** */
	/**
	 * 附件ID
	 */
    private String id;
	/**
	 * 帖子ID
	 */
    private String themeid;
	/**
	 * 回复ID
	 */
    private String replyid;
	/**
	 * 文件URL
	 */
    private String fileurl;
	/**
	 * 文件名称
	 */
    private String filename;
	/**
	 * 记录时间
	 */
    private String recordtime;
	/**
	 * 附件类型1：图片类型、2：文件类型
	 */
    private String accessorytype;
    private String isupload;

    @Override
    public String toString() {
        return "SympCommunicationAccessory{" +
                "id='" + id + '\'' +
                ", themeid='" + themeid + '\'' +
                ", replyid='" + replyid + '\'' +
                ", fileurl='" + fileurl + '\'' +
                ", filename='" + filename + '\'' +
                ", recordtime='" + recordtime + '\'' +
                ", accessorytype='" + accessorytype + '\'' +
                ", isupload='" + isupload + '\'' +
                '}';
    }

    public boolean equals(Object o){
        if (!(o instanceof TrFeedBackAccessory))
            return false;
     	 TrFeedBackAccessory t=(TrFeedBackAccessory)o;
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
    
    public String getThemeid() {
        return themeid;
    }
    public void setThemeid(String themeid) {
        this.themeid = themeid;
    }
    
    public String getReplyid() {
        return replyid;
    }
    public void setReplyid(String replyid) {
        this.replyid = replyid;
    }
    
    public String getFileurl() {
        return fileurl;
    }
    public void setFileurl(String fileurl) {
        this.fileurl = fileurl;
    }
    
    public String getFilename() {
        return filename;
    }
    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getRecordtime() {
        return recordtime;
    }

    public void setRecordtime(String recordtime) {
        this.recordtime = recordtime;
    }

    public String getAccessorytype() {
        return accessorytype;
    }
    public void setAccessorytype(String accessorytype) {
        this.accessorytype = accessorytype;
    }
    
    public String getIsupload() {
        return isupload;
    }

    public void setIsupload(String isupload) {
        this.isupload = isupload;
    }

    /* ******** table column/field name  mnemonic symbol ******** */
    public static final String TABLENAME="TR_FEEDBACK_ACCESSORY";
}
