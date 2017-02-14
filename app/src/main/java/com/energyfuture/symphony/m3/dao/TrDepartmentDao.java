package com.energyfuture.symphony.m3.dao;

import android.content.Context;

import com.energyfuture.symphony.m3.domain.Department;
import com.energyfuture.symphony.m3.util.DBHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/3/24.
 */
public class TrDepartmentDao {
    DBHelper dbHelper = null;

    public TrDepartmentDao(Context cxt) {
        dbHelper = new DBHelper(cxt);
    }

    public SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 获取部门表信息
     *
     * @param @param  null
     * @param @return
     * @return list
     * @designer
     * @author dingwujun
     * @since 2014-11-6
     */
    public List<Department> queryDepartmentList(Department departmentParam) {
        List<Department> list = new ArrayList<Department>();
        String sqlwhere="";
        if(departmentParam != null){
            if(departmentParam.getBmid() != null && !departmentParam.getBmid().equals("")){
                sqlwhere+=" AND BMID='"+departmentParam.getBmid()+"'";
            }
            if(departmentParam.getBmlx() != null && !departmentParam.getBmlx().equals("")){
                sqlwhere+=" AND BMLX='"+departmentParam.getBmlx()+"'";
            }
            if(departmentParam.getSsbm() != null && !departmentParam.getSsbm().equals("")){
                sqlwhere+=" AND SSBM='"+departmentParam.getSsbm()+"'";
            }
        }
        
        String sql = "SELECT * FROM DEPARTMENT WHERE 1=1 "+sqlwhere;
        List<Map<String,String>> dblist = dbHelper.selectSQL(sql, null);
        for (int i = 0; i < dblist.size(); i++){
            Map<String, String> m = dblist.get(i);
            Department department = new Department();
            department.setBmid(m.get("BMID"));
            department.setBmmc(m.get("BMMC"));
            department.setSsbm(m.get("SSBM"));
            department.setBmlx(m.get("BMLX"));
            list.add(department);
        }
        return list;
    }

    public void insertListData(List<Department> departmentList)
    {
        if(departmentList!=null&&departmentList.size()>0)
        {
            List<String> listSql= new ArrayList<String>();
            for(int i=0;i<departmentList.size();i++)
            {
                String sql="REPLACE INTO DEPARTMENT (" +
                        "BMID,BMMC,SSBM,BMLX"+
                        ") VALUES (" +
                        "'"+departmentList.get(i).getBmid()+"'," +
                        "'"+departmentList.get(i).getBmmc()+"'," +
                        "'"+departmentList.get(i).getSsbm()+"'," +
                        "'"+departmentList.get(i).getBmlx()+"'" +
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
     * @param departmentList
     */
    public void deleteListData(List<Department> departmentList){
        if(departmentList != null && departmentList.size() > 0){
            {
                List<String> listSql= new ArrayList<String>();
                for(int i=0;i<departmentList.size();i++)
                {
                    String sql="Delete from DEPARTMENT where bmid='" + departmentList.get(i).getBmid() + "'";
                    listSql.add(sql);
                }
                if(listSql!=null&&listSql.size()>0)
                {
                    dbHelper.deleteSQLAll(listSql);
                }
            }
        }
    }

    /**
     * 根据条件修改部门表数据
     * @designer
     * @since  2014-11-6
     * @author dingwujun
     * @param @param List<PmDevice>
     * @param @return
     * @return null
     */
    public void updateDepartmentInfo(Map<Object,Object> columnsMap,Map<Object,Object> wheresMap)
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
            sql="UPDATE DEPARTMENT SET " +columns +" WHERE 1=1 "+sqlWhere;

            if(sql!=null&&!sql.equals("")) {
                dbHelper.execSQL(sql);
            }
        }
    }

}
