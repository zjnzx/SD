package com.energyfuture.symphony.m3.dao;

import android.content.Context;

import com.energyfuture.symphony.m3.domain.TrSpecialOliData;
import com.energyfuture.symphony.m3.util.DBHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/3/24.
 */
public class TrSpecialOliDataDao {
    DBHelper dbHelper = null;

    public TrSpecialOliDataDao(Context cxt) {
        dbHelper = new DBHelper(cxt);
    }

    public SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 获取检测项目（油色谱）数据表信息
     *
     * @param @param  null
     * @param @return
     * @return list
     * @designer
     * @author dingwujun
     * @since 2014-11-6
     */
    public List<TrSpecialOliData> queryTrSpecialOliDataList(TrSpecialOliData trSpecialOliDataParam) {
        List<TrSpecialOliData> list = new ArrayList<TrSpecialOliData>();
        String sqlwhere="";

        if(trSpecialOliDataParam.getDetectionprojectid() != null && !trSpecialOliDataParam.getDetectionprojectid().equals("")){
            sqlwhere+=" AND DETECTIONPROJECTID='"+trSpecialOliDataParam.getDetectionprojectid()+"'";
        }

        String sql = "SELECT * FROM TR_SPECIAL_OLI_DATA WHERE 1=1 "+sqlwhere + " ORDER BY ORDERBY";
        List<Map<String,String>> dblist = dbHelper.selectSQL(sql, null);
        for (int i = 0; i < dblist.size(); i++){
            Map<String, String> m = dblist.get(i);
            TrSpecialOliData trSpecialOliData = new TrSpecialOliData();
            trSpecialOliData.setId(m.get("ID"));
            trSpecialOliData.setDetectionprojectid(m.get("DETECTIONPROJECTID"));
            trSpecialOliData.setDetectionmethod(m.get("DETECTIONMETHOD"));
            trSpecialOliData.setDetectionstandard(m.get("DETECTIONSTANDARD"));
            trSpecialOliData.setDetectionexample(m.get("DETECTIONEXAMPLE"));
            trSpecialOliData.setStatus(m.get("STATUS"));
            trSpecialOliData.setRequired(m.get("REQUIRED"));
            trSpecialOliData.setDataformat(m.get("DATAFORMAT"));
            trSpecialOliData.setDataunit(m.get("DATAUNIT"));
            trSpecialOliData.setDetectiondes(m.get("DETECTIONDES"));
            trSpecialOliData.setResult1(m.get("RESULT1"));
            trSpecialOliData.setResult2(m.get("RESULT2"));
            trSpecialOliData.setResult3(m.get("RESULT3"));
            trSpecialOliData.setResultdescribe(m.get("RESULTDESCRIBE"));
            trSpecialOliData.setMobildisplay(m.get("MOBILDISPLAY"));
            trSpecialOliData.setRemarks(m.get("REMARKS"));
            trSpecialOliData.setUpdatetime(m.get("UPDATETIME"));
            trSpecialOliData.setUpdateperson(m.get("UPDATEPERSON"));
            trSpecialOliData.setOrderby(m.get("ORDERBY"));
            list.add(trSpecialOliData);
        }
        return list;
    }

    public void insertListData(List<TrSpecialOliData> trSpecialOliDataList)
    {
        if(trSpecialOliDataList!=null&&trSpecialOliDataList.size()>0)
        {
            List<String> listSql= new ArrayList<String>();
            for(int i=0;i<trSpecialOliDataList.size();i++)
            {
                String sql="REPLACE INTO TR_SPECIAL_OLI_DATA (" +
                        "ID,DETECTIONPROJECTID,DETECTIONMETHOD,DETECTIONSTANDARD,DETECTIONEXAMPLE,STATUS,REQUIRED,DATAFORMAT,DATAUNIT,"+
                        "DETECTIONDES,RESULT1,RESULT2,RESULT3,RESULTDESCRIBE,MOBILDISPLAY,REMARKS,ORDERBY,UPDATETIME,UPDATEPERSON"+
                        ") VALUES (" +
                        "'"+trSpecialOliDataList.get(i).getId()+"'," +
                        "'"+trSpecialOliDataList.get(i).getDetectionprojectid()+"'," +
                        "'"+trSpecialOliDataList.get(i).getDetectionmethod()+"'," +
                        "'"+trSpecialOliDataList.get(i).getDetectionstandard()+"'," +
                        "'"+trSpecialOliDataList.get(i).getDetectionexample()+"'," +
                        "'"+trSpecialOliDataList.get(i).getStatus()+"'," +
                        "'"+trSpecialOliDataList.get(i).getRequired()+"'," +
                        "'"+trSpecialOliDataList.get(i).getDataformat()+"'," +
                        "'"+trSpecialOliDataList.get(i).getDataunit()+"'," +
                        "'"+trSpecialOliDataList.get(i).getDetectiondes()+"'," +
                        "'"+trSpecialOliDataList.get(i).getResult1()+"'," +
                        "'"+trSpecialOliDataList.get(i).getResult2()+"'," +
                        "'"+trSpecialOliDataList.get(i).getResult3()+"'," +
                        "'"+trSpecialOliDataList.get(i).getResultdescribe()+"'," +
                        "'"+trSpecialOliDataList.get(i).getMobildisplay()+"'," +
                        "'"+trSpecialOliDataList.get(i).getRemarks()+"'," +
                        "'"+trSpecialOliDataList.get(i).getOrderby()+"'," +
                        "'"+trSpecialOliDataList.get(i).getUpdatetime()+"'," +
                        "'"+trSpecialOliDataList.get(i).getUpdateperson()+"'" +
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
     * 根据条件修改检测项目（油色谱）数据表数据
     * @designer
     * @since  2014-11-6
     * @author dingwujun
     * @param @param List<PmDevice>
     * @param @return
     * @return null
     */
    public void updateTrSpecialOliDataInfo(Map<Object,Object> columnsMap,Map<Object,Object> wheresMap)
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
            sql="UPDATE TR_SPECIAL_OLI_DATA SET " +columns +" WHERE 1=1 "+sqlWhere;

            if(sql!=null&&!sql.equals("")) {
                dbHelper.execSQL(sql);
            }
        }
    }

}
