package com.energyfuture.symphony.m3.domain;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/10/9.
 */
public class TrNoiseRecordGroup implements Serializable {
    /**
     * ID
     */
    private String id;
    /**
     *
     */
    private String noiseid;
    /**
     *
     */
    private String groupname;
    /**
     *
     */
    private String orderby;

    @Override
    public String toString() {
        return "TrNoiseRecordGroup{" +
                "id='" + id + '\'' +
                ", noiseid='" + noiseid + '\'' +
                ", groupname='" + groupname + '\'' +
                ", orderby='" + orderby + '\'' +
                '}';
    }

    public boolean equals(Object o){
        if (!(o instanceof TrNoiseRecordGroup))
            return false;
        TrNoiseRecordGroup t=(TrNoiseRecordGroup)o;
        if (id==null)
            return this==t;
        return  id.equals(t.id);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNoiseid() {
        return noiseid;
    }

    public void setNoiseid(String noiseid) {
        this.noiseid = noiseid;
    }

    public String getGroupname() {
        return groupname;
    }

    public void setGroupname(String groupname) {
        this.groupname = groupname;
    }

    public String getOrderby() {
        return orderby;
    }

    public void setOrderby(String orderby) {
        this.orderby = orderby;
    }

    public static final String TABLENAME="TR_NOISE_RECORD_GROUP";
}
