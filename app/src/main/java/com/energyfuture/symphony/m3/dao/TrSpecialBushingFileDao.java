package com.energyfuture.symphony.m3.dao;

import android.content.Context;

import com.energyfuture.symphony.m3.domain.TrSpecialBushingFile;
import com.energyfuture.symphony.m3.util.DBHelper;
import com.energyfuture.symphony.m3.util.SQLHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/3/24.
 */
public class TrSpecialBushingFileDao {
    DBHelper dbHelper = null;

    public TrSpecialBushingFileDao(Context cxt) {
        dbHelper = new DBHelper(cxt);
    }

    public SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    /**
     * 获取套管文件表信息
     *
     * @param @param  null
     * @param @return
     * @return list
     * @designer
     * @author dingwujun
     * @since 2014-11-6
     */
    public List<TrSpecialBushingFile> queryTrSpecialBushingFileList(TrSpecialBushingFile trSpecialBushingDataFileParam) {
        List<TrSpecialBushingFile> list = new ArrayList<TrSpecialBushingFile>();
        String sqlwhere="";
        if(trSpecialBushingDataFileParam.getPositionid() != null && !trSpecialBushingDataFileParam.getPositionid().equals("")){
            sqlwhere += " AND POSITIONID='" + trSpecialBushingDataFileParam.getPositionid() + "'";
        }
        if(trSpecialBushingDataFileParam.getFilenumber() != null && !trSpecialBushingDataFileParam.getFilenumber().equals("")){
            sqlwhere += " AND FILENUMBER='" + trSpecialBushingDataFileParam.getFilenumber() + "'";
        }

        String sql = "SELECT * FROM TR_SPECIAL_BUSHING_FILE WHERE 1=1 "+sqlwhere+" AND ISUPLOAD <>'2' ORDER BY ORDERBY ASC";
        List<Map<String,String>> dblist = dbHelper.selectSQL(sql, null);
        for (int i = 0; i < dblist.size(); i++){
            Map<String, String> m = dblist.get(i);
            TrSpecialBushingFile trSpecialBushingFile = new TrSpecialBushingFile();
            trSpecialBushingFile.setId(m.get("ID"));
            trSpecialBushingFile.setPositionid(m.get("POSITIONID"));
            trSpecialBushingFile.setRequired(m.get("REQUIRED"));
            trSpecialBushingFile.setFilenumber(m.get("FILENUMBER"));
            trSpecialBushingFile.setFilename(m.get("FILENAME"));
            trSpecialBushingFile.setFileurl(m.get("FILEURL"));
            trSpecialBushingFile.setIsupload(m.get("ISUPLOAD"));
            trSpecialBushingFile.setUpdatetime(m.get("UPDATETIME"));
            trSpecialBushingFile.setUpdateperson(m.get("UPDATEPERSON"));
            trSpecialBushingFile.setProjectid(m.get("PROJECTID"));
            trSpecialBushingFile.setTaskid(m.get("TASKID"));
            trSpecialBushingFile.setOrderby(m.get("ORDERBY"));
            list.add(trSpecialBushingFile);
        }
        return list;
    }

    public void insertListData(List<TrSpecialBushingFile> trSpecialBushingFileList)
    {
        if(trSpecialBushingFileList!=null&&trSpecialBushingFileList.size()>0)
        {
            List<String> listSql= new ArrayList<String>();
            for(int i=0;i<trSpecialBushingFileList.size();i++)
            {
                String sql="REPLACE INTO TR_SPECIAL_BUSHING_FILE (" +
                        "ID,POSITIONID,REQUIRED,FILENUMBER,FILENAME,FILEURL,ISUPLOAD,UPDATETIME,PROJECTID,TASKID,ORDERBY,UPDATEPERSON"+
                        ") VALUES (" +
                        "'"+trSpecialBushingFileList.get(i).getId()+"'," +
                        "'"+trSpecialBushingFileList.get(i).getPositionid()+"'," +
                        "'"+trSpecialBushingFileList.get(i).getRequired()+"'," +
                        "'"+trSpecialBushingFileList.get(i).getFilenumber()+"'," +
                        "'"+trSpecialBushingFileList.get(i).getFilename()+"'," +
                        "'"+trSpecialBushingFileList.get(i).getFileurl()+"'," +
                        "'"+trSpecialBushingFileList.get(i).getIsupload()+"'," +
                        "'"+trSpecialBushingFileList.get(i).getUpdatetime()+"'," +
                        "'"+trSpecialBushingFileList.get(i).getProjectid()+"'," +
                        "'"+trSpecialBushingFileList.get(i).getTaskid()+"'," +
                        "'"+trSpecialBushingFileList.get(i).getOrderby()+"'," +
                        "'"+trSpecialBushingFileList.get(i).getUpdateperson()+"'" +
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
     * 根据条件修改套管文件表数据
     * @designer
     * @since  2014-11-6
     * @author dingwujun
     * @param @param List<PmDevice>
     * @param @return
     * @return null
     */
    public boolean updateTrSpecialBushingFileInfo(Map<Object,Object> columnsMap,Map<Object,Object> wheresMap)
    {
        boolean flag = false;
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
            sql="UPDATE TR_SPECIAL_BUSHING_FILE SET " +columns +", orderby = '" + (++SQLHelper.Order) + "' WHERE 1=1 "+sqlWhere;

            if(sql!=null&&!sql.equals("")) {
                dbHelper.execSQL(sql);
                flag = true;
            }
        }
        return flag;
    }

    /**
     * 获取所有待上传的图片信息
     * @param @param  sympImageDataInfo
     * @param @return
     */
    public List<TrSpecialBushingFile> queryFileList() {

        List<TrSpecialBushingFile> list = new ArrayList<TrSpecialBushingFile>();
        String  sqlWhere="(ISUPLOAD = '0' OR ISUPLOAD is null) AND PROJECTID is not null and PROJECTID <> 'null'  AND FILENAME is not null AND FILENAME <> 'null' ";
        String sql = "SELECT * FROM tr_special_bushing_file WHERE "+sqlWhere + " order by UPDATETIME desc LIMIT 50";
        List<Map<String,String>> dblist = dbHelper.selectSQL(sql, null);
        for (int i = 0; i < dblist.size(); i++){
            Map<String, String> m = dblist.get(i);
            TrSpecialBushingFile bean = new TrSpecialBushingFile();
            bean.setId(m.get("ID"));
            bean.setPositionid(m.get("POSITIONID"));
            bean.setRequired(m.get("REQUIRED"));
            bean.setFilenumber(m.get("FILENUMBER"));
            bean.setFilename(m.get("FILENAME"));
            bean.setFileurl(m.get("FILEURL"));
            bean.setUpdatetime(m.get("UPDATETIME"));
            bean.setUpdateperson(m.get("UPDATEPERSON"));
            bean.setIsupload(m.get("ISUPLOAD"));
            bean.setProjectid(m.get("PROJECTID"));
            bean.setTaskid(m.get("TASKID"));
            bean.setOrderby(m.get("ORDERBY"));
            list.add(bean);
        }
        return list;
    }

    /**
     * 修改图片上传状态
     * @designer
     * @since  2014-11-6
     * @author dingwujun
     * @param @param List<PmDevice>
     * @param @return
     * @return null
     */
    public void updateUpload(String id)
    {
        String sql="";
        if(id!=null&&!id.equals(""))
        {
            sql="UPDATE tr_special_bushing_file SET ISUPLOAD = '1' WHERE ID='" +id +"'";
            dbHelper.execSQL(sql);
        }
    }

    //删除套管文件
    public void deleteListData(List<TrSpecialBushingFile> trSpecialBushingFiles){
        if(trSpecialBushingFiles != null && trSpecialBushingFiles.size() > 0){
            {
                List<String> listSql= new ArrayList<String>();
                for(int i=0;i<trSpecialBushingFiles.size();i++)
                {
                    String sql="Delete from TR_SPECIAL_BUSHING_FILE where id='" + trSpecialBushingFiles.get(i).getId() + "'";
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
