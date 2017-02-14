package com.energyfuture.symphony.m3.dao;

import android.content.Context;
import android.util.Log;

import com.energyfuture.symphony.m3.domain.TrDetectiontempletObj;
import com.energyfuture.symphony.m3.domain.TrDetectiontypEtempletObj;
import com.energyfuture.symphony.m3.domain.TrSpecialWork;
import com.energyfuture.symphony.m3.util.DBHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/3/24.
 */
public class TrDetectionTempletObjDao {
    DBHelper dbHelper = null;

    public TrDetectionTempletObjDao(Context cxt) {
        dbHelper = new DBHelper(cxt);
    }

    public SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 获取检测项目模板实体表信息
     *
     * @param @param  null
     * @param @return
     * @return list
     * @designer
     * @author dingwujun
     * @since 2014-11-6
     */
    public List<TrDetectiontempletObj> queryTrDetectionTempletObjList(TrDetectiontempletObj trDetectionTempletObjParam) {
        List<TrDetectiontempletObj> list = new ArrayList<TrDetectiontempletObj>();
        String sqlwhere="";

        if(trDetectionTempletObjParam.getId()!=null&&!trDetectionTempletObjParam.getId().equals("")){
            sqlwhere+="AND ID='"+trDetectionTempletObjParam.getId()+"'";
        }

        String sql = "SELECT * FROM TR_DETECTIONTEMPLET_OBJ WHERE 1=1 "+sqlwhere;
        List<Map<String,String>> dblist = dbHelper.selectSQL(sql, null);
        for (int i = 0; i < dblist.size(); i++){
            Map<String, String> m = dblist.get(i);
            TrDetectiontempletObj trDetectiontempletObj = new TrDetectiontempletObj();
            trDetectiontempletObj.setId(m.get("ID"));
            trDetectiontempletObj.setDetectiondes(m.get("DETECTIONDES"));
            trDetectiontempletObj.setDetectionmethod(m.get("DETECTIONMETHOD"));
            trDetectiontempletObj.setDetectionstandard(m.get("DETECTIONSTANDARD"));
            trDetectiontempletObj.setDetectionexample(m.get("DETECTIONEXAMPLE"));
            trDetectiontempletObj.setUpdatetime(m.get("UPDATETIME"));
            trDetectiontempletObj.setUpdateperson(m.get("UPDATEPERSON"));
            trDetectiontempletObj.setStatus(m.get("STATUS"));
            trDetectiontempletObj.setResultdescribe(m.get("RESULTDESCRIBE"));
            trDetectiontempletObj.setRemarks(m.get("REMARKS"));
            list.add(trDetectiontempletObj);
        }
        return list;
    }

    public void insertListData(List<TrDetectiontempletObj> trDetectionTempletObjList)
    {
        if(trDetectionTempletObjList!=null&&trDetectionTempletObjList.size()>0)
        {
            List<String> listSql= new ArrayList<String>();
            for(int i=0;i<trDetectionTempletObjList.size();i++)
            {
                String sql="REPLACE INTO TR_DETECTIONTEMPLET_OBJ (" +
                        "ID,DETECTIONDES,DETECTIONMETHOD,DETECTIONSTANDARD,DETECTIONEXAMPLE,UPDATETIME,UPDATEPERSON,STATUS,RESULTDESCRIBE,REMARKS"+
                        ") VALUES (" +
                        "'"+trDetectionTempletObjList.get(i).getId()+"'," +
                        "'"+trDetectionTempletObjList.get(i).getDetectiondes()+"'," +
                        "'"+trDetectionTempletObjList.get(i).getDetectionmethod()+"'," +
                        "'"+trDetectionTempletObjList.get(i).getDetectionstandard()+"'," +
                        "'"+trDetectionTempletObjList.get(i).getDetectionexample()+"'," +
                        "'"+trDetectionTempletObjList.get(i).getUpdatetime()+"'," +
                        "'"+trDetectionTempletObjList.get(i).getUpdateperson()+"'," +
                        "'"+trDetectionTempletObjList.get(i).getStatus()+"'," +
                        "'"+trDetectionTempletObjList.get(i).getResultdescribe()+"'," +
                        "'"+trDetectionTempletObjList.get(i).getRemarks()+"'" +
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
     * 根据条件修改检测项目模板实体表数据
     * @designer
     * @since  2014-11-6
     * @author dingwujun
     * @param @param List<PmDevice>
     * @param @return
     * @return null
     */
    public void updateTrDetectionTempletObjInfo(Map<Object,Object> columnsMap,Map<Object,Object> wheresMap)
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
            sql="UPDATE TR_DETECTIONTEMPLET_OBJ SET " +columns +" WHERE 1=1 "+sqlWhere;

            if(sql!=null&&!sql.equals("")) {
                dbHelper.execSQL(sql);
                Log.i("############################","#########TR_DETECTIONTEMPLET_OBJ-----"+sql);
            }
        }
    }
    public List<TrDetectiontempletObj> getTrDetectiontempletObj(TrDetectiontypEtempletObj data){ //获取检测项目模板表信息
        String sql=
                "SELECT * FROM TR_DETECTIONTEMPLET_OBJ WHERE ID IN(" +
                        "select detectiontemplet from tr_detectiontyp_etemplet_obj where ID='"+data.getId()+"')";
        List<TrDetectiontempletObj> list = new ArrayList<>();
        List<Map<String,String>> dblist = dbHelper.selectSQL(sql, null);
        for (int i = 0; i < dblist.size(); i++){
            Map<String, String> m = dblist.get(i);
            TrDetectiontempletObj trDetectiontempletObj = new TrDetectiontempletObj();
            trDetectiontempletObj.setId(m.get("ID"));
            trDetectiontempletObj.setDetectiondes(m.get("DETECTIONDES"));
            trDetectiontempletObj.setDetectionmethod(m.get("DETECTIONMETHOD"));
            trDetectiontempletObj.setDetectionstandard(m.get("DETECTIONSTANDARD"));
            trDetectiontempletObj.setDetectionexample(m.get("DETECTIONEXAMPLE"));
            trDetectiontempletObj.setUpdatetime(m.get("UPDATETIME"));
            trDetectiontempletObj.setUpdateperson(m.get("UPDATEPERSON"));
            trDetectiontempletObj.setStatus(m.get("STATUS"));
            trDetectiontempletObj.setResultdescribe(m.get("RESULTDESCRIBE"));
            trDetectiontempletObj.setRemarks(m.get("REMARKS"));
            list.add(trDetectiontempletObj);
        }
        return list;
    }

    public List<TrDetectiontempletObj> getTrDetectiontempletObj(TrSpecialWork data){ //获取检测项目模板表信息
        String sql=
                "SELECT * FROM TR_DETECTIONTEMPLET_OBJ WHERE ID IN(" +
                        "select detectiontemplet from tr_detectiontyp_etemplet_obj where ID='"+data.getDetectionprojectid()+"')";
        List<TrDetectiontempletObj> list = new ArrayList<>();
        List<Map<String,String>> dblist = dbHelper.selectSQL(sql, null);
        for (int i = 0; i < dblist.size(); i++){
            Map<String, String> m = dblist.get(i);
            TrDetectiontempletObj trDetectiontempletObj = new TrDetectiontempletObj();
            trDetectiontempletObj.setId(m.get("ID"));
            trDetectiontempletObj.setDetectiondes(m.get("DETECTIONDES"));
            trDetectiontempletObj.setDetectionmethod(m.get("DETECTIONMETHOD"));
            trDetectiontempletObj.setDetectionstandard(m.get("DETECTIONSTANDARD"));
            trDetectiontempletObj.setDetectionexample(m.get("DETECTIONEXAMPLE"));
            trDetectiontempletObj.setUpdatetime(m.get("UPDATETIME"));
            trDetectiontempletObj.setUpdateperson(m.get("UPDATEPERSON"));
            trDetectiontempletObj.setStatus(m.get("STATUS"));
            trDetectiontempletObj.setResultdescribe(m.get("RESULTDESCRIBE"));
            trDetectiontempletObj.setRemarks(m.get("REMARKS"));
            list.add(trDetectiontempletObj);
        }
        return list;
    }
}
