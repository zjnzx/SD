package com.energyfuture.symphony.m3.domain;

import java.io.Serializable;

public class TrEquipment implements Serializable{
    /* ******** private property ******** */
	/**
	 * 设备编号
	 */
    private String equipmentid;
	/**
	 * 供电局名称
	 */
    private String bureauname;
	/**
	 * 供电局编号
	 */
    private String bureauid;
	/**
	 * 变电站名称
	 */
    private String stationname;
	/**
	 * 变电站编号
	 */
    private String stationid;
	/**
	 * 电压等级
	 */
    private String voltage;
	/**
	 * 设备类型
	 */
    private String equipmenttype;
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
	 * 投运日期
	 */
    private String usedate;
	/**
	 * 检测序号
	 */
    private String xh;
	/**
	 * 图片路径
	 */
    private String equipmentimg;
	/**
	 * 出厂编号
	 */
    private String ccbh;
	/**
	 * 运行编号
	 */
    private String ybbh;
	/**
	 * 检测顺序
	 */
    private String orderby;
	/**
	 * 
	 */
    private String ratedcapacity;
	/**
	 * 
	 */
    private String updatetime;
	/**
	 * 
	 */
    private String voltagesetmodel;
	/**
	 * 相位（A相、B相、C相、ABC相）
	 */
    private String phase;
    private String phasecount;
    private String frequency;
    private String connectnumber;
    private String usecondition;
    private String oil;
    private String coolmethod;

    @Override
    public String toString() {
        return "TrEquipment{" +
                "equipmentid='" + equipmentid + '\'' +
                ", bureauname='" + bureauname + '\'' +
                ", bureauid='" + bureauid + '\'' +
                ", stationname='" + stationname + '\'' +
                ", stationid='" + stationid + '\'' +
                ", voltage='" + voltage + '\'' +
                ", equipmenttype='" + equipmenttype + '\'' +
                ", company='" + company + '\'' +
                ", model='" + model + '\'' +
                ", equipmentname='" + equipmentname + '\'' +
                ", productiondate='" + productiondate + '\'' +
                ", usedate='" + usedate + '\'' +
                ", xh='" + xh + '\'' +
                ", equipmentimg='" + equipmentimg + '\'' +
                ", ccbh='" + ccbh + '\'' +
                ", ybbh='" + ybbh + '\'' +
                ", orderby='" + orderby + '\'' +
                ", ratedcapacity='" + ratedcapacity + '\'' +
                ", updatetime='" + updatetime + '\'' +
                ", voltagesetmodel='" + voltagesetmodel + '\'' +
                ", phase='" + phase + '\'' +
                ", phasecount='" + phasecount + '\'' +
                ", frequency='" + frequency + '\'' +
                ", connectnumber='" + connectnumber + '\'' +
                ", usecondition='" + usecondition + '\'' +
                ", oil='" + oil + '\'' +
                ", coolmethod='" + coolmethod + '\'' +
                '}';
    }

    public boolean equals(Object o){
        if (!(o instanceof TrEquipment))
            return false;
     	 TrEquipment t=(TrEquipment)o;
        if (equipmentid==null)
            return this==t;
        return  equipmentid.equals(t.equipmentid);
    }
    
     public int hashCode(){
        return equipmentid.hashCode();
    }

    
    /* ******** property's public getter setter ******** */
    public String getEquipmentid() {
        return equipmentid;
    }
    public void setEquipmentid(String equipmentid) {
        this.equipmentid = equipmentid;
    }
    
    public String getBureauname() {
        return bureauname;
    }
    public void setBureauname(String bureauname) {
        this.bureauname = bureauname;
    }
    
    public String getBureauid() {
        return bureauid;
    }
    public void setBureauid(String bureauid) {
        this.bureauid = bureauid;
    }
    
    public String getStationname() {
        return stationname;
    }
    public void setStationname(String stationname) {
        this.stationname = stationname;
    }
    
    public String getStationid() {
        return stationid;
    }
    public void setStationid(String stationid) {
        this.stationid = stationid;
    }
    
    public String getVoltage() {
        return voltage;
    }
    public void setVoltage(String voltage) {
        this.voltage = voltage;
    }
    
    public String getEquipmenttype() {
        return equipmenttype;
    }
    public void setEquipmenttype(String equipmenttype) {
        this.equipmenttype = equipmenttype;
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

    public String getProductiondate() {
        return productiondate;
    }

    public void setProductiondate(String productiondate) {
        this.productiondate = productiondate;
    }

    public String getUsedate() {
        return usedate;
    }

    public void setUsedate(String usedate) {
        this.usedate = usedate;
    }

    public String getXh() {
        return xh;
    }

    public void setXh(String xh) {
        this.xh = xh;
    }

    public String getEquipmentimg() {
        return equipmentimg;
    }
    public void setEquipmentimg(String equipmentimg) {
        this.equipmentimg = equipmentimg;
    }
    
    public String getCcbh() {
        return ccbh;
    }
    public void setCcbh(String ccbh) {
        this.ccbh = ccbh;
    }
    
    public String getYbbh() {
        return ybbh;
    }
    public void setYbbh(String ybbh) {
        this.ybbh = ybbh;
    }
    
    public String getRatedcapacity() {
        return ratedcapacity;
    }
    public void setRatedcapacity(String ratedcapacity) {
        this.ratedcapacity = ratedcapacity;
    }

    public String getOrderby() {
        return orderby;
    }

    public void setOrderby(String orderby) {
        this.orderby = orderby;
    }

    public String getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
    }

    public String getVoltagesetmodel() {
        return voltagesetmodel;
    }
    public void setVoltagesetmodel(String voltagesetmodel) {
        this.voltagesetmodel = voltagesetmodel;
    }
    
    public String getPhase() {
        return phase;
    }
    public void setPhase(String phase) {
        this.phase = phase;
    }

    public String getCoolmethod() {
        return coolmethod;
    }

    public void setCoolmethod(String coolmethod) {
        this.coolmethod = coolmethod;
    }

    public String getPhasecount() {
        return phasecount;
    }

    public void setPhasecount(String phasecount) {
        this.phasecount = phasecount;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getConnectnumber() {
        return connectnumber;
    }

    public void setConnectnumber(String connectnumber) {
        this.connectnumber = connectnumber;
    }

    public String getUsecondition() {
        return usecondition;
    }

    public void setUsecondition(String usecondition) {
        this.usecondition = usecondition;
    }

    public String getOil() {
        return oil;
    }

    public void setOil(String oil) {
        this.oil = oil;
    }

    /* ******** table column/field name  mnemonic symbol ******** */
    public static final String TABLENAME="tr_equipment";
    public static final class FIELDS{
		/**
		 * 设备编号
		 */
        public static final String EQUIPMENTID="tr_equipment.EQUIPMENTID";
		/**
		 * 供电局名称
		 */
        public static final String BUREAUNAME="tr_equipment.BUREAUNAME";
		/**
		 * 供电局编号
		 */
        public static final String BUREAUID="tr_equipment.BUREAUID";
		/**
		 * 变电站名称
		 */
        public static final String STATIONNAME="tr_equipment.STATIONNAME";
		/**
		 * 变电站编号
		 */
        public static final String STATIONID="tr_equipment.STATIONID";
		/**
		 * 电压等级
		 */
        public static final String VOLTAGE="tr_equipment.VOLTAGE";
		/**
		 * 设备类型
		 */
        public static final String EQUIPMENTTYPE="tr_equipment.EQUIPMENTTYPE";
		/**
		 * 生产厂家
		 */
        public static final String COMPANY="tr_equipment.COMPANY";
		/**
		 * 设备型号
		 */
        public static final String MODEL="tr_equipment.MODEL";
		/**
		 * 设备名称
		 */
        public static final String EQUIPMENTNAME="tr_equipment.EQUIPMENTNAME";
		/**
		 * 出厂日期
		 */
        public static final String PRODUCTIONDATE="tr_equipment.PRODUCTIONDATE";
		/**
		 * 投运日期
		 */
        public static final String USEDATE="tr_equipment.USEDATE";
		/**
		 * 检测序号
		 */
        public static final String XH="tr_equipment.XH";
		/**
		 * 图片路径
		 */
        public static final String EQUIPMENTIMG="tr_equipment.EQUIPMENTIMG";
		/**
		 * 出厂编号
		 */
        public static final String CCBH="tr_equipment.CCBH";
		/**
		 * 运行编号
		 */
        public static final String YBBH="tr_equipment.YBBH";
		/**
		 * 检测顺序
		 */
        public static final String ORDERBY="tr_equipment.ORDERBY";
		/**
		 * 
		 */
        public static final String RATEDCAPACITY="tr_equipment.RATEDCAPACITY";
		/**
		 * 
		 */
        public static final String UPDATETIME="tr_equipment.UPDATETIME";
		/**
		 * 
		 */
        public static final String VOLTAGESETMODEL="tr_equipment.VOLTAGESETMODEL";
		/**
		 * 相位（A相、B相、C相、ABC相）
		 */
        public static final String PHASE="tr_equipment.PHASE";
    }
}
