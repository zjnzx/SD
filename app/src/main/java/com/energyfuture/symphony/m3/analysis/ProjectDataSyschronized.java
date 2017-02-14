package com.energyfuture.symphony.m3.analysis;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.energyfuture.symphony.m3.common.ApiClient;
import com.energyfuture.symphony.m3.common.URLs;
import com.energyfuture.symphony.m3.dao.TrProjectDao;
import com.energyfuture.symphony.m3.domain.Operation;
import com.energyfuture.symphony.m3.domain.TrDataSynchronization;
import com.energyfuture.symphony.m3.domain.TrProject;
import com.energyfuture.symphony.m3.domain.TrTask;
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
public class ProjectDataSyschronized {

    DBHelper dbHelper = null;
    Context dCtx;
    List<TrDataSynchronization> syncData = new ArrayList<>();
    public ProjectDataSyschronized(Context cxt) {
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
                String sql = getSql(condMap);
                //1，查询本地库获取条件集合
                List<Map<String, String>> dataList = getLocalData(sql);
                //2，根据条件集合拼装json查询条件，调用http请求，获取json格式查询结果，转换成map返回
                Map<String, String> dataMap = null;
                Map<String, Object> resMap = new HashMap<String, Object>();
                Message msg = new Message();
                try {
                    dataMap = getDataFromWebToMap(condMap, dataList);
                    if(dataMap != null) {
                        //3，解析map入库
                        resMap = parseFromWebDataToMap(dataMap);
                    }else{
                        resMap.put("1","网络异常");
                    }
                    msg.obj = resMap;
                    msg.what = 1;
                    handler.sendMessage(msg);
                } catch (JSONException e) {
                    e.printStackTrace();
                    msg.what = 0;
                    handler.sendMessage(msg);
                    Constants.recordExceptionInfo(e, dCtx, dCtx.toString()+"/ProjectDataSyschronized");
                }
            }
        }.start();
    }

    public void getProDataFromWeb(final Map<String, Object> condMap, final Handler handler){
        new Thread() {
            @Override
            public void run() {
                //拼装查询sql
                String sql = getSql(condMap);
                //1，查询本地库获取条件集合
                List<Map<String, String>> dataList = getLocalData(sql);
                //2，根据条件集合拼装json查询条件，调用http请求，获取json格式查询结果，转换成map返回
                Map dataMap = null;
                List<TrProject> trProjects = new ArrayList<TrProject>();
                Message msg = new Message();
                dataMap = getProDataFromWebToMap(condMap, dataList);
                if(dataMap != null) {
                    //3，解析map入库
                    try{
                        trProjects = parseProFromWebDataToMap(dataMap);
                    }catch (JSONException e){
                        Constants.recordExceptionInfo(e, dCtx, dCtx.toString()+"/ProjectDataSyschronized");
                    }
                }else{
//                        resMap.put("1","网络异常");
                }
                msg.obj = trProjects;
                msg.what = 1;
                handler.sendMessage(msg);
            }
        }.start();
    }

    public void getProTaskDataFromWeb(final Handler handler,final String projcetid){
        new Thread() {
            @Override
            public void run() {
                //2，根据条件集合拼装json查询条件，调用http请求，获取json格式查询结果，转换成map返回
                Map dataMap = null;
                List<TrTask> trTasks = new ArrayList<TrTask>();
                Message msg = new Message();
                dataMap = getProTaskDataFromWebToMap(projcetid);
                if(dataMap != null) {
                    //3，解析map入库
                    try{
                        trTasks = parseTaskFromWebDataToMap(dataMap);
                    }catch (JSONException e){
                        Constants.recordExceptionInfo(e, dCtx, dCtx.toString()+"/ProjectDataSyschronized");
                    }
                }else{
//                        resMap.put("1","网络异常");
                }
                msg.obj = trTasks;
                msg.what = 1;
                handler.sendMessage(msg);
            }
        }.start();
    }

    /**
     * 终端要从网络获取任务数据
     * @param condMap 数据获取条件
     * @return
     */
    public void getTaskDataFromWeb(final Map<String, Object> condMap, final Handler handler){
        Map<String, String> dataMap = null;
        Map<String, Object> resMap = new HashMap<String, Object>();
        Message msg = new Message();
        try {
            dataMap = getDataFromWebToMap(condMap, new ArrayList<Map<String, String>>());
            if(dataMap != null) {
                //3，解析map入库
                resMap = parseFromWebDataToMap(dataMap);
            }else{
                resMap.put("1","网络异常");
            }
            msg.obj = resMap;
            msg.what = 2;
            handler.sendMessage(msg);
        } catch (JSONException e) {
            e.printStackTrace();
            msg.what = 0;
            handler.sendMessage(msg);
            Constants.recordExceptionInfo(e, dCtx, dCtx.toString()+"/ProjectDataSyschronized");
        }
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
        List<TrTask> sympTasks = new ArrayList<TrTask>();
        TrProject project = null;
        if(null != dataMap){
            json = dataMap.get("json");
        }
        JSONObject object = new JSONObject(json);
        JSONArray dataArray = object.getJSONArray("DATA");
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
                if(opt.equals("I")||opt.equals("U")){//插入或更新
                    if(tableName.equals("TR_DATA_SYNCHRONIZATION")&&data.getString("DATATYPE").equals("2")){
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
                    //如果为最新的项目，则做如下操作 2015-8-26 zhaohd
                    else if (tableName.equals("TR_PROJECT")) {
                        //查询本地是否已存在该项目信息
//                        List<TrProject> trProjects = queryProjectById(data.getString("PROJECTID"));
//                        if(trProjects.size() == 0) {
                            project = new TrProject();
                            project.setProjectname(data.getString("PROJECTNAME"));
//                        }
                    }else if (tableName.equals("TR_TASK")){
                        TrTask task = new TrTask();
                        task.setTaskid(data.getString("TASKID"));
                        task.setDetectionttype(data.getString("DETECTIONTTYPE"));
                        sympTasks.add(task);
                    }
                    StringBuilder sql = new StringBuilder();
                    sql.append( "REPLACE INTO ").append(tableName);
                    String key = "";
                    String value = "";
                    Iterator it = data.keys();
                    while(it.hasNext()){
                        String temp = it.next().toString();
                        key += temp+",";
                        String valueitem = data.getString(temp);
                        if(tableName.equals("TR_PROJECT") && temp.equals("STARTTIME") || temp.equals("ENDTIME")){
                            valueitem = TimeStamp2Date(data.getString(temp),"yyyy-MM-dd");
                        }
                        value +="'"+valueitem+"',";
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
                    sql.append(" DELETE FROM ").append(tableName).append(" WHERE ");
                    if(tableName.equals("USER_INFO")){
                        sql.append(" YHID =").append("'"+data.getString("YHID")+"'");
                    }else if(tableName.equals("TR_PROJECT")){
                        sql.append(" PROJECTID =").append("'"+ data.getString("PROJECTID")+"'");
                    }else if(tableName.equals("TR_TASK")){
                        sql.append(" TASKID =").append("'"+data.getString("TASKID")+"'");
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
        //如果为最新的项目，则做如下操作 2015-8-26 zhaohd
        if(project != null){
            resultMap.put("project",project);
            resultMap.put("task",sympTasks);
        }
        resultMap.put("0","ok");
        return resultMap;
    }

    public List<TrProject> parseProFromWebDataToMap(Map dataMap) throws JSONException{
        List<TrProject> trProjectList = new ArrayList<TrProject>();
        if(dataMap.get("ok").equals("true")){
            net.sf.json.JSONArray array = net.sf.json.JSONArray .fromObject(dataMap.get("project"));
            for(int i = 0 ; i < array.length() ; i++){
                TrProject trProject = new TrProject();
                net.sf.json.JSONObject object = array.getJSONObject(i);
                trProject.setProjectid(object.get("projectid").toString());
                trProject.setProjectname(object.get("projectname").toString());
                trProject.setProjectstate(object.get("projectstate").toString());
                trProject.setUpdatetime(object.get("updatetime").toString());
                trProject.setStationname(object.get("stationname").toString());
                trProject.setActualsendtime(object.get("actualsendtime").toString());
                trProject.setActualstarttime(object.get("actualstarttime").toString());
                trProject.setAware(object.get("aware").toString());
                trProject.setCreateperson(object.get("createperson").toString());
                trProject.setCreatetime(object.get("createtime").toString());
                trProject.setDepartment(object.get("department").toString());
                trProject.setStationid(object.get("stationid").toString());
                trProject.setTaskcount(object.get("taskcount").toString());
                trProject.setWorkperson(object.get("workperson").toString());
                trProject.setZrpearson(object.get("zrpearson").toString());
                trProject.setZpperson(object.get("zpperson").toString());
                trProject.setStarttime(object.get("starttime").toString());
                trProject.setEndtime(object.get("endtime").toString());
                trProject.setFinishcount(object.get("finishcount").toString());
                trProject.setReasondescribe(object.get("reasondescribe").toString());
                trProject.setSafetynotice(object.get("safetynotice").toString());
                trProjectList.add(trProject);
            }
        }
        return trProjectList;
    }

    public List<TrTask> parseTaskFromWebDataToMap(Map dataMap) throws JSONException{
        List<TrTask> trTasks = new ArrayList<TrTask>();
        if(dataMap.get("ok").equals("true")){
            net.sf.json.JSONArray array = net.sf.json.JSONArray .fromObject(dataMap.get("task"));
            for(int i = 0 ; i < array.length() ; i++){
                TrTask trTask = new TrTask();
                net.sf.json.JSONObject object = array.getJSONObject(i);
                trTask.setTaskid(object.get("taskid").toString());
                trTask.setDetectionttype(object.get("detectionttype").toString());
                trTask.setAuditperson(object.get("auditperson").toString());
                trTask.setPlanendtime(object.get("planendtime").toString());
                trTask.setFinishcount(object.get("finishcount").toString());
                trTask.setTaskname(object.get("taskname").toString());
                trTask.setTaskperson(object.get("taskperson").toString());
                trTask.setChangeother(object.get("changeother").toString());
                trTask.setEquipmentid(object.get("equipmentid").toString());
                trTask.setActualstarttime(object.get("actualstarttime").toString());
                trTask.setStepcount(object.get("stepcount").toString());
                trTask.setWorkperson(object.get("workperson").toString());
                trTask.setOrderby(object.get("orderby").toString());
                trTask.setAppraise_changeflow(object.get("appraiseChangeflow").toString());
                trTask.setChangework(object.get("changework").toString());
                trTask.setConclusionperson(object.get("conclusionperson").toString());
                trTask.setTaskstate(object.get("taskstate").toString());
                trTask.setCleanperson(object.get("cleanperson").toString());
                trTask.setFinishtime(object.get("finishtime").toString());
                trTask.setImprove(object.get("improve").toString());
                trTask.setPerson(object.get("person").toString());
                trTask.setAudit_result(object.get("auditResult").toString());
                trTask.setAppraise_abnormal(object.get("appraiseAbnormal").toString());
                trTask.setMethod(object.get("method").toString());
                trTask.setUpdatetime(object.get("updatetime").toString());
                trTask.setAppraise_changeperson(object.get("appraiseChangeperson").toString());
                trTask.setChangeequipment(object.get("changeequipment").toString());
                trTask.setProjectid(object.get("projectid").toString());
                trTask.setPlanstarttime(object.get("planstarttime").toString());
                trTask.setAudit_question(object.get("auditQuestion").toString());
                trTask.setAppraise_suggest(object.get("appraiseSuggest").toString());
                trTask.setRecoverperson(object.get("recoverperson").toString());
                trTask.setActualsendtime(object.get("actualsendtime").toString());
                trTask.setChangeenv(object.get("changeenv").toString());
                trTasks.add(trTask);
            }
        }
        return trTasks;
    }

    public String TimeStamp2Date(String timestampString, String formats){
        Long timestamp = Long.parseLong(timestampString);
        String date = new java.text.SimpleDateFormat(formats).format(new java.util.Date(timestamp));
        return date;
    }

    private List<TrProject> queryProjectById(String projectid) {
        TrProject project = new TrProject();
        project.setProjectid(projectid);
        TrProjectDao sympProjectDao = new TrProjectDao(dCtx);
        return sympProjectDao.queryTrProjectsList(project);
    }

    /**
     * 将本地的查询出的数据封装成json提交到平台，让台进行对比，如果数据有改变则把数据以json
     * 格式返回，同步到本地数据
     * @param condMap 从平台得到的数据
     * @return
     */
    private Map<String, String> getDataFromWebToMap(Map<String, Object> condMap,List<Map<String,String>> dataList) {
        Map<String,String> jsonMap=null;
        try{
           String obj = (String)condMap.get("OBJ");
           List<Operation> optList = (List<Operation>)condMap.get("OPELIST");
           String type = (String)condMap.get("TYPE");
           int size = (Integer)condMap.get("SIZE");
           String userId = (String)condMap.get("USERID");
           String key = (String)condMap.get("KEY");

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
               String updateTime = null;
               if(dataList.size()>0){
                   updateTime = dataList.get(0).get("UPDATETIME");
               }
               dataObj.put("UPDATE",updateTime);
               array.put(dataObj);
           }else{
               if(dataList.size()>0) {
                   for (int i = 0; i < dataList.size(); i++) {
                       JSONObject dataObj = new JSONObject();
                       if (obj.equals("PROJECT")) {
                           dataObj.put("ID", dataList.get(i).get("PROJECTID"));
                           dataObj.put("UPDATE", dataList.get(i).get("UPDATETIME"));
                       } else if (obj.equals("TASK")) {
                           dataObj.put("ID", dataList.get(i).get("TASKID"));
                           dataObj.put("UPDATE", dataList.get(i).get("UPDATETIME"));
                       }else if(obj.equals("LEDGER")){
                           dataObj.put("ID",dataList.get(i).get("SYNCHRONIZATIONID"));
                           dataObj.put("UPDATE",dataList.get(i).get("RECORDTIME"));
                           dataObj.put("VERSIONNUMBER",dataList.get(i).get("VERSIONNUMBER"));
                       }
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
           jsonMap = ApiClient.httpGetDataFromWeb(null,params,"DataFromWeb","1",dCtx,"文本");
       }
       catch(Exception e)
       {
           Log.e("getDataFromWebToMap","平台数据获取错误");
           e.printStackTrace();
           Constants.recordExceptionInfo(e, dCtx, dCtx.toString()+"/ProjectDataSyschronized");
       }

        return jsonMap;

    }

    private Map getProDataFromWebToMap(Map<String, Object> condMap,List<Map<String,String>> dataList) {
        Map resultMap =null;
        try{
            String obj = (String)condMap.get("OBJ");
            List<Operation> optList = (List<Operation>)condMap.get("OPELIST");
            String type = (String)condMap.get("TYPE");
            int size = (Integer)condMap.get("SIZE");
            String userId = (String)condMap.get("USERID");
            String key = (String)condMap.get("KEY");

            JSONObject object = new JSONObject();
            object.put("OPTE","QUARY");
            object.put("OBJECT",obj);
            object.put("USERID",userId);
            object.put("TESTINGTYPE",type);
            object.put("COUNT",""+size);
            object.put("KEY",key);


            Map<String,Object> params = new HashMap<>();
            StringBuffer sb = new StringBuffer();
            if(dataList.size()>0) {
                for (int i = 0; i < dataList.size(); i++) {
                    if(dataList.size() - i == 1){
                        sb.append(dataList.get(i).get("PROJECTID"));
                    }else{
                        sb.append(dataList.get(i).get("PROJECTID") + ",");
                    }
                }
            }
            params.put("dataObj",sb.toString());
            Log.e("----------------",sb.toString());
            String http = URLs.HTTP+URLs.HOST+"/"+ "inspectfuture/" +URLs.PROJECTCONTRAST;
            resultMap = ApiClient.sendHttpPostMessageData(null, http, params, "", dCtx, "文本");
//            jsonMap = ApiClient.httpProGetDataFromWeb(null, params, "DataFromWeb", "1", dCtx, "文本");
        }
        catch(Exception e)
        {
            Log.e("getDataFromWebToMap","平台数据获取错误");
            e.printStackTrace();
            Constants.recordExceptionInfo(e, dCtx, dCtx.toString()+"/ProjectDataSyschronized");
        }

        return resultMap;

    }

    private Map getProTaskDataFromWebToMap(String projcetid) {
        Map resultMap =null;
        try{
            Map<String,Object> params = new HashMap<>();

            params.put("projectId",projcetid);
            String http = URLs.HTTP+URLs.HOST+"/" +"inspectfuture/" +URLs.PROJECTDOWNLOAD;
            resultMap = ApiClient.sendHttpPostMessageData(null, http, params, "",dCtx , "文本");
        }
        catch(Exception e)
        {
            Log.e("getDataFromWebToMap","平台数据获取错误");
            e.printStackTrace();
            Constants.recordExceptionInfo(e, dCtx, dCtx.toString());
        }

        return resultMap;

    }

    /**
     * 根据拼装好的sql在本地库查询出数据
     *
     * @param sql 拼装好的sql语句
     * @return
     */
    private List<Map<String, String>> getLocalData(String sql) {
        List<Map<String, String>> dataMap = null;
        dataMap = dbHelper.selectSQL(sql,null);
        return dataMap;
    }
    /**
     * 拼装查询本地库的sql
     * @return 拼装好的sql语句
     */
    private String getSql(final Map<String, Object> condMap) {
        String obj = (String)condMap.get("OBJ");
        List<Operation> opeList = (List<Operation>)condMap.get("OPELIST");
        String type = (String)condMap.get("TYPE");
        int size = (Integer)condMap.get("SIZE");
        String key = (String)condMap.get("KEY");
        StringBuilder sql = new StringBuilder();
        String orderField = "RECORDTIME";
        sql.append("SELECT * FROM ");
        if(type == null || "".equals(type)){
            if(obj.equals("USER")){
                sql.append(" USER_INFO ");
            }else if(obj.equals("PROJECT")){
                sql.append(" TR_PROJECT ");
                orderField = "UPDATETIME";
            }else if(obj.equals("TASK")){
                sql.append(" TR_TASK ");
                orderField = "UPDATETIME";
            }else if(obj.equals("COND")){
                sql.append(" TR_CONDITION_INFO ");
                orderField = "RECORDDATE";
            }else if(obj.equals("LEDGER")){
                sql.append(" TR_DATA_SYNCHRONIZATION ");
            }
        }
        sql.append(" WHERE 1 = 1 ");
        if(null != opeList){
            for(int i=0;i<opeList.size();i++){
                Operation ope = opeList.get(i);
                sql.append(" AND " + ope.getField()).append(ope.getOperate()).append("'" + ope.getValue() + "'").append(" ");
            }
        }
        if(key != null){
            if(obj.equals("TASK")){
                sql.append(" AND PROJECTID =" + "'" + key + "'");
            }else if(obj.equals("STEP")) {
                sql.append(" AND TASKID =" + "'" + key + "'");
            }else if(obj.equals("COND")){
                sql.append(" AND PROJECTID =" + "'" + key + "'");
            }else if(obj.equals("CARD")){
                sql.append(" AND STEPID =" + "'" + key + "'");
            }
        }
        if(!obj.equals("USER")){
            if(!obj.equals("PROJECT")){
                size = 1000;
            }
            sql.append(" ORDER BY " + orderField + " LIMIT 0,").append(size);

        }
        return sql.toString();

    }


}
