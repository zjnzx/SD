package com.energyfuture.symphony.m3.dao;

import android.content.Context;

import com.energyfuture.symphony.m3.domain.TrNoiseRecordValue;
import com.energyfuture.symphony.m3.util.DBHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/10/9.
 */
public class TrNoiseRecordValueDao {
    DBHelper dbHelper = null;

    public TrNoiseRecordValueDao(Context cxt) {
        dbHelper = new DBHelper(cxt);
    }

    public List<TrNoiseRecordValue> queryTrNoiseRecordValueList(TrNoiseRecordValue trNoiseRecordValue) {
        List<TrNoiseRecordValue> list = new ArrayList<TrNoiseRecordValue>();
        String sqlwhere="";
        if(trNoiseRecordValue.getGroupid() != null && !trNoiseRecordValue.getGroupid().equals("")){
            sqlwhere += " AND GROUPID='" + trNoiseRecordValue.getGroupid() + "'";
        }

        String sql = "SELECT * FROM TR_NOISE_RECORD_VALUE WHERE 1=1 "+sqlwhere+"ORDER BY SIGHT";
        List<Map<String,String>> dblist = dbHelper.selectSQL(sql, null);
        for (int i = 0; i < dblist.size(); i++){
            Map<String, String> m = dblist.get(i);
            TrNoiseRecordValue trNoiseRecordValue1 = new TrNoiseRecordValue();
            trNoiseRecordValue1.setId(m.get("ID"));
            trNoiseRecordValue1.setGroupid(m.get("GROUPID"));
            trNoiseRecordValue1.setSight(m.get("SIGHT"));
            trNoiseRecordValue1.setHeight13(m.get("HEIGHT13"));
            trNoiseRecordValue1.setHeight23(m.get("HEIGHT23"));
            list.add(trNoiseRecordValue1);
        }
        return list;
    }

    public void insertListData(List<TrNoiseRecordValue> trNoiseRecordValueList)
    {
        if(trNoiseRecordValueList!=null&&trNoiseRecordValueList.size()>0)
        {
            List<String> listSql= new ArrayList<String>();
            for(int i=0;i<trNoiseRecordValueList.size();i++)
            {
                String sql="REPLACE INTO TR_NOISE_RECORD_VALUE (" +
                        "ID,GROUPID,SIGHT,HEIGHT13,HEIGHT23"+
                        ") VALUES (" +
                        "'"+trNoiseRecordValueList.get(i).getId()+"'," +
                        "'"+trNoiseRecordValueList.get(i).getGroupid()+"'," +
                        "'"+trNoiseRecordValueList.get(i).getSight()+"'," +
                        "'"+trNoiseRecordValueList.get(i).getHeight13()+"'," +
                        "'"+trNoiseRecordValueList.get(i).getHeight23()+"'" +
                        ")";
                listSql.add(sql);
            }
            if(listSql!=null&&listSql.size()>0)
            {
                dbHelper.insertSQLAll(listSql);
            }
        }
    }


    public void updateTrNoiseRecordValueInfo(Map<Object,Object> columnsMap,Map<Object,Object> wheresMap)
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
            sql="UPDATE TR_NOISE_RECORD_VALUE SET " +columns +" WHERE 1=1 "+sqlWhere;

            if(sql!=null&&!sql.equals("")) {
                dbHelper.execSQL(sql);
            }
        }
    }
}
