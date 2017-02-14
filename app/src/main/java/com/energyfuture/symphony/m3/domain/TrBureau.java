package com.energyfuture.symphony.m3.domain;

import java.io.Serializable;

public class TrBureau implements Serializable{
    /* ******** private property ******** */
	/**
	 * 供电局编号
	 */
    private String bureauid;
	/**
	 * 供电局名称
	 */
    private String bureauname;
	/**
	 * 显示顺序
	 */
    private String listid;
	/**
	 * 图片路径
	 */
    private String bureauimg;
	/**
	 * 坐标X轴
	 */
    private String positionx;
	/**
	 * 坐标Y轴
	 */
    private String positiony;
	/**
	 * 所属省公司
	 */
    private String provinceid;

    public String toString(){
        return "{"+"bureauid:"+bureauid+","+"bureauname:"+bureauname+","+"listid:"+listid+","+"bureauimg:"+bureauimg+","+"positionx:"+positionx+","+"positiony:"+positiony+","+"provinceid:"+provinceid+"}";
    }
    
    public boolean equals(Object o){
        if (!(o instanceof TrBureau))
            return false;
     	 TrBureau t=(TrBureau)o;
        if (bureauid==null)
            return this==t;
        return  bureauid.equals(t.bureauid);
    }
    
     public int hashCode(){
        return bureauid.hashCode();
    }

    
    /* ******** property's public getter setter ******** */
    public String getBureauid() {
        return bureauid;
    }
    public void setBureauid(String bureauid) {
        this.bureauid = bureauid;
    }
    
    public String getBureauname() {
        return bureauname;
    }
    public void setBureauname(String bureauname) {
        this.bureauname = bureauname;
    }
    
    public String getListid() {
        return listid;
    }
    public void setListid(String listid) {
        this.listid = listid;
    }
    
    public String getBureauimg() {
        return bureauimg;
    }
    public void setBureauimg(String bureauimg) {
        this.bureauimg = bureauimg;
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

    public String getProvinceid() {
        return provinceid;
    }
    public void setProvinceid(String provinceid) {
        this.provinceid = provinceid;
    }
    
	
    /* ******** table column/field name  mnemonic symbol ******** */
    public static final String TABLENAME="tr_bureau";
    public static final class FIELDS{
		/**
		 * 供电局编号
		 */
        public static final String BUREAUID="tr_bureau.BUREAUID";
		/**
		 * 供电局名称
		 */
        public static final String BUREAUNAME="tr_bureau.BUREAUNAME";
		/**
		 * 显示顺序
		 */
        public static final String LISTID="tr_bureau.LISTID";
		/**
		 * 图片路径
		 */
        public static final String BUREAUIMG="tr_bureau.BUREAUIMG";
		/**
		 * 坐标X轴
		 */
        public static final String POSITIONX="tr_bureau.POSITIONX";
		/**
		 * 坐标Y轴
		 */
        public static final String POSITIONY="tr_bureau.POSITIONY";
		/**
		 * 所属省公司
		 */
        public static final String PROVINCEID="tr_bureau.PROVINCEID";
    }
}
