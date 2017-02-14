package com.energyfuture.symphony.m3.domain;

import java.io.Serializable;

public class TrFeedBackReply implements Serializable{
    /* ******** private property ******** */
	/**
	 * ID
	 */
    private String id;
	/**
	 * 主贴ID
	 */
    private String themeid;
	/**
	 * 回复人
	 */
    private String replypersonid;
	/**
	 * 回复内容
	 */
    private String replycontent;
	/**
	 * 回复创建时间
	 */
    private String replytime;
	/**
	 * 状态 0：未阅读；1：已读
	 */
    private String state;
    /**
     * 回复人名称
     */
    private String personname;

    @Override
    public String toString() {
        return "SympCommunicationReply{" +
                "id='" + id + '\'' +
                ", themeid='" + themeid + '\'' +
                ", replypersonid='" + replypersonid + '\'' +
                ", replycontent='" + replycontent + '\'' +
                ", replytime='" + replytime + '\'' +
                ", state='" + state + '\'' +
                ", personname='" + personname + '\'' +
                '}';
    }

    public boolean equals(Object o){
        if (!(o instanceof TrFeedBackReply))
            return false;
     	 TrFeedBackReply t=(TrFeedBackReply)o;
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
    
    public String getThemeid() {
        return themeid;
    }
    public void setThemeid(String themeid) {
        this.themeid = themeid;
    }

    public String getReplypersonid() {
        return replypersonid;
    }

    public void setReplypersonid(String replypersonid) {
        this.replypersonid = replypersonid;
    }

    public String getReplycontent() {
        return replycontent;
    }
    public void setReplycontent(String replycontent) {
        this.replycontent = replycontent;
    }

    public String getReplytime() {
        return replytime;
    }

    public void setReplytime(String replytime) {
        this.replytime = replytime;
    }

    public String getState() {
        return state;
    }
    public void setState(String state) {
        this.state = state;
    }
    

    public String getPersonname() {
        return personname;
    }

    public void setPersonname(String personname) {
        this.personname = personname;
    }

    /* ******** table column/field name  mnemonic symbol ******** */
    public static final String TABLENAME="TR_FEEDBACK_REPLY";
}
