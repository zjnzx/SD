package com.energyfuture.symphony.m3.dao;

import android.content.Context;

import com.energyfuture.symphony.m3.domain.TrDataSynchronization;
import com.energyfuture.symphony.m3.util.DBHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/3/24.
 */
public class TrDataSynchronizationDao {
    DBHelper dbHelper = null;

    public TrDataSynchronizationDao(Context cxt) {
        dbHelper = new DBHelper(cxt);
    }

    public SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    /**
     * 获取最大版本号
     *
     * @param @param  null
     * @param @return
     * @return list
     * @designer
     * @author dingwujun
     * @since 2014-11-6
     */
    public String queryTrDataSynchronizationMax() {
        String max = "";
        String sql = "SELECT MAX(VERSIONNUMBER) AS MAX FROM TR_DATA_SYNCHRONIZATION";
        List<Map<String,String>> dblist = dbHelper.selectSQL(sql, null);
        for (int i = 0; i < dblist.size(); i++){
            Map<String, String> m = dblist.get(i);
            max = m.get("MAX");
        }
        return max;
    }

    public List<TrDataSynchronization> queryTrDataSynchronizationList(String max) {
        List<TrDataSynchronization> trDataSynchronizations = new ArrayList<TrDataSynchronization>();
        String sqlwhere = "";
        if(max != null){
            sqlwhere += " AND VERSIONNUMBER > " + max;
        }
        String sql = "SELECT * FROM TR_DATA_SYNCHRONIZATION WHERE 1=1 AND OPERTYPE <> '3' AND DATATYPE = '1'" + sqlwhere;
        List<Map<String,String>> dblist = dbHelper.selectSQL(sql, null);
        for (int i = 0; i < dblist.size(); i++){
            Map<String, String> m = dblist.get(i);
            TrDataSynchronization trDataSynchronization = new TrDataSynchronization();
            trDataSynchronization.setSynchronizationid(m.get("SYNCHRONIZATIONID"));
            trDataSynchronizations.add(trDataSynchronization);
        }
        return trDataSynchronizations;
    }

}