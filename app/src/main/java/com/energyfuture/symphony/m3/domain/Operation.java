package com.energyfuture.symphony.m3.domain;

/**
 * Created by Administrator on 2015/6/2.
 */
public class Operation {

    private String field;
    private String operate;
    private String value;

    public Operation(String field, String operate, String value) {
        this.field = field;
        this.operate = operate;
        this.value = value;
    }



    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getOperate() {
        return operate;
    }

    public void setOperate(String operate) {
        this.operate = operate;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Operation{" +
                "field='" + field + '\'' +
                ", operate='" + operate + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
