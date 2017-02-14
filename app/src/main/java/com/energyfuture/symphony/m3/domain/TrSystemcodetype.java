package com.energyfuture.symphony.m3.domain;

import java.io.Serializable;

public class TrSystemcodetype implements Serializable{
    /* ******** private property ******** */
	/**
	 * 代码ID
	 */
    private String id;
	/**
	 * 代码名称
	 */
    private String codename;
	/**
	 * 代码值
	 */
    private String codevalue;
	/**
	 * 展示顺序
	 */
    private String forder;

    public String toString(){
        return "{"+"id:"+id+","+"codename:"+codename+","+"codevalue:"+codevalue+","+"forder:"+forder+"}";
    }
    
    public boolean equals(Object o){
        if (!(o instanceof TrSystemcodetype))
            return false;
     	 TrSystemcodetype t=(TrSystemcodetype)o;
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
    
    public String getForder() {
        return forder;
    }
    public void setForder(String forder) {
        this.forder = forder;
    }
    
	
    /* ******** table column/field name  mnemonic symbol ******** */
    public static final String TABLENAME="tr_systemcodetype";
    public static final class FIELDS{
		/**
		 * 代码ID
		 */
        public static final String ID="tr_systemcodetype.ID";
		/**
		 * 代码名称
		 */
        public static final String CODENAME="tr_systemcodetype.CODENAME";
		/**
		 * 代码值
		 */
        public static final String CODEVALUE="tr_systemcodetype.CODEVALUE";
		/**
		 * 展示顺序
		 */
        public static final String FORDER="tr_systemcodetype.FORDER";
    }
}
