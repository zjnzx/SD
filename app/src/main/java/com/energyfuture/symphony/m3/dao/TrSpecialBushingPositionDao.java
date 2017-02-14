package com.energyfuture.symphony.m3.dao;

import android.content.Context;

import com.energyfuture.symphony.m3.domain.TrSpecialBushingPosition;
import com.energyfuture.symphony.m3.util.DBHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/3/24.
 */
public class TrSpecialBushingPositionDao {
    DBHelper dbHelper = null;

    public TrSpecialBushingPositionDao(Context cxt) {
        dbHelper = new DBHelper(cxt);
    }

    public SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 获取检测项目（套管）检测位置表信息
     *
     * @param @param  null
     * @param @return
     * @return list
     * @designer
     * @author dingwujun
     * @since 2014-11-6
     */
    public List<TrSpecialBushingPosition> queryTrSpecialBushingPositionList(TrSpecialBushingPosition trSpecialBushingPositionParam) {
        List<TrSpecialBushingPosition> list = new ArrayList<TrSpecialBushingPosition>();
        String sqlwhere="";
        if(trSpecialBushingPositionParam.getBushingid() != null && !trSpecialBushingPositionParam.getBushingid().equals("")){
            sqlwhere += " AND BUSHINGID='" + trSpecialBushingPositionParam.getBushingid() + "'";
        }
        
        String sql = "SELECT * FROM tr_special_bushing_position WHERE 1=1 "+sqlwhere + " ORDER BY ORDERBY ASC";
        List<Map<String,String>> dblist = dbHelper.selectSQL(sql, null);
        for (int i = 0; i < dblist.size(); i++){
            Map<String, String> m = dblist.get(i);
            TrSpecialBushingPosition trSpecialBushingPosition = new TrSpecialBushingPosition();
            trSpecialBushingPosition.setId(m.get("ID"));
            trSpecialBushingPosition.setBushingid(m.get("BUSHINGID"));
            trSpecialBushingPosition.setVoltage(m.get("VOLTAGE"));
            trSpecialBushingPosition.setPhash(m.get("PHASH"));
            trSpecialBushingPosition.setOrderby(m.get("ORDERBY"));
            list.add(trSpecialBushingPosition);
        }
        return list;
    }

    public void insertListData(List<TrSpecialBushingPosition> trSpecialBushingPositionList)
    {
        if(trSpecialBushingPositionList!=null&&trSpecialBushingPositionList.size()>0)
        {
            List<String> listSql= new ArrayList<String>();
            for(int i=0;i<trSpecialBushingPositionList.size();i++)
            {
                String sql="REPLACE INTO TR_SPECIAL_BUSHING_POSITION (" +
                        "ID,BUSHINGID,VOLTAGE,ORDERBY,PHASH"+
                        ") VALUES (" +
                        "'"+trSpecialBushingPositionList.get(i).getId()+"'," +
                        "'"+trSpecialBushingPositionList.get(i).getBushingid()+"'," +
                        "'"+trSpecialBushingPositionList.get(i).getVoltage()+"'," +
                        "'"+trSpecialBushingPositionList.get(i).getOrderby()+"'," +
                        "'"+trSpecialBushingPositionList.get(i).getPhash()+"'" +
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
     * 根据条件修改检测项目（套管）检测位置表数据
     * @designer
     * @since  2014-11-6
     * @author dingwujun
     * @param @param List<PmDevice>
     * @param @return
     * @return null
     */
    public void updateTrSpecialBushingPositionInfo(Map<Object,Object> columnsMap,Map<Object,Object> wheresMap)
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
            sql="UPDATE TR_SPECIAL_BUSHING_POSITION SET " +columns +" WHERE 1=1 "+sqlWhere;

            if(sql!=null&&!sql.equals("")) {
                dbHelper.execSQL(sql);
            }
        }
    }

}
