package com.energyfuture.symphony.m3.util;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.net.ParseException;
import java.io.StringWriter;
import java.io.IOException;

import org.codehaus.jackson.map.ObjectMapper;



/**
 * 
 * 1:将JavaBean转换成Map、JSONObject 2:将Map转换成Javabean 3:将JSONObject转换成Map、Javabean
 * 
 */
public class JsonHelper {
	/**
     * 将Javabean转换为Map
     *
     * @param javaBean
     *            javaBean
     * @return Map对象
     */
	public static Map toMap(Object javaBean) {

		Map result = new HashMap();
		Method[] methods = javaBean.getClass().getDeclaredMethods();

		for (Method method : methods) {

			try {

				if (method.getName().startsWith("get")) {

					String field = method.getName();
					field = field.substring(field.indexOf("get") + 3);
					field = field.toLowerCase().charAt(0) + field.substring(1);

					Object value = method.invoke(javaBean, (Object[]) null);
					result.put(field, null == value ? "" : value.toString());

				}

			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		return result;

	}

    /**
     * 将Json对象转换成Map
     * @param jsonString
     * @return
     * @throws JSONException
     */
	public static Map toMap(String jsonString) throws JSONException {

		JSONObject jsonObject = new JSONObject(jsonString);

		Map result = new HashMap();
		Iterator iterator = jsonObject.keys();
		String key = null;
		String value = null;

		while (iterator.hasNext()) {

			key = (String) iterator.next();
			value = jsonObject.getString(key);
			result.put(key, value);

		}
		return result;

	}
	
	/**
	 * 讲json对象转成List<Map>对象
	 * @designer
	 * @since  2014-11-24
	 * @author zhaogr
	 * @param @param jsonString
	 * @param @return
	 * @param @throws JSONException 
	 * @return List<Map>
	 */
	public static List<Map> toListMap(String jsonString){
		List<Map> listResult =  new ArrayList<Map>();
		try {
			
			 JSONArray jsonArray = new JSONArray(jsonString); 
			
			
//			jsonString = "{\"data\":"+jsonString+"}";
//			
//			JSONObject jsonObject = new JSONObject(jsonString);
			
			
			 for (int i = 0; i < jsonArray.length(); i++) { 
		        	JSONObject jsonObject = jsonArray.getJSONObject(i); 
		        	Iterator iterator = jsonObject.keys();
		        	Map result = new HashMap();
		        	while (iterator.hasNext()) {
						String key = (String) iterator.next();
						String value = jsonObject.getString(key);
						result.put(key, value);
						
					}
		        	if(result != null && result.size() > 0){
		        		listResult.add(result);
		        	}
		        	
		        }  
			
			
			
			
//			
//			Iterator iterator = jsonObject.keys();
//			String key = null;
//			String value = null;
//			
//			
//			
//			while (iterator.hasNext()) {
//				Map result = new HashMap();
//				key = (String) iterator.next();
//				value = jsonObject.getString(key);
//				
//				
//				
//				result.put(key, value);
//				listResult.add(result);
//			}
		} catch (JSONException e) {
			//如果json字符串出现空的情况，则说明json在转的过程中出现空
			// TODO Auto-generated catch block
//			e.printStackTrace();
		}
		return listResult;
	}

	/**
	 * 将JavaBean转换成JSONObject（通过Map中转）
	 * 
	 * @param bean
	 *            javaBean
	 * @return json对象
	 */
	public static JSONObject toJSON(Object bean) {

		return new JSONObject(toMap(bean));

	}

	/**
	 * 将Map转换成Javabean
	 * 
	 * @param javabean
	 *            javaBean
	 * @param data
	 *            Map数据
	 */
	public static Object toJavaBean(Object javabean, Map data) {

		Method[] methods = javabean.getClass().getDeclaredMethods();
		for (Method method : methods) {

			try {
				if (method.getName().startsWith("set")) {

					String field = method.getName();
					field = field.substring(field.indexOf("set") + 3);
					field = field.toLowerCase().charAt(0) + field.substring(1);
					method.invoke(javabean, new Object[] {

					data.get(field)

					});

				}
			} catch (Exception e) {
			}

		}

		return javabean;

	}

    /**
     * JSONObject到JavaBean
     * @param javabean
     * @param jsonString
     * @throws ParseException
     * @throws JSONException
     */
	public static void toJavaBean(Object javabean, String jsonString)
			throws ParseException, JSONException {

		JSONObject jsonObject = new JSONObject(jsonString);

		Map map = toMap(jsonObject.toString());

		toJavaBean(javabean, map);

	}

    /**
     * 将java对象转换为json字符串
     * @param obj
     * @return
     */
    public static String  Object2Json(Object obj) {

        ObjectMapper objectMapper = new ObjectMapper();
        StringWriter str=new StringWriter();
        try {
            objectMapper.writeValue(str, obj);
            return str.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
