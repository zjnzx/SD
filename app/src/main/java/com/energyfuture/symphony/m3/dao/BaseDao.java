package com.energyfuture.symphony.m3.dao;

import android.content.Context;

import com.energyfuture.symphony.m3.util.Constants;
import com.energyfuture.symphony.m3.util.DBHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/3/27.
 */
public class BaseDao {

    DBHelper dbHelper = null;
    Context context;

    public BaseDao(Context cxt) {
        dbHelper = new DBHelper(cxt);
        context=cxt;
    }

    public SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    /**
     * 插入数据数据
     * @designer
     * @since  2014-11-6
     * @author dingwujun
     * @param @param List<PmDevice>
     * @param @return
     * @return null
     */
    public void insertListData(String tablename,List<Map<Object,Object>> perList){
        try {
            if(perList!=null&&perList.size()>0)
            {
                List<String> listSql= new ArrayList<String>();
                for(int i=0;i<perList.size();i++)
                {
                    Map<Object,Object> map =perList.get(i);
                    String column="";
                    String columnValue="";
                    for (Map.Entry<Object,Object> entry : map.entrySet()) {
                        column+= entry.getKey()+",";
                        columnValue+="'"+ entry.getValue()+"',";
                    }
                    column=column.substring(0,column.lastIndexOf(","));
                    columnValue=columnValue.substring(0,columnValue.lastIndexOf(","));
                    String sql="REPLACE INTO "+tablename+" (" +column +
                            ")VALUES (" +columnValue+")";
                    listSql.add(sql);
                }
                if(listSql!=null&&listSql.size()>0)
                {
                    dbHelper.insertSQLAll(listSql);
                }
            }

        }catch (Exception e ){
            e.printStackTrace();
            Constants.recordExceptionInfo(e, context, context.toString() + "/BaseDao");
        }
    }
}
