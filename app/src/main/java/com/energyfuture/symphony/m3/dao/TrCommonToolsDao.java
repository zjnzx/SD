package com.energyfuture.symphony.m3.dao;

import android.content.Context;

import com.energyfuture.symphony.m3.domain.TrCommonTools;
import com.energyfuture.symphony.m3.util.DBHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/3/24.
 */
public class TrCommonToolsDao {
    DBHelper dbHelper = null;

    public TrCommonToolsDao(Context cxt) {
        dbHelper = new DBHelper(cxt);
    }

    public SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 常用工具表信息
     *
     * @param @param  null
     * @param @return
     * @return list
     * @designer
     * @author dingwujun
     * @since 2014-11-6
     */
    public List<TrCommonTools> queryTrCommonToolsList(TrCommonTools trCommonToolsParam) {
        List<TrCommonTools> list = new ArrayList<TrCommonTools>();
        String sqlwhere="";
        if(trCommonToolsParam != null){
            if(trCommonToolsParam.getType() != null && !trCommonToolsParam.getType().equals("")){
                sqlwhere+=" AND TYPE='"+trCommonToolsParam.getType()+"'";
            }
            if(trCommonToolsParam.getFilename() != null && !trCommonToolsParam.getFilename().equals("")){
                sqlwhere+=" AND FILENAME='"+trCommonToolsParam.getFilename()+"'";
            }
        }
        
        String sql = "SELECT * FROM TR_COMMONTOOLS WHERE 1=1 "+sqlwhere;
        List<Map<String,String>> dblist = dbHelper.selectSQL(sql, null);
        for (int i = 0; i < dblist.size(); i++){
            Map<String, String> m = dblist.get(i);
            TrCommonTools trCommonTools = new TrCommonTools();
            trCommonTools.setId(m.get("ID"));
            trCommonTools.setFilename(m.get("FILENAME"));
            trCommonTools.setFileurl(m.get("FILEURL"));
            trCommonTools.setUploadpersonid(m.get("UPLOADPERSONID"));
            trCommonTools.setUploadtime(m.get("UPLOADTIME"));
            trCommonTools.setRemarks(m.get("REMARKS"));
            trCommonTools.setFilesize(m.get("FILESIZE"));
            trCommonTools.setType(m.get("TYPE"));
            list.add(trCommonTools);
        }
        return list;
    }

    public void insertListData(List<TrCommonTools> trCommonToolsList)
    {
        if(trCommonToolsList!=null&&trCommonToolsList.size()>0)
        {
            List<String> listSql= new ArrayList<String>();
            for(int i=0;i<trCommonToolsList.size();i++)
            {
                String sql="REPLACE INTO TR_COMMONTOOLS (" +
                        "ID,FILENAME,FILEURL,UPLOADPERSONID,UPLOADTIME,REMARKS,FILESIZE,TYPE"+
                        ") VALUES (" +
                        "'"+trCommonToolsList.get(i).getId()+"'," +
                        "'"+trCommonToolsList.get(i).getFilename()+"'," +
                        "'"+trCommonToolsList.get(i).getFileurl()+"'," +
                        "'"+trCommonToolsList.get(i).getUploadpersonid()+"'," +
                        "'"+trCommonToolsList.get(i).getUploadtime()+"'," +
                        "'"+trCommonToolsList.get(i).getRemarks()+"'," +
                        "'"+trCommonToolsList.get(i).getFilesize()+"'," +
                        "'"+trCommonToolsList.get(i).getType()+"'" +
                        ")";
                listSql.add(sql);
            }
            if(listSql!=null&&listSql.size()>0)
            {
                dbHelper.insertSQLAll(listSql);
            }
        }
    }

}
