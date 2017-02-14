package com.energyfuture.symphony.m3.domain;

import java.io.Serializable;

public class TrStation implements Serializable{
    /* ******** private property ******** */
	/**
	 * 变电站编号
	 */
    private String stationid;
	/**
	 * 供电局编号
	 */
    private String bureauid;
	/**
	 * 电压等级
	 */
    private String voltage;
	/**
	 * 变电站名称
	 */
    private String stationname;
	/**
	 * 变电站编码
	 */
    private String subcode;
	/**
	 * 是否显示(0，不显示，1，显示)
	 */
    private String visible;
	/**
	 * 变电站坐标X轴
	 */
    private String positionx;
	/**
	 * 变电站坐标Y轴
	 */
    private String positiony;

    public String toString(){
        return "{"+"stationid:"+stationid+","+"bureauid:"+bureauid+","+"voltage:"+voltage+","+"stationname:"+stationname+","+"subcode:"+subcode+","+"visible:"+visible+","+"positionx:"+positionx+","+"positiony:"+positiony+"}";
    }
    
    public boolean equals(Object o){
        if (!(o instanceof TrStation))
            return false;
     	 TrStation t=(TrStation)o;
        if (stationid==null)
            return this==t;
        return  stationid.equals(t.stationid);
    }
    
     public int hashCode(){
        return stationid.hashCode();
    }

    
    /* ******** property's public getter setter ******** */
    public String getStationid() {
        return stationid;
    }
    public void setStationid(String stationid) {
        this.stationid = stationid;
    }
    
    public String getBureauid() {
        return bureauid;
    }
    public void setBureauid(String bureauid) {
        this.bureauid = bureauid;
    }
    
    public String getVoltage() {
        return voltage;
    }
    public void setVoltage(String voltage) {
        this.voltage = voltage;
    }
    
    public String getStationname() {
        return stationname;
    }
    public void setStationname(String stationname) {
        this.stationname = stationname;
    }
    
    public String getSubcode() {
        return subcode;
    }
    public void setSubcode(String subcode) {
        this.subcode = subcode;
    }
    
    public String getVisible() {
        return visible;
    }
    public void setVisible(String visible) {
        this.visible = visible;
    }

    public String getPositionx() {
        return positionx;
    }

    public void setPositionx(String positionx) {
        this.positionx = positionx;
    }

    public String getPositiony() {
        return positiony;
    }

    public void setPositiony(String positiony) {
        this.positiony = positiony;
    }

    /* ******** table column/field name  mnemonic symbol ******** */
    public static final String TABLENAME="tr_station";
    public static final class FIELDS{
		/**
		 * 变电站编号
		 */
        public static final String STATIONID="tr_station.STATIONID";
		/**
		 * 供电局编号
		 */
        public static final String BUREAUID="tr_station.BUREAUID";
		/**
		 * 电压等级
		 */
        public static final String VOLTAGE="tr_station.VOLTAGE";
		/**
		 * 变电站名称
		 */
        public static final String STATIONNAME="tr_station.STATIONNAME";
		/**
		 * 变电站编码
		 */
        public static final String SUBCODE="tr_station.SUBCODE";
		/**
		 * 是否显示(0，不显示，1，显示)
		 */
        public static final String VISIBLE="tr_station.VISIBLE";
		/**
		 * 变电站坐标X轴
		 */
        public static final String POSITIONX="tr_station.POSITIONX";
		/**
		 * 变电站坐标Y轴
		 */
        public static final String POSITIONY="tr_station.POSITIONY";
    }
}
