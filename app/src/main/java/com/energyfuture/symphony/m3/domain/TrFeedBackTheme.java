package com.energyfuture.symphony.m3.domain;

import java.io.Serializable;

public class TrFeedBackTheme implements Serializable{
    /* ******** private property ******** */
	/**
	 * 主贴ID
	 */
    private String id;
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
     * 主题创建人姓名
     */
    private String createpersonname;
    /**
     * 主题创建人头像
     */
    private String head;
    /**
     * 回复数量
     */
    private String replyCount;

    @Override
    public String toString() {
        return "TrFeedBackTheme{" +
                "id='" + id + '\'' +
                ", themetitle='" + themetitle + '\'' +
                ", themecontent='" + themecontent + '\'' +
                ", createpersonid='" + createpersonid + '\'' +
                ", createtime='" + createtime + '\'' +
                ", themestate='" + themestate + '\'' +
                ", createpersonname='" + createpersonname + '\'' +
                ", head=" + head +
                ", replyCount='" + replyCount + '\'' +
                '}';
    }

    public boolean equals(Object o){
        if (!(o instanceof TrFeedBackTheme))
            return false;
     	 TrFeedBackTheme t=(TrFeedBackTheme)o;
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
    
    public String getCreatepersonname() {
        return createpersonname;
    }

    public void setCreatepersonname(String createpersonname) {
        this.createpersonname = createpersonname;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String getReplyCount() {
        return replyCount;
    }

    public void setReplyCount(String replyCount) {
        this.replyCount = replyCount;
    }

    /* ******** table column/field name  mnemonic symbol ******** */
    public static final String TABLENAME="TR_FEEDBACK_THEME";
}
