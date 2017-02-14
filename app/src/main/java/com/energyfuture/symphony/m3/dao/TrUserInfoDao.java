package com.energyfuture.symphony.m3.dao;

import android.content.Context;

import com.energyfuture.symphony.m3.domain.UserInfo;
import com.energyfuture.symphony.m3.util.DBHelper;
import com.energyfuture.symphony.m3.util.SQLHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/3/26.
 */
public class TrUserInfoDao {
    DBHelper dbHelper = null;
    public TrUserInfoDao(Context context){
        dbHelper = new DBHelper(context);
    }


    public List<UserInfo> queryUserInfoList(UserInfo userInfopram){
        List<UserInfo> list = new ArrayList<UserInfo>();
        String sqlwhere="";
        if(userInfopram != null){
            if(userInfopram.getYhmm()!=null&&!userInfopram.getYhmm().equals(""))
            {
                sqlwhere+=" AND YHMM='"+userInfopram.getYhmm()+"'";
            }
            if(userInfopram.getYhid()!=null&&!userInfopram.getYhid().equals(""))
            {
                sqlwhere+=" AND YHID='"+userInfopram.getYhid()+"'";
            }
            if(userInfopram.getXm()!=null&&!userInfopram.getXm().equals(""))
            {
                sqlwhere+=" AND XM='"+userInfopram.getXm()+"'";
            }
            if(userInfopram.getBmbh()!=null&&!userInfopram.getBmbh().equals(""))
            {
                sqlwhere+=" AND BMBH='"+userInfopram.getBmbh()+"'";
            }
        }

        String sql = "SELECT * FROM USER_INFO WHERE 1=1 "+sqlwhere + " ORDER BY SSXT ASC";
        List<Map<String,String>> dblist = dbHelper.selectSQL(sql, null);
        for (int i = 0; i < dblist.size(); i++){
            Map<String, String> m = dblist.get(i);
            UserInfo userInfo = new UserInfo();
            userInfo.setYhid(m.get("YHID"));
            userInfo.setXm(m.get("XM"));
            userInfo.setYhmm(m.get("YHMM"));
            userInfo.setBmbh(m.get("BMBH"));
            userInfo.setSfzh(m.get("SFZH"));
            userInfo.setYhzw(m.get("YHZW"));
            userInfo.setEmail(m.get("EMAIL"));
            userInfo.setSjhm(m.get("SJHM"));
            userInfo.setYhzt(m.get("YHZT"));
            userInfo.setCjr(m.get("CJR"));
            userInfo.setCjsj(m.get("CJSJ"));
            userInfo.setXgr(m.get("XGR"));
            userInfo.setXgsj(m.get("XGSJ"));
            userInfo.setSsxt(m.get("SSXT"));
            list.add(userInfo);
        }
        return list;
    }

    /**
     * 删除
     * @param userInfoList
     */
    public void deleteListData(List<UserInfo> userInfoList){
        if(userInfoList != null && userInfoList.size() > 0){
            {
                List<String> listSql= new ArrayList<String>();
                for(int i=0;i<userInfoList.size();i++)
                {
                    String sql="Delete from USER_INFO where bmid='" + userInfoList.get(i).getYhid() + "'";
                    listSql.add(sql);
                }
                if(listSql!=null&&listSql.size()>0)
                {
                    dbHelper.deleteSQLAll(listSql);
                }
            }
        }
    }
    public boolean updateUserInfo(Map<Object,Object> columnsMap,Map<Object,Object> wheresMap)
    {
        boolean flag=false;
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
            sql="UPDATE USER_INFO SET " +columns +" WHERE 1=1 "+sqlWhere;

            if(sql!=null&&!sql.equals("")) {
                dbHelper.execSQL(sql);
                flag=true;
            }
        }
        return flag;
    }
}
