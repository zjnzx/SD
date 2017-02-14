package com.energyfuture.symphony.m3.activity;

import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import com.energyfuture.symphony.m3.adapter.CommonToolsAdapter;
import com.energyfuture.symphony.m3.common.ApiClient;
import com.energyfuture.symphony.m3.common.URLs;
import com.energyfuture.symphony.m3.dao.TrCommonToolsDao;
import com.energyfuture.symphony.m3.domain.TrCommonTools;
import com.energyfuture.symphony.m3.download.DownloadApkService;
import com.energyfuture.symphony.m3.util.Constants;

import net.sf.json.JSONArray;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/11/23 0023.
 */
public class CommToolsActivity extends ActionBarActivity implements View.OnClickListener {
    private Context context = CommToolsActivity.this;
    /**
     * 返回
     */
    private LinearLayout mllBack;
    private GridView mGridView;
    private TrCommonToolsDao trCommonToolsDao = new TrCommonToolsDao(context);
    private List<TrCommonTools> trCommonToolsList = new ArrayList<TrCommonTools>();
    private CommonToolsAdapter commonToolsAdapter;
    private ProgressBar mProgressBar,mProgressBar2;
    private TextView mToolInstall;
    private DownloadApkService msgService;
    private Intent intent;
    private boolean mBound = false; //标识是否绑定服务

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comm_tools);

        initViews();

    }

    private void initViews() {
        mGridView = (GridView) findViewById(R.id.comm_tool_gridview);
        mProgressBar2 = (ProgressBar) findViewById(R.id.comm_tool_progressBar);
        mllBack = (LinearLayout) findViewById(R.id.theme_back_tool);
        mllBack.setOnClickListener(this);

        mProgressBar2.setVisibility(View.VISIBLE);

        getCommonToolsFromWeb();

        commonToolsAdapter = new CommonToolsAdapter(this,new ArrayList<TrCommonTools>());
        mGridView.setAdapter(commonToolsAdapter);

        mProgressBar2.setVisibility(View.GONE);
    }

    /**
     * 打开文件时调用的下载方法
     */
    public void openFileDownLoadApk(){
        Intent intent = getIntent();
        //标识是pdf还是word
        int flag = intent.getFlags();
        TrCommonTools trCommonTools = null;
        ProgressBar toolProgress = null;
        TextView toolInstall = null;
        Map<String,Object> map = null;

        if(flag == 1){//pdf
            trCommonTools = getTrCommonTools(trCommonToolsList,"PDF阅读器.apk");
            map = Constants.commToolMap.get("pdf");
        }else if(flag == 2){//word
            trCommonTools = getTrCommonTools(trCommonToolsList,"WORD.apk");
            map = Constants.commToolMap.get("word");
        }
        if(map != null){
            toolProgress = (ProgressBar) map.get("toolProgress");
            toolInstall = (TextView) map.get("toolInstall");
            downLoadApk(trCommonTools,toolProgress,toolInstall);
        }
    }

    /**
     * 获取到对应的下载对象
     * @param apkName
     * @return
     */
    private TrCommonTools getTrCommonTools(List<TrCommonTools> commonToolsList,String apkName){
        for(int i = 0;i < commonToolsList.size();i++){
            if(commonToolsList.get(i).getFilename().equals(apkName)){
                return commonToolsList.get(i);
            }
        }
        return null;
    }

    /**
     * 查询常用工具
     * @return
     */
    private List<TrCommonTools> getTrCommonToolsList(){
        TrCommonTools trCommonTools = new TrCommonTools();
        trCommonTools.setType("2");
        trCommonToolsList = trCommonToolsDao.queryTrCommonToolsList(trCommonTools);
        commonToolsAdapter.trCommonToolsList.addAll(trCommonToolsList);
        commonToolsAdapter.notifyDataSetChanged();
        return trCommonToolsList;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.theme_back_tool:
                onBackPressed();
                break;
            default:
                break;
        }
    }

    private void getCommonToolsFromWeb(){
        Runnable runnable = new Runnable(){
            @Override
            public void run() {
                Message message = new Message();
                //提交消息到云平台
                Map<String, Object> dataMap = new HashMap<String, Object>();
                String http = URLs.HTTP+URLs.HOST+"/"+ "inspectfuture/" +URLs.COMMONTOOLSDOWNLOAD;
                Map resultMap = ApiClient.sendHttpPostMessageData(null, http, dataMap, "", context, "文本");
                Log.i("=============resultMap=", resultMap + "");
                //新增成功
                if(resultMap != null){
                    String dataStr = (String) resultMap.get("date");
                    JSONArray dataArray = new JSONArray(dataStr);
                    for(int i=0;i<dataArray.length();i++){
                        net.sf.json.JSONObject dataObj = dataArray.getJSONObject(i);
                        TrCommonTools trCommonTools = new TrCommonTools();
                        trCommonTools.setId(dataObj.getString("id"));
                        trCommonTools.setFilename(dataObj.getString("filename"));
                        trCommonTools.setFileurl(dataObj.getString("fileurl"));
                        trCommonTools.setUploadpersonid(dataObj.getString("uploadpersonid"));
                        trCommonTools.setUploadtime(dataObj.getString("uploadtime"));
                        trCommonTools.setRemarks(dataObj.getString("remarks"));
                        trCommonTools.setFilesize(dataObj.getString("filesize"));
                        trCommonTools.setType(dataObj.getString("type"));
                        trCommonToolsList.add(trCommonTools);
                    }
                    trCommonToolsDao.insertListData(trCommonToolsList);
                    message.what = 1;
                }else{
                    message.what = 0;
                }
                handler.sendMessage(message);
            }
        };
        new Thread(runnable).start();
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == 1){
                List<TrCommonTools> commonToolsList = getTrCommonToolsList();
            }
            super.handleMessage(msg);
        }
    };

    /**
     * 启动服务,下载apk
     */
    public void downLoadApk(TrCommonTools trCommonTools,ProgressBar toolProgress,TextView toolInstall){
        mProgressBar = toolProgress;
        mToolInstall = toolInstall;
        mProgressBar.setVisibility(View.VISIBLE);
        mToolInstall.setText("(正在下载...)");
        Constants.isDownLoad = false;
        intent =new Intent(this, DownloadApkService.class);
        intent.putExtra("trCommonTools",trCommonTools);
        intent.setAction("com.energyfuture.symphony.m3.download.DOWNLOADAPD_SERVICE");
        startService(intent);
        bindService(intent, conn, BIND_AUTO_CREATE);

    }

    /** 定交ServiceConnection，用于绑定Service的*/
    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBound = false;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            //返回一个MsgService对象
            msgService = ((DownloadApkService.MsgBinder)service).getService();
            mBound = true;
            //注册回调接口来接收下载进度的变化
            msgService.setOnProgressListener(new DownloadApkService.OnProgressListener() {

                @Override
                public void onProgress(int progress) {
                    mProgressBar.setProgress(progress);
                    updateHandler.sendEmptyMessage(progress);
                }

            });

        }
    };

    private Handler updateHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == 100){
                mProgressBar.setVisibility(View.INVISIBLE);
                mToolInstall.setText("(下载完成,点击安装)");
                Constants.isDownLoad = true;
                Log.i("============isDownLoad="+Constants.isDownLoad,"msg.what="+msg.what);
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onDestroy() {
        if (mBound) {
            unbindService(conn);
            mBound = false;
        }
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
//        boolean b = Constants.isDownLoad;
        if(Constants.isDownLoad){
            super.onBackPressed();
        }else{
            Toast.makeText(context, "正在下载,请稍候...", Toast.LENGTH_SHORT).show();
        }
    }

    private void notificationInit(){
        //通知栏内显示下载进度条
        Intent intent=new Intent(this,CommToolsActivity.class);//点击进度条，进入程序
        PendingIntent pIntent=PendingIntent.getActivity(this, 0, intent, 0);
//        mNotificationManager=(NotificationManager) this.getSystemService(NOTIFICATION_SERVICE);
//        mNotification=new Notification();
//        mNotification.icon=R.drawable.ic_launcher;
//        mNotification.tickerText="开始下载";
        RemoteViews contentView=new RemoteViews(getPackageName(),R.layout.comm_tool_down);//通知栏中进度布局
//        mNotification.contentIntent=pIntent;
//  mNotificationManager.notify(0,mNotification);
    }
}
