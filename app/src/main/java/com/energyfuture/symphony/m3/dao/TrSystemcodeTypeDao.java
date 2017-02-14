package com.energyfuture.symphony.m3.dao;

import android.content.Context;

import com.energyfuture.symphony.m3.domain.TrSystemcodetype;
import com.energyfuture.symphony.m3.util.DBHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/3/24.
 */
public class TrSystemcodeTypeDao {
    DBHelper dbHelper = null;

    public TrSystemcodeTypeDao(Context cxt) {
        dbHelper = new DBHelper(cxt);
    }

    /**
     * 获取系统代码类型表信息
     *
     * @param @param  null
     * @param @return
     * @return list
     * @designer
     * @author dingwujun
     * @since 2014-11-6
     */
    public List<TrSystemcodetype> queryTrSystemcodetypesList(TrSystemcodetype trSystemcodetypeParam) {
        List<TrSystemcodetype> list = new ArrayList<TrSystemcodetype>();
        String  sqlWhere="";

        String sql = "SELECT * FROM TR_SYSTEMCODETYPE WHERE 1=1 "+sqlWhere;
        List<Map<String,String>> dblist = dbHelper.selectSQL(sql, null);
        for (int i = 0; i < dblist.size(); i++){
            Map<String, String> m = dblist.get(i);
            TrSystemcodetype trSystemcodetype = new TrSystemcodetype();
            trSystemcodetype.setId(m.get("ID"));
            trSystemcodetype.setCodename(m.get("CODENAME"));
            trSystemcodetype.setCodevalue(m.get("CODEVALUE"));
            trSystemcodetype.setForder(m.get("FORDER"));
            list.add(trSystemcodetype);
            }

        return list;
    }


}
