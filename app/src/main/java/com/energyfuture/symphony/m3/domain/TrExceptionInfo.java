package com.energyfuture.symphony.m3.domain;

/**
 * Created by Administrator on 2016/1/25.
 */
public class TrExceptionInfo {
    /**
     * ID
     */
    private String id;
    /**
     * 异常所在的类
     */
    private String exceptionclass;
    /**
     * 异常信息
     */
    private String exceptionmessage;

    /**
     * 当前操作用户
     */
    private String userid;

    /**
     * 异常来源
     */
    private String exceptionfrom;

    /**
     * 异常产生的时间
     */
    private String updatetime;

    public TrExceptionInfo(){}

    public TrExceptionInfo(String id,String exceptionclass,String exceptionmessage,String userid,String exceptionfrom,String updatetime){
        this.id=id;
        this.exceptionclass=exceptionclass;
        this.exceptionmessage=exceptionmessage;
        this.userid=userid;
        this.exceptionfrom=exceptionfrom;
        this.updatetime=updatetime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getExceptionclass() {
        return exceptionclass;
    }

    public void setExceptionclass(String exceptionclass) {
        this.exceptionclass = exceptionclass;
    }

    public String getExceptionmessage() {
        return exceptionmessage;
    }

    public void setExceptionmessage(String exceptionmessage) {
        this.exceptionmessage = exceptionmessage;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getExceptionfrom() {
        return exceptionfrom;
    }

    public void setExceptionfrom(String exceptionfrom) {
        this.exceptionfrom = exceptionfrom;
    }

    public String getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
    }

    public static final String TABLENAME="tr_exception_info";

    public static final class FIELDS{
        /**
         * ID
         */
        public static final String ID="tr_exception_info.ID";
        /**
         * 检测工具类型
         */
        public static final String EXCEPTIONCLASS="tr_exception_info.EXCEPTIONCLASS";
        /**
         * 生产厂家
         */
        public static final String EXCEPTIONMESSAGE="tr_exception_info.EXCEPTIONMESSAGE";
        /**
         * 设备型号
         */
        public static final String USERID="tr_exception_info.USERID";
        /**
         * 设备名称
         */
        public static final String EXCEPTIONFROM="tr_exception_info.EXCEPTIONFROM";
        /**
         * 出厂日期
         */
        public static final String UPDATETIME="tr_exception_info.UPDATETIME";
    }
}
