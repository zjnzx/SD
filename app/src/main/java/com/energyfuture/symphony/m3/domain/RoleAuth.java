package com.energyfuture.symphony.m3.domain;


public class RoleAuth extends RoleAuthKey {
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
    private String sqlx;

    public String toString(){
        return "{"+"jsid:"+jsid+","+"qxid:"+qxid+","+"sqr:"+sqr+","+"sqsj:"+sqsj+","+"spzt:"+spzt+","+"sqlx:"+sqlx+"}";
    }
    
    public boolean equals(Object o){
        if (!(o instanceof RoleAuth))
            return false;
     	 RoleAuth t=(RoleAuth)o;
        if (jsid==null||qxid==null)
            return this==t;

        return jsid.equals(t.jsid) && qxid.equals(t.qxid);
    }

    public int hashCode(){
        return jsid.hashCode()+qxid.hashCode();
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
    
    public String getSqlx() {
        return sqlx;
    }
    public void setSqlx(String sqlx) {
        this.sqlx = sqlx;
    }
    
	
    /* ******** table column/field name  mnemonic symbol ******** */
    public static final String TABLENAME="role_auth";
    public static final class FIELDS{
		/**
	 	 *角色ID
	 	 */
        public static final String JSID="role_auth.JSID";
		/**
	 	 *权限ID
	 	 */
        public static final String QXID="role_auth.QXID";
		/**
	 	 *授权人
	 	 */
        public static final String SQR="role_auth.SQR";
		/**
	 	 *授权时间
	 	 */
        public static final String SQSJ="role_auth.SQSJ";
		/**
	 	 *
	 	 */
        public static final String SPZT="role_auth.SPZT";
		/**
	 	 *
	 	 */
        public static final String SQLX="role_auth.SQLX";
    }
}
