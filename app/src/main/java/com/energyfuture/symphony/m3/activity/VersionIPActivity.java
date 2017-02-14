package com.energyfuture.symphony.m3.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.energyfuture.symphony.m3.common.ApiClient;
import com.energyfuture.symphony.m3.common.AppException;
import com.energyfuture.symphony.m3.common.URLs;
import com.energyfuture.symphony.m3.util.Constants;
import com.energyfuture.symphony.m3.version.Update;

public class VersionIPActivity extends ActionBarActivity {
    private Handler handler;
    private Update mUpdate;
    private LinearLayout them_back;
    private TextView ipEdit;
    private String city;
    private String ip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.activity_version_ip);
        ipEdit = (TextView)findViewById(R.id.IPEdit);
        them_back = (LinearLayout) findViewById(R.id.theme_back);
        imgback();
        ip = URLs.HOSTA;
        if(ip.equals("121.42.25.101:80")){
            city = " (陕西)";
        }else if(ip.equals("115.28.34.4:82")){
            city = " (湖南)";
        }else if(ip.equals("115.28.34.4:80")){
            city = " (上海)";
        }else if(ip.equals("121.42.25.101:81")){
            city = " (宁夏一队)";
        }else if(ip.equals("115.28.34.4:81")){
            city = " (宁夏二队)";
        }else if(ip.equals("121.42.25.101:82")){
            city = " (上海演示)";
        }else if(ip.equals("192.168.2.169:80") || ip.equals("115.28.34.4:81")){
            city = " (测试)";
        }
        ipEdit.setText(ip + city);
//        GetHtmlThread thread = new GetHtmlThread();
//        thread.start();
//        handler = new Handler(){
//            @Override
//            public void handleMessage(Message msg) {
//                if(msg.what == 1){
//                    mUpdate = (Update)msg.obj;
//                    ip = mUpdate.getDownloadUrl();
//                    ipEdit.setText(ip.substring(7,ip.indexOf("/versionUpdate")));
//                }
//                super.handleMessage(msg);
//            }
//        };
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
                    Constants.recordExceptionInfo(e, VersionIPActivity.this, "VersionIPActivity/GetHtmlThread");
                }
                handler.sendMessage(msg);
                super.run();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_version_i, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
