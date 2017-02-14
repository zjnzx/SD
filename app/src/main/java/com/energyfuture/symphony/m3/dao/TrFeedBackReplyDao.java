package com.energyfuture.symphony.m3.dao;

import android.content.Context;

import com.energyfuture.symphony.m3.domain.TrFeedBackReply;
import com.energyfuture.symphony.m3.util.DBHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/11/25.
 */
public class TrFeedBackReplyDao {
    DBHelper dbHelper = null;

    public TrFeedBackReplyDao(Context cxt) {
        dbHelper = new DBHelper(cxt);
    }

    public SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    /**
     *
     * @param @return
     * @return list
     * @designer
     * @author
     * @since 2015-11-25
     */
    public List<TrFeedBackReply> queryTrFeedBackReplyList(TrFeedBackReply parameterObj) {
        List<TrFeedBackReply> list = new ArrayList<TrFeedBackReply>();
        String  sqlWhere="";
        if(parameterObj.getReplypersonid()!=null&&!parameterObj.getReplypersonid().equals(""))
        {
            sqlWhere+=" AND THEMEID = '"+parameterObj.getThemeid()+"' AND (REPLYPERSONID='"+parameterObj.getReplypersonid()+"' OR REPLYPERSONID='guest')";
        }
        if(parameterObj.getReplytime()!=null&&!parameterObj.getReplytime().equals(""))
        {
            sqlWhere+=" AND REPLYTIME > '"+parameterObj.getReplytime()+"'";
        }
        String sql = "SELECT REPLY.* FROM TR_FEEDBACK_REPLY REPLY WHERE 1=1 "+sqlWhere+ " ORDER BY REPLY.REPLYTIME ASC";
        List<Map<String,String>> dblist = dbHelper.selectSQL(sql, null);
        for (int i = 0; i < dblist.size(); i++){
            Map<String, String> m = dblist.get(i);
            TrFeedBackReply obj = new TrFeedBackReply();
            obj.setId(m.get("ID"));
            obj.setThemeid(m.get("THEMEID"));
            obj.setReplypersonid(m.get("REPLYPERSONID"));
            obj.setReplycontent(m.get("REPLYCONTENT"));
            obj.setReplytime(m.get("REPLYTIME"));
            obj.setState(m.get("STATE"));
            list.add(obj);
        }
        return list;
    }

    /**
     * 查询回复数量
     * @param loginUserId
     * @return
     */
    public String queryTrFeedBackReplyCount(String loginUserId,String themeId) {
        String count = "0";
        String  sqlWhere="";
        if(loginUserId!=null&&!loginUserId.equals(""))
        {
            sqlWhere+=" AND THEMEID = '"+themeId+"' AND  (REPLYPERSONID='"+loginUserId+"' OR REPLYPERSONID='guest')";
        }
        String sql = "SELECT COUNT(ID) COUNT FROM TR_FEEDBACK_REPLY WHERE 1=1 "+sqlWhere;
        List<Map<String,String>> dblist = dbHelper.selectSQL(sql, null);
        for (int i = 0; i < dblist.size(); i++){
            Map<String, String> m = dblist.get(i);
            count = m.get("COUNT");
        }
        return count;
    }


    /**
     * 插入反馈回复表数据
     * @designer
     * @since  2015-11-25
     * @author
     * @param @param List<PmDevice>
     * @param @return
     * @return null
     */
    public void insertListData(TrFeedBackReply parameterObj)
    {
        if(parameterObj!=null)
        {
            String sql="REPLACE INTO TR_FEEDBACK_REPLY (" +
                    "ID,THEMEID,REPLYPERSONID,REPLYCONTENT,REPLYTIME,STATE" +
                    ") VALUES (" +
                    "'"+parameterObj.getId()+"'," +
                    "'"+parameterObj.getThemeid()+"'," +
                    "'"+parameterObj.getReplypersonid()+"'," +
                    "'"+parameterObj.getReplycontent()+"'," +
                    "'"+parameterObj.getReplytime()+"'," +
                    "'"+parameterObj.getState()+"'" +
                    ")";
            if(sql!=null&&!sql.equals("")){
                dbHelper.execSQL(sql);
            }
        }
    }


    /**
     * 根据条件修改回复表数据
     * @designer
     * @since  2015-11-25
     * @author
     * @param @param List<PmDevice>
     * @param @return
     * @return null
     */
    public void udateTrFeedBackReply(Map<Object,Object> columnsMap,Map<Object,Object> wheresMap)
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
                 sql="UPDATE TR_FEEDBACK_REPLY SET " +columns +" WHERE 1=1 "+sqlWhere;

            if(sql!=null&&!sql.equals(""))
            {
                dbHelper.execSQL(sql);
            }
        }
    }

    /**
     * 根据id删除回复表数据
     * @designer
     * @since  2014-11-6
     * @author dingwujun
     * @param @param List<PmDevice>
     * @param @return
     * @return null
     */
    public void deleteTrFeedBackReply(String id)
    {
        String sql="";
        if(id!=null&&!id.equals(""))
        {
            sql="DELETE FROM TR_FEEDBACK_REPLY WHERE ID='" +id +"'";
            if(sql!=null&&!sql.equals(""))
            {
                dbHelper.execSQL(sql);
            }
        }
    }
    /**
     * 根据id删除沟通交流主题回复表数据
     * @designer
     * @since  2015-11-25
     * @author
     * @param @param List<PmDevice>
     * @param @return
     * @return null
     */
    public void deleteSympFeedBackThemeReply(String id)
    {
        String sql="";
        if(id!=null&&!id.equals(""))
        {
            sql="DELETE FROM SYMP_FEEDBACK_REPLY WHERE THEMEID='" +id +"'";
            if(sql!=null&&!sql.equals(""))
            {
                dbHelper.execSQL(sql);
            }
        }
    }
}


