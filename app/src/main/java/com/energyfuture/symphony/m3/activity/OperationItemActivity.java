package com.energyfuture.symphony.m3.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.CardView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.energyfuture.symphony.m3.adapter.IndexAdapter;
import com.energyfuture.symphony.m3.adapter.OperationItemAdaper;
import com.energyfuture.symphony.m3.adapter.OperationItemAdaperOne;
import com.energyfuture.symphony.m3.analysis.DataSyschronized;
import com.energyfuture.symphony.m3.common.ApiClient;
import com.energyfuture.symphony.m3.common.Base;
import com.energyfuture.symphony.m3.common.GetPicFromWifiSDScard;
import com.energyfuture.symphony.m3.common.URLs;
import com.energyfuture.symphony.m3.dao.SympMessageRealDao;
import com.energyfuture.symphony.m3.dao.TrDetectionTempletFileObjDao;
import com.energyfuture.symphony.m3.dao.TrDetectionTypeTempletObjDao;
import com.energyfuture.symphony.m3.dao.TrProjectDao;
import com.energyfuture.symphony.m3.dao.TrSpecialBushingFileDao;
import com.energyfuture.symphony.m3.domain.SympMessageReal;
import com.energyfuture.symphony.m3.domain.TrDetectiontempletFileObj;
import com.energyfuture.symphony.m3.domain.TrDetectiontypEtempletObj;
import com.energyfuture.symphony.m3.domain.TrProject;
import com.energyfuture.symphony.m3.domain.TrSpecialBushingFile;
import com.energyfuture.symphony.m3.domain.TrTask;
import com.energyfuture.symphony.m3.piclook.ImagePagerActivity;
import com.energyfuture.symphony.m3.ui.MaterialDialog;
import com.energyfuture.symphony.m3.util.BitmapUtils;
import com.energyfuture.symphony.m3.util.Constants;
import com.energyfuture.symphony.m3.util.DBHelper;
import com.energyfuture.symphony.m3.util.ImageLoaderHelper;
import com.energyfuture.symphony.m3.util.Network;
import com.energyfuture.symphony.m3.wifi.util.WifiAdmin;
import com.energyfuture.symphony.m3.wifi.util.WifiAdmin2;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OperationItemActivity extends ActionBarActivity implements OperationItemAdaper.MyItemClickListener,OperationItemAdaperOne.MyItemClickListener,View.OnClickListener{

    private Context context = OperationItemActivity.this;
    private DBHelper dbHelper = new DBHelper(context);
    private TrDetectionTypeTempletObjDao trDetectionTypeTempletObjDao;
    private LinearLayout toolbar_back,toolbar_wifi;
    private ImageView toolbar_head_image,back;
    private TextView toolbar_news_show,index_button;
    private ExpandableListView job_list,select_condition;
    private OperationItemAdaper adaper; //列表的adapter
    private IndexAdapter indexAdapter; //索引的adapter
    private ProgressBar progressBar_job;
    private int flag = 0;//0表示默认隐藏，1表示展开
    private List<TrDetectiontypEtempletObj> trDetectiontypEtempletObjList = new ArrayList<TrDetectiontypEtempletObj>();
    private DrawerLayout mDrawerLayout;
    private String rid;
    private DecimalFormat df = new DecimalFormat("####.0000");
    private String detectionname;
    private TextView task_name,substation_name,task_group,task_state,toolbar_name;
    private FrameLayout frameLayout;
    private TrTask trTask;
    private List<TrProject> list;
    private GetPicFromWifiSDScard getPicFromWifiSDScard;
    private String pic_route;//图片存储路径
    private final String pathTemp = Environment.getExternalStorageDirectory()
            .getAbsolutePath();
    private final String FILE_SAVEPATH = pathTemp.substring(0,
            pathTemp.length() - 1)
            + "legacy" + "/M3/transformer/picture/";
    private File file = new File(FILE_SAVEPATH);
    private List<List<TrDetectiontypEtempletObj>> indexlist;

    private int PHOTO_REQUEST_CAMERA=1; //拍照启动Activity的请求码
    private int PHOTO_REQUEST_GALLERY=2; //启动图库的请求码
    private ImageView imageView,task_type;  //用来显示图片的控件
    private EditText imagename;  //用来显示图号的控件
    private TextView hind;
    private RelativeLayout imageshow;
    private LinearLayout imageViewCamera;
//    private String photopath; //图片保存的路径
    private String sphotopath; //图片保存的路径
    private String ophotopath; //图片保存的路径
    private String name;  //图片的名字
    private String picType;

    private int lastclick=-1; //上次点击的第一级的位置
    private ImageView lastview; //上次显示图标的imageview
    private RelativeLayout lastrelativeLayout; //上次显示颜色的relativeLayout
    private LinearLayout lastdellayput; //上次显示的删除图标
    private String id; //从采集卡传过来的菜单的ID
    private String useid; //当前登录人
    private SympMessageRealDao sympMessageRealDao = new SympMessageRealDao(context);
    ImageLoaderHelper imageLoaderHelper;
    MaterialDialog mMaterialDialog;
    private Map<String,String> idMap;
    private int firstgroupPosition,firstchildPosition;

    public static Map<Integer,View> map=new HashMap<>();
    public static Map<String,View> viewmap=new HashMap<>(); //存放第二级采集卡
    private PopupWindow window;
    private RelativeLayout relativeLayout,toolbar_head;
    private LinearLayout layout,wifi_hint,toolbar_camera;
    private ImageView toolbar_tool_image,toolbar_camera_image;

    private MaterialDialog waitdialog;
    private Switch toolbar_switch;
    public Handler wifihandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.job_content_elv);

        imageLoaderHelper = new ImageLoaderHelper(context);

        initView();

        setNetConnect();

    }

    /**
     * 设置SD卡网络连接状态
     */
    private void setNetConnect(){
        Runnable runnableNet = new Runnable(){
            @Override
            public void run() {
                //判断网络连接状态 连接
                if(!Network.isconnectFlashairWifi(context)){
                    wifihandler.sendEmptyMessage(1);
                }else{ //断开
                    wifihandler.sendEmptyMessage(0);
                }
                wifihandler.postDelayed(this, 2000);
            }
        };
        wifihandler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what==1){
                    wifi_hint.setVisibility(View.GONE);
                    toolbar_tool_image.setImageResource(R.drawable.wifion_green_24dp);
                }else if(msg.what == 0){
                    if(Constants.state==1||detectionname.equals("红外测温")) {
                        wifi_hint.setVisibility(View.VISIBLE);
                    }else{
                        wifi_hint.setVisibility(View.GONE);
                    }
                    toolbar_tool_image.setImageResource(R.drawable.wifioff24dp);
                }
            }
        };
        wifihandler.post(runnableNet);

    }

    /**
     * 获取图片配置
     */
    private void picConfigure(){
        //通过wifi获取图片
        if(file.exists())
        {
            File projectDir=new File(FILE_SAVEPATH+trTask.getProjectid()+"/");
            if(!projectDir.exists())
            {
                projectDir.mkdirs();
            }
            pic_route= FILE_SAVEPATH+trTask.getProjectid()+"/"+trTask.getTaskid()+"/";
            File taskDir=new File(pic_route);
            if(!taskDir.exists())
            {
                taskDir.mkdirs();
            }
        }
        SharedPreferences M3_SETTNG = this.getSharedPreferences("M3_SETTNG", getApplicationContext().MODE_PRIVATE);
        String sd_route  = M3_SETTNG.getString("rh_sd_route","");//sd卡路径
        String file_prefix  = M3_SETTNG.getString("rh_file_prefix","");//前缀
        String file_postfix  = M3_SETTNG.getString("rh_file_postfix","");//后缀
        getPicFromWifiSDScard=new GetPicFromWifiSDScard(context,sd_route,file_prefix,file_postfix,pic_route,trTask);

        //判断是否第一次进入
        /*SharedPreferences wifiSharedPreferences = context.getSharedPreferences("wifi", context.getApplicationContext().MODE_PRIVATE);
        Utils.wificount = wifiSharedPreferences.getString("count","0");
        if(!("1").equals(Utils.wificount) && ("2").equals(trTask.getTaskstate())){
            //打开sd卡WiFi列表
            Constants.openWifi(context,trTask);
        }*/
    }

    /**
     * 查询检测项目与检测模板的关联关系实体表
     * @return
     */
    private List<TrDetectiontypEtempletObj> queryTrDetectiontypEtempletObj(String rid){
        List<TrDetectiontypEtempletObj> trDetectiontypEtempletObjs = new ArrayList<TrDetectiontypEtempletObj>();
        TrDetectiontypEtempletObj trDetectiontypEtempletObj = new TrDetectiontypEtempletObj();
        trDetectiontypEtempletObj.setRid(rid);
        trDetectiontypEtempletObj.setTaskid(trTask.getTaskid());
        trDetectionTypeTempletObjDao = new TrDetectionTypeTempletObjDao(context);
        trDetectiontypEtempletObjs = trDetectionTypeTempletObjDao.queryTrDetectionTypeTempletObjList(trDetectiontypEtempletObj);
        return trDetectiontypEtempletObjs;
    }

    private void initView(){
        trTask = (TrTask)getIntent().getSerializableExtra("trTask");
        //父id
        rid  = (String) getIntent().getExtras().get("rid");
        detectionname = (String ) getIntent().getExtras().get("detectionname");
        TrProject trProject=new TrProject();
        trProject.setProjectid(trTask.getProjectid());
        TrProjectDao trProjectDao=new TrProjectDao(context);
        list = trProjectDao.queryTrProjectsList(trProject);

        CardView include= (CardView) findViewById(R.id.job_row_name);

        toolbar_wifi= (LinearLayout) findViewById(R.id.toolbar_wifi);
        task_type= (ImageView) include.findViewById(R.id.task_receive_type);
        task_name= (TextView) include.findViewById(R.id.task_receive_name);
        substation_name= (TextView) include.findViewById(R.id.substation_name);
        task_group= (TextView) include.findViewById(R.id.tack_group);
        task_state= (TextView) include.findViewById(R.id.task_receive_state);
        toolbar_back = (LinearLayout)findViewById(R.id.toolbar_back);
        toolbar_head_image = (ImageView)findViewById(R.id.toolbar_head_image);
        relativeLayout= (RelativeLayout) findViewById(R.id.relativeLayout);
        toolbar_head= (RelativeLayout) findViewById(R.id.toolbar_head);
        layout= (LinearLayout) findViewById(R.id.background);

        wifi_hint= (LinearLayout) findViewById(R.id.wifi_hint);
        toolbar_camera= (LinearLayout) findViewById(R.id.toolbar_camera);
        toolbar_tool_image= (ImageView) findViewById(R.id.toolbar_tool_image);
        toolbar_camera_image= (ImageView) findViewById(R.id.toolbar_camera_image);
        toolbar_switch= (Switch) findViewById(R.id.toolbar_switch);

        /*toolbar_news_show = (TextView)findViewById(R.id.toolbar_news_show);*/
        toolbar_name = (TextView)findViewById(R.id.toolbar_name);
        toolbar_name.setText(detectionname);
        toolbar_wifi.setOnClickListener(this);
        toolbar_wifi.setEnabled(false);

        if(!detectionname.equals("红外测温")){
            toolbar_camera.setVisibility(View.VISIBLE);
            toolbar_switch.setVisibility(View.VISIBLE);
            toolbar_tool_image.setImageAlpha(50);
        }else{
            toolbar_wifi.setEnabled(true);
        }

        //wifi断开
        if(Network.isconnectFlashairWifi(context)){
            if(Constants.state==1||detectionname.equals("红外测温")){
                wifi_hint.setVisibility(View.VISIBLE);
            }else{
                wifi_hint.setVisibility(View.GONE);
            }
        }else{//WiFi连接
            toolbar_tool_image.setImageResource(R.drawable.wifion_green_24dp);
            wifi_hint.setVisibility(View.GONE);
        }

        toolbar_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    Constants.state=1; //wifi获取
                    toolbar_camera_image.setImageAlpha(50);
                    toolbar_tool_image.setImageAlpha(255);
                    if(Network.isconnectFlashairWifi(context)){
                        wifi_hint.setVisibility(View.VISIBLE);
                    }
                    toolbar_wifi.setEnabled(true);
                }else{
                    Constants.state=2; //拍照
                    toolbar_camera_image.setImageAlpha(255);
                    toolbar_tool_image.setImageAlpha(50);
                    wifi_hint.setVisibility(View.GONE);
                    if(!Constants.wifiid.equals("")){
                        Network.closeWIFI(context);
                    }else{
                        if(!Network.isconnectFlashairWifi(context)){
                            WifiAdmin mWifiAdmin = new WifiAdmin(context);
                            String networkid=Network.getWifiId(context);
                            mWifiAdmin.disConnectionWifi(Integer.parseInt(networkid));
                        }
                    }
                    toolbar_wifi.setEnabled(false);
                }
            }
        });

        if(trTask.getTaskstate().equals("1")||trTask.getTaskstate().equals("2")){
            toolbar_wifi.setVisibility(View.VISIBLE);
        }else{
            toolbar_camera.setVisibility(View.GONE);
            toolbar_switch.setVisibility(View.GONE);
            toolbar_wifi.setVisibility(View.GONE);
        }

        job_list = (ExpandableListView) findViewById(R.id.job_list);
        progressBar_job = (ProgressBar)findViewById(R.id.progressBar_job);
        index_button = (TextView)findViewById(R.id.index_button);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.job_content_drawer);
        select_condition = (ExpandableListView) findViewById(R.id.select_condition);
        back = (ImageView)findViewById(R.id.back);

        progressBar_job.setVisibility(View.GONE);
//        refreshLayout= (SwipeRefreshLayout) findViewById(R.id.job_swipe);
//        refreshLayout.setColorSchemeColors(Color.parseColor("#0288D1"));

        frameLayout= (FrameLayout) include.findViewById(R.id.receive_progressBar_FrameLayout);
        task_name.setText(trTask.getTaskname());
        substation_name.setText(Constants.getStationName(context, list));
        task_group.setText(Constants.getGroupName(context,list));
        Constants.setState(trTask.getTaskstate(),frameLayout,task_type,task_state);

        //获取二级菜单的数据
        trDetectiontypEtempletObjList = queryTrDetectiontypEtempletObj(rid);

        //获取三级菜单数据
        final List<List<TrDetectiontypEtempletObj>> list=new ArrayList<List<TrDetectiontypEtempletObj>>();
        for(int i=0;i<trDetectiontypEtempletObjList.size();i++){
            String rid=trDetectiontypEtempletObjList.get(i).getRid();
            TrDetectiontypEtempletObj trDetectiontypEtempletObj = new TrDetectiontypEtempletObj();
            trDetectiontypEtempletObj.setPid(rid);
            trDetectiontypEtempletObj.setTaskid(trTask.getTaskid());
            List<TrDetectiontypEtempletObj> data = trDetectionTypeTempletObjDao.queryTrDetectionTypeTempletObjList(trDetectiontypEtempletObj);
            if(i == 1){ //超声波定位/变压器本体 下的第一个采集卡是图片
                if(data != null && data.size() > 0){
                    TrDetectiontypEtempletObj obj  = data.get(0);
                    if(obj.getDetectionname().equals("高压侧")){
                        data.add(0,obj);
                        list.add(data);
                    }else{
                        list.add(data);
                    }
                }
            }else{
                list.add(data);
            }
        }

        //索引二级菜单数据
        indexlist=new ArrayList<List<TrDetectiontypEtempletObj>>();
        for(int i=0;i<trDetectiontypEtempletObjList.size();i++){
            String rid=trDetectiontypEtempletObjList.get(i).getRid();
            TrDetectiontypEtempletObj trDetectiontypEtempletObj = new TrDetectiontypEtempletObj();
            trDetectiontypEtempletObj.setPid(rid);
            trDetectiontypEtempletObj.setTemplettype("index");
            trDetectiontypEtempletObj.setTaskid(trTask.getTaskid());
            List<TrDetectiontypEtempletObj> data = trDetectionTypeTempletObjDao.queryTrDetectionTypeTempletObjList(trDetectiontypEtempletObj);
            indexlist.add(data);
        }

        final Handler handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what==1){
                        View view = viewmap.get(firstgroupPosition + "|" + firstchildPosition);
                        if(view!=null){
                            LinearLayout relativeLayout= (LinearLayout) view.findViewById(R.id.job_layout);
                            relativeLayout.performClick();  //点击事件
                    }
                }
            }
        };

        Constants.sechandler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                adaper.notifyDataSetChanged();
            }
        };

        picConfigure();

        adaper=new OperationItemAdaper(handler,trDetectiontypEtempletObjList,list,context,pic_route,trTask,detectionname,job_list);
        job_list.setAdapter(adaper);
        adaper.setOnItemClickListener(this);

        //点击第二层展开
        job_list.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                List<TrDetectiontypEtempletObj> datalist = list.get(groupPosition);
                for(int i=0;i<datalist.size();i++){
                    TrDetectiontypEtempletObj obj = datalist.get(i);
                    List<TrDetectiontypEtempletObj> data = getData(obj);
                    if(data.size()!=0){
                        return;
                    }
                }

                createwaitDialog();
                //查询模板类型
                TrDetectiontypEtempletObj trDetectiontypEtempletObj = (TrDetectiontypEtempletObj) adaper.getGroup(groupPosition);
                String objid = trDetectiontypEtempletObj.getId();
                String rid = trDetectiontypEtempletObj.getRid();
                //查询模板类型
                TrDetectionTypeTempletObjDao objDao = new TrDetectionTypeTempletObjDao(context);
                TrDetectiontypEtempletObj objParam = new TrDetectiontypEtempletObj();
                objParam.setPid(rid);
                final List<TrDetectiontypEtempletObj> list = objDao.queryTrDetectionTypeTempletObjList(objParam);


                if (list != null && list.size() > 0) {
                    TrDetectiontypEtempletObj objdata = list.get(0);

                    if (objdata.getTemplettype().equals("general")) {
                        //普通模板
                        final Map<String, Object> condMap = Constants.getRequestParam(context, "INSPINFO", "GENERAL", objid + "@@" + rid + "@@" + trTask.getTaskid());
                        final Handler handler = new Handler() {
                            @Override
                            public void handleMessage(Message msg) {
//                                if (msg.what == 1 && msg.obj != null) {
//                                    adaper.notifyDataSetChanged();
//                                }
                                if(waitdialog!=null){
                                    waitdialog.dismiss();
                                }
                            }
                        };
                        new DataSyschronized(context).getDataFromWeb(condMap, handler);
                    }
                    if (objdata.getTemplettype().equals("bushing")) {
                        //套管模板
                        final Map<String, Object> condMap = Constants.getRequestParam(context, "INSPINFO", "BUSHING", objid + "@@" + rid + "@@" + trTask.getTaskid());
                        final Handler handler = new Handler() {
                            @Override
                            public void handleMessage(Message msg) {
                                waitdialog.dismiss();
                            }
                        };
                        new DataSyschronized(context).getDataFromWeb(condMap, handler);
                    }
                    if (objdata.getTemplettype().equals("oli")) {
                        //油色谱模板
                        final Map<String, Object> condMap = Constants.getRequestParam(context, "INSPINFO", "OLI", objid + "@@" + rid + "@@" + trTask.getTaskid());
                        final Handler handler = new Handler() {
                            @Override
                            public void handleMessage(Message msg) {
                                waitdialog.dismiss();
                            }
                        };
                        new DataSyschronized(context).getDataFromWeb(condMap, handler);
                    }
                    if (objdata.getTemplettype().equals("work")) {
                        //作业情况模板
                        final Map<String, Object> condMap = Constants.getRequestParam(context, "INSPINFO", "WORK", objid + "@@" + rid + "@@" + trTask.getTaskid());
                        final Handler handler = new Handler() {
                            @Override
                            public void handleMessage(Message msg) {
                                waitdialog.dismiss();
                            }
                        };
                        new DataSyschronized(context).getDataFromWeb(condMap, handler);
                    }
                    if (objdata.getTemplettype().equals("ultrasonic")) {
                        //超声波模板
                        final Map<String, Object> condMap = Constants.getRequestParam(context, "INSPINFO", "ULTRASONIC", objid + "@@" + rid + "@@" + trTask.getTaskid());
                        final Handler handler = new Handler() {
                            @Override
                            public void handleMessage(Message msg) {
                                waitdialog.dismiss();
                            }
                        };
                        new DataSyschronized(context).getDataFromWeb(condMap, handler);
                    }
                }
            }
        });


        //第二层点击事件
        job_list.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, final int groupPosition, long id) {
                RelativeLayout joblayout= (RelativeLayout) v.findViewById(R.id.job_layout);
                //点击关闭其它项
                ImageView view= (ImageView) v.findViewById(R.id.job_coutent_pic2);
                LinearLayout dellayout= (LinearLayout) v.findViewById(R.id.job_content_del);
                if(lastclick==-1){
                    job_list.expandGroup(groupPosition);
                    view.setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp);
                    joblayout.setBackgroundColor(Color.rgb(221,221,220)); //展开后的颜色
                    job_list.setSelectedGroup(groupPosition);
                }
                if(lastclick!=-1&&lastclick!=groupPosition){
                    job_list.collapseGroup(lastclick);
                    lastdellayput.setVisibility(View.INVISIBLE);
                    if(lastview!=null&&lastrelativeLayout!=null){
                        lastview.setImageResource(R.drawable.ic_keyboard_arrow_down_grey600_24dp);
                        lastrelativeLayout.setBackgroundColor(Color.parseColor("#ECECEC")); //收起后的颜色
                    }
                    job_list.expandGroup(groupPosition);
                    view.setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp);
                    joblayout.setBackgroundColor(Color.rgb(221,221,220)); //展开后的颜色
                    job_list.setSelectedGroup(groupPosition);
                }
                if(lastclick==groupPosition){
                    if(job_list.isGroupExpanded(groupPosition)){
                        job_list.collapseGroup(groupPosition);
                        adaper.flag=true;
                        viewmap.clear();
                        firstgroupPosition=-1;
                        firstchildPosition=-1;
                        view.setImageResource(R.drawable.ic_keyboard_arrow_down_grey600_24dp);
                        joblayout.setBackgroundColor(Color.parseColor("#ECECEC")); //收起后的颜色

                        dellayout.setVisibility(View.INVISIBLE);
                    }else{
                        job_list.expandGroup(groupPosition);
                        view.setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp);
                        joblayout.setBackgroundColor(Color.rgb(221,221,220)); //展开后的颜色
                        job_list.setSelectedGroup(groupPosition);
                    }
                }
                lastclick=groupPosition;
                lastview=view;
                lastrelativeLayout=joblayout;
                lastdellayput=dellayout;
                return true;
            }
        });

        progressBar_job.setVisibility(View.GONE);

        //索引
        index_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(Gravity.RIGHT);
            }
        });
        indexAdapter=new IndexAdapter(trDetectiontypEtempletObjList,indexlist,context,select_condition);
        select_condition.setAdapter(indexAdapter);
        select_condition.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                View view=map.get(groupPosition);
                RelativeLayout joblayout= (RelativeLayout) view.findViewById(R.id.job_layout);
                //点击关闭其它项
                ImageView imageview= (ImageView) view.findViewById(R.id.job_coutent_pic2);
                if(lastclick==-1){
                    job_list.expandGroup(groupPosition);
                    imageview.setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp);
                    joblayout.setBackgroundColor(Color.rgb(221,221,220)); //展开后的颜色
                    job_list.setSelectedGroup(groupPosition);
                }
                if(lastclick!=-1&&lastclick!=groupPosition){
                    job_list.collapseGroup(lastclick);
                    if(lastview!=null&&lastrelativeLayout!=null){
                        lastview.setImageResource(R.drawable.ic_keyboard_arrow_down_grey600_24dp);
                        lastrelativeLayout.setBackgroundColor(Color.parseColor("#ECECEC")); //收起后的颜色
                    }
                    job_list.expandGroup(groupPosition);
                    imageview.setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp);
                    joblayout.setBackgroundColor(Color.rgb(221,221,220)); //展开后的颜色
                    job_list.setSelectedGroup(groupPosition);
                }
                if(lastclick==groupPosition){
                    if(job_list.isGroupExpanded(groupPosition)){
                        job_list.collapseGroup(groupPosition);
                        adaper.flag=true;
                        viewmap.clear();
                        firstgroupPosition=-1;
                        firstchildPosition=-1;
                        imageview.setImageResource(R.drawable.ic_keyboard_arrow_down_grey600_24dp);
                        joblayout.setBackgroundColor(Color.parseColor("#ECECEC")); //收起后的颜色
                    }else{
                        job_list.expandGroup(groupPosition);
                        imageview.setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp);
                        joblayout.setBackgroundColor(Color.rgb(221,221,220)); //展开后的颜色
                        job_list.setSelectedGroup(groupPosition);
                    }
                }
                lastclick=groupPosition;
                lastview=imageview;
                lastrelativeLayout=joblayout;
                mDrawerLayout.closeDrawer(Gravity.RIGHT);
                return false;
            }
        });
        select_condition.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                firstgroupPosition=groupPosition;
                firstchildPosition=childPosition;

                if(lastclick==-1){
                    job_list.expandGroup(groupPosition);
                }
                if(lastclick!=-1&&lastclick!=groupPosition){
                    job_list.collapseGroup(lastclick);
                    job_list.expandGroup(groupPosition);
                }
                if(lastclick==groupPosition){
                    if(!job_list.isGroupExpanded(groupPosition)){
                        job_list.expandGroup(groupPosition);
                    }
                }
                lastclick=groupPosition;
                View view = viewmap.get(groupPosition + "|" + childPosition);
                if(view!=null){
                    LinearLayout relativeLayout= (LinearLayout) view.findViewById(R.id.job_layout);
                    relativeLayout.performClick();
                }
                mDrawerLayout.closeDrawer(Gravity.RIGHT);
                return false;
            }
        });

        menuClick();
        useid = Constants.getLoginPerson(this).get("userId");
        if(!Constants.getUsericon(context).equals("")){
            String imagepath=Constants.getUsericon(context);
            toolbar_head_image.setImageURI(Uri.parse(imagepath));
        }else{
            int userhead= Constants.userHead(useid);
            toolbar_head_image.setImageResource(userhead);
        }
        /*toolbar_news_show.bringToFront();*/

        toolbar_head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(window==null){
                    window=Constants.createPopupWindow(useid,OperationItemActivity.this,layout);
                }
                if(!window.isShowing()){
                    window.showAtLocation(relativeLayout,Gravity.TOP|Gravity.RIGHT,20,140);
                    if(layout.getVisibility()==View.GONE){
                        layout.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(layout.getVisibility()==View.VISIBLE){
                    layout.setVisibility(View.GONE);
                    window.dismiss();
                    window=null;
                }
            }
        });
    }

    private void menuClick() {
        toolbar_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!Constants.wifiid.equals("")){
                    Network.closeWIFI(context);
                }else{
                    if(!Network.isconnectFlashairWifi(context)){
                        WifiAdmin mWifiAdmin = new WifiAdmin(context);
                        String networkid=Network.getWifiId(context);
                        mWifiAdmin.disConnectionWifi(Integer.parseInt(networkid));
                    }
                }
                onBackPressed();
                return;
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.closeDrawer(Gravity.RIGHT);
            }
        });
    }

    @Override
    public boolean OnSingleClick(View v) {
        int id = v.getId();
        final View viewParent1 = (View) v.getParent();
        final View viewParent2 = (View) viewParent1.getParent();
        switch (id){
            case R.id.general_item_image1:
                LinearLayout imageviwe = (LinearLayout) v;
                final String itemid = (String)imageviwe.getTag(R.id.item_id);
                final String pid = (String)imageviwe.getTag(R.id.pid);
                final int groupPositionid = (int)imageviwe.getTag(R.id.groupPosition_id);
                final int childPositionid = (int)imageviwe.getTag(R.id.childPosition_id);
                mMaterialDialog = new MaterialDialog(context);
                mMaterialDialog.setTitle("确认删除该检测点吗？")
                        .setMessage("")
                        .setPositiveButton(
                                "确认", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Map<Object,Object> columnsMap = new HashMap<Object, Object>();
                                        columnsMap.put("STATUS","2");
                                        Map<Object,Object> wheresMap = new HashMap<Object, Object>();
                                        wheresMap.put("ID", itemid);
                                        trDetectionTypeTempletObjDao.updateTrDetectionTypeTempletObjInfo(columnsMap, wheresMap);
                                        viewParent2.setVisibility(View.GONE);
                                        TrDetectiontypEtempletObj trDetectiontypEtempletObj = new TrDetectiontypEtempletObj();
                                        trDetectiontypEtempletObj.setStatus("2");
                                        trDetectiontypEtempletObj.setId(itemid);
                                        List<List<Object>> list1 = new ArrayList<List<Object>>();
                                        List<Object> list2 = new ArrayList<Object>();
                                        list2.add(trDetectiontypEtempletObj);
                                        list1.add(list2);
                                        sympMessageRealDao.updateTextMessages(list1);
                                        adaper.data2.get(groupPositionid).remove(childPositionid);
                                        adaper.notifyDataSetChanged();
                                        mMaterialDialog.dismiss();
                                        setStatus(trTask.getTaskid(),pid);
                                    }
                                }
                        )
                        .setNegativeButton(
                                "取消", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        mMaterialDialog.dismiss();
                                    }
                                }
                        )
                        .setCanceledOnTouchOutside(false)
                        .setOnDismissListener(
                                new DialogInterface.OnDismissListener() {
                                    @Override
                                    public void onDismiss(DialogInterface dialog) {
                                    }
                                }
                        )
                        .show();
                break;
        }
        EditText editText;
        String fileNumber = "",pid = "";
        ImageView imageViwe;

        String url  = URLs.HTTP+URLs.HOSTA+URLs.IMAGEPATH+trTask.getProjectid() +"/"+trTask.getTaskid()+"/original/";
        Intent intent = new Intent(context, ImagePagerActivity.class);
        intent.putExtra("url",url);
        intent.putExtra("localurl",pic_route+"original");
        intent.putExtra("type","bushing");
        switch (id){
            //外观巡检/套管/油位   点击图片放大
            case R.id.oil_ha_image:
                editText = (EditText) viewParent2.findViewById(R.id.oil_ha_index);
                fileNumber = editText.getText().toString();
                imageViwe = (ImageView) v;
                pid = (String) imageViwe.getTag(R.id.position_id);
                intent.putExtra("id",pid);

                if(!fileNumber.equals("")){
                    if(Constants.isExistPic(pic_route + "small/",fileNumber,context)){
                        context.startActivity(intent);
                    }else{
                        Toast.makeText(context,"暂无照片",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(context, "暂无照片", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.oil_hb_image:
                editText = (EditText) viewParent2.findViewById(R.id.oil_hb_index);
                fileNumber = editText.getText().toString();
                imageViwe = (ImageView) v;
                pid = (String) imageViwe.getTag(R.id.position_id);
                intent.putExtra("id",pid);

                if(!fileNumber.equals("")){
                    if(Constants.isExistPic(pic_route + "small/",fileNumber,context)){
                        context.startActivity(intent);
                    }else{
                        Toast.makeText(context,"暂无照片",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(context, "暂无照片", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.oil_hc_image:
                editText = (EditText) viewParent2.findViewById(R.id.oil_hc_index);
                fileNumber = editText.getText().toString();
                imageViwe = (ImageView) v;
                pid = (String) imageViwe.getTag(R.id.position_id);
                intent.putExtra("id",pid);
                if(!fileNumber.equals("")){
                    if(Constants.isExistPic(pic_route + "small/",fileNumber,context)){
                        context.startActivity(intent);
                    }else{
                        Toast.makeText(context,"暂无照片",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(context, "暂无照片", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.oil_ho_image:
                editText = (EditText) viewParent2.findViewById(R.id.oil_ho_index);
                fileNumber = editText.getText().toString();
                imageViwe = (ImageView) v;
                pid = (String) imageViwe.getTag(R.id.position_id);
                intent.putExtra("id",pid);
                if(!fileNumber.equals("")){
                    if(Constants.isExistPic(pic_route + "small/",fileNumber,context)){
                        context.startActivity(intent);
                    }else{
                        Toast.makeText(context,"暂无照片",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(context, "暂无照片", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.oil_ma_image:
                editText = (EditText) viewParent2.findViewById(R.id.oil_ma_index);
                fileNumber = editText.getText().toString();
                imageViwe = (ImageView) v;
                pid = (String) imageViwe.getTag(R.id.position_id);
                intent.putExtra("id",pid);
                if(!fileNumber.equals("")){
                    if(Constants.isExistPic(pic_route + "small/",fileNumber,context)){
                        context.startActivity(intent);
                    }else{
                        Toast.makeText(context,"暂无照片",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(context, "暂无照片", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.oil_mb_image:
                editText = (EditText) viewParent2.findViewById(R.id.oil_mb_index);
                fileNumber = editText.getText().toString();
                imageViwe = (ImageView) v;
                pid = (String) imageViwe.getTag(R.id.position_id);
                intent.putExtra("id",pid);
                if(!fileNumber.equals("")){
                    if(Constants.isExistPic(pic_route + "small/",fileNumber,context)){
                        context.startActivity(intent);
                    }else{
                        Toast.makeText(context,"暂无照片",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(context, "暂无照片", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.oil_mc_image:
                editText = (EditText) viewParent2.findViewById(R.id.oil_mc_index);
                fileNumber = editText.getText().toString();
                imageViwe = (ImageView) v;
                pid = (String) imageViwe.getTag(R.id.position_id);
                intent.putExtra("id",pid);
                if(!fileNumber.equals("")){
                    if(Constants.isExistPic(pic_route + "small/",fileNumber,context)){
                        context.startActivity(intent);
                    }else{
                        Toast.makeText(context,"暂无照片",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(context, "暂无照片", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.oil_mo_image:
                editText = (EditText) viewParent2.findViewById(R.id.oil_mo_index);
                fileNumber = editText.getText().toString();
                imageViwe = (ImageView) v;
                pid = (String) imageViwe.getTag(R.id.position_id);
                intent.putExtra("id",pid);
                if(!fileNumber.equals("")){
                    if(Constants.isExistPic(pic_route + "small/",fileNumber,context)){
                        context.startActivity(intent);
                    }else{
                        Toast.makeText(context,"暂无照片",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(context, "暂无照片", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.oil_la_image:
                editText = (EditText) viewParent2.findViewById(R.id.oil_la_index);
                fileNumber = editText.getText().toString();
                imageViwe = (ImageView) v;
                pid = (String) imageViwe.getTag(R.id.position_id);
                intent.putExtra("id",pid);
                if(!fileNumber.equals("")){
                    if(Constants.isExistPic(pic_route + "small/",fileNumber,context)){
                        context.startActivity(intent);
                    }else{
                        Toast.makeText(context,"暂无照片",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(context, "暂无照片", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.oil_lb_image:
                editText = (EditText) viewParent2.findViewById(R.id.oil_lb_index);
                fileNumber = editText.getText().toString();
                imageViwe = (ImageView) v;
                pid = (String) imageViwe.getTag(R.id.position_id);
                intent.putExtra("id",pid);
                if(!fileNumber.equals("")){
                    if(Constants.isExistPic(pic_route + "small/",fileNumber,context)){
                        context.startActivity(intent);
                    }else{
                        Toast.makeText(context,"暂无照片",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(context, "暂无照片", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.oil_lc_image:
                editText = (EditText) viewParent2.findViewById(R.id.oil_lc_index);
                fileNumber = editText.getText().toString();
                imageViwe = (ImageView) v;
                pid = (String) imageViwe.getTag(R.id.position_id);
                intent.putExtra("id",pid);
                if(!fileNumber.equals("")){
                    if(Constants.isExistPic(pic_route + "small/",fileNumber,context)){
                        context.startActivity(intent);
                    }else{
                        Toast.makeText(context,"暂无照片",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(context, "暂无照片", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.oil_lo_image:
                editText = (EditText) viewParent2.findViewById(R.id.oil_lo_index);
                fileNumber = editText.getText().toString();
                imageViwe = (ImageView) v;
                pid = (String) imageViwe.getTag(R.id.position_id);
                intent.putExtra("id",pid);
                if(!fileNumber.equals("")){
                    if(Constants.isExistPic(pic_route + "small/",fileNumber,context)){
                        context.startActivity(intent);
                    }else{
                        Toast.makeText(context,"暂无照片",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(context, "暂无照片", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.oil_s1_image:
                editText = (EditText) viewParent2.findViewById(R.id.oil_s1_index);
                fileNumber = editText.getText().toString();
                imageViwe = (ImageView) v;
                pid = (String) imageViwe.getTag(R.id.position_id);
                intent.putExtra("id",pid);
                if(!fileNumber.equals("")){
                    if(Constants.isExistPic(pic_route + "small/",fileNumber,context)){
                        context.startActivity(intent);
                    }else{
                        Toast.makeText(context,"暂无照片",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(context, "暂无照片", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.oil_s2_image:
                editText = (EditText) viewParent2.findViewById(R.id.oil_s2_index);
                fileNumber = editText.getText().toString();
                imageViwe = (ImageView) v;
                pid = (String) imageViwe.getTag(R.id.position_id);
                intent.putExtra("id",pid);
                if(!fileNumber.equals("")){
                    if(Constants.isExistPic(pic_route + "small/",fileNumber,context)){
                        context.startActivity(intent);
                    }else{
                        Toast.makeText(context,"暂无照片",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(context, "暂无照片", Toast.LENGTH_SHORT).show();
                }
        }
        return false;
    }


    /**
     * 双击获取图片
     * @param view
     * @return
     */
    @Override
    public boolean OnDoubleClick(View view) {
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.toolbar_wifi:
                int i = 1;
                if(detectionname.equals("红外测温")){
                    i = 2;
                }
                Constants.openWifi(context,trTask,i);
                break;
        }
    }

    //普通图片获取
    public void imageGet(final TrDetectiontempletFileObj file,final ImageView imageView, final ProgressBar bar){
        if (file.getFilename() != null && !file.getFilename().equals("") && !file.getFilename().equals("null")) {

            File filePic = new File(pic_route + "small/" + file.getFilename());
            if (filePic.exists()) {
                try {
                    imageView.setImageBitmap(Base.revitionImageSize(filePic, 50));
                    imageView.setBackgroundResource(R.drawable.image_square);
                    bar.setVisibility(View.GONE);
                } catch (IOException e) {
                    e.printStackTrace();
                    Constants.recordExceptionInfo(e, context, context.toString());
                }
                imageView.setTag(file.getFilename());//表示已有图片
                //                   添加图片名称     代盼   2015/9/30

            } else {
                final Float totalRxFront = ApiClient.rxBytes(context);
                final String url  = URLs.HTTP+URLs.HOSTA+URLs.IMAGEPATH+trTask.getProjectid() +"/"+trTask.getTaskid()+"/small/"+file.getFilename().toString();
                //显示图片的配置

                ImageLoader.getInstance().displayImage(url, imageView, new ImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String arg0, View arg1) {
                        //开始加载
                        imageView.setImageResource(R.drawable.ic_switch_camera_grey600_36dp);
                        bar.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onLoadingFailed(String arg0, View arg1,
                                                FailReason arg2) {
                        //加载失败
                        imageView.setTag("");//表示暂无图片
                        imageView.setImageResource(R.drawable.ic_switch_camera_grey600_36dp);
                        bar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onLoadingComplete(String arg0, View arg1,
                                                  Bitmap arg2) {
                        //加载成功
                        Base.saveMyBitmap(file.getFilename().toString(), arg2, pic_route + "small");
                        //                                        图片加载成功将名称封装
                        imageView.setTag(file.getFilename());//表示已有图片
                        //保存流量表
                        Float totalRxAfter = ApiClient.rxBytes(context);
                        String totalRx = df.format(totalRxAfter - totalRxFront);
                        ApiClient.download(context, "文件", totalRx);
                        bar.setVisibility(View.GONE);
                        imageView.setBackgroundResource(R.drawable.image_square);
                    }

                    @Override
                    public void onLoadingCancelled(String arg0, View arg1) {
                        //加载取消
                    }
                });
            }
        } else {
            imageView.setTag("");//表示暂无图片
            imageView.setImageResource(R.drawable.ic_switch_camera_grey600_36dp);
            bar.setVisibility(View.GONE);
        }
    }

    //套管图片获取
    public void imageBushingGet(final TrSpecialBushingFile file,final ImageView imageView, final ProgressBar bar){
        if (file.getFilename() != null && !file.getFilename().equals("") && !file.getFilename().equals("null")) {

            File filePic = new File(pic_route + "small/" + file.getFilename());

            if (filePic.exists()) {
                try {
                    imageView.setImageBitmap(Base.revitionImageSize(filePic, 50));
                    imageView.setBackgroundResource(R.drawable.image_square2);
                    bar.setVisibility(View.GONE);
                } catch (IOException e) {
                    e.printStackTrace();
                    Constants.recordExceptionInfo(e, context, context.toString());
                }
                imageView.setTag(file.getFilename());//表示已有图片
                //                   添加图片名称     代盼   2015/9/30

            } else {
                final Float totalRxFront = ApiClient.rxBytes(context);
                final String url  = URLs.HTTP+URLs.HOSTA+URLs.IMAGEPATH+trTask.getProjectid() +"/"+trTask.getTaskid()+"/small/"+file.getFilename().toString();
                //显示图片的配置

                ImageLoader.getInstance().displayImage(url,imageView,new ImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String arg0, View arg1) {
                        //开始加载
                        imageView.setImageResource(R.drawable.ic_switch_camera_grey600_36dp);
                        bar.setVisibility(View.VISIBLE);
                    }
                    @Override
                    public void onLoadingFailed(String arg0, View arg1,
                                                FailReason arg2) {
                        //加载失败
                        imageView.setTag("");//表示暂无图片
                        imageView.setImageResource(R.drawable.ic_switch_camera_grey600_36dp);
                        bar.setVisibility(View.GONE);
                    }
                    @Override
                    public void onLoadingComplete(String arg0, View arg1,
                                                  Bitmap arg2) {
                        //加载成功
                        Base.saveMyBitmap(file.getFilename().toString(), arg2, pic_route + "small");
                        //                                        图片加载成功将名称封装
                        imageView.setTag(file.getFilename());//表示已有图片
                        //保存流量表
                        Float totalRxAfter = ApiClient.rxBytes(context);
                        String totalRx = df.format(totalRxAfter - totalRxFront);
                        ApiClient.download(context,"文件",totalRx);
                        bar.setVisibility(View.GONE);
                        imageView.setBackgroundResource(R.drawable.image_square);
                    }
                    @Override
                    public void onLoadingCancelled(String arg0, View arg1) {
                        //加载取消
                    }
                });
            }
        } else {
            imageView.setTag("");//表示暂无图片
            imageView.setImageResource(R.drawable.ic_switch_camera_grey600_36dp);
            bar.setVisibility(View.GONE);
        }
    }

    //超声波图片获取
    public void imageUltGet(final String filename,final ImageView imageView, final TrDetectiontypEtempletObj obj, final String filenumber){
        //点击图片放大
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file = new File(pic_route + "small/" + filenumber + ".jpg");
                if(!filenumber.equals("")){
                    if(file.exists()){
                        String url  = URLs.HTTP+URLs.HOSTA+URLs.IMAGEPATH+trTask.getProjectid() +"/"+trTask.getTaskid()+"/original/";
                        Intent intent = new Intent(context, ImagePagerActivity.class);
                        intent.putExtra("filenumber", filenumber);
                        intent.putExtra("url",url);
                        intent.putExtra("localurl",pic_route+"original");
                        intent.putExtra("type","general");
                        intent.putExtra("id",obj.getId());
                        context.startActivity(intent);
                    }else{
                        Toast.makeText(context,"暂无照片",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(context,"暂无照片",Toast.LENGTH_SHORT).show();
                }
            }
        });
        if (filename != null && !filename.equals("") && !filename.equals("null")) {
            File filePic = new File(pic_route + "small/" + filename);
            if (filePic.exists()) {
                try {
                    imageView.setImageBitmap(Base.revitionImageSize(filePic, 50));
                    imageView.setBackgroundResource(R.drawable.image_square);
                } catch (IOException e) {
                    e.printStackTrace();
                    Constants.recordExceptionInfo(e, context, context.toString());
                }
                imageView.setTag(filename);//表示已有图片
                //                   添加图片名称     代盼   2015/9/30

            } else {
                final Float totalRxFront = ApiClient.rxBytes(context);
                final String url  = URLs.HTTP+URLs.HOSTA+URLs.IMAGEPATH+trTask.getProjectid() +"/"+trTask.getTaskid()+"/small/"+filename;
                //显示图片的配置

                ImageLoader.getInstance().displayImage(url,imageView,new ImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String arg0, View arg1) {
                        //开始加载
                        imageView.setImageResource(R.drawable.ic_switch_camera_grey600_36dp);
                    }
                    @Override
                    public void onLoadingFailed(String arg0, View arg1,
                                                FailReason arg2) {
                        //加载失败
                        imageView.setTag("");//表示暂无图片
                        imageView.setImageResource(R.drawable.ic_switch_camera_grey600_36dp);
                    }
                    @Override
                    public void onLoadingComplete(String arg0, View arg1,
                                                  Bitmap arg2) {
                        //加载成功
                        Base.saveMyBitmap(filename.toString(), arg2, pic_route + "small");
                        //                                        图片加载成功将名称封装
                        imageView.setTag(filename);//表示已有图片
                        //保存流量表
                        Float totalRxAfter = ApiClient.rxBytes(context);
                        String totalRx = df.format(totalRxAfter - totalRxFront);
                        ApiClient.download(context,"文件",totalRx);
                        imageView.setBackgroundResource(R.drawable.image_square);
                    }
                    @Override
                    public void onLoadingCancelled(String arg0, View arg1) {
                        //加载取消
                    }
                });
            }
        } else {
            imageView.setTag("");//表示暂无图片
            imageView.setImageResource(R.drawable.ic_switch_camera_grey600_36dp);
        }
    }

    public void getImageView(ImageView view){
        imageView=view;
    }

    public void getEditText(EditText view){
        imagename=view;
    }

    public void getTextView(TextView view){
        hind = view;
    }

    public void getRelativelayout(RelativeLayout imageshow) {this.imageshow=imageshow;}

    public void getLinearlayout(LinearLayout imageViewCamera) {this.imageViewCamera=imageViewCamera;}

    public void getType(String type){
        picType = type;
    }

    public void getItemID(String id){this.id=id;}

    public void toTakePhoto() {   //拍照
        if (hasSdcard()) {
            name=new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
            String url = pic_route+"original/";
            sphotopath=pic_route+"small/"+name+".jpg";
            ophotopath=pic_route+"original/"+name+".jpg";
            if(!url.equals("")){
                Base.mardDir(url);
            }
            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
            intent.putExtra(MediaStore.EXTRA_OUTPUT,
                    Uri.fromFile(new File(ophotopath)));
            startActivityForResult(intent, PHOTO_REQUEST_CAMERA);
        }
    }

//    public void toAlbum(){  //从相册获取
//        Intent intent = new Intent(Intent.ACTION_PICK);
//        intent.setType("image/*");
//        startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
//    }

    public static boolean hasSdcard() { //是否有SD卡
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != 0) { //判断是否拍照或者选择图片
            if (requestCode == PHOTO_REQUEST_CAMERA) {  //相机
                try {
                    BitmapUtils.getThumbUploadPath(ophotopath, 550, ophotopath);
                } catch (Exception e) {
                    e.printStackTrace();
                    Constants.recordExceptionInfo(e, context, context.toString());
                }
                Constants.saveFile(ophotopath, sphotopath,pic_route + "small/","1");//保存小图片
                File file = new File(sphotopath);
                Bitmap bitmap = null;
                try {
                    bitmap = Base.revitionImageSize(file, 50);
                } catch (IOException e) {
                    e.printStackTrace();
                    Constants.recordExceptionInfo(e, context, context.toString());
                }

                //存入数据库
                if(("general").equals(picType)){ //普通图片
                    SaveGeneral(name);
                }else if(("bush").equals(picType)){ //套管图片
                    SaveBush(name,0);
                }

                imageView.setImageBitmap(bitmap);
                imageView.setBackgroundResource(R.drawable.image_square);
                imagename.setText(name);

                if(imageshow!=null&&imageshow.getVisibility()==View.GONE){
                    imageshow.setVisibility(View.VISIBLE);
                }
                if(imageViewCamera!=null&&imageViewCamera.getVisibility()==View.VISIBLE){
                    imageViewCamera.setVisibility(View.GONE);
                }
            }
        }else{
            if(("general").equals(picType)){ //普通图片
                Constants.takephotohandler.sendEmptyMessage(0);
            }else if(("bush").equals(picType)){ //套管图片
                SaveBush(name,1);
            }
        }
    }

    /**
     * 保存普通图片
     * @param fileNum
     */
    private void SaveGeneral(String fileNum) {
        if (hind.getText().toString().equals("")) {
            String uuid = Constants.getUuid();
            hind.setText(uuid);
        }
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        TrDetectionTempletFileObjDao fileObjDao = new TrDetectionTempletFileObjDao(context);


        Map<Object, Object> columnsMap = new HashMap<>();
        columnsMap.put("Filenumber", fileNum);
        columnsMap.put("Filename", fileNum + ".jpg");
        columnsMap.put("Updatetime", date);
        columnsMap.put("Updateperson", useid);
        columnsMap.put("PROJECTID", trTask.getProjectid());
        columnsMap.put("TASKID", trTask.getTaskid());
        columnsMap.put("TEMPLETTYPE", "general");
        columnsMap.put("DETECTIONOBJID", id);
        columnsMap.put("TEMPLETNAME", "普通模板");
        columnsMap.put("ISUPLOAD", "0");

        Map<Object, Object> wheresMap = new HashMap<>();
        wheresMap.put("ID", hind.getText().toString());

        fileObjDao.updateTrDetectionTempletFileObjInfo(columnsMap, wheresMap);

        TrDetectiontempletFileObj obj = new TrDetectiontempletFileObj();
        obj.setId(hind.getText().toString());
        obj.setFilenumber(fileNum);
        obj.setFilename(fileNum + ".jpg");
        obj.setUpdatetime(date);
        obj.setUpdateperson(useid);
        obj.setIsupload("0");
        obj.setProjectid(trTask.getProjectid());
        obj.setTaskid(trTask.getTaskid());
        obj.setDetectionobjid(id);
        obj.setTemplettype("general");
        obj.setTempletname("普通模板");
        sendMessageGeneral(obj);
    }

    /**
     * 保存套管图片
     * @param fileNum
     */
    private void SaveBush(String fileNum,int isOK) {
        String picId = (String) imageView.getTag(R.id.pic_id);
        TrSpecialBushingFileDao fileObjDao = new TrSpecialBushingFileDao(context);
        TrSpecialBushingFile obj = new TrSpecialBushingFile();
        if(isOK == 0){
            String positionId = (String) imageView.getTag(R.id.position_id);
            String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

            Map<Object, Object> columnsMap = new HashMap<>();
            columnsMap.put("Filenumber", fileNum);
            columnsMap.put("Filename", fileNum + ".jpg");
            columnsMap.put("Updatetime", date);
            columnsMap.put("Updateperson", useid);
            columnsMap.put("PROJECTID", trTask.getProjectid());
            columnsMap.put("TASKID", trTask.getTaskid());
            //columnsMap.put("POSITIONID",positionId);
            columnsMap.put("ISUPLOAD", "0");

            Map<Object, Object> wheresMap = new HashMap<>();
            wheresMap.put("ID", picId);

            fileObjDao.updateTrSpecialBushingFileInfo(columnsMap, wheresMap);
            obj.setId(picId);
            obj.setFilenumber(fileNum);
            obj.setFilename(fileNum + ".jpg");
            obj.setUpdatetime(date);
            obj.setUpdateperson(useid);
            obj.setIsupload("0");
            obj.setProjectid(trTask.getProjectid());
            obj.setTaskid(trTask.getTaskid());
            obj.setPositionid(positionId);
            sendMessageBush(obj);
        }else{
            Map<Object,Object> columnsMap=new HashMap<>();
            columnsMap.put("ISUPLOAD","2");
            Map<Object,Object> wheresMap=new HashMap<>();
            wheresMap.put("ID",picId);
            fileObjDao.updateTrSpecialBushingFileInfo(columnsMap,wheresMap);

            obj.setId(picId);
            obj.setIsupload("2");
            List<List<Object>> list1 = new ArrayList<List<Object>>();
            List<Object> list2 = new ArrayList<Object>();
            list2.add(obj);
            list1.add(list2);
            sympMessageRealDao.updateTextMessages(list1);
        }

    }

    /**
     * 普通图片消息封装
     * @param obj
     */
    private void sendMessageGeneral(TrDetectiontempletFileObj obj){
        List<List<Object>> list1 = new ArrayList<List<Object>>();
        List<Object> list2 = new ArrayList<Object>();
        list2.add(obj);
        list1.add(list2);
        sympMessageRealDao.updateTextMessages(list1);
        List<SympMessageReal> sympMessageReals = new ArrayList<SympMessageReal>();
        SympMessageReal sympMessageReal = new SympMessageReal();
        sympMessageReal.setPresonid(useid);
        sympMessageReal.setContent(obj.TABLENAME + "," + obj.getId());
        sympMessageReal.setMessagestate("1");
        sympMessageReal.setMessagetype("2");
        sympMessageReal.setCreatedate(obj.getUpdatetime());
        sympMessageReals.add(sympMessageReal);
        sympMessageRealDao.insertListData(sympMessageReals);
    }

    /**
     * 套管图片消息封装
     * @param obj
     */
    private void sendMessageBush(TrSpecialBushingFile obj){
        List<List<Object>> list1 = new ArrayList<List<Object>>();
        List<Object> list2 = new ArrayList<Object>();
        list2.add(obj);
        list1.add(list2);
        sympMessageRealDao.updateTextMessages(list1);
        List<SympMessageReal> sympMessageReals = new ArrayList<SympMessageReal>();
        SympMessageReal sympMessageReal = new SympMessageReal();
        sympMessageReal.setPresonid(useid);
        sympMessageReal.setContent(obj.TABLENAME + "," + obj.getId());
        sympMessageReal.setMessagestate("1");
        sympMessageReal.setMessagetype("2");
        sympMessageReal.setCreatedate(obj.getUpdatetime());
        sympMessageReals.add(sympMessageReal);
        sympMessageRealDao.insertListData(sympMessageReals);
    }
    private void createwaitDialog() {
       /* if(!context.isFinishing()){  //不加判断关闭Avtivity重新打开会出异常*/
            waitdialog = new MaterialDialog(context);
            View v= LayoutInflater.from(context).inflate(R.layout.progressdialog, null);
            TextView text= (TextView) v.findViewById(R.id.text);
            text.setText("正在获取数据，请稍候..");
            waitdialog.setView(v).show();
       /* }*/
    }
    //获取采集卡
    public List<TrDetectiontypEtempletObj> getData(TrDetectiontypEtempletObj trDetectiontypEtempletObj) {
        String id = trDetectiontypEtempletObj.getRid();
        String taskid = trDetectiontypEtempletObj.getTaskid();
        TrDetectiontypEtempletObj trDetectiontypEtempletObj1 = new TrDetectiontypEtempletObj();
        trDetectiontypEtempletObj1.setPid(id);
        trDetectiontypEtempletObj1.setTaskid(taskid);
        TrDetectionTypeTempletObjDao trDetectionTypeTempletObjDao = new TrDetectionTypeTempletObjDao(context);
        List<TrDetectiontypEtempletObj> list = trDetectionTypeTempletObjDao.queryTrDetectionTypeTempletObjList(trDetectiontypEtempletObj1);
        return list;
    }

    public void setStatus(String taskid ,String pid){
        if("0".equals(pid)) return;
        String sqlone = "select * from tr_detectiontyp_etemplet_obj where taskid = '" + taskid + "' and pid = '" + pid + "' and status <> '2'";
        List<Map<String, String>> list = dbHelper.selectSQL(sqlone, null);
        String status = "1";//最终状态，默认为1
        for (int i = 0; i < list.size(); i++) {
            Map<String,String> m = list.get(i);
            String newStatus = m.get("STATUS");
            //如果里面有一个是null，则跳出
            if(newStatus == null || "".equals(newStatus)){
                status = "";
                break;
            }
            //第一次循环，将新状态赋值给最终状态
//            if(status == null) {
//                    status = newStatus;
//                    continue;
//            }
            //如果一个状态为0，则将0赋值为最终状态
            if("0".equals(newStatus)) status = newStatus;
        }
        //获取上级信息，修改上级状态
        String sqltwo = "select * from tr_detectiontyp_etemplet_obj where taskid = '" + taskid + "' and rid = '" + pid + "' and status <> '2'";
        list = dbHelper.selectSQL(sqltwo, null);
        if(list.size() > 0) {
            pid = list.get(0).get("PID");
            String id = list.get(0).get("ID");
            String newStatus = list.get(0).get("STATUS");
            if(!status.equals(newStatus)){
                dbHelper.execSQL("update tr_detectiontyp_etemplet_obj set status = '" + status + "' where id = '" + id + "'");
                TrDetectiontypEtempletObj trDetectiontypEtempletObj1 = new TrDetectiontypEtempletObj();
                trDetectiontypEtempletObj1.setStatus(status);
                trDetectiontypEtempletObj1.setId(id);
                List list1 = new ArrayList<>();
                List<Object> list2 = new ArrayList<Object>();
                list2.add(trDetectiontypEtempletObj1);
                list1.add(list2);
                sympMessageRealDao.updateTextMessages(list1);
            }
            setStatus(taskid, pid);
        }
        System.out.println(list);
    }


    @Override
    protected void onDestroy() {
        if(("红外测温").equals(detectionname)){
            WifiAdmin2 wifiAdmin2 = new WifiAdmin2(context);
            if(Constants.isConnNet()){
                //获取wifi的netid
                int NetId = wifiAdmin2.getConnNetId();
                //根据netid断开zhidingwifi
                wifiAdmin2.disConnectionWifi(NetId);
            }
        }
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        Constants.state = 2;
        toolbar_back.setFocusable(true);
        toolbar_back.setFocusableInTouchMode(true);
        toolbar_back.requestFocus();
        toolbar_back.requestFocusFromTouch();
        finish();
        super.onBackPressed();
    }

    public void inioadapter(int groupPosition,int childPosition){
        if(groupPosition!=-1){
            if(childPosition==-1){
                trDetectiontypEtempletObjList.remove(groupPosition-1);
            }else{
                indexlist.get(groupPosition).remove(childPosition);
            }
            indexAdapter.notifyDataSetChanged();
        }
    }
}
