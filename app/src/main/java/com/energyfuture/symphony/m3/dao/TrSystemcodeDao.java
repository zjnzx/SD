package com.energyfuture.symphony.m3.dao;

import android.content.Context;

import com.energyfuture.symphony.m3.domain.TrSystemcode;
import com.energyfuture.symphony.m3.util.DBHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/3/24.
 */
public class TrSystemcodeDao {
    DBHelper dbHelper = null;

    public TrSystemcodeDao(Context cxt) {
        dbHelper = new DBHelper(cxt);
    }

    /**
     * 获取代码表信息
     *
     * @param @param  null
     * @param @return
     * @return list
     * @designer
     * @author dingwujun
     * @since 2014-11-6
     */
    public List<TrSystemcode> queryTrSystemcodesList(TrSystemcode trSystemcodeParam) {
        List<TrSystemcode> list = new ArrayList<TrSystemcode>();
        String  sqlWhere="";
        if(trSystemcodeParam.getCodeid()!=null&&!trSystemcodeParam.getCodeid().equals(""))
        {
            sqlWhere=" AND CODEID='"+trSystemcodeParam.getCodeid()+"'";
        }
        if(trSystemcodeParam.getCodetype()!=null&&!trSystemcodeParam.getCodetype().equals(""))
        {
            sqlWhere=" AND CODETYPE='"+trSystemcodeParam.getCodetype()+"'";
        }
        if(trSystemcodeParam.getCodevalue()!=null&&!trSystemcodeParam.getCodevalue().equals(""))
        {
            sqlWhere=" AND CODEVALUE='"+trSystemcodeParam.getCodevalue()+"'";
        }
        if(trSystemcodeParam.getCodetype()!=null&&!trSystemcodeParam.getCodetype().equals(""))
        {
            sqlWhere=" AND CODETYPE='"+trSystemcodeParam.getCodetype()+"'";
        }

        String sql = "SELECT * FROM TR_SYSTEMCODE WHERE 1=1 "+sqlWhere;
        List<Map<String,String>> dblist = dbHelper.selectSQL(sql, null);
        for (int i = 0; i < dblist.size(); i++){
            Map<String, String> m = dblist.get(i);
            TrSystemcode trSystemcode = new TrSystemcode();
            trSystemcode.setCodeid(m.get("CODEID"));
            trSystemcode.setCodename(m.get("CODENAME"));
            trSystemcode.setCodevalue(m.get("CODEVALUE"));
            trSystemcode.setCodetype(m.get("CODETYPE"));
            trSystemcode.setForder(m.get("FORDER"));
            trSystemcode.setPid(m.get("PID"));
            trSystemcode.setIsleaf(m.get("ISLEAF"));
            list.add(trSystemcode);
            }

        return list;
    }


}
