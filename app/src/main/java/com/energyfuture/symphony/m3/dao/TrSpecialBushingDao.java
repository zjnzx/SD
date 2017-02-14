package com.energyfuture.symphony.m3.dao;

import android.content.Context;

import com.energyfuture.symphony.m3.domain.TrSpecialBushing;
import com.energyfuture.symphony.m3.util.DBHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/3/24.
 */
public class TrSpecialBushingDao {
    DBHelper dbHelper = null;

    public TrSpecialBushingDao(Context cxt) {
        dbHelper = new DBHelper(cxt);
    }

    public SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 获取检测项目（套管）信息
     *
     * @param @param  null
     * @param @return
     * @return list
     * @designer
     * @author dingwujun
     * @since 2014-11-6
     */
    public List<TrSpecialBushing> queryTrSpecialBushingList(TrSpecialBushing trSpecialBushingParam) {
        List<TrSpecialBushing> list = new ArrayList<TrSpecialBushing>();
        String sqlwhere="";
        if(trSpecialBushingParam.getId() != null && !trSpecialBushingParam.getId().equals("")){
            sqlwhere += " AND ID='" + trSpecialBushingParam.getId() + "'";
        }
        if(trSpecialBushingParam.getDetectionprojectid() != null && !trSpecialBushingParam.getDetectionprojectid().equals("")){
            sqlwhere += " AND DETECTIONPROJECTID='" + trSpecialBushingParam.getDetectionprojectid() + "'";
        }

        String sql = "SELECT * FROM TR_SPECIAL_BUSHING WHERE 1=1 "+sqlwhere;
        List<Map<String,String>> dblist = dbHelper.selectSQL(sql, null);
        for (int i = 0; i < dblist.size(); i++){
            Map<String, String> m = dblist.get(i);
            TrSpecialBushing trSpecialBushing = new TrSpecialBushing();
            trSpecialBushing.setId(m.get("ID"));
            trSpecialBushing.setDetectionprojectid(m.get("DETECTIONPROJECTID"));
            trSpecialBushing.setDetectiondes(m.get("DETECTIONDES"));
            trSpecialBushing.setDetectionmethod(m.get("DETECTIONMETHOD"));
            trSpecialBushing.setDetectionstandard(m.get("DETECTIONSTANDARD"));
            trSpecialBushing.setDetectionexample(m.get("DETECTIONEXAMPLE"));
            trSpecialBushing.setStatus(m.get("STATUS"));
            trSpecialBushing.setResultdescribe(m.get("RESULTDESCRIBE"));
            trSpecialBushing.setRemarks(m.get("REMARKS"));
            trSpecialBushing.setUpdatetime(m.get("UPDATETIME"));
            trSpecialBushing.setUpdateperson(m.get("UPDATEPERSON"));
            list.add(trSpecialBushing);
        }
        return list;
    }

    public void insertListData(List<TrSpecialBushing> trSpecialBushingList)
    {
        if(trSpecialBushingList!=null&&trSpecialBushingList.size()>0)
        {
            List<String> listSql= new ArrayList<String>();
            for(int i=0;i<trSpecialBushingList.size();i++)
            {
                String sql="REPLACE INTO TR_SPECIAL_BUSHING (" +
                        "ID,DETECTIONPROJECTID,DETECTIONDES,DETECTIONMETHOD,DETECTIONSTANDARD,DETECTIONEXAMPLE,STATUS,RESULTDESCRIBE,"+
                        "REMARKS,UPDATETIME,UPDATEPERSON"+
                        ") VALUES (" +
                        "'"+trSpecialBushingList.get(i).getId()+"'," +
                        "'"+trSpecialBushingList.get(i).getDetectionprojectid()+"'," +
                        "'"+trSpecialBushingList.get(i).getDetectiondes()+"'," +
                        "'"+trSpecialBushingList.get(i).getDetectionmethod()+"'," +
                        "'"+trSpecialBushingList.get(i).getDetectionstandard()+"'," +
                        "'"+trSpecialBushingList.get(i).getDetectionexample()+"'," +
                        "'"+trSpecialBushingList.get(i).getStatus()+"'," +
                        "'"+trSpecialBushingList.get(i).getResultdescribe()+"'," +
                        "'"+trSpecialBushingList.get(i).getRemarks()+"'," +
                        "'"+trSpecialBushingList.get(i).getUpdatetime()+"'," +
                        "'"+trSpecialBushingList.get(i).getUpdateperson()+"'" +
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
     * 根据条件修改检测项目（套管）数据
     * @designer
     * @since  2014-11-6
     * @author dingwujun
     * @param @param List<PmDevice>
     * @param @return
     * @return null
     */
    public void updateTrSpecialBushingInfo(Map<Object,Object> columnsMap,Map<Object,Object> wheresMap)
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
            sql="UPDATE TR_SPECIAL_BUSHING SET " +columns +" WHERE 1=1 "+sqlWhere;

            if(sql!=null&&!sql.equals("")) {
                dbHelper.execSQL(sql);
            }
        }
    }

}
