package com.energyfuture.symphony.m3.domain;

/**
 * Created by Administrator on 2016/1/28.
 */
public class TrVersionUpdate {
    /**
     * ID
     */
    private String id;
    /**
     * 新增内容
     */
    private String appinfo;
    /**
     * 修改及优化内容
     */
    private String updateinfo;

    /**
     * 删除内容
     */
    private String deleteinfo;

    /**
     * 版本号
     */
    private int versioncode;

    /**
     * 发布号
     */
    private String versionname;

    /**
     * 上传人
     */
    private String loadperson;

    /**
     * 更新时间
     */
    private String updatetime;

    public TrVersionUpdate(){}

    public TrVersionUpdate(String id,String appinfo,String updateinfo,String deleteinfo,int versioncode,String versionname,String loadperson,String updatetime){
        this.id=id;
        this.appinfo=appinfo;
        this.updateinfo=updateinfo;
        this.deleteinfo=deleteinfo;
        this.versioncode=versioncode;
        this.versionname=versionname;
        this.loadperson=loadperson;
        this.updatetime=updatetime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAppinfo() {
        return appinfo;
    }

    public void setAppinfo(String appinfo) {
        this.appinfo = appinfo;
    }

    public String getUpdateinfo() {
        return updateinfo;
    }

    public void setUpdateinfo(String updateinfo) {
        this.updateinfo = updateinfo;
    }

    public String getDeleteinfo() {
        return deleteinfo;
    }

    public void setDeleteinfo(String deleteinfo) {
        this.deleteinfo = deleteinfo;
    }

    public int getVersioncode() {
        return versioncode;
    }

    public void setVersioncode(int versioncode) {
        this.versioncode = versioncode;
    }

    public String getVersionname() {
        return versionname;
    }

    public void setVersionname(String versionname) {
        this.versionname = versionname;
    }

    public String getLoadperson() {
        return loadperson;
    }

    public void setLoadperson(String loadperson) {
        this.loadperson = loadperson;
    }

    public String getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
    }

    public static final String TABLENAME="tr_versionupdate";
}
