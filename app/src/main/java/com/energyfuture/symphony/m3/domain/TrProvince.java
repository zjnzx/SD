package com.energyfuture.symphony.m3.domain;

import java.io.Serializable;

public class TrProvince implements Serializable{
    /* ******** private property ******** */
	/**
	 * 省公司编号
	 */
    private String provinceid;
	/**
	 * 省公司名称
	 */
    private String provincename;

    public String toString(){
        return "{"+"provinceid:"+provinceid+","+"provincename:"+provincename+"}";
    }
    
    public boolean equals(Object o){
        if (!(o instanceof TrProvince))
            return false;
     	 TrProvince t=(TrProvince)o;
        if (provinceid==null)
            return this==t;
        return  provinceid.equals(t.provinceid);
    }
    
     public int hashCode(){
        return provinceid.hashCode();
    }

    
    /* ******** property's public getter setter ******** */
    public String getProvinceid() {
        return provinceid;
    }
    public void setProvinceid(String provinceid) {
        this.provinceid = provinceid;
    }
    
    public String getProvincename() {
        return provincename;
    }
    public void setProvincename(String provincename) {
        this.provincename = provincename;
    }
    
	
    /* ******** table column/field name  mnemonic symbol ******** */
    public static final String TABLENAME="tr_province";
    public static final class FIELDS{
		/**
		 * 省公司编号
		 */
        public static final String PROVINCEID="tr_province.PROVINCEID";
		/**
		 * 省公司名称
		 */
        public static final String PROVINCENAME="tr_province.PROVINCENAME";
    }
}
