package com.energyfuture.symphony.m3.domain;

import java.io.Serializable;

public class SympMessageReal implements Serializable{
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
	 * 消息状态(1.未发送、2.已发送、3.发送成功)
	 */
    private String messagestate;
	/**
	 * 消息类型 (1.文字、2.图片、3.文件)
	 */
    private String messagetype;
	/**
	 * 备注
	 */
    private String fcomment;
	/**
	 * 消息创建时间
	 */
    private String createdate;
	/**
	 * 发送时间
	 */
    private String senddate;

    public String toString(){
        return "{"+"messageid:"+messageid+","+"presonid:"+presonid+","+"content:"+content+","+"messagestate:"+messagestate+","+"messagetype:"+messagetype+","+"fcomment:"+fcomment+","+"createdate:"+createdate+","+"senddate:"+senddate+"}";
    }
    
    public boolean equals(Object o){
        if (!(o instanceof SympMessageReal))
            return false;
     	 SympMessageReal t=(SympMessageReal)o;
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

    public String getCreatedate() {
        return createdate;
    }

    public void setCreatedate(String createdate) {
        this.createdate = createdate;
    }

    public String getSenddate() {
        return senddate;
    }

    public void setSenddate(String senddate) {
        this.senddate = senddate;
    }

    /* ******** table column/field name  mnemonic symbol ******** */
    public static final String TABLENAME="symp_message_real";
    public static final class FIELDS{
		/**
		 * 日志ID
		 */
        public static final String MESSAGEID="symp_message_real.MESSAGEID";
		/**
		 * 用户ID
		 */
        public static final String PRESONID="symp_message_real.PRESONID";
		/**
		 * 消息内容
		 */
        public static final String CONTENT="symp_message_real.CONTENT";
		/**
		 * 消息状态(1.未发送、2.已发送、3.发送成功)
		 */
        public static final String MESSAGESTATE="symp_message_real.MESSAGESTATE";
		/**
		 * 消息类型 (1.文字、2.图片、3.文件)
		 */
        public static final String MESSAGETYPE="symp_message_real.MESSAGETYPE";
		/**
		 * 备注
		 */
        public static final String FCOMMENT="symp_message_real.FCOMMENT";
		/**
		 * 消息创建时间
		 */
        public static final String CREATEDATE="symp_message_real.CREATEDATE";
		/**
		 * 发送时间
		 */
        public static final String SENDDATE="symp_message_real.SENDDATE";
    }
}
