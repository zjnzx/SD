package com.energyfuture.symphony.m3.dao;

import android.content.Context;

import com.energyfuture.symphony.m3.domain.TrBureau;
import com.energyfuture.symphony.m3.util.DBHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/3/24.
 */
public class TrBureauDao {
    DBHelper dbHelper = null;

    public TrBureauDao(Context cxt) {
        dbHelper = new DBHelper(cxt);
    }

    public SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    /**
     * 获取任供电局信息
     *
     * @param @param  null
     * @param @return
     * @return list
     * @designer
     * @author dingwujun
     * @since 2014-11-6
     */
    public List<TrBureau> queryTrBureauList(TrBureau trBureauParam) {
        List<TrBureau> list = new ArrayList<TrBureau>();
        String  sqlWhere="";
        if(trBureauParam.getBureauid() != null && !trBureauParam.getBureauid().equals("")){
            sqlWhere = " AND BUREAUID='" + trBureauParam.getBureauid() + "'";
        }
        if (trBureauParam.getBureauname() != null && !trBureauParam.getBureauname().equals("")) {
            sqlWhere = " AND BUREAUNAME LIKE '%" + trBureauParam.getBureauname() + "%'";
        }

        String sql = "SELECT * FROM TR_BUREAU WHERE 1=1 " + sqlWhere;
        List<Map<String,String>> dblist = dbHelper.selectSQL(sql, null);
        for (int i = 0; i < dblist.size(); i++){
            Map<String, String> m = dblist.get(i);
            TrBureau trBureau = new TrBureau();
            trBureau.setBureauid(m.get("BUREAUID"));
            trBureau.setBureauname(m.get("BUREAUNAME"));
            trBureau.setListid(m.get("LISTID"));
            trBureau.setBureauimg(m.get("BUREAUIMG"));
            trBureau.setPositionx(m.get("POSITIONX"));
            trBureau.setPositiony(m.get("POSITIONY"));
            trBureau.setProvinceid(m.get("PROVINCEID"));
            list.add(trBureau);
            }

        return list;
    }

    /**
     * 插入供电局数据
     * @designer
     * @since  2014-11-6
     * @author dingwujun
     * @param @param List<PmDevice>
     * @param @return
     * @return null
     */
    public void insertListData(List<TrBureau> trBureauList)
    {
        if(trBureauList!=null&&trBureauList.size()>0)
        {
            List<String> listSql= new ArrayList<String>();
            for(int i=0;i<trBureauList.size();i++)
            {
                String sql="REPLACE INTO TR_BUREAU (" +
                        "BUREAUID,BUREAUNAME,LISTID,BUREAUIMG,POSITIONX,POSITIONY,PROVINCEID" +
                        ") VALUES (" +
                        "'"+trBureauList.get(i).getBureauid()+"'," +
                        "'"+trBureauList.get(i).getBureauname()+"'," +
                        "'"+trBureauList.get(i).getListid()+"'," +
                        "'"+trBureauList.get(i).getBureauimg()+"'," +
                        "'"+trBureauList.get(i).getPositionx()+"'," +
                        "'"+trBureauList.get(i).getPositiony()+"'," +
                        "'"+trBureauList.get(i).getProvinceid()+"'" +
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
     * 根据条件修改供电局数据
     * @designer
     * @since  2014-11-6
     * @author dingwujun
     * @param @param List<PmDevice>
     * @param @return
     * @return null
     */
    public void updateTrBureau(Map<Object,Object> columnsMap,Map<Object,Object> wheresMap)
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
                sql="UPDATE TR_BUREAU SET " +columns +" WHERE 1=1 "+sqlWhere;

            if(sql!=null&&!sql.equals(""))
            {
                dbHelper.execSQL(sql);
            }
        }
    }

    //删除项目
    public void deleteListData(List<TrBureau> trBureaus){
        if(trBureaus != null && trBureaus.size() > 0){
            {
                List<String> listSql= new ArrayList<String>();
                for(int i=0;i<trBureaus.size();i++)
                {
                    String sql="Delete from TR_BUREAU where bureauid='" + trBureaus.get(i).getBureauid() + "'";
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