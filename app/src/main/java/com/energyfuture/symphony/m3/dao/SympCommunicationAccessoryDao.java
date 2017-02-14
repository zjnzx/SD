package com.energyfuture.symphony.m3.dao;

import android.content.Context;

import com.energyfuture.symphony.m3.domain.SympCommunicationAccessory;
import com.energyfuture.symphony.m3.util.DBHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/3/24.
 */
public class SympCommunicationAccessoryDao {
    DBHelper dbHelper = null;

    public SympCommunicationAccessoryDao(Context cxt) {
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
    public List<SympCommunicationAccessory> querySympCommunicationAccessoryList(SympCommunicationAccessory parameterObj) {
        List<SympCommunicationAccessory> list = new ArrayList<SympCommunicationAccessory>();
        String  sqlWhere="";
        if(parameterObj.getReplyid()!=null&&!parameterObj.getReplyid().equals(""))
        {
            sqlWhere+=" AND REPLYID='"+parameterObj.getReplyid()+"'";
        }
        if(parameterObj.getThemeid()!=null&&!parameterObj.getThemeid().equals(""))
        {
            sqlWhere=" AND THEMEID='"+parameterObj.getThemeid()+"' AND REPLYID=''or REPLYID=null ";
        }
        String sql = "SELECT * FROM SYMP_COMMUNICATION_ACCESSORY WHERE 1=1 "+sqlWhere;
        List<Map<String,String>> dblist = dbHelper.selectSQL(sql, null);
        for (int i = 0; i < dblist.size(); i++){
            Map<String, String> m = dblist.get(i);
            SympCommunicationAccessory obj = new SympCommunicationAccessory();
                obj.setId(m.get("ID"));
                obj.setThemeid(m.get("THEMEID"));
                obj.setReplyid(m.get("REPLYID"));
                obj.setFileurl(m.get("FILEURL"));
                obj.setFilename(m.get("FILENAME"));
                obj.setRemarks(m.get("REMARKS"));
                obj.setRecordtime(m.get("RECORDTIME"));
                obj.setAccessorytype(m.get("ACCESSORYTYPE"));
                list.add(obj);
            }
        return list;
    }
    //shanchufujianbiaoxinxichaxun
    public List<SympCommunicationAccessory> querySympCommunicationAccessoryList1(SympCommunicationAccessory parameterObj) {
        List<SympCommunicationAccessory> list = new ArrayList<SympCommunicationAccessory>();
        String  sqlWhere="";
        if(parameterObj.getReplyid()!=null&&!parameterObj.getReplyid().equals(""))
        {
            sqlWhere+=" AND REPLYID='"+parameterObj.getReplyid()+"'";
        }
        if(parameterObj.getThemeid()!=null&&!parameterObj.getThemeid().equals(""))
        {
            sqlWhere=" AND THEMEID='"+parameterObj.getThemeid()+"'";
        }
        String sql = "SELECT * FROM SYMP_COMMUNICATION_ACCESSORY WHERE 1=1 "+sqlWhere;
        List<Map<String,String>> dblist = dbHelper.selectSQL(sql, null);
        for (int i = 0; i < dblist.size(); i++){
            Map<String, String> m = dblist.get(i);
            SympCommunicationAccessory obj = new SympCommunicationAccessory();
                obj.setId(m.get("ID"));
                obj.setThemeid(m.get("THEMEID"));
                obj.setReplyid(m.get("REPLYID"));
                obj.setFileurl(m.get("FILEURL"));
                obj.setFilename(m.get("FILENAME"));
                obj.setRemarks(m.get("REMARKS"));
                obj.setRecordtime(m.get("RECORDTIME"));
                obj.setAccessorytype(m.get("ACCESSORYTYPE"));
                list.add(obj);
            }
        return list;
    }

    /**
     * 获取所有待上传的图片信息
     * @param @param  sympImageDataInfo
     * @param @return
     */
    public List<SympCommunicationAccessory> queryAccessoryFileList() {

        List<SympCommunicationAccessory> list = new ArrayList<SympCommunicationAccessory>();
        String  sqlWhere="(ISUPLOAD = '0' OR ISUPLOAD is null) AND THEMEID is not null and THEMEID <> 'null'  AND FILENAME is not null AND FILENAME <> 'null' LIMIT 20";
        String sql = "SELECT * FROM SYMP_COMMUNICATION_ACCESSORY WHERE "+sqlWhere;
        List<Map<String,String>> dblist = dbHelper.selectSQL(sql, null);
        for (int i = 0; i < dblist.size(); i++){
            Map<String, String> m = dblist.get(i);
            SympCommunicationAccessory bean = new SympCommunicationAccessory();
            bean.setId(m.get("ID"));
            bean.setThemeid(m.get("THEMEID"));
            bean.setReplyid(m.get("REPLYID"));
            bean.setFileurl(m.get("FILEURL"));
            bean.setFilename(m.get("FILENAME"));
            bean.setRemarks(m.get("REMARKS"));
            bean.setRecordtime(m.get("RECORDTIME"));
            bean.setAccessorytype(m.get("ACCESSORYTYPE"));
            bean.setIsupload(m.get("ISUPLOAD"));
            list.add(bean);
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
    public void insertListData(List<SympCommunicationAccessory> parameterObjList)
    {
        if(parameterObjList!=null&&parameterObjList.size()>0)
        {
            List<String> listSql= new ArrayList<String>();
            for(int i=0;i<parameterObjList.size();i++)
            {
                String sql="REPLACE INTO SYMP_COMMUNICATION_ACCESSORY (" +
                        "ID,THEMEID,REPLYID,FILEURL,FILENAME,REMARKS,RECORDTIME,ACCESSORYTYPE" +
                        ") VALUES (" +
                        "'"+parameterObjList.get(i).getId()+"'," +
                        "'"+parameterObjList.get(i).getThemeid()+"'," +
                        "'"+parameterObjList.get(i).getReplyid()+"'," +
                        "'"+parameterObjList.get(i).getFileurl()+"'," +
                        "'"+parameterObjList.get(i).getFilename()+"'," +
                        "'"+parameterObjList.get(i).getRemarks()+"'," +
                        "'"+parameterObjList.get(i).getRecordtime()+"'," +
                        "'"+parameterObjList.get(i).getAccessorytype()+"'" +
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
     * 根据条件修改沟通交流附件表数据
     * @designer
     * @since  2014-11-6
     * @author dingwujun
     * @param @param List<PmDevice>
     * @param @return
     * @return null
     */
    public void udateSympCommunicationAccessory(Map<Object,Object> columnsMap,Map<Object,Object> wheresMap)
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
                 sql="UPDATE SYMP_COMMUNICATION_ACCESSORY SET " +columns +" WHERE 1=1 "+sqlWhere;

            if(sql!=null&&!sql.equals(""))
            {
                dbHelper.execSQL(sql);
            }
        }
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
            sql="UPDATE SYMP_COMMUNICATION_ACCESSORY SET ISUPLOAD = '1' WHERE ID='" +id +"'";
            dbHelper.execSQL(sql);
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
    public void deleteSympCommunicationAccessory(String id,String colum)
    {
        String sql="";
        if(id!=null&&!id.equals(""))
        {
            sql="DELETE FROM SYMP_COMMUNICATION_ACCESSORY WHERE "+colum+"='" +id +"'";
            if(sql!=null&&!sql.equals(""))
            {
                dbHelper.execSQL(sql);
            }
        }
    }

}


