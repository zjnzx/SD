package com.energyfuture.symphony.m3.domain;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/8/19.
 */
public class SympFlowInfo implements Serializable {
    /**
     * 流量表ID
     */
    private String flowinfoid;
    /**
     * 用户ID
     */
    private String userid;
    /**
     * 流量类型：文本，图片
     */
    private String flowtype;
    /**
     * 使用类型：上传，下载
     */
    private String usetype;
    /**
     * 连接类型：wifi，移动，联通
     */
    private String connecttype;
    /**
     * 网络类型：2G，3G，4G
     */
    private String webtype;
    /**
     * 当前时间
     */
    private String recordtime;
    /**
     * 使用量
     */
    private String usequantity;
    /**
     * 当前板子ID
     */
    private String equipmentid;
    /**
     * 业务类型
     */
    private String businesstype;
    /**
     * 备注
     */
    private String remark;

    @Override
    public String toString() {
        return "SympFlowInfo{" +
                "flowinfoid='" + flowinfoid + '\'' +
                ", userid='" + userid + '\'' +
                ", flowtype='" + flowtype + '\'' +
                ", usetype='" + usetype + '\'' +
                ", connecttype='" + connecttype + '\'' +
                ", webtype='" + webtype + '\'' +
                ", recordtime='" + recordtime + '\'' +
                ", usequantity='" + usequantity + '\'' +
                ", equipmentid='" + equipmentid + '\'' +
                ", businesstype='" + businesstype + '\'' +
                ", remark='" + remark + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof SympFlowInfo))
            return false;
        SympFlowInfo t=(SympFlowInfo)o;
        if (flowinfoid==null)
            return this==t;
        return  flowinfoid.equals(t.flowinfoid);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    public String getFlowinfoid() {
        return flowinfoid;
    }

    public void setFlowinfoid(String flowinfoid) {
        this.flowinfoid = flowinfoid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getFlowtype() {
        return flowtype;
    }

    public void setFlowtype(String flowtype) {
        this.flowtype = flowtype;
    }

    public String getUsetype() {
        return usetype;
    }

    public void setUsetype(String usetype) {
        this.usetype = usetype;
    }

    public String getConnecttype() {
        return connecttype;
    }

    public void setConnecttype(String connecttype) {
        this.connecttype = connecttype;
    }

    public String getWebtype() {
        return webtype;
    }

    public void setWebtype(String webtype) {
        this.webtype = webtype;
    }

    public String getRecordtime() {
        return recordtime;
    }

    public void setRecordtime(String recordtime) {
        this.recordtime = recordtime;
    }

    public String getUsequantity() {
        return usequantity;
    }

    public void setUsequantity(String usequantity) {
        this.usequantity = usequantity;
    }

    public String getEquipmentid() {
        return equipmentid;
    }

    public void setEquipmentid(String equipmentid) {
        this.equipmentid = equipmentid;
    }

    public String getBusinesstype() {
        return businesstype;
    }

    public void setBusinesstype(String businesstype) {
        this.businesstype = businesstype;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
