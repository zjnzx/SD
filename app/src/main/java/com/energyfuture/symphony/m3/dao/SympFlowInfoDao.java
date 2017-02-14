package com.energyfuture.symphony.m3.dao;

import android.content.Context;

import com.energyfuture.symphony.m3.domain.SympFlowInfo;
import com.energyfuture.symphony.m3.util.DBHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/8/19.
 */
public class SympFlowInfoDao {

    DBHelper dbHelper = null;

    public SympFlowInfoDao(Context cxt) {
        dbHelper = new DBHelper(cxt);
    }


    /**
     * 获取流量
     *
     * @param @param  null
     * @param @return
     * @return list
     * @designer
     * @author daipan
     * @since 2014-11-6
     */
    public void inserFlowInfo(String userID, String totalTx, SympFlowInfo sympFlowInfo) {

        if (!sympFlowInfo.equals("") || sympFlowInfo != null) {
            String sql = "REPLACE INTO SYMP_FLOW_INFO (" +
                    "FLOWINFOID,USERID,FLOWTYPE,USETYPE,CONNECTTYPE,WEBTYPE,RECORDTIME,USEQUANTITY,EQUIPMENTID,BUSINESSTYPE,REMARK" +
                    ") VALUES (" +
                    "'" + sympFlowInfo.getFlowinfoid() + "'," +
                    "'" + sympFlowInfo.getUserid() + "'," +
                    "'" + sympFlowInfo.getFlowtype() + "'," +
                    "'" + sympFlowInfo.getUsetype() + "'," +
                    "'" + sympFlowInfo.getConnecttype() + "'," +
                    "'" + sympFlowInfo.getWebtype() + "'," +
                    "'" + sympFlowInfo.getRecordtime() + "'," +
                    "'" + sympFlowInfo.getUsequantity() + "'," +
                    "'" + sympFlowInfo.getEquipmentid() + "'," +
                    "'" + (sympFlowInfo.getBusinesstype() == null ? "" : sympFlowInfo.getBusinesstype()) + "'," +
                    "'" + (sympFlowInfo.getRemark() == null ? "" : sympFlowInfo.getRemark()) + "'" +
                    ")";

            dbHelper.execSQL(sql);

        }

    }

    public List<SympFlowInfo> queryFlowInfo(String date) {
        List<SympFlowInfo> list = new ArrayList<SympFlowInfo>();
        String sql = "SELECT * FROM SYMP_FLOW_INFO WHERE 1=1 ";
        if (date != null) {
            sql += " AND RECORDTIME < '" + date + "'";
        }
        sql += " ORDER BY RECORDTIME DESC LIMIT 35 ";
        List<Map<String, String>> dblist = dbHelper.selectSQL(sql, null);
        for (int i = 0; i < dblist.size(); i++) {
            Map<String, String> m = dblist.get(i);
            SympFlowInfo obj = new SympFlowInfo();
            obj.setFlowinfoid(m.get("FLOWINFOID"));
            obj.setUserid(m.get("USERID"));
            obj.setFlowtype(m.get("FLOWTYPE"));
            obj.setUsetype(m.get("USETYPE"));
            obj.setConnecttype(m.get("CONNECTTYPE"));
            obj.setWebtype(m.get("WEBTYPE"));
            obj.setRecordtime(m.get("RECORDTIME"));
            obj.setUsequantity(m.get("USEQUANTITY"));
            obj.setEquipmentid(m.get("EQUIPMENTID"));
            obj.setBusinesstype(m.get("BUSINESSTYPE"));
            obj.setRemark(m.get("REMARK"));
            list.add(obj);
        }

        return list;
    }

    public List<SympFlowInfo> querySelectFlowInfo(String date, String user, String flow, String connect, String use,String stateTime,String endTime) {
        List<SympFlowInfo> list = new ArrayList<SympFlowInfo>();
        String sql = "SELECT * FROM SYMP_FLOW_INFO WHERE 1=1 ";
        if (date != null) {
            sql += " AND RECORDTIME < '" + date + "'";
        }
        if (!user.equals("全部")) {
            if (user != null) {
                sql += " AND USERID = '" + user + "'";
            }
        }
        if (!connect.equals("全部")) {
            sql += " AND CONNECTTYPE = '" + connect + "'";
        }
        if (!flow.equals("全部")) {
            sql += " AND FLOWTYPE = '" + flow + "'";
        }
        if (!use.equals("全部")) {
            sql += " AND USETYPE = '" + use + "'";
        }
        if (!stateTime.equals("全部") ) {

            sql += " AND RECORDTIME >= '" + stateTime + "'";
        }
        if (!endTime.equals("全部")) {

            sql += " AND RECORDTIME < '" + endTime + "'";
        }


        sql += " ORDER BY RECORDTIME DESC LIMIT 35 ";
        List<Map<String, String>> dblist = dbHelper.selectSQL(sql, null);
        for (int i = 0; i < dblist.size(); i++) {
            Map<String, String> m = dblist.get(i);
            SympFlowInfo obj = new SympFlowInfo();
            obj.setFlowinfoid(m.get("FLOWINFOID"));
            obj.setUserid(m.get("USERID"));
            obj.setFlowtype(m.get("FLOWTYPE"));
            obj.setUsetype(m.get("USETYPE"));
            obj.setConnecttype(m.get("CONNECTTYPE"));
            obj.setWebtype(m.get("WEBTYPE"));
            obj.setRecordtime(m.get("RECORDTIME"));
            obj.setUsequantity(m.get("USEQUANTITY"));
            obj.setEquipmentid(m.get("EQUIPMENTID"));
            obj.setBusinesstype(m.get("BUSINESSTYPE"));
            obj.setRemark(m.get("REMARK"));
            list.add(obj);
        }

        return list;
    }

    public List<SympFlowInfo> querySelectFlowInfo1(String date, String user, String flow, String connect, String use,String stateTime,String endTime) {
        List<SympFlowInfo> list = new ArrayList<SympFlowInfo>();
        String sql = "SELECT * FROM SYMP_FLOW_INFO WHERE 1=1 ";
        if (date != null) {
            sql += " AND RECORDTIME < '" + date + "'";
        }
        if (!user.equals("全部")) {
            if (user != null) {
                sql += " AND USERID = '" + user + "'";
            }
        }
        if (!connect.equals("全部")) {
            sql += " AND CONNECTTYPE = '" + connect + "'";
        }
        if (!flow.equals("全部")) {
            sql += " AND FLOWTYPE = '" + flow + "'";
        }
        if (!use.equals("全部")) {
            sql += " AND USETYPE = '" + use + "'";
        }
        if (!stateTime.equals("全部") ) {

            sql += " AND RECORDTIME >= '" + stateTime + "'";
        }
        if (!endTime.equals("全部")) {

            sql += " AND RECORDTIME < '" + endTime + "'";
        }

        List<Map<String, String>> dblist = dbHelper.selectSQL(sql, null);
        for (int i = 0; i < dblist.size(); i++) {
            Map<String, String> m = dblist.get(i);
            SympFlowInfo obj = new SympFlowInfo();
            obj.setFlowinfoid(m.get("FLOWINFOID"));
            obj.setUserid(m.get("USERID"));
            obj.setFlowtype(m.get("FLOWTYPE"));
            obj.setUsetype(m.get("USETYPE"));
            obj.setConnecttype(m.get("CONNECTTYPE"));
            obj.setWebtype(m.get("WEBTYPE"));
            obj.setRecordtime(m.get("RECORDTIME"));
            obj.setUsequantity(m.get("USEQUANTITY"));
            obj.setEquipmentid(m.get("EQUIPMENTID"));
            obj.setBusinesstype(m.get("BUSINESSTYPE"));
            obj.setRemark(m.get("REMARK"));
            list.add(obj);
        }

        return list;
    }

    public String querySelectFlow(String type) {
        String sumFlow = "";
        String sql = "SELECT SUM(USEQUANTITY) COUNT FROM SYMP_FLOW_INFO WHERE USETYPE='"+type+"'";


        List<Map<String, String>> dblist = dbHelper.selectSQL(sql, null);
        for (int i = 0; i < dblist.size(); i++) {
            Map<String, String> m = dblist.get(i);
            sumFlow = m.get("COUNT");
        }

        return sumFlow;
    }

}

