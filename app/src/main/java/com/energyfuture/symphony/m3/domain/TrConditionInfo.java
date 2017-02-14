package com.energyfuture.symphony.m3.domain;

import java.io.Serializable;

public class TrConditionInfo implements Serializable{
    /* ******** private property ******** */
	/**
	 * 记录ID
	 */
    private String recordid;
	/**
	 * 任务ID
	 */
    private String taskid;
	/**
	 * 记录人ID
	 */
    private String personid;
	/**
	 * 记录时间
	 */
    private String recorddate;
	/**
	 * 环境温度
	 */
    private String temperature;
	/**
	 * 相对湿度
	 */
    private String humidity;
	/**
	 * 天气
	 */
    private String weather;
	/**
	 * 风速
	 */
    private String anemograph;
	/**
	 * 辐射系数
	 */
    private String radiation;
	/**
	 * 变电站ID
	 */
    private String stationid;

    public String toString(){
        return "{"+"recordid:"+recordid+","+"taskid:"+taskid+","+"personid:"+personid+","+"recorddate:"+recorddate+","+"temperature:"+temperature+","+"humidity:"+humidity+","+"weather:"+weather+","+"anemograph:"+anemograph+","+"radiation:"+radiation+","+"stationid:"+stationid+"}";
    }
    
    public boolean equals(Object o){
        if (!(o instanceof TrConditionInfo))
            return false;
     	 TrConditionInfo t=(TrConditionInfo)o;
        if (recordid==null)
            return this==t;
        return  recordid.equals(t.recordid);
    }
    
     public int hashCode(){
        return recordid.hashCode();
    }

    
    /* ******** property's public getter setter ******** */
    public String getRecordid() {
        return recordid;
    }
    public void setRecordid(String recordid) {
        this.recordid = recordid;
    }

    public String getTaskid() {
        return taskid;
    }

    public void setTaskid(String taskid) {
        this.taskid = taskid;
    }

    public String getPersonid() {
        return personid;
    }
    public void setPersonid(String personid) {
        this.personid = personid;
    }

    public String getRecorddate() {
        return recorddate;
    }

    public void setRecorddate(String recorddate) {
        this.recorddate = recorddate;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public String getAnemograph() {
        return anemograph;
    }

    public void setAnemograph(String anemograph) {
        this.anemograph = anemograph;
    }

    public String getRadiation() {
        return radiation;
    }

    public void setRadiation(String radiation) {
        this.radiation = radiation;
    }

    public String getStationid() {
        return stationid;
    }
    public void setStationid(String stationid) {
        this.stationid = stationid;
    }
    
	
    /* ******** table column/field name  mnemonic symbol ******** */
    public static final String TABLENAME="tr_condition_info";
    public static final class FIELDS{
		/**
		 * 记录ID
		 */
        public static final String RECORDID="tr_condition_info.RECORDID";
		/**
		 * 项目ID
		 */
        public static final String PROJECTID="tr_condition_info.PROJECTID";
		/**
		 * 记录人ID
		 */
        public static final String PERSONID="tr_condition_info.PERSONID";
		/**
		 * 记录时间
		 */
        public static final String RECORDDATE="tr_condition_info.RECORDDATE";
		/**
		 * 环境温度
		 */
        public static final String TEMPERATURE="tr_condition_info.TEMPERATURE";
		/**
		 * 相对湿度
		 */
        public static final String HUMIDITY="tr_condition_info.HUMIDITY";
		/**
		 * 天气
		 */
        public static final String WEATHER="tr_condition_info.WEATHER";
		/**
		 * 风速
		 */
        public static final String ANEMOGRAPH="tr_condition_info.ANEMOGRAPH";
		/**
		 * 辐射系数
		 */
        public static final String RADIATION="tr_condition_info.RADIATION";
		/**
		 * 变电站ID
		 */
        public static final String STATIONID="tr_condition_info.STATIONID";
    }
}
