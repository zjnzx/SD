package com.energyfuture.symphony.m3.domain;


public class UserAuth extends UserAuthKey {
    /* ******** private property ******** */
	/**
	 * 授权人
	 */
    private String sqr;
	/**
	 * 授权时间
	 */
    private String sqsj;
	/**
	 * 
	 */
    private String spzt;
	/**
	 * 
	 */
    private String bpzyy;
	/**
	 * 
	 */
    private String ms;
	/**
	 * 
	 */
    private String sqlx;

    public String toString(){
        return "{"+"yhid:"+yhid+","+"jsid:"+jsid+","+"sqr:"+sqr+","+"sqsj:"+sqsj+","+"spzt:"+spzt+","+"bpzyy:"+bpzyy+","+"ms:"+ms+","+"sqlx:"+sqlx+"}";
    }
    
    public boolean equals(Object o){
        if (!(o instanceof UserAuth))
            return false;
     	 UserAuth t=(UserAuth)o;
        if (yhid==null||jsid==null)
            return this==t;

        return yhid.equals(t.yhid) && jsid.equals(t.jsid);
    }

    public int hashCode(){
        return yhid.hashCode()+jsid.hashCode();
    }
    
    /* ******** property's public getter setter ******** */
    public String getSqr() {
        return sqr;
    }
    public void setSqr(String sqr) {
        this.sqr = sqr;
    }
    
    public String getSqsj() {
        return sqsj;
    }
    public void setSqsj(String sqsj) {
        this.sqsj = sqsj;
    }
    
    public String getSpzt() {
        return spzt;
    }
    public void setSpzt(String spzt) {
        this.spzt = spzt;
    }
    
    public String getBpzyy() {
        return bpzyy;
    }
    public void setBpzyy(String bpzyy) {
        this.bpzyy = bpzyy;
    }
    
    public String getMs() {
        return ms;
    }
    public void setMs(String ms) {
        this.ms = ms;
    }
    
    public String getSqlx() {
        return sqlx;
    }
    public void setSqlx(String sqlx) {
        this.sqlx = sqlx;
    }
    
	
    /* ******** table column/field name  mnemonic symbol ******** */
    public static final String TABLENAME="user_auth";
    public static final class FIELDS{
		/**
	 	 *用户ID
	 	 */
        public static final String YHID="user_auth.YHID";
		/**
	 	 *角色ID
	 	 */
        public static final String JSID="user_auth.JSID";
		/**
	 	 *授权人
	 	 */
        public static final String SQR="user_auth.SQR";
		/**
	 	 *授权时间
	 	 */
        public static final String SQSJ="user_auth.SQSJ";
		/**
	 	 *
	 	 */
        public static final String SPZT="user_auth.SPZT";
		/**
	 	 *
	 	 */
        public static final String BPZYY="user_auth.BPZYY";
		/**
	 	 *
	 	 */
        public static final String MS="user_auth.MS";
		/**
	 	 *
	 	 */
        public static final String SQLX="user_auth.SQLX";
    }
}
