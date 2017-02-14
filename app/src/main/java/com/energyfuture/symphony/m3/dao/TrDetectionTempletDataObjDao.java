package com.energyfuture.symphony.m3.dao;

import android.content.Context;

import com.energyfuture.symphony.m3.domain.TrDetectiontempletDataObj;
import com.energyfuture.symphony.m3.util.DBHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/3/24.
 */
public class TrDetectionTempletDataObjDao {
    DBHelper dbHelper = null;

    public TrDetectionTempletDataObjDao(Context cxt) {
        dbHelper = new DBHelper(cxt);
    }

    public SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 获取检测项目模板（输入项）实体表信息
     *
     * @param @param  null
     * @param @return
     * @return list
     * @designer
     * @author dingwujun
     * @since 2014-11-6
     */
    public List<TrDetectiontempletDataObj> queryTrDetectionTempletDataObjList(TrDetectiontempletDataObj trDetectionTempletDataObjParam) {
        List<TrDetectiontempletDataObj> list = new ArrayList<TrDetectiontempletDataObj>();
        String sqlwhere="";

        if(trDetectionTempletDataObjParam.getDetectionobjid()!=null&&!trDetectionTempletDataObjParam.getDetectionobjid().equals(""))
        {
            sqlwhere+=" AND DETECTIONOBJID='"+trDetectionTempletDataObjParam.getDetectionobjid()+"'";
        }
        
        String sql = "SELECT * FROM TR_DETECTIONTEMPLET_DATA_OBJ WHERE 1=1 "+sqlwhere+"ORDER BY ORDERMUNBER ASC";
        List<Map<String,String>> dblist = dbHelper.selectSQL(sql, null);
        for (int i = 0; i < dblist.size(); i++){
            Map<String, String> m = dblist.get(i);
            TrDetectiontempletDataObj trDetectionTempletDataObj = new TrDetectiontempletDataObj();
            trDetectionTempletDataObj.setId(m.get("ID"));
            trDetectionTempletDataObj.setDetectionobjid(m.get("DETECTIONOBJID"));
            trDetectionTempletDataObj.setRequired(m.get("REQUIRED"));
            trDetectionTempletDataObj.setDataformat(m.get("DATAFORMAT"));
            trDetectionTempletDataObj.setDataunit(m.get("DATAUNIT"));
            trDetectionTempletDataObj.setUpdatetime(m.get("UPDATETIME"));
            trDetectionTempletDataObj.setUpdateperson(m.get("UPDATEPERSON"));
            trDetectionTempletDataObj.setDescribetion(m.get("DESCRIBETION"));
            trDetectionTempletDataObj.setOrdermunber(m.get("ORDERMUNBER"));
            trDetectionTempletDataObj.setResult(m.get("RESULT"));
            trDetectionTempletDataObj.setResultdescribe(m.get("RESULTDESCRIBE"));
            list.add(trDetectionTempletDataObj);
        }
        return list;
    }

    public void insertListData(List<TrDetectiontempletDataObj> trDetectionTempletDataObjList)
    {
        if(trDetectionTempletDataObjList!=null&&trDetectionTempletDataObjList.size()>0)
        {
            List<String> listSql= new ArrayList<String>();
            for(int i=0;i<trDetectionTempletDataObjList.size();i++)
            {
                String sql="REPLACE INTO TR_DETECTIONTEMPLET_DATA_OBJ (" +
                        "ID,DETECTIONOBJID,REQUIRED,DATAFORMAT,DATAUNIT,UPDATETIME,UPDATEPERSON,DESCRIBETION,ORDERMUNBER,RESULT,RESULTDESCRIBE"+
                        ") VALUES (" +
                        "'"+trDetectionTempletDataObjList.get(i).getId()+"'," +
                        "'"+trDetectionTempletDataObjList.get(i).getDetectionobjid()+"'," +
                        "'"+trDetectionTempletDataObjList.get(i).getRequired()+"'," +
                        "'"+trDetectionTempletDataObjList.get(i).getDataformat()+"'," +
                        "'"+trDetectionTempletDataObjList.get(i).getDataunit()+"'," +
                        "'"+trDetectionTempletDataObjList.get(i).getUpdatetime()+"'," +
                        "'"+trDetectionTempletDataObjList.get(i).getUpdateperson()+"'," +
                        "'"+trDetectionTempletDataObjList.get(i).getDescribetion()+"'," +
                        "'"+trDetectionTempletDataObjList.get(i).getOrdermunber()+"'," +
                        "'"+trDetectionTempletDataObjList.get(i).getResult()+"'," +
                        "'"+trDetectionTempletDataObjList.get(i).getResultdescribe()+"'" +
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
     * 根据条件修改检测项目模板（输入项）实体表数据
     * @designer
     * @since  2014-11-6
     * @author dingwujun
     * @param @param List<PmDevice>
     * @param @return
     * @return null
     */
    public void updateTrDetectionTempletDataObjInfo(Map<Object,Object> columnsMap,Map<Object,Object> wheresMap)
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
            sql="UPDATE TR_DETECTIONTEMPLET_DATA_OBJ SET " +columns +" WHERE 1=1 "+sqlWhere;

            if(sql!=null&&!sql.equals("")) {
                dbHelper.execSQL(sql);
            }
        }
    }
}
