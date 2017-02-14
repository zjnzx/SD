package com.energyfuture.symphony.m3.dao;

import android.content.Context;

import com.energyfuture.symphony.m3.domain.TrStation;
import com.energyfuture.symphony.m3.util.DBHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/3/24.
 */
public class TrStationDao {
    DBHelper dbHelper = null;

    public TrStationDao(Context cxt) {
        dbHelper = new DBHelper(cxt);
    }


    /**
     * 获取变电站表信息
     *
     * @param @param  null
     * @param @return
     * @return list
     * @designer
     * @author dingwujun
     * @since 2014-11-6
     */
    public List<TrStation> queryTrStationList(TrStation trStationParam) {
        List<TrStation> list = new ArrayList<TrStation>();
        String  sqlWhere="";
        if(trStationParam!=null) {
            if (trStationParam.getStationid() != null && !trStationParam.getStationid().equals("")) {
                sqlWhere = " AND STATIONID='" + trStationParam.getStationid() + "'";
            }
            if (trStationParam.getStationname() != null && !trStationParam.getStationname().equals("")) {
                sqlWhere = " AND STATIONNAME LIKE '%" + trStationParam.getStationname() + "%'";
            }
        }
        String sql = "SELECT * FROM TR_STATION WHERE 1=1 "+sqlWhere;
        List<Map<String,String>> dblist = dbHelper.selectSQL(sql, null);
        for (int i = 0; i < dblist.size(); i++){
            Map<String, String> m = dblist.get(i);
            TrStation trStation = new TrStation();
            trStation.setStationid(m.get("STATIONID"));
            trStation.setBureauid(m.get("BUREAUID"));
            trStation.setVoltage(m.get("VOLTAGE"));
            trStation.setStationname(m.get("STATIONNAME"));
            trStation.setSubcode(m.get("SUBCODE"));
            trStation.setVisible(m.get("VISIBLE"));
            trStation.setPositionx(m.get("POSITIONX"));
            trStation.setPositiony(m.get("POSITIONY"));
            list.add(trStation);
            }
        return list;
    }

    public void insertListData(List<TrStation> trStations){
        if(trStations!=null&&trStations.size()>0)
        {
            List<String> listSql= new ArrayList<String>();
            for(int i=0;i<trStations.size();i++)
            {
                String sql="REPLACE INTO TR_STATION "+
                        "(STATIONID,BUREAUID,VOLTAGE,STATIONNAME,SUBCODE,VISIBLE,POSITIONX,POSITIONY)"+
                        " VALUES "+
                        "('"+trStations.get(i).getStationid()+"'," +
                        "'"+trStations.get(i).getBureauid()+"'," +
                        "'"+trStations.get(i).getVoltage()+"'," +
                        "'"+trStations.get(i).getStationname()+"'," +
                        "'"+trStations.get(i).getSubcode()+"'," +
                        "'"+trStations.get(i).getVisible()+"'," +
                        "'"+trStations.get(i).getPositionx()+"'," +
                        "'"+trStations.get(i).getPositiony()+"'" +
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
     * 删除
     * @param trStationList
     */
    public void deleteListData(List<TrStation> trStationList){
        if(trStationList != null && trStationList.size() > 0){
            {
                List<String> listSql= new ArrayList<String>();
                for(int i=0;i<trStationList.size();i++)
                {
                    String sql="Delete from TR_STATION where bmid='" + trStationList.get(i).getStationid() + "'";
                    listSql.add(sql);
                }
                if(listSql!=null&&listSql.size()>0)
                {
                    dbHelper.deleteSQLAll(listSql);
                }
            }
        }
    }

}
