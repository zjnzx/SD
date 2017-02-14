package com.energyfuture.symphony.m3.domain;

import java.io.Serializable;

public class RoleAuthKey implements Serializable{
    /* ******** private property ******** */
	/**
	 *角色ID
	 */
    protected String jsid;
	/**
	 *权限ID
	 */
    protected String qxid;

    public String toString(){
        return "{"+"jsid:"+jsid+","+"qxid:"+qxid+"}";
    }
    
    public boolean equals(Object o){
        if (!(o instanceof RoleAuthKey ))
            return false;
     	RoleAuthKey t=(RoleAuthKey)o;
        if (jsid==null||qxid==null)
            return this==t;

        return jsid.equals(t.jsid)&&qxid.equals(t.qxid);
    }
    
    public int hashCode(){
        return jsid.hashCode()+qxid.hashCode();
    }

    /* ******** property's public getter setter ******** */
    public String getJsid() {
        return jsid;
    }
    public void setJsid(String jsid) {
        this.jsid = jsid;
    }
    
    public String getQxid() {
        return qxid;
    }
    public void setQxid(String qxid) {
        this.qxid = qxid;
    }
    
}
