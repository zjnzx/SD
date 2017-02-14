package com.energyfuture.symphony.m3.download;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Binder;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import com.energyfuture.symphony.m3.activity.CommToolsActivity;
import com.energyfuture.symphony.m3.activity.R;
import com.energyfuture.symphony.m3.common.URLs;
import com.energyfuture.symphony.m3.domain.TrCommonTools;
import com.energyfuture.symphony.m3.util.Constants;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Author: zhaoxun  下载apk的服务
 */
public class DownloadApkService extends Service {
    private Context context=DownloadApkService.this;
    //标题
    private String titleId = "";
    private String apkName = "";
    //文件存储
    private File updateDir = null;
    private File updateFile = null;
    //通知栏
    private NotificationManager updateNotificationManager = null;
    private Notification updateNotification = null;
    //通知栏跳转Intent
    private Intent updateIntent = null;
    private PendingIntent updatePendingIntent = null;
    //下载状态
    private final static int DOWNLOAD_COMPLETE = 0;
    private final static int DOWNLOAD_FAIL = 1;
    //文件路径
    private String url = "";
     //进度条的进度值
    private int progress = 0;
    //更新进度的回调接口
    private OnProgressListener onProgressListener;

    public int getProgress() {
        return progress;
    }
    public interface OnProgressListener {
        void onProgress(int progress);
    }

    /**
     * 注册回调接口的方法，供外部调用
     * @param onProgressListener
     */
    public void setOnProgressListener(OnProgressListener onProgressListener) {
        this.onProgressListener = onProgressListener;
    }

    public class MsgBinder extends Binder {
        /**
         * 获取当前Service的实例
         * @return
         */
        public DownloadApkService getService(){
            return DownloadApkService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new MsgBinder();
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //获取传值
        TrCommonTools trCommonTools = (TrCommonTools) intent.getSerializableExtra("trCommonTools");
        apkName = trCommonTools.getFilename();
        url = URLs.HTTP + URLs.HOST + "/INSP/" + trCommonTools.getFileurl() + URLEncoder.encode(trCommonTools.getFilename());
        //创建文件
        if(android.os.Environment.MEDIA_MOUNTED.equals(android.os.Environment.getExternalStorageState())){
            String pathTemp = Environment.getExternalStorageDirectory().getAbsolutePath();
            String pathTemp2 = pathTemp.substring(0, pathTemp.length() - 1) + "legacy" + "/temp/";
            Log.i("*****************","*****************"+url);
            updateDir = new File(pathTemp2);
            updateFile = new File(updateDir.getPath(),apkName);
            Log.i("================","======="+updateFile.getAbsolutePath());
        }

        this.updateNotificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        this.updateNotification = new Notification();

        //设置下载过程中，点击通知栏，回到主界面
        updateIntent = new Intent(this, CommToolsActivity.class);
        updatePendingIntent = PendingIntent.getActivity(this,0,updateIntent,0);
        //设置通知栏显示内容
        updateNotification.icon = R.drawable.ic_launcher;
        updateNotification.tickerText = "开始下载";
        updateNotification.setLatestEventInfo(this,apkName,"0%",updatePendingIntent);
        //发出通知
        updateNotificationManager.notify(0,updateNotification);

        //开启一个新的线程下载，如果使用Service同步下载，会导致ANR问题，Service本身也会阻塞
        new Thread(new updateRunnable()).start();//这个是下载的重点，是下载的过程
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    class updateRunnable implements Runnable { 
        Message message = updateHandler.obtainMessage();
        public void run() {
            message.what = DOWNLOAD_COMPLETE;
            try{
                if(!updateDir.exists()){
                    updateDir.mkdirs();
                }
                if(!updateFile.exists()){
                    updateFile.createNewFile();
                }
                //下载函数，
                long downloadSize = downloadUpdateFile(url,updateFile);
                if(downloadSize>0){
                    //下载成功
                    updateHandler.sendMessage(message);
                }
            }catch(Exception ex){
                ex.printStackTrace();
                message.what = DOWNLOAD_FAIL;
                //下载失败
                updateHandler.sendMessage(message);
                Constants.recordExceptionInfo(ex, context, context.toString());
            }
        }

        public long downloadUpdateFile(String downloadUrl, File saveFile) throws Exception {
            //这样的下载代码很多，我就不做过多的说明
            int downloadCount = 0;
            int currentSize = 0;
            long totalSize = 0;
            int updateTotalSize = 0;

            HttpURLConnection httpConnection = null;
            InputStream is = null;
            FileOutputStream fos = null;

            try {
                URL url = new URL(downloadUrl);
                httpConnection = (HttpURLConnection)url.openConnection();
                httpConnection.setRequestProperty("User-Agent", "PacificHttpClient");
                if(currentSize > 0) {
                    httpConnection.setRequestProperty("RANGE", "bytes=" + currentSize + "-");
                }
                httpConnection.setConnectTimeout(60000);
                httpConnection.setReadTimeout(90000);
                updateTotalSize = httpConnection.getContentLength();
                if (httpConnection.getResponseCode() == 404) {
                    Log.i("================","fail");
                    throw new Exception("fail!");
                }
                is = httpConnection.getInputStream();
                fos = new FileOutputStream(saveFile, false);
                byte buffer[] = new byte[4096];
                int readsize = 0;
                while((readsize = is.read(buffer)) != -1){
                    fos.write(buffer, 0, readsize);
                    totalSize += readsize;
                    progress = (int)(totalSize*100/updateTotalSize);
                    //为了防止频繁的通知导致应用吃紧，百分比增加10才通知一次
                    if((downloadCount == 0)||progress>=downloadCount){
                        downloadCount += 1;
                        updateNotification.setLatestEventInfo(DownloadApkService.this, "正在下载", progress+"%", updatePendingIntent);
                        updateNotificationManager.notify(0, updateNotification);
//                        progress = (int)(totalSize*100/updateTotalSize);
                        //进度发生变化通知调用方
                        if(onProgressListener != null){
                            onProgressListener.onProgress(progress);
                            Log.i(updateTotalSize+"=totalSize===========updateTotalSize="+totalSize,"==progress="+progress);
                        }
                    }
                }
            }catch (Exception e) {
                e.printStackTrace();
                Constants.recordExceptionInfo(e, context, context.toString());
            }finally
             {
                if(httpConnection != null) {
                    httpConnection.disconnect();
                }
                if(is != null) {
                    is.close();
                }
                if(fos != null) {
                    fos.close();
                }
            }
            return totalSize;
        }


    }

    private Handler updateHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what){
                case DOWNLOAD_COMPLETE:
                    //点击安装PendingIntent
                    Uri uri = Uri.fromFile(updateFile);
                    Intent installIntent = new Intent(Intent.ACTION_VIEW);
                    installIntent.setDataAndType(uri, "application/vnd.android.package-archive");
                    updatePendingIntent = PendingIntent.getActivity(DownloadApkService.this, 0, installIntent, 0);

                    updateNotification.defaults = Notification.DEFAULT_SOUND;//铃声提醒 
                    updateNotification.setLatestEventInfo(DownloadApkService.this, apkName, "下载完成,点击安装。", updatePendingIntent);
                    updateNotificationManager.notify(0, updateNotification);

                    //停止服务
                    stopService(updateIntent);
                case DOWNLOAD_FAIL:
                    //下载失败
                    Log.i("=============","DOWNLOAD_FAIL");
                    Constants.isDownLoad = true;
                    updateNotification.setLatestEventInfo(DownloadApkService.this, apkName, "下载完成,点击安装。", updatePendingIntent);
                    updateNotificationManager.notify(0, updateNotification);
                default:
                    stopService(updateIntent);
            }
        }
    };
}
