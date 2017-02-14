package com.energyfuture.symphony.m3.dao;

import android.content.Context;
import com.energyfuture.symphony.m3.domain.TrDetectionTemplet;
import com.energyfuture.symphony.m3.util.DBHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/3/24.
 */
public class TrDetectionTempletDao {
    DBHelper dbHelper = null;

    public TrDetectionTempletDao(Context cxt) {
        dbHelper = new DBHelper(cxt);
    }

    public SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 获取检测项目模板表信息
     *
     * @param @param  null
     * @param @return
     * @return list
     * @designer
     * @author dingwujun
     * @since 2014-11-6
     */
    public List<TrDetectionTemplet> queryTrDetectionTempletList(TrDetectionTemplet trDetectionTempletParam) {
        List<TrDetectionTemplet> list = new ArrayList<TrDetectionTemplet>();
        String sqlwhere="";

        if(trDetectionTempletParam.getId()!=null&&!trDetectionTempletParam.getId().equals("")){
            sqlwhere+="AND ID='"+trDetectionTempletParam.getId()+"'";
        }

        String sql = "SELECT * FROM TR_DETECTION_TEMPLET WHERE 1=1 "+sqlwhere;
        List<Map<String,String>> dblist = dbHelper.selectSQL(sql, null);
        for (int i = 0; i < dblist.size(); i++){
            Map<String, String> m = dblist.get(i);
            TrDetectionTemplet trDetectionTemplet = new TrDetectionTemplet();
            trDetectionTemplet.setId(m.get("ID"));
            trDetectionTemplet.setDetectionmethod(m.get("DETECTIONMETHOD"));
            trDetectionTemplet.setDetectionstandard(m.get("DETECTIONSTANDARD"));
            trDetectionTemplet.setDetectionexample(m.get("DETECTIONEXAMPLE"));
            trDetectionTemplet.setRemarks(m.get("REMARKS"));
            trDetectionTemplet.setUpdatetime(m.get("UPDATETIME"));
            trDetectionTemplet.setUpdateperson(m.get("UPDATEPERSON"));
            trDetectionTemplet.setTemplettype(m.get("TEMPLETTYPE"));
            list.add(trDetectionTemplet);
        }
        return list;
    }

    public void insertListData(List<TrDetectionTemplet> trDetectionTempletList)
    {
        if(trDetectionTempletList!=null&&trDetectionTempletList.size()>0)
        {
            List<String> listSql= new ArrayList<String>();
            for(int i=0;i<trDetectionTempletList.size();i++)
            {
                String sql="REPLACE INTO TR_DETECTION_TEMPLET (" +
                        "ID,DETECTIONMETHOD,DETECTIONSTANDARD,DETECTIONEXAMPLE,REMARKS,UPDATETIME,UPDATEPERSON,TEMPLETTYPE"+
                        ") VALUES (" +
                        "'"+trDetectionTempletList.get(i).getId()+"'," +
                        "'"+trDetectionTempletList.get(i).getDetectionmethod()+"'," +
                        "'"+trDetectionTempletList.get(i).getDetectionstandard()+"'," +
                        "'"+trDetectionTempletList.get(i).getDetectionexample()+"'," +
                        "'"+trDetectionTempletList.get(i).getRemarks()+"'," +
                        "'"+trDetectionTempletList.get(i).getUpdatetime()+"'," +
                        "'"+trDetectionTempletList.get(i).getUpdateperson()+"'," +
                        "'"+trDetectionTempletList.get(i).getTemplettype()+"'" +
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
     * 根据条件修改检测项目模板表数据
     * @designer
     * @since  2014-11-6
     * @author dingwujun
     * @param @param List<PmDevice>
     * @param @return
     * @return null
     */
    public void updateTrDetectionTempletInfo(Map<Object,Object> columnsMap,Map<Object,Object> wheresMap)
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
            sql="UPDATE TR_DETECTION_TEMPLET SET " +columns +" WHERE 1=1 "+sqlWhere;

            if(sql!=null&&!sql.equals("")) {
                dbHelper.execSQL(sql);
            }
        }
    }

}
