package com.energyfuture.symphony.m3.domain;

import java.io.Serializable;

public class RoleInfo implements Serializable{
    /* ******** private property ******** */
	/**
	 * 角色ID
	 */
    private String jsid;
	/**
	 * 角色名称
	 */
    private String jsmc;
	/**
	 * 
	 */
    private String sfky;
	/**
	 * 
	 */
    private String cjr;
	/**
	 * 
	 */
    private String cjsj;
	/**
	 * 
	 */
    private String jgdm;
	/**
	 * 
	 */
    private String jssx;
	/**
	 * 
	 */
    private String yyxtdm;
	/**
	 * 
	 */
    private String sqlx;
	/**
	 * 
	 */
    private String scyy;

    public String toString(){
        return "{"+"jsid:"+jsid+","+"jsmc:"+jsmc+","+"sfky:"+sfky+","+"cjr:"+cjr+","+"cjsj:"+cjsj+","+"jgdm:"+jgdm+","+"jssx:"+jssx+","+"yyxtdm:"+yyxtdm+","+"sqlx:"+sqlx+","+"scyy:"+scyy+"}";
    }
    
    public boolean equals(Object o){
        if (!(o instanceof RoleInfo))
            return false;
     	 RoleInfo t=(RoleInfo)o;
        if (jsid==null)
            return this==t;
        return  jsid.equals(t.jsid);
    }
    
     public int hashCode(){
        return jsid.hashCode();
    }

    
    /* ******** property's public getter setter ******** */
    public String getJsid() {
        return jsid;
    }
    public void setJsid(String jsid) {
        this.jsid = jsid;
    }
    
    public String getJsmc() {
        return jsmc;
    }
    public void setJsmc(String jsmc) {
        this.jsmc = jsmc;
    }
    
    public String getSfky() {
        return sfky;
    }
    public void setSfky(String sfky) {
        this.sfky = sfky;
    }
    
    public String getCjr() {
        return cjr;
    }
    public void setCjr(String cjr) {
        this.cjr = cjr;
    }
    
    public String getCjsj() {
        return cjsj;
    }
    public void setCjsj(String cjsj) {
        this.cjsj = cjsj;
    }
    
    public String getJgdm() {
        return jgdm;
    }
    public void setJgdm(String jgdm) {
        this.jgdm = jgdm;
    }
    
    public String getJssx() {
        return jssx;
    }
    public void setJssx(String jssx) {
        this.jssx = jssx;
    }
    
    public String getYyxtdm() {
        return yyxtdm;
    }
    public void setYyxtdm(String yyxtdm) {
        this.yyxtdm = yyxtdm;
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
    
	
    /* ******** table column/field name  mnemonic symbol ******** */
    public static final String TABLENAME="role_info";
    public static final class FIELDS{
		/**
		 * 角色ID
		 */
        public static final String JSID="role_info.JSID";
		/**
		 * 角色名称
		 */
        public static final String JSMC="role_info.JSMC";
		/**
		 * 
		 */
        public static final String SFKY="role_info.SFKY";
		/**
		 * 
		 */
        public static final String CJR="role_info.CJR";
		/**
		 * 
		 */
        public static final String CJSJ="role_info.CJSJ";
		/**
		 * 
		 */
        public static final String JGDM="role_info.JGDM";
		/**
		 * 
		 */
        public static final String JSSX="role_info.JSSX";
		/**
		 * 
		 */
        public static final String YYXTDM="role_info.YYXTDM";
		/**
		 * 
		 */
        public static final String SQLX="role_info.SQLX";
		/**
		 * 
		 */
        public static final String SCYY="role_info.SCYY";
    }
}
