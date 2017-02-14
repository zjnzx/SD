package com.energyfuture.symphony.m3.domain;

import java.io.Serializable;

public class AuthResource implements Serializable{
    /* ******** private property ******** */
	/**
	 * 权限ID
	 */
    private String qxid;
	/**
	 * 权限资源类别
	 */
    private String qxzylb;
	/**
	 * 权限资源标识
	 */
    private String qxzybs;
	/**
	 * 权限资源名称
	 */
    private String qxzymc;
	/**
	 * 应用系统代码
	 */
    private String yyxtdm;
	/**
	 * 
	 */
    private String sfky;
	/**
	 * 顺序
	 */
    private Integer sx;
	/**
	 * 
	 */
    private String qxzyurl;
	/**
	 * 
	 */
    private String pid;
	/**
	 * 
	 */
    private String sqlx;
	/**
	 * 
	 */
    private String scyy;
	/**
	 * 
	 */
    private String cjr;

    public String toString(){
        return "{"+"qxid:"+qxid+","+"qxzylb:"+qxzylb+","+"qxzybs:"+qxzybs+","+"qxzymc:"+qxzymc+","+"yyxtdm:"+yyxtdm+","+"sfky:"+sfky+","+"sx:"+sx+","+"qxzyurl:"+qxzyurl+","+"pid:"+pid+","+"sqlx:"+sqlx+","+"scyy:"+scyy+","+"cjr:"+cjr+"}";
    }
    
    public boolean equals(Object o){
        if (!(o instanceof AuthResource))
            return false;
     	 AuthResource t=(AuthResource)o;
        if (qxid==null)
            return this==t;
        return  qxid.equals(t.qxid);
    }
    
     public int hashCode(){
        return qxid.hashCode();
    }

    
    /* ******** property's public getter setter ******** */
    public String getQxid() {
        return qxid;
    }
    public void setQxid(String qxid) {
        this.qxid = qxid;
    }
    
    public String getQxzylb() {
        return qxzylb;
    }
    public void setQxzylb(String qxzylb) {
        this.qxzylb = qxzylb;
    }
    
    public String getQxzybs() {
        return qxzybs;
    }
    public void setQxzybs(String qxzybs) {
        this.qxzybs = qxzybs;
    }
    
    public String getQxzymc() {
        return qxzymc;
    }
    public void setQxzymc(String qxzymc) {
        this.qxzymc = qxzymc;
    }
    
    public String getYyxtdm() {
        return yyxtdm;
    }
    public void setYyxtdm(String yyxtdm) {
        this.yyxtdm = yyxtdm;
    }
    
    public String getSfky() {
        return sfky;
    }
    public void setSfky(String sfky) {
        this.sfky = sfky;
    }
    
    public Integer getSx() {
        return sx;
    }
    public void setSx(Integer sx) {
        this.sx = sx;
    }
    
    public String getQxzyurl() {
        return qxzyurl;
    }
    public void setQxzyurl(String qxzyurl) {
        this.qxzyurl = qxzyurl;
    }
    
    public String getPid() {
        return pid;
    }
    public void setPid(String pid) {
        this.pid = pid;
    }
    
    public String getSqlx() {
        return sqlx;
    }
    public void setSqlx(String sqlx) {
        this.sqlx = sqlx;
    }
    
    public String getScyy() {
        return scyy;
    }
    public void setScyy(String scyy) {
        this.scyy = scyy;
    }
    
    public String getCjr() {
        return cjr;
    }
    public void setCjr(String cjr) {
        this.cjr = cjr;
    }
    
	
    /* ******** table column/field name  mnemonic symbol ******** */
    public static final String TABLENAME="auth_resource";
    public static final class FIELDS{
		/**
		 * 权限ID
		 */
        public static final String QXID="auth_resource.QXID";
		/**
		 * 权限资源类别
		 */
        public static final String QXZYLB="auth_resource.QXZYLB";
		/**
		 * 权限资源标识
		 */
        public static final String QXZYBS="auth_resource.QXZYBS";
		/**
		 * 权限资源名称
		 */
        public static final String QXZYMC="auth_resource.QXZYMC";
		/**
		 * 应用系统代码
		 */
        public static final String YYXTDM="auth_resource.YYXTDM";
		/**
		 * 
		 */
        public static final String SFKY="auth_resource.SFKY";
		/**
		 * 顺序
		 */
        public static final String SX="auth_resource.SX";
		/**
		 * 
		 */
        public static final String QXZYURL="auth_resource.QXZYURL";
		/**
		 * 
		 */
        public static final String PID="auth_resource.PID";
		/**
		 * 
		 */
        public static final String SQLX="auth_resource.SQLX";
		/**
		 * 
		 */
        public static final String SCYY="auth_resource.SCYY";
		/**
		 * 
		 */
        public static final String CJR="auth_resource.CJR";
    }
}
