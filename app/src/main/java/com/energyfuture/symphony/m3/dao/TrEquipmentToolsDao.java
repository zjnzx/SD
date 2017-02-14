package com.energyfuture.symphony.m3.dao;

import android.content.Context;

import com.energyfuture.symphony.m3.domain.TrEquipmentTools;
import com.energyfuture.symphony.m3.util.DBHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/9/9.
 */
public class TrEquipmentToolsDao {
    DBHelper dbHelper = null;

    public TrEquipmentToolsDao(Context cxt) {
        dbHelper = new DBHelper(cxt);
    }

    public List<TrEquipmentTools> queryTrEquipmentToolsList(TrEquipmentTools TrEquipmentTools) {
        List<TrEquipmentTools> list = new ArrayList<TrEquipmentTools>();
        String sqlwhere="";

        if(TrEquipmentTools.getTaskid()!=null&&!TrEquipmentTools.getTaskid().equals(""))
        {
            sqlwhere+=" AND TASKID='"+TrEquipmentTools.getTaskid()+"'";
        }

        if(TrEquipmentTools.getToolid()!=null&&!TrEquipmentTools.getToolid().equals(""))
        {
            sqlwhere+=" AND TOOLID='"+TrEquipmentTools.getToolid()+"'";
        }

        String sql = "SELECT * FROM tr_equipment_tools WHERE 1=1 "+sqlwhere;
        List<Map<String,String>> dblist = dbHelper.selectSQL(sql, null);
        for (int i = 0; i < dblist.size(); i++){
            Map<String, String> m = dblist.get(i);
            TrEquipmentTools trEquipmentTool = new TrEquipmentTools();
            trEquipmentTool.setId(m.get("ID"));
            trEquipmentTool.setTaskid(m.get("TASKID"));
            trEquipmentTool.setToolid(m.get("TOOLID"));
            trEquipmentTool.setAmount(m.get("AMOUNT"));
            trEquipmentTool.setPersion(m.get("PERSION"));
            trEquipmentTool.setUpdatetime(m.get("UPDATETIME"));
            list.add(trEquipmentTool);
        }
        return list;
    }

    /**
     * 查询任务下的工具数量
     * @return
     */
    public String queryToolCount(TrEquipmentTools trEquipmentTools){
        String  sqlWhere="";
        if(trEquipmentTools.getTaskid()!=null && !trEquipmentTools.getTaskid().equals(""))
        {
            sqlWhere+=" AND TASKID='"+trEquipmentTools.getTaskid()+"'";
        }
        String count = "";
        String sql = "SELECT SUM(AMOUNT) COUNT FROM TR_EQUIPMENT_TOOLS WHERE 1=1"+sqlWhere;
        List<Map<String,String>> dblist = dbHelper.selectSQL(sql, null);
        for (int i = 0; i < dblist.size(); i++){
            Map<String, String> m = dblist.get(i);
            count = m.get("COUNT");
        }
        return count;
    }

    public void insertTrEquipmentTools(List<TrEquipmentTools> trEquipmentToolses) {
        if(trEquipmentToolses!=null&&trEquipmentToolses.size()>0)
        {
            List<String> listSql= new ArrayList<String>();
            for(int i=0;i<trEquipmentToolses.size();i++)
            {
                String sql="REPLACE INTO tr_equipment_tools (" +
                        "ID,TASKID,TOOLID,AMOUNT,PERSION,UPDATETIME" +
                        ") VALUES (" +
                        "'"+trEquipmentToolses.get(i).getId()+"'," +
                        "'"+trEquipmentToolses.get(i).getTaskid()+"'," +
                        "'"+trEquipmentToolses.get(i).getToolid()+"'," +
                        "'"+trEquipmentToolses.get(i).getAmount()+"'," +
                        "'"+trEquipmentToolses.get(i).getPersion()+"'," +
                        "'"+trEquipmentToolses.get(i).getUpdatetime()+"'" +
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
     * 根据id修改
     * @param columnsMap
     * @param wheresMap
     */
    public void updateTrEquipmentToolsInfo(Map<Object,Object> columnsMap,Map<Object,Object> wheresMap)
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
            sql="UPDATE tr_equipment_tools SET " +columns +" WHERE 1=1 "+sqlWhere;

            if(sql!=null&&!sql.equals("")) {
                dbHelper.execSQL(sql);
            }
        }
    }
}
