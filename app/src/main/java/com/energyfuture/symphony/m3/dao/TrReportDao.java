package com.energyfuture.symphony.m3.dao;

import android.content.Context;

import com.energyfuture.symphony.m3.domain.TrReport;
import com.energyfuture.symphony.m3.util.DBHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/3/24.
 */
public class TrReportDao {
    DBHelper dbHelper = null;

    public TrReportDao(Context cxt) {
        dbHelper = new DBHelper(cxt);
    }


    /**
     * 获取报告信息
     *
     * @param @param  null
     * @param @return
     * @return list
     * @designer
     * @author dingwujun
     * @since 2014-11-6
     */
    public List<TrReport> queryTrReportList() {
        List<TrReport> list = new ArrayList<TrReport>();
        String sql = "SELECT * FROM TR_REPORT";
        List<Map<String, String>> cursor = dbHelper.selectSQL(sql, null);
        if (cursor.size() > 0) {
            for (int i = 0; i < cursor.size(); i++){
                Map<String, String> m = cursor.get(i);
                TrReport trReport = new TrReport();
                trReport.setId(m.get("ID"));
                trReport.setProjectId(m.get("PROJECTID"));
                trReport.setStationName(m.get("STATIONNAME"));
                trReport.setStationId(m.get("STATIONID"));
                trReport.setReportName(m.get("REPORTNAME"));
                trReport.setCreatePerson(m.get("CREATEPERSON"));
                trReport.setCreateTime(m.get("CREATETIME"));
                trReport.setStatus(m.get("STATUS"));
                trReport.setAuditingPerson(m.get("AUDITINGPERSON"));
                trReport.setAuditingSuggestion(m.get("AUDITINGSUGGESTION"));
                trReport.setAuditingTime(m.get("AUDITINGTIME"));
                trReport.setWordUrl(m.get("WORDURL"));
                list.add(trReport);
            }
        }
        return list;
    }

    public void insertListData(List<TrReport> trReportList) {
        if(trReportList!=null&&trReportList.size()>0)
        {
            List<String> listSql= new ArrayList<String>();
            for(int i=0;i<trReportList.size();i++)
            {
                String sql="REPLACE INTO TR_REPORT "+
                        "(ID,PROJECTID,STATIONNAME,STATIONID,REPORTNAME,CREATEPERSON,CREATETIME,STATUS,"+
                        "AUDITINGPERSON,AUDITINGSUGGESTION,AUDITINGTIME,WORDURL)"+
                        "VALUES "+
                        "('"+trReportList.get(i).getId()+"'," +
                        "'"+trReportList.get(i).getProjectId()+"'," +
                        "'"+trReportList.get(i).getStationName()+"'," +
                        "'"+trReportList.get(i).getStationId()+"'," +
                        "'"+trReportList.get(i).getReportName()+"'," +
                        "'"+trReportList.get(i).getCreatePerson()+"'," +
                        "'"+trReportList.get(i).getCreateTime()+"'," +
                        "'"+trReportList.get(i).getStatus()+"'," +
                        "'"+trReportList.get(i).getAuditingPerson()+"'," +
                        "'"+trReportList.get(i).getAuditingSuggestion()+"'," +
                        "'"+trReportList.get(i).getAuditingTime()+"'," +
                        "'"+trReportList.get(i).getWordUrl()+"'" +
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
     * 删除报告
     * @param TrReportList
     */
    public void deleteListData(List<TrReport> TrReportList){
        if(TrReportList != null && TrReportList.size() > 0){
            {
                List<String> listSql= new ArrayList<String>();
                for(int i=0;i<TrReportList.size();i++)
                {
                    String sql="Delete from TR_REPORT where id='" + TrReportList.get(i).getId() + "'";
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
