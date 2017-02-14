package com.energyfuture.symphony.m3.domain;

import java.io.Serializable;

public class TrSafetytipsTemplet implements Serializable{
    /* ******** private property ******** */
	/**
	 * ID
	 */
    private String id;
	/**
	 * 名称
	 */
    private String endangername;
	/**
	 * 危害分布、特性及产生风险条件/注意事项/工作内容
	 */
    private String endangerrang;
	/**
	 * 风险范畴
	 */
    private String riiskrang;
	/**
	 * 可能导致的风险后果
	 */
    private String result;
	/**
	 * 基准等级
	 */
    private String grade;
	/**
	 * 基准风险评估预控措施
	 */
    private String precontrolmethod;
	/**
	 * 新增预控措施
	 */
    private String method;
	/**
	 * 持续等级
	 */
    private String continuelevel;
	/**
	 * 备注
	 */
    private String remarks;
	/**
	 * 安全须知类别
	 */
    private String safetype;
	/**
	 * 序号
	 */
    private String ordermunber;

    public String toString(){
        return "{"+"id:"+id+","+"endangername:"+endangername+","+"endangerrang:"+endangerrang+","+"riiskrang:"+riiskrang+","+"result:"+result+","+"grade:"+grade+","+"precontrolmethod:"+precontrolmethod+","+"method:"+method+","+"continuelevel:"+continuelevel+","+"remarks:"+remarks+","+"safetype:"+safetype+","+"ordermunber:"+ordermunber+"}";
    }
    
    public boolean equals(Object o){
        if (!(o instanceof TrSafetytipsTemplet))
            return false;
     	 TrSafetytipsTemplet t=(TrSafetytipsTemplet)o;
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

    public String getOrdermunber() {
        return ordermunber;
    }

    public void setOrdermunber(String ordermunber) {
        this.ordermunber = ordermunber;
    }

    /* ******** table column/field name  mnemonic symbol ******** */
    public static final String TABLENAME="tr_safetytips_templet";
    public static final class FIELDS{
		/**
		 * ID
		 */
        public static final String ID="tr_safetytips_templet.ID";
		/**
		 * 名称
		 */
        public static final String ENDANGERNAME="tr_safetytips_templet.ENDANGERNAME";
		/**
		 * 危害分布、特性及产生风险条件/注意事项/工作内容
		 */
        public static final String ENDANGERRANG="tr_safetytips_templet.ENDANGERRANG";
		/**
		 * 风险范畴
		 */
        public static final String RiISKRANG="tr_safetytips_templet.RiISKRANG";
		/**
		 * 可能导致的风险后果
		 */
        public static final String RESULT="tr_safetytips_templet.RESULT";
		/**
		 * 基准等级
		 */
        public static final String GRADE="tr_safetytips_templet.GRADE";
		/**
		 * 基准风险评估预控措施
		 */
        public static final String PRECONTROLMETHOD="tr_safetytips_templet.PRECONTROLMETHOD";
		/**
		 * 新增预控措施
		 */
        public static final String METHOD="tr_safetytips_templet.METHOD";
		/**
		 * 持续等级
		 */
        public static final String CONTINUELEVEL="tr_safetytips_templet.CONTINUELEVEL";
		/**
		 * 备注
		 */
        public static final String REMARKS="tr_safetytips_templet.REMARKS";
		/**
		 * 安全须知类别
		 */
        public static final String SAFETYPE="tr_safetytips_templet.SAFETYPE";
		/**
		 * 序号
		 */
        public static final String ORDERMUNBER="tr_safetytips_templet.ORDERMUNBER";
    }
}
