package com.energyfuture.symphony.m3.dao;

import android.content.Context;

import com.energyfuture.symphony.m3.domain.TrDetectiontempletData;
import com.energyfuture.symphony.m3.util.DBHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/3/24.
 */
public class TrDetectionTempletDataDao {
    DBHelper dbHelper = null;

    public TrDetectionTempletDataDao(Context cxt) {
        dbHelper = new DBHelper(cxt);
    }

    public SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 获取检测项目模板（输入项）表信息
     *
     * @param @param  null
     * @param @return
     * @return list
     * @designer
     * @author dingwujun
     * @since 2014-11-6
     */
    public List<TrDetectiontempletData> queryTrDetectionTempletDataList(TrDetectiontempletData trDetectionTempletDataParam) {
        List<TrDetectiontempletData> list = new ArrayList<TrDetectiontempletData>();
        String sqlwhere="";
        
        String sql = "SELECT * FROM TR_DETECTIONTEMPLET_DATA WHERE 1=1 "+sqlwhere;
        List<Map<String,String>> dblist = dbHelper.selectSQL(sql, null);
        for (int i = 0; i < dblist.size(); i++){
            Map<String, String> m = dblist.get(i);
            TrDetectiontempletData trDetectionTempletData = new TrDetectiontempletData();
            trDetectionTempletData.setId(m.get("ID"));
            trDetectionTempletData.setTempletid(m.get("TEMPLETID"));
            trDetectionTempletData.setRequired(m.get("REQUIRED"));
            trDetectionTempletData.setDataformat(m.get("DATAFORMAT"));
            trDetectionTempletData.setDataunit(m.get("DATAUNIT"));
            trDetectionTempletData.setUpdatetime(m.get("UPDATETIME"));
            trDetectionTempletData.setUpdateperson(m.get("UPDATEPERSON"));
            trDetectionTempletData.setDescribetion(m.get("DESCRIBETION"));
            trDetectionTempletData.setOrdermunber(m.get("ORDERMUNBER"));
            list.add(trDetectionTempletData);
        }
        return list;
    }

    public void insertListData(List<TrDetectiontempletData> trDetectionTempletDataList)
    {
        if(trDetectionTempletDataList!=null&&trDetectionTempletDataList.size()>0)
        {
            List<String> listSql= new ArrayList<String>();
            for(int i=0;i<trDetectionTempletDataList.size();i++)
            {
                String sql="REPLACE INTO TR_DETECTIONTEMPLET_DATA (" +
                        "ID,TEMPLETID,REQUIRED,DATAFORMAT,DATAUNIT,UPDATETIME,UPDATEPERSON,DESCRIBETION,ORDERMUNBER"+
                        ") VALUES (" +
                        "'"+trDetectionTempletDataList.get(i).getId()+"'," +
                        "'"+trDetectionTempletDataList.get(i).getTempletid()+"'," +
                        "'"+trDetectionTempletDataList.get(i).getRequired()+"'," +
                        "'"+trDetectionTempletDataList.get(i).getDataformat()+"'," +
                        "'"+trDetectionTempletDataList.get(i).getDataunit()+"'," +
                        "'"+trDetectionTempletDataList.get(i).getUpdatetime()+"'," +
                        "'"+trDetectionTempletDataList.get(i).getUpdateperson()+"'," +
                        "'"+trDetectionTempletDataList.get(i).getDescribetion()+"'," +
                        "'"+trDetectionTempletDataList.get(i).getOrdermunber()+"'" +
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
     * 根据条件修改检测项目模板（输入项）表数据
     * @designer
     * @since  2014-11-6
     * @author dingwujun
     * @param @param List<PmDevice>
     * @param @return
     * @return null
     */
    public void updateTrDetectionTempletDataInfo(Map<Object,Object> columnsMap,Map<Object,Object> wheresMap)
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
            sql="UPDATE TR_DETECTIONTEMPLET_DATA SET " +columns +" WHERE 1=1 "+sqlWhere;

            if(sql!=null&&!sql.equals("")) {
                dbHelper.execSQL(sql);
            }
        }
    }

}
