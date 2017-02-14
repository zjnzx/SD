package com.energyfuture.symphony.m3.analysis;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;

import com.energyfuture.symphony.m3.common.JsonHelper;
import com.energyfuture.symphony.m3.domain.SympCommunicationTheme;
import com.energyfuture.symphony.m3.domain.TrProject;
import com.energyfuture.symphony.m3.util.Constants;
import com.energyfuture.symphony.m3.util.DBHelper;

import org.json.JSONException;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 数据解析程序
 * @designer
 * @author   上海艾飞能源科技有限公司
 * @version  1.0
 * @since    2015-4-7
 */
public class DataAnalysis {

    DBHelper dbHelper = null;
    Context context;
    public DataAnalysis(Context cxt)
    {
        this.context=cxt;
        dbHelper = new DBHelper(cxt);
    }

    //获取服务绝对路径
    private static final String pathTemp = Environment.getExternalStorageDirectory().getAbsolutePath();
    //取得图片绝对路径
    private static final String FILE_SAVEPATH = pathTemp.substring(0,pathTemp.length() - 1)
            + "legacy" + "/M3/transformer/picture/";

    /**
     * 将从平台获取到的数据解析成需要入库的对象
     * @designer
     * @since  2015-4-7
     * @author zhaogr
     * @param @param json 需要解析的json数据
     * @param @param messageList 需要返回进行提醒的消息信息
     * @return void
     */
    public LinkedHashMap<Object,Object> JsonDataAnalysis(String json, List<String> messageList){
        //标记解析入库结果
        boolean flag = false;
        LinkedHashMap<Object,Object> returnmap=new LinkedHashMap<>();

        if(json != null && !json.equals(""))
        {
            try {
                List<Map<Object, Object>> listData = JsonHelper.converMessageFormString(json);
                if (listData != null && listData.size() > 0) {
                    for (Map<Object, Object> map : listData) {
                        //获取到数据库名称
                        String tableName = map.get("TABLENAME").toString();
                        //获取消息提醒信息
                        if (map.get("MESSAGE") != null) {
                            String message = map.get("MESSAGE").toString();
                            messageList.add(message);
                        }
                        //获取数据库操作类型(1、增、2、修改、3、删除、)
                        int opte = Integer.parseInt(map.get("OPTE").toString());
                        //获取当前数据数量
                        int size = Integer.parseInt(map.get("SIZE").toString());
                        //获取主键名称，用于修改和删除操作
                        String keyName = map.get("KEY").toString();
                        //获取数据详情
                        List<Map<Object, Object>> datas = JsonHelper.converMessageFormString(map.get("DATA").toString());
                        if(datas != null && datas.size() > 0){
                            //如果是图片表数据，需要解析图片信息
                            if(tableName.equalsIgnoreCase("TR_DETECTIONTEMPLET_FILE_OBJ")){
                                //普通图片解析
                                taskImageDataAnalysis(datas);
                            }else if(tableName.equalsIgnoreCase("TR_SPECIAL_BUSHING_FILE")){
                                //套管类别图片
                                abnormalImageDataAnalysis(datas);
                            }

                            switch (opte) {
                                case 1:
                                    ArrayList<String> insertList = insertData(tableName, datas);
                                    if (insertList != null && insertList.size() > 0) {
                                        dbHelper.insertSQLAll(insertList);
                                        flag = true;
                                        if(tableName.equalsIgnoreCase("SYMP_COMMUNICATION_THEME")){
                                            for(Map<Object,Object> map1 : datas)
                                            {
                                                SympCommunicationTheme sympCommunicationTheme=new SympCommunicationTheme();
                                                sympCommunicationTheme.setId((String)map1.get("ID"));
                                                sympCommunicationTheme.setBelongId((String)map1.get("BELONGID"));
                                                sympCommunicationTheme.setPosition((String)map1.get("POSITION"));
                                                sympCommunicationTheme.setThemetitle((String)map1.get("THEMETITLE"));
                                                sympCommunicationTheme.setThemecontent((String)map1.get("THEMECONTENT"));
                                                sympCommunicationTheme.setCreatepersonid((String)map1.get("CREATEPERSONID"));
                                                sympCommunicationTheme.setCreatetime((String)map1.get("CREATETIME"));
                                                sympCommunicationTheme.setThemestate((String)map1.get("THEMESTATE"));
                                                sympCommunicationTheme.setParticipator((String)map1.get("PARTICIPATOR"));
                                                returnmap.put(sympCommunicationTheme,flag);
                                            }
                                        }else if(tableName.equalsIgnoreCase("SYMP_COMMUNICATION_REPLY")){
                                            for(Map<Object,Object> map1 : datas) {
                                                String sqlWhere = "";
                                                sqlWhere += " AND ID='" +map1.get("THEMEID") + "'";
                                                String sql = "SELECT * FROM SYMP_COMMUNICATION_THEME WHERE 1=1 " + sqlWhere + " ORDER BY CREATETIME DESC";
                                                List<Map<String, String>> dblist = dbHelper.selectSQL(sql, null);
                                                for (int i = 0; i < dblist.size(); i++) {
                                                    Map<String, String> m = dblist.get(i);
                                                    SympCommunicationTheme obj = new SympCommunicationTheme();
                                                    obj.setId(m.get("ID"));
                                                    obj.setBelongId(m.get("BELONGID"));
                                                    obj.setPosition(m.get("POSITION"));
                                                    obj.setThemetitle(m.get("THEMETITLE"));
                                                    obj.setThemecontent(m.get("THEMECONTENT"));
                                                    obj.setCreatepersonid(m.get("CREATEPERSONID"));
                                                    obj.setCreatetime(m.get("CREATETIME"));
                                                    obj.setThemestate(m.get("THEMESTATE"));
                                                    obj.setParticipator(m.get("PARTICIPATOR"));
                                                    returnmap.put(obj,flag);
                                                }
                                            }
                                        } else if(tableName.equalsIgnoreCase("TR_PROJECT")) {
                                            for (Map<Object, Object> map1 : datas) {
                                                TrProject trProject = new TrProject();
                                                trProject.setProjectid((String)map1.get("PROJECTID"));
                                                trProject.setProjectname((String)map1.get("PROJECTNAME"));
                                                trProject.setProjectstate((String)map1.get("PROJECTSTATE"));
//                                                trProject.setGrade((String)map1.get("GRADE"));
                                                trProject.setStationname((String)map1.get("STATIONNAME"));
                                                trProject.setStationid((String)map1.get("STATIONID"));
                                                trProject.setStarttime((String)map1.get("STARTTIME"));
                                                trProject.setEndtime((String)map1.get("ENDTIME"));
                                                trProject.setActualstarttime((String)map1.get("ACTUALSTARTTIME"));
                                                trProject.setActualsendtime((String)map1.get("ACTUALSENDTIME"));
                                                trProject.setCreatetime((String)map1.get("CREATETIME"));
                                                trProject.setUpdatetime((String)map1.get("UPDATETIME"));
                                                trProject.setAware((String)map1.get("AWARE"));
                                                trProject.setZpperson((String)map1.get("ZPPERSON"));
                                                trProject.setCreateperson((String)map1.get("CREATEPERSON"));
                                                trProject.setZrpearson((String)map1.get("ZRPEARSON"));
                                                trProject.setReasondescribe((String)map1.get("REASONDESCRIBE"));
                                                trProject.setSafetynotice((String)map1.get("SAFETYNOTICE"));
                                                trProject.setTaskcount((String)map1.get("TASKCOUNT"));
                                                trProject.setFinishcount((String)map1.get("FINISHCOUNT"));
//                                                trProject.setInstrument((String)map1.get("INSTRUMENT"));
                                                trProject.setWorkperson((String)map1.get("WORKPERSON"));
                                                returnmap.put("trProject", flag);
                                            }
                                        }
                                        else{
                                            returnmap.put("",flag);
                                        }
                                    }
                                    break;
                                case 2:
                                    ArrayList<String> updateList = updateData(tableName, datas, keyName);
                                    if (updateList != null && updateList.size() > 0) {
                                        try {
                                            dbHelper.insertSQLAll(updateList);
                                        }catch (Exception e){
                                            e.printStackTrace();
                                            Constants.recordExceptionInfo(e, context, context.toString() + "/DataAnalysis");
                                        }
                                        flag = true;
                                        returnmap.put("",flag);

                                    }
                                    break;
                                case 3:
                                    ArrayList<String> deleteList = deleteData(tableName, datas, keyName);
                                    if (deleteList != null && deleteList.size() > 0) {
                                        dbHelper.insertSQLAll(deleteList);
                                        flag = true;
                                    }
                                    break;
                                default:

                                    break;
                            }
                        }
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
                Constants.recordExceptionInfo(e, context, context.toString() + "/DataAnalysis");
            }
        }else{
            System.out.print("解析数据为空！");
        }
        return returnmap;
    }


    /**
     * 新增sql语句
     * @designer
     * @since  2015-4-7
     * @author zhaogr
     * @param @param tanmeName 表名称
     * @param @param datas 字段对象集合
     * @param @return sql集合
     * @return ArrayList<String>
     */
    private static ArrayList<String> insertData(String tableName,List<Map<Object,Object>> datas)
    {
        ArrayList<String> array = new ArrayList<String>();
        if(datas != null && datas.size() > 0)
        {
            for(Map<Object,Object> map : datas)
            {
                StringBuffer values = new StringBuffer();
                values.append("VALUES (");
                StringBuffer insert = new StringBuffer();
                insert.append("REPLACE INTO "+tableName+ "(");
                Set<Object> keyes = map.keySet();
                int count = 1;
                for(Object key : keyes){
                    if(map.get(key) != null && !map.get(key).toString().equals("")){
                        if(count == 1)
                        {
                            insert.append(key);
                            values.append("'"+map.get(key)+"'");
                        }else
                        {
                            insert.append(","+key);
                            values.append(",'"+map.get(key)+"'");
                        }
                        count ++;
                    }
                }
                insert.append(")");
                values.append(")");
                array.add(insert.toString()+" "+values.toString());
            }
        }
        return  array;
    }

    /**
     * 修改sql语句
     * @designer
     * @since  2015-4-7
     * @author zhaogr
     * @param @param tanmeName 表名称
     * @param @param datas 字段对象集合
     * @param @param keyName 主键值
     * @param @return sql集合
     * @return ArrayList<String>
     */
    private static ArrayList<String> updateData(String tableName,List<Map<Object,Object>> datas, String keyName)
    {
        ArrayList<String> array = new ArrayList<String>();
        if(datas != null && datas.size() > 0)
        {
            for(Map<Object,Object> map : datas)
            {
                StringBuffer values = new StringBuffer();
                values.append(" WHERE ");
                StringBuffer insert = new StringBuffer();
                insert.append("UPDATE "+tableName+ " SET ");
                int count = 1;
                //先把where条件封装进去
                values.append(keyName+"='"+map.get(keyName)+"'");
                //将key从map中先移除掉
                map.remove(keyName);
                Set<Object> keyes = map.keySet();
                for(Object key : keyes){
                    if(map.get(key) != null && !map.get(key).toString().equals("")){
                        if(count == 1){
                            insert.append(key+"='"+map.get(key)+"'");
                        }else{
                            insert.append(","+key+"='"+map.get(key)+"'");
                        }
                        count ++;
                    }
                }
                array.add(insert.toString()+" "+values.toString());
            }
        }
        return  array;
    }

    /**
     * 删除sql语句
     * @designer
     * @since  2015-4-7
     * @author zhaogr
     * @param @param tanmeName 表名称
     * @param @param datas 字段对象集合
     * @param @param keyName 主键值
     * @param @return sql集合
     * @return ArrayList<String>
     */
    private ArrayList<String> deleteData(String tableName,List<Map<Object,Object>> datas, String keyName)
    {
        ArrayList<String> array = new ArrayList<String>();
        if(datas != null && datas.size() > 0)
        {
            for(Map<Object,Object> map : datas)
            {
                StringBuffer insert = new StringBuffer();
                insert.append("DELETE FROM "+tableName);
                insert.append(" WHERE "+keyName+"='"+map.get(keyName)+"'");
                array.add(insert.toString());
                break;
            }
        }
        return  array;
    }


    /**
     * 监测部位图片解析
     * @designer
     * @since  2015-4-15
     * @author zhaogr
     * @param @param datas 图片信息表
     * @param @return  是否解析成功
     * @return boolean
     */
    private static void taskImageDataAnalysis(List<Map<Object,Object>> datas){
        int count = 0;
        if(datas != null && datas.size() > 0){
            for(Map<Object,Object> map : datas){
                if(map.get("FILENAME") != null && !map.get("FILENAME").toString().equals("")){
//                    final String url  = URLs.HTTP+URLs.HOSTA+ "/SYMP/IMG/"+map.get("PROJECTID") +"/"+map.get("TASKID")+"/"+map.get("IMAGENAME").toString();
//                    final String imageName = map.get("IMAGENAME").toString();
                    datas.get(count).remove("PROJECTID");
                    datas.get(count).remove("TASKID");
                }
                count ++;
            }
        }
    }

    /**
     * 异常列表图片解析
     * @designer
     * @since  2015-4-15
     * @author zhaogr
     * @param @param datas
     * @return void
     */
    private static void abnormalImageDataAnalysis(List<Map<Object,Object>> datas){
        int count = 0;
        if(datas != null && datas.size() > 0){
            for(Map<Object,Object> map : datas){
                if(map.get("FILENAME") != null && !map.get("FILENAME").toString().equals("")){
//                    final String url  = URLs.HTTP+URLs.HOSTA+ "/SYMP/ABNORMAL/"+map.get("PROJECTID") +"/"+map.get("TASKID")+"/"+map.get("IMAGENAME").toString();
//                    final String imageName = map.get("IMAGENAME").toString();
                    datas.get(count).remove("PROJECTID");
                    datas.get(count).remove("TASKID");
                }
                count ++;
            }
        }
    }

    /**
     * Get image from newwork
     * @param path The path of image
     * @return byte[]
     * @throws Exception
     */
    public byte[] getImage(String path) throws Exception{
        URL url = new URL(path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(5 * 1000);
        conn.setRequestMethod("GET");
        InputStream inStream = conn.getInputStream();
        if(conn.getResponseCode() == HttpURLConnection.HTTP_OK){
            return readStream(inStream);
        }
        return null;
    }

    /**
     * Get image from newwork
     * @param path The path of image
     * @return InputStream
     * @throws Exception
     */
    public static InputStream getImageStream(String path) throws Exception{
        URL url = new URL(path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(5 * 1000);
        conn.setRequestMethod("GET");
        if(conn.getResponseCode() == HttpURLConnection.HTTP_OK){
            return conn.getInputStream();
        }
        return null;
    }
    /**
     * Get data from stream
     * @param inStream
     * @return byte[]
     * @throws Exception
     */
    public static byte[] readStream(InputStream inStream) throws Exception{
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while( (len=inStream.read(buffer)) != -1){
            outStream.write(buffer, 0, len);
        }
        outStream.close();
        inStream.close();
        return outStream.toByteArray();
    }

    /**
     * 保存文件
     * @param bm
     * @param fileName
     * @throws java.io.IOException
     */
    public static void saveFile(Bitmap bm, String fileName) throws IOException {
        File dirFile = new File(FILE_SAVEPATH);
        if(!dirFile.exists()){
            dirFile.mkdir();
        }
        File myCaptureFile = new File(FILE_SAVEPATH +"/"+ fileName);
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
        bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);
        bos.flush();
        bos.close();
    }


}
