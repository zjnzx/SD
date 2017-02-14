package com.energyfuture.symphony.m3.dao;

import android.content.Context;

import com.energyfuture.symphony.m3.domain.SympCommunicationPrompt;
import com.energyfuture.symphony.m3.util.DBHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/3/24.
 */
public class SympCommunicationPromptDao {
    DBHelper dbHelper = null;

    public SympCommunicationPromptDao(Context cxt) {
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
    public List<SympCommunicationPrompt> querySympCommunicationPromptList(SympCommunicationPrompt parameterObj) {
        List<SympCommunicationPrompt> list = new ArrayList<SympCommunicationPrompt>();
        String  sqlWhere="";
        if(parameterObj.getThemeid()!=null&&!parameterObj.getThemeid().equals(""))
        {
            sqlWhere+=" AND THEMEID='"+parameterObj.getThemeid()+"'";
        }
        if(parameterObj.getPrompttype()!=null&&!parameterObj.getPrompttype().equals(""))
        {
            sqlWhere+=" AND PROMPTTYPE='"+parameterObj.getPrompttype()+"'";
        }
        if(parameterObj.getState()!=null&&!parameterObj.getState().equals(""))
        {
            sqlWhere+=" AND STATE='"+parameterObj.getState()+"'";
        }
        if(parameterObj.getReplyid()!=null&&!parameterObj.getReplyid().equals(""))
        {
            sqlWhere+=" AND REPLYID='"+parameterObj.getReplyid()+"'";
        }
        String sql = "SELECT * FROM SYMP_COMMUNICATION_PROMPT WHERE 1=1 "+sqlWhere;
        List<Map<String,String>> dblist = dbHelper.selectSQL(sql, null);
        for (int i = 0; i < dblist.size(); i++){
            Map<String, String> m = dblist.get(i);
            SympCommunicationPrompt obj = new SympCommunicationPrompt();
                obj.setId(m.get("ID"));
                obj.setThemeid(m.get("THEMEID"));
                obj.setReplyid(m.get("REPLYID"));
                obj.setPromptcontent(m.get("PROMPTCONTENT"));
                obj.setCreatetime(m.get("CREATETIME"));
                obj.setState(m.get("STATE"));
                obj.setPromptpersonid(m.get("PROMPTPERSONID"));
                obj.setPrompttype(m.get("PROMPTTYPE"));
                list.add(obj);
            }
        return list;
    }

    /**
     * 查询是否有人@
     * @return
     */
    public List<String> querySympCommunicationPrompt(SympCommunicationPrompt parameterObj) {
        List<String> list = new ArrayList<String>();
        String  sqlWhere="";
        if(parameterObj.getThemeid()!=null&&!parameterObj.getThemeid().equals(""))
        {
            sqlWhere+=" AND PROMPT.THEMEID='"+parameterObj.getThemeid()+"'";
        }
        String sql = "SELECT REPLY.REPLYCONTENT CONTENT FROM SYMP_COMMUNICATION_PROMPT PROMPT LEFT JOIN SYMP_COMMUNICATION_REPLY REPLY ON PROMPT.THEMEID=REPLY.THEMEID " +
                "WHERE 1=1 "+sqlWhere+" AND PROMPT.PROMPTTYPE='3' AND PROMPT.STATE='0'";
        List<Map<String,String>> dblist = dbHelper.selectSQL(sql, null);
        for (int i = 0; i < dblist.size(); i++){
            Map<String, String> m = dblist.get(i);
            list.add(m.get("CONTENT"));
        }
        return list;
    }



    /**
     * 插入沟通交流提醒表数据
     * @designer
     * @since  2014-11-6
     * @author dingwujun
     * @param @param List<PmDevice>
     * @param @return
     * @return null
     */
    public void insertListData(List<SympCommunicationPrompt> parameterObjList)
    {
        if(parameterObjList!=null&&parameterObjList.size()>0)
        {
            List<String> listSql= new ArrayList<String>();
            for(int i=0;i<parameterObjList.size();i++)
            {
                String sql="REPLACE INTO SYMP_COMMUNICATION_PROMPT (" +
                        "ID,THEMEID,REPLYID,PROMPTCONTENT,CREATETIME,STATE,PROMPTPERSONID,PROMPTTYPE" +
                        ") VALUES (" +
                        "'"+parameterObjList.get(i).getId()+"'," +
                        "'"+parameterObjList.get(i).getThemeid()+"'," +
                        "'"+parameterObjList.get(i).getReplyid()+"'," +
                        "'"+parameterObjList.get(i).getPromptcontent()+"'," +
                        "'"+parameterObjList.get(i).getCreatetime()+"'," +
                        "'"+parameterObjList.get(i).getState()+"'," +
                        "'"+parameterObjList.get(i).getPromptpersonid()+"'," +
                        "'"+parameterObjList.get(i).getPrompttype()+"'" +
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
     * 根据条件修改沟通交流提醒表数据
     * @designer
     * @since  2014-11-6
     * @author dingwujun
     * @param @param List<PmDevice>
     * @param @return
     * @return null
     */
    public void udateSympCommunicationPrompt(Map<Object,Object> columnsMap,Map<Object,Object> wheresMap)
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
                 sql="UPDATE SYMP_COMMUNICATION_PROMPT SET " +columns +" WHERE 1=1 "+sqlWhere;

            if(sql!=null&&!sql.equals(""))
            {
                dbHelper.execSQL(sql);
            }
        }
    }

    /**
     * 根据id删除沟通交流提醒表数据
     * @designer
     * @since  2014-11-6
     * @author dingwujun
     * @param @param List<PmDevice>
     * @param @return
     * @return null
     */
    public void deleteSympCommunicationPrompt(String id,String colum)
    {
        String sql="";
        if(id!=null&&!id.equals(""))
        {
            sql="DELETE FROM SYMP_COMMUNICATION_PROMPT WHERE "+colum+"='" +id +"'";
            if(sql!=null&&!sql.equals(""))
            {
                dbHelper.execSQL(sql);
            }
        }
    }

}


