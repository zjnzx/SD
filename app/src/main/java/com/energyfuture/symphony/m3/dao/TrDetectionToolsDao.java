package com.energyfuture.symphony.m3.dao;

import android.content.Context;

import com.energyfuture.symphony.m3.domain.TrDetectionTools;
import com.energyfuture.symphony.m3.util.DBHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/3/24.
 */
public class TrDetectionToolsDao {
    DBHelper dbHelper = null;

    public TrDetectionToolsDao(Context cxt) {
        dbHelper = new DBHelper(cxt);
    }

    public SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 获取检测设备及工具表信息
     *
     * @param @param  null
     * @param @return
     * @return list
     * @designer
     * @author dingwujun
     * @since 2014-11-6
     */
    public List<TrDetectionTools> queryTrDetectionToolsList(TrDetectionTools trDetectionToolsParam) {
        List<TrDetectionTools> list = new ArrayList<TrDetectionTools>();
        String sqlWhere="";
        if(trDetectionToolsParam.getToolstype()!=null&&!trDetectionToolsParam.getToolstype().equals(""))
        {
            sqlWhere+=" AND TOOLSTYPE='"+trDetectionToolsParam.getToolstype()+"'";
        }
        String sql = "SELECT * FROM TR_DETECTION_TOOLS WHERE 1=1 "+sqlWhere;
        List<Map<String,String>> dblist = dbHelper.selectSQL(sql, null);
        for (int i = 0; i < dblist.size(); i++){
            Map<String, String> m = dblist.get(i);
            TrDetectionTools trDetectionTools = new TrDetectionTools();
            trDetectionTools.setId(m.get("ID"));
            trDetectionTools.setOrdernumber(m.get("ORDERNUMBER"));
            trDetectionTools.setToolstype(m.get("TOOLSTYPE"));
            trDetectionTools.setCompany(m.get("COMPANY"));
            trDetectionTools.setModel(m.get("MODEL"));
            trDetectionTools.setEquipmentname(m.get("EQUIPMENTNAME"));
            trDetectionTools.setProductiondate(m.get("PRODUCTIONDATE"));
            trDetectionTools.setCcbh(m.get("CCBH"));
            trDetectionTools.setState(m.get("STATE"));
            trDetectionTools.setValiddate(m.get("VALIDDATE"));
            trDetectionTools.setDetectiontype(m.get("DETECTIONTYPE"));
            trDetectionTools.setUnit(m.get("UNIT"));
            trDetectionTools.setCorrecting(m.get("CORRECTING"));
            list.add(trDetectionTools);
        }
        return list;
    }

    public void insertListData(List<TrDetectionTools> trDetectionToolsList)
    {
        if(trDetectionToolsList!=null&&trDetectionToolsList.size()>0)
        {
            List<String> listSql= new ArrayList<String>();
            for(int i=0;i<trDetectionToolsList.size();i++)
            {
                String sql="REPLACE INTO TR_DETECTION_TOOLS (" +
                        "ID,ORDERNUMBER,TOOLSTYPE,COMPANY,MODEL,EQUIPMENTNAME,PRODUCTIONDATE,CCBH,STATE,VALIDDATE,DETECTIONTYPE,UNIT,CORRECTING"+
                        ") VALUES (" +
                        "'"+trDetectionToolsList.get(i).getId()+"'," +
                        "'"+trDetectionToolsList.get(i).getOrdernumber()+"'," +
                        "'"+trDetectionToolsList.get(i).getToolstype()+"'," +
                        "'"+trDetectionToolsList.get(i).getCompany()+"'," +
                        "'"+trDetectionToolsList.get(i).getModel()+"'," +
                        "'"+trDetectionToolsList.get(i).getEquipmentname()+"'," +
                        "'"+trDetectionToolsList.get(i).getProductiondate()+"'," +
                        "'"+trDetectionToolsList.get(i).getCcbh()+"'," +
                        "'"+trDetectionToolsList.get(i).getState()+"'," +
                        "'"+trDetectionToolsList.get(i).getValiddate()+"'" +
                        "'"+trDetectionToolsList.get(i).getDetectiontype()+"'" +
                        "'"+trDetectionToolsList.get(i).getUnit()+"'" +
                        "'"+trDetectionToolsList.get(i).getCorrecting()+"'" +
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
     * 根据条件修改检测设备及工具表数据
     * @designer
     * @since  2014-11-6
     * @author dingwujun
     * @param @param List<PmDevice>
     * @param @return
     * @return null
     */
    public void updateTrDetectionToolsInfo(Map<Object,Object> columnsMap,Map<Object,Object> wheresMap)
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
            sql="UPDATE TR_DETECTION_TOOLS SET " +columns +" WHERE 1=1 "+sqlWhere;

            if(sql!=null&&!sql.equals("")) {
                dbHelper.execSQL(sql);
            }
        }
    }

}
