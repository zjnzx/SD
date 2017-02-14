package com.energyfuture.symphony.m3.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.energyfuture.symphony.m3.adapter.ProjectListAdapter;
import com.energyfuture.symphony.m3.adapter.SelectConditionAdapter;
import com.energyfuture.symphony.m3.analysis.ProjectDataSyschronized;
import com.energyfuture.symphony.m3.common.ApiClient;
import com.energyfuture.symphony.m3.common.URLs;
import com.energyfuture.symphony.m3.dao.SympMessageRealDao;
import com.energyfuture.symphony.m3.dao.TrDepartmentDao;
import com.energyfuture.symphony.m3.dao.TrProjectDao;
import com.energyfuture.symphony.m3.dao.TrStationDao;
import com.energyfuture.symphony.m3.dao.TrTaskDao;
import com.energyfuture.symphony.m3.datepicker.DatePicker;
import com.energyfuture.symphony.m3.domain.Department;
import com.energyfuture.symphony.m3.domain.TrProject;
import com.energyfuture.symphony.m3.domain.TrStation;
import com.energyfuture.symphony.m3.domain.TrTask;
import com.energyfuture.symphony.m3.floatingbutton.FloatingActionButton;
import com.energyfuture.symphony.m3.util.Constants;
import com.energyfuture.symphony.m3.util.ProDownloadDialog;
import com.energyfuture.symphony.m3.util.Utils;
import com.energyfuture.symphony.m3.widget.CustomItemAnimator;

import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import com.energyfuture.symphony.m3.util.UploadHelper;

public class ProjectListActivity extends ActionBarActivity {
    private Context context = ProjectListActivity.this;
    private List<TrProject> applicationList = new ArrayList<TrProject>();
    private ProjectListAdapter mAdapter;
    private SelectConditionAdapter selectConditionAdapter;
    private RecyclerView mRecyclerView;
    private RecyclerView select_condition;
    private TextView none_data;
    public TextView menu_download_text;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ProgressBar mProgressBar;
    private SearchView searchView;
    private ImageView menu_head_image;
    private LinearLayout menu_back,menu_select,addProject,menu_download;
    private RelativeLayout menu_head,relativeLayout;
    private List<TrProject> downloadProList = new ArrayList<TrProject>();
    DrawerLayout mDrawerLayout;
    TrProject trProject=null;
    TextView grade_select_value,grade_select_id,station_select_id,station_select_value,station_start_time,station_end_time,news_show;
    private  LinearLayout child_menu,select_roject_state_LinearLayout,station_start_time_LinearLayout,station_end_time_LinearLayout,station_select_LinearLayout,grade_select_LinearLayout ;
    private Switch  select_roject_state;

    String userId = "";

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    Map<String, Object> condMap = new HashMap<String, Object>();
    private ProgressDialog progressDialog;
    private int finish = 0;
    private int taskcount = 0;
    private Handler handler,handler1,handler2;
    private PopupWindow window;
    private LinearLayout layout;
    private int clickcount;
    private FloatingActionButton addProjectBtn;
    private SympMessageRealDao sympMessageRealDao = new SympMessageRealDao(context);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        trProject = new TrProject();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.project_list);

        Map<String,String> userMap = Constants.getLoginPerson(ProjectListActivity.this);
        userId = userMap.get("userId");

        addProjectBtn = (FloatingActionButton) findViewById(R.id.add_project);
        addProject = (LinearLayout) findViewById(R.id.menu_add_project);
        station_start_time = (TextView) this.findViewById(R.id.station_start_time);
        station_start_time_LinearLayout= (LinearLayout) this.findViewById(R.id.station_start_time_LinearLayout);
        station_end_time = (TextView) this.findViewById(R.id.station_end_time);
        station_end_time_LinearLayout=(LinearLayout) this.findViewById(R.id.station_end_time_LinearLayout);
        select_roject_state = (Switch) findViewById(R.id.select_roject_state);
        select_roject_state_LinearLayout=(LinearLayout) findViewById(R.id.select_roject_state_LinearLayout);
        station_select_id = (TextView) this.findViewById(R.id.station_select_id);
        grade_select_id = (TextView) this.findViewById(R.id.grade_select_id);
        menu_head_image = (ImageView)findViewById(R.id.menu_head_image);
        menu_back = (LinearLayout)findViewById(R.id.menu_back);
        menu_select = (LinearLayout)findViewById(R.id.menu_select);
        menu_head = (RelativeLayout)findViewById(R.id.menu_head);
        menu_download = (LinearLayout)findViewById(R.id.menu_download);
        menu_download_text = (TextView)findViewById(R.id.menu_download_text);
        /*news_show = (TextView)findViewById(R.id.news_show);*/
        relativeLayout= (RelativeLayout) findViewById(R.id.relativeLayout);
        layout= (LinearLayout) findViewById(R.id.background);
        none_data = (TextView) this.findViewById(R.id.none_data);

        // Handle Toolbar
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ProjectListActivity.this.onBackPressed();
//            }
//        });
        // Handle DrawerLayout
        /*news_show.bringToFront();*/
        //标题栏头像展示
        if(!Constants.getUsericon(context).equals("")){
            String imagepath=Constants.getUsericon(context);
            menu_head_image.setImageURI(Uri.parse(imagepath));
        }else{
            int userhead= Constants.userHead(userId);
            menu_head_image.setImageResource(userhead);
        }

        //新增项目
        addProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,ProjectAddActivity.class);

                startActivityForResult(intent, 1);
                //清空集合
                Utils.radioButtonMap.clear();
                Utils.equipmentMap.clear();
            }
        });

        menu_head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {  //点击头像弹框
                if(window==null){
                    window=Constants.createPopupWindow(userId,ProjectListActivity.this,layout);
                }
                if (!window.isShowing()) {
                    window.showAtLocation(relativeLayout, Gravity.TOP | Gravity.RIGHT, 20, 140);
                    if (layout.getVisibility() == View.GONE) {
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

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        select_roject_state.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                // TODO Auto-generated method stub
                if (isChecked) {
                    select_roject_state.setTextOn("开启");//打开蓝牙
                } else {
                    select_roject_state.setTextOff("关闭");//打开蓝牙
                }
            }
        });

        select_roject_state_LinearLayout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(select_roject_state.isChecked())
                {
                    select_roject_state.setChecked(false);
                    trProject.setProjectstate(null);
                }
                else
                {
                    select_roject_state.setChecked(true);
                    trProject.setProjectstate("2");

                }
            }
        });

        //提交筛选条件
        RelativeLayout submit_select_condition = (RelativeLayout) findViewById(R.id.submit_select_condition);
        submit_select_condition.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                trProject.setStationid(station_select_id.getText().toString());
                trProject.setDepartment(grade_select_id.getText().toString());
                trProject.setEndtime(station_end_time.getText().toString());
                trProject.setStarttime(station_start_time.getText().toString());
                trProject.setDepartment(grade_select_id.getText().toString());

              /*  if(select_roject_state.isChecked()) {
                    trProject.setProjectstate("2");
                }
                else
                {
                    trProject.setProjectstate(null);
                }*/
                mDrawerLayout.closeDrawer(Gravity.RIGHT);
//                List<TrProject> liInitializeApplicationsTaskst = queryProjectLists();
                new InitializeApplicationsTask().execute();

            }
        });

        child_menu = (LinearLayout) findViewById(R.id.child_menu);

        ImageButton child_menu_back = (ImageButton) findViewById(R.id.child_menu_back);
        child_menu_back.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mDrawerLayout.closeDrawer(child_menu);

            }
        });
        RelativeLayout father_menu_back = (RelativeLayout) findViewById(R.id.father_menu_back);
        father_menu_back.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mDrawerLayout.closeDrawer(Gravity.RIGHT);

            }
        });

        Date now =new Date();
        String start= sdf.format(now);
        station_start_time.setText(start);
        now.setMonth(now.getMonth()+1);
        String end= sdf.format(now);
        station_end_time.setText(end);
        grade_select_value = (TextView) findViewById(R.id.grade_select_value);
        station_select_value = (TextView) findViewById(R.id.station_select_value);

        station_start_time_LinearLayout.setOnClickListener(new StartDateTimeOnClick());
        station_end_time_LinearLayout.setOnClickListener(new EndDateTimeOnClick());
        //二级筛选条件列表
        select_condition = (RecyclerView) findViewById(R.id.select_condition);
        select_condition.setLayoutManager(new LinearLayoutManager(ProjectListActivity.this));
        //变电站
        station_select_LinearLayout = (LinearLayout) findViewById(R.id.station_select_LinearLayout);
        station_select_LinearLayout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                TextView condition_name = (TextView) findViewById(R.id.condition_name);
                condition_name.setText("变电站");
                List<Map<String,String>> list =new ArrayList<Map<String,String>>();
                TrStationDao trStationDao = new TrStationDao(ProjectListActivity.this);
                List<TrStation> listTrStation=trStationDao.queryTrStationList(null);
                Map<String,String> mapAll=new HashMap<String,String>();
                mapAll.put("select_name","全部");
                mapAll.put("select_id",null);
                list.add(mapAll);
                for(int i=0;i<listTrStation.size();i++) {
                    Map<String,String> map=new HashMap<String,String>();
                    map.put("select_name", listTrStation.get(i).getStationname());
                    map.put("select_id",listTrStation.get(i).getStationid());
                    list.add(map);
                }
                select_condition.setItemAnimator(new CustomItemAnimator());
                selectConditionAdapter = new SelectConditionAdapter(list, R.layout.project_select_condition, ProjectListActivity.this,mDrawerLayout,child_menu,station_select_value,station_select_id);
                select_condition.setAdapter(selectConditionAdapter);
                mDrawerLayout.openDrawer(child_menu);
            }
        });

        //负责班组
        grade_select_LinearLayout = (LinearLayout) findViewById(R.id.grade_select_LinearLayout);
        grade_select_LinearLayout.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                TextView condition_name = (TextView) findViewById(R.id.condition_name);
                condition_name.setText("负责班组");
                List<Map<String,String>>    list =new ArrayList<Map<String,String>>();
                TrDepartmentDao trDepartmentDao = new TrDepartmentDao(ProjectListActivity.this);
                Department department = new Department();
                department.setBmlx("2");
                List<Department> departments = trDepartmentDao.queryDepartmentList(department);
                Map<String,String> mapAll=new HashMap<String,String>();
                mapAll.put("select_name","全部");
                mapAll.put("select_id",null);
                list.add(mapAll);
                for(int i=0;i<departments.size();i++) {
                    Map<String,String> map=new HashMap<String,String>();
                    map.put("select_name", departments.get(i).getBmmc());
                    map.put("select_id",departments.get(i).getBmid());
                    list.add(map);
                }
                select_condition.setItemAnimator(new CustomItemAnimator());
                selectConditionAdapter = new SelectConditionAdapter(list, R.layout.project_select_condition, ProjectListActivity.this,mDrawerLayout,child_menu,grade_select_value,grade_select_id);
                select_condition.setAdapter(selectConditionAdapter);
                mDrawerLayout.openDrawer(child_menu);
            }
        });

        //平台消息发送获取处理
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what==1 && msg.obj != null){
                    Map<String,Object> resMap = (Map<String,Object>)msg.obj;
                    if("ok".equals(resMap.get("0"))) {
                            mRecyclerView.setVisibility(View.VISIBLE);
                            mProgressBar.setVisibility(View.GONE);
                            new InitializeApplicationsTask().execute();

                    }else {
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                }
            }

        };

        handler1 = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                downloadProList = (List<TrProject>)msg.obj;
                menu_download_text.setText(downloadProList.size() + "");
                if(applicationList.size() <= 0){
                    ProDownloadDialog proDownloadDialog = new ProDownloadDialog(ProjectListActivity.this, R.style.FullHeightDialog,downloadProList);
                    proDownloadDialog.show();
                }
            }
        };

        handler2 = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                List<TrTask> trTasks = (List<TrTask>)msg.obj;
                TrTaskDao trTaskDao = new TrTaskDao(context);
                trTaskDao.insertListData(trTasks);
                showTaskProgressDialog(trTasks);
            }
        };
        /**
         * 项目数据同步
         * @autor SongLin Yang
         */
        getDataFromWeb();

        mRecyclerView = (RecyclerView) findViewById(R.id.list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        trProject.setStationid(station_select_id.getText().toString());
        mAdapter = new ProjectListAdapter(new ArrayList<TrProject>(), R.layout.project_list_item, ProjectListActivity.this,none_data);
        mRecyclerView.setAdapter(mAdapter);
        //添加滚动事件
        addProjectBtn.attachToRecyclerView(mRecyclerView);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
//      mSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.theme_accent));
        mSwipeRefreshLayout.setColorSchemeColors(Color.parseColor("#0288D1"));
        mSwipeRefreshLayout.setRefreshing(true);
        //项目下拉刷新
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                /**
                 * 项目数据同步
                 * @autor SongLin Yang
                 */
                getDataFromWeb();
            }
        });
        mProgressBar.setVisibility(View.VISIBLE);
        new InitializeApplicationsTask().execute();
        getProDataFromWeb();
        menuClick();
    }

    private void menuClick() {
        menu_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                return;
            }
        });
        menu_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(Gravity.RIGHT);
                return;
            }
        });
        menu_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProDownloadDialog proDownloadDialog = new ProDownloadDialog(ProjectListActivity.this, R.style.FullHeightDialog,downloadProList);
                proDownloadDialog.show();
            }
        });
    }

    private final class StartDateTimeOnClick implements View.OnClickListener {
        public void onClick(View v) {

            overridePendingTransition(R.anim.dialog_enter, R.anim.dialog_exit);
            DatePicker dataPicker = new DatePicker(ProjectListActivity.this);
            dataPicker.selectDateDialog(station_start_time,1,null,null,1);

        }
    }
    private final class EndDateTimeOnClick implements View.OnClickListener {
        public void onClick(View v) {

            overridePendingTransition(R.anim.dialog_enter, R.anim.dialog_exit);
            DatePicker dataPicker = new DatePicker(ProjectListActivity.this);
            dataPicker.selectDateDialog(station_end_time,1,null,null,1);
        }
    }

    public List<TrProject> queryProjectLists() {
        TrProjectDao trProjectDao = new TrProjectDao(ProjectListActivity.this);
        List<TrProject> list = trProjectDao.queryTrProjectsList(trProject);
        Log.i("Size",list.size()+"");
        return list;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    public void animateActivity(TrProject appInfo) {
        Intent i = new Intent(this, TaskListActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("project",appInfo);
        i.putExtras(bundle);
        startActivityForResult(i,1);
    }

    public void deletePro(TrProject trProject,int position){
        List<TrProject> trProjects = new ArrayList<TrProject>();
        TrTask trTask = new TrTask();
        trTask.setProjectid(trProject.getProjectid());
        trProjects.add(trProject);
        TrProjectDao trProjectDao = new TrProjectDao(context);
        TrTaskDao trTaskDao = new TrTaskDao(context);
        List<TrTask> trTasks = trTaskDao.queryTrTaskList(trTask);
        trTaskDao.deleteListData(trTasks);
        trProjectDao.deleteListData(trProjects);
        mAdapter.applications.remove(position);
        mAdapter.notifyDataSetChanged();
        getProDataFromWeb();
    }

    Handler deleteProHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 1){
                int position = (int)msg.obj;
                List<TrProject> trProjects = new ArrayList<TrProject>();
                TrTask trTask = new TrTask();
                trTask.setProjectid(trProject.getProjectid());
                trProjects.add(trProject);
                TrProjectDao trProjectDao = new TrProjectDao(context);
                TrTaskDao trTaskDao = new TrTaskDao(context);
                List<TrTask> trTasks = trTaskDao.queryTrTaskList(trTask);
                trTaskDao.deleteListData(trTasks);
                trProjectDao.deleteListData(trProjects);
                mAdapter.applications.remove(position);
                mAdapter.notifyDataSetChanged();
                getProDataFromWeb();
                Toast.makeText(context, "删除任务成功！", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(context, "删除任务失败！", Toast.LENGTH_SHORT).show();
            }
        }
    };

    public void deleteProWeb(TrProject trProject,final int position){
        Toast.makeText(context, "正在删除项目...", Toast.LENGTH_SHORT).show();
        final Map<String,Object> dataMap = new HashMap<String,Object>();
        dataMap.put("id",trProject.getProjectid());
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                String http = URLs.HTTP+URLs.HOST+"/"+ "inspectfuture/" +URLs.DELETEPRO;
                Map resultMap = ApiClient.sendHttpPostMessageData(null, http, dataMap, "", context, "文本");
                if(resultMap != null && resultMap.get("ok").equals("true")){
                    Message msg = new Message();
                    msg.obj = position;
                    msg.what = 1;
                    deleteProHandler.sendMessage(msg);
                }else{
                    Message msg = new Message();
                    msg.what = 2;
                    deleteProHandler.sendMessage(msg);
                }
            }
        };
        new Thread(runnable).start();
    }

    public void downloadPro(List<TrProject> projectList,String projectid) throws JSONException{
        applicationList = projectList;
        TrProjectDao trProjectDao = new TrProjectDao(context);
        trProjectDao.insertListData(projectList);
        getProDataFromWeb();
        new ProjectDataSyschronized(ProjectListActivity.this).getProTaskDataFromWeb(handler2,projectid);
    }

    private class InitializeApplicationsTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            mProgressBar.setVisibility(View.VISIBLE);
            mAdapter.clearApplications();
            mAdapter.notifyDataSetChanged();

            if( mAdapter.applications.size()>0)
            {
                none_data.setVisibility(View.INVISIBLE);
            }
            else
            {
                none_data.setVisibility(View.VISIBLE);

            }
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            applicationList.clear();
            //Query the applications
            final Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
            mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
            //获取全部项目信息
            List<TrProject> ril = queryProjectLists();
            for (TrProject ri : ril) {
                applicationList.add(ri);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            //handle visibility
//            mRecyclerView.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.GONE);

            //set data for list
            mAdapter.addApplications(applicationList);
            mSwipeRefreshLayout.setRefreshing(false);

            super.onPostExecute(result);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            getProDataFromWeb();
        }
    }

    /**
     * 刷新向平台获取请求
     */
    private void getDataFromWeb() {

        condMap.put("OBJ","PROJECT");
        condMap.put("TYPE",null);
        condMap.put("SIZE",20);
        condMap.put("USERID", userId);
        new ProjectDataSyschronized(ProjectListActivity.this).getDataFromWeb(condMap, handler);

    }

    public void getProDataFromWeb(){
        Map<String, Object> proCondMap = new HashMap<String, Object>();
        proCondMap.put("OBJ","PROJECT");
        proCondMap.put("TYPE",null);
        proCondMap.put("SIZE",20);
        proCondMap.put("USERID",userId);
        new ProjectDataSyschronized(ProjectListActivity.this).getProDataFromWeb(proCondMap,handler1);
    }

    private void showTaskProgressDialog(final List<TrTask> newtaskData) {
        //获取平台下发任务总数
        taskcount = newtaskData.size();
        progressDialog = new ProgressDialog(this);
        progressDialog.setIcon(R.drawable.green);
        //获取当前项目名称
        progressDialog.setCancelable(false);
        progressDialog.setMessage("正在下载变压器巡检任务"+"("+finish +"/"+taskcount+")");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.show();
        final Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                downTask(newtaskData);
            }
        });
        thread.start();
    }
    //任务下载
    private void downTask( final List<TrTask> newtaskData) {
        progressDialog.setMax(taskcount);//设置任务总数
        try {
            for (int i=0;i<taskcount;i++){
                //通过任务ID向终端发送请求
                condMap.put("OBJ","TASKALLINFO");
                condMap.put("TYPE",newtaskData.get(i).getDetectionttype());
                condMap.put("SIZE",5);
                condMap.put("USERID",userId);
                condMap.put("KEY", newtaskData.get(i).getTaskid());
                condMap.put("ISAB",false);
                new ProjectDataSyschronized(ProjectListActivity.this).getTaskDataFromWeb(condMap, taskHandler);
            }
            Thread.sleep(1000);
            Message msg = new Message();
            msg.what = 1;
            taskHandler.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
            Constants.recordExceptionInfo(e, context, context.toString());
        }
    }

    //消息进行处理
    final Handler taskHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1){
                //更新项目列表
                refshproject();
                mAdapter.notifyDataSetChanged();
                Toast.makeText(ProjectListActivity.this,"项目下载完成",Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                mSwipeRefreshLayout.setRefreshing(false);
                finish = 0;
            }else if (msg.what == 2){
                finish++;
                //已下载/任务总数
                progressDialog.setMessage("正在下载变压器巡检任务"+"("+finish +"/"+taskcount+")");
                //更新进度条
                progressDialog.setProgress(finish);
            }
        }
    };

    public void refshproject(){
        applicationList.clear();
        applicationList = queryProjectLists();
        mAdapter.clearApplications();
        mAdapter.addApplications(applicationList);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        new InitializeApplicationsTask().execute();
    }
}
