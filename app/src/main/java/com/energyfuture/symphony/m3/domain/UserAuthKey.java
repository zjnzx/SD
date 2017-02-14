package com.energyfuture.symphony.m3.domain;

import java.io.Serializable;

public class UserAuthKey implements Serializable{
    /* ******** private property ******** */
	/**
	 *用户ID
	 */
    protected String yhid;
	/**
	 *角色ID
	 */
    protected String jsid;

    public String toString(){
        return "{"+"yhid:"+yhid+","+"jsid:"+jsid+"}";
    }
    
    public boolean equals(Object o){
        if (!(o instanceof UserAuthKey ))
            return false;
     	UserAuthKey t=(UserAuthKey)o;
        if (yhid==null||jsid==null)
            return this==t;

        return yhid.equals(t.yhid)&&jsid.equals(t.jsid);
    }
    
    public int hashCode(){
        return yhid.hashCode()+jsid.hashCode();
    }

    /* ******** property's public getter setter ******** */
    public String getYhid() {
        return yhid;
    }
    public void setYhid(String yhid) {
        this.yhid = yhid;
    }
    
    public String getJsid() {
        return jsid;
    }
    public void setJsid(String jsid) {
        this.jsid = jsid;
    }
    
}
