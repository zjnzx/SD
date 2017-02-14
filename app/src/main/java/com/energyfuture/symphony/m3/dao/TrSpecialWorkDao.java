package com.energyfuture.symphony.m3.dao;

import android.content.Context;

import com.energyfuture.symphony.m3.domain.TrSpecialWork;
import com.energyfuture.symphony.m3.util.DBHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/3/24.
 */
public class TrSpecialWorkDao {
    DBHelper dbHelper = null;

    public TrSpecialWorkDao(Context cxt) {
        dbHelper = new DBHelper(cxt);
    }

    public SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 获取工作记录表信息
     *
     * @param @param  null
     * @param @return
     * @return list
     * @designer
     * @author dingwujun
     * @since 2014-11-6
     */
    public List<TrSpecialWork> queryTrSpecialWorkList(TrSpecialWork trSpecialWorkParam) {
        List<TrSpecialWork> list = new ArrayList<TrSpecialWork>();
        String sqlwhere="";

        if(trSpecialWorkParam.getDetectionprojectid() != null && !trSpecialWorkParam.getDetectionprojectid().equals("")){
            sqlwhere+=" AND DETECTIONPROJECTID='"+trSpecialWorkParam.getDetectionprojectid()+"'";
        }

        if(trSpecialWorkParam.getId() != null && !trSpecialWorkParam.getId().equals("")){
            sqlwhere+=" AND ID='"+trSpecialWorkParam.getId()+"'";
        }

        String sql = "SELECT * FROM TR_SPECIAL_WORK WHERE 1=1 "+sqlwhere;
        List<Map<String,String>> dblist = dbHelper.selectSQL(sql, null);
        for (int i = 0; i < dblist.size(); i++){
            Map<String, String> m = dblist.get(i);
            TrSpecialWork trSpecialWork = new TrSpecialWork();
            trSpecialWork.setId(m.get("ID"));
            trSpecialWork.setDetectionprojectid(m.get("DETECTIONPROJECTID"));
            trSpecialWork.setDetectiondes(m.get("DETECTIONDES"));
            trSpecialWork.setDetectiondate(m.get("DETECTIONDATE"));
            trSpecialWork.setDetectiontime(m.get("DETECTIONTIME"));
            trSpecialWork.setDetectioncondition(m.get("DETECTIONCONDITION"));
            trSpecialWork.setTemp(m.get("TEMP"));
            trSpecialWork.setRadiation(m.get("RADIATION"));
            trSpecialWork.setLoads(m.get("LOADS"));
            trSpecialWork.setDetectiondeperson(m.get("DETECTIONDEPERSON"));
            trSpecialWork.setUpdatetime(m.get("UPDATETIME"));
            trSpecialWork.setUpdateperson(m.get("UPDATEPERSON"));
            trSpecialWork.setRemarks(m.get("REMARKS"));

            list.add(trSpecialWork);
        }
        return list;
    }

    public void insertListData(List<TrSpecialWork> trSpecialWorkList)
    {
        if(trSpecialWorkList!=null&&trSpecialWorkList.size()>0)
        {
            List<String> listSql= new ArrayList<String>();
            for(int i=0;i<trSpecialWorkList.size();i++)
            {
                String sql="REPLACE INTO TR_SPECIAL_WORK (" +
                        "ID,DETECTIONPROJECTID,DETECTIONDES,DETECTIONDATE,DETECTIONTIME,DETECTIONCONDITION,TEMP,RADIATION,LOADS,"+
                        "DETECTIONDEPERSON,UPDATETIME,UPDATEPERSON,REMARKS"+
                        ") VALUES (" +
                        "'"+trSpecialWorkList.get(i).getId()+"'," +
                        "'"+trSpecialWorkList.get(i).getDetectionprojectid()+"'," +
                        "'"+trSpecialWorkList.get(i).getDetectiondes()+"'," +
                        "'"+trSpecialWorkList.get(i).getDetectiondate()+"'," +
                        "'"+trSpecialWorkList.get(i).getDetectiontime()+"'," +
                        "'"+trSpecialWorkList.get(i).getDetectioncondition()+"'," +
                        "'"+trSpecialWorkList.get(i).getTemp()+"'," +
                        "'"+trSpecialWorkList.get(i).getRadiation()+"'," +
                        "'"+trSpecialWorkList.get(i).getLoads()+"'," +
                        "'"+trSpecialWorkList.get(i).getDetectiondeperson()+"'," +
                        "'"+trSpecialWorkList.get(i).getUpdatetime()+"'," +
                        "'"+trSpecialWorkList.get(i).getUpdateperson()+"'" +
                        "'"+trSpecialWorkList.get(i).getRemarks()+"'" +
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
     * 根据条件修改工作记录表数据
     * @designer
     * @since  2014-11-6
     * @author dingwujun
     * @param @param List<PmDevice>
     * @param @return
     * @return null
     */
    public void updateTrSpecialWorkInfo(Map<Object,Object> columnsMap,Map<Object,Object> wheresMap)
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
            sql="UPDATE TR_SPECIAL_WORK SET " +columns +" WHERE 1=1 "+sqlWhere;

            if(sql!=null&&!sql.equals("")) {
                dbHelper.execSQL(sql);
            }
        }
    }

}
