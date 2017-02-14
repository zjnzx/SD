package com.energyfuture.symphony.m3.dao;

import android.content.Context;

import com.energyfuture.symphony.m3.domain.TrFeedBackTheme;
import com.energyfuture.symphony.m3.util.DBHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/11/25.
 */
public class TrFeedBackThemeDao {
    DBHelper dbHelper = null;

    public TrFeedBackThemeDao(Context cxt) {
        dbHelper = new DBHelper(cxt);
    }

    public SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    /**
     *查询反馈主题列表
     * @param @return
     * @return list
     * @designer
     * @author
     * @since 2015/11/25.
     */
    public List<TrFeedBackTheme> queryTrFeedBackThemeList(TrFeedBackTheme parameterObj) {
        List<TrFeedBackTheme> list = new ArrayList<TrFeedBackTheme>();
        String  sqlWhere="";
        if(parameterObj.getCreatepersonid()!=null&&!parameterObj.getCreatepersonid().equals(""))
        {
            sqlWhere+=" AND CREATEPERSONID='"+parameterObj.getCreatepersonid()+"'";
        }
        String sql = "SELECT * FROM TR_FEEDBACK_THEME WHERE 1=1 "+sqlWhere;
        List<Map<String,String>> dblist = dbHelper.selectSQL(sql, null);
        for (int i = 0; i < dblist.size(); i++){
            Map<String, String> m = dblist.get(i);
            TrFeedBackTheme obj = new TrFeedBackTheme();
                obj.setId(m.get("ID"));
                obj.setThemetitle(m.get("THEMETITLE"));
                obj.setThemecontent(m.get("THEMECONTENT"));
                obj.setCreatepersonid(m.get("CREATEPERSONID"));
                obj.setCreatetime(m.get("CREATETIME"));
                obj.setThemestate(m.get("THEMESTATE"));
                list.add(obj);
            }
        return list;
    }


    /**
     * 插入反馈主贴表数据
     * @designer
     * @since  2015/11/25.
     * @author
     * @param @param List<PmDevice>
     * @param @return
     * @return null
     */
    public void insertListData(List<TrFeedBackTheme> parameterObjList)
    {
        if(parameterObjList!=null&&parameterObjList.size()>0)
        {
            List<String> listSql= new ArrayList<String>();
            for(int i=0;i<parameterObjList.size();i++)
            {
                String sql="REPLACE INTO TR_FEEDBACK_THEME (" +
                        "ID,THEMETITLE,THEMECONTENT,CREATEPERSONID,CREATETIME,THEMESTATE" +
                        ") VALUES (" +
                        "'"+parameterObjList.get(i).getId()+"'," +
                        "'"+parameterObjList.get(i).getThemetitle()+"'," +
                        "'"+parameterObjList.get(i).getThemecontent()+"'," +
                        "'"+parameterObjList.get(i).getCreatepersonid()+"'," +
                        "'"+parameterObjList.get(i).getCreatetime()+"'," +
                        "'"+parameterObjList.get(i).getThemestate()+"'" +
                        ")";
                listSql.add(sql);
            }
            if(listSql!=null&&listSql.size()>0)
            {
                dbHelper.insertSQLAll(listSql);
            }
        }
    }
}


