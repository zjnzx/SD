package com.energyfuture.symphony.m3.domain;

import java.io.Serializable;

public class SympCommunicationPrompt implements Serializable{
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
	 * 回复ID
	 */
    private String replyid;
	/**
	 * 提醒内容
	 */
    private String promptcontent;
	/**
	 * 提醒创建时间
	 */
    private String createtime;
	/**
	 * 提醒类型 0：参与提醒 1：回复提醒  3：@提醒
	 */
    private String prompttype;
	/**
	 * 状态 0：未阅读；1：已读
	 */
    private String state;
	/**
	 * 提醒人id
	 */
    private String promptpersonid;

    @Override
    public String toString() {
        return "SympCommunicationPrompt{" +
                "id='" + id + '\'' +
                ", themeid='" + themeid + '\'' +
                ", replyid='" + replyid + '\'' +
                ", promptcontent='" + promptcontent + '\'' +
                ", createtime='" + createtime + '\'' +
                ", prompttype='" + prompttype + '\'' +
                ", state='" + state + '\'' +
                ", promptpersonid='" + promptpersonid + '\'' +
                '}';
    }

    public boolean equals(Object o){
        if (!(o instanceof SympCommunicationPrompt))
            return false;
     	 SympCommunicationPrompt t=(SympCommunicationPrompt)o;
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
    
    public String getReplyid() {
        return replyid;
    }
    public void setReplyid(String replyid) {
        this.replyid = replyid;
    }
    
    public String getPromptcontent() {
        return promptcontent;
    }
    public void setPromptcontent(String promptcontent) {
        this.promptcontent = promptcontent;
    }
    

    public String getPrompttype() {
        return prompttype;
    }
    public void setPrompttype(String prompttype) {
        this.prompttype = prompttype;
    }
    
    public String getState() {
        return state;
    }
    public void setState(String state) {
        this.state = state;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getPromptpersonid() {
        return promptpersonid;
    }

    public void setPromptpersonid(String promptpersonid) {
        this.promptpersonid = promptpersonid;
    }

    /* ******** table column/field name  mnemonic symbol ******** */
    public static final String TABLENAME="SYMP_COMMUNICATION_PROMPT";
    public static final class FIELDS{
		/**
		 * ID
		 */
        public static final String ID="SYMP_COMMUNICATION_PROMPT.ID";
		/**
		 * 主贴ID
		 */
        public static final String THEMEID="SYMP_COMMUNICATION_PROMPT.THEMEID";
		/**
		 * 回复ID
		 */
        public static final String REPLYID="SYMP_COMMUNICATION_PROMPT.REPLYID";
		/**
		 * 提醒内容
		 */
        public static final String PROMPTCONTENT="SYMP_COMMUNICATION_PROMPT.PROMPTCONTENT";
		/**
		 * 提醒创建时间
		 */
        public static final String CRATETIME="SYMP_COMMUNICATION_PROMPT.CRATETIME";
		/**
		 * 提醒类型 0：参与提醒 1：回复提醒  3：@提醒
		 */
        public static final String PROMPTTYPE="SYMP_COMMUNICATION_PROMPT.PROMPTTYPE";
		/**
		 * 状态 0：未阅读；1：已读
		 */
        public static final String STATE="SYMP_COMMUNICATION_PROMPT.STATE";
    }
}
