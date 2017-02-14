package com.energyfuture.symphony.m3.dao;

import android.content.Context;

import com.energyfuture.symphony.m3.domain.TrDetectiontypEtempletObj;
import com.energyfuture.symphony.m3.util.DBHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/3/24.
 */
public class TrDetectionTypeTempletObjDao {
    DBHelper dbHelper = null;

    public TrDetectionTypeTempletObjDao(Context cxt) {
        dbHelper = new DBHelper(cxt);
    }

    public SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 获取检测项目与检测模板的关联关系实体表信息
     *
     * @param @param  null
     * @param @return
     * @return list
     * @designer
     * @author dingwujun
     * @since 2014-11-6
     */
    public List<TrDetectiontypEtempletObj> queryTrDetectionTypeTempletObjList(TrDetectiontypEtempletObj trDetectionTempletTypeObjParam) {
        List<TrDetectiontypEtempletObj> list = new ArrayList<TrDetectiontypEtempletObj>();
        String sqlwhere="";
        if(trDetectionTempletTypeObjParam.getTaskid() != null && !trDetectionTempletTypeObjParam.getTaskid().equals("")){
            sqlwhere += " AND TASKID='" + trDetectionTempletTypeObjParam.getTaskid() + "'";
        }

        if(trDetectionTempletTypeObjParam.getPid()!= null && !trDetectionTempletTypeObjParam.getPid().equals("")){
            sqlwhere += " AND PID='" + trDetectionTempletTypeObjParam.getPid() + "'";
        }
        if(trDetectionTempletTypeObjParam.getRid()!= null && !trDetectionTempletTypeObjParam.getRid().equals("")){
            sqlwhere += " AND PID='" + trDetectionTempletTypeObjParam.getRid() + "'";
        }
        if(trDetectionTempletTypeObjParam.getTemplettype()!=null&&trDetectionTempletTypeObjParam.getTemplettype().equals("index")){
            sqlwhere += " AND TEMPLETTYPE=''or TEMPLETTYPE=null";
        }

        String sql = "SELECT * FROM TR_DETECTIONTYP_ETEMPLET_OBJ WHERE 1=1 "+sqlwhere + " AND STATUS<>'2' ORDER BY ORDERBY ASC";
        List<Map<String,String>> dblist = dbHelper.selectSQL(sql, null);
        for (int i = 0; i < dblist.size(); i++){
            Map<String, String> m = dblist.get(i);
            TrDetectiontypEtempletObj trDetectiontypEtempletObj = new TrDetectiontypEtempletObj();
            trDetectiontypEtempletObj.setId(m.get("ID"));
            trDetectiontypEtempletObj.setDetectionname(m.get("DETECTIONNAME"));
            trDetectiontypEtempletObj.setPid(m.get("PID"));
            trDetectiontypEtempletObj.setRid(m.get("RID"));
            trDetectiontypEtempletObj.setTaskid(m.get("TASKID"));
            trDetectiontypEtempletObj.setDetectiontemplet(m.get("DETECTIONTEMPLET"));
            trDetectiontypEtempletObj.setTemplettype(m.get("TEMPLETTYPE"));
            trDetectiontypEtempletObj.setStatus(m.get("STATUS"));
            trDetectiontypEtempletObj.setOrderby(m.get("ORDERBY"));
            trDetectiontypEtempletObj.setPiccount((m.get("PICCOUNT")==null||m.get("PICCOUNT").equals("null"))?"0":m.get("PICCOUNT"));
            list.add(trDetectiontypEtempletObj);
        }
        return list;
    }

    public void insertListData(List<TrDetectiontypEtempletObj> trDetectionTypeTempletObjList)
    {
        if(trDetectionTypeTempletObjList!=null&&trDetectionTypeTempletObjList.size()>0)
        {
            List<String> listSql= new ArrayList<String>();
            for(int i=0;i<trDetectionTypeTempletObjList.size();i++)
            {
                String sql="REPLACE INTO TR_DETECTIONTYP_ETEMPLET_OBJ (" +
                        "ID,DETECTIONNAME,PID,TASKID,TEMPLETTYPE,STATUS,ORDERBY,PICCOUNT,DETECTIONTEMPLET"+
                        ") VALUES (" +
                        "'"+trDetectionTypeTempletObjList.get(i).getId()+"'," +
                        "'"+trDetectionTypeTempletObjList.get(i).getDetectionname()+"'," +
                        "'"+trDetectionTypeTempletObjList.get(i).getPid()+"'," +
                        "'"+trDetectionTypeTempletObjList.get(i).getTaskid()+"'," +
                        "'"+trDetectionTypeTempletObjList.get(i).getTemplettype()+"'," +
                        "'"+trDetectionTypeTempletObjList.get(i).getStatus()+"'," +
                        "'"+trDetectionTypeTempletObjList.get(i).getOrderby()+"'," +
                        "'"+trDetectionTypeTempletObjList.get(i).getPiccount()+"'," +
                        "'"+trDetectionTypeTempletObjList.get(i).getDetectiontemplet()+"'" +
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
     * 根据条件修改检测项目与检测模板的关联关系实体表数据
     * @designer
     * @since  2014-11-6
     * @author dingwujun
     * @param @param List<PmDevice>
     * @param @return
     * @return null
     */
    public void updateTrDetectionTypeTempletObjInfo(Map<Object,Object> columnsMap,Map<Object,Object> wheresMap)
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
            sql="UPDATE TR_DETECTIONTYP_ETEMPLET_OBJ SET " +columns +" WHERE 1=1 "+sqlWhere;

            if(sql!=null&&!sql.equals("")) {
                dbHelper.execSQL(sql);
            }
        }
    }

}
