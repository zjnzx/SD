package com.energyfuture.symphony.m3.analysis;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.energyfuture.symphony.m3.common.ApiClient;
import com.energyfuture.symphony.m3.dao.TrEquipmentDao;
import com.energyfuture.symphony.m3.domain.Operation;
import com.energyfuture.symphony.m3.domain.TrDataSynchronization;
import com.energyfuture.symphony.m3.domain.TrEquipment;
import com.energyfuture.symphony.m3.util.Constants;
import com.energyfuture.symphony.m3.util.DBHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/6/2.
 */
public class DataSyschronized {

    DBHelper dbHelper = null;
    Context dCtx;
    List<TrDataSynchronization> syncData = new ArrayList<>();
    public DataSyschronized(Context cxt) {
        dbHelper = new DBHelper(cxt);
        dCtx = cxt;
    }
    public SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    /**
     * 终端要从网络获取的数据
     * @param condMap 数据获取条件
     * @return
     */
    public void getDataFromWeb(final Map<String, Object> condMap, final Handler handler){
        new Thread() {
            @Override
            public void run() {

                //拼装查询sql
//                String sql = getSql(condMap);
                //1，查询本地库获取条件集合
//                List<Map<String, String>> dataList = getLocalData(sql);
                //2，根据条件集合拼装json查询条件，调用http请求，获取json格式查询结果，转换成map返回
                Map<String, String> dataMap = null;
                Map<String, Object> resMap = new HashMap<String, Object>();
                Message msg = new Message();
                try {
                    dataMap = getDataFromWebToMap(condMap);
                    if(dataMap != null) {
                        //3，解析map入库
                        resMap = parseFromWebDataToMap(dataMap);
                    }else{
                        resMap.put("0","网络异常");
                    }
                    msg.obj = resMap;
                    msg.what = 1;
                    handler.sendMessage(msg);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Constants.recordExceptionInfo(e, dCtx, dCtx.toString() + "/DataSyschronized");
                    msg.what = 0;
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }

    /**
     * 解析从平台获取的封装在map中的json格式的数据
     * @param dataMap 从平台得到的数据
     * @return
     */
    private Map<String, Object> parseFromWebDataToMap(Map<String, String> dataMap) throws JSONException {
        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("1","数据解析异常");
        int saveCount = 0;
        String json="";
        if(null != dataMap){
            json = dataMap.get("json");
        }
        JSONObject object = new JSONObject(json);
        JSONArray dataArray = object.getJSONArray("DATA");
        String obj = object.getString("OBJECT");
        List<String> addOrUpdateList = new ArrayList<String>() ;
        List<String> deleteList = new ArrayList<String>();
        if(dataArray == null) return resultMap;
        for(int i=0;i<dataArray.length();i++){
            JSONObject dataObj = dataArray.getJSONObject(i);
            String tableName = dataObj.getString("TABLENAME");
            Log.i("存储数据",tableName);
            JSONArray array = dataObj.getJSONArray("DATA");
            for(int y=0;y<array.length();y++){
                JSONObject data = array.getJSONObject(y);
                String opt = data.getString("#OPT#");//后台根据数据是否更新传个标识
                data.remove("#OPT#");
                if(opt.equals("I")||opt.equals("U")){   //插入或更新
                    if(tableName.equals("TR_DATA_SYNCHRONIZATION")){
                       TrDataSynchronization sds = new TrDataSynchronization();
                        sds.setSynchronizationid(data.getString("SYNCHRONIZATIONID"));
                        sds.setTablename(data.getString("TABLENAME"));
                        sds.setKeyname(data.getString("KEYNAME"));
                        sds.setKeynamevalue(data.getString("KEYNAMEVALUE"));
                        sds.setVersionnumber(data.getString("VERSIONNUMBER"));
                        sds.setRecordtime(data.getString("RECORDTIME"));
                        sds.setVersionpersonid(data.getString("VERSIONPERSONID"));
                        sds.setOpertype(data.getString("OPERTYPE"));
                        sds.setDatatype(data.getString("DATATYPE"));
                        sds.setFileurl(data.getString("FILEURL"));
                        sds.setFilename(data.getString("FILENAME"));
                        syncData.add(sds);
                    }
                    if(("PROJECTADD").equals(obj)){
                        //清空表
                        StringBuilder delSql = new StringBuilder();
                        delSql.append(" DELETE FROM ").append(tableName);
                        deleteList.add(delSql.toString());
                        dbHelper.deleteSQLAll(deleteList);
                        deleteList.clear();
                    }else if(("PROJECTADDEQUIPMENT").equals(obj)){
                        //删除选中变电站下的设备
                        TrEquipmentDao trEquipmentDao = new TrEquipmentDao(dCtx);
                        TrEquipment trEquipment = new TrEquipment();
                        trEquipment.setStationid(data.getString("STATIONID"));
                        List<TrEquipment> trEquipmentList = new ArrayList<TrEquipment>();
                        trEquipmentList.add(trEquipment);
                        trEquipmentDao.deleteListData(trEquipmentList);
                    }
                    StringBuilder sql = new StringBuilder();
                    sql.append( "REPLACE INTO ").append(tableName);
                    String key = "";
                    String value = "";
                    Iterator it = data.keys();
                    while(it.hasNext()){
                        String temp = it.next().toString();
                        key += temp+",";
                        value +="'"+data.getString(temp)+"',";
                    }
                    key =key.substring(0,key.lastIndexOf(","));
                    value = value.substring(0,value.lastIndexOf(","));
                    sql.append(" ( ").append(key).append(" ) VALUES(").append(value).append(" )");
                    addOrUpdateList.add(sql.toString());
                    saveCount ++ ;
                    if(saveCount % 100  == 0){
                        dbHelper.insertSQLAll(addOrUpdateList);
                        addOrUpdateList.clear();
                        Log.i("存储数据","已存储100条");
                    }

                }else if(opt.equals("D")){//删除操作
                    StringBuilder sql = new StringBuilder();
                    sql.append(" DELETE FROM ").append(tableName).append(" WHERE 1=1 ");
                    if(tableName.equals("USER_INFO")){
                        sql.append("AND YHID =").append("'"+data.getString("YHID")+"'");
                    }else if(tableName.equals("TR_PROJECT")){
                        sql.append("AND PROJECTID =").append("'"+ data.getString("PROJECTID")+"'");
                    }else if(tableName.equals("TR_TASK")){
                        sql.append("AND TASKID =").append("'"+data.getString("TASKID")+"'");
                    }else if(tableName.equals(" TR_DATA_SYNCHRONIZATION ")){
                        sql.append("AND SYNCHRONIZATIONID =").append("'"+data.getString("SYNCHRONIZATIONID")+"'");
                    }else if(tableName.equals("TR_EQUIPMENT")){
                        sql.append("AND EQUIPMENTID =").append("'"+data.getString("EQUIPMENTID")+"'");
                    }else if(tableName.equals("TR_STATION")){
                        sql.append("AND STATIONID =").append("'"+data.getString("STATIONID")+"'");
                    }
                    deleteList.add(sql.toString());
                }
            }
        }
        dbHelper.insertSQLAll(addOrUpdateList);
        dbHelper.deleteSQLAll(deleteList);
        Log.i("存储数据", "共存储" + saveCount + "条");
        Log.i("删除数据","共删除" + deleteList.size() + "条");
        Log.i("存储", "共处理" + (saveCount + deleteList.size()) + "条");
        resultMap = new HashMap<>();
        resultMap.put("data",syncData);
        resultMap.put("0","ok");
        return resultMap;
    }

    /**
     * 将本地的查询出的数据封装成json提交到平台，让台进行对比，如果数据有改变则把数据以json
     * 格式返回，同步到本地数据
     * @param condMap 从平台得到的数据
     * @return
     */
    private Map<String, String> getDataFromWebToMap(Map<String, Object> condMap) {
        Map<String,String> jsonMap=null;
        try{
           String obj = (String)condMap.get("OBJ");
           List<Operation> optList = (List<Operation>)condMap.get("OPELIST");
           String type = (String)condMap.get("TYPE");
           int size = (Integer)condMap.get("SIZE");
           String userId = (String)condMap.get("USERID");
           String key = (String)condMap.get("KEY");
           String data = (String)condMap.get("DATA");

           JSONObject object = new JSONObject();
           object.put("OPTE","QUARY");
           object.put("OBJECT",obj);
           object.put("USERID",userId);
           object.put("TESTINGTYPE",type);
           object.put("COUNT",""+size);
           object.put("KEY",key);
           JSONArray array = new JSONArray();
           if(obj.equals("USER")){
               String pwd = "";
               for (Operation op :optList){
                   if("YHMM".equals(op.getField())){
                       pwd = op.getValue();
                       break;
                   }
               }
               JSONObject dataObj = new JSONObject();
               dataObj.put("ID",userId + "#" + pwd);
//               String updateTime = null;
//               if(dataList.size()>0){
//                   updateTime = dataList.get(0).get("UPDATETIME");
//               }
//               dataObj.put("UPDATE",updateTime);
               array.put(dataObj);
           }else{
               if (obj.equals("PROJECT")) {
                   JSONObject dataObj = new JSONObject();
                   dataObj.put("TYPE", type);
                   array.put(dataObj);
               } else if (obj.equals("TASK")) {
                   String[] str = data.split(",");
                   for(int i = 0;i < str.length;i++){
                       JSONObject dataObj = new JSONObject();
                       dataObj.put("ID",str[i]);
                       array.put(dataObj);
                   }
//                   dataObj.put("TYPE", type);
               }else if (obj.equals("INSPLIST")) {//6大项
                   JSONObject dataObj = new JSONObject();
                   dataObj.put("TYPE", type);
                   array.put(dataObj);
               }else if (obj.equals("INSPINFO") && type.equals("GENERAL")) {//普通模板详细信息
                   JSONObject dataObj = new JSONObject();
                   dataObj.put("TYPE", type);
                   array.put(dataObj);
               }else if (obj.equals("INSPINFO") && type.equals("BUSHING")) {//套管模板详细信息
                   JSONObject dataObj = new JSONObject();
                   dataObj.put("TYPE", type);
                   array.put(dataObj);
               }else if (obj.equals("INSPINFO") && type.equals("WORK")) {//作业情况模板详细信息
                   JSONObject dataObj = new JSONObject();
                   dataObj.put("TYPE", type);
                   array.put(dataObj);
               }else if (obj.equals("INSPINFO") && type.equals("OILLEVEL")) {//油位指示模板详细信息
                   JSONObject dataObj = new JSONObject();
                   dataObj.put("TYPE", type);
                   array.put(dataObj);
               }else if (obj.equals("INSPINFO") && type.equals("OLI")) {//油色谱模板详细信息
                   JSONObject dataObj = new JSONObject();
                   dataObj.put("TYPE", type);
                   array.put(dataObj);
               }else if(obj.equals("COMMTHEME")){//沟通交流主题信息
                   JSONObject dataObj = new JSONObject();
                   dataObj.put("TYPE", type);
                   array.put(dataObj);
               }else if(obj.equals("COMMREPLY")){//沟通交流回复信息
                   JSONObject dataObj = new JSONObject();
                   dataObj.put("TYPE", type);
                   array.put(dataObj);
               }else if(obj.equals("LEDGER")){//台账表
                   String max = (String)condMap.get("MAX");
                   if(!max.equals("")){
                       JSONObject dataObj = new JSONObject();
                       dataObj.put("MAX", max);
                       array.put(dataObj);
                   }
               }
           }

           if(array.length()==0){
               object.put("DATA",null);
           }else {
               object.put("DATA",array);
           }
           String jsonData = object.toString();
           Map<String,Object> params = new HashMap<>();
           params.put("json",jsonData);
           jsonMap = ApiClient.httpGetDataFromWeb(null,params,"DataFromWeb","0",dCtx,"文本");
       }
       catch(Exception e)
       {
           Log.e("getDataFromWebToMap","平台数据获取错误");
           e.printStackTrace();
           Constants.recordExceptionInfo(e, dCtx, dCtx.toString() + "/DataSyschronized");
       }

        return jsonMap;

    }
    /**
     * 根据拼装好的sql在本地库查询出数据
     *
     * @param sql 拼装好的sql语句
     * @return
     */
//    private List<Map<String, String>> getLocalData(String sql) {
//        List<Map<String, String>> dataMap = null;
//        dataMap = dbHelper.selectSQL(sql,null);
//        return dataMap;
//    }

}
