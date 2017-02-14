package com.energyfuture.symphony.m3.domain;

import java.io.Serializable;

public class AuthResType implements Serializable{
    /* ******** private property ******** */
	/**
	 * 权限资源类别
	 */
    private String qxzylb;
	/**
	 * 权限类别描述
	 */
    private String qxlbms;
	/**
	 * 是否可用（1可用，0不可用）
	 */
    private String sfky;
	/**
	 * 资源属性
	 */
    private String zysx;
	/**
	 * 应用系统代码
	 */
    private String yyxtdm;
	/**
	 * 顺序
	 */
    private Integer sx;

    public String toString(){
        return "{"+"qxzylb:"+qxzylb+","+"qxlbms:"+qxlbms+","+"sfky:"+sfky+","+"zysx:"+zysx+","+"yyxtdm:"+yyxtdm+","+"sx:"+sx+"}";
    }
    
    public boolean equals(Object o){
        if (!(o instanceof AuthResType))
            return false;
     	 AuthResType t=(AuthResType)o;
        if (qxzylb==null)
            return this==t;
        return  qxzylb.equals(t.qxzylb);
    }
    
     public int hashCode(){
        return qxzylb.hashCode();
    }

    
    /* ******** property's public getter setter ******** */
    public String getQxzylb() {
        return qxzylb;
    }
    public void setQxzylb(String qxzylb) {
        this.qxzylb = qxzylb;
    }
    
    public String getQxlbms() {
        return qxlbms;
    }
    public void setQxlbms(String qxlbms) {
        this.qxlbms = qxlbms;
    }
    
    public String getSfky() {
        return sfky;
    }
    public void setSfky(String sfky) {
        this.sfky = sfky;
    }
    
    public String getZysx() {
        return zysx;
    }
    public void setZysx(String zysx) {
        this.zysx = zysx;
    }
    
    public String getYyxtdm() {
        return yyxtdm;
    }
    public void setYyxtdm(String yyxtdm) {
        this.yyxtdm = yyxtdm;
    }
    
    public Integer getSx() {
        return sx;
    }
    public void setSx(Integer sx) {
        this.sx = sx;
    }
    
	
    /* ******** table column/field name  mnemonic symbol ******** */
    public static final String TABLENAME="auth_res_type";
    public static final class FIELDS{
		/**
		 * 权限资源类别
		 */
        public static final String QXZYLB="auth_res_type.QXZYLB";
		/**
		 * 权限类别描述
		 */
        public static final String QXLBMS="auth_res_type.QXLBMS";
		/**
		 * 是否可用（1可用，0不可用）
		 */
        public static final String SFKY="auth_res_type.SFKY";
		/**
		 * 资源属性
		 */
        public static final String ZYSX="auth_res_type.ZYSX";
		/**
		 * 应用系统代码
		 */
        public static final String YYXTDM="auth_res_type.YYXTDM";
		/**
		 * 顺序
		 */
        public static final String SX="auth_res_type.SX";
    }
}
