package com.energyfuture.symphony.m3.domain;

import java.io.Serializable;

public class SympCommunicationReply implements Serializable{
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
	 * 回复ID
	 */
    private String replyid;
	/**
	 * 回复对象ID
	 */
    private String replyobjectid;
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
                ", replyid='" + replyid + '\'' +
                ", replyobjectid='" + replyobjectid + '\'' +
                ", personname='" + personname + '\'' +
                '}';
    }

    public boolean equals(Object o){
        if (!(o instanceof SympCommunicationReply))
            return false;
     	 SympCommunicationReply t=(SympCommunicationReply)o;
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
    
    public String getReplyid() {
        return replyid;
    }
    public void setReplyid(String replyid) {
        this.replyid = replyid;
    }
    
    public String getReplyobjectid() {
        return replyobjectid;
    }
    public void setReplyobjectid(String replyobjectid) {
        this.replyobjectid = replyobjectid;
    }

    public String getPersonname() {
        return personname;
    }

    public void setPersonname(String personname) {
        this.personname = personname;
    }

    /* ******** table column/field name  mnemonic symbol ******** */
    public static final String TABLENAME="SYMP_COMMUNICATION_REPLY";
    public static final class FIELDS{
		/**
		 * ID
		 */
        public static final String ID="SYMP_COMMUNICATION_REPLY.ID";
		/**
		 * 主贴ID
		 */
        public static final String THEMEID="SYMP_COMMUNICATION_REPLY.THEMEID";
		/**
		 * 回复人
		 */
        public static final String PEPLYPERSONID="SYMP_COMMUNICATION_REPLY.PEPLYPERSONID";
		/**
		 * 回复内容
		 */
        public static final String REPLYCONTENT="SYMP_COMMUNICATION_REPLY.REPLYCONTENT";
		/**
		 * 回复创建时间
		 */
        public static final String REPLYTIME="SYMP_COMMUNICATION_REPLY.REPLYTIME";
		/**
		 * 状态 0：未阅读；1：已读
		 */
        public static final String STATE="SYMP_COMMUNICATION_REPLY.STATE";
		/**
		 * 回复ID
		 */
        public static final String REPLYID="SYMP_COMMUNICATION_REPLY.REPLYID";
		/**
		 * 回复对象ID
		 */
        public static final String REPLYOBJECTID="SYMP_COMMUNICATION_REPLY.REPLYOBJECTID";
    }
}
