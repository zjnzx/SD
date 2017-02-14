package com.energyfuture.symphony.m3.dao;

import android.content.Context;

import com.energyfuture.symphony.m3.domain.TrParameter;
import com.energyfuture.symphony.m3.util.DBHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/3/24.
 */
public class TrParameterDao {
    DBHelper dbHelper = null;

    public TrParameterDao(Context cxt) {
        dbHelper = new DBHelper(cxt);
    }

    public SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 获取tr_parameter表信息
     *
     * @param @param  null
     * @param @return
     * @return list
     * @designer
     * @author daipan
     * @since 2016-1-6
     */
    public List<TrParameter> queryTrParameterList(TrParameter object) {
        List<TrParameter> list = new ArrayList<TrParameter>();
        String sqlwhere="";
        if(object!=null) {
            if(object.getParametertype() != null && !object.getParametertype().equals("")){
                sqlwhere += " AND PARAMETERTYPE = '" + object.getParametertype() + "'";
            }
        }
        String sql = "SELECT * FROM tr_parameter WHERE 1=1 "+sqlwhere;
        List<Map<String,String>> dblist = dbHelper.selectSQL(sql, null);
        for (int i = 0; i < dblist.size(); i++){
            Map<String, String> m = dblist.get(i);
            TrParameter trParameter = new TrParameter();
            trParameter.setId(m.get("ID"));
            trParameter.setParametertype(m.get("PARAMETERTYPE"));
            trParameter.setCompany(m.get("COMPANY"));
            trParameter.setModel(m.get("MODEL"));
            trParameter.setSdfileurl(m.get("SDFILEURL"));
            trParameter.setFileprefix(m.get("FILEPREFIX"));
            trParameter.setFilesuffix(m.get("FILESUFFIX"));
            trParameter.setUpdateperson(m.get("UPDATEPERSON"));
            trParameter.setUpdatetime(m.get("UPDATETIME"));
            list.add(trParameter);
        }
        return list;
    }

    /**
     * 根据条件修改tr_parameter表数据
     * @designer
     * @since  2016-1-6
     * @author daipan
     * @param @param List<PmDevice>
     * @param @return
     * @return null
     */
    public void updateTrParameterInfo(Map<Object,Object> columnsMap,Map<Object,Object> wheresMap)
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
            sql="UPDATE tr_parameter SET " +columns +" WHERE 1=1 "+sqlWhere;

            if(sql!=null&&!sql.equals("")) {
                dbHelper.execSQL(sql);
            }
        }
    }
    public void insertListData(List<TrParameter> trParameters)
    {
        if(trParameters!=null&&trParameters.size()>0)
        {
            List<String> listSql= new ArrayList<String>();
            for(int i=0;i<trParameters.size();i++)
            {
                String sql="REPLACE INTO tr_parameter (" +
                        "ID,PARAMETERTYPE,COMPANY,MODEL,SDFILEURL,FILEPREFIX,FILESUFFIX,UPDATEPERSON,UPDATETIME"+
                        ") VALUES (" +
                        "'"+trParameters.get(i).getId()+"'," +
                        "'"+trParameters.get(i).getParametertype()+"'," +
                        "'"+trParameters.get(i).getCompany()+"'," +
                        "'"+trParameters.get(i).getModel()+"'," +
                        "'"+trParameters.get(i).getSdfileurl()+"'," +
                        "'"+trParameters.get(i).getFileprefix()+"'," +
                        "'"+trParameters.get(i).getFilesuffix()+"'," +
                        "'"+trParameters.get(i).getUpdateperson()+"'," +
                        "'"+trParameters.get(i).getUpdatetime()+"'" +
                        ")";
                listSql.add(sql);
            }
            if(listSql!=null&&listSql.size()>0)
            {
                dbHelper.insertSQLAll(listSql);
            }
        }
    }

    //删除设备
    public void deleteListData(List<TrParameter> trParameters){
        if(trParameters != null && trParameters.size() > 0){
            {
                List<String> listSql= new ArrayList<String>();
                for(int i=0;i<trParameters.size();i++)
                {
                    String sql="Delete from tr_parameter where id='" + trParameters.get(i).getId() + "'";
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
