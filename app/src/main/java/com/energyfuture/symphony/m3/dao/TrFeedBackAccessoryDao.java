package com.energyfuture.symphony.m3.dao;

import android.content.Context;

import com.energyfuture.symphony.m3.domain.TrFeedBackAccessory;
import com.energyfuture.symphony.m3.util.DBHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/11/25.
 */
public class TrFeedBackAccessoryDao {
    DBHelper dbHelper = null;

    public TrFeedBackAccessoryDao(Context cxt) {
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
    public List<TrFeedBackAccessory> queryTrFeedBackAccessoryList(TrFeedBackAccessory parameterObj) {
        List<TrFeedBackAccessory> list = new ArrayList<TrFeedBackAccessory>();
        String  sqlWhere="";
        if(parameterObj.getReplyid()!=null&&!parameterObj.getReplyid().equals(""))
        {
            sqlWhere+=" AND REPLYID='"+parameterObj.getReplyid()+"'";
        }
        String sql = "SELECT * FROM TR_FEEDBACK_ACCESSORY WHERE 1=1 "+sqlWhere;
        List<Map<String,String>> dblist = dbHelper.selectSQL(sql, null);
        for (int i = 0; i < dblist.size(); i++){
            Map<String, String> m = dblist.get(i);
            TrFeedBackAccessory obj = new TrFeedBackAccessory();
            obj.setId(m.get("ID"));
            obj.setThemeid(m.get("THEMEID"));
            obj.setReplyid(m.get("REPLYID"));
            obj.setFileurl(m.get("FILEURL"));
            obj.setFilename(m.get("FILENAME"));
            obj.setRecordtime(m.get("RECORDTIME"));
            obj.setAccessorytype(m.get("ACCESSORYTYPE"));
            list.add(obj);
            }
        return list;
    }

    /**
     * 插入沟通交流附件表数据
     * @designer
     * @since  2014-11-6
     * @author dingwujun
     * @param @param List<PmDevice>
     * @param @return
     * @return null
     */
    public void insertListData(List<TrFeedBackAccessory> parameterObjList)
    {
        if(parameterObjList!=null&&parameterObjList.size()>0)
        {
            List<String> listSql= new ArrayList<String>();
            for(int i=0;i<parameterObjList.size();i++)
            {
                String sql="REPLACE INTO TR_FEEDBACK_ACCESSORY (" +
                        "ID,THEMEID,REPLYID,FILEURL,FILENAME,RECORDTIME,ACCESSORYTYPE,ISUPLOAD" +
                        ") VALUES (" +
                        "'"+parameterObjList.get(i).getId()+"'," +
                        "'"+parameterObjList.get(i).getThemeid()+"'," +
                        "'"+parameterObjList.get(i).getReplyid()+"'," +
                        "'"+parameterObjList.get(i).getFileurl()+"'," +
                        "'"+parameterObjList.get(i).getFilename()+"'," +
                        "'"+parameterObjList.get(i).getRecordtime()+"'," +
                        "'"+parameterObjList.get(i).getAccessorytype()+"'," +
                        "'"+parameterObjList.get(i).getIsupload()+"'" +
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
     * 根据id删除沟通交流附件表沟通交流附件表数据
     * @designer
     * @since  2014-11-6
     * @author dingwujun
     * @param @param List<PmDevice>
     * @param @return
     * @return null
     */
    public void deleteTrFeedBackAccessory(String id)
    {
        String sql="";
        if(id!=null&&!id.equals(""))
        {
            sql="DELETE FROM TR_FEEDBACK_ACCESSORY WHERE REPLYID='" +id +"'";
            if(sql!=null&&!sql.equals(""))
            {
                dbHelper.execSQL(sql);
            }
        }
    }

}


