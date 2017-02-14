package com.energyfuture.symphony.m3.dao;

import android.content.Context;

import com.energyfuture.symphony.m3.domain.UserInfo;
import com.energyfuture.symphony.m3.util.DBHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/3/26.
 */
public class SympUserInfoDao {
    DBHelper dbHelper = null;
    public SympUserInfoDao(Context context){
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
        }

        String sql = "SELECT * FROM USER_INFO WHERE 1=1 "+sqlwhere + "ORDER BY SSXT";
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
//                userInfo.setMj(m.get("MJ"));
            userInfo.setCjr(m.get("CJR"));
            userInfo.setCjsj(m.get("CJSJ"));
            userInfo.setXgr(m.get("XGR"));
            userInfo.setXgsj(m.get("XGSJ"));
            userInfo.setSsxt(m.get("SSXT"));
            list.add(userInfo);
        }
        return list;
    }
}
