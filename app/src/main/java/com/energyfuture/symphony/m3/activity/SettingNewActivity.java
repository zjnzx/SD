package com.energyfuture.symphony.m3.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.energyfuture.symphony.m3.analysis.DataSyschronized;
import com.energyfuture.symphony.m3.dao.TrDataSynchronizationDao;
import com.energyfuture.symphony.m3.domain.TrDataSynchronization;
import com.energyfuture.symphony.m3.util.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/11/23 0023.
 */
public class SettingNewActivity extends ActionBarActivity implements View.OnClickListener {
    private Context context = SettingNewActivity.this;
    /**
     * SD卡管理,消息内容管理,流量管理,存储
     */
    private LinearLayout mllSdManager, mllMessageManager, mllFlowManager, mllMemory,llDataUpload,llTools;
    /**
     * 返回
     */
    private LinearLayout mllBack;
    private ProgressDialog progressDialog;
    private int finish = 0;
    private int datacount = 0;
    private TrDataSynchronizationDao trDataSynchronizationDao = new TrDataSynchronizationDao(context);
    private String max = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_new);
        initViews();
    }

    private void initViews() {
        mllSdManager = (LinearLayout) findViewById(R.id.ll_sdManager);
        mllSdManager.setOnClickListener(this);
        mllMessageManager = (LinearLayout) findViewById(R.id.ll_MessageManager);
        mllMessageManager.setOnClickListener(this);
        mllFlowManager = (LinearLayout) findViewById(R.id.ll_flowManager);
        mllFlowManager.setOnClickListener(this);
        mllMemory = (LinearLayout) findViewById(R.id.ll_memory);
        mllMemory.setOnClickListener(this);
        mllBack = (LinearLayout) findViewById(R.id.theme_back);
        mllBack.setOnClickListener(this);
        llDataUpload = (LinearLayout) findViewById(R.id.ll_dataUpload);
        llDataUpload.setOnClickListener(this);
        llTools = (LinearLayout) findViewById(R.id.ll_tools);
        llTools.setOnClickListener(this);
    }

    final Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==1 && msg.obj != null){
                final Map<String,Object> resMap = (Map<String,Object>)msg.obj;
                    if("ok".equals(resMap.get("0"))) {
                    List<TrDataSynchronization> syncData  = (ArrayList) resMap.get("data");
                    boolean flag = true;
                    for(TrDataSynchronization trDataSynchronization : syncData){
                        if(trDataSynchronization.getDatatype().equals("1") && !trDataSynchronization.getOpertype().equals("3")){
                            List<TrDataSynchronization> list = trDataSynchronizationDao.queryTrDataSynchronizationList(max);
                            if(list.size() > 0){
                                flag = false;
                                showDataProgressDialog(list);
                            }
                            break;
                        }
                    }
                    if(flag){
                        Toast.makeText(context,"暂无数据需要更新",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    };


    //消息进行处理
    final Handler dataHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 2){
                //更新项目列表
                Toast.makeText(SettingNewActivity.this, "数据更新完成", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                finish = 0;
            }else if (msg.what == 1){
                finish++;
                //已下载/任务总数
                progressDialog.setMessage("正在进行数据更新"+"("+finish +"/"+datacount+")");
                //更新进度条
                progressDialog.setProgress(finish);
            }
        }
    };

    private void showDataProgressDialog(final List<TrDataSynchronization> syncData) {
        //获取平台下发任务总数
        datacount = syncData.size();
        progressDialog = new ProgressDialog(this);
        progressDialog.setIcon(R.drawable.green);
        //获取当前项目名称
        progressDialog.setCancelable(false);
        progressDialog.setMessage("正在进行数据更新"+"("+finish +"/"+datacount+")");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.show();
        final Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                downData(syncData);
            }
        });
        thread.start();
    }

    //任务下载
    private void downData( final List<TrDataSynchronization> syncData) {
        progressDialog.setMax(datacount);//设置任务总数
        try {
            for (int i=0;i<datacount;i++){
                //通过任务ID向终端发送请求
                Map<String,Object> condMap = new HashMap<String, Object>();
                condMap.put("OBJ","LEDGERINFO");
                condMap.put("TYPE",null);
                condMap.put("SIZE",5);
                condMap.put("USERID",Constants.getLoginPerson(context).get("userId"));
                condMap.put("KEY", syncData.get(i).getSynchronizationid());
                new DataSyschronized(SettingNewActivity.this).getDataFromWeb(condMap, dataHandler);
            }
            Thread.sleep(1000);
            Message msg = new Message();
            msg.what = 2;
            dataHandler.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
            Constants.recordExceptionInfo(e, context, context.toString());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_sdManager:
                startActivity(new Intent().setClass(this,SettingImageActivity.class));
                break;
            case R.id.ll_MessageManager:
                startActivity(new Intent().setClass(this,MessageMagActivity.class));
                break;
            case R.id.ll_flowManager:
                startActivity(new Intent().setClass(this,FlowActivity.class));
                break;
            case R.id.ll_memory:
                break;
            case R.id.theme_back:
                finish();
                break;
            case R.id.ll_dataUpload:
                Toast.makeText(SettingNewActivity.this,"数据更新中...",Toast.LENGTH_SHORT).show();
                max = trDataSynchronizationDao.queryTrDataSynchronizationMax();
                Map<String,Object> condMap = new HashMap<String, Object>();
                condMap.put("OBJ","LEDGER");
                condMap.put("OPELIST",null);
                condMap.put("TYPE",null);
                condMap.put("SIZE",5);
                if(max == null){
                    condMap.put("MAX","");
                }else{
                    condMap.put("MAX",max);
                }
                condMap.put("USERID", Constants.getLoginPerson(context).get("userId"));
                new DataSyschronized(SettingNewActivity.this).getDataFromWeb(condMap, handler);
                break;
            case R.id.ll_tools:
                startActivity(new Intent().setClass(this,CommToolsActivity.class));
                break;

            default:
                break;


        }

    }
}
