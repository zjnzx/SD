package com.energyfuture.symphony.m3.dao;

import android.content.Context;

import com.energyfuture.symphony.m3.domain.TrSpecialBushingData;
import com.energyfuture.symphony.m3.util.DBHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/3/24.
 */
public class TrSpecialBushingDataDao {
    DBHelper dbHelper = null;

    public TrSpecialBushingDataDao(Context cxt) {
        dbHelper = new DBHelper(cxt);
    }

    public SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 获取检测项目（套管）数据表信息
     *
     * @param @param  null
     * @param @return
     * @return list
     * @designer
     * @author dingwujun
     * @since 2014-11-6
     */
    public List<TrSpecialBushingData> queryTrSpecialBushingDataList(TrSpecialBushingData trSpecialBushingDataParam) {
        List<TrSpecialBushingData> list = new ArrayList<TrSpecialBushingData>();
        String sqlwhere="";
        if(trSpecialBushingDataParam.getPositionid() != null && !trSpecialBushingDataParam.getPositionid().equals("")){
            sqlwhere += " AND POSITIONID='" + trSpecialBushingDataParam.getPositionid() + "'";
        }
        
        String sql = "SELECT * FROM TR_SPECIAL_BUSHING_DATA WHERE 1=1 "+sqlwhere + " ORDER BY ORDERMUNBER";
        List<Map<String,String>> dblist = dbHelper.selectSQL(sql, null);
        for (int i = 0; i < dblist.size(); i++){
            Map<String, String> m = dblist.get(i);
            TrSpecialBushingData trSpecialBushingData = new TrSpecialBushingData();
            trSpecialBushingData.setId(m.get("ID"));
            trSpecialBushingData.setPositionid(m.get("POSITIONID"));
            trSpecialBushingData.setResult(m.get("RESULT"));
            trSpecialBushingData.setOrdermunber(m.get("ORDERMUNBER"));
            trSpecialBushingData.setDescribetion(m.get("DESCRIBETION"));
            trSpecialBushingData.setUpdatetime(m.get("UPDATETIME"));
            trSpecialBushingData.setUpdateperson(m.get("UPDATEPERSON"));
            list.add(trSpecialBushingData);
        }
        return list;
    }

    public void insertListData(List<TrSpecialBushingData> trSpecialBushingDataList)
    {
        if(trSpecialBushingDataList!=null&&trSpecialBushingDataList.size()>0)
        {
            List<String> listSql= new ArrayList<String>();
            for(int i=0;i<trSpecialBushingDataList.size();i++)
            {
                String sql="REPLACE INTO TR_SPECIAL_BUSHING_DATA (" +
                        "ID,POSITIONID,RESULT,ORDERMUNBER,DESCRIBETION,UPDATETIME,UPDATEPERSON"+
                        ") VALUES (" +
                        "'"+trSpecialBushingDataList.get(i).getId()+"'," +
                        "'"+trSpecialBushingDataList.get(i).getPositionid()+"'," +
                        "'"+trSpecialBushingDataList.get(i).getResult()+"'," +
                        "'"+trSpecialBushingDataList.get(i).getOrdermunber()+"'," +
                        "'"+trSpecialBushingDataList.get(i).getDescribetion()+"'," +
                        "'"+trSpecialBushingDataList.get(i).getUpdatetime()+"'," +
                        "'"+trSpecialBushingDataList.get(i).getUpdateperson()+"'" +
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
     * 根据条件修改检测项目（套管）数据表数据
     * @designer
     * @since  2014-11-6
     * @author dingwujun
     * @param @param List<PmDevice>
     * @param @return
     * @return null
     */
    public void updateTrSpecialBushingDataInfo(Map<Object,Object> columnsMap,Map<Object,Object> wheresMap)
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
            sql="UPDATE TR_SPECIAL_BUSHING_DATA SET " +columns +" WHERE 1=1 "+sqlWhere;

            if(sql!=null&&!sql.equals("")) {
                dbHelper.execSQL(sql);
            }
        }
    }

}
