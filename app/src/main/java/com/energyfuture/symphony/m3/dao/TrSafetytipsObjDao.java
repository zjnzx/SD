package com.energyfuture.symphony.m3.dao;


import android.content.Context;

import com.energyfuture.symphony.m3.domain.TrSafetytipsObj;
import com.energyfuture.symphony.m3.util.DBHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/3/24.
 */
public class TrSafetytipsObjDao {
    DBHelper dbHelper = null;

    public TrSafetytipsObjDao(Context cxt) {
        dbHelper = new DBHelper(cxt);
    }

    public SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 获取安全须知实体表信息
     *
     * @param @param  null
     * @param @return
     * @return list
     * @designer
     * @author dingwujun
     * @since 2014-11-6
     */
    public List<TrSafetytipsObj> queryTrSafetytipsObjList(TrSafetytipsObj trSafetytipsObjParam) {
        List<TrSafetytipsObj> list = new ArrayList<TrSafetytipsObj>();
        String sqlwhere="";
        if(trSafetytipsObjParam.getSafetype() != null && !trSafetytipsObjParam.getSafetype().equals("")){
            sqlwhere += " AND SAFETYPE='" + trSafetytipsObjParam.getSafetype() + "'";
        }
        if(trSafetytipsObjParam.getTaskid() != null && !trSafetytipsObjParam.getTaskid().equals("")){
            sqlwhere += " AND TASKID='" + trSafetytipsObjParam.getTaskid() + "'";
        }
        if(trSafetytipsObjParam.getUpdatetime() != null && !trSafetytipsObjParam.getUpdatetime().equals("")){
            sqlwhere += " AND UPDATETIME='" + trSafetytipsObjParam.getUpdatetime() + "'";
        }
        if(trSafetytipsObjParam.getPersion() != null && !trSafetytipsObjParam.getPersion().equals("")){
            sqlwhere += " AND PERSION='" + trSafetytipsObjParam.getPersion() + "'";
        }

        String sql = "SELECT * FROM TR_SAFETYTIPS_OBJ WHERE 1=1 "+sqlwhere;
        List<Map<String,String>> dblist = dbHelper.selectSQL(sql, null);
        for (int i = 0; i < dblist.size(); i++){
            Map<String, String> m = dblist.get(i);
            TrSafetytipsObj trSafetytipsObj = new TrSafetytipsObj();
            trSafetytipsObj.setId(m.get("ID"));
            trSafetytipsObj.setEndangername(m.get("ENDANGERNAME"));
            trSafetytipsObj.setEndangerrang(m.get("ENDANGERRANG"));
            trSafetytipsObj.setRiiskrang(m.get("RiISKRANG"));
            trSafetytipsObj.setResult(m.get("RESULT"));
            trSafetytipsObj.setGrade(m.get("GRADE"));
            trSafetytipsObj.setPrecontrolmethod(m.get("PRECONTROLMETHOD"));
            trSafetytipsObj.setMethod(m.get("METHOD"));
            trSafetytipsObj.setContinuelevel(m.get("CONTINUELEVEL"));
            trSafetytipsObj.setRemarks(m.get("REMARKS"));
            trSafetytipsObj.setSafetype(m.get("SAFETYPE"));
            trSafetytipsObj.setTaskid(m.get("TASKID"));
            trSafetytipsObj.setOrdermunber(m.get("ORDERMUNBER"));
            trSafetytipsObj.setPersion(m.get("PERSION"));
            trSafetytipsObj.setUpdatetime(m.get("UPDATETIME"));
            list.add(trSafetytipsObj);
        }
        return list;
    }

    public void insertListData(List<TrSafetytipsObj> trSafetytipsObjList)
    {
        if(trSafetytipsObjList!=null&&trSafetytipsObjList.size()>0)
        {
            List<String> listSql= new ArrayList<String>();
            for(int i=0;i<trSafetytipsObjList.size();i++)
            {
                String sql="REPLACE INTO TR_SAFETYTIPS_OBJ (" +
                        "ID,ENDANGERNAME,ENDANGERRANG,RiISKRANG,RESULT,GRADE,PRECONTROLMETHOD,METHOD,CONTINUELEVEL,REMARKS,"+
                        "SAFETYPE,TASKID,ORDERMUNBER,PERSION,UPDATETIME"+
                        ") VALUES (" +
                        "'"+trSafetytipsObjList.get(i).getId()+"'," +
                        "'"+trSafetytipsObjList.get(i).getEndangername()+"'," +
                        "'"+trSafetytipsObjList.get(i).getEndangerrang()+"'," +
                        "'"+trSafetytipsObjList.get(i).getRiiskrang()+"'," +
                        "'"+trSafetytipsObjList.get(i).getResult()+"'," +
                        "'"+trSafetytipsObjList.get(i).getGrade()+"'," +
                        "'"+trSafetytipsObjList.get(i).getPrecontrolmethod()+"'," +
                        "'"+trSafetytipsObjList.get(i).getMethod()+"'," +
                        "'"+trSafetytipsObjList.get(i).getContinuelevel()+"'," +
                        "'"+trSafetytipsObjList.get(i).getRemarks()+"'," +
                        "'"+trSafetytipsObjList.get(i).getSafetype()+"'," +
                        "'"+trSafetytipsObjList.get(i).getTaskid()+"'," +
                        "'"+trSafetytipsObjList.get(i).getOrdermunber()+"'," +
                        "'"+trSafetytipsObjList.get(i).getPersion()+"'," +
                        "'"+trSafetytipsObjList.get(i).getUpdatetime()+"'" +
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
     * 根据条件修改全须知实体表数据
     * @designer
     * @since  2014-11-6
     * @author dingwujun
     * @param @param List<PmDevice>
     * @param @return
     * @return null
     */
    public void updateTrSafetytipsObjInfo(Map<Object,Object> columnsMap,Map<Object,Object> wheresMap)
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
            sql="UPDATE TR_SAFETYTIPS_OBJ SET " +columns +" WHERE 1=1 "+sqlWhere;

            if(sql!=null&&!sql.equals(""))
            {
                dbHelper.execSQL(sql);
            }
        }
    }

}
