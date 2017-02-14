package com.energyfuture.symphony.m3.domain;

import java.io.Serializable;

public class TrSpecialBushingPosition implements Serializable{
    /* ******** private property ******** */
	/**
	 * ID
	 */
    private String id;
	/**
	 * 检测项目ID
	 */
    private String bushingid;
	/**
	 * 电压等级
	 */
    private String voltage;
	/**
	 * 相位
	 */
    private String phash;
    private String orderby;

    @Override
    public String toString() {
        return "TrSpecialBushingPosition{" +
                "id='" + id + '\'' +
                ", bushingid='" + bushingid + '\'' +
                ", voltage='" + voltage + '\'' +
                ", phash='" + phash + '\'' +
                ", orderby='" + orderby + '\'' +
                '}';
    }

    public boolean equals(Object o){
        if (!(o instanceof TrSpecialBushingPosition))
            return false;
     	 TrSpecialBushingPosition t=(TrSpecialBushingPosition)o;
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
    
    public String getBushingid() {
        return bushingid;
    }
    public void setBushingid(String bushingid) {
        this.bushingid = bushingid;
    }
    
    public String getVoltage() {
        return voltage;
    }
    public void setVoltage(String voltage) {
        this.voltage = voltage;
    }
    
    public String getPhash() {
        return phash;
    }
    public void setPhash(String phash) {
        this.phash = phash;
    }

    public String getOrderby() {
        return orderby;
    }

    public void setOrderby(String orderby) {
        this.orderby = orderby;
    }

    /* ******** table column/field name  mnemonic symbol ******** */
    public static final String TABLENAME="tr_special_bushing_position";
    public static final class FIELDS{
		/**
		 * ID
		 */
        public static final String ID="tr_special_bushing_position.ID";
		/**
		 * 检测项目ID
		 */
        public static final String BUSHINGID="tr_special_bushing_position.BUSHINGID";
		/**
		 * 电压等级
		 */
        public static final String VOLTAGE="tr_special_bushing_position.VOLTAGE";
		/**
		 * 相位
		 */
        public static final String PHASH="tr_special_bushing_position.PHASH";
    }
}
