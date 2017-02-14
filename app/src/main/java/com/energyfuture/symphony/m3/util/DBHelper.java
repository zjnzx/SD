package com.energyfuture.symphony.m3.util;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * 数据库管理类
 *
 * @author 上海艾飞能源科技有限公司
 * @version 1.0
 * @designer
 * @since 2014-11-6
 */
public class DBHelper  {

    private static Context mCtx;
    private static SQLHelper mDbHelper;
    private static SQLiteDatabase mDb = null;

    public DBHelper(Context context) {
        this.mCtx = context;
    }

    /**
     * 打开数据库连接
     *
     * @param @return
     * @param @throws SQLException
     * @return DBHelper
     * @designer
     * @author zhaogr
     * @since 2014-11-6
     */
    public static SQLiteDatabase createDB() {
//        openCount ++;
        if (mDbHelper==null) {
            mDbHelper = new SQLHelper(mCtx);
        }
        mDb = mDbHelper.getWritableDatabase();
        if (Build.VERSION.SDK_INT >= 11) {
//            mDb.enableWriteAheadLogging();// 允许读写同时进行
        }
//        Log.i("database", "创建了一个database");
        return mDb;
//        Log.i("openDB 连接个数",openCount+"");
//        return this;
    }


    static int count = 0;
    private static synchronized List<Map<String,String>> exec(String type, Map<String, Object> cond){
        List<Map<String, String>> valueList = new ArrayList<Map<String, String>>();
        Cursor cursor = null;
//        Log.i("status","开始。。。。。。。。。。。" + (++count) + "==" + type);
        try {
            while (mDb == null || !mDb.isOpen()){
                mDb = createDB();
            }
            if ("u".equals(type)) {
                List<String> listSql = (List<String>)cond.get("sqlList");
//                mDb.beginTransaction();  //开始事务
                for (String sql : listSql) {
                    if (sql != null && !sql.equals("")) {
//                        Log.i(mDb.isOpen() + "usql = " , sql);
                        mDb.execSQL(sql);
                    }
                }
//                mDb.setTransactionSuccessful();  //设置事务成功完成
//                mDb.endTransaction();    //结束事务
            }else if ("q".equals(type)) {
                String sql = (String)cond.get("sql");
//                Log.i(mDb.isOpen() + "qsql = " , sql);
                String[] selectionArgs =  (String[])cond.get("selectionArgs");
                cursor = mDb.rawQuery(sql, selectionArgs);
                String[] cursorNames = cursor.getColumnNames();
                while (cursor.moveToNext()) {
                    Map<String, String> m = new HashMap<String, String>();
                    for (int i = 0; i < cursorNames.length; i++) {
                        m.put(cursorNames[i],cursor.getString(cursor.getColumnIndexOrThrow(cursorNames[i])));
                    }
                    valueList.add(m);
                }
            }else if ("q2".equals(type)) {
                String tableName = (String)cond.get("tableName");
                String[] columns = (String[])cond.get("columns");
                String selection = (String)cond.get("selection");
                String[] selectionArgs = (String[])cond.get("selectionArgs");
                String groupBy = (String)cond.get("groupBy");
                String having = (String)cond.get("having");
                String orderBy = (String)cond.get("orderBy");
//                Log.i(mDb.isOpen() + "qsq2 = " , tableName);
                cursor = mDb.query(tableName, columns, selection, selectionArgs, groupBy, having, orderBy);
                String[] cursorNames = cursor.getColumnNames();
                while (cursor.moveToNext()) {
                    Map<String, String> m = new HashMap<String, String>();
                    for (int i = 0; i < cursorNames.length; i++) {
                        m.put(cursorNames[i], cursor.getString(cursor.getColumnIndexOrThrow(cursorNames[i])));
                    }
                    valueList.add(m);
                }
            }
        }catch(Exception ex){
            ex.printStackTrace();
            Constants.recordExceptionInfo(ex, mCtx, mCtx.toString()+"/DBHelper");
        }finally {
            if(cursor != null) cursor.close();
            if(mDb != null)  mDb.close();
//            Log.i("status","finally。。。。。。。。。。。" + (count));
        }
//        Log.i("status","结束。。。。。。。。。。。" + (count));
        return valueList;
    }

    /**
     * 需要执行的sql语句
     *
     * @param @param sql
     * @return void
     * @designer
     * @author zhaogr
     * @since 2014-11-6
     */
    public void execSQL(String sql) {
        Map<String, Object> m = new HashMap();
        List<String> sqlList= new ArrayList<String>();
        sqlList.add(sql);
        m.put("sqlList",sqlList);
        exec("u", m);
    }

    /**
     * @param @param  sql 查询sql语句
     * @param @param  selectionArgs 查询语句参数
     * @param @return 返回结果对象
     * @return Cursor
     * @designer 公共查询方法
     * @author zhaogr
     * @since 2014-11-6
     */
    public List<Map<String,String>> selectSQL(String sql, String[] selectionArgs) {
        Map<String, Object> m = new HashMap();
        m.put("sql", sql);
        m.put("selectionArgs", selectionArgs);
        List<Map<String, String>> valueList = exec("q", m);
        return valueList;
    }

    /**
     * 关联查询
     *
     * @param @param  tableName
     * @param @param  columns
     * @param @param  selection
     * @param @param  selectionArgs
     * @param @param  groupBy
     * @param @param  having
     * @param @param  orderBy
     * @param @return
     * @return Cursor
     * @designer
     * @author zhaogr
     * @since 2014-11-6
     */
    public List<Map<String,String>> selectSQL(String tableName, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy) {
        Map<String, Object> m = new HashMap();
        m.put("tableName", tableName);
        m.put("columns", columns);
        m.put("selection", selection);
        m.put("selectionArgs", selectionArgs);
        m.put("groupBy", groupBy);
        m.put("having", having);
        m.put("orderBy", orderBy);
        List<Map<String, String>> valueList = exec("q2", m);
        return valueList;
    }

    /**
     * 批量新增
     *
     * @param @param listSql sql集合
     * @return void
     * @designer
     * @author zhaogr
     * @since 2014-11-6
     */
    public void insertSQLAll(List<String> sqlList) {
        Map<String, Object> m = new HashMap();
        m.put("sqlList", sqlList);
        exec("u", m);
    }
    /**
     * 批量删除
     *
     * @param @param listSql sql集合
     * @return void
     * @designer
     * @author zhaogr
     * @since 2014-11-6
     */
    public void deleteSQLAll(List<String> sqlList) {
        Map<String, Object> m = new HashMap();
        m.put("sqlList", sqlList);
        exec("u", m);
    }
}