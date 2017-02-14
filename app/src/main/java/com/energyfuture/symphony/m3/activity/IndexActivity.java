package com.energyfuture.symphony.m3.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.energyfuture.symphony.m3.analysis.DataSyschronized;
import com.energyfuture.symphony.m3.common.UIHelper;
import com.energyfuture.symphony.m3.dao.SympMessageRealDao;
import com.energyfuture.symphony.m3.dao.SympUserInfoDao;
import com.energyfuture.symphony.m3.dao.TrFeedBackThemeDao;
import com.energyfuture.symphony.m3.domain.TrFeedBackTheme;
import com.energyfuture.symphony.m3.domain.UserInfo;
import com.energyfuture.symphony.m3.ripple.RippleView;
import com.energyfuture.symphony.m3.util.Constants;
import com.energyfuture.symphony.m3.version.UpdateManager;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


public class IndexActivity extends ActionBarActivity implements View.OnClickListener{
    private Context context = IndexActivity.this;
    private LinearLayout setting,feedback,about,back_login,file_pdf,report;
    private ImageButton inspection, monitoring, diagnose;
    private ImageView user_icon,user_online ;
    private TextView user_name,user_onlineName;
    private RippleView upvs;
    private String userId;
    private static final int DOUBLE_CLICK_TIME = 350;//双击间隔时间350毫秒
    private int waitDouble = 0;
    List<UserInfo> list;
    public static  String userName ;
    private String path;
    private File pathFile;
    private SympUserInfoDao sympUserInfoDao = new SympUserInfoDao(this);
    private final String pathTemp = Environment.getExternalStorageDirectory()
            .getAbsolutePath();
    private final String M3FILE_SAVEPATH = pathTemp.substring(0,
            pathTemp.length() - 1)
            + "legacy" + "/M3/";
    private final String pictureFILE_SAVEPATH = pathTemp.substring(0,
            pathTemp.length() - 1)
            + "legacy" + "/M3/transformer/picture/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.indexmain);

        Intent intent = getIntent();
        userId = intent.getStringExtra("userId");
        UserInfo userInfo = new UserInfo();
        userInfo.setYhid(userId);
        list = sympUserInfoDao.queryUserInfoList(userInfo);
        initView();
        setToolbar();
        //版本检测
//       UpdateManager.getUpdateManager().checkAppUpdate(this,false,(TextView)findViewById(R.id.versiontext));
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
            Constants.recordExceptionInfo(e,context,context.toString());
        }
        File M3Dir=new File(M3FILE_SAVEPATH);
        if(!M3Dir.exists())
        {
            M3Dir.mkdirs();
            File pictureDir=new File(pictureFILE_SAVEPATH);
            if(!pictureDir.exists())
            {
                pictureDir.mkdirs();
            }
        }
        final SharedPreferences sharedPreferences = this.getSharedPreferences("M3_SETTNG", getApplicationContext().MODE_PRIVATE);
        String rh_sd_route_context = sharedPreferences.getString("rh_sd_route","");
        String rh_file_prefix_context  = sharedPreferences.getString("rh_file_prefix","");

        SharedPreferences M3_SETTNG = this.getSharedPreferences("M3_SETTNG", getApplicationContext().MODE_PRIVATE);
        SharedPreferences.Editor editor = M3_SETTNG.edit();
        if(rh_sd_route_context.equals(""))
        {
            editor.putString("rh_sd_route","http://flashair/images/DirA/");
        }
        if(rh_file_prefix_context.equals(""))
        {
            editor.putString("rh_file_prefix","IR_;DC_");
        }
        editor.commit();
    }

    private void initView() {
        user_name = (TextView)findViewById(R.id.user_name);
        user_icon=(ImageView)findViewById(R.id.user_icon);
        user_online=(ImageView)findViewById(R.id.user_menu_icon);
        user_onlineName=(TextView)findViewById(R.id.user_menu_name);

        setting= (LinearLayout) findViewById(R.id.setting);
        feedback= (LinearLayout) findViewById(R.id.feedback);
        about= (LinearLayout) findViewById(R.id.about);
        back_login= (LinearLayout) findViewById(R.id.back_login);
        file_pdf= (LinearLayout) findViewById(R.id.file_pdf);
        report= (LinearLayout) findViewById(R.id.index_report);

        setting.setOnClickListener(this);
        feedback.setOnClickListener(this);
        about.setOnClickListener(this);
        back_login.setOnClickListener(this);
        file_pdf.setOnClickListener(this);
        report.setOnClickListener(this);
        user_icon.setOnClickListener(this);
        user_online.setOnClickListener(this);

        user_name.setText(list.get(0).getXm());
        user_onlineName.setText(list.get(0).getXm());
        String userId=list.get(0).getYhid()==null?"":list.get(0).getYhid();
        if(!Constants.getUsericon(context).equals("")){
            String imagepath=Constants.getUsericon(context);
            user_icon.setImageURI(Uri.parse(imagepath));
            user_online.setImageURI(Uri.parse(imagepath));
        }else{
            int userhead= Constants.userHead(userId);
            user_icon.setImageResource(userhead);
            user_online.setImageResource(userhead);
        }
        //展示用户头像

        View task = findViewById(R.id.index_task);
        task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIHelper.showRwgl(IndexActivity.this);
            }
        });

        //打开计算器
        View calc = findViewById(R.id.index_calc);
        calc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent();
                mIntent.setClassName("com.android.calculator2","com.android.calculator2.Calculator");
                startActivity(mIntent);
            }
        });

        upvs = (RippleView)findViewById(R.id.upvs);
        //版本更新按钮监听
        upvs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(IndexActivity.this,VersionUpdateActivity.class);
                    startActivity(intent);
                    UpdateManager.getUpdateManager().checkAppUpdate(IndexActivity.this,false,(TextView)findViewById(R.id.versiontext));
                } catch (Exception e) {
                    e.printStackTrace();
                    Constants.recordExceptionInfo(e,context,context.toString());
                }
            }
        });
    }

    private void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_index);
        toolbar.setTitle("主页");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        DrawerLayout mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerIndex);
//        DrawerLayout mDrawerLayout = new DrawerLayout(this);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        actionBarDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener(actionBarDrawerToggle);

        LinearLayout mDrawerList = (LinearLayout) findViewById(R.id.drawer_index_list);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.setting:
                Intent intent1=new Intent(this, SettingNewActivity.class);
                startActivity(intent1);
                break;
            case R.id.feedback:
//                Toast.makeText(IndexActivity.this,"该功能正在研发中，敬请期待！",Toast.LENGTH_SHORT).show();
                getFeedBackDataFromWeb();
                break;
            case R.id.about:
//                Toast.makeText(IndexActivity.this,"该功能正在研发中，敬请期待！",Toast.LENGTH_SHORT).show();
                Intent about =new Intent(this, AboutActivity.class);
                startActivity(about);
                break;
            case R.id.back_login:
                Intent i =new Intent(IndexActivity.this,LoginActivity.class);
                i.putExtra("deletePassword",1);
                startActivity(i);
                break;
            case R.id.file_pdf:
                Intent filepdf =new Intent(IndexActivity.this,FilePdfActivity.class);
                startActivity(filepdf);
                break;
            case R.id.index_report:
                Intent intentReport =new Intent(IndexActivity.this,ReportViewActivity.class);
                startActivity(intentReport);
                break;
            case R.id.user_icon:
                Intent intentUserinfo =new Intent(IndexActivity.this,UserInfoActivity.class);
                startActivity(intentUserinfo);
                break;
            case R.id.user_menu_icon:
                Intent intentUserinfo1 =new Intent(IndexActivity.this,UserInfoActivity.class);
                startActivity(intentUserinfo1);
                break;
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;

        }
        return false;

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        UserInfo userInfo = new UserInfo();
        userInfo.setYhid(userId);
        List<UserInfo> userList = sympUserInfoDao.queryUserInfoList(userInfo);
        user_name.setText(userList.get(0).getXm());
        user_onlineName.setText(userList.get(0).getXm());
        String userId=userList.get(0).getYhid()==null?"":userList.get(0).getYhid();
        if(!Constants.getUsericon(context).equals("")){
            String imagepath=Constants.getUsericon(context);
            user_icon.setImageURI(Uri.parse(imagepath));
            user_online.setImageURI(Uri.parse(imagepath));
        }else{
            int userhead= Constants.userHead(userId);
            user_icon.setImageResource(userhead);
            user_online.setImageResource(userhead);
        }
    }

    /**
     * 反馈主题表新增数据
     */
    private TrFeedBackTheme insertFeedBackTheme(){
        TrFeedBackThemeDao trFeedBackThemeDao = new TrFeedBackThemeDao(this);
        List<TrFeedBackTheme> list = new ArrayList<TrFeedBackTheme>();
        TrFeedBackTheme trFeedBackTheme = new TrFeedBackTheme();
        trFeedBackTheme.setId(Constants.getUuid());
        trFeedBackTheme.setCreatepersonid(userId);
        trFeedBackTheme.setCreatetime(Constants.dateformat2.format(new Date()));
        trFeedBackTheme.setThemestate("0");
        trFeedBackTheme.setThemetitle("意见及建议");
        list.add(trFeedBackTheme);
        trFeedBackThemeDao.insertListData(list);

        //发送消息
        SympMessageRealDao sympMessageRealDao = new SympMessageRealDao(this);
        List<List<Object>> list1 = new ArrayList<List<Object>>();
        List<Object> list2 = new ArrayList<Object>();
        list2.add(trFeedBackTheme);
        list1.add(list2);
        sympMessageRealDao.addTextMessages(list1);
        return trFeedBackTheme;
    }

    /**
     * 根据创建人id查表
     * @param createPersonId
     */
    private TrFeedBackTheme queryFeedBackThemeByCreatePersonId(String createPersonId){
        TrFeedBackThemeDao trFeedBackThemeDao = new TrFeedBackThemeDao(this);
        TrFeedBackTheme trFeedBackTheme = new TrFeedBackTheme();
        trFeedBackTheme.setCreatepersonid(createPersonId);
        List<TrFeedBackTheme> list = trFeedBackThemeDao.queryTrFeedBackThemeList(trFeedBackTheme);
        if(list.size() > 0){
            TrFeedBackTheme theme = list.get(0);
            return theme;
        }
        return null;
    }

    /**
     * 向平台请求新数据,判断是否已有反馈主题
     */
    private void getFeedBackDataFromWeb(){
        final Map<String, Object> condMap = Constants.getRequestParam(this,"FEEDBACK",null,userId);
        final Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                TrFeedBackTheme trFeedBackTheme = queryFeedBackThemeByCreatePersonId(userId);
                Intent intent = new Intent(context,TrFeedBackReplyActivity.class);
                Bundle bundle=new Bundle();
                //如果没有主题就创建
                if(trFeedBackTheme == null){
                    TrFeedBackTheme theme = insertFeedBackTheme();
                    bundle.putSerializable("theme",theme);
                }else{
                    bundle.putSerializable("theme",trFeedBackTheme);
                }
                intent.putExtras(bundle);
                startActivity(intent);
            }
        };
        new DataSyschronized(this).getDataFromWeb(condMap, handler);
    }

}
