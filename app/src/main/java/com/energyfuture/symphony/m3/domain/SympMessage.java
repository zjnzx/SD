package com.energyfuture.symphony.m3.domain;

import java.io.Serializable;
import java.util.Date;

public class SympMessage implements Serializable{
    /* ******** private property ******** */
	/**
	 * 日志ID
	 */
    private String messageid;
	/**
	 * 用户ID
	 */
    private String presonid;
	/**
	 * 消息内容
	 */
    private String content;
	/**
	 * 消息状态
	 */
    private String messagestate;
	/**
	 * 消息类型
	 */
    private String messagetype;
	/**
	 * 备注
	 */
    private String fcomment;
	/**
	 * 消息创建时间
	 */
    private Date createdate;
	/**
	 * 发送时间
	 */
    private Date senddate;

    public String toString(){
        return "{"+"messageid:"+messageid+","+"presonid:"+presonid+","+"content:"+content+","+"messagestate:"+messagestate+","+"messagetype:"+messagetype+","+"fcomment:"+fcomment+","+"createdate:"+createdate+","+"senddate:"+senddate+"}";
    }
    
    public boolean equals(Object o){
        if (!(o instanceof SympMessage))
            return false;
     	 SympMessage t=(SympMessage)o;
        if (messageid==null)
            return this==t;
        return  messageid.equals(t.messageid);
    }
    
     public int hashCode(){
        return messageid.hashCode();
    }

    
    /* ******** property's public getter setter ******** */
    public String getMessageid() {
        return messageid;
    }
    public void setMessageid(String messageid) {
        this.messageid = messageid;
    }
    
    public String getPresonid() {
        return presonid;
    }
    public void setPresonid(String presonid) {
        this.presonid = presonid;
    }
    
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    
    public String getMessagestate() {
        return messagestate;
    }
    public void setMessagestate(String messagestate) {
        this.messagestate = messagestate;
    }
    
    public String getMessagetype() {
        return messagetype;
    }
    public void setMessagetype(String messagetype) {
        this.messagetype = messagetype;
    }
    
    public String getFcomment() {
        return fcomment;
    }
    public void setFcomment(String fcomment) {
        this.fcomment = fcomment;
    }

    public Date getCreatedate() {
        return createdate;
    }
    public void setCreatedate(Date createdate) {
        this.createdate = createdate;
    }

    public Date getSenddate() {
        return senddate;
    }
    public void setSenddate(Date senddate) {
        this.senddate = senddate;
    }


    /* ******** table column/field name  mnemonic symbol ******** */
    public static final String TABLENAME="symp_message";
    public static final class FIELDS{
		/**
		 * 日志ID
		 */
        public static final String MESSAGEID="symp_message.MESSAGEID";
		/**
		 * 用户ID
		 */
        public static final String PRESONID="symp_message.PRESONID";
		/**
		 * 消息内容
		 */
        public static final String CONTENT="symp_message.CONTENT";
		/**
		 * 消息状态
		 */
        public static final String MESSAGESTATE="symp_message.MESSAGESTATE";
		/**
		 * 消息类型
		 */
        public static final String MESSAGETYPE="symp_message.MESSAGETYPE";
		/**
		 * 备注
		 */
        public static final String FCOMMENT="symp_message.FCOMMENT";
		/**
		 * 消息创建时间
		 */
        public static final String CREATEDATE="symp_message.CREATEDATE";
		/**
		 * 发送时间
		 */
        public static final String SENDDATE="symp_message.SENDDATE";
    }
}
