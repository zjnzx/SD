package com.energyfuture.symphony.m3.domain;

import java.io.Serializable;
import java.util.Date;

public class UserInfo implements Serializable{
    /* ******** private property ******** */
	/**
	 * 用户ID
	 */
    private String yhid;
	/**
	 * 部门编号
	 */
    private String bmbh;
	/**
	 * 姓名
	 */
    private String xm;
	/**
	 * 用户密码
	 */
    private String yhmm;
	/**
	 * 
	 */
    private String sfzh;
	/**
	 * 
	 */
    private String yhzw;
	/**
	 * 
	 */
    private String email;
	/**
	 * 
	 */
    private String sjhm;
	/**
	 * 
	 */
    private String yhzt;
	/**
	 * 
	 */
    private Integer mj;
	/**
	 * 
	 */
    private String cjr;
	/**
	 * 
	 */
    private String cjsj;
	/**
	 * 
	 */
    private String xgr;
	/**
	 * 
	 */
    private String xgsj;
	/**
	 * 
	 */
    private String ssxt;
	/**
	 * 用户更新时间
	 */
    private Date updatetime;

    @Override
    public String toString() {
        return "UserInfo{" +
                "yhid='" + yhid + '\'' +
                ", bmbh='" + bmbh + '\'' +
                ", xm='" + xm + '\'' +
                ", yhmm='" + yhmm + '\'' +
                ", sfzh='" + sfzh + '\'' +
                ", yhzw='" + yhzw + '\'' +
                ", email='" + email + '\'' +
                ", sjhm='" + sjhm + '\'' +
                ", yhzt='" + yhzt + '\'' +
                ", mj=" + mj +
                ", cjr='" + cjr + '\'' +
                ", cjsj='" + cjsj + '\'' +
                ", xgr='" + xgr + '\'' +
                ", xgsj='" + xgsj + '\'' +
                ", ssxt='" + ssxt + '\'' +
                ", updatetime=" + updatetime +
                '}';
    }

    public boolean equals(Object o){
        if (!(o instanceof UserInfo))
            return false;
     	 UserInfo t=(UserInfo)o;
        if (yhid==null)
            return this==t;
        return  yhid.equals(t.yhid);
    }
    
     public int hashCode(){
        return yhid.hashCode();
    }

    
    /* ******** property's public getter setter ******** */
    public String getYhid() {
        return yhid;
    }
    public void setYhid(String yhid) {
        this.yhid = yhid;
    }
    
    public String getBmbh() {
        return bmbh;
    }
    public void setBmbh(String bmbh) {
        this.bmbh = bmbh;
    }
    
    public String getXm() {
        return xm;
    }
    public void setXm(String xm) {
        this.xm = xm;
    }
    
    public String getYhmm() {
        return yhmm;
    }
    public void setYhmm(String yhmm) {
        this.yhmm = yhmm;
    }
    
    public String getSfzh() {
        return sfzh;
    }
    public void setSfzh(String sfzh) {
        this.sfzh = sfzh;
    }
    
    public String getYhzw() {
        return yhzw;
    }
    public void setYhzw(String yhzw) {
        this.yhzw = yhzw;
    }
    
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getSjhm() {
        return sjhm;
    }
    public void setSjhm(String sjhm) {
        this.sjhm = sjhm;
    }
    
    public String getYhzt() {
        return yhzt;
    }
    public void setYhzt(String yhzt) {
        this.yhzt = yhzt;
    }
    
    public Integer getMj() {
        return mj;
    }
    public void setMj(Integer mj) {
        this.mj = mj;
    }
    
    public String getCjr() {
        return cjr;
    }
    public void setCjr(String cjr) {
        this.cjr = cjr;
    }
    
    public String getCjsj() {
        return cjsj;
    }
    public void setCjsj(String cjsj) {
        this.cjsj = cjsj;
    }
    
    public String getXgr() {
        return xgr;
    }
    public void setXgr(String xgr) {
        this.xgr = xgr;
    }
    
    public String getXgsj() {
        return xgsj;
    }
    public void setXgsj(String xgsj) {
        this.xgsj = xgsj;
    }
    
    public String getSsxt() {
        return ssxt;
    }
    public void setSsxt(String ssxt) {
        this.ssxt = ssxt;
    }
    
    public Date getUpdatetime() {
        return updatetime;
    }
    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }

    /* ******** table column/field name  mnemonic symbol ******** */
    public static final String TABLENAME="user_info";
    public static final class FIELDS{
		/**
		 * 用户ID
		 */
        public static final String YHID="user_info.YHID";
		/**
		 * 部门编号
		 */
        public static final String BMBH="user_info.BMBH";
		/**
		 * 姓名
		 */
        public static final String XM="user_info.XM";
		/**
		 * 用户密码
		 */
        public static final String YHMM="user_info.YHMM";
		/**
		 * 
		 */
        public static final String SFZH="user_info.SFZH";
		/**
		 * 
		 */
        public static final String YHZW="user_info.YHZW";
		/**
		 * 
		 */
        public static final String EMAIL="user_info.EMAIL";
		/**
		 * 
		 */
        public static final String SJHM="user_info.SJHM";
		/**
		 * 
		 */
        public static final String YHZT="user_info.YHZT";
		/**
		 * 
		 */
        public static final String MJ="user_info.MJ";
		/**
		 * 
		 */
        public static final String CJR="user_info.CJR";
		/**
		 * 
		 */
        public static final String CJSJ="user_info.CJSJ";
		/**
		 * 
		 */
        public static final String XGR="user_info.XGR";
		/**
		 * 
		 */
        public static final String XGSJ="user_info.XGSJ";
		/**
		 * 
		 */
        public static final String SSXT="user_info.SSXT";
		/**
		 * 用户更新时间
		 */
        public static final String UPDATETIME="user_info.UPDATETIME";
    }
}
