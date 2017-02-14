package com.energyfuture.symphony.m3.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.energyfuture.symphony.m3.common.ApiClient;
import com.energyfuture.symphony.m3.common.MD5;
import com.energyfuture.symphony.m3.common.URLs;
import com.energyfuture.symphony.m3.dao.TrUserInfoDao;
import com.energyfuture.symphony.m3.domain.UserInfo;
import com.energyfuture.symphony.m3.util.Constants;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AlertPasswordActivity extends ActionBarActivity {
    private EditText oldpassword,newpasswprd,confimpassword;
    private Button confim;
    private LinearLayout toolbar_back;
    private Context context=AlertPasswordActivity.this;
    private String userId;
    private TrUserInfoDao userInfoDao;
    private Handler handler;
    private List<UserInfo> userInfolist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert_password);
        inioView();

        handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what==1){
                    String newpassword= (String) msg.obj;
                    Map<Object,Object> columnsMap=new HashMap<>();
                    Map<Object,Object> wheresMap=new HashMap<>();
                    columnsMap.put("YHMM",newpassword);
                    wheresMap.put("YHID",userId);

                    boolean issuccess = userInfoDao.updateUserInfo(columnsMap, wheresMap);
                    if(issuccess){
                        Toast.makeText(context,"修改成功",Toast.LENGTH_SHORT).show();
                        Intent i =new Intent(context,LoginActivity.class);
                        i.putExtra("deletePassword",1);
                        startActivity(i);
                    }else{
                        Toast.makeText(context,"修改失败",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(context,"修改失败",Toast.LENGTH_SHORT).show();
                }
                confim.setEnabled(true);
            }
        };
    }

    private void inioView() {
        userInfolist=queryUserInfo();
        oldpassword= (EditText) findViewById(R.id.oldpassword);
        newpasswprd= (EditText) findViewById(R.id.newpassword);
        confimpassword= (EditText) findViewById(R.id.confimpassword);
        toolbar_back= (LinearLayout) findViewById(R.id.toolbar_back);
        confim= (Button) findViewById(R.id.confim);

        confim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oldtext=oldpassword.getText().toString();
                String newtext=newpasswprd.getText().toString();
                String congfimtext=confimpassword.getText().toString();

                if(TextUtils.isEmpty(oldtext)){
                    Toast.makeText(context,"原密码不能为空",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(newtext)){
                    Toast.makeText(context,"新密码不能为空",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(congfimtext)){
                    Toast.makeText(context,"请确认新密码",Toast.LENGTH_SHORT).show();
                    return;
                }

                String oldpassword  = new MD5().getMD5ofStr(oldtext).toUpperCase();
                if(userInfolist.get(0).getYhmm().equals(oldpassword)){
                    if(!newtext.equals(congfimtext)){
                        Toast.makeText(context,"两次输入的密码不一致",Toast.LENGTH_SHORT).show();
                        return;
                    }else{
                        String newpassword=new MD5().getMD5ofStr(newtext).toUpperCase();
                        alertPassword(newpassword);
                    }

                }else{
                    Toast.makeText(context,"原密码错误",Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });

        toolbar_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void alertPassword(final String newpassword) {
        confim.setEnabled(false);
        final Map<String,Object> dataMap = new HashMap<String,Object>();

        dataMap.put("YHMM",newpassword);
        dataMap.put("YHID",userId);

        new Thread(new Runnable() {
            @Override
            public void run() {
                String http = URLs.HTTP+URLs.HOST+"/"+ "inspectfuture/" +URLs.UPDATEUSER;
                Map resultMap = ApiClient.sendHttpPostMessageData(null, http, dataMap, "", context, "文本");

                if(resultMap != null && resultMap.get("ISTRUE").equals("1")){
                    Message msg = new Message();
                    msg.obj = newpassword;
                    msg.what = 1;
                    handler.sendMessage(msg); //成功
                }else{
                    handler.sendEmptyMessage(2); //修改失败
                }
            }
        }).start();
    }

    private List<UserInfo> queryUserInfo() {
        userId = Constants.getLoginPerson(this).get("userId");
        userInfoDao=new TrUserInfoDao(context);
        UserInfo userInfo=new UserInfo();
        userInfo.setYhid(userId);
        return userInfoDao.queryUserInfoList(userInfo);
    }
}
