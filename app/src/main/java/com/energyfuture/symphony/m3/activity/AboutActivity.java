package com.energyfuture.symphony.m3.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.energyfuture.symphony.m3.common.ApiClient;
import com.energyfuture.symphony.m3.common.AppException;
import com.energyfuture.symphony.m3.dao.SympMessageRealDao;
import com.energyfuture.symphony.m3.domain.TrExceptionInfo;
import com.energyfuture.symphony.m3.util.Constants;
import com.energyfuture.symphony.m3.version.Update;
import com.energyfuture.symphony.m3.version.UpdateManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
* Created by Administrator on 2015/12/2 0023.
*/
public class AboutActivity extends ActionBarActivity implements View.OnClickListener {

    /**
     * 检查更新 产品介绍
     */
    private LinearLayout mllCheckUpadate,mllProductPre;

    /**
     * 产品介绍
     */
    private LinearLayout mllAboutDes;

    /**
     * 返回
     */
    private LinearLayout mllBack;

    private ImageView mImgUp,mImgDown;
    private Boolean isTrue = false;

    /**
     * 版本号
     */
    private TextView mTxtVerNum;
    private View view;

    private Handler handler;
    private Update mUpdate;
    private View viewCon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        initViews();
    }

    private void initViews() {
        view = (View)findViewById(R.id.view);

        mllCheckUpadate = (LinearLayout) findViewById(R.id.ll_check_update);
        mllCheckUpadate.setOnClickListener(this);

        mllProductPre = (LinearLayout) findViewById(R.id.ll_product_pre);
        mllProductPre.setOnClickListener(this);

        mllBack = (LinearLayout) findViewById(R.id.theme_back);
        mllBack.setOnClickListener(this);

        mImgUp = (ImageView) findViewById(R.id.img_about_up);
        mImgDown= (ImageView) findViewById(R.id.img_about_down);
        mllAboutDes= (LinearLayout) findViewById(R.id.ll_about_des);

        mTxtVerNum = (TextView) findViewById(R.id.txt_version_num);
        GetHtmlThread thread = new GetHtmlThread();
        thread.start();
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what == 1){
                    mUpdate = (Update)msg.obj;
                    String name = mUpdate.getVersionName();
                    mTxtVerNum.setText(name);
                }
                super.handleMessage(msg);
            }
        };

         viewCon = LayoutInflater.from(this).inflate(R.layout.indexmain, null);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_check_update:
                startActivity(new Intent().setClass(AboutActivity.this, VersionUpdateActivity.class));
//                CheckUpdate();
                break;
            case R.id.ll_product_pre:
                if (isTrue == true){
                    mImgUp.setVisibility(View.VISIBLE);
                    mImgDown.setVisibility(View.GONE);
                    mllAboutDes.setVisibility(View.VISIBLE);
                    view.setVisibility(View.VISIBLE);
                    isTrue=false;
                }else{
                    mImgUp.setVisibility(View.GONE);
                    mImgDown.setVisibility(View.VISIBLE);
                    mllAboutDes.setVisibility(View.GONE);
                    view.setVisibility(View.GONE);
                    isTrue=true;
                }
                break;
            case R.id.theme_back:
                finish();
                break;

            default:
                break;
        }

    }

    private void CheckUpdate() {
        UpdateManager.getUpdateManager().checkAppUpdate(AboutActivity.this, true, (TextView) viewCon.findViewById(R.id.versiontext));
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
                Constants.recordExceptionInfo(e,AboutActivity.this,"AboutActivity/GetHtmlThread");
            }
            handler.sendMessage(msg);
            super.run();
        }
    }
}
