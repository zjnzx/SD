package com.energyfuture.symphony.m3.dao;

import android.content.Context;
import android.util.Log;

import com.energyfuture.symphony.m3.common.Base;
import com.energyfuture.symphony.m3.common.StringUtils;
import com.energyfuture.symphony.m3.domain.SympMessage;
import com.energyfuture.symphony.m3.domain.SympMessageReal;
import com.energyfuture.symphony.m3.util.Constants;
import com.energyfuture.symphony.m3.util.DBHelper;
import com.energyfuture.symphony.m3.util.JsonHelper;
import com.energyfuture.symphony.m3.util.SQLHelper;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by heqiang on 2015/4/15
 */
public class SympMessageRealDao {
    DBHelper dbHelper = null;
    private Context context;
    private String networkInfo;

    public SympMessageRealDao(Context cxt) {
        this.context = cxt;
        dbHelper = new DBHelper(cxt);
    }
    /**
     * 获取消息实时表数据
     *
     * @param @param  null
     * @param @return
     * @return list
     * @designer
     * @author heq
     * @since 2015-04-15
     */
    public List<Map<String,String>> querySympMessageRealByParameter(SympMessageReal sympMessageReal) {
        List<Map<String,String>> list = new ArrayList<Map<String,String>>();
        try {
            String sql = "SELECT * FROM SYMP_MESSAGE_REAL WHERE 1=1 ";
            if(sympMessageReal != null && !StringUtils.isEmpty(sympMessageReal.getMessageid()))
            {
                sql+=" AND MESSAGEID ='"+sympMessageReal.getMessageid()+"'";
            }
            if(sympMessageReal != null && ("2").equals(sympMessageReal.getMessagetype()))
            {
                sql+=" AND MESSAGETYPE = '"+sympMessageReal.getMessagetype()+"'";
            }else if(sympMessageReal != null && ("1").equals(sympMessageReal.getMessagetype())){
                sql+=" AND MESSAGETYPE <> '2'";
            }

            sql += " ORDER BY CREATEDATE ASC LIMIT 20";

//            dbHelper.openDB();

            List<Map<String,String>> dblist = dbHelper.selectSQL(sql, null);
//            Log.i("=================查询消息表的sql=",sql);
            for (int i = 0; i < dblist.size(); i++){
                Map<String, String> m = dblist.get(i);
                    SympMessageReal bean = new SympMessageReal();
                    Map<String,String> map = new HashMap<String,String>();
                    map.put("MESSAGEID",m.get("MESSAGEID"));
                    map.put("CONTENT",m.get("CONTENT"));
                    map.put("MESSAGESTATE",m.get("MESSAGESTATE"));
                    map.put("MESSAGETYPE",m.get("MESSAGETYPE"));
                    map.put("FCOMMENT",m.get("FCOMMENT"));
                    map.put("CREATEDATE",m.get("CREATEDATE"));
                    map.put("SENDDATE",m.get("SENDDATE"));
                    map.put("PRESONID",m.get("PRESONID"));
                    list.add(map);
            }
//            dbHelper.closeDB();
        }catch (Exception e){
//            if (dbHelper!=null){
//                dbHelper.closeDB();
//            }
            e.printStackTrace();
            Constants.recordExceptionInfo(e, context, context.toString() + "/SympMessageRealDao");
        }finally {
//            if (dbHelper!=null){
//                dbHelper.closeDB();
//            }
        }

        return list;
    }

    /**
     * 传入对象
     * @return
     */
    public Context getContent(){
        return SympMessageRealDao.this.context;
    }
    /**
     * 插入消息实时表数据
     * @designer
     * @since  2015-04-15
     * @author heq
     * @param @param List<PmDevice>
     * @param @return
     * @return null
     */
    public void insertListData(List<SympMessageReal> sympMessageRealList)
    {
     try{
         if(sympMessageRealList!=null&&sympMessageRealList.size()>0)
         {
             List<String> listSql= new ArrayList<String>();
             for(int i=0;i<sympMessageRealList.size();i++)
             {
                 String sql="REPLACE INTO SYMP_MESSAGE_REAL (" +
                         "MESSAGEID,PRESONID,CONTENT,MESSAGESTATE,MESSAGETYPE,FCOMMENT,CREATEDATE,SENDDATE"+
                         ") VALUES (" +
                         "'"+ Constants.getUuid()+"'," +
                         "'"+sympMessageRealList.get(i).getPresonid()+"'," +
                         "'"+sympMessageRealList.get(i).getContent()+"'," +
                         "'"+sympMessageRealList.get(i).getMessagestate()+"'," +
                         "'"+sympMessageRealList.get(i).getMessagetype()+"'," +
                         "'"+sympMessageRealList.get(i).getFcomment()+"'," +
                         "'"+ Base.getNow() +"'," +
                         "'"+(sympMessageRealList.get(i).getSenddate()==null ? "" : sympMessageRealList.get(i).getSenddate())+"'" +
                         ")";
                 listSql.add(sql);
             }
             if(listSql!=null&&listSql.size()>0)
             {
                 dbHelper.insertSQLAll(listSql);

             }
         }
     }catch (Exception e){
         e.printStackTrace();
         Constants.recordExceptionInfo(e, context, context.toString() + "/SympMessageRealDao");
     }

    }

    /**
     * 获取消息实时表数据
     * 设置消息记录中调用
     *
     * @param sympMessageReal
     * @return
     */
    public List<Map<String,String>> querySympMessageRealByParameter1(SympMessageReal sympMessageReal) {
        List<Map<String,String>> list = new ArrayList<Map<String,String>>();
        try {
            String sql = "SELECT CREATEDATE FROM SYMP_MESSAGE_REAL WHERE 1=1 ";
            sql += " ORDER BY CREATEDATE ASC LIMIT 1";

            List<Map<String,String>> dblist = dbHelper.selectSQL(sql, null);
            for (int i = 0; i < dblist.size(); i++){
                Map<String, String> m = dblist.get(i);
                SympMessageReal bean = new SympMessageReal();
                Map<String,String> map = new HashMap<String,String>();

                map.put("CREATEDATE",m.get("CREATEDATE"));
                list.add(map);
            }
        }catch (Exception e){
            e.printStackTrace();
            Constants.recordExceptionInfo(e, context, context.toString() + "/SympMessageRealDao");
        }finally {
        }

        return list;
    }

    public List<Map<String,String>> querySympMessageRealByParameter2(String date,String type) {
        List<Map<String,String>> list = new ArrayList<Map<String,String>>();
        try {
            String sql = "select * from SYMP_MESSAGE_REAL  WHERE 1=1 ";
            if(date!=null){
                sql+=" AND CREATEDATE> '"+date+"'";
            }
            if(type!=null && ("1").equals(type)){
                sql+=" AND MESSAGETYPE <> '2' ";
            }else if(type!=null && ("2").equals(type)){
                sql+=" AND MESSAGETYPE = '" + type + "'";
            }
            sql += " ORDER BY CREATEDATE ASC LIMIT 20";

            List<Map<String,String>> dblist = dbHelper.selectSQL(sql, null);
            for (int i = 0; i < dblist.size(); i++){
                Map<String, String> m = dblist.get(i);
                Map<String,String> map = new HashMap<String,String>();

                map.put("MESSAGEID",m.get("MESSAGEID"));
                map.put("CONTENT",m.get("CONTENT"));
                map.put("MESSAGESTATE",m.get("MESSAGESTATE"));
                map.put("MESSAGETYPE",m.get("MESSAGETYPE"));
                map.put("FCOMMENT",m.get("FCOMMENT"));
                map.put("CREATEDATE",m.get("CREATEDATE"));
                map.put("SENDDATE",m.get("SENDDATE"));
                map.put("PRESONID",m.get("PRESONID"));
                list.add(map);
            }
        }catch (Exception e){
            e.printStackTrace();
            Constants.recordExceptionInfo(e, context, context.toString() + "/SympMessageRealDao");
        }finally {
        }

        return list;
    }

    public String queryMessageCount(String type){
        String count = "";
        String sql = "SELECT COUNT(MESSAGEID) C FROM SYMP_MESSAGE_REAL WHERE 1=1 ";
        if(("1").equals(type)){
            sql += "AND MESSAGETYPE <> 2";
        }else if(("2").equals(type)){
            sql += "AND MESSAGETYPE = '" + type + "'";
        }
        List<Map<String, String>> dblist = dbHelper.selectSQL(sql, null);
        for (int i = 0; i < dblist.size(); i++) {
            Map<String, String> m = dblist.get(i);
            count = m.get("C");
        }
        return count;
    }

    /**
     * 获取消息实时表数据
     *
     * @param @param  null
     * @param @return
     * @return list
     * @designer
     * @author heq
     * @since 2015-04-15
     */
    public List<Map<String,String>> querySympMessageReal(SympMessageReal sympMessageReal) {
        List<Map<String,String>> list = new ArrayList<Map<String,String>>();
        try {
            String sql = "SELECT * FROM SYMP_MESSAGE_REAL WHERE 1=1 ";
            if(sympMessageReal != null && ("2").equals(sympMessageReal.getMessagetype()))
            {
                sql+=" AND MESSAGETYPE = '"+sympMessageReal.getMessagetype()+"'";
            }else if(sympMessageReal != null && ("1").equals(sympMessageReal.getMessagetype())){
                sql+=" AND MESSAGETYPE <> '2'";
            }

            sql += " ORDER BY CREATEDATE";

            List<Map<String,String>> dblist = dbHelper.selectSQL(sql, null);
//            Log.i("=================查询消息表的sql=",sql);
            for (int i = 0; i < dblist.size(); i++){
                Map<String, String> m = dblist.get(i);
                SympMessageReal bean = new SympMessageReal();
                Map<String,String> map = new HashMap<String,String>();
                map.put("MESSAGEID",m.get("MESSAGEID"));
                map.put("CONTENT",m.get("CONTENT"));
                map.put("MESSAGESTATE",m.get("MESSAGESTATE"));
                map.put("MESSAGETYPE",m.get("MESSAGETYPE"));
                map.put("FCOMMENT",m.get("FCOMMENT"));
                map.put("CREATEDATE",m.get("CREATEDATE"));
                map.put("SENDDATE",m.get("SENDDATE"));
                map.put("PRESONID",m.get("PRESONID"));
                list.add(map);
            }
        }catch (Exception e){
            e.printStackTrace();
            Constants.recordExceptionInfo(e, context, context.toString() + "/SympMessageRealDao");
        }finally {
        }

        return list;
    }

    /**
     * 根据id删除历史表数据
     * @designer
     * @since  2015-04-15
     * @author zhaogr
     * @param @param List<PmDevice>
     * @param @return
     * @return null
     */

    public void deleteMessageData(List<String> sympMessageRealList) {
        try{
            if (sympMessageRealList != null && sympMessageRealList.size() > 0) {
                List<String> listSql = new ArrayList<String>();
                for (int i = 0; i < sympMessageRealList.size(); i++) {
                    String sql = "DELETE FROM SYMP_MESSAGE_REAL WHERE 1=1";
                    sql += " AND MESSAGEID='"+sympMessageRealList.get(i)+"'";
                    Log.i("============5555555==删除数据",sql);

                    listSql.add(sql);
                }
                if (listSql != null && listSql.size() > 0) {
                    dbHelper.insertSQLAll(listSql);

                }
            }
        }catch (Exception e){
            e.printStackTrace();
            Constants.recordExceptionInfo(e, context, context.toString() + "/SympMessageRealDao");
        }

    }


    /**
     * 插入消息历史表数据
     * @designer
     * @since  2015-04-15
     * @author zhaogr
     * @param @param List<PmDevice>
     * @param @return
     * @return null
     */
    public void insertMessageListData(List<SympMessage> sympMessageRealList)
    {
        try{
            if(sympMessageRealList!=null&&sympMessageRealList.size()>0)
            {
                List<String> listSql= new ArrayList<String>();
                for(int i=0;i<sympMessageRealList.size();i++)
                {
                    String sql="REPLACE INTO SYMP_MESSAGE (" +
                            "MESSAGEID,PRESONID,CONTENT,MESSAGESTATE,MESSAGETYPE,FCOMMENT,CREATEDATE,SENDDATE"+
                            ")VALUES (" +
                            "'"+sympMessageRealList.get(i).getMessageid()+"'," +
                            "'"+sympMessageRealList.get(i).getPresonid()+"'," +
                            "'"+sympMessageRealList.get(i).getContent()+"'," +
                            "'"+sympMessageRealList.get(i).getMessagestate()+"'," +
                            "'"+sympMessageRealList.get(i).getMessagetype()+"'," +
                            "'"+sympMessageRealList.get(i).getFcomment()+"'," +
                            "'"+sympMessageRealList.get(i).getCreatedate()+"'," +
                            "'"+Base.getNow()+"'" +
                            ")";
                    listSql.add(sql);
                }
                if(listSql!=null&&listSql.size()>0)
                {
                    dbHelper.insertSQLAll(listSql);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            Constants.recordExceptionInfo(e, context, context.toString() + "/SympMessageRealDao");
        }


    }

    /**
     * 根据id删除消息实时表数据
     * @designer
     * @since  2015-04-15
     * @author heq
     * @param @return
     * @return null
     */
    public void deleteSympMessageReal(String id)
    {
        try{
            String sql="";
            if(id!=null&&!id.equals(""))
            {
                sql="DELETE FROM SYMP_MESSAGE_REAL WHERE MESSAGEID='" +id +"'";
                if(sql!=null&&!sql.equals(""))
                {
                    dbHelper.execSQL(sql);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            Constants.recordExceptionInfo(e, context, context.toString() + "/SympMessageRealDao");
        }

    }


    /**
     *
     * @param sympMessageReal	消息实体，CONTENT不需要
     * @param dataTypes		数据类型
     * @param operationType		操作类型，1、新增、2、修改、3、删除
     * @param messageContents	消息内容，Map中key表示列名称，列名称大写，value表示列对应的值
     * @return		返回结果，1、新增成功  2.新增失败
     * @throws Exception
     */
    public int addMessageList(SympMessageReal sympMessageReal, List<String> dataTypes,
                          int operationType,Map<String,List<Map<String, Object>> > messageContents )
            throws Exception {
        List<Map<String, Object>> messageResultList = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < dataTypes.size(); i++) {
            int dataType = Integer.parseInt(dataTypes.get(i));
            Map<String, Object> messageResult = new HashMap<String, Object>();
//            StringBuffer message = new StringBuffer("");
            String message = sympMessageReal.getContent();
            String tablename = "";
            String key = "";
            switch (dataType) {
                // 项目
                case 1:
                    tablename = "TR_PROJECT";
                    key = "PROJECTID";
                    break;
                // 任务
                case 2:
                    tablename = "TR_TASK";
                    key = "TASKID";
                    break;
                // 用户
                case 3:
                    tablename = "USER_INFO";
                    key = "YHID";
                    break;
                // 安全须知实体表
                case 4:
                    tablename = "TR_SAFETYTIPS_OBJ";
                    key = "ID";
                    break;
                // 记录本次任务所携带的工具
                case 5:
                    tablename = "TR_EQUIPMENT_TOOLS";
                    key = "ID";
                    break;
                // 沟通交流主贴
                case 6:
                    tablename = "SYMP_COMMUNICATION_THEME";
                    key = "ID";
                    break;
                // 沟通交流回复
                case 7:
                    tablename = "SYMP_COMMUNICATION_REPLY";
                    key = "ID";
                    break;
                // 沟通交流提醒
                case 8:
                    tablename = "SYMP_COMMUNICATION_PROMPT";
                    key = "ID";
                    break;
                // 沟通交流附件
                case 9:
                    tablename = "SYMP_COMMUNICATION_ACCESSORY";
                    key = "ID";
                    break;
                // 反馈回复
                case 10:
                    tablename = "TR_FEEDBACK_REPLY";
                    key = "ID";
                    break;
                // 反馈附件表
                case 11:
                    tablename = "TR_FEEDBACK_ACCESSORY";
                    key = "ID";
                    break;
                default:
                    break;
            }

            messageResult.put("MESSAGE", message);
            messageResult.put("TABLENAME", tablename);

            if (sympMessageReal.getMessagetype().equals("1") || sympMessageReal.getMessagetype().equals("2") || sympMessageReal.getMessagetype().equals("3")) {
                messageResult.put("TYPE", sympMessageReal.getMessagetype());
            } else {
                throw new Exception();
            }

            if (operationType == 1 || operationType == 2 || operationType == 3) {
                messageResult.put("OPTE", operationType + "");
            } else {
                throw new Exception();
            }

            List<Map<String, Object>> messageContent = messageContents.get(String.valueOf(dataType));

            messageResult.put("KEY", key);
            messageResult.put("SIZE", messageContent.size());
            messageResult.put("DATA", messageContent);
            messageResultList.add(messageResult);


        }
        sympMessageReal.setContent(JsonHelper.Object2Json(messageResultList));

        List<SympMessageReal> beans = new ArrayList<SympMessageReal>();
        beans.add(sympMessageReal);
        try {
            insertListData(beans);
            Log.i("封装消息", "已封装完成。。。" + messageContents);
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            Constants.recordExceptionInfo(e, context, context.toString() + "/SympMessageRealDao");
            return 0;
        }
    }

    /**
     * 上传修改类型的文本消息
     * @param messageContents 需要保存的消息集合
     * @return 1成功，0失败
     * @throws Exception
     */
    public int updateTextMessages(List<List<Object>> messageContents){
        return addMessages(messageContents,"1","2");
    }

    /**
     * 上传新增类型的文本消息
     * @param messageContents 需要保存的消息集合
     * @return 1成功，0失败
     * @throws Exception
     */
    public int addTextMessages(List<List<Object>> messageContents){
        return addMessages(messageContents,"1","1");
    }

    public int deleteTextMessages(List<List<Object>> messageContents){
        return addMessages(messageContents,"1","3");
    }

    public int addMessages(List<List<Object>> messageContents, String type, String opt) {
        List<Map<String, Object>> messageResultList = new ArrayList<Map<String, Object>>();
        for(int j = 0; j < messageContents.size(); j++){
            List l = messageContents.get(j);
            List<Map<String, Object>> messageList = new ArrayList<Map<String, Object>>();
            String tableName = "";
            for(int i = 0; i < l.size(); i++) {
                Map<String, Object> tm = priseValue(l.get(i));
                tableName = tm.get("TABLENAME").toString().toUpperCase();
                //删除TABLENAME属性
                tm.remove("TABLENAME");
                messageList.add(tm);
            }
            Map<String, Object> messageResult = new HashMap<String, Object>();
            messageResult.put("MESSAGE", "");
            messageResult.put("TYPE", type);
            messageResult.put("OPTE", opt);
            messageResult.put("TABLENAME", tableName);
            messageResult.put("KEY", SQLHelper.getTableKey(tableName));
            messageResult.put("SIZE", messageList.size());
            messageResult.put("DATA", messageList);
            messageResultList.add(messageResult);
        }
        SympMessageReal sympMessageReal = Constants.getSympMessageReal(context, "1","");
        sympMessageReal.setContent(JsonHelper.Object2Json(messageResultList));
        List<SympMessageReal> beans = new ArrayList<SympMessageReal>();
        beans.add(sympMessageReal);
        try{
            insertListData(beans);
            Log.i("封装消息", "已封装完成。。。" + messageContents );
            return 1;
        }catch(Exception e){
            e.printStackTrace();
            Constants.recordExceptionInfo(e, context, context.toString() + "/SympMessageRealDao");
            return 0;
        }
    }

    /**
     * 将Bean对象拼装成Map对象
     * @param o 创建的Bean实体类
     * @return
     */
    public static Map<String, Object> priseValue(Object o){
        Map<String, Object> val = new HashMap<String, Object>();
        Field fields[] = o.getClass().getDeclaredFields();
        String[] name = new String[fields.length];
        Object[] value = new Object[fields.length];
        try{
            Field.setAccessible(fields, true);
            for(int i = 0;i < name.length; i++)   {
                name[i] = fields[i].getName();
                value[i] = fields[i].get(o);
                Log.i("封装消息","属性-" + name[i] + "，值-" + value[i]);
                //属性不为null才转化为map
                if(value[i] != null) val.put(name[i].toUpperCase(), value[i]);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return val;
    }


    /**
     *
     * @param sympMessageReal	消息实体，CONTENT不需要
     * @param dataType		数据类型
     * @param operationType		操作类型，1、新增、2、修改、3、删除
     * @param messageContent	消息内容，Map中key表示列名称，列名称大写，value表示列对应的值
     * @return		返回结果，1、新增成功  2.新增失败
     * @throws Exception
     */
    public int addMessage(SympMessageReal sympMessageReal, int dataType,
                          int operationType,List<Map<String, Object>> messageContent)
            throws Exception {
        List<Map<String, Object>> messageResultList = new ArrayList<Map<String, Object>>();
        Map<String, Object> messageResult = new HashMap<String, Object>();
        StringBuffer message = new StringBuffer("");
        String tablename = "";
        String key = "";
        switch (dataType) {
            // 项目
            case 1:
                tablename = "TR_PROJECT";
                key = "PROJECTID";
                break;
            // 任务
            case 2:
                tablename = "TR_TASK";
                key = "TASKID";
                break;
            // 用户
            case 3:
                tablename = "USER_INFO";
                key = "YHID";
                break;
            // 安全须知实体表
            case 4:
                tablename = "TR_SAFETYTIPS_OBJ";
                key = "ID";
                break;
            // 记录本次任务所携带的工具
            case 5:
                tablename = "TR_EQUIPMENT_TOOLS";
                key = "ID";
                break;
            // 普通图片表
            case 6:
                tablename = "TR_DETECTIONTEMPLET_FILE_OBJ";
                key = "ID";
                break;
            // 套管图片表
            case 7:
                tablename = "TR_SPECIAL_BUSHING_FILE";
                key = "ID";
                break;
            default:
                break;
        }



        messageResult.put("MESSAGE", message);
        messageResult.put("TABLENAME", tablename);

        if(sympMessageReal.getMessagetype().equals("1")||sympMessageReal.getMessagetype().equals("2")||sympMessageReal.getMessagetype().equals("3")){
            messageResult.put("TYPE", sympMessageReal.getMessagetype());
        }else{
            throw  new Exception();
        }

        if(operationType==1||operationType==2||operationType==3){
            messageResult.put("OPTE", operationType+"");
        }else{
            throw  new Exception();
        }

        messageResult.put("KEY", key);
        messageResult.put("SIZE", messageContent.size());
        messageResult.put("DATA", messageContent);

        messageResultList.add(messageResult);

        sympMessageReal.setContent(JsonHelper.Object2Json(messageResultList));

        List<SympMessageReal> beans = new ArrayList<SympMessageReal>();
        beans.add(sympMessageReal);
        try{
            insertListData(beans);
            return 1;
        }catch(Exception e){
            e.printStackTrace();
            Constants.recordExceptionInfo(e, context, context.toString() + "/SympMessageRealDao");
            return 0;
        }

    }

}
