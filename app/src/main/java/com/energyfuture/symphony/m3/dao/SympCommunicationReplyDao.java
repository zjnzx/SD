package com.energyfuture.symphony.m3.dao;

import android.content.Context;

import com.energyfuture.symphony.m3.domain.SympCommunicationReply;
import com.energyfuture.symphony.m3.util.DBHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/3/24.
 */
public class SympCommunicationReplyDao {
    DBHelper dbHelper = null;

    public SympCommunicationReplyDao(Context cxt) {
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
    public List<SympCommunicationReply> querySympCommunicationReplyList(SympCommunicationReply parameterObj) {
        List<SympCommunicationReply> list = new ArrayList<SympCommunicationReply>();
        String  sqlWhere="";
        if(parameterObj.getThemeid()!=null&&!parameterObj.getThemeid().equals(""))
        {
            sqlWhere+=" AND THEMEID='"+parameterObj.getThemeid()+"'";
        }
        if(parameterObj.getReplytime()!=null&&!parameterObj.getReplytime().equals(""))
        {
            String replyTime = parameterObj.getReplytime();
            if(replyTime.endsWith(".0")){
                replyTime = replyTime.substring(0,replyTime.indexOf("."));
            }
            sqlWhere+=" AND REPLYTIME > '"+replyTime+"'";
        }
        String sql = "SELECT REPLY.* FROM SYMP_COMMUNICATION_REPLY REPLY WHERE 1=1 "+sqlWhere+ " ORDER BY REPLY.REPLYTIME ASC";
        List<Map<String,String>> dblist = dbHelper.selectSQL(sql, null);
        for (int i = 0; i < dblist.size(); i++){
            Map<String, String> m = dblist.get(i);
            SympCommunicationReply obj = new SympCommunicationReply();
            String replyTime = m.get("REPLYTIME");
            if(replyTime.endsWith(".0")){
                replyTime = replyTime.substring(0,replyTime.indexOf("."));
            }
            obj.setId(m.get("ID"));
            obj.setThemeid(m.get("THEMEID"));
            obj.setReplypersonid(m.get("REPLYPERSONID"));
            obj.setReplycontent(m.get("REPLYCONTENT"));
            obj.setReplytime(replyTime);
            obj.setReplyid(m.get("REPLYID"));
            obj.setReplyobjectid(m.get("REPLYOBJECTID"));
            obj.setState(m.get("STATE"));
            list.add(obj);
        }
        return list;
    }

    /**
     * 查询回复数量
     * @param id
     * @return
     */
    public String querySympCommunicationReplyCount(String id,String state) {
        String count = "0";
        String  sqlWhere="";
        if(id!=null&&!id.equals(""))
        {
            sqlWhere+=" AND THEMEID='"+id+"'";
        }
        if(state!=null&&!state.equals(""))
        {
            sqlWhere+=" AND STATE='"+state+"'";
        }
        String sql = "SELECT COUNT(ID) COUNT FROM SYMP_COMMUNICATION_REPLY WHERE 1=1 "+sqlWhere;
        List<Map<String,String>> dblist = dbHelper.selectSQL(sql, null);
        for (int i = 0; i < dblist.size(); i++){
            Map<String, String> m = dblist.get(i);
            count = m.get("COUNT");
        }
        return count;
    }


    /**
     * 插入沟通交流回复表数据
     * @designer
     * @since  2014-11-6
     * @author dingwujun
     * @param @param List<PmDevice>
     * @param @return
     * @return null
     */
    public void insertListData(SympCommunicationReply parameterObj)
    {
        if(parameterObj!=null)
        {
            String sql="REPLACE INTO SYMP_COMMUNICATION_REPLY (" +
                    "ID,THEMEID,REPLYPERSONID,REPLYCONTENT,REPLYTIME,REPLYID,REPLYOBJECTID,STATE" +
                    ") VALUES (" +
                    "'"+parameterObj.getId()+"'," +
                    "'"+parameterObj.getThemeid()+"'," +
                    "'"+parameterObj.getReplypersonid()+"'," +
                    "'"+parameterObj.getReplycontent()+"'," +
                    "'"+parameterObj.getReplytime()+"'," +
                    "'"+parameterObj.getReplyid()+"'," +
                    "'"+parameterObj.getReplyobjectid()+"'," +
                    "'"+parameterObj.getState()+"'" +
                    ")";
            if(sql!=null&&!sql.equals("")){
                dbHelper.execSQL(sql);
            }
        }
    }


    /**
     * 根据条件修改沟通交流回复表数据
     * @designer
     * @since  2014-11-6
     * @author dingwujun
     * @param @param List<PmDevice>
     * @param @return
     * @return null
     */
    public void udateSympCommunicationReply(Map<Object,Object> columnsMap,Map<Object,Object> wheresMap)
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
                 sql="UPDATE SYMP_COMMUNICATION_REPLY SET " +columns +" WHERE 1=1 "+sqlWhere;

            if(sql!=null&&!sql.equals(""))
            {
                dbHelper.execSQL(sql);
            }
        }
    }

    /**
     * 根据id删除沟通交流回复表数据
     * @designer
     * @since  2014-11-6
     * @author dingwujun
     * @param @param List<PmDevice>
     * @param @return
     * @return null
     */
    public void deleteSympCommunicationReply(String id,String colum)
    {
        String sql="";
        if(id!=null&&!id.equals(""))
        {
            sql="DELETE FROM SYMP_COMMUNICATION_REPLY WHERE "+ colum +" = '" +id +"'";
            if(sql!=null&&!sql.equals(""))
            {
                dbHelper.execSQL(sql);
            }
        }
    }
}


