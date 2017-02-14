package com.energyfuture.symphony.m3.common;

import android.net.ParseException;

import com.energyfuture.symphony.m3.util.Constants;

import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;


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
	 * 
	 *
	 *            json对象
	 * @return Map对象
	 * @throws org.json.JSONException
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
	 * 
	 *
	 *            javaBean
	 * @return json对象
	 * @throws android.net.ParseException
	 *             json解析异常
	 * @throws org.json.JSONException
	 */
	public static void toJavaBean(Object javabean, String jsonString)
			throws ParseException, JSONException {

		JSONObject jsonObject = new JSONObject(jsonString);

		Map map = toMap(jsonObject.toString());

		toJavaBean(javabean, map);

	}
	
	/** 
     * 将一个json字串转为list,如参必须是List<PmTask>的json对象
     *
     * @return List<PmTask>
	 * @throws org.json.JSONException
	 * @throws android.net.ParseException
     */  
    public static List<Map<Object,Object>> converMessageFormString(String answer) throws ParseException, JSONException{
        if (answer == null || answer.equals(""))  
            return new ArrayList();  
  
        JSONArray jsonArray = new JSONArray(); 
		try {
			jsonArray = new JSONArray(answer);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        List<Map<Object,Object>> messageList = new ArrayList<Map<Object,Object>>();
        for (int i = 0; i < jsonArray.length(); i++) { 
        	JSONObject item = jsonArray.getJSONObject(i); 
        	//int id = item.getInt("id");
            //String name = item.getString("name");

            Map<Object,Object> bean = new HashMap<Object,Object>();
            bean=toMap(item.toString());

            messageList.add(bean);
        }   
          
        return messageList;
    }

//    /**
//     * 将一个json字串转为list,入参必须是List<PmTaskRelationDevice>的json对象
//     * @param props
//     * @return List<PmTaskRelationDevice>
//	 * @throws org.json.JSONException
//	 * @throws android.net.ParseException
//     */
//    public static Map<String,List> converPmTaskRelaListFormString(String answer){
//        if (answer == null || answer.equals(""))
//            return new HashMap();
//
//        JSONArray jsonArray = new JSONArray();
//		try {
//			jsonArray = new JSONArray(answer);
//		} catch (JSONException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		Map<String,List> result = new HashMap<String,List>();
//		List<PmTaskRelationDevice> listRelation = new ArrayList<PmTaskRelationDevice>();
//		List<PmDevice> listDevice = new ArrayList<PmDevice>();
//        for (int i = 0; i < jsonArray.length(); i++) {
//        	JSONObject item = null;
//			try {
//				item = jsonArray.getJSONObject(i);
//			} catch (JSONException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//        	PmTaskRelationDevice bean = new PmTaskRelationDevice();
//			try {
//				toJavaBean(bean, item.toString());
//			} catch (ParseException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (JSONException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			listRelation.add(bean);
//
//			String conTactDevice = "";
//			try {
//				conTactDevice = item.getString("conTactDevice");
//			} catch (JSONException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			PmDevice beanDevice = new PmDevice();
//			try {
//				toJavaBean(beanDevice, conTactDevice);
//			} catch (ParseException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (JSONException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			listDevice.add(beanDevice);
//        }
//
//        result.put("pmTaskRelationDevice", listRelation);
//        result.put("pmDevice", listDevice);
//
//
//        return result;
//    }
//
    /**
     * 将map数据解析出来，并拼接成json字符串
     * 
     * @param map
     * @return
     */
    public static JSONObject setJosn(Map map) throws Exception {
            JSONObject json = null;
            StringBuffer temp = new StringBuffer();
            if (!map.isEmpty()) {
                    temp.append("{");
                    // 遍历map
                    Set set = map.entrySet();
                    Iterator i = set.iterator();
                    while (i.hasNext()) {
                            Map.Entry entry = (Map.Entry) i.next();
                            String key = (String) entry.getKey();
                            Object value = entry.getValue();
                            temp.append("\"" + key + "\":");
                            if (value instanceof Map<?, ?>) {
                                    temp.append(setJosn((Map<String, Object>) value) + ",");
                            } else if (value instanceof List<?>) {
                                    temp.append(setList((List<Map<String, Object>>) value)
                                                    + ",");
                            } else {
                                    temp.append(value + ",");
                            }
                    }
                    if (temp.length() > 1) {
                            temp = new StringBuffer(temp.substring(0, temp.length() - 1));
                    }
                    temp.append("}");
                    json = new JSONObject(temp.toString());
            }
            return json;
    }
    
    /**
     * 将单个list转成json字符串
     * 
     * @param list
     * @return
     * @throws Exception
     */
    public static String setList(List<Map<String, Object>> list)
                    throws Exception {
            String jsonL = "";
            StringBuffer temp = new StringBuffer();
            temp.append("[");
            for (int i = 0; i < list.size(); i++) {
                    Map<String, Object> m = list.get(i);
                    if (i == list.size() - 1) {
                            temp.append(setJosn(m));
                    } else {
                            temp.append(setJosn(m) + ",");
                    }
            }
            if (temp.length() > 1) {
                    temp = new StringBuffer(temp.substring(0, temp.length()));
            }
            temp.append("]");
            jsonL = temp.toString();
            return jsonL;
    }

    public static String  List2Json(List<Map<Object,Object>> obj) {

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
    public static List<Map<Object,Object>> jsonArray2List(JSONArray jsonArray){
         List<Map<Object,Object>> listResult = new ArrayList<Map<Object,Object>>();
            try {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    Iterator iterator = jsonObject.keys();
                    Map result = new HashMap();
                    while (iterator.hasNext()) {
                        String key = (String) iterator.next();
                        String value = jsonObject.getString(key);
                        result.put(key, value);
                    }
                    if (result != null && result.size() > 0) {
                        listResult.add(result);
                    }
                }
            } catch (JSONException e) {
                //如果json字符串出现空的情况，则说明json在转的过程中出现空
                // TODO Auto-generated catch block
//			e.printStackTrace();
            }
        return listResult;
    }



}
