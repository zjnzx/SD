package com.energyfuture.symphony.m3.dao;

import android.content.Context;

import com.energyfuture.symphony.m3.domain.TrNoiseRecordGroup;
import com.energyfuture.symphony.m3.util.DBHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/10/9.
 */
public class TrNoiseRecordGroupDao {
    DBHelper dbHelper = null;

    public TrNoiseRecordGroupDao(Context cxt) {
        dbHelper = new DBHelper(cxt);
    }

    public List<TrNoiseRecordGroup> queryTrNoiseRecordGroupList(TrNoiseRecordGroup trNoiseRecordGroup) {
        List<TrNoiseRecordGroup> list = new ArrayList<TrNoiseRecordGroup>();
        String sqlwhere="";
        if(trNoiseRecordGroup.getNoiseid() != null && !trNoiseRecordGroup.getNoiseid().equals("")){
            sqlwhere += " AND NOISEID='" + trNoiseRecordGroup.getNoiseid() + "'";
        }
        String sql = "SELECT * FROM TR_NOISE_RECORD_GROUP WHERE 1=1 "+sqlwhere;
        List<Map<String,String>> dblist = dbHelper.selectSQL(sql, null);
        for (int i = 0; i < dblist.size(); i++){
            Map<String, String> m = dblist.get(i);
            TrNoiseRecordGroup trNoiseRecordGroup1 = new TrNoiseRecordGroup();
            trNoiseRecordGroup1.setId(m.get("ID"));
            trNoiseRecordGroup1.setNoiseid(m.get("NOISEID"));
            trNoiseRecordGroup1.setGroupname(m.get("GROUPNAME"));
            trNoiseRecordGroup1.setOrderby(m.get("ORDERBY"));
            list.add(trNoiseRecordGroup1);
        }
        return list;
    }

    public void insertListData(List<TrNoiseRecordGroup> trNoiseRecordGroupList)
    {
        if(trNoiseRecordGroupList!=null&&trNoiseRecordGroupList.size()>0)
        {
            List<String> listSql= new ArrayList<String>();
            for(int i=0;i<trNoiseRecordGroupList.size();i++)
            {
                String sql="REPLACE INTO TR_NOISE_RECORD_GROUP (" +
                        "ID,NOISEID,GROUPNAME,ORDERBY"+
                        ") VALUES (" +
                        "'"+trNoiseRecordGroupList.get(i).getId()+"'," +
                        "'"+trNoiseRecordGroupList.get(i).getNoiseid()+"'," +
                        "'"+trNoiseRecordGroupList.get(i).getGroupname()+"'," +
                        "'"+trNoiseRecordGroupList.get(i).getOrderby()+"'" +
                        ")";
                listSql.add(sql);
            }
            if(listSql!=null&&listSql.size()>0)
            {
                dbHelper.insertSQLAll(listSql);
            }
        }
    }


    public void updateTrNoiseRecordGroupInfo(Map<Object,Object> columnsMap,Map<Object,Object> wheresMap)
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
            sql="UPDATE TR_NOISE_RECORD_GROUP SET " +columns +" WHERE 1=1 "+sqlWhere;

            if(sql!=null&&!sql.equals("")) {
                dbHelper.execSQL(sql);
            }
        }
    }
}
