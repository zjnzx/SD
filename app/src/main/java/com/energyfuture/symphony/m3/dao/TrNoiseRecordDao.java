package com.energyfuture.symphony.m3.dao;

import android.content.Context;

import com.energyfuture.symphony.m3.domain.TrNoiseRecord;
import com.energyfuture.symphony.m3.util.DBHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/10/9.
 */
public class TrNoiseRecordDao {
    DBHelper dbHelper = null;

    public TrNoiseRecordDao(Context cxt) {
        dbHelper = new DBHelper(cxt);
    }

    public List<TrNoiseRecord> queryTrNoiseRecordList(TrNoiseRecord trNoiseRecord) {
        List<TrNoiseRecord> list = new ArrayList<TrNoiseRecord>();
        String sqlwhere="";
        if(trNoiseRecord.getDetectionobjid() != null && !trNoiseRecord.getDetectionobjid().equals("")){
            sqlwhere += " AND DETECTIONOBJID='" + trNoiseRecord.getDetectionobjid() + "'";
        }

        String sql = "SELECT * FROM TR_NOISE_RECORD WHERE 1=1 "+sqlwhere;
        List<Map<String,String>> dblist = dbHelper.selectSQL(sql, null);
        for (int i = 0; i < dblist.size(); i++){
            Map<String, String> m = dblist.get(i);
            TrNoiseRecord trNoiseRecord1 = new TrNoiseRecord();
            trNoiseRecord1.setId(m.get("ID"));
            trNoiseRecord1.setDetectionobjid(m.get("DETECTIONOBJID"));
            trNoiseRecord1.setDcfan(m.get("DCFAN"));
            trNoiseRecord1.setOilpump(m.get("OILPUMP"));
            trNoiseRecord1.setNoiseavg(m.get("NOISEAVG"));
            list.add(trNoiseRecord1);
        }
        return list;
    }

    public void insertListData(List<TrNoiseRecord> trNoiseRecordList)
    {
        if(trNoiseRecordList!=null&&trNoiseRecordList.size()>0)
        {
            List<String> listSql= new ArrayList<String>();
            for(int i=0;i<trNoiseRecordList.size();i++)
            {
                String sql="REPLACE INTO TR_NOISE_RECORD (" +
                        "ID,DETECTIONOBJID,DCFAN,OILPUMP,NOISEAVG"+
                        ") VALUES (" +
                        "'"+trNoiseRecordList.get(i).getId()+"'," +
                        "'"+trNoiseRecordList.get(i).getDetectionobjid()+"'," +
                        "'"+trNoiseRecordList.get(i).getDcfan()+"'," +
                        "'"+trNoiseRecordList.get(i).getOilpump()+"'," +
                        "'"+trNoiseRecordList.get(i).getNoiseavg()+"'" +
                        ")";
                listSql.add(sql);
            }
            if(listSql!=null&&listSql.size()>0)
            {
                dbHelper.insertSQLAll(listSql);
            }
        }
    }


    public void updateTrNoiseRecordInfo(Map<Object,Object> columnsMap,Map<Object,Object> wheresMap)
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
            sql="UPDATE TR_NOISE_RECORD SET " +columns +" WHERE 1=1 "+sqlWhere;

            if(sql!=null&&!sql.equals("")) {
                dbHelper.execSQL(sql);
            }
        }
    }
}
