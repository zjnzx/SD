package com.energyfuture.symphony.m3.domain;

import java.io.Serializable;

public class TrSafetytipsObj implements Serializable{
    /* ******** private property ******** */
	/**
	 * 代码ID
	 */
    private String id;
	/**
	 * 代码名称
	 */
    private String endangername;
	/**
	 * 代码值
	 */
    private String endangerrang;
	/**
	 * 代码类型
	 */
    private String riiskrang;
	/**
	 * 展示顺序
	 */
    private String result;
	/**
	 * 
	 */
    private String grade;
	/**
	 * 
	 */
    private String precontrolmethod;
	/**
	 * 
	 */
    private String method;
	/**
	 * 
	 */
    private String continuelevel;
	/**
	 * 
	 */
    private String remarks;
	/**
	 * 
	 */
    private String safetype;
	/**
	 * 
	 */
    private String taskid;
	/**
	 * 
	 */
    private String ordermunber;
	/**
	 * 
	 */
    private String persion;
	/**
	 * 
	 */
    private String updatetime;

    public String toString(){
        return "{"+"id:"+id+","+"endangername:"+endangername+","+"endangerrang:"+endangerrang+","+"riiskrang:"+riiskrang+","+"result:"+result+","+"grade:"+grade+","+"precontrolmethod:"+precontrolmethod+","+"method:"+method+","+"continuelevel:"+continuelevel+","+"remarks:"+remarks+","+"safetype:"+safetype+","+"taskid:"+taskid+","+"ordermunber:"+ordermunber+","+"persion:"+persion+","+"updatetime:"+updatetime+"}";
    }
    
    public boolean equals(Object o){
        if (!(o instanceof TrSafetytipsObj))
            return false;
     	 TrSafetytipsObj t=(TrSafetytipsObj)o;
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
    
    public String getEndangername() {
        return endangername;
    }
    public void setEndangername(String endangername) {
        this.endangername = endangername;
    }
    
    public String getEndangerrang() {
        return endangerrang;
    }
    public void setEndangerrang(String endangerrang) {
        this.endangerrang = endangerrang;
    }
    
    public String getRiiskrang() {
        return riiskrang;
    }
    public void setRiiskrang(String riiskrang) {
        this.riiskrang = riiskrang;
    }
    
    public String getResult() {
        return result;
    }
    public void setResult(String result) {
        this.result = result;
    }
    
    public String getGrade() {
        return grade;
    }
    public void setGrade(String grade) {
        this.grade = grade;
    }
    
    public String getPrecontrolmethod() {
        return precontrolmethod;
    }
    public void setPrecontrolmethod(String precontrolmethod) {
        this.precontrolmethod = precontrolmethod;
    }
    
    public String getMethod() {
        return method;
    }
    public void setMethod(String method) {
        this.method = method;
    }
    
    public String getContinuelevel() {
        return continuelevel;
    }
    public void setContinuelevel(String continuelevel) {
        this.continuelevel = continuelevel;
    }
    
    public String getRemarks() {
        return remarks;
    }
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
    
    public String getSafetype() {
        return safetype;
    }
    public void setSafetype(String safetype) {
        this.safetype = safetype;
    }
    
    public String getTaskid() {
        return taskid;
    }
    public void setTaskid(String taskid) {
        this.taskid = taskid;
    }

    public String getOrdermunber() {
        return ordermunber;
    }

    public void setOrdermunber(String ordermunber) {
        this.ordermunber = ordermunber;
    }

    public String getPersion() {
        return persion;
    }
    public void setPersion(String persion) {
        this.persion = persion;
    }

    public String getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
    }

    /* ******** table column/field name  mnemonic symbol ******** */
    public static final String TABLENAME="tr_safetytips_obj";
    public static final class FIELDS{
		/**
		 * 代码ID
		 */
        public static final String ID="tr_safetytips_obj.ID";
		/**
		 * 代码名称
		 */
        public static final String ENDANGERNAME="tr_safetytips_obj.ENDANGERNAME";
		/**
		 * 代码值
		 */
        public static final String ENDANGERRANG="tr_safetytips_obj.ENDANGERRANG";
		/**
		 * 代码类型
		 */
        public static final String RiISKRANG="tr_safetytips_obj.RiISKRANG";
		/**
		 * 展示顺序
		 */
        public static final String RESULT="tr_safetytips_obj.RESULT";
		/**
		 * 
		 */
        public static final String GRADE="tr_safetytips_obj.GRADE";
		/**
		 * 
		 */
        public static final String PRECONTROLMETHOD="tr_safetytips_obj.PRECONTROLMETHOD";
		/**
		 * 
		 */
        public static final String METHOD="tr_safetytips_obj.METHOD";
		/**
		 * 
		 */
        public static final String CONTINUELEVEL="tr_safetytips_obj.CONTINUELEVEL";
		/**
		 * 
		 */
        public static final String REMARKS="tr_safetytips_obj.REMARKS";
		/**
		 * 
		 */
        public static final String SAFETYPE="tr_safetytips_obj.SAFETYPE";
		/**
		 * 
		 */
        public static final String TASKID="tr_safetytips_obj.TASKID";
		/**
		 * 
		 */
        public static final String ORDERMUNBER="tr_safetytips_obj.ORDERMUNBER";
		/**
		 * 
		 */
        public static final String PERSION="tr_safetytips_obj.PERSION";
		/**
		 * 
		 */
        public static final String UPDATETIME="tr_safetytips_obj.UPDATETIME";
    }
}
