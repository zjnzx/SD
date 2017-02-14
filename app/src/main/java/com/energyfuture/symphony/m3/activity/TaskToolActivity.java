package com.energyfuture.symphony.m3.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.CardView;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.energyfuture.symphony.m3.adapter.TaskToolAdapter;
import com.energyfuture.symphony.m3.analysis.DataSyschronized;
import com.energyfuture.symphony.m3.dao.SympMessageRealDao;
import com.energyfuture.symphony.m3.dao.TrDetectionToolsDao;
import com.energyfuture.symphony.m3.dao.TrEquipmentToolsDao;
import com.energyfuture.symphony.m3.dao.TrProjectDao;
import com.energyfuture.symphony.m3.domain.TrDetectionTools;
import com.energyfuture.symphony.m3.domain.TrEquipmentTools;
import com.energyfuture.symphony.m3.domain.TrProject;
import com.energyfuture.symphony.m3.domain.TrTask;
import com.energyfuture.symphony.m3.util.Constants;
import com.energyfuture.symphony.m3.util.Utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskToolActivity extends ActionBarActivity {
    private Context context = TaskToolActivity.this;
    //信息栏
    private TextView task_name,substation_name,task_group,task_state,toolbar_news_show,submit,toolbar_name;
    private LinearLayout toolbar_back;
    private ImageView toolbar_head_image,task_type;
    private TextView index_button;
    private TrTask trTask;
    private int flag; //用来标记从从哪个activity跳转的
    private FrameLayout frameLayout;
    private SwipeRefreshLayout tool_swipe;
    private List<TrProject> list;  //根据项目ID查出来的项目列表
    private PopupWindow window;
    private RelativeLayout relativeLayout,toolbar_head;
    private LinearLayout layout;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_tool);

        Utils.countMap.clear();

        initTaskInfo();
    }

    /**
     * 初始化任务信息
     */
    private void initTaskInfo() {
        trTask= (TrTask)getIntent().getSerializableExtra("trTask");
        flag=getIntent().getIntExtra("flag",-1);

        TrProject trProject=new TrProject();
        trProject.setProjectid(trTask.getProjectid());
        TrProjectDao trProjectDao=new TrProjectDao(context);
        list = trProjectDao.queryTrProjectsList(trProject);

        CardView include= (CardView) findViewById(R.id.receive_row_name);

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
        progressBar= (ProgressBar) findViewById(R.id.progressBar_tool);
       /* toolbar_news_show = (TextView)findViewById(R.id.toolbar_news_show);*/
        submit = (TextView)findViewById(R.id.submit);
        index_button = (TextView)findViewById(R.id.index_button);
        index_button.setVisibility(View.GONE);
        toolbar_name = (TextView)findViewById(R.id.toolbar_name);
        toolbar_name.setText("选择工具");

        if(!trTask.getTaskstate().equals("1")&!trTask.getTaskstate().equals("2")){
            submit.setVisibility(View.GONE);
        }else{
            //工具确认
            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    insertTrEquipmentTools();
                }
            });
        }
        frameLayout= (FrameLayout) include.findViewById(R.id.receive_progressBar_FrameLayout);

        task_name.setText(trTask.getTaskname());
        substation_name.setText(Constants.getStationName(context, list));
        task_group.setText(Constants.getGroupName(context,list));
        Constants.setState(trTask.getTaskstate(),frameLayout,task_type,task_state);
        menuClick();
        final String useid = Constants.getLoginPerson(this).get("userId");
        if(!Constants.getUsericon(context).equals("")){
            String imagepath=Constants.getUsericon(context);
            toolbar_head_image.setImageURI(Uri.parse(imagepath));
        }else{
            int userhead= Constants.userHead(useid);
            toolbar_head_image.setImageResource(userhead);
        }
        /*toolbar_news_show.bringToFront();*/

        final Map<String, Object> condMap = Constants.getRequestParam(context,"TOOL",null,trTask.getTaskid());
        final Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what==1 && msg.obj != null){
                    initListView();
                    tool_swipe.setRefreshing(false);
                }
            }
        };
        new DataSyschronized(context).getDataFromWeb(condMap, handler);

        tool_swipe = (SwipeRefreshLayout)findViewById(R.id.tool_swipe);
        tool_swipe.setColorSchemeColors(getResources().getColor(R.color.theme_accent));
        tool_swipe.setRefreshing(true);
        //下拉刷新
        tool_swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new DataSyschronized(context).getDataFromWeb(condMap, handler);
            }
        });

        toolbar_head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(window==null){
                    window=Constants.createPopupWindow(useid,TaskToolActivity.this,layout);
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
                finish();
                return;
            }
        });
    }

    /**
     * 封装消息
     * @param
     */
    public void addMessageRealData(String userId,String date,String count,String id){
        SympMessageRealDao sympMessageRealDao = new SympMessageRealDao(this);
        TrEquipmentTools trEquipmentTools = new TrEquipmentTools();
        trEquipmentTools.setAmount(count);
        trEquipmentTools.setPersion(userId);
        trEquipmentTools.setUpdatetime(date);
        trEquipmentTools.setTaskid(trTask.getTaskid());
        trEquipmentTools.setId(id);
        List<List<Object>> list1 = new ArrayList<List<Object>>();
        List<Object> list2 = new ArrayList<Object>();
        list2.add(trEquipmentTools);
        list1.add(list2);
        sympMessageRealDao.updateTextMessages(list1);
    }

    /**
     * 插入工具表数据
     */
    private void insertTrEquipmentTools(){
        String userId = Constants.getLoginPerson(context).get("userId");
        String date = Constants.dateformat2.format(new Date());
        TrEquipmentToolsDao trEquipmentToolsDao = new TrEquipmentToolsDao(context);
        if(Utils.countMap.size() > 0){
            for(Map.Entry<String, String> entry: Utils.countMap.entrySet()){
                TrEquipmentTools trEquipmentTools = new TrEquipmentTools(); //查询时的实体
                trEquipmentTools.setTaskid(trTask.getTaskid());
                trEquipmentTools.setToolid(entry.getKey());
                List<TrEquipmentTools> tools = trEquipmentToolsDao.queryTrEquipmentToolsList(trEquipmentTools);
                String id = "";
                if(tools != null && tools.size() > 0){
                    id = tools.get(0).getId();
                }

                Map<Object,Object> columnsMap = new HashMap<Object, Object>();
                Map<Object,Object> wheresMap = new HashMap<Object,Object>();
                columnsMap.put("AMOUNT",entry.getValue());
                columnsMap.put("PERSION",userId);
                columnsMap.put("UPDATETIME",date);
                wheresMap.put("ID",id);
                trEquipmentToolsDao.updateTrEquipmentToolsInfo(columnsMap,wheresMap);

                addMessageRealData(userId,date,entry.getValue(),id);
            }
            if(flag!=-1){
                finish();
            }else{
                Intent intent = new Intent(context, WorkRecordActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("trTask",trTask);
                intent.putExtras(bundle);
                startActivity(intent);
                Utils.personMap.clear();
                finish();
            }
        }else {
            if(flag!=-1){
                finish();
            }else{
                Toast.makeText(TaskToolActivity.this,"请选择工具",Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * 初始化listview,绑定适配器
     */
    private void initListView(){
        ListView instrument_listview = (ListView) findViewById(R.id.instrument_listview);
        ListView material_listview = (ListView) findViewById(R.id.material_listview);
        ListView tools_listview = (ListView) findViewById(R.id.tools_listview);

        List<TrDetectionTools> instrumentList = queryTrDetectionTools("instrument");
        List<TrDetectionTools> materialList = queryTrDetectionTools("material");
        List<TrDetectionTools> toolList = queryTrDetectionTools("tool");

        //仪器
        TaskToolAdapter instrumentAdapter = new TaskToolAdapter(context,instrumentList,trTask);
        instrument_listview.setAdapter(instrumentAdapter);
        if(flag==1){
            instrumentAdapter.setTaskid(trTask.getTaskid());
        }
        //材料
        TaskToolAdapter materialAdapter = new TaskToolAdapter(context,materialList,trTask);
        material_listview.setAdapter(materialAdapter);
        if(flag==1){
            materialAdapter.setTaskid(trTask.getTaskid());
        }

        //工具
        TaskToolAdapter toolAdapter = new TaskToolAdapter(context,toolList,trTask);
        tools_listview.setAdapter(toolAdapter);
        if(flag==1){
            toolAdapter.setTaskid(trTask.getTaskid());
        }
        progressBar.setVisibility(View.GONE);
    }

    /**
     * 根据类型查询工具表
     * @param toolType
     * @return
     */
    private List<TrDetectionTools> queryTrDetectionTools(String toolType){
        TrDetectionToolsDao trDetectionToolsDao = new TrDetectionToolsDao(context);
        TrDetectionTools trDetectionTools = new TrDetectionTools();
        trDetectionTools.setToolstype(toolType);
        List<TrDetectionTools> list = trDetectionToolsDao.queryTrDetectionToolsList(trDetectionTools);
        return list;
    }
}
