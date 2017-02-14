package com.energyfuture.symphony.m3.domain;

import java.io.Serializable;

public class Department implements Serializable{
    /* ******** private property ******** */
	/**
	 * 部门ID
	 */
    private String bmid;
	/**
	 * 部门名称
	 */
    private String bmmc;
	/**
	 * 所属部门
	 */
    private String ssbm;
	/**
	 * 部门类型
	 */
    private String bmlx;

    public String toString(){
        return "{"+"bmid:"+bmid+","+"bmmc:"+bmmc+","+"ssbm:"+ssbm+","+"bmlx:"+bmlx+"}";
    }
    
    public boolean equals(Object o){
        if (!(o instanceof Department))
            return false;
     	 Department t=(Department)o;
        if (bmid==null)
            return this==t;
        return  bmid.equals(t.bmid);
    }
    
     public int hashCode(){
        return bmid.hashCode();
    }

    
    /* ******** property's public getter setter ******** */
    public String getBmid() {
        return bmid;
    }
    public void setBmid(String bmid) {
        this.bmid = bmid;
    }
    
    public String getBmmc() {
        return bmmc;
    }
    public void setBmmc(String bmmc) {
        this.bmmc = bmmc;
    }
    
    public String getSsbm() {
        return ssbm;
    }
    public void setSsbm(String ssbm) {
        this.ssbm = ssbm;
    }
    
    public String getBmlx() {
        return bmlx;
    }
    public void setBmlx(String bmlx) {
        this.bmlx = bmlx;
    }
    
	
    /* ******** table column/field name  mnemonic symbol ******** */
    public static final String TABLENAME="department";
    public static final class FIELDS{
		/**
		 * 部门ID
		 */
        public static final String BMID="department.BMID";
		/**
		 * 部门名称
		 */
        public static final String BMMC="department.BMMC";
		/**
		 * 所属部门
		 */
        public static final String SSBM="department.SSBM";
		/**
		 * 部门类型
		 */
        public static final String BMLX="department.BMLX";
    }
}
