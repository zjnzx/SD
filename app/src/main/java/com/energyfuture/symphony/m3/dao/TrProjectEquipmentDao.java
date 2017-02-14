package com.energyfuture.symphony.m3.dao;

import android.content.Context;

import com.energyfuture.symphony.m3.domain.TrProjectEquipment;
import com.energyfuture.symphony.m3.util.DBHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/3/24.
 */
public class TrProjectEquipmentDao {
    DBHelper dbHelper = null;

    public TrProjectEquipmentDao(Context cxt) {
        dbHelper = new DBHelper(cxt);
    }

    public SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 获取项目对应检测类型表信息
     *
     * @param @param  null
     * @param @return
     * @return list
     * @designer
     * @author dingwujun
     * @since 2014-11-6
     */
    public List<TrProjectEquipment> queryTrProjectEquipmentList(TrProjectEquipment trProjectEquipmentParam) {
        List<TrProjectEquipment> list = new ArrayList<TrProjectEquipment>();
        String sqlwhere="";
        
        String sql = "SELECT * FROM TR_PROJECT_EQUIPMENT WHERE 1=1 "+sqlwhere;
        List<Map<String,String>> dblist = dbHelper.selectSQL(sql, null);
        for (int i = 0; i < dblist.size(); i++){
            Map<String, String> m = dblist.get(i);
            TrProjectEquipment trProjectEquipment = new TrProjectEquipment();
            trProjectEquipment.setRelationid(m.get("RELATIONID"));
            trProjectEquipment.setDetectionname(m.get("DETECTIONNAME"));
            trProjectEquipment.setProjectid(m.get("PROJECTID"));
            trProjectEquipment.setOrderby(m.get("ORDERBY"));
            list.add(trProjectEquipment);
        }
        return list;
    }

    public void insertListData(List<TrProjectEquipment> trProjectEquipmentList)
    {
        if(trProjectEquipmentList!=null&&trProjectEquipmentList.size()>0)
        {
            List<String> listSql= new ArrayList<String>();
            for(int i=0;i<trProjectEquipmentList.size();i++)
            {
                String sql="REPLACE INTO TR_PROJECT_EQUIPMENT (" +
                        "RELATIONID,DETECTIONNAME,PROJECTID,ORDERBY"+
                        ") VALUES (" +
                        "'"+trProjectEquipmentList.get(i).getRelationid()+"'," +
                        "'"+trProjectEquipmentList.get(i).getDetectionname()+"'," +
                        "'"+trProjectEquipmentList.get(i).getProjectid()+"'," +
                        "'"+trProjectEquipmentList.get(i).getOrderby()+"'" +
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
     * 根据条件修改项目对应检测类型表数据
     * @designer
     * @since  2014-11-6
     * @author dingwujun
     * @param @param List<PmDevice>
     * @param @return
     * @return null
     */
    public void updateTrProjectEquipmentInfo(Map<Object,Object> columnsMap,Map<Object,Object> wheresMap)
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
            sql="UPDATE TR_PROJECT_EQUIPMENT SET " +columns +" WHERE 1=1 "+sqlWhere;

            if(sql!=null&&!sql.equals("")) {
                dbHelper.execSQL(sql);
            }
        }
    }

}
