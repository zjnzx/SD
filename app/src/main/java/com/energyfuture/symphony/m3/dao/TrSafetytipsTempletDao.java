package com.energyfuture.symphony.m3.dao;

import android.content.Context;

import com.energyfuture.symphony.m3.domain.TrSafetytipsTemplet;
import com.energyfuture.symphony.m3.util.DBHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/3/24.
 */
public class TrSafetytipsTempletDao {
    DBHelper dbHelper = null;

    public TrSafetytipsTempletDao(Context cxt) {
        dbHelper = new DBHelper(cxt);
    }

    public SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 获取安全须知模板表信息
     *
     * @param @param  null
     * @param @return
     * @return list
     * @designer
     * @author dingwujun
     * @since 2014-11-6
     */
    public List<TrSafetytipsTemplet> queryTrSafetytipsTempletList(TrSafetytipsTemplet trSafetytipsTempletParam) {
        List<TrSafetytipsTemplet> list = new ArrayList<TrSafetytipsTemplet>();
        String sqlwhere="";
        
        String sql = "SELECT * FROM TR_SAFETYTIPS_TEMPLET WHERE 1=1 "+sqlwhere;
        List<Map<String,String>> dblist = dbHelper.selectSQL(sql, null);
        for (int i = 0; i < dblist.size(); i++){
            Map<String, String> m = dblist.get(i);
            TrSafetytipsTemplet trSafetytipsTemplet = new TrSafetytipsTemplet();
            trSafetytipsTemplet.setId(m.get("ID"));
            trSafetytipsTemplet.setEndangername(m.get("ENDANGERNAME"));
            trSafetytipsTemplet.setEndangerrang(m.get("ENDANGERRANG"));
            trSafetytipsTemplet.setRiiskrang(m.get("RiISKRANG"));
            trSafetytipsTemplet.setResult(m.get("RESULT"));
            trSafetytipsTemplet.setGrade(m.get("GRADE"));
            trSafetytipsTemplet.setPrecontrolmethod(m.get("PRECONTROLMETHOD"));
            trSafetytipsTemplet.setMethod(m.get("METHOD"));
            trSafetytipsTemplet.setContinuelevel(m.get("CONTINUELEVEL"));
            trSafetytipsTemplet.setRemarks(m.get("REMARKS"));
            trSafetytipsTemplet.setSafetype(m.get("SAFETYPE"));
            trSafetytipsTemplet.setOrdermunber(m.get("ORDERMUNBER"));
            list.add(trSafetytipsTemplet);
        }
        return list;
    }

    public void insertListData(List<TrSafetytipsTemplet> trSafetytipsTempletList)
    {
        if(trSafetytipsTempletList!=null&&trSafetytipsTempletList.size()>0)
        {
            List<String> listSql= new ArrayList<String>();
            for(int i=0;i<trSafetytipsTempletList.size();i++)
            {
                String sql="REPLACE INTO TR_SAFETYTIPS_TEMPLET (" +
                        "ID,ENDANGERNAME,ENDANGERRANG,RiISKRANG,RESULT,GRADE,PRECONTROLMETHOD,METHOD,CONTINUELEVEL,REMARKS,"+
                        "SAFETYPE,ORDERMUNBER"+
                        ") VALUES (" +
                        "'"+trSafetytipsTempletList.get(i).getId()+"'," +
                        "'"+trSafetytipsTempletList.get(i).getEndangername()+"'," +
                        "'"+trSafetytipsTempletList.get(i).getEndangerrang()+"'," +
                        "'"+trSafetytipsTempletList.get(i).getRiiskrang()+"'," +
                        "'"+trSafetytipsTempletList.get(i).getResult()+"'," +
                        "'"+trSafetytipsTempletList.get(i).getGrade()+"'," +
                        "'"+trSafetytipsTempletList.get(i).getPrecontrolmethod()+"'," +
                        "'"+trSafetytipsTempletList.get(i).getMethod()+"'," +
                        "'"+trSafetytipsTempletList.get(i).getContinuelevel()+"'," +
                        "'"+trSafetytipsTempletList.get(i).getRemarks()+"'," +
                        "'"+trSafetytipsTempletList.get(i).getSafetype()+"'," +
                        "'"+trSafetytipsTempletList.get(i).getOrdermunber()+"'" +
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
     * 根据条件修改安全须知模板表数据
     * @designer
     * @since  2014-11-6
     * @author dingwujun
     * @param @param List<PmDevice>
     * @param @return
     * @return null
     */
    public void updateTrSafetytipsTempletInfo(Map<Object,Object> columnsMap,Map<Object,Object> wheresMap)
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
            sql="UPDATE TR_SAFETYTIPS_TEMPLET SET " +columns +" WHERE 1=1 "+sqlWhere;

            if(sql!=null&&!sql.equals("")) {
                dbHelper.execSQL(sql);
            }
        }
    }

}
