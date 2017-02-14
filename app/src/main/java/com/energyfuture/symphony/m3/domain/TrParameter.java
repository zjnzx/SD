package com.energyfuture.symphony.m3.domain;

import java.io.Serializable;

public class TrParameter implements Serializable{
    /* ******** private property ******** */
	/**
	 * ID
	 */
    private String id;
    /**
     * 参数类型
     */
    private String parametertype;
    /**
     * 厂家
     */
    private String company;
    /**
     * 型号
     */
    private String model;
    /**
     * SD卡文件路径
     */
    private String sdfileurl;
    /**
     * 文件名前缀
     */
    private String fileprefix;
    /**
     * 文件名后缀
     */
    private String filesuffix;
    /**
     * 修改人
     */
    private String updateperson;
    /**
     * 修改时间
     */
    private String updatetime;

    public String toString(){
        return "{"+"id:"+id+","+"parametertype:"+parametertype+","+"company:"+company+","+"model:"+model+","+"sdfileurl:"+sdfileurl+","+"fileprefix:"+fileprefix+","+"filesuffix:"+filesuffix+","+"updateperson:"+updateperson+","+"updatetime:"+updatetime+"}";
    }
    
    public boolean equals(Object o){
        if (!(o instanceof TrParameter))
            return false;
     	 TrParameter t=(TrParameter)o;
        if (id==null)
            return this==t;
        return  id.equals(t.id);
    }
    
     public int hashCode(){
        return id.hashCode();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParametertype() {
        return parametertype;
    }

    public void setParametertype(String parametertype) {
        this.parametertype = parametertype;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getSdfileurl() {
        return sdfileurl;
    }

    public void setSdfileurl(String sdfileurl) {
        this.sdfileurl = sdfileurl;
    }

    public String getFileprefix() {
        return fileprefix;
    }

    public void setFileprefix(String fileprefix) {
        this.fileprefix = fileprefix;
    }

    public String getFilesuffix() {
        return filesuffix;
    }

    public void setFilesuffix(String filesuffix) {
        this.filesuffix = filesuffix;
    }

    public String getUpdateperson() {
        return updateperson;
    }

    public void setUpdateperson(String updateperson) {
        this.updateperson = updateperson;
    }

    public String getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
    }

    /* ******** table column/field name  mnemonic symbol ******** */
    public static final String TABLENAME="tr_parameter";
    public static final class FIELDS{
		/**
		 * ID
		 */
        public static final String ID="tr_parameter.ID";
		/**
		 * 参数类型
		 */
        public static final String PARAMETERTYPE="tr_parameter.PARAMETERTYPE";
		/**
		 * 厂家
		 */
        public static final String COMPANY="tr_parameter.COMPANY";
		/**
		 * 型号
		 */
        public static final String MODEL="tr_parameter.MODEL";
		/**
		 * SD卡文件路径
		 */
        public static final String SDFILEURL="tr_parameter.SDFILEURL";
		/**
		 * 文件名前缀
		 */
        public static final String FILEPREFIX="tr_parameter.FILEPREFIX";
		/**
		 * 文件名后缀
		 */
        public static final String FILESUFFIX="tr_parameter.FILESUFFIX";
        /**
         * 修改人
         */
        public static final String UPDATEPERSON="tr_parameter.UPDATEPERSON";
        /**
         * 修改时间
         */
        public static final String UPDATETIME="tr_parameter.UPDATETIME";
    }
}
