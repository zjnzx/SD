package com.energyfuture.symphony.m3.dao;

import android.content.Context;

import com.energyfuture.symphony.m3.domain.TrProject;
import com.energyfuture.symphony.m3.domain.TrTask;
import com.energyfuture.symphony.m3.util.DBHelper;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/3/24.
 */
public class TrTaskDao {
    DBHelper dbHelper = null;

    public TrTaskDao(Context cxt) {
        dbHelper = new DBHelper(cxt);
    }

    public SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    /**
     * 获取任务表信息
     *
     * @param @param  null
     * @param @return
     * @return list
     * @designer
     * @author dingwujun
     * @since 2014-11-6
     */
    public List<TrTask> queryTrTaskList(TrTask trTaskParam) {
        List<TrTask> list = new ArrayList<TrTask>();
        String  sqlWhere="";
        if(trTaskParam.getTaskid()!=null&&!trTaskParam.getTaskid().equals(""))
        {
            sqlWhere+=" AND TASKID='"+trTaskParam.getTaskid()+"'";
        }

        if(trTaskParam.getProjectid()!=null&&!trTaskParam.getProjectid().equals(""))
        {
            sqlWhere+=" AND PROJECTID='"+trTaskParam.getProjectid()+"'";
        }

        String sql = "SELECT * FROM TR_TASK WHERE 1=1 "+sqlWhere +" ORDER BY TASKNAME,ORDERBY";

        List<Map<String,String>> dblist = dbHelper.selectSQL(sql, null);
        for (int i = 0; i < dblist.size(); i++){
            Map<String, String> m = dblist.get(i);
            TrTask sympTask = new TrTask();
            sympTask.setTaskid(m.get("TASKID"));
            sympTask.setTaskname(m.get("TASKNAME"));
            sympTask.setTaskstate(m.get("TASKSTATE"));
            sympTask.setPerson(m.get("PERSON"));
            sympTask.setPlanstarttime(m.get("PLANSTARTTIME"));
            sympTask.setPlanendtime(m.get("PLANENDTIME"));
            sympTask.setActualstarttime(m.get("ACTUALSTARTTIME"));
            sympTask.setActualsendtime(m.get("ACTUALSENDTIME"));
            sympTask.setPerson(m.get("PERSON"));
            sympTask.setTaskperson(m.get("TASKPERSON"));
            sympTask.setDetectionttype(m.get("DETECTIONTTYPE"));
            sympTask.setUpdatetime(m.get("UPDATETIME"));
            sympTask.setFinishtime(m.get("FINISHTIME"));
            sympTask.setStepcount(m.get("STEPCOUNT"));
            sympTask.setFinishcount(m.get("FINISHCOUNT"));
            sympTask.setWorkperson(m.get("WORKPERSON"));
            sympTask.setProjectid(m.get("PROJECTID"));
            sympTask.setEquipmentid(m.get("EQUIPMENTID"));
            sympTask.setOrderby(m.get("ORDERBY"));
            sympTask.setAppraise_abnormal(m.get("APPRAISE_ABNORMAL"));
            sympTask.setAppraise_suggest(m.get("APPRAISE_SUGGEST"));
            sympTask.setAppraise_changeflow(m.get("APPRAISE_CHANGEFLOW"));
            sympTask.setAppraise_changeperson(m.get("APPRAISE_CHANGEPERSON"));
            sympTask.setChangeenv(m.get("CHANGEENV"));
            sympTask.setChangeequipment(m.get("CHANGEEQUIPMENT"));
            sympTask.setChangework(m.get("CHANGEWORK"));
            sympTask.setChangeother(m.get("CHANGEOTHER"));
            sympTask.setImprove(m.get("IMPROVE"));
            sympTask.setMethod(m.get("METHOD"));
            sympTask.setRecoverperson(m.get("RECOVERPERSON"));
            sympTask.setCleanperson(m.get("CLEANPERSON"));
            sympTask.setConclusionperson(m.get("CONCLUSIONPERSON"));
            sympTask.setAudit_result(m.get("AUDIT_RESULT"));
            sympTask.setAudit_question(m.get("AUDIT_QUESTION"));
            sympTask.setAuditperson(m.get("AUDITPERSON"));
            list.add(sympTask);
            }

        return list;
    }

    /**
     * 查项目下的所有任务的id
     * @param trProjectParam
     * @return
     */
    public String queryTrTaskIdList(TrProject trProjectParam) {
        StringBuffer stringBuffer = new StringBuffer();
        String  sqlWhere="";
        if(trProjectParam.getProjectid()!=null&&!trProjectParam.getProjectid().equals(""))
        {
            sqlWhere+=" AND PROJECTID='"+trProjectParam.getProjectid()+"'";
        }

        String sql = "SELECT * FROM TR_TASK WHERE 1=1 "+sqlWhere;

        List<Map<String,String>> dblist = dbHelper.selectSQL(sql, null);
        for (int i = 0; i < dblist.size(); i++){
            Map<String, String> m = dblist.get(i);
            stringBuffer.append(m.get("TASKID")).append(",");
        }
        return stringBuffer.toString();
    }

    /**
     * 插入任务表数据
     * @designer
     * @since  2014-11-6
     * @author dingwujun
     * @param @param List<PmDevice>
     * @param @return
     * @return null
     */
    public void insertListData(List<TrTask> trTaskList)
    {
        if(trTaskList!=null&&trTaskList.size()>0)
        {
            List<String> listSql= new ArrayList<String>();
            for(int i=0;i<trTaskList.size();i++)
            {
                String sql="REPLACE INTO TR_TASK (" +
                        "TASKID,PROJECTID,EQUIPMENTID,TASKNAME,TASKSTATE,PLANSTARTTIME,PLANENDTIME,ACTUALSTARTTIME,ACTUALSENDTIME,PERSON,TASKPERSON," +
                        "DETECTIONTTYPE,UPDATETIME,FINISHTIME,STEPCOUNT,FINISHCOUNT,WORKPERSON,ORDERBY,APPRAISE_ABNORMAL,APPRAISE_SUGGEST,APPRAISE_CHANGEFLOW," +
                        "APPRAISE_CHANGEPERSON,CHANGEENV,CHANGEEQUIPMENT,CHANGEWORK,CHANGEOTHER,IMPROVE,METHOD,RECOVERPERSON,CLEANPERSON,CONCLUSIONPERSON," +
                        "AUDIT_RESULT,AUDIT_QUESTION,AUDITPERSON" +
                        ") VALUES (" +
                        "'"+trTaskList.get(i).getTaskid()+"'," +
                        "'"+trTaskList.get(i).getProjectid()+"'," +
                        "'"+trTaskList.get(i).getEquipmentid()+"'," +
                        "'"+trTaskList.get(i).getTaskname()+"'," +
                        "'"+trTaskList.get(i).getTaskstate()+"'," +
                        "'"+trTaskList.get(i).getPlanstarttime()+"'," +
                        "'"+trTaskList.get(i).getPlanendtime()+"'," +
                        "'"+trTaskList.get(i).getActualstarttime()+"'," +
                        "'"+trTaskList.get(i).getActualsendtime()+"'," +
                        "'"+trTaskList.get(i).getPerson()+"'," +
                        "'"+trTaskList.get(i).getTaskperson()+"'," +
                        "'"+trTaskList.get(i).getDetectionttype()+"'," +
                        "'"+trTaskList.get(i).getUpdatetime()+"'," +
                        "'"+trTaskList.get(i).getFinishtime()+"'," +
                        "'"+trTaskList.get(i).getStepcount()+"'," +
                        "'"+trTaskList.get(i).getFinishcount()+"'," +
                        "'"+trTaskList.get(i).getWorkperson()+"'," +
                        "'"+trTaskList.get(i).getOrderby()+"'," +
                        "'"+trTaskList.get(i).getAppraise_abnormal()+"'," +
                        "'"+trTaskList.get(i).getAppraise_suggest()+"'," +
                        "'"+trTaskList.get(i).getAppraise_changeflow()+"'," +
                        "'"+trTaskList.get(i).getAppraise_changeperson()+"'," +
                        "'"+trTaskList.get(i).getChangeenv()+"'," +
                        "'"+trTaskList.get(i).getChangeequipment()+"'," +
                        "'"+trTaskList.get(i).getChangework()+"'," +
                        "'"+trTaskList.get(i).getChangeother()+"'," +
                        "'"+trTaskList.get(i).getImprove()+"'," +
                        "'"+trTaskList.get(i).getMethod()+"'," +
                        "'"+trTaskList.get(i).getRecoverperson()+"'," +
                        "'"+trTaskList.get(i).getCleanperson()+"'," +
                        "'"+trTaskList.get(i).getConclusionperson()+"'," +
                        "'"+trTaskList.get(i).getAudit_result()+"'," +
                        "'"+trTaskList.get(i).getAudit_question()+"'," +
                        "'"+trTaskList.get(i).getAuditperson()+"'" +
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
     * 根据条件修改任务表数据
     * @designer
     * @since  2014-11-6
     * @author dingwujun
     * @param @param List<PmDevice>
     * @param @return
     * @return null
     */
    public void updateTrTaskInfo(Map<Object,Object> columnsMap,Map<Object,Object> wheresMap)
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
                sql="UPDATE TR_TASK SET " +columns +" WHERE 1=1 "+sqlWhere;

            if(sql!=null&&!sql.equals(""))
            {
                dbHelper.execSQL(sql);
            }
        }
    }

    /**
     * zhangshuang
     * 项目下步骤的个数
     * @param projectId
     * @return
     */
    public int  taskStepCount(String  projectId){
        Map map=new HashMap();
        String sql="";
        Double bz=0.0;
        int res=0;
        if(projectId!=null&&!projectId.equals(""))
        {
            sql="SELECT * FROM TR_TASK B WHERE B.PROJECTID='"+projectId+"'";
            if(sql!=null&&!sql.equals("")) {
                int fCount=0;
                int tCount=0;
                List<Map<String,String>> dblist = dbHelper.selectSQL(sql, null);
                for (int i = 0; i < dblist.size(); i++){
                    Map<String, String> m = dblist.get(i);
                        String taskId=m.get("TASKID");
                            String tabName="TR_TASK";
                           map= this.hwStepCount(taskId,tabName);
                            if (map !=null){
                                tCount+=Integer.parseInt(map.get("tCount")==null?"0":map.get("tCount").toString());
                                fCount+=Integer.parseInt(map.get("fCount")==null?"0":map.get("fCount").toString());
                        }
                    }
                    DecimalFormat dDormat=new DecimalFormat("#.00");
                    if (tCount!=0){
                        bz=Double.valueOf(fCount)/Double.valueOf(tCount);
                    }

                    res=Integer.parseInt(new DecimalFormat("0").format(bz*100));

                }

            }

        return  res;
    }

    public Map  hwStepCount (String taskId,String tabName){
        Map map=new HashMap();
        int fCount=0;
        int tCount=0;
        String sql="";
        if (taskId!=null){
            sql="SELECT COUNT(*) TCOUNT FROM "+tabName+"  WHERE TASKID='"+taskId+"'";

            List<Map<String,String>> dblist = dbHelper.selectSQL(sql, null);
            for (int i = 0; i < dblist.size(); i++){
                Map<String, String> m = dblist.get(i);
                    String tc=m.get("TCOUNT");
                    tCount+=Integer.parseInt(tc);
                }
            }
            sql="SELECT COUNT(*) TCOUNT FROM "+tabName+" WHERE TASKID='"+taskId+"' AND TASKSTATE='7'";
        List<Map<String,String>> dblist = dbHelper.selectSQL(sql, null);
        for (int i = 0; i < dblist.size(); i++){
            Map<String, String> m = dblist.get(i);
                    String fc=m.get("TCOUNT");
                    fCount+=Integer.parseInt(fc);
                }


        map.put("tCount",tCount);
        map.put("fCount",fCount);

        return map;
    }

    //删除任务
    public void deleteListData(List<TrTask> trTasks){
        if(trTasks != null && trTasks.size() > 0){
            {
                List<String> listSql= new ArrayList<String>();
                for(int i=0;i<trTasks.size();i++)
                {
                    String sql="Delete from tr_task where taskid='" + trTasks.get(i).getTaskid() + "'";
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