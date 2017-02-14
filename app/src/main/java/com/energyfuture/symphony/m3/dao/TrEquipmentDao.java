package com.energyfuture.symphony.m3.dao;

import android.content.Context;

import com.energyfuture.symphony.m3.domain.TrEquipment;
import com.energyfuture.symphony.m3.domain.TrProject;
import com.energyfuture.symphony.m3.util.DBHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/3/24.
 */
public class TrEquipmentDao {
    DBHelper dbHelper = null;

    public TrEquipmentDao(Context cxt) {
        dbHelper = new DBHelper(cxt);
    }


    /**
     * 获取设备表信息
     *
     * @param @param  null
     * @param @return
     * @return list
     * @designer
     * @author dingwujun
     * @since 2014-11-6
     */
    public List<TrEquipment> queryTrEquipmentList(TrEquipment trEquipmentParam) {
        List<TrEquipment> list = new ArrayList<TrEquipment>();
        String  sqlWhere="";
        if(trEquipmentParam!=null) {
            if (trEquipmentParam.getStationid() != null && !trEquipmentParam.getStationid().equals("")) {
                sqlWhere = " AND STATIONID='" + trEquipmentParam.getStationid() + "'";
            }
            if(trEquipmentParam.getEquipmenttype() != null && !trEquipmentParam.getEquipmenttype().equals("")){
                sqlWhere = " AND EQUIPMENTTYPE = '" + trEquipmentParam.getEquipmenttype() + "'";
            }
            if(trEquipmentParam.getEquipmentid() != null && !trEquipmentParam.getEquipmentid().equals("")){
                sqlWhere = " AND EQUIPMENTID = '" + trEquipmentParam.getEquipmentid() + "'";
            }
        }
        String sql = "SELECT * FROM TR_EQUIPMENT WHERE 1=1 "+sqlWhere;
        List<Map<String, String>> cursor = dbHelper.selectSQL(sql, null);
        if (cursor.size() > 0) {
            for (int i = 0; i < cursor.size(); i++){
                Map<String, String> m = cursor.get(i);
                TrEquipment trEquipment = new TrEquipment();
                trEquipment.setEquipmentid(m.get("EQUIPMENTID"));
                trEquipment.setBureauname(m.get("BUREAUNAME"));
                trEquipment.setStationname(m.get("STATIONNAME"));
                trEquipment.setStationid(m.get("STATIONID"));
                trEquipment.setBureauid(m.get("BUREAUID"));
                trEquipment.setVoltage(m.get("VOLTAGE"));
                trEquipment.setEquipmenttype(m.get("EQUIPMENTTYPE"));
                trEquipment.setCompany(m.get("COMPANY"));
                trEquipment.setModel(m.get("MODEL"));
                trEquipment.setEquipmentname(m.get("EQUIPMENTNAME"));
                trEquipment.setProductiondate(m.get("PRODUCTIONDATE"));
                trEquipment.setUsedate(m.get("USEDATE"));
                trEquipment.setEquipmentimg(m.get("EQUIPMENTIMG"));
                trEquipment.setCcbh(m.get("CCBH"));
                trEquipment.setYbbh(m.get("YBBH"));
                trEquipment.setPhase(m.get("PHASE"));
                trEquipment.setXh(m.get("XH"));
                trEquipment.setOrderby(m.get("ORDERBY"));
                trEquipment.setRatedcapacity(m.get("RATEDCAPACITY"));
                trEquipment.setUpdatetime(m.get("UPDATETIME"));
                trEquipment.setCoolmethod(m.get("COOLMETHOD"));
                trEquipment.setVoltagesetmodel(m.get("VOLTAGESETMODEL"));
                trEquipment.setPhasecount(m.get("PHASECOUNT"));
                trEquipment.setFrequency(m.get("FREQUENCY"));
                trEquipment.setConnectnumber(m.get("CONNECTNUMBER"));
                trEquipment.setUsecondition(m.get("USECONDITION"));
                trEquipment.setOil(m.get("OIL"));
                list.add(trEquipment);
            }
        }
        return list;
    }

    /**
     * 查询没有发布任务的设备
     *
     * @param @param  null
     * @param @return
     * @return list
     * @designer
     * @author dingwujun
     * @since 2014-11-6
     */
    public List<TrEquipment> queryTrEquipment(TrProject trProject) {
        List<TrEquipment> list = new ArrayList<TrEquipment>();
        String sql = "SELECT EQUIP.* FROM TR_EQUIPMENT EQUIP where EQUIP.STATIONID='"+trProject.getStationid()+"' and EQUIP.EQUIPMENTID not in "
                +"(select task.EQUIPMENTID from TR_TASK task where task.PROJECTID='"+trProject.getProjectid()+"')";
        List<Map<String, String>> cursor = dbHelper.selectSQL(sql, null);
        if (cursor.size() > 0) {
            for (int i = 0; i < cursor.size(); i++){
                Map<String, String> m = cursor.get(i);
                TrEquipment trEquipment = new TrEquipment();
                trEquipment.setEquipmentid(m.get("EQUIPMENTID"));
                trEquipment.setBureauname(m.get("BUREAUNAME"));
                trEquipment.setStationname(m.get("STATIONNAME"));
                trEquipment.setStationid(m.get("STATIONID"));
                trEquipment.setBureauid(m.get("BUREAUID"));
                trEquipment.setVoltage(m.get("VOLTAGE"));
                trEquipment.setEquipmenttype(m.get("EQUIPMENTTYPE"));
                trEquipment.setCompany(m.get("COMPANY"));
                trEquipment.setModel(m.get("MODEL"));
                trEquipment.setEquipmentname(m.get("EQUIPMENTNAME"));
                trEquipment.setProductiondate(m.get("PRODUCTIONDATE"));
                trEquipment.setUsedate(m.get("USEDATE"));
                trEquipment.setEquipmentimg(m.get("EQUIPMENTIMG"));
                trEquipment.setCcbh(m.get("CCBH"));
                trEquipment.setYbbh(m.get("YBBH"));
                trEquipment.setPhase(m.get("PHASE"));
                trEquipment.setXh(m.get("XH"));
                trEquipment.setOrderby(m.get("ORDERBY"));
                trEquipment.setRatedcapacity(m.get("RATEDCAPACITY"));
                trEquipment.setUpdatetime(m.get("UPDATETIME"));
                trEquipment.setCoolmethod(m.get("COOLMETHOD"));
                trEquipment.setVoltagesetmodel(m.get("VOLTAGESETMODEL"));
                trEquipment.setPhasecount(m.get("PHASECOUNT"));
                trEquipment.setFrequency(m.get("FREQUENCY"));
                trEquipment.setConnectnumber(m.get("CONNECTNUMBER"));
                trEquipment.setUsecondition(m.get("USECONDITION"));
                trEquipment.setOil(m.get("OIL"));
                list.add(trEquipment);
            }
        }
        return list;
    }

    public void insertListData(List<TrEquipment> trEquipmentList) {
        if(trEquipmentList!=null&&trEquipmentList.size()>0)
        {
            List<String> listSql= new ArrayList<String>();
            for(int i=0;i<trEquipmentList.size();i++)
            {
                String sql="REPLACE INTO TR_EQUIPMENT "+
                        "(EQUIPMENTID,BUREAUNAME,BUREAUID,STATIONNAME,STATIONID,VOLTAGE,EQUIPMENTTYPE,COMPANY,MODEL,"+
                        "EQUIPMENTNAME,PRODUCTIONDATE,USEDATE,XH,EQUIPMENTIMG,CCBH,YBBH,ORDERBY,RATEDCAPACITY,UPDATETIME,"+
                        "VOLTAGESETMODEL,PHASE,PHASECOUNT,FREQUENCY,CONNECTNUMBER,USECONDITION,COOLMETHOD,OIL)"+
                        "VALUES "+
                        "('"+trEquipmentList.get(i).getEquipmentid()+"'," +
                        "'"+trEquipmentList.get(i).getBureauname()+"'," +
                        "'"+trEquipmentList.get(i).getBureauid()+"'," +
                        "'"+trEquipmentList.get(i).getStationname()+"'," +
                        "'"+trEquipmentList.get(i).getStationid()+"'," +
                        "'"+trEquipmentList.get(i).getVoltage()+"'," +
                        "'"+trEquipmentList.get(i).getEquipmenttype()+"'," +
                        "'"+trEquipmentList.get(i).getCompany()+"'," +
                        "'"+trEquipmentList.get(i).getModel()+"'," +
                        "'"+trEquipmentList.get(i).getEquipmentname()+"'," +
                        "'"+trEquipmentList.get(i).getProductiondate()+"'," +
                        "'"+trEquipmentList.get(i).getUsedate()+"'," +
                        "'"+trEquipmentList.get(i).getXh()+"'," +
                        "'"+trEquipmentList.get(i).getEquipmentimg()+"'," +
                        "'"+trEquipmentList.get(i).getCcbh()+"'," +
                        "'"+trEquipmentList.get(i).getYbbh()+"'," +
                        "'"+trEquipmentList.get(i).getOrderby()+"'," +
                        "'"+trEquipmentList.get(i).getRatedcapacity()+"'," +
                        "'"+trEquipmentList.get(i).getUpdatetime()+"'," +
                        "'"+trEquipmentList.get(i).getVoltagesetmodel()+"'," +
                        "'"+trEquipmentList.get(i).getPhase()+"'," +
                        "'"+trEquipmentList.get(i).getPhasecount()+"'," +
                        "'"+trEquipmentList.get(i).getFrequency()+"'," +
                        "'"+trEquipmentList.get(i).getConnectnumber()+"'," +
                        "'"+trEquipmentList.get(i).getUsecondition()+"'," +
                        "'"+trEquipmentList.get(i).getCoolmethod()+"'," +
                        "'"+trEquipmentList.get(i).getOil()+"'" +
                        ")";
                listSql.add(sql);
            }
            if(listSql!=null&&listSql.size()>0)
            {
                dbHelper.insertSQLAll(listSql);
            }
        }
    }
    public void updateTrEquipmentList(Map<Object,Object> columnsMap,Map<Object,Object> wheresMap){
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
            sql="UPDATE TR_EQUIPMENT SET " +columns +" WHERE 1=1 "+sqlWhere;

            if(sql!=null&&!sql.equals("")) {
                dbHelper.execSQL(sql);
            }
        }
    }

    //删除设备
    public void deleteListData(List<TrEquipment> trEquipmentList){
        if(trEquipmentList != null && trEquipmentList.size() > 0){
            {
                List<String> listSql= new ArrayList<String>();
                for(int i=0;i<trEquipmentList.size();i++)
                {
                    String sql="Delete from tr_equipment where STATIONID='" + trEquipmentList.get(i).getStationid() + "'";
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
