package com.energyfuture.symphony.m3.domain;

import java.io.Serializable;

public class SympCommunicationTheme implements Serializable{
    /* ******** private property ******** */
	/**
	 * 主贴ID
	 */
    private String id;
    /**
     * 所属ID
     */
    private String belongId;
	/**
	 * 讨论发起位置
	 */
    private String position;
	/**
	 * 讨论主题
	 */
    private String themetitle;
	/**
	 * 讨论内容
	 */
    private String themecontent;
	/**
	 * 主题创建人
	 */
    private String createpersonid;
	/**
	 * 主贴创建时间
	 */
    private String createtime;
	/**
	 * 主贴状态 0：未开始；1：进行中；2：暂停：3：取消；4：已完成
	 */
    private String themestate;
	/**
	 * 参与人员
	 */
    private String participator;
    /**
     * 主题创建人姓名
     */
    private String createpersonname;
    /**
     * 主题创建人头像
     */
    private int head;
    /**
     * 回复数量
     */
    private String replyCount;
    /**
     * 发起位置
     */
    private String launchPosition;

    @Override
    public String toString() {
        return "SympCommunicationTheme{" +
                "id='" + id + '\'' +
                ", belongId='" + belongId + '\'' +
                ", position='" + position + '\'' +
                ", themetitle='" + themetitle + '\'' +
                ", themecontent='" + themecontent + '\'' +
                ", createpersonid='" + createpersonid + '\'' +
                ", createtime='" + createtime + '\'' +
                ", themestate='" + themestate + '\'' +
                ", participator='" + participator + '\'' +
                ", createpersonname='" + createpersonname + '\'' +
                ", head=" + head +
                ", replyCount='" + replyCount + '\'' +
                ", launchPosition='" + launchPosition + '\'' +
                '}';
    }

    public boolean equals(Object o){
        if (!(o instanceof SympCommunicationTheme))
            return false;
     	 SympCommunicationTheme t=(SympCommunicationTheme)o;
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
    
    public String getPosition() {
        return position;
    }
    public void setPosition(String position) {
        this.position = position;
    }
    
    public String getThemetitle() {
        return themetitle;
    }
    public void setThemetitle(String themetitle) {
        this.themetitle = themetitle;
    }
    
    public String getThemecontent() {
        return themecontent;
    }
    public void setThemecontent(String themecontent) {
        this.themecontent = themecontent;
    }
    
    public String getCreatepersonid() {
        return createpersonid;
    }
    public void setCreatepersonid(String createpersonid) {
        this.createpersonid = createpersonid;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getThemestate() {
        return themestate;
    }
    public void setThemestate(String themestate) {
        this.themestate = themestate;
    }
    
    public String getParticipator() {
        return participator;
    }
    public void setParticipator(String participator) {
        this.participator = participator;
    }

    public String getBelongId() {
        return belongId;
    }

    public void setBelongId(String belongId) {
        this.belongId = belongId;
    }

    public String getCreatepersonname() {
        return createpersonname;
    }

    public void setCreatepersonname(String createpersonname) {
        this.createpersonname = createpersonname;
    }

    public int getHead() {
        return head;
    }

    public void setHead(int head) {
        this.head = head;
    }

    public String getReplyCount() {
        return replyCount;
    }

    public void setReplyCount(String replyCount) {
        this.replyCount = replyCount;
    }

    public String getLaunchPosition() {
        return launchPosition;
    }

    public void setLaunchPosition(String launchPosition) {
        this.launchPosition = launchPosition;
    }

    /* ******** table column/field name  mnemonic symbol ******** */
    public static final String TABLENAME="SYMP_COMMUNICATION_THEME";
    public static final class FIELDS{
		/**
		 * 主贴ID
		 */
        public static final String ID="SYMP_COMMUNICATION_THEME.ID";
		/**
		 * 讨论发起位置
		 */
        public static final String POSITION="SYMP_COMMUNICATION_THEME.POSITION";
		/**
		 * 讨论主题
		 */
        public static final String THEMETITLE="SYMP_COMMUNICATION_THEME.THEMETITLE";
		/**
		 * 讨论内容
		 */
        public static final String THEMECONTENT="SYMP_COMMUNICATION_THEME.THEMECONTENT";
		/**
		 * 主题创建人
		 */
        public static final String CREATEPERSONID="SYMP_COMMUNICATION_THEME.CREATEPERSONID";
		/**
		 * 主贴创建时间
		 */
        public static final String CREATETIME="SYMP_COMMUNICATION_THEME.CREATETIME";
		/**
		 * 主贴状态 0：未开始；1：进行中；2：暂停：3：取消；4：已完成
		 */
        public static final String THEMESTATE="SYMP_COMMUNICATION_THEME.THEMESTATE";
		/**
		 * 参与人员
		 */
        public static final String PARTICIPATOR="SYMP_COMMUNICATION_THEME.PARTICIPATOR";
    }
}
