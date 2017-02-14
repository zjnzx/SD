package com.energyfuture.symphony.m3.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.energyfuture.symphony.m3.adapter.TaskListAdapter;
import com.energyfuture.symphony.m3.analysis.DataSyschronized;
import com.energyfuture.symphony.m3.common.ApiClient;
import com.energyfuture.symphony.m3.common.URLs;
import com.energyfuture.symphony.m3.dao.SympMessageRealDao;
import com.energyfuture.symphony.m3.dao.TrBureauDao;
import com.energyfuture.symphony.m3.dao.TrConditionInfoDao;
import com.energyfuture.symphony.m3.dao.TrDepartmentDao;
import com.energyfuture.symphony.m3.dao.TrEquipmentToolsDao;
import com.energyfuture.symphony.m3.dao.TrProjectDao;
import com.energyfuture.symphony.m3.dao.TrStationDao;
import com.energyfuture.symphony.m3.dao.TrTaskDao;
import com.energyfuture.symphony.m3.domain.Department;
import com.energyfuture.symphony.m3.domain.TrBureau;
import com.energyfuture.symphony.m3.domain.TrConditionInfo;
import com.energyfuture.symphony.m3.domain.TrEquipmentTools;
import com.energyfuture.symphony.m3.domain.TrProject;
import com.energyfuture.symphony.m3.domain.TrStation;
import com.energyfuture.symphony.m3.domain.TrTask;
import com.energyfuture.symphony.m3.floatingbutton.FloatingActionButton;
import com.energyfuture.symphony.m3.ui.RoundImageView;
import com.energyfuture.symphony.m3.util.Constants;
import com.energyfuture.symphony.m3.util.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskListActivity extends ActionBarActivity implements View.OnClickListener{
    private Context context = TaskListActivity.this;
    private ImageView roundImageView; //标题栏图片
    //任务信息
    private CardView include;
    private TextView name,date,group,state,task_type,toolbar_name,id_bureau,id_voltage;
    private RoundImageView project_people;
    private LinearLayout toolbar_back,toolbar_comm,add_task;
    private FrameLayout progressBar_FrameLayout;
    private ProgressBar task_progressBar;
    private TextView task_progressbar_text;
    private ProgressBar progressBar;
    private TaskListAdapter mAdapter;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout refreshLayout;
    private String userId;
    private TextView none_data,toolbar_news_show;
    private FloatingActionButton addTaskBtn;
    private SympMessageRealDao sympMessageRealDao = new SympMessageRealDao(context);

    private List<TrTask> taskList=new ArrayList<>();
    private TrTask trTask=new TrTask();

    private String projectid;  //项目id
    private TrProject project;
    private PopupWindow window;
    private RelativeLayout relativeLayout,toolbar_head;
    private LinearLayout layout;
    private TrTaskDao trTaskDao;
    private TrStationDao trStationDao = new TrStationDao(context);
    private TrBureauDao trBureauDao = new TrBureauDao(context);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_list);

        userId = Constants.getLoginPerson(this).get("userId");

        initView();
        initDate();

    }
    private void initView() {
        project= (TrProject) getIntent().getSerializableExtra("project");
        projectid=project.getProjectid();

        trTaskDao=new TrTaskDao(TaskListActivity.this);

        addTaskBtn = (FloatingActionButton) findViewById(R.id.add_task);
        relativeLayout= (RelativeLayout) findViewById(R.id.relativeLayout);
        toolbar_head= (RelativeLayout) findViewById(R.id.toolbar_head);
        layout= (LinearLayout) findViewById(R.id.background);
        roundImageView= (RoundImageView) findViewById(R.id.toolbar_head_image);
        progressBar= (ProgressBar) findViewById(R.id.progressBar_task);
        include= (CardView) findViewById(R.id.row_name);
        name= (TextView)include.findViewById(R.id.task_name);
        id_bureau= (TextView)include.findViewById(R.id.id_bureau);
        id_voltage= (TextView)include.findViewById(R.id.id_voltage);
        date= (TextView)include.findViewById(R.id.date);
        group= (TextView)include.findViewById(R.id.group);
        state= (TextView)include.findViewById(R.id.task_state);
        project_people= (RoundImageView)include.findViewById(R.id.project_people);
        task_type= (TextView) include.findViewById(R.id.task_type);
        toolbar_back = (LinearLayout)findViewById(R.id.toolbar_back);
        toolbar_comm = (LinearLayout)findViewById(R.id.toolbar_comm);
        add_task = (LinearLayout)findViewById(R.id.menu_add_task);
        /*toolbar_news_show = (TextView)findViewById(R.id.toolbar_news_show);*/
        progressBar_FrameLayout= (FrameLayout) include.findViewById(R.id.progressBar_FrameLayout);
        task_progressBar = (ProgressBar) include.findViewById(R.id.task_progressBar);
        task_progressbar_text = (TextView) include.findViewById(R.id.task_progressbar_text);
        toolbar_name = (TextView)findViewById(R.id.toolbar_name);
        toolbar_name.setText("巡检任务");
        toolbar_comm.setVisibility(View.VISIBLE);
        add_task.setVisibility(View.VISIBLE);

        toolbar_head.setOnClickListener(this);
        toolbar_comm.setOnClickListener(this);
        add_task.setOnClickListener(this);

        mAdapter=new TaskListAdapter(new ArrayList<TrTask>(),R.layout.task_list_item,this);
        recyclerView= (RecyclerView) findViewById(R.id.task_recycle);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter);
        //添加滚动事件
//        addTaskBtn.attachToRecyclerView(recyclerView);

        String trTaskIdStr = trTaskDao.queryTrTaskIdList(project);

        //从平台同步数据
        final Map<String, Object> condMap = new HashMap<String, Object>();
        condMap.put("OBJ","TASK");
        condMap.put("TYPE",null);
        condMap.put("SIZE",5);
        condMap.put("USERID",userId);
        condMap.put("KEY",projectid);
        condMap.put("DATA",trTaskIdStr);
        final Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what==1 && msg.obj != null){
                    new InitializeTask().execute();
                }
            }
        };
        new DataSyschronized(context).getDataFromWeb(condMap, handler);

        refreshLayout= (SwipeRefreshLayout) findViewById(R.id.task_swipe);
        refreshLayout.setColorSchemeColors(Color.parseColor("#0288D1"));
        refreshLayout.setRefreshing(true);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                String trTaskIdStr = trTaskDao.queryTrTaskIdList(project);
                condMap.put("DATA",trTaskIdStr);
                new DataSyschronized(context).getDataFromWeb(condMap, handler);
            }
        });
        menuClick();
        /*toolbar_news_show.bringToFront();*/

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
                finish();
                return;
            }
        });
    }

    private void initDate() {

        //设置标题栏图片

        if(!Constants.getUsericon(context).equals("")){
            String imagepath=Constants.getUsericon(context);
            roundImageView.setImageURI(Uri.parse(imagepath));
        }else{
            int userhead= Constants.userHead(userId);
            roundImageView.setImageResource(userhead);
        }

        //设置任务信息
        name.setText(project.getProjectname());
        date.setText(project.getStarttime()+"/"+project.getEndtime());
        group.setText(getGroupName(project.getDepartment()));
        state.setText(getState(project.getProjectid()));

        String workpersons = project.getWorkperson();
        if(workpersons != null && !workpersons.equals("")){
            workpersons = workpersons.substring(0,workpersons.indexOf(";"));
        }

        if(!Constants.getWorkicon(context,workpersons).equals("")){
            String imagepath=Constants.getWorkicon(context,workpersons);
            project_people.setImageURI(Uri.parse(imagepath));
        }else{
            int userhead= Constants.userHead(workpersons);
            project_people.setImageResource(userhead);
        }

        //project_people.setImageResource(getWorkpersonImage(project.getWorkperson()));

        TrStation trStation = new TrStation();
        trStation.setStationid(project.getStationid());
        List<TrStation> trStations = trStationDao.queryTrStationList(trStation);
        if(trStations.size() > 0){
            TrBureau trBureau = new TrBureau();
            trBureau.setBureauid(trStations.get(0).getBureauid());
            List<TrBureau> trBureaus = trBureauDao.queryTrBureauList(trBureau);
            id_voltage.setText(trStations.get(0).getVoltage() + "kV");
            if(trBureaus.size() > 0){
                id_bureau.setText(trBureaus.get(0).getBureauname());
            }
        }

    }


    private String getGroupName(String departmentid) {
        Department department=new Department();
        department.setBmid(departmentid);
        List<Department> date = new TrDepartmentDao(this).queryDepartmentList(department);
        String groupName = "";
        if(date != null && date.size() > 0){
            groupName = date.get(0).getBmmc();
        }
        return groupName;
    }

    private String getState(String projectid) {
        String state = "1";
        TrProjectDao trProjectDao = new TrProjectDao(context);
        TrProject trProject = new TrProject();
        trProject.setProjectid(projectid);
        List<TrProject> trProjects = trProjectDao.queryTrProjectsList(trProject);
        if(trProjects.size() > 0 && !trProjects.get(0).getProjectstate().equals("")){
            state = trProjects.get(0).getProjectstate();
        }
        if(state.equals("1")){
            progressBar_FrameLayout.setVisibility(View.GONE);
            task_type.setBackgroundResource(R.drawable.orange);
            return "未开始";
        }
        if(state.equals("3")){
            TrTaskDao taskDao=new TrTaskDao(context);
            int res= taskDao.taskStepCount(projectid);
            task_type.setBackgroundResource(R.drawable.green);
            task_progressBar.setProgress(res);
            task_progressbar_text.setVisibility(View.VISIBLE);
            task_progressbar_text.setText(res+"%");
            return "进行中";
        }
        if(state.equals("4")){
            progressBar_FrameLayout.setVisibility(View.GONE);
            task_type.setBackgroundResource(R.drawable.blue);
            return "已完成";
        }
        return "";
    }

    private int getWorkpersonImage(String workpersons) {
        int image=R.drawable.zhaogr;
        String firstworkperson="";
        if(!workpersons.equals("") && workpersons != null){
            firstworkperson = workpersons.substring(0,workpersons.indexOf(";"));
        }
        if (firstworkperson!=null){
            image= Constants.userHead(firstworkperson);
        }
        return image;
    }
    public void animateActivity(TrTask trTask) {
        if(trTask.getTaskstate().equals("1")){
            Intent i;
            if(trTask.getEquipmentid().equals("-1")){ //站内巡检
                i = new Intent(this, JobContentActivity.class);
            }else{//普通任务
                i = new Intent(this, ReceiveTaskActivity.class);
            }
            Bundle bundle = new Bundle();
            bundle.putSerializable("trTask",trTask);
            i.putExtras(bundle);
            startActivity(i);
        }else{
            if(trTask.getEquipmentid().equals("-1")){ //站内巡检
                Intent i = new Intent(this, JobContentActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("trTask",trTask);
                i.putExtras(bundle);
                startActivity(i);
            }else{
                TrEquipmentTools trEquipmentTools = new TrEquipmentTools();
                trEquipmentTools.setTaskid(trTask.getTaskid());
                TrEquipmentToolsDao trEquipmentToolsDao = new TrEquipmentToolsDao(context);
                TrConditionInfo trConditionInfo = new TrConditionInfo();
                trConditionInfo.setTaskid(trTask.getTaskid());
                TrConditionInfoDao trConditionInfoDao = new TrConditionInfoDao(context);
                if(trTask.getTaskstate().equals("2")){
                    String count = trEquipmentToolsDao.queryToolCount(trEquipmentTools);
                    List<TrConditionInfo> list = trConditionInfoDao.queryTrConditionInfoList(trConditionInfo);
                    if(count!=null && !count.equals("0")){//如果工具数量为0
                        Intent i;
                        if(list != null && list.size() > 0){//如果环境表不为空
                            i = new Intent(this, JobContentActivity.class);
                        }else{
                            i = new Intent(this, WorkRecordActivity.class);
                        }
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("trTask",trTask);
                        i.putExtras(bundle);
                        startActivity(i);

                    }else{
                        Intent i = new Intent(this, TaskToolActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("trTask",trTask);
                        i.putExtras(bundle);
                        startActivity(i);
                    }
                }else{
                    Intent i = new Intent(this, JobContentActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("trTask",trTask);
                    i.putExtras(bundle);
                    startActivity(i);
                }
            }
        }
        //清空persionMap
        if(Utils.personMap.size() > 0){
            Utils.personMap.clear();
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 1){
                int position = (int)msg.obj;
                TrTaskDao trTaskDao = new TrTaskDao(context);
                List<TrTask> trTasks = new ArrayList<TrTask>();
                trTasks.add(trTask);
                trTaskDao.deleteListData(trTasks);
                mAdapter.taskList.remove(position);
                mAdapter.notifyDataSetChanged();
                Toast.makeText(context, "删除任务成功！", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(context, "删除任务失败！", Toast.LENGTH_SHORT).show();
            }
        }
    };

    public void deleteTask(TrTask trTask, final int position){
        Toast.makeText(context, "正在删除任务...", Toast.LENGTH_SHORT).show();
        final Map<String,Object> dataMap = new HashMap<String,Object>();
        dataMap.put("id",trTask.getTaskid());
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                String http = URLs.HTTP+URLs.HOST+"/"+ "inspectfuture/" +URLs.DELETETASK;
                Map resultMap = ApiClient.sendHttpPostMessageData(null, http, dataMap, "", context, "文本");
                if(resultMap != null && resultMap.get("ok").equals("true")){
                    Message msg = new Message();
                    msg.obj = position;
                    msg.what = 1;
                    handler.sendMessage(msg);
                }else{
                    Message msg = new Message();
                    msg.what = 2;
                    handler.sendMessage(msg);
                }
            }
        };
        new Thread(runnable).start();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            //用户头像
            case R.id.toolbar_head:
                if(window==null){
                    window=Constants.createPopupWindow(userId,TaskListActivity.this,layout);
                }
                if(!window.isShowing()){
                    window.showAtLocation(relativeLayout,Gravity.TOP|Gravity.RIGHT,20,140);
                    if(layout.getVisibility()==View.GONE){
                        layout.setVisibility(View.VISIBLE);
                    }
                }
                break;
            //沟通交流
            case R.id.toolbar_comm:
                Intent intent = new Intent(context,CommunionThemeListActivity.class);
                Bundle bundle2=new Bundle();
                bundle2.putSerializable("belongType",project);
                bundle2.putSerializable("type","project");
                intent.putExtras(bundle2);
                startActivity(intent);
                break;
            //新增任务
            case R.id.menu_add_task:
                Intent intent2 = new Intent(context,TaskAddActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("trProject",project);
                intent2.putExtras(bundle);
                startActivity(intent2);
                //清空集合
                Utils.equipmentMap.clear();
                break;
        }
    }

    private class InitializeTask extends AsyncTask<Void, Void, Void>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mAdapter.clearTask();
            mAdapter.notifyDataSetChanged();
        }

        @Override
        protected Void doInBackground(Void... params) {
            taskList.clear();
            trTask.setProjectid(projectid);

            final Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
            mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);

            List<TrTask> datelist = trTaskDao.queryTrTaskList(trTask);
            for (TrTask task: datelist) {
                taskList.add(task);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mAdapter.addTask(taskList);
            refreshLayout.setRefreshing(false);
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        new InitializeTask().execute();
        initDate();
    }

}
