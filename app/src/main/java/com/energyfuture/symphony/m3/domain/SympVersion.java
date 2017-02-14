package com.energyfuture.symphony.m3.domain;

import java.io.Serializable;

public class SympVersion implements Serializable{
    /* ******** private property ******** */
	/**
	 * 版本ID
	 */
    private String id;
	/**
	 * 版本号
	 */
    private String versionCode;
	/**
	 * 更新时间
	 */
    private String updateDate;
	/**
	 * 更新内容
	 */
    private String updateContent;

    @Override
    public String toString() {
        return "SympVersion{" +
                "id='" + id + '\'' +
                ", versionCode='" + versionCode + '\'' +
                ", updateDate='" + updateDate + '\'' +
                ", updateContent='" + updateContent + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(String versionCode) {
        this.versionCode = versionCode;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public String getUpdateContent() {
        return updateContent;
    }

    public void setUpdateContent(String updateContent) {
        this.updateContent = updateContent;
    }
}
