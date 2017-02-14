package com.energyfuture.symphony.m3.domain;

import java.io.Serializable;
import java.util.Date;

public class TrLog implements Serializable{
    /* ******** private property ******** */
	/**
	 * 日志ID
	 */
    private String logid;
	/**
	 * 用户ID
	 */
    private String userid;
	/**
	 * 用户姓名
	 */
    private String username;
	/**
	 * 操作信息
	 */
    private String czxx;
	/**
	 * 操作时间
	 */
    private Date czdate;
	/**
	 * 操作类型
	 */
    private String cztype;
	/**
	 * 操作状态；0：操作成功，1：操作失败
	 */
    private String czstate;

    public String toString(){
        return "{"+"logid:"+logid+","+"userid:"+userid+","+"username:"+username+","+"czxx:"+czxx+","+"czdate:"+czdate+","+"cztype:"+cztype+","+"czstate:"+czstate+"}";
    }
    
    public boolean equals(Object o){
        if (!(o instanceof TrLog))
            return false;
     	 TrLog t=(TrLog)o;
        if (logid==null)
            return this==t;
        return  logid.equals(t.logid);
    }
    
     public int hashCode(){
        return logid.hashCode();
    }

    
    /* ******** property's public getter setter ******** */
    public String getLogid() {
        return logid;
    }
    public void setLogid(String logid) {
        this.logid = logid;
    }
    
    public String getUserid() {
        return userid;
    }
    public void setUserid(String userid) {
        this.userid = userid;
    }
    
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getCzxx() {
        return czxx;
    }
    public void setCzxx(String czxx) {
        this.czxx = czxx;
    }
    
    public Date getCzdate() {
        return czdate;
    }
    public void setCzdate(Date czdate) {
        this.czdate = czdate;
    }
    
    public String getCztype() {
        return cztype;
    }
    public void setCztype(String cztype) {
        this.cztype = cztype;
    }
    
    public String getCzstate() {
        return czstate;
    }
    public void setCzstate(String czstate) {
        this.czstate = czstate;
    }
    
	
    /* ******** table column/field name  mnemonic symbol ******** */
    public static final String TABLENAME="tr_log";
    public static final class FIELDS{
		/**
		 * 日志ID
		 */
        public static final String LOGID="tr_log.LOGID";
		/**
		 * 用户ID
		 */
        public static final String USERID="tr_log.USERID";
		/**
		 * 用户姓名
		 */
        public static final String USERNAME="tr_log.USERNAME";
		/**
		 * 操作信息
		 */
        public static final String CZXX="tr_log.CZXX";
		/**
		 * 操作时间
		 */
        public static final String CZDATE="tr_log.CZDATE";
		/**
		 * 操作类型
		 */
        public static final String CZTYPE="tr_log.CZTYPE";
		/**
		 * 操作状态；0：操作成功，1：操作失败
		 */
        public static final String CZSTATE="tr_log.CZSTATE";
    }
}
