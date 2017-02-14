package com.energyfuture.symphony.m3.dao;

import android.content.Context;

import com.energyfuture.symphony.m3.domain.SympCommunicationTheme;
import com.energyfuture.symphony.m3.util.DBHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/3/24.
 */
public class SympCommunicationThemeDao {
    DBHelper dbHelper = null;

    public SympCommunicationThemeDao(Context cxt) {
        dbHelper = new DBHelper(cxt);
    }

    public SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    /**
     *
     * @param @return
     * @return list
     * @designer
     * @author zhangshuang
     * @since 2014-11-6
     */
    public List<SympCommunicationTheme> querySympCommunicationThemeList(SympCommunicationTheme parameterObj) {
        List<SympCommunicationTheme> list = new ArrayList<SympCommunicationTheme>();
        String  sqlWhere="";
        if(parameterObj.getId()!=null&&!parameterObj.getId().equals(""))
        {
            sqlWhere+=" AND ID='"+parameterObj.getId()+"'";
        }
        if(parameterObj.getBelongId()!=null&&!parameterObj.getBelongId().equals(""))
        {
            sqlWhere+=" AND BELONGID='"+parameterObj.getBelongId()+"'";
        }
        if(parameterObj.getCreatepersonid()!=null&&!parameterObj.getCreatepersonid().equals(""))
        {
            sqlWhere+=" AND CREATEPERSONID='"+parameterObj.getCreatepersonid()+"'";
        }
        String sql = "SELECT * FROM SYMP_COMMUNICATION_THEME WHERE 1=1 "+sqlWhere+" ORDER BY CREATETIME DESC";
        List<Map<String,String>> dblist = dbHelper.selectSQL(sql, null);
        for (int i = 0; i < dblist.size(); i++){
            Map<String, String> m = dblist.get(i);
            SympCommunicationTheme obj = new SympCommunicationTheme();
                obj.setId(m.get("ID"));
                obj.setBelongId(m.get("BELONGID"));
                obj.setPosition(m.get("POSITION"));
                obj.setThemetitle(m.get("THEMETITLE"));
                obj.setThemecontent(m.get("THEMECONTENT"));
                obj.setCreatepersonid(m.get("CREATEPERSONID"));
                obj.setCreatetime(m.get("CREATETIME"));
                obj.setThemestate(m.get("THEMESTATE"));
                obj.setParticipator(m.get("PARTICIPATOR"));
                list.add(obj);
            }
        return list;
    }


    /**
     * 插入沟通交流主贴表数据
     * @designer
     * @since  2014-11-6
     * @author dingwujun
     * @param @param List<PmDevice>
     * @param @return
     * @return null
     */
    public void insertListData(List<SympCommunicationTheme> parameterObjList)
    {
        if(parameterObjList!=null&&parameterObjList.size()>0)
        {
            List<String> listSql= new ArrayList<String>();
            for(int i=0;i<parameterObjList.size();i++)
            {
                String sql="REPLACE INTO SYMP_COMMUNICATION_THEME (" +
                        "ID,BELONGID,POSITION,THEMETITLE,THEMECONTENT,CREATEPERSONID,CREATETIME,THEMESTATE,PARTICIPATOR" +
                        ") VALUES (" +
                        "'"+parameterObjList.get(i).getId()+"'," +
                        "'"+parameterObjList.get(i).getBelongId()+"'," +
                        "'"+parameterObjList.get(i).getPosition()+"'," +
                        "'"+parameterObjList.get(i).getThemetitle()+"'," +
                        "'"+parameterObjList.get(i).getThemecontent()+"'," +
                        "'"+parameterObjList.get(i).getCreatepersonid()+"'," +
                        "'"+parameterObjList.get(i).getCreatetime()+"'," +
                        "'"+parameterObjList.get(i).getThemestate()+"'," +
                        "'"+parameterObjList.get(i).getParticipator()+"'" +
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
     * 根据条件修改沟通交流主贴表数据
     * @designer
     * @since  2014-11-6
     * @author dingwujun
     * @param @param List<PmDevice>
     * @param @return
     * @return null
     */
    public void udateSympCommunicationTheme(Map<Object,Object> columnsMap,Map<Object,Object> wheresMap)
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
                 sql="UPDATE SYMP_COMMUNICATION_THEME SET " +columns +" WHERE 1=1 "+sqlWhere;

            if(sql!=null&&!sql.equals(""))
            {
                dbHelper.execSQL(sql);
            }
        }
    }

    /**
     * 根据id删除沟通交流主贴表数据
     * @designer
     * @since  2014-11-6
     * @author dingwujun
     * @param @param List<PmDevice>
     * @param @return
     * @return null
     */
    public void deleteSympCommunicationTheme(String id)
    {
        String sql="";
        if(id!=null&&!id.equals(""))
        {
            sql="DELETE FROM SYMP_COMMUNICATION_THEME WHERE ID='" +id +"'";
            if(sql!=null&&!sql.equals(""))
            {
                dbHelper.execSQL(sql);
            }
        }
    }

}


