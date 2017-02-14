package com.energyfuture.symphony.m3.domain;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/9/9.
 */
public class TrEquipmentTools implements Serializable {
    /**
     * ID
     */
    private String id;
    /**
     * 任务ID
     */
    private String taskid;
    /**
     * 工具ID
     */
    private String toolid;
    /**
     * 数量
     */
    private String amount;
    /**
     * 确认人
     */
    private String persion;
    /**
     * 确认时间
     */
    private String updatetime;

    public String toString(){
        return "{"+"id:"+id+","+"taskid:"+taskid+","+"toolid:"+toolid+","+"amount:"+amount+","+"persion:"+persion+","+"updatetime:"+updatetime+"}";
    }

    public static final String TABLENAME="tr_equipment_tools";

    public boolean equals(Object o){
        if (!(o instanceof TrEquipmentTools))
            return false;
        TrEquipmentTools t=(TrEquipmentTools)o;
        if (id==null)
            return this==t;
        return  id.equals(t.id);
    }

    public int hashCode(){
        return id.hashCode();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
    }

    public String getPersion() {
        return persion;
    }

    public void setPersion(String persion) {
        this.persion = persion;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getToolid() {
        return toolid;
    }

    public void setToolid(String toolid) {
        this.toolid = toolid;
    }

    public String getTaskid() {
        return taskid;
    }

    public void setTaskid(String taskid) {
        this.taskid = taskid;
    }
}
