package com.energyfuture.symphony.m3.domain;

import java.io.Serializable;

public class TrCommonTools implements Serializable{
    /* ******** private property ******** */
    private String id;
    private String filename;
    private String fileurl;
    private String uploadpersonid;
    private String uploadtime;
    private String remarks;
    private String type;
    private String filesize;

    @Override
    public String toString() {
        return "TrCommonTools{" +
                "id='" + id + '\'' +
                ", filename='" + filename + '\'' +
                ", fileurl='" + fileurl + '\'' +
                ", uploadpersonid='" + uploadpersonid + '\'' +
                ", uploadtime='" + uploadtime + '\'' +
                ", remarks='" + remarks + '\'' +
                ", type='" + type + '\'' +
                ", filesize='" + filesize + '\'' +
                '}';
    }

    public boolean equals(Object o){
        if (!(o instanceof TrCommonTools))
            return false;
     	 TrCommonTools t=(TrCommonTools)o;
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

    public String getUploadpersonid() {
        return uploadpersonid;
    }

    public void setUploadpersonid(String uploadpersonid) {
        this.uploadpersonid = uploadpersonid;
    }

    public String getUploadtime() {
        return uploadtime;
    }

    public void setUploadtime(String uploadtime) {
        this.uploadtime = uploadtime;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFilesize() {
        return filesize;
    }

    public void setFilesize(String filesize) {
        this.filesize = filesize;
    }

    /* ******** table column/field name  mnemonic symbol ******** */
    public static final String TABLENAME="tr_commontools";
}
