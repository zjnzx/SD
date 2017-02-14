package com.energyfuture.symphony.m3.dao;

import android.content.Context;

import com.energyfuture.symphony.m3.domain.TrDetectiontypeTemplet;
import com.energyfuture.symphony.m3.util.DBHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/3/24.
 */
public class TrDetectionTypeTempletDao {
    DBHelper dbHelper = null;

    public TrDetectionTypeTempletDao(Context cxt) {
        dbHelper = new DBHelper(cxt);
    }

    public SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 获取检测项目与检测模板关联模板表信息
     *
     * @param @param  null
     * @param @return
     * @return list
     * @designer
     * @author dingwujun
     * @since 2014-11-6
     */
//    public List<TrDetectiontypeTemplet> queryTrDetectionTypeTempletList(TrDetectiontypeTemplet trDetectionTempletTypeParam) {
//        List<TrDetectiontypeTemplet> list = new ArrayList<TrDetectiontypeTemplet>();
//        String sqlwhere="";
//
//        String sql = "SELECT * FROM TR_DETECTIONTYPE_TEMPLET WHERE 1=1 "+sqlwhere;
//        List<Map<String,String>> dblist = dbHelper.selectSQL(sql, null);
//        for (int i = 0; i < dblist.size(); i++){
//            Map<String, String> m = dblist.get(i);
//            TrDetectiontypeTemplet trDetectionTypeTemplet = new TrDetectiontypeTemplet();
//            trDetectionTypeTemplet.setId(m.get("ID"));
//            trDetectionTypeTemplet.setDetectionname(m.get("DETECTIONNAME"));
//            trDetectionTypeTemplet.setPid(m.get("PID"));
//            trDetectionTypeTemplet.setDetectiontempletid(m.get("DETECTIONTEMPLETID"));
//            trDetectionTypeTemplet.setUpdatetime(m.get("UPDATETIME"));
//            trDetectionTypeTemplet.setUpdateperson(m.get("UPDATEPERSON"));
//            trDetectionTypeTemplet.setOrderby(m.get("ORDERBY"));
//            trDetectionTypeTemplet.setPiccount(Integer.parseInt(m.get("PICCOUNT")));
//            list.add(trDetectionTypeTemplet);
//        }
//        return list;
//    }

    public void insertListData(List<TrDetectiontypeTemplet> trDetectionTypeTempletList)
    {
        if(trDetectionTypeTempletList!=null&&trDetectionTypeTempletList.size()>0)
        {
            List<String> listSql= new ArrayList<String>();
            for(int i=0;i<trDetectionTypeTempletList.size();i++)
            {
                String sql="REPLACE INTO TR_DETECTIONTYPE_TEMPLET (" +
                        "ID,DETECTIONNAME,PID,DETECTIONTEMPLETID,UPDATETIME,ORDERBY,PICCOUNT,UPDATEPERSON"+
                        ") VALUES (" +
                        "'"+trDetectionTypeTempletList.get(i).getId()+"'," +
                        "'"+trDetectionTypeTempletList.get(i).getDetectionname()+"'," +
                        "'"+trDetectionTypeTempletList.get(i).getPid()+"'," +
                        "'"+trDetectionTypeTempletList.get(i).getDetectiontempletid()+"'," +
                        "'"+trDetectionTypeTempletList.get(i).getUpdatetime()+"'," +
                        "'"+trDetectionTypeTempletList.get(i).getOrderby()+"'," +
                        "'"+trDetectionTypeTempletList.get(i).getPiccount()+"'," +
                        "'"+trDetectionTypeTempletList.get(i).getUpdateperson()+"'" +
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
     * 根据条件修改检测项目与检测模板关联模板表数据
     * @designer
     * @since  2014-11-6
     * @author dingwujun
     * @param @param List<PmDevice>
     * @param @return
     * @return null
     */
    public void updateTrDetectionTypeTempletInfo(Map<Object,Object> columnsMap,Map<Object,Object> wheresMap)
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
            sql="UPDATE TR_DETECTIONTYPE_TEMPLET SET " +columns +" WHERE 1=1 "+sqlWhere;

            if(sql!=null&&!sql.equals("")) {
                dbHelper.execSQL(sql);
            }
        }
    }

}
