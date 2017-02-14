package com.energyfuture.symphony.m3.domain;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/7/3.
 */
public class TrDataSynchronization implements Serializable {

    private String synchronizationid;
    private String tablename;
    private String keyname;
    private String keynamevalue;
    private String versionnumber;
    private String recordtime;
    private String versionpersonid;
    private String opertype;
    private String datatype;
    private String fileurl;
    private String filename;

    public String getSynchronizationid() {
        return synchronizationid;
    }

    public void setSynchronizationid(String synchronizationid) {
        this.synchronizationid = synchronizationid;
    }

    public String getTablename() {
        return tablename;
    }

    public void setTablename(String tablename) {
        this.tablename = tablename;
    }

    public String getKeyname() {
        return keyname;
    }

    public void setKeyname(String keyname) {
        this.keyname = keyname;
    }

    public String getKeynamevalue() {
        return keynamevalue;
    }

    public void setKeynamevalue(String keynamevalue) {
        this.keynamevalue = keynamevalue;
    }

    public String getVersionnumber() {
        return versionnumber;
    }

    public void setVersionnumber(String versionnumber) {
        this.versionnumber = versionnumber;
    }

    public String getRecordtime() {
        return recordtime;
    }

    public void setRecordtime(String recordtime) {
        this.recordtime = recordtime;
    }

    public String getVersionpersonid() {
        return versionpersonid;
    }

    public void setVersionpersonid(String versionpersonid) {
        this.versionpersonid = versionpersonid;
    }

    public String getOpertype() {
        return opertype;
    }

    public void setOpertype(String opertype) {
        this.opertype = opertype;
    }

    public String getDatatype() {
        return datatype;
    }

    public void setDatatype(String datatype) {
        this.datatype = datatype;
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

    public static final String TABLENAME="tr_data_synchronization";
}
