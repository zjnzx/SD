package com.energyfuture.symphony.m3.dao;

import android.content.Context;
import android.util.Log;

import com.energyfuture.symphony.m3.domain.TrConditionInfo;
import com.energyfuture.symphony.m3.util.DBHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/3/24.
 */
public class TrConditionInfoDao {
    DBHelper dbHelper = null;

    public TrConditionInfoDao(Context cxt) {
        dbHelper = new DBHelper(cxt);
    }

    public SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 获取巡检任务表信息
     *
     * @param @param  null
     * @param @return
     * @return list
     * @designer
     * @author dingwujun
     * @since 2014-11-6
     */
    public List<TrConditionInfo> queryTrConditionInfoList(TrConditionInfo conditionObj) {
        List<TrConditionInfo> list = new ArrayList<TrConditionInfo>();
        String sqlwhere="";
        if(conditionObj!=null) {
            if(conditionObj.getStationid()!=null&&!conditionObj.getStationid().equals(""))
            {
                sqlwhere+=" AND STATIONID='"+conditionObj.getStationid()+"'";
            }
            if(conditionObj.getTaskid()!=null&&!conditionObj.getTaskid().equals(""))
            {
                sqlwhere+=" AND TASKID='"+conditionObj.getTaskid()+"'";
            }
        }
        String sql = "SELECT * FROM TR_CONDITION_INFO WHERE 1=1 "+sqlwhere+" ORDER BY RECORDDATE DESC";
        Log.v("logsql",sql);
        List<Map<String, String>> cursor = dbHelper.selectSQL(sql, null);
        if (cursor.size() > 0) {
            for (int i = 0; i < cursor.size(); i++){
                Map<String, String> m = cursor.get(i);
                TrConditionInfo obj = new TrConditionInfo();
                obj.setRecordid(m.get("RECORDID"));
                obj.setTaskid(m.get("TASKID"));
                obj.setPersonid(m.get("PERSONID"));
                obj.setRecorddate(m.get("RECORDDATE"));
                obj.setTemperature(m.get("TEMPERATURE"));
                obj.setHumidity(m.get("HUMIDITY"));
                obj.setWeather(m.get("WEATHER"));
                obj.setAnemograph(m.get("ANEMOGRAPH"));
                obj.setRadiation(m.get("RADIATION"));
                obj.setStationid(m.get("STATIONID"));
                list.add(obj);
            }
        }
        return list;
    }

    /**
     * 获取微水、气体、超声波表的信息
     *
     * @param @param  null
     * @param @return
     * @return list<Map>
     * @designer
     */
//    public  List<Map> queryManyTableTaskDataInfo(SympTask parameterObj,String table){
//        List<Map> list= new ArrayList<Map>();
//        if (parameterObj!=null){
//            if (parameterObj.getTaskid()!=null){
//
//                String sql="SELECT * FROM( SELECT A.PROJECTID,A.TASKNAME,A.PERSON,A.WORKPERSON,A.TASKID," +
//                        " C.STATIONNAME,C.EQUIPMENTTYPE,C.COMPANY,C.MODEL,C.EQUIPMENTID,C.EQUIPMENTNAME CNAME, B.RECORDTIME,B.STEPID,B.YARDMANNUMBER," +
//                        " E.EQUIPMENTNAME YQNAME,E.MODEL YQMODEL,E.DETECTIONEQUIPMENTID,F.CODENAME SBLX FROM SYMP_TASK A  " +
//                        " LEFT JOIN "+table+" B ON A.TASKID=B.TASKID   " +
//                        " LEFT JOIN SYMP_EQUIPMENT C ON C.EQUIPMENTID=B.EQUITEMNETID   " +
//                        " LEFT JOIN SYMP_DETECTION_EQUIPMENT E ON E.DETECTIONTYPE=A.DETECTIONTTYPE  " +
//                        " LEFT JOIN SYMP_SYSTEMCODE F ON  C.EQUIPMENTTYPE=F.CODEVALUE AND F.CODETYPE='D1'  " +
//                        " WHERE A.TASKID='"+parameterObj.getTaskid()+"' AND B.FORDERBY=1) G " +
//                        " LEFT JOIN SYMP_CONDITION_INFO D ON  D.PROJECTID=G.PROJECTID  ORDER BY D.RECORDDATE DESC LIMIT 0,1";
//                List<Map<String, String>> cursor = dbHelper.selectSQL(sql, null);
//                if (cursor.size() > 0) {
//                    for (int i = 0; i < cursor.size(); i++){
//                        Map<String, String> m = cursor.get(i);
//                        Map obj = new HashMap();
//                        obj.put("STATIONNAME",m.get("STATIONNAME"));
//                        obj.put("COMPANY",m.get("COMPANY"));
//                        obj.put("EQUIPMENTTYPE",m.get("SBLX"));
//                        obj.put("MODEL",m.get("MODEL"));
//                        obj.put("EQUIPMENTID",m.get("EQUIPMENTID"));
//                        obj.put("CNAME",m.get("CNAME"));
//
//
//                        obj.put("PERSON",m.get("PERSON"));
//                        obj.put("RECORDTIME",m.get("RECORDTIME"));
//                        obj.put("STEPID",m.get("STEPID"));
//                        obj.put("TASKNAME",m.get("TASKNAME"));
//                        obj.put("WORKPERSON",m.get("WORKPERSON"));
//                        obj.put("TASKID",m.get("TASKID"));
//                        obj.put("YARDMANNUMBER",m.get("YARDMANNUMBER"));
//
//                        obj.put("TEMPERATURE",m.get("TEMPERATURE"));
//                        obj.put("HUMIDITY",m.get("HUMIDITY"));
//                        obj.put("WEATHER",m.get("WEATHER"));
//                        obj.put("ANEMOGRAPH",m.get("ANEMOGRAPH"));
//                        obj.put("RECORDID",m.get("RECORDID"));
//
//                        //仪器信号
//                        obj.put("YQMODEL",m.get("YQMODEL"));
//                        //仪器名称
//                        obj.put("YQNAME",m.get("YQNAME"));
//                        obj.put("DETECTIONEQUIPMENTID",m.get("DETECTIONEQUIPMENTID"));
//                        list.add(obj);
//                    }
//                }
//            }
//        }
//
//        return  list;
//    }

    public List<Map> queryConditionInfo(String time){
        List<Map> list= new ArrayList<Map>();
        String sql = "SELECT * from TR_CONDITION_INFO info " +
                "where info.RECORDDATE < '"+ time +"' " +
                "ORDER BY info.RECORDDATE desc " +
                "limit 1";
        List<Map<String, String>> cursor = dbHelper.selectSQL(sql, null);
        if(cursor.size() > 0){
            for(int i = 0; i < cursor.size(); i++){
                Map<String, String> m = cursor.get(i);
                Map obj = new HashMap();
                obj.put("TEMPERATURE",m.get("TEMPERATURE"));
                obj.put("HUMIDITY",m.get("HUMIDITY"));
                list.add(obj);
            }
        }else{
            sql = "SELECT * from TR_CONDITION_INFO info " +
                    "where info.RECORDDATE > '"+ time +"' " +
                    "ORDER BY info.RECORDDATE " +
                    "limit 1";
            cursor = dbHelper.selectSQL(sql, null);
            if(cursor.size() > 0){
                for(int i = 0; i < cursor.size(); i++){
                    Map<String, String> m = cursor.get(i);
                    Map obj = new HashMap();
                    obj.put("TEMPERATURE",m.get("TEMPERATURE"));
                    obj.put("HUMIDITY",m.get("HUMIDITY"));
                    list.add(obj);
                }
            }
        }
        return list;
    }

    /**
     * 根据条件修改环境数据
     * @designer
     * @since  2014-11-6
     * @author dingwujun
     * @param @param List<PmDevice>
     * @param @return
     * @return null
     */
    public void updateTrConditionInfo(Map<Object,Object> columnsMap,Map<Object,Object> wheresMap)
    {

        String sql="";
        if(columnsMap!=null&&columnsMap.size()>0)
        {
            //修改的条件拼接
            String sqlWhere="";
            if(wheresMap!=null&&wheresMap.size()>0)
            {

                    Map<Object,Object> map =wheresMap;
                    for (Map.Entry<Object,Object> entry : map.entrySet()) {
                        sqlWhere+= (" AND "+entry.getKey()+"='"+entry.getValue()+"' ");
                    }
            }
            //需要修改的字段

                Map<Object,Object> map =columnsMap;
                String columns="";
                for (Map.Entry<Object,Object> entry : map.entrySet()) {
                    columns+= (entry.getKey()+"='"+entry.getValue()+"',");
                }
                columns=columns.substring(0,columns.lastIndexOf(","));
                sql="UPDATE TR_CONDITION_INFO SET " +columns +" WHERE 1=1 "+sqlWhere;

            if(sql!=null&&!sql.equals(""))
            {
                dbHelper.execSQL(sql);
            }
        }
    }

    /**
     * 插入环境表数据
     * @designer
     * @since  2014-11-6
     * @author dingwujun
     * @param @param List<PmDevice>
     * @param @return
     * @return null
     */
    public void insertListData(List<TrConditionInfo> trConditionInfoList) {
        if(trConditionInfoList!=null&&trConditionInfoList.size()>0)
        {
            List<String> listSql= new ArrayList<String>();
            for(int i=0;i<trConditionInfoList.size();i++)
            {
                String sql="REPLACE INTO TR_CONDITION_INFO (" +
                        "RECORDID,TASKID,PERSONID,RECORDDATE,TEMPERATURE,HUMIDITY,WEATHER,ANEMOGRAPH,RADIATION,STATIONID" +
                        ")VALUES (" +
                        "'"+trConditionInfoList.get(i).getRecordid()+"'," +
                        "'"+trConditionInfoList.get(i).getTaskid()+"'," +
                        "'"+trConditionInfoList.get(i).getPersonid()+"'," +
                        "'"+trConditionInfoList.get(i).getRecorddate()+"'," +
                        "'"+trConditionInfoList.get(i).getTemperature()+"'," +
                        "'"+trConditionInfoList.get(i).getHumidity()+"'," +
                        "'"+trConditionInfoList.get(i).getWeather()+"'," +
                        "'"+trConditionInfoList.get(i).getAnemograph()+"'," +
                        "'"+trConditionInfoList.get(i).getRadiation()+"'," +
                        "'"+trConditionInfoList.get(i).getStationid()+"'" +
                        ")";
                listSql.add(sql);
            }
            if(listSql!=null&&listSql.size()>0)
            {
                dbHelper.insertSQLAll(listSql);
            }
        }
    }

    /**
     * 根据id删除环境表数据
     * @designer
     * @since  2014-11-6
     * @author dingwujun
     * @param @param List<PmDevice>
     * @param @return
     * @return null
     */
    public void deleteTrConditionInfo(String id)
    {
        String sql="";
        if(id!=null&&!id.equals(""))
        {
            sql="DELETE FROM TR_CONDITION_INFO WHERE RECORDID='" +id +"'";
            if(sql!=null&&!sql.equals(""))
            {
                dbHelper.execSQL(sql);
            }
        }
    }
}
