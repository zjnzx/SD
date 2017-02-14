package com.energyfuture.symphony.m3.domain;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/10/9.
 */
public class TrNoiseRecord implements Serializable {
    /**
     * ID
     */
    private String id;
    /**
     *
     */
    private String detectionobjid;
    /**
     *
     */
    private String dcfan;
    /**
     *
     */
    private String oilpump;
    /**
     *
     */
    private String noiseavg;

    public String toString(){
        return "{"+"id:"+id+","+"detectionobjid:"+detectionobjid+","+"dcfan:"+dcfan+","+"oilpump:"+oilpump+","+"noiseavg:"+noiseavg+"}";
    }

    public boolean equals(Object o){
        if (!(o instanceof TrNoiseRecord))
            return false;
        TrNoiseRecord t=(TrNoiseRecord)o;
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

    public String getDetectionobjid() {
        return detectionobjid;
    }

    public void setDetectionobjid(String detectionobjid) {
        this.detectionobjid = detectionobjid;
    }

    public String getDcfan() {
        return dcfan;
    }

    public void setDcfan(String dcfan) {
        this.dcfan = dcfan;
    }

    public String getOilpump() {
        return oilpump;
    }

    public void setOilpump(String oilpump) {
        this.oilpump = oilpump;
    }

    public String getNoiseavg() {
        return noiseavg;
    }

    public void setNoiseavg(String noiseavg) {
        this.noiseavg = noiseavg;
    }

    public static final String TABLENAME="TR_NOISE_RECORD";
}
