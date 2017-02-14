package com.energyfuture.symphony.m3.common;

import org.apache.http.HttpException;

/**
 * Created by 超 on 2015/6/5.
 */
public class MyHttpException extends HttpException {
    private String msg;
    MyHttpException(String msg) {
        this.msg = msg;
    }

    public String toString() {
        return "MyHttpException [" + msg + "]";
    }
}
