package com.energyfuture.symphony.m3.dao;

import android.content.Context;

import com.energyfuture.symphony.m3.domain.TrDetectiontempletFile;
import com.energyfuture.symphony.m3.util.DBHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/3/24.
 */
public class TrDetectionTempletFileDao {
    DBHelper dbHelper = null;

    public TrDetectionTempletFileDao(Context cxt) {
        dbHelper = new DBHelper(cxt);
    }

    public SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 获取文件模板表信息
     *
     * @param @param  null
     * @param @return
     * @return list
     * @designer
     * @author dingwujun
     * @since 2014-11-6
     */
    public List<TrDetectiontempletFile> queryTrDetectionTempletFileList(TrDetectiontempletFile trDetectiontempletFileParam) {
        List<TrDetectiontempletFile> list = new ArrayList<TrDetectiontempletFile>();
        String sqlwhere="";
        
        String sql = "SELECT * FROM TR_DETECTIONTEMPLET_FILE WHERE 1=1 "+sqlwhere;
        List<Map<String,String>> dblist = dbHelper.selectSQL(sql, null);
        for (int i = 0; i < dblist.size(); i++){
            Map<String, String> m = dblist.get(i);
            TrDetectiontempletFile trDetectionTempletFile = new TrDetectiontempletFile();
            trDetectionTempletFile.setId(m.get("ID"));
            trDetectionTempletFile.setTempletid(m.get("TEMPLETID"));
            trDetectionTempletFile.setRequired(m.get("REQUIRED"));
            trDetectionTempletFile.setFiletype(m.get("FILETYPE"));
            trDetectionTempletFile.setUpdatetime(m.get("UPDATETIME"));
            trDetectionTempletFile.setUpdateperson(m.get("UPDATEPERSON"));
            trDetectionTempletFile.setDescribetion(m.get("DESCRIBETION"));
            list.add(trDetectionTempletFile);
        }
        return list;
    }

    public void insertListData(List<TrDetectiontempletFile> trDetectionTempletFileList)
    {
        if(trDetectionTempletFileList!=null&&trDetectionTempletFileList.size()>0)
        {
            List<String> listSql= new ArrayList<String>();
            for(int i=0;i<trDetectionTempletFileList.size();i++)
            {
                String sql="REPLACE INTO TR_DETECTIONTEMPLET_FILE (" +
                        "ID,TEMPLETID,REQUIRED,FILETYPE,UPDATETIME,UPDATEPERSON,DESCRIBETION"+
                        ") VALUES (" +
                        "'"+trDetectionTempletFileList.get(i).getId()+"'," +
                        "'"+trDetectionTempletFileList.get(i).getTempletid()+"'," +
                        "'"+trDetectionTempletFileList.get(i).getRequired()+"'," +
                        "'"+trDetectionTempletFileList.get(i).getFiletype()+"'," +
                        "'"+trDetectionTempletFileList.get(i).getUpdatetime()+"'," +
                        "'"+trDetectionTempletFileList.get(i).getUpdateperson()+"'," +
                        "'"+trDetectionTempletFileList.get(i).getDescribetion()+"'" +
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
     * 根据条件修改文件模板表数据
     * @designer
     * @since  2014-11-6
     * @author dingwujun
     * @param @param List<PmDevice>
     * @param @return
     * @return null
     */
    public void updateTrDetectionTempletFileInfo(Map<Object,Object> columnsMap,Map<Object,Object> wheresMap)
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
            sql="UPDATE TR_DETECTIONTEMPLET_FILE SET " +columns +" WHERE 1=1 "+sqlWhere;

            if(sql!=null&&!sql.equals("")) {
                dbHelper.execSQL(sql);
            }
        }
    }

}
