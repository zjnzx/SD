package com.energyfuture.symphony.m3.domain;

import java.io.Serializable;

public class TrSystemcode implements Serializable{
    /* ******** private property ******** */
	/**
	 * 代码ID
	 */
    private String codeid;
	/**
	 * 代码名称
	 */
    private String codename;
	/**
	 * 代码值
	 */
    private String codevalue;
	/**
	 * 代码类型
	 */
    private String codetype;
	/**
	 * 展示顺序
	 */
    private String forder;
	/**
	 * 
	 */
    private String pid;
	/**
	 * 
	 */
    private String isleaf;

    public String toString(){
        return "{"+"codeid:"+codeid+","+"codename:"+codename+","+"codevalue:"+codevalue+","+"codetype:"+codetype+","+"forder:"+forder+","+"pid:"+pid+","+"isleaf:"+isleaf+"}";
    }
    
    public boolean equals(Object o){
        if (!(o instanceof TrSystemcode))
            return false;
     	 TrSystemcode t=(TrSystemcode)o;
        if (codeid==null)
            return this==t;
        return  codeid.equals(t.codeid);
    }
    
     public int hashCode(){
        return codeid.hashCode();
    }

    
    /* ******** property's public getter setter ******** */
    public String getCodeid() {
        return codeid;
    }
    public void setCodeid(String codeid) {
        this.codeid = codeid;
    }
    
    public String getCodename() {
        return codename;
    }
    public void setCodename(String codename) {
        this.codename = codename;
    }
    
    public String getCodevalue() {
        return codevalue;
    }
    public void setCodevalue(String codevalue) {
        this.codevalue = codevalue;
    }
    
    public String getCodetype() {
        return codetype;
    }
    public void setCodetype(String codetype) {
        this.codetype = codetype;
    }
    
    public String getForder() {
        return forder;
    }
    public void setForder(String forder) {
        this.forder = forder;
    }
    
    public String getPid() {
        return pid;
    }
    public void setPid(String pid) {
        this.pid = pid;
    }
    
    public String getIsleaf() {
        return isleaf;
    }
    public void setIsleaf(String isleaf) {
        this.isleaf = isleaf;
    }
    
	
    /* ******** table column/field name  mnemonic symbol ******** */
    public static final String TABLENAME="tr_systemcode";
    public static final class FIELDS{
		/**
		 * 代码ID
		 */
        public static final String CODEID="tr_systemcode.CODEID";
		/**
		 * 代码名称
		 */
        public static final String CODENAME="tr_systemcode.CODENAME";
		/**
		 * 代码值
		 */
        public static final String CODEVALUE="tr_systemcode.CODEVALUE";
		/**
		 * 代码类型
		 */
        public static final String CODETYPE="tr_systemcode.CODETYPE";
		/**
		 * 展示顺序
		 */
        public static final String FORDER="tr_systemcode.FORDER";
		/**
		 * 
		 */
        public static final String PID="tr_systemcode.PID";
		/**
		 * 
		 */
        public static final String ISLEAF="tr_systemcode.ISLEAF";
    }
}
