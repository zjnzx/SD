package com.energyfuture.symphony.m3.dao;

import android.content.Context;

import com.energyfuture.symphony.m3.domain.SympVersion;
import com.energyfuture.symphony.m3.util.DBHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/3/24.
 */
public class SympVersionDao {
    DBHelper dbHelper = null;

    public SympVersionDao(Context cxt) {
        dbHelper = new DBHelper(cxt);
    }

    public SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 查询版本信息表
     * @return
     */
    public List<SympVersion> querySympVersionList() {
        List<SympVersion> list = new ArrayList<SympVersion>();
        String sql = "SELECT * FROM SYMP_VERSION ORDER BY UPDATEDATE DESC";
        List<Map<String,String>> dblist = dbHelper.selectSQL(sql, null);
        for (int i = 0; i < dblist.size(); i++){
            Map<String, String> m = dblist.get(i);
            SympVersion sympVersion = new SympVersion();
            sympVersion.setId(m.get("ID"));
            sympVersion.setVersionCode(m.get("VERSIONCODE"));
            sympVersion.setUpdateDate(m.get("UPDATEDATE"));
            sympVersion.setUpdateContent(m.get("UPDATECONTENT"));
            list.add(sympVersion);
        }
        return list;
    }

}
