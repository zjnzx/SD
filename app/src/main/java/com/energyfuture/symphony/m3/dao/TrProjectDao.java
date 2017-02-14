package com.energyfuture.symphony.m3.dao;

import android.content.Context;

import com.energyfuture.symphony.m3.domain.TrProject;
import com.energyfuture.symphony.m3.util.DBHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/3/24.
 */
public class TrProjectDao {
    DBHelper dbHelper = null;

    public TrProjectDao(Context cxt) {
        dbHelper = new DBHelper(cxt);
    }

    public SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 获取项目表信息
     *
     * @param @param  null
     * @param @return
     * @return list
     * @designer
     * @author dingwujun
     * @since 2014-11-6
     */
    public List<TrProject> queryTrProjectsList(TrProject trProjectParam) {
        List<TrProject> list = new ArrayList<TrProject>();
        String sqlwhere="";
        if(trProjectParam!=null) {
            if(trProjectParam.getZrpearson()!=null&&!trProjectParam.getZrpearson().equals("")){
                sqlwhere+=" AND ZRPEARSON='"+trProjectParam.getZrpearson()+"'";
            }
            if(trProjectParam.getStationid()!=null&&!trProjectParam.getStationid().equals(""))
            {
                sqlwhere+=" AND STATIONID='"+trProjectParam.getStationid()+"'";
            }
            if(trProjectParam.getProjectstate()!=null&&!trProjectParam.getProjectstate().equals(""))
            {
                sqlwhere+=" AND PROJECTSTATE='"+trProjectParam.getProjectstate()+"'";
            }
            if((trProjectParam.getStarttime()!=null&&!trProjectParam.getStarttime().equals(""))&&(trProjectParam.getEndtime()!=null&&!trProjectParam.getEndtime().equals("")))
            {
                sqlwhere+=" AND STARTTIME>='"+trProjectParam.getStarttime()+"' AND ENDTIME<='"+trProjectParam.getEndtime()+"'";
            }
            if(trProjectParam.getProjectid()!=null&&!trProjectParam.getProjectid().equals("")){
                sqlwhere+=" AND PROJECTID='"+trProjectParam.getProjectid()+"'";
            }
            if(trProjectParam.getCreatetime()!=null&&!trProjectParam.getCreatetime().equals("")){
                sqlwhere+=" AND CREATETIME <'"+trProjectParam.getCreatetime()+"'";
            }

            if(trProjectParam.getDepartment()!=null&&!trProjectParam.getDepartment().equals("")){
                sqlwhere+=" AND DEPARTMENT ='"+trProjectParam.getDepartment()+"'";
            }
            if(trProjectParam.getDepartment()!=null&&!trProjectParam.getDepartment().equals("")){
                sqlwhere+=" AND DEPARTMENT='"+trProjectParam.getDepartment()+"'";
            }
        }

        String sql = "SELECT * FROM TR_PROJECT WHERE 1=1 "+sqlwhere +" ORDER BY CREATETIME DESC";

        List<Map<String,String>> dblist = dbHelper.selectSQL(sql, null);
        for (int i = 0; i < dblist.size(); i++){
            Map<String, String> m = dblist.get(i);
            TrProject trProject = new TrProject();
            trProject.setProjectid(m.get("PROJECTID"));
            trProject.setProjectname(m.get("PROJECTNAME"));
            trProject.setProjectstate(m.get("PROJECTSTATE"));
            trProject.setStationname(m.get("STATIONNAME"));
            trProject.setStationid(m.get("STATIONID"));
            trProject.setStarttime(m.get("STARTTIME"));
            trProject.setEndtime(m.get("ENDTIME"));
            trProject.setActualstarttime(m.get("ACTUALSTARTTIME"));
            trProject.setActualsendtime(m.get("ACTUALSENDTIME"));
            trProject.setCreatetime(m.get("CREATETIME"));
            trProject.setUpdatetime(m.get("UPDATETIME"));
            trProject.setAware(m.get("AWARE"));
            trProject.setZpperson(m.get("ZPPERSON"));
            trProject.setCreateperson(m.get("CREATEPERSON"));
            trProject.setZrpearson(m.get("ZRPEARSON"));
            trProject.setReasondescribe(m.get("REASONDESCRIBE"));
            trProject.setSafetynotice(m.get("SAFETYNOTICE"));
            trProject.setTaskcount(m.get("TASKCOUNT"));
            trProject.setFinishcount(m.get("FINISHCOUNT"));
            trProject.setWorkperson(m.get("WORKPERSON"));
            trProject.setDepartment(m.get("DEPARTMENT"));
            list.add(trProject);
        }
        return list;
    }

    /**
     * 根据条件修改项目表数据
     * @designer
     * @since  2014-11-6
     * @author dingwujun
     * @param @param List<PmDevice>
     * @param @return
     * @return null
     */
    public void updateProjectInfo(Map<Object,Object> columnsMap,Map<Object,Object> wheresMap)
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
            sql="UPDATE TR_PROJECT SET " +columns +" WHERE 1=1 "+sqlWhere;

            if(sql!=null&&!sql.equals("")) {
                dbHelper.execSQL(sql);
            }
        }
    }
    public void insertListData(List<TrProject> trProjectList)
    {
        if(trProjectList!=null&&trProjectList.size()>0)
        {
            List<String> listSql= new ArrayList<String>();
            for(int i=0;i<trProjectList.size();i++)
            {
                String sql="REPLACE INTO TR_PROJECT (" +
                        "PROJECTID,PROJECTNAME,PROJECTSTATE,STATIONNAME,STATIONID,STARTTIME,ENDTIME,ACTUALSTARTTIME,ACTUALSENDTIME,CREATETIME,"+
                        "AWARE,ZPPERSON,CREATEPERSON,ZRPEARSON,WORKPERSON,UPDATETIME,REASONDESCRIBE,SAFETYNOTICE,TASKCOUNT,FINISHCOUNT,DEPARTMENT" +
                        ") VALUES (" +
                        "'"+trProjectList.get(i).getProjectid()+"'," +
                        "'"+trProjectList.get(i).getProjectname()+"'," +
                        "'"+trProjectList.get(i).getProjectstate()+"'," +
                        "'"+trProjectList.get(i).getStationname()+"'," +
                        "'"+trProjectList.get(i).getStationid()+"'," +
                        "'"+trProjectList.get(i).getStarttime()+"'," +
                        "'"+trProjectList.get(i).getEndtime()+"'," +
                        "'"+trProjectList.get(i).getActualstarttime()+"'," +
                        "'"+trProjectList.get(i).getActualsendtime()+"'," +
                        "'"+trProjectList.get(i).getCreatetime()+"'," +
                        "'"+trProjectList.get(i).getAware()+"'," +
                        "'"+trProjectList.get(i).getZpperson()+"'," +
                        "'"+trProjectList.get(i).getCreateperson()+"'," +
                        "'"+trProjectList.get(i).getZrpearson()+"'," +
                        "'"+trProjectList.get(i).getWorkperson()+"'," +
                        "'"+trProjectList.get(i).getUpdatetime()+"'," +
                        "'"+trProjectList.get(i).getReasondescribe()+"'," +
                        "'"+trProjectList.get(i).getSafetynotice()+"'," +
                        "'"+trProjectList.get(i).getTaskcount()+"'," +
                        "'"+trProjectList.get(i).getFinishcount()+"'," +
                        "'"+trProjectList.get(i).getDepartment()+"'" +
                        ")";
                listSql.add(sql);
            }
            if(listSql!=null&&listSql.size()>0)
            {
                dbHelper.insertSQLAll(listSql);
            }
        }
    }

    //删除项目
    public void deleteListData(List<TrProject> trProjectList){
        if(trProjectList != null && trProjectList.size() > 0){
            {
                List<String> listSql= new ArrayList<String>();
                for(int i=0;i<trProjectList.size();i++)
                {
                    String sql="Delete from tr_project where projectid='" + trProjectList.get(i).getProjectid() + "'";
                    listSql.add(sql);
                }
                if(listSql!=null&&listSql.size()>0)
                {
                    dbHelper.deleteSQLAll(listSql);
                }
            }
        }
    }

    /**
     * 根据项目ID判断是否有最新的项目
     *
     */

//    public List<SympProject> queryNewSympProject(SympProject sympProjecttype){
//        List<SympProject> list = new ArrayList<>();
//        String sqlwhere="";
//        if (sympProjecttype!=null){
//            if (sympProjecttype.getProjectid()!=null && !sympProjecttype.getProjectid().equals("")){
//                sqlwhere+=" AND PROJECTID='"+sympProjecttype.getProjectid()+"'";
//            }
//        }
//        String sql = "SELECT * FROM SYMP_PROJECT WHERE 1=1 "+sqlwhere +" ORDER BY CREATETIME DESC";
//        Log.v("logsql",sql);
//        List<Map<String,String>> dblist = dbHelper.selectSQL(sql, null);
//        for (int i = 0; i < dblist.size(); i++){
//            Map<String, String> m = dblist.get(i);
//            SympProject sympProject = new SympProject();
//            sympProject.setProjectid(m.get("PROJECTID"));
//            sympProject.setProjectname(m.get("PROJECTNAME"));
//            sympProject.setProjectstate(m.get("PROJECTSTATE"));
//            sympProject.setGrade(m.get("GRADE"));
//            sympProject.setStationname(m.get("STATIONNAME"));
//            sympProject.setStationid(m.get("STATIONID"));
//            sympProject.setStatetime(m.get("STATETIME"));
//            sympProject.setEndtime(m.get("ENDTIME"));
//            sympProject.setActualstatetime(m.get("ACTUALSTATETIME"));
//            sympProject.setActualsendtime(m.get("ACTUALSENDTIME"));
//            sympProject.setCreatetime(m.get("CREATETIME"));
//            sympProject.setUpdatetime(m.get("UPDATETIME"));
//            sympProject.setAware(m.get("AWARE"));
//            sympProject.setZpperson(m.get("ZPPERSON"));
//            sympProject.setCreateperson(m.get("CREATEPERSON"));
//            sympProject.setZrpearson(m.get("ZRPEARSON"));
//            sympProject.setReasondescribe(m.get("REASONDESCRIBE"));
//            sympProject.setSafetynotice(m.get("SAFETYNOTICE"));
//            sympProject.setTaskcount(m.get("TASKCOUNT"));
//            sympProject.setFinishcount(m.get("FINISHCOUNT"));
//            sympProject.setInstrument(m.get("INSTRUMENT"));
//            sympProject.setWorkperson(m.get("WORKPERSON"));
//            list.add(sympProject);
//        }
//        return list;
//    }
}
