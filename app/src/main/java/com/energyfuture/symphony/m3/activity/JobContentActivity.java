package com.energyfuture.symphony.m3.activity;

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
import android.support.v7.widget.CardView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.energyfuture.symphony.m3.adapter.JobContentAdapter;
import com.energyfuture.symphony.m3.analysis.DataSyschronized;
import com.energyfuture.symphony.m3.dao.SympMessageRealDao;
import com.energyfuture.symphony.m3.dao.TrDetectionTypeTempletObjDao;
import com.energyfuture.symphony.m3.dao.TrProjectDao;
import com.energyfuture.symphony.m3.dao.TrTaskDao;
import com.energyfuture.symphony.m3.domain.TrDetectiontypEtempletObj;
import com.energyfuture.symphony.m3.domain.TrProject;
import com.energyfuture.symphony.m3.domain.TrTask;
import com.energyfuture.symphony.m3.ui.MaterialDialog;
import com.energyfuture.symphony.m3.util.Constants;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JobContentActivity extends ActionBarActivity {
    private Context context = JobContentActivity.this;
    private List<TrDetectiontypEtempletObj> objList=new ArrayList<TrDetectiontypEtempletObj>();
    private TrDetectionTypeTempletObjDao trDetectionTypeTempletObjDao;
    private LinearLayout toolbar_back,toolbar_comm;
    private ImageView toolbar_head_image,task_type;
    private TextView toolbar_news_show,task_name,substation_name,task_group,task_state,toolbar_name;
    private TrTask trTask;
    private FrameLayout frameLayout;
    private List<TrProject> list;
    private ListView job_list;
    private SwipeRefreshLayout refreshLayout;
    private JobContentAdapter jobContentAdapter;
    private ProgressBar progressBar_job;
    private List<TrDetectiontypEtempletObj> trDetectiontypEtempletObjList = new ArrayList<TrDetectiontypEtempletObj>();
    private DrawerLayout mDrawerLayout;
    private String useid;
    private TextView index_button;

    private Button finish_bt;  //现场作业完成
    private MaterialDialog dialog;
    private PopupWindow window;
    private RelativeLayout relativeLayout,toolbar_head;
    private LinearLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.job_content_lv);

        initView();
    }

    private void initView(){
        useid = Constants.getLoginPerson(this).get("userId");
        trTask = (TrTask)getIntent().getSerializableExtra("trTask");
        TrProject trProject=new TrProject();
        trProject.setProjectid(trTask.getProjectid());
        TrProjectDao trProjectDao=new TrProjectDao(context);
        list = trProjectDao.queryTrProjectsList(trProject);

        CardView include= (CardView) findViewById(R.id.job_row_name);

        finish_bt= (Button) findViewById(R.id.job_finish);

        mDrawerLayout= (DrawerLayout) findViewById(R.id.job_content_drawer);
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
        /*toolbar_news_show = (TextView)findViewById(R.id.toolbar_news_show);*/
        progressBar_job = (ProgressBar)findViewById(R.id.progressBar_job);
        job_list = (ListView) findViewById(R.id.job_list);
        toolbar_name = (TextView)findViewById(R.id.toolbar_name);
        index_button = (TextView)findViewById(R.id.index_button);
        toolbar_comm = (LinearLayout)findViewById(R.id.toolbar_comm);
        toolbar_name.setText("巡检作业项");
        index_button.setVisibility(View.GONE);
        toolbar_comm.setVisibility(View.VISIBLE);
        //禁用侧滑
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        toolbar_comm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,CommunionThemeListActivity.class);
                Bundle bundle=new Bundle();
                bundle.putSerializable("belongType",trTask);
                bundle.putSerializable("type","task");
                bundle.putSerializable("projectName",list.get(0).getProjectname());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        //从平台同步数据
        final Map<String, Object> condMap = Constants.getRequestParam(context,"INSPLIST",null,trTask.getTaskid());
        final Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what==1 && msg.obj != null){
                    new InitializeTask().execute();
                }
            }
        };
//        new  InitializeTask().execute();
        new DataSyschronized(context).getDataFromWeb(condMap, handler);

        if(objList.size()==0){
            progressBar_job.setVisibility(View.VISIBLE);
        }
        jobContentAdapter = new JobContentAdapter(context,objList,trTask);
        job_list.setAdapter(jobContentAdapter);

        //刷新
        refreshLayout= (SwipeRefreshLayout) findViewById(R.id.job_swipe);
//      refreshLayout.setColorSchemeColors(getResources().getColor(R.color.theme_accent));
        refreshLayout.setColorSchemeColors(Color.parseColor("#0288D1"));
        refreshLayout.setRefreshing(true);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new DataSyschronized(context).getDataFromWeb(condMap, handler);
//                new  InitializeTask().execute();
            }
        });

        //listview的item点击事件
        job_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TrDetectiontypEtempletObj obj  = (TrDetectiontypEtempletObj) jobContentAdapter.getItem(position);
                if(obj.getTemplettype().equals("safe")){
                    Intent intent = new Intent(context,ReceiveTaskActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("trTask",trTask);
                    bundle.putInt("flag",1);
                    intent.putExtras(bundle);
                    startActivity(intent);

                }else if(obj.getTemplettype().equals("tool")){
                    Intent intent = new Intent(context,TaskToolActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("trTask",trTask);
                    bundle.putInt("flag",1);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }else if(obj.getTemplettype().equals("work")){
                    Intent intent = new Intent(context,WorkRecordActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("trTask",trTask);
                    bundle.putInt("flag",1);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }else{
                    toOperationItemActivity(obj.getId(),obj.getRid(),obj.getDetectionname());
                }
            }
        });

        frameLayout= (FrameLayout) include.findViewById(R.id.receive_progressBar_FrameLayout);
        task_name.setText(trTask.getTaskname());
        substation_name.setText(Constants.getStationName(context,list));
        task_group.setText(Constants.getGroupName(context,list));
        Constants.setState(trTask.getTaskstate(),frameLayout,task_type,task_state);

        menuClick();

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
                    window=Constants.createPopupWindow(useid,JobContentActivity.this,layout);
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


        if(!trTask.getTaskstate().equals("1")&&!trTask.getTaskstate().equals("2")){
            finish_bt.setVisibility(View.GONE);
        }else{
            finish_bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    createDialog();
                }
            });
        }

    }


    /**
     * 跳转
     */
    public void toOperationItemActivity(String pid,String rid,String detectionname){
        Intent intent = new Intent(context,OperationItemActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("trTask",trTask);
        bundle.putString("rid",rid);
        bundle.putString("detectionname",detectionname);
        intent.putExtras(bundle);
        startActivity(intent);
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

    private List<TrDetectiontypEtempletObj> queryTrDetectiontypEtempletObj(){
        List<TrDetectiontypEtempletObj> trDetectiontypEtempletObjs = new ArrayList<TrDetectiontypEtempletObj>();
        TrDetectiontypEtempletObj trDetectiontypEtempletObj = new TrDetectiontypEtempletObj();
        trDetectiontypEtempletObj.setTaskid(trTask.getTaskid());
        trDetectiontypEtempletObj.setPid("0");
        trDetectionTypeTempletObjDao = new TrDetectionTypeTempletObjDao(context);
        trDetectiontypEtempletObjs = trDetectionTypeTempletObjDao.queryTrDetectionTypeTempletObjList(trDetectiontypEtempletObj);
        if(!trTask.getEquipmentid().equals("-1")){//如果不是站内巡检任务
            for(int i = 0;i < 3;i++){
                TrDetectiontypEtempletObj trDetectiontypEtempletObj1 = new TrDetectiontypEtempletObj();
                trDetectiontypEtempletObj1.setId(Constants.getUuid().toString());
                trDetectiontypEtempletObj1.setTaskid(trTask.getTaskid());
                if(i == 0){
                    trDetectiontypEtempletObj1.setDetectionname("现场作业情况登记");
                    trDetectiontypEtempletObj1.setTemplettype("work");
                }else if(i == 1){
                    trDetectiontypEtempletObj1.setDetectionname("工具选择");
                    trDetectiontypEtempletObj1.setTemplettype("tool");
                }else if(i == 2){
                    trDetectiontypEtempletObj1.setDetectionname("安全须知");
                    trDetectiontypEtempletObj1.setTemplettype("safe");
                }
                trDetectiontypEtempletObjs.add(0,trDetectiontypEtempletObj1);
            }
        }
        return trDetectiontypEtempletObjs;
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

    private class InitializeTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            objList.clear();

            List<TrDetectiontypEtempletObj> list = queryTrDetectiontypEtempletObj();
            for (TrDetectiontypEtempletObj obj: list) {
                objList.add(obj);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            jobContentAdapter.notifyDataSetChanged();
            refreshLayout.setRefreshing(false);
            progressBar_job.setVisibility(View.GONE);
        }
    }

    private void createDialog() {
        dialog = new MaterialDialog(context);
        dialog.setView(getView()).setCanceledOnTouchOutside(true).show();
    }
    private View getView(){
        View v=LayoutInflater.from(context).inflate(R.layout.work_finish_dialog,null);

        RelativeLayout rl1= (RelativeLayout) v.findViewById(R.id.work_finish_rl1);
        RelativeLayout rl2= (RelativeLayout) v.findViewById(R.id.work_finish_rl2);
        RelativeLayout rl3= (RelativeLayout) v.findViewById(R.id.work_finish_rl3);

        final CheckBox checkBox1= (CheckBox) v.findViewById(R.id.checkbox1);
        final CheckBox checkBox2= (CheckBox) v.findViewById(R.id.checkbox2);
        final CheckBox checkBox3= (CheckBox) v.findViewById(R.id.checkbox3);

        Button confirm= (Button) v.findViewById(R.id.confirm);
        Button cancel= (Button) v.findViewById(R.id.cancel);

        rl1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkBox1.isChecked()){
                    checkBox1.setChecked(false);
                }else{
                    checkBox1.setChecked(true);
                }
            }
        });
        rl2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkBox2.isChecked()){
                    checkBox2.setChecked(false);
                }else{
                    checkBox2.setChecked(true);
                }
            }
        });
        rl3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkBox3.isChecked()){
                    checkBox3.setChecked(false);
                }else{
                    checkBox3.setChecked(true);
                }
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkBox1.isChecked()&&checkBox2.isChecked()&&checkBox3.isChecked()){
                    updateTaskState();//修改任务状态
                    sendMessage();    //封装消息

                    //站内巡检
                    if(("-1").equals(trTask.getEquipmentid())){
                        task_state.setText("已完成");
                        trTask.setTaskstate("6");
                    }else{
                        task_state.setText("待补录");
                        trTask.setTaskstate("3");
                    }
                    dialog.dismiss();
                    finish_bt.setVisibility(View.GONE);
                    Toast.makeText(context,"任务已完成.",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(context,"请确认所有项已完成.",Toast.LENGTH_SHORT).show();
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        return v;
    }

    //    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        Intent intent = new Intent(context,TaskListActivity.class);
//        startActivity(intent);
//    }
    private void updateTaskState(){
        TrTaskDao trTaskDao=new TrTaskDao(context);
        Map<Object,Object> columnsMap=new HashMap<Object, Object>();
        Map<Object,Object> wheresMap=new HashMap<Object, Object>();

        if(("-1").equals(trTask.getEquipmentid())){
            columnsMap.put("TASKSTATE",6);
        }else{
            columnsMap.put("TASKSTATE",3);
        }
        wheresMap.put("TASKID",trTask.getTaskid());

        trTaskDao.updateTrTaskInfo(columnsMap,wheresMap);
    }
    private void sendMessage() {
        SympMessageRealDao sympMessageRealDao = new SympMessageRealDao(context);
        TrTask task=new TrTask();
        if(("-1").equals(trTask.getEquipmentid())){
            task.setTaskstate(""+6);
        }else{
            task.setTaskstate(""+3);
        }
        task.setFinishtime(Constants.dateformat2.format(new Date()));
        task.setTaskid(trTask.getTaskid());

        List<List<Object>> list1 = new ArrayList<List<Object>>();
        List<Object> list2 = new ArrayList<Object>();
        list2.add(task);
        list1.add(list2);
        sympMessageRealDao.updateTextMessages(list1);
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        new InitializeTask().execute();
    }
}

