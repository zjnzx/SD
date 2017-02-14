package com.energyfuture.symphony.m3.domain;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/10/9.
 */
public class TrNoiseRecordValue implements Serializable {
    /**
     * ID
     */
    private String id;
    /**
     *
     */
    private String groupid;
    /**
     *
     */
    private String sight;
    /**
     *
     */
    private String height13;
    /**
     *
     */
    private String height23;

    public String toString(){
        return "{"+"id:"+id+","+"groupid:"+groupid+","+"sight:"+sight+","+"height13:"+height13+","+"height23:"+height23+"}";
    }

    public boolean equals(Object o){
        if (!(o instanceof TrNoiseRecordValue))
            return false;
        TrNoiseRecordValue t=(TrNoiseRecordValue)o;
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

    public String getGroupid() {
        return groupid;
    }

    public void setGroupid(String groupid) {
        this.groupid = groupid;
    }

    public String getSight() {
        return sight;
    }

    public void setSight(String sight) {
        this.sight = sight;
    }

    public String getHeight13() {
        return height13;
    }

    public void setHeight13(String height13) {
        this.height13 = height13;
    }

    public String getHeight23() {
        return height23;
    }

    public void setHeight23(String height23) {
        this.height23 = height23;
    }

    public static final String TABLENAME="TR_NOISE_RECORD_VALUE";
}
