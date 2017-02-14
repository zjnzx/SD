package com.energyfuture.symphony.m3.common;

import java.io.IOException;
import java.io.InputStream;

import android.net.ParseException;



/**
 * 数据操作结果实体
 * @version 1.0
 */
public class Result extends Base {

	private int code;
	private String message;
	
	public boolean OK() {
		return code == 1;
	}

	/**
	 * 解析调用结果
	 * 
	 * @param stream
	 * @return
	 * @throws java.io.IOException
	 * @throws org.xmlpull.v1.XmlPullParserException
	 */
	public static Result parse(InputStream stream) throws IOException {
		Result res = new Result();
        if (stream != null) {
		    try {
                String json = ApiClient.convertStreamToString(stream);
                String[] data = json.split(",");
                res.setCode(0);
                if (data.length == 1) {
                    if (!data[0].equals("") && data[0].contains("code")) {
                        String code = data[0].split(":")[1];
                        String code2 = (!code.equals("") ? code.substring(0, 1) : "0");
                        res.setCode(Integer.valueOf(code2));
                    }
                } else if (data.length == 2) {
                    if (!data[1].equals("") && data[1].contains("code")) {
                        String code = data[1].split(":")[1];
                        String code2 = (!code.equals("") ? code.substring(0, 1) : "0");
                        res.setCode(Integer.valueOf(code2));
                    }
                }
            }catch(ParseException e){
                e.printStackTrace();
            }
        }
		return res;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
}
