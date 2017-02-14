package com.energyfuture.symphony.m3.common;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;
import com.energyfuture.symphony.m3.dao.SympMessageRealDao;
import com.energyfuture.symphony.m3.dao.TrDetectionTempletFileObjDao;
import com.energyfuture.symphony.m3.dao.TrSpecialBushingFileDao;
import com.energyfuture.symphony.m3.domain.SympMessageReal;
import com.energyfuture.symphony.m3.domain.TrDetectiontempletFileObj;
import com.energyfuture.symphony.m3.domain.TrSpecialBushingFile;
import com.energyfuture.symphony.m3.domain.TrTask;
import com.energyfuture.symphony.m3.util.Constants;
import com.energyfuture.symphony.m3.wifi.util.WifiAdmin2;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 通过wifi获取sd卡的图片信息
 *  (http://my.oschina.net/liux)
 * @version 1.0
 * @created 2012-3-21
 */
public class GetPicFromWifiSDScard implements Serializable{

    public Handler mHandler = null;
    public  boolean isgetPicFromSdCarding=false;
    public boolean isgetpicfromsd=true;
    // WIFI管理者
    public WifiAdmin2 wifiAdmin2;
    public static WifiManager wifiManager;
    public Handler wifihandler;
    public TrTask trTask;
    public String wifiName;
    public  boolean  wifiContected=false;
    private Handler inioViewHandler;
    //获取sdk文件的路径和文件的前缀
    String sd_route;
    String file_prefix;
    String pic_route;
    String file_postfix;
    Context con=null;
    String userId;
    TrSpecialBushingFileDao trSpecialBushingFileDao;
    TrDetectionTempletFileObjDao trDetectionTempletFileObjDao;
    SympMessageRealDao sympMessageRealDao;
    public GetPicFromWifiSDScard(Context con, String sd_route, String file_prefix,String file_postfix, String pic_route,TrTask trTask) {
        super();
        wifiAdmin2 = new WifiAdmin2(con);
		/* 以getSystemService取得WIFI_SERVICE */
        wifiManager = (WifiManager) con.getSystemService(Context.WIFI_SERVICE);
        this.con=con;
        this.sd_route  =sd_route;
        this.file_prefix  = file_prefix;
        this.file_postfix  = file_postfix;
        this.pic_route=pic_route;
        this.trTask = trTask;
        trSpecialBushingFileDao = new TrSpecialBushingFileDao(con);
        trDetectionTempletFileObjDao = new TrDetectionTempletFileObjDao(con);
        sympMessageRealDao = new SympMessageRealDao(con);
        userId = Constants.getLoginPerson(con).get("userId");
    }

    public void getImage(List<String> imagelist){
        new Mythread(imagelist).start();
    }

    public class Mythread extends Thread{
        List<String> imagelist=new ArrayList<String>();

        public Mythread(List<String> imagelist) {
            this.imagelist = imagelist;
        }

        @Override
        public void run() {
            // 参数
            HttpParams httpParameters = new BasicHttpParams();
            // 设置连接超时
            HttpConnectionParams.setConnectionTimeout(httpParameters, 10000);
            // 设置socket超时
            HttpConnectionParams.setSoTimeout(httpParameters, 10000);
            // 使用网络连接类HttpClient类对网络数据的提取
            HttpClient httpClient = new DefaultHttpClient(httpParameters);
            String[] imgType = {".jpg", ".png",".jpeg"};
            for(int i=0;i<imagelist.size();i++){
                String data=imagelist.get(i);
                String fileNumber="";
                String fileId="";
                String type="";
                if(data!=null&&!data.equals("")){
                    if(data.contains("|")){
                        int first=data.indexOf("|");
                        int second=data.indexOf("|",first+1);
                        fileNumber=data.substring(0,first);
                        fileId=data.substring(first+1,second);
                        type=data.substring(second+1);
                    }
                }
                a:for(int img=0;img<imgType.length;img++){
                    if (file_prefix.contains(";")) { //前缀
                        String prefixArray[] = file_prefix.split(";");
                        for(int a=0;a<prefixArray.length;a++){
                            if(file_postfix.contains(";")) { //后缀
                                String postfixArray[] = file_postfix.split(";");
                                for(int b = 0;b < postfixArray.length;b++) {
                                    try{
                                        String url = sd_route + prefixArray[a] + fileNumber +postfixArray[b]+ imgType[img];
                                        url = url.replaceAll("\\s*", "");
                                        HttpGet httpGet = new HttpGet(url);
                                        Bitmap bitmap = null;
                                        HttpResponse httpResponse = httpClient.execute(httpGet);
                                        if (httpResponse.getStatusLine().getStatusCode() == 200) {
                                            HttpEntity httpEntity = httpResponse.getEntity();
                                            byte[] imagedata = EntityUtils.toByteArray(httpEntity);
                                            bitmap = BitmapFactory.decodeByteArray(imagedata, 0, imagedata.length);
                                            if(bitmap!=null){
                                                if (new File(pic_route).exists()) {
                                                    String fileName=prefixArray[a] + fileNumber + imgType[img];
                                                    Base.saveMyBitmap(fileName, bitmap, pic_route + "small/");
                                                    Base.saveMyBitmap(fileName, bitmap, pic_route + "original/");

                                                    if(type.equals("general")){ //保存普通图片
                                                        updateGeneralFile(fileName,fileNumber,trTask,fileId);
                                                    }else if(type.equals("bushing")){
                                                        updateBushingFile(fileName,fileNumber,trTask,fileId);
                                                    }
                                                }
                                                break a;
                                            }
                                        }
                                    }catch (Exception e){
                                        e.printStackTrace();
                                        Constants.recordExceptionInfo(e, con, con.toString()+"/GetPicFromWifiSDScard");
                                    }
                                }
                              }else{
                                try{
                                    String url = sd_route + prefixArray[a] + fileNumber +file_postfix+ imgType[img];
                                    url = url.replaceAll("\\s*", "");
                                    HttpGet httpGet = new HttpGet(url);
                                    Bitmap bitmap = null;
                                    HttpResponse httpResponse = httpClient.execute(httpGet);
                                    if (httpResponse.getStatusLine().getStatusCode() == 200) {
                                        HttpEntity httpEntity = httpResponse.getEntity();
                                        byte[] imagedata = EntityUtils.toByteArray(httpEntity);
                                        bitmap = BitmapFactory.decodeByteArray(imagedata, 0, imagedata.length);
                                        if(bitmap!=null){
                                            if (new File(pic_route).exists()) {
                                                String fileName=prefixArray[a] + fileNumber + imgType[img];
                                                Base.saveMyBitmap(fileName, bitmap, pic_route + "small/");
                                                Base.saveMyBitmap(fileName, bitmap, pic_route + "original/");

                                                if(type.equals("general")){ //保存普通图片
                                                    updateGeneralFile(fileName,fileNumber,trTask,fileId);
                                                }else if(type.equals("bushing")){
                                                    updateBushingFile(fileName,fileNumber,trTask,fileId);
                                                }
                                            }
                                            break a;
                                        }
                                    }
                                }catch (Exception e){
                                    e.printStackTrace();
                                    Constants.recordExceptionInfo(e, con, con.toString()+"/GetPicFromWifiSDScard");
                                }
                              }
                           }
                        }else{
                            if(file_postfix.contains(";")) { //后缀
                                String postfixArray[] = file_postfix.split(";");
                                for(int b = 0;b < postfixArray.length;b++) {
                                    try{
                                        String url = sd_route + file_prefix + fileNumber +postfixArray[b]+ imgType[img];
                                        url = url.replaceAll("\\s*", "");
                                        HttpGet httpGet = new HttpGet(url);
                                        Bitmap bitmap = null;
                                        HttpResponse httpResponse = httpClient.execute(httpGet);
                                        if (httpResponse.getStatusLine().getStatusCode() == 200) {
                                            HttpEntity httpEntity = httpResponse.getEntity();
                                            byte[] imagedata = EntityUtils.toByteArray(httpEntity);
                                            bitmap = BitmapFactory.decodeByteArray(imagedata, 0, imagedata.length);
                                            if(bitmap!=null){
                                                if (new File(pic_route).exists()) {
                                                    String fileName=file_prefix + fileNumber + imgType[img];
                                                    Base.saveMyBitmap(fileName, bitmap, pic_route + "small/");
                                                    Base.saveMyBitmap(fileName, bitmap, pic_route + "original/");

                                                    if(type.equals("general")){ //保存普通图片
                                                        updateGeneralFile(fileName,fileNumber,trTask,fileId);
                                                    }else if(type.equals("bushing")){
                                                        updateBushingFile(fileName,fileNumber,trTask,fileId);
                                                    }
                                                }
                                                break a;
                                            }
                                        }
                                    }catch (Exception e){
                                        e.printStackTrace();
                                        Constants.recordExceptionInfo(e, con, con.toString()+"/GetPicFromWifiSDScard");
                                    }
                                }
                            }else{
                                try{
                                    String url = sd_route + file_prefix + fileNumber +file_postfix+ imgType[img];
                                    url = url.replaceAll("\\s*", "");
                                    HttpGet httpGet = new HttpGet(url);
                                    Bitmap bitmap = null;
                                    HttpResponse httpResponse = httpClient.execute(httpGet);
                                    if (httpResponse.getStatusLine().getStatusCode() == 200) {
                                        HttpEntity httpEntity = httpResponse.getEntity();
                                        byte[] imagedata = EntityUtils.toByteArray(httpEntity);
                                        bitmap = BitmapFactory.decodeByteArray(imagedata, 0, imagedata.length);
                                        if(bitmap!=null){
                                            if (new File(pic_route).exists()) {
                                                String fileName=file_prefix + fileNumber + imgType[img];
                                                Base.saveMyBitmap(fileName, bitmap, pic_route + "small/");
                                                Base.saveMyBitmap(fileName, bitmap, pic_route + "original/");

                                                if(type.equals("general")){ //保存普通图片
                                                    updateGeneralFile(fileName,fileNumber,trTask,fileId);
                                                }else if(type.equals("bushing")){
                                                    updateBushingFile(fileName,fileNumber,trTask,fileId);
                                                }
                                            }
                                            break a;
                                        }
                                    }
                                }catch (Exception e){
                                    e.printStackTrace();
                                    Constants.recordExceptionInfo(e, con, con.toString()+"/GetPicFromWifiSDScard");
                                }
                            }
                        }
                    }
                if(i==imagelist.size()-1){
                    Constants.getImageHandler.sendEmptyMessage(1); //图片获取完成
                  }
               }
            }
        }
    /**
     * 修改图片信息和发送消息(普通图片)
     */
    private void updateGeneralFile(String fileName,String fileNumber,TrTask trTask,String id){
        String date = Constants.dateformat2.format(new Date());
        Map<Object, Object> columnsMapImage = new HashMap<Object, Object>();
        columnsMapImage.put("FILENAME", fileName);
        columnsMapImage.put("FILENUMBER", fileNumber);
        columnsMapImage.put("ISUPLOAD", "0");
        columnsMapImage.put("UPDATETIME", date);
        columnsMapImage.put("UPDATEPERSON", userId);
        columnsMapImage.put("TASKID", trTask.getTaskid());
        columnsMapImage.put("PROJECTID", trTask.getProjectid());
        Map<Object, Object> wheresMapImage = new HashMap<Object, Object>();
        wheresMapImage.put("ID", id);
        trDetectionTempletFileObjDao.updateTrDetectionTempletFileObjInfo(columnsMapImage, wheresMapImage);

        //发送消息
        TrDetectiontempletFileObj trDetectiontempletFileObj = new TrDetectiontempletFileObj();
        trDetectiontempletFileObj.setFilename(fileName);
        trDetectiontempletFileObj.setFilenumber(fileNumber);
        trDetectiontempletFileObj.setUpdatetime(date);
        trDetectiontempletFileObj.setUpdateperson(userId);
        trDetectiontempletFileObj.setTaskid(trTask.getTaskid());
        trDetectiontempletFileObj.setProjectid(trTask.getProjectid());
        trDetectiontempletFileObj.setIsupload("0");
        trDetectiontempletFileObj.setId(id);
        List<List<Object>> list1 = new ArrayList<List<Object>>();
        List<Object> list2 = new ArrayList<Object>();
        list2.add(trDetectiontempletFileObj);
        list1.add(list2);
        sympMessageRealDao.updateTextMessages(list1);
        List<SympMessageReal> sympMessageReals = new ArrayList<SympMessageReal>();
        SympMessageReal sympMessageReal = new SympMessageReal();
        sympMessageReal.setPresonid(userId);
        sympMessageReal.setContent(trDetectiontempletFileObj.TABLENAME + "," + id);
        sympMessageReal.setMessagestate("1");
        sympMessageReal.setMessagetype("2");
        sympMessageReal.setCreatedate(date);
        sympMessageReals.add(sympMessageReal);
        sympMessageRealDao.insertListData(sympMessageReals);

        if(fileName!=null&&!fileName.equals("")){
            Constants.successcount++;
            Constants.getImageHandler.sendEmptyMessage(2); //更新获取成功的数量
            Constants.datamap.put(fileNumber,fileName);
        }
    }
    /**
     * 修改图片信息和发送消息(套管图片)
     */
    private void updateBushingFile(String fileName,String fileNumber,TrTask trTask,String id){
        String date = Constants.dateformat2.format(new Date());
        Map<Object, Object> columnsMapImage = new HashMap<Object, Object>();
        columnsMapImage.put("FILENAME", fileName);
        columnsMapImage.put("FILENUMBER", fileNumber);
        columnsMapImage.put("ISUPLOAD", "0");
        columnsMapImage.put("UPDATETIME", date);
        columnsMapImage.put("UPDATEPERSON", userId);
        columnsMapImage.put("TASKID", trTask.getTaskid());
        columnsMapImage.put("PROJECTID", trTask.getProjectid());
        Map<Object, Object> wheresMapImage = new HashMap<Object, Object>();
        wheresMapImage.put("ID", id);
        trSpecialBushingFileDao.updateTrSpecialBushingFileInfo(columnsMapImage, wheresMapImage);

        //发送消息
        TrSpecialBushingFile trSpecialBushingFile = new TrSpecialBushingFile();
        trSpecialBushingFile.setFilename(fileName);
        trSpecialBushingFile.setFilenumber(fileNumber);
        trSpecialBushingFile.setUpdatetime(date);
        trSpecialBushingFile.setUpdateperson(userId);
        trSpecialBushingFile.setTaskid(trTask.getTaskid());
        trSpecialBushingFile.setProjectid(trTask.getProjectid());
        trSpecialBushingFile.setIsupload("0");
        trSpecialBushingFile.setId(id);
        List<List<Object>> list1 = new ArrayList<List<Object>>();
        List<Object> list2 = new ArrayList<Object>();
        list2.add(trSpecialBushingFile);
        list1.add(list2);
        sympMessageRealDao.updateTextMessages(list1);
        List<SympMessageReal> sympMessageReals = new ArrayList<SympMessageReal>();
        SympMessageReal sympMessageReal = new SympMessageReal();
        sympMessageReal.setPresonid(userId);
        sympMessageReal.setContent(trSpecialBushingFile.TABLENAME + "," + id);
        sympMessageReal.setMessagestate("1");
        sympMessageReal.setMessagetype("2");
        sympMessageReal.setCreatedate(date);
        sympMessageReals.add(sympMessageReal);
        sympMessageRealDao.insertListData(sympMessageReals);

        if(fileName!=null&&!fileName.equals("")){
            Constants.successcount++;
            Constants.getImageHandler.sendEmptyMessage(2); //更新获取成功的数量
            Constants.datamap.put(fileNumber,fileName);
        }
    }

    /**
     * 判断WIFI是否连接成功
     *
     * @param context
     * @return
     */
    public static boolean isWifiContected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (info != null && info.isConnected()) {
            Log.i("sjf", "Wifi网络连接成功");
            return true;
        }
        Log.i("sjf", "Wifi网络连接失败");
        return false;
    }


    /***********wifi切换start********/

    public class WifiThread extends Thread {
        String wifiName;
        String wifiPass;

        public WifiThread(String wifiName,String wifiPass) {
            super();
            this.wifiName = wifiName;
            this.wifiPass = wifiPass;

        }
        private boolean flag = true;
        public  void stopThread(boolean flag) {
            this.flag = !flag;
        }
        public  void run() {
            try {
//            Looper.prepare();
//            /** 1.进度等待对话框 **/
//            pd = new ProgressDialog(UltrasonicTaskStepActivity.this);
//            /** 2.进度等待对话框 **/
//            pd.setTitle("提醒");
//            /** 3.进度等待对话框 **/
//            pd.setMessage("正在上传数据...请稍等。");
//            /** 4.进度等待对话框 **/
//            pd.show();
//            pd.setCanceledOnTouchOutside(false);

                wifiManager.disconnect();
                wifiAdmin2.OpenWiFi(con);

                // 循环判断WIFI状态，直至已连接好后连接WIFI
                while (true) {
                    int state = wifiManager.getWifiState();
                    // WIFI网卡可用
                    if(WifiManager.WIFI_STATE_ENABLED == state)
                        break;
                    //等待完全开启
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                        Constants.recordExceptionInfo(e, con, con.toString()+"/GetPicFromWifiSDScard");
                    }
                }
                wifiContected = wifiAdmin2.addNetwork(wifiAdmin2.createWifiInfo(wifiName, wifiPass, 3));

                while (true) {
                    if (isWifiContected(con)) {
                        break;
                    }
                    //等待完全开启
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                        Constants.recordExceptionInfo(e, con, con.toString()+"/GetPicFromWifiSDScard");
                    }

                }
                if(wifiContected) {
                    Message msg = new Message();
                    msg.what = 0;
                    wifihandler.sendMessage(msg);
                }
                else {
                    Message msg = new Message();
                    msg.what = 1;
                    wifihandler.sendMessage(msg);
                }


            } catch (Exception e) {
                // TODO Auto-generated catch block
                Log.e("WifiThread","连接wifi失败！");
                Message msg = new Message();
                msg.what = 1;
                wifihandler.sendMessage(msg);
                e.printStackTrace();
                Constants.recordExceptionInfo(e, con, con.toString()+"/GetPicFromWifiSDScard");
            }
        }
    }
    /***********wifi切换end********/


    /**
     * 判断WIFI是否连接成功
     *
     * @param context
     * @return
     */
    public static boolean isTheWifiContected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (info != null &&info.getExtraInfo()!=null&&(info.getExtraInfo().contains("flashair")||info.getExtraInfo().contains("efwifisd"))&& info.isConnected()) {
            Log.i("sjf", "Wifi网络连接成功");
            return true;
        }
        Log.i("sjf", "Wifi网络连接失败");
        return false;
    }
}