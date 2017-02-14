package com.energyfuture.symphony.m3.activity;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.energyfuture.symphony.m3.analysis.Alarmreceiver;
import com.energyfuture.symphony.m3.analysis.DataSyschronized;
import com.energyfuture.symphony.m3.analysis.GuardService;
import com.energyfuture.symphony.m3.common.ApiClient;
import com.energyfuture.symphony.m3.common.MD5;
import com.energyfuture.symphony.m3.common.URLs;
import com.energyfuture.symphony.m3.dao.SympUserInfoDao;
import com.energyfuture.symphony.m3.dao.TrVersionUpdateDao;
import com.energyfuture.symphony.m3.domain.Operation;
import com.energyfuture.symphony.m3.domain.TrVersionUpdate;
import com.energyfuture.symphony.m3.domain.UserInfo;
import com.energyfuture.symphony.m3.mqttservice.PushService;
import com.energyfuture.symphony.m3.ui.MaterialDialog;
import com.energyfuture.symphony.m3.ui.RedMaterialDialog;
import com.energyfuture.symphony.m3.util.AlarmreceiverReceiver;
import com.energyfuture.symphony.m3.util.Constants;
import com.energyfuture.symphony.m3.util.UplodaFile;
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

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoginActivity extends Activity {
    private Context context = LoginActivity.this;
    private Button btn_login;
    private int  deletePassword=0;
    private SharedPreferences sharedPreferences;
    private EditText user_name,user_password;
    private CheckBox is_password,is_autologin;
    private Editor editor = null;
    private MaterialDialog dialog;
    private Handler loginhandler;
    private ProgressBar bar;
    private String DEVICE_ID;
    // 用于判断进程是否运行
    private String Process_Name = "com.energyfuture.symphony.debug:guardService";
    private final String pathTemp = Environment.getExternalStorageDirectory()
            .getAbsolutePath();
    private final String FILE_SAVEPATH = pathTemp.substring(0,
            pathTemp.length() - 1)
            + "legacy" + "/M3/transformer/";
    private List<TrVersionUpdate> list=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        //在状态栏显示系统图标
        AlarmreceiverReceiver.showNotification(getBaseContext());

        getVeisioninfo();

        //版本检测
        UpdateManager.getUpdateManager().checkAppUpdate(this, false, new TextView(this));

        TelephonyManager tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        DEVICE_ID = tm.getDeviceId();

//      flow = ApiClient.sendFlow(this);

        initView();

        setUserInfo();
//        Intent i=getIntent();
//        if(i!=null){
//            deletePassword=i.getIntExtra("deletePassword",0);
//        }
//
//        if(deletePassword==1){
//            user_password.setText("");
//        }
        File file2=new File(FILE_SAVEPATH );
        if(!file2.exists()){
            file2.mkdirs();
        }
        File file=new File(FILE_SAVEPATH+"picture/" );
        if(!file.exists()){
            file.mkdirs();
        }
        File file1=new File(FILE_SAVEPATH+"file/" );
        if(!file1.exists()){
            file1.mkdirs();
        }
    }

    private void getVeisioninfo() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                getAppuptodateVersion();
            }
        }).start();
    }



    private void getAppuptodateVersion() {

        String url = URLs.HTTP+URLs.HOST+"/"+ "inspectfuture/" +URLs.GETAPPUPTODATEVERSION;
        HttpClient client=new DefaultHttpClient();

        List<NameValuePair> params = new ArrayList<NameValuePair>();



        params.add(new BasicNameValuePair("CODE", "0"));

        HttpPost httpPost = new HttpPost(url);
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
            HttpResponse httpResponse = client.execute(httpPost);
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                String result = EntityUtils.toString(httpResponse.getEntity());
                parseJson(result);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            Constants.recordExceptionInfo(e,context,context.toString());
        } catch (IOException e) {
            e.printStackTrace();
            Constants.recordExceptionInfo(e,context,context.toString());
        }
    }

    private void parseJson(String result) {
        try {
            JSONArray array=new JSONArray(result);
            for(int i=0;i<array.length();i++){
                TrVersionUpdate versionUpdate=new TrVersionUpdate();
                JSONObject object=array.getJSONObject(i);
                versionUpdate.setId(object.getString("ID"));
                versionUpdate.setAppinfo(object.getString("ADDINFO").equals("")? "暂无新增内容" : object.getString("ADDINFO"));
                versionUpdate.setUpdateinfo(object.getString("UPDATEINFO").equals("") ? "暂无更新内容" : object.getString("UPDATEINFO"));
                versionUpdate.setDeleteinfo(object.getString("DELETEINFO").equals("") ? "暂无删除内容" : object.getString("DELETEINFO"));
                versionUpdate.setVersioncode(object.getInt("VERSIONCODE"));
                versionUpdate.setVersionname(object.getString("VERSIONNAME").equals("") ? "暂无版本名称" : object.getString("VERSIONNAME"));
                versionUpdate.setLoadperson(object.getString("LOADPERSON"));
                versionUpdate.setUpdatetime(object.getString("UPDATETIME").equals("") ? Constants.dateformat2.format(new Date()):Constants.dateformat1.format(new Long(object.getString("UPDATETIME"))));

                list.add(versionUpdate);
            }
                savetoDb();
        } catch (JSONException e) {
            e.printStackTrace();
            Constants.recordExceptionInfo(e,context,context.toString());
        }
    }

    private void savetoDb() {
        TrVersionUpdateDao updateDao=new TrVersionUpdateDao(context);
        updateDao.insertListData(list);
    }

    /**
     * 记住用户信息
     */
    public void setUserInfo(){
        sharedPreferences = this.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        String userId = sharedPreferences.getString("userId","");
        String userPassword = sharedPreferences.getString("userPassword","");
        user_name.setText(userId);
        //是否记住密码
        Intent i=getIntent();
        deletePassword=i.getIntExtra("deletePassword",0);
        if(deletePassword == 1){
            is_password.setChecked(false);
            is_autologin.setChecked(false);
            editor.putBoolean("isPassword",false);
            editor.putBoolean("isAutoLogin",false);
            editor.commit();
        }else{
            is_password.setChecked(sharedPreferences.getBoolean("isPassword",false));
            is_autologin.setChecked(sharedPreferences.getBoolean("isAutoLogin",false));
        }

        if(is_password.isChecked()){
            user_password.setText(userPassword);
        }
        //是否自动登录
        if(deletePassword == 0){
            if(is_autologin.isChecked()){
                final String userPassword2 = new MD5().getMD5ofStr(userPassword).toUpperCase();
                checkUserInfo(userId, userPassword2, userPassword);
            }
        }

    }

    /**
     * 初始化控件
     */
    private void initView() {
        user_name = (EditText) findViewById(R.id.id_user_name);
        user_password = (EditText) findViewById(R.id.id_user_password);
        btn_login = (Button) findViewById(R.id.id_btn_login);
        is_password = (CheckBox) findViewById(R.id.ispassword);
        is_autologin = (CheckBox) findViewById(R.id.isautologin);

        //记住密码
        is_password.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editor.putBoolean("isPassword",isChecked);
            }
        });
        //自动登录
        is_autologin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editor.putBoolean("isAutoLogin",isChecked);
            }
        });

        //登陆事件
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               createDialog();
               btn_login.setEnabled(false);

                final String userId = user_name.getText().toString().trim();
                if ("".equals(userId)) {
                    Toast.makeText(getBaseContext(), "请输入工号！", Toast.LENGTH_SHORT).show();
                    btn_login.setEnabled(true);
                    dialog.dismiss();
                    return;
                }
                final String userPassword = user_password.getText().toString().trim();
                if ("".equals(userPassword)) {
                    Toast.makeText(getBaseContext(), "请输入密码！", Toast.LENGTH_SHORT).show();
                    btn_login.setEnabled(true);
                    dialog.dismiss();
                    return;
                }
                final String userPassword2 = new MD5().getMD5ofStr(userPassword).toUpperCase();

                final Handler loginHandler = new Handler(){
                    @Override
                    public void handleMessage(Message msg) {
                        if(msg.what==1 && msg.obj != null){
                            Map<String,String> resMap = (Map<String,String>)msg.obj;
                            if("ok".equals(resMap.get("0"))) {

                            }else{

                            }
                            userImage();
//                            checkUserInfo(userId, userPassword2, userPassword);
                        }else{
                            Toast.makeText(LoginActivity.this, "", Toast.LENGTH_SHORT).show();
                        }
                    }
                };

                loginhandler = new Handler(){
                    @Override
                    public void handleMessage(Message msg) {
                            checkUserInfo(userId, userPassword2, userPassword);
                    }
                };
                /**
                 * 根据用户名密码登陆
                 * @autor SongLin Yang
                 */
//                List<Operation> operationList = new ArrayList<Operation>();
//                operationList.add(new Operation("YHID","=",userId));
//                operationList.add(new Operation("YHMM","=",userPassword2));
//                Map<String, Object> condMap = new HashMap<String, Object>();
//                condMap.put("OBJ","USER");
//                condMap.put("OPELIST",operationList);
//                condMap.put("TYPE",null);
//                condMap.put("SIZE",5);
//                condMap.put("USERID",userId);
//                new DataSyschronized(LoginActivity.this).getDataFromWeb(condMap, loginHandler);

                final Handler runnHandler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        if(msg.what == 1){
                            /**
                             * 根据用户名密码登陆
                             * @autor SongLin Yang
                             */
                            List<Operation> operationList = new ArrayList<Operation>();
                            operationList.add(new Operation("YHID","=",userId));
                            operationList.add(new Operation("YHMM","=",userPassword2));
                            Map<String, Object> condMap = new HashMap<String, Object>();
                            condMap.put("OBJ","USER");
                            condMap.put("OPELIST",operationList);
                            condMap.put("TYPE",null);
                            condMap.put("SIZE",5);
                            condMap.put("USERID",userId);
                            new DataSyschronized(LoginActivity.this).getDataFromWeb(condMap, loginHandler);
                        }else{
                            final RedMaterialDialog redDialog = new RedMaterialDialog(context);
                            redDialog.setTitle("授权码：" + DEVICE_ID)
                                    .setMessage("您的移动终端暂未授权，请联系管理人员授权后使用，谢谢！")
                                    .setPositiveButton("确定", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            btn_login.setEnabled(true);
                                            dialog.dismiss();
                                            redDialog.dismiss();
                                        }
                                    }).setCanceledOnTouchOutside(false).show();
                        }
                    }
                };

                final Map<String,Object> dataMap = new HashMap<String,Object>();
                dataMap.put("USERID",userId);
                dataMap.put("IDENTIFYING",DEVICE_ID);

                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        String http = URLs.HTTP+URLs.HOST+"/"+ "inspectfuture/" +URLs.IDENTIFYING;
                        Map resultMap = ApiClient.sendHttpPostMessageData(null, http, dataMap, "", context, "文本");

                        if(resultMap != null && resultMap.get("ISEMPOWER").equals("0")){
                            Message msg = new Message();
                            msg.what = 1;
                            runnHandler.sendMessage(msg);
                        }else{
                            Message msg = new Message();
                            msg.what = 2;
                            runnHandler.sendMessage(msg);
                        }
                    }
                };
                new Thread(runnable).start();

                Intent intent =new Intent(LoginActivity.this, Alarmreceiver.class);
                PendingIntent sender=PendingIntent
                        .getBroadcast(LoginActivity.this, 0, intent, 0);

                //每天凌晨1点发送广播查新资料库数据
                //开始时间
                Calendar c = Calendar.getInstance();
                c.setTimeInMillis(System.currentTimeMillis());
                c.set(Calendar.DAY_OF_MONTH, (c.get(Calendar.DAY_OF_MONTH)));
                c.set(Calendar.HOUR, 13);        //设置闹钟小时数
                c.set(Calendar.MINUTE, 0);            //设置闹钟的分钟数
                c.set(Calendar.SECOND, 0);                //设置闹钟的秒数
                c.set(Calendar.MILLISECOND, 0);

                AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);
                long a  = c.getTimeInMillis();
                //发送广播
                am.setRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), 86400000, sender);
            }
        });
    }

    private void userImage(){
        SympUserInfoDao userDao = new SympUserInfoDao(this);
        UserInfo userInfo = new UserInfo();
        final List<UserInfo> list = userDao.queryUserInfoList(userInfo);
        new Thread(){
            @Override
            public void run() {
                for(UserInfo user : list){
                    if(!user.getSsxt().equals("")){
                        String url = "";
                        String fileUrl = user.getSsxt().replaceAll("\\\\","/");
                        user.setSsxt(fileUrl);
                        String username = user.getSsxt().substring(5);
                        File file = new File(FILE_SAVEPATH + "picture/head/USER/small/" + username);
                        if(!file.exists()){
                            try {
                                url = URLs.HTTP+ URLs.HOST+"/"+"INSP/"+ URLEncoder.encode(user.getSsxt(), "UTF-8").replaceAll("%2F", "/");
                                UplodaFile.downuserFile(url, user);
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                Message msg = new Message();
                msg.what = 1;
                loginhandler.sendMessage(msg);
            }
        }.start();
    }

    /**
     *  检查用户名是否存在
     * @param userId
     * @param userPassword
     * @return
     */
    private void checkUserInfo(String userId,String userPassword2,String userPassword){
        SympUserInfoDao userDao = new SympUserInfoDao(this);
        UserInfo userInfo = new UserInfo();
        userInfo.setYhid(userId);
        userInfo.setYhmm(userPassword2);
        List<UserInfo> list = userDao.queryUserInfoList(userInfo);
            if( null!=list && list.size() > 0){
                editor.putString("userId",userId);
                editor.putString("userName",list.get(0).getXm());
                editor.putString("userPassword",userPassword);
                editor.putString("department",list.get(0).getBmbh());
                editor.commit();
                //重新根据用户名启动消息服务
                Intent intent = new Intent(getApplicationContext(), PushService.class);
                stopService(intent);
                startService(intent);
//                Intent intent2 = new Intent(getApplicationContext(), MessageHandlerService.class);
//                stopService(intent2);
//                startService(intent2);

                //登录成功后启动消息扫描服务
                boolean isRun = Constants.isProessRunning(LoginActivity.this, Process_Name);
                if (isRun == false) {
                    Intent messageintent = new Intent(getApplicationContext(), GuardService.class);
//              stopService(messageintent);
                    startService(messageintent);
                }

                //登陆成功
                toIndexActivity(userId);
            }else{
                Toast.makeText(this,"工号不存在或密码有误，请重新输入!",Toast.LENGTH_SHORT).show();
                btn_login.setEnabled(true);
                if(dialog!=null){
                    dialog.dismiss();
                }
            }
    }

    /**
     * 登陆成功,跳转到首页
     */
    private void toIndexActivity(final String userId){

        Intent intent = new Intent(LoginActivity.this,IndexActivity.class);
        intent.putExtra("userId",userId);

        startActivity(intent);

        btn_login.setEnabled(true);
    }

//    //判断头像是否已下载
//    public boolean fileIsExists(){
//        try{
//            File f=new File(FILE_SAVEPATH + "avatar/" + userIdtwo + ".jpg");
//            if(!f.exists()){
//                return false;
//            }
//
//        }catch (Exception e) {
//            // TODO: handle exception
//            return false;
//        }
//        return true;
//    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            return true;
        }
        return super.onKeyDown(keyCode, event);

    }
    private void createDialog() {
        dialog=new MaterialDialog(LoginActivity.this);
        View view=LayoutInflater.from(context).inflate(R.layout.progressdialog, null);
        dialog.setView(view).show();
    }
}
