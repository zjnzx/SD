package com.energyfuture.symphony.m3.dao;

import android.content.Context;

import com.energyfuture.symphony.m3.domain.TrDetectiontempletFileObj;
import com.energyfuture.symphony.m3.util.DBHelper;
import com.energyfuture.symphony.m3.util.SQLHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/3/24.
 */
public class TrDetectionTempletFileObjDao {
    DBHelper dbHelper = null;

    public TrDetectionTempletFileObjDao(Context cxt) {
        dbHelper = new DBHelper(cxt);
    }

    public SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 获取文件模板实体表信息
     *
     * @param @param  null
     * @param @return
     * @return list
     * @designer
     * @author dingwujun
     * @since 2014-11-6
     */
    public List<TrDetectiontempletFileObj> queryTrDetectionTempletFileObjList(TrDetectiontempletFileObj trDetectionTempletFIleObjParam) {
        List<TrDetectiontempletFileObj> list = new ArrayList<TrDetectiontempletFileObj>();
        String sqlwhere="";
        String sqlwhere2="";

        if(trDetectionTempletFIleObjParam.getFilename()!= null&&!trDetectionTempletFIleObjParam.getFilename().equals("")){
            sqlwhere+=" AND FILENAME='"+trDetectionTempletFIleObjParam.getFilename()+"'";
        }

        if(trDetectionTempletFIleObjParam.getDetectionobjid() != null && !trDetectionTempletFIleObjParam.getDetectionobjid().equals("")){
            sqlwhere+=" AND DETECTIONOBJID='"+trDetectionTempletFIleObjParam.getDetectionobjid()+"'";
        }

        if(trDetectionTempletFIleObjParam.getId() != null && !trDetectionTempletFIleObjParam.getId().equals("")){
            sqlwhere+=" AND ID='"+trDetectionTempletFIleObjParam.getId()+"'";
        }

        if(trDetectionTempletFIleObjParam.getSignid() != null && !trDetectionTempletFIleObjParam.getSignid().equals("")){
            sqlwhere+=" AND SIGNID='"+trDetectionTempletFIleObjParam.getSignid()+"'";
            sqlwhere2+=" ORDER BY ORDERBY";
        }


        if(trDetectionTempletFIleObjParam.getFilenumber() != null && !trDetectionTempletFIleObjParam.getFilenumber().equals("")){
            sqlwhere+=" AND FILENUMBER LIKE '"+trDetectionTempletFIleObjParam.getFilenumber()+"%'";
        }

        String sql = "SELECT * FROM TR_DETECTIONTEMPLET_FILE_OBJ WHERE 1=1 "+sqlwhere + " AND ISUPLOAD <>'2'" + sqlwhere2;
        List<Map<String,String>> dblist = dbHelper.selectSQL(sql, null);
        for (int i = 0; i < dblist.size(); i++){
            Map<String, String> m = dblist.get(i);
            TrDetectiontempletFileObj trDetectionTempletFileObj = new TrDetectiontempletFileObj();
            trDetectionTempletFileObj.setId(m.get("ID"));
            trDetectionTempletFileObj.setDetectionobjid(m.get("DETECTIONOBJID"));
            trDetectionTempletFileObj.setRequired(m.get("REQUIRED"));
            trDetectionTempletFileObj.setUpdatetime(m.get("UPDATETIME"));
            trDetectionTempletFileObj.setUpdateperson(m.get("UPDATEPERSON"));
            trDetectionTempletFileObj.setDescribetion(m.get("DESCRIBETION"));
            trDetectionTempletFileObj.setTempletname(m.get("TEMPLETNAME"));
            trDetectionTempletFileObj.setTemplettype(m.get("TEMPLETTYPE"));
            trDetectionTempletFileObj.setFilenumber(m.get("FILENUMBER"));
            trDetectionTempletFileObj.setFilename(m.get("FILENAME"));
            trDetectionTempletFileObj.setFileurl(m.get("FILEURL"));
            trDetectionTempletFileObj.setProjectid(m.get("PROJECTID"));
            trDetectionTempletFileObj.setTaskid(m.get("TASKID"));
            trDetectionTempletFileObj.setIsupload(m.get("ISUPLOAD"));
            trDetectionTempletFileObj.setSignid(m.get("SIGNID"));
            trDetectionTempletFileObj.setOrderby(m.get("ORDERBY"));
            list.add(trDetectionTempletFileObj);
        }
        return list;
    }

    /**
     * 获取红外下一组图片的id
     *
     * @param @param  null
     * @param @return
     * @return list
     * @designer
     * @author dingwujun
     * @since 2014-11-6
     */
    public List<String> queryTrDetectionTempletFileId(TrDetectiontempletFileObj trDetectionTempletFIleObjParam) {
        List<String> list = new ArrayList<String>();
        String sqlwhere="";

        if(trDetectionTempletFIleObjParam.getDetectionobjid() != null && !trDetectionTempletFIleObjParam.getDetectionobjid().equals("")){
            sqlwhere+=" AND DETECTIONOBJID='"+trDetectionTempletFIleObjParam.getDetectionobjid()+"'";
        }
        String sql = "SELECT SIGNID FROM TR_DETECTIONTEMPLET_FILE_OBJ WHERE 1=1 "+sqlwhere + " AND ISUPLOAD <>'2' GROUP BY SIGNID ORDER BY UPDATETIME";
        List<Map<String,String>> dblist = dbHelper.selectSQL(sql, null);
        for (int i = 0; i < dblist.size(); i++){
            Map<String, String> m = dblist.get(i);
            String id = m.get("SIGNID");
            list.add(id);
        }
        return list;
    }

    /**
     * 获取文件模板实体表的图片组数
     *
     * @param @param  null
     * @param @return
     * @return list
     * @designer
     * @author dingwujun
     * @since 2014-11-6
     */
    public String queryTrDetectionTempletFileObjCount(TrDetectiontempletFileObj trDetectionTempletFIleObjParam) {
        List<TrDetectiontempletFileObj> list = new ArrayList<TrDetectiontempletFileObj>();
        String count = "";
        String sqlwhere="";

        if(trDetectionTempletFIleObjParam.getSignid()!= null&&!trDetectionTempletFIleObjParam.getSignid().equals("")){
            sqlwhere+=" AND SIGNID='"+trDetectionTempletFIleObjParam.getSignid()+"'";
        }

        String sql = "SELECT DISTINCT(SIGNID) COUNT FROM TR_DETECTIONTEMPLET_FILE_OBJ WHERE 1=1 "+sqlwhere ;
        List<Map<String,String>> dblist = dbHelper.selectSQL(sql, null);
        for (int i = 0; i < dblist.size(); i++){
            Map<String, String> m = dblist.get(i);
            count = m.get("COUNT");
        }
        return count;
    }

    public void insertListData(List<TrDetectiontempletFileObj> trDetectionTempletFileObjList)
    {
        if(trDetectionTempletFileObjList!=null&&trDetectionTempletFileObjList.size()>0)
        {
            List<String> listSql= new ArrayList<String>();
            for(int i=0;i<trDetectionTempletFileObjList.size();i++)
            {
                String sql="REPLACE INTO TR_DETECTIONTEMPLET_FILE_OBJ (" +
                        "ID,DETECTIONOBJID,REQUIRED,UPDATETIME,UPDATEPERSON,DESCRIBETION,TEMPLETNAME,TEMPLETTYPE,FILENUMBER,FILENAME,FILEURL,PROJECTID,TASKID,ISUPLOAD,SIGNID,ORDERBY"+
                        ") VALUES (" +
                        "'"+trDetectionTempletFileObjList.get(i).getId()+"'," +
                        "'"+trDetectionTempletFileObjList.get(i).getDetectionobjid()+"'," +
                        "'"+trDetectionTempletFileObjList.get(i).getRequired()+"'," +
                        "'"+trDetectionTempletFileObjList.get(i).getUpdatetime()+"'," +
                        "'"+trDetectionTempletFileObjList.get(i).getUpdateperson()+"'," +
                        "'"+trDetectionTempletFileObjList.get(i).getDescribetion()+"'," +
                        "'"+trDetectionTempletFileObjList.get(i).getTempletname()+"'," +
                        "'"+trDetectionTempletFileObjList.get(i).getTemplettype()+"'," +
                        "'"+trDetectionTempletFileObjList.get(i).getFilenumber()+"'," +
                        "'"+trDetectionTempletFileObjList.get(i).getFilename()+"'," +
                        "'"+trDetectionTempletFileObjList.get(i).getFileurl()+"'," +
                        "'"+trDetectionTempletFileObjList.get(i).getProjectid()+"'," +
                        "'"+trDetectionTempletFileObjList.get(i).getTaskid()+"'," +
                        "'"+trDetectionTempletFileObjList.get(i).getIsupload()+"'," +
                        "'"+trDetectionTempletFileObjList.get(i).getSignid()+"'," +
                        "'"+trDetectionTempletFileObjList.get(i).getOrderby()+"'" +
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
     * 根据条件修改文件模板实体表数据
     * @designer
     * @since  2014-11-6
     * @author dingwujun
     * @param @param List<PmDevice>
     * @param @return
     * @return null
     */
    public void updateTrDetectionTempletFileObjInfo(Map<Object,Object> columnsMap,Map<Object,Object> wheresMap)
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
            sql="UPDATE TR_DETECTIONTEMPLET_FILE_OBJ SET " +columns +", orderby = '" + (++SQLHelper.Order) + "' WHERE 1=1 "+sqlWhere;

            if(sql!=null&&!sql.equals("")) {
                dbHelper.execSQL(sql);
            }
        }
    }

    /**
     * 获取所有待上传的图片信息
     * @param @param  sympImageDataInfo
     * @param @return
     */
    public List<TrDetectiontempletFileObj> queryFileList() {

        List<TrDetectiontempletFileObj> list = new ArrayList<TrDetectiontempletFileObj>();
        String  sqlWhere="(ISUPLOAD = '0' OR ISUPLOAD is null) AND PROJECTID is not null and PROJECTID <> 'null'  AND FILENAME is not null AND FILENAME <> 'null' ";
        String sql = "SELECT * FROM TR_DETECTIONTEMPLET_FILE_OBJ WHERE "+sqlWhere +" order by UPDATETIME desc LIMIT 50";
        List<Map<String,String>> dblist = dbHelper.selectSQL(sql, null);
        for (int i = 0; i < dblist.size(); i++){
            Map<String, String> m = dblist.get(i);
            TrDetectiontempletFileObj bean = new TrDetectiontempletFileObj();
            bean.setId(m.get("ID"));
            bean.setDetectionobjid(m.get("DETECTIONOBJID"));
            bean.setTempletname(m.get("TEMPLETNAME"));
            bean.setTemplettype(m.get("TEMPLETTYPE"));
            bean.setDescribetion(m.get("DESCRIBETION"));
            bean.setRequired(m.get("REQUIRED"));
            bean.setFilenumber(m.get("FILENUMBER"));
            bean.setFilename(m.get("FILENAME"));
            bean.setFileurl(m.get("FILEURL"));
            bean.setUpdatetime(m.get("UPDATETIME"));
            bean.setUpdateperson(m.get("UPDATEPERSON"));
            bean.setIsupload(m.get("ISUPLOAD"));
            bean.setProjectid(m.get("PROJECTID"));
            bean.setTaskid(m.get("TASKID"));
            bean.setSignid(m.get("SIGNID"));
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
            sql="UPDATE TR_DETECTIONTEMPLET_FILE_OBJ SET ISUPLOAD = '1' WHERE ID='" +id +"'";
            dbHelper.execSQL(sql);
        }
    }
    public void deleteFile(TrDetectiontempletFileObj trDetectionTempletFIleObjParam){
        String sqlwhere="";

        if(trDetectionTempletFIleObjParam.getId()!= null&&!trDetectionTempletFIleObjParam.getId().equals("")){
            sqlwhere+=" AND ID='"+trDetectionTempletFIleObjParam.getId()+"'";
            String sql = "DELETE FROM TR_DETECTIONTEMPLET_FILE_OBJ WHERE 1=1 "+sqlwhere;
            dbHelper.execSQL(sql);
        }
    }
    public List<String> getnoImageValue(String taskid){
        String sql="SELECT t1.* FROM tr_detectiontemplet_file_obj t1 " +
                "where (t1.FILENUMBER != 'null' and  t1.FILENUMBER != '') " +
                "and (t1.FILENAME = 'null' or t1.FILENAME = '') and t1.TASKID = '" + taskid + "' AND t1.ISUPLOAD <>'2';";
        List<Map<String,String>> dblist = dbHelper.selectSQL(sql, null);
        List<String> valueList = new ArrayList<>();
        for (int i=0;i<dblist.size();i++){
            String value = dblist.get(i).get("FILENUMBER") + "|" + dblist.get(i).get("ID")+"|general";
            valueList.add(value);
        }
        sql = "select * from tr_special_bushing_file t where " +
                "(t.FILENUMBER != 'null' and t.FILENUMBER != '') " +
                "and (t.FILENAME = 'null' or t.FILENAME = '') and  t.TASKID = '" + taskid + "' AND t.ISUPLOAD <>'2';";
        dblist = dbHelper.selectSQL(sql, null);
        for (int i=0;i<dblist.size();i++){
            String value = dblist.get(i).get("FILENUMBER") + "|" + dblist.get(i).get("ID")+"|bushing";
            valueList.add(value);
        }
        return valueList;
    }
}
