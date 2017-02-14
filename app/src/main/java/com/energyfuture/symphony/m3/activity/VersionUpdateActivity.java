package com.energyfuture.symphony.m3.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.energyfuture.symphony.m3.common.ApiClient;
import com.energyfuture.symphony.m3.common.AppException;
import com.energyfuture.symphony.m3.common.URLs;
import com.energyfuture.symphony.m3.dao.TrVersionUpdateDao;
import com.energyfuture.symphony.m3.domain.TrVersionUpdate;
import com.energyfuture.symphony.m3.util.Constants;
import com.energyfuture.symphony.m3.util.VersionUpdateAdapter;
import com.energyfuture.symphony.m3.version.Update;
import com.energyfuture.symphony.m3.version.UpdateManager;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VersionUpdateActivity extends ActionBarActivity {

    private Context context = VersionUpdateActivity.this;
    private VersionUpdateAdapter versionUpdateAdapter;

    private Handler handler;
    private Update mUpdate;

    private ImageView update_back;
    private TextView now_version;
    private TextView version_name;
    private ListView log_listview;
    private LinearLayout them_back;
    private Button check_update;
    private View viewCon;
    private List<TrVersionUpdate> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_version_update);
        initview();
       //检查更新
        CheckUpdate();
        //初始化数据
        //init();
        //点击返回
        imgback();
    }

    private void imgback() {
        them_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                return;
            }
        });
    }

    private void inioAdapter() {
        TrVersionUpdateDao updateDao=new TrVersionUpdateDao(context);
        TrVersionUpdate update=new TrVersionUpdate();
        list=updateDao.queryVersionUpdateList(update);
        if(list.size()!=0){
            versionUpdateAdapter=new VersionUpdateAdapter(list,context);
            log_listview.setAdapter(versionUpdateAdapter);
        }
    }

    //更新实现
    private void CheckUpdate() {

    check_update.setOnClickListener( new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            UpdateManager.getUpdateManager().checkAppUpdate(context, true, (TextView) viewCon.findViewById(R.id.versiontext));
        }
    });
    }



    public class GetHtmlThread extends Thread
    {
        @Override
        public void run() // 线程处理的内容
        {
            Message msg = new Message();
            try {
                Update update = ApiClient.checkVersion(null);
                msg.what = 1;
                msg.obj = update;
            } catch (AppException e) {
                e.printStackTrace();
                Constants.recordExceptionInfo(e, context, context.toString()+"/GetHtmlThread");
            }
            handler.sendMessage(msg);
            super.run();
        }
    }

    //布局初始化
    private void initview() {
         them_back = (LinearLayout) findViewById(R.id.theme_back);
         log_listview = (ListView) findViewById(R.id.log_listview);
         update_back = (ImageView) findViewById(R.id.update_back);
//         check_update = (TextView) findViewById(R.id.check_update);
         now_version = (TextView) findViewById(R.id.now_version);
         version_name = (TextView) findViewById(R.id.version_name);
         check_update = (Button)findViewById(R.id.check_bt);
         viewCon = LayoutInflater.from(this).inflate(R.layout.indexmain, null);

        GetHtmlThread thread = new GetHtmlThread();
        thread.start();
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what == 1){
                    mUpdate = (Update)msg.obj;
                    String name = mUpdate.getVersionName();
                    version_name.setText(name);
                }
                if(msg.what==2){
                    inioAdapter();
                }
            }
        };
        inioAdapter();
    }
}
