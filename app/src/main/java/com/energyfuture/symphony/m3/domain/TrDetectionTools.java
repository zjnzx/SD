package com.energyfuture.symphony.m3.domain;

import java.io.Serializable;

public class TrDetectionTools implements Serializable{
    /* ******** private property ******** */
	/**
	 * 设备ID
	 */
    private String id;
	/**
	 * 检测工具类型
	 */
    private String toolstype;
	/**
	 * 生产厂家
	 */
    private String company;
	/**
	 * 设备型号
	 */
    private String model;
	/**
	 * 设备名称
	 */
    private String equipmentname;
	/**
	 * 出厂日期
	 */
    private String productiondate;
	/**
	 * 出厂编号
	 */
    private String ccbh;
	/**
	 * 是否可用
	 */
    private String state;
	/**
	 * 有效日期
	 */
    private String validdate;
	/**
	 * 带电检测类型
	 */
    private String detectiontype;
	/**
	 * 单位（台、套等）
	 */
    private String unit;
	/**
	 * 校准机构
	 */
    private String correcting;
	/**
	 * 序号
	 */
    private String ordernumber;

    @Override
    public String toString() {
        return "TrDetectionTools{" +
                "id='" + id + '\'' +
                ", toolstype='" + toolstype + '\'' +
                ", company='" + company + '\'' +
                ", model='" + model + '\'' +
                ", equipmentname='" + equipmentname + '\'' +
                ", productiondate='" + productiondate + '\'' +
                ", ccbh='" + ccbh + '\'' +
                ", state='" + state + '\'' +
                ", validdate='" + validdate + '\'' +
                ", detectiontype='" + detectiontype + '\'' +
                ", unit='" + unit + '\'' +
                ", correcting='" + correcting + '\'' +
                ", ordernumber='" + ordernumber + '\'' +
                '}';
    }

    public boolean equals(Object o){
        if (!(o instanceof TrDetectionTools))
            return false;
     	 TrDetectionTools t=(TrDetectionTools)o;
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
    
    public String getToolstype() {
        return toolstype;
    }
    public void setToolstype(String toolstype) {
        this.toolstype = toolstype;
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
    
    public String getEquipmentname() {
        return equipmentname;
    }
    public void setEquipmentname(String equipmentname) {
        this.equipmentname = equipmentname;
    }
    
    public String getCcbh() {
        return ccbh;
    }
    public void setCcbh(String ccbh) {
        this.ccbh = ccbh;
    }
    
    public String getState() {
        return state;
    }
    public void setState(String state) {
        this.state = state;
    }

    public String getProductiondate() {
        return productiondate;
    }

    public void setProductiondate(String productiondate) {
        this.productiondate = productiondate;
    }

    public String getValiddate() {
        return validdate;
    }

    public void setValiddate(String validdate) {
        this.validdate = validdate;
    }

    public String getDetectiontype() {
        return detectiontype;
    }
    public void setDetectiontype(String detectiontype) {
        this.detectiontype = detectiontype;
    }
    
    public String getUnit() {
        return unit;
    }
    public void setUnit(String unit) {
        this.unit = unit;
    }
    
    public String getCorrecting() {
        return correcting;
    }
    public void setCorrecting(String correcting) {
        this.correcting = correcting;
    }

    public String getOrdernumber() {
        return ordernumber;
    }

    public void setOrdernumber(String ordernumber) {
        this.ordernumber = ordernumber;
    }

    /* ******** table column/field name  mnemonic symbol ******** */
    public static final String TABLENAME="tr_detection_tools";
    public static final class FIELDS{
		/**
		 * 设备ID
		 */
        public static final String ID="tr_detection_tools.ID";
		/**
		 * 检测工具类型
		 */
        public static final String TOOLSTYPE="tr_detection_tools.TOOLSTYPE";
		/**
		 * 生产厂家
		 */
        public static final String COMPANY="tr_detection_tools.COMPANY";
		/**
		 * 设备型号
		 */
        public static final String MODEL="tr_detection_tools.MODEL";
		/**
		 * 设备名称
		 */
        public static final String EQUIPMENTNAME="tr_detection_tools.EQUIPMENTNAME";
		/**
		 * 出厂日期
		 */
        public static final String PRODUCTIONDATE="tr_detection_tools.PRODUCTIONDATE";
		/**
		 * 出厂编号
		 */
        public static final String CCBH="tr_detection_tools.CCBH";
		/**
		 * 是否可用
		 */
        public static final String STATE="tr_detection_tools.STATE";
		/**
		 * 有效日期
		 */
        public static final String VALIDDATE="tr_detection_tools.VALIDDATE";
		/**
		 * 带电检测类型
		 */
        public static final String DETECTIONTYPE="tr_detection_tools.DETECTIONTYPE";
		/**
		 * 单位（台、套等）
		 */
        public static final String UNIT="tr_detection_tools.UNIT";
		/**
		 * 校准机构
		 */
        public static final String CORRECTING="tr_detection_tools.CORRECTING";
    }
}
