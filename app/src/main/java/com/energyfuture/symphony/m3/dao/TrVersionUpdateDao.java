package com.energyfuture.symphony.m3.dao;

import android.content.Context;

import com.energyfuture.symphony.m3.domain.TrDetectionTools;
import com.energyfuture.symphony.m3.domain.TrVersionUpdate;
import com.energyfuture.symphony.m3.domain.UserInfo;
import com.energyfuture.symphony.m3.util.DBHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/3/26.
 */
public class TrVersionUpdateDao {
    DBHelper dbHelper = null;
    public TrVersionUpdateDao(Context context){
        dbHelper = new DBHelper(context);
    }


    public List<TrVersionUpdate> queryVersionUpdateList(TrVersionUpdate versionUpdate){
        List<TrVersionUpdate> list = new ArrayList<TrVersionUpdate>();
        String sqlwhere="";
        if(versionUpdate != null){
            if(versionUpdate.getId()!=null&&!versionUpdate.getId().equals(""))
            {
                sqlwhere+=" AND ID='"+versionUpdate.getId()+"'";
            }
        }

        String sql = "SELECT * FROM TR_VERSIONUPDATE WHERE 1=1 "+sqlwhere + " ORDER BY VERSIONCODE DESC";
        List<Map<String,String>> dblist = dbHelper.selectSQL(sql, null);
        for (int i = 0; i < dblist.size(); i++){
            Map<String, String> m = dblist.get(i);
            TrVersionUpdate update=new TrVersionUpdate();
            update.setId(m.get("ID"));
            update.setAppinfo(m.get("ADDINFO"));
            update.setUpdateinfo(m.get("UPDATEINFO"));
            update.setDeleteinfo(m.get("DELETEINFO"));
            update.setVersioncode(Integer.parseInt(m.get("VERSIONCODE")));
            update.setVersionname(m.get("VERSIONNAME"));
            update.setLoadperson(m.get("LOADPERSON"));
            update.setUpdatetime(m.get("UPDATETIME"));
            list.add(update);
        }
        return list;
    }

    public void insertListData(List<TrVersionUpdate> list)
    {
        if(list!=null&&list.size()>0)
        {
            List<String> listSql= new ArrayList<String>();
            for(int i=0;i<list.size();i++)
            {
                String sql="REPLACE INTO TR_VERSIONUPDATE (" +
                        "ID,ADDINFO,UPDATEINFO,DELETEINFO,VERSIONCODE,VERSIONNAME,LOADPERSON,UPDATETIME"+
                        ") VALUES (" +
                        "'"+list.get(i).getId()+"'," +
                        "'"+list.get(i).getAppinfo()+"'," +
                        "'"+list.get(i).getUpdateinfo()+"'," +
                        "'"+list.get(i).getDeleteinfo()+"'," +
                        "'"+list.get(i).getVersioncode()+"'," +
                        "'"+list.get(i).getVersionname()+"'," +
                        "'"+list.get(i).getLoadperson()+"'," +
                        "'"+list.get(i).getUpdatetime()+"'" +
                        ")";
                listSql.add(sql);
            }
            if(listSql!=null&&listSql.size()>0)
            {
                dbHelper.insertSQLAll(listSql);
            }
        }
    }

    public boolean updateTrVersionUpdate(Map<Object,Object> columnsMap,Map<Object,Object> wheresMap)
    {
        boolean flag=false;
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
            sql="UPDATE TR_VERSIONUPDATE SET " +columns +" WHERE 1=1 "+sqlWhere;

            if(sql!=null&&!sql.equals("")) {
                dbHelper.execSQL(sql);
                flag=true;
            }
        }
        return flag;
    }
}
